FccTVApp.stores.MyVideoStore = new Ext.data.Store({
	model : 'QueryListModel',
	autoLoad: true,
	pageSize: configuredPageSize,
	clearOnPageLoad: false,
	currentPage: 1,
	proxy : {
		type : 'ajax',
		url : './queryEmpty.action'
	}
});