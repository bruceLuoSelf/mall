/*
 * 出货子订单
 */
Ext.define('MyApp.store.DeliverySubOrderStore', {
    extend: 'Ext.data.Store',
    requires: [
        'MyApp.model.DeliverySubOrderModel'
    ],
    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            model: 'MyApp.model.DeliverySubOrderModel',
            proxy: {
                type: 'ajax',
                actionMethods: 'POST',
                url: './delivery/queryDeliverySubOrder.action',
                reader: {
                    type: 'json',
                    root: 'deliverySubOrderList',
                    totalProperty : 'totalCount'
                }
            }
        }, cfg)]);
    }
});