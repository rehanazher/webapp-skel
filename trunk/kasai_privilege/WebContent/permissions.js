/* Permission handling */
var currentItem = '';
var currentObjectId = '';

var permissionCellFunctions = [
  function(item) { 
  	if (item.user){
  	 return "The user <u>" + item.user + "</u> has the role of <u>" + item.roleName + "</u> over <u>" + currentObjectId + "</u>" + " | " + "<a href='javascript:deleteObjectUserRole(" + item.id + ")' class='list-item'>Remove this permission</a>&nbsp;";
  	} else {
   	 return "The group <u>" + item.group + "</u> has the role of <u>" + item.roleName + "</u> over <u>" + currentObjectId + "</u>" + " | " + "<a href='javascript:deleteObjectGroupRole(" + item.id + ")' class='list-item'>Remove this permission</a>&nbsp;";
  	}
  }
];

function modifyPermissions(id, objectId){
	startWorking();
	
	hideModifyForm();
	hideModifyPermissions();
	
	var tbl = document.getElementById("items-list");
	var row = tbl.insertRow(document.getElementById(id + "-row").rowIndex);
	row.id = "modifyPermissionsRow";
	row.className='listEditForm';
	
	lastEditedItem = document.getElementById(id + "-row");
	lastEditedItem.className = 'listEdit';
	lastEditedItem.onmouseover=function(){return false;};
	lastEditedItem.onmouseout=function(){return false;};
	
	var cell = row.insertCell(0);
	cell.id = "modifyPermissionsCell";
	cell.colSpan = 5;
	
	currentItem = id;
	currentObjectId = objectId;
	
	BaseFacade.listObjectRoles(objectId, handleModifyPermissions);
}

function handleModifyPermissions(objectRoles){		
	var cell = document.getElementById("modifyPermissionsCell");
	
	cell.innerHTML = "<h3 class='inner'>Permissions granted over " + currentObjectId + "</h3>\
	<div class='horizontalSeparator vMargins'></div>\
	<table class='inner' id='permissionTable'><tbody id='permission-list'></tbody></table>";
	
	DWRUtil.addRows("permission-list",objectRoles,permissionCellFunctions, {
	  rowCreator:function(options) {
	    var tr = document.createElement("tr");
		tr.className="";
		tr.id=options.rowData.id + "-row";
		return tr;
	  },
	  cellCreator:function(options) {
	    var td = document.createElement("td");
	    return td;
	  },
	  escapeHtml:false
	});
	
	BaseFacade.listUsersGroupsRoles(handleListUsersGroupsRoles);
}

function handleListUsersGroupsRoles(map){
	var cell = document.getElementById("modifyPermissionsCell");
	
	html = "<div class='horizontalSeparator vMargins'></div>";
	html += '<div class="authorizeLabel">Authorize user <select id="permission-users" class="authorizeSelect"></select>&nbsp;</div><div class="authorizeParagraph">as <select id="permission-user-roles"></select> <input type="button" value="Assign" onclick="createObjectUserRole();" /></div>';
	html += '<div class="authorizeLabel">Authorize group <select id="permission-groups" class="authorizeSelect"></select>&nbsp;</div><div class="authorizeParagraph">as <select id="permission-group-roles"></select> <input type="button" value="Assign" onclick="createObjectGroupRole();" /></div>';
	html += "<div class='horizontalSeparator vMargins'></div><p><a href ='javascript:hideModifyPermissions();'>Finish</a>";
	cell.innerHTML = cell.innerHTML + html;
	
	var sel = DWRUtil.getValue('permission-users');
	DWRUtil.removeAllOptions('permission-users');
	DWRUtil.addOptions('permission-users', map['users'], 'login', 'fullNameWithLogin');
	DWRUtil.setValue('permission-users', sel);
	
	var sel = DWRUtil.getValue('permission-groups');
	DWRUtil.removeAllOptions('permission-groups');
	DWRUtil.addOptions('permission-groups', map['groups'], 'id', 'id');
	DWRUtil.setValue('permission-groups', sel);
	
	var sel = DWRUtil.getValue('permission-user-roles');
	DWRUtil.removeAllOptions('permission-user-roles');
	DWRUtil.addOptions('permission-user-roles', map['roles'], 'id', 'name');
	DWRUtil.setValue('permission-user-roles', sel);
	
	var sel = DWRUtil.getValue('permission-group-roles');
	DWRUtil.removeAllOptions('permission-group-roles');
	DWRUtil.addOptions('permission-group-roles', map['roles'], 'id', 'name');
	DWRUtil.setValue('permission-group-roles', sel);
	
	stopWorking();
}

function createObjectUserRole(){
	startWorking();
	
	user = DWRUtil.getValue('permission-users');
	roleId = DWRUtil.getValue('permission-user-roles');
	
	BaseFacade.createObjectUserRole(currentObjectId, user, roleId, handleCreatePermission);
}

function createObjectGroupRole(){  
	startWorking();
	
	group = DWRUtil.getValue('permission-groups');
	roleId = DWRUtil.getValue('permission-group-roles');
	
	BaseFacade.createObjectGroupRole(currentObjectId, group, roleId, handleCreatePermission);
}

function handleCreatePermission(){
	displayMessage('info','The permission has been assigned');
	modifyPermissions(currentItem, currentObjectId);
}

function deleteObjectUserRole(idPermission){
	startWorking();
	BaseFacade.deleteObjectUserRole(idPermission, handleDeletePermission);
}

function deleteObjectGroupRole(idPermission){
	startWorking();
	BaseFacade.deleteObjectGroupRole(idPermission, handleDeletePermission);
}

function handleDeletePermission(){
	displayMessage('info','The permission has been deleted');
	modifyPermissions(currentItem, currentObjectId);
}

/* End permission handling */