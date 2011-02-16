package org.manentia.kasai;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;


/**
 *
 * @author  fpena
 *
 */
public class AuthObject {
    
    private String id;
    
    private Collection objectsUsersRoles;
    
    private Collection objectsGroupsRoles;
    
    /** Creates a new instance of Object */
    public AuthObject() {
        objectsUsersRoles = new ArrayList();
        objectsGroupsRoles = new ArrayList();
    }
    
    public AuthObject (ResultSet rs) throws SQLException{
        id = rs.getString("id");
    }
    
    public String getId() {
        return this.id;
    }
    
    public void setId(String id) {
        this.id = id;
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

    public void removeObjectUserRole (ObjectUserRole objectUserRole){
        if (objectUserRole != null){
            this.objectsUsersRoles.remove(objectUserRole);
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
    
    public boolean equals (java.lang.Object obj){
        boolean result = false;
        
        try{
            if (obj instanceof AuthObject){
                if (((AuthObject)obj).getId().equals (this.id)){
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
