FccTVApp.frames.MyVideoFavorite = new Ext.List({
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
    			type: 1,
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
    		var tab = this.up("tabpanel");
    		tab.setActiveItem(2);
    		if (tab.query("> ")[2].getActiveItem()){
    			tab.query("> ")[2].getActiveItem().destroy();
    		}
    		if (Ext.is.Phone){
    			FccTVApp.loadMask.show();
    			Ext.Ajax.request({
    				url: './prepareVideo.action',
    				params: {
    					type: 'video',
    					fileId: record.get('fileName')
    				},
    				success: function(response, opts) {
					  var obj = Ext.decode(response.responseText);
					  FccTVApp.loadMask.hide();
					  FccTVApp.player = new FccTVApp.frames.MyVideoPlayer({'record': record, 'phoneVideoUrl': obj.msg});
		    		  tab.query("> ")[2].setActiveItem(FccTVApp.player, 'fade');
		    		  FccTVApp.addHistory(FccTVApp.viewcache.MyVideoView.navigatorPref + 'player');
					},
					failure: function(response, opts) {
					  FccTVApp.loadMask.hide();
					} 
				});
    		}else {
	    		FccTVApp.addHistory(FccTVApp.viewcache.MyVideoView.navigatorPref + 'player');
	    		FccTVApp.player = new FccTVApp.frames.MyVideoPlayer({'record': record});
	    		tab.query("> ")[2].setActiveItem(FccTVApp.player, 'fade');
    		}
    	}
    }
});