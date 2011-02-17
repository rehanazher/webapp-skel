package org.manentia.kasai.exceptions;

import com.manentia.commons.NonCriticalException;

/**
 *
 * @author  rzuasti
 */
public class AlreadyExistsException extends NonCriticalException {
     
    private static final long serialVersionUID = 3257848779335349809L;

    public AlreadyExistsException() {
        super();
    }
        
    public AlreadyExistsException(String msg) {
        super(msg);
    }
    
    public AlreadyExistsException(String msg, Throwable cause) {
        super(msg, cause);
    }
    
    public AlreadyExistsException(Throwable cause) {
        super(cause);
    }
}
