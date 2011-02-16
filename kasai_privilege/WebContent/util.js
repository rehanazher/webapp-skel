n4 = (document.layers)? true:false;
ie = (document.all)? true:false;
n6 = (document.getElementById)? true:false;

function getElementById(id)
{
    if (ie)
		return window.document.all[id];
	else
		return window.document.getElementById(id);
}

function startWorking(){
	getElementById('working').className='visible';
}

function stopWorking(){
	getElementById('working').className='invisible';
}

function DWRExceptionHandler(message, exception){ 
	if (exception.javaClassName=="org.manentia.kasai.ui.exceptions.SessionExpiredException"){
		location.href='/kasai/login.action';
	} else {
		BaseFacade.decodeMessage(exception.message, handleErrorMessage);
	}
}

function DWRErrorHandler(message){
	displayMessage("error", message);
	stopWorking();
}

function handleErrorMessage(data){
	displayMessage(data.level, data.message);
	stopWorking();
}

function displayMessage(level, message){
	//alert(level + " - " + message);
	mBox = getElementById('messageBox');
		
	mBox.className='messageBox-' + level + ' fade-1';
	mBox.innerHTML=message;	
	
	setTimeout("fadeMessage()", 5000);
}

function closeMessage(){
	mBox = getElementById('messageBox');
		
	mBox.className='invisible';
}

function fadeMessage(){
	mBox = getElementById('messageBox');
	
	switch (mBox.className){
		case "messageBox-info fade-1":
			mBox.className='messageBox-info fade-2';
			setTimeout("fadeMessage()", 100);
			break;
		case "messageBox-info fade-2":
			mBox.className='messageBox-info fade-3';
			setTimeout("fadeMessage()", 100);
			break;
		case "messageBox-info fade-3":
			mBox.className='messageBox-info fade-4';
			setTimeout("fadeMessage()", 100);
			break;
		case "messageBox-info fade-4":
			mBox.className='messageBox-info fade-5';
			setTimeout("fadeMessage()", 100);
			break;
		case "messageBox-info fade-5":
			mBox.className='messageBox-info fade-6';
			setTimeout("fadeMessage()", 100);
			break;
		case "messageBox-info fade-6":
			mBox.className='messageBox-info fade-7';
			setTimeout("fadeMessage()", 100);
			break;
		case "messageBox-info fade-7":
			mBox.className='messageBox-info fade-8';
			setTimeout("fadeMessage()", 100);
			break;
		case "messageBox-info fade-8":
			mBox.className='messageBox-info fade-9';
			setTimeout("fadeMessage()", 100);
			break;
		case "messageBox-info fade-9":
			mBox.className='messageBox-info fade-10';
			setTimeout("fadeMessage()", 100);
			break;
		case "messageBox-info fade-10":
			mBox.className='messageBox-info fade-11';
			setTimeout("fadeMessage()", 100);
			break;
		case "messageBox-warning fade-1":
			mBox.className='messageBox-warning fade-2';
			setTimeout("fadeMessage()", 100);
			break;
		case "messageBox-warning fade-2":
			mBox.className='messageBox-warning fade-3';
			setTimeout("fadeMessage()", 100);
			break;
		case "messageBox-warning fade-3":
			mBox.className='messageBox-warning fade-4';
			setTimeout("fadeMessage()", 100);
			break;
		case "messageBox-warning fade-4":
			mBox.className='messageBox-warning fade-5';
			setTimeout("fadeMessage()", 100);
			break;
		case "messageBox-warning fade-5":
			mBox.className='messageBox-warning fade-6';
			setTimeout("fadeMessage()", 100);
			break;
		case "messageBox-warning fade-6":
			mBox.className='messageBox-warning fade-7';
			setTimeout("fadeMessage()", 100);
			break;
		case "messageBox-warning fade-7":
			mBox.className='messageBox-warning fade-8';
			setTimeout("fadeMessage()", 100);
			break;
		case "messageBox-warning fade-8":
			mBox.className='messageBox-warning fade-9';
			setTimeout("fadeMessage()", 100);
			break;
		case "messageBox-warning fade-9":
			mBox.className='messageBox-warning fade-10';
			setTimeout("fadeMessage()", 100);
			break;
		case "messageBox-warning fade-10":
			mBox.className='messageBox-warning fade-11';
			setTimeout("fadeMessage()", 100);
			break;
		case "messageBox-error fade-1":
			mBox.className='messageBox-error fade-2';
			setTimeout("fadeMessage()", 100);
			break;
		case "messageBox-error fade-2":
			mBox.className='messageBox-error fade-3';
			setTimeout("fadeMessage()", 100);
			break;
		case "messageBox-error fade-3":
			mBox.className='messageBox-error fade-4';
			setTimeout("fadeMessage()", 100);
			break;
		case "messageBox-error fade-4":
			mBox.className='messageBox-error fade-5';
			setTimeout("fadeMessage()", 100);
			break;
		case "messageBox-error fade-5":
			mBox.className='messageBox-error fade-6';
			setTimeout("fadeMessage()", 100);
			break;
		case "messageBox-error fade-6":
			mBox.className='messageBox-error fade-7';
			setTimeout("fadeMessage()", 100);
			break;
		case "messageBox-error fade-7":
			mBox.className='messageBox-error fade-8';
			setTimeout("fadeMessage()", 100);
			break;
		case "messageBox-error fade-8":
			mBox.className='messageBox-error fade-9';
			setTimeout("fadeMessage()", 100);
			break;
		case "messageBox-error fade-9":
			mBox.className='messageBox-error fade-10';
			setTimeout("fadeMessage()", 100);
			break;
		case "messageBox-error fade-10":
			mBox.className='messageBox-error fade-11';
			setTimeout("fadeMessage()", 100);
			break;
	}
}

function deleteNodeById(nodeId){
	var toDelete = getElementById(nodeId);
	if (toDelete){
		toDelete.parentNode.removeChild(toDelete);
	}
}

function arrayContains(array, element){
	result = false;
	
	for (var i in array){
		if (array[i]==element){
			result = true;
			break;
		}
	}
	
	return result;
}

function associativeToArray(asoArray){
	result = new Array();
	for (var i in asoArray){
		result[result.length]=asoArray[i];
	}
	
	return result;
}

function formatDateTime(dateTime){
	result = "";
	
	result += dateTime.getFullYear();
	result += "-" + padLeft(dateTime.getMonth()+1,2,"0");
	result += "-" + padLeft(dateTime.getDate(),2,"0");
	result += " " + padLeft(dateTime.getHours(),2,"0");
	result += ":" + padLeft(dateTime.getMinutes(),2,"0");
	result += ":" + padLeft(dateTime.getSeconds(),2,"0");
	result += "." + padLeft(dateTime.getMilliseconds(), 3, "0");
	
	return result;
}

function padLeft(text, len, filler){
	result = text.toString();
	
	while (result.length<len){
		result = filler.toString() + result;
	}
	
	return result;
}

dwr.engine.setErrorHandler(DWRExceptionHandler);