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
package org.jspresso.hrsample.development;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.jspresso.framework.application.security.ISecurityPlugin;
import org.jspresso.framework.security.ISecurable;
import org.jspresso.framework.util.automation.IPermIdSource;

/**
 * A custom security plugin to dump security requests.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class SnifferSecurityPlugin implements ISecurityPlugin {

  private BufferedWriter writer;

  /**
   * Always grants but dumps security requests to a file.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean isAccessGranted(ISecurable securable,
      Map<String, Object> context) {
    try {
      if (writer != null) {
        writer.write("Target  :\n");
        writer.write("\tType    : " + securable.getClass().getName() + "\n");
        if (securable instanceof IPermIdSource) {
          writer.write("\tPerm Id : " + ((IPermIdSource) securable).getPermId()
              + "\n");
        }
        writer.write("Context :\n");
        SortedMap<String, Object> sortedContext = new TreeMap<String, Object>(
            context);
        for (Map.Entry<String, Object> contextEntry : sortedContext.entrySet()) {
          writer.write("\t" + contextEntry.getKey() + " : "
              + contextEntry.getValue() + "\n");
        }
        writer.write("\n\n\n");
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return true;
  }

  /**
   * Configures the file name to dump security rquests to.
   * 
   * @param fileName
   *          the file name.
   */
  public void setFileName(String fileName) {
    try {
      this.writer = new BufferedWriter(new OutputStreamWriter(
          new FileOutputStream(fileName)));
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

}
