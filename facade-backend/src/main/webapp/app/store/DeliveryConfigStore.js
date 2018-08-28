/*
 * 收货信息配置
 */
Ext.define('MyApp.store.DeliveryConfigStore', {
    extend: 'Ext.data.Store',
    requires: [
        'MyApp.model.DeliveryConfigModel'
    ],
    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            model: 'MyApp.model.DeliveryConfigModel',
            proxy: {
                type: 'ajax',
                actionMethods: 'POST',
                url: './deliveryType/queryDeliveryCofig.action',
                reader: {
                    type: 'json',
                    root: 'deliveryConfigList',
                    totalProperty : 'totalCount'
                }
            }
        }, cfg)]);
    }
});