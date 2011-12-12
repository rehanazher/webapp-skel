

FccTVApp.stores.DailyStore = new Ext.data.JsonStore({
    model : 'QueryListModel',
    pageSize: configuredPageSize,
	clearOnPageLoad: false,
	currentPage: 1,
	autoLoad: true,
    proxy : {
    	type : 'ajax',
    	url : './queryEmpty.action'
    },
});
