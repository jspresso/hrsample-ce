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

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.junit.Test;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import org.jspresso.framework.application.backend.BackendControllerHolder;
import org.jspresso.framework.application.backend.BackendException;
import org.jspresso.framework.application.backend.ControllerAwareTransactionTemplate;
import org.jspresso.framework.application.backend.persistence.hibernate.HibernateBackendController;
import org.jspresso.framework.application.backend.persistence.hibernate.HibernateHelper;
import org.jspresso.framework.application.backend.session.EMergeMode;
import org.jspresso.framework.model.entity.IEntity;
import org.jspresso.framework.model.persistence.hibernate.criterion.EnhancedDetachedCriteria;

import org.jspresso.hrsample.model.City;
import org.jspresso.hrsample.model.Company;
import org.jspresso.hrsample.model.ContactInfo;
import org.jspresso.hrsample.model.Department;
import org.jspresso.hrsample.model.Employee;
import org.jspresso.hrsample.model.Event;
import org.jspresso.hrsample.model.Nameable;

/**
 * Unit Of Work management integration tests.
 *
 * @author Vincent Vandenschrick
 */
public class JspressoUnitOfWorkTest extends BackTestStartup {

  /**
   * Test retrieve UOW cloned instance from session. See bug 746.
   */
  @Test
  public void testClonedInstanceFromHibernateSession() {
    final HibernateBackendController hbc = (HibernateBackendController) getBackendController();

    EnhancedDetachedCriteria departmentCriteria = EnhancedDetachedCriteria
        .forClass(Department.class);
    final List<Department> departments = hbc.findByCriteria(departmentCriteria,
        EMergeMode.MERGE_KEEP, Department.class);

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

            Map<Serializable, Department> hibernateSessionDepartmentsById = new HashMap<Serializable, Department>();
            for (Department d : hibernateSessionDepartments) {
              hibernateSessionDepartmentsById.put(d.getId(), d);
            }

            // Now clone each of the Jspresso session department and verify that
            // there is no reference duplication, ie.e we are able to retrieve
            // the Hibernate session instances.
            List<Department> departmentClones = hbc
                .cloneInUnitOfWork(departments);
            for (Department d : departmentClones) {
              Department hibernateSessionDepartment = hibernateSessionDepartmentsById
                  .get(d.getId());
              if (hibernateSessionDepartment != null) {
                assertSame(hibernateSessionDepartment, d);
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
    emp.setAlternativeEvents(events);

    hbc.getTransactionTemplate().execute(
        new TransactionCallbackWithoutResult() {

          @Override
          protected void doInTransactionWithoutResult(TransactionStatus status) {
            hbc.cloneInUnitOfWork(emp);
          }
        });
    emp.addToAlternativeEvents(hbc.getEntityFactory().createEntityInstance(Event.class));
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
                Employee emp = hbc.findFirstByCriteria(empCrit, null, Employee.class);
                emp.setFirstName("Committed");
                return emp.getId();
              }
            });
        // asserts that UOW is still active after the end of the nested transaction.
        assertTrue("UOW should still be active since outer TX is ongoing.", hbc.isUnitOfWorkActive());
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
                Employee emp2 = hbc.findFirstByCriteria(empCrit, null, Employee.class);
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

    tt.execute(new TransactionCallbackWithoutResult() {

      @Override
      protected void doInTransactionWithoutResult(TransactionStatus status) {
        final City randomCity = hbc.findFirstByCriteria(DetachedCriteria.forClass(City.class), EMergeMode.MERGE_KEEP,
            City.class);
        TransactionTemplate nestedTT = new ControllerAwareTransactionTemplate(
            tt.getTransactionManager());
        nestedTT
            .setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        nestedTT.execute(new TransactionCallbackWithoutResult() {

          @Override
          public void doInTransactionWithoutResult(TransactionStatus nestedStatus) {
            DetachedCriteria cityById = DetachedCriteria.forClass(City.class);
            cityById.add(Restrictions.eq(IEntity.ID, randomCity.getId()));
            City innerRandomCity = (City) cityById.getExecutableCriteria(hbc.getHibernateSession()).uniqueResult();
            // If we reach this point without exception, there is no mix between the inner TX and the outer UOW.
            // See bug #1118
          }
        });
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
    assertEquals("test", c3.getNameRaw());

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
    assertEquals("test", c4.getNameRaw());

    final City c5 = hbc.findById(c1.getId(), EMergeMode.MERGE_CLEAN_LAZY,
        City.class);
    assertSame(c1, c5);
    assertEquals("test2", c5.getNameRaw());
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

  /**
   * Test dirty properties in UOW. See bug #1018.
   */
  @Test
  public void testDirtyPropertiesInUOW() {
    final HibernateBackendController hbc = (HibernateBackendController) getBackendController();

    EnhancedDetachedCriteria companyCriteria = EnhancedDetachedCriteria
        .forClass(Company.class);
    final Company comp = hbc.findFirstByCriteria(companyCriteria,
        EMergeMode.MERGE_KEEP, Company.class);
    assertTrue("Dirty properties are not initialized",
        hbc.getDirtyProperties(comp) != null);
    assertTrue("Dirty properties are not empty", hbc.getDirtyProperties(comp)
        .isEmpty());
    comp.setName("Updated");
    assertTrue("Company name is not dirty", hbc.getDirtyProperties(comp)
        .containsKey(Nameable.NAME));

    hbc.getTransactionTemplate().execute(
        new TransactionCallbackWithoutResult() {

          @Override
          protected void doInTransactionWithoutResult(TransactionStatus status) {
            EnhancedDetachedCriteria departmentCriteria = EnhancedDetachedCriteria
                .forClass(Department.class);
            Department dep = hbc.findFirstByCriteria(departmentCriteria,
                EMergeMode.MERGE_KEEP, Department.class);
            assertFalse("Company property is already initialized", Hibernate
                .isInitialized(dep.straightGetProperty(Department.COMPANY)));
            // Should be initialized now
            Company uowComp = dep.getCompany();
            assertTrue("Dirty properties are not initialized",
                hbc.getDirtyProperties(uowComp) != null);
            assertTrue("Dirty properties are not empty", hbc
                .getDirtyProperties(uowComp).isEmpty());
            assertFalse("Company is not fresh from DB",
                comp.getName().equals(uowComp.getName()));
            uowComp.setNameRaw("UpdatedUow");
            assertTrue("Company name is not dirty",
                hbc.getDirtyProperties(uowComp).containsKey(Nameable.NAME_RAW));
          }
        });
    assertEquals("Company name has not been correctly committed", "UpdatedUow",
        comp.getNameRaw());
  }

  /**
   * Test uninitialized reference properties merge on commit. See bug #1023.
   */
  @Test
  public void testUnititializedPropertiesMerge() {
    final HibernateBackendController hbc = (HibernateBackendController) getBackendController();

    EnhancedDetachedCriteria departmentCriteria = EnhancedDetachedCriteria
        .forClass(Department.class);
    List<Department> departments = hbc.findByCriteria(departmentCriteria,
        EMergeMode.MERGE_KEEP, Department.class);
    final Department department = departments.get(0);
    final Company existingCompany = (Company) department
        .straightGetProperty(Department.COMPANY);
    assertFalse("Company property is already initialized",
        Hibernate.isInitialized(existingCompany));

    Serializable newCompanyId = hbc.getTransactionTemplate().execute(
        new TransactionCallback<Serializable>() {

          @Override
          public Serializable doInTransaction(TransactionStatus status) {
            Department departmentClone = hbc.cloneInUnitOfWork(department);
            Company newCompany = hbc.getEntityFactory().createEntityInstance(
                Company.class);
            newCompany.setName("NewCompany");
            departmentClone.setCompany(newCompany);
            return newCompany.getId();
          }
        });
    assertEquals("New company reference is not correctly merged", newCompanyId,
        department.getCompany().getId());
    assertEquals("New company name is not correctly merged", "NewCompany",
        department.getCompany().getName());

    hbc.getTransactionTemplate().execute(
        new TransactionCallbackWithoutResult() {

          @Override
          public void doInTransactionWithoutResult(TransactionStatus status) {
            List<IEntity> clonedEntities = hbc.cloneInUnitOfWork(Arrays.asList(
                (IEntity) existingCompany, department));
            Company existingCompanyClone = (Company) clonedEntities.get(0);
            assertFalse("Company clone is already initialized",
                Hibernate.isInitialized(existingCompanyClone));
            Department departmentClone = (Department) clonedEntities.get(1);
            departmentClone.setCompany(existingCompanyClone);
          }
        });
    assertEquals("New company reference is not correctly merged",
        existingCompany.getId(), department.getCompany().getId());

    final Department otherDepartment = departments.get(1);
    Department deptFromUow = hbc.getTransactionTemplate().execute(
        new TransactionCallback<Department>() {

          @Override
          public Department doInTransaction(TransactionStatus status) {
            Department d = hbc.findById(otherDepartment.getId(), null,
                Department.class);
            return d;
          }
        });
    assertFalse("Department Company property from UOW is initialized",
        Hibernate.isInitialized(deptFromUow
            .straightGetProperty(Department.COMPANY)));
    hbc.merge(deptFromUow, EMergeMode.MERGE_EAGER);
  }

  /**
   * Test update / delete in TX. See bug #1009.
   */
  @Test
  public void testUpdateDeleteInTx() {
    final HibernateBackendController hbc = (HibernateBackendController) getBackendController();

    final City c1 = hbc.getEntityFactory().createEntityInstance(City.class);
    c1.setName("ToUpdate");
    c1.setZip("12345");
    hbc.registerForUpdate(c1);
    hbc.getTransactionTemplate().execute(
        new TransactionCallbackWithoutResult() {

          @Override
          protected void doInTransactionWithoutResult(TransactionStatus status) {
            hbc.performPendingOperations();
          }
        });
    hbc.getTransactionTemplate().execute(
        new TransactionCallbackWithoutResult() {

          @Override
          protected void doInTransactionWithoutResult(TransactionStatus status) {
            City c1Clone = hbc.cloneInUnitOfWork(c1);
            c1Clone.setName("ToDelete");
            hbc.registerForUpdate(c1Clone);
            hbc.registerForDeletion(c1Clone);
          }
        });
    City c2 = hbc.findById(c1.getId(), EMergeMode.MERGE_KEEP, City.class);
    assertNull("City has not been deleted correctly.", c2);
  }

  /**
   * Test clone in UOW of just initialized collection properties (recorded
   * original value might be UNKNOWN).
   */
  @Test
  public void testJustInitializedCollectionPropertyClone() {
    final HibernateBackendController hbc = (HibernateBackendController) getBackendController();

    final EnhancedDetachedCriteria departmentCriteria = EnhancedDetachedCriteria
        .forClass(Department.class);
    final List<Department> departments = hbc.findByCriteria(departmentCriteria,
        EMergeMode.MERGE_KEEP, Department.class);
    final Department department = departments.get(0);
    assertFalse("Department teams property from is initialized",
        Hibernate.isInitialized(department
            .straightGetProperty(Department.TEAMS)));

    hbc.getTransactionTemplate().execute(
        new TransactionCallbackWithoutResult() {

          @Override
          protected void doInTransactionWithoutResult(TransactionStatus status) {
            Department departmentInTx = hbc.findById(department.getId(),
                EMergeMode.MERGE_KEEP, Department.class);

            assertFalse("TX department teams property is initialized",
                Hibernate.isInitialized(departmentInTx
                    .straightGetProperty(Department.TEAMS)));

            departmentInTx.getTeams();
            Map<String, Object> inTxDirtyProperties = hbc
                .getDirtyProperties(departmentInTx);
            assertFalse("teams property is dirty whereas is shouldn't",
                inTxDirtyProperties.containsKey(Department.TEAMS));
          }
        });

    department.getTeams();

    hbc.getTransactionTemplate().execute(
        new TransactionCallbackWithoutResult() {

          @Override
          protected void doInTransactionWithoutResult(TransactionStatus status) {
            Department departmentInTx = hbc.cloneInUnitOfWork(department);

            departmentInTx.getTeams();
            Map<String, Object> inTxDirtyProperties = hbc
                .getDirtyProperties(departmentInTx);
            assertFalse("teams property is dirty whereas is shouldn't",
                inTxDirtyProperties.containsKey(Department.TEAMS));
          }
        });
    Map<String, Object> dirtyProperties = hbc.getDirtyProperties(department);
    assertFalse("teams property is dirty whereas is shouldn't",
        dirtyProperties.containsKey(Department.TEAMS));

    final Department anotherDepartment = departments.get(1);
    assertFalse("Other department teams property is initialized",
        Hibernate.isInitialized(anotherDepartment
            .straightGetProperty(Department.TEAMS)));
    final Department anotherDepartmentClone = hbc.getTransactionTemplate()
        .execute(new TransactionCallback<Department>() {

          @Override
          public Department doInTransaction(TransactionStatus status) {
            Department anotherDeptClone = hbc.cloneInUnitOfWork(
                anotherDepartment, true);

            assertFalse("Other department clone teams property is initialized",
                Hibernate.isInitialized(anotherDeptClone
                    .straightGetProperty(Department.TEAMS)));

            anotherDeptClone.getTeams();
            return anotherDeptClone;
          }
        });
    hbc.merge(anotherDepartmentClone, EMergeMode.MERGE_EAGER);
    Map<String, Object> anotherDirtyProperties = hbc.getDirtyProperties(anotherDepartment);
    assertFalse(
        "Other department teams property is dirty whereas is shouldn't",
        anotherDirtyProperties.containsKey(Department.TEAMS));

    hbc.getTransactionTemplate().execute(new TransactionCallback<Department>() {

      @Override
      public Department doInTransaction(TransactionStatus status) {
        Department yetAnotherDeptClone = hbc.cloneInUnitOfWork(
            anotherDepartmentClone, false);
        return yetAnotherDeptClone;
      }
    });
  }

  /**
   * Tests that an in-memory TX isolates its UOW from any inner transaction (see bug #63).
   */
  @Test
  public void testInMemoryTxIsolation() {
    final HibernateBackendController hbc = (HibernateBackendController) getBackendController();

    hbc.beginUnitOfWork();
    EnhancedDetachedCriteria compCrit = EnhancedDetachedCriteria.forClass(Company.class);

    hbc.findFirstByCriteria(compCrit, EMergeMode.MERGE_KEEP, Company.class);

    // Try to clone the company in the UOW.
    assertTrue("The in-memory TX has been terminated by the previous find", hbc.isUnitOfWorkActive());

    hbc.rollbackUnitOfWork();
  }

  /**
   * Tests that an in-memory TX preserves entities unicity in the UOW even if
   * multiple requests are spanned (see bug #1043).
   */
  @Test
  public void testInMemoryTxEntityUnicity() {
    final HibernateBackendController hbc = (HibernateBackendController) getBackendController();

    EnhancedDetachedCriteria compCrit = EnhancedDetachedCriteria.forClass(Company.class);
    Company company = hbc.findFirstByCriteria(compCrit, EMergeMode.MERGE_KEEP, Company.class);

    // Simulates a new request
    hbc.cleanupRequestResources();

    hbc.beginUnitOfWork();
    Company companyClone = hbc.cloneInUnitOfWork(company);
    assertNotSame("The company clone is the same instance as the original", companyClone, company);

    hbc.cleanupRequestResources();

    Company c1 = (Company) hbc.getHibernateSession().byId(Company.class).load(company.getId());
    assertSame("Both company instances should have been the same in UOW", c1, companyClone);

    hbc.rollbackUnitOfWork();

    hbc.beginUnitOfWork();
    Company c2 = (Company) hbc.getHibernateSession().byId(Company.class).load(company.getId());

    hbc.cleanupRequestResources();
    Company c3 = (Company) hbc.getHibernateSession().byId(Company.class).load(company.getId());

    hbc.cleanupRequestResources();
    companyClone = hbc.cloneInUnitOfWork(company);

    assertSame("Both company instances should have been the same in UOW", c2, c3);
    assertSame("Both company instances should have been the same in UOW", c2, companyClone);
    assertNotSame("The company clone is the same instance as the original", companyClone, company);
    assertNotSame("The company clone is the same instance as the first UOW clone", c1, c2);

    hbc.rollbackUnitOfWork();
  }

  /**
   * Test in memory tx entity update.
   */
  @Test
  public void testInMemoryTxEntityUpdate() {
    final HibernateBackendController hbc = (HibernateBackendController) getBackendController();
    EnhancedDetachedCriteria compCrit = EnhancedDetachedCriteria.forClass(Company.class);

    Company company = hbc.findFirstByCriteria(compCrit, EMergeMode.MERGE_KEEP, Company.class);

    // Simulates a new request
    hbc.cleanupRequestResources();

    hbc.beginUnitOfWork();
    final Company companyClone = hbc.cloneInUnitOfWork(company);
    String modifiedInUOW = "modifiedInUOW";
    companyClone.setName(modifiedInUOW);

    hbc.getTransactionTemplate()
       .execute(new TransactionCallbackWithoutResult() {

         @Override
         protected void doInTransactionWithoutResult(TransactionStatus status) {
           Company companyCloneClone = hbc.cloneInUnitOfWork(companyClone);
           assertNotSame("Cloning in a nested TX results in another clone", companyClone, companyCloneClone);
         }
       });
    assertTrue("The outer in-memory TX has been closed", hbc.isUnitOfWorkActive());
    hbc.commitUnitOfWork();

    assertFalse("The outer in-memory TX is still open", hbc.isUnitOfWorkActive());

    company = hbc.findById(company.getId(), EMergeMode.MERGE_CLEAN_EAGER, Company.class);
    assertEquals("Company has not been correctly merged", modifiedInUOW, company.getName());

    company = hbc.findById(company.getId(), EMergeMode.MERGE_CLEAN_EAGER, Company.class);
    assertEquals("Company has not been correctly persisted", modifiedInUOW, company.getName());
  }

  /**
   * Tests that an exception is thrown if we detect a flush of an entity that was not previously cloned.
   */
  @Test(expected = BackendException.class)
  public void testExceptionIfNotCloned() {
    final HibernateBackendController hbc = (HibernateBackendController) getBackendController();

    final City newCity = hbc.getEntityFactory().createEntityInstance(City.class);
    newCity.setName("New");

    hbc.getTransactionTemplate().execute(
        new TransactionCallbackWithoutResult() {

          @Override
          protected void doInTransactionWithoutResult(TransactionStatus status) {
            hbc.registerForUpdate(newCity);
          }
        });
  }


  /**
   * Tests fix for bug #1127.
   */
  @Test
  public void testInMemoryTxDirtyCollectionProperty() {
    final HibernateBackendController hbc = (HibernateBackendController) getBackendController();

    EnhancedDetachedCriteria compCrit = EnhancedDetachedCriteria.forClass(Company.class);
    Company company = hbc.findFirstByCriteria(compCrit, EMergeMode.MERGE_KEEP, Company.class);

    hbc.beginUnitOfWork();

    Company companyClone = hbc.cloneInUnitOfWork(company);
    companyClone.addToDepartments(hbc.getEntityFactory().createEntityInstance(Department.class));

    hbc.cleanupRequestResources();

    compCrit.getExecutableCriteria(hbc.getHibernateSession()).list();
  }

  /**
   * Tests fix for bug #1130.
   */
  @Test
  public void testDetachedEntitiesManagement() {
    final HibernateBackendController hbc = (HibernateBackendController) getBackendController();

    final Company company = hbc.getEntityFactory().createEntityInstance(Company.class);
    company.setName("TestCompany");

    final Department department = hbc.getEntityFactory().createEntityInstance(Department.class);
    department.setName("TestDepartment");
    department.setOuId("TE-001");

    company.addToDepartments(department);
    company.removeFromDepartments(department);

    Set<?> detachedEntities = (Set<?>) company.straightGetProperty("detachedEntities");
    assertTrue("DetachedEntities is empty and should not be.",
        detachedEntities != null && !detachedEntities.isEmpty());

    hbc.getTransactionTemplate().execute(
        new TransactionCallbackWithoutResult() {

          @Override
          protected void doInTransactionWithoutResult(TransactionStatus status) {
            Company companyClone = hbc.cloneInUnitOfWork(company);
            Department departmentClone = hbc.cloneInUnitOfWork(department);

            Set<?> detachedEntities = (Set<?>) companyClone.straightGetProperty("detachedEntities");
            assertTrue("DetachedEntities is empty and should not be.",
                detachedEntities != null && !detachedEntities.isEmpty());

            assertSame("DetachedEntities have not been cloned correctly.", departmentClone,
                detachedEntities.iterator().next());

            hbc.registerForUpdate(companyClone);
          }
        });

    detachedEntities = (Set<?>) company.straightGetProperty("detachedEntities");
    assertNull("DetachedEntities should have been reset.", detachedEntities);
  }

  /**
   * Tests fix for bug #1153.
   */
  @Test
  public void testAfterTxInitializationKeepsIsolation() {
    final HibernateBackendController hbc = (HibernateBackendController) getBackendController();
    EnhancedDetachedCriteria compCrit = EnhancedDetachedCriteria.forClass(Company.class);
    Company company = hbc.findFirstByCriteria(compCrit, EMergeMode.MERGE_KEEP, Company.class);

    Department uowDepartment = hbc.getTransactionTemplate().execute(new TransactionCallback<Department>() {
      @Override
      public Department doInTransaction(TransactionStatus status) {
        EnhancedDetachedCriteria deptCrit = EnhancedDetachedCriteria.forClass(Department.class);
        return hbc.findFirstByCriteria(deptCrit, null, Department.class);
      }
    });
    assertTrue("UOW dept company is initialized",
        !Hibernate.isInitialized(uowDepartment.straightGetProperty(Department.COMPANY)));
    Company uowCompany = uowDepartment.getCompany();
    assertFalse("Session company has been assigned to UOW department",
        HibernateHelper.objectEquals(company, uowCompany));
  }
}
