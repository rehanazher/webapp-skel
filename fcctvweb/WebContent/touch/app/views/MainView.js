FccTVApp.views.MainView = Ext.extend(Ext.Panel, {
	fullscreen: true,
	hidden: true,
	layout: 'fit',
	html: '<ol id="grid">'+
		'<li><a id="tv" href="javascript:void(0)" onclick="javascript: FccTVApp.views.viewport = FccTVApp.viewcache.TvView; FccTVApp.views.viewport.show();">' +
	    '<img src="./images/tv.png" alt="Video" height="72" width="72">' +
	    '<span>' + bundle.getText('main.desc.tv') +'</span>' +
	    '</a></li>' +
        '<li><a id="video" href="javascript:void(0)">' +
        '<img src="./images/video.png" alt="Video" height="72" width="72">' +
        '<span>' + bundle.getText('main.desc.video') +'</span>' +
	    '</a></li>' +
	    '<li><a id="file" href="javascript:void(0)"> ' +
	    '    <img src="./images/file.png" alt="File" height="72" width="72">' +
	    '    <span>' + bundle.getText('main.desc.doc') +'</span>' +
	    '</a></li>' +
	    '<li><a id="music" href="javascript:void(0)">' +
	    '    <img src="./images/music.png" alt="Music" height="72" width="72">' +
	    '    <span>' + bundle.getText('main.desc.music') +'</span>' +
	    '</a></li>' +
	    '<li><a id="photo" href="javascript:void(0)">' +
	    '    <img src="images/photo.png" alt="Photo" height="72" width="72">' +
	    '    <span>' + bundle.getText('main.desc.photo') +'</span>' +
	    '</a></li>' +
	    (Ext.is.Desktop ? 
	    ('<li><a id="upload" href="../uploader.action">' +
	    '    <img src="images/upload.png" alt="Photo" height="72" width="72">' +
	    '    <span>' + bundle.getText('main.desc.upload') +'</span>' +
	    '</a></li>') 
	    : '') +
	    '</ol>',
	initComponent : function() {
		FccTVApp.views.MainView.superclass.initComponent.call(this, arguments);
	}
});