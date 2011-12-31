FccTVApp.frames.ChannelList = new Ext.List({
	fullSreen : true,
	nav: 'nav/3',
	parentTitle: bundle.getText('menu.3'),
	itemTpl : '{chName}',
	store : FccTVApp.stores.ChannelStore,
	listeners:{
		itemtap: function(list, index, el, e){
			var record = list.getStore().getAt(index);
			var channel = record.get('ch');
			FccTVApp.stores.DailyStore.setProxy({
				type: 'ajax',
				url: './queryVideo.action',
				extraParams: {
					ch: channel
				}
			});
			FccTVApp.loadMask.show();
			
			var prevCard = this;
			FccTVApp.stores.DailyStore.load(function(){
				FccTVApp.loadMask.hide();
				FccTVApp.frames.QueryList.bindStore(FccTVApp.stores.DailyStore);
				FccTVApp.prevCard = prevCard;
				var backBtn = Ext.getCmp("backButton");
				backBtn.setText(record.get('chName'));
				prevCard.up('tabpanel').getActiveItem().setActiveItem(FccTVApp.frames.QueryList, 'slide');
				FccTVApp.addHistory(FccTVApp.viewcache.TvView.navigatorPref + 'channel/' + channel);
			});
			
		}
	}
});
