FccTVApp.stores.MyDocTreeStore = new Ext.data.TreeStore({
    model: 'MyDocModel',
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