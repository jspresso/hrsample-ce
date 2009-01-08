/*
 * Generated by Jspresso. All rights reserved.
 */
package org.jspresso.hrsample.model;

/**
 * Company entity.
 * <p>
 * Generated by Jspresso. All rights reserved.
 * <p>
 *
 * @hibernate.mapping
 *           default-access = "org.jspresso.framework.model.persistence.hibernate.property.EntityPropertyAccessor"
 * @hibernate.class
 *           table = "COMPANY"
 *           dynamic-insert = "true"
 *           dynamic-update = "true"
 *persister = "org.jspresso.framework.model.persistence.hibernate.entity.persister.EntityProxyJoinedSubclassEntityPersister"
 * @author Generated by Jspresso
 * @version $LastChangedRevision$
 */
public interface Company extends
  org.jspresso.hrsample.model.Nameable,
  org.jspresso.hrsample.model.Traceable,
  org.jspresso.framework.model.entity.IEntity {

  /**
   * @hibernate.id generator-class = "assigned" column = "ID" type = "string"
   *               length = "36"
   * <p>
   * {@inheritDoc}
   */
  java.io.Serializable getId();

  /**
   * @hibernate.version column = "VERSION" unsaved-value = "null"
   * <p>
   * {@inheritDoc}
   */
  Integer getVersion();

  /**
   * Gets the contact.
   *
   * @hibernate.component
   *           prefix = "CONTACT_"
   * @return the contact.
   */
  org.jspresso.hrsample.model.ContactInfo getContact();

  /**
   * Sets the contact.
   *
   * @param contact
   *          the contact to set.
   */
  void setContact(org.jspresso.hrsample.model.ContactInfo contact);

  /**
   * Gets the departments.
   *
   * @hibernate.set
   *           cascade = "persist,merge,save-update,refresh,evict,replicate,delete"
   *           inverse = "true"
   * @hibernate.key
   *           column = "COMPANY_ID"
   *           not-null = "true"
   * @hibernate.one-to-many
   *           class = "org.jspresso.hrsample.model.Department"
   * @return the departments.
   */
  java.util.Set<org.jspresso.hrsample.model.Department> getDepartments();

  /**
   * Sets the departments.
   *
   * @param departments
   *          the departments to set.
   */
  void setDepartments(java.util.Set<org.jspresso.hrsample.model.Department> departments);

  /**
   * Adds an element to the departments.
   *
   * @param departmentsElement
   *          the departments element to add.
   */
  void addToDepartments(org.jspresso.hrsample.model.Department departmentsElement);

  /**
   * Removes an element from the departments.
   *
   * @param departmentsElement
   *          the departments element to remove.
   */
  void removeFromDepartments(org.jspresso.hrsample.model.Department departmentsElement);

  /**
   * Gets the employees.
   *
   * @hibernate.set
   *           cascade = "persist,merge,save-update,refresh,evict,replicate,delete"
   *           inverse = "true"
   * @hibernate.key
   *           column = "COMPANY_ID"
   *           not-null = "true"
   * @hibernate.one-to-many
   *           class = "org.jspresso.hrsample.model.Employee"
   * @return the employees.
   */
  java.util.Set<org.jspresso.hrsample.model.Employee> getEmployees();

  /**
   * Sets the employees.
   *
   * @param employees
   *          the employees to set.
   */
  void setEmployees(java.util.Set<org.jspresso.hrsample.model.Employee> employees);

  /**
   * Adds an element to the employees.
   *
   * @param employeesElement
   *          the employees element to add.
   */
  void addToEmployees(org.jspresso.hrsample.model.Employee employeesElement);

  /**
   * Removes an element from the employees.
   *
   * @param employeesElement
   *          the employees element to remove.
   */
  void removeFromEmployees(org.jspresso.hrsample.model.Employee employeesElement);

}
