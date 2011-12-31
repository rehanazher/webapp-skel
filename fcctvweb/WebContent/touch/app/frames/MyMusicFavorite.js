FccTVApp.frames.MyMusicFavorite = new Ext.List({
	emptyText: bundle.getText('common.paging.not.record'),
    store: new Ext.data.JsonStore({
        model : 'MyVideoModel',
        pageSize: configuredPageSize,
    	clearOnPageLoad: false,
    	currentPage: 1,
    	autoLoad: false,
        proxy : {
        	type : 'ajax',
    		url : './queryMyFile.action',
    		extraParams:{
    			type: 3,
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
    		'<audio src="{videoUrl}" controls="controls"></audio>',
    		'<h2>{name}({fileName})</h2>'
    		),
    listeners: {
    	itemtap: function(list, index, el, e){
    	}
    }
});