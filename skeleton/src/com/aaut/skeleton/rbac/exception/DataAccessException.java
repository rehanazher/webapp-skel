package com.aaut.skeleton.rbac.exception;

import com.aaut.skeleton.commons.CriticalException;

/**
 *
 * @author  rzuasti
 */
public class DataAccessException extends CriticalException {
     
    private static final long serialVersionUID = 3257291314021478964L;

    public DataAccessException() {
        super();
    }
        
    public DataAccessException(String msg) {
        super(msg);
    }
    
    public DataAccessException(String msg, Throwable cause) {
        super(msg, cause);
    }
    
    public DataAccessException(Throwable cause) {
        super(cause);
    }
}
