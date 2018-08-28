Ext.define('MyApp.store.ShGameGoodsTypeStore', {
    extend: 'Ext.data.Store',
    requires: [
        'MyApp.model.ShGameGoodsTypeModel'
    ],
    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            model: 'MyApp.model.ShGameGoodsTypeModel',
            proxy: {
                type: 'ajax',
                actionMethods: 'GET',
                url: './delivery/queryGameGoodsType.action',
                reader: {
                    type: 'json',
                    root: 'gameGoodsTypeList'
                }
            }
        }, cfg)]);
    }
});