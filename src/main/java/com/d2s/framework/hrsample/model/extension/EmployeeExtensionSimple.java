/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.hrsample.model.extension;

import java.util.Date;

import com.d2s.framework.hrsample.model.Employee;
import com.d2s.framework.model.component.AbstractComponentExtension;

/**
 * Helper class computing extended properties for Employee entity.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
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
    if (getComponent().getBirthDate() != null) {
      return new Integer((int) ((new Date().getTime() - getComponent()
          .getBirthDate().getTime()) / (1000L * 60 * 60 * 24 * 365)));
    }
    return null;
  }
}
