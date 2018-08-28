/*
 * 寄售自动化配置
 */
Ext.define('MyApp.store.AutoDeliverSettingStore', {
    extend: 'Ext.data.Store',
    requires: [
        'MyApp.model.AutoDeliverSettingModel'
    ],
    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            model: 'MyApp.model.AutoDeliverSettingModel',
            proxy: {
                type: 'ajax',
                actionMethods: 'POST',
                url: './repository/queryAutoDeliverSetting.action',
                reader: {
                    type: 'json',
                    root: 'autoDeliverSettingList',
                    totalProperty : 'totalCount'
                }
            }
        }, cfg)]);
    }
});