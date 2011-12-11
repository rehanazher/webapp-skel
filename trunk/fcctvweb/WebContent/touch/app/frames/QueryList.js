FccTVApp.frames.QueryList = new Ext.List({
	emptyText: '<p class="no-searches">No tweets found matching that search</p>',
    store: FccTVApp.stores.QueryListStore,
    plugins: [{
        ptype: 'listpaging',
        autoPaging: false,
        loadMoreText: bundle.getText('common.paging.load.more')
    }],
    itemTpl: new Ext.XTemplate('{posterUrl} {title}'),
    listeners: {
    	itemtap: function(){
    		
    	}
    }
});