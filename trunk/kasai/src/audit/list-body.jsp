<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<script type='text/javascript' src='<html:rewrite page='/dwr/interface/AuditFacade.js'/>'></script>
<script type='text/javascript' src='<html:rewrite page='/permissions.js'/>'></script>
<script language="javascript" type="text/javascript" src="<html:rewrite page='/actb.js'/>"></script>
<script language="javascript" type="text/javascript" src="<html:rewrite page='/common.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/datepicker.js'/>"></script>
<script>
	var rowsPerPage = '${rowsPerPage}';
	var currentPage = 1;
</script>
<h2>Auditing</h2>
<div id='messageBox' class="invisible"></div>

<div id="buttonEnclosing">
	<a id="buttonSearch" href="javascript:displaySearchForm();">Search</a>
</div>

<div id="searchFormWrapper" class="formWrapper">
	<h3 class="formHeading">Search</h3>
	<a href="javascript:hideSearchForm();" class="buttonFormClose">Close</a>
	<div class="horizontalSeparator hMargins"></div>
	<form name='searchForm' class="defaultForm normalText">
		<p>
			<label class='clearing'>User</label><input type="text" name="user"/><br />
			<label class='clearing'>Operation</label><input type="text" name="operation" id="operation"/><br />
			<label class='clearing'>From</label><input type="text" name="dateFrom" id="dateFrom" class="w8em format-y-m-d divider-dash highlight-days-67 range-high-today no-transparency"/><input type="text" name="fromHours" id="fromHours" value="00"/><label class="hourSeparator">:</label><input type="text" name="fromMinutes" id="fromMinutes" value="00"/><br />
			<label class='clearing'>To</label><input type="text" name="dateTo" id="dateTo" class="w8em format-y-m-d divider-dash highlight-days-67 range-high-today no-transparency"/><input type="text" name="toHours" id="toHours" value="23"/><label class="hourSeparator">:</label><input type="text" name="toMinutes" id="toMinutes" value="59"/><br />
		</p>	
		<div class="horizontalSeparator vMargins"></div>
		<p><a href ="javascript:search();">Search</a> | <a href="javascript:document.searchForm.reset();refreshList();">View all</a></p>
	</form>
</div>

<table id="list" class="list">
	<thead>
		<tr>
			<th><a href="javascript:sortEntries('dateTime')" class="listTitle">Date time</a></th>
			<th><a href="javascript:sortRoles('user')" class="listTitle">User</a></th>
			<th><a href="javascript:sortRoles('operation')" class="listTitle">Operation</a></th>
			<th><a href="javascript:sortRoles('clientIP')" class="listTitle">Client IP</a></th>
			<th><a href="javascript:sortRoles('returnCode')" class="listTitle">Return code</a></th>
			<th><a href="javascript:sortRoles('duration')" class="listTitle">Duration</a></th>
			<th class="actionsWrapper">Actions</th>
		</tr>	
	</thead>
	<tbody id="items-list">
	</tbody>
</table>
<p class="pagination" id="paging"></p>
<script>
	var allOperatives;

	var cellFunctions = [
	  function(item) { 
	  	return formatDateTime(item.dateTime); 
	  },
	  function(item) { 
  		return item.user; 
	  },
	  function(item) { 
  		return item.operation; 
	  },
	  function(item) {
  		return item.clientIP; 
	  },
	  function(item) { 
  		return item.returnCode; 
	  },
	  function(item) { 
  		return item.duration + "ms"; 
	  },
	  function(item) {
	  	var ul = document.createElement("ul");
	  	ul.className="listActions";
	  	var options = "";
	  	options += "<li><a href='javascript:viewDetails(\"" + item.id + "\")' class='buttonView' title='View details'>details</a></li>";
	  	ul.innerHTML = options;
	    return ul;
	  }
	];

	function handleGetEntries(map){
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
				pagingHtml += "<a href='javascript:goToPageEntries("+i+");'>"+i+"</a>&nbsp;";
			}
		}
				
		pagingCell.innerHTML = pagingHtml;
		
		stopWorking();
	}
	
	var selectedEntryId;
	var lastEditedItem; 
	
	function viewDetails(entryId){
		selectedEntryId = entryId;
		
		startWorking();
		hideDetailsForm();
		
		AuditFacade.getEntryDetails(entryId, handleViewDetails);
	}
	
	function handleViewDetails(data){
		stopWorking();

		var tbl = document.getElementById("items-list");
		lastEditedItem = document.getElementById(selectedEntryId + "-row");
		lastEditedItem.className = 'listEdit';
		lastEditedItem.onmouseover=function(){return false;};
		lastEditedItem.onmouseout=function(){return false;};
		
		var row = tbl.insertRow(lastEditedItem.rowIndex);
		row.id = "detailsRow";
		row.className='listEditForm';
		
		var cell = row.insertCell(0);
		cell.colSpan = 7; 
		cell.style.width ="100%";
		
		cell.innerHTML = "<form class='defaultForm'>\
				<textarea rows='6' cols='150' id='detailsArea' readonly='readonly'></textarea><br/>\
			<div class='horizontalSeparator vMargins'></div>\
			<p><a href='javascript:hideDetailsForm();'>Close</a></p></form>";
			
		document.getElementById('detailsArea').value=data;
		stopWorking();
	}
	
	function hideDetailsForm(){
		if (lastEditedItem != null){
			lastEditedItem.className='';
			lastEditedItem.onmouseover=function(){this.className='listMouseOver';return false;};
			lastEditedItem.onmouseout=function(){this.className='';return false;};
		}
	
		deleteNodeById("detailsRow");
	}
			
	function goToPageEntries(newPage){
		startWorking();
		AuditFacade.goToPageEntries(newPage,handleGetEntries);
		currentPage = newPage;
	}
	
	function sortEntries(sortOrder){
		startWorking();
		AuditFacade.sortEntries(sortOrder, handleGetEntries);
		
		currentPage = 1;
	}
	
	function refreshList(){
		startWorking();
		AuditFacade.refresh(currentPage, document.searchForm.dateFrom.value, document.searchForm.fromHours.value, document.searchForm.fromMinutes.value, document.searchForm.dateTo.value, document.searchForm.toHours.value, document.searchForm.toMinutes.value, document.searchForm.user.value, document.searchForm.operation.value, handleGetEntries);
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
	 
	refreshList();
</script>