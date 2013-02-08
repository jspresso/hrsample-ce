/*
 * Copyright (c) 2005-2013 Vincent Vandenschrick. All rights reserved.
 *
 *  This file is part of the Jspresso framework.
 *
 *  Jspresso is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Jspresso is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with Jspresso.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jspresso.hrsample.backend;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hamcrest.Description;
import org.hibernate.SQLQuery;
import org.jspresso.framework.application.backend.entity.ControllerAwareEntityInvocationHandler;
import org.jspresso.framework.application.backend.persistence.hibernate.HibernateBackendController;
import org.jspresso.framework.application.backend.session.EMergeMode;
import org.jspresso.framework.model.component.ComponentException;
import org.jspresso.framework.model.persistence.hibernate.criterion.EnhancedDetachedCriteria;
import org.jspresso.framework.util.uid.ByteArray;
import org.jspresso.hrsample.model.City;
import org.jspresso.hrsample.model.Company;
import org.jspresso.hrsample.model.Department;
import org.jspresso.hrsample.model.Employee;
import org.jspresso.hrsample.model.Event;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

/**
 * Model integration tests.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class JspressoModelTests extends BackTestStartup {

  /**
   * Tests computed fire property change. See bug 708.
   * 
   * @throws Throwable
   *           whenever an unexpected error occurs.
   */
  @Test
  public void testComputedFirePropertyChange() throws Throwable {
    final HibernateBackendController hbc = (HibernateBackendController) getBackendController();
    boolean wasThrowExceptionOnBadUsage = hbc.isThrowExceptionOnBadUsage();
    try {
      hbc.setThrowExceptionOnBadUsage(false);
      EnhancedDetachedCriteria employeeCriteria = EnhancedDetachedCriteria
          .forClass(Employee.class);
      Employee employee = hbc.findFirstByCriteria(employeeCriteria,
          EMergeMode.MERGE_KEEP, Employee.class);
      ControllerAwareEntityInvocationHandler handlerSpy = (ControllerAwareEntityInvocationHandler) spy(Proxy
          .getInvocationHandler(employee));
      Employee employeeMock = (Employee) Proxy.newProxyInstance(getClass()
          .getClassLoader(), new Class<?>[] {
        Employee.class
      }, handlerSpy);

      Method firePropertyChangeMethod = Employee.class.getMethod(
          "firePropertyChange", new Class<?>[] {
              String.class, Object.class, Object.class
          });

      employeeMock.setBirthDate(new Date(0));
      verify(handlerSpy, never()).invoke(eq(employeeMock),
          eq(firePropertyChangeMethod),
          argThat(new PropertyMatcher(Employee.AGE)));

      PropertyChangeListener fakeListener = new PropertyChangeListener() {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
          // NO-OP
        }
      };

      employeeMock.addPropertyChangeListener(fakeListener);
      employeeMock.setBirthDate(new Date(1));
      verify(handlerSpy, times(1)).invoke(eq(employeeMock),
          eq(firePropertyChangeMethod),
          argThat(new PropertyMatcher(Employee.AGE)));

      employeeMock.removePropertyChangeListener(fakeListener);
      employeeMock.setBirthDate(new Date(2));
      verify(handlerSpy, times(1)).invoke(eq(employeeMock),
          eq(firePropertyChangeMethod),
          argThat(new PropertyMatcher(Employee.AGE)));

      employeeMock.addPropertyChangeListener(Employee.AGE, fakeListener);
      employeeMock.setBirthDate(new Date(3));
      verify(handlerSpy, times(2)).invoke(eq(employeeMock),
          eq(firePropertyChangeMethod),
          argThat(new PropertyMatcher(Employee.AGE)));
    } finally {
      hbc.setThrowExceptionOnBadUsage(wasThrowExceptionOnBadUsage);
    }
  }

  static class PropertyMatcher extends ArgumentMatcher<Object[]> {

    private String propertyName;

    /**
     * Constructs a new <code>PropertyMatcher</code> instance.
     * 
     * @param propertyName
     */
    public PropertyMatcher(String propertyName) {
      this.propertyName = propertyName;
    }

    @Override
    public boolean matches(Object param) {
      Object[] args = (Object[]) param;
      return args != null && args.length > 0 && propertyName.equals(args[0]);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void describeTo(Description description) {
      super.describeTo(description);
      description.appendText("[" + propertyName + "]");
    }
  }

  /**
   * Tests adding twice the same entity to a list property. See bug 758.
   */
  @Test(expected = ComponentException.class)
  public void testAddToListTwice() {
    final HibernateBackendController hbc = (HibernateBackendController) getBackendController();

    EnhancedDetachedCriteria employeeCriteria = EnhancedDetachedCriteria
        .forClass(Employee.class);
    final Employee emp = hbc.findFirstByCriteria(employeeCriteria,
        EMergeMode.MERGE_KEEP, Employee.class);

    Event evt = emp.getEvents().get(0);
    emp.addToEvents(evt);
    assertSame(emp.getEvents().get(0), evt);
    assertSame(emp.getEvents().get(emp.getEvents().size() - 1), evt);

    hbc.getTransactionTemplate().execute(
        new TransactionCallbackWithoutResult() {

          @Override
          protected void doInTransactionWithoutResult(TransactionStatus status) {
            hbc.cloneInUnitOfWork(emp);
          }
        });
    hbc.reload(emp);
    assertSame(emp.getEvents().get(0), evt);
    assertSame(emp.getEvents().get(emp.getEvents().size() - 1), evt);
  }

  /**
   * Tests 1st level cache.
   */
  @Test
  public void testCache() {
    final HibernateBackendController hbc = (HibernateBackendController) getBackendController();
    EnhancedDetachedCriteria crit = EnhancedDetachedCriteria
        .forClass(City.class);
    List<City> cities = hbc.findByCriteria(crit, EMergeMode.MERGE_KEEP,
        City.class);
    City firstCity = hbc.findById(cities.get(0).getId(), EMergeMode.MERGE_KEEP,
        City.class);
    assertSame(cities.get(0), firstCity);
  }

  /**
   * Tests EAGER uninitialized merge on null value.
   */
  @Test
  public void testUninitializedMerge() {
    final HibernateBackendController hbc = (HibernateBackendController) getBackendController();
    EnhancedDetachedCriteria crit = EnhancedDetachedCriteria
        .forClass(Department.class);
    final Department d1 = hbc.findFirstByCriteria(crit, EMergeMode.MERGE_LAZY,
        Department.class);
    d1.straightSetProperty("company", null);

    Department d2 = hbc.getTransactionTemplate().execute(
        new TransactionCallback<Department>() {

          @Override
          public Department doInTransaction(TransactionStatus status) {
            Department d = (Department) hbc.getHibernateSession().get(
                Department.class, d1.getId());
            status.setRollbackOnly();
            return d;
          }
        });
    d2 = hbc.merge(d2, EMergeMode.MERGE_EAGER);
    assertSame(d1, d2);
  }

  /**
   * Component backref initialization.
   */
  @Test
  public void testComponentBackRef() {
    final HibernateBackendController hbc = (HibernateBackendController) getBackendController();
    Company c = hbc.getEntityFactory().createEntityInstance(Company.class);
    assertSame(c, c.getContact().getOwningComponent());

    EnhancedDetachedCriteria crit = EnhancedDetachedCriteria
        .forClass(Department.class);
    final Department d = hbc.findFirstByCriteria(crit, EMergeMode.MERGE_LAZY,
        Department.class);
    assertSame(d, d.getContact().getOwningComponent());

    hbc.getTransactionTemplate().execute(
        new TransactionCallbackWithoutResult() {

          @Override
          protected void doInTransactionWithoutResult(TransactionStatus status) {
            Department dClone = hbc.cloneInUnitOfWork(d);
            assertSame(dClone, dClone.getContact().getOwningComponent());
            status.setRollbackOnly();
          }
        });
  }

  /**
   * Creates a SQL select and use ID.
   */
  @Test
  public void testSelectById() {
    final HibernateBackendController hbc = (HibernateBackendController) getBackendController();

    EnhancedDetachedCriteria crit = EnhancedDetachedCriteria
        .forClass(Department.class);
    final Department d = hbc.findFirstByCriteria(crit, EMergeMode.MERGE_LAZY,
        Department.class);

    SQLQuery query = hbc.getHibernateSession().createSQLQuery(
        "UPDATE ORGANIZATIONAL_UNIT SET NAME = 'Test' WHERE ID = :ID_PARAM");
    Object depId = d.getId();
    if (depId instanceof ByteArray) {
      depId = ((ByteArray) depId).getBytes();
    }
    query.setParameter("ID_PARAM", depId);
    assertEquals(1, query.executeUpdate());
  }

  /**
   * Tests the performance of adder when the collection is big.
   */
  @Test(timeout = 15000)
  public void testCollectionSetterPerf() {
    final HibernateBackendController hbc = (HibernateBackendController) getBackendController();

    Company company = hbc.getEntityFactory()
        .createEntityInstance(Company.class);
    // for (int i = 0; i < 5000; i++) {
    // Employee emp = hbc.getEntityFactory()
    // .createEntityInstance(Employee.class);
    // // The employee collection should be sorted by name
    // emp.setName(Integer.toHexString(emp.hashCode()));
    // company.addToEmployees(emp);
    // }
    Set<Employee> employees = new HashSet<Employee>();
    for (int i = 0; i < 5000; i++) {
      Employee emp = hbc.getEntityFactory()
          .createEntityInstance(Employee.class);
      // The employee collection should be sorted by name
      emp.setName(Integer.toHexString(emp.hashCode()));
      employees.add(emp);
    }
    company.setEmployees(employees);
  }

  /**
   * Tests fix for bug #787.
   */
  @Test
  public void testBigDecimalScale() {
    final HibernateBackendController hbc = (HibernateBackendController) getBackendController();

    Employee e1 = hbc.getEntityFactory().createEntityInstance(Employee.class);
    Employee e2 = hbc.getEntityFactory().createEntityInstance(Employee.class);

    e1.setSalary(new BigDecimal("4"));
    e2.setSalary(new BigDecimal("4.00"));
    assertEquals(e1.getSalary(), e2.getSalary());

    e1.setSalary(new BigDecimal("4.12352"));
    e2.setSalary(new BigDecimal("4.12437"));
    assertEquals(e1.getSalary(), e2.getSalary());
  }
}
