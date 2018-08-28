/**
 * Created by Administrator on 2017/1/4.
 */
//出货地址配置
Ext.define('MyApp.store.ShGameConfigStore', {
    extend: 'Ext.data.Store',
    requires: [
        'MyApp.model.ShGameConfigModel'
    ],
    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            model: 'MyApp.model.ShGameConfigModel',
            proxy: {
                type: 'ajax',
                actionMethods: 'POST',
                 url: './delivery/queryShGameConfig.action',
                reader: {
                    type: 'json',
                    root: 'gameConfigList',
                    totalProperty : 'totalCount'
                }
            }
        }, cfg)]);
    }
});