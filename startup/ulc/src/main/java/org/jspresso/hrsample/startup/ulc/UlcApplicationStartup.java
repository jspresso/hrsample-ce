/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.hrsample.startup.ulc;

import org.jspresso.framework.application.startup.ulc.UlcStartup;

/**
 * ULC HR sample startup class.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class UlcApplicationStartup extends UlcStartup {

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
