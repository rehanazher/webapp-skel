package com.aaut.skeleton.rbac.exception;

import com.aaut.skeleton.commons.NonCriticalException;

/**
 *
 * @author  fpena
 */
public class InvalidPasswordException extends NonCriticalException {
    
    private static final long serialVersionUID = 3690195464431876406L;

    public InvalidPasswordException() {
    }
    
    public InvalidPasswordException(String msg) {
        super(msg);
    }
    
    public InvalidPasswordException(String msg, Throwable cause) {
        super(msg, cause);
    }
    
    public InvalidPasswordException(Throwable cause) {
        super(cause);
    }
}