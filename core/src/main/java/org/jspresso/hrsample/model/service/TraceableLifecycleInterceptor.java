/*
 * Copyright (c) 2005-2010 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.hrsample.model.service;

import java.util.Date;

import org.jspresso.framework.model.component.service.EmptyLifecycleInterceptor;
import org.jspresso.framework.model.entity.IEntityFactory;
import org.jspresso.framework.model.entity.IEntityLifecycleHandler;
import org.jspresso.framework.security.UserPrincipal;

import org.jspresso.hrsample.model.Traceable;

/**
 * Default lifecycle service for tracing.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class TraceableLifecycleInterceptor extends
    EmptyLifecycleInterceptor<Traceable> {

  /**
   * Sets the create timestamp.
   * <p>
   * {@inheritDoc}
   */
  @Override
  @SuppressWarnings("unused")
  public boolean onPersist(Traceable traceable, IEntityFactory entityFactory,
      UserPrincipal principal, IEntityLifecycleHandler entityLifecycleHandler) {
    traceable.setCreateTimestamp(new Date());
    return true;
  }

  /**
   * Sets the last update timestamp.
   * <p>
   * {@inheritDoc}
   */
  @Override
  @SuppressWarnings("unused")
  public boolean onUpdate(Traceable traceable, IEntityFactory entityFactory,
      UserPrincipal principal, IEntityLifecycleHandler entityLifecycleHandler) {
    traceable.setLastUpdateTimestamp(new Date());
    return true;
  }
}
