/*
 * 商城的所有游戏名称和ID
 */
Ext.define('MyApp.store.MallGameNameIdStore', {
    extend: 'Ext.data.Store',
    requires: [
        'MyApp.model.MallGameNameIdModel'
    ],
    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            model: 'MyApp.model.MallGameNameIdModel',
            proxy: {
                type: 'ajax',
                actionMethods: 'GET',
                url: './order/queryGameNameIdList.action',
                reader: {
                    type: 'json',
                    root: 'gameNameIdList'
                }
            }
        }, cfg)]);
    }
});