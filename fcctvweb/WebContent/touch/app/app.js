FccTVApp = new Ext.Application({
	name : 'FccTV',
	tabletStartupScreen : 'tablet_startup.jpg',
	phoneStartupScreen : 'phone_startup.jpg',
	tabletIcon : 'icon-ipad.png',
	phoneIcon : 'icon-iphone.png',
	glossOnIcon : false,
	launch : function() {
		document.getElementById("loading_mask").style.display = "none";
		
		Ext.History.init();
	    
	    Ext.History.on('change', function(token) {
	    	console.log(token);
//	        if (token) {
//	            refreshPage(token);
//	            tree = Ext.getCmp('navigatorTree');
//	            item = tree.getRootNode().findChildBy(function(n) {
//	                return token === n.raw.key;
//	            }, this, true);
//	            tree.getSelectionModel().select(item);
//	        }
	    });
	    
	    directPage = function(key){
	    	refreshPage(key);
	        
	    	// history
	        newToken = key;
	        oldToken = Ext.History.getToken();
	        if (oldToken === null || oldToken.search(newToken) === -1) {
	            Ext.History.add(newToken);
	        }
	    };
		
		this.viewcache.MainView = new FccTVApp.views.MainView();
		this.viewcache.LoginView = new this.views.LoginView();
		this.viewcache.TvView = new this.views.TvView();
		this.viewcache.MyVideoView = new this.views.MyVideoView();
		this.viewcache.MyDocView = new this.views.MyDocView();

		if (loginFlag){
			this.views.viewport = this.viewcache.MainView;
			this.views.viewport.show();
		}else{
			this.views.viewport = this.viewcache.LoginView;
			this.views.viewport.show();
		}
	}
});

FccTVApp.loadMask = new Ext.LoadMask(Ext.getBody(), {
	msg : bundle.getText('common.mask.loading')
});

Ext.ns('FccTVApp.frames', 'FccTVApp.utils', 'FccTVApp.viewcache');
