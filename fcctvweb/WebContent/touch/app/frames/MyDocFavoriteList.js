FccTVApp.frames.MyDocFavorite = new Ext.List({
	emptyText: bundle.getText('common.paging.not.record'),
    store: new Ext.data.JsonStore({
        model : 'MyVideoModel',
        pageSize: configuredPageSize,
    	clearOnPageLoad: false,
    	currentPage: 1,
    	autoLoad: true,
        proxy : {
        	type : 'ajax',
    		url : './queryMyFile.action',
    		extraParams:{
    			type: 2,
				favorite: 1
			}
        }
    }),
    plugins: [{
        ptype: 'listpaging',
        autoPaging: false,
        loadMoreText: bundle.getText('common.paging.load.more')
    }],
    itemTpl: new Ext.XTemplate(
    		'<img class="querylist-img" style="height: 68px; width: 65px;" src="{posterUrl}"/>',
    		'<div class="querylist-anchor"></div>',
    		'<div class="querylist-frame">',
    			'<div class="querylist-desc">',
    				'<h2>{name}</h2>',
    				'{fileName}',
    				'<p>',
    				bundle.getText('video.item.text.create.at'),
    				': {fullCreationTime}</p>',
    			'</div>',
    		'</div>'
    		),
    listeners: {
    	itemtap: function(list, index, el, e){
    		var record = list.getStore().getAt(index);
    		console.log(record.get('videoUrl'))
//    		var tab = this.up("tabpanel");
//    		tab.setActiveItem(2);
//    		if (tab.query("> ")[2].getActiveItem()){
//    			tab.query("> ")[2].getActiveItem().destroy();
//    		}
//    		FccTVApp.player = new FccTVApp.frames.MyVideoPlayer({'record': record});
//    		tab.query("> ")[2].setActiveItem(FccTVApp.player, 'fade');
    	}
    }
});