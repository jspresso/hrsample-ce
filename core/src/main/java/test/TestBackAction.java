/*
 * Copyright (c) 2005-2012 Vincent Vandenschrick. All rights reserved.
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

import java.util.Map;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.backend.BackendControllerHolder;
import org.jspresso.framework.application.backend.action.BackendAction;
import org.jspresso.framework.application.backend.persistence.hibernate.HibernateBackendController;
import org.jspresso.hrsample.model.City;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

/**
 * Test back action.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class TestBackAction extends BackendAction {

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler, Map<String, Object> context) {
    try {
      System.out.println("########## Async action begin");
      System.out.println(">> ActionHandler : " + actionHandler);
      System.out.println(">> Controller    : " + getController(context));
      System.out.println(">> TxTemplate    : " + getTransactionTemplate(context).hashCode());
      System.out.println(">> BcHolder c    : " + BackendControllerHolder.getCurrentBackendController());
      Thread.sleep(10000);
      final HibernateBackendController bc = (HibernateBackendController) getController(context);
      bc.getTransactionTemplate().execute(new TransactionCallbackWithoutResult() {

        @Override
        protected void doInTransactionWithoutResult(@SuppressWarnings("unused") TransactionStatus status) {
          City test = bc.getEntityFactory().createEntityInstance(City.class);
          test.setName(Long.toString(System.currentTimeMillis()));
          test.setZip("12345");
          bc.registerForUpdate(test);
        }
      });
      System.out.println("########## Async action end");
    } catch (InterruptedException ex) {
      ex.printStackTrace();
    }
    return super.execute(actionHandler, context);
  }
}
