/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
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
 * <p>
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
 * <p>
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
