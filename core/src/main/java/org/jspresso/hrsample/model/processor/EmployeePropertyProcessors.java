/*
 * Copyright (c) 2005-2013 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.hrsample.model.processor;

import java.util.Date;

import org.jspresso.framework.util.bean.integrity.EmptyPropertyProcessor;
import org.jspresso.framework.util.bean.integrity.IntegrityException;

import org.jspresso.hrsample.model.Employee;

/**
 * Employee property processors.
 *
 * @author Vincent Vandenschrick
 */
public class EmployeePropertyProcessors {

  /**
   * Birth date property processor.
   */
  public static class BirthDateProcessor extends EmptyPropertyProcessor<Employee, Date> {

    /**
     * Checks that the employee age is at least 18.
     * <p/>
     * {@inheritDoc}
     */
    @Override
    public void preprocessSetter(Employee employee, Date newBirthDate) {
      if (newBirthDate == null || employee.computeAge(newBirthDate) < 18) {
        throw new IntegrityException("Age is below 18", "age.below.18");
      }
    }
  }

  /**
   * First name property processor.
   */
  public static class FirstNameProcessor extends EmptyPropertyProcessor<Employee, String> {

    /**
     * Formats the new first name. The formatting is : <li>Capitalize the 1st
     * letter <li>Lower case all the other letters
     * <p/>
     * {@inheritDoc}
     */
    @Override
    public String interceptSetter(Employee employee, String newFirstName) {
      if (newFirstName != null && newFirstName.length() > 0) {
        StringBuilder formattedName = new StringBuilder();
        formattedName.append(newFirstName.substring(0, 1).toUpperCase());
        formattedName.append(newFirstName.substring(1).toLowerCase());
        return formattedName.toString();
      }
      return super.interceptSetter(employee, newFirstName);
    }
  }

  /**
   * Photo property processor.
   */
  public static class PhotoProcessor extends EmptyPropertyProcessor<Employee, byte[]> {

    /**
     * Resize the photo.
     * <p/>
     * {@inheritDoc}
     */
    @Override
    public byte[] interceptSetter(Employee employee, byte[] newPhoto) {
//      try {
//        return ImageHelper.scaleImage(newPhoto, 100, -1);
//      } catch (IOException ioe) {
//        return super.interceptSetter(employee, newPhoto);
//      }, scaledWidth:150
      return super.interceptSetter(employee, newPhoto);
    }
  }
}
