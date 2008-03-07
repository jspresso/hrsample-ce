/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.hrsample.startup.development;

import org.springframework.beans.factory.BeanFactory;

import com.d2s.framework.application.backend.startup.development.AbstractTestDataPersister;
import com.d2s.hrsample.model.City;

/**
 * Persists some test data for the HR sample application.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class HrTestDataPersister extends AbstractTestDataPersister {

  /**
   * Constructs a new <code>HrTestDataPersister</code> instance.
   * 
   * @param beanFactory
   *            the spring bean factory to use.
   */
  public HrTestDataPersister(BeanFactory beanFactory) {
    super(beanFactory);
  }

  /**
   * Creates some test data using the passed in Spring application context.
   */
  @Override
  public void persistTestData() {
    City paris = createEntityInstance(City.class);
    paris.setName("Paris I");
    paris.setZip("75001");
    saveOrUpdate(paris);
  }

}
