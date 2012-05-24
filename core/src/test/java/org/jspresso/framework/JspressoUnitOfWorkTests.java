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

import static org.junit.Assert.assertEquals;

import java.util.Locale;
import java.util.TimeZone;

import org.jspresso.framework.application.startup.AbstractBackendStartup;
import org.jspresso.hrsample.development.TestDataPersister;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Unit Of Work management integration tests.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class JspressoUnitOfWorkTests extends AbstractBackendStartup {

  /**
   * Constructs a new <code>JspressoUnitOfWorkTests</code> instance.
   */
  public JspressoUnitOfWorkTests() {
    setClientTimeZone(TimeZone.getDefault());
    setStartupLocale(Locale.ENGLISH);
    new TestDataPersister(getApplicationContext()).persistTestData();
  }

  /**
   * TODO Comment needed.
   * 
   * @throws java.lang.Exception
   */
  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  /**
   * TODO Comment needed.
   * 
   * @throws java.lang.Exception
   */
  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  /**
   * TODO Comment needed.
   * 
   * @throws java.lang.Exception
   */
  @Before
  public void setUp() throws Exception {
    start();
    configureApplicationSession(createSubject("test"), Locale.ENGLISH);
  }

  /**
   * TODO Comment needed.
   * 
   * @throws java.lang.Exception
   */
  @After
  public void tearDown() throws Exception {
    getBackendController().cleanupRequestResources();
    getBackendController().stop();
  }

  @Test
  public void test1() {
    System.out.println(getBackendController());
    assertEquals(1, 1);
  }

  @Test
  public void test2() {
    System.out.println(getBackendController());
    assertEquals(1, 1);
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
