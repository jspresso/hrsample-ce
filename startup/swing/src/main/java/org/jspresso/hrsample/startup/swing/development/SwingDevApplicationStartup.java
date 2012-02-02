/*
 * Copyright (c) 2005-2012 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.hrsample.startup.swing.development;

import org.jspresso.hrsample.development.TestDataPersister;
import org.jspresso.hrsample.startup.swing.SwingApplicationStartup;

/**
 * Swing development HR sample startup class.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class SwingDevApplicationStartup extends SwingApplicationStartup {

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
