/*
 * 防骗短信规则数据
 */
Ext.define('MyApp.store.TextMessageStore', {
    extend: 'Ext.data.Store',
    requires: [
        'MyApp.model.TextMessageModel'
    ],
    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            model: 'MyApp.model.TextMessageModel',
			proxy: {
				type: 'ajax',
				actionMethods: 'POST',
				url: './messageRule/list.action',
				reader: {
					type: 'json',
					root: 'messageRules',
					totalProperty : 'totalCount'
				}
			}
        }, cfg)]);
    }
});