/*
 * AuditHandler.java
 *
 * Created on June 25, 2004, 6:11 PM
 */

package org.manentia.kasai.audit;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.manentia.kasai.Constants;
import org.manentia.kasai.exceptions.CannotAuditException;
import org.manentia.kasai.exceptions.DataAccessException;
import org.w3c.dom.Document;

import com.manentia.commons.NonCriticalException;
import com.manentia.commons.audit.AuditBean;
import com.manentia.commons.log.Log;
import com.manentia.commons.xml.XMLUtil;

/**
 *
 * @author  rzuasti
 */
public class AuditHandler {
    
    public static Collection listEntries(java.util.Date dateFrom,
                                        java.util.Date dateTo,
                                        java.lang.String user,
                                        java.lang.String operation) throws NonCriticalException, DataAccessException {
   
    	Log.write("Enter (dateFrom=" + dateToString(dateFrom) + 
                ", dateTo=" + dateToString(dateTo) +
                ", user=" + StringUtils.defaultString(user, "<null>") +
                ", operation=" + StringUtils.defaultString(operation, "<null>") + ")", 
                Log.INFO, "listEntries", AuditHandler.class);

        ResourceBundle res = ResourceBundle.getBundle(Constants.CONFIG_PROPERTY_FILE);        
        int maxRecords = Integer.parseInt(res.getString("lists.maxTotalRows"));
        Collection result = null;
        
        try {
             result = AuditBean.list(dateFrom, dateTo, user, operation, "kasai_audit", Constants.CONFIG_PROPERTY_FILE, Constants.DATABASE_SOURCE, null, null, maxRecords);
        }catch (SQLException sqle){
        	Log.write("SQL Error", sqle, Log.ERROR, "listEntries", AuditHandler.class);
        	
            throw new DataAccessException(sqle);
        }
        
        Log.write("Exit", Log.INFO, "listEntries", AuditHandler.class);
        
        return result;
    }
    
    public static void createEntry(
        String userId,
        int returnCode,
        String errorDescription,
        long duration,
        String clientIP,
        String operation,
        String objectID,
        Document transactionData) throws CannotAuditException {
    
    		Log.write("Enter (userId="+StringUtils.defaultString(userId, "<null>")+
                    ", returnCode=" + returnCode +
                    ", errorDescription="+StringUtils.defaultString(errorDescription, "<null>")+
                    ", duration=" + duration +
                    ", clientIP="+StringUtils.defaultString(clientIP, "<null>")+
                    ", operation="+StringUtils.defaultString(operation, "<null>")+
                    ", objectID="+StringUtils.defaultString(objectID, "<null>")+
                    ", transactionData="+ (transactionData==null ? "<null>" : "<data>"), 
                    Log.INFO, "createEntry", AuditHandler.class);
    
            if (StringUtils.isEmpty(userId)){
            	Log.write("User was not specified", Log.ERROR, "createEntry", AuditHandler.class);
            	                
                throw new CannotAuditException(AuditHandler.class.getName() + ".createAuditEntry.noUser");
            }
                            
            if (StringUtils.isEmpty(operation)){
            	Log.write("Operation was not specified", Log.ERROR, "createEntry", AuditHandler.class);
            	
                throw new CannotAuditException(AuditHandler.class.getName() + ".createAuditEntry.noOperation");
            }
                
            AuditBean auditBean = new AuditBean("kasai_audit", Constants.CONFIG_PROPERTY_FILE, Constants.DATABASE_SOURCE, null, null);
            auditBean.setClientIP(clientIP);
            auditBean.setDateTime(new java.util.Date());
            auditBean.setDuration(duration);
            auditBean.setErrorDescription(errorDescription);
            auditBean.setOperation(operation);
            auditBean.setReturnCode(returnCode);
            auditBean.setUser(userId);
            auditBean.setProperty("object_id", objectID);
            auditBean.setProperty("transaction_data", XMLUtil.documentToString(transactionData));
            
            try {
                auditBean.commit();
            } catch (NonCriticalException nce){
                throw new CannotAuditException(nce);
            } catch (SQLException  sqle){
            	Log.write("SQL Error", sqle, Log.ERROR, "createEntry", AuditHandler.class);
            	
                throw new CannotAuditException(sqle);
            }
                
            Log.write("Exit", Log.INFO, "createEntry", AuditHandler.class);            
    }
    
    public static String dateToString(java.util.Date date){
        DateFormat format = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss:SSS");
        String result = "<null>";
        
        if (date != null){
            result = format.format(date);
        }
        
        return result;
    }
}
