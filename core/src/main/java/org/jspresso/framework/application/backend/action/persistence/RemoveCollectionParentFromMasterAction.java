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
package org.jspresso.framework.application.backend.action.persistence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.jspresso.framework.model.descriptor.ICollectionDescriptorProvider;

/**
 * An action used in master/detail repeater views to remove selected details.
 * 
 * @author Maxime Hamm
 */
public class RemoveCollectionParentFromMasterAction extends RemoveCollectionFromMasterAction {

  /**
   * {@inheritDoc}
   */
  @Override
  protected ICollectionDescriptorProvider<?> getModelDescriptor(Map<String, Object> context) {
    return (ICollectionDescriptorProvider<?>) getModelConnector(context).getModelDescriptor();
  }
  
  /**
   * {@inheritDoc}
   */
  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Override
  protected int[] getSelectedIndices(int[] viewPath, Map<String, Object> context) {
    List<?> list = new ArrayList<>((Collection)getModelConnector(context).getConnectorValue());
    Object model = getModel(context);
    return new int[]{list.indexOf(model)};
  }
}
