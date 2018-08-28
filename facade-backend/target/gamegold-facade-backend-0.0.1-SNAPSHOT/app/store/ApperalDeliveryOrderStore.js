/*
 * 收货申诉订单
 */
Ext.define('MyApp.store.ApperalDeliveryOrderStore', {
    extend: 'Ext.data.Store',
    requires: [
        'MyApp.model.DeliveryOrderModel'
    ],
    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            model: 'MyApp.model.DeliveryOrderModel',
            proxy: {
                type: 'ajax',
                actionMethods: 'POST',
                url: './appealDelivery/queryAppealDeliveryOrder.action',
                reader: {
                    type: 'json',
                    root: 'deliveryOrderList',
                    totalProperty : 'totalCount'
                }
            }
        }, cfg)]);
    }
});