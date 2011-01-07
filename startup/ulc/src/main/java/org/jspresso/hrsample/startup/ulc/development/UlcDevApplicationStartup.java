/*
 * Copyright (c) 2005-2011 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.hrsample.startup.ulc.development;

import org.jspresso.hrsample.development.TestDataPersister;
import org.jspresso.hrsample.startup.ulc.UlcApplicationStartup;

/**
 * ULC development HR sample startup class.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class UlcDevApplicationStartup extends UlcApplicationStartup {

  /**
   * Sets up some test data before actually starting.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void start() {
    new TestDataPersister(getApplicationContext()).persistTestData();
    super.start();
  }
}
