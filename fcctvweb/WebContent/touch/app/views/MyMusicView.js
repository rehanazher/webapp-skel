FccTVApp.views.MyMusicView = Ext.extend(Ext.Panel, {//Ext.TabPanel, {
	showAnimation : 'fade',
	id : 'musicview',
	navigatorPref : 'music/',
	//html: 'Test Page',
	title : bundle.getText("music.title"),
	fullscreen : true,
	hidden: true,
	items : [
new Ext.List({
	disableSelection: true,
	emptyText: bundle.getText('common.paging.not.record'),
    store: FccTVApp.stores.MyMusicStore,
    plugins: [{
        ptype: 'listpaging',
        autoPaging: false,
        loadMoreText: bundle.getText('common.paging.load.more')
    }],
    itemTpl: new Ext.XTemplate(
    		'<audio name="myaudio-list" src="{videoUrl}" controls="controls" onplay="pauseExceptMe(this)"></audio>',
    		'<h2>{name}({fileName})</h2>'
    		),
    listeners: {
    	itemtap: function(list, index, el, e){
//    		var record = list.getStore().getAt(index);
//    		var tab = this.up("tabpanel");
//    		tab.setActiveItem(2);
//    		if (tab.query("> ")[2].getActiveItem()){
//    			tab.query("> ")[2].getActiveItem().destroy();
//    		}
//    		if (Ext.is.Phone){
//    			FccTVApp.loadMask.show();
//    			Ext.Ajax.request({
//    				url: './prepareVideo.action',
//    				params: {
//    					type: 'video',
//    					fileId: record.get('fileName')
//    				},
//    				success: function(response, opts) {
//					  var obj = Ext.decode(response.responseText);
//					  FccTVApp.loadMask.hide();
//					  FccTVApp.player = new FccTVApp.frames.MyVideoPlayer({'record': record, 'phoneVideoUrl': obj.msg});
//		    		  tab.query("> ")[2].setActiveItem(FccTVApp.player, 'fade');
//		    		  FccTVApp.addHistory(FccTVApp.viewcache.MyVideoView.navigatorPref + 'player');
//					},
//					failure: function(response, opts) {
//					  FccTVApp.loadMask.hide();
//					} 
//				});
//    		}else {
//	    		FccTVApp.addHistory(FccTVApp.viewcache.MyVideoView.navigatorPref + 'player');
//	    		FccTVApp.player = new FccTVApp.frames.MyVideoPlayer({'record': record});
//	    		tab.query("> ")[2].setActiveItem(FccTVApp.player, 'fade');
//    		}
    	}
    }
})
//	         {
//		iconCls : 'bookmarks',
//		title : bundle.getText('music.tab.1'),
//		layout : 'card',
//		items : [new Ext.List({
//			disableSelection: true,
//			emptyText: bundle.getText('common.paging.not.record'),
//		    store: FccTVApp.stores.MyMusicStore,
//		    plugins: [{
//		        ptype: 'listpaging',
//		        autoPaging: false,
//		        loadMoreText: bundle.getText('common.paging.load.more')
//		    }],
//		    itemTpl: new Ext.XTemplate(
//		    		'<audio src="{videoUrl}" controls="controls"></audio>',
//		    		'<h2>{name}({fileName})</h2>'
//		    		),
//		    listeners: {
//		    	itemtap: function(list, index, el, e){
////		    		var record = list.getStore().getAt(index);
////		    		var tab = this.up("tabpanel");
////		    		tab.setActiveItem(2);
////		    		if (tab.query("> ")[2].getActiveItem()){
////		    			tab.query("> ")[2].getActiveItem().destroy();
////		    		}
////		    		if (Ext.is.Phone){
////		    			FccTVApp.loadMask.show();
////		    			Ext.Ajax.request({
////		    				url: './prepareVideo.action',
////		    				params: {
////		    					type: 'video',
////		    					fileId: record.get('fileName')
////		    				},
////		    				success: function(response, opts) {
////							  var obj = Ext.decode(response.responseText);
////							  FccTVApp.loadMask.hide();
////							  FccTVApp.player = new FccTVApp.frames.MyVideoPlayer({'record': record, 'phoneVideoUrl': obj.msg});
////				    		  tab.query("> ")[2].setActiveItem(FccTVApp.player, 'fade');
////				    		  FccTVApp.addHistory(FccTVApp.viewcache.MyVideoView.navigatorPref + 'player');
////							},
////							failure: function(response, opts) {
////							  FccTVApp.loadMask.hide();
////							} 
////						});
////		    		}else {
////			    		FccTVApp.addHistory(FccTVApp.viewcache.MyVideoView.navigatorPref + 'player');
////			    		FccTVApp.player = new FccTVApp.frames.MyVideoPlayer({'record': record});
////			    		tab.query("> ")[2].setActiveItem(FccTVApp.player, 'fade');
////		    		}
//		    	}
//		    }
//		})]
//	}, {
//		iconCls : 'favorites',
//		title : bundle.getText('video.tab.2'),
//		layout: 'card',
//		items : [FccTVApp.frames.MyMusicFavorite]
//	}
	],
	dockedItems : [{
		xtype : 'toolbar',
		title : bundle.getText('music.title'),
		items : [{
			xtype : 'button',
			id : 'musicBackButton',
			text : bundle.getText("main.title"),
			ui : 'back',
			handler : function() {
				
				if (FccTVApp.player){
					FccTVApp.player.destroy();
				}
				
				FccTVApp.addHistory("main");
				FccTVApp.views.viewport.hide();
				FccTVApp.views.viewport = FccTVApp.viewcache.MainView;
				FccTVApp.views.viewport.show();
			}
		},{
			xtype: 'button',
			id : 'musicRefreshBtn',
			iconMask: true,
			iconCls: 'refresh',
			hidden: true,
			handler: function(){
				var tabPanel = this.up('tabpanel');
				var children = tabPanel.query('> ');
				
				if (children[1] === tabPanel.getActiveItem()){
					FccTVApp.loadMask.show();
					FccTVApp.frames.MyVideoFavorite.bindStore(new Ext.data.JsonStore({
				        model : 'MyVideoModel',
				        pageSize: configuredPageSize,
				    	clearOnPageLoad: false,
				    	currentPage: 1,
				    	autoLoad: true,
				        proxy : {
				        	type : 'ajax',
				    		url : './queryMyFile.action',
				    		extraParams:{
				    			type: 3,
								favorite: 1
							}
				        }
				    }));
					FccTVApp.frames.MyVideoFavorite.store.load(function(){
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
//	listeners: {
//		cardswitch : function( tabPanel, newCard, oldCard, index, animated ){
//			var children = tabPanel.query('> ');
//			if (children[0] === newCard){
////				var navPnl = Ext.getCmp("navigatorPanel");
////				var activeItem = navPnl.getActiveItem();
////				var recordNode = activeItem.recordNode;
////				var parentNode = recordNode.parentNode;
//				Ext.getCmp("musicBackButton").show();
//				
//			}
//
//			if (children[1] === newCard){
//				Ext.getCmp('musicRefreshBtn').show();
//			}
//			
//			var activeItem = newCard.getActiveItem();
//			if (activeItem && activeItem.getSelectionModel){
//				var selModel = activeItem.getSelectionModel();
//				Ext.defer(selModel.deselectAll, 500, selModel);
//			}
//			
//			FccTVApp.addHistory(this.retrieveNav());
//		},
//		beforecardswitch : function ( tabPanel, newCard, oldCard, index, animated ){
//			var children = tabPanel.query('> ');
//			if (tabPanel.child('') !== newCard){
//				Ext.getCmp("musicBackButton").hide();
//				// var navPnl = Ext.getCmp("navigatorPanel");
//				// var activeItem = navPnl.getActiveItem();
//				// var recordNode = activeItem.recordNode;
//				// var parentNode = recordNode.parentNode;
//				// if (parentNode){
//					// Ext.getCmp("backButton").show();
//				// }
//			}
//			if (!(children[1] === newCard)){
//				Ext.getCmp('musicRefreshBtn').hide();
//			}
//		} 
//	},
	retrieveNav : function(){
		var children = this.query('> ');
		if (children[0] == this.getActiveItem()){
			return FccTVApp.viewcache.MyMusicView.navigatorPref;
		}else if (children[1] == this.getActiveItem()){
			return FccTVApp.viewcache.MyMusicView.navigatorPref + "/favorite";
		}
	}
});

function pauseExceptMe(src){
	var a = document.getElementsByName("myaudio-list");
	for (var i = 0; i < a.length; i++){
		if (a[i] != src){
			a[i].pause();
		}
	}
}
