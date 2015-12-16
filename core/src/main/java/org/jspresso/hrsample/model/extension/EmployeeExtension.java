/*
 * Copyright (c) 2005-2013 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.hrsample.model.extension;

import org.jspresso.framework.model.component.AbstractComponentExtension;
import org.jspresso.framework.model.component.service.DependsOn;
import org.jspresso.framework.model.descriptor.IEnumerationPropertyDescriptor;

import org.jspresso.hrsample.model.Employee;

/**
 * Helper class computing extended properties for Employee entity.
 * 
 * @author Vincent Vandenschrick
 */
public class EmployeeExtension extends AbstractComponentExtension<Employee> {

  /**
   * Constructs a new {@code EmployeeExtension} instance.
   * 
   * @param extendedEmployee
   *          The extended Employee instance.
   */
  public EmployeeExtension(Employee extendedEmployee) {
    super(extendedEmployee);
  }


  /**
   * Computes the employee age.
   *
   * @return The employee age.
   */
  @DependsOn(Employee.BIRTH_DATE)
  public Integer getAge() {
    return getComponent().computeAge(getComponent().getBirthDate());
  }

  /**
   * Returns the gender image url.
   *
   * @return the gender image url.
   */
  @DependsOn(Employee.GENDER)
  public String getGenderImageUrl() {
    IEnumerationPropertyDescriptor genderDescriptor = (IEnumerationPropertyDescriptor) getComponentFactory()
        .getComponentDescriptor(Employee.class).getPropertyDescriptor("gender");
    return genderDescriptor.getIconImageURL(getComponent().getGender());
  }

  /**
   * Computes the concatenation of last name and first name.
   *
   * @return the concatenation of last name and first name.
   */
  @DependsOn({Employee.FIRST_NAME, Employee.NAME })
  public String getFullName() {
    StringBuilder buff = new StringBuilder();
    if (getComponent().getName() != null) {
      buff.append(getComponent().getName());
      if (getComponent().getFirstName() != null) {
        buff.append(" ");
      }
    }
    if (getComponent().getFirstName() != null) {
      buff.append(getComponent().getFirstName());
    }
    return buff.toString();
  }

  /**
   * Computes the HTML description of an employee.
   *
   * @return the HTML description of an employee.
   */
  @DependsOn(Employee.FULL_NAME)
  public String getHtmlDescription() {
    return "<html><b><i>" + getComponent().getFullName() + "</i></b><br>" + "  Age: " + getComponent().getAge() + "</html>";

  }
}
