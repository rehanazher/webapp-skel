/**
 * Added by James
 * on 2011-2-11
 */
package com.aaut.skeleton.commons;

import org.apache.commons.lang.exception.NestableException;

public class NonCriticalException extends NestableException {

	private static final long serialVersionUID = -6917834458312424920L;

	public NonCriticalException()
    {
    }

    public NonCriticalException(String msg)
    {
        super(msg);
    }

    public NonCriticalException(String msg, Throwable cause)
    {
        super(msg, cause);
    }

    public NonCriticalException(Throwable cause)
    {
        super(cause);
    }
}
