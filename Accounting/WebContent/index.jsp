<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>Accounting</title>

<link rel="stylesheet" type="text/css" href="./script/ext4/resources/css/ext-all.css" />

<script type="text/javascript" src="./script/ext4/ext-core.js" ></script>
<script type="text/javascript" src="./script/ext4/ext-all.js" ></script>
<script type="text/javascript">
Ext.onReady(function(){
	new Ext.Window({
        title: 'Accounting Login',
        width: 600,
        height: 400,
        headerPosition: 'left',
        closable: false,
        resizable: false,
        defaults: {
            anchor: '100%'
        },
        items: [{
       		    xtype: 'form',
       		    url:'./login.action',
       		    items: [{
                	xtype:'label',
                    html: '<h1><div width="100%" align="center" style="margin: 25px 0 0 0; font-size: 26pt">Accounting (EXT & Cloud)</div></h1>'
            	},{
                	xtype:'label',
                    html: '<div width="80%" align="right" style="margin: 0 0 25px 0;font-size: 14pt"> ---- Testing By James v0.1 &nbsp;&nbsp;&nbsp;&nbsp;</div>'
            	},{
           			xtype: 'field',
           			fieldLabel: 'User Name',
           			name: 'userName'
               	},{
           			xtype: 'field',
           			fieldLabel: 'Password',
           			name: 'password'
               	}],
               		
        		buttons: [
        	        {text: 'Login'},
           	        {text: 'Cancel'}
            	]
         }]
    }).show();
});

</script>
</head>
<body>

</body>
</html>