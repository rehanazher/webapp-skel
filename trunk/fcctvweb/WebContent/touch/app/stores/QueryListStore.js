Ext.regModel('QueryListModel', {
	fields : [ {
		name : 'posterUrl',
		type : 'string'
	}, {
		name : 'title',
		type : 'string'
	}, {
		name : 'desc',
		type : 'string'
	},{
		name : 'url',
		type : 'string'
	} ]
});

FccTVApp.stores.QueryListStore = new Ext.data.Store({
	model : 'QueryListModel',
	autoLoad: true,
	pageSize: configuredPageSize,
	clearOnPageLoad: false,
	currentPage: 1,
	proxy : {
		type : 'ajax',
		url : './test.action'
	}
});