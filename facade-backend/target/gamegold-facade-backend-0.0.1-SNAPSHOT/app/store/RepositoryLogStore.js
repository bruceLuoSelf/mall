/*
 * 库存日志数据
 */
Ext.define('MyApp.store.RepositoryLogStore', {
    extend: 'Ext.data.Store',
    requires: [
        'MyApp.model.RepositoryLogModel'
    ],
    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            model: 'MyApp.model.RepositoryLogModel',
            proxy: {
				type: 'ajax',
				actionMethods: 'POST',
				url: './log/queryRepositoryLogs.action',
				reader: {
					type: 'json',
					root: 'logs',
					totalProperty : 'totalCount'
				}
			}
        }, cfg)]);
    }
});