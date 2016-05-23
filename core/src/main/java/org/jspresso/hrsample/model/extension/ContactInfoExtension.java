package org.jspresso.hrsample.model.extension;

import org.jspresso.framework.application.backend.BackendControllerHolder;
import org.jspresso.framework.model.component.AbstractComponentExtension;
import org.jspresso.framework.model.component.service.DependsOn;
import org.jspresso.hrsample.model.ContactInfo;

/**
 * ContactInfo extension.
 */
public class ContactInfoExtension extends AbstractComponentExtension<ContactInfo> {

  /**
   * ContactInfoExtension constructor.
   * @param component The component.
   */
	public ContactInfoExtension(ContactInfo component) {
		super(component);	}

  /**
   * {@inheritDoc}
   * 
   * @return
   */
  /**
   * Gets phone as html.
   * @return the phone number as html.
   */
  @DependsOn({ContactInfo.PHONE})
  public String getPhoneAsHtml() {
    ContactInfo cinfo = getComponent();

    String phone = cinfo.getPhone(); 
    if (phone == null) {
      return null;
    }
    
    if (!BackendControllerHolder.getCurrentBackendController().getClientType().isHTML5()) {
      return phone;
    }

    return "<a href='tel:" + phone +"' style='pointer-events:all;'>" + phone + "</a>";
  }
  
}
