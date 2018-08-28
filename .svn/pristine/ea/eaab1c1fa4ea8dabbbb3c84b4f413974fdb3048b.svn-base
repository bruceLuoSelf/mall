//黑名单地址
Ext.define('MyApp.store.BlackListStore', {
    extend: 'Ext.data.Store',
    requires: [
        'MyApp.model.BlackListModel'
    ],
    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            model: 'MyApp.model.BlackListModel',
            proxy: {
                type: 'ajax',
                actionMethods: 'POST',
                url: './blackList/queryBlackList.action',
                reader: {
                    type: 'json',
                    root: 'blackListList',
                    totalProperty : 'totalCount'
                }
            }
        }, cfg)]);
    }
});