/*
 * 退款订单
 */
Ext.define('MyApp.model.RefundOrderModel', {
    extend: 'Ext.data.Model',
    fields: [{
        name: 'id',
        type: 'int'
    },{
        name: 'orderId'//订单号
    },{
        name: 'payOrderId'//支付单号
    },{
            name: 'buyerAccount'//申请人账号
        }, {
            name: 'uid'//UID
        },{
            name: 'phone'//电话
        },{
            name: 'name'//名字
        },{
            name: 'qq'//QQ
        },{
        name: 'refundAmount',//退款金额
        type: 'float'
    },{
        name: 'reason'//退款原因
    },{
        name: 'status',//状态
        type: 'int'
    },{
        name: 'createTime',//创建时间
        dateReadFormat: 'Y-m-dTH:i:s',
        type: 'date'
    },{
        name: 'auditor'//审核者
    },{
        name: 'auditTime',//审核时间
        dateReadFormat: 'Y-m-dTH:i:s',
        type: 'date'
    },{
        name: 'finishTime',//退款时间
        dateReadFormat: 'Y-m-dTH:i:s',
        type: 'date'
    }]
});