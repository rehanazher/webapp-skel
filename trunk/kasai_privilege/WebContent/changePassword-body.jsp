<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<script type='text/javascript' src='<html:rewrite page='/dwr/interface/BaseFacade.js'/>'></script>
<script type='text/javascript' src='<html:rewrite page='/permissions.js'/>'></script>
<script>
	var rowsPerPage = '${rowsPerPage}'; 
	var currentPage = 1;
</script>
<h2>Change password</h2>
<div id='messageBox' class="invisible"></div>

<div id="changePasswordFormWrapper" class="formWrapper">
	<form name='changePasswordForm' class="defaultForm normalText">
		<p>
			<label class='clearing'>Old password</label><input type="password" name="oldPassword"/><br />
			<label class='clearing'>New password</label><input type="password" name="newPassword"/><br />
			<label class='clearing'>Confirmation</label><input type="password" name="confirmation"/><br />
		</p>	
		<div class="horizontalSeparator vMargins"></div>
		<p><a href ="javascript:changePassword();">Change</a></p>
	</form>
</div>

<script>
	
	function changePassword(login){
		startWorking();
		
		BaseFacade.changePassword(document.changePasswordForm.oldPassword.value, document.changePasswordForm.newPassword.value, document.changePasswordForm.confirmation.value, handleChangePassword);
	}
	
	function handleChangePassword(){
		displayMessage('info','Password changed succesfully');
		stopWorking();
	}
</script>