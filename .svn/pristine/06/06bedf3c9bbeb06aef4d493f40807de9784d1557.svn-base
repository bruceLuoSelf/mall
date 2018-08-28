/*
 * 出货订单
 */
Ext.define('MyApp.store.DeliveryOrderStore', {
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
                url: './delivery/queryDeliveryOrder.action',
                reader: {
                    type: 'json',
                    root: 'deliveryOrderList',
                    totalProperty : 'totalCount'
                }
            }
        }, cfg)]);
    }
});