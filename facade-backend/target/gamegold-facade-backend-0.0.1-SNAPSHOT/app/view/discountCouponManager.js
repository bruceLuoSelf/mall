Ext.define('MyApp.view.discountCouponManager', {
    extend: 'Ext.panel.Panel',
    id:'discountCouponManager',
    closable: true,
    title: '优惠券记录',
    autoScroll: false,
    listeners:{
        'resize':function(){
            this.logGrid.setHeight(window.document.body.offsetHeight-235);
        }
    },
    sellerSettingWindow: null,
    getSellerSettingWindow: function(){
        if(this.sellerSettingWindow == null){
            this.sellerSettingWindow = new MyApp.view.SellerSettingWindow();
        }
        return this.sellerSettingWindow;
    },
    queryForm: null,
    getQueryForm: function(){
        var me = this;
        if(me.queryForm==null){
            me.queryForm = Ext.widget('form',{
                layout: 'column',
                defaults: {
                    margin: '10 10 10 10',
                    xtype: 'textfield'
                },
                items: [{
                    fieldLabel: '订单号',
                    columnWidth: 0.25,
                    name: 'orderId'
                },DataDictionary.getDataDictionaryCombo('couponState',{
                    fieldLabel: '类型',
                    name: 'couponType',
                    columnWidth: 0.25,
                    value: 0,
                    editable: false
                }),{
                    fieldLabel: '优惠券码',
                    columnWidth: 0.25,
                    name: 'code'
                },DataDictionary.getDataDictionaryCombo('check',{
                    fieldLabel: '是否使用',
                    columnWidth: 0.25,
                    name: 'isUsed',
                    editable: false
                },{value:null,display:'全部'}),{
                    fieldLabel: '查询日期',
                    columnWidth: .5,
                    xtype: 'rangedatefield',
                    //起始日期组件的name属性。
                    fromName: 'createStartTime',
                    //终止日期组件的name属性。
                    toName: 'createEndTime',
                    fromValue:new Date((new Date()).getTime()-6*24*60*60*1000),
                    toValue: new Date()
                }],
                buttons: [{
                    text:'重置',
                    handler: function() {
                        me.getQueryForm().getForm().reset();
                    }
                },'->',{
                    text:'查询',
                    handler: function() {
                        me.getPagingToolbar().moveFirst();
                    }
                }]
            });
        }
        return me.queryForm;
    },
    toolbar: null,
    getToolbar: function(){
        var me = this;
        if(Ext.isEmpty(me.toolbar)){
            me.toolbar = Ext.widget('toolbar',{
                dock: 'top',
                items: [{
                    text: '导出',
                    listeners: {
                        click: {
                            fn: me.exportRepository,
                            scope: me
                        }
                    }
                }]
            });
        }
        return me.toolbar;
    },
    // 导出优惠券
    exportRepository: function(button, e, eOpts){
        var me = this,url,
            queryForm = me.getQueryForm();
        if (queryForm != null) {
            var values = queryForm.getValues();

            var params = {
                'discountCoupon.code': values.code,
                'discountCoupon.couponType': values.couponType,
                'discountCoupon.orderId': values.orderId,
                'discountCoupon.startTime': values.createStartTime,
                'discountCoupon.endTime': values.createEndTime
            }
            if(!Ext.isEmpty(values.isUsed)){
                params = Ext.Object.merge(params,{
                    'isUsed':values.isUsed
                });
            }
        }

        Ext.Ajax.request({
            url : "./order/validateUser.action",
            method: 'POST',
            success : function(response, opts) {
                url = './order/exportCoupon.action?' + Ext.urlEncode(params);
                window.open(url);
            },
            exception : function(response, opts) {
                var json = Ext.decode(response.responseText);
                Ext.ux.Toast.msg("温馨提示", json.message);
            }
        });
    },
    store: null,
    getStore: function(){
        var me = this;
        if(me.store==null){
            me.store = Ext.create('MyApp.store.DiscountCouponStore',{
                autoLoad: true,
                listeners: {
                    beforeload : function(store, operation, eOpts) {
                        var queryForm = me.getQueryForm();
                        if (queryForm != null) {
                            var values = queryForm.getValues(),params={};
                            //var region = Ext.String.trim(values.region);
                            params= {
                                'discountCoupon.code': values.code,
                                'discountCoupon.couponType': values.couponType,
                                'discountCoupon.orderId': values.orderId,
                                'discountCoupon.startTime': values.createStartTime,
                                'discountCoupon.endTime': values.createEndTime
                            };
                            if(!Ext.isEmpty(values.isUsed)){
                                params = Ext.Object.merge(params,{
                                    'isUsed':values.isUsed
                                });
                            }
                            Ext.apply(operation, {
                                params : params
                            });
                        }
                    }
                }
            });
        }
        return me.store;
    },
    pagingToolbar: null,
    getPagingToolbar: function(){
        var me = this;
        if(me.pagingToolbar==null){
            me.pagingToolbar = Ext.widget('pagingtoolbar',{
                store: me.getStore(),
                dock: 'bottom',
                displayInfo: true
            });
        }
        return me.pagingToolbar;
    },
    logGrid: null,
    getLogGrid: function(){
        var me = this;
        if(Ext.isEmpty(me.logGrid)){
            me.logGrid = Ext.widget('gridpanel',{
                header: false,
                columnLines: true,
                store: me.getStore(),
                columns: [{
                    xtype: 'rownumberer'
                },{
                    dataIndex: 'code',
                    text: '优惠券码',
                    flex: 1,
                    align: 'center'
                },{
                    dataIndex: 'price',
                    text: '金额',
                    flex: 0.5,
                    align: 'center'
                },{
                    dataIndex: 'couponType',
                    xtype: 'ellipsiscolumn',
                    flex: 0.5,
                    sortable: false,
                    align: 'center',
                    text: '类型',
                    renderer: function(value){
                        return DataDictionary.rendererSubmitToDisplay(value,'couponState');
                    }
                },{
                    dataIndex: 'isUsed',
                    text: '是否已使用',
                    flex: 0.7,
                    align: 'center',
                    renderer: function(value, metaData, record, rowIndex, colIndex, store, view) {
                        return CommonFunction.rendererEnable(!value);
                    }
                },{
                    dataIndex: 'orderId',
                    text: '订单号',
                    flex: 1.2,
                    align: 'center'
                },{
                    xtype: 'datecolumn',
                    format:'Y-m-d',
                    dataIndex: 'startTime',
                    sortable: false,
                    flex: 0.8,
                    align: 'center',
                    text: '开始时间 '
                },{
                    xtype: 'datecolumn',
                    format:'Y-m-d',
                    dataIndex: 'endTime',
                    sortable: false,
                    flex: 0.8,
                    align: 'center',
                    text: '截至时间 '
                }],
                dockedItems: [me.getToolbar(),me.getPagingToolbar()],
                selModel: Ext.create('Ext.selection.CheckboxModel', {
                    allowDeselect: false,
                    mode: 'single'
                })
            });
        }
        return me.logGrid;
    },
    initComponent: function() {
        var me = this;
        Ext.applyIf(me, {
            items: [me.getQueryForm(),me.getLogGrid()]
        });
        me.callParent(arguments);
    }
});