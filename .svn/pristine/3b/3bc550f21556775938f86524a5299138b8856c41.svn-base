/*
 * 收货模式
 */
Ext.define('MyApp.store.FirmAccountStore', {
    extend: 'Ext.data.Store',
    requires: [
        'MyApp.model.FirmsAccountModel'
    ],
    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            model: 'MyApp.model.FirmsAccountModel',
            proxy: {
                type: 'ajax',
                actionMethods: 'POST',
                url: './goods/queryFirmsAccounts.action',
                reader: {
                    type: 'json',
                    root: 'firmInfoList',
                    totalProperty : 'totalCount'
                }
            }
        }, cfg)]);
    }
});