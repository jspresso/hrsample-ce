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
package org.jspresso.hrsample.chart;

import java.sql.Connection;
import java.util.Locale;
import java.util.Random;

import org.jspresso.framework.application.charting.descriptor.AbstractChartDescriptor;
import org.jspresso.framework.util.i18n.ITranslationProvider;
import org.jspresso.hrsample.model.Company;
import org.jspresso.hrsample.model.Employee;

/**
 * The company sample chart.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class CompanyChart extends AbstractChartDescriptor {

  /**
   * {@inheritDoc}
   */
  public String getData(Object model,
      @SuppressWarnings("unused") Connection jdbcConnection,
      ITranslationProvider translationProvider, Locale locale) {
    Company company = (Company) model;
    StringBuffer chartData = new StringBuffer("<graph caption='"
        + translationProvider.getTranslation(getTitle(), locale)
        + "' xAxisName='"
        + translationProvider.getTranslation(Employee.class.getName(), locale)
        + "' yAxisName='" + translationProvider.getTranslation("age", locale)
        + "' showNames='1' decimalPrecision='0' formatNumberScale='0'>");
    for (Employee emp : company.getEmployees()) {
      Random r = new Random();
      String color = Integer.toHexString(r.nextInt(255))
          + Integer.toHexString(r.nextInt(255))
          + Integer.toHexString(r.nextInt(255));
      chartData.append("<set name='" + emp.getName() + "' value='"
          + emp.getAge().intValue() + "' color='" + color + "' />");
    }
    chartData.append("</graph>");
    return chartData.toString();
  }
}
