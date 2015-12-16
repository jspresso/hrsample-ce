/*
 * Copyright (c) 2005-2013 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.hrsample.startup.swing;

import org.jspresso.framework.application.startup.swing.SwingStartup;

/**
 * Swing HR sample startup class.
 * 
 * @author Vincent Vandenschrick
 */
public class SwingApplicationStartup extends SwingStartup {

  /**
   * Returns the "hrsample-swing-context" value.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected String getApplicationContextKey() {
    return "hrsample-swing-context";
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
