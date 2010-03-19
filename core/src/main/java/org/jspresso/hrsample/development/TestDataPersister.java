/*
 * Copyright (c) 2005-2010 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.hrsample.development;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.jspresso.framework.application.startup.development.AbstractTestDataPersister;
import org.jspresso.hrsample.model.City;
import org.jspresso.hrsample.model.Company;
import org.jspresso.hrsample.model.Department;
import org.jspresso.hrsample.model.Employee;
import org.jspresso.hrsample.model.Team;
import org.springframework.beans.factory.BeanFactory;

/**
 * Persists some test data for the HR sample application.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class TestDataPersister extends AbstractTestDataPersister {

  /**
   * Constructs a new <code>TestDataPersister</code> instance.
   * 
   * @param beanFactory
   *          the spring bean factory to use.
   */
  public TestDataPersister(BeanFactory beanFactory) {
    super(beanFactory);
  }

  /**
   * Creates some test data using the passed in Spring application context.
   */
  @Override
  public void persistTestData() {

    // Cities
    City paris = createCity("Paris I", "75001");
    City suresnes = createCity("Suresnes", "92150");
    City evry = createCity("Evry", "91000");

    // Company
    Company design2see = createCompany("Design2See",
        "123 avenue de la Liberté", paris, "contact@design2see.com",
        "+33 123 456 000");

    // Employees
    Employee johnDoe = createEmployee("M", "Doe", "John", "doepass",
        "12 allée du Chien qui Fume", evry, "john.doe@design2see.com",
        "+33 1 152 368 984", "02/05/1972", "03/08/2005", "1523698754", true,
        "0xFF449911", "100000", design2see);

    Employee mikeDen = createEmployee("M", "Den", "Mike", "denpass",
        "26 rue de la Pie qui Chante", suresnes, "mike.den@design2see.com",
        "+33 1 968 846 398", "05/07/1970", "01/03/2004", "1859637461", false,
        "0xFFCC1255", "80000", design2see);

    Employee evaGreen = createEmployee("F", "Green", "Eva", null,
        "68 rue de l'Eléphant Vert", suresnes, "eva.green@design2see.com",
        "+33 1 958 536 972", "10/08/1977", "06/04/2002", "2856752387", true,
        "0xFFAA4411", "85000", design2see);

    Employee gloriaSan = createEmployee("F", "San", "Gloria", null,
        "13 avenue du Poisson Enragé", evry, "gloria.san@design2see.com",
        "+33 1 956 367 412", "09/01/1969", "03/01/2006", "2597853274", false,
        "0xFF001276", "75000", design2see);

    Employee mariaTrulli = createEmployee("F", "Trulli", "Maria", null,
        "20 avenue du Crocodile Marteau", evry, "maria.trulli@design2see.com",
        "+33 1 868 745 369", "01/02/1976", "03/10/2006", "2325985423", true,
        "0xFF9489AB", "110000", design2see);

    Employee isabelleMartin = createEmployee("F", "Martin", "Isabelle", null,
        "20 allée de la Gazelle Sauteuse", evry,
        "isabelle.martin@design2see.com", "+33 1 698 256 365", "04/07/1970",
        "12/06/2001", "2652398751", false, "0xFFAA6512", "95000", design2see);

    // Departments and teams.
    Department hrDepartment = createDepartment("Human Resources", "HR-000",
        "124 avenue de la Liberté", paris, "hr@design2see.com",
        "+33 123 456 100", design2see, johnDoe);

    Team hr001Team = createTeam("HR 001 Team", "HR-001",
        "124 avenue de la Liberté", paris, "hr001@design2see.com",
        "+33 123 456 101", hrDepartment, mikeDen);
    hr001Team.addToTeamMembers(johnDoe);
    hr001Team.addToTeamMembers(mikeDen);

    Team hr002Team = createTeam("HR 002 Team", "HR-002",
        "124 avenue de la Liberté", paris, "hr002@design2see.com",
        "+33 123 456 102", hrDepartment, evaGreen);
    hr002Team.addToTeamMembers(johnDoe);
    hr002Team.addToTeamMembers(evaGreen);

    Department itDepartment = createDepartment("Information Technology",
        "IT-000", "125 avenue de la Liberté", paris, "it@design2see.com",
        "+33 123 456 200", design2see, gloriaSan);

    Team it001Team = createTeam("IT 001 Team", "IT-001",
        "125 avenue de la Liberté", paris, "it001@design2see.com",
        "+33 123 456 201", itDepartment, mariaTrulli);
    it001Team.addToTeamMembers(gloriaSan);
    it001Team.addToTeamMembers(mariaTrulli);

    Team it002Team = createTeam("IT 002 Team", "IT-002",
        "125 avenue de la Liberté", paris, "it002@design2see.com",
        "+33 123 456 202", itDepartment, isabelleMartin);
    it002Team.addToTeamMembers(gloriaSan);
    it002Team.addToTeamMembers(isabelleMartin);

    saveOrUpdate(design2see);
  }

  private City createCity(String name, String zip) {
    City city = createEntityInstance(City.class);
    city.setName(name);
    city.setZip(zip);
    saveOrUpdate(city);
    return city;
  }

  private Company createCompany(String name, String address, City city,
      String email, String phone) {
    Company company = createEntityInstance(Company.class);
    company.setName(name);
    company.getContact().setAddress(address);
    company.getContact().setCity(city);
    company.getContact().setEmail(email);
    company.getContact().setPhone(phone);
    return company;
  }

  private Department createDepartment(String name, String ouId, String address,
      City city, String email, String phone, Company company, Employee manager) {
    Department department = createEntityInstance(Department.class);
    department.setName(name);
    department.setOuId(ouId);
    department.getContact().setAddress(address);
    department.getContact().setCity(city);
    department.getContact().setEmail(email);
    department.getContact().setPhone(phone);
    department.setCompany(company);
    department.setManager(manager);
    return department;
  }

  private Team createTeam(String name, String ouId, String address, City city,
      String email, String phone, Department department, Employee manager) {
    Team team = createEntityInstance(Team.class);
    team.setName(name);
    team.setOuId(ouId);
    team.getContact().setAddress(address);
    team.getContact().setCity(city);
    team.getContact().setEmail(email);
    team.getContact().setPhone(phone);
    team.setDepartment(department);
    team.setManager(manager);
    return team;
  }

  private Employee createEmployee(String gender, String name, String firstName,
      String password, String address, City city, String email, String phone,
      String birthDate, String hireDate, String ssn, boolean married,
      String preferredColor, String salary, Company company) {
    SimpleDateFormat df = new SimpleDateFormat("DD/MM/yyyy");

    Employee employee = createEntityInstance(Employee.class);
    employee.setGender(gender);
    employee.setName(name);
    employee.setFirstName(firstName);
    employee.setPassword(password);
    employee.getContact().setAddress(address);
    employee.getContact().setCity(city);
    employee.getContact().setEmail(email);
    employee.getContact().setPhone(phone);
    try {
      employee.setBirthDate(df.parse(birthDate));
    } catch (ParseException ex) {
      ex.printStackTrace(System.err);
    }
    try {
      employee.setHireDate(df.parse(hireDate));
    } catch (ParseException ex) {
      ex.printStackTrace(System.err);
    }
    employee.setSsn(ssn);
    employee.setMarried(married);
    employee.setPreferredColor(preferredColor);
    employee.setSalary(new BigDecimal(salary));
    employee.setCompany(company);
    return employee;
  }
}
