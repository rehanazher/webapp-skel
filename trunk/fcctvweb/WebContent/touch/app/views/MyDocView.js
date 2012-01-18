FccTVApp.views.MyDocView = Ext.extend(Ext.Panel, {
	id: 'docview',
	showAnimation : 'fade',
	navigatorPref : 'doc',
	//html: 'Test Page',
	fullscreen : true,
	hidden: true,
	layout: 'card',
	items : [],
	dockedItems : [{
		xtype : 'toolbar',
		title : '',
		items : [{
			xtype : 'button',
			id : 'docBackButton',
			text : bundle.getText("main.title"),
			ui : 'back',
			handler : function() {
				
				var panel = this.up('panel');
				var activeItem = panel.getActiveItem();
				if (activeItem){
					var recordNode = activeItem.recordNode;
					if (recordNode.parentNode){
						if (recordNode.parentNode.isRoot){
							if (FccTVApp.player){
								FccTVApp.player.destroy();
							}
							FccTVApp.views.viewport.hide();
							FccTVApp.addHistory("main");
							FccTVApp.views.viewport = FccTVApp.viewcache.MainView;
							FccTVApp.views.viewport.show();
						}else{
							var nav = panel.navigatorPref + "/" + recordNode.parentNode.attributes.record.get('position');
							FccTVApp.addHistory(nav);
							FccTVApp.dispatch(nav, true);
						}
					}
				}else{
					if (FccTVApp.player){
						FccTVApp.player.destroy();
					}
					FccTVApp.views.viewport.hide();
					FccTVApp.addHistory("main");
					FccTVApp.views.viewport = FccTVApp.viewcache.MainView;
					FccTVApp.views.viewport.show();
				}
			}
		}, {
			xtype : 'spacer'
		}]
	}],
	tabBar : {
		dock : 'bottom',
		scroll : {
			direction : 'horizontal',
			useIndicators : false
		},
		layout : {
			pack : 'center'
		}
	},
	listeners: {
	},
	getListConfig: function(itemId, node) {
		return {
	        itemId: itemId,
	        xtype: 'list',
	        autoDestroy: true,
	        recordNode: node,
	        store: FccTVApp.stores.MyDocTreeStore.getSubStore(node),
	        itemTpl: new Ext.XTemplate(
		    		'<img style="float: left; height: 36px; width: 36px;"',
		    		'<tpl if="type == \'folder\'"> src="./images/folder.png"</tpl>',
		    		'<tpl if="type == \'file\' && extName == \'doc\'"> src="./images/word.png"</tpl>',
		    		'<tpl if="type == \'file\' && extName == \'docx\'"> src="./images/word.png"</tpl>',
		    		'<tpl if="type == \'file\' && extName == \'pdf\'"> src="./images/pdf.png"</tpl>',
		    		'<tpl if="type == \'file\' && extName == \'xls\'"> src="./images/xls.png"</tpl>',
		    		'<tpl if="type == \'file\' && extName == \'xlsx\'"> src="./images/xls.png"</tpl>',
		    		'<tpl if="type == \'file\' && extName == \'txt\'"> src="./images/txt.png"</tpl>',
		    		'  />',
					'<h2 style="float:left; padding: 7px 10px;">{name}</h2>'
		    		),
	        listeners: {
	        	itemtap: function(list, index, el, e){
	        		var record = list.getStore().getAt(index);
	        		var panel = this.up('panel');
	        		if (record.get('type') == 'folder'){
	        			var nav = panel.navigatorPref + "/" + record.get('position');
						FccTVApp.addHistory(nav);
						FccTVApp.dispatch(nav);
	        		}else if(record.get('type') == 'file'){
	        			FccTVApp.loadMask.show();
	        			location.href='./watch.action?fileId=' + record.get('key') + '&type=' + record.get('extName');
	        			FccTVApp.loadMask.hide();
//	        			Ext.Ajax.request({
//	        				url: './docAnalyze.action',
//	        				params: {
//	        					fileId : record.get('key'),
//	        					type: record.get('extName')
//	        				},
//		        			success: function(response, opts) {
//		  					  var obj = Ext.decode(response.responseText);
//		  					  FccTVApp.loadMask.hide();
//		  					  location.href='./watch.action?fileId=' + record.get('key') + '&type=' + record.get('extName');
//		  					  // location.href='./docPrev.action?fileId=' + record.get('key') + '&type=' + record.get('extName') + '&height=' + obj.value.height + '&width=' + obj.value.width;
//		  					},
//		  					failure: function(response, opts) {
//		  					  FccTVApp.loadMask.hide();
//		  					} 
//	        			});
	        		}
	        	},
	        	itemswipe : function(list, index, el, e) {
	        		var record = list.getStore().getAt(index);
	        		var panel = this.up('panel');
	        		panel.overlay.fileId = record.get('key');
	        		panel.overlay.show();
	        	}
	        }
		};
    },
    overlay : new Ext.Panel({
		id : 'docOverlay',
        floating: true,
//        modal: true,
        centered: true,
        fileId: null,
        width: Ext.is.Phone ? 280 : 440,
        height: Ext.is.Phone ? 240 : 440,
        styleHtmlContent: true,
        layout: 'fit',
        scroll: 'vertical',
        storeLoadCallBack:  function(){
        	console.log('callback: ' + Ext.History.getToken());
			FccTVApp.dispatch(Ext.History.getToken());
        },
        items:[{
        	xtype: 'list',
        	autoDestroy: true,
        	disableSelection: true,
	        store: FccTVApp.stores.MyDocFlatStore,
	        itemTpl: new Ext.XTemplate('{folderName}'),
        	listeners:{
        		itemtap: function(list, index, el, e){
        			FccTVApp.loadMask.show();
        			var record = list.getStore().getAt(index);
        			overlay = Ext.getCmp('docOverlay');
        			
        			
        			Ext.Ajax.request({
        				url: './changeDocDir.action',
        				params: {
        					fileId : overlay.fileId,
        					toDirId: record.get('key')
        				},
	        			success: function(response, opts) {
	        				FccTVApp.stores.MyDocTreeStore.load();
	        				Ext.defer(function(){
		        					FccTVApp.dispatch(Ext.History.getToken());
		        					FccTVApp.loadMask.hide();
			        				overlay.hide();
	        					}, 1000);
	  					},
	  					failure: function(response, opts) {
		  					FccTVApp.loadMask.hide();
		  					console.log(overlay);
		  					overlay.hide();
	  					},
	  					scope: overlay
        			});
        		}
        	}
        }]
    })
});
