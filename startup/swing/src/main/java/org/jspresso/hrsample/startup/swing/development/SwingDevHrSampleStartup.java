/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.hrsample.startup.swing.development;

import org.jspresso.hrsample.development.HrTestDataPersister;
import org.jspresso.hrsample.startup.swing.SwingHrSampleStartup;

/**
 * Swing development HR sample startup class.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class SwingDevHrSampleStartup extends SwingHrSampleStartup {

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
