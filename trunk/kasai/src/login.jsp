<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>Kasai: Authentication and authorization framework</title>
    <link href="<html:rewrite page='/mainStyle.css'/>" rel="stylesheet" type="text/css" />
    <link href="<html:rewrite page='/login.css'/>" rel="stylesheet" type="text/css" />
    <script type='text/javascript' src='<html:rewrite page='/dwr/engine.js'/>'></script>
	<script type='text/javascript' src='<html:rewrite page='/dwr/util.js'/>'></script>
	<script type='text/javascript' src='<html:rewrite page='/dwr/interface/BaseFacade.js'/>'></script>
	<script type='text/javascript' src='<html:rewrite page='/util.js'/>'></script>
</head>

<body id="loginBody">
	<div id="loginWrapper">
		<h1>Kasai: Authentication and authorization framework</h1>
		<div id="loginBox">			
			<form id="loginForm" name="loginForm" onsubmit="login(); return false;">
				<h2>Write your login and password to continue</h2>
				<div id='messageBox' class="invisible"></div>
				<label for="loginName">Login</label><input type="text" name="username" id="loginUser"/><br />
				<label for="loginPassword">Password</label><input type="password" name="password" id="loginPassword" /><br />
				<label for="enterButton">&nbsp;</label><input name="enterButton" type="submit" value="Enter" />
			</form>
			<div id="working" class="invisible">working...</div>
		</div>
	</div>
	<script> 
	function login(){
		startWorking();
		BaseFacade.login(document.loginForm.username.value, document.loginForm.password.value, {callback: handleLogin});
	}
	
	function handleLogin(){
		stopWorking();
		location.href="<html:rewrite page='/loadParams.action'/>";
	}
	
	document.loginForm.username.focus();
	</script>
</body>
</html>