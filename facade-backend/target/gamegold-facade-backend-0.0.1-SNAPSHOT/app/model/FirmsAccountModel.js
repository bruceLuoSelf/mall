/*
 * 收货模式
 */
Ext.define('MyApp.model.FirmsAccountModel', {
    extend: 'Ext.data.Model',
    idProperty: 'extId',
    idgen: 'uuid',
    fields: [{
        name: 'id',
        type: 'int'
    },{
        name: 'firmsName'
    },{
        name: 'firmsSecret'
    },{
        name: 'enabled',
        type: 'boolean'
    },{
        name: 'createTime',
        dateReadFormat: 'Y-m-dTH:i:s',
        type: 'date'
    },{
        name: 'updateTime',
        dateReadFormat: 'Y-m-dTH:i:s',
        type: 'date'
    },{
        name: 'uid'
    },{
        name: 'loginAccount'
    }]
});