/*
 * Copyright (c) 2005-2011 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.hrsample.model.extension;

import java.util.LinkedHashSet;
import java.util.Set;

import org.jspresso.framework.model.component.AbstractComponentExtension;
import org.jspresso.hrsample.model.Department;
import org.jspresso.hrsample.model.Employee;
import org.jspresso.hrsample.model.Team;

/**
 * Helper class computing extended properties for Department entity.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class DepartmentExtension extends AbstractComponentExtension<Department> {

  /**
   * Constructs a new <code>DepartmentExtension</code> instance.
   * 
   * @param department
   *          The extended Department instance.
   */
  public DepartmentExtension(Department department) {
    super(department);
  }

  /**
   * Computes Employees that belong to this department.
   * 
   * @return the company this organizational unit is attached to. If the
   *         organizational unit is a department, returns the departments
   *         company; if this organizational unit is a team, then we must
   *         navigate to the enclosing department to get its team.
   */
  public Set<Employee> getEmployees() {
    Set<Employee> employees = new LinkedHashSet<Employee>();
    Set<Team> teams = getComponent().getTeams();
    if (teams != null) {
      for (Team team : teams) {
        Set<Employee> teamMembers = team.getTeamMembers();
        if (team.getTeamMembers() != null) {
          employees.addAll(teamMembers);
        }
      }
    }
    return employees;
  }
}
