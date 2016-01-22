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
package org.jspresso.hrsample.backend;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;

import org.jspresso.framework.application.backend.IBackendController;
import org.jspresso.hrsample.model.Employee;
import org.junit.Test;

/**
 * Employee tests
 *
 * @author Maxime Hamm
 */
public class EmployeeTest extends BackTestStartup {

  /**
   * Test the computed field age calculation
   */
  @Test
  public void testComputedAge() {
    final IBackendController controller = getBackendController();
    final Calendar cal = Calendar.getInstance();

    // check age is null if birth date is not set
    final Employee employee = controller.getEntityFactory().createEntityInstance(Employee.class);
    assertEquals(null, employee.getAge());

    // check age is correctly calculated if the birth date is set
    cal.add(Calendar.YEAR, - 28);
    employee.setBirthDate(cal.getTime());
    assertEquals(28, (long)employee.getAge());

    // check age is correctly update if birth date is modified
    cal.add(Calendar.YEAR, - 2);
    employee.setBirthDate(cal.getTime());
    assertEquals(30, (long)employee.getAge());

  }


}
