<%@page import="jp.co.fcctvweb.config.Config"%>
<%@page import="jp.co.fcctvweb.actions.BasicJsonAction.I18N"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.Enumeration"%>
<%@page import="java.util.PropertyResourceBundle"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% I18N i18n = (I18N)pageContext.findAttribute("i18n"); %>
<% Calendar serverTime = (Calendar)pageContext.findAttribute("serverTime"); %>
<% Config configurations = (Config)pageContext.findAttribute("configurations"); %>


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
                width: 180px;
                height: 27px;
            }
            #loading_mask .logo {
                background: url(images/loading.gif) no-repeat;
                position: absolute;
                display: block;
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
            String key = keys.nextElement(); %>resourcesBundle["<%= key %>"]="<%=b.getString(key)%>";<%
       }
    %>

    return resourcesBundle;
})();

bundle.getText = function(key){
    return bundle[key] || key;
};

var loginFlag = <%= pageContext.findAttribute("loginFlag") %>;
var configuredPageSize = <%= configurations.getPageSize() %>;
        </script>
    </head>
    <body>
<!--        <embed src="test.pdf" width="100%" height="100000px" type="application/pdf"/>-->
<!--<div style="position: fixed; top: 0; left: 0; z-index: 100;">Back Button</div>-->
<!--<iframe style="position: relative; z-index: 50;" src="test.docx" height="4800"  width="100%">-->
<!--</iframe>-->
<!--height="229894px"-->
    
    
        <div id="loading_mask">
            <span class="title"></span><span class="logo"></span>
        </div>
        <script type="text/javascript" src="jslib/sencha-touch.js"></script>
        
        <script type="text/javascript" src="app/app.js"></script>
        
        <!-- utils -->
        <script type="text/javascript" src="app/utils/lang-zh_CN.js"></script>
        <script type="text/javascript" src="app/utils/AppUtils.js"></script>
        
        <!-- models -->
        <script type="text/javascript" src="app/models/QueryListModel.js"></script>
        <script type="text/javascript" src="app/models/MyVideoListModel.js"></script>
<!--        -->
        <!-- stores -->
        <script type="text/javascript" src="app/stores/DailyListStore.js"></script>
        <script type="text/javascript" src="app/stores/QueryListStore.js"></script>
        <script type="text/javascript" src="app/stores/DailyStore.js"></script>
        <script type="text/javascript" src="app/stores/TypeStore.js"></script>
        <script type="text/javascript" src="app/stores/MyVideoStore.js"></script>
        <script type="text/javascript" src="app/stores/MyDocStore.js"></script>
        
        <!-- frames -->
        <script type="text/javascript" src="app/frames/Player.js"></script>
        <script type="text/javascript" src="app/frames/MyVideoPlayer.js"></script>
        <script type="text/javascript" src="app/frames/MyVideoFavoriteList.js"></script>
        <script type="text/javascript" src="app/frames/MyDocFavoriteList.js"></script>
        <script type="text/javascript" src="app/frames/TodayList.js"></script>
        <script type="text/javascript" src="app/frames/FavoriteList.js"></script>
        <script type="text/javascript" src="app/frames/DailyList.js"></script>
        <script type="text/javascript" src="app/frames/QueryList.js"></script>
        
        <!-- navigation store, depends on all utils, stores and frames -->
        <script type="text/javascript" src="app/stores/NavigationStore.js"></script>
        
        <!-- views -->
        <script type="text/javascript" src="app/views/MainView.js"></script>
        <script type="text/javascript" src="app/views/LoginView.js"></script>
        <script type="text/javascript" src="app/views/TvView.js"></script>
        <script type="text/javascript" src="app/views/MyVideoView.js"></script>
        <script type="text/javascript" src="app/views/MyDocView.js"></script>
<script type="text/javascript">
	//document.getElementById("test").focus();    
</script>
    </body>
</html>