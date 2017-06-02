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
package org.jspresso.hrsample.model.extension;

import org.jspresso.framework.model.component.AbstractComponentExtension;
import org.jspresso.hrsample.model.Company;
import org.jspresso.hrsample.model.Department;
import org.jspresso.hrsample.model.IOrganizationalUnitExtension;
import org.jspresso.hrsample.model.Nameable;
import org.jspresso.hrsample.model.OrganizationalUnit;
import org.jspresso.hrsample.model.Team;

/**
 * Helper class computing extended properties for OrganizationalUnit entity.
 *
 * @author Vincent Vandenschrick
 */
public class OrganizationalUnitExtension extends
    AbstractComponentExtension<OrganizationalUnit> implements IOrganizationalUnitExtension {

  /**
   * Constructs a new <code>OrganizationalUnitExtension</code> instance.
   *
   * @param organizationalUnit
   *          The extended OrganizationalUnit instance.
   */
  public OrganizationalUnitExtension(OrganizationalUnit organizationalUnit) {
    super(organizationalUnit);
    registerNotificationForwarding(organizationalUnit,
        OrganizationalUnit.OU_ID, OrganizationalUnit.HTML_DESCRIPTION);
    registerNotificationForwarding(organizationalUnit, Nameable.NAME,
        OrganizationalUnit.HTML_DESCRIPTION);
  }

  /**
   * Computes the company this organizational unit is attached to.
   *
   * @return the company this organizational unit is attached to. If the
   *         organizational unit is a department, returns the departments
   *         company; if this organizational unit is a team, then we must
   *         navigate to the enclosing department to get its team.
   */
  public Company getCompany() {
    if (getComponent() instanceof Team
        && ((Team) getComponent()).getDepartment() != null) {
      return ((Team) getComponent()).getDepartment().getCompany();
    } else if (getComponent() instanceof Department) {
      return ((Department) getComponent()).getCompany();
    }
    return null;
  }

  /**
   * Computes the HTML representation of an organisational unit.
   *
   * @return the HTML representation of an organisational unit.
   */
  public String getHtmlDescription() {
    return "<html><b><i>" + ((Nameable) getComponent()).getName() + "</i></b><br>"
        + getComponent().getOuId() + "</html>";
  }
}
