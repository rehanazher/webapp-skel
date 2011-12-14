FccTVApp = new Ext.Application({
	name : 'FccTV',
	tabletStartupScreen : 'tablet_startup.jpg',
	phoneStartupScreen : 'phone_startup.jpg',
	tabletIcon : 'icon-ipad.png',
	phoneIcon : 'icon-iphone.png',
	glossOnIcon : false,
	launch : function() {
		document.getElementById("loading_mask").style.display = "none";
		
		this.viewcache.MainView = new FccTVApp.views.MainView({title : 'FCC TV'});
		this.viewcache.LoginView = new this.views.LoginView({title: 'FCC TV'});
		this.viewcache.TvView = new this.views.TvView({title: 'FCC TV'});

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
