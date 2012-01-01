FccTVApp.stores.MyDocTreeStore = new Ext.data.TreeStore({
    model: 'MyDocModel',
    proxy: {
        url: './retrieveDocTree.action',
        type: 'ajax',
        reader: {
            type: 'tree',
            root: 'children'
        }
    },
    listeners:{
    	load: function(store, records, isSuccess ){
    		console.log('tree loaded!');
    	},
    	update: function(){
    		console.log('tree updated!');
    	}
    }
});

FccTVApp.stores.MyDocTreeStore.load();