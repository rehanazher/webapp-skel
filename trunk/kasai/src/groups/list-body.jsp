<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<script type='text/javascript' src='<html:rewrite page='/dwr/interface/GroupFacade.js'/>'></script>
<script type='text/javascript' src='<html:rewrite page='/permissions.js'/>'></script>
<script language="javascript" type="text/javascript" src="<html:rewrite page='/actb.js'/>"></script>
<script language="javascript" type="text/javascript" src="<html:rewrite page='/common.js'/>"></script>
<script>
	var rowsPerPage = '${rowsPerPage}';
	var currentPage = 1;
</script>
<h2>Group management</h2>
<div id='messageBox' class="invisible"></div>

<div id="buttonEnclosing">
	<a id="buttonAdd" href="javascript:displayNewForm();">Add group</a>
	<a id="buttonSearch" href="javascript:displaySearchForm();">Search group</a>
</div>

<div id="addFormWrapper" class="invisible formWrapper">
	<h3 class="formHeading">Add group</h3>
	<a href="javascript:hideNewForm();" class="buttonFormClose">Close</a>
	<div class="horizontalSeparator hMargins"></div>
	<form name='newForm' class="defaultForm normalText">
		<p>
			<label class="clearing">Name</label><input type="text" name="name"/><br />
			<label class="clearing">Description</label><textarea rows="4" cols="40" name="description"></textarea><br />
			<label class="clearing">Blocked</label><input type="checkbox" name="blocked"/><br/>
			<label class="clearing">Members</label>&nbsp;<span id="newMembers"></span><br/>
			<label class="clearing">&nbsp;</label>
			<input type="text" name="newMember" id="newMember"/>
				<div class="groupMembersLinks">
					<a href="javascript:addMemberToNewGroup();">add</a>
				</div><br/>
		</p>	
		<div class="horizontalSeparator vMargins"></div>
		<p><a href ="javascript:createNew();">Add group</a> | <a href="javascript:hideNewForm();">Cancel</a></p>
	</form>
</div>

<div id="searchFormWrapper" class="invisible formWrapper">
	<h3 class="formHeading">Search group</h3>
	<a href="javascript:hideSearchForm();" class="buttonFormClose">Close</a>
	<div class="horizontalSeparator hMargins"></div>
	<form name='searchForm' class="defaultForm normalText">
		<p>
			<label class='clearing'>Name</label><input type="text" name="name"/><br />
			<label class='clearing'>Description</label><textarea rows="4" cols="40" name="description"></textarea><br />
			<label class='clearing'>Blocked</label>
			<select name="blocked" id="search-blocked">
				<option value="-1" selected="selected"></option>
				<option value="0">No</option>
				<option value="1">Yes</option>
			</select><br/>
			<label class='clearing'>System group</label>
			<select name="system" id="search-system">
				<option value="-1" selected="selected"></option>
				<option value="0">No</option>
				<option value="1">Yes</option>
			</select><br/>
		</p>	
		<div class="horizontalSeparator vMargins"></div>
		<p><a href ="javascript:search();">Search</a> | <a href="javascript:document.searchForm.reset();refreshList();">View all</a></p>
	</form>
</div>

<table id="list" class="list">
	<thead>
		<tr>
			<th><a href="javascript:sortGroups('id')" class="listTitle">Name</a></th>
			<th><a href="javascript:sortGroups('descriptionPrefix')" class="listTitle">Description</a></th>
			<th><a href="javascript:sortGroups('system')" class="listTitle">System group</a></th>
			<th class="actionsWrapper">Actions</th>
		</tr>	
	</thead>
	<tbody id="items-list">
	</tbody>
</table>
<p class="pagination" id="paging"></p>
<script>
	var newMembers;	
	var modifyMembers;
	var allUsernames;

	var cellFunctions = [
	  function(item) { 
	  	if (item.blocked) 
	  		return "<span class='blocked'>" + item["id"] + "</span>";
	  	else 
	  		return item["id"]; 
	  },
	  function(item) { 
	  	if (item.blocked) 
	  		return "<span class='blocked'>" + item.descriptionPrefix + "</span>";
	  	else 
	  		return item.descriptionPrefix; 
	  },
	  function(item) { 
	    result = item.system ? "Yes" : "No";
	  	if (item.blocked) 
	  		return "<span class='blocked'>" + result + "</span>";
	  	else 
	  		return result; 
	  },
	  function(item) {
	  	var ul = document.createElement("ul");
	  	ul.className="listActions";
	  	var options = "";
	  	if (item.blocked){
		  	options += "<li><a href='javascript:unblock(\"" + item["id"] + "\")' class='buttonUnBlock' title='Unblock'>Unblock</a></li>";
		} else {
			options += "<li><a href='javascript:block(\"" + item["id"] + "\")' class='buttonBlock' title='Block'>Block</a></li>";
		}
	  	options += "<li><a href='javascript:modifyPermissions(\"" + item["id"] + "\",\"" + item.objectId + "\")' class='buttonEditPermissions' title='Edit permissions'>permissions</a></li>";
	  	options += "<li><a href='javascript:modify(\"" + item["id"] + "\")' class='buttonEdit' title='Modify'>Modify</a></li>";
	  	options += "<li><a href='javascript:deleteGroup(\"" + item["id"] + "\")' class='buttonDelete' title='Delete'>Delete</a></li>";
	  	ul.innerHTML = options;
	    return ul;
	  }
	];

	function handleGetGroups(map){
		var list = map["list"];
		var totalQty = map["totalSize"];
		DWRUtil.removeAllRows("items-list");
		DWRUtil.addRows("items-list",list,cellFunctions, {
		  rowCreator:function(options) {
		    var tr = document.createElement("tr");
			tr.onmouseover=function(){this.className='listMouseOver';return false;};
			tr.onmouseout=function(){this.className='';return false;};
			tr.id=options.rowData["id"] + "-row";
			return tr;
		  },
		  cellCreator:function(options) {
		    var td = document.createElement("td");
		    td.className='actionsWrapper';
		    return td;
		  }
		});
				
		pagingCell = document.getElementById('paging');
		pages = (totalQty / rowsPerPage);
		if (totalQty % rowsPerPage > 1){
			pages++;
		}
		
		pagingHtml = "<strong>Page:</strong> ";
		
		for (i=1; i<=pages; i++){
			if (currentPage == i){
				pagingHtml += "<strong>"+i+"</strong>&nbsp;";
			} else {
				pagingHtml += "<a href='javascript:goToPageGroups("+i+");'>"+i+"</a>&nbsp;";
			}
		}
				
		pagingCell.innerHTML = pagingHtml;
		
		stopWorking();
	}
		
	function goToPageGroups(newPage){
		startWorking();
		GroupFacade.goToPageGroups(newPage,handleGetGroups);
		currentPage = newPage;
	}
	
	function sortGroups(sortOrder){
		startWorking();
		GroupFacade.sortGroups(sortOrder, handleGetGroups);
		
		currentPage = 1;
	}
		
	var lastEditedItem;
	
	function hideModifyForm(){
		if (lastEditedItem != null){
			lastEditedItem.className='';
			lastEditedItem.onmouseover=function(){this.className='listMouseOver';return false;};
			lastEditedItem.onmouseout=function(){this.className='';return false;};
		}
	
		deleteNodeById("modifyRow");
	}
	
	function hideModifyPermissions(){
		if (lastEditedItem != null){
			lastEditedItem.className='';
			lastEditedItem.onmouseover=function(){this.className='listMouseOver';return false;};
			lastEditedItem.onmouseout=function(){this.className='';return false;};
		}
	
		deleteNodeById("modifyPermissionsRow");
	}
	
	function modify(id){
		startWorking();
		
		hideModifyForm();
		hideModifyPermissions();
		
		GroupFacade.readGroup(id, handleModify);
	}
	
	function handleModify(data){
		var tbl = document.getElementById("items-list");
		lastEditedItem = document.getElementById(data["id"] + "-row");
		lastEditedItem.className = 'listEdit';
		lastEditedItem.onmouseover=function(){return false;};
		lastEditedItem.onmouseout=function(){return false;};
		
		var row = tbl.insertRow(lastEditedItem.rowIndex);
		row.id = "modifyRow";
		row.className='listEditForm';
		
		var cell = row.insertCell(0);
		cell.colSpan = 5;
		
		var blocked = "";
		if (data.blocked){
			blocked = "checked";
		}
		
		var system = "";
		if (data.system){
			system = "checked";
		}
		
		cell.innerHTML = "<form name='modifyForm' class='defaultForm' id='groupModifyForm'>\
			<p>\
				<label class='clearing'>Name</label><input type='text' name='name' class='mandatory' value='" + data["id"] + "' readonly='readonly'/><br/>\
				<label class='clearing'>Description</label>\
				<textarea name='description' rows='4' cols='50'>"+ data.description +"</textarea><br/>\
				<label class='clearing'>Blocked</label><input type='checkbox' name='blocked' "+ blocked +" /><br/>\
				<label class='clearing'>System group</label><input type='checkbox' name='system' "+ system +" disabled='disabled'/><br/>\
				<label class='clearing'>Members</label>&nbsp;<span id='modifyMembers'></span><br/>\
				<label class='clearing'>&nbsp;</label>\
				<input type='text' name='newModifyMember' id='newModifyMember'/>\
					<div class='groupMembersLinks'>\
						<a href='javascript:addMemberToModifyGroup();'>add</a>\
					</div><br/>\
			</p>\
			<div class='horizontalSeparator vMargins'></div>\
			<p><a href ='javascript:doModify();'>Confirm</a> | <a href='javascript:hideModifyForm();'>Cancel</a></p></form>";
			
		modifyMembers = new Object();
		for (var i=0; i<data.members.length; i++){
			modifyMembers[data.members[i]] = data.members[i];
		}
		refreshModifyGroupMembers();
		
		objACTB = actb(document.getElementById('newModifyMember'),allUsernames);
		objACTB.actb_fFamily = 'Trebuchet MS, Verdana, Arial, Helvetica, sans-serif';
		objACTB.actb_fSize = '1.2em';
			
		stopWorking();
	}
	
	
	
	function doModify(){
		startWorking();
		GroupFacade.modifyGroup(document.modifyForm.name.value, document.modifyForm.description.value,
			document.modifyForm.blocked.checked, associativeToArray(modifyMembers), handleDoModify);
	}
	
	function handleDoModify(){
		refreshList();
		hideModifyForm();
		displayMessage('info','The group information has been saved');
	}
	
	function block(id){
		startWorking();
		GroupFacade.block(id, handleBlock);
	}
	
	function handleBlock(){
		displayMessage('info','The group has been blocked');
		refreshList();
	}
	
	function unblock(id){
		startWorking();
		GroupFacade.unblock(id, handleUnblock);
	}
	
	function handleUnblock(){
		displayMessage('info','The group has been unblocked');
		refreshList();
	}
	
	function deleteGroup(id){
		startWorking();
		GroupFacade.deleteGroup(id, handleDelete);
	}
	
	function handleDelete(){
		displayMessage('info','The group has been deleted');
		refreshList();
	}
	
	function refreshList(){
		startWorking();
		
		GroupFacade.refresh(currentPage, document.searchForm.name.value,
			document.searchForm.description.value,
			DWRUtil.getValue('search-blocked'), DWRUtil.getValue('search-system'), handleGetGroups);
	}
	
	function refreshUsernamesList(){
		BaseFacade.listUsernames(handleRefreshUsernamesList);
	}
	
	function handleRefreshUsernamesList(usernames){
		allUsernames = usernames;
		objACTB = actb(document.getElementById('newMember'),allUsernames);
		objACTB.actb_fFamily = 'Trebuchet MS, Verdana, Arial, Helvetica, sans-serif';
		objACTB.actb_fSize = '1.2em';
	}
	
	// New User
	function displayNewForm(){
		document.getElementById("addFormWrapper").className='visible formWrapper';
	}
	
	function hideNewForm(){
		document.getElementById("addFormWrapper").className='invisible formWrapper';
	}
	
	function createNew(){
		startWorking();
		GroupFacade.createNewGroup(document.newForm.name.value, 
			document.newForm.description.value, document.newForm.blocked.checked, associativeToArray(newMembers), handleCreateNew);
	}
	
	function handleCreateNew(){
		refreshList();
		hideNewForm();
		document.newForm.reset();
		displayMessage('info','The group has been created');
	}
	
	// Search Users
	function displaySearchForm(){
		document.getElementById("searchFormWrapper").className='visible formWrapper';
	}
	
	function hideSearchForm(){
		document.getElementById("searchFormWrapper").className='invisible formWrapper';
	}
	
	function search(){
		startWorking();
		currentPage = 1;
		refreshList();
	}
	
	
	
	function addMemberToNewGroup(){
		newMemberId = document.getElementById("newMember").value;
		
		if (!arrayContains(newMembers, newMemberId)){
			if (arrayContains(allUsernames, newMemberId)){
				newMembers[newMemberId] = newMemberId;
				refreshNewGroupMembers();
			}
		}
	}
		
	function removeMemberFromNewGroup(memberId){
		delete newMembers[memberId];
		refreshNewGroupMembers();
	}
	
	function refreshNewGroupMembers(){
		firstOne = 1;
		document.getElementById("newMembers").innerHTML = "";
		
		for (var memberId in newMembers){
			if (firstOne!=1){
				document.getElementById("newMembers").innerHTML += ", ";
			}
			
			document.getElementById("newMembers").innerHTML += memberId;
			document.getElementById("newMembers").innerHTML += " (<a href='javascript:removeMemberFromNewGroup(\"" + memberId + "\");'>x</a>)";			firstOne = 0;
			
		}
	}
	
	function addMemberToModifyGroup(){
		newModifyMemberId = document.getElementById("newModifyMember").value;
		
		if (!arrayContains(modifyMembers, newModifyMemberId)){
			if (arrayContains(allUsernames, newModifyMemberId)){
				modifyMembers[newModifyMemberId] = newModifyMemberId;
				refreshModifyGroupMembers();
			}
		}
	}
		
	function removeMemberFromModifyGroup(memberId){
		delete modifyMembers[memberId];
		refreshModifyGroupMembers();
	}
	
	function refreshModifyGroupMembers(){
		firstOne = 1;
		document.getElementById("modifyMembers").innerHTML = "";
		
		for (var memberId in modifyMembers){
			if (firstOne!=1){
				document.getElementById("modifyMembers").innerHTML += ", ";
			}
			
			document.getElementById("modifyMembers").innerHTML += memberId;
			document.getElementById("modifyMembers").innerHTML += " (<a href='javascript:removeMemberFromModifyGroup(\"" + memberId + "\");'>x</a>)";
			firstOne = 0;
		}
	}
	 
	refreshList();
	refreshUsernamesList();
</script>