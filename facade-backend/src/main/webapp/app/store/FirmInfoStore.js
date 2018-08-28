/*
 * 收货模式
 */
Ext.define('MyApp.store.FirmInfoStore', {
    extend: 'Ext.data.Store',
    requires: [
        'MyApp.model.FirmInfoModel'
    ],
    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            model: 'MyApp.model.FirmInfoModel',
            proxy: {
                type: 'ajax',
                actionMethods: 'POST',
                url: './goods/queryFirmInfos.action',
                reader: {
                    type: 'json',
                    root: 'firmInfoList',
                    totalProperty : 'totalCount'
                }
            }
        }, cfg)]);
    }
});