FccTVApp = new Ext.Application({
	name : 'FccTV',
	tabletStartupScreen : 'tablet_startup.jpg',
	phoneStartupScreen : 'phone_startup.jpg',
	tabletIcon : 'icon-ipad.png',
	phoneIcon : 'icon-iphone.png',
	glossOnIcon : false,
	launch : function() {
		document.getElementById("loading_mask").style.display = "none";
		
		if (loginFlag){
			this.views.viewport = new FccTVApp.views.MainView({title : 'FCC TV'});
//			 this.views.viewport = new FccTVApp.views.PhoneViewport({
//				 title : 'FCC TV'
//			 });
			 this.views.viewport.show();
		}else{
			this.views.viewport = new this.views.LoginView({title: 'FCC TV'});
		}
	}
});

FccTVApp.loadMask = new Ext.LoadMask(Ext.getBody(), {
	msg : bundle.getText('common.mask.loading')
});

Ext.ns('FccTVApp.frames', 'FccTVApp.utils');
