package com.aaut.skeleton.rbac.exception;

import com.aaut.skeleton.commons.NonCriticalException;

/**
 *
 * @author  fpena
 */
public class NotEnoughPermissionException extends NonCriticalException {
    
    private static final long serialVersionUID = 3257283638881104176L;

    public NotEnoughPermissionException() {
    }
    
    public NotEnoughPermissionException(String msg) {
        super(msg);
    }
    
    public NotEnoughPermissionException(String msg, Throwable cause) {
        super(msg, cause);
    }
    
    public NotEnoughPermissionException(Throwable cause) {
        super(cause);
    }
}
