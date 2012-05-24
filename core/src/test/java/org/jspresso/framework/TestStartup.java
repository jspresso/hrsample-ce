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
package org.jspresso.framework;

import org.hibernate.criterion.DetachedCriteria;
import org.jboss.util.NestedRuntimeException;
import org.jspresso.framework.application.backend.persistence.hibernate.HibernateBackendController;
import org.jspresso.framework.application.startup.AbstractBackendStartup;
import org.jspresso.framework.model.persistence.hibernate.criterion.EnhancedDetachedCriteria;
import org.jspresso.hrsample.development.TestDataPersister;
import org.jspresso.hrsample.model.City;
import org.jspresso.hrsample.model.Company;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

/**
 * Base class for integration tests.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class TestStartup extends AbstractBackendStartup {

  /**
   * Performs DB initialization and test data creation.
   * 
   * @throws java.lang.Exception
   */
  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    TestStartup startup = new TestStartup();
    new TestDataPersister(startup.getApplicationContext()).persistTestData();
  }

  /**
   * Destroys all data from DB.
   * 
   * @throws java.lang.Exception
   */
  @AfterClass
  public static void tearDownAfterClass() throws Exception {
    TestStartup startup = new TestStartup();
    startup.start();
    final HibernateBackendController bc = (HibernateBackendController) startup
        .getBackendController();
    bc.getTransactionTemplate().execute(new TransactionCallbackWithoutResult() {

      @Override
      protected void doInTransactionWithoutResult(@SuppressWarnings("unused")
      TransactionStatus status) {
        DetachedCriteria crit = EnhancedDetachedCriteria
            .forClass(Company.class);
        for (Company company : bc.findByCriteria(crit, null, Company.class)) {
          try {
            bc.cleanRelationshipsOnDeletion(company, false);
          } catch (Exception ex) {
            throw new NestedRuntimeException(ex);
          }
        }
        bc.getHibernateSession().flush();
        bc.getHibernateSession()
            .createQuery("delete from " + City.class.getName()).executeUpdate();
      }
    });
  }

  /**
   * Starts a new controller and creates the session using the "test" user with
   * english locale.
   * 
   * @throws java.lang.Exception
   */
  @Before
  public void setUp() throws Exception {
    start();
    configureApplicationSession(createSubject("test"), getStartupLocale());
  }

  /**
   * Stops the controller.
   * 
   * @throws java.lang.Exception
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
