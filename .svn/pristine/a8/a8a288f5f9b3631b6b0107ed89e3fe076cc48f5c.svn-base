/*
 * 防骗短信规则数据
 */
Ext.define('MyApp.store.TextMessageLogStore', {
    extend: 'Ext.data.Store',
    requires: [
        'MyApp.model.TextMessageLogModel'
    ],
    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            model: 'MyApp.model.TextMessageLogModel',
			proxy: {
				type: 'ajax',
				actionMethods: 'POST',
				url: './messageRule/loadRuleLogs.action',
				reader: {
					type: 'json',
					root: 'ruleLogs',
					totalProperty : 'totalCount'
				}
			}
        }, cfg)]);
    }
});