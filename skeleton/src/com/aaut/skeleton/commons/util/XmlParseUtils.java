/**
 * Added by James
 * on 2011-2-15
 */
package com.aaut.skeleton.commons.util;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.aaut.skeleton.commons.XmlException;
import com.aaut.skeleton.commons.xml.XmlConvertible;


public class XmlParseUtils {
	public static Map<String, String> parseXMLMap(String xml) throws SAXException, IOException,
			ParserConfigurationException, FactoryConfigurationError {
		Map<String, String> result = new HashMap<String, String>();

		if (StringUtils.isNotEmpty(xml)) {
			DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();

			Document doc = docBuilder.parse(new InputSource(new StringReader(
					xml)));

			Element root = doc.getDocumentElement();
			NodeList rootChilds = root.getChildNodes();
			Element attribute = null;
			Element key = null;
			Element value = null;

			for (int i = 0; i < rootChilds.getLength(); i++) {
				if (rootChilds.item(i).getNodeType() == Node.ELEMENT_NODE) {
					attribute = (Element) rootChilds.item(i);
					if (attribute.getNodeName().equals("Attribute")) {
						key = (Element) attribute.getElementsByTagName("Key")
								.item(0);
						value = (Element) attribute.getElementsByTagName(
								"Value").item(0);

						result.put(getElementTextContents(key), getElementTextContents(value));
					}
				}
			}
		}

		return result;
	}

	public static String serializeMapToXML(Map<String, String> map)
			throws ParserConfigurationException, FactoryConfigurationError {
		String result = "";

		if (map != null) {
			DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();

			Document doc = docBuilder.newDocument();
			Element root = doc.createElement("Attributes");
			Element attribute = null;
			Element key = null;
			Element value = null;
			Text elementText = null;

			doc.appendChild(root);

			String keyStr = null;
			String valueStr = null;

			for (Iterator<String> iter = map.keySet().iterator(); iter.hasNext();) {
				keyStr = iter.next();
				valueStr = map.get(keyStr);

				attribute = doc.createElement("Attribute");
				root.appendChild(attribute);

				key = doc.createElement("Key");
				attribute.appendChild(key);
				elementText = doc.createTextNode("keyText");
				elementText.setData(keyStr);
				key.appendChild(elementText);

				value = doc.createElement("Value");
				attribute.appendChild(value);
				elementText = doc.createTextNode("valueText");
				elementText.setData(valueStr);
				value.appendChild(elementText);
			}

			result = documentToString(doc);
		}

		return result;
	}
	
	public static String getElementTextContents(Element element)
    {
        String result = "";
        if(element != null)
        {
            NodeList childs = element.getChildNodes();
            Text text = null;
            CDATASection cdata = null;
            for(int i = 0; i < childs.getLength(); i++)
            {
                if(childs.item(i).getNodeType() == 4)
                {
                    cdata = (CDATASection)childs.item(i);
                    result = result + cdata.getData();
                    continue;
                }
                if(childs.item(i).getNodeType() == 3)
                {
                    text = (Text)childs.item(i);
                    result = result + text.getData();
                }
            }

        }
        return result;
    }

    public static String documentToString(Document doc)
    {
        try
        {
            OutputFormat format = new OutputFormat();
            StringWriter output = null;
            XMLSerializer serializer = null;
            output = new StringWriter();
            serializer = new XMLSerializer(output, format);
            serializer.asDOMSerializer();
            serializer.serialize(doc);
            return output.toString();
        }
        catch(Exception e)
        {
            return "";
        }
    }

    public static String elementToString(Element elem)
    {
        try
        {
            OutputFormat format = new OutputFormat();
            StringWriter output = null;
            XMLSerializer serializer = null;
            output = new StringWriter();
            serializer = new XMLSerializer(output, format);
            serializer.asDOMSerializer();
            serializer.serialize(elem);
            return output.toString();
        }
        catch(Exception e)
        {
            return "";
        }
    }

    public static Document serializeCollection(Collection<XmlConvertible> col)
        throws XmlException
    {
        return serializeCollection(col, "list");
    }

    public static Document serializeCollection(Collection<XmlConvertible> col, String collectionName)
        throws XmlException
    {
        Document result = null;
        Iterator<XmlConvertible> iter = null;
        try
        {
            result = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Element root = result.createElement(collectionName);
            result.appendChild(root);
            if(col != null)
            {
                Element element;
                for(iter = col.iterator(); iter.hasNext(); root.appendChild(result.importNode(element, true)))
                    element = iter.next().toXML().getDocumentElement();

            }
        }
        catch(Exception e)
        {
            throw new XmlException("Error serializing collection", e);
        }
        return result;
    }
}
