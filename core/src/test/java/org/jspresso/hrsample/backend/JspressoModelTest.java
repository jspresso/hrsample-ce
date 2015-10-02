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

import static org.junit.Assert.*;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hamcrest.Description;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.collection.internal.PersistentSet;
import org.hibernate.collection.spi.PersistentCollection;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.jspresso.framework.application.backend.entity.ControllerAwareEntityInvocationHandler;
import org.jspresso.framework.application.backend.persistence.hibernate.HibernateBackendController;
import org.jspresso.framework.application.backend.session.EMergeMode;
import org.jspresso.framework.model.component.ComponentException;
import org.jspresso.framework.model.descriptor.MandatoryPropertyException;
import org.jspresso.framework.model.entity.IEntity;
import org.jspresso.framework.model.persistence.hibernate.criterion.EnhancedDetachedCriteria;
import org.jspresso.framework.util.bean.integrity.IntegrityException;
import org.jspresso.framework.util.reflect.ReflectHelper;
import org.jspresso.framework.util.uid.ByteArray;
import org.jspresso.hrsample.model.City;
import org.jspresso.hrsample.model.Company;
import org.jspresso.hrsample.model.ContactInfo;
import org.jspresso.hrsample.model.Department;
import org.jspresso.hrsample.model.Employee;
import org.jspresso.hrsample.model.Event;
import org.jspresso.hrsample.model.Link;
import org.jspresso.hrsample.model.Nameable;
import org.jspresso.hrsample.model.OrganizationalUnit;
import org.jspresso.hrsample.model.Team;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

/**
 * Model integration tests.
 *
 * @author Vincent Vandenschrick
 */
public class JspressoModelTest extends BackTestStartup {

  /**
   * Tests computed fire property change. See bug 708.
   *
   * @throws Throwable
   *     whenever an unexpected error occurs.
   */
  @Test
  public void testComputedFirePropertyChange() throws Throwable {
    final HibernateBackendController hbc = (HibernateBackendController) getBackendController();
    boolean wasThrowExceptionOnBadUsage = hbc.isThrowExceptionOnBadUsage();
    try {
      hbc.setThrowExceptionOnBadUsage(false);
      EnhancedDetachedCriteria employeeCriteria = EnhancedDetachedCriteria.forClass(Employee.class);
      Employee employee = hbc.findFirstByCriteria(employeeCriteria, EMergeMode.MERGE_KEEP, Employee.class);
      ControllerAwareEntityInvocationHandler handlerSpy = (ControllerAwareEntityInvocationHandler) spy(
          Proxy.getInvocationHandler(employee));
      Employee employeeMock = (Employee) Proxy.newProxyInstance(getClass().getClassLoader(),
          new Class<?>[]{Employee.class}, handlerSpy);

      Method firePropertyChangeMethod = Employee.class.getMethod("firePropertyChange",
          String.class, Object.class, Object.class);

      employeeMock.setBirthDate(new Date(0));
      verify(handlerSpy, never()).invoke(eq(employeeMock), eq(firePropertyChangeMethod), argThat(new PropertyMatcher(
          Employee.AGE)));

      PropertyChangeListener fakeListener = new PropertyChangeListener() {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
          // NO-OP
        }
      };

      employeeMock.addPropertyChangeListener(fakeListener);
      employeeMock.setBirthDate(new Date(1));
      verify(handlerSpy, times(1)).invoke(eq(employeeMock), eq(firePropertyChangeMethod), argThat(new PropertyMatcher(
          Employee.AGE)));

      employeeMock.removePropertyChangeListener(fakeListener);
      employeeMock.setBirthDate(new Date(2));
      verify(handlerSpy, times(1)).invoke(eq(employeeMock), eq(firePropertyChangeMethod), argThat(new PropertyMatcher(
          Employee.AGE)));

      employeeMock.addPropertyChangeListener(Employee.AGE, fakeListener);
      employeeMock.setBirthDate(new Date(3));
      verify(handlerSpy, times(2)).invoke(eq(employeeMock), eq(firePropertyChangeMethod), argThat(new PropertyMatcher(
          Employee.AGE)));
    } finally {
      hbc.setThrowExceptionOnBadUsage(wasThrowExceptionOnBadUsage);
    }
  }

  /**
   * Tests adding twice the same entity to a list property. See bug 758.
   */
  @Test(expected = ComponentException.class)
  public void testAddToListTwice() {
    final HibernateBackendController hbc = (HibernateBackendController) getBackendController();

    EnhancedDetachedCriteria employeeCriteria = EnhancedDetachedCriteria.forClass(Employee.class);
    final Employee emp = hbc.findFirstByCriteria(employeeCriteria, EMergeMode.MERGE_KEEP, Employee.class);

    Event evt = emp.getEvents().get(0);
    emp.addToEvents(evt);
    assertSame(emp.getEvents().get(0), evt);
    assertSame(emp.getEvents().get(emp.getEvents().size() - 1), evt);

    hbc.getTransactionTemplate().execute(new TransactionCallbackWithoutResult() {

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
    EnhancedDetachedCriteria crit = EnhancedDetachedCriteria.forClass(City.class);
    List<City> cities = hbc.findByCriteria(crit, EMergeMode.MERGE_KEEP, City.class);
    City firstCity = hbc.findById(cities.get(0).getId(), EMergeMode.MERGE_KEEP, City.class);
    assertSame(cities.get(0), firstCity);
  }

  /**
   * Tests EAGER uninitialized merge on null value.
   */
  @Test
  public void testUninitializedMerge() {
    final HibernateBackendController hbc = (HibernateBackendController) getBackendController();
    EnhancedDetachedCriteria crit = EnhancedDetachedCriteria.forClass(Department.class);
    final Department d1 = hbc.findFirstByCriteria(crit, EMergeMode.MERGE_LAZY, Department.class);
    d1.straightSetProperty("company", null);

    Department d2 = hbc.getTransactionTemplate().execute(new TransactionCallback<Department>() {

      @Override
      public Department doInTransaction(TransactionStatus status) {
        Department d = (Department) hbc.getHibernateSession().get(Department.class, d1.getId());
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

    EnhancedDetachedCriteria crit = EnhancedDetachedCriteria.forClass(Department.class);
    final Department d = hbc.findFirstByCriteria(crit, EMergeMode.MERGE_LAZY, Department.class);
    assertSame(d, d.getContact().getOwningComponent());

    hbc.getTransactionTemplate().execute(new TransactionCallbackWithoutResult() {

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

    EnhancedDetachedCriteria crit = EnhancedDetachedCriteria.forClass(Department.class);
    final Department d = hbc.findFirstByCriteria(crit, EMergeMode.MERGE_LAZY, Department.class);

    SQLQuery query = hbc.getHibernateSession().createSQLQuery(
        "UPDATE DEPARTMENT SET NAME = 'Test' WHERE ID = :ID_PARAM");
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

    Company company = hbc.getEntityFactory().createEntityInstance(Company.class);
    // for (int i = 0; i < 5000; i++) {
    // Employee emp = hbc.getEntityFactory()
    // .createEntityInstance(Employee.class);
    // // The employee collection should be sorted by name
    // emp.setName(Integer.toHexString(emp.hashCode()));
    // company.addToEmployees(emp);
    // }
    Set<Employee> employees = new HashSet<Employee>();
    for (int i = 0; i < 5000; i++) {
      Employee emp = hbc.getEntityFactory().createEntityInstance(Employee.class);
      // The employee collection should be sorted by name
      emp.setName(Integer.toHexString(emp.hashCode()));
      employees.add(emp);
    }
    company.setEmployees(employees);
  }

  /**
   * Tests entities memory consumption.
   */
  @Test
  public void testEntitiesMemoryConsumption() throws InterruptedException {
    final HibernateBackendController hbc = (HibernateBackendController) getBackendController();
    System.gc();
    //Thread.sleep(2000);
    long start = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    List<Employee> employees = new ArrayList<>();
    for (int i = 0; i < 5000; i++) {
      Employee emp = hbc.getEntityFactory().createEntityInstance(Employee.class);
      // The employee collection should be sorted by name
      //emp.setName(Integer.toHexString(emp.hashCode()));
      emp.straightSetProperty(IEntity.ID, null);
      employees.add(emp);
    }
    System.gc();
    //Thread.sleep(2000);
    long end = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    System.out.println((end - start) / 1024 / 1024 + " MB used memory for " + employees.size() + " entities. This " +
        "means " + (end - start) / employees.size() + " B per entity.");
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

  /**
   * Tests fix for bug #928.
   */
  @Test
  public void testJoinOrderBy() {
    final HibernateBackendController hbc = (HibernateBackendController) getBackendController();
    EnhancedDetachedCriteria crit = EnhancedDetachedCriteria.forClass(Department.class);

    DetachedCriteria companyCrit = crit.getSubCriteriaFor(crit, Department.COMPANY, JoinType.INNER_JOIN);
    companyCrit.add(Restrictions.eq(Nameable.NAME, "Design2See"));

    DetachedCriteria teamsCrit = crit.getSubCriteriaFor(crit, Department.TEAMS, JoinType.LEFT_OUTER_JOIN);
    teamsCrit.add(Restrictions.eq(OrganizationalUnit.OU_ID, "HR-001"));

    crit.addOrder(Order.desc(Nameable.NAME));
    crit.addOrder(Order.asc(IEntity.ID));

    List<Department> depts = hbc.findByCriteria(crit, null, Department.class);
    for (Department d : depts) {
      // force collection sorting.
      Set<Team> teams = d.getTeams();
      Set<?> innerSet;
      try {
        innerSet = (Set<?>) ReflectHelper.getPrivateFieldValue(PersistentSet.class, "set", teams);
      } catch (Exception ex) {
        throw new RuntimeException(ex);
      }
      assertTrue("innerSet is a LinkedHashSet", LinkedHashSet.class.isInstance(innerSet));
    }
  }

  /**
   * Tests fix for bug #920.
   */
  @Test
  public void testRemovedEntityIsClean() {
    final HibernateBackendController hbc = (HibernateBackendController) getBackendController();
    final City c = hbc.getEntityFactory().createEntityInstance(City.class);
    c.setName("Remove");
    c.setZip("00000");

    assertTrue(!c.isPersistent());
    assertTrue(hbc.isDirty(c));

    hbc.getTransactionTemplate().execute(new TransactionCallbackWithoutResult() {

      @Override
      protected void doInTransactionWithoutResult(TransactionStatus status) {
        hbc.registerForUpdate(hbc.cloneInUnitOfWork(c));
      }
    });

    assertTrue(c.isPersistent());
    assertTrue(!hbc.isDirty(c));

    hbc.getTransactionTemplate().execute(new TransactionCallbackWithoutResult() {

      @Override
      protected void doInTransactionWithoutResult(TransactionStatus status) {
        City c1 = hbc.cloneInUnitOfWork(c);
        try {
          hbc.cleanRelationshipsOnDeletion(c1, false);
        } catch (Exception ex) {
          throw new RuntimeException(ex);
        }
      }
    });

    assertTrue("Entity is still considered persistent after deletion", !c.isPersistent());
    assertTrue("Entity is still considered dirty, although we can't do much with it", !hbc.isDirty(c));
  }

  /**
   * Tests that 3+ level nested property changes get notified.
   */
  @Test
  public void testSubNestedPropertyChange() {
    final HibernateBackendController hbc = (HibernateBackendController) getBackendController();

    EnhancedDetachedCriteria crit = EnhancedDetachedCriteria.forClass(Department.class);
    final Department d = hbc.findFirstByCriteria(crit, EMergeMode.MERGE_LAZY, Department.class);
    final StringBuilder buff = new StringBuilder();
    d.addPropertyChangeListener(
        OrganizationalUnit.MANAGER + "." + Employee.CONTACT + "." + ContactInfo.CITY + "." + Nameable.NAME,
        new PropertyChangeListener() {

          @Override
          public void propertyChange(PropertyChangeEvent evt) {
            buff.append(evt.getNewValue());
          }
        });
    City currentCity = d.getManager().getContact().getCity();
    currentCity.setName("testSubNotif");
    assertEquals("Sub-nested notification did not arrive correctly", currentCity.getName(), buff.toString());
    buff.delete(0, buff.length());

    City newCity = hbc.getEntityFactory().createEntityInstance(City.class);
    newCity.setName("newSubNotif");
    newCity.setZip("12345");
    d.getManager().getContact().setCity(newCity);
    assertEquals("Sub-nested notification did not arrive correctly", newCity.getName(), buff.toString());
    buff.delete(0, buff.length());

    newCity.setName("anotherOne");
    assertEquals("Sub-nested notification did not arrive correctly", newCity.getName(), buff.toString());
    buff.delete(0, buff.length());

    currentCity.setName("noNotifExpected");
    assertEquals("Sub-nested notification arrived whereas is shouldn't", "", buff.toString());
    buff.delete(0, buff.length());

    City anotherNewCity = hbc.getEntityFactory().createEntityInstance(City.class);
    anotherNewCity.setName("anotherNewCity");
    anotherNewCity.setZip("12345");

    Employee newManager = hbc.getEntityFactory().createEntityInstance(Employee.class);
    newManager.getContact().setCity(anotherNewCity);
    newManager.setCompany(d.getCompany());
    d.setManager(newManager);
    assertEquals("Sub-nested notification did not arrive correctly", anotherNewCity.getName(), buff.toString());
    buff.delete(0, buff.length());

    anotherNewCity.setName("anotherNewNotif");
    assertEquals("Sub-nested notification did not arrive correctly", anotherNewCity.getName(), buff.toString());
    buff.delete(0, buff.length());
  }

  /**
   * Tests that property cache gets correctly reset when there is no listener
   * (bug #852) and that the property change events are correctly fired when the
   * computed property is supposed to change (bug #1025).
   */
  @Test
  public void testComputedPropertyCacheResetAndNotif() {
    Employee emp = getBackendController().getEntityFactory().createEntityInstance(Employee.class);
    Calendar c = Calendar.getInstance();
    c.set(Calendar.YEAR, 1973);

    emp.setBirthDate(c.getTime());
    assertEquals("Age is not correctly computed.", emp.computeAge(emp.getBirthDate()), emp.getAge());

    c.add(Calendar.YEAR, -3);
    emp.setBirthDate(c.getTime());
    assertEquals("Age is not correctly computed after birth date modification.", emp.computeAge(emp.getBirthDate()),
        emp.getAge());

    final StringBuilder buff = new StringBuilder();
    emp.addPropertyChangeListener(Employee.AGE, new PropertyChangeListener() {

      @Override
      public void propertyChange(PropertyChangeEvent evt) {
        buff.append(evt.getNewValue());
      }
    });
    c.add(Calendar.YEAR, -5);
    emp.setBirthDate(c.getTime());
    assertEquals("Age is not correctly computedafter 2nd birth date modification.", emp.computeAge(emp.getBirthDate()),
        emp.getAge());
    assertTrue("Age notification failed.", buff.length() > 0);
    assertEquals("Age notification contains bad value.", emp.computeAge(emp.getBirthDate()).toString(),
        buff.toString());
  }

  /**
   * Tests Hibernate null components handling.
   * See bug #1041
   */
  @Test
  public void testNullComponentHandling() {
    final HibernateBackendController hbc = (HibernateBackendController) getBackendController();
    final Company companyWithNullContact = hbc.getEntityFactory().createEntityInstance(Company.class);
    companyWithNullContact.setName("CompanyWithNullContact");
    hbc.getTransactionTemplate().execute(new TransactionCallbackWithoutResult() {

      @Override
      protected void doInTransactionWithoutResult(TransactionStatus status) {
        Company cClone = hbc.cloneInUnitOfWork(companyWithNullContact);
        hbc.registerForUpdate(cClone);
      }
    });

    EnhancedDetachedCriteria companyCrit = EnhancedDetachedCriteria.forClass(Company.class);
    List<Company> companies = hbc.findByCriteria(companyCrit, EMergeMode.MERGE_KEEP, Company.class);
    for (Company c : companies) {
      assertNotNull("Company has a null contact (embedded component) instead of an empty one.", c.getContact());
      assertFalse("Company is dirty whereas it shouldn't", hbc.isDirty(c));
    }
  }

  /**
   * Test component dirty properties. See bug #1038
   */
  @Test
  public void testComponentDirtyProperties() {
    final HibernateBackendController hbc = (HibernateBackendController) getBackendController();
    EnhancedDetachedCriteria crit = EnhancedDetachedCriteria.forClass(Company.class);
    Company c = hbc.findFirstByCriteria(crit, EMergeMode.MERGE_CLEAN_EAGER, Company.class);
    String oldAddress = c.getContact().getAddress();
    c.getContact().setAddress("dirty");
    Map<String, Object> dirtyProperties = hbc.getDirtyProperties(c);
    assertNotNull("Contact old value is null", dirtyProperties.get(Company.CONTACT));
    assertEquals("Old contact address does not have the correct oldValue", oldAddress,
        ((ContactInfo) dirtyProperties.get(Company.CONTACT)).getAddress());
  }

  /**
   * Test component toString. See bug #1042
   */
  @Test
  public void testComponentToString() {
    final HibernateBackendController hbc = (HibernateBackendController) getBackendController();
    EnhancedDetachedCriteria crit = EnhancedDetachedCriteria.forClass(Company.class);
    Company c = hbc.findFirstByCriteria(crit, EMergeMode.MERGE_CLEAN_EAGER, Company.class);
    ContactInfo companyContact = c.getContact();
    assertEquals("contact toString() should be the holding entity one when not null.", c.toString(),
        companyContact.toString());
    ContactInfo orphanContact = hbc.getEntityFactory().createComponentInstance(ContactInfo.class);
    orphanContact.setAddress("toString test");
    assertEquals("contact toString() should be address when the holding entity is null.", "toString test",
        orphanContact.toString());
  }

  /**
   * Test Collection null elements. See bug #643
   */
  @Test(expected = MandatoryPropertyException.class)
  public void testNullElementAllowed() {
    final HibernateBackendController hbc = (HibernateBackendController) getBackendController();
    EnhancedDetachedCriteria crit = EnhancedDetachedCriteria.forClass(Employee.class);
    Employee e = hbc.findFirstByCriteria(crit, EMergeMode.MERGE_CLEAN_EAGER, Employee.class);
    e.addToEvents(null);
    hbc.registerForUpdate(e);
    hbc.getTransactionTemplate().execute(new TransactionCallbackWithoutResult() {
      @Override
      protected void doInTransactionWithoutResult(TransactionStatus status) {
        hbc.performPendingOperations();
      }
    });
  }

  /**
   * Test Enumeration value rejection. See bug #841
   */
  @Test(expected = IntegrityException.class)
  public void testEnumValueRejection() {
    final HibernateBackendController hbc = (HibernateBackendController) getBackendController();
    Employee emp = hbc.getEntityFactory().createEntityInstance(Employee.class);
    emp.setGender("C");
  }

  /**
   * Persistent collection substitution on save. See bug #1114
   */
  @Test
  public void testPersistentCollectionSubstitution() {
    final HibernateBackendController hbc = (HibernateBackendController) getBackendController();
    Company comp = hbc.getEntityFactory().createEntityInstance(Company.class);
    comp.setName("testC");

    Department dept = hbc.getEntityFactory().createEntityInstance(Department.class);
    dept.setName("testD");
    dept.setOuId("TE-001");

    comp.addToDepartments(dept);

    hbc.registerForUpdate(comp);
    hbc.getTransactionTemplate().execute(new TransactionCallbackWithoutResult() {
      @Override
      protected void doInTransactionWithoutResult(TransactionStatus status) {
        hbc.performPendingOperations();
      }
    });

    assertTrue("Persistent entity collection is not instance of PersistentCollection",
        comp.straightGetProperty(Company.DEPARTMENTS) instanceof PersistentCollection);
    assertTrue("Entity is transient after save", comp.isPersistent());
  }

  /**
   * Test translated name.
   */
  @Test
  public void testPropertyTranslation() {
    final HibernateBackendController hbc = (HibernateBackendController) getBackendController();
    Serializable cityId = hbc.getTransactionTemplate().execute(new TransactionCallback<Serializable>() {
      @Override
      public Serializable doInTransaction(TransactionStatus status) {
        City c = hbc.getEntityFactory().createEntityInstance(City.class);
        c.setNameRaw("raw");
        c.setName("test");
        hbc.registerForUpdate(c);
        return c.getId();
      }
    });
    final City c = hbc.findById(cityId, EMergeMode.MERGE_KEEP, City.class);
    assertEquals("A message translation should have been created", 1, c.getPropertyTranslations().size());
    hbc.getTransactionTemplate().execute(new TransactionCallbackWithoutResult() {
      @Override
      protected void doInTransactionWithoutResult(TransactionStatus status) {
        City cClone = hbc.cloneInUnitOfWork(c);
        cClone.setName("modified");
      }
    });
    hbc.reload(c);
    assertEquals("The message translation should have been updated", "modified", c.getName());
  }

  /**
   * Tests fix for bug #1115.
   */
  @Test
  public void testRecursiveComponentHashCodeEquals() {
    Link head = getBackendController().getEntityFactory().createComponentInstance(Link.class);
    Link tail = getBackendController().getEntityFactory().createComponentInstance(Link.class);
    head.setName("head");
    tail.setName("tail");
    head.addToChildren(tail);
    tail.setParent(head);
    int hc = head.hashCode();
    assertTrue("The hash code is not correctly computed", hc != 0);
    assertNotEquals("Equality is not correctly computed", head, tail);

    Link left = getBackendController().getEntityFactory().createComponentInstance(Link.class);
    Link right = getBackendController().getEntityFactory().createComponentInstance(Link.class);

    assertEquals("Equality is not correctly computed", left, right);

    left.addToChildren(right);
    right.addToChildren(left);
    left.setParent(right);
    right.setParent(left);

    assertNotEquals("Equality is not correctly computed", left, right);

    left.setName("left");
    right.setName("right");

    assertNotEquals("Equality is not correctly computed", left, right);
  }

  /**
   * Tests fix for bug #1126.
   */
  @Test
  public void testResetUninitializedReference() {
    final HibernateBackendController hbc = (HibernateBackendController) getBackendController();
    EnhancedDetachedCriteria crit = EnhancedDetachedCriteria.forClass(Employee.class);
    final Employee e = hbc.findFirstByCriteria(crit, EMergeMode.MERGE_KEEP, Employee.class);
    assertFalse(Hibernate.isInitialized(e.straightGetProperty(Employee.COMPANY)));
    e.straightSetProperty(Employee.COMPANY, null);
    e.getCompany();
  }

  /**
   * Test one to one dB access.
   */
  @Test
  public void testOneToOneDBAccess() {
    final HibernateBackendController hbc = (HibernateBackendController) getBackendController();
    hbc.getTransactionTemplate().execute(new TransactionCallbackWithoutResult() {
      @Override
      protected void doInTransactionWithoutResult(TransactionStatus status) {
        EnhancedDetachedCriteria critDept = EnhancedDetachedCriteria.forClass(Department.class);
        critDept.add(Restrictions.like(Department.OU_ID, "HR", MatchMode.START));
        Department dept = hbc.findFirstByCriteria(critDept, EMergeMode.MERGE_KEEP, Department.class);
        assertTrue("OneToOne has not been initialized when querying", Hibernate.isInitialized(dept.straightGetProperty(
            Department.MANAGER)));
      }
    });
    hbc.getTransactionTemplate().execute(new TransactionCallbackWithoutResult() {
      @Override
      protected void doInTransactionWithoutResult(TransactionStatus status) {
        EnhancedDetachedCriteria critEmp = EnhancedDetachedCriteria.forClass(Employee.class);
        critEmp.add(Restrictions.like(Employee.FIRST_NAME, "Gloria", MatchMode.START));
        Employee emp = hbc.findFirstByCriteria(critEmp, EMergeMode.MERGE_KEEP, Employee.class);
        assertFalse("ManyToOne has not been initialized when querying", Hibernate.isInitialized(emp.straightGetProperty(
            Employee.MANAGED_OU)));
      }
    });
  }

  /**
   * Test lifecycle called at right time. Fixes bug #55
   */
  @Test
  public void testLifecycleCalledAtRightTime() {
    final HibernateBackendController hbc = (HibernateBackendController) getBackendController();

    final Serializable id1 = hbc.getTransactionTemplate().execute(new TransactionCallback<Serializable>() {
      @Override
      public Serializable doInTransaction(TransactionStatus status) {
        Company company = hbc.getEntityFactory().createEntityInstance(Company.class);
        company.setName("testComp");
        hbc.registerForUpdate(company);
        // Old behaviour restored
        //assertNull("Create timestamp assigned too early", company.getCreateTimestamp());
        company.setName("testCompMod");
        return company.getId();
      }
    });
    Company sessionCompany = hbc.findById(id1, EMergeMode.MERGE_KEEP, Company.class);
    assertNotNull("Create timestamp not assigned", sessionCompany.getCreateTimestamp());
    assertNull("Last update timestamp assigned, but should not be", sessionCompany.getLastUpdateTimestamp());

    final Serializable id2 = hbc.getTransactionTemplate().execute(new TransactionCallback<Serializable>() {
      @Override
      public Serializable doInTransaction(TransactionStatus status) {
        Company company = hbc.getEntityFactory().createEntityInstance(Company.class);
        company.setName("testComp2");
        hbc.registerForUpdate(company);
        hbc.flush();
        assertNotNull("Create timestamp not assigned after flush", company.getCreateTimestamp());
        company.setName("testComp2Mod");
        return company.getId();
      }
    });
    Company sessionCompany2 = hbc.findById(id2, EMergeMode.MERGE_KEEP, Company.class);
    assertNotNull("Create timestamp not assigned", sessionCompany2.getCreateTimestamp());
    assertNotNull("Last update timestamp not assigned", sessionCompany2.getLastUpdateTimestamp());

    hbc.getTransactionTemplate().execute(new TransactionCallbackWithoutResult() {
      @Override
      protected void doInTransactionWithoutResult(TransactionStatus status) {
        Company company1 = hbc.findById(id1, EMergeMode.MERGE_KEEP, Company.class);
        Company company2 = hbc.findById(id2, EMergeMode.MERGE_KEEP, Company.class);
        hbc.registerForDeletion(company1);
        hbc.registerForDeletion(company2);
      }
    });
    assertNull("Company 1 not correctly deleted.", hbc.findById(id1, EMergeMode.MERGE_KEEP, Company.class));
    assertNull("Company 2 not correctly deleted.", hbc.findById(id2, EMergeMode.MERGE_KEEP, Company.class));
  }


  static class PropertyMatcher extends ArgumentMatcher<Object[]> {

    private final String propertyName;

    /**
     * Constructs a new {@code PropertyMatcher} instance.
     *
     * @param propertyName the property name
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
}
