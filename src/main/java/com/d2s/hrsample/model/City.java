/*
 * Generated by Design2see. All rights reserved.
 */
package com.d2s.hrsample.model;

/**
 * City entity.
 * <p>
 * Generated by Design2see. All rights reserved.
 * <p>
 *
 * @hibernate.mapping
 *           default-access = "com.d2s.framework.model.persistence.hibernate.property.EntityPropertyAccessor"
 * @hibernate.class
 *           table = "CITY"
 *           dynamic-insert = "true"
 *           dynamic-update = "true"
 *           persister = "com.d2s.framework.model.persistence.hibernate.entity.persister.EntityProxyJoinedSubclassEntityPersister"
 * @author Generated by Design2see
 * @version $LastChangedRevision$
 */
public interface City extends
  com.d2s.hrsample.model.Nameable,
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
   * Gets the zip.
   *
   * @hibernate.property
   * @hibernate.column
   *           name = "ZIP"
   *           length = "10"
   * @return the zip.
   */
  java.lang.String getZip();

  /**
   * Sets the zip.
   *
   * @param zip
   *          the zip to set.
   */
  void setZip(java.lang.String zip);

}
