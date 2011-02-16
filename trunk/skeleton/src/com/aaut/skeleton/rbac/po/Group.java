package com.aaut.skeleton.rbac.po;

import java.io.IOException;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import com.aaut.skeleton.commons.util.XmlParseUtils;
import com.aaut.skeleton.rbac.exception.InvalidAttributesException;

/**
 *
 * @author  James
 *
 */

public class Group implements Serializable {

	private static final long serialVersionUID = -2538368494729355971L;
	
	private static Logger logger = Logger.getLogger(Group.class);

	private String id;

    private boolean blocked;

    private String description;

    private boolean system;
    
    private Map<String, String> attributes;
    
    private String[] members;
    
    
    public Group() {
    	attributes = new HashMap<String, String>();
    }

    public Group (ResultSet rs) throws SQLException, SAXException, IOException, ParserConfigurationException, FactoryConfigurationError{
        id = rs.getString("id");
        blocked = (rs.getInt("blocked")!=0);
        description = rs.getString("description");
        system = (rs.getInt("system")!=0);
        attributes = XmlParseUtils.parseXMLMap(rs.getString("data"));
    }
    
    public void setAttribute(String key, String value){
        this.attributes.put(key, value);
    }

    public String getAttribute(String key){
        return attributes.get(key);
    }
    
    public String getAttributesXML() throws ParserConfigurationException, FactoryConfigurationError{
    	return XmlParseUtils.serializeMapToXML(attributes);
    }
    
    public boolean getBlocked() {
        return this.blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void validate()  throws InvalidAttributesException{
    	
    	logger.info("Enter");
    	
        if ((this.getId() == null) || (this.getId().length()==0)){
        	logger.warn("Id was not specified");
        	
            throw new InvalidAttributesException(Group.class.getName() + ".emptyId");
        }
        
        logger.info("Exit");
    }
    
    public boolean equals (java.lang.Object obj){
        boolean result = false;
        
        try{
            if (obj instanceof Group){
                if (((Group)obj).getId().equals (this.id)){
                    result = true;
                }
            }
        }
        catch (Exception e){
            result = false;
        }
        return result;
    }
    
    public boolean getSystem() {
        return this.system;
    }
    
    public void setSystem(boolean system) {
        this.system = system;
    }
    
    public String getDescriptionPrefix() {
        String result = StringUtils.defaultString(description);
        
        if (result.length() > 60){
            result = result.substring(0, 57) + "...";
        }
        
        return result;
    }
    
    public String getObjectId() {
        return "/kasai/group/" + this.getId();
    }

	public String[] getMembers() {
		return members;
	}

	public void setMembers(String[] members) {
		this.members = members;
	}
    
}