package org.manentia.kasai;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author  fpena
 *
 */
public class ObjectUserRole{
    
    private int id;
    private String objectId;
    private int role;
    private String roleName;
    private String user;
    
    public ObjectUserRole() {
    }
    
    public ObjectUserRole(ResultSet rs) throws SQLException{
        id = rs.getInt("id");
        objectId =rs.getString ("id_object");
        role =rs.getInt ("id_role");
        user =rs.getString ("id_user");
        roleName =rs.getString ("role_name");        
    }
    
    public String getObjectId() {
        return objectId;
    }    
    
    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }
    
    public int getRole() {
        return role;
    }
    
    public void setRole(int role) {
        this.role = role;
    }
    
    public String getUser() {
        return user;
    }
    
    public void setUser(String user) {
        this.user = user;
    }

    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public boolean equals (java.lang.Object obj){
        boolean result = false;
        ObjectUserRole our;
        try{
            if (obj instanceof ObjectUserRole){
                our = (ObjectUserRole)obj; 
                result =  (our.getId() == this.id);
                
                if (!result){
                    result = (our.getUser().equals(this.getUser())) &&
                        (our.getObjectId().equals(this.getObjectId())) &&
                        (our.getRole() == (this.getRole()));
                }
            }
            
        }
        catch (Exception e){
            result = false;
        }
        return result;
    }
    
    public java.lang.String getRoleName() {
        return roleName;
    }
    
    public void setRoleName(java.lang.String roleName) {
        this.roleName = roleName;
    }
    
}