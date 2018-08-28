/*
 * 安心买游戏币数量下拉菜单数据
 */
Ext.define('MyApp.store.HotRecommendConfigStore', {
    extend: 'Ext.data.Store',
    requires: [
        'MyApp.model.HotRecommendConfigModel'
    ],
    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            model: 'MyApp.model.HotRecommendConfigModel',
            proxy: {
				type: 'ajax',
				actionMethods: 'POST',
				url: './hotrecommend/queryConfigList.action',
				reader: {
					type: 'json',
					root: 'configList',
					totalProperty : 'totalCount'
				}
			}
        }, cfg)]);
    }
});