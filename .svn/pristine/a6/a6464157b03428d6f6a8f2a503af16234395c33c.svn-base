/*
 *  支付详情 
 */
Ext.define('MyApp.store.PayOrderStore',{
    extend:'Ext.data.Store',
    requires:[
        'MyApp.model.PayOrderModel'
    ],
    constructor: function (cfg) {
        var me=this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            model:'MyApp.model.PayOrderModel',
            proxy: {
                type:'ajax',
                actionMethods: 'POST',
                url: './delivery/queryPayOrder.action',
                reader: {
                    type: 'json',
                    root: 'payOrderlIST',
                    totalProperty: 'totalCount'
                }
            }
        }, cfg)]);
    }
})
