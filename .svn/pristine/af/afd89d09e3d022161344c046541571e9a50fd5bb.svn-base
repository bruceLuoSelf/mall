/*
 * Update History
 * Date        Name                Reason for change
 * ----------  ----------------    ----------------------
 * 2017/07/17  wurongfan           ZW_J_JB_00072金币商城对外接口优化
 *
 */
Ext.define('MyApp.store.FirmLogStore', {
    extend: 'Ext.data.Store',
    requires: [
        'MyApp.model.FirmLogModel'
    ],
    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            model: 'MyApp.model.FirmLogModel',
            proxy: {
                type: 'ajax',
                actionMethods: 'POST',
                url: './goods/selectLogs.action',
                reader: {
                    type: 'json',
                    root: 'firmsLogList',
                    totalProperty : 'totalCount'
                }
            }
        }, cfg)]);
    }
});