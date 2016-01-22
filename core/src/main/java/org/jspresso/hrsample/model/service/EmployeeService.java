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

import org.jspresso.framework.util.i18n.ITranslationProvider;

/**
 * Services offered by the Employee entity.
 *
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
