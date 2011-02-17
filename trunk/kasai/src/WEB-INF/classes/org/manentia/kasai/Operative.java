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
public class Operative{

    private String id;

    private int sequence;

    private String description;
    private Collection roles;
    
    public Operative(){
        roles = new ArrayList();
    }
    
    public Operative(ResultSet rs) throws SQLException{
        roles = new ArrayList();
        id = rs.getString ("id");
        sequence = rs.getInt("sequence");
        description = rs.getString("description");
    }
    
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getSequence() {
        return this.sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
       
    public Collection getRoles() {
        return roles;
    }

    public void setRoles(Collection roles) {
        this.roles = roles;
    }
    
    public void addRole(Role role) {
        if (role != null) {
            if(!roles.contains(role)){
                this.roles.add(role);
            }
        }
    }

    public void removeRole (Role role){
        if (role != null){
            this.roles.remove(role);
        }
    }
    
    public boolean equals (java.lang.Object obj){
        boolean result = false;
        
        try{
            if (obj instanceof Operative){
                if (((Operative)obj).getId().equals (this.id)){
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
