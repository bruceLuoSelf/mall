/**
 * Created by Administrator on 2017/1/10.
 */
/*
 *  支付详情 
 */
Ext.define('MyApp.store.QQOnLineStore',{
    extend:'Ext.data.Store',
    requires:[
        'MyApp.model.QQOnLineModel'
    ],
    constructor: function (cfg) {
        var me=this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            model:'MyApp.model.QQOnLineModel',
            proxy: {
                type:'ajax',
                actionMethods: 'POST',
                url: './delivery/queryQqOnLine.action',
                reader: {
                    type: 'json',
                    root: 'qqOnLineEOList',
                    totalProperty: 'totalCount'
                }
            }
        }, cfg)]);
    }
})
