/*
 * 保险设定数据
 */
Ext.define('MyApp.store.InsuranceSettingsStore', {
    extend: 'Ext.data.Store',
    requires: [
        'MyApp.model.InsuranceSettingsModel'
    ],
    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            model: 'MyApp.model.InsuranceSettingsModel',
            proxy: {
				type: 'ajax',
				actionMethods: 'POST',
				url: './insurance/queryPage.action',
				reader: {
					type: 'json',
					root: 'list',
					totalProperty : 'totalCount'
				}
			}
        }, cfg)]);
    }
});