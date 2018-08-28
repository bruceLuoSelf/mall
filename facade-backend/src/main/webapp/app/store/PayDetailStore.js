/*
 *  支付详情 
 */
Ext.define('MyApp.store.PayDetailStore',{
    extend:'Ext.data.Store',
    requires:[
        'MyApp.model.PayDetailModel'
    ],
    constructor: function (cfg) {
        var me=this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            model:'MyApp.model.PayDetailModel',
            proxy: {
                type:'ajax',
                actionMethods: 'POST',
                url: './delivery/queryPayDetail.action',
                reader: {
                    type: 'json',
                    root: 'payDetailList',
                    totalProperty: 'totalCount'
                }
            }
        }, cfg)]);
    }
})
