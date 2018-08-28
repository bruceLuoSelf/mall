//游戏类目配置
Ext.define('MyApp.store.GameCategoryConfigForSelectStore', {
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
                url: './shpurchase/queryGoodsTypeName.action',
                reader: {
                    type: 'json',
                    root: 'gameCategoryConfigList',
                    totalProperty : 'totalCount'
                }
            }
        }, cfg)]);
    }
});