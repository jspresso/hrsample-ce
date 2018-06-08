package org.jspresso.hrsample.frontend;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.action.FrontendAction;
import org.jspresso.hrsample.model.Attachment;

import java.util.Map;

public class OpenAttachementAction<E, F, G> extends FrontendAction<E, F, G> {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean execute(IActionHandler actionHandler, Map<String, Object> context) {

        Attachment att = getSelectedModel(context);
        if (att != null) {
            setActionParameter(att.getUrl(), context);
            return super.execute(actionHandler, context);
        }

        return false;
    }
}
