/*
 红包
 */
Ext.define('MyApp.model.DiscountCouponModel', {
    extend: 'Ext.data.Model',
    fields: [{
        name: 'id',
        type: 'int'
    },{
        name: 'code'//号码
    },{
        name: 'price',//价值
        type: 'float'
    },{
        name: 'isUsed',//是否使用
        type: 'boolean'
    },{
        name: 'couponType',//红包类型（1:红包；2:店铺券）
        type: 'int'
    },{
        name: 'orderId'//订单号
    },{
        name: 'startTime',//开始时间
        dateReadFormat: 'Y-m-dTH:i:s',
        type: 'date'
    },{
        name: 'endTime',//结束时间
        dateReadFormat: 'Y-m-dTH:i:s',
        type: 'date'
    }]
});