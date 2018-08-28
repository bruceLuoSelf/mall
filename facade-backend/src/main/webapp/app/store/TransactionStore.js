/*
 * 交易流水数据
 */
Ext.define('MyApp.store.TransactionStore', {
    extend: 'Ext.data.Store',
    requires: [
        'MyApp.model.TransactionModel'
    ],
    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            model: 'MyApp.model.TransactionModel',
			proxy: {
				type: 'ajax',
				actionMethods: 'POST',
				url: './funds/queryTransaction.action',
				reader: {
					type: 'json',
					root: 'transactionInfoList',
					totalProperty : 'totalCount'
				}
			}
        }, cfg)]);
    }
});