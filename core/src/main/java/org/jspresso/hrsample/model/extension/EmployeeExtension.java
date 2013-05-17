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
    registerNotificationForwarding(extendedEmployee, Employee.BIRTH_DATE,
        Employee.AGE);
  }
}
