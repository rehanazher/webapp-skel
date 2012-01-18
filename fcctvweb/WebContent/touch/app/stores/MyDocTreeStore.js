FccTVApp.stores.MyDocTreeStore = new Ext.data.TreeStore({
    model: 'MyDocModel',
    autoLoad: true,
    proxy: {
        url: './retrieveDocTree.action',
        type: 'ajax',
        reader: {
            type: 'tree',
            root: 'children'
        }
    }
});

FccTVApp.stores.MyDocTreeStore.load();