/*
 * 出货订单日志
 */
Ext.define('MyApp.model.DeliveryOrderLogModel', {
    extend: 'Ext.data.Model',
    fields: [{
        name: 'id',
        type: 'int'
    },{
        name: 'orderId'//关联的出货订单号
    },{
        name: 'log'//日志内容

    },{
        name: 'createTime',//创建时间
        dateReadFormat: 'Y-m-dTH:i:s',
        type: 'date'
    }]
});