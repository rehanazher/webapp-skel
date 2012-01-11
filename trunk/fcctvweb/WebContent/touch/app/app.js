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
		this.viewcache.MyMusicView = new this.views.MyMusicView();
		this.viewcache.MyPhotoView = new this.views.MyPhotoView();

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

FccTVApp.dispatch = function(token, reverse){
	FccTVApp.loadMask.hide();
	var parts = token.split("/");
	Ext.Ajax.request({
		url: './detect.action',
		scope: FccTVApp,
		success: function(response, opts) {
			var obj = Ext.decode(response.responseText);
			if (obj.msg == "login"){
				Ext.History.add("login");
				
				if (FccTVApp.views.viewport){
					if (FccTVApp.views.viewport != this.viewcache.LoginView){
						FccTVApp.views.viewport.hide();
					}
				}
				FccTVApp.views.viewport = this.viewcache.LoginView;
				FccTVApp.views.viewport.show();
			}else{
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
								
								if (!item){
									FccTVApp.dispatch("main");
									return;
								}
								
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
									FccTVApp.frames.QueryList.bindStore(FccTVApp.stores.DailyStore);
									FccTVApp.prevCard = FccTVApp.frames.DailyList;
									
									tvview.setActiveItem(0);
									tvview.getActiveItem().setActiveItem(FccTVApp.frames.QueryList);
								});
							}
						}else if (parts[1] == "channel"){
							var backBtn = Ext.getCmp("backButton");
							var ch = parts[2];
							
							var chItem = FccTVApp.stores.ChannelStore.queryBy(function(record, id){
								return record.get('ch') == ch;
							});
							if (chItem.length == 1){
								var chNode = chItem.items[0];
								backBtn.setText(chNode.get('chName'));
								
								FccTVApp.stores.DailyStore.setProxy({
									type: 'ajax',
									url: './queryVideo.action',
									extraParams: {
										ch: ch
									}
								});
								FccTVApp.loadMask.show();
								FccTVApp.stores.DailyStore.load(function(){
									FccTVApp.loadMask.hide();
									FccTVApp.frames.QueryList.bindStore(FccTVApp.stores.DailyStore);
									FccTVApp.prevCard = FccTVApp.frames.ChannelList;
									
									tvview.setActiveItem(0);
									tvview.getActiveItem().setActiveItem(FccTVApp.frames.QueryList);
								});
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
					if (FccTVApp.views.viewport != this.viewcache.MyVideoView){
						if (FccTVApp.views.viewport){
							FccTVApp.views.viewport.hide();
						}
						FccTVApp.views.viewport = this.viewcache.MyVideoView;
						FccTVApp.views.viewport.show();
					}
					
					var videoview = Ext.getCmp('videoview');
					
					if (parts[1]){
						if (parts[1] == "favorite"){
							videoview.setActiveItem(1);
						}else if (parts[1] == "player"){
							videoview.setActiveItem(2);
						}
					}else {
						videoview.setActiveItem(0);
					}
				}else if (parts[0] == "doc"){
					if (FccTVApp.views.viewport != this.viewcache.MyDocView){
						if (FccTVApp.views.viewport){
							FccTVApp.views.viewport.hide();
						}
						FccTVApp.views.viewport = this.viewcache.MyDocView;
						FccTVApp.views.viewport.show();
					}
					
					var docview = Ext.getCmp('docview');
					
					if (parts[1]){
						var rootNode = FccTVApp.stores.MyDocTreeStore.getRootNode();
						var node = rootNode.findChildBy(function(n) {
			                return n.attributes.record.raw.type == "folder" && parts[1] === n.attributes.record.raw.position;
			            }, this, true);
						
						var record = node.attributes.record;
						var itemId = 'MYDOC' + record.get('position');
						var	card = docview.add(this.viewcache.MyDocView.getListConfig(itemId, node));
						
						docview.setActiveItem(card, reverse? {type: 'slide', reverse: true}: 'slide');
						docview.dockedItems.items[0].setTitle(record.get('name'));
						var backBtn = Ext.getCmp('docBackButton');
						if (node.parentNode){
							if (node.parentNode.isRoot){
								backBtn.setText(bundle.getText('main.title'));
							}else{
								backBtn.setText(node.parentNode.attributes.record.get('name'));
							}
						}
					}

				}else if (parts[0] == "music"){
					if (FccTVApp.views.viewport != this.viewcache.MyMusicView){
						if (FccTVApp.views.viewport){
							FccTVApp.views.viewport.hide();
						}
						FccTVApp.views.viewport = this.viewcache.MyMusicView;
						FccTVApp.views.viewport.show();
					}
					
//					var musicview = Ext.getCmp('musicview');
//					if (parts[1] && parts[1] == 'favorite'){
//						musicview.setActiveItem(1);
//					}else{
//						musicview.setActiveItem(0);
//					}
				}else if (parts[0] == "photo"){
					if (FccTVApp.views.viewport != this.viewcache.MyPhotoView){
						if (FccTVApp.views.viewport){
							FccTVApp.views.viewport.hide();
						}
						
						FccTVApp.views.viewport = this.viewcache.MyPhotoView;
						if (!Ext.get('photoCarousel')){
							FccTVApp.views.viewport.addCarousel();
						}
						FccTVApp.views.viewport.show();
					}
				}
			}
		},
		failure: function(){
			
		}
	});
	
	
	
		
};

FccTVApp.addHistory = function(newToken){
	oldToken = Ext.History.getToken();
    if (newToken && oldToken != newToken) {
        Ext.History.add(newToken);
    }
};

Ext.ns('FccTVApp.frames', 'FccTVApp.utils', 'FccTVApp.viewcache');
