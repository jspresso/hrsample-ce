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

import java.util.LinkedHashSet;
import java.util.Set;

import org.hibernate.Hibernate;
import org.jspresso.framework.model.component.AbstractComponentExtension;
import org.jspresso.hrsample.model.Department;
import org.jspresso.hrsample.model.Employee;
import org.jspresso.hrsample.model.Team;

/**
 * Helper class computing extended properties for Department entity.
 *
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

  /**
   * Retrieves the department team count.
   *
   * @return the department team count, either by looking into the entity
   *         properties that have been hydrated by the SQL formula or counting
   *         the teams in the set.
   */
  public Integer getTeamCount() {
    // to avoid initialization of optimized proxy.
    Set<Team> teams = (Set<Team>) getComponent().straightGetProperty(
        Department.TEAMS);
    if (teams != null && Hibernate.isInitialized(teams)) {
      return Integer.valueOf(teams.size());
    }
    return (Integer) getComponent().straightGetProperty(Department.TEAM_COUNT);
  }
}
