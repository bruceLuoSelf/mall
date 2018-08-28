/*
 * 库存信息
 *  * Date        Name                Reason for change
 * ----------  ----------------    ----------------------
 * 2017/05/15  liuchanghua           ZW_C_JB_00008 商城增加通货
 */
Ext.define('MyApp.store.WarningStore', {
    extend: 'Ext.data.Store',
    requires: [
        'MyApp.model.WarningModel'
    ],
    constructor: function(cfg) {
        var me = this;
        	cfg = cfg || {};
        me.callParent([Ext.apply({
            model: 'MyApp.model.WarningModel',
            proxy: {
				type: 'ajax',
				actionMethods: 'POST',
				url: './goods/queryWarning.action',
				reader: {
					type: 'json',
					root: 'warningList',
					totalProperty : 'totalCount'
				}
			}
        }, cfg)]);
    }
});