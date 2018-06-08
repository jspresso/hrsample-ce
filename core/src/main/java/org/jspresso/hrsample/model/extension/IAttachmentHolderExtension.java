package org.jspresso.hrsample.model.extension;

import org.jspresso.framework.application.backend.BackendControllerHolder;
import org.jspresso.framework.application.backend.IBackendController;
import org.jspresso.framework.model.component.AbstractComponentExtension;
import org.jspresso.framework.model.component.service.DependsOn;
import org.jspresso.hrsample.model.Attachment;
import org.jspresso.hrsample.model.IAttachmentHolder;
import org.jspresso.hrsample.model.IIAttachmentHolderExtension;

import java.util.Set;

public class IAttachmentHolderExtension extends AbstractComponentExtension<IAttachmentHolder> implements IIAttachmentHolderExtension {

    private Set<Attachment> selectedAttachments;

    /**
     * Constructs a new {@code IAttachmentHolderExtension} instance.
     *
     * @param extendedIAttachmentHolder The extended IAttachmentHolder instance.
     */
    public IAttachmentHolderExtension(IAttachmentHolder extendedIAttachmentHolder) {
        super(extendedIAttachmentHolder);
    }

    /**
     * Gets the selectedAttachments.
     *
     * @return the selectedAttachments.
     */
    @Override
    public Set<Attachment> getSelectedAttachments() {
        return selectedAttachments;
    }


    /**
     * Sets the selectedAttachments.
     *
     * @param selectedAttachments the selectedAttachments.
     */
    @Override
    public void setSelectedAttachments(Set<Attachment> selectedAttachments) {
        Set<Attachment> oldValue = this.selectedAttachments;
        this.selectedAttachments = selectedAttachments;
        getComponent().firePropertyChange(IAttachmentHolder.SELECTED_ATTACHMENTS, oldValue, selectedAttachments);
    }


    /**
     * Add to selectedAttachments.
     *
     * @param selectedAttachments the selectedAttachments.
     */
    @Override
    public void addToSelectedAttachments(Attachment selectedAttachments) {
        throw new RuntimeException("Not supported yet");
    }


    /**
     * Remove a selectedAttachments.
     *
     * @param selectedAttachments the selectedAttachments.
     */
    @Override
    public void removeFromSelectedAttachments(Attachment selectedAttachments) {
        throw new RuntimeException("Not supported yet");
    }


    /**
     * Gets the attachmentsLabel.
     *
     * @return the attachmentsLabel.
     */
    @DependsOn(IAttachmentHolder.ATTACHMENTS)
    @Override
    public String getAttachmentsLabel() {
        IBackendController controller = BackendControllerHolder.getCurrentBackendController();
        return controller.getTranslation("attachments.count", new Object[]{getComponent().getAttachments().size()}, controller.getLocale());
    }

}
