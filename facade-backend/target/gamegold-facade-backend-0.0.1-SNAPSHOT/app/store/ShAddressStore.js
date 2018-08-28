/*
 * 防骗短信规则数据
 */
Ext.define('MyApp.store.ShAddressStore', {
    extend: 'Ext.data.Store',
    requires: [
        'MyApp.model.ShAddressModel'
    ],
    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            model: 'MyApp.model.ShAddressModel',
			proxy: {
				type: 'ajax',
				actionMethods: 'POST',
				url: './delivery/queryConfig.action',
				reader: {
					type: 'json',
					root: 'configList',
					totalProperty : 'totalCount'
				}
			}
        }, cfg)]);
    }
});