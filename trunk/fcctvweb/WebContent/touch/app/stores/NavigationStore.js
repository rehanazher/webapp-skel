FccTVApp.stores.Structure = [{
    	text: 'Text 1',
    	url: 'url 1',
    	key: '1',
    	items: [{
    		text: 'Text 1-1',
    		url: 'url 1-1',
    		key: '1-1',
    		items: [{
	    		text: 'Text 1-1-1',
	    		url: 'url 1-1-1',
	    		key: '1-1-1',
	    		leaf: true
	    	},{
	    		text: 'Text 1-1-2',
	    		url: 'url 1-1-2',
	    		key: '1-1-2',
	    		leaf: true
	    	}]
    	},{
    		text: 'Text 1-2',
    		url: 'url 1-2',
    		key: '1-2',
    		leaf: true
    	},{
    		text: 'Text 1-3',
    		url: 'url 1-3',
    		key: '1-3',
    		leaf: true
    	}]
    },{
    	text: 'Examples',
    	url: 'url 2',
    	key: '2',
    	items: [{
    		text: 'Video',
    		url: 'url 2-1',
    		key: '2-1',
    		card: FccTVApp.frames.Video,
    		leaf: true
    	},{
    		text: 'Form',
    		url: 'url 2-2',
    		key: '2-2',
    		card: FccTVApp.frames.Forms,
    		leaf: true
    	}],
    },{
    	text: 'Text 3',
    	url: 'url 3',
    	key: '3',
    	items: [{
    		text: 'Text 3-1',
    		url: 'url 3-1',
    		key: '3-1',
    		leaf: true
    	}],
    }];


Ext.regModel('NavigationModel', {
    fields: [
        {name: 'text',  type: 'string'},
        {name: 'url',  type: 'string'},
        {name: 'key',  type: 'string'},
        {name: 'card'}
    ]
});

FccTVApp.stores.NavigationStore = new Ext.data.TreeStore({
    model: 'NavigationModel',
    root: {
        items: FccTVApp.stores.Structure
    },
    proxy: {
        type: 'ajax',
        reader: {
            type: 'tree',
            root: 'items'
        }
    }
});