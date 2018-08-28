/*
 * 用户信息数据
 */

Ext.define('MyApp.store.UserStore', {
    extend: 'Ext.data.Store',
    requires: [
        'MyApp.model.UserModel'
    ],
    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            model: 'MyApp.model.UserModel',
			proxy: {
				type: 'ajax',
				actionMethods: 'POST',
				url: './user/queryUser.action',
				reader: {
					type: 'json',
					root: 'userList',
					totalProperty : 'totalCount'
				}
			}
        }, cfg)]);
    }
});