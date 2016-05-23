package org.jspresso.hrsample.frontend;

import java.util.Locale;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.backend.persistence.hibernate.HibernateBackendController;
import org.jspresso.framework.application.backend.session.EMergeMode;
import org.jspresso.framework.application.frontend.action.FrontendAction;
import org.jspresso.framework.security.UserPrincipal;
import org.jspresso.hrsample.model.Employee;
import org.jspresso.hrsample.model.User;

/**
 * UserSessionInitAction.
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
public class UserSessionInitAction<E, F, G> extends FrontendAction<E, F, G> {

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler, Map<String, Object> context) {
   
    HibernateBackendController controller = (HibernateBackendController) getBackendController(context);
    UserPrincipal principal = controller.getApplicationSession().getPrincipal();
    
    DetachedCriteria dc = DetachedCriteria.forClass(User.class);
    dc.add(Restrictions.eq(User.LOGIN, principal.getName()));
    User user = controller.findFirstByCriteria(dc, EMergeMode.MERGE_KEEP, User.class);
     
    // complete principal with community, etc.
    principal.putCustomProperty(User.USER_ENTITY_ID, user.getId());
    Employee employee = user.getEmployee();
    if (employee!=null) {
      principal.putCustomProperty(User.USER_ENTITY_TRACE_NAME, employee.getFullName());
      principal.putCustomProperty(UserPrincipal.LANGUAGE_PROPERTY, employee.getLanguage());
      getBackendController(context).getApplicationSession().setLocale(new Locale((String) principal.getCustomProperty(UserPrincipal.LANGUAGE_PROPERTY)));

    }
    else { 
      principal.putCustomProperty(User.USER_ENTITY_TRACE_NAME, user.getLogin());
    }

    
    return super.execute(actionHandler, context);
  }
}
