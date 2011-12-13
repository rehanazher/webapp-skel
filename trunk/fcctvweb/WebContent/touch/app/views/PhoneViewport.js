FccTVApp.views.PhoneViewport = Ext.extend(Ext.TabPanel, {
	showAnimation : 'fade',
	//html: 'Test Page',
	title : bundle.getText("app.name.mobile"),
	fullscreen : true,
	hidden: true,
	items : [{
		iconCls : 'bookmarks',
		title : bundle.getText('tab.1'),
		layout : 'card',
		items : [new Ext.NestedList({
			id : 'navigatorPanel',
			store : FccTVApp.stores.NavigationStore,
			useToolbar : false,
			updateTitleText : false,
			listeners : {
				itemtap : function(list, index, el, e) {
					var record = list.getStore().getAt(index);
					var key = record.get('key');

					if(key == "2-1") {

					}

					var recordNode = list.recordNode;
					// var parentNode = recordNode ? recordNode.parentNode : null

					var backButton = Ext.getCmp("backButton");
					console.log(record);
					if(!record.get("leaf")) {
						if(recordNode.isRoot) {
							backButton.setText("FCC");
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
							this.up('tabpanel').getActiveItem().setActiveItem(card, 'slide');
							backButton.setText(record.get("text"));
							backButton.show();
						}
					}

					// var currList = this.getActiveItem();
					// var currIdx = this.items.indexOf(currList);
					// var	recordNode = currList.recordNode;
					// var title = this.renderTitleText(recordNode);
					// var parentNode = recordNode ? recordNode.parentNode : null, backTxt = (parentNode && !parentNode.isRoot) ? navPnl.renderTitleText(parentNode) : this.title || '';
					// var activeItem;
					//
					// if(currIdx <= 0) {
					// Ext.getCmp("backButton").hide();
					// } else {
					// var backButton = Ext.getCmp("backButton");
					// backButton.show();
					// }
					// FccTVApp.views.PhoneViewport.toggleUiBackButton(scope);
				}
			}
		})]
	}, {
		iconCls : 'time',
		title : bundle.getText('tab.2'),
		scroll : 'vertical',
		layout: 'card',
		items : [
new Ext.List({
	emptyText: bundle.getText('common.paging.not.record'),
    store: new Ext.data.JsonStore({
        model : 'QueryListModel',
        pageSize: configuredPageSize,
    	clearOnPageLoad: false,
    	currentPage: 1,
    	autoLoad: true,
        proxy : {
        	type: 'ajax',
			url: './queryVideo.action',
			extraParams: {
				'date': date.getFullYear() + "-" + (date.getMonth() < 9 ? "0" + (date.getMonth() + 1) : (date.getMonth() + 1)) + "-" + (date.getDate() < 10 ? "0" + date.getDate() : date.getDate())
			}
        },
    }),
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
    				'<h2>{contentname}</h2>',
    				'{contentdesc}',
    				'<p>{chName} {playTime} ({duration})</p>',
    			'</div>',
    		'</div>'
    		),
    listeners: {
    	itemtap: function(list, index, el, e){
    		var record = list.getStore().getAt(index);
    		var vPanel = new Ext.Panel({
    			iconCls : 'search',
    			title : bundle.getText('tab.5'),
    			scroll : 'vertical',
    			layout : 'auto',
    			items : [{
    				xtype : 'video',
    				url : record.get("videoUrl"),
    				loop : false,
    				width : 330,
    				height : 250,
    				posterUrl : record.get('posterUrl')
    			}, {
    				xtype: 'fieldset',
    				title: record.get('contentname'),
    				instructions: 'GTAG:' + record.get('gtvid'),
    				items:[{
    					html: '<div>' +
    							record.get('contentdesc') +
    							'<p>' + record.get('bstartTime') + ' ' + 
    							record.get('playTime') + '(' + record.get('duration') + ')</p>' +
    							'<p>' + record.get('chName') + '</p>' +
    						  '</div>'
    				}]
    			},{
					xtype: 'button',
					text: 'Add Favorate'
				}]
    		});
//    		Ext.getCmp("app-tab5").getActiveItem().setActiveItem(vPanel,'fade');
//    		var video = Ext.getCmp("video-player");
//    		video.url = record.get("videoUrl");
//    		video.posterUrl = record.get('posterUrl');
//    		console.log(Ext.getCmp("video-player"));
//    		console.log(this.up("tabpanel").query("> ")[4]);
    		var tab = this.up("tabpanel");
    		Ext.defer(function(){
    			tab.remove(4);
    			tab.setActiveItem(vPanel, 'fade');
    		}, 1000);
//    		Ext.getCmp("app-tab5").update(vPanel);
    		// vPanel.show();
//    		video.up("tab");
//    		video = new Ext.Video({
//    			id : 'video-player',
//    			url : record.get("videoUrl"),
//    			loop : false,
//    			width : 300,
//    			height : 250,
//    			posterUrl : record.get("posterUrl")
//    		});
    	}
    }
})
		]
	}, {
		iconCls : 'favorites',
		title : bundle.getText('tab.3'),
		html : 'Pressed Favorites'
	}, {
		iconCls : 'settings',
		title : bundle.getText('tab.4'),
		html : 'Pressed Settings'
	}, {
		iconCls : 'search',
		title : bundle.getText('tab.5'),
		scroll : 'vertical',
		layout : 'vbox',
		items : [
		         {
		        	 xtype: 'panel',
		        	 html: 'test'
		         }
//		         {
//			id : 'video-player',
//			xtype : 'video',
//			url : '',
//			loop : false,
//			width : 300,
//			height : 250,
//			posterUrl : ''
//		}, {
//			xtype: 'fieldset',
//			title: bundle.getText('player.title.unselect'),
//			layout: 'vbox',
//			items:[{
//				html: '<div style="width: 300px; height: 300px;"></div>'
//			}]
//		}
		]
	}],
	dockedItems : [{
		xtype : 'toolbar',
		title : 'FCC',
		items : [{
			xtype : 'button',
			id : 'backButton',
			text : bundle.getText("accounting.common.title.back"),
			ui : 'back',
			handler : function() {
				var navPnl = Ext.getCmp("navigatorPanel");
				var backButton = Ext.getCmp("backButton");

				// we were on a demo, slide back into
				// navigation
				if(this.up('tabpanel').getActiveItem().getActiveItem() != navPnl) {
					this.up('tabpanel').getActiveItem().setActiveItem(navPnl, {
						type : 'slide',
						reverse : true
					});
				} else {
					navPnl.onBackTap();
				}

				var activeItem = navPnl.getActiveItem();
				var recordNode = activeItem.recordNode;
				var parentNode = recordNode.parentNode;
				if(parentNode) {
					if(parentNode.isRoot) {
						backButton.setText("FCC");
					} else {
						backButton.setText(recordNode.attributes.record.get("text"));
					}
					backButton.show();
				} else {
					backButton.hide();
				}
				
				var selModel = activeItem.getSelectionModel();
                Ext.defer(selModel.deselectAll, 500, selModel);
			},
			hidden : true,
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
			if (tabPanel.child('') === newCard){
				var navPnl = Ext.getCmp("navigatorPanel");
				var activeItem = navPnl.getActiveItem();
				var recordNode = activeItem.recordNode;
				var parentNode = recordNode.parentNode;
				if (parentNode){
					Ext.getCmp("backButton").show();
				}
			}else{
				// Ext.getCmp("backButton").hide();
			}
		},
		beforecardswitch : function ( tabPanel, newCard, oldCard, index, animated ){
			if (tabPanel.child('') === newCard){
				// var navPnl = Ext.getCmp("navigatorPanel");
				// var activeItem = navPnl.getActiveItem();
				// var recordNode = activeItem.recordNode;
				// var parentNode = recordNode.parentNode;
				// if (parentNode){
					// Ext.getCmp("backButton").show();
				// }
			}else{
				Ext.getCmp("backButton").hide();
			}
		} 
	},

	onNavButtonTap : function() {
		console.log(this.navigationPanel);
		this.navigationPanel.showBy(this.navigationButton, 'fade');
	},
	onUiBack : function() {
		var navPnl = Ext.getCmp("navigatorPanel");
		console.log(navPnl);
		navPnl.onBackTap();
		// we were on a demo, slide back into
		// navigation
		this.toggleUiBackButton();
	},
	toggleUiBackButton : function() {
		var navPnl = Ext.getCmp("navigatorPanel");
		activeItem = navPnl.getActiveItem();
		recordNode = activeItem.recordNode;
		backTxt = (recordNode && !recordNode.isRoot) ? navPnl.renderTitleText(recordNode) : this.title || '';

		if(this.useTitleAsBackText) {
			this.backButton.setText(backTxt);
		}
		this.backButton.show();

	},
});
