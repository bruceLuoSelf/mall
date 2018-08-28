/*
 * 区服订单数据
 */
Ext.define('MyApp.store.gameTradingStore', {
    extend: 'Ext.data.Store',
    requires: [
        'MyApp.model.gameTradingModel'
    ],
    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            model: 'MyApp.model.gameTradingModel',
			proxy: {
				type: 'ajax',
				actionMethods: 'POST',
				timeout:300000,
				url: './funds/queryGameTradingList.action',
				reader: {
					type: 'json',
					root: 'gameTradingList',
					totalProperty : 'totalCount'
				}
			}
        }, cfg)]);
    }
});