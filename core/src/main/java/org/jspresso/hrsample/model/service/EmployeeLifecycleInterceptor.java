/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.hrsample.model.service;

import org.jspresso.framework.model.component.service.EmptyLifecycleInterceptor;
import org.jspresso.framework.model.entity.IEntityFactory;
import org.jspresso.framework.model.entity.IEntityLifecycleHandler;
import org.jspresso.framework.security.UserPrincipal;
import org.jspresso.hrsample.model.Employee;
import org.jspresso.hrsample.model.Event;

/**
 * Default lifecycle service for employee.
 * 
 * @version $LastChangedRevision: 2097 $
 * @author Vincent Vandenschrick
 */
public class EmployeeLifecycleInterceptor extends
    EmptyLifecycleInterceptor<Employee> {

  /**
   * {@inheritDoc}
   */
  @Override
  @SuppressWarnings("unused")
  public boolean onPersist(Employee employee, IEntityFactory entityFactory,
      UserPrincipal principal, IEntityLifecycleHandler entityLifecycleHandler) {
    Event event = entityFactory.createEntityInstance(Event.class);
    event.setText("Employee " + employee.getName() + " has been updated.");
    employee.addToEvents(event);
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @SuppressWarnings("unused")
  public boolean onUpdate(Employee employee, IEntityFactory entityFactory,
      UserPrincipal principal, IEntityLifecycleHandler entityLifecycleHandler) {
    Event event = entityFactory.createEntityInstance(Event.class);
    event.setText("Employee " + employee.getName() + " has been updated.");
    employee.addToEvents(event);
    return true;
  }
}