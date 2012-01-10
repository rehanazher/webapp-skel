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

    	}
    }
})
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
