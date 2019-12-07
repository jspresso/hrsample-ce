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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;

import org.jspresso.framework.model.component.AbstractComponentExtension;
import org.jspresso.framework.model.component.service.DependsOn;
import org.jspresso.framework.util.gui.map.MapHelper;
import org.jspresso.framework.util.gui.map.Point;
import org.jspresso.framework.util.gui.map.Route;
import org.jspresso.framework.util.gui.map.Shape;
import org.jspresso.framework.util.gui.map.Zone;

import org.jspresso.hrsample.model.City;
import org.jspresso.hrsample.model.ICityExtension;
import org.jspresso.hrsample.model.Nameable;

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
      return "0xf2d0cb";
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
      return "0xf2d0cb";
    }
    return null;
  }


  /**
   * Gets the mapContent.
   *
   * @return the mapContent.
   */
  @DependsOn({City.LATITUDE, City.LONGITUDE, Nameable.NAME, City.ROUTE})
  public String getMapContent() {

    City city = getComponent();

    // Get city point and route
    Point[] points = city.getPoint() != null ? new Point[]{city.getPoint()} : null;
    Route[] routes = city.getRoute() != null ? new Route[]{city.getRoute()} : null;

    if (city.getName()!=null && city.getName().contains("Paris")) {
      return MapHelper.buildMap(points, routes, new Shape[]{getParisShape()});
    }
    // Build map
    return MapHelper.buildMap(points, routes);
  }

  /**
   * Gets the route.
   *
   * @return the route.
   */
  @DependsOn({City.LATITUDE, City.LONGITUDE})
  @Override
  public Route getRoute() {

    return null;

//    City city = getComponent();
//    Double longitude = city.getLongitude();
//    if (longitude == null) {
//      return null;
//    }
//    Double latitude = city.getLatitude();
//    if (latitude == null) {
//      return null;
//    }
//
//    Random random = new Random();
//    int routeCount = random.nextInt(5) + 5;
//    Point[] points = new Point[routeCount];
//    for (int r = 1; r < routeCount - 1; r++) {
//
//      longitude = Math.round(1000d * (longitude + random.nextDouble() * 0.05d)) / 1000d;
//      latitude = Math.round(1000d * (latitude + random.nextDouble() * 0.05d * (random.nextBoolean() ? 1 : -1))) / 1000d;
//
//      points[r] = new Point(longitude, latitude);
//    }
//    points[0] = points[routeCount - 1] = city.getPoint();
//
//    return new Route(points);
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

  public Shape getParisShape() {
    List<Point> zonePoints = new ArrayList<>();
    try {
      JSONArray parisShape = new JSONArray(
          "[[2.3319,48.81701],[2.33247,48.81825],[2.29219,48.82715],[2.28359,48.8308],[2.27906,48.83249],[2"
              + ".27542,48.82951],[2.2728,48.82792],[2.26754,48.82779],[2.26722,48.83154],[2.27005,48.83301],[2.2673,48"
              + ".83466],[2.2628,48.83392],[2.25514,48.83481],[2.25171,48.83882],[2.25123,48.8429],[2.25248,48"
              + ".84548],[2"
              + ".25062,48.84555],[2.24237,48.84766],[2.23919,48.85004],[2.22566,48.85302],[2.22422,48.85352],[2"
              + ".22452,48"
              + ".85654],[2.22822,48.86518],[2.23174,48.86907],[2.23545,48.87059],[2.24109,48.87243],[2.24273,48"
              + ".87356],[2"
              + ".24569,48.87643],[2.25481,48.87408],[2.25541,48.87426],[2.25847,48.88039],[2.27749,48.87797],[2"
              + ".27995,48"
              + ".87857],[2.2809,48.8828],[2.28567,48.88657],[2.29151,48.88948],[2.29505,48.88987],[2.29863,48"
              + ".89172],[2"
              + ".30782,48.89606],[2.31246,48.89778],[2.31852,48.89963],[2.31988,48.90046],[2.32063,48.9008],[2"
              + ".32726,48"
              + ".90107],[2.34839,48.90153],[2.37029,48.90165],[2.38515,48.90201],[2.39033,48.90101],[2.3949,48"
              + ".89844],[2"
              + ".39762,48.89458],[2.39844,48.89094],[2.39891,48.88563],[2.4002,48.88383],[2.40409,48.8814],[2.40946,48"
              + ".8801],[2.41239,48.876],[2.41394,48.87083],[2.41402,48.86804],[2.41366,48.863],[2.41471,48.85865],[2"
              + ".41634,48.84924],[2.41574,48.84531],[2.41357,48.83826],[2.41224,48.83454],[2.4135,48.83372],[2"
              + ".41734,48"
              + ".83419],[2.42214,48.83579],[2.42093,48.83933],[2.41953,48.84145],[2.41987,48.84345],[2.42184,48"
              + ".84444],[2"
              + ".42453,48.84189],[2.43354,48.84105],[2.43718,48.84089],[2.43796,48.84467],[2.44074,48.84441],[2"
              + ".44075,48"
              + ".84596],[2.44653,48.84575],[2.44641,48.84493],[2.45789,48.84349],[2.46226,48.84254],[2.46522,48"
              + ".84111],[2"
              + ".46971,48.83658],[2.46896,48.83392],[2.46569,48.83181],[2.46496,48.83044],[2.46457,48.82766],[2"
              + ".46618,48"
              + ".82733],[2.46504,48.82409],[2.46286,48.82018],[2.4627,48.81906],[2.459,48.81724],[2.45723,48.81698],[2"
              + ".45333,48.81716],[2.4498,48.81788],[2.44186,48.81795],[2.43746,48.81818],[2.43745,48.81911],[2"
              + ".43491,48"
              + ".81967],[2.42923,48.82362],[2.42624,48.82413],[2.41996,48.82408],[2.41652,48.82474],[2.41114,48"
              + ".82489],[2"
              + ".4077,48.82635],[2.40493,48.82844],[2.40247,48.82954],[2.39396,48.82743],[2.38901,48.82512],[2"
              + ".38153,48"
              + ".82242],[2.38076,48.8217],[2.37361,48.81934],[2.36293,48.81608],[2.35615,48.81598],[2.35239,48"
              + ".81856],[2" + ".34717,48.8161],[2.33371,48.81677],[2.3319,48.81701]]");
      for (int i = 0; i < parisShape.length(); i++) {
        JSONArray point = parisShape.getJSONArray(i);
        zonePoints.add(new Point(point.getDouble(0), point.getDouble(1)));
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return new Shape(new Zone(zonePoints.toArray(new Point[0])));
  }
}
