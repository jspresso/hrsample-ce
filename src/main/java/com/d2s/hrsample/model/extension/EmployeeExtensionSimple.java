/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.hrsample.model.extension;

import com.d2s.framework.model.component.AbstractComponentExtension;
import com.d2s.hrsample.model.Employee;

/**
 * Helper class computing extended properties for Employee entity.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
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
