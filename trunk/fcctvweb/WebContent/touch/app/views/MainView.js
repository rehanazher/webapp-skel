FccTVApp.views.MainView = Ext.extend(Ext.Panel, {
	fullscreen: true,
	layout: 'fit',
	html: '<ol id="grid">'+
        '<li><a id="video" href="javascript:void(0)" onclick="javascript: FccTVApp.views.viewport = new FccTVApp.views.PhoneViewport(); FccTVApp.views.viewport.show();">' +
        '<img src="./images/video.png" alt="Video" height="72" width="72">' +
        '<span>Video</span>' +
    '</a></li>' +
    '<li><a id="file" href="javascript:void(0)"> ' +
    '    <img src="./images/file.png" alt="File" height="72" width="72">' +
    '    <span>File</span>' +
    '</a></li>' +
    '<li><a id="music" href="javascript:void(0)">' +
    '    <img src="./images/music.png" alt="Music" height="72" width="72">' +
    '    <span>Music</span>' +
    '</a></li>' +
    '<li><a id="photo" href="javascript:void(0)">' +
    '    <img src="images/photo.png" alt="Photo" height="72" width="72">' +
    '    <span>Photo</span>' +
    '</a></li>' +
    '</ol>',
//	items:[{
//		xtype: 'panel',
//		layout: 'hbox',
//		items:[{
//			html: '<a href="javascript:void(0)" onclick="javascript: FccTVApp.views.viewport = new FccTVApp.views.PhoneViewport(); FccTVApp.views.viewport.show();">icon1</a>'
//		},{
//			html: 'icon2'
//		}]
//	},{
//		xtype: 'panel',
//		layout: 'hbox',
//		items:[{
//			html: 'icon3'
//		},{
//			html: 'icon4'
//		}]
//	}],
	initComponent : function() {
		FccTVApp.views.MainView.superclass.initComponent.call(this, arguments);
	}
});