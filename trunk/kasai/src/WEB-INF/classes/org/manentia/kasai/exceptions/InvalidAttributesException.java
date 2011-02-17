package org.manentia.kasai.exceptions;

import com.manentia.commons.NonCriticalException;

/**
 *
 * @author  rzuasti
 */
public class InvalidAttributesException extends NonCriticalException {
    
    private static final long serialVersionUID = 3257003237663257650L;

    public InvalidAttributesException() {
        super();
    }
        
    public InvalidAttributesException(String msg) {
        super(msg);
    }
    
    public InvalidAttributesException(String msg, Throwable cause) {
        super(msg, cause);
    }
    
    public InvalidAttributesException(Throwable cause) {
        super(cause);
    }
}
