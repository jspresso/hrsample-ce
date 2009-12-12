/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.hrsample.model.extension;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.jspresso.framework.model.component.AbstractComponentExtension;
import org.jspresso.framework.model.descriptor.IEnumerationPropertyDescriptor;
import org.jspresso.framework.util.bean.IPropertyChangeCapable;
import org.jspresso.hrsample.model.Employee;

/**
 * Helper class computing extended properties for Employee entity.
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
   *          The extended Employee instance.
   */
  public EmployeeExtension(Employee extendedEmployee) {
    super(extendedEmployee);
    extendedEmployee.addPropertyChangeListener("birthDate",
        new PropertyChangeListener() {

          public void propertyChange(
              @SuppressWarnings("unused") PropertyChangeEvent evt) {
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

  /**
   * Returns the gender image url.
   * 
   * @return the gender image url.
   */
  public String getGenderImageUrl() {
    IEnumerationPropertyDescriptor genderDescriptor = (IEnumerationPropertyDescriptor) getComponentFactory()
        .getComponentDescriptor(Employee.class).getPropertyDescriptor("gender");
    return genderDescriptor.getIconImageURL(getComponent().getGender());
  }
}
