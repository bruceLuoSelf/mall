/**
 * 担保订单移交
 */


Ext.define('MyApp.view.transferOrderManager', {
    extend: 'Ext.panel.Panel',
    id: 'transferOrderManager',
    closable: true,
    title: '担保订单移交',
    queryForm: null,
    fieldset:null,
    margin:'15 15 15 15',
    getFieldset:function(){
        var me = this;
        if (me.fieldset == null) {
            me.fieldset = Ext.widget('fieldset', { xtype:'fieldset',
                columnWidth: 1,
                title: '单笔订单移交',
                collapsible: true,
                defaultType: 'textfield',
                defaults: {anchor: '90%'},
                layout: 'anchor',
                items: [me.getQueryForm()]
            });
        }
        return me.fieldset;
    },
    getQueryForm: function () {
        var me = this;
        if (me.queryForm == null) {
            me.queryForm = Ext.widget('form', {
                border: false,
                layout: 'column',
                defaults: {
                    margin: "20 10 20 10",
                    labelWidth: 80,
                    xtype: 'textfield'
                },
                items: [{
                    columnWidth: 0.4,
                    name: 'orderId',
                    fieldLabel: '订单号',
                    allowBlank: false,
                    emptyText: "例如：YX1503130000773_2014020"
                },{
                    xtype: 'button',
                    text: '移交',
                    handler: function () {
                        me.transfer();
                    }
                }]
            });
        }
        return me.queryForm;
    },
    transfer: function() {
        var form = this.getQueryForm().form;
        if (form.isValid()) {
            Ext.Msg.confirm('移交订单', '未发货的订单请不要移交，这笔订单您确定要移交吗？', function(btn){
                if (btn == 'yes'){
                    var orderId = form.findField('orderId').value;
                    Ext.Ajax.request({
                        url: './order/transferOrder.action',
                        params: {'id': orderId},
                        success: function (response, opts) {
                            var json = Ext.decode(response.responseText);
                            Ext.ux.Toast.msg("温馨提示", "移交成功");
                        },
                        exception: function (response, opts) {
                            var json = Ext.decode(response.responseText);
                            Ext.ux.Toast.msg("温馨提示", json.message);
                        }
                    });
                }
            });
        }
    },
    mulitFieldset:null,
    getMulitFieldset:function(){
        var me = this;
        if (me.mulitFieldset == null) {
            me.mulitFieldset = Ext.widget('fieldset', { xtype:'fieldset',
                columnWidth: 1,
                title: '批量订单移交',
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
                    xtype: 'textareafield',
                    columnWidth: 0.4,
                    name: 'orderIdMulit',
                    fieldLabel: '订单号',
                    allowBlank: false,
                    emptyText: "每行一个订单号，用回车键分开",
                    height: 350
                },{
                    xtype: 'container',
                    columnWidth: 1,
                    items:{
                        xtype: 'button',
                        text: '批量移交',
                        handler: function () {
                            me.transferMulit();
                        }
                    },
                    margin:"-10 0 40 100"
                }]
            });
        }
        return me.queryMulitForm;
    },
    transferMulit: function() {
        var form = this.getQueryMulitForm().form;
        if (form.isValid()) {
            Ext.Msg.confirm('移交订单', '未发货的订单请不要移交，上述订单您确定要移交吗？', function(btn){
                if (btn == 'yes'){
                    var orderId = form.findField('orderIdMulit').value;
                    Ext.Ajax.request({
                        url: './order/batchTransferOrder.action',
                        params: {'orderIds': orderId},
                        success: function (response, opts) {
                            var json = Ext.decode(response.responseText);

                            var failureMsg=getArrayMsg(json.failureOrders);
                            var successMsg=getArrayMsg(json.successOrders);
                            successMsg=successMsg==""?"0个":successMsg;
                            failureMsg=failureMsg==""?"0个":failureMsg;
                            Ext.Msg.alert("温馨提示","<B style='color:#157fcc'>提交成功订单号</B>：" +successMsg+"<br/><B style='color:#157fcc'>提交失败订单号</B>："+failureMsg);
                        },
                        exception: function (response, opts) {
                            var json = Ext.decode(response.responseText);
                            Ext.ux.Toast.msg("温馨提示", json.message);
                        }
                    });
                }
            });

        }
    },
    initComponent: function() {
        var me = this;
        Ext.applyIf(me, {
            items: [me.getFieldset(),me.getMulitFieldset()]
        });
        me.callParent(arguments);
    }
});

//处理批量订单数组信息，每五个换行
function getArrayMsg(arr){
    var msg="";
    if(arr.length>0) {
        for (var i = 0; i < arr.length; i++) {
            if (i % 5 == 0) {
                msg += "<br/>";
            }
            msg += arr[i] + ",";
        }
        msg=msg.substring(0,msg.length-1);
    }
    return msg;
}