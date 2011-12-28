FccTVApp.views.TvView = Ext.extend(Ext.TabPanel, {
	navigatorPref : 'tv/',
	showAnimation : 'fade',
	//html: 'Test Page',
	title : bundle.getText("app.name.mobile"),
	fullscreen : true,
	hidden: true,
	items : [{
		iconCls : 'bookmarks',
		title : bundle.getText('tab.1'),
		layout : 'card',
		nav: 'nav',
		items : [new Ext.NestedList({
			id : 'navigatorPanel',
			store : FccTVApp.stores.NavigationStore,
			useToolbar : false,
			updateTitleText : false,
			listeners : {
				itemtap : function(list, index, el, e) {
					var record = list.getStore().getAt(index);
					var key = record.get('key');
					var url = record.get('url');

					var recordNode = list.recordNode;
					// var parentNode = recordNode ? recordNode.parentNode : null
					
					FccTVApp.addHistory(FccTVApp.viewcache.TvView.navigatorPref + "nav/" + record.get('nav'));
					
					var backButton = Ext.getCmp("backButton");
					if(!record.get("leaf")) {
						if(recordNode.isRoot) {
							backButton.setText(record.get('text'));
							backButton.show();
						} else {

							if(recordNode.attributes.record) {
								backButton.setText(record.get("text"));
							}
							backButton.show();
						}
					} else {
						var card = record.get("card");
						if(card) {
							if (url == 'type-query'){
								FccTVApp.stores.TypeStore.setProxy({
									type: 'ajax',
									url: './queryVideo.action',
									extraParams: {
										type: key
									}
								});
								FccTVApp.loadMask.show();
								FccTVApp.stores.TypeStore.load(function(){
									FccTVApp.loadMask.hide();
								});
								FccTVApp.frames.QueryList.bindStore(FccTVApp.stores.TypeStore);
							}
							
							FccTVApp.prevCard = Ext.getCmp("navigatorPanel");
							FccTVApp.prevTitle = record.get('text');
							this.up('tabpanel').getActiveItem().setActiveItem(card, 'slide');
							backButton.setText(record.get("text"));
							backButton.show();
						}
					}
				}
			}
		})]
	}, {
		iconCls : 'time',
		title : bundle.getText('tab.2'),
		scroll : 'vertical',
		layout: 'card',
		nav: 'today',
		items : [FccTVApp.frames.Today]
	}, {
		iconCls : 'favorites',
		title : bundle.getText('tab.3'),
		layout: 'card',
		nav: 'favorite',
		items : [FccTVApp.frames.Favorite]
	}, {
		iconCls : 'settings',
		nav: 'setting',
		title : bundle.getText('tab.4'),
		html : 'Pressed Settings'
	},{
		iconCls : 'search',
		nav: 'player',
		title : bundle.getText('tab.5'),
		scroll : 'both',
		layout : 'vbox',
		items : []
	}],
	dockedItems : [{
		xtype : 'toolbar',
		title : bundle.getText('app.name.mobile'),
		items : [{
			xtype : 'button',
			id : 'backButton',
			text : bundle.getText("main.title"),
			ui : 'back',
			handler : function() {
				var navPnl = Ext.getCmp("navigatorPanel");
				var backButton = Ext.getCmp("backButton");
				var activeItem = navPnl.getActiveItem();
				var recordNode = activeItem.recordNode;
				var parentNode = recordNode.parentNode;
				
				// navigation
				if(this.up('tabpanel').getActiveItem().getActiveItem() != navPnl) {
					if (FccTVApp.prevCard === navPnl){
						this.up('tabpanel').getActiveItem().setActiveItem(FccTVApp.prevCard, {
							type : 'slide',
							reverse : true
						});
						// backButton.setText(FccTVApp.prevTitle);
						
						if (recordNode.isRoot){
							backButton.setText(bundle.getText("main.title"));
							FccTVApp.addHistory(FccTVApp.viewcache.TvView.navigatorPref + "nav");
						} else {
							FccTVApp.addHistory(FccTVApp.viewcache.TvView.navigatorPref + "nav/" + recordNode.attributes.record.data.nav);
							backButton.setText(recordNode.attributes.record.get("text"));
						}
					}else {
						
						var prevCard = FccTVApp.prevCard;
						FccTVApp.addHistory(FccTVApp.viewcache.TvView.navigatorPref + prevCard.nav);
						// nav 1:1
						this.up('tabpanel').getActiveItem().setActiveItem(prevCard, {
							type : 'slide',
							reverse : true
						});
						backButton.setText(prevCard.parentTitle);
						
						FccTVApp.prevCard = navPnl;
					}
				} else {
					
					if (recordNode.isRoot) {
						if (FccTVApp.player){
							FccTVApp.player.destroy();
						}
						FccTVApp.views.viewport.hide();
						FccTVApp.addHistory("main");
						FccTVApp.views.viewport = FccTVApp.viewcache.MainView;
						FccTVApp.views.viewport.show();
					}else{
						var list = navPnl.getSubList(parentNode);
						list = navPnl.add(list);
						navPnl.setActiveItem(list, {
							type : 'slide',
							reverse : true
						});

						if(parentNode.isRoot) {
							backButton.setText(bundle.getText("main.title"));
							FccTVApp.addHistory(FccTVApp.viewcache.TvView.navigatorPref + "nav");
						} else {
							FccTVApp.addHistory(FccTVApp.viewcache.TvView.navigatorPref + "nav/" + parentNode.attributes.record.data.nav);
							backButton.setText(parentNode.attributes.record.get("text"));
						}
					}
				}
				
				var selModel = activeItem.getSelectionModel();
                Ext.defer(selModel.deselectAll, 500, selModel);
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
					FccTVApp.frames.Today.bindStore(new Ext.data.JsonStore({
				        model : 'QueryListModel',
				        pageSize: configuredPageSize,
				    	clearOnPageLoad: false,
				    	currentPage: 1,
				    	autoLoad: true,
				        proxy : {
				        	type: 'ajax',
							url: './queryVideo.action',
							extraParams: {
								date: dayList[0].date
							}
				        }
				    }));
					FccTVApp.frames.Today.store.load(function(){
						FccTVApp.loadMask.hide();
					});
				}else if (children[2] === tabPanel.getActiveItem()){
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
			FccTVApp.addHistory(this.retrieveNav());
		},
		beforecardswitch : function ( tabPanel, newCard, oldCard, index, animated ){
			var children = tabPanel.query('> ');
			if (children[0] !== newCard){
				Ext.getCmp("backButton").hide();
			}
			if (!(children[1] === newCard || children[2] === newCard)){
				Ext.getCmp('refreshBtn').hide();
			}
		}
	},
	initComponent : function() {
		FccTVApp.views.TvView.superclass.initComponent.call(this, arguments);
	},
	retrieveNav : function(){
		var children = this.query('> ');
		if (children[0] == this.getActiveItem()){
			var navPnl = Ext.getCmp('navigatorPanel');
			var listActItem = children[0].getActiveItem(); 
			if ( listActItem === navPnl){
				var activeItem = navPnl.getActiveItem();
				var recordNode = activeItem.recordNode;
				if (recordNode.isRoot){
					return FccTVApp.viewcache.TvView.navigatorPref + 'nav';
				}else{
					return FccTVApp.viewcache.TvView.navigatorPref + 'nav/' + recordNode.attributes.record.get('nav');
				}
			}else{
				if (listActItem === FccTVApp.frames.DailyList){
					return FccTVApp.viewcache.TvView.navigatorPref + 'nav/1';
				}else if (listActItem === FccTVApp.frames.QueryList){
					if (FccTVApp.frames.QueryList.store.proxy.extraParams.date){
						return FccTVApp.viewcache.TvView.navigatorPref + 'daily/' + FccTVApp.frames.QueryList.store.proxy.extraParams.date;
					}else if (FccTVApp.frames.QueryList.store.proxy.extraParams.type){
						var activeNode = navPnl.store.getRootNode().findChildBy(function(n){
							return n.attributes.record.get('key') === FccTVApp.frames.QueryList.store.proxy.extraParams.type;
						}, true, true);
						return FccTVApp.viewcache.TvView.navigatorPref + 'nav/' + activeNode.attributes.record.get('nav');
					}
				}
			}
		}else if (children[4] == this.getActiveItem()){
			return FccTVApp.viewcache.TvView.navigatorPref + 'player';
		}else if (children[1] == this.getActiveItem()){
			return FccTVApp.viewcache.TvView.navigatorPref + 'today';
		}else if (children[2] == this.getActiveItem()){
			return FccTVApp.viewcache.TvView.navigatorPref + 'favorite';
		}else if (children[3] == this.getActiveItem()){
			return FccTVApp.viewcache.TvView.navigatorPref + 'setting';
		}
	}

});
