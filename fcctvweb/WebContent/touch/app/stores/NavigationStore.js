channelItems = (function(){
	var result = [];
	
	for (var i = 0; i < chNames.length; i++){
		record = chNames[i];
		result[i] = {
				xtype: 'textfield',
	        	id: 'deviceBc' + i,
	            name: 'deviceBc' + i,
	            label: record,
	            disabled: true,
	            disabledCls: 'basicInfo',
	            placeHolder: '-'
	            	};
	}
	return result;
})();

hardwareCard = new Ext.Panel({
	scroll: 'vertical',
	items:[{
		xtype: 'form',
		items: [{
			xtype: 'fieldset',
	        title: bundle.getText('app.device.hdd.title'),
	        // instructions: 'Please enter the information above.',
	        defaults: {
	            labelWidth: '35%'
	        },
	        items: [{
	        	xtype: 'textfield',
	        	id: 'deviceHddFree',
	            name: 'deviceHddFree',
	            label: bundle.getText('app.device.hdd.free'),
	            disabled: true,
	            disabledCls: 'basicInfo',
	            placeHolder: '-'
	        },{
	        	xtype: 'textfield',
	        	id: 'deviceHddTotal',
	            name: 'deviceHddTotal',
	            label: bundle.getText('app.device.hdd.full'),
	            disabled: true,
	            disabledCls: 'basicInfo',
	            placeHolder: '-'
	        },{
	        	xtype: 'textfield',
	        	id: 'deviceHddUsage',
	            name: 'deviceHddUsage',
	            label: bundle.getText('app.device.hdd.usage'),
	            disabled: true,
	            disabledCls: 'basicInfo',
	            placeHolder: '-'
	        }]
		},{
			xtype: 'fieldset',
	        title: bundle.getText('app.device.bc.title'),
	        // instructions: 'Please enter the information above.',
	        defaults: {
	            labelWidth: '35%'
	        },
	        items: channelItems
		},{
			xtype: 'fieldset',
	        title: bundle.getText('app.device.tunner.title'),
	        // instructions: 'Please enter the information above.',
	        defaults: {
	            labelWidth: '35%'
	        },
	        items: [{
	        	xtype: 'textfield',
	        	id: 'deviceTunner0',
	            name: 'deviceTunner0',
	            label: bundle.getText('app.device.tunner.0'),
	            disabled: true,
	            disabledCls: 'basicInfo',
	            placeHolder: '-'
	        },{
	        	xtype: 'textfield',
	        	id: 'deviceTunner1',
	            name: 'deviceTunner1',
	            label: bundle.getText('app.device.tunner.1'),
	            disabled: true,
	            disabledCls: 'basicInfo',
	            placeHolder: '-'
	        },{
	        	xtype: 'textfield',
	        	id: 'deviceTunner2',
	            name: 'deviceTunner2',
	            label: bundle.getText('app.device.tunner.2'),
	            disabled: true,
	            disabledCls: 'basicInfo',
	            placeHolder: '-'
	        },{
	        	xtype: 'textfield',
	        	id: 'deviceTunner3',
	            name: 'deviceTunner3',
	            label: bundle.getText('app.device.tunner.3'),
	            disabled: true,
	            disabledCls: 'basicInfo',
	            placeHolder: '-'
	        },{
	        	xtype: 'textfield',
	        	id: 'deviceTunner4',
	            name: 'deviceTunner4',
	            label: bundle.getText('app.device.tunner.4'),
	            disabled: true,
	            disabledCls: 'basicInfo',
	            placeHolder: '-'
	        },{
	        	xtype: 'textfield',
	        	id: 'deviceTunner5',
	            name: 'deviceTunner5',
	            label: bundle.getText('app.device.tunner.5'),
	            disabled: true,
	            disabledCls: 'basicInfo',
	            placeHolder: '-'
	        },{
	        	xtype: 'textfield',
	        	id: 'deviceTunner6',
	            name: 'deviceTunner6',
	            label: bundle.getText('app.device.tunner.6'),
	            disabled: true,
	            disabledCls: 'basicInfo',
	            placeHolder: '-'
	        }]
		},{
			xtype: 'fieldset',
	        title: bundle.getText('app.device.tv.title'),
	        // instructions: 'Please enter the information above.',
	        defaults: {
	            labelWidth: '35%'
	        },
	        items: [{
	        	xtype: 'textfield',
	        	id: 'deviceTvId',
	            name: 'deviceTvId',
	            label: bundle.getText('app.device.tv.label'),
	            disabled: true,
	            disabledCls: 'basicInfo',
	            placeHolder: '-'
	        }]
		},{
			xtype: 'fieldset',
	        title: bundle.getText('app.device.software.title'),
	        // instructions: 'Please enter the information above.',
	        defaults: {
	            labelWidth: '35%'
	        },
	        items: [{
	        	xtype: 'textfield',
	        	id: 'deviceSoftwareId',
	            name: 'deviceSoftwareId',
	            label: bundle.getText('app.device.software.label'),
	            disabled: true,
	            disabledCls: 'basicInfo',
	            placeHolder: '-'
	        }]
		}]
	}], 
	listeners:{
		show: function(){
			Ext.Ajax.request({
				url: './getHddInfo.action',
				success: function(response, opts) {
					var obj = Ext.decode(response.responseText);
					Ext.getCmp('deviceHddFree').setValue(obj.value.freeSpace);
					Ext.getCmp('deviceHddTotal').setValue(obj.value.fullSpace);
					Ext.getCmp('deviceHddUsage').setValue(obj.value.usage);
				},
				failure: function(){
					
				}
			});
			
			Ext.Ajax.request({
				url: './getChannelInfo.action',
				success: function(response, opts) {
					var obj = Ext.decode(response.responseText);
					console.log(obj);
					var list = obj.value;
					for (var i = 0; i < chNames.length; i++){
						var name = chNames[i];
						for (var k = 0; k < list.length; k++){
							if (list[k].chName == name){
								Ext.getCmp('deviceBc' + i).setValue(list[k].amount + " " + bundle.getText('app.device.bc.unit'));
								break;
							}
						}
					}
				},
				failure: function(){
					
				}
			});
			
			Ext.Ajax.request({
				url: './getTunners.action',
				success: function(response, opts) {
					var obj = Ext.decode(response.responseText);
					Ext.getCmp('deviceTunner0').setValue(obj.value.ant0Level);
					Ext.getCmp('deviceTunner1').setValue(obj.value.ant1Level);
					Ext.getCmp('deviceTunner2').setValue(obj.value.ant2Level);
					Ext.getCmp('deviceTunner3').setValue(obj.value.ant3Level);
					Ext.getCmp('deviceTunner4').setValue(obj.value.ant4Level);
					Ext.getCmp('deviceTunner5').setValue(obj.value.ant5Level);
					Ext.getCmp('deviceTunner6').setValue(obj.value.ant6Level);
				},
				failure: function(){
					
				}
			});
			
			Ext.Ajax.request({
				url: './getSoftwareId.action',
				success: function(response, opts) {
					var obj = Ext.decode(response.responseText);
					Ext.getCmp('deviceSoftwareId').setValue(obj.value);
				},
				failure: function(){
					
				}
			});
			
			Ext.Ajax.request({
				url: './getTvTerminalId.action',
				success: function(response, opts) {
					var obj = Ext.decode(response.responseText);
					Ext.getCmp('deviceTvId').setValue(obj.value);
				},
				failure: function(){
					
				}
			});
		}
	}
});

searchCard = new Ext.Panel({
	scroll: 'vertical',
	items: [{
		xtype: 'form',
		url: '',
		items: [{
			xtype: 'fieldset',
	        title: bundle.getText('app.search.title'),
	        // instructions: 'Please enter the information above.',
	        defaults: {
	            labelWidth: '35%'
	        },
	        items: [{
	        	xtype: 'textfield',
	        	name: 'searchtext',
	        	label: bundle.getText('app.search.text.label')
	        },{
	        	xtype: 'selectfield',
	        	name: 'searchtype',
	        	label: bundle.getText('app.search.type.label'),
	        	options: [
	        	          {text: bundle.getText('app.search.type.genre'),  value: '0'},
	        	          {text: bundle.getText('app.search.type.sub'), value: '1'}
	        	          ]
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
					console.log();
					
					FccTVApp.stores.TypeStore.setProxy({
						type: 'ajax',
						url: './queryVideo.action',
						extraParams: this.up('form').getValues()
					});
					FccTVApp.loadMask.show();
					FccTVApp.stores.TypeStore.load(function(){
						FccTVApp.loadMask.hide();
					});
					FccTVApp.frames.QueryList.bindStore(FccTVApp.stores.TypeStore);
					
					// FccTVApp.prevCard = Ext.getCmp("navigatorPanel");
					// FccTVApp.prevTitle = record.get('text');
					this.up('tabpanel').getActiveItem().setActiveItem(FccTVApp.frames.QueryList, 'slide');
					
					// this.up('form').customSubmitForm();
				}
			}]
		}]
	}]
});

FccTVApp.stores.Structure = [{
    	text: bundle.getText('menu.1'),
    	nav: '1',
    	url: 'daily-query',
    	key: '1',
    	leaf: true,
    	card: FccTVApp.frames.DailyList
    },{
    	text: bundle.getText('menu.2'),
    	nav: '2',
    	url: 'url 2',
    	key: '2',
    	items: [{
    		text: bundle.getText('menu.2.1'),
    		nav: '2:1',
    		url: 'type-query',
    		key: '0',
    		items: [{
    			text: bundle.getText('menu.2.1.1'),
    			nav: '2:1:1',
        		url: 'type-query',
        		key: '0,0',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.1.2'),
    			nav: '2:1:2',
        		url: 'type-query',
        		key: '0,1',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.1.3'),
    			nav: '2:1:3',
        		url: 'type-query',
        		key: '0,2',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.1.4'),
    			nav: '2:1:4',
        		url: 'type-query',
        		key: '0,3',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.1.5'),
    			nav: '2:1:5',
        		url: 'type-query',
        		key: '0,4',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.1.6'),
    			nav: '2:1:6',
        		url: 'type-query',
        		key: '0,5',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.1.7'),
    			nav: '2:1:7',
        		url: 'type-query',
        		key: '0,6',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.1.8'),
    			nav: '2:1:8',
        		url: 'type-query',
        		key: '0,7',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.1.9'),
    			nav: '2:1:9',
        		url: 'type-query',
        		key: '0,8',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.1.10'),
    			nav: '2:1:10',
        		url: 'type-query',
        		key: '0,9',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.1.11'),
    			nav: '2:1:11',
        		url: 'type-query',
        		key: '0,10',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.1.12'),
    			nav: '2:1:12',
        		url: 'type-query',
        		key: '0,11',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.1.13'),
    			nav: '2:1:13',
        		url: 'type-query',
        		key: '0,12',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		}]
    	},{
    		text: bundle.getText('menu.2.2'),
    		nav: '2:2',
    		url: 'url 2-2',
    		key: '1',
    		items: [{
    			text: bundle.getText('menu.2.2.1'),
    			nav: '2:2:1',
        		url: 'type-query',
        		key: '1,0',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.2.2'),
    			nav: '2:2:2',
        		url: 'type-query',
        		key: '1,1',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.2.3'),
    			nav: '2:2:3',
        		url: 'type-query',
        		key: '1,2',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.2.4'),
    			nav: '2:2:4',
        		url: 'type-query',
        		key: '1,3',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.2.5'),
    			nav: '2:2:5',
        		url: 'type-query',
        		key: '1,4',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.2.6'),
    			nav: '2:2:6',
        		url: 'type-query',
        		key: '1,5',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.2.7'),
    			nav: '2:2:7',
        		url: 'type-query',
        		key: '1,6',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.2.8'),
    			nav: '2:2:8',
        		url: 'type-query',
        		key: '1,7',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.2.9'),
    			nav: '2:2:9',
        		url: 'type-query',
        		key: '1,8',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.2.10'),
    			nav: '2:2:10',
        		url: 'type-query',
        		key: '1,9',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.2.11'),
    			nav: '2:2:11',
        		url: 'type-query',
        		key: '1,10',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.2.12'),
    			nav: '2:2:12',
        		url: 'type-query',
        		key: '1,11',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.2.13'),
    			nav: '2:2:13',
        		url: 'type-query',
        		key: '1,12',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		}]
    	},{
    		text: bundle.getText('menu.2.3'),
    		nav: '2:3',
    		url: 'url 2-2',
    		key: '2',
    		items: [{
    			text: bundle.getText('menu.2.3.1'),
    			nav: '2:3:1',
        		url: 'type-query',
        		key: '2,0',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.3.2'),
    			nav: '2:3:2',
        		url: 'type-query',
        		key: '2,1',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.3.3'),
    			nav: '2:3:3',
        		url: 'type-query',
        		key: '2,2',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.3.4'),
    			nav: '2:3:4',
        		url: 'type-query',
        		key: '2,3',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.3.5'),
    			nav: '2:3:5',
        		url: 'type-query',
        		key: '2,4',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.3.6'),
    			nav: '2:3:6',
        		url: 'type-query',
        		key: '2,5',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.3.7'),
    			nav: '2:3:7',
        		url: 'type-query',
        		key: '2,6',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.3.8'),
    			nav: '2:3:8',
        		url: 'type-query',
        		key: '2,7',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.3.9'),
    			nav: '2:3:9',
        		url: 'type-query',
        		key: '2,8',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.3.10'),
    			nav: '2:3:10',
        		url: 'type-query',
        		key: '2,9',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		}]
    	},{
    		text: bundle.getText('menu.2.4'),
    		nav: '2:4',
    		url: 'url 2-2',
    		key: '3',
    		items: [{
    			text: bundle.getText('menu.2.4.1'),
    			nav: '2:4:1',
        		url: 'type-query',
        		key: '3,0',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.4.2'),
    			nav: '2:4:2',
        		url: 'type-query',
        		key: '3,1',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.4.3'),
    			nav: '2:4:3',
        		url: 'type-query',
        		key: '3,2',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.4.4'),
    			nav: '2:4:4',
        		url: 'type-query',
        		key: '3,3',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.4.5'),
    			nav: '2:4:5',
        		url: 'type-query',
        		key: '3,4',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		}]
    	},{
    		text: bundle.getText('menu.2.5'),
    		nav: '2:5',
    		url: 'url 2-2',
    		key: '4',
    		items: [{
    			text: bundle.getText('menu.2.5.1'),
    			nav: '2:5:1',
        		url: 'type-query',
        		key: '4,0',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.5.2'),
    			nav: '2:5:2',
        		url: 'type-query',
        		key: '4,1',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.5.3'),
    			nav: '2:5:3',
        		url: 'type-query',
        		key: '4,2',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.5.4'),
    			nav: '2:5:4',
        		url: 'type-query',
        		key: '4,3'
    		},{
    			text: bundle.getText('menu.2.5.5'),
    			nav: '2:5:5',
        		url: 'type-query',
        		key: '4,4',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.5.6'),
    			nav: '2:5:6',
        		url: 'type-query',
        		key: '4,5',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.5.7'),
    			nav: '2:5:7',
        		url: 'type-query',
        		key: '4,6',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.5.8'),
    			nav: '2:5:8',
        		url: 'type-query',
        		key: '4,7',
        		leaf: true,
    			card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.5.9'),
    			nav: '2:5:9',
        		url: 'type-query',
        		key: '4,8',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.5.10'),
    			nav: '2:5:10',
        		url: 'type-query',
        		key: '4,9',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.5.11'),
    			nav: '2:5:11',
        		url: 'type-query',
        		key: '4,10',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.5.12'),
    			nav: '2:5:12',
        		url: 'type-query',
        		key: '4,11',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.5.13'),
    			nav: '2:5:13',
        		url: 'type-query',
        		key: '4,12',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		}]
    	},{
    		text: bundle.getText('menu.2.6'),
    		nav: '2:6',
    		url: 'url 2-2',
    		key: '5',
    		items: [{
    			text: bundle.getText('menu.2.6.1'),
    			nav: '2:6:1',
        		url: 'type-query',
        		key: '5,0',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.6.2'),
    			nav: '2:6:2',
        		url: 'type-query',
        		key: '5,1',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.6.3'),
    			nav: '2:6:3',
        		url: 'type-query',
        		key: '5,2',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.6.4'),
    			nav: '2:6:4',
        		url: 'type-query',
        		key: '5,3',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.6.5'),
    			nav: '2:6:5',
        		url: 'type-query',
        		key: '5,4',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.6.6'),
    			nav: '2:6:6',
        		url: 'type-query',
        		key: '5,5',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.6.7'),
    			nav: '2:6:7',
        		url: 'type-query',
        		key: '5,6',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.6.8'),
    			nav: '2:6:8',
        		url: 'type-query',
        		key: '5,7',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.6.9'),
    			nav: '2:6:9',
        		url: 'type-query',
        		key: '5,8',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		}]
    	},{
    		text: bundle.getText('menu.2.7'),
    		nav: '2:7',
    		url: 'url 2-2',
    		key: '6',
    		items: [{
    			text: bundle.getText('menu.2.7.1'),
    			nav: '2:7:1',
        		url: 'type-query',
        		key: '6,0',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.7.2'),
    			nav: '2:7:2',
        		url: 'type-query',
        		key: '6,1',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.7.3'),
    			nav: '2:7:3',
        		url: 'type-query',
        		key: '6,2',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.7.4'),
    			nav: '2:7:4',
        		url: 'type-query',
        		key: '6,3',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.7.5'),
    			nav: '2:7:5',
        		url: 'type-query',
        		key: '6,4',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		}]
    	},{
    		text: bundle.getText('menu.2.8'),
    		nav: '2:8:1',
    		url: 'url 2-2',
    		key: '7',
    		items: [{
    			text: bundle.getText('menu.2.8.1'),
    			nav: '2:8:1',
        		url: 'type-query',
        		key: '7,0',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.8.2'),
    			nav: '2:8:2',
        		url: 'type-query',
        		key: '7,1',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.8.3'),
    			nav: '2:8:3',
        		url: 'type-query',
        		key: '7,2',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.8.4'),
    			nav: '2:8:4',
        		url: 'type-query',
        		key: '7,3',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.8.5'),
    			nav: '2:8:5',
        		url: 'type-query',
        		key: '7,4',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		}]
    	},{
    		text: bundle.getText('menu.2.9'),
    		nav: '2:9',
    		url: 'url 2-2',
    		key: '8',
    		items: [{
    			text: bundle.getText('menu.2.9.1'),
    			nav: '2:9:1',
        		url: 'type-query',
        		key: '8,0',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.9.2'),
    			nav: '2:9:2',
        		url: 'type-query',
        		key: '8,1',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.9.3'),
    			nav: '2:9:3',
        		url: 'type-query',
        		key: '8,2',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.9.4'),
    			nav: '2:9:4',
        		url: 'type-query',
        		key: '8,3',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.9.5'),
    			nav: '2:9:5',
        		url: 'type-query',
        		key: '8,4',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.9.6'),
    			nav: '2:9:6',
        		url: 'type-query',
        		key: '8,5',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.9.7'),
    			nav: '2:9:7',
        		url: 'type-query',
        		key: '8,6',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.9.8'),
    			nav: '2:9:8',
        		url: 'type-query',
        		key: '8,7',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.9.9'),
    			nav: '2:9:9',
        		url: 'type-query',
        		key: '8,8',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.9.10'),
    			nav: '2:9:10',
        		url: 'type-query',
        		key: '8,9',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.9.11'),
    			nav: '2:9:11',
        		url: 'type-query',
        		key: '8,10',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		}]
    	},{
    		text: bundle.getText('menu.2.10'),
    		nav: '2:10',
    		url: 'url 2-2',
    		key: '9',
    		items: [{
    			text: bundle.getText('menu.2.10.1'),
    			nav: '2:10:1',
        		url: 'type-query',
        		key: '9,0',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.10.2'),
    			nav: '2:10:2',
        		url: 'type-query',
        		key: '9,1',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.10.3'),
    			nav: '2:10:3',
        		url: 'type-query',
        		key: '9,2',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.10.4'),
    			nav: '2:10:4',
        		url: 'type-query',
        		key: '9,3',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.10.5'),
    			nav: '2:10:5',
        		url: 'type-query',
        		key: '9,4',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.10.6'),
    			nav: '2:10:6',
        		url: 'type-query',
        		key: '9,5',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.10.7'),
    			nav: '2:10:7',
        		url: 'type-query',
        		key: '9,6',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		}]
    	},{
    		text: bundle.getText('menu.2.11'),
    		nav: '2:11',
    		url: 'url 2-2',
    		key: '10',
    		items: [{
    			text: bundle.getText('menu.2.11.1'),
    			nav: '2:11:1',
        		url: 'type-query',
        		key: '10,0',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.11.2'),
    			nav: '2:11:2',
        		url: 'type-query',
        		key: '10,1',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.11.3'),
    			nav: '2:11:3',
        		url: 'type-query',
        		key: '10,2',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.11.4'),
    			nav: '2:11:4',
        		url: 'type-query',
        		key: '10,3',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.11.5'),
    			nav: '2:11:5',
        		url: 'type-query',
        		key: '10,4',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.11.6'),
    			nav: '2:11:6',
        		url: 'type-query',
        		key: '10,5',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.11.7'),
    			nav: '2:11:7',
        		url: 'type-query',
        		key: '10,6',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.11.8'),
    			nav: '2:11:8',
        		url: 'type-query',
        		key: '10,7',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.11.9'),
    			nav: '2:11:9',
        		url: 'type-query',
        		key: '10,8',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.11.10'),
    			nav: '2:11:10',
        		url: 'type-query',
        		key: '10,9',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.11.11'),
    			nav: '2:11:11',
        		url: 'type-query',
        		key: '10,10',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.11.12'),
    			nav: '2:11:12',
        		url: 'type-query',
        		key: '10,11',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.11.13'),
    			nav: '2:11:13',
        		url: 'type-query',
        		key: '10,12',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.11.14'),
    			nav: '2:11:14',
        		url: 'type-query',
        		key: '10,13',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.11.15'),
    			nav: '2:11:15',
        		url: 'type-query',
        		key: '10,14',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		}]
    	},{
    		text: bundle.getText('menu.2.12'),
    		nav: '2:12',
    		url: 'url 2-2',
    		key: '11',
    		items: [{
    			text: bundle.getText('menu.2.12.1'),
    			nav: '2:12:1',
        		url: 'type-query',
        		key: '11,0',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.12.2'),
    			nav: '2:12:2',
        		url: 'type-query',
        		key: '11,1',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.12.3'),
    			nav: '2:12:3',
        		url: 'type-query',
        		key: '11,2',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.12.4'),
    			nav: '2:12:4',
        		url: 'type-query',
        		key: '11,3',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.12.5'),
    			nav: '2:12:5',
        		url: 'type-query',
        		key: '11,4',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.12.6'),
    			nav: '2:12:6',
        		url: 'type-query',
        		key: '11,5',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.12.7'),
    			nav: '2:12:7',
        		url: 'type-query',
        		key: '11,6',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.12.8'),
    			nav: '2:12:8',
        		url: 'type-query',
        		key: '11,7',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.12.9'),
    			nav: '2:12:9',
        		url: 'type-query',
        		key: '11,8',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		}]
    	},{
    		text: bundle.getText('menu.2.13'),
    		nav: '2:13',
    		url: 'url 2-2',
    		key: '12',
    		items: [{
    			text: bundle.getText('menu.2.13.1'),
    			nav: '2:13:1',
        		url: 'type-query',
        		key: '12,0',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		},{
    			text: bundle.getText('menu.2.13.2'),
    			nav: '2:13:2',
        		url: 'type-query',
        		key: '12,1',
        		leaf: true,
        		card: FccTVApp.frames.QueryList
    		}]
    	}]
    },{
    	text: bundle.getText('menu.3'),
    	nav: '3',
    	url: 'url 3',
    	key: '3',
    	leaf: true,
    	card: FccTVApp.frames.ChannelList
    },{
    	text: bundle.getText('menu.4'),
    	nav: '4',
    	url: 'url 3',
    	key: '3',
    	leaf: true,
    	card: searchCard
    },{
    	text: bundle.getText('menu.5'),
    	nav: '5',
    	url: 'url 3',
    	key: '3',
    	leaf: true,
    	card: hardwareCard
    },{
    	text: bundle.getText('menu.6'),
    	nav: '6',
    	url: 'logout',
    	key: '3',
    	leaf: true
    }];


Ext.regModel('NavigationModel', {
    fields: [
        {name: 'text',  type: 'string'},
        {name: 'nav',  type: 'string'},
        {name: 'url',  type: 'string'},
        {name: 'key',  type: 'string'},
        {name: 'card'}
    ]
});

FccTVApp.stores.NavigationStore = new Ext.data.TreeStore({
    model: 'NavigationModel',
    root: {
        items: FccTVApp.stores.Structure
    },
    proxy: {
        type: 'ajax',
        reader: {
            type: 'tree',
            root: 'items'
        }
    }
});