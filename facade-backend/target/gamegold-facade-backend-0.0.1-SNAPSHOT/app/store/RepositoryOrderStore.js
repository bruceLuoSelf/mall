/*
 * 库存数据
 */
Ext.define('MyApp.store.RepositoryOrderStore', {
    extend: 'Ext.data.Store',
    requires: [
        'MyApp.model.RepositoryModel'
    ],
    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            model: 'MyApp.model.RepositoryModel',
            proxy: {
                type: 'ajax',
                actionMethods: 'POST',
                url: './repository/queryOrderRepository.action',
                reader: {
                    type: 'json',
                    root: 'repositoryList',
                    totalProperty : 'totalCount'
                }
            }
        }, cfg)]);
    }
});