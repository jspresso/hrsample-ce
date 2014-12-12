/*
 * Copyright (c) 2005-2013 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.hrsample.startup.swing.development;

import org.jspresso.hrsample.development.HibernateTestDataPersister;
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
    new HibernateTestDataPersister(getApplicationContext()).persistTestData();
    super.start();
//    SwingUtilities.invokeLater(new Runnable() {
//      @Override
//      public void run() {
//        org.hsqldb.util.DatabaseManagerSwing.main(new String[0]);
//      }
//    });
  }
}
