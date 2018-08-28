/*
 * 出库订单数据
 */
Ext.define('MyApp.store.SentMessageStore', {
    extend: 'Ext.data.Store',
    requires: [
        'MyApp.model.SentMessageModel'
    ],
    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            model: 'MyApp.model.SentMessageModel',
			proxy: {
				type: 'ajax',
				actionMethods: 'POST',
				url: './sentMessage/list.action',
				reader: {
					type: 'json',
					root: 'list',
					totalProperty : 'totalCount'
				}
			}
        }, cfg)]);
    }
});