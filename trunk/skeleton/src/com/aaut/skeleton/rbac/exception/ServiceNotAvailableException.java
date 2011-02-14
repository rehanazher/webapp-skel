package com.aaut.skeleton.rbac.exception;

import com.aaut.skeleton.commons.CriticalException;

/**
 *
 * @author  rzuasti
 */
public class ServiceNotAvailableException extends CriticalException {
     
    private static final long serialVersionUID = 3256719572219213108L;

    public ServiceNotAvailableException() {
        super();
    }
        
    public ServiceNotAvailableException(String msg) {
        super(msg);
    }
    
    public ServiceNotAvailableException(String msg, Throwable cause) {
        super(msg, cause);
    }
    
    public ServiceNotAvailableException(Throwable cause) {
        super(cause);
    }
}
