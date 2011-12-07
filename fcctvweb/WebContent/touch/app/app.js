FccTVApp = new Ext.Application({
	name : 'FccTV',
	tabletStartupScreen : 'tablet_startup.jpg',
	phoneStartupScreen : 'phone_startup.jpg',
	tabletIcon : 'icon-ipad.png',
	phoneIcon : 'icon-iphone.png',
	glossOnIcon : false,
	launch : function() {
		document.getElementById("loading_mask").style.display = "none";
		this.views.viewport = new this.views.LoginView({title: 'FCC TV Web'});
		// this.views.viewport = new FccTVApp.views.PhoneViewport({
			// title : 'FCC TV Web'
		// });
		// this.views.viewport.show();
	}
});

Ext.ns('FccTVApp.frames', 'FccTVApp.utils');
