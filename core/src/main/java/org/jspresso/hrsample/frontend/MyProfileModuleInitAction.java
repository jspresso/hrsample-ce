package org.jspresso.hrsample.frontend;

import java.io.Serializable;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.backend.persistence.hibernate.HibernateBackendController;
import org.jspresso.framework.application.backend.session.EMergeMode;
import org.jspresso.framework.application.frontend.action.FrontendAction;
import org.jspresso.framework.application.model.BeanModule;
import org.jspresso.framework.security.UserPrincipal;
import org.jspresso.hrsample.model.User;

/**
 * MyProfileModuleInitAction.
 * 
 * @author Maxime HAMM
 *
 * @param <E>
 *          the actual gui component type used.
 * @param <F>
 *          the actual icon type used.
 * @param <G>
 *          the actual action type used.
 */
public class MyProfileModuleInitAction<E, F, G> extends FrontendAction<E, F, G> {

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler, Map<String, Object> context) {
    
    User user = getUser((HibernateBackendController) getBackendController(context));
    BeanModule module = (BeanModule)getModule(context);
    module.setModuleObject(user.getEmployee());
    
    return super.execute(actionHandler, context);
  }
  
  /**
   * Get user.
   * @param controller The controller
   * @return the User.
   */
  private static User getUser(HibernateBackendController controller) {
    UserPrincipal up = controller.getApplicationSession().getPrincipal();
    if (up == null) 
      return null;
    
    Serializable userId = (Serializable) up.getCustomProperty(User.USER_ENTITY_ID);
    DetachedCriteria dc = DetachedCriteria.forClass(User.class);
    dc.add(Restrictions.idEq(userId));
    
    return controller.findFirstByCriteria(dc, EMergeMode.MERGE_KEEP, User.class);
  }
}
