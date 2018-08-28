/*
 * 商城的所有游戏名称和ID
 */
Ext.define('MyApp.store.MallGoodsTypeNameIdStore', {
    extend: 'Ext.data.Store',
    requires: [
        'MyApp.model.MallGoodsTypeNameIdModel'
    ],
    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            model: 'MyApp.model.MallGoodsTypeNameIdModel',
            proxy: {
                type: 'ajax',
                actionMethods: 'GET',
                url: './order/queryGoodsTypeNameIdList.action',
                reader: {
                    type: 'json',
                    root: 'goodsTypeNameIdList'
                }
            }
        }, cfg)]);
    }
});
