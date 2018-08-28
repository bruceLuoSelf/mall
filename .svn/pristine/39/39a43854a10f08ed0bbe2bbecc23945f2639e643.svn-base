/*
 * 出货订单日志数据
 */
Ext.define('MyApp.store.DeliveryOrderLogStore', {
    extend: 'Ext.data.Store',
    requires: [
        'MyApp.model.DeliveryOrderLogModel'
    ],
    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            model: 'MyApp.model.DeliveryOrderLogModel',
            proxy: {
                type: 'ajax',
                actionMethods: 'POST',
                url: './deliveryOrderLog/queryDeliveryOrderLog.action',
                reader: {
                    type: 'json',
                    root: 'orderLogList',
                    totalProperty : 'totalCount'
                }
            }
        }, cfg)]);
    }
});