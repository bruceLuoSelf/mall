/*
 * 退款订单
 */
Ext.define('MyApp.store.RefundOrderStore', {
    extend: 'Ext.data.Store',
    requires: [
        'MyApp.model.RefundOrderModel'
    ],
    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            model: 'MyApp.model.RefundOrderModel',
            proxy: {
                type: 'ajax',
                actionMethods: 'POST',
                url: './delivery/queryRefundOrder.action',
                reader: {
                    type: 'json',
                    root: 'refundOrderList',
                    totalProperty : 'totalCount'
                }
            }
        }, cfg)]);
    }
});