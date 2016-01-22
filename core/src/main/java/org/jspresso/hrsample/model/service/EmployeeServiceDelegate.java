/*
 * Copyright (c) 2005-2016 Vincent Vandenschrick. All rights reserved.
 *
 *  This file is part of the Jspresso framework.
 *
 *  Jspresso is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Jspresso is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with Jspresso.  If not, see <http://www.gnu.org/licenses/>.
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
