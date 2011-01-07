/*
 * Copyright (c) 2005-2011 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.hrsample.startup.wings;

import org.jspresso.framework.application.startup.wings.WingsStartup;

/**
 * Wings HR sample startup class.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class WingsApplicationStartup extends WingsStartup {

  /**
   * Returns the "hrsample-wings-context" value.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected String getApplicationContextKey() {
    return "hrsample-wings-context";
  }
}
