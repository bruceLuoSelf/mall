/**
 *  * Update History
 * Date        Name                Reason for change
 * ----------  ----------------    ----------------------
 * 2017/08/16 wangmin				ZW_C_JB_00021 商城资金改造
 *
 */
Ext.define('MyApp.store.sevenBaoLogStore', {
    extend: 'Ext.data.Store',
    requires: [
        'MyApp.model.sevenBaoLogModel'
    ],
    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            model: 'MyApp.model.sevenBaoLogModel',
            proxy: {
                type: 'ajax',
                actionMethods: 'POST',
                url: './goods/selectLog.action',
                reader: {
                    type: 'json',
                    root: 'sevenBaoBindLogEOList',
                    totalProperty : 'totalCount'
                }
            }
        }, cfg)]);
    }
});