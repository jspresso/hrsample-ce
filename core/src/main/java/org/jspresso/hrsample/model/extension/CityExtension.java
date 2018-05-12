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
import org.jspresso.framework.model.component.service.DependsOnGroup;
import org.jspresso.framework.util.gui.map.MapHelper;
import org.jspresso.framework.util.gui.map.Point;
import org.jspresso.framework.util.gui.map.Route;
import org.jspresso.hrsample.model.City;
import org.jspresso.hrsample.model.ICityExtension;

import java.util.Random;

/**
 * City extension.
 *
 * @author Vincent Vandenschrick
 */
public class CityExtension extends AbstractComponentExtension<City> implements ICityExtension {

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


  /**
   * Gets the mapContent.
   *
   * @return the mapContent.
   */
  @DependsOn({City.LATITUDE,
          City.LONGITUDE,
          City.ROUTE})
  public String getMapContent() {

    City city = getComponent();

    // Get city point and route
    Point[] points = city.getPoint() != null ? new Point[]{city.getPoint()} : null;
    Route[] routes = city.getRoute() != null ? new Route[]{city.getRoute()} : null;

    // Build map
    return MapHelper.buildMap(points, routes);
  }

  /**
   * Gets the route.
   *
   * @return the route.
   */
  @DependsOn({City.LATITUDE,
          City.LONGITUDE})
  @Override
  public org.jspresso.framework.util.gui.map.Route getRoute() {

    City city = getComponent();
    Double longitude = city.getLongitude();
    if (longitude == null) {
      return null;
    }
    Double latitude = city.getLatitude();
    if (latitude == null) {
      return null;
    }

    Random random = new Random();
    int routeCount = random.nextInt(5)+5;
    Point[] points = new Point[routeCount];
    for (int r = 1; r < routeCount-1; r++) {

      longitude = Math.round(1000d * (longitude + random.nextDouble() * 0.05d)) / 1000d;
      latitude =  Math.round(1000d * (latitude + random.nextDouble() * 0.05d * (random.nextBoolean() ? 1 : -1))) / 1000d;

      points[r] = new Point(longitude, latitude);
    }
    points[0] = points[routeCount-1] = city.getPoint();

    return new Route(points);
  }


  /**
   * Gets the cardSelector.
   *
   * @return the cardSelector.
   */
  @DependsOn({City.LATITUDE, City.LONGITUDE})
  @Override
  public String getCardSelector() {
    City city = getComponent();
    if (city.getLongitude() != null && city.getLatitude() != null) {
      return "MAP";
    } else {
      return "NOMAP";
    }
  }


}
