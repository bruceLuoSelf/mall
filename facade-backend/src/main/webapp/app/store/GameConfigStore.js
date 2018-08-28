/*
 * 游戏配置数据
 */
Ext.define('MyApp.store.GameConfigStore', {
    extend: 'Ext.data.Store',
    requires: [
        'MyApp.model.GameConfigModel'
    ],
    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            model: 'MyApp.model.GameConfigModel',
            proxy: {
				type: 'ajax',
				actionMethods: 'POST',
				url: './order/queryGameConfig.action',
				reader: {
					type: 'json',
					root: 'gameConfigList',
					totalProperty : 'totalCount'
				}
			}
        }, cfg)]);
    }
});