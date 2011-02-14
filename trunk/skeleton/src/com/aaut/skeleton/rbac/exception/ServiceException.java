package com.aaut.skeleton.rbac.exception;

import com.aaut.skeleton.commons.CriticalException;

/**
 *
 * @author  rzuasti
 */
public class ServiceException extends CriticalException {
     
    private static final long serialVersionUID = 4049638988300497974L;

    public ServiceException() {
        super();
    }
        
    public ServiceException(String msg) {
        super(msg);
    }
    
    public ServiceException(String msg, Throwable cause) {
        super(msg, cause);
    }
    
    public ServiceException(Throwable cause) {
        super(cause);
    }
}
