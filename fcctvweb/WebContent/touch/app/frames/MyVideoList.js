FccTVApp.frames.MyVideoList = new Ext.List({
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
    				'<h2>{contentname}</h2>',
    				'{contentdesc}',
    				'<p>{chName} {playTime} ({duration})</p>',
    			'</div>',
    		'</div>'
    		),
    listeners: {
    	itemtap: function(list, index, el, e){
    		var record = list.getStore().getAt(index);
    		var tab = this.up("tabpanel");
    		tab.setActiveItem(4);
    		tab.query("> ")[4].setActiveItem(new FccTVApp.frames.Player({'record': record}), 'fade');
    		// tab.getActiveItem().setActiveItem(new FccTVApp.frames.Player({'record': record}), 'fade');
    	}
    }
});