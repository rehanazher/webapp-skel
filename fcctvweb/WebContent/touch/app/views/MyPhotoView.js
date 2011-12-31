FccTVApp.views.MyPhotoView = Ext.extend(Ext.Panel, {
	showAnimation : 'fade',
	id : 'photoview',
	navigatorPref : 'photo/',
	fullscreen : true,
	hidden: true,
	layout: 'fit',
	items : [],
	dockedItems : [{
		xtype : 'toolbar',
		title : bundle.getText('photo.title'),
		items : [{
			xtype : 'button',
			text : bundle.getText("main.title"),
			ui : 'back',
			handler : function() {
				
				if (FccTVApp.player){
					FccTVApp.player.destroy();
				}
				
				FccTVApp.addHistory("main");
				FccTVApp.views.viewport.hide();
				FccTVApp.views.viewport = FccTVApp.viewcache.MainView;
				FccTVApp.views.viewport.show();
			}
		},{
			xtype : 'spacer'
		}]
	}],
	initComponent : function() {
		FccTVApp.views.MyPhotoView.superclass.initComponent.call(this, arguments);
	},
	addCarousel: function(){
		FccTVApp.loadMask.show();
		Ext.Ajax.request({
			url: './photoAmount.action',
			success: function(response, opts) {
			  var obj = Ext.decode(response.responseText);
			  var amount = parseInt(obj.value);
			  var page = parseInt(amount / 16);
			  if (amount % 16 != 0 || amount == 0){
				  page++;
			  }
			  
			  var items = [];
			  for (var i = 0; i < page; i++){
				  if (amount == 0){
					  items[i] = {html: bundle.getText('photo.empty')};
				  }else{
					  items[i] = {html: '' + i};
				  }
				  
			  }
			  
			  var carousel = new Ext.Carousel({
				  	id : 'photoCarousel',
			        items: items,
			        listeners:{
			        	beforecardswitch : function (carousel, newCard, oldCard, index, animated ){
			        		if (!newCard.paint){
			        			var html = '';
			        			for (var i = 1; i <= 16; i++){
			        				html += '<img style="float:left height: 72px; width: 72px; margin-left: 5px;" src="./watch.action?type=photo&index=' + (index * 12 + i) + '" onclick="javascript: console.log(1)"/>';
			        			}
				        		newCard.update(html);
				        		newCard.paint = true;
			        		}
			        	}
			        }
			    });
				
			  this.add(carousel);
			  if (amount != 0){
				  this.paintCard(0);
			  }
			  this.doLayout();
			  FccTVApp.loadMask.hide();
			},
			failure: function(response, opts) {
			  FccTVApp.loadMask.hide();
			},
			scope: this
		});
	},
	paintCard: function(index){
		var carousel = Ext.getCmp('photoCarousel');
		var card = carousel.getComponent(index);
		if (!card.paint){
			var html = '';
			for (var i = 0; i < 16; i++){
				var src = './watch.action?type=photo&index=' + (index * 12 + i);
				var width = (Ext.is.Phone ? 280 : 440) - 44;
				var height = (Ext.is.Phone ? 240 : 440) - 44;
				html += '<img style="float:left height: 72px; width: 72px; margin-left: 5px;" src="' + src + '" onclick="javascript: var overlay = Ext.getCmp(\'photoOverlay\'); overlay.update(\'<img src=' + src + ' width='+ width +' height=' + height + ' />\'); overlay.show()"/>';
			}
			card.update(html);
			card.paint = true;
		}
	},
	overlay : new Ext.Panel({
		id : 'photoOverlay',
        floating: true,
        modal: true,
        centered: true,
        width: Ext.is.Phone ? 280 : 440,
        height: Ext.is.Phone ? 240 : 440,
        styleHtmlContent: true,
        html: ''
    })

});
