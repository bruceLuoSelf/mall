//游戏类目配置
Ext.define('MyApp.store.GameCategoryConfigStore', {
    extend: 'Ext.data.Store',
    requires: [
        'MyApp.model.GameCategoryConfigModel'
    ],
    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            model: 'MyApp.model.GameCategoryConfigModel',
            proxy: {
                type: 'ajax',
                actionMethods: 'POST',
                url: './shpurchase/queryGameCategoryConfig.action',
                reader: {
                    type: 'json',
                    root: 'gameCategoryConfigList',
                    totalProperty : 'totalCount'
                }
            }
        }, cfg)]);
    }
});