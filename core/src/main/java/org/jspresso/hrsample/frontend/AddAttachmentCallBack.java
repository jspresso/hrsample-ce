package org.jspresso.hrsample.frontend;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.jspresso.framework.action.ActionException;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.action.AbstractActionContextAware;
import org.jspresso.framework.application.backend.persistence.hibernate.HibernateBackendController;
import org.jspresso.framework.application.backend.session.EMergeMode;
import org.jspresso.framework.application.frontend.action.FrontendAction;
import org.jspresso.framework.application.frontend.file.IFileOpenCallback;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.model.descriptor.basic.BasicBinaryPropertyDescriptor;
import org.jspresso.framework.model.entity.IEntity;
import org.jspresso.framework.model.persistence.hibernate.criterion.EnhancedDetachedCriteria;
import org.jspresso.framework.security.UserPrincipal;
import org.jspresso.framework.util.bean.integrity.IntegrityException;
import org.jspresso.hrsample.model.Attachment;
import org.jspresso.hrsample.model.IAttachmentHolder;
import org.jspresso.hrsample.model.User;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

public class AddAttachmentCallBack extends AbstractActionContextAware implements IFileOpenCallback {

    /**
     * {@inheritDoc}
     */
    @Override
    public void fileChosen(String name, InputStream in, IActionHandler actionHandler, Map<String, Object> context) {

        final HibernateBackendController controller = (HibernateBackendController) getBackendController(context);
        IPropertyDescriptor propertyDescriptor = controller.getEntityFactory().getComponentDescriptor(Attachment.class).getPropertyDescriptor(Attachment.ATTACHED);
        int maxSize = ((BasicBinaryPropertyDescriptor)propertyDescriptor).getMaxLength();

        byte[] data;
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int l;
            do {
                l = (in.read(buffer));
                if (l>0) {
                    out.write(buffer,0,l);
                }
                if (out.size()>maxSize) {
                    throw new IntegrityException("Attachement too large", "message.attachement.too.large", maxSize/1024);
                }
            }
            while (l>0);
            data = out.toByteArray();
        }
        catch (IOException e) {
            throw new ActionException(e);
        }

        if (data==null || data.length == 0)
            return;

        boolean mobile = !controller.getClientType().isDesktop();

        Attachment att = controller.getEntityFactory().createEntityInstance(Attachment.class);
        att.setName(name);
        att.setAttached(data);
        att.setSize((long) data.length);

        User user = getUser(controller);
        if (user!=null)
            att.setAttachedBy(user.getLogin());

        final IAttachmentHolder attachable;
        Object model = getModel(context);
        if (model instanceof IAttachmentHolder) {
            // mobile
            attachable = (IAttachmentHolder) model;
        } else {
            //desktop
            attachable = getParentModel(context);
        }

        Set<Attachment> attachments = new LinkedHashSet<>();
        attachments.add(att);
        attachments.addAll(attachable.getAttachments());

        attachable.setAttachments(attachments);

        // If using a mobile device : save immediatly
        if (mobile) {

            controller.getTransactionTemplate().execute(new TransactionCallback<Object>() {
                @Override
                public Object doInTransaction(TransactionStatus status) {
                    controller.cloneInUnitOfWork((IEntity)attachable);
                    return null;
                }
            });
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void cancel(IActionHandler actionHandler, Map<String, Object> context) {
    }

    private static User getUser(HibernateBackendController controller) {

        UserPrincipal up = controller.getApplicationSession().getPrincipal();
        if (up == null)
            return null;

        Serializable userId = (Serializable) up.getCustomProperty(User.USER_ENTITY_ID);
        DetachedCriteria dc = DetachedCriteria.forClass(User.class);
        dc.add(Restrictions.idEq(userId));

        return controller.findFirstByCriteria(dc, EMergeMode.MERGE_KEEP, User.class);
    }
}
