package org.jspresso.hrsample.model.extension;

import org.jspresso.framework.model.component.AbstractComponentExtension;
import org.jspresso.framework.model.component.service.DependsOn;

import org.jspresso.framework.model.component.service.DependsOnGroup;
import org.jspresso.hrsample.model.IUserExtension;
import org.jspresso.hrsample.model.User;
import org.jspresso.hrsample.model.Role;

/**
 * User extension.
 */
public class UserExtension extends AbstractComponentExtension<User> implements IUserExtension {

  /**
   * UserExtension constructor.
   */
	public UserExtension(User component) {
		super(component);

		//registerNotificationForwarding(component, User.FIELD, User.COMPUTED_FIELD);
	}

  /**
   * {@inheritDoc}
   *
   * @return
   */
  @DependsOn(User.ROLES)
  public Role getMainRole() {
    User user = getComponent();

    if (! user.getRoles().isEmpty()) {
      return user.getRoles().iterator().next();
    }
    return null;
  }


    /**
     * Gets the rolesCount.
     *
     * @return the rolesCount.
     */
    @DependsOn(sourceCollection = User.ROLES, value = Role.ROLE_ID)
    @Override
    public Integer getRolesCount() {

        User user = getComponent();
        int i= 0;
        for (Role role : user.getRoles()) {
          if (role.isRoleIdDefined()) {
              i++;
          }
        }
        return i;
    }


}
