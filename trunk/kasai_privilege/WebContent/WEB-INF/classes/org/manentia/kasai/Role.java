package org.manentia.kasai;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.manentia.kasai.exceptions.InvalidAttributesException;

import com.manentia.commons.log.Log;


/**
 *
 * @author  fpena
 *
 */
public class Role implements Serializable {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 3282853957984740860L;

	private int id;
    
    private String name;
    
    private String description;
    
    private Collection operatives;
    
    private Collection objectsUsersRoles;
    
    private Collection objectsGroupsRoles;
    
    public Role() {
        operatives = new ArrayList ();
        objectsUsersRoles = new ArrayList();
        objectsGroupsRoles = new ArrayList();
    }
    
    public Role (ResultSet rs) throws SQLException{
        id = rs.getInt("id");
        name = rs.getString ("name");
        description = rs.getString("description");
        operatives = new ArrayList ();
        objectsUsersRoles = new ArrayList();
        objectsGroupsRoles = new ArrayList();
        
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
    
    public Collection getOperatives() {
        return operatives;
    }
    
    public void setOperatives(Collection operatives) {
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
    
    public Collection getObjectsGroupsRoles(){
        return objectsGroupsRoles;
    }
    
    public void setObjectsGroupsRoles(Collection objectGroupRole){
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
    
    public Collection getObjectsUsersRoles(){
        return objectsUsersRoles;
    }
    
    public void setObjectsUsersRoles(Collection objectUserRole){
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
        
    	Log.write("Enter", Log.INFO, "validate", Role.class);
        
        if ((this.getName() == null) || (this.getName().length()==0)){
        	Log.write("Name was not specified", Log.WARN, "validate", Role.class);
        	
            throw new InvalidAttributesException(Role.class.getName() + ".emptyName");
        }
        
        Log.write("Exit", Log.INFO, "validate", Role.class);
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
