package org.jspresso.hrsample.model.service;

import org.jspresso.framework.model.component.service.AbstractComponentServiceDelegate;
import org.jspresso.framework.util.gui.Dimension;
import org.jspresso.framework.util.gui.map.Point;
import org.jspresso.hrsample.model.City;

public class CityServiceDelegate extends AbstractComponentServiceDelegate<City> implements CityService {

    @Override
    public Point getPoint() {

        City city = getComponent();
        if (city.getLongitude() != null && city.getLatitude() != null) {

            Point p = new Point(city.getLongitude(), city.getLatitude());
            p.setImagePath("/org/jspresso/hrsample/images/city.png");
            p.setImageDimension(new Dimension(24, 24));

            return p;
        }

        return null;
    }
}
