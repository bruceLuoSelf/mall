//游戏类目配置
Ext.define('MyApp.view.gameCategoryManager', {
    extend: 'Ext.panel.Panel',
    id: 'gameCategoryManager',
    layout: "border",
    closable: true,
    title: '游戏类目配置',
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
                            fn: me.addConfig,
                            scope: me
                        }
                    }
                }, /*'-', {
                    text: '修改配置',
                    listeners: {
                        click: {
                            fn: me.modifyConfig,
                            scope: me
                        }
                    }
                },*/ '-', {
                    text: '删除配置',
                    listeners: {
                        click: {
                            fn: me.deleteConfig,
                            scope: me
                        }
                    }
                }, '-', {
                    xtype: 'button',
                    itemId: 'enableButton',
                    text: '启用',
                    listeners: {
                        click: {
                            fn: me.enableUser,
                            scope: me
                        }
                    }
                }, '-', {
                    xtype: 'button',
                    itemId: 'disableButton',
                    text: '禁用',
                    listeners: {
                        click: {
                            fn: me.disableUser,
                            scope: me
                        }
                    }
                }]
            });
        }
        return me.toolbar;
    },
    // 增加系统配置地信息
    addConfigWindow: null,
    getAddConfigWindow: function () {
        if (this.addConfigWindow == null) {
            this.addConfigWindow = new MyApp.view.addGameCateConfigWindow();
        }
        return this.addConfigWindow;
    },

    addConfig: function (button, e, eOpts) {
        var window = this.getAddConfigWindow();
        window.bindData(Ext.create('MyApp.model.ConfigModel'), false);
        window.show();
    },
    // 修改系统配置地信息
    modifyConfig: function (button, e, eOpts) {
        var grid = this.getConfigGrid(),
            selModel = grid.getSelectionModel(),
            selRecords = selModel.getSelection(),
            window = this.getAddConfigWindow();
        if (selRecords == null || selRecords.length <= 0) {
            Ext.ux.Toast.msg("温馨提示", "请先选择要修改配置信息");
            return;
        }
        window.bindData(selRecords[0], true);
        window.show();
    },
    // 启用用户
    enableUser: function (button, e, eOpts) {
        var grid = this.getConfigGrid(),
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
                    url: './shpurchase/qyGameCategoryConfig.action',
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
        var grid = this.getConfigGrid(),
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
                    url: './shpurchase/disableGameCategory.action',
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

    changeConfig: function () {
        var me = this,
            selModel = me.getConfigGrid().getSelectionModel(),
            selRecords = selModel.getSelection(),
            window = me.getupdateConfig();
        if (selRecords == null || selRecords.length <= 0) {
            Ext.ux.Toast.msg("温馨提示", "请先选择要修改的配置");
            return;
        }
        ;
        window.bindData(selRecords[0]);
        window.show();
    },

    deleteConfig: function () {
        var me = this,
            grid = me.getConfigGrid(),
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
                    url: './shpurchase/deleteGameCategoryConfig.action',
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
                region: 'north',
                layout: 'column',
                defaults: {
                    margin: '10 10 10 10'
                },
                items: [{
                    xtype: 'textfield',
                    columnWidth: .3,
                    labelWidth: 80,
                    fieldLabel: '游戏类目',
                    name: 'name'
                }],
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
    getStore: function () {
        var me = this;
        if (me.store == null) {
            me.store = Ext.create('MyApp.store.GameCategoryConfigStore', {
                autoLoad: true,
                listeners: {
                    beforeload: function (store, operation, eOpts) {
                        var queryForm = me.getQueryForm();
                        if (queryForm != null) {
                            var values = queryForm.getValues();
                            var params = {
                                'gameCategoryConfig.name': Ext.String.trim(values.name),
                            }
                            Ext.apply(operation, {
                                params: params
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
    updateConfig: null,
    getupdateConfig: function () {
        if (this.updateWindow == null) {
            this.updateWindow = new MyApp.view.updateConfig();
        }
        return this.updateWindow;
    },
    ConfigGrid: null,
    getConfigGrid: function () {
        var me = this;
        if (Ext.isEmpty(me.refundOrderGrid)) {
            me.refundOrderGrid = Ext.widget('gridpanel', {
                region: 'center',
                header: false,
                columnLines: true,
                store: me.getStore(),
                columns: [{
                    xtype: 'rownumberer'
                }, {
                    dataIndex: 'name',
                    text: '游戏类目',
                    flex: 1.7,
                    align: 'center'
                },{
                    dataIndex: 'isEnabled',
                    sortable: false,
                    flex: 1,
                    text: '是否启用',
                    renderer: function (value, metaData, record, rowIndex, colIndex, store, view) {
                        return DataDictionary.rendererSubmitToDisplay(value, 'isEnabled');
                    }
                },{
                    xtype: 'datecolumn',
                    dataIndex: 'createTime',
                    text: '创建时间',
                    flex:  1.7,
                    align: 'center',
                    format:'Y-m-d H:i:s',
                    sortable: false,
                },{
                    xtype: 'datecolumn',
                    dataIndex: 'updateTime',
                    text: '更新时间',
                    flex:  1.7,
                    align: 'center',
                    format:'Y-m-d H:i:s',
                    sortable: false,
                }],
                dockedItems: [me.getToolbar(), me.getPagingToolbar()],
                selModel: Ext.create('Ext.selection.CheckboxModel', {
                    allowDeselect: true,
                    mode: 'MULTI'
                })
            });
        }
        return me.refundOrderGrid;
    },
    initComponent: function () {
        var me = this;
        Ext.applyIf(me, {
            items: [me.getQueryForm(), me.getConfigGrid()]
        });
        me.callParent(arguments);
    }
});

//增加游戏类目配置
Ext.define('MyApp.view.addGameCateConfigWindow', {
    extend: 'Ext.window.Window',
    title: '新增/修改游戏类目配置信息',
    width: 600,
    closeAction: 'hide',
    modal: true,
    form: null,
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
                    fieldLabel: '游戏类目',
                    allowBlank: false,
                    name: 'name'
                }],
                buttons: [{
                    text: '保存',
                    handler: function () {
                        var formView = me.getForm(),
                            form = formView.getForm(), params,
                            record = form.getRecord(),
                            url = './shpurchase/addGameCategoryConfig.action',
                            message = '新增';
                        if (!form.isValid()) {
                            return;
                        }
                        form.updateRecord(record);
                        params = {
                            'gameCategoryConfig.name':record.get('name')
                        };
                        if (me.isUpdate) {
                            url = './shpurchase/updateGameCategoryConfig.action';
                            message = '修改';
                            Ext.Object.merge(params, {
                                'gameCategoryConfig.id': record.get('id')
                            });
                        }
                        form.submit({
                            url: url,
                            method: 'POST',
                            params: params,
                            success: function (from, action, json) {
                                var gameConfigManager = Ext.getCmp('gameCategoryManager'),
                                    store = gameConfigManager.getStore();
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
    isUpdate: null,
    bindData: function (record, isUpdate) {
        var me = this,
            form = me.getForm().getForm();
        form.reset();
        form.loadRecord(record);
        me.isUpdate = isUpdate;
        if (!isUpdate) {
            form.reset();
        }
    },
    constructor: function (config) {
        var me = this, cfg = Ext.apply({}, config);
        me.items = [me.getForm()]
        me.callParent([cfg]);
    }
});
