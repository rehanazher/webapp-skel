package com.aaut.skeleton.rbac.exception;

import com.aaut.skeleton.commons.NonCriticalException;

/**
 *
 * @author  fpena
 */
public class NotFoundException extends NonCriticalException {
    
    private static final long serialVersionUID = 3257282552271287093L;

    public NotFoundException() {
    }
    
    public NotFoundException(String msg) {
        super(msg);
    }
    
    public NotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }
    
    public NotFoundException(Throwable cause) {
        super(cause);
    }
}
