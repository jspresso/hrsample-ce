package org.jspresso.hrsample.model.service;

import org.apache.commons.lang.RandomStringUtils;
import org.jspresso.framework.model.component.service.AbstractComponentServiceDelegate;
import org.jspresso.hrsample.model.User;

/**
 * User services delegate.
 */
public class UserServiceDelegate 
      extends AbstractComponentServiceDelegate<User>
      implements UserService {

  /**
   * GeneratePassword
   */
  @Override
  public void generatePassword() {
    getComponent().setPassword("HR" + RandomStringUtils.random(5, "*#0123456789"));
  } 
        
}