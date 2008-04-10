/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.hrsample.model.extension;

import com.d2s.framework.model.component.AbstractComponentExtension;
import com.d2s.hrsample.model.Company;
import com.d2s.hrsample.model.Department;
import com.d2s.hrsample.model.OrganizationalUnit;
import com.d2s.hrsample.model.Team;

/**
 * Helper class computing extended properties for OrganizationalUnit entity.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
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