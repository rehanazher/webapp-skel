<%@page import="jp.co.fcctvweb.actions.BasicJsonAction.I18N"%>
<%@page import="java.util.Enumeration"%>
<%@page import="java.util.PropertyResourceBundle"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% I18N i18n = (I18N)pageContext.findAttribute("i18n"); %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <title>FCC TV Web Sample</title>
        <link rel="stylesheet" href="resources/css/sencha-touch.css" type="text/css">
        <link rel="stylesheet" href="css/fixed.css" type="text/css">
        <style type="text/css">
            #loading_mask {
                position: absolute;
                width: 180px;
                margin: 0;
                height: 140px;
                top: 50%;
                left: 50%;
            }
            #loading_mask .title {
                position: absolute;
                display: block;
                top: 0;
                left: 0px;
                width: 180px;
                height: 27px;
            }
            #loading_mask .logo {
                background: url(images/loading.gif) no-repeat;
                position: absolute;
                display: block;
                top: 25px;
                left: 22px;
                width: 120px;
                height: 120px;
            }
        </style>
        <script type="text/javascript">
            // i18n
bundle = (function(){
    var resourcesBundle = {};
    <% PropertyResourceBundle b = i18n.getBundle();
       Enumeration<String> keys = b.getKeys();
       while (keys.hasMoreElements()){
            String key = keys.nextElement(); %>resourcesBundle["<%= key %>"] = "<%=b.getString(key)%>";<%
       }
    %>

    return resourcesBundle;
})();

bundle.getText = function(key){
    return bundle[key] || key;
};

var loginFlag = <%= pageContext.findAttribute("loginFlag") %>;
        </script>
    </head>
    <body>
        <div id="loading_mask">
            <span class="title">Loading...</span><span class="logo"></span>
        </div>
        <script type="text/javascript" src="jslib/sencha-touch.js"></script>
        
        <script type="text/javascript" src="app/app.js"></script>
        
        <!-- utils -->
        <script type="text/javascript" src="app/utils/lang-zh_CN.js"></script>
        
        
        <!-- frames -->
        <script type="text/javascript" src="app/frames/Video.js"></script>
        <script type="text/javascript" src="app/frames/Forms.js"></script>
        <!-- <script type="text/javascript" src="app/frames/InputCard.js"></script>
        <script type="text/javascript" src="app/frames/QueryCard.js"></script> -->
        
        <!-- stores -->
        <script type="text/javascript" src="app/stores/NavigationStore.js"></script>
        
        <!-- views -->
        <script type="text/javascript" src="app/views/LoginView.js"></script>
        <script type="text/javascript" src="app/views/PhoneViewport.js"></script>
    </body>
</html>