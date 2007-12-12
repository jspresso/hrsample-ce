/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.hrsample.startup;

import com.d2s.framework.application.frontend.startup.ulc.UlcStartup;

/**
 * ULC HR sample startup class.
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class UlcHrSampleStartup extends UlcStartup {

  /**
   * Returns the "sample-ulc-context" value.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected String getApplicationContextKey() {
    return "hrsample-ulc-context";
  }
}
