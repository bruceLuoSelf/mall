/*
 * 日志数据
 */
Ext.define('MyApp.store.LogStore', {
    extend: 'Ext.data.Store',
    requires: [
        'MyApp.model.LogModel'
    ],
    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            model: 'MyApp.model.LogModel',
            proxy: {
				type: 'ajax',
				actionMethods: 'POST',
				url: './log/queryLog.action',
				reader: {
					type: 'json',
					root: 'logList',
					totalProperty : 'totalCount'
				}
			}
        }, cfg)]);
    }
});