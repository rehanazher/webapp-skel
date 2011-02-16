package com.aaut.skeleton.rbac.po;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.aaut.skeleton.rbac.exception.InvalidAttributesException;


/**
 *
 * @author  James
 *
 */
public class Role implements Serializable {
    
	private static final long serialVersionUID = 5083759729494180017L;
	
	private static Logger logger = Logger.getLogger(Role.class);
	
	private int id;
    
    private String name;
    
    private String description;
    
    private Collection<Operative> operatives;
    
    private Collection<ObjectUserRole> objectsUsersRoles;
    
    private Collection<ObjectGroupRole> objectsGroupsRoles;
    
    public Role() {
        operatives = new ArrayList<Operative> ();
        objectsUsersRoles = new ArrayList<ObjectUserRole>();
        objectsGroupsRoles = new ArrayList<ObjectGroupRole>();
    }
    
    public Role (ResultSet rs) throws SQLException{
        id = rs.getInt("id");
        name = rs.getString ("name");
        description = rs.getString("description");
        operatives = new ArrayList<Operative> ();
        objectsUsersRoles = new ArrayList<ObjectUserRole>();
        objectsGroupsRoles = new ArrayList<ObjectGroupRole>();
        
    }
    
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Collection<Operative> getOperatives() {
        return operatives;
    }
    
    public void setOperatives(Collection<Operative> operatives) {
        this.operatives = operatives;
    }

    public void addOperative(Operative operative) {
        if (operative != null) {
            if(!operatives.contains(operative)){
                this.operatives.add(operative);
            }
        }
    }

    public void removeOperative (Operative operative){
        if (operative != null){
            this.operatives.remove(operative);
        }
    }
    
    public Collection<ObjectGroupRole> getObjectsGroupsRoles(){
        return objectsGroupsRoles;
    }
    
    public void setObjectsGroupsRoles(Collection<ObjectGroupRole> objectGroupRole){
        this.objectsGroupsRoles = objectGroupRole;
    }
    
    public void addObjectGroupRole(ObjectGroupRole objectGroupRole) {
        if (objectGroupRole != null) {
            if(!objectsGroupsRoles.contains(objectGroupRole)){
                this.objectsGroupsRoles.add(objectGroupRole);
            }
        }
    }

    public void removeObjectGroupRole (ObjectGroupRole objectGroupRole){
        if (objectGroupRole != null){
            this.objectsGroupsRoles.remove(objectGroupRole);
        }
    }
    
    public Collection<ObjectUserRole> getObjectsUsersRoles(){
        return objectsUsersRoles;
    }
    
    public void setObjectsUsersRoles(Collection<ObjectUserRole> objectUserRole){
        this.objectsUsersRoles = objectUserRole;
    }
    
    public void addObjectUserRole(ObjectUserRole objectUserRole) {
        if (objectUserRole != null) {
            if(!objectsUsersRoles.contains(objectUserRole)){
                this.objectsUsersRoles.add(objectUserRole);
            }
        }
    }
    
    public String getDescriptionPrefix() {
        String result = StringUtils.defaultString(description);
        
        if (result.length() > 60){
            result = result.substring(0, 57) + "...";
        }
        
        return result;
    }
    
    public String getObjectId() {
        return "/kasai/role/" + this.getId();
    }

    public void removeObjectUserRole (ObjectUserRole objectUserRole){
        if (objectUserRole != null){
            this.objectsUsersRoles.remove(objectUserRole);
        }
    }
    
    public void validate() throws InvalidAttributesException{
        
    	logger.info("Enter");
        
        if ((this.getName() == null) || (this.getName().length()==0)){
        	logger.warn("Name was not specified");
        	
            throw new InvalidAttributesException(Role.class.getName() + ".emptyName");
        }
        
        logger.info("Exit");
    }
    
    public boolean equals (java.lang.Object obj){
        boolean result = false;
        
        try{
            if (obj instanceof Role){
                if (((Role)obj).getId() == this.id){
                    result = true;
                }
            }
        }
        catch (Exception e){
            result = false;
        }
        return result;
    }
}
