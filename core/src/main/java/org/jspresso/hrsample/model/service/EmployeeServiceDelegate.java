/*
 * Copyright (c) 2005-2013 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.hrsample.model.service;

import java.util.Date;

import org.jspresso.framework.model.component.service.AbstractComponentServiceDelegate;
import org.jspresso.framework.util.i18n.ITranslationProvider;
import org.jspresso.hrsample.model.Employee;

/**
 * The services delegate of the Employee entity.
 * 
 * @author Vincent Vandenschrick
 */
public class EmployeeServiceDelegate extends
    AbstractComponentServiceDelegate<Employee> implements EmployeeService {

  private ITranslationProvider translationProvider;

  /**
   * Computes the employee age.
   * 
   * @param birthDate
   *          a birth date (might be different than the actual employee birth
   *          date).
   * @return the age computed from the birth date passed as parameter.
   */
  @Override
  public Integer computeAge(Date birthDate) {
    if (birthDate != null) {
      return Integer
          .valueOf((int) ((new Date().getTime() - birthDate.getTime()) / (1000L * 60 * 60 * 24 * 365)));
    }
    return null;
  }

  /**
   * Gets the translationProvider.
   * 
   * @return the translationProvider.
   */
  @Override
  public ITranslationProvider getTranslationProvider() {
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
