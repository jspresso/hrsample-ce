/*
 * Copyright (c) 2005-2012 Vincent Vandenschrick. All rights reserved.
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

  private Integer age = null;

  /**
   * Constructs a new <code>EmployeeExtension</code> instance.
   * 
   * @param extendedEmployee
   *          The extended Employee instance.
   */
  public EmployeeExtension(Employee extendedEmployee) {
    super(extendedEmployee);
//    extendedEmployee.addPropertyChangeListener("birthDate",
//        new PropertyChangeListener() {
//
//          @Override
//          public void propertyChange(
//               PropertyChangeEvent evt) {
//            Integer oldAge = age;
//            age = null;
//            getComponent().firePropertyChange("age", oldAge,
//                IPropertyChangeCapable.UNKNOWN);
//          }
//        });
    registerNotificationForwarding(extendedEmployee, Employee.BIRTH_DATE, Employee.AGE);
  }

  /**
   * Computes the employee age.
   * 
   * @return The employee age.
   */
  @Override
  public Integer getAge() {
    if (age != null) {
      return age;
    }
    age = super.getAge();
    return age;
  }
}
