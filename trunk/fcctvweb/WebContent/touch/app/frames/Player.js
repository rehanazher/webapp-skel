FccTVApp.frames.Player = Ext.extend(Ext.Panel, {
	/*
	 * QueryListModel
	 */
	record : null,
	phoneVideoUrl: null,
	scroll : 'vertical',
	layout : 'auto',
	items : [],
	initComponent: function() {
		
		this.video = new Ext.Video({
			url : this.phoneVideoUrl ? this.phoneVideoUrl : this.record.get("videoUrl"),
			loop : false,
			width : 330,
			height : 250,
			posterUrl : this.record.get('posterUrl')
		});
		
		this.fieldset = new Ext.form.FieldSet({
			title: this.record.get('contentname'),
			instructions: 'GTAG:' + this.record.get('gtvid'),
			items:[{
				html: '<div>' +
					this.record.get('contentdesc') +
					'<p>' + this.record.get('bstartTime') + ' ' + 
					this.record.get('playTime') + '(' + this.record.get('duration') + ')</p>' +
					'<p>' + this.record.get('chName') + '</p>' +
				    '</div>'
			}]
		});
		
		var gtvid = this.record.get('gtvid');
		var addBtn = this.addFavoriteBtn;
		var removeBtn = this.removeFavoriteBtn;
		
		this.addFavoriteBtn = new Ext.Button({
			id : 'addFavoriteBtn',
			hidden: this.record.get('favorite') == 1,
			text: bundle.getText('app.player.favorite.add'),
			handler: function(){
				FccTVApp.loadMask.show();
				
				Ext.Ajax.request({
					params: {
						'gtvid': gtvid
					},
					url: './addFavorite.action',
					success: function(response, opts) {
					  var obj = Ext.decode(response.responseText);
					  console.dir(obj);
					  
					  Ext.getCmp('addFavoriteBtn').hide();
					  Ext.getCmp('removeFavoriteBtn').show();
					  
					  FccTVApp.loadMask.hide();
					},
					failure: function(response, opts) {
					  console.log('server-side failure with status code ' + response.status);
					  FccTVApp.loadMask.hide();
					}
				});
			}
		});
		
		this.removeFavoriteBtn = new Ext.Button({
			id : 'removeFavoriteBtn',
			hidden: this.record.get('favorite') == 0,
			text: bundle.getText('app.player.favorite.remove'),
			handler: function(){
				FccTVApp.loadMask.show();
				
				Ext.Ajax.request({
					params: {
						'gtvid': gtvid
					},
					url: './removeFavorite.action',
					success: function(response, opts) {
					  var obj = Ext.decode(response.responseText);
					  console.dir(obj);
					  Ext.getCmp('removeFavoriteBtn').hide();
					  Ext.getCmp('addFavoriteBtn').show();
					  
					  FccTVApp.loadMask.hide();
					},
					failure: function(response, opts) {
					  console.log('server-side failure with status code ' + response.status);
					  
					  FccTVApp.loadMask.hide();
					}
				});
			}
		});
		
		this.items = [];
		this.items.unshift(this.video, this.fieldset, this.addFavoriteBtn, this.removeFavoriteBtn);
		
		FccTVApp.frames.Player.superclass.initComponent.call(this);
	}
});