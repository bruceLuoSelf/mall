/**
 * Created by zhujun on 2017/5/13. ZW_C_JB_00008 商城增加通货
 */
//商品类型配置
Ext.define('MyApp.store.GoodsStore', {
    extend: 'Ext.data.Store',
    requires: [
        'MyApp.model.GoodsModel'
    ],
    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            model: 'MyApp.model.GoodsModel',
            proxy: {
                type: 'ajax',
                actionMethods: 'POST',
                url: './goods/queryGoods.action',
                reader: {
                    type: 'json',
                    root: 'goodsList',
                    totalProperty : 'totalCount'
                }
            }
        }, cfg)]);
    }
});