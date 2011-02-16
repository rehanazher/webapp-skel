/**
 * Added by James
 * on 2011-2-15
 */
package com.aaut.skeleton.commons;

public class XmlException extends CriticalException {

	private static final long serialVersionUID = 116849920131323024L;

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
