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
package org.jspresso.hrsample.frontend;

import java.util.Map;

import org.jspresso.framework.action.ActionException;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.action.FrontendAction;
import org.jspresso.framework.application.frontend.controller.AbstractFrontendController;
import org.junit.Test;

/**
 * Actions sanitization integration tests.
 * 
 * @author Vincent Vandenschrick
 */
public class JspressoActionSanitizationTests extends FrontTestStartup {

  /**
   * Tests action thread-safety sanity check. On own through attribute
   * assignment state modification. See bug 585.
   */
  @SuppressWarnings("rawtypes")
  @Test(expected = ActionException.class)
  public void testSanitizeMutableAction() {

    FrontendAction badAction = new FrontendAction() {

      @SuppressWarnings("unused")
      private String state = "initial";

      @Override
      public boolean execute(IActionHandler actionHandler,
          Map<String, Object> context) {
        state = "updated";
        return super.execute(actionHandler, context);
      }
    };
    getFrontendController().execute(badAction,
        getFrontendController().getInitialActionContext());
  }

  /**
   * Tests action thread-safety sanity check. On superclass state modification.
   * See bug 585.
   */
  @SuppressWarnings("rawtypes")
  @Test(expected = ActionException.class)
  public void testSanitizeMutableSuperAction() {

    FrontendAction badAction = new FrontendAction() {

      @Override
      public boolean execute(IActionHandler actionHandler,
          Map<String, Object> context) {
        setNextAction(new FrontendAction());
        return super.execute(actionHandler, context);
      }
    };
    getFrontendController().execute(badAction,
        getFrontendController().getInitialActionContext());
  }

  /**
   * Tests action thread-safety sanity check. On nested action state
   * modification. See bug 585.
   */
  @SuppressWarnings("rawtypes")
  @Test(expected = ActionException.class)
  public void testSanitizeMutableNestedAction() {

    FrontendAction badAction = new FrontendAction() {

      @Override
      public boolean execute(IActionHandler actionHandler,
          Map<String, Object> context) {
        ((FrontendAction) getNextAction(context))
            .setWrappedAction(new FrontendAction());
        return super.execute(actionHandler, context);
      }
    };
    badAction.setNextAction(new FrontendAction());

    getFrontendController().execute(badAction,
        getFrontendController().getInitialActionContext());
  }

  /**
   * Tests action thread-safety sanity check disablement. See bug 585.
   */
  @SuppressWarnings("rawtypes")
  @Test
  public void testActionSanitizeDisablement() {
    ((AbstractFrontendController) getFrontendController())
        .setCheckActionThreadSafety(false);
    testSanitizeMutableAction();
    ((AbstractFrontendController) getFrontendController())
        .setCheckActionThreadSafety(true);
  }
}
