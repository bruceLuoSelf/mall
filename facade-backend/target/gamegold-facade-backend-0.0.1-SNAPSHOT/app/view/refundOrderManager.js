Ext.define('MyApp.view.refundOrderManager', {
    extend: 'Ext.panel.Panel',
    id:'refundOrderManager',
    closable: true,
    title: '退款审核',
    autoScroll: false,
    layout: "border",
    toolbar: null,
    getToolbar: function(){
        var me = this;
        if(Ext.isEmpty(me.toolbar)){
            me.toolbar = Ext.widget('toolbar',{
                dock: 'top',
                items: [{
                    text: '审核通过',
                    handler: function(){
                        me.auditRefund(3);
                    }
                },'-',{
                    text: '审核不通过',
                    handler: function(){
                        me.auditRefund(2);
                    }
                }]
            });
        }
        return me.toolbar;
    },
    auditRefund: function(audit){
        var grid = this.getRefundOrderGrid(),
            selModel = grid.getSelectionModel(),
            selRecords = selModel.getSelection();
        if(selRecords == null||selRecords.length<=0){
            Ext.ux.Toast.msg("温馨提示", "请先选择审核的记录");
            return;
        }
        var record = selRecords[0];
        if(record.get("status")==2){
            Ext.ux.Toast.msg("温馨提示", "该记录审核状态已为不通过，无法继续审核");
            return;
        }
        else if(record.get("status")==3){
            Ext.ux.Toast.msg("温馨提示", "该记录审核状态已为已退款，无法继续审核");
            return;
        }

        Ext.MessageBox.confirm('温馨提示', '确定审核该记录？', function(btn){
            if(btn == 'yes'){
                Ext.Ajax.request({
                    url : './delivery/auditRefund.action',
                    params:{'refundOrder.id':record.get('id'),'refundOrder.status':audit},
                    success : function(response, opts) {
                        Ext.ux.Toast.msg("温馨提示", "操作成功！");
                        grid.getStore().load();
                        grid.getSelectionModel().deselectAll();
                    },
                    exception : function(response, opts) {
                        var json = Ext.decode(response.responseText);
                        Ext.ux.Toast.msg("温馨提示", json.message);
                    }
                });
            }else{
                return;
            }
        });
    },
    refundOrderWindow: null,
    getRefundOrderWindow: function(){
        if(this.refundOrderWindow == null){
            this.refundOrderWindow = new MyApp.view.RefundOrderWindow();
        }
        return this.refundOrderWindow;
    },
    queryForm: null,
    getQueryForm: function(){
        var me = this;
        if(me.queryForm==null){
            me.queryForm = Ext.widget('form',{
                layout: 'column',
                region: 'north',
                defaults: {
                    margin: '10 10 10 10',
                    xtype: 'textfield'
                },
                items: [{
                    fieldLabel: '申请时间',
                    xtype: 'rangedatefield',
                    fromName: 'createStartTime',
                    toName: 'createEndTime',
                    fromValue: new Date(new Date()-30*24*60*60*1000),
                    toValue: new Date()
                },{
                    fieldLabel: '退款单号',
                    name: 'orderId'
                },{
                    fieldLabel: '充值单号',
                    name: 'payOrderId'
                },{
                        fieldLabel: '申请人账号',
                        name: 'buyerAccount'
                    },{
                        fieldLabel: '申请人UID',
                        name: 'uid'
                    },{
                        fieldLabel: '申请人电话',
                        name: 'phone'
                    },{
                        fieldLabel: '申请人姓名',
                        name: 'name'
                    },{
                        fieldLabel: '申请人QQ',
                        name: 'qq'
                    },DataDictionary.getDataDictionaryCombo('refundOrderStatus',{
                    fieldLabel: '订单类型',
                    labelWidth: 100,
                    name: 'status',
                    value:0,
                    editable: false
                },{
                    value:0,
                    display:'全部'
                })],
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
            me.store = Ext.create('MyApp.store.RefundOrderStore',{
                autoLoad: true,
                listeners: {
                    beforeload : function(store, operation, eOpts) {
                        var queryForm = me.getQueryForm();
                        if (queryForm != null) {
                            var values = queryForm.getValues();
                            Ext.apply(operation, {
                                params: {
                                    'refundOrder.orderId': Ext.String.trim(values.orderId),
                                    'startTime':values.createStartTime,
                                    'endTime':values.createEndTime,
                                    'refundOrder.payOrderId': Ext.String.trim(values.payOrderId),
                                    'refundOrder.buyerAccount': Ext.String.trim(values.buyerAccount),
                                    'refundOrder.uid': Ext.String.trim(values.uid),
                                    'refundOrder.phone': Ext.String.trim(values.phone),
                                    'refundOrder.name': Ext.String.trim(values.name),
                                    'refundOrder.qq': Ext.String.trim(values.qq),
                                    'refundOrder.status': values.status
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
    refundOrderGrid: null,
    getRefundOrderGrid: function(){
        var me = this;
        if(Ext.isEmpty(me.refundOrderGrid)){
            me.refundOrderGrid = Ext.widget('gridpanel',{
                header: false,
                columnLines: true,
                region: 'center',
                store: me.getStore(),
                columns: [{
                    xtype: 'rownumberer'
                },{
                    dataIndex: 'orderId',
                    text: '退款单号',
                    flex:  1.7,
                    align: 'center'
                },{
                    dataIndex: 'payOrderId',
                    text: '充值单号',
                    flex:  1.7,
                    align: 'center'
                },{
                    dataIndex: 'buyerAccount',
                    text: '申请人账号',
                    flex:  1.2,
                    align: 'center'
                },{
                    dataIndex: 'phone',
                    text: '电话',
                    flex:  1.2,
                    align: 'center'
                },{
                    dataIndex: 'name',
                    text: '姓名',
                    flex:  1,
                    align: 'center'
                },{
                    dataIndex: 'qq',
                    text: 'QQ',
                    flex:  1.2,
                    align: 'center'
                },{
                    dataIndex: 'refundAmount',
                    text: '退款金额',
                    flex: 0.7,
                    align: 'center'
                },{
                    xtype: 'linebreakcolumn',
                    dataIndex: 'reason',
                    text: '退款理由',
                    flex: 3.0,
                    align: 'center'
                },{
                    dataIndex: 'status',
                    text: '订单状态 ',
                    flex: 1,
                    sortable: false,
                    align: 'center',
                    renderer: function(value, metaData, record, rowIndex, colIndex, store, view) {
                        if(value!=0){
                            return DataDictionary.rendererSubmitToDisplay(value,'refundOrderStatus');
                        }else{
                            return '';
                        }
                    }
                },{
                    xtype: 'datecolumn',
                    format:'Y-m-d H:i:s',
                    dataIndex: 'createTime',
                    sortable: false,
                    flex: 1.5,
                    align: 'center',
                    text: '申请时间 '
                },{
                    dataIndex: 'auditor',
                    text: '审核人',
                    flex: 1.2,
                    align: 'center'
                },{
                    xtype: 'datecolumn',
                    format:'Y-m-d H:i:s',
                    dataIndex: 'auditTime',
                    sortable: false,
                    flex: 1.5,
                    align: 'center',
                    text: '审核时间 '
                },{
                    xtype: 'datecolumn',
                    format:'Y-m-d H:i:s',
                    dataIndex: 'finishTime',
                    sortable: false,
                    flex: 1.5,
                    align: 'center',
                    text: '退款时间 '
                }],
                dockedItems: [me.getToolbar(),me.getPagingToolbar()],
                selModel: Ext.create('Ext.selection.CheckboxModel', {
                    allowDeselect: true,
                    mode: 'MULTI'
                })
            });
        }
        return me.refundOrderGrid;

    },
    initComponent: function() {
        var me = this;
        Ext.applyIf(me, {
            items: [me.getQueryForm(),me.getRefundOrderGrid()]
        });
        me.callParent(arguments);
    }
});