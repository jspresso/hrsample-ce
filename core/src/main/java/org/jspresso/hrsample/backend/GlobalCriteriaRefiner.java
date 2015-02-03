/*
 * Copyright (c) 2005-2013 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.hrsample.backend;

import java.util.Map;

import org.jspresso.framework.application.backend.action.persistence.hibernate.ICriteriaRefiner;
import org.jspresso.framework.model.component.IQueryComponent;
import org.jspresso.framework.model.persistence.hibernate.criterion.EnhancedDetachedCriteria;
import org.jspresso.hrsample.model.Employee;

/**
 * Application global criteria refiner.
 * 
 * @author Vincent Vandenschrick
 */
public class GlobalCriteriaRefiner implements ICriteriaRefiner {

  /**
   * Demonstrates how a global criteria refiner could be handled.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void refineCriteria(EnhancedDetachedCriteria criteria,
      IQueryComponent queryComponent, Map<String, Object> context) {
    if (Employee.class.isAssignableFrom(queryComponent.getQueryContract())) {
      // ((QueryComponent) queryComponent).setDistinctEnforced(true);
      // criteria.createCriteria("company", CriteriaSpecification.LEFT_JOIN);
      // criteria.getSubCriteriaFor(criteria, "company", "company");
      // criteria.addOrder(Order.desc("company.name"));
    }
  }

}
