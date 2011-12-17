FccTVApp.views.MyVideoView = Ext.extend(Ext.TabPanel, {
	showAnimation : 'fade',
	//html: 'Test Page',
	title : bundle.getText("video.title"),
	fullscreen : true,
	hidden: true,
	items : [{
		iconCls : 'bookmarks',
		title : bundle.getText('tab.1'),
		layout : 'card',
		items : [new Ext.List({
			emptyText: bundle.getText('common.paging.not.record'),
		    store: FccTVApp.stores.MyVideoStore,
		    plugins: [{
		        ptype: 'listpaging',
		        autoPaging: false,
		        loadMoreText: bundle.getText('common.paging.load.more')
		    }],
		    itemTpl: new Ext.XTemplate(
		    		'<img class="querylist-img" src="{posterUrl}"/>',
		    		'<div class="querylist-anchor"></div>',
		    		'<div class="querylist-frame">',
		    			'<div class="querylist-desc">',
		    				'<h2>{name}</h2>',
		    				'{fileName}',
		    				'<p>',
		    				bundle.getText('video.item.text.create.at'),
		    				'{creationTime})</p>',
		    			'</div>',
		    		'</div>'
		    		),
		    listeners: {
		    	itemtap: function(list, index, el, e){
		    		var record = list.getStore().getAt(index);
		    		var tab = this.up("tabpanel");
		    		tab.setActiveItem(2);
		    		tab.query("> ")[2].setActiveItem(new FccTVApp.frames.Player({'record': record}), 'fade');
		    		// tab.getActiveItem().setActiveItem(new FccTVApp.frames.Player({'record': record}), 'fade');
		    	}
		    }
		})]
	}, {
		iconCls : 'favorites',
		title : bundle.getText('tab.2'),
		layout: 'card',
		items : [FccTVApp.frames.Favorite]
	}, {
		iconCls : 'search',
		title : bundle.getText('tab.3'),
		scroll : 'vertical',
		layout : 'card',
		items : [
		         {
		        	 xtype: 'panel',
		        	 html: bundle.getText('video.player.no.select')
		         }
		]
	}],
	dockedItems : [{
		xtype : 'toolbar',
		title : bundle.getText('main.desc.video'),
		items : [{
			xtype : 'button',
			id : 'backButton',
			text : bundle.getText("main.title"),
			ui : 'back',
			handler : function() {
				FccTVApp.views.viewport.hide();
				FccTVApp.views.viewport = FccTVApp.viewcache.MainView;
				FccTVApp.views.viewport.show();
			}
		},{
			xtype: 'button',
			id : 'refreshBtn',
			iconMask: true,
			iconCls: 'refresh',
			hidden: true,
			handler: function(){
				var tabPanel = this.up('tabpanel');
				var children = tabPanel.query('> ');
				
				if (children[1] === tabPanel.getActiveItem()){
					FccTVApp.loadMask.show();
					FccTVApp.frames.Favorite.bindStore(new Ext.data.JsonStore({
					        model : 'QueryListModel',
					        pageSize: configuredPageSize,
					    	clearOnPageLoad: false,
					    	currentPage: 1,
					    	autoLoad: true,
					        proxy : {
					        	type: 'ajax',
								url: './queryVideo.action',
								extraParams: {
									favorite: 1
								}
					        }
					    }));
					FccTVApp.frames.Favorite.store.load(function(){
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
				Ext.getCmp("backButton").show();
				
			}

			if (children[1] === newCard || children[2] === newCard){
				Ext.getCmp('refreshBtn').show();
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
				Ext.getCmp("backButton").hide();
				// var navPnl = Ext.getCmp("navigatorPanel");
				// var activeItem = navPnl.getActiveItem();
				// var recordNode = activeItem.recordNode;
				// var parentNode = recordNode.parentNode;
				// if (parentNode){
					// Ext.getCmp("backButton").show();
				// }
			}
			if (!(children[1] === newCard || children[2] === newCard)){
				Ext.getCmp('refreshBtn').hide();
			}
		} 
	}
});
