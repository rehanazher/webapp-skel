<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html:html locale="true" xhtml="true">
	 <head>
        <title>Kasai: Authentication and authorization framework</title>
        <link href="<html:rewrite page='/mainStyle.css'/>" rel="stylesheet" type="text/css" />
        <tiles:insert attribute='declarations'/>
		<script type='text/javascript' src='<html:rewrite page='/dwr/engine.js'/>'></script>
		<script type='text/javascript' src='<html:rewrite page='/dwr/util.js'/>'></script>
		<script type='text/javascript' src='<html:rewrite page='/dwr/interface/BaseFacade.js'/>'></script>
    	<script type='text/javascript' src='<html:rewrite page='/util.js'/>'></script>
    </head>
    <body>
    	<div id="working" class="invisible">working...</div>
    	<div id="header"><tiles:insert page="/toolbar.jsp"/></div>
    	<div id="content"><tiles:insert attribute='body'/></div>
    	<div id="footer"><tiles:insert page="/footer.jsp"/></div>
    </body>
</html:html>