/*
 * 店铺配置
 */
Ext.define('MyApp.store.SellerSettingStore', {
    extend: 'Ext.data.Store',
    requires: [
        'MyApp.model.SellerSettingModel'
    ],
    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            model: 'MyApp.model.SellerSettingModel',
            proxy: {
                type: 'ajax',
                actionMethods: 'POST',
                url: './repository/querySellerSetting.action',
                reader: {
                    type: 'json',
                    root: 'sellerSettingList',
                    totalProperty : 'totalCount'
                }
            }
        }, cfg)]);
    }
});