/*
 * 收货模式
 */
Ext.define('MyApp.store.TransferFileStore', {
    extend: 'Ext.data.Store',
    requires: [
        'MyApp.model.transFileModel'
    ],
    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            model: 'MyApp.model.transFileModel',
            proxy: {
                type: 'ajax',
                actionMethods: 'POST',
                url: './goods/selectTransferFile.action',
                reader: {
                    type: 'json',
                    root: 'transferFileList',
                    totalProperty : 'totalCount'
                }
            }
        }, cfg)]);
    }
});