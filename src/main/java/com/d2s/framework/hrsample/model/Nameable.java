/*
 * Generated by Design2see. All rights reserved.
 */
package com.d2s.framework.hrsample.model;

/**
 * Nameable component.
 * <p>
 * Generated by Design2see. All rights reserved.
 * <p>
 * 
 * @author Generated by Design2see
 * @version $LastChangedRevision$
 */
public interface Nameable {

  /**
   * Gets the name.
   * 
   * @hibernate.property
   * @hibernate.column name = "NAME" length = "64" not-null = "true"
   * @return the name.
   */
  java.lang.String getName();

  /**
   * Sets the name.
   * 
   * @param name
   *            the name to set.
   */
  void setName(java.lang.String name);

}
