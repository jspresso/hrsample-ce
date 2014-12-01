/*
 * Copyright (c) 2005-2014 Vincent Vandenschrick. All rights reserved.
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

import static org.junit.Assert.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
import org.junit.Test;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

import org.jspresso.framework.application.backend.persistence.hibernate.HibernateBackendController;
import org.jspresso.framework.application.backend.persistence.hibernate.HibernateHelper;
import org.jspresso.framework.application.backend.session.EMergeMode;
import org.jspresso.framework.model.persistence.hibernate.criterion.EnhancedDetachedCriteria;

import org.jspresso.hrsample.frontend.FrontTestStartup;
import org.jspresso.hrsample.model.Company;
import org.jspresso.hrsample.model.Department;
import org.jspresso.hrsample.model.Employee;
import org.jspresso.hrsample.model.OrganizationalUnit;
import org.jspresso.hrsample.model.Team;

/**
 * Performance tests.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class PerformanceTests extends FrontTestStartup {

  /**
   * Test performance of obtaining controller instance
   */
  @Test
  public void testControllerCreationPerformance() {
    for(int i = 0; i < 200; i++) {
      start();
    }
    long t1 = System.currentTimeMillis();
    start();
    long t2 = System.currentTimeMillis();
    assertTrue("Controller creation took more than 20 ms : " + (t2-t1) + "ms.", (t2-t1) < 20);
  }

}
