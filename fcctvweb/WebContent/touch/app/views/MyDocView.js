FccTVApp.views.MyDocView = Ext.extend(Ext.TabPanel, {
	showAnimation : 'fade',
	//html: 'Test Page',
	title : bundle.getText("doc.title"),
	fullscreen : true,
	hidden: true,
	items : [{
		iconCls : 'bookmarks',
		title : bundle.getText('doc.tab.1'),
		layout : 'card',
		items : [new Ext.List({
			emptyText: bundle.getText('common.paging.not.record'),
		    store: FccTVApp.stores.MyDocStore,
		    plugins: [{
		        ptype: 'listpaging',
		        autoPaging: false,
		        loadMoreText: bundle.getText('common.paging.load.more')
		    }],
		    itemTpl: new Ext.XTemplate(
		    		'<img class="querylist-img" style="height: 68px; width: 65px;" src="{posterUrl}"/>',
		    		'<div class="querylist-anchor"></div>',
		    		'<div class="querylist-frame">',
		    			'<div class="querylist-desc">',
		    				'<h2>{name}</h2>',
		    				'{fileName}',
		    				'<p>',
		    				bundle.getText('video.item.text.create.at'),
		    				': {fullCreationTime}</p>',
		    			'</div>',
		    		'</div>'
		    		),
		    listeners: {
		    	itemtap: function(list, index, el, e){
		    		var record = list.getStore().getAt(index);
		    		window.open(record.get('videoUrl'));
//		    		var tab = this.up("tabpanel");
//		    		tab.setActiveItem(2);
//		    		if (tab.query("> ")[2].getActiveItem()){
//		    			tab.query("> ")[2].getActiveItem().destroy();
//		    		}
//		    		FccTVApp.player = new FccTVApp.frames.MyVideoPlayer({'record': record});
//		    		tab.query("> ")[2].setActiveItem(FccTVApp.player, 'fade');
		    	}
		    }
		})]
	}, {
		iconCls : 'search',
		title : bundle.getText('doc.tab.2'),
		layout: 'fit',
		scroll : 'both',
		items : [{
			xtype: 'panel',
			scroll: 'vertical',
			height: 4800,
			width: 100,
			layout: 'vbox',
			html : ''
		}]
	}],
	dockedItems : [{
		xtype : 'toolbar',
		title : bundle.getText('main.desc.video'),
		items : [{
			xtype : 'button',
			id : 'docBackButton',
			text : bundle.getText("main.title"),
			ui : 'back',
			handler : function() {
				
				if (FccTVApp.player){
					FccTVApp.player.destroy();
				}
				FccTVApp.views.viewport.hide();
				FccTVApp.views.viewport = FccTVApp.viewcache.MainView;
				FccTVApp.views.viewport.show();
			}
		},{
			xtype: 'button',
			id : 'docRefreshBtn',
			iconMask: true,
			iconCls: 'refresh',
			hidden: true,
			handler: function(){
				var tabPanel = this.up('tabpanel');
				var children = tabPanel.query('> ');
				
				if (children[1] === tabPanel.getActiveItem()){
					FccTVApp.loadMask.show();
					FccTVApp.frames.MyDocFavorite.bindStore(new Ext.data.JsonStore({
				        model : 'MyVideoModel',
				        pageSize: configuredPageSize,
				    	clearOnPageLoad: false,
				    	currentPage: 1,
				    	autoLoad: true,
				        proxy : {
				        	type : 'ajax',
				    		url : './queryMyFile.action',
				    		extraParams:{
				    			type: 2,
								favorite: 1
							}
				        }
				    }));
					FccTVApp.frames.MyDocFavorite.store.load(function(){
						FccTVApp.loadMask.hide();
					});
				}
			}
		}, {
			xtype : 'spacer'
		}, {
			xtype : 'searchfield',
			placeHolder : 'Search',
			name : 'searchfield',
			width : 100,
			listeners : {
				blur : function(text, e) {
					if(text.getValue()) {
						Ext.Msg.alert('System Dialog', 'Search key: ' + text.getValue());
					}
				}
			}
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
		cardswitch : function( tabPanel, newCard, oldCard, index, animated ){
			var children = tabPanel.query('> ');
			if (children[0] === newCard){
//				var navPnl = Ext.getCmp("navigatorPanel");
//				var activeItem = navPnl.getActiveItem();
//				var recordNode = activeItem.recordNode;
//				var parentNode = recordNode.parentNode;
				Ext.getCmp("docBackButton").show();
				
			}

			if (children[1] === newCard){
				Ext.getCmp('docRefreshBtn').show();
				
				// FccTVApp.views.viewport.hide();
				var openWindow = window.open("");
				var dom = openWindow.document;
				dom.write('<iframe style="z-index: 50; border: 0; height: 4800px; width: 100%;" src="./test.pdf" height="4800px" width="100%"></iframe>');
			}
			
			var activeItem = newCard.getActiveItem();
			if (activeItem && activeItem.getSelectionModel){
				var selModel = activeItem.getSelectionModel();
				Ext.defer(selModel.deselectAll, 500, selModel);
			}
		},
		beforecardswitch : function ( tabPanel, newCard, oldCard, index, animated ){
			var children = tabPanel.query('> ');
			if (tabPanel.child('') !== newCard){
				Ext.getCmp("docBackButton").hide();
				// var navPnl = Ext.getCmp("navigatorPanel");
				// var activeItem = navPnl.getActiveItem();
				// var recordNode = activeItem.recordNode;
				// var parentNode = recordNode.parentNode;
				// if (parentNode){
					// Ext.getCmp("backButton").show();
				// }
			}
			if (!(children[1] === newCard)){
				Ext.getCmp('docRefreshBtn').hide();
			}
		} 
	}
});
