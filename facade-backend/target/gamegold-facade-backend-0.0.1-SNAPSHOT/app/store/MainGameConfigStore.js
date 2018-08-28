/*
 * 寄售自动化配置
 */
Ext.define('MyApp.store.MainGameConfigStore', {
    extend: 'Ext.data.Store',
    requires: [
        'MyApp.model.MainGameConfigModel'
    ],
    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            model: 'MyApp.model.MainGameConfigModel',
            proxy: {
                type: 'ajax',
                actionMethods: 'POST',
                url: './shpurchase/queryGameConfig.action',
                reader: {
                    type: 'json',
                    root: 'mainGameConfigList',
                    totalProperty : 'totalCount'
                }
            }
        }, cfg)]);
    }
});