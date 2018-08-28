/*
 * 评价
 */
Ext.define('MyApp.store.ServiceEvaluateStore', {
    extend: 'Ext.data.Store',
    requires: [
        'MyApp.model.ServiceEvaluateModel'
    ],
    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            model: 'MyApp.model.ServiceEvaluateModel',
            proxy: {
                type: 'ajax',
                actionMethods: 'POST',
                url: './order/queryServiceEvaluate.action',
                reader: {
                    type: 'json',
                    root: 'serviceEvaluateStatisticsList',
                    totalProperty : 'totalCount'
                }
            }
        }, cfg)]);
    }
});