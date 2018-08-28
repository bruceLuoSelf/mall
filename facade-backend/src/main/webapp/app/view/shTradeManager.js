
//出货模式配置
Ext.define('MyApp.view.shTradeManager', {
    extend: 'Ext.panel.Panel',
    id: 'shTradeManager',
    layout: "border",
    closable: true,
    title: '收货模式配置管理',
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
                            fn: me.addShTrade,
                            scope: me
                        }
                    }
                }, '-', {
                    text: '修改配置',
                    listeners: {
                        click: {
                            fn: me.updateShTrade,
                            scope: me
                        }
                    }
                }, '-', {
                    text: '删除配置',
                    listeners: {
                        click: {
                            fn: me.deleteShTrade,
                            scope: me
                        }
                    }
                }, '-', {
                    xtype: 'button',
                    itemId: 'enableButton',
                    text: '启用',
                    listeners: {
                        click: {
                            fn: me.enableTrade,
                            scope: me
                        }
                    }
                }, '-', {
                    xtype: 'button',
                    itemId: 'disableButton',
                    text: '禁用',
                    listeners: {
                        click: {
                            fn: me.disableTrade,
                            scope: me
                        }
                    }
                }]
            });
        }
        return me.toolbar;
    },

    addTradeWindow: null,
    getAddTradeWindow: function () {
        if (this.addTradeWindow == null) {
            this.addTradeWindow = new MyApp.view.addTradeWindow();
        }
        return this.addTradeWindow;
    },

    addShTrade: function (button, e, eOpts) {
        var window = this.getAddTradeWindow();
        window.bindData(Ext.create('MyApp.model.ShTradeModel'), false);
        window.show();
    },
    // 修改系统配置地信息
    updateShTrade: function (button, e, eOpts) {
        var grid = this.getShTradeGrid(),
            selModel = grid.getSelectionModel(),
            selRecords = selModel.getSelection(),
            window = this.getAddTradeWindow();
        if (selRecords == null || selRecords.length <= 0) {
            Ext.ux.Toast.msg("温馨提示", "请先选择要修改配置信息");
            return;
        }
        window.bindData(selRecords[0], true);
        window.show();
    },
    // 启用
    enableTrade: function (button, e, eOpts) {
        var grid = this.getShTradeGrid(),
            selModel = grid.getSelectionModel(),
            selRecords = selModel.getSelection();
        if (selRecords == null || selRecords.length <= 0) {
            Ext.ux.Toast.msg("温馨提示", "请先选择要启用的配置");
            return;
        }
        var record = selRecords[0];
        if (record.get('enabled')) {
            Ext.ux.Toast.msg("温馨提示", "该配置已经启用！");
            return;
        }
        Ext.MessageBox.confirm('温馨提示', '确定启用该配置吗？', function (btn) {
            if (btn == 'yes') {
                Ext.Ajax.request({
                    url: './delivery/enabledTrade.action',
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
    // 禁用
    disableTrade: function (button, e, eOpts) {
        var grid = this.getShTradeGrid(),
            selModel = grid.getSelectionModel(),
            selRecords = selModel.getSelection();
        if (selRecords == null || selRecords.length <= 0) {
            Ext.ux.Toast.msg("温馨提示", "请先选择要禁用的配置");
            return;
        }
        var record = selRecords[0];
        if (!record.get('enabled')) {
            Ext.ux.Toast.msg("温馨提示", "该配置已经禁用！");
            return;
        }
        Ext.MessageBox.confirm('温馨提示', '确定禁用该配置吗？', function (btn) {
            if (btn == 'yes') {
                Ext.Ajax.request({
                    url: './delivery/disabledTrade.action',
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



    deleteShTrade: function () {
        var me = this,
            grid = me.getShTradeGrid(),
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
                    url: './delivery/deleteTrade.action',
                    params: {'id': record.get('id')},
                    method: 'POST',

                    success: function (response, opts) {
                        Ext.ux.Toast.msg("温馨提示", "删除成功！");
                        me.getStore().load();
                        grid.getSelectionModel().deselectAll();
                    },
                    exception: function (response, opts) {
                        var json = Ext.decode(response.responseText);
                        Ext.ux.Toast.msg("温馨提示", json.message);
                        me.getStore().load();
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
                    fieldLabel: '收货配置',
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
            me.store = Ext.create('MyApp.store.ShTradeStore', {
                autoLoad: true,
                listeners: {
                    beforeload: function (store, operation, eOpts) {
                        var queryForm = me.getQueryForm();
                        if (queryForm != null) {
                            var values = queryForm.getValues();
                            var params = {
                                'trade.name': values.name,
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
    ShTradeGrid: null,
    getShTradeGrid: function () {
        var me = this;
        if (Ext.isEmpty(me.ShTradeGrid)) {
            me.ShTradeGrid = Ext.widget('gridpanel', {
                region: 'center',
                header: false,
                columnLines: true,
                store: me.getStore(),
                columns: [{
                    xtype: 'rownumberer'
                },{
                    dataIndex: 'name',
                    text: '收货模式',
                    flex: 1.7,
                    align: 'center'
                },{
                    dataIndex: 'tradeLogo',
                    text: '收货模式标识',
                    flex: 1.7,
                    align: 'center'
                },{
                    dataIndex: 'enabled',
                    sortable: false,
                    flex: 1,
                    align: 'center',
                    text: '是否启用',
                    renderer: function (value, metaData, record, rowIndex, colIndex, store, view) {
                        return DataDictionary.rendererSubmitToDisplay(value, 'check');
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
                    dataIndex: 'updateTime',
                    sortable: false,
                    flex: 1.5,
                    align: 'center',
                    text: '最后修改时间 '
                }],
                dockedItems: [me.getToolbar(), me.getPagingToolbar()],
                selModel: Ext.create('Ext.selection.CheckboxModel', {
                    allowDeselect: true,
                    mode: 'MULTI'
                })
            });
        }
        return me.ShTradeGrid;
    },
    initComponent: function () {
        var me = this;
        Ext.applyIf(me, {
            items: [me.getQueryForm(), me.getShTradeGrid()]
        });
        me.callParent(arguments);
    }
});

//增加出货系统配置
Ext.define('MyApp.view.addTradeWindow', {
    extend: 'Ext.window.Window',
    title: '新增/修改收货模式配置信息',
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
                items: [
                    {
                    xtype: 'hidden',
                    name: 'id'
                },{
                    fieldLabel: '收货模式',
                    allowBlank: false,
                    name: 'name'
                },{
                    fieldLabel: '收货模式标识',
                    allowBlank: false,
                    xtype: 'numberfield',
                    name: 'tradeLogo'
            }],
                buttons: [{
                    text: '保存',
                    handler: function () {
                        var formView = me.getForm(),
                            form = formView.getForm(),
                            params,
                            record = form.getRecord(),
                            url = './delivery/addTrade.action',
                            message = '新增';
                        if (!form.isValid()) {
                            return;
                        }
                        console.log(form.findField("id").getValue())
                        params = {
                            'trade.name': form.findField("name").getValue(),
                            'trade.tradeLogo':form.findField("tradeLogo").getValue()
                        };
                        if (me.isUpdate) {
                            url = './delivery/updateTrade.action';
                            message = '修改';
                            params = {
                                'trade.id': form.findField("id").getValue(),
                                'trade.tradeLogo':form.findField("tradeLogo").getValue()
                            };
                            // Ext.Object.merge(params, {
                            //     'trade.id': record.get('id')
                            // });
                        }
                        form.submit({
                            url: url,
                            method: 'POST',
                            params: params,
                            success: function (from, action, json) {
                                var tradeManager = Ext.getCmp('shTradeManager'),
                                    store = tradeManager.getStore();
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
            form.findField("name").setReadOnly(false)
            form.findField("name").setDisabled(false)
        }else {
            form.findField("name").setReadOnly(true)
            form.findField("name").setDisabled(true)
        }
    },
    constructor: function (config) {
        var me = this, cfg = Ext.apply({}, config);
        me.items = [me.getForm()]
        me.callParent([cfg]);
    }
});





