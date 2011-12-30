Ext.regModel('MyDocModel', {
    fields : [ {
        name : 'key',
        type : 'int'
    },{
        name : 'name',
        type : 'string'
    },{
        name : 'fileName',
        type : 'string'
    },{
        name : 'folderName',
        type : 'string'
    },{
        name : 'extName',
        type : 'string'
    },{
        name : 'type',
        type : 'string'
    },{
        name : 'position',
        type : 'string'
    },{
        name : 'parentId',
        type : 'int'
    },{
        name : 'root',
        type : 'boolean'
    },{
        name : 'leaf',
        type : 'boolean'
    }]
});
