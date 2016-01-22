/*
 * Copyright (c) 2005-2016 Vincent Vandenschrick. All rights reserved.
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jspresso.framework.application.backend.persistence.hibernate.HibernateBackendController;
import org.jspresso.framework.application.backend.persistence.hibernate.HibernateHelper;
import org.jspresso.framework.application.backend.session.EMergeMode;
import org.jspresso.framework.model.persistence.hibernate.criterion.EnhancedDetachedCriteria;

import org.jspresso.hrsample.model.City;
import org.jspresso.hrsample.model.Company;
import org.jspresso.hrsample.model.Department;
import org.jspresso.hrsample.model.Employee;
import org.jspresso.hrsample.model.OrganizationalUnit;
import org.jspresso.hrsample.model.Team;

import org.hibernate.Hibernate;
import org.junit.Test;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

/**
 * Application session integration tests.
 *
 * @author Vincent Vandenschrick
 */
public class ApplicationSessionTest extends BackTestStartup {

  /**
   * Tests that uninitialized reference property are correctly deduped when
   * merged to session. See bug 773.
   */
  @Test
  public void testUninitializedPropertyDedupedWhenMerging() {
    final HibernateBackendController hbc = (HibernateBackendController) getBackendController();

    EnhancedDetachedCriteria ouCriteria = EnhancedDetachedCriteria
        .forClass(OrganizationalUnit.class);
    List<OrganizationalUnit> ous = hbc.findByCriteria(ouCriteria,
        EMergeMode.MERGE_KEEP, OrganizationalUnit.class);
    Map<Serializable, OrganizationalUnit> ousById = new HashMap<Serializable, OrganizationalUnit>();
    for (OrganizationalUnit ou : ous) {
      ousById.put(ou.getId(), ou);
    }

    EnhancedDetachedCriteria employeeCriteria = EnhancedDetachedCriteria
        .forClass(Employee.class);
    List<Employee> employees = hbc.findByCriteria(employeeCriteria,
        EMergeMode.MERGE_KEEP, Employee.class);
    for (Employee emp : employees) {
      OrganizationalUnit managedOu = (OrganizationalUnit) emp
          .straightGetProperty(Employee.MANAGED_OU);
      if (managedOu != null) {
        assertSame(managedOu, ousById.get(managedOu.getId()));
      }
    }
  }

  /**
   * Tests that uninitialized reference property are correctly loaded and registered to the session
   * when actually loaded. see bug #1150.
   */
  @Test
  public void testUninitializedPropertyRegisteredWhenLoaded() {
    final HibernateBackendController hbc = (HibernateBackendController) getBackendController();

    OrganizationalUnit ou = hbc.getTransactionTemplate().execute(new TransactionCallback<OrganizationalUnit>() {
      @Override
      public OrganizationalUnit doInTransaction(TransactionStatus status) {
        EnhancedDetachedCriteria employeeCriteria = EnhancedDetachedCriteria
            .forClass(Employee.class);
        List<Employee> employees = hbc.findByCriteria(employeeCriteria,
            null, Employee.class);
        for (Employee emp : employees) {
          OrganizationalUnit managedOu = (OrganizationalUnit) emp
              .straightGetProperty(Employee.MANAGED_OU);
          if (!Hibernate.isInitialized(managedOu)) {
            EnhancedDetachedCriteria companyCriteria = EnhancedDetachedCriteria
                .forClass(Company.class);
            Company company = hbc.findFirstByCriteria(companyCriteria, EMergeMode.MERGE_KEEP, Company.class);
            emp = hbc.merge(emp, EMergeMode.MERGE_LAZY);
            for (Department department :company.getDepartments()) {
              for (Team team : department.getTeams()) {
                team.getName();
              }
            }
            // Will initialize
            return emp.getManagedOu();
          }
        }
        return null;
      }
    });
    if(ou != null) {
      assertTrue("Loaded lazy reference has not correctly been merged to the application session",
          HibernateHelper.objectEquals(ou, hbc.getRegisteredEntity(OrganizationalUnit.class, ou.getId())));
    }
  }

  /**
   * Tests bug #1244. findById should return null for transient instances.
   */
  @Test
  public void testTransientGetReturnsNull() {
    final HibernateBackendController hbc = (HibernateBackendController) getBackendController();

    final City city = hbc.getEntityFactory().createEntityInstance(City.class);

    City c1 = hbc.findById(city.getId(), EMergeMode.MERGE_KEEP, City.class);
    assertNull("findById should never return transient instances", c1);

    City c2 = hbc.getTransactionTemplate().execute(new TransactionCallback<City>() {
      @Override
      public City doInTransaction(TransactionStatus status) {
        hbc.cloneInUnitOfWork(city);
        return hbc.findById(city.getId(), EMergeMode.MERGE_KEEP, City.class);
      }
    });
    assertNull("findById should never return transient instances", c2);
  }
}
