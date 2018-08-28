/*
 * 评价
 */
Ext.define('MyApp.store.OrderEvaluateStore', {
    extend: 'Ext.data.Store',
    requires: [
        'MyApp.model.OrderEvaluateModel'
    ],
    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            model: 'MyApp.model.OrderEvaluateModel',
            proxy: {
                type: 'ajax',
                actionMethods: 'POST',
                url: './order/queryOrderEvaluate.action',
                reader: {
                    type: 'json',
                    root: 'serviceEvaluateList',
                    totalProperty : 'totalCount'
                }
            }
        }, cfg)]);
    }
});