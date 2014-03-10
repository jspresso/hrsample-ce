/*
 * Copyright (c) 2005-2013 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.hrsample.startup.swing.development;

import javax.swing.SwingUtilities;

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
//    SwingUtilities.invokeLater(new Runnable() {
//      @Override
//      public void run() {
//        org.hsqldb.util.DatabaseManagerSwing.main(new String[0]);
//      }
//    });
  }
}
