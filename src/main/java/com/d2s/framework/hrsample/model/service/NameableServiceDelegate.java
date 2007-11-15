/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.hrsample.model.service;

import com.d2s.framework.hrsample.model.Nameable;
import com.d2s.framework.model.component.service.IComponentService;

/**
 * A very usefull ;) service to capitalize the first letter of a Nameable name
 * and lower case the remaining letters.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class NameableServiceDelegate implements IComponentService {

  /**
   * Formats the name of a nameable.
   * 
   * @param nameable
   *            The nameable to format the name.
   */
  public void fomatName(Nameable nameable) {
    if (nameable.getName() != null && nameable.getName().length() > 0) {
      StringBuffer formattedName = new StringBuffer();
      formattedName.append(nameable.getName().substring(0, 1).toUpperCase());
      formattedName.append(nameable.getName().substring(1).toLowerCase());
      nameable.setName(formattedName.toString());
    }
  }
}
