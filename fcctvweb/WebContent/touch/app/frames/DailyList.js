FccTVApp.frames.DailyList = new Ext.List({
	fullSreen : true,
	itemTpl : '{value}',
	grouped : true,
	store : FccTVApp.stores.DailyListStore,
	listeners:{
		itemtap: function(list, index, el, e){
			var record = list.getStore().getAt(index);
			var date = record.get('date');
			console.log(record);
			FccTVApp.stores.DailyStore.setProxy({
				type: 'ajax',
				url: './queryVideo.action',
				extraParams: {
					date: date
				}
			});
			FccTVApp.stores.DailyStore.load();
			FccTVApp.frames.QueryList.bindStore(FccTVApp.stores.DailyStore);
			FccTVApp.prevCard = this;
			var backBtn = Ext.getCmp("backButton");
			backBtn.setText(record.get('value'));
			this.up('tabpanel').getActiveItem().setActiveItem(FccTVApp.frames.QueryList, 'slide');
			
		}
	}
});
