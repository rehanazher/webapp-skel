FccTVApp.frames.Today = new Ext.List({
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
				date: dayList[0].date
			}
        }
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
    		var tab = this.up("tabpanel");
    		tab.setActiveItem(4);
    		tab.getActiveItem().setActiveItem(new FccTVApp.frames.Player({'record': record}), 'fade');
    	}
    }
});