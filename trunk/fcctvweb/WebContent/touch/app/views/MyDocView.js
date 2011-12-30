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
	        			Ext.Ajax.request({
	        				url: './docAnalyze.action',
	        				params: {
	        					fileId : record.get('key'),
	        					type: record.get('extName')
	        				},
		        			success: function(response, opts) {
		  					  var obj = Ext.decode(response.responseText);
		  					  FccTVApp.loadMask.hide();
		  					  location.href='./docPrev.action?fileId=' + record.get('key') + '&type=' + record.get('extName') + '&height=' + obj.value.height + '&width=' + obj.value.width;
		  					  
//		  					  var win = window.open('test.html');
//		  					  var dom = win.document;
//		  					  console.log(obj);
//		  					  dom.write('<div style="position: fixed; top:0; left:0;-webkit-box-orient: horizontal; -webkit-box-direction: normal; -webkit-box-pack: start; -webkit-box-align: center; min-width: 1434px; height: 46px; ">');
//		  					  dom.write('<div style="background-image: -webkit-gradient(linear, 50% 0%, 50% 100%, color-stop(0%, #466890), color-stop(2%, #1f2f41), color-stop(100%, #080c11));background-image: -webkit-linear-gradient(#466890,#1f2f41 2%,#080c11);background-image: linear-gradient(#466890,#1f2f41 2%,#080c11);" class=" x-button x-button-back">');
//		  					  dom.write('<span class="x-button-label" id="ext-gen1167">Main</span></div>');
//		  					  dom.write('<div id="ext-comp-1047" class=" x-component" style="-webkit-box-flex: 1; width: 1366px; "></div></div>');
//		  					  dom.write('<iframe src="./watch.action?type=' + record.get('extName') + '&fileId=' + record.get('fileName') + '" style="border: 0;height: ' + obj.value.height + '; width: ' + obj.value.width + ';"></iframe>');
		  					},
		  					failure: function(response, opts) {
		  					  FccTVApp.loadMask.hide();
		  					} 
	        			});
	        		}
	        	},
	        	itemswipe : function(list, index, el, e) {
	        		console.log('swipe:' + index);
	        		
	        		
	        	}
	        }
		};
    }
});
