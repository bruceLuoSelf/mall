/*
 * 收货模式
 */
Ext.define('MyApp.model.transFileModel', {
    extend: 'Ext.data.Model',
    idProperty: 'extId',
    idgen: 'uuid',
    fields: [{
        name: 'id',
        type: 'int'
    },{
        name: 'gameName'
    },{
        name: 'jsonString',
    },{
        name: 'updateTime',
        dateReadFormat: 'Y-m-dTH:i:s',
        type: 'date'
    }]
});