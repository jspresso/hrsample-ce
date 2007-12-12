/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.hrsample.model.extension;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.d2s.framework.hrsample.model.Employee;
import com.d2s.framework.model.component.AbstractComponentExtension;
import com.d2s.framework.util.bean.IPropertyChangeCapable;

/**
 * Helper class computing extended properties for Employee entity.
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class EmployeeExtension extends AbstractComponentExtension<Employee> {

  private Integer age = null;

  /**
   * Constructs a new <code>EmployeeExtension</code> instance.
   * 
   * @param extendedEmployee
   *            The extended Employee instance.
   */
  public EmployeeExtension(Employee extendedEmployee) {
    super(extendedEmployee);
    extendedEmployee.addPropertyChangeListener("birthDate",
        new PropertyChangeListener() {

          public void propertyChange(@SuppressWarnings("unused")
          PropertyChangeEvent evt) {
            Integer oldAge = age;
            age = null;
            getComponent().firePropertyChange("age", oldAge,
                IPropertyChangeCapable.UNKNOWN);
          }
        });
  }

  /**
   * Computes the employee age.
   * 
   * @return The employee age.
   */
  public Integer getAge() {
    if (age != null) {
      return age;
    }
    age = getComponent().computeAge(getComponent().getBirthDate());
    return age;
  }
}
