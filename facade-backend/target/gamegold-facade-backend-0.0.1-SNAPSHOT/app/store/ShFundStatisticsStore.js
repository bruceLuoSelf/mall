/**
 * Created by Administrator on 2016/12/15.
 */
Ext.define('MyApp.store.ShFundStatisticsStore', {
    extend: 'Ext.data.Store',
    requires: [
        'MyApp.model.ShFundStatisticsModel'
    ],
    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            model: 'MyApp.model.ShFundStatisticsModel',
            proxy: {
                type: 'ajax',
                actionMethods: 'POST',
                url: './delivery/queryShFundStatistics.action',
                reader: {
                    type: 'json',
                    root: 'shFundStatisticsList',
                    totalProperty : 'totalCount'
                }
            }
        }, cfg)]);
    }
});