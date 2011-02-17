<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<h1><a href="#">Kasai: Authentication and authorization framework</a></h1>
<div id="tabNavigation">
	<ul>	
		<li <c:if test="${fn:contains(pageContext.request.requestURL, '/users/')}">id="selectedTab"</c:if>><a href="<html:rewrite page='/users/list.action'/>">Users</a></li>
		<li <c:if test="${fn:contains(pageContext.request.requestURL, '/groups/')}">id="selectedTab"</c:if>><a href="<html:rewrite page='/groups/list.action'/>">Groups</a></li>
		
		<li <c:if test="${fn:contains(pageContext.request.requestURL, '/roles/')}">id="selectedTab"</c:if>><a href="<html:rewrite page='/roles/list.action'/>">Profiles</a></li>
		<li <c:if test="${fn:contains(pageContext.request.requestURL, '/audit/')}">id="selectedTab"</c:if>><a href="<html:rewrite page='/audit/list.action'/>">Auditing</a></li>
	</ul>
</div>
<div id="headerLinks">
	<a href="<html:rewrite page='/changePassword.action'/>">Change password</a> | <a href="<html:rewrite page='/logout.action'/>">Logout</a>
</div>