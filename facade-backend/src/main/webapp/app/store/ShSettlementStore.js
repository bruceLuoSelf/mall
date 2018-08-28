/*
 * 分仓日志
 */
Ext.define('MyApp.store.ShSettlementStore', {
    extend: 'Ext.data.Store',
    requires: [
        'MyApp.model.ShSettlementModel'
    ],
    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
                model: 'MyApp.model.ShSettlementModel',
            proxy: {
                type: 'ajax',
                actionMethods: 'POST',
                url: './settlement/querySettlement.action',
                reader: {
                    type: 'json',
                    root: 'settlementList',
                    totalProperty : 'totalCount'
                }
            }
        }, cfg)]);
    }
});