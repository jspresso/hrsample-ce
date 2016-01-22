/*
 * Copyright (c) 2005-2016 Vincent Vandenschrick. All rights reserved.
 *
 *  This file is part of the Jspresso framework.
 *
 *  Jspresso is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Jspresso is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with Jspresso.  If not, see <http://www.gnu.org/licenses/>.
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
 * @author Vincent Vandenschrick
 */
public class EmployeeLifecycleInterceptor extends
    EmptyLifecycleInterceptor<Employee> {

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean onPersist(Employee employee, IEntityFactory entityFactory,
      UserPrincipal principal, IEntityLifecycleHandler entityLifecycleHandler) {
    Event event = entityFactory.createEntityInstance(Event.class);
    event.setText("<html>Employee <b>" + employee.getName()
        + "</b> has been created.</html>");
    employee.addToEvents(event);
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean onUpdate(Employee employee, IEntityFactory entityFactory,
      UserPrincipal principal, IEntityLifecycleHandler entityLifecycleHandler) {
    // List<Event> events = employee.getEvents();
    // if (!events.isEmpty()) {
    // Event delEvent = events.get(0);
    // employee.removeFromEvents(delEvent);
    // entityLifecycleHandler.registerForDeletion(delEvent);
    // }
    Event event = entityFactory.createEntityInstance(Event.class);
    event.setText("<html>Employee <b>" + employee.getName()
        + "</b> has been updated.</html>");
    employee.addToEvents(event);

    return true;
  }
}
