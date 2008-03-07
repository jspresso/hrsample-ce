/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.hrsample.startup;

import com.d2s.framework.application.startup.swing.SwingStartup;

/**
 * Swing HR sample startup class.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class SwingHrSampleStartup extends SwingStartup {

  /**
   * Returns the "hrsample-swing-context" value.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected String getApplicationContextKey() {
    return "hrsample-swing-context";
  }
}
