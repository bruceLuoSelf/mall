/*
 * 收货角色
 */
Ext.define('MyApp.store.ShGameAccountStore', {
    extend: 'Ext.data.Store',
    requires: [
        'MyApp.model.ShGameAccountModel'
    ],
    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            model: 'MyApp.model.ShGameAccountModel',
            proxy: {
                type: 'ajax',
                actionMethods: 'POST',
                url: './delivery/queryShGameAccount.action',
                reader: {
                    type: 'json',
                    root: 'gameAccountList',
                    totalProperty : 'totalCount'
                }
            }
        }, cfg)]);
    }
});