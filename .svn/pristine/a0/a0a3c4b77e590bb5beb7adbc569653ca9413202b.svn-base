/*
 * 出库订单数据
 */
Ext.define('MyApp.store.ShippingStore', {
    extend: 'Ext.data.Store',
    requires: [
        'MyApp.model.ShippingModel'
    ],
    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            model: 'MyApp.model.ShippingModel',
			proxy: {
				type: 'ajax',
				actionMethods: 'POST',
				url: './order/queryShipping.action',
				reader: {
					type: 'json',
					root: 'shippingInfoList',
					totalProperty : 'totalCount'
				}
			}
        }, cfg)]);
    }
});