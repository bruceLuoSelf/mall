/*
 * 用户信息日志信息
 */

Ext.define('MyApp.store.UserLoginStore', {
    extend: 'Ext.data.Store',
    requires: [
        'MyApp.model.UserLogModel'
    ],
    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            model: 'MyApp.model.UserLogModel',
            proxy: {
                type: 'ajax',
                actionMethods: 'POST',
                url: './log/queryUserLog.action',
                reader: {
                    type: 'json',
                    root: 'userLoginList',
                    totalProperty : 'totalCount'
                }
            }
        }, cfg)]);
    }
});