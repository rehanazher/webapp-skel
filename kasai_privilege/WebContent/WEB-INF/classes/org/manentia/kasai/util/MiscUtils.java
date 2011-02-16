package org.manentia.kasai.util;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.manentia.commons.xml.XMLException;
import com.manentia.commons.xml.XMLUtil;

public class MiscUtils {

	public static Map parseXMLMap(String xml) throws SAXException, IOException, ParserConfigurationException, FactoryConfigurationError{
		Map result = new HashMap();
		
		if (StringUtils.isNotEmpty(xml)){
			DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			
			Document doc = docBuilder.parse(new InputSource(
					new StringReader(xml)));
			
			Element root = doc.getDocumentElement();
			NodeList rootChilds = root.getChildNodes();
			Element attribute = null;
			Element key = null;
			Element value = null;
			
			for (int i=0;i<rootChilds.getLength(); i++){
				if (rootChilds.item(i).getNodeType() == Node.ELEMENT_NODE){
					attribute = (Element) rootChilds.item(i);
					if (attribute.getNodeName().equals("Attribute")){
						key = (Element) attribute.getElementsByTagName("Key").item(0);
						value = (Element) attribute.getElementsByTagName("Value").item(0);
						
						result.put(XMLUtil.getElementTextContents(key), XMLUtil.getElementTextContents(value));
					}
				}
			}
		}

		return result;
	}
	
	public static String serializeMapToXML(Map map) throws ParserConfigurationException, FactoryConfigurationError{
		String result = "";
		
		if (map != null){
			DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			
			Document doc = docBuilder.newDocument();
			Element root = doc.createElement("Attributes");
			Element attribute = null;
			Element key = null;
			Element value = null;
			Text elementText = null;
			
			doc.appendChild(root);
			
			String keyStr = null;
			String valueStr = null;
			
			for (Iterator iter = map.keySet().iterator(); iter.hasNext(); ){
				keyStr = (String) iter.next();
				valueStr = (String) map.get(keyStr);
				
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
			
			result = XMLUtil.documentToString(doc);
		}
		
		return result;
	}

}
