package org.manentia.kasai.ui.audit;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.commons.lang.StringUtils;
import org.manentia.kasai.KasaiFacade;
import org.manentia.kasai.audit.AuditHandler;
import org.manentia.kasai.exceptions.CannotAuditException;
import org.manentia.kasai.exceptions.DataAccessException;
import org.manentia.kasai.ui.UserView;
import org.manentia.kasai.ui.actions.BaseAction;
import org.manentia.kasai.ui.exceptions.SessionExpiredException;

import uk.ltd.getahead.dwr.WebContext;
import uk.ltd.getahead.dwr.WebContextFactory;

import com.manentia.commons.NonCriticalException;
import com.manentia.commons.audit.AuditBean;
import com.manentia.commons.log.Log;
import com.manentia.commons.xml.XMLException;

public class AuditFacade {
	private String lastSortOrder = null;
	
	public Map refresh(int page, String dateFrom, int dateFromHour, int dateFromMinute,
			String dateTo, int dateToHour, int dateToMinute,
			String user, String operation) throws SessionExpiredException, DataAccessException, XMLException, CannotAuditException, NonCriticalException{
		
		WebContext ctx = WebContextFactory.get();
		HttpServletRequest request = ctx.getHttpServletRequest();
		
		UserView userView = new BaseAction().validateSession(request);
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		Date dateFromDate = null;
		try {
			if (StringUtils.isNotEmpty(dateFrom)){
				dateFromDate = dateFormat.parse(dateFrom);
				GregorianCalendar dateFromCal = new GregorianCalendar();
				dateFromCal.setTime(dateFromDate);
				dateFromCal.set(Calendar.HOUR_OF_DAY, 0);
				dateFromCal.set(Calendar.MINUTE, 0);
				dateFromCal.set(Calendar.SECOND, 0);
				dateFromCal.set(Calendar.MILLISECOND, 0);
				
				if (dateFromHour>=0){
					
					dateFromCal.set(Calendar.HOUR_OF_DAY, dateFromHour);
					if (dateFromMinute>=0){
						dateFromCal.set(Calendar.MINUTE, dateFromMinute);
					}
				}
				dateFromDate = dateFromCal.getTime();
				
			}
		} catch (Exception e){}
		
		
		Date dateToDate = null;
		try {
			if (StringUtils.isNotEmpty(dateTo)){
				dateToDate = dateFormat.parse(dateTo);
				GregorianCalendar dateToCal = new GregorianCalendar();
				dateToCal.setTime(dateToDate);
				dateToCal.set(Calendar.HOUR_OF_DAY, 23);
				dateToCal.set(Calendar.MINUTE, 59);
				dateToCal.set(Calendar.SECOND, 59);
				dateToCal.set(Calendar.MILLISECOND, 999);
				
				if (dateToHour>=0){
					dateToCal.set(Calendar.HOUR_OF_DAY, dateToHour);
					if (dateToMinute>=0){
						dateToCal.set(Calendar.MINUTE, dateToMinute);
					}
				}
				
				dateToDate = dateToCal.getTime();
			}
		} catch (Exception e){}
		
		Collection<AuditBean> entries = KasaiFacade.getInstance().listAuditEntries(userView.getUser(), dateFromDate, dateToDate, user,
				operation, request.getRemoteAddr());
		        
        request.getSession().setAttribute("entries", entries);
        
        return goToPageEntries(page);
	}
	
	public String getEntryDetails(int entryId) throws XMLException, IOException{
		WebContext ctx = WebContextFactory.get();
		HttpServletRequest request = ctx.getHttpServletRequest();
		
		String result = null;
		
		AuditBean entry = null;
		Collection<AuditBean> entries = (Collection<AuditBean>) request.getSession().getAttribute("entries");
		
		for (AuditBean currentEntry : entries) {
			if (currentEntry.getId()==entryId){
				entry = currentEntry;
				break;
			}
		}
		
		if (entry != null){
			result = (String) entry.getProperty("transaction_data");
		}
		
		return result;		
	}
	 
	public Map goToPageEntries(int page){
		ArrayList result = null;
		WebContext ctx = WebContextFactory.get();
		HttpServletRequest request = ctx.getHttpServletRequest();
		
		Map map = new HashMap();
		
		Collection entries = (List) request.getSession().getAttribute("entries");
        int rowsPerPage = ((Integer) request.getSession().getAttribute("rowsPerPage")).intValue();
        
        result = new ArrayList(rowsPerPage);
        int index = 0;
        for (Iterator iter=entries.iterator(); iter.hasNext() && index<rowsPerPage*page;){
        	AuditBean entry = (AuditBean) iter.next();
        	if (index>=rowsPerPage*(page-1) && index<rowsPerPage*page){
        		result.add(entry);
        	}
        	index++;
        }
        
        map.put("list", result);
        map.put("totalSize", new Integer(entries.size()));
        
        return map;
	}
	
	public Map sortEntries(String sortOrder) {
		WebContext ctx = WebContextFactory.get();
		HttpServletRequest request = ctx.getHttpServletRequest();
		
		if (StringUtils.isEmpty(sortOrder)){
			sortOrder = "dateFrom";
		}
		
		boolean reverse = sortOrder.equalsIgnoreCase(lastSortOrder);
		Comparator comparator = new BeanComparator(sortOrder);
	
		if (reverse){
			comparator = new ReverseComparator(comparator);
		}
		
		List entries = (List) request.getSession().getAttribute("entries");
		Collections.sort(entries, comparator);
		
		lastSortOrder = reverse ? "__CHANGE__" : sortOrder;
		
		return goToPageEntries(1);
	}
	
	
}
