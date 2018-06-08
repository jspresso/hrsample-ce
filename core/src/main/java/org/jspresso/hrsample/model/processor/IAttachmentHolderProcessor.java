package org.jspresso.hrsample.model.processor;

import org.jspresso.framework.util.bean.integrity.EmptyCollectionPropertyProcessor;
import org.jspresso.hrsample.model.Attachment;
import org.jspresso.hrsample.model.IAttachmentHolder;

import java.util.Set;

public class IAttachmentHolderProcessor {

    /**
     * Attachments field's collection property processor.
     */
    public static class AttachmentsProcessor extends EmptyCollectionPropertyProcessor<IAttachmentHolder, Set<Object>, Object> {

        @Override
        public void postprocessSetter(IAttachmentHolder target, Set<Object> oldPropertyValue, Set<Object> newPropertyValue) {
            cleanAttachmentsSelection(target);
        }

        @Override
        public void postprocessRemover(IAttachmentHolder target, Set<Object> collection, Object removedValue) {
            cleanAttachmentsSelection(target);
        }

        private void cleanAttachmentsSelection(IAttachmentHolder attacheableHolder) {
            attacheableHolder.setSelectedAttachments(null);
        }
    }

}
