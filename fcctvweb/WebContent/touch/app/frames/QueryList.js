FccTVApp.frames.QueryList = new Ext.List({
	emptyText: bundle.getText('common.paging.not.record'),
    store: FccTVApp.stores.QueryListStore,
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
    			layout : 'vbox',
    			scroll : 'vertical',
    			layout : 'vbox',
    			items : [{
    				xtype : 'video',
    				url : record.get("videoUrl"),
    				loop : false,
    				width : 300,
    				height : 250,
    				posterUrl : record.get('posterUrl')
    			}, {
    				xtype: 'fieldset',
    				title: bundle.getText('player.title.unselect'),
    				layout: 'vbox',
    				items:[{
    					html: '<div style="width: 300px; height: 300px;">Test</div>'
    				}]
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
});