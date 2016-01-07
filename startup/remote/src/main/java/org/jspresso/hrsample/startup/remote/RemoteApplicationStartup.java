/*
 * Copyright (c) 2005-2013 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.hrsample.startup.remote;

import org.jspresso.framework.application.startup.remote.RemoteStartup;

/**
 * Remote HR sample startup class.
 * 
 * @author Vincent Vandenschrick
 */
public class RemoteApplicationStartup extends RemoteStartup {

  /**
   * Returns the "hrsample-remote-context" value.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected String getApplicationContextKey() {
    return "hrsample-remote-context";
  }

  /**
   * Returns "org/jspresso/hrsample/beanRefFactory.xml".
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected String getBeanFactorySelector() {
    return "org/jspresso/hrsample/beanRefFactory.xml";
  }
}
