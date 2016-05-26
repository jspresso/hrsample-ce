/*
 * Copyright (c) 2005-2016 Maxime HAMM. All rights reserved.
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
package org.jspresso.framework.application.frontend.action.std;

import java.util.Map;

import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.entity.IEntity;

/**
 * This action is designed to create and add a (collection of) component(s) 
 * to the PARENT model collection of the view it's installed on.
 * Read more here : {@link AddCollectionToMasterAction}
 *
 * @author Maxime Hamm
 * @param <E>
 *          the actual gui component type used.
 * @param <F>
 *          the actual icon type used.
 * @param <G>
 *          the actual action type used.
 */
public class AddToParentMasterFrontAction<E, F, G> extends AddCollectionToMasterAction<E, F, G> {

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  protected IComponentDescriptor<IEntity> getElementEntityDescriptor(Map<String, Object> context) {
    IComponentDescriptor<IEntity> d = super.getElementEntityDescriptor(context);
    if (d!=null) {
      return d;
    }
    
    return (IComponentDescriptor<IEntity>) getModelDescriptor(context);
  }
}
