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
	        if (token) {
	        	FccTVApp.dispatch(token);
	        }
	    });
		
		this.viewcache.MainView = new FccTVApp.views.MainView();
		this.viewcache.LoginView = new this.views.LoginView();
		this.viewcache.TvView = new this.views.TvView({id: 'tvview'});
		this.viewcache.MyVideoView = new this.views.MyVideoView();
		this.viewcache.MyDocView = new this.views.MyDocView();

		var token = window.location.href.substr(window.location.href.indexOf("#") + 1);
		var newToken = "";
		if (window.location.href.indexOf("#") != -1 && token.length != 0){
			FccTVApp.dispatch(token);
			newToken = token;
		}else{
			if (loginFlag){
				this.views.viewport = this.viewcache.MainView;
				newToken = "main";
				this.views.viewport.show();
			}else{
				this.views.viewport = this.viewcache.LoginView;
				newToken = "login";
				this.views.viewport.show();
			}
		}
		FccTVApp.addHistory(newToken);
	}
});

FccTVApp.loadMask = new Ext.LoadMask(Ext.getBody(), {
	msg : bundle.getText('common.mask.loading')
});

FccTVApp.dispatch = function(token){
	var parts = token.split("/");
	var length = parts.length;
	if (parts[0] == "main"){
		if (FccTVApp.views.viewport != this.viewcache.MainView){
			if (FccTVApp.views.viewport){
				FccTVApp.views.viewport.hide();
			}
			FccTVApp.views.viewport = this.viewcache.MainView;
			FccTVApp.views.viewport.show();
		}
	}else if (parts[0] == "login"){
		if (FccTVApp.views.viewport){
			FccTVApp.views.viewport.hide();
		}
		FccTVApp.views.viewport = this.viewcache.LoginView;
		FccTVApp.views.viewport.show();
	}else if (parts[0] == "doc"){
		if (FccTVApp.views.viewport){
			FccTVApp.views.viewport.hide();
		}
		FccTVApp.views.viewport = this.viewcache.MyDocView;
		FccTVApp.views.viewport.show();
	}else if (parts[0] == "tv"){
		if (FccTVApp.views.viewport != this.viewcache.TvView){
			if (FccTVApp.views.viewport){
				FccTVApp.views.viewport.hide();
			}
			FccTVApp.views.viewport = this.viewcache.TvView;
			FccTVApp.views.viewport.show();
		}
		
		var tvview = Ext.getCmp('tvview');
		
		if (parts[1]){
			if (parts[1] == "nav"){
				var backBtn = Ext.getCmp("backButton");
				tvview.setActiveItem(0);
				var navigator = Ext.getCmp("navigatorPanel");
				if (parts[2]){
					item = navigator.store.getRootNode().findChildBy(function(n) {
		                return parts[2] === n.attributes.record.raw.nav;
		            }, this, true);
					
					var parentNode = item.parentNode;
					if (item.leaf){
						var record = item.attributes.record;
						if (record.get('url') == 'type-query'){
							FccTVApp.stores.TypeStore.setProxy({
								type: 'ajax',
								url: './queryVideo.action',
								extraParams: {
									type: record.get('key')
								}
							});
							FccTVApp.loadMask.show();
							FccTVApp.stores.TypeStore.load(function(){
								FccTVApp.loadMask.hide();
							});
							FccTVApp.frames.QueryList.bindStore(FccTVApp.stores.TypeStore);
							
						}else if (record.get('url') == 'daily-query'){
							// navigator.setActiveItem(record.get('card'), 'slide');
						}
						tvview.getActiveItem().setActiveItem(record.get('card'), 'slide');
						// navigator.setActiveItem(record.get('card'), 'slide');
						
						var prevList = navigator.getSubList(parentNode);
						var prevCard = navigator.add(prevList);
						navigator.setActiveItem(prevCard);
						FccTVApp.prevCard = navigator;
						if (parentNode.isRoot){
							FccTVApp.prevTitle = bundle.getText('main.title');
						}else{
							FccTVApp.prevTitle = parentNode.attributes.record.get('text');
						}
						backBtn.setText(record.get("text"));
					} else{
						var nextList = navigator.getSubList(item);
						nextList = navigator.add(nextList);
						navigator.setActiveItem(nextList);
						if (tvview.getActiveItem().getActiveItem() != navigator){
							tvview.getActiveItem().setActiveItem(navigator);
						}
						var backText = bundle.getText("main.title");
						backText = item.attributes.record.data.text;
						backBtn.setText(backText);
					}
					backBtn.show();
				}else{
					var rootNode = navigator.store.getRootNode();
					var nextList = navigator.getSubList(rootNode);
					nextList = navigator.add(nextList);
					navigator.setActiveItem(nextList);
					if (tvview.getActiveItem().getActiveItem() != navigator){
						tvview.getActiveItem().setActiveItem(navigator);
					}
					var backText = bundle.getText("main.title");
					backBtn.setText(backText);
				}
			}else if (parts[1] == 'daily'){
				var backBtn = Ext.getCmp("backButton");
				var date = parts[2];
				
				var dailyItem = FccTVApp.stores.DailyListStore.queryBy(function(record, id){
					return record.get('date') == date;
				});
				if (dailyItem.length == 1){
					var dailyNode = dailyItem.items[0];
					backBtn.setText(dailyNode.get('value'));
					
					FccTVApp.stores.DailyStore.setProxy({
						type: 'ajax',
						url: './queryVideo.action',
						extraParams: {
							date: date
						}
					});
					FccTVApp.loadMask.show();
					FccTVApp.stores.DailyStore.load(function(){
						FccTVApp.loadMask.hide();
					});
					FccTVApp.frames.QueryList.bindStore(FccTVApp.stores.DailyStore);
					FccTVApp.prevCard = FccTVApp.frames.DailyList;
					
					tvview.setActiveItem(0);
					tvview.getActiveItem().setActiveItem(FccTVApp.frames.QueryList);
				}
			}else if (parts[1] == "player"){
				tvview.setActiveItem(4);
			}else if (parts[1] == "today"){
				tvview.setActiveItem(1);
			}else if (parts[1] == "favorite"){
				console.log(parts[1]);
				tvview.setActiveItem(2);
			}else if (parts[1] == "setting"){
				tvview.setActiveItem(3);
			}
			
			
		}

	}else if (parts[0] == "video"){
		FccTVApp.views.viewport.hide();
		FccTVApp.views.viewport = this.viewcache.MyVideoView;
		FccTVApp.views.viewport.show();
	}else if (parts[0] == "music"){
		
	}else if (parts[0] == "photo"){
		
	}
	
		
};

FccTVApp.addHistory = function(newToken){
	oldToken = Ext.History.getToken();
    if (oldToken === null || oldToken != newToken) {
        Ext.History.add(newToken);
    }
};

Ext.ns('FccTVApp.frames', 'FccTVApp.utils', 'FccTVApp.viewcache');
