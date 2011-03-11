<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>RBAC Module</title>

<link rel="stylesheet" type="text/css" href="../script/ext4/resources/css/ext-all.css" />

<script type="text/javascript" src="../script/ext4/ext-core-debug.js" ></script>
<script type="text/javascript" src="../script/ext4/ext-all-debug.js" ></script>

<script type="text/javascript">
Ext.onReady(function(){
	new Ext.Window({
        title: 'RBAC Module',
        width: 600,
        height: 400,
        headerPosition: 'left',
        closable: false,
        resizable: false,
        items: [{
       		    xtype: 'form',
	            bodyStyle:'padding:5px 5px 0',
	            width: 600,
	            height: 400,
	            anchor: '100%',
	            layout: 'column',
       		    items: [{
       	            xtype: 'label',
       	         	columnWidth: .5,
       	            forId: 'myFieldId',
       	            text: 'My Awesome Field'
       			},{
           			xtype: 'field',
           			columnWidth: .5,
           			label: 'User Name',
           			name: 'userName'
               	},{
           			xtype: 'field',
           			columnWidth: .5,
           			label: 'Password',
           			name: 'password'
               	}],
               	items: [{
       	            xtype: 'label',
       	         	columnWidth: .5,
       	            text: 'My Awesome Field'
       			},{
       			 xtype: 'label',
    	         	columnWidth: .5,
    	            text: 'My Awesome Field'
               	},{
               	 xtype: 'label',
    	         	columnWidth: .5,
    	            text: 'My Awesome Field'
               	}]
			}]
    }).show();
});
</script>

</head>
<body>
<div></div>
</body>
</html>