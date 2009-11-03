/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.hrsample.model.extension;

import org.jspresso.framework.model.component.AbstractComponentExtension;

import org.jspresso.hrsample.model.Company;
import org.jspresso.hrsample.model.Department;
import org.jspresso.hrsample.model.OrganizationalUnit;
import org.jspresso.hrsample.model.Team;

/**
 * Helper class computing extended properties for OrganizationalUnit entity.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class OrganizationalUnitExtension extends
    AbstractComponentExtension<OrganizationalUnit> {

  /**
   * Constructs a new <code>OrganizationalUnitExtension</code> instance.
   * 
   * @param organizationalUnit
   *            The extended OrganizationalUnit instance.
   */
  public OrganizationalUnitExtension(OrganizationalUnit organizationalUnit) {
    super(organizationalUnit);
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
}
