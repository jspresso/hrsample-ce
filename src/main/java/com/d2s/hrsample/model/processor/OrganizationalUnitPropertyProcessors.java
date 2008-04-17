/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.hrsample.model.processor;

import org.jspresso.framework.util.bean.integrity.EmptyPropertyProcessor;
import org.jspresso.framework.util.bean.integrity.IntegrityException;

import com.d2s.hrsample.model.Employee;
import com.d2s.hrsample.model.OrganizationalUnit;

/**
 * OrganizationalUnit property processors.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class OrganizationalUnitPropertyProcessors {

  /**
   * Manager property processor.
   * <p>
   * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
   * <p>
   * 
   * @version $LastChangedRevision$
   * @author Vincent Vandenschrick
   */
  public static class ManagerProcessor extends
      EmptyPropertyProcessor<OrganizationalUnit, Employee> {

    /**
     * Checks that the manager belongs to the same company as the managed
     * OrganizationalUnit.
     * <p>
     * {@inheritDoc}
     */
    @Override
    public void preprocessSetter(OrganizationalUnit organizationalUnit,
        Employee newManager) {
      if (newManager == null || newManager.getCompany() == null
          || !newManager.getCompany().equals(organizationalUnit.getCompany())) {
        throw new IntegrityException(
            "A manager must belong to the same company as its managed organizational unit.",
            "manager.company.invalid");
      }
    }
  }
}
