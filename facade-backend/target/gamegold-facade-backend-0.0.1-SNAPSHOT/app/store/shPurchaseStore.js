/**
 * Created by Administrator on 2016/12/14.
 */

/**
 * 采购商
 */
Ext.define('MyApp.store.shPurchaseStore', {
    extend: 'Ext.data.Store',
    requires: [
        'MyApp.model.shPurchaseModel'
    ],
    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            model: 'MyApp.model.shPurchaseModel',
            proxy: {
                type: 'ajax',
                actionMethods: 'POST',
                url: './shpurchase/shPurchase.action',
                reader: {
                    type: 'json',
                    root: 'purchaserDataList',
                    totalProperty : 'totalCount'
                }
            }
        }, cfg)]);
    }
});