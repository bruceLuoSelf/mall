

Ext.define('MyApp.view.shGameAccountManager', {
    extend: 'Ext.panel.Panel',
    id:'shGameAccountManager',
    closable: true,
    layout: "border",
    title: '收货角色管理',
    autoScroll: false,
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
                    fieldLabel: '更新时间',
                    xtype: 'rangedatefield',
                    fromName: 'createStartTime',
                    toName: 'createEndTime',
                    fromValue: new Date(new Date()-30*24*60*60*1000),
                    toValue: new Date()
                },{
                    fieldLabel: '收货方账号',
                    name: 'buyerAccount'
                },{
                    fieldLabel: '收货方UID',
                    name: 'buyerUid'
                },{
                    fieldLabel: '收货的角色名',
                    labelWidth: 100,
                    name: 'roleName'
                },{
                    xtype: 'gamelinkselector',
                    itemId : 'MyApp_view_goods_gamelink_ID',
                    //allowBlank: false,
                    fieldLabel: '游戏属性'
                },DataDictionary.getDataDictionaryCombo('roleStatus',{
                    fieldLabel: '角色状态',
                    labelWidth: 100,
                    name: 'status',
                    editable: false,
                    value: 0
                },{
                    value:0,
                    display:'全部'
                }),{
                    fieldLabel: '收货游戏账号',
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
            me.store = Ext.create('MyApp.store.ShGameAccountStore',{
                autoLoad: true,
                listeners: {
                    beforeload : function(store, operation, eOpts) {
                        var queryForm = me.getQueryForm();
                        if (queryForm != null) {
                            var values = queryForm.getValues();
                            var gameName = queryForm.getForm().findField('gameName').getRawValue();
                            Ext.apply(operation, {
                                params: {
                                    'startTime':values.createStartTime,
                                    'endTime':values.createEndTime,
                                    'shGameAccount.buyerAccount': Ext.String.trim(values.buyerAccount),
                                    'shGameAccount.buyerUid': Ext.String.trim(values.buyerUid),
                                    'shGameAccount.gameAccount': Ext.String.trim(values.gameAccount),
                                    'shGameAccount.roleName': Ext.String.trim(values.roleName),
                                    'shGameAccount.status': values.status,
                                    'shGameAccount.gameName': Ext.String.trim(gameName),
                                    'shGameAccount.region': Ext.String.trim(values.region),
                                    'shGameAccount.server': Ext.String.trim(values.server),
                                    'shGameAccount.gameRace': Ext.String.trim(values.gameRace)

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
    shGameAccountGrid: null,
    getShGameAccountGrid: function(){
        var me = this;
        if(Ext.isEmpty(me.shGameAccountGrid)){
            me.shGameAccountGrid = Ext.widget('gridpanel',{
                header: false,
                region: 'center',
                columnLines: true,
                store: me.getStore(),
                columns: [{
                    xtype: 'rownumberer'
                },{
                    dataIndex: 'buyerAccount',
                    text: '收货方5173账号',
                    flex: 0.7,
                    align: 'center'
                },{
                    dataIndex: 'buyerUid',
                    text: '收货方5173UID',
                    flex: 0.7,
                    align: 'center'
                },{
                    dataIndex: 'gameName',
                    text: '游戏名称',
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
                    text: '收货的游戏账号',
                    flex: 0.7,
                    align: 'center'
                },{
                    dataIndex: 'roleName',
                    text: '收货的角色名',
                    flex: 0.7,
                    align: 'center'
                },{
                    dataIndex: 'level',
                    text: '等级',
                    flex: 0.7,
                    align: 'center'
                },{
                    dataIndex: 'count',
                    text: '收货数量',
                    flex: 0.7,
                    align: 'center'
                },{
                    dataIndex: 'price',
                    text: '收货单价',
                    flex: 0.7,
                    align: 'center'
                },{
                    dataIndex: 'repositoryCount',
                    text: '库存数量',
                    flex: 0.7,
                    align: 'center'
                },{
                    dataIndex: 'isShRole',
                    text: '是否收货角色 ',
                    flex: 0.7,
                    sortable: false,
                    align: 'center',
                    renderer: function(value){
                        return DataDictionary.rendererSubmitToDisplay(value,'isShRoleType');
                    }
                }
                //     ,{
                //     dataIndex: 'isPackFull',
                //     text: '背包是否已满 ',
                //     flex: 1,
                //     sortable: false,
                //     align: 'center',
                //     renderer: function(value){
                //         return DataDictionary.rendererSubmitToDisplay(value,'isPackFullType');
                //     }
                // }
                    ,{
                    dataIndex: 'status',
                    text: '角色状态 ',
                    flex: 1,
                    sortable: false,
                    align: 'center',
                    renderer: function(value, metaData, record, rowIndex, colIndex, store, view) {
                        if(value!=0){
                            return DataDictionary.rendererSubmitToDisplay(value,'roleStatus');
                        }else{
                            return '';
                        }
                    }
                },{
                    xtype: 'datecolumn',
                    format:'Y-m-d H:i:s',
                    dataIndex: 'updateTime',
                    sortable: false,
                    flex: 1.5,
                    align: 'center',
                    text: '更新时间 '
                }],
                dockedItems: [me.getPagingToolbar()],
                selModel: Ext.create('Ext.selection.CheckboxModel', {
                    allowDeselect: true,
                    mode: 'MULTI'
                })
            });
        }
        return me.shGameAccountGrid;

    },
    initComponent: function() {
        var me = this;
        Ext.applyIf(me, {
            items: [me.getQueryForm(),me.getShGameAccountGrid()]
        });
        me.callParent(arguments);
    }
});