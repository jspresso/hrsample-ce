package org.jspresso.hrsample.frontend.mobile;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.action.FrontendAction;
import org.jspresso.hrsample.model.Attachment;
import org.jspresso.hrsample.model.IAttachmentHolder;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class RemoveAttachmentFrontAction<E, F, G> extends FrontendAction<E, F, G> {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean execute(IActionHandler actionHandler, Map<String, Object> context) {

        IAttachmentHolder holder = getModel(context);
        Set<Attachment> selectedAttachments = holder.getSelectedAttachments();

        if (selectedAttachments !=null) {
            for (Attachment att : new ArrayList<>(selectedAttachments)) {
                holder.removeFromAttachments(att);
            }
        }

        return super.execute(actionHandler, context);
    }
}
