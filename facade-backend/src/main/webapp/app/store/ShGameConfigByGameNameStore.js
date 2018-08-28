/**
 * Created by liuchanghua on 2017/5/13.
 * Update History
 * Date        Name                Reason for change
 * ----------  ----------------    ----------------------
 * 2017/05/13  liuchanghua           ZW_C_JB_00008 商城增加通货
 */
//商品类型配置
Ext.define('MyApp.store.ShGameConfigByGameNameStore', {
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
                 url: './delivery/queryConfigByGameName.action',
                reader: {
                    type: 'json',
                    root: 'gameConfigList',
                    totalProperty : 'totalCount'
                }
            }
        }, cfg)]);
    }
});