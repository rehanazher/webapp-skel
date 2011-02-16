package org.manentia.kasai.util;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.manentia.kasai.Constants;

import com.manentia.commons.CriticalException;
import com.manentia.commons.NonCriticalException;


/**
 * DOCUMENT ME!
 *
 * @author mpena
 */
public class ExceptionUtil {

    //~ Metodos --------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param msg DOCUMENT ME!
     *
     * @throws CriticalException DOCUMENT ME!
     */
    public static void throwCriticalException(String msg)
        throws CriticalException {
        try {

            ResourceBundle messages = ResourceBundle.getBundle(Constants.MESSAGES_PROPERTY_FILE);

            throw new CriticalException(messages.getString(msg));
        } catch (MissingResourceException mre) {
            throw new CriticalException(msg);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param msg DOCUMENT ME!
     * @param cause DOCUMENT ME!
     *
     * @throws CriticalException DOCUMENT ME!
     */
    public static void throwCriticalException(String msg, Throwable cause)
        throws CriticalException {
        try {

            ResourceBundle messages = ResourceBundle.getBundle(Constants.MESSAGES_PROPERTY_FILE);

            throw new CriticalException(messages.getString(msg), cause);
        } catch (MissingResourceException mre) {
            throw new CriticalException(msg, cause);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param msg DOCUMENT ME!
     *
     * @throws NonCriticalException DOCUMENT ME!
     */
    public static void throwNonCriticalException(String msg)
        throws NonCriticalException {
        try {

            ResourceBundle messages = ResourceBundle.getBundle(Constants.MESSAGES_PROPERTY_FILE);

            throw new NonCriticalException(messages.getString(msg));
        } catch (MissingResourceException mre) {
            throw new NonCriticalException(msg);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param msg DOCUMENT ME!
     * @param cause DOCUMENT ME!
     *
     * @throws NonCriticalException DOCUMENT ME!
     */
    public static void throwNonCriticalException(String msg, Throwable cause)
        throws NonCriticalException {
        try {

            ResourceBundle messages = ResourceBundle.getBundle(Constants.MESSAGES_PROPERTY_FILE);

            throw new NonCriticalException(messages.getString(msg), cause);
        } catch (MissingResourceException mre) {
            throw new NonCriticalException(msg, cause);
        }
    }
}
