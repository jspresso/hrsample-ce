/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.hrsample.startup;

import com.d2s.framework.application.frontend.startup.swing.SwingStartup;

/**
 * Swing HR sample startup class.
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class SwingHrSampleStartup extends SwingStartup {

  /**
   * Returns the "sample-swing-context" value.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected String getApplicationContextKey() {
    return "hrsample-swing-context";
  }
}
