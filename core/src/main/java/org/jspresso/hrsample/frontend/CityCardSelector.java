package org.jspresso.hrsample.frontend;

import org.jspresso.framework.view.descriptor.ICardNameSelector;
import org.jspresso.hrsample.model.City;

import javax.security.auth.Subject;

/**
 * CityCardSelector
 * User: Maxime HAMM
 * Date: 10/01/2018
 */
public class CityCardSelector implements ICardNameSelector {

    private static final String MAP = "MAP";
    private static final String NOMAP = "NOMAP";

    public String getCardNameForModel(Object model, Subject subject) {
        if (model == null) {
            return null;
        }
        City city = (City)model;
        if (city.getLongitude() !=null && city.getLatitude() != null) {
            return MAP;
        }
        else {
            return NOMAP;
        }
    }
}
