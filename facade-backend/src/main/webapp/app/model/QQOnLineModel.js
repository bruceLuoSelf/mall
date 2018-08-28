/*
 *  付款详细
 */
Ext.define('MyApp.model.QQOnLineModel',{
    extend:'Ext.data.Model',
    fields:[{
        name:'id',
        type:'int'
    },{
        name:'qqNumber'
    },{
        name:'online',
    },{
        name:'nickname'
    },{
        name:'createTime',
        type:'date',
        dateReadFormat:'Y-m-dTH:i:s'
    },{
        name:'lastUpdateTime',
        type:'date',
        dateReadFormat:'Y-m-dTH:i:s'
    }]
});
