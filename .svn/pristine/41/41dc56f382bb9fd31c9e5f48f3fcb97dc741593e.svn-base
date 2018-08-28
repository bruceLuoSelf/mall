/*
 * 库存数据
 */
Ext.define('MyApp.store.ConfigPowerStore', {
    extend: 'Ext.data.Store',
    requires: [
        'MyApp.model.ConfigPowerModel'
    ],
    constructor: function(cfg) {
        var me = this;
        	cfg = cfg || {};
        me.callParent([Ext.apply({
            model: 'MyApp.model.ConfigPowerModel',
            proxy: {
				type: 'ajax',
				actionMethods: 'POST',
				url: './order/queryConfig.action',
				reader: {
					type: 'json',
					root: 'configPowerList',
					totalProperty : 'totalCount'
				}
			}
        }, cfg)]);
    }
});