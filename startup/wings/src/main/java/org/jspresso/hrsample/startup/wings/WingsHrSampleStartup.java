/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.hrsample.startup.wings;

import org.jspresso.framework.application.startup.wings.WingsStartup;

/**
 * Wings HR sample startup class.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class WingsHrSampleStartup extends WingsStartup {

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
