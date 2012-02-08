
FccTVApp.frames.Settings = new Ext.Panel({
	scroll: 'vertical',
	items: [{
		xtype: 'form',
		items: [{
			xtype: 'fieldset',
	        title: bundle.getText('app.setting.expire.title'),
	        // instructions: 'Please enter the information above.',
	        defaults: {
	            labelWidth: '35%'
	        },
	        items: [{
	        	xtype: 'spinnerfield',
	        	id: 'settingExpireDate',
	        	label: bundle.getText('app.setting.expire.label'),
	        	minValue: 0,
	            maxValue: 14,
	            cycle: true,
	            listeners: {
	            	change : function( cmp, newValue, oldValue ){
	            		FccTVApp.loadMask.show();
	            		Ext.Ajax.request({
	            			url: './changeMovieExpire.action',
	            			params: {
	            				expiredDate: newValue
	            			},
	            			success: function(response, opts) {
	            				FccTVApp.loadMask.hide();
	        				},
	        				failure: function(){
	        					FccTVApp.loadMask.hide();
	        				}
	            		});
	            	},
	            	spin : function( cmp, value, direction ){
	            		FccTVApp.loadMask.show();
	            		Ext.Ajax.request({
	            			url: './changeMovieExpire.action',
	            			params: {
	            				expiredDate: value
	            			},
	            			success: function(response, opts) {
	            				FccTVApp.loadMask.hide();
	        				},
	        				failure: function(){
	        					FccTVApp.loadMask.hide();
	        				}
	            		});
	            	} 
	            }
	        }]
		},{
			xtype: 'fieldset',
	        title: bundle.getText('app.setting.hdd.format.title'),
	        // instructions: 'Please enter the information above.',
	        defaults: {
	            labelWidth: '35%'
	        },
	        items: [{
	        	xtype: 'selectfield',
	        	id: 'settingHddId',
                name : 'settingHddId',
                label: bundle.getText('app.setting.hdd.list.label'),
                valueField : 'key',
                displayField : 'name',
                store : new Ext.data.JsonStore({
                	model: 'HddModel',
                	proxy: {
                		type: 'ajax',
                		url: './hddList.action'
                	},
                	autoLoad: true
                }),
                listeners:{
                	change: function(){
                		Ext.getCmp('settingHddFormat').setValue(0);
                	}
                }
	        },{
	        	xtype: 'togglefield',
	        	id: 'settingHddFormat',
	        	label: bundle.getText('app.setting.hdd.format.label'),
	            listeners: {
	            	change : function (cmp, thumb, newValue, oldValue ){
	            		if(newValue == 1){
	            			
            	            Ext.Msg.confirm(bundle.getText('common.dialog.confirm'), bundle.getText('app.setting.hdd.format.confirmation'), function(clickedBtn){
            	            	if (clickedBtn == "yes"){
                	            	FccTVApp.loadMask.show();
        	            			Ext.Ajax.request({
        		            			url: './hddFormat.action',
        		            			params: {
        		            				key: Ext.getCmp('settingHddId').getValue()
        		            			},
        		            			success: function(response, opts) {
        		            				FccTVApp.loadMask.hide();
        		        				},
        		        				failure: function(){
        		        					FccTVApp.loadMask.hide();
        		        				}
        		            		});
            	            	}else{
            	            		cmp.setValue(0);
            	            	}
            	            });
	            		}
	            	}
	            }
	        }]
		},{
			xtype: 'fieldset',
	        title: bundle.getText('app.setting.device.reset.title'),
	        // instructions: 'Please enter the information above.',
	        defaults: {
	            labelWidth: '35%'
	        },
	        items: [{
	        	xtype: 'togglefield',
	        	id: 'settingDeviceReset',
	        	label: bundle.getText('app.setting.device.reset.label'),
	            listeners: {
	            	change : function (cmp, thumb, newValue, oldValue ){
	            		if(newValue == 1){
	            			Ext.Msg.confirm(bundle.getText('common.dialog.confirm'), bundle.getText('app.setting.device.reset.confirmation'), function(clickedBtn){
            	            	if (clickedBtn == "yes"){
            	            		FccTVApp.loadMask.show();
        	            			Ext.Ajax.request({
        		            			url: './deviceReset.action',
        		            			success: function(response, opts) {
        		            				FccTVApp.loadMask.hide();
        		        				},
        		        				failure: function(){
        		        					FccTVApp.loadMask.hide();
        		        				}
        		            		});
            	            	}else{
            	            		cmp.setValue(0);
            	            	}
            	            });
	            			
	            		}
	            	}
	            }
	        }]
		},{
			xtype: 'fieldset',
	        title: bundle.getText('app.setting.device.shutdown.title'),
	        // instructions: 'Please enter the information above.',
	        defaults: {
	            labelWidth: '35%'
	        },
	        items: [{
	        	xtype: 'togglefield',
	        	id: 'settingDeviceShutdown',
	        	label: bundle.getText('app.setting.device.shutdown.label'),
	            listeners: {
	            	change : function (cmp, thumb, newValue, oldValue ){
	            		if(newValue == 1){
	            			Ext.Msg.confirm(bundle.getText('common.dialog.confirm'), bundle.getText('app.setting.device.shutdown.confirmation'), function(clickedBtn){
            	            	if (clickedBtn == "yes"){
            	            		FccTVApp.loadMask.show();
        	            			Ext.Ajax.request({
        		            			url: './deviceShutdown.action',
        		            			success: function(response, opts) {
        		            				FccTVApp.loadMask.hide();
        		        				},
        		        				failure: function(){
        		        					FccTVApp.loadMask.hide();
        		        				}
        		            		});
            	            	}else{
            	            		cmp.setValue(0);
            	            	}
            	            });
	            		}
	            	}
	            }
	        }]
		}]
	}],
	listeners: {
		activate: function(cmp){
			Ext.Ajax.request({
				url: './getMovieExpire.action',
				success: function(response, opts) {
					var obj = Ext.decode(response.responseText);
					Ext.getCmp('settingExpireDate').setValue(obj.value);
				},
				failure: function(){
					
				}
			});
			
			Ext.Ajax.request({
				url: './getMovieExpire.action',
				success: function(response, opts) {
					var obj = Ext.decode(response.responseText);
					Ext.getCmp('settingExpireDate').setValue(obj.value);
				},
				failure: function(){
					
				}
			});
		}
	}
});