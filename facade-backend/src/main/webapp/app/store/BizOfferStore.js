Ext.define('MyApp.store.BizOfferStore', {
    extend: 'Ext.data.Store',
    requires: [
        'MyApp.model.BizOfferModel'
    ],
    constructor: function(cfg) {
        var me = this;
        	cfg = cfg || {};
        me.callParent([Ext.apply({
            model: 'MyApp.model.BizOfferModel',
            proxy: {
				type: 'ajax',
				actionMethods: 'POST',
				url: './order/queryTradeOrderList.action',
				reader: {
					type: 'json',
					root: 'bizOfferList',
					totalProperty : 'totalCount'
				}
			}
        }, cfg)]);
    }
});