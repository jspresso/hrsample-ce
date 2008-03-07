/*
 * Generated by Design2see. All rights reserved.
 */
package com.d2s.hrsample.model;

/**
 * OrganizationalUnit entity.
 * <p>
 * Generated by Design2see. All rights reserved.
 * <p>
 *
 * @hibernate.mapping
 *           default-access = "com.d2s.framework.model.persistence.hibernate.property.EntityPropertyAccessor"
 * @hibernate.class
 *           table = "ORGANIZATIONAL_UNIT"
 *           dynamic-insert = "true"
 *           dynamic-update = "true"
 *           persister = "com.d2s.framework.model.persistence.hibernate.entity.persister.EntityProxyJoinedSubclassEntityPersister"
 *           abstract = "true"
 * @author Generated by Design2see
 * @version $LastChangedRevision$
 */
public interface OrganizationalUnit extends
  com.d2s.hrsample.model.Nameable,
  com.d2s.hrsample.model.Traceable,
  com.d2s.framework.model.entity.IEntity {

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
   * Gets the ouId.
   *
   * @hibernate.property
   * @hibernate.column
   *           name = "OU_ID"
   *           length = "6"
   *           not-null = "true"
   * @return the ouId.
   */
  java.lang.String getOuId();

  /**
   * Sets the ouId.
   *
   * @param ouId
   *          the ouId to set.
   */
  void setOuId(java.lang.String ouId);

  /**
   * Gets the contact.
   *
   * @hibernate.component
   *           prefix = "CONTACT_"
   * @return the contact.
   */
  com.d2s.hrsample.model.ContactInfo getContact();

  /**
   * Sets the contact.
   *
   * @param contact
   *          the contact to set.
   */
  void setContact(com.d2s.hrsample.model.ContactInfo contact);

  /**
   * Gets the manager.
   *
   * @hibernate.one-to-one
   *           cascade = "persist,merge,save-update,refresh,evict,replicate"
   *           property-ref = "managedOu"
   * @return the manager.
   */
  com.d2s.hrsample.model.Employee getManager();

  /**
   * Sets the manager.
   *
   * @param manager
   *          the manager to set.
   */
  void setManager(com.d2s.hrsample.model.Employee manager);

}
