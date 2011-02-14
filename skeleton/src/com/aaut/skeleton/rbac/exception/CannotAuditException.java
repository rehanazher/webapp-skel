package com.aaut.skeleton.rbac.exception;

import com.aaut.skeleton.commons.CriticalException;

/**
 *
 * @author  rzuasti
 */
public class CannotAuditException extends CriticalException {
     
    /**
     * 
     */
    private static final long serialVersionUID = 3258413915410216249L;

    public CannotAuditException() {
        super();
    }
        
    public CannotAuditException(String msg) {
        super(msg);
    }
    
    public CannotAuditException(String msg, Throwable cause) {
        super(msg, cause);
    }
    
    public CannotAuditException(Throwable cause) {
        super(cause);
    }
}
