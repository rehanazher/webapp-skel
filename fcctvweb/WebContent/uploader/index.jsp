<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@page import="jp.co.fcctvweb.actions.BasicJsonAction.I18N"%>
<% I18N i18n = (I18N)pageContext.findAttribute("i18n"); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><%= i18n.getI18nText("uploader.title") %></title>
<link rel="stylesheet" type="text/css" href="../resources/css/ext-all.css" />
<style type="text/css">
.upload-icon {
    background: url('../images/file_add.png') no-repeat 0 0 !important;
}
label.grey div#tips{
	padding: 10px 20px;
	color: 'gray';
}
</style>
<script type="text/javascript" src="../jslib/ext-all.js"></script>
<script type="text/javascript">
Ext.onReady(function(){
    if(Ext.is.Desktop){
    	var msg = function(title, msg) {
            Ext.Msg.show({
                title: title,
                msg: msg,
                minWidth: 200,
                modal: true,
                icon: Ext.Msg.INFO,
                buttons: Ext.Msg.OK
            });
        };

        Ext.create('Ext.form.Panel', {
            renderTo: Ext.getBody(),
            width: 500,
            frame: true,
            floating: true,
            draggable: true,
            title: '<%= i18n.getI18nText("uploader.title") %>',
            bodyPadding: '10 10 0',

            defaults: {
                anchor: '100%',
                allowBlank: false,
                msgTarget: 'side',
                labelWidth: 100
            },

            items: [{
                xtype: 'textfield',
                name: 'name',
                fieldLabel: '<%= i18n.getI18nText("uploader.label.name") %>'
            },{
                xtype: 'combo',
                name: 'type',
                store : Ext.create('Ext.data.Store', {
                    fields: [ {name: 'type',  type: 'int'}, {name: 'desc', type: 'string'}],
                    data: [
                       {type: 1, desc: '<%= i18n.getI18nText("uploader.type.video") %>'},
                       {type: 2, desc: '<%= i18n.getI18nText("uploader.type.doc") %>'},
                       {type: 3, desc: '<%= i18n.getI18nText("uploader.type.music") %>'},
                       {type: 4, desc: '<%= i18n.getI18nText("uploader.type.photo") %>'}
                    ]
                }),
                queryMode: 'local',
                fieldLabel: '<%= i18n.getI18nText("uploader.label.type") %>',
                value: 0,
                typeAhead: true,
                valueField : 'type',
                displayField : 'desc',
                allowBlank: false
            },{
                xtype: 'filefield',
                id: 'form-file',
                emptyText: '<%= i18n.getI18nText("uploader.label.file.empty.tips") %>',
                fieldLabel: '<%= i18n.getI18nText("uploader.label.file") %>',
                name: 'uploadedFile',
                buttonText: '',
                buttonConfig: {
                    iconCls: 'upload-icon'
                }
            },{
                xtype: 'hidden',
                id: 'filePath',
                name: 'filePath',
                value: ''
            },{
            	xtype: 'label',
            	cls: 'grey',
            	html: '<div id="tips" style="color: gray;"><%= i18n.getI18nText("uploader.upload.tips") %></div>'
    		}],

            buttons: [{
                text: '<%= i18n.getI18nText("common.button.upload") %>',
                handler: function(){
                    var form = this.up('form').getForm();
                    Ext.getCmp("filePath").setValue(Ext.getCmp("form-file").getValue());
                    if(form.isValid()){
                        form.submit({
                            url: './uploadFiles.action',
                            waitMsg: '<%= i18n.getI18nText("uploader.msg.waiting") %>',
                            success: function(fp, o) {
                                msg('<%= i18n.getI18nText("common.dialog.title") %>', o.result.msg);
                            },
                            failure: function(fp, o){
                            	msg('<%= i18n.getI18nText("common.dialog.title") %>', o.result.msg);
                            }
                        });
                    }
                }
            },{
                text: '<%= i18n.getI18nText("common.button.reset") %>',
                handler: function() {
                    this.up('form').getForm().reset();
                }
            }]
        });
    }else {
        location.href = "../touch.action";
    }
});
</script>
</head>
<body>
</body>
</html>