/*
 * Copyright (c) 2005-2012 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.hrsample.model.service;

import java.util.Date;

import org.jspresso.framework.model.component.service.IComponentService;
import org.jspresso.framework.util.i18n.ITranslationProvider;
import org.jspresso.hrsample.model.Employee;

/**
 * The services delegate of the Employee entity.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class EmployeeServiceDelegate implements IComponentService {

  private ITranslationProvider translationProvider;

  /**
   * Computes the employee age.
   * 
   * @param employee
   *          the employee this service execution has been triggered on.
   * @param birthDate
   *          a birth date (might be different than the actual employee birth
   *          date).
   * @return the age computed from the birth date passed as parameter.
   */
  public Integer computeAge(Employee employee, Date birthDate) {
    if (birthDate != null) {
      return new Integer(
          (int) ((new Date().getTime() - birthDate.getTime()) / (1000L * 60 * 60 * 24 * 365)));
    }
    return null;
  }

  /**
   * Gets the translationProvider.
   * 
   * @param employee
   *          the employee this service execution has been triggered on.
   * @return the translationProvider.
   */
  public ITranslationProvider getTranslationProvider(Employee employee) {
    return translationProvider;
  }

  /**
   * Sets the translationProvider on this extension instance.
   * 
   * @param translationProvider
   *          the translationProvider to set.
   */
  public void setTranslationProvider(ITranslationProvider translationProvider) {
    this.translationProvider = translationProvider;
  }
}
