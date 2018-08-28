/**
 * 分仓请求
 */
Ext.define('MyApp.view.splitRepositoryRequestManager', {
    extend: 'Ext.panel.Panel',
    id:'splitRepositoryRequestManager',
    closable: true,
    title: '分仓管理',
    autoScroll: false,
    layout: "border",

    getToolbar: function () {
        var me = this;
        if (Ext.isEmpty(me.toolbar)) {
            me.toolbar = Ext.widget('toolbar', {
                dock: 'top',
                items: [{
                    text: '明细',
                    listeners: {
                        click: {
                            fn: me.showSplitRepository,
                            scope: me
                        }
                    }
                },{
                    text: '导出',
                    handler: function () {
                        me.exportPurchaseGameAccount();
                    }
                }
                ]
            });
        }
        return me.toolbar;
    },
    showSplitRepository: function (button, e, eOpts) {
        var grid = this.getSplitRepositoryRequestGrid(),
            selModel = grid.getSelectionModel(),
            selRecords = selModel.getSelection(),
            window = this.getSplitRepositoryRequestWindow();
        if (selRecords == null || selRecords.length <= 0) {
            Ext.ux.Toast.msg("温馨提示", "请先选择收货订单记录");
            return;
        }
        record = selRecords[0];
        window.bindData(record);
        window.show();
    },
    listeners:{
        'resize':function(){
            this.splitRepositoryRequestGrid.setHeight(window.document.body.offsetHeight-235);
        }
    },
    splitRepositoryRequestWindow: null,
    getSplitRepositoryRequestWindow: function(){
        if(this.splitRepositoryRequestWindow == null){
            this.splitRepositoryRequestWindow = new MyApp.view.splitRepositoryWindow();
        }
        return this.splitRepositoryRequestWindow;
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
                    fieldLabel: '订单号',
                    name: 'orderId'
                },DataDictionary.getDataDictionaryCombo('splitRepositoryRequestStatus',{
                    fieldLabel: '状态',
                    labelWidth: 90,
                    name: 'status',
                    editable: false,
                    value:-2
                }),{
                    fieldLabel: '收货方账号',
                    name: 'buyerAccount'
                },{
                    fieldLabel: '游戏账号',
                    name: 'gameAccount'
                },{
                    fieldLabel: '游戏角色名',
                    name: 'gameRole'
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
            me.store = Ext.create('MyApp.store.SplitRepositoryRequestStore',{
                autoLoad: true,
                listeners: {
                    beforeload : function(store, operation, eOpts) {
                        var queryForm = me.getQueryForm();
                        if (queryForm != null) {
                            var values = queryForm.getValues();
                            Ext.apply(operation, {
                                params: {
                                    'splitRepositoryRequest.orderId': Ext.String.trim(values.orderId),
                                    'startTime':values.createStartTime,
                                    'endTime':values.createEndTime,
                                    'splitRepositoryRequest.buyerAccount': Ext.String.trim(values.buyerAccount),
                                    'splitRepositoryRequest.gameAccount': Ext.String.trim(values.gameAccount),
                                    'splitRepositoryRequest.fmsRoleName': Ext.String.trim(values.gameRole),
                                    'splitRepositoryRequest.gameRole': Ext.String.trim(values.gameRole),
                                    'splitRepositoryRequest.status':values.status
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
    splitRepositoryRequestGrid: null,
    getSplitRepositoryRequestGrid: function(){
        var me = this;
        if(Ext.isEmpty(me.splitRepositoryRequestGrid)){
            me.splitRepositoryRequestGrid = Ext.widget('gridpanel',{
                region: 'center',
                header: false,
                columnLines: true,
                store: me.getStore(),
                columns: [{
                    xtype: 'rownumberer'
                },{
                    dataIndex: 'orderId',
                    text: '订单号',
                    flex: 0.7,
                    align: 'center'
                },{
                    dataIndex: 'buyerAccount',
                    text: '收货方账号',
                    flex: 0.7,
                    align: 'center'
                },{
                    dataIndex: 'status',
                    text: '状态 ',
                    flex: 0.8,
                    sortable: false,
                    align: 'center',
                    renderer: function(value, metaData, record, rowIndex, colIndex, store, view) {
                        return DataDictionary.rendererSubmitToDisplay(value,'splitRepositoryRequestStatus');
                    }
                },{
                    dataIndex: 'gameName',
                    text: '游戏名',
                    flex: 0.7,
                    align: 'center'
                },{
                    dataIndex: 'region',
                    text: '游戏区',
                    flex: 0.7,
                    align: 'center'
                },{
                    dataIndex: 'server',
                    text: '游戏服',
                    flex: 0.7,
                    align: 'center'
                },{
                    dataIndex: 'gameRace',
                    text: '游戏阵营',
                    flex: 0.7,
                    align: 'center'
                },{
                    dataIndex: 'gameAccount',
                    text: '游戏账号',
                    flex: 0.7,
                    align: 'center'
                },{
                    dataIndex: 'gameRole',
                    text: '游戏角色名',
                    flex: 0.7,
                    align: 'center'
                },{
                    dataIndex: 'realCount',
                    text: '分仓总数量(万金)',
                    flex: 0.7,
                    align: 'center'
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
                    dataIndex: 'updateTime',
                    sortable: false,
                    flex: 1.5,
                    align: 'center',
                    text: '更新时间 '
                }],
                dockedItems: [me.getToolbar(),me.getPagingToolbar()],
                selModel: Ext.create('Ext.selection.CheckboxModel', {
                    allowDeselect: true,
                    mode: 'MULTI'
                })
            });
        }
        return me.splitRepositoryRequestGrid;

    },

    exportPurchaseGameAccount: function (button, e, eOpts) {
        var me = this;
        var queryForm = me.getQueryForm();

        if (queryForm != null) {
            var values = queryForm.getValues();
            var params = {
                'startTime':values.createStartTime,
                'endTime':values.createEndTime,
                'splitRepositoryRequest.orderId': Ext.String.trim(values.orderId),
                'splitRepositoryRequest.buyerAccount': Ext.String.trim(values.buyerAccount),
                'splitRepositoryRequest.gameAccount': Ext.String.trim(values.gameAccount),
                'splitRepositoryRequest.fmsRoleName': Ext.String.trim(values.gameRole),
                'splitRepositoryRequest.gameRole': Ext.String.trim(values.gameRole),
                'splitRepositoryRequest.status':values.status
            };
            var p = "";
            for (var key in params) {
                var value = "";
                if (!Ext.isEmpty(params[key]))
                    value = params[key];
                p += key + "=" + value + "&";
            }
            //window.open('./fund/exportFundExcel.action');
            window.open('./delivery/exportFundExcel.action?' + p);
        }

    },
    initComponent: function() {
        var me = this;
        Ext.applyIf(me, {
            items: [me.getQueryForm(),me.getSplitRepositoryRequestGrid()]
        });
        me.callParent(arguments);
    },

});


//订单明细
Ext.define('MyApp.view.splitRepositoryWindow', {
    extend: 'Ext.window.Window',
    title: '分仓订单明细',
    width: 1500,
    border: false,
    padding: '10 10 10 10',
    autoScroll: true,
    closeAction: 'hide',
    modal: true,
    form: null,
    getForm: function () {
        var me = this;
        if (me.form == null) {
            me.form = Ext.create('MyApp.view.splitRepositoryForm');
        }
        return me.form;
    },
    grid: null,
    getGrid: function () {
        var me = this;
        if (Ext.isEmpty(me.grid)) {
            var store = Ext.create('Ext.data.Store', {
                model: 'MyApp.model.SplitRepositorySubRequestModel',
                proxy: {
                    type: 'ajax',
                    actionMethods: 'POST',
                    url: './delivery/querySplitRepositprySubRequest.action',
                    reader: {
                        type: 'json',
                        root: 'splitRepositorySubRequestList'
                    }
                }
            });
            me.grid = Ext.widget('gridpanel', {
                header: false,
                columnLines: true,
                store: store,
                columns: [
                    {
                        dataIndex: 'id',
                        text: '子订单id',
                        sortable: false,
                        align: 'center',
                        flex: 1
                    }
                    ,{
                        dataIndex: 'orderId',
                        text: '分仓主订单',
                        sortable: false,
                        align: 'center',
                        flex: 1
                    },{
                        dataIndex: 'gameRole',
                        text: '被分仓角色',
                        sortable: false,
                        align: 'center',
                        flex: 1
                    },{
                        dataIndex: 'useRepertoryCount',
                        text: '使用帐号金库剩余金币(万金)',
                        sortable: false,
                        align: 'center',
                        flex: 1
                    },{
                        dataIndex: 'count',
                        text: '缺口值(万金)',
                        sortable: false,
                        align: 'center',
                        flex: 1
                    },{
                        dataIndex: 'realCount',
                        text: '实际分仓数量(万金)',
                        sortable: false,
                        align: 'center',
                        flex: 1
                    }, {
                        dataIndex: 'status',
                        text: '订单状态 ',
                        flex: 1,
                        sortable: false,
                        align: 'center',
                        renderer: function (value, metaData, record, rowIndex, colIndex, store, view) {
                                return DataDictionary.rendererSubmitToDisplay(value, 'splitRepositoryRequestStatus');
                            
                        }
                    },{
                        dataIndex: 'robotOtherReason',
                        text: '分仓原因',
                        sortable: false,
                        align: 'center',
                        flex: 1
                    }, {
                        xtype: 'datecolumn',
                        format: 'Y-m-d H:i:s',
                        dataIndex: 'createTime',
                        sortable: false,
                        flex: 1.5,
                        align: 'center',
                        text: '创建时间 '
                    },
                    {
                        xtype: 'datecolumn',
                        format: 'Y-m-d H:i:s',
                        dataIndex: 'updateTime',
                        sortable: false,
                        flex: 1.5,
                        align: 'center',
                        text: '更新时间'
                    }
                ]
            });
        }
        return me.grid;
    },
    bindData: function (record) {
        var me = this,
            store = me.getGrid().getStore(),
            form = me.getForm().getForm();
        form.reset();
        form.loadRecord(record);
        store.load({
            params: {
                'splitRepositorySubRequest.orderId': record.get('orderId')
            }
        });
    },
    initComponent: function () {
        var me = this;
        Ext.applyIf(me, {
            items: [me.getForm(), me.getGrid()]
        });
        me.callParent(arguments);
    }
});


//订单明细内容
Ext.define('MyApp.view.splitRepositoryForm', {
    extend: 'Ext.form.Panel',
    layout: 'column',
    border: false,
    defaults: {
        margin: '5 5 5 5',
        columnWidth: .333,
        labelWidth: 100,
        readOnly: true,
        xtype: 'textfield'
    },
    constructor: function (config) {
        var me = this, cfg = Ext.apply({}, config);
        // configInfoList = [];
        me.items = [{
            name: 'orderId',
            fieldLabel: '订单号'
        }, {
            name: 'buyerAccount',
            fieldLabel: '收货方账号'
        }, DataDictionary.getDataDictionaryCombo('splitRepositoryRequestStatus', {
            fieldLabel: '订单状态',
            readOnly: true,
            name: 'status'
        }), {
            name: 'gameName',
            fieldLabel: '游戏名称'
        }, {
            name: 'region',
            fieldLabel: '游戏区'
        }, {
            name: 'server',
            fieldLabel: '游戏服'
        }, {
            name: 'gameRace',
            fieldLabel: '游戏战营'
        }, {
            name: 'gameAccount',
            fieldLabel: '游戏账号'
        }, {
            name: 'gameRole',
            fieldLabel: '游戏角色'
        }, {
            name: 'realCount',
            fieldLabel: '分仓总数量(万金)'
        }, {
            name: 'updateTime',
            xtype: 'datefield',
            format: 'Y-m-d H:i:s',
            fieldLabel: '更新时间'
        }];
        
        me.callParent([cfg]);
    }

});