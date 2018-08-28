Ext.define('MyApp.view.fundDetailManager', {
    extend: 'Ext.panel.Panel',
    id:'fundDetailManager',
    closable: true,
    title: '资金明细',
    autoScroll: false,
    layout: "border",
    listeners:{
        'resize':function(){
            this.fundDetailGrid.setHeight(window.document.body.offsetHeight-235);
        }
    },
    fundDetailWindow: null,
    getFundDetailWindow: function(){
        if(this.fundDetailWindow == null){
            this.fundDetailWindow = new MyApp.view.FundDetailWindow();
        }
        return this.fundDetailWindow;
    },
    queryForm: null,
    getQueryForm: function(){
        var me = this;
        if(me.queryForm==null){
            me.queryForm = Ext.widget('form',{
                region: 'north',
                layout: 'column',

                defaults: {
                    margin: '10 10 10 10',
                    xtype: 'textfield'
                },
                items: [{
                    fieldLabel: '创建日期',
                    xtype: 'rangedatefield',
                    fromName: 'createStartTime',
                    toName: 'createEndTime',
                    fromValue: new Date(new Date()-30*24*60*60*1000),
                    toValue: new Date()
                },{
                    fieldLabel: '采购商5173账号',
                    labelWidth: 120,
                    name: 'buyerAccount'
                },DataDictionary.getDataDictionaryCombo('fundDetailType',{
                    fieldLabel: '类型',
                    labelWidth: 90,
                    name: 'type',
                    editable: false,
                    value: 0
                }),{
                    fieldLabel: '充值单号',
                    name: 'payOrderId'
                },{
                    fieldLabel: '付款明细订单号',
                    name: 'payDetailOrderId'
                },{
                    fieldLabel: '退款订单号',
                    name: 'refundOrderId'
                },{
                    fieldLabel: '出货订单号',
                    name: 'deliveryOrderId'
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
    store: null,
    getStore: function(){
        var me = this;
        if(me.store==null){
            me.store = Ext.create('MyApp.store.FundDetailStore',{
                autoLoad: true,
                listeners: {
                    beforeload : function(store, operation, eOpts) {
                        var queryForm = me.getQueryForm();
                        if (queryForm != null) {
                            var values = queryForm.getValues();
                            Ext.apply(operation, {
                                params: {
                                    'fundDetail.buyerAccount': Ext.String.trim(values.buyerAccount),
                                    'startTime':values.createStartTime,
                                    'endTime':values.createEndTime,
                                    'fundDetail.payOrderId': Ext.String.trim(values.payOrderId),
                                    'fundDetail.refundOrderId': Ext.String.trim(values.refundOrderId),
                                    'fundDetail.deliveryOrderId': Ext.String.trim(values.deliveryOrderId),
                                    'fundDetail.payDetailOrderId': Ext.String.trim(values.payDetailOrderId),
                                    'fundDetail.type': values.type
                                }
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
    fundDetailGrid: null,
    getFundDetailGrid: function(){
        var me = this;
        if(Ext.isEmpty(me.fundDetailGrid)){
            me.fundDetailGrid = Ext.widget('gridpanel',{
                region: 'center',
                header: false,
                columnLines: true,
                store: me.getStore(),
                columns: [{
                    xtype: 'rownumberer'
                },{
                    dataIndex: 'buyerAccount',
                    text: '采购商5173账号',
                    flex:  1,
                    align: 'center'
                },{
                    dataIndex: 'payOrderId',
                    text: '充值单号',
                    flex: 1.2,
                    align: 'center'
                },{
                    dataIndex: 'payDetailOrderId',
                    text: '付款明细订单号',
                    flex: 1.2,
                    align: 'center'
                },{
                    dataIndex: 'refundOrderId',
                    text: '退款订单号',
                    flex: 1.2,
                    align: 'center'
                },{
                    dataIndex: 'deliveryOrderId',
                    text: '出货订单号',
                    flex: 1.2,
                    align: 'center'
                },{
                    dataIndex: 'type',
                    text: '类型 ',
                    flex: 0.8,
                    sortable: false,
                    align: 'center',
                    renderer: function(value, metaData, record, rowIndex, colIndex, store, view) {
                        return DataDictionary.rendererSubmitToDisplay(value,'fundDetailType');
                    }
                },{
                    dataIndex: 'amount',
                    text: '金额',
                    flex: 1,
                    align: 'center'
                },{
                    xtype: 'linebreakcolumn',
                    dataIndex: 'log',
                    text: '日志详情',
                    flex: 2.2,
                    align: 'center'
                },{
                    xtype: 'datecolumn',
                    format:'Y-m-d H:i:s',
                    dataIndex: 'createTime',
                    sortable: false,
                    flex: 1.5,
                    align: 'center',
                    text: '操作时间'
                }],
                dockedItems: [me.getPagingToolbar()],
                selModel: Ext.create('Ext.selection.CheckboxModel', {
                    allowDeselect: true,
                    mode: 'MULTI'
                })
            });
        }
        return me.fundDetailGrid;

    },
    initComponent: function() {
        var me = this;
        Ext.applyIf(me, {
            items: [me.getQueryForm(),me.getFundDetailGrid()]
        });
        me.callParent(arguments);
    }
});