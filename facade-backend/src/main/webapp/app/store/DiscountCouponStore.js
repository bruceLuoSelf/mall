/*
 * 红包
 */
Ext.define('MyApp.store.DiscountCouponStore', {
    extend: 'Ext.data.Store',
    requires: [
        'MyApp.model.DiscountCouponModel'
    ],
    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            model: 'MyApp.model.DiscountCouponModel',
            proxy: {
                type: 'ajax',
                actionMethods: 'POST',
                url: './order/queryDiscountCoupon.action',
                reader: {
                    type: 'json',
                    root: 'discountCouponList',
                    totalProperty : 'totalCount'
                }
            }
        }, cfg)]);
    }
});