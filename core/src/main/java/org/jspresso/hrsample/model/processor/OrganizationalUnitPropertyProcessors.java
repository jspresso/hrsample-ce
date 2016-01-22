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
package org.jspresso.hrsample.model.processor;

import org.jspresso.framework.util.bean.integrity.EmptyPropertyProcessor;
import org.jspresso.framework.util.bean.integrity.IntegrityException;
import org.jspresso.hrsample.model.Employee;
import org.jspresso.hrsample.model.OrganizationalUnit;

/**
 * OrganizationalUnit property processors.
 *
 * @author Vincent Vandenschrick
 */
public class OrganizationalUnitPropertyProcessors {

  /**
   * Manager property processor.
   *
     * @author Vincent Vandenschrick
   */
  public static class ManagerProcessor extends
      EmptyPropertyProcessor<OrganizationalUnit, Employee> {

    /**
     * Checks that the manager belongs to the same company as the managed
     * OrganizationalUnit.
     * <p>
     * {@inheritDoc}
     */
    @Override
    public void preprocessSetter(OrganizationalUnit organizationalUnit,
        Employee newManager) {
      if (newManager != null
          && (newManager.getCompany() == null || !newManager.getCompany()
              .equals(organizationalUnit.getCompany()))) {
        throw new IntegrityException(
            "A manager must belong to the same company as its managed organizational unit.",
            "manager.company.invalid");
      }
    }
  }
}
