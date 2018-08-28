/*
 * 收货模式
 */
Ext.define('MyApp.store.ShTradeForSelectStore', {
    extend: 'Ext.data.Store',
    requires: [
        'MyApp.model.ShTradeModel'
    ],
    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            model: 'MyApp.model.ShTradeModel',
            proxy: {
                type: 'ajax',
                actionMethods: 'POST',
                url: './shpurchase/queryTradeTypeName.action',
                reader: {
                    type: 'json',
                    root: 'tradeList',
                    totalProperty : 'totalCount'
                }
            }
        }, cfg)]);
    }
});