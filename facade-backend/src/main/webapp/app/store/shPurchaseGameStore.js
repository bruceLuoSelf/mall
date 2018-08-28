
/**
 * 采购商游戏属性配置管理
 */
Ext.define('MyApp.store.shPurchaseGameStore', {
    extend: 'Ext.data.Store',
    requires: [
        'MyApp.model.shPurchaseGameModel'
    ],
    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            model: 'MyApp.model.shPurchaseGameModel',
            proxy: {
                type: 'ajax',
                actionMethods: 'POST',
                url: './shpurchase/shPurchaseGameConfig.action',
                reader: {
                    type: 'json',
                    root: 'purchaseGameList',
                    totalProperty : 'totalCount'
                }
            }
        }, cfg)]);
    }
});