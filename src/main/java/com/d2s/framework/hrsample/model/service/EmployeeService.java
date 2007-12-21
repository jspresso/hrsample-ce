/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.hrsample.model.service;

import java.util.Date;

/**
 * Services offered by the Employee entity.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface EmployeeService {

  /**
   * Computes the employee age.
   * 
   * @param birthDate
   *            the employee birth date.
   * @return the computed age based on the birth date or null if the birt date
   *         is not available.
   */
  Integer computeAge(Date birthDate);
}
