/*
 * 订单数据
 */
Ext.define('MyApp.store.BuyInsuranceOrderStore', {
    extend: 'Ext.data.Store',
    requires: [
        'MyApp.model.OrderModel'
    ],
    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            model: 'MyApp.model.OrderModel',
			proxy: {
				type: 'ajax',
				actionMethods: 'POST',
				url: './order/queryBuyInsuranceOrder.action',
				reader: {
					type: 'json',
					root: 'orderInfoList',
					totalProperty : 'totalCount'
				}
			}
        }, cfg)]);
    }
});