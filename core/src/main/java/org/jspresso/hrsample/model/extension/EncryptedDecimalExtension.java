package org.jspresso.hrsample.model.extension;

import org.jspresso.framework.model.component.AbstractComponentExtension;
import org.jspresso.framework.model.component.service.DependsOn;
import org.jspresso.hrsample.model.EncryptedDecimal;
import org.jspresso.hrsample.model.IEncryptedDecimalExtension;

/**
 * EncryptedDecimalExtension
 * User: Maxime HAMM
 * Date: 14/10/2017
 */
public class EncryptedDecimalExtension extends AbstractComponentExtension<EncryptedDecimal> implements IEncryptedDecimalExtension {

    /**
     *  * Constructs a new {@code EncryptedDecimalExtension} instance.
     * <p>
     *  * @param extendedEncryptedDecimal
     *  *          The extended EncryptedDecimal instance.
     *  
     */
    public EncryptedDecimalExtension(EncryptedDecimal extendedEncryptedDecimal) {
        super(extendedEncryptedDecimal);
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @DependsOn(EncryptedDecimal.ENCRYPTED_VALUE)
    public Double getDecryptedValue() {
        byte[] encryptedValue = getComponent().getEncryptedValue();
        if (encryptedValue == null)
            return null;

        return new Double(new String(encryptedValue));
    }

    /**
     * Set decrypted value
     * @param decryptedValue The decrypted value
     */
    public void setDecryptedValue(Double decryptedValue) {
        if (decryptedValue == null)
            getComponent().setEncryptedValue(null);
        else
            getComponent().setEncryptedValue(Double.toHexString(decryptedValue).getBytes());
    }

}
