/*
 * 分仓请求
 */
Ext.define('MyApp.store.SplitRepositoryRequestStore', {
    extend: 'Ext.data.Store',
    requires: [
        'MyApp.model.SplitRepositoryRequestModel'
    ],
    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            model: 'MyApp.model.SplitRepositoryRequestModel',
            proxy: {
                type: 'ajax',
                actionMethods: 'POST',
                url: './delivery/splitRepositoryRequest.action',
                reader: {
                    type: 'json',
                    root: 'splitRepositoryRequestList',
                    totalProperty : 'totalCount'
                }
            }
        }, cfg)]);
    }
});