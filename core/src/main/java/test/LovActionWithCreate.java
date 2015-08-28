/*
 * Copyright (c) 2005-2015 Vincent Vandenschrick. All rights reserved.
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

import java.util.List;
import java.util.Map;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.action.ModalDialogAction;
import org.jspresso.framework.application.frontend.action.lov.LovAction;
import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.component.IQueryComponent;
import org.jspresso.framework.model.descriptor.IReferencePropertyDescriptor;
import org.jspresso.framework.view.IView;
import org.jspresso.framework.view.action.IDisplayableAction;

/**
 * @author Vincent Vandenschrick
 */
public class LovActionWithCreate<E, F, G> extends LovAction<E, F, G> {

  private IDisplayableAction createAction;

  @Override
  protected void feedContextWithDialog(IReferencePropertyDescriptor<IComponent> erqDescriptor,
                                       IQueryComponent queryComponent, IView<E> lovView, IActionHandler actionHandler,
                                       Map<String, Object> context) {
    super.feedContextWithDialog(erqDescriptor, queryComponent, lovView, actionHandler, context);
    List<IDisplayableAction> defaultLovDialogActions = (List<IDisplayableAction>) context.get(
        ModalDialogAction.DIALOG_ACTIONS);
    defaultLovDialogActions.add(1, getCreateAction());
  }

  /**
   * Gets create action.
   *
   * @return the create action
   */
  protected IDisplayableAction getCreateAction() {
    return createAction;
  }

  /**
   * Sets create action.
   *
   * @param createAction
   *     the create action
   */
  public void setCreateAction(IDisplayableAction createAction) {
    this.createAction = createAction;
  }
}
