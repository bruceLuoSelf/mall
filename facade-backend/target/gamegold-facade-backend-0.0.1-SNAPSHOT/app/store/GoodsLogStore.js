/*
 * 商品日志数据
 */
Ext.define('MyApp.store.GoodsLogStore', {
    extend: 'Ext.data.Store',
    requires: [
        'MyApp.model.GoodsLogModel'
    ],
    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            model: 'MyApp.model.GoodsLogModel',
            proxy: {
				type: 'ajax',
				actionMethods: 'POST',
				url: './log/queryGoodsLogs.action',
				reader: {
					type: 'json',
					root: 'logs',
					totalProperty : 'totalCount'
				}
			}
        }, cfg)]);
    }
});