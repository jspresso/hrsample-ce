/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.hrsample.startup.qooxdoo;

import java.util.List;

import net.sf.qooxdoo.rpc.RemoteService;
import net.sf.qooxdoo.rpc.RemoteServiceException;

import org.jspresso.framework.application.frontend.command.remote.RemoteCommand;
import org.jspresso.hrsample.startup.remote.RemoteApplicationStartup;

/**
 * Qooxdoo application startup.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class QooxdooApplicationStartup extends RemoteApplicationStartup
    implements RemoteService {

  /**
   * Delegates to start.
   * 
   * @param startupLanguage
   *          the client language.
   * @return the commands to be executed by the client peer on startup.
   * @throws RemoteServiceException
   *           whenever an exception occurs.
   */
  public List<RemoteCommand> startQx(String startupLanguage)
      throws RemoteServiceException {
    return super.start(startupLanguage);
  }

  /**
   * Recieves and handle a list of commands.
   * 
   * @param commands
   *          the command list.
   * @return the resulting commands.
   * @throws RemoteServiceException
   *           whenever an exception occurs.
   */
  public List<RemoteCommand> handleCommandsQx(List<RemoteCommand> commands)
      throws RemoteServiceException {
    return handleCommands(commands);
  }
}
