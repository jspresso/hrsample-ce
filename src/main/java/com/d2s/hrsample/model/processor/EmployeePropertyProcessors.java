/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.hrsample.model.processor;

import java.util.Date;

import com.d2s.framework.util.bean.integrity.EmptyPropertyProcessor;
import com.d2s.framework.util.bean.integrity.IntegrityException;
import com.d2s.hrsample.model.Employee;

/**
 * Employee property processors.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class EmployeePropertyProcessors {

  /**
   * Birth date property processor.
   * <p>
   * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
   * <p>
   * 
   * @version $LastChangedRevision$
   * @author Vincent Vandenschrick
   */
  public static class BirthDateProcessor extends
      EmptyPropertyProcessor<Employee, Date> {

    /**
     * Checks that the employee age is at least 18.
     * <p>
     * {@inheritDoc}
     */
    @Override
    public void preprocessSetter(Employee employee,
        @SuppressWarnings("unused")
        Date oldBirthDate, Date newBirthDate) {
      if (newBirthDate == null
          || employee.computeAge(newBirthDate).intValue() < 18) {
        throw new IntegrityException("Age is below 18", "age.below.18");
      }
    }
  }

  /**
   * First name property processor.
   * <p>
   * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
   * <p>
   * 
   * @version $LastChangedRevision$
   * @author Vincent Vandenschrick
   */
  public static class FirstNameProcessor extends
      EmptyPropertyProcessor<Employee, String> {

    /**
     * Formats the new first name. The formatting is :
     * <li>Capitalize the 1st letter
     * <li>Lower case all the other letters
     * <p>
     * {@inheritDoc}
     */
    @Override
    public String interceptSetter(Employee employee,
        @SuppressWarnings("unused")
        String oldFirstName, String newFirstName) {
      if (newFirstName != null && newFirstName.length() > 0) {
        StringBuffer formattedName = new StringBuffer();
        formattedName.append(newFirstName.substring(0, 1).toUpperCase());
        formattedName.append(newFirstName.substring(1).toLowerCase());
        return formattedName.toString();
      }
      return super.interceptSetter(employee, oldFirstName, newFirstName);
    }
  }
}