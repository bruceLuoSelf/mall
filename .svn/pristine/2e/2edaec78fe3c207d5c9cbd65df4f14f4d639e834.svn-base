//采购单管理页面
Ext.define('MyApp.view.shPurchaseOrderManager', {
    extend: 'Ext.panel.Panel',
    layout: "border",
    id: 'shPurchaseOrderManager',
    closable: true,
    title: '采购单管理',
    toolbar: null,
    getToolbar: function(){
        var me = this;
        if(Ext.isEmpty(me.toolbar)){
            me.toolbar = Ext.widget('toolbar',{
                dock: 'top',
                items: [{
                    text: '上架',
                    listeners: {
                        click: {
                            fn: me.online,
                            scope: me
                        }
                    }
                },{
                    text: '下架',
                    listeners:{
                        click: {
                            fn: me.offline,
                            scope: me
                        }
                    }
                }]
            });
        }
        return me.toolbar;
    },
    online: function() {
        var me = this,
            grid = me.getRefundOrderGrid().getSelectionModel(),
            selRecords = grid.getSelection();
        if (selRecords == null || selRecords.length <= 0) {
            Ext.ux.Toast.msg("温馨提示", "请先选择要上架的采购单");
            return;
        }
        var record = selRecords[0];
        if (record.get('isOnline')) {
            Ext.ux.Toast.msg("温馨提示", "该采购单已经上架！");
            return;
        }
        Ext.MessageBox.confirm('温馨提示', '确定要上架该采购单？', function(btn){
            if(btn == 'yes'){
                Ext.Ajax.request({
                    url : './shpurchase/online.action',
                    params: {
                        'id':selRecords[0].get("id"),
                    },
                    success : function(response, opts) {
                        Ext.ux.Toast.msg("温馨提示", "上架成功！");
                        grid.getStore().load();
                        grid.getSelectionModel().deselectAll();
                    },
                    exception : function(response) {
                        var json = Ext.decode(response.responseText);
                        Ext.ux.Toast.msg("温馨提示", json.responseStatus.message);
                    }
                });
            }else{
                return;
            }
        });
    },
    offline: function() {
        var me = this,
            grid = me.getRefundOrderGrid().getSelectionModel(),
            selRecords = grid.getSelection();
        if (selRecords == null || selRecords.length <= 0) {
            Ext.ux.Toast.msg("温馨提示", "请先选择要下架的采购单");
            return;
        }
        var record = selRecords[0];
        if (!record.get('isOnline')) {
            Ext.ux.Toast.msg("温馨提示", "该采购单已经下架！");
            return;
        }
        Ext.MessageBox.confirm('温馨提示', '确定要下架该采购单？', function(btn){
            if(btn == 'yes'){
                Ext.Ajax.request({
                    url : './shpurchase/offline.action',
                    params: {
                        'id':selRecords[0].get("id"),
                    },
                    success : function(response, opts) {
                        Ext.ux.Toast.msg("温馨提示", "下架成功！");
                        grid.getStore().load();
                        grid.getSelectionModel().deselectAll();
                    },
                    exception : function(response) {
                        var json = Ext.decode(response.responseText);
                        Ext.ux.Toast.msg("温馨提示", json.responseStatus.message);
                    }
                });
            }else{
                return;
            }
        });
    },
    queryForm: null,
    getQueryForm: function () {
        var me = this;
        if (me.queryForm == null) {
            me.queryForm = Ext.widget('form', {
                layout: 'column',
                region: 'north',
                defaults: {
                    margin: '10 10 10 10'
                },
                items: [{
                    fieldLabel: '创建日期',
                    columnWidth: .5,
                    labelWidth: 80,
                    xtype: 'rangedatefield',
                    fromName: 'createStartTime',
                    toName: 'createEndTime',
                    fromValue: new Date(new Date()-30*24*60*60*1000),
                    toValue: new Date()
                }, {
                    xtype: 'textfield',
                    columnWidth: .2,
                    labelWidth: 80,
                    fieldLabel: '游戏名',
                    name: 'gameName'
                }, {
                    xtype: 'textfield',
                    columnWidth: .2,
                    labelWidth: 80,
                    fieldLabel: '收货方账号',
                    name: 'buyerAccount'
                },DataDictionary.getDataDictionaryCombo('theDeliveryType',{
                    fieldLabel: '收货方式',
                    labelWidth: 100,
                    name: 'deliveryType',
                    editable:false,
                    value:0
                }),DataDictionary.getDataDictionaryCombo('isOnline',{
                    fieldLabel: '是否上架',
                    labelWidth: 100,
                    name: 'isOnline',
                    editable: false,
                    value: "-1"
                })
                ],
                buttons: [{
                    text: '查询',
                    handler: function () {
                        me.getPagingToolbar().moveFirst();
                    }
                }, '-', {
                    text: '重置',
                    handler: function () {
                        me.getQueryForm().getForm().reset();
                    }
                }, '->']
            });
        }
        return this.queryForm;
    },
    
    store: null,
    getStore: function(){
        var me = this;
        if(me.store==null){
            me.store = Ext.create('MyApp.store.shPurchaseOrderStore',{
                autoLoad: true,
                listeners: {
                    beforeload: function (store, operation, eOpts) {
                        var queryForm = me.getQueryForm();
                        if (queryForm != null) {
                            var values = queryForm.getValues();
                            Ext.apply(operation, {
                                params: {
                                    'purchaseOrder.gameName': Ext.String.trim(values.gameName),
                                    'purchaseOrder.buyerAccount': Ext.String.trim(values.buyerAccount),
                                    'sTime':values.createStartTime,
                                    'eTime':values.createEndTime,
                                    'purchaseOrder.deliveryType': values.deliveryType,
                                    'isOnline':values.isOnline,
                                }
                            })
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
    purchaseGrid: null,
    getRefundOrderGrid: function(){
        var me = this;
        if(Ext.isEmpty(me.refundOrderGrid)){
            me.refundOrderGrid = Ext.widget('gridpanel',{
                header: false,
                region: 'center',
                columnLines: true,
                store: me.getStore(),
                columns: [{
                    xtype: 'rownumberer'
                },{
                    dataIndex: 'buyerAccount',
                    text: '收货方5173账号',
                    flex:  1.7,
                    align: 'center'
                },{
                    dataIndex: 'gameName',
                    text: '游戏名',
                    flex:  1.7,
                    align: 'center'
                },{
                    dataIndex: 'region',
                    text: '游戏区',
                    flex:  1.7,
                    align: 'center'
                },{
                    dataIndex: 'server',
                    text: '游戏服',
                    flex:  1.7,
                    align: 'center'
                },{
                    dataIndex: 'gameRace',
                    text: '游戏阵营',
                    flex:  1.7,
                    align: 'center'
                },{
                    dataIndex: 'count',
                    text: '收货数量',
                    flex:  1.7,
                    align: 'center'
                },{
                    dataIndex: 'price',
                    text: '收货单价',
                    flex:  1.7,
                    align: 'center'
                },{
                    dataIndex: 'minCount',
                    text: '单笔最小收货量',
                    flex:  1.7,
                    align: 'center'
                },{
                    dataIndex: 'isOnline',
                    text: '是否上架',
                    flex:  1.7,
                    align: 'center',
                    sortable: false,
                    flex: 1,
                    renderer: function (value, metaData, record, rowIndex, colIndex, store, view) {
                        return DataDictionary.rendererSubmitToDisplay(value, 'check');
                    }
                },{
                    xtype: 'datecolumn',
                    dataIndex: 'updateTime',
                    text: '更新时间',
                    flex:  1.7,
                    align: 'center',
                    format:'Y-m-d H:i:s',
                    sortable: false,
                },{
                    dataIndex: 'buyerUid',
                    text: '收货方5173UID',
                    flex:  1.7,
                    align: 'center'
                },{
                    dataIndex: 'deliveryType',
                    text: '收货方式 ',
                    flex: 1.7,
                    sortable: false,
                    align: 'center',
                    renderer: function(value, metaData, record, rowIndex, colIndex, store, view) {
                        if(value!=0){
                            return DataDictionary.rendererSubmitToDisplay(value,'theDeliveryType');
                        }else{
                            return '';
                        }
                    }
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