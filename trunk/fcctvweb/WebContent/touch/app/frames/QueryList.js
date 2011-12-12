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
    					html: '<div style="width: 300px; height: 300px;"></div>'
    				}]
    			}]
    		});
//    		Ext.getCmp("app-tab5").getActiveItem().setActiveItem(vPanel,'fade');
    		this.up("tabpanel").setActiveItem(4);
//    		console.log(this.up("tabpanel").query("> ")[4].getActiveItem());
    		this.up("tabpanel").getActiveItem().setActiveItem(vPanel,'fade');
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