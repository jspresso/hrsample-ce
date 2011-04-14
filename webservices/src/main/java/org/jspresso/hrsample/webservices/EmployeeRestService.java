/*
 * Copyright (c) 2005-2011 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.hrsample.webservices;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.jspresso.framework.application.backend.persistence.hibernate.HibernateBackendController;
import org.jspresso.framework.application.backend.session.EMergeMode;
import org.jspresso.framework.model.persistence.hibernate.criterion.EnhancedDetachedCriteria;
import org.jspresso.hrsample.model.ContactInfo;
import org.jspresso.hrsample.model.Employee;
import org.jspresso.hrsample.model.Event;

/**
 * A sample employee web service.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
@Path("/employees")
public class EmployeeRestService extends AbstractService {

  /**
   * Retrieves an employee by its name.
   * 
   * @param name
   *          the name of the employee to retrieve.
   * @return the employee simplified DTO.
   */
  @GET
  @Path("/employee/{name}")
  @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
  public EmployeeDto getEmployee(@PathParam("name") String name) {
    DetachedCriteria crit = EnhancedDetachedCriteria.forClass(Employee.class);
    crit.add(Restrictions.eq("name", name));
    Employee e = ((HibernateBackendController) getBackendController())
        .findFirstByCriteria(crit, EMergeMode.MERGE_KEEP, Employee.class);
    if (e != null) {
      return new EmployeeDto(e);
    }
    return null;
  }

  // //////////////////////////////////////////////////
  // Data transfer objects used for JAXB marshaling //
  // //////////////////////////////////////////////////

  /**
   * Employee DTO.
   */
  @XmlRootElement(name = "employee")
  @XmlAccessorType(XmlAccessType.FIELD)
  public static class EmployeeDto {

    /**
     * <code>name</code>.
     */
    public String         name;
    /**
     * <code>firstName</code>.
     */
    public String         firstName;
    /**
     * <code>age</code>.
     */
    public Integer        age;
    /**
     * <code>address</code>.
     */
    public AddressDto     address;

    /**
     * <code>events</code>.
     */
    @XmlElementWrapper
    @XmlElement(name = "event")
    public List<EventDto> events;

    /**
     * Default required constructor.
     */
    public EmployeeDto() {
      // Empty constructor.
    }

    /**
     * Constructs a new <code>EmployeeDto</code> instance.
     * 
     * @param employee
     *          the employee to create the DTO for.
     */
    public EmployeeDto(Employee employee) {
      this.name = employee.getName();
      this.firstName = employee.getFirstName();
      this.age = employee.getAge();
      this.address = new AddressDto(employee.getContact());
      this.events = new ArrayList<EventDto>();
      for (Event evt : employee.getEvents()) {
        this.events.add(new EventDto(evt));
      }
    }
  }

  /**
   * Address DTO.
   */
  @XmlRootElement(name = "address")
  @XmlAccessorType(XmlAccessType.FIELD)
  public static class AddressDto {

    /**
     * <code>address</code>.
     */
    public String address;
    /**
     * <code>zip</code>.
     */
    public String zip;
    /**
     * <code>city</code>.
     */
    public String city;

    /**
     * Default required constructor.
     */
    public AddressDto() {
      // Empty constructor.
    }

    /**
     * Constructs a new <code>AddressDto</code> instance.
     * 
     * @param contact
     *          the contact to create the DTO for.
     */
    public AddressDto(ContactInfo contact) {
      this.address = contact.getAddress();
      this.zip = contact.getCity().getZip();
      this.city = contact.getCity().getName();
    }
  }

  /**
   * Event DTO.
   */
  @XmlRootElement(name = "event")
  @XmlAccessorType(XmlAccessType.FIELD)
  public static class EventDto {

    /**
     * <code>text</code>.
     */
    @XmlAttribute
    public String text;

    /**
     * Default required constructor.
     */
    public EventDto() {
      // Empty constructor.
    }

    /**
     * Constructs a new <code>EventDto</code> instance.
     * 
     * @param event
     *          the event to create the DTO for.
     */
    public EventDto(Event event) {
      this.text = event.getText();
    }
  }
}
