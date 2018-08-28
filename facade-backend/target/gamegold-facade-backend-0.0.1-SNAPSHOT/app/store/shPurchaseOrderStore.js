
/**
 * 采购商单
 */
Ext.define('MyApp.store.shPurchaseOrderStore', {
    extend: 'Ext.data.Store',
    requires: [
        'MyApp.model.shPurchaseOrderModel'
    ],
    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            model: 'MyApp.model.shPurchaseOrderModel',
            proxy: {
                type: 'ajax',
                actionMethods: 'POST',
                url: './shpurchase/shPurchaseOrder.action',
                reader: {
                    type: 'json',
                    root: 'purchaserOrderList',
                    totalProperty : 'totalCount'
                }
            }
        }, cfg)]);
    }
});