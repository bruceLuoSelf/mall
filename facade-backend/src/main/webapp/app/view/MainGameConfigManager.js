//主游戏配置
Ext.define('MyApp.view.MainGameConfigManager', {
        extend: 'Ext.panel.Panel',
        id: 'MainGameConfigManager',
        closable: true,
        layout: "border",
        title: '主游戏配置',
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
                    }, '-', {
                        text: '删除配置',
                        listeners: {
                            click: {
                                fn: me.deleteConfig,
                                scope: me
                            }
                        }
                    }, '-', {
                        xtype: 'button',
                        text: '开启收货',
                        listeners: {
                            click: {
                                fn: me.enableUser,
                                scope: me
                            }
                        }
                    }, '-', {
                        xtype: 'button',
                        text: '关闭收货',
                        listeners: {
                            click: {
                                fn: me.disableUser,
                                scope: me
                            }
                        }
                    }, '-', {
                        xtype: 'button',
                        text: '开启商城出售',
                        listeners: {
                            click: {
                                fn: me.ableSell,
                                scope: me
                            }
                        }
                    }, '-', {
                        xtype: 'button',
                        text: '关闭商城出售',
                        listeners: {
                            click: {
                                fn: me.disableSell,
                                scope: me
                            }
                        }
                    }]
                });
            }
            return me.toolbar;
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
                    items: [
                        {
                            xtype: 'gameName',
                            itemId: 'GameName_selector_GameName_ID',
                            fieldLabel: '游戏名称'
                        }
                    ],
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
        addConfig: function (button, e, eOpts) {
            var window = this.getMainGameConfigWindow();
            window.bindData(Ext.create('MyApp.model.MainGameConfigModel'));
            window.show();
        },
        //增加系统配置
        addMainGameConfigWindow: null,
        getMainGameConfigWindow: function () {
            if (this.addMainGameConfigWindow == null) {
                this.addMainGameConfigWindow = new MyApp.view.addMainGameConfigWindow();
            }
            return this.addMainGameConfigWindow;
        },
        //删除系统配置
        deleteConfig: function () {
            var me = this;
            var grid = me.getGameConfigGrid();
            var selModel = grid.getSelectionModel();
            var selRecords = selModel.getSelection();
            if (selRecords == null || selRecords.length <= 0) {
                Ext.ux.Toast.msg("温馨提示", "请先选择要删除的配置");
                return;
            }
            var record = selRecords[0];
            Ext.MessageBox.confirm('温馨提示', '确定删除吗？', function (btn) {
                if (btn == 'yes') {
                    me.getStore().remove(record);
                    Ext.Ajax.request({
                        url: './shpurchase/deleteMainGameConfig.action',
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
        //启用收货
        enableUser: function (button, e, eOpts) {
            var grid = this.getGameConfigGrid(),
                selModel = grid.getSelectionModel(),
                selRecords = selModel.getSelection();
            if (selRecords == null || selRecords.length <= 0) {
                Ext.ux.Toast.msg("温馨提示", "请先选择要启用的用户");
                return;
            }
            var record = selRecords[0];
            if (record.get('ableDelivery')) {
                Ext.ux.Toast.msg("温馨提示", "该用户已经启用！");
                return;
            }
            Ext.MessageBox.confirm('温馨提示', '确定启用该用户吗？', function (btn) {
                if (btn == 'yes') {
                    Ext.Ajax.request({
                        url: './shpurchase/qyDelivery.action',
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
        //关闭收货
        disableUser: function () {
            var grid = this.getGameConfigGrid(),
                selModel = grid.getSelectionModel(),
                selRecords = selModel.getSelection();
            if (selRecords == null || selRecords.length <= 0) {
                Ext.ux.Toast.msg("温馨提示", "请先选择要禁用的用户");
                return;
            }
            var record = selRecords[0];
            if (!record.get('ableDelivery')) {
                Ext.ux.Toast.msg("温馨提示", "该用户已经禁用！");
                return;
            }
            Ext.MessageBox.confirm('温馨提示', '确定禁用该用户吗？', function (btn) {
                if (btn == 'yes') {
                    Ext.Ajax.request({
                        url: './shpurchase/disableDelivery.action',
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
        //开启商城出售
        ableSell: function () {
            var grid = this.getGameConfigGrid(),
                selModel = grid.getSelectionModel(),
                selRecords = selModel.getSelection();
            if (selRecords == null || selRecords.length <= 0) {
                Ext.ux.Toast.msg("温馨提示", "请先选择要启用的用户");
                return;
            }
            var record = selRecords[0];
            if (record.get('ableSell')) {
                Ext.ux.Toast.msg("温馨提示", "该用户已经启用！");
                return;
            }
            Ext.MessageBox.confirm('温馨提示', '确定启用该用户吗？', function (btn) {
                if (btn == 'yes') {
                    Ext.Ajax.request({
                        url: './shpurchase/qySell.action',
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
        //关闭商城出售
        disableSell: function () {
            var grid = this.getGameConfigGrid(),
                selModel = grid.getSelectionModel(),
                selRecords = selModel.getSelection();
            if (selRecords == null || selRecords.length <= 0) {
                Ext.ux.Toast.msg("温馨提示", "请先选择要禁用的用户");
                return;
            }
            var record = selRecords[0];
            if (!record.get('ableSell')) {
                Ext.ux.Toast.msg("温馨提示", "该用户已经禁用！");
                return;
            }
            Ext.MessageBox.confirm('温馨提示', '确定禁用该用户吗？', function (btn) {
                if (btn == 'yes') {
                    Ext.Ajax.request({
                        url: './shpurchase/disableSell.action',
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
        store: null,
        getStore: function () {
            var me = this;
            if (me.store == null) {
                me.store = Ext.create('MyApp.store.MainGameConfigStore', {
                    autoLoad: true,
                    listeners: {
                        beforeload: function (store, operation, eOpts) {
                            var queryForm = me.getQueryForm();
                            if (queryForm != null) {
                                var values = queryForm.getValues();
                                Ext.apply(operation, {
                                    params: {
                                        'mainGameConfig.gameName': values.name,
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
        gameConfigGrid: null,
        getGameConfigGrid: function () {
            var me = this;
            if (Ext.isEmpty(me.gameConfigGrid)) {
                me.gameConfigGrid = Ext.widget('gridpanel', {
                    header: false,
                    columnLines: true,
                    region: 'center',
                    store: me.getStore(),
                    columns: [{
                        xtype: 'rownumberer'
                    }, {
                        dataIndex: 'gameName',
                        text: '游戏名称',
                        flex: 1.2,
                        align: 'center'
                    }, {
                        dataIndex: 'gameId',
                        text: '游戏ID',
                        flex: 1.2,
                        align: 'center'
                    }, {
                        dataIndex: 'ableDelivery',
                        sortable: false,
                        flex: 0.7,
                        align: 'center',
                        text: '是否开启收货',
                        renderer: function (value, metaData, record, rowIndex, colIndex, store, view) {
                            return DataDictionary.rendererSubmitToDisplay(value, 'ableDelivery');
                        }
                    }, {
                        dataIndex: 'ableSell',
                        sortable: false,
                        flex: 1,
                        align: 'center',
                        text: '是否开启商城出售',
                        renderer: function (value, metaData, record, rowIndex, colIndex, store, view) {
                            return DataDictionary.rendererSubmitToDisplay(value, 'ableSell');
                        }
                    }, {
                        xtype: 'datecolumn',
                        dataIndex: 'createTime',
                        format: 'Y-m-d H:i:s',
                        dataIndex: 'createTime',
                        sortable: false,
                        flex: 1.5,
                        align: 'center',
                        text: '创建时间 '
                    }, {
                        xtype: 'datecolumn',
                        dataIndex: 'updateTime',
                        text: '更新时间',
                        format: 'Y-m-d H:i:s',
                        sortable: false,
                        flex: 1.5,
                        align: 'center'
                    }],
                    dockedItems: [me.getToolbar(), me.getPagingToolbar()],
                    selModel: Ext.create('Ext.selection.CheckboxModel', {
                        allowDeselect: true,
                        mode: 'SINGLE'
                    })
                });
            }
            return me.gameConfigGrid;
        },
        initComponent: function () {
            var me = this;
            Ext.applyIf(me, {
                items: [me.getQueryForm(), me.getGameConfigGrid()]
            });
            me.callParent(arguments);
        }
    }
);
Ext.define('GameName.selector.GameNameContainer', {
    extend: 'Gamegold.selector.LinkedContainer',
    alias: 'widget.gameName',
    layout: 'column',
    gameName: null,
    getGameName: getGameName,
    initComponent: function () {
        var me = this;
        me.items = [me.getGameName()];
        me.callParent();
    }
});
function getGameName() {
    var me = this;
    if (Ext.isEmpty(me.gameName)) {
        me.gameName = Ext.widget('linkedselector', {
            itemId: 'GameName_selector_GameName_ID',
            store: Ext.create('MyApp.store.MainGameConfigWithAllStore'),
            displayField: 'gameName',
            valueField: 'gameName',
            name: 'name',
            emptyText: '-请选择-',
            hideTrigger: true,
            columnWidth: .333,
            readOnly: me.readOnly,
            editable: false,
            isPaging: false,
            listeners: {
                'blur': function (ths, the, eOpts) {
                    if (Ext.isEmpty(ths.value)) {
                        this.setRawValue(null);
                    }
                }
            }
        });
    }
    return me.gameName;
}

Ext.define('MyApp.view.addMainGameConfigWindow', {
    extend: 'Ext.window.Window',
    title: '新增游戏配置信息',
    width: 800,
    border: false,
    padding: '10 10 10 10',
    autoScroll: true,
    closeAction: 'hide',
    modal: true,
    layout: 'anchor',
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
                items: [
                    Ext.create('Ext.form.ComboBox', {
                        fieldLabel: '游戏名称',
                        name: 'name',
                        columnWidth: 0.5,
                        labelWidth: 100,
                        store: Ext.create(
                            'MyApp.store.MallGameNameIdStore', {
                                autoLoad: true
                            }),
                        displayField: 'name',
                        valueField: 'name',
                        editable: false
                    }), {
                        fieldLabel: '游戏ID',
                        allowBlank: false,
                        name: 'gameId'
                    }
                ],
                buttons: [{
                    text: '保存',
                    handler: function () {
                        var formView = me.getForm(),
                            form = formView.getForm(), params,
                            record = form.getRecord(),
                            url = './shpurchase/addMainGameConfig.action',
                            message = '新增';
                        if (!form.isValid()) {
                            return;
                        }
                        form.updateRecord(record);
                        params = {};
                        form.submit({
                            url: url,
                            method: 'POST',
                            params: params,
                            success: function (from, action, json) {
                                var mainGameConfigManager = Ext.getCmp('MainGameConfigManager'),
                                    store = mainGameConfigManager.getStore();
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
    bindData: function (record) {
        var me = this,
        form = me.getForm().getForm();
        form.reset();
        form.loadRecord(record);
    }, constructor: function (config) {
        var me = this, cfg = Ext.apply({}, config);
        me.items = [me.getForm()]
        me.callParent([cfg]);
    }
});
