<%@page import="jp.co.fcctvweb.config.Config"%>
<%@page import="jp.co.fcctvweb.actions.BasicJsonAction.I18N"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.Enumeration"%>
<%@page import="java.util.PropertyResourceBundle"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% I18N i18n = (I18N)pageContext.findAttribute("i18n"); %>
<% int height = (Integer)pageContext.findAttribute("height"); %>
<% int width = (Integer)pageContext.findAttribute("width"); %>
<% String type = (String)pageContext.findAttribute("type"); %>
<% int fileId = (Integer)pageContext.findAttribute("fileId"); %>
<% String title = (String)pageContext.findAttribute("title"); %>


<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <title>Doc Prev</title>
        <style type="text/css">
            #fixedTitle {
                text-align: center;
                position:fixed; 
                min-width: 842px;
                width: 100%;
                height: 46px; 
                background: url(./images/top-sub-bg.png) repeat-x;
            }
            #fixedTitle span {
                display:inline-block;
                padding-right:57px;
                color: white; 
                text-shadow: rgba(0, 0, 0, 0.5) 0 -0.08em 0; 
                line-height: 2.1em; 
                font-weight: bold; 
                font-size: 1.2em;
                font-family: "Helvetica Neue", HelveticaNeue, "Helvetica-Neue", Helvetica, "BBAlpha Sans", sans-serif;    
            }
        
            #fixedTitle .backLink, 
            #fixedTitle .backLinkDown {
                float:left;
                margin: 6px 10px;
                font-family: "Helvetica Neue", HelveticaNeue, "Helvetica-Neue", Helvetica, "BBAlpha Sans", sans-serif;
                color: white;
                text-shadow: rgba(0, 0, 0, 0.5) 0 -0.08em 0;
                font-size: 14px;
                font-weight: bold;
            }
            .backLink{
                height: 33px; 
                width: 57px; 
                border: 0 solid;
                background: url(./images/top-sub-btn.png) 0 0 no-repeat;
            }
            
            .backLinkDown{
                height: 33px; 
                width: 57px; 
                border: 0 solid; 
                background: url(./images/top-sub-btn.png) 100% 0 no-repeat;
            }
        </style>
    </head>
    <body style="padding: 0; margin: 0; width: <%= width %>px;">
        <div id="fixedTitle" >
          <input type="button" value=" <%= i18n.getI18nText("common.button.back") %>" class="backLink" onmousedown="this.className='backLinkDown'" onclick="javascript: history.back(-1);" onmouseup="this.className='backLink'"/>
          <span><%= title %></span>
        </div>
        <embed style="margin-top: 40px;" src="./watch.action?type=<%= type %>&fileId=<%= fileId %>" height="<%= height%>" width="<%= width %>"></embed>
        
<!--        <input type="button" value="Back" style="height: 33px; width: 57px; border: 0 solid; background: url(./images/top-sub-btn.png) left top no-repeat;">-->
    </body>
    
    
</html>