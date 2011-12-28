FccTVApp.frames.DailyList = new Ext.List({
	fullSreen : true,
	nav: 'nav/1',
	parentTitle: bundle.getText('menu.1'),
	itemTpl : '{value}',
	grouped : true,
	store : FccTVApp.stores.DailyListStore,
	listeners:{
		itemtap: function(list, index, el, e){
			var record = list.getStore().getAt(index);
			var date = record.get('date');
			FccTVApp.stores.DailyStore.setProxy({
				type: 'ajax',
				url: './queryVideo.action',
				extraParams: {
					date: date
				}
			});
			FccTVApp.loadMask.show();
			FccTVApp.stores.DailyStore.load(function(){
				FccTVApp.loadMask.hide();
			});
			FccTVApp.frames.QueryList.bindStore(FccTVApp.stores.DailyStore);
			FccTVApp.prevCard = this;
			var backBtn = Ext.getCmp("backButton");
			backBtn.setText(record.get('value'));
			this.up('tabpanel').getActiveItem().setActiveItem(FccTVApp.frames.QueryList, 'slide');
			FccTVApp.addHistory(FccTVApp.viewcache.TvView.navigatorPref + 'daily/' + date);
		}
	}
});
