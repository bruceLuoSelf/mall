/*
 * 资金明细
 */
Ext.define('MyApp.store.FundDetailStore', {
    extend: 'Ext.data.Store',
    requires: [
        'MyApp.model.FundDetailModel'
    ],
    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            model: 'MyApp.model.FundDetailModel',
            proxy: {
                type: 'ajax',
                actionMethods: 'POST',
                url: './delivery/queryFundDetail.action',
                reader: {
                    type: 'json',
                    root: 'fundDetailList',
                    totalProperty : 'totalCount'
                }
            }
        }, cfg)]);
    }
});