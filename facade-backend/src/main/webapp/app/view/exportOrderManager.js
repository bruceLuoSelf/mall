
/*
 * 已退款已取消订单导出
 */

Ext.define('MyApp.view.exportOrderManager', {
    extend: 'Ext.panel.Panel',
    id: 'exportOrderManager',
    closable: true,
    title: '订单导出',
    queryForm: null,
    fieldset:null,
    margin:'15 15 15 15',
    mulitFieldset:null,
    getMulitFieldset:function(){
        var me = this;
        if (me.mulitFieldset == null) {
            me.mulitFieldset = Ext.widget('fieldset', { xtype:'fieldset',
                columnWidth: 1,
                title: '订单导出',
                collapsible: true,
                defaultType: 'textfield',
                layout: 'anchor',
                items: [me.getQueryMulitForm()]
            });
        }
        return me.mulitFieldset;
    },
    queryMulitForm: null,
    getQueryMulitForm: function () {
        var me = this;
        if (me.queryMulitForm == null) {
            me.queryMulitForm = Ext.widget('form', {
                border: false,
                layout: 'column',
                defaults: {
                    margin: "20 10 20 10",
                    labelWidth: 80,
                    xtype: 'textfield'
                },
                items: [{
                    fieldLabel: '下单日期',
                    columnWidth:  0.6,
                    xtype: 'rangedatefield',
                    //起始日期组件的name属性。
                    fromName: 'createStartTime',
                    //终止日期组件的name属性。
                    toName: 'createEndTime',
                    fromValue:new Date(),
                    toValue:new Date((new Date()).getTime() + 24 * 60 * 60 * 1000)
                },DataDictionary.getDataDictionaryCombo('SgameName',{
                    fieldLabel: '游戏名称',
                    name: 'gameName',
                    labelWidth: 100,
                    editable: false
                },{value: null,display:'全部'}),DataDictionary.getDataDictionaryCombo('cancleOrderState',{
                    fieldLabel: '订单状态',
                    columnWidth: .3,
                    labelWidth: 100,
                    name: 'orderState',
                    value: 7,
                    editable: false
                },{value: null,display:'全部'}),{
                    xtype: 'container',
                    columnWidth: 1,
                    items:{
                        xtype: 'button',
                        text: '导出',
                        handler: function () {
                            me.transferMulit();
                        }
                    },
                    margin:"15 0 40 100"
                }]
            });
        }
        return me.queryMulitForm;
    },
    transferMulit: function() {
        var form = this.getQueryMulitForm().form,url;
        if (form.isValid()) {
            var values = this.getQueryMulitForm().getValues();

            var params = {
                'createStartTime': values.createStartTime,
                'createEndTime': values.createEndTime,
                'orderState': values.orderState,
                'gameName': values.gameName
            }

            url = './order/exportCancelOrder.action?' + Ext.urlEncode(params);
            window.open(url);
        }
    },
    initComponent: function() {
        var me = this;
        Ext.applyIf(me, {
            items: [me.getMulitFieldset()]
        });
        me.callParent(arguments);
    }
});