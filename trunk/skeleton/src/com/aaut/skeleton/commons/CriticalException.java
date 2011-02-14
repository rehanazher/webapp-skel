/**
 * Added by James
 * on 2011-2-11
 */
package com.aaut.skeleton.commons;

import org.apache.commons.lang.exception.NestableException;

public class CriticalException extends NestableException {

	private static final long serialVersionUID = 4206951568292322486L;

	public CriticalException()
    {
    }

    public CriticalException(String msg)
    {
        super(msg);
    }

    public CriticalException(String msg, Throwable cause)
    {
        super(msg, cause);
    }

    public CriticalException(Throwable cause)
    {
        super(cause);
    }
}
