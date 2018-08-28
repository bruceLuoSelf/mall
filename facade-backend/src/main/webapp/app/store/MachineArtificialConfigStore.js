/*
 * 参考价库存限制配置
 */
Ext.define('MyApp.store.MachineArtificialConfigStore', {
    extend: 'Ext.data.Store',
    requires: [
        'MyApp.model.MachineArtificialConfigModel'
    ],
    constructor: function (cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            model: 'MyApp.model.MachineArtificialConfigModel',
            proxy: {
                type: 'ajax',
                actionMethods: 'POST',
                url: './repository/queryMachineArtificialConfig.action',
                reader: {
                    type: 'json',
                    root: 'machineArtificialConfigList',
                    totalProperty: 'totalCount'
                }
            }
        }, cfg)]);
    }
});