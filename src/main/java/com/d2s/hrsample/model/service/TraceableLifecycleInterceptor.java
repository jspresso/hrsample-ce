/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.hrsample.model.service;

import java.util.Date;

import com.d2s.framework.model.component.service.EmptyLifecycleInterceptor;
import com.d2s.framework.model.entity.IEntityFactory;
import com.d2s.framework.model.entity.IEntityLifecycleHandler;
import com.d2s.framework.security.UserPrincipal;
import com.d2s.hrsample.model.Traceable;

/**
 * Default lifecycle service for tracing.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
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
