
//出货模式配置
Ext.define('MyApp.view.qqOnLineManager', {
    extend: 'Ext.panel.Panel',
    id: 'qqOnLineManager',
    layout: "border",
    closable: true,
    title: '客服QQ',
    toolbar: null,
    getToolbar: function () {
        var me = this;
        if (Ext.isEmpty(me.toolbar)) {
            me.toolbar = Ext.widget('toolbar', {
                dock: 'top',
                items: [{
                    text: '添加QQ',
                    listeners: {
                        click: {
                            fn: me.addShTrade,
                            scope: me
                        }
                    }
                }, '-', {
                    text: '修改QQ',
                    listeners: {
                        click: {
                            fn: me.updateShTrade,
                            scope: me
                        }
                    }
                }, '-', {
                    text: '删除QQ',
                    listeners: {
                        click: {
                            fn: me.deleteShTrade,
                            scope: me
                        }
                    }
                }, '-', {
                    xtype: 'button',
                    itemId: 'enableButton',
                    text: '上线',
                    listeners: {
                        click: {
                            fn: me.enableTrade,
                            scope: me
                        }
                    }
                }, '-', {
                    xtype: 'button',
                    itemId: 'disableButton',
                    text: '下线',
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
    // 修改系统QQ地信息
    updateShTrade: function (button, e, eOpts) {
        var grid = this.getShTradeGrid(),
            selModel = grid.getSelectionModel(),
            selRecords = selModel.getSelection(),
            window = this.getAddTradeWindow();
        if (selRecords == null || selRecords.length <= 0) {
            Ext.ux.Toast.msg("温馨提示", "请先选择要修改QQ信息");
            return;
        }
        window.bindData(selRecords[0], true);
        window.show();
    },
    // 上线
    enableTrade: function (button, e, eOpts) {
        var grid = this.getShTradeGrid(),
            selModel = grid.getSelectionModel(),
            selRecords = selModel.getSelection();
        if (selRecords == null || selRecords.length <= 0) {
            Ext.ux.Toast.msg("温馨提示", "请先选择要上线的用户");
            return;
        }
        var record = selRecords[0];
        if (record.get('online')) {
            Ext.ux.Toast.msg("温馨提示", "该用户已经上线！");
            return;
        }
        Ext.MessageBox.confirm('温馨提示', '确定上线该用户吗？', function (btn) {
            if (btn == 'yes') {
                Ext.Ajax.request({
                    url: './delivery/enabledQqOnLine.action',
                    params: {'id': record.get('id')},
                    success: function (response, opts) {
                        Ext.ux.Toast.msg("温馨提示", "上线成功！");
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
    // 下线
    disableTrade: function (button, e, eOpts) {
        var grid = this.getShTradeGrid(),
            selModel = grid.getSelectionModel(),
            selRecords = selModel.getSelection();
        if (selRecords == null || selRecords.length <= 0) {
            Ext.ux.Toast.msg("温馨提示", "请先选择要下线的用户");
            return;
        }
        var record = selRecords[0];
        if (!record.get('online')) {
            Ext.ux.Toast.msg("温馨提示", "该用户已经下线！");
            return;
        }
        Ext.MessageBox.confirm('温馨提示', '确定下线该用户吗？', function (btn) {
            if (btn == 'yes') {
                Ext.Ajax.request({
                    url: './delivery/disabledQqOnLine.action',
                    params: {'id': record.get('id')},
                    success: function (response, opts) {
                        Ext.ux.Toast.msg("温馨提示", "下线成功！");
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
            Ext.ux.Toast.msg("温馨提示", "请先选择要删除的QQ");
            return;
        }
        var record = selRecords[0];
        Ext.MessageBox.confirm('温馨提示', '确定删除吗？', function (btn) {
            if (btn == 'yes') {
                me.getStore().remove(record);
                Ext.Ajax.request({
                    url: './delivery/deleteQqOnLine.action',
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
                    fieldLabel: '客服QQ',
                    name: 'qqNumber'
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
            me.store = Ext.create('MyApp.store.QQOnLineStore', {
                autoLoad: true,
                listeners: {
                    beforeload: function (store, operation, eOpts) {
                        var queryForm = me.getQueryForm();
                        if (queryForm != null) {
                            var values = queryForm.getValues();
                            var params = {
                                'qqNumber': values.qqNumber,
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
    updateTrade: null,
    getUpdateTrade: function () {
        if (this.updateWindow == null) {
            this.updateWindow = new MyApp.view.updateTrade();
        }
        return this.updateWindow;
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
                    dataIndex: 'qqNumber',
                    text: 'QQ号码',
                    flex: 1.7,
                    align: 'center'
                },{
                    dataIndex: 'nickname',
                    text: 'QQ昵称',
                    flex: 1.7,
                    align: 'center'
                },{
                    dataIndex: 'online',
                    sortable: false,
                    flex: 1,
                    align: 'center',
                    text: '是否上线',
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
                    dataIndex: 'lastUpdateTime',
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

//增加QQ
Ext.define('MyApp.view.addTradeWindow', {
    extend: 'Ext.window.Window',
    title: '新增/修改QQ信息',
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
                    fieldLabel: 'QQ号码',
                    allowBlank: false,
                    name: 'qqNumber'
                },{
                    fieldLabel: 'QQ昵称',
                    allowBlank: false,
                    name: 'nickname'
                }],
                buttons: [{
                    text: '保存',
                    handler: function () {
                        var formView = me.getForm(),
                            form = formView.getForm(), params,
                            record = form.getRecord(),
                            url = './delivery/addQqOnLine.action',
                            message = '新增';
                        if (!form.isValid()) {
                            return;
                        }
                        form.updateRecord(record);
                        params = {
                            'qqOnLineEO.qqNumber': form.findField('qqNumber').getRawValue(),
                            'qqOnLineEO.nickname': form.findField('nickname').getRawValue(),
                        };
                        if (me.isUpdate) {
                            url = './delivery/updateQqOnLine.action';
                            message = '修改';
                            Ext.Object.merge(params, {
                                'qqOnLineEO.id': record.get('id'),
                                'qqOnLineEO.qqNumber': record.get('qqNumber'),
                                'qqOnLineEO.nickname': record.get('nickname'),
                            });
                        }
                        form.submit({
                            url: url,
                            method: 'POST',
                            params: params,
                            success: function (from, action, json) {
                                var qqOnLineManager = Ext.getCmp('qqOnLineManager'),
                                    store = qqOnLineManager.getStore();
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





