/**
 * 收货游戏配置管理
 */
Ext.define('MyApp.view.shGameConfigManager', {
    extend: 'Ext.panel.Panel',
    id: 'shGameConfigManager',
    closable: true,
    layout: "border",
    title: '收货游戏配置管理',
    autoScroll: false,
    toolbar: null,
    getToolbar: function () {
        var me = this;
        if (Ext.isEmpty(me.toolbar)) {
            me.toolbar = Ext.widget('toolbar', {
                dock: 'top',
                items: [{
                    text: '添加配置',
                    listeners: {
                        click: {
                            fn: me.addShGameConfig,
                            scope: me
                        }
                    }
                }, '-', {
                    text: '修改配置',
                    listeners: {
                        click: {
                            fn: me.modifyShGameConfig,
                            scope: me
                        }
                    }
                }, '-', {
                    text: '删除配置',
                    listeners: {
                        click: {
                            fn: me.deleteShGameConfig,
                            scope: me
                        }
                    }
                }, '-', {
                    xtype: 'button',
                    text: '启用收货',
                    listeners: {
                        click: {
                            fn: me.enableUser,
                            scope: me
                        }
                    }
                }, '-', {
                    xtype: 'button',
                    text: '禁用收货',
                    listeners: {
                        click: {
                            fn: me.disableUser,
                            scope: me
                        }
                    }
                }, '-', {
                    xtype: 'button',
                    text: '启用商城',
                    listeners: {
                        click: {
                            fn: me.enableMall,
                            scope: me
                        }
                    }
                }, '-', {
                    xtype: 'button',
                    text: '禁用商城',
                    listeners: {
                        click: {
                            fn: me.disableMall,
                            scope: me
                        }
                    }
                }, '-', {
                    xtype: 'button',
                    text: '启用九宫格',
                    listeners: {
                        click: {
                            fn: me.enableNineBlock,
                            scope: me
                        }
                    }
                }, '-', {
                    xtype: 'button',
                    text: '禁用九宫格',
                    listeners: {
                        click: {
                            fn: me.disableNineBlock,
                            scope: me
                        }
                    }
                }, '-', {
                    xtype: 'button',
                    text: '启用分仓配置',
                    listeners: {
                        click: {
                            fn: me.isSplitWarehouse,
                            scope: me
                        }
                    }
                }, '-', {
                    xtype: 'button',
                    text: '禁用分仓配置',
                    listeners: {
                        click: {
                            fn: me.disableWarehouse,
                            scope: me
                        }
                    }
                }]
            });
        }
        return me.toolbar;
    },
    // 增加系统配置地信息
    addShGameConfigWindow: null,
    getAddShGameConfigWindow: function () {
        if (this.addShGameConfigWindow == null) {
            this.addShGameConfigWindow = new MyApp.view.addShGameConfigWindow();
        }
        return this.addShGameConfigWindow;
    },

    addShGameConfig: function (button, e, eOpts) {
        var window = this.getAddShGameConfigWindow();
        window.bindData(Ext.create('MyApp.model.ShGameConfigModel'), false);
        // window.getForm().getForm().findField('gameName').readOnly = false;
        // window.getForm().getForm().findField('goodsTypeName').readOnly = false;
        window.show();
    },
    // 修改系统配置地信息
    modifyShGameConfig: function (button, e, eOpts) {
        var grid = this.getShGameConfigGrid(),
            selModel = grid.getSelectionModel(),
            selRecords = selModel.getSelection(),
            window = this.getAddShGameConfigWindow();
        if (selRecords == null || selRecords.length <= 0) {
            Ext.ux.Toast.msg("温馨提示", "请先选择要修改配置信息");
            return;
        }
        window.bindData(selRecords[0], true);
        // window.getForm().getForm().findField('gameName').readOnly=true;
        // window.getForm().getForm().findField('goodsTypeName').readOnly=true;
        window.show();


    },
    // 启用用户
    enableUser: function (button, e, eOpts) {
        var grid = this.getShGameConfigGrid(),
            selModel = grid.getSelectionModel(),
            selRecords = selModel.getSelection();
        if (selRecords == null || selRecords.length <= 0) {
            Ext.ux.Toast.msg("温馨提示", "请先选择要启用的用户");
            return;
        }
        var record = selRecords[0];
        if (record.get('isEnabled')) {
            Ext.ux.Toast.msg("温馨提示", "该用户已经启用！");
            return;
        }
        Ext.MessageBox.confirm('温馨提示', '确定启用该用户吗？', function (btn) {
            if (btn == 'yes') {
                Ext.Ajax.request({
                    url: './delivery/qyShGameConfig.action',
                    params: {'id': record.get('id')},
                    success: function (response, opts) {
                        Ext.ux.Toast.msg("温馨提示", "启用成功！");
                        grid.getStore().load();
                        grid.getSelectionModel().deselectAll();
                    },
                    exception: function (response, opts) {
                        var json = Ext.decode(response.responseText);
                        Ext.ux.Toast.msg("温馨提示", json.message);
                    }
                });
            } else {
                return;
            }
        });
    },
    // 禁用用户
    disableUser: function (button, e, eOpts) {
        var grid = this.getShGameConfigGrid(),
            selModel = grid.getSelectionModel(),
            selRecords = selModel.getSelection();
        if (selRecords == null || selRecords.length <= 0) {
            Ext.ux.Toast.msg("温馨提示", "请先选择要禁用的用户");
            return;
        }
        var record = selRecords[0];
        if (!record.get('isEnabled')) {
            Ext.ux.Toast.msg("温馨提示", "该用户已经禁用！");
            return;
        }
        Ext.MessageBox.confirm('温馨提示', '确定禁用该用户吗？', function (btn) {
            if (btn == 'yes') {
                Ext.Ajax.request({
                    url: './delivery/disableShGameConfig.action',
                    params: {'id': record.get('id')},
                    success: function (response, opts) {
                        Ext.ux.Toast.msg("温馨提示", "禁用成功！");
                        grid.getStore().load();
                        grid.getSelectionModel().deselectAll();
                    },
                    exception: function (response, opts) {
                        var json = Ext.decode(response.responseText);
                        Ext.ux.Toast.msg("温馨提示", json.message);
                    }
                });
            } else {
                return;
            }
        });
    },

    //商城启用 add by lcs 2017.5.12
    enableMall: function (button, e, eOpts) {
        var grid = this.getShGameConfigGrid(),
            selModel = grid.getSelectionModel(),
            selRecords = selModel.getSelection();
        if (selRecords == null || selRecords.length <= 0) {
            Ext.ux.Toast.msg("温馨提示", "请先选择要启用的");
            return;
        }
        var record = selRecords[0];
        if (record.get('enableMall')) {
            Ext.ux.Toast.msg("温馨提示", "已经启用！");
            return;
        }
        Ext.MessageBox.confirm('温馨提示', '确定启用吗？', function (btn) {
            if (btn == 'yes') {
                Ext.Ajax.request({
                    url: './delivery/enableMallShGameConfig.action',
                    params: {'id': record.get('id')},
                    success: function (response, opts) {
                        Ext.ux.Toast.msg("温馨提示", "启用成功！");
                        grid.getStore().load();
                        grid.getSelectionModel().deselectAll();
                    },
                    exception: function (response, opts) {
                        var json = Ext.decode(response.responseText);
                        Ext.ux.Toast.msg("温馨提示", json.message);
                    }
                });
            } else {
                return;
            }
        });
    },
    enableNineBlock: function (button, e, eOpts) {
        var grid = this.getShGameConfigGrid(),
            selModel = grid.getSelectionModel(),
            selRecords = selModel.getSelection();
        if (selRecords == null || selRecords.length <= 0) {
            Ext.ux.Toast.msg("温馨提示", "请先选择要启用的");
            return;
        }
        var record = selRecords[0];
        if (record.get('nineBlockEnable')) {
            Ext.ux.Toast.msg("温馨提示", "已经启用！");
            return;
        }
        Ext.MessageBox.confirm('温馨提示', '确定启用吗？', function (btn) {
            if (btn == 'yes') {
                Ext.Ajax.request({
                    url: './delivery/enableNineBlock.action',
                    params: {'id': record.get('id')},
                    success: function (response, opts) {
                        Ext.ux.Toast.msg("温馨提示", "启用成功！");
                        grid.getStore().load();
                        grid.getSelectionModel().deselectAll();
                    },
                    exception: function (response, opts) {
                        var json = Ext.decode(response.responseText);
                        Ext.ux.Toast.msg("温馨提示", json.message);
                    }
                });
            } else {
                return;
            }
        });
    },
    // 商城禁用 add by lcs 2017.5.12
    disableMall: function (button, e, eOpts) {
        var grid = this.getShGameConfigGrid(),
            selModel = grid.getSelectionModel(),
            selRecords = selModel.getSelection();
        if (selRecords == null || selRecords.length <= 0) {
            Ext.ux.Toast.msg("温馨提示", "请先选择要禁用的");
            return;
        }
        var record = selRecords[0];
        if (!record.get('enableMall')) {
            Ext.ux.Toast.msg("温馨提示", "已经禁用！");
            return;
        }
        Ext.MessageBox.confirm('温馨提示', '确定禁用吗？', function (btn) {
            if (btn == 'yes') {
                Ext.Ajax.request({
                    url: './delivery/disableMallShGameConfig.action',
                    params: {'id': record.get('id')},
                    success: function (response, opts) {
                        Ext.ux.Toast.msg("温馨提示", "禁用成功！");
                        grid.getStore().load();
                        grid.getSelectionModel().deselectAll();
                    },
                    exception: function (response, opts) {
                        var json = Ext.decode(response.responseText);
                        Ext.ux.Toast.msg("温馨提示", json.message);
                    }
                });
            } else {
                return;
            }
        });
    },
    disableNineBlock:function(button, e, eOpts){
        var grid = this.getShGameConfigGrid(),
            selModel = grid.getSelectionModel(),
            selRecords = selModel.getSelection();
        if (selRecords == null || selRecords.length <= 0) {
            Ext.ux.Toast.msg("温馨提示", "请先选择要禁用的");
            return;
        }
        var record = selRecords[0];
        if (!record.get('nineBlockEnable')) {
            Ext.ux.Toast.msg("温馨提示", "已经禁用！");
            return;
        }
        Ext.MessageBox.confirm('温馨提示', '确定禁用吗？', function (btn) {
            if (btn == 'yes') {
                Ext.Ajax.request({
                    url: './delivery/disableNineBlock.action',
                    params: {'id': record.get('id')},
                    success: function (response, opts) {
                        Ext.ux.Toast.msg("温馨提示", "禁用成功！");
                        grid.getStore().load();
                        grid.getSelectionModel().deselectAll();
                    },
                    exception: function (response, opts) {
                        var json = Ext.decode(response.responseText);
                        Ext.ux.Toast.msg("温馨提示", json.message);
                    }
                });
            } else {
                return;
            }
        });
        
    },
    isSplitWarehouse: function (button, e, eOpts) {
        var grid = this.getShGameConfigGrid(),
            selModel = grid.getSelectionModel(),
            selRecords = selModel.getSelection();
        if (selRecords == null || selRecords.length <= 0) {
            Ext.ux.Toast.msg("温馨提示", "请先选择要启用的");
            return;
        }
        var record = selRecords[0];
        if (record.get('isSplit')) {
            Ext.ux.Toast.msg("温馨提示", "已经启用！");
            return;
        }
        Ext.MessageBox.confirm('温馨提示', '确定启用吗？', function (btn) {
            if (btn == 'yes') {
                Ext.Ajax.request({
                    url: './delivery/isSplitWarehouse.action',
                    params: {'id': record.get('id')},
                    success: function (response, opts) {
                        Ext.ux.Toast.msg("温馨提示", "启用成功！");
                        grid.getStore().load();
                        grid.getSelectionModel().deselectAll();
                    },
                    exception: function (response, opts) {
                        var json = Ext.decode(response.responseText);
                        Ext.ux.Toast.msg("温馨提示", json.message);
                    }
                });
            } else {
                return;
            }
        });
    },
    disableWarehouse:function(button, e, eOpts){
        var grid = this.getShGameConfigGrid(),
            selModel = grid.getSelectionModel(),
            selRecords = selModel.getSelection();
        if (selRecords == null || selRecords.length <= 0) {
            Ext.ux.Toast.msg("温馨提示", "请先选择要禁用的");
            return;
        }
        var record = selRecords[0];
        if (!record.get('isSplit')) {
            Ext.ux.Toast.msg("温馨提示", "已经禁用！");
            return;
        }
        Ext.MessageBox.confirm('温馨提示', '确定禁用吗？', function (btn) {
            if (btn == 'yes') {
                Ext.Ajax.request({
                    url: './delivery/disableWarehouse.action',
                    params: {'id': record.get('id')},
                    success: function (response, opts) {
                        Ext.ux.Toast.msg("温馨提示", "禁用成功！");
                        grid.getStore().load();
                        grid.getSelectionModel().deselectAll();
                    },
                    exception: function (response, opts) {
                        var json = Ext.decode(response.responseText);
                        Ext.ux.Toast.msg("温馨提示", json.message);
                    }
                });
            } else {
                return;
            }
        });

    },
    deleteShGameConfig: function () {
        var me = this,
            grid = me.getShGameConfigGrid(),
            selModel = grid.getSelectionModel(),
            selRecords = selModel.getSelection();
        if (selRecords == null || selRecords.length <= 0) {
            Ext.ux.Toast.msg("温馨提示", "请先选择要删除的配置");
            return;
        }
        var record = selRecords[0];
        Ext.MessageBox.confirm('温馨提示', '确定删除吗？', function (btn) {
            if (btn == 'yes') {
                me.getStore().remove(record);
                Ext.Ajax.request({
                    url: './delivery/deleteShGameConfig.action',
                    params: {'id': record.get('id')},
                    method: 'POST',

                    success: function (response, opts) {
                        Ext.ux.Toast.msg("温馨提示", "删除成功！");
                        me.getStore().load();
                        grid.getSelectionModel().deselectAll();
                    },
                    exception: function (response, opts) {
                        var json = Ext.decode(response.responseText);
                        Ext.ux.Toast.msg("温馨提示", json.responseStatus.message);
                    }
                });
            } else {
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
                    margin: '10 10 10 10',
                    xtype: 'textfield'
                },
                items: [{
                    xtype: 'gameselectorsellersetting',
                    itemId : 'MyApp_view_goods_gamelink_ID',
                    columnWidth: .7,
                    allowBlank: false,
                    gameChanged: function(ths, the, eOpts){
                        var form = me.getForm().getForm();
                        var categoryCombo = form.findField("goodsTypeId");
                        categoryCombo.setRawValue(null);
                        categoryCombo.getStore().load();
                    },
                    fieldLabel: '游戏名称'
                },Ext.create('Ext.form.ComboBox', {
                    fieldLabel: '商品类型',
                    labelWidth: 100,
                    name: 'goodsTypeName',
                    store: Ext.create('MyApp.store.MallGoodsTypeNameIdStore', {
                        listeners: {
                            load: function (store, records, successful, eOpts) {
                                //添加
                                store.insert(0,{id:0, name:'全部'});
                            }
                        }
                    }),
                    displayField: 'name',
                    valueField: 'name',
                    value:'全部',
                    editable: false
                })],
                buttons: [{
                    text: '重置',
                    handler: function () {
                        me.getQueryForm().getForm().reset();
                    }
                }, '->', {
                    text: '查询',
                    handler: function () {
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
            me.store = Ext.create('MyApp.store.ShGameConfigStore',{
                autoLoad: true,
                listeners: {
                    beforeload : function(store, operation, eOpts) {
                        var queryForm = me.getQueryForm();
                        var values = queryForm.getValues();
                        var gameName = queryForm.getForm().findField('gameName');
                        if (queryForm != null) {
                            Ext.apply(operation, {
                                params: {
                                    'shGameConfig.gameName': Ext.String.trim(gameName.getRawValue()),
                                    'shGameConfig.goodsTypeName': values.goodsTypeName
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
    getPagingToolbar: function () {
        var me = this;
        if (me.pagingToolbar == null) {
            me.pagingToolbar = Ext.widget('pagingtoolbar', {
                store: me.getStore(),
                dock: 'bottom',
                displayInfo: true
            });
        }
        return me.pagingToolbar;
    },
    shGameConfigGrid: null,
    getShGameConfigGrid: function () {
        var me = this;
        if (Ext.isEmpty(me.shGameConfigGrid)) {
            me.shGameConfigGrid = Ext.widget('gridpanel', {
                header: false,
                region: 'center',
                columnLines: true,
                store: me.getStore(),
                columns: [{
                    xtype: 'rownumberer'
                }, {
                    dataIndex: 'gameName',
                    text: '游戏名称',
                    flex: 1,
                    align: 'center'
                }, {
                    dataIndex: 'unitName',
                    text: '单位',
                    flex: 0.5,
                    align: 'center'
                },  {
                    dataIndex: 'poundage',
                    text: '拍卖手续费',
                    flex: 0.5,
                    align: 'center'
                }, {
                    dataIndex: 'tradeType',
                    text: '支持的交易方式',
                    flex: 2,
                    align: 'center',

                }, {
                    dataIndex: 'isSplit',
                    sortable: true,
                    flex: 0.5,
                    text: '收货商分仓开关',
                    renderer: function (value, metaData, record, rowIndex, colIndex, store, view) {
                        return DataDictionary.rendererSubmitToDisplay(value, 'isSplit');
                    }
                }, {
                    dataIndex: 'isEnabled',
                    sortable: false,
                    flex: 0.5,
                    text: '是否启用收货',
                    renderer: function (value, metaData, record, rowIndex, colIndex, store, view) {
                        return DataDictionary.rendererSubmitToDisplay(value, 'isEnabled');
                    }
                }, {
                    dataIndex: 'enableMall',
                    sortable: false,
                    flex: 0.5,
                    text: '是否启用商城',
                    renderer: function (value, metaData, record, rowIndex, colIndex, store, view) {
                        return DataDictionary.rendererSubmitToDisplay(value, 'isEnabled');
                    }
                }, {
                    dataIndex: 'enableRobot',
                    sortable: false,
                    flex: 0.5,
                    text: '是否机器收货',
                    renderer: function (value, metaData, record, rowIndex, colIndex, store, view) {
                        return DataDictionary.rendererSubmitToDisplay(value, 'enableRobot');
                    }
                }, {
                    dataIndex: 'goodsTypeName',
                    text: '游戏类目',
                    flex: 1,
                    align: 'center'
                }, {
                    dataIndex: 'nineBlockEnable',
                    sortable: false,
                    flex: 0.5,
                    text: '是否启用九宫格',
                    renderer: function (value, metaData, record, rowIndex, colIndex, store, view) {
                        return DataDictionary.rendererSubmitToDisplay(value, 'isEnabled');
                    }
                }, {
                    dataIndex: 'nineBlockConfigure',
                    text: '九宫格数据',
                    flex: 1,
                    align: 'center'
                }, {
                    dataIndex: 'deliveryMessage',
                    text: '发货信息',
                    flex: 1,
                    align: 'center'
                },{
                    dataIndex: 'minBuyAmount',
                    text: '最低购买金额',
                    flex: 1,
                    align: 'center'
                }, {
                    dataIndex: 'minCount',
                    text: '最低库存数量',
                    flex: 1,
                    align: 'center'
                }, {
                    dataIndex: 'repositoryCount',
                    text: '库存上限',
                    flex: 1,
                    align: 'center'
                }, {
                    dataIndex: 'needCount',
                    text: '缺口上限',
                    flex: 1,
                    align: 'center'
                }, {
                    dataIndex: 'mailFee',
                    text: '邮寄手续费',
                    flex: 1,
                    align: 'center'
                }, {
                    dataIndex: 'thresholdCount',
                    text: '库存阈值',
                    flex: 1,
                    align: 'center'
                }, {
                    xtype: 'datecolumn',
                    format: 'Y-m-d H:i:s',
                    dataIndex: 'createTime',
                    sortable: false,
                    flex: 1.5,
                    align: 'center',
                    text: '创建时间 '
                }, {
                    xtype: 'datecolumn',
                    format: 'Y-m-d H:i:s',
                    dataIndex: 'updateTime',
                    sortable: false,
                    flex: 1.5,
                    align: 'center',
                    text: '更新时间 '
                }],
                dockedItems: [me.getToolbar(), me.getPagingToolbar()],
                selModel: Ext.create('Ext.selection.CheckboxModel', {
                    allowDeselect: true,
                    mode: 'MULTI'
                })
            });
        }
        return me.shGameConfigGrid;

    },
    initComponent: function () {
        var me = this;
        Ext.applyIf(me, {
            items: [me.getQueryForm(), me.getShGameConfigGrid()]
        });
        me.callParent(arguments);
    }
});

/**
 *新增游戏配置
 */
Ext.define('MyApp.view.addShGameConfigWindow', {
    extend: 'Ext.window.Window',
    title: '新增/修改游戏配置信息',
    width: 800,
    border: false,
    padding: '10 10 10 10',
    autoScroll: true,
    closeAction: 'hide',
    modal: true,
    layout: 'anchor',
    form: null,
    deliveryData: null,
    getForm: function () {
        var me = this;
        if (me.form == null) {
            me.form = Ext.widget('form', {
                layout: 'column',
                defaults: {
                    margin: '10 10 5 5',
                    columnWidth: .5,
                    labelWidth: 130,
                    xtype: 'textfield'
                },
                items: [{
                        xtype: 'gameselectorsellersetting',
                        itemId : 'MyApp_view_goods_gamelink_ID',
                        columnWidth: 1,
                        allowBlank: false,
                        name: 'gameName',
                        fieldLabel: '游戏名字'
                    }
                    //Ext.create('Ext.form.ComboBox', {
                //     fieldLabel: '游戏名称',
                //     allowBlank: false,
                //     name: 'gameName',
                //     columnWidth: 0.5,
                //     labelWidth: 100,
                //     store: Ext.create(
                //         'MyApp.store.MallGameNameIdStore', {
                //             autoLoad: true
                //         }),
                //     displayField: 'name',
                //     valueField: 'id',
                //     editable: false
                // }),
                ,Ext.create('Ext.form.ComboBox', {
                    fieldLabel: '交易类目',
                        itemId: 'MyApp_view_goods_type_ID',
                        allowBlank: false,
                        name: 'goodsTypeName',
                        columnWidth: 0.5,
                        store: Ext.create(
                            'MyApp.store.MallGoodsTypeNameIdStore', {
                                autoLoad: true
                            }),
                        displayField: 'name',
                        valueField: 'id',
                        editable: false
                    }), DataDictionary.getDataDictionaryCombo('isEnabled', {
                        fieldLabel: '收货是否启用',
                        editable: false,
                        name: 'isEnabled',
                        labelWidth: 100,
                        value: true
                    }), {
                        fieldLabel: '单位',
                        // allowBlank: false,
                        width: 10,
                        name: 'unitName'
                    }, {
                        fieldLabel: '最小库存数量',
                        // allowBlank: false,
                        width: 10,
                        name: 'minCount'
                    },{
                        fieldLabel: '九宫格配置',
                        // allowBlank: false,
                        width: 10,
                        name: 'nineBlockConfigure'
                    },{
                        fieldLabel: '发货提示',
                        // allowBlank: false,
                        width: 10,
                        name: 'deliveryMessage'
                    },{
                        fieldLabel: '拍卖手续费',
                        width: 10,
                        xtype:'numberfield',
                        decimalPrecision: 6,
                        name: 'poundage'
                    }, DataDictionary.getDataDictionaryCombo('isEnabled', {
                        fieldLabel: '是否支持机器收货',
                        columnWidth: 0.5,
                        editable: false,
                        name: 'enableRobot',
                        labelWidth: 130,
                        value: true
                    }), {
                        fieldLabel: '最低购买金额',
                        allowBlank: false,
                        width: 10,
                        name: 'minBuyAmount'
                    }, {
                        fieldLabel: '库存上限',
                        allowBlank: true,
                        width: 10,
                        name: 'repositoryCount'
                    }, {
                        fieldLabel: '缺口上限',
                        allowBlank: true,
                        width: 10,
                        name: 'needCount'
                    }, {
                        fieldLabel: '邮寄手续费',
                        allowBlank: true,
                        width: 10,
                        name: 'mailFee'
                    }, {
                        fieldLabel: '库存异常阈值',
                        allowBlank: true,
                        width: 10,
                        name: 'thresholdCount'
                    }],
                buttons: [{
                    text: '保存',
                    handler: function () {
                        var formView = me.getForm(),
                            form = formView.getForm(),
                            record = form.getRecord(),
                            gameName = form.findField('gameName'),
                            goodsTypeId = form.findField('goodsTypeId'),
                            url = './delivery/addShGameConfig.action',
                            message = '新增';
                        console.log(form.findField('checkBoxCategoryIcon1'));
                        if (form.isValid()) {
                            var categoryIcon1_record = "",
                                categoryIcon1_record_id = "",
                                checkBoxCategoryIcon1 = form.findField('checkBoxCategoryIcon1').getChecked();
                            if (checkBoxCategoryIcon1.length > 5) {
                                Ext.ux.Toast.msg("温馨提示", "收货模式太多了！");
                                return;
                            }
                            if (checkBoxCategoryIcon1.length <= 0) {
                                Ext.ux.Toast.msg("温馨提示", "请至少选择一种收货模式！");
                                return;
                            }
                            for (var i = 0, j = checkBoxCategoryIcon1.length; i < j; i++) {
                                categoryIcon1_record += checkBoxCategoryIcon1[i].boxLabel;
                                categoryIcon1_record_id += checkBoxCategoryIcon1[i].getSubmitValue();
                                if (i != (j - 1)) {
                                    categoryIcon1_record += ",";
                                    categoryIcon1_record_id += ",";
                                }
                            };
                        }
                        form.updateRecord(record);
                        params = {
                            'shGameConfig.gameName': Ext.String.trim(gameName.getRawValue()),
                            'shGameConfig.tradeType': categoryIcon1_record,
                            'shGameConfig.tradeTypeId': categoryIcon1_record_id,
                            'shGameConfig.unitName': record.get('unitName'),
                            'shGameConfig.poundage':record.get('poundage'),
                            'shGameConfig.isEnabled': record.get('isEnabled'),
                            'shGameConfig.goodsTypeName': form.findField('goodsTypeName').getRawValue(),
                            'shGameConfig.enableRobot': record.get('enableRobot'),
                            'shGameConfig.enableMall': record.get('enableMall'),
                            'shGameConfig.minBuyAmount': record.get('minBuyAmount'),//最低购买金额 ADD 20170606
                            'shGameConfig.minCount': record.get('minCount'),
                            'shGameConfig.nineBlockConfigure': record.get('nineBlockConfigure'),
                            'shGameConfig.deliveryMessage': record.get('deliveryMessage'),
                            'shGameConfig.repositoryCount': record.get('repositoryCount'), //库存上限
                            'shGameConfig.needCount': record.get('needCount'), //缺口上限
                            'shGameConfig.mailFee': record.get('mailFee'), //邮寄手续费
                            'shGameConfig.thresholdCount': record.get('thresholdCount')  //库存阈值
                        };
                        if (me.isUpdate) {
                            url = './delivery/updateShGameConfig.action';
                            message = '修改';
                            Ext.Object.merge(params, {
                                'shGameConfig.id': record.get('id')
                            });
                        }
                        form.submit({
                            url: url,
                            method: 'POST',
                            params: params,
                            success: function (from, action, json) {
                                var shGameConfigManager = Ext.getCmp('shGameConfigManager'),
                                    store = shGameConfigManager.getStore();
                                Ext.ux.Toast.msg("温馨提示", message + "成功");
                                me.close();
                                store.load();
                            },
                            exception: function (from, action, json) {
                                Ext.ux.Toast.msg("温馨提示", json.message);
                            }
                        });
                    }
                }]
            });
        }
        return me.form;
    },
    checkboxGroup: null,
    getMyCheckboxItems: function () {
        var me = this;
        if (me.checkboxGroup == null) {
            var theItems = [];
            var store = Ext.create('MyApp.store.ShTradeStore', {
                autoLoad: true,
                listeners: {
                    'load': function (store) {
                        var count = store.getCount();
                        for (var i = 0; i < count; i++) {
                            var boxLabel = store.getAt(i).getData().name;
                            // console.log(store.getAt(i).getData().name);
                            var id = store.getAt(i).getData().id;
                            if (store.getAt(i).getData().enabled) {
                                theItems.push({
                                        boxLabel: boxLabel,
                                        name: "CheckboxGroup11",
                                        inputValue: id
                                    }
                                );
                            }
                        }
                        me.checkboxGroup = Ext.create('Ext.form.CheckboxGroup', {
                            columns: 5,
                            name: 'checkBoxCategoryIcon1',
                            vertical: false,
                            items: theItems,
                        });
                        var fieldset = Ext.widget('fieldset', {
                            columnWidth: 1,
                            title: '收货模式配置',
                            defaultType: 'checkbox', // each item will be a checkbox
                            defaults: {
                                hideEmptyLabel: false,
                                allowBlank: true,
                                align: 'center',
                            }
                        });

                        fieldset.add(me.checkboxGroup);
                        me.getForm().add(fieldset);


                        form = me.getForm().getForm();
                        checkBoxCategoryIcon1 = form.findField('checkBoxCategoryIcon1');
                        if (checkBoxCategoryIcon1 != null && me.deliveryData != "") {
                            var categoryIcon1_string = me.deliveryData.split(",");
                            checkBoxCategoryIcon1.setValue(categoryIcon1_string);

                        }
                    }
                }
            })
        }
        return me.checkboxGroup;
    },
    isUpdate: null,
    bindData: function (record, isUpdate) {
        var me = this,
            // form = me.getForm().getForm();
            formView = me.getForm()
            form = formView.getForm(),
            gameProp = formView.getComponent('MyApp_view_goods_gamelink_ID'),
                goodsProp =formView.getComponent('MyApp_view_goods_type_ID'),
        form.loadRecord(record);
        form.reset();
        me.deliveryData = record.data.tradeTypeId;
        form.loadRecord(record);
        me.isUpdate = isUpdate;
        if (!isUpdate) {
            gameProp.setDisabled(false);
            goodsProp.setDisabled(false);
            form.reset();
        } else {
            gameProp.setDisabled(true);
            goodsProp.setDisabled(true);
            checkBoxCategoryIcon1 = form.findField('checkBoxCategoryIcon1');
            if (checkBoxCategoryIcon1 != null && me.deliveryData != "") {
                var categoryIcon1_string = me.deliveryData.split(",");
                checkBoxCategoryIcon1.setValue(categoryIcon1_string);

            }
        }
    },


    constructor: function (config) {
        var me = this, cfg = Ext.apply({}, config);
        me.items = [me.getForm()]
        me.callParent([cfg]);
        me.getMyCheckboxItems();
    }

});
Ext.override(Ext.form.CheckboxGroup, {
    //在inputValue中找到定义的内容后，设置到items里的各个checkbox中
    setValue: function (value) {
        this.items.each(function (f) {
            for (i = 0; i < value.length; i++) {
                if (f.inputValue == value[i]) {
                    f.setValue(true);
                }
            }
        });

    },
    //以value1,value2的形式拼接group内的值
    getValue: function () {
        var re = "";
        this.items.each(function (f) {
            if (f.getValue() == true) {
                re += f.inputValue + ",";
            }
        });
        return re.substr(0, re.length - 1);
    },
    //在Field类中定义的getName方法不符合CheckBoxGroup中默认的定义，因此需要重写该方法使其可以被BasicForm找到
    getName: function () {
        return this.name;
    }
});