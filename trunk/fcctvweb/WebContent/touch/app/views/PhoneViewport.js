FccTVApp.views.PhoneViewport = Ext.extend(Ext.TabPanel, {
	showAnimation : 'fade',
	//html: 'Test Page',
	title : bundle.getText("app.name.mobile"),
	fullscreen : true,
	hidden: true,
	items : [{
		iconCls : 'bookmarks',
		title : 'Main',
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
					if(!record.get("leaf")) {
						if(recordNode.isRoot) {
							backButton.setText("FCC");
							backButton.show();
						} else {

							if(recordNode.attributes.record) {
								backButton.setText(recordNode.attributes.record.get("text"));
							}
							backButton.show();
						}
					} else {
						var card = record.get("card");
						if(card) {
							this.up('tabpanel').getActiveItem().setActiveItem(card, 'slide');
							backButton.setText("Back");
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
		title : 'Today List',
		html : '<a href="test.pdf" target="_blank">pdf</a><br /> <a href="test.doc" target="_blank">doc</a>'
	}, {
		iconCls : 'favorites',
		title : 'Favorites',
		html : 'Pressed Favorites'
	}, {
		iconCls : 'settings',
		title : 'Settings',
		html : 'Pressed Settings'
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
