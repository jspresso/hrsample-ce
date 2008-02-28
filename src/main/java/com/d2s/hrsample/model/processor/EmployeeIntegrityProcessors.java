/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.hrsample.model.processor;

import java.util.Date;

import com.d2s.framework.util.bean.integrity.EmptyPropertyIntegrityProcessor;
import com.d2s.framework.util.bean.integrity.IntegrityException;
import com.d2s.hrsample.model.Employee;

/**
 * Employee integrity processors.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class EmployeeIntegrityProcessors {

  /**
   * Birth date integrity processor.
   * <p>
   * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
   * <p>
   * 
   * @version $LastChangedRevision$
   * @author Vincent Vandenschrick
   */
  public static class BirthDateIntegrityProcessor extends
      EmptyPropertyIntegrityProcessor<Employee, Date> {

    /**
     * Checks that the employee age is at least 18.
     * <p>
     * {@inheritDoc}
     */
    @Override
    public void preprocessSetterIntegrity(Employee employee,
        @SuppressWarnings("unused")
        Date oldBirthDate, Date newBirthDate) {
      if (newBirthDate == null
          || employee.computeAge(newBirthDate).intValue() < 18) {
        throw new IntegrityException("Age is below 18", "age.below.18");
      }
    }
  }

  /**
   * First name integrity processor.
   * <p>
   * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
   * <p>
   * 
   * @version $LastChangedRevision$
   * @author Vincent Vandenschrick
   */
  public static class FirstNameIntegrityProcessor extends
      EmptyPropertyIntegrityProcessor<Employee, String> {

    /**
     * Formats the new first name. The formatting is :
     * <li>Capitalize the 1st letter
     * <li>Lower case all the other letters
     * <p>
     * {@inheritDoc}
     */
    @Override
    public void postprocessSetterIntegrity(Employee employee,
        @SuppressWarnings("unused")
        String oldFirstName, String newFirstName) {
      if (newFirstName != null && newFirstName.length() > 0) {
        StringBuffer formattedName = new StringBuffer();
        formattedName.append(newFirstName.substring(0, 1).toUpperCase());
        formattedName.append(newFirstName.substring(1).toLowerCase());
        employee.setFirstName(formattedName.toString());
      }
    }
  }
}
