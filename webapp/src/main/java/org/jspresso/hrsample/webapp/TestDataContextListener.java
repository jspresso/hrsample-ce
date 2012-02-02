/*
 * Copyright (c) 2005-2012 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.hrsample.webapp;

import org.jspresso.framework.application.startup.development.AbstractTestDataContextListener;
import org.jspresso.hrsample.development.TestDataPersister;
import org.springframework.beans.factory.BeanFactory;

/**
 * A simple listener to hook in webapp startup and persist sample data.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class TestDataContextListener extends AbstractTestDataContextListener {

  /**
   * {@inheritDoc}
   */
  @Override
  public void persistTestData(BeanFactory beanFactory) {
    new TestDataPersister(beanFactory).persistTestData();
  }

}
