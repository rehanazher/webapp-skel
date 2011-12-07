<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Redirect Page</title>
<script type="text/javascript" src="jslib/ext.js"></script>
<script type="text/javascript">
Ext.onReady(function(){
    //if (Ext.is.Desktop){
    //    location.href = "desktop.action";
    //}else{// if(Ext.is.Phone){
        location.href = "touch.action";
    //};
});
</script>
</head>
<body>

</body>
</html>