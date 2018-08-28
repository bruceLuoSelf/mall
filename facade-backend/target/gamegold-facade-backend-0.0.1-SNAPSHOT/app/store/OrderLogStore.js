/*
 * 订单日志数据
 */
Ext.define('MyApp.store.OrderLogStore', {
    extend: 'Ext.data.Store',
    requires: [
        'MyApp.model.OrderLogModel'
    ],
    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            model: 'MyApp.model.OrderLogModel',
            proxy: {
				type: 'ajax',
				actionMethods: 'POST',
				url: './log/queryOrderLogs.action',
				reader: {
					type: 'json',
					root: 'logs',
					totalProperty : 'totalCount'
				}
			}
        }, cfg)]);
    }
});