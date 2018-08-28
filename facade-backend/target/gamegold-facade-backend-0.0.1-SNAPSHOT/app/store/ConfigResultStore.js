/*
 * 订单数据
 */
Ext.define('MyApp.store.ConfigResultStore', {
    extend: 'Ext.data.Store',
    requires: [
        'MyApp.model.ConfigResultModel'
    ],
    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            model: 'MyApp.model.ConfigResultModel',
            groupField: 'orderId',
			proxy: {
				type: 'ajax',
				actionMethods: 'POST',
				url: './order/queryOrderConfig.action',
				reader: {
					type: 'json',
					root: 'configInfoList',
					totalProperty : 'totalCount'
				}
			}
        }, cfg)]);
    }
});