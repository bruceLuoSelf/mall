/*
 * 卖家信息数据
 */
Ext.define('MyApp.store.SellerStore', {
    extend: 'Ext.data.Store',
    requires: [
        'MyApp.model.SellerModel'
    ],
    constructor: function(cfg) {
        var me = this;
        	cfg = cfg || {};
        me.callParent([Ext.apply({
            model: 'MyApp.model.SellerModel',
            proxy: {
				type: 'ajax',
				actionMethods: 'POST',
				url: './repository/querySeller.action',
				reader: {
					type: 'json',
					root: 'sellerList',
					totalProperty : 'totalCount'
				}
			}
        }, cfg)]);
    }
});