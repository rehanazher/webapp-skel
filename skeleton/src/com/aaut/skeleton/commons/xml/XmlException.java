/**
 * Added by James
 * on 2011-2-16
 */
package com.aaut.skeleton.commons.xml;

import com.aaut.skeleton.commons.CriticalException;

public class XmlException extends CriticalException {

	private static final long serialVersionUID = -1189201341081935506L;

	public XmlException() {
	}

	public XmlException(String msg) {
		super(msg);
	}

	public XmlException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public XmlException(Throwable cause) {
		super(cause);
	}

}
