//出货地址配置
Ext.define('MyApp.store.ConfigStore', {
    extend: 'Ext.data.Store',
    requires: [
        'MyApp.model.ConfigModel'
    ],
    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            model: 'MyApp.model.ConfigModel',
            proxy: {
                type: 'ajax',
                actionMethods: 'POST',
                url: './shpurchase/queryConfig.action',
                reader: {
                    type: 'json',
                    root: 'configList',
                    totalProperty : 'totalCount'
                }
            }
        }, cfg)]);
    }
});