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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import org.hibernate.Hibernate;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.jspresso.framework.application.backend.BackendControllerHolder;
import org.jspresso.framework.application.backend.BackendException;
import org.jspresso.framework.application.backend.ControllerAwareTransactionTemplate;
import org.jspresso.framework.application.backend.persistence.hibernate.HibernateBackendController;
import org.jspresso.framework.application.backend.session.EMergeMode;
import org.jspresso.framework.model.entity.IEntity;
import org.jspresso.framework.model.persistence.hibernate.criterion.EnhancedDetachedCriteria;
import org.jspresso.hrsample.model.City;
import org.jspresso.hrsample.model.Company;
import org.jspresso.hrsample.model.ContactInfo;
import org.jspresso.hrsample.model.Department;
import org.jspresso.hrsample.model.Employee;
import org.jspresso.hrsample.model.Event;
import org.junit.Test;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * Unit Of Work management integration tests.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class JspressoUnitOfWorkTests extends BackTestStartup {

  /**
   * Test retrieve UOW cloned instance from session. See bug 746.
   */
  @Test
  public void testClonedInstanceFromHibernateSession() {
    final HibernateBackendController hbc = (HibernateBackendController) getBackendController();

    EnhancedDetachedCriteria departmentCriteria = EnhancedDetachedCriteria
        .forClass(Department.class);
    final String updatedName = "testUpdatedName";
    final List<Department> departments = hbc.findByCriteria(departmentCriteria,
        EMergeMode.MERGE_KEEP, Department.class);
    for (Department d : departments) {
      d.setName(updatedName);
    }

    EnhancedDetachedCriteria companyCriteria = EnhancedDetachedCriteria
        .forClass(Company.class);
    final Company company = hbc.findFirstByCriteria(companyCriteria,
        EMergeMode.MERGE_KEEP, Company.class);

    hbc.getTransactionTemplate().execute(
        new TransactionCallbackWithoutResult() {

          @Override
          protected void doInTransactionWithoutResult(TransactionStatus status) {
            // Clone the company
            Company companyClone = hbc.cloneInUnitOfWork(company);
            // Check that the departments property is not initialized
            assertFalse(Hibernate.isInitialized(companyClone
                .straightGetProperty(Company.DEPARTMENTS)));
            // Retrieve the company departments (load them in the Hibernate
            // session)
            Set<Department> hibernateSessionDepartments = companyClone
                .getDepartments();

            Map<Serializable, Department> sessionDepartmentsById = new HashMap<Serializable, Department>();
            for (Department d : hibernateSessionDepartments) {
              sessionDepartmentsById.put(d.getId(), d);
            }

            // Now clone each of the Jspresso session department and verify that
            // there is no reference duplication, ie.e we are able to retrieve
            // the Hibernate session instances.
            List<Department> departmentClones = hbc
                .cloneInUnitOfWork(departments);
            for (Department d : departmentClones) {
              Department sessionDepartment = sessionDepartmentsById.get(d
                  .getId());
              if (sessionDepartment != null) {
                assertSame(sessionDepartment, d);
                assertEquals(updatedName, sessionDepartment.getName());
              }
            }
          }
        });
  }

  /**
   * Clone/merge of entity lists with holes. See bug 757.
   */
  @Test
  public void testCloneEntityListWithHoles() {
    final HibernateBackendController hbc = (HibernateBackendController) getBackendController();

    EnhancedDetachedCriteria employeeCriteria = EnhancedDetachedCriteria
        .forClass(Employee.class);
    final Employee emp = hbc.findFirstByCriteria(employeeCriteria,
        EMergeMode.MERGE_KEEP, Employee.class);

    List<Event> events = new ArrayList<Event>();
    events.add(hbc.getEntityFactory().createEntityInstance(Event.class));
    events.add(null);
    events.add(hbc.getEntityFactory().createEntityInstance(Event.class));
    emp.setEvents(events);

    hbc.getTransactionTemplate().execute(
        new TransactionCallbackWithoutResult() {

          @Override
          protected void doInTransactionWithoutResult(TransactionStatus status) {
            hbc.cloneInUnitOfWork(emp);
          }
        });
    emp.addToEvents(hbc.getEntityFactory().createEntityInstance(Event.class));
    hbc.getTransactionTemplate().execute(
        new TransactionCallbackWithoutResult() {

          @Override
          protected void doInTransactionWithoutResult(TransactionStatus status) {
            hbc.cloneInUnitOfWork(emp);
          }
        });
  }

  /**
   * Clone/merge of component lists with holes. See bug 757.
   */
  @Test
  public void testCloneComponentListWithHoles() {
    final HibernateBackendController hbc = (HibernateBackendController) getBackendController();

    EnhancedDetachedCriteria employeeCriteria = EnhancedDetachedCriteria
        .forClass(Employee.class);
    final Employee emp = hbc.findFirstByCriteria(employeeCriteria,
        EMergeMode.MERGE_KEEP, Employee.class);

    List<ContactInfo> alternativeContacts = new ArrayList<ContactInfo>();
    alternativeContacts.add(hbc.getEntityFactory().createComponentInstance(
        ContactInfo.class));
    alternativeContacts.add(null);
    alternativeContacts.add(hbc.getEntityFactory().createComponentInstance(
        ContactInfo.class));
    emp.setAlternativeContacts(alternativeContacts);

    hbc.getTransactionTemplate().execute(
        new TransactionCallbackWithoutResult() {

          @Override
          protected void doInTransactionWithoutResult(TransactionStatus status) {
            hbc.cloneInUnitOfWork(emp);
          }
        });
    emp.addToAlternativeContacts(hbc.getEntityFactory()
        .createComponentInstance(ContactInfo.class));
    hbc.getTransactionTemplate().execute(
        new TransactionCallbackWithoutResult() {

          @Override
          protected void doInTransactionWithoutResult(TransactionStatus status) {
            hbc.cloneInUnitOfWork(emp);
          }
        });
  }

  /**
   * Tests the use of nested transactions.
   */
  @Test
  public void testNestedTransactions() {
    final HibernateBackendController hbc = (HibernateBackendController) getBackendController();
    final TransactionTemplate tt = hbc.getTransactionTemplate();

    Serializable empId = tt.execute(new TransactionCallback<Serializable>() {

      @Override
      public Serializable doInTransaction(TransactionStatus status) {
        TransactionTemplate nestedTT = new ControllerAwareTransactionTemplate(
            tt.getTransactionManager());
        nestedTT
            .setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        Serializable id = nestedTT
            .execute(new TransactionCallback<Serializable>() {

              @Override
              public Serializable doInTransaction(TransactionStatus nestedStatus) {
                DetachedCriteria empCrit = DetachedCriteria
                    .forClass(Employee.class);
                Employee emp = (Employee) empCrit
                    .getExecutableCriteria(hbc.getHibernateSession()).list()
                    .iterator().next();
                emp.setFirstName("Committed");
                return emp.getId();
              }
            });
        // forces rollback of outer TX.
        status.setRollbackOnly();
        return id;
      }
    });
    DetachedCriteria empById = DetachedCriteria.forClass(Employee.class);
    empById.add(Restrictions.eq(IEntity.ID, empId));
    Employee emp = hbc.findFirstByCriteria(empById,
        EMergeMode.MERGE_CLEAN_EAGER, Employee.class);
    assertTrue("Inner transaction should have been committed",
        "Committed".equals(emp.getFirstName()));

    Serializable emp2Id = tt.execute(new TransactionCallback<Serializable>() {

      @Override
      public Serializable doInTransaction(TransactionStatus status) {
        TransactionTemplate nestedTT = new ControllerAwareTransactionTemplate(
            tt.getTransactionManager());
        nestedTT
            .setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        Serializable id = nestedTT
            .execute(new TransactionCallback<Serializable>() {

              @Override
              public Serializable doInTransaction(TransactionStatus nestedStatus) {
                DetachedCriteria empCrit = DetachedCriteria
                    .forClass(Employee.class);
                Employee emp2 = (Employee) empCrit
                    .getExecutableCriteria(hbc.getHibernateSession()).list()
                    .iterator().next();
                emp2.setFirstName("Rollbacked");
                return emp2.getId();
              }
            });
        // forces rollback of outer TX.
        status.setRollbackOnly();
        return id;
      }
    });
    DetachedCriteria emp2ById = DetachedCriteria.forClass(Employee.class);
    emp2ById.add(Restrictions.eq(IEntity.ID, emp2Id));
    Employee emp2 = hbc.findFirstByCriteria(empById,
        EMergeMode.MERGE_CLEAN_EAGER, Employee.class);
    assertFalse("Inner transaction should have been rollbacked",
        "Rollbacked".equals(emp2.getFirstName()));

    tt.execute(new TransactionCallbackWithoutResult() {

      @Override
      protected void doInTransactionWithoutResult(TransactionStatus status) {
        City newCity = hbc.getEntityFactory().createEntityInstance(City.class);
        newCity.setName("Test City");

        TransactionTemplate nestedTT = new ControllerAwareTransactionTemplate(
            tt.getTransactionManager());
        nestedTT
            .setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        String testZip = nestedTT.execute(new TransactionCallback<String>() {

          @Override
          public String doInTransaction(TransactionStatus nestedStatus) {
            return "12345";
          }
        });
        newCity.setZip(testZip);
        hbc.registerForUpdate(newCity);
      }
    });
  }

  /**
   * Tests sanity check on linked components. See bug #846.
   */
  @Test(expected = BackendException.class)
  public void testSanityChecksOnComponents() {
    final HibernateBackendController hbc = (HibernateBackendController) getBackendController();
    final TransactionTemplate tt = hbc.getTransactionTemplate();

    Employee emp = tt.execute(new TransactionCallback<Employee>() {

      /**
       * {@inheritDoc}
       */
      @Override
      public Employee doInTransaction(TransactionStatus status) {
        DetachedCriteria empCrit = DetachedCriteria.forClass(Employee.class);
        return (Employee) empCrit
            .getExecutableCriteria(hbc.getHibernateSession()).list().iterator()
            .next();
      }
    });
    // From here, any modification on employee should result in an exception
    // since this instance of employee is not merged in session.
    // The exception should also occur on component (contact) properties
    // modification.
    emp.getContact().setAddress("test");
  }

  /**
   * Tests merge modes.
   */
  @Test
  public void testMergeModes() {
    final HibernateBackendController hbc = (HibernateBackendController) getBackendController();

    EnhancedDetachedCriteria crit = EnhancedDetachedCriteria
        .forClass(City.class);
    final City c1 = hbc.findFirstByCriteria(crit, EMergeMode.MERGE_CLEAN_EAGER,
        City.class);
    String name = c1.getName();

    JdbcTemplate jdbcTemplate = getApplicationContext().getBean("jdbcTemplate",
        JdbcTemplate.class);
    jdbcTemplate.execute(new ConnectionCallback<Object>() {

      @Override
      public Object doInConnection(Connection con) throws SQLException {
        PreparedStatement ps = con
            .prepareStatement("UPDATE CITY SET NAME = ? WHERE ID = ?");
        ps.setString(1, "test");
        ps.setObject(2, c1.getId());
        assertEquals(1, ps.executeUpdate());
        return null;
      }
    });
    final City c2 = hbc.findById(c1.getId(), EMergeMode.MERGE_CLEAN_LAZY,
        City.class);

    assertSame(c1, c2);
    assertEquals(name, c2.getName());

    final City c3 = hbc.findById(c1.getId(), EMergeMode.MERGE_CLEAN_EAGER,
        City.class);
    assertSame(c1, c3);
    assertEquals("test", c3.getName());

    jdbcTemplate.execute(new ConnectionCallback<Object>() {

      @Override
      public Object doInConnection(Connection con) throws SQLException {
        PreparedStatement ps = con
            .prepareStatement("UPDATE CITY SET NAME = ?, VERSION = VERSION+1 WHERE ID = ?");
        ps.setString(1, "test2");
        ps.setObject(2, c1.getId());
        assertEquals(1, ps.executeUpdate());
        return null;
      }
    });

    final City c4 = hbc.findById(c1.getId(), EMergeMode.MERGE_KEEP, City.class);
    assertSame(c1, c4);
    assertEquals("test", c4.getName());

    final City c5 = hbc.findById(c1.getId(), EMergeMode.MERGE_CLEAN_LAZY,
        City.class);
    assertSame(c1, c5);
    assertEquals("test2", c5.getName());
  }

  /**
   * Tests in TX collection element update with // optimistick locking.
   */
  @Test
  public void testInTXCollectionElementUpdate() {
    final HibernateBackendController hbc = (HibernateBackendController) getBackendController();

    final AtomicInteger countDown = new AtomicInteger(10);
    ExecutorService es = Executors.newFixedThreadPool(countDown.get());
    List<Future<Set<String>>> futures = new ArrayList<Future<Set<String>>>();
    for (int t = countDown.intValue(); t > 0; t--) {
      futures.add(es.submit(new Callable<Set<String>>() {

        @Override
        public Set<String> call() throws Exception {
          final HibernateBackendController threadHbc = getApplicationContext()
              .getBean("applicationBackController",
                  HibernateBackendController.class);
          final TransactionTemplate threadTT = threadHbc
              .getTransactionTemplate();
          threadHbc.start(hbc.getLocale(), hbc.getClientTimeZone());
          threadHbc.setApplicationSession(hbc.getApplicationSession());
          BackendControllerHolder.setThreadBackendController(threadHbc);
          return threadTT.execute(new TransactionCallback<Set<String>>() {

            /**
             * {@inheritDoc}
             */
            @Override
            public Set<String> doInTransaction(TransactionStatus status) {
              DetachedCriteria compCrit = DetachedCriteria
                  .forClass(Company.class);
              Set<String> names = new HashSet<String>();
              Company c = (Company) compCrit
                  .getExecutableCriteria(threadHbc.getHibernateSession())
                  .list().iterator().next();

              synchronized (countDown) {
                countDown.decrementAndGet();
                // wait for all threads to arrive here so that we are sure they
                // have all read the same data.
                try {
                  countDown.wait();
                } catch (InterruptedException ex) {
                  throw new BackendException("Test has been interrupted");
                }
              }

              if (c.getName().startsWith("TX_")) {
                throw new BackendException("Wrong data read from DB");
              }
              c.setName("TX_" + Long.toHexString(System.currentTimeMillis()));
              names.add(c.getName());
              for (Department d : c.getDepartments()) {
                d.setName(Long.toHexString(System.currentTimeMillis()));
                names.add(d.getName());
              }
              return names;
            }
          });
        }
      }));
    }
    while (countDown.get() > 0) {
      try {
        Thread.sleep(200);
      } catch (InterruptedException ex) {
        throw new BackendException("Test has been interrupted");
      }
    }
    synchronized (countDown) {
      countDown.notifyAll();
    }
    int successfullTxCount = 0;
    Set<String> names = new HashSet<String>();
    for (Future<Set<String>> f : futures) {
      try {
        names = f.get();
        successfullTxCount++;
      } catch (Exception ex) {
        if (ex.getCause() instanceof OptimisticLockingFailureException) {
          // safely ignore since this is what we are testing.
        } else {
          throw new BackendException(ex);
        }
      }
    }
    es.shutdown();
    assertTrue("Only 1 TX succeeded", successfullTxCount == 1);

    DetachedCriteria compCrit = DetachedCriteria.forClass(Company.class);
    Company c = hbc.findFirstByCriteria(compCrit, EMergeMode.MERGE_LAZY,
        Company.class);
    assertTrue("the company name is the one of the successfull TX",
        names.contains(c.getName()));
    for (Department d : c.getDepartments()) {
      assertTrue("the department name is the one of the successfull TX",
          names.contains(d.getName()));
    }

  }
}
