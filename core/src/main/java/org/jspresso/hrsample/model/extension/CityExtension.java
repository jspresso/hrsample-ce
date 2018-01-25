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
import java.util.Arrays;
import java.util.List;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import org.jspresso.framework.model.component.AbstractComponentExtension;
import org.jspresso.framework.model.component.service.DependsOn;
import org.jspresso.framework.util.exception.NestedRuntimeException;
import org.jspresso.framework.util.gui.Dimension;
import org.jspresso.framework.util.resources.server.ResourceProviderServlet;
import org.jspresso.framework.view.descriptor.IMapViewDescriptor;

import org.jspresso.hrsample.model.City;
import org.jspresso.hrsample.model.ICityExtension;

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
  @DependsOn({City.LATITUDE, City.LONGITUDE, City.ROUTES})
  public String getMapContent() {
    City city = getComponent();

    try {
      JSONObject mapContent = new JSONObject();
      if (city.getLongitude() != null && city.getLatitude() != null) {
        JSONObject marker = new JSONObject();
        marker.put(IMapViewDescriptor.MARKER_COORD_KEY, Arrays.asList(city.getLongitude(), city.getLatitude()));
        JSONObject image = new JSONObject();
        image.put("src", ResourceProviderServlet
            .computeImageResourceDownloadUrl("classpath:/org/jspresso/hrsample/images/city.png",
                new Dimension(24, 24)));
        marker.put(IMapViewDescriptor.MARKER_IMAGE_KEY, image);
        mapContent.put(IMapViewDescriptor.MARKERS_KEY, Arrays.asList(marker));
      }
      double[][][] routes = city.getRoutes();
      if (routes != null && routes.length > 0) {
        List<JSONObject> routesList = new ArrayList<>();
        for (int i = 0; i < routes.length; i++) {
          double[][] routePoints = routes[i];
          List<List<Double>> routePath = new ArrayList<>();
          for (int j = 0; j < routePoints.length; j++) {
            routePath.add(Arrays.asList(routePoints[j][0], routePoints[j][1]));
          }
          JSONObject route = new JSONObject();
          route.put(IMapViewDescriptor.ROUTE_PATH_KEY, routePath);
          JSONObject routeStyle = new JSONObject();
          routeStyle.put("color", "#9F78FF");
          routeStyle.put("width", 4);
          route.put(IMapViewDescriptor.ROUTE_STYLE_KEY, routeStyle);
          routesList.add(route);
        }
        mapContent.put(IMapViewDescriptor.ROUTES_KEY, routesList);
      }
      return mapContent.toString(2);
    } catch (JSONException ex) {
      throw new NestedRuntimeException(ex);
    }
  }
}
