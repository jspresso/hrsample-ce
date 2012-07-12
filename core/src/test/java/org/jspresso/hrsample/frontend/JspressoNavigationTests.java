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
package org.jspresso.hrsample.frontend;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Navigation integration tests.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class JspressoNavigationTests extends FrontTestStartup {

  /**
   * Test 1.
   */
  @Test
  public void test1() {
    System.out.println(getFrontendController());
    assertEquals(1, 1);
  }

  /**
   * Test 2.
   */
  @Test
  public void test2() {
    System.out.println(getFrontendController());
    assertEquals(1, 1);
  }

}
