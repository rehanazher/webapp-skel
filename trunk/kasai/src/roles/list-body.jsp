<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<script type='text/javascript' src='<html:rewrite page='/dwr/interface/RoleFacade.js'/>'></script>
<script type='text/javascript' src='<html:rewrite page='/permissions.js'/>'></script>
<script language="javascript" type="text/javascript" src="<html:rewrite page='/actb.js'/>"></script>
<script language="javascript" type="text/javascript" src="<html:rewrite page='/common.js'/>"></script>
<script>
	var rowsPerPage = '${rowsPerPage}';
	var currentPage = 1;
</script>
<h2>Profiles management</h2>
<div id='messageBox' class="invisible"></div>

<div id="buttonEnclosing">
	<a id="buttonAdd" href="javascript:displayNewForm();">Add profile</a>
	<a id="buttonSearch" href="javascript:displaySearchForm();">Search profile</a>
</div>

<div id="addFormWrapper" class="invisible formWrapper">
	<h3 class="formHeading">Add role</h3>
	<a href="javascript:hideNewForm();" class="buttonFormClose">Close</a>
	<div class="horizontalSeparator hMargins"></div>
	<form name='newForm' class="defaultForm normalText">
		<p>
			<label class="clearing">Name</label><input type="text" name="name"/><br />
			<label class="clearing">Description</label><textarea rows="4" cols="40" name="description"></textarea><br />
			<label class="clearing">Operatives</label>&nbsp;<span id="newOperatives"></span><br/>
			<label class="clearing">&nbsp;</label>
			<input type="text" name="newOperative" id="newOperative"/>
				<div class="roleOperativesLinks">
					<a href="javascript:addOperativeToNewRole();">add</a>
				</div><br/>
		</p>	
		<div class="horizontalSeparator vMargins"></div>
		<p><a href ="javascript:createNew();">Add profile</a> | <a href="javascript:hideNewForm();">Cancel</a></p>
	</form>
</div>

<div id="searchFormWrapper" class="invisible formWrapper">
	<h3 class="formHeading">Search profile</h3>
	<a href="javascript:hideSearchForm();" class="buttonFormClose">Close</a>
	<div class="horizontalSeparator hMargins"></div>
	<form name='searchForm' class="defaultForm normalText">
		<p>
			<label class='clearing'>Name</label><input type="text" name="name"/><br />
		</p>	
		<div class="horizontalSeparator vMargins"></div>
		<p><a href ="javascript:search();">Search</a> | <a href="javascript:document.searchForm.reset();refreshList();">View all</a></p>
	</form>
</div>

<table id="list" class="list">
	<thead>
		<tr>
			<th><a href="javascript:sortRoles('id')" class="listTitle">Name</a></th>
			<th><a href="javascript:sortRoles('descriptionPrefix')" class="listTitle">Description</a></th>
			<th class="actionsWrapper">Actions</th>
		</tr>	
	</thead>
	<tbody id="items-list">
	</tbody>
</table>
<p class="pagination" id="paging"></p>
<script>
	var newOperatives;	
	var modifyOperatives;
	var allOperatives;

	var cellFunctions = [
	  function(item) { 
	  	return item.name; 
	  },
	  function(item) { 
  		return item.descriptionPrefix; 
	  },
	  function(item) {
	  	var ul = document.createElement("ul");
	  	ul.className="listActions";
	  	var options = "";
	  	options += "<li><a href='javascript:modifyPermissions(\"" + item.id + "\",\"" + item.objectId + "\")' class='buttonEditPermissions' title='Edit permissions'>permissions</a></li>";
	  	options += "<li><a href='javascript:modify(\"" + item.id + "\")' class='buttonEdit' title='Modify'>Modify</a></li>";
	  	options += "<li><a href='javascript:deleteRole(\"" + item.id + "\")' class='buttonDelete' title='Delete'>Delete</a></li>";
	  	ul.innerHTML = options;
	    return ul;
	  }
	];

	function handleGetRoles(map){
		var list = map["list"];
		var totalQty = map["totalSize"];
	
		DWRUtil.removeAllRows("items-list");
		DWRUtil.addRows("items-list",list,cellFunctions, {
		  rowCreator:function(options) {
		    var tr = document.createElement("tr");
			tr.onmouseover=function(){this.className='listMouseOver';return false;};
			tr.onmouseout=function(){this.className='';return false;};
			tr.id=options.rowData.id + "-row";
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
				pagingHtml += "<a href='javascript:goToPageRoles("+i+");'>"+i+"</a>&nbsp;";
			}
		}
				
		pagingCell.innerHTML = pagingHtml;
		
		stopWorking();
	}
		
	function goToPageRoles(newPage){
		startWorking();
		RoleFacade.goToPageRoles(newPage,handleGetRoles);
		currentPage = newPage;
	}
	
	function sortRoles(sortOrder){
		startWorking();
		RoleFacade.sortRoles(sortOrder, handleGetRoles);
		
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
		
		RoleFacade.readRole(id, handleModify);
	}
	
	function handleModify(data){
		var tbl = document.getElementById("items-list");
		lastEditedItem = document.getElementById(data.id + "-row");
		lastEditedItem.className = 'listEdit';
		lastEditedItem.onmouseover=function(){return false;};
		lastEditedItem.onmouseout=function(){return false;};
		
		var row = tbl.insertRow(lastEditedItem.rowIndex);
		row.id = "modifyRow";
		row.className='listEditForm';
		
		var cell = row.insertCell(0);
		cell.colSpan = 5;
		
		cell.innerHTML = "<form name='modifyForm' class='defaultForm' id='roleModifyForm'>\
			<p>\
				<input type='hidden' name='id' value='" + data.id + "'/>\
				<label class='clearing'>Name</label><input type='text' name='name' class='mandatory' value='" + data.name + "'/><br/>\
				<label class='clearing'>Description</label>\
				<textarea name='description' rows='4' cols='50'>"+ data.description +"</textarea><br/>\
				<label class='clearing'>Operatives</label>&nbsp;<span id='modifyOperatives'></span><br/>\
				<label class='clearing'>&nbsp;</label>\
				<input type='text' name='newModifyOperative' id='newModifyOperative'/>\
					<div class='roleOperativesLinks'>\
						<a href='javascript:addOperativeToModifyRole();'>add</a>\
					</div><br/>\
			</p>\
			<div class='horizontalSeparator vMargins'></div>\
			<p><a href ='javascript:doModify();'>Confirm</a> | <a href='javascript:hideModifyForm();'>Cancel</a></p></form>";
			
		modifyOperatives = new Object();
		for (var i=0; i<data["operatives"].length; i++){
			modifyOperatives[data["operatives"][i]["id"]] = data["operatives"][i]["id"];
		}
		refreshModifyRoleOperatives();
		
		objACTB = actb(document.getElementById('newModifyOperative'),allOperatives);
		objACTB.actb_fFamily = 'Trebuchet MS, Verdana, Arial, Helvetica, sans-serif';
		objACTB.actb_fSize = '1.2em';
			
		stopWorking();
	}
	
	
	
	function doModify(){
		startWorking();
		RoleFacade.modifyRole(document.modifyForm.id.value, document.modifyForm.name.value, document.modifyForm.description.value,
			associativeToArray(modifyOperatives), handleDoModify);
	}
	
	function handleDoModify(){
		refreshList();
		hideModifyForm();
		displayMessage('info','The profile information has been saved');
	}
		
	function deleteRole(id){
		startWorking();
		RoleFacade.deleteRole(id, handleDelete);
	}
	
	function handleDelete(){
		displayMessage('info','The role has been deleted');
		refreshList();
	}
	
	function refreshList(){
		startWorking();
		
		RoleFacade.refresh(currentPage, document.searchForm.name.value, handleGetRoles);
	}
	
	function refreshOperativesList(){
		BaseFacade.listOperatives(handleRefreshOperativesList);
	}
	
	function handleRefreshOperativesList(operatives){
		allOperativesTmp = new Object();
		for (var i=0; i<operatives.length; i++){
			allOperativesTmp[operatives[i]["id"]] = operatives[i]["id"];
		}
		
		allOperatives = associativeToArray(allOperativesTmp);
		
		objACTB = actb(document.getElementById('newOperative'),allOperatives);
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
		RoleFacade.createNewRole(document.newForm.name.value, 
			document.newForm.description.value, associativeToArray(newOperatives), handleCreateNew);
	}
	
	function handleCreateNew(){
		refreshList();
		hideNewForm();
		document.newForm.reset();
		displayMessage('info','The profile has been created');
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
	
	function addOperativeToNewRole(){
		newOperativeId = document.getElementById("newOperative").value;
		
		if (!arrayContains(newOperatives, newOperativeId)){
			if (arrayContains(allOperatives, newOperativeId)){
				newOperatives[newOperativeId] = newOperativeId;
				refreshNewRoleOperatives();
			}
		}
	}
		
	function removeOperativeFromNewRole(memberId){
		delete newOperatives[memberId];
		refreshNewRoleOperatives();
	}
	
	function refreshNewRoleOperatives(){
		firstOne = 1;
		document.getElementById("newOperatives").innerHTML = "";
		
		for (var operativeId in newOperatives){
			if (firstOne!=1){
				document.getElementById("newOperatives").innerHTML += ", ";
			}
			
			document.getElementById("newOperatives").innerHTML += operativeId;
			document.getElementById("newOperatives").innerHTML += " (<a href='javascript:removeOperativeFromNewRole(\"" + operativeId + "\");'>x</a>)";
			firstOne = 0;
			
		}
	}
	
	function addOperativeToModifyRole(){
		newModifyOperativeId = document.getElementById("newModifyOperative").value;
		
		if (!arrayContains(modifyOperatives, newModifyOperativeId)){
			if (arrayContains(allOperatives, newModifyOperativeId)){
				modifyOperatives[newModifyOperativeId] = newModifyOperativeId;
				refreshModifyRoleOperatives();
			}
		}
	}
		
	function removeOperativeFromModifyRole(operativeId){
		delete modifyOperatives[operativeId];
		refreshModifyRoleOperatives();
	}
	
	function refreshModifyRoleOperatives(){
		firstOne = 1;
		document.getElementById("modifyOperatives").innerHTML = "";
		
		
		
		for (var operativeId in modifyOperatives){
			if (firstOne!=1){
				document.getElementById("modifyOperatives").innerHTML += ", ";
			}
			
			document.getElementById("modifyOperatives").innerHTML += operativeId;
			document.getElementById("modifyOperatives").innerHTML += " (<a href='javascript:removeOperativeFromModifyRole(\"" + operativeId + "\");'>x</a>)";
			firstOne = 0;
		}
	}
	 
	refreshList();
	refreshOperativesList();
</script>