/*
 * Copyright (c) 2005-2012 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.hrsample.model.extension;

import org.jspresso.framework.model.component.AbstractComponentExtension;

import org.jspresso.hrsample.model.Employee;

/**
 * Helper class computing extended properties for Employee entity.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class EmployeeExtensionSimple extends
    AbstractComponentExtension<Employee> {

  /**
   * Constructs a new <code>EmployeeExtension</code> instance.
   * 
   * @param extendedEmployee
   *            The extended Employee instance.
   */
  public EmployeeExtensionSimple(Employee extendedEmployee) {
    super(extendedEmployee);
  }

  /**
   * Computes the employee age.
   * 
   * @return The employee age.
   */
  public Integer getAge() {
    return getComponent().computeAge(getComponent().getBirthDate());
  }
}
