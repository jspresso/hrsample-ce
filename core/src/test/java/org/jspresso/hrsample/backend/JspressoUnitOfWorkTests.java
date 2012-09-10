/*
 * Copyright (c) 2005-2012 Vincent Vandenschrick. All rights reserved.
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Hibernate;
import org.jspresso.framework.application.backend.persistence.hibernate.HibernateBackendController;
import org.jspresso.framework.application.backend.session.EMergeMode;
import org.jspresso.framework.model.persistence.hibernate.criterion.EnhancedDetachedCriteria;
import org.jspresso.hrsample.model.Company;
import org.jspresso.hrsample.model.ContactInfo;
import org.jspresso.hrsample.model.Department;
import org.jspresso.hrsample.model.Employee;
import org.jspresso.hrsample.model.Event;
import org.junit.Test;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

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
          protected void doInTransactionWithoutResult(
              @SuppressWarnings("unused") TransactionStatus status) {
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
    final Employee emp = hbc.findByCriteria(employeeCriteria,
        EMergeMode.MERGE_KEEP, Employee.class).get(0);

    List<Event> events = new ArrayList<Event>();
    events.add(hbc.getEntityFactory().createEntityInstance(Event.class));
    events.add(null);
    events.add(hbc.getEntityFactory().createEntityInstance(Event.class));
    emp.setEvents(events);

    hbc.getTransactionTemplate().execute(
        new TransactionCallbackWithoutResult() {

          @Override
          protected void doInTransactionWithoutResult(
              @SuppressWarnings("unused") TransactionStatus status) {
            hbc.cloneInUnitOfWork(emp);
          }
        });
    emp.addToEvents(hbc.getEntityFactory().createEntityInstance(Event.class));
    hbc.getTransactionTemplate().execute(
        new TransactionCallbackWithoutResult() {

          @Override
          protected void doInTransactionWithoutResult(
              @SuppressWarnings("unused") TransactionStatus status) {
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
    final Employee emp = hbc.findByCriteria(employeeCriteria,
        EMergeMode.MERGE_KEEP, Employee.class).get(0);

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
          protected void doInTransactionWithoutResult(
              @SuppressWarnings("unused") TransactionStatus status) {
            hbc.cloneInUnitOfWork(emp);
          }
        });
    emp.addToAlternativeContacts(hbc.getEntityFactory().createComponentInstance(
        ContactInfo.class));
    hbc.getTransactionTemplate().execute(
        new TransactionCallbackWithoutResult() {

          @Override
          protected void doInTransactionWithoutResult(
              @SuppressWarnings("unused") TransactionStatus status) {
            hbc.cloneInUnitOfWork(emp);
          }
        });
  }
}
