/*
 * Generated by Design2see. All rights reserved.
 */
package com.d2s.hrsample.model;

/**
 * ContactInfo component.
 * <p>
 * Generated by Design2see. All rights reserved.
 * <p>
 *
 * @author Generated by Design2see
 * @version $LastChangedRevision$
 */
public interface ContactInfo extends
  com.d2s.framework.model.component.IComponent {

  /**
   * Gets the address.
   *
   * @hibernate.property
   * @hibernate.column
   *           name = "ADDRESS"
   *           length = "256"
   * @return the address.
   */
  java.lang.String getAddress();

  /**
   * Sets the address.
   *
   * @param address
   *          the address to set.
   */
  void setAddress(java.lang.String address);

  /**
   * Gets the city.
   *
   * @hibernate.many-to-one
   *           cascade = "persist,merge,save-update,refresh,evict,replicate"
   * @hibernate.column
   *           name = "CITY_ID"
   * @return the city.
   */
  com.d2s.hrsample.model.City getCity();

  /**
   * Sets the city.
   *
   * @param city
   *          the city to set.
   */
  void setCity(com.d2s.hrsample.model.City city);

  /**
   * Gets the phone.
   *
   * @hibernate.property
   * @hibernate.column
   *           name = "PHONE"
   *           length = "32"
   * @return the phone.
   */
  java.lang.String getPhone();

  /**
   * Sets the phone.
   *
   * @param phone
   *          the phone to set.
   */
  void setPhone(java.lang.String phone);

  /**
   * Gets the email.
   *
   * @hibernate.property
   * @hibernate.column
   *           name = "EMAIL"
   *           length = "128"
   * @return the email.
   */
  java.lang.String getEmail();

  /**
   * Sets the email.
   *
   * @param email
   *          the email to set.
   */
  void setEmail(java.lang.String email);

}
