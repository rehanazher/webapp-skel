package com.aaut.skeleton.commons.xml;

import org.w3c.dom.Document;

public interface XmlConvertible
{

    public abstract Document toXML()
        throws XmlException;
}