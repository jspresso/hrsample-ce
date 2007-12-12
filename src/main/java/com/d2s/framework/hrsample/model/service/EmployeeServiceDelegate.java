/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.hrsample.model.service;

import java.util.Date;

import com.d2s.framework.hrsample.model.Employee;
import com.d2s.framework.model.component.service.IComponentService;

/**
 * The services delegate of the Employee entity
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class EmployeeServiceDelegate implements IComponentService {

  /**
   * Computes the employee age.
   * 
   * @param employee
   *            the employee this service execution has been triggered on.
   * @param birthDate
   *            a birth date (might be different than the actual employee birth
   *            date).
   * @return the age computed from the birth date passed as parameter.
   */
  public Integer computeAge(Employee employee, Date birthDate) {
    if (birthDate != null) {
      return new Integer(
          (int) ((new Date().getTime() - birthDate.getTime()) / (1000L * 60 * 60 * 24 * 365)));
    }
    return null;
  }
}
