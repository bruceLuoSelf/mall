/**
 * 分仓日志
 */
Ext.define('MyApp.view.splitRepositoryLogManager', {
    extend: 'Ext.panel.Panel',
    layout: "border",
    id:'splitRepositoryLogManager',
    closable: true,
    title: '分仓日志',
    autoScroll: false,
    listeners:{
        'resize':function(){
            this.splitRepositoryLogGrid.setHeight(window.document.body.offsetHeight-290);
        }
    },
    splitRepositoryLogWindow: null,
    getSplitRepositoryLogWindow: function(){
        if(this.splitRepositoryLogWindow == null){
            this.splitRepositoryLogWindow = new MyApp.view.SplitRepositoryLogWindow();
        }
        return this.splitRepositoryLogWindow;
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
                    fieldLabel: '创建日期',
                    xtype: 'rangedatefield',
                    fromName: 'startCreateTime',
                    toName: 'endCreateTime',
                    fromValue: new Date(new Date()-30*24*60*60*1000),
                    toValue: new Date()
                },{
                    fieldLabel: '主订单号',
                    name: 'fcOrderId'
                },{
                    fieldLabel: '收购方账号',
                    name: 'buyerAccount'
                },{
                    fieldLabel: '游戏账号',
                    name: 'gameAccount'
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
            me.store = Ext.create('MyApp.store.SplitRepositoryLogStore',{
                autoLoad: true,
                listeners: {
                    beforeload : function(store, operation, eOpts) {
                        var queryForm = me.getQueryForm();
                        if (queryForm != null) {
                            var values = queryForm.getValues();
                            Ext.apply(operation, {
                                params: {
                                    'splitRepositoryLog.fcOrderId': Ext.String.trim(values.fcOrderId),
                                    'startTime':values.startCreateTime,
                                    'endTime':values.endCreateTime,
                                    'splitRepositoryLog.buyerAccount': Ext.String.trim(values.buyerAccount),
                                    'splitRepositoryLog.gameAccount': Ext.String.trim(values.gameAccount)
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
    splitRepositoryLogGrid: null,
    getSplitRepositoryLogGrid: function(){
        var me = this;
        if(Ext.isEmpty(me.splitRepositoryLogGrid)){
            me.splitRepositoryLogGrid = Ext.widget('gridpanel',{
                header: false,
                region: 'center',
                columnLines: true,
                store: me.getStore(),
                columns: [{
                    xtype: 'rownumberer'
                },{
                    dataIndex: 'gameName',
                    text: '游戏',
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
                    dataIndex: 'buyerAccount',
                    text: '收购方',
                    flex: 0.7,
                    align: 'center'
                },{
                    dataIndex: 'gameAccount',
                    text: '游戏账号',
                    flex: 0.7,
                    align: 'center'
                },{
                    xtype: 'linebreakcolumn',
                    dataIndex: 'log',
                    text: '日志内容',
                    flex: 0.7,
                    align: 'center'
                },{
                    dataIndex: 'imgPath',
                    text: '图片路径',
                    flex: 0.9,
                    align: 'center'
                },{
                    dataIndex: 'roleName',
                    text: '角色名称',
                    flex: 0.9,
                    align: 'center'
                }, {
                    dataIndex: 'logType',
                    text: '收入支出明细 ',
                    flex: 0.9,
                    sortable: false,
                    align: 'center',
                    renderer: function (value, metaData, record, rowIndex, colIndex, store, view) {
                        return DataDictionary.rendererSubmitToDisplay(value, 'SplitRepositoryLogType');

                    }
                },{
                    dataIndex: 'fcOrderId',
                    text: '主订单',
                    flex: 0.9,
                    align: 'center'
                },{
                    dataIndex: 'count',
                    text: '分仓数量',
                    flex: 0.9,
                    align: 'center'
                }, {
                    dataIndex: 'incomeType',
                    text: '收入支出类型 ',
                    flex: 0.9,
                    sortable: false,
                    align: 'center',
                    renderer: function (value, metaData, record, rowIndex, colIndex, store, view) {
                        return DataDictionary.rendererSubmitToDisplay(value, 'IncomeType');

                    }
                },{
                    xtype: 'datecolumn',
                    format:'Y-m-d H:i:s',
                    dataIndex: 'createTime',
                    sortable: false,
                    flex: 1.5,
                    align: 'center',
                    text: '创建时间 '
                }],
                dockedItems: [me.getPagingToolbar()],
                selModel: Ext.create('Ext.selection.CheckboxModel', {
                    allowDeselect: true,
                    mode: 'MULTI'
                })
            });
        }
        return me.splitRepositoryLogGrid;

    },
    initComponent: function() {
        var me = this;
        Ext.applyIf(me, {
            items: [me.getQueryForm(),me.getSplitRepositoryLogGrid()]
        });
        me.callParent(arguments);
    }
});