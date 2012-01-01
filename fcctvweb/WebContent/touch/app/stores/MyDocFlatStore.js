FccTVApp.stores.MyDocFlatStore = new Ext.data.JsonStore({
    model: 'MyDocModel',
    autoLoad: true,
    proxy: {
        url: './retrieveDocFlatDir.action',
        type: 'ajax',
    }
});
