package org.manentia.kasai.exceptions;

import com.manentia.commons.NonCriticalException;


/**
 *
 * @author  rzuasti
 */
public class DoesntExistsException extends NonCriticalException {
     
    private static final long serialVersionUID = 3258410642594871602L;

    public DoesntExistsException() {
        super();
    }
        
    public DoesntExistsException(String msg) {
        super(msg);
    }
    
    public DoesntExistsException(String msg, Throwable cause) {
        super(msg, cause);
    }
    
    public DoesntExistsException(Throwable cause) {
        super(cause);
    }
}
