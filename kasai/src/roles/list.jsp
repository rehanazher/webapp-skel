<%@taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<tiles:insert page="/template-standard.jsp" flush="true">
    <tiles:put name="body" value="/roles/list-body.jsp" />
    <tiles:put name="declarations" type="string">    
	    <link href="<html:rewrite page='/roles/roles.css'/>" rel="stylesheet" type="text/css" />
	</tiles:put>
</tiles:insert>