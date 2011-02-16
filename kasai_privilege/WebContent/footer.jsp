<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<p>
<c:choose>
<c:when test="${fn:contains(pageContext.request.requestURL, '/users/')}">
	<strong>Users</strong>
</c:when>
<c:otherwise>
	<a href="<html:rewrite page='/users/list.action'/>">Users</a>
</c:otherwise>
</c:choose>
&nbsp;|&nbsp;
<c:choose>
<c:when test="${fn:contains(pageContext.request.requestURL, '/groups/')}">
	<strong>Groups</strong>
</c:when>
<c:otherwise>
	<a href="<html:rewrite page='/groups/list.action'/>">Groups</a>
</c:otherwise>
</c:choose>
&nbsp;|&nbsp;
<c:choose>
<c:when test="${fn:contains(pageContext.request.requestURL, '/roles/')}">
	<strong>Profiles</strong>
</c:when>
<c:otherwise>
	<a href="<html:rewrite page='/roles/list.action'/>">Profiles</a>
</c:otherwise>
</c:choose>
&nbsp;|&nbsp;
<c:choose>
<c:when test="${fn:contains(pageContext.request.requestURL, '/audit/')}">
	<strong>Auditing</strong>
</c:when>
<c:otherwise>
	<a href="#">Auditing</a>
</c:otherwise>
</c:choose>
<br/>
<a href="http://www.manentiasoftware.com">&copy; 2003-2007 Manentia Software</a></p>