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

import java.security.acl.Group;

import javax.security.auth.Subject;

import org.hibernate.criterion.DetachedCriteria;
import org.jboss.security.SimpleGroup;
import org.jboss.security.SimplePrincipal;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import org.jspresso.framework.application.backend.persistence.hibernate.HibernateBackendController;
import org.jspresso.framework.application.startup.AbstractBackendStartup;
import org.jspresso.framework.model.entity.IEntity;
import org.jspresso.framework.model.persistence.hibernate.criterion.EnhancedDetachedCriteria;
import org.jspresso.framework.security.SecurityHelper;
import org.jspresso.framework.security.UserPrincipal;
import org.jspresso.framework.util.exception.NestedRuntimeException;

import org.jspresso.hrsample.development.HibernateTestDataPersister;
import org.jspresso.hrsample.model.City;
import org.jspresso.hrsample.model.Company;
import org.jspresso.hrsample.model.Role;

/**
 * Base class for integration tests.
 *
 * @author Vincent Vandenschrick
 */
public class BackTestStartup extends AbstractBackendStartup {

  /**
   * Performs DB initialization and testDdd data creation.
   *
   * @throws Exception
   *     the exception
   */
  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    BackTestStartup startup = new BackTestStartup();
    new HibernateTestDataPersister(startup.getApplicationContext(), true).persistTestData();
  }

  /**
   * Destroys all data from DB.
   *
   * @throws Exception
   *     the exception
   */
  @AfterClass
  public static void tearDownAfterClass() throws Exception {
    BackTestStartup startup = new BackTestStartup();
    startup.start();
    final HibernateBackendController bc = (HibernateBackendController) startup
        .getBackendController();
    bc.getTransactionTemplate().execute(new TransactionCallbackWithoutResult() {

      @Override
      protected void doInTransactionWithoutResult(TransactionStatus status) {
        deleteAllInstances(bc, Company.class);
        deleteAllInstances(bc, City.class);
        deleteAllInstances(bc, Role.class);
      }
    });
  }

  private static <E extends IEntity> void deleteAllInstances(HibernateBackendController bc, Class<E> clazz) {
    DetachedCriteria crit;
    crit = EnhancedDetachedCriteria
        .forClass(clazz);
    for (E entity : bc.findByCriteria(crit, null, clazz)) {
      try {
        bc.cleanRelationshipsOnDeletion(entity, false);
      } catch (Exception ex) {
        throw new NestedRuntimeException(ex);
      }
    }
    bc.getHibernateSession().flush();
  }

  /**
   * Starts a new controller and creates the session using the "testDdd" user with
   * english locale.
   *
   * @throws Exception
   *     the exception
   */
  @Before
  public void setUp() throws Exception {
    start();
    configureApplicationSession(createTestSubject(), getStartupLocale());
  }

  private Subject createTestSubject() {
    Subject testSubject = new Subject();
    UserPrincipal p = new UserPrincipal("demo");
    testSubject.getPrincipals().add(p);
    p.putCustomProperty(UserPrincipal.LANGUAGE_PROPERTY, "en");
    Group rolesGroup = new SimpleGroup(SecurityHelper.ROLES_GROUP_NAME);
    rolesGroup.addMember(new SimplePrincipal("administrator"));
    testSubject.getPrincipals().add(rolesGroup);
    return testSubject;
  }

  /**
   * Stops the controller.
   *
   * @throws Exception
   *     the exception
   */
  @After
  public void tearDown() throws Exception {
    getBackendController().cleanupRequestResources();
    getBackendController().stop();
  }

  /**
   * Returns the "hrsample-remote-context" value.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected String getApplicationContextKey() {
    return "hrsample-backend-context";
  }

  /**
   * Returns "org/jspresso/hrsample/beanRefFactory.xml".
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected String getBeanFactorySelector() {
    return "org/jspresso/hrsample/beanRefFactory.xml";
  }
}
