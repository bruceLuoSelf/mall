/*
 * 分仓日志
 */
Ext.define('MyApp.store.SplitRepositoryLogStore', {
    extend: 'Ext.data.Store',
    requires: [
        'MyApp.model.SplitRepositoryLogModel'
    ],
    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            model: 'MyApp.model.SplitRepositoryLogModel',
            proxy: {
                type: 'ajax',
                actionMethods: 'POST',
                url: './delivery/querySplitRepositoryLog.action',
                reader: {
                    type: 'json',
                    root: 'splitRepositoryLogList',
                    totalProperty : 'totalCount'
                }
            }
        }, cfg)]);
    }
});