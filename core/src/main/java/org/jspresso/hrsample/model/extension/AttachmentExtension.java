package org.jspresso.hrsample.model.extension;

import org.apache.commons.io.FileUtils;
import org.jspresso.framework.model.component.AbstractComponentExtension;
import org.jspresso.framework.model.component.service.DependsOn;
import org.jspresso.framework.util.resources.MemoryResource;
import org.jspresso.framework.util.resources.server.ResourceManager;
import org.jspresso.hrsample.model.Attachment;
import org.jspresso.hrsample.model.IAttachmentExtension;

import static org.jspresso.framework.util.resources.server.ResourceProviderServlet.computeDownloadUrl;

public class AttachmentExtension extends AbstractComponentExtension<Attachment> implements IAttachmentExtension {

    /**
     * Constructs a new {@code AttachmentExtension} instance.
     *
     * @param extendedAttachment The extended Attachment instance.
     */
    public AttachmentExtension(Attachment extendedAttachment) {
        super(extendedAttachment);
    }

    /**
     * Gets the url.
     *
     * @return the url.
     */
    @DependsOn({Attachment.ATTACHED,
            Attachment.NAME})
    @Override
    public String getUrl() {

        final String name = getComponent().getName().replaceAll(" ", "_");
        final String mimeType;
        if (name.endsWith(".doc") || name.endsWith(".docx"))
            mimeType = "application/msword";
        else if (name.endsWith(".xls") || name.endsWith(".xlsx"))
            mimeType = "application/msexcel";
        else if (name.endsWith(".ppt") || name.endsWith(".pptx"))
            mimeType = "application/vnd.ms-powerpoint";
        else if (name.endsWith(".pdf"))
            mimeType = "application/pdf";
        else
            mimeType = null;

        MemoryResource memo = new MemoryResource(getComponent().getName(), mimeType, getComponent().getAttached()) {
            public String getName() {
                return name;
            }
        };

        String memoId = ResourceManager.getInstance().register(memo);
        return computeDownloadUrl(memoId);
    }


    /**
     * Gets the sizeToDisplay.
     *
     * @return the sizeToDisplay.
     */
    @DependsOn(Attachment.SIZE)
    @Override
    public String getSizeToDisplay() {
        Attachment attachment = getComponent();

        Long size = attachment.getSize();
        if (size == null)
            return null;

        return FileUtils.byteCountToDisplaySize(size);
    }

    /**
     * Gets the htmlMobileDescription.
     *
     * @return the htmlMobileDescription.
     */
    @DependsOn({Attachment.ATTACHED_BY,
            Attachment.NAME,
            Attachment.SIZE,
            Attachment.SIZE_TO_DISPLAY})
    @Override
    public String getHtmlMobileDescription() {

        Attachment attachment = getComponent();

        Object bottomLeft = attachment.getAttachedBy();
        Object bottomRight = attachment.getSizeToDisplay();

        return "<table border=0; style=\"width: 100%; white-space: nowrap; table-layout: fixed; margin-right: 15px;\">" +
                "<tr><td colspan=2><b><div>" + attachment.getName() + "</div></b></td></tr>" +
                "<tr><td align='left'; width=100%;><font size=\"-1\">" + (bottomLeft != null ? bottomLeft.toString() : "") + "</font></td>" +
                "<td align='right'; width=100%;><font size=\"-1\">" + (bottomRight != null ? bottomRight.toString() : "") + "</font></td></tr>" +
                "</table>";
    }

}
