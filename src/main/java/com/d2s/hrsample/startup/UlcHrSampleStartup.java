/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.hrsample.startup;

import org.jspresso.framework.application.startup.ulc.UlcStartup;

/**
 * ULC HR sample startup class.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class UlcHrSampleStartup extends UlcStartup {

  /**
   * Returns the "hrsample-ulc-context" value.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected String getApplicationContextKey() {
    return "hrsample-ulc-context";
  }
}
