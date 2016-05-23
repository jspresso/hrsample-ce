package org.jspresso.hrsample.model.service;

/**
 * UserService services.
 */
public interface UserService {

  /**
   * USER_ENTITY_ID
   */
  public static final String USER_ENTITY_ID = "USER_ENTITY_ID";
  
  
  /**
   * USER_ENTITY_TRACE_NAME
   */
  public static final String USER_ENTITY_TRACE_NAME = "USER_ENTITY_TRACING_NAME";

  /**
   * Generate password.
   */
  void generatePassword();
  
}