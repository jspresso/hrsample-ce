/*
 * Copyright (c) 2005-2013 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.hrsample.model.extension;

import org.jspresso.hrsample.model.Employee;

/**
 * Helper class computing extended properties for Employee entity.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class EmployeeExtension extends EmployeeExtensionSimple {

  /**
   * Constructs a new <code>EmployeeExtension</code> instance.
   * 
   * @param extendedEmployee
   *          The extended Employee instance.
   */
  public EmployeeExtension(Employee extendedEmployee) {
    super(extendedEmployee);
  }

  @Override
  public void postCreate() {
    Employee extendedEmployee = getComponent();
    registerNotificationForwarding(extendedEmployee, Employee.BIRTH_DATE,
        Employee.AGE);
    registerNotificationForwarding(extendedEmployee, Employee.FIRST_NAME,
        Employee.FULL_NAME);
    registerNotificationForwarding(extendedEmployee, Employee.NAME,
        Employee.FULL_NAME);
    registerNotificationForwarding(extendedEmployee, Employee.FULL_NAME, Employee.HTML_DESCRIPTION);
    registerNotificationForwarding(extendedEmployee, Employee.GENDER, Employee.GENDER_IMAGE_URL);
    // Breaks test on computed property changes
    // registerNotificationForwarding(extendedEmployee, Employee.AGE, Employee.HTML_DESCRIPTION);
  }
}
