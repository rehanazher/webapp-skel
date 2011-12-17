FccTVApp.stores.MyVideoStore = new Ext.data.Store({
	model : 'MyVideoModel',
	autoLoad: true,
	pageSize: configuredPageSize,
	clearOnPageLoad: false,
	currentPage: 1,
	proxy : {
		type : 'ajax',
		url : './queryMyFile.action',
		extraParams:{
			type: 1
		}
	}
});