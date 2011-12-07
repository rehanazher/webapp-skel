FccTVApp.frames.Video = new Ext.Panel({
	title : 'Video test',
	scroll : 'vertical',
	layout : 'vbox',
	items : [{
		xtype : 'video',
		url : 'space.mp4',
		loop : true,
		width : 300,
		height : 250,
		posterUrl : 'Screenshot.png'
	}, {
		xtype: 'fieldset',
		title: 'Video Title',
		layout: 'vbox',
		items:[{
			html: '<div style="width: 300px; height: 300px;">Content...</div>'
		}]
	}]
});
