/*
 * Copyright (c) 2005-2012 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.hrsample.model.service;

import java.util.Date;

import org.jspresso.framework.util.i18n.ITranslationProvider;

/**
 * Services offered by the Employee entity.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface EmployeeService {

  /**
   * Computes the employee age.
   * 
   * @param birthDate
   *          the employee birth date.
   * @return the computed age based on the birth date or null if the birth date
   *         is not available.
   */
  Integer computeAge(Date birthDate);

  /**
   * Gets the translation provider injected into its service layer.
   * 
   * @return the translation provider injected into its service layer.
   */
  ITranslationProvider getTranslationProvider();

}
