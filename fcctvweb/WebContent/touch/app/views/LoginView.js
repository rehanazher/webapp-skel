FccTVApp.views.LoginView = Ext.extend(Ext.form.FormPanel, {
	url: '../login.action',
	standardSubmit : false,
	fullscreen : true,
	autoRender : true,
	floating : false,
	centered : true,
	hidden: true,
	scroll : false,
	showAnimation : 'fade',
	hideOnMaskTap : false,
	height : 385,
	width : 480,
	items : [{
		xtype : 'fieldset',
		title : bundle.getText('app.login.title'),
		instructions : bundle.getText('app.login.instructions'),
		defaults : {
			required : true,
			labelAlign : 'left',
			labelWidth : '40%'
		},
		items : [{
			xtype : 'textfield',
			name : 'username',
			label : bundle.getText('app.login.username'),
			useClearIcon : true,
			autoCapitalize : false
		}, {
			xtype : 'passwordfield',
			name : 'password',
			label : bundle.getText('app.login.password'),
			useClearIcon : false
		}]
	}],
	dockedItems : [{
		xtype : 'toolbar',
		dock : 'bottom',
		items : [{
			xtype : 'spacer'
		}, {
			text : bundle.getText('common.button.reset'),
			handler : function() {
				this.up('form').reset();
			}
		}, {
			text : bundle.getText('common.button.submit'),
			ui : 'confirm',
			handler : function() {
				this.up('form').customSubmitForm();
			}
		}]
	}],
	listeners : {
		submit : function(form, data, options) {
			this.customSubmitForm();
		}
	},
	customSubmitForm: function(){
//		var loadMask = new Ext.LoadMask(Ext.getBody(), {
//			msg : bundle.getText('common.mask.loading')
//		});
		FccTVApp.loadMask.show();

		this.submit({
			 method: 'post',
			 params: this.getValues(),
			 success : function(form, action) {
				 FccTVApp.loadMask.hide();
				 this.hide();
			 	 FccTVApp.views.viewport = FccTVApp.viewcache.MainView;
				 FccTVApp.views.viewport.show();
			 },
			 failure : function(form, action) {
				 // form.hideMask();
				 Ext.Msg.alert(bundle.getText('common.dialog.title'), action.msg, function(){
					 FccTVApp.loadMask.hide();
				 });
			 }
		 });
	},
	initComponent : function() {
		FccTVApp.views.LoginView.superclass.initComponent.call(this, arguments);
	}
});
