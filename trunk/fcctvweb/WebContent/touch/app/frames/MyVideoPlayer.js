FccTVApp.frames.MyVideoPlayer = Ext.extend(Ext.Panel, {
	/*
	 * QueryListModel
	 */
	record : null,
	scroll : 'vertical',
	layout : 'auto',
	items : [],
	initComponent: function() {
		
		this.video = new Ext.Video({
			url : this.record.get("videoUrl"),
			loop : false,
			width : 330,
			height : 250,
			posterUrl : this.record.get('posterUrl')
		});
		
		this.fieldset = new Ext.form.FieldSet({
			title: this.record.get('name'),
			instructions: '',
			items:[{
				html: '<div>' +
					this.record.get('fileName') +
					'<p>' + 
    				bundle.getText('video.item.text.create.at') + ": " + this.record.get('fullCreationTime') +
    				'</p>' +
					'<p>' + 
    				bundle.getText('video.item.text.length') + ": " + this.record.get('length') +
    				'</p>' +
				    '</div>'
			}]
		});
		
		var id = this.record.get('id');
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
						'id': id
					},
					url: './addMyFileFavorite.action',
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
						'id': id
					},
					url: './removeMyFileFavorite.action',
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