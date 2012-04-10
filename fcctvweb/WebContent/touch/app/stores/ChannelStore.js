Ext.regModel('ChannelModel', {
    fields: [
             {name: 'ch', type: 'int'},
             'chName', 
             {name: 'chNwId', type: 'int'}, 
             {name: 'chServiceId', type: 'int'}
            ]
});

FccTVApp.stores.ChannelStore = new Ext.data.JsonStore({
    model : 'ChannelModel',
    autoLoad : false,
    proxy : {
    	type: 'ajax',
    	url: './retrieveChannels.action'
    }
});
