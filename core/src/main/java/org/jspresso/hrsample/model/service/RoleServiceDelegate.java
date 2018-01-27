package org.jspresso.hrsample.model.service;

import org.jspresso.framework.model.component.service.AbstractComponentServiceDelegate;
import org.jspresso.hrsample.model.Role;

public class RoleServiceDelegate extends AbstractComponentServiceDelegate<Role> implements RoleService {

    @Override
    public boolean isRoleIdDefined() {
        return getComponent().getRoleId()!=null;
    }
}
