
Ext.define('MyApp.view.generateCouponManager', {
    extend: 'Ext.panel.Panel',
    id: 'generateCouponManager',
    closable: true,
    title: '发放优惠券',
    queryForm: null,
    fieldset:null,
    margin:'15 15 15 15',
    mulitFieldset:null,
    getMulitFieldset:function(){
        var me = this;
        if (me.mulitFieldset == null) {
            me.mulitFieldset = Ext.widget('fieldset', { xtype:'fieldset',
                columnWidth: 1,
                title: '批量发放优惠券',
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
                    xtype: 'numberfield',
                    fieldLabel: '优惠券数量',
                    allowBlank: false,
                    columnWidth: 0.25,
                    labelWidth: 85,
                    name: 'num',
                    minValue: 1
                },DataDictionary.getDataDictionaryCombo('couponPrice',{
                    fieldLabel: '金额',
                    name: 'price',
                    columnWidth: 0.25,
                    labelWidth: 55,
                    value: 0,
                    editable: false
                }),DataDictionary.getDataDictionaryCombo('couponType',{
                    fieldLabel: '优惠券类型',
                    name: 'couponType',
                    columnWidth: 0.25,
                    labelWidth: 85,
                    value: 0,
                    editable: false
                }),{
                    fieldLabel: '使用期限',
                    columnWidth:  0.4,
                    xtype: 'rangedatefield',
                    //起始日期组件的name属性。
                    fromName: 'createStartTime',
                    //终止日期组件的name属性。
                    toName: 'createEndTime',
                    fromValue:new Date(),
                    toValue:new Date((new Date()).getTime() + 24 * 60 * 60 * 1000)
                },{
                    xtype: 'container',
                    columnWidth: 1,
                    items:{
                        xtype: 'button',
                        text: '生成优惠券',
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
            if(values.price==0){
                Ext.ux.Toast.msg("温馨提示", "请选择金额");
                return;
            }
            if(values.couponType==0){
                Ext.ux.Toast.msg("温馨提示", "请选择优惠券类型");
                return;
            }

            var params = {
                'num':values.num,
                'discountCoupon.price': values.price,
                'discountCoupon.couponType': values.couponType,
                'discountCoupon.startTime': values.createStartTime,
                'discountCoupon.endTime': values.createEndTime,
            }

            Ext.Ajax.request({
                url : "./order/validateUser.action",
                method: 'POST',
                success : function(response, opts) {
                    url = './order/generateCoupon.action?' + Ext.urlEncode(params);
                    window.open(url);
                },
                exception : function(response, opts) {
                    var json = Ext.decode(response.responseText);
                    Ext.ux.Toast.msg("温馨提示", json.message);
                }
            });
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