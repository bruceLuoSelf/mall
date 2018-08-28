/*
 * 参考价库存限制配置
 */
Ext.define('MyApp.store.RepositoryConfineStore', {
    extend: 'Ext.data.Store',
    requires: [
        'MyApp.model.RepositoryConfigModel'
    ],
    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            model: 'MyApp.model.RepositoryConfigModel',
            proxy: {
                type: 'ajax',
                actionMethods: 'POST',
                url: './repository/queryRepositoryConfig.action',
                reader: {
                    type: 'json',
                    root: 'repositoryConfigList',
                    totalProperty : 'totalCount'
                }
            }
        }, cfg)]);
    }
});