<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<script type='text/javascript' src='<html:rewrite page='/dwr/interface/UserFacade.js'/>'></script>
<script type='text/javascript' src='<html:rewrite page='/permissions.js'/>'></script>
<script>
	var rowsPerPage = '${rowsPerPage}'; 
	var currentPage = 1;
</script>
<h2>User management</h2>
<div id='messageBox' class="invisible"></div>

<div id="buttonEnclosing">
	<a id="buttonAddUser" href="javascript:displayNewUserForm();">Add user</a>
	<a id="buttonSearchUser" href="javascript:displaySearchUsersForm();">Search user</a>
</div>

<div id="addUserFormWrapper" class="invisible formWrapper">
	<h3 class="formHeading">Add user</h3>
	<a href="javascript:hideNewUserForm();" class="buttonFormClose">Close</a>
	<div class="horizontalSeparator hMargins"></div>
	<form name='newUserForm' class="defaultForm normalText">
		<p>
			<label class='clearing'>User name</label><input type="text" name="login"/><br />
			<label class='clearing'>First name</label><input type="text" name="firstName"/><br />
			<label class='clearing'>Last name</label><input type="text" name="lastName"/><br />
			<label class='clearing'>E-mail</label><input type="text" name="eMail" /><br />			
			<label class='clearing'>Description</label><textarea rows="4" cols="40" name="description"></textarea><br />
			<label class='clearing'>Super user</label><input type="checkbox" name="superUser"/><br />
			<label class='clearing'>Blocked</label><input type="checkbox" name="blocked"/>
		</p>	
		<div class="horizontalSeparator vMargins"></div>
		<p><a href ="javascript:createNewUser();">Add user</a> | <a href="javascript:hideNewUserForm();">Cancel</a></p>
	</form>
</div>

<div id="searchUserFormWrapper" class="invisible formWrapper">
	<h3 class="formHeading">Search user</h3>
	<a href="javascript:hideSearchUsersForm();" class="buttonFormClose">Close</a>
	<div class="horizontalSeparator hMargins"></div>
	<form name='searchUsersForm' class="defaultForm normalText">
		<p>
			<label class='clearing'>User name</label><input type="text" name="login"/><br />
			<label class='clearing'>First name</label><input type="text" name="firstName"/><br />
			<label class='clearing'>Last name</label><input type="text" name="lastName"/><br />
			<label class='clearing'>E-mail</label><input type="text" name="eMail" /><br />			
			<label class='clearing'>Description</label><textarea rows="4" cols="40" name="description"></textarea><br />
			<label class='clearing'>Group</label>
			<select name="group" id="search-group">
				<option value="" selected="selected"></option>
				<c:forEach var="group" items="${groups}">
					<option value="${group.id}">${group.id}</option>
				</c:forEach>
			</select>
		</p>	
		<div class="horizontalSeparator vMargins"></div>
		<p><a href ="javascript:searchUsers();">Search</a> | <a href="javascript:document.searchUsersForm.reset();refreshList();">View all</a></p>
	</form>
</div>

<table id="userList" class="list">
	<thead>
		<tr>
			<th><a href="javascript:sortUsers('login')" class="listTitle">Login</a></th>
			<th><a href="javascript:sortUsers('fullName')" class="listTitle">Name</a></th>
			<th><a href="javascript:sortUsers('descriptionPrefix')" class="listTitle">Description</a></th>
			<th><a href="javascript:sortUsers('email')" class="listTitle">E-Mail</a></th>
			<th class="actionsWrapper">Actions</th>
		</tr>	
	</thead>
	<tbody id="items-list">
	</tbody>
</table>
<p class="pagination" id="paging"></p>
<script>
	

	var cellFunctions = [
	  function(item) { 
	  	if (item.blocked) 
	  		return "<span class='blocked'>" + item.login + "</span>";
	  	else 
	  		return item.login; 
	  },
	  function(item) { 
	  	if (item.blocked) 
	  		return "<span class='blocked'>" + item.fullName + "</span>";
	  	else 
	  		return item.fullName; 
	  },
	  function(item) { 
	  	if (item.blocked) 
	  		return "<span class='blocked'>" + item.descriptionPrefix + "</span>";
	  	else 
	  		return item.descriptionPrefix; 
	  },
	  function(item) { 
	  	if (item.blocked) 
	  		return "<span class='blocked'>" + item.email + "</span>";
	  	else 
	  		return item.email; 
	  },
	  function(item) {
	  	var ul = document.createElement("ul");
	  	ul.className="listActions";
	  	var options = "";
	  	options += "<li><a href='javascript:newPassword(\"" + item.login + "\")' class='buttonEditPassword' title='Generate new password'>New password</a></li>";
	  	if (item.blocked){
		  	options += "<li><a href='javascript:unblock(\"" + item.login + "\")' class='buttonUnBlock' title='Unblock'>Unblock</a></li>";
		} else {
			options += "<li><a href='javascript:block(\"" + item.login + "\")' class='buttonBlock' title='Block'>Block</a></li>";
		}
	  	options += "<li><a href='javascript:modifyPermissions(\"" + item.login + "\",\"" + item.objectId + "\")' class='buttonEditPermissions' title='Edit permissions'>permissions</a></li>";
	  	options += "<li><a href='javascript:modify(\"" + item.login + "\")' class='buttonEdit' title='Modify'>Modify</a></li>";
	  	options += "<li><a href='javascript:deleteUser(\"" + item.login + "\")' class='buttonDelete' title='Delete'>Delete</a></li>";
	  	ul.innerHTML = options;
	    return ul;
	  }
	];

	function handleGetUsers(map){
		var list = map["list"];
		var totalUsers = map["totalSize"];
	
		DWRUtil.removeAllRows("items-list");
		DWRUtil.addRows("items-list",list,cellFunctions, {
		  rowCreator:function(options) {
		    var tr = document.createElement("tr");
			tr.onmouseover=function(){this.className='listMouseOver';return false;};
			tr.onmouseout=function(){this.className='';return false;};
			tr.id=options.rowData.login + "-row";
			return tr;
		  },
		  cellCreator:function(options) {
		    var td = document.createElement("td");
		    td.className='actionsWrapper';
		    return td;
		  }
		});
		
		pagingCell = document.getElementById('paging');
		
		pages = (totalUsers / rowsPerPage);
		if (totalUsers % rowsPerPage > 1){
			pages++;
		}
				
		pagingHtml = "<strong>Page:</strong> ";
		
		for (i=1; i<=pages; i++){
			if (currentPage == i){
				pagingHtml += "<strong>"+i+"</strong>&nbsp;";
			} else {
				pagingHtml += "<a href='javascript:goToPageUsers("+i+");'>"+i+"</a>&nbsp;";
			}
		}
				
		pagingCell.innerHTML = pagingHtml;
		
		stopWorking();
	}
		
	function goToPageUsers(newPage){
		startWorking();
		UserFacade.goToPageUsers(newPage,handleGetUsers);
		currentPage = newPage;
	}
	
	function sortUsers(sortOrder){
		startWorking();
		UserFacade.sortUsers(sortOrder, handleGetUsers);
		
		currentPage = 1;
	}
	
	
	
	var lastEditedItem;
	
	function hideModifyForm(){
		if (lastEditedItem != null){
			lastEditedItem.className='';
			lastEditedItem.onmouseover=function(){this.className='listMouseOver';return false;};
			lastEditedItem.onmouseout=function(){this.className='';return false;};
		}
	
		deleteNodeById("modifyUserRow");
	}
	
	function hideModifyPermissions(){
		if (lastEditedItem != null){
			lastEditedItem.className='';
			lastEditedItem.onmouseover=function(){this.className='listMouseOver';return false;};
			lastEditedItem.onmouseout=function(){this.className='';return false;};
		}
	
		deleteNodeById("modifyPermissionsRow");
	}
	
	function modify(login){
		startWorking();
		
		hideModifyForm();
		hideModifyPermissions();
		
		UserFacade.readUser(login, handleModify);
	}
	
	function handleModify(data){
		var tbl = document.getElementById("items-list");
		lastEditedItem = document.getElementById(data.login + "-row");
		lastEditedItem.className = 'listEdit';
		lastEditedItem.onmouseover=function(){return false;};
		lastEditedItem.onmouseout=function(){return false;};
		
		var row = tbl.insertRow(lastEditedItem.rowIndex);
		row.id = "modifyUserRow";
		row.className='listEditForm';
		
		var cell = row.insertCell(0);
		cell.colSpan = 5;
		
		var blocked = "";
		if (data.blocked){
			blocked = "checked";
		}
		
		var superUser = "";
		if (data.superUser){
			superUser = "checked";
		}
		
		cell.innerHTML = "<form name='modifyUserForm' class='defaultForm'>\
			<p>\
				<label class='clearing'>Login</label><input type='text' name='login' class='mandatory' value='" + data.login + "' readonly='readonly'/><br/>\
				<label class='clearing'>First name</label><input type='text' name='firstName' value='" + data.firstName + "'/><br/>\
				<label class='clearing'>Last name</label><input type='text' name='lastName' value='" + data.lastName + "'/><br/>\
				<label class='clearing'>E-Mail</label><input type='text' name='eMail' value='" + data.email + "'/><br/>\
				<label class='clearing'>Blocked</label><input type='checkbox' name='blocked' "+ blocked +" /><br/>\
				<label class='clearing'>Super user</label><input type='checkbox' name='superUser' "+ superUser +" /><br/>\
				<label class='clearing'>Description</label>\
				<textarea name='description' rows='4' cols='50'>"+ data.description +"</textarea><br/>\
			</p>\
			<div class='horizontalSeparator vMargins'></div>\
			<p><a href ='javascript:doModify();'>Confirm</a> | <a href='javascript:hideModifyForm();'>Cancel</a></p></form>";
			
		stopWorking();
	}
	
	
	
	function doModify(){
		startWorking();
		UserFacade.modifyUser(document.modifyUserForm.login.value, document.modifyUserForm.firstName.value, 
			document.modifyUserForm.lastName.value, document.modifyUserForm.eMail.value, 
			document.modifyUserForm.blocked.checked, document.modifyUserForm.description.value, 
			document.modifyUserForm.superUser.checked, handleDoModify);
	}
	
	function handleDoModify(){
		refreshList();
		hideModifyForm();
		displayMessage('info','The user information has been saved');
	}
	
	function block(login){
		startWorking();
		UserFacade.block(login, handleBlock);
	}
	
	function handleBlock(){
		displayMessage('info','The user has been blocked');
		refreshList();
	}
	
	function unblock(login){
		startWorking();
		UserFacade.unblock(login, handleUnblock);
	}
	
	function handleUnblock(){
		displayMessage('info','The user has been unblocked');
		refreshList();
	}
	
	function deleteUser(login){
		startWorking();
		UserFacade.deleteUser(login, handleDeleteUser);
	}
	
	function handleDeleteUser(){
		displayMessage('info','The user has been deleted');
		refreshList();
	}
	
	function newPassword(login){
		startWorking();
		UserFacade.newPassword(login,handleNewPassword);
	}
	
	function handleNewPassword(){
		displayMessage('info','A new password has been generated and sent to the user via email');
		refreshList();
	}
	
	function refreshList(){
		startWorking();
		
		UserFacade.refresh(currentPage, document.searchUsersForm.login.value,
			document.searchUsersForm.firstName.value, document.searchUsersForm.lastName.value,
			document.searchUsersForm.eMail.value, document.searchUsersForm.description.value,
			DWRUtil.getValue('search-group'), handleGetUsers);
	}
	
	// New User
	function displayNewUserForm(){
		document.getElementById("addUserFormWrapper").className='visible formWrapper';
	}
	
	function hideNewUserForm(){
		document.getElementById("addUserFormWrapper").className='invisible formWrapper';
	}
	
	function createNewUser(){
		startWorking();
		UserFacade.createNewUser(document.newUserForm.login.value, document.newUserForm.firstName.value, 
			document.newUserForm.lastName.value, document.newUserForm.eMail.value, 
			document.newUserForm.blocked.checked, document.newUserForm.description.value, 
			document.newUserForm.superUser.checked, handleCreateNewUser);
	}
	
	function handleCreateNewUser(){
		refreshList();
		hideNewUserForm();
		document.newUserForm.reset();
		displayMessage('info','The user has been created');
	}
	
	// Search Users
	function displaySearchUsersForm(){
		document.getElementById("searchUserFormWrapper").className='visible formWrapper';
	}
	
	function hideSearchUsersForm(){
		document.getElementById("searchUserFormWrapper").className='invisible formWrapper';
	}
	
	function searchUsers(){
		startWorking();
		currentPage = 1;
		refreshList();
	}
	
	refreshList();
</script>