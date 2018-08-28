Ext.define('MyApp.store.ReferencePriceStore', {
    extend: 'Ext.data.Store',
    requires: [
        'MyApp.model.ReferencePriceModel'
    ],
    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            model: 'MyApp.model.ReferencePriceModel',
            proxy: {
                type: 'ajax',
                actionMethods: 'POST',
                url: './repository/queryReferencePrice.action',
                reader: {
                    type: 'json',
                    root: 'referencePriceList',
                    totalProperty : 'totalCount'
                }
            }
        }, cfg)]);
    }
});
