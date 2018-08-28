

Ext.define('MyApp.view.DetailPayWindow',{
    extend: 'Ext.window.Window',
    title: '付款明细',
    width: 800,
    border: false,
    padding: '10 10 10 10',
    autoScroll: true,
    closeAction: 'hide',
    modal: true,
    grid: null,
    getGrid: function(){
        var me=this;
        if(Ext.isEmpty(me.grid)){
            var store=Ext.create('Ext.data.Store',{
                model: 'MyApp.model.PayDetailModel',
                proxy: {
                    type:'ajax',
                    actionMethods: 'POST',
                    url: './delivery/queryPayDetail.action',
                    reader: {
                        type: 'json',
                        root: 'payDetailList'
                    }
                }
            });
            me.grid=Ext.widget('gridpanel',{
                header: false,
                columnLines: true,
                store: store,
                columns:[{
                    dataIndex:'orderId',
                    text:'付款明细订单号',
                    sortable: false,
                    align:'center',
                    flex: 1
                },{
                    dataIndex:'payOrderId',
                    text:'充值订单号',
                    sortable: false,
                    align:'center',
                    flex: 1
                },{
                    dataIndex:'chOrderId',
                    text:'出货订单号',
                    sortable: false,
                    align:'center',
                    flex: 1
                },{
                    dataIndex:'amount',
                    text:'金额',
                    sortable: false,
                    align:'center',
                    flex: 1
                },{
                    xtype: 'datecolumn',
                    format:'Y-m-d H:i:s',
                    dataIndex: 'createTime',
                    sortable: false,
                    flex: 1.5,
                    align: 'center',
                    text: '创建时间 '
                }]
            });
        }
        return me.grid;
    },
    bindData: function(record){
        var me = this,
            store = me.getGrid().getStore();
            store.load({
            params:{
                'payDetail.payOrderId': record.get('orderId')
            }
        });
    },
    initComponent: function() {
        var me= this;
        Ext.applyIf(me, {
            items:[me.getGrid()]
        });
        me.callParent(arguments);
    }
});



Ext.define('MyApp.view.shRechargeManager', {
    extend: 'Ext.panel.Panel',
    id:'shRechargeManager',
    closable: true,
    layout:"border",
    title: '充值明细管理',
    autoScroll: false,
    toolbar: null,
    getToolbar: function(){
        var me = this;
        if(Ext.isEmpty(me.toolbar)){
            me.toolbar=Ext.widget('toolbar',{
                dock: 'top',
                items: [{
                    text: '付款明细',
                    listeners:{
                        click: {
                            fn: me.showPayDetail,
                            scope: me
                        }
                    }
                },{
                    text: '人工补单',
                    listeners:{
                        click: {
                            fn: me.manualPayShOrder,
                            scope: me
                        }
                    }
                }]
            });
        }
        return me.toolbar
    },
    manualPayShOrder: function(button,e,eOpts){
        var grid =this.getPayOrderGrid(),
            selModel = grid.getSelectionModel(),
            selRecords = selModel.getSelection(),record;
        var me = this;
        if(selRecords == null||selRecords.length<=0){
            Ext.ux.Toast.msg("温馨提示","请先选择订单");
            return;
        }
        record=selRecords[0];
        if(record.data['status']!=0){
            Ext.ux.Toast.msg("温馨提示","只有待支付的订单才可以人工补单");
            return;
        }

        Ext.Ajax.request({
            url: './delivery/manualPayShOrder.action',
            method: 'POST',
            params: {
                'payOrder.orderId': record.data['orderId']
            },
            success: function (response, opts) {
                var store = me.getStore();
                Ext.ux.Toast.msg("温馨提示", "人工补单成功");
                store.load();
            },
            exception: function (response, opts) {
                var json = Ext.decode(response.responseText);
                Ext.ux.Toast.msg("温馨提示", json.message);
            }
        });
    },
    showPayDetail: function(button,e,eOpts){
        var grid =this.getPayOrderGrid(),
            selModel = grid.getSelectionModel(),
            selRecords = selModel.getSelection(),record,
            window= this.getDetailPayWindow();
        if(selRecords == null||selRecords.length<=0){
            Ext.ux.Toast.msg("温馨提示","请先选择要查询的订单");
            return;
        }
        record=selRecords[0];
        window.show();
        window.bindData(record);
    },
    DetailPayWindow: null,
    getDetailPayWindow: function(){
        var me = this;
        if(me.DetailPayWindow==null){
            me.DetailPayWindow=Ext.create('MyApp.view.DetailPayWindow');
        }
        return me.DetailPayWindow;
    },
    queryForm: null,
    getQueryForm: function(){
        var me = this;
        if(me.queryForm==null){
            me.queryForm = Ext.widget('form',{
                layout: 'column',
                region:'north',
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
                    fieldLabel: '充值单号',
                    name: 'orderId'
                },{
                    fieldLabel: '收货方5173UID',
                    name: 'uid'
                },{
                    fieldLabel: '收货方5173账号',
                    name: 'account'
                },DataDictionary.getDataDictionaryCombo('statusOfPayOrder',{
                    fieldLabel: '状态',
                    labelWidth: 100,
                    name: 'status',
                    editable: false,
                    value: -1
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
            me.store = Ext.create('MyApp.store.PayOrderStore',{
                autoLoad: true,
                listeners: {
                    beforeload : function(store,operation,eOpts) {
                        var queryForm = me.getQueryForm();
                        if (queryForm != null) {
                            var values = queryForm.getValues();
                            Ext.apply(operation, {
                                params: {
                                    'payOrder.orderId': Ext.String.trim(values.orderId),
                                    'startTime':values.createStartTime,
                                    'endTime':values.createEndTime,
                                    'payOrder.uid': Ext.String.trim(values.uid),
                                    'payOrder.account': Ext.String.trim(values.account),
                                    'payOrder.status': values.status
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
    payOrderGrid: null,
    getPayOrderGrid: function(){
        var me = this;
        if(Ext.isEmpty(me.payOrderGrid)){
            me.payOrderGrid = Ext.widget('gridpanel',{
                header: false,
                columnLines: true,
                region:'center',
                store: me.getStore(),
                columns: [{
                    xtype: 'rownumberer'
                },{
                    dataIndex: 'orderId',
                    text: '充值单号',
                    flex: 1.2,
                    align: 'center'
                },{
                    dataIndex: 'uid',
                    text: '收货方5173UID',
                    flex: 1.2,
                    align: 'center'
                },{
                    dataIndex: 'account',
                    text: '收货方5173账号',
                    flex: 1.2,
                    align: 'center'
                },{
                    dataIndex: 'amount',
                    text: '充值金额',
                    flex: 1,
                    align: 'center'
                },{
                    dataIndex: 'usedAmount',
                    text: '已用金额',
                    flex: 1,
                    align: 'center'
                },{
                    dataIndex: 'balance',
                    text: '剩余金额',
                    flex: 1,
                    align: 'center'
                },{
                    dataIndex: 'status',
                    text: '状态 ',
                    flex: 0.7,
                    sortable: false,
                    align: 'center',
                    renderer: function(value, metaData, record, rowIndex, colIndex, store, view) {
                        if(value!=-1){
                            return DataDictionary.rendererSubmitToDisplay(value,'statusOfPayOrder');
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
                    text: '创建时间 '
                },{
                    xtype: 'datecolumn',
                    format:'Y-m-d H:i:s',
                    dataIndex: 'payTime',
                    sortable: false,
                    flex: 1.5,
                    align: 'center',
                    text: '支付时间 '
                },{
                    xtype: 'datecolumn',
                    format:'Y-m-d H:i:s',
                    dataIndex: 'lastUpdateTime',
                    sortable: false,
                    flex: 1.5,
                    align: 'center',
                    text: '最后更新时间 '
                }],
                dockedItems: [me.getToolbar(),me.getPagingToolbar()],
                selModel: Ext.create('Ext.selection.CheckboxModel', {
                    allowDeselect: true,
                    mode: 'SINGLE'
                })
            });
        }
        return me.payOrderGrid;

    },
    initComponent: function() {
        var me = this;
        Ext.applyIf(me, {
            items: [me.getQueryForm(),me.getPayOrderGrid()]
        });
        me.callParent(arguments);
    }
});