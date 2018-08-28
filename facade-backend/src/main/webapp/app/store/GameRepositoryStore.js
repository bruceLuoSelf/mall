/*
 * 游戏详细库存限制配置
 */
Ext.define('MyApp.store.GameRepositoryStore', {
    extend: 'Ext.data.Store',
    requires: [
        'MyApp.model.GameRepositoryModel'
    ],
    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            model: 'MyApp.model.GameRepositoryModel',
            proxy: {
                type: 'ajax',
                actionMethods: 'POST',
                url: './repository/queryGameRepository.action',
                reader: {
                    type: 'json',
                    root: 'repositoryConfineInfos',
                    totalProperty : 'totalCount'
                }
            }
        }, cfg)]);
    }
});