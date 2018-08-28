//出货地址配置
Ext.define('MyApp.store.CurrencyConfigStore', {
    extend: 'Ext.data.Store',
    requires: [
        'MyApp.model.CurrencyConfigModel'
    ],
    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            model: 'MyApp.model.CurrencyConfigModel',
            proxy: {
                type: 'ajax',
                actionMethods: 'POST',
                url: './CurrencyConfig/queryCurrencyConfig.action',
                reader: {
                    type: 'json',
                    root: 'currencyConfigList',
                    totalProperty : 'totalCount'
                }
            }
        }, cfg)]);
    }
});