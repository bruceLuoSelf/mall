/*
 * 出货子订单
 */
Ext.define('MyApp.model.ShSettlementModel', {
    extend: 'Ext.data.Model',
    fields: [{
        name: 'chOrderId',//出货单号
        type: 'int'
    },{
        name: 'sellerAccount'//出货方5173账号
    },{
        name: 'sellerUid'//出货方5173UID
    },{
        name: 'amount'//付款金额
    },{
        name: 'status'//状态
    },{
        name: 'createTime',//创建时间
        dateReadFormat: 'Y-m-dTH:i:s',
        type: 'date'
    },{
        name: 'payTime',//付款时间
        dateReadFormat: 'Y-m-dTH:i:s',
        type: 'date'
    }]
});