Ext.define('MyApp.store.ShGameTradeStore',{
    extend:'Ext.data.Store',
    requires:[
        'MyApp.model.ShGameTradeModel'
    ],
    constructor:function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            model:'MyApp.model.ShGameTradeModel',
            proxy:{
                type:'ajax',
                actionMethods:'POST',
                url:'./delivery/queryShGameTrade.action',
                reader:{
                    type:'json',
                    root:'shGameTradeList',
                    totalProperty:'totalCount'
                }
            }
        },cfg)]);
    }
})