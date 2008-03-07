/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.hrsample.startup.development;

import com.d2s.hrsample.startup.UlcHrSampleStartup;

/**
 * ULC development HR sample startup class.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class UlcDevHrSampleStartup extends UlcHrSampleStartup {

  /**
   * Sets up some test data before actually starting.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void start() {
    new HrTestDataPersister(getApplicationContext()).persistTestData();
    super.start();
  }
}
