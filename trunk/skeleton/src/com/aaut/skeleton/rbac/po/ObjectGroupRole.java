package com.aaut.skeleton.rbac.po;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author  James
 *
 */
public class ObjectGroupRole{
    
    private int id;
    private String object;
    private int role;
    private String roleName;
    private String group;
    
    public ObjectGroupRole() {
    }
    
    
    public ObjectGroupRole(ResultSet rs) throws SQLException{
        id = rs.getInt("id");
        object =rs.getString ("id_object");
        role =rs.getInt ("id_role");
        group =rs.getString ("id_group");
        roleName =rs.getString ("role_name");        
    }
    
    public String getObject() {
        return object;
    }    
    
    public void setObject(String object) {
        this.object = object;
    }
    
    public int getRole() {
        return role;
    }
    
    public void setRole(int role) {
        this.role = role;
    }
    
    public String getGroup() {
        return group;
    }
    
    public void setGroup(String group) {
        this.group = group;
    }
    
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public boolean equals (java.lang.Object obj){
        boolean result = false;
        ObjectGroupRole ogr;
        try{
            if (obj instanceof ObjectGroupRole){
                ogr = (ObjectGroupRole)obj; 
                result =  (ogr.getId() == this.id);
                
                if (!result){
                    result = (ogr.getGroup().equals(this.getGroup())) &&
                        (ogr.getObject().equals(this.getObject())) &&
                        (ogr.getRole() == (this.getRole()));
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
