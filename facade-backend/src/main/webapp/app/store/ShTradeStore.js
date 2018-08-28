/*
 * 收货模式
 */
Ext.define('MyApp.store.ShTradeStore', {
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
                url: './delivery/queryTrade.action',
                reader: {
                    type: 'json',
                    root: 'tradeList',
                    totalProperty : 'totalCount'
                }
            }
        }, cfg)]);
    }
});