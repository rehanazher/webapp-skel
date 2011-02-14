package com.aaut.skeleton.rbac.exception;

import com.aaut.skeleton.commons.NonCriticalException;

/**
 *
 * @author  fpena
 */
public class UserBlockedException extends NonCriticalException {
    
    private static final long serialVersionUID = 3545515088808393013L;

    public UserBlockedException() {
    }
    
    public UserBlockedException(String msg) {
        super(msg);
    }
    
    public UserBlockedException(String msg, Throwable cause) {
        super(msg, cause);
    }
    
    public UserBlockedException(Throwable cause) {
        super(cause);
    }
}
