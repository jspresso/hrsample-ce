/*
 * Copyright (c) 2005-2013 Vincent Vandenschrick. All rights reserved.
 *
 *  This file is part of the Jspresso framework.
 *
 *  Jspresso is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Jspresso is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with Jspresso.  If not, see <http://www.gnu.org/licenses/>.
 */
package test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.backend.action.Asynchronous;
import org.jspresso.framework.application.backend.action.BackendAction;
import org.jspresso.framework.application.backend.persistence.hibernate.HibernateBackendController;
import org.jspresso.framework.application.backend.session.EMergeMode;
import org.jspresso.framework.application.frontend.action.FrontendAction;
import org.jspresso.framework.application.frontend.action.flow.InfoAction;
import org.jspresso.framework.model.persistence.hibernate.criterion.EnhancedDetachedCriteria;

import org.jspresso.hrsample.model.City;

/**
 * Test back action.
 * 
 * @author Vincent Vandenschrick
 */
@Asynchronous(pushRuntimeExceptions = true, autoMergeBackEntities = true)
public class TestBackAction extends BackendAction {

  private InfoAction completionAction = new InfoAction();

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler, Map<String, Object> context) {
    Date start = new Date();
    try {
      for (int i = 1; i <= 10; i++) {
        Thread.sleep(1000);
        setProgress(((double) i) / 10);
      }
      final HibernateBackendController bc = (HibernateBackendController) getController(context);
      bc.getTransactionTemplate().execute(new TransactionCallbackWithoutResult() {

        @Override
        protected void doInTransactionWithoutResult(TransactionStatus status) {
          EnhancedDetachedCriteria cityCrit = EnhancedDetachedCriteria.forClass(City.class);
          cityCrit.add(Restrictions.eq(City.ZIP, "69000"));
          City lyon = bc.findFirstByCriteria(cityCrit, EMergeMode.MERGE_KEEP, City.class);
          lyon.setName(Long.toString(System.currentTimeMillis()));
        }
      });
//      if (System.currentTimeMillis() % 2 == 0) {
//        throw new NullPointerException(
//            "This is a test for an unhandled exception on thread " + Thread.currentThread().getName());
//      }
    } catch (InterruptedException ex) {
      ex.printStackTrace();
    }
    Date end = new Date();
    Map<String, Object> completionContext = new HashMap<>();
    setActionParameter("Asynchronous action that was started at " + start + "has completed at " + end,
        completionContext);
    actionHandler.executeLater(completionAction, completionContext);
    return super.execute(actionHandler, context);
  }
}
