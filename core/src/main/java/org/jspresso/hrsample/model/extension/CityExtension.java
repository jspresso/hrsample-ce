/*
 * Copyright (c) 2005-2017 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.hrsample.model.extension;

import org.jspresso.framework.model.component.AbstractComponentExtension;
import org.jspresso.framework.model.component.service.DependsOn;

import org.jspresso.hrsample.model.City;

/**
 * City extension.
 *
 * @author Vincent Vandenschrick
 */
public class CityExtension extends AbstractComponentExtension<City> {

  /**
   *  * Constructs a new {@code CityExtension} instance.
   * <p>
   *  * @param extendedCity
   *  *          The extended City instance.
   *  
   */
  public CityExtension(City extendedCity) {
    super(extendedCity);
  }

  /**
   * Gets the longitudeBackground.
   *
   * @return the longitudeBackground.
   */
  @DependsOn({City.LONGITUDE})
  public String getLongitudeBackground() {
    City city = getComponent();
    if (city.getLongitude() == null) {
      return "0xFF0000";
    }
    return null;
  }


  /**
   * Gets the latitudeBackground.
   *
   * @return the latitudeBackground.
   */
  @DependsOn({City.LATITUDE})
  public String getLatitudeBackground() {
    City city = getComponent();
    if (city.getLatitude() == null) {
      return "0xFF0000";
    }
    return null;
  }


}
