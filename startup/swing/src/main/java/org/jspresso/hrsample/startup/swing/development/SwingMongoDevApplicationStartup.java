/*
 * Copyright (c) 2005-2014 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.hrsample.startup.swing.development;

import org.jspresso.hrsample.development.MongoTestDataPersister;
import org.jspresso.hrsample.startup.swing.SwingMongoApplicationStartup;

/**
 * Swing development HR sample startup class.
 * 
 * @author Vincent Vandenschrick
 */
public class SwingMongoDevApplicationStartup extends SwingMongoApplicationStartup {

  /**
   * Sets up some test data before actually starting.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void start() {
    new MongoTestDataPersister(getApplicationContext()).persistTestData();
    super.start();
//    SwingUtilities.invokeLater(new Runnable() {
//      @Override
//      public void run() {
//        org.hsqldb.util.DatabaseManagerSwing.main(new String[0]);
//      }
//    });
  }
}
