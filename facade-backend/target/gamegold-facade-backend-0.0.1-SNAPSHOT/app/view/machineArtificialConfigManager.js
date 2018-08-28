/*
 * 添加配置管理界面
 */

Ext.define('MyApp.view.addGetMachineArtificialConfigWindow', {
    extend: 'Ext.window.Window',
    width: 700,
    title: '新增/修改配置',
    closeAction: 'hide',
    modal: true,
    form: null,
    getForm: function () {
        var me = this;
        if (me.form == null) {
            me.form = Ext.widget('form', {
                layout: 'column',
                defaults: {
                    margin: '5 5 5 5',
                    columnWidth: .333,
                    labelWidth: 85,
                    xtype: 'textfield'
                },
                items: [Ext.create('Ext.form.ComboBox', {
                    fieldLabel: '游戏名称',
                    id: 'gameName',
                    columnWidth: 0.5,
                    store: Ext.create(
                        'MyApp.store.MallGameNameIdStore', {
                            autoLoad: true
                        }),
                    displayField: 'name',
                    valueField: 'id'
                }), DataDictionary.getDataDictionaryCombo('isEnabled', {
                    fieldLabel: '是否启用',
                    editable: false,
                    name: 'enable',
                    labelWidth: 100,
                    value: true
                })],
                buttons: [{
                    text: '保存',
                    handler: function () {
                        var formView = me.getForm(),
                            form = formView.getForm(), params,
                            record = form.getRecord(),
                            url = './repository/addMachineArtificialConfig.action',
                            message = '新增';
                        if (!form.isValid()) {
                            return;
                        }
                        form.updateRecord(record);
                        var params = {
                            'machineArtificialConfig.gameName': form.findField('gameName').getRawValue(),
                            'machineArtificialConfig.enable': record.get('enable')
                        };
                        // if (me.isUpdate) {
                        //
                        //     url = './repository/updateMachineArtificialConfig';
                        //     message = '修改';
                        //     Ext.Object.merge(params, {
                        //         'machineArtificialConfig.id': record.get('id')
                        //     });
                        // }

                        form.submit({
                            url: url,
                            params: params,
                            success: function (from, action, json) {
                                var machineArtificialConfigManager = Ext.getCmp('machineArtificialConfigManager'),
                                    store = machineArtificialConfigManager.getStore();
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
        return this.form;
    },
    isUpdate: null,
    bindData: function (record, isUpdate) {
        var me = this,
            form = me.getForm().getForm(),
            gameName = form.findField('gameName');

        form.reset();
        form.loadRecord(record);
        me.isUpdate = isUpdate;
        if (isUpdate) {
            gameName.setReadOnly(true);
        } else {
            gameName.setReadOnly(false);
            form.reset();
        }

    },
    initComponent: function () {
        var me = this;
        Ext.applyIf(me, {
            items: [me.getForm()]
        });
        me.callParent(arguments);
    }
});


/*
 * 机器转人工配置
 */
Ext.define('MyApp.view.machineArtificialConfigManager', {
    extend: 'Ext.panel.Panel',
    id: 'machineArtificialConfigManager',
    closable: true,
    title: '异常转人工开关配置',
    autoScroll: false,
    listeners: {
        'resize': function () {
            this.userGrid.setHeight(window.document.body.offsetHeight - 190);
        }
    },
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
                            fn: me.addGetMachineArtificialConfig,
                            scope: me
                        }
                    }
                }, '-', {
                    text: '删除配置',
                    listeners: {
                        click: {
                            fn: me.deleteMachineArtificialConfig,
                            scope: me
                        }
                    }
                }, '-', {
                    xtype: 'button',
                    text: '启用配置',
                    listeners: {
                        click: {
                            fn: me.enableMachineArtificialConfig,
                            scope: me
                        }
                    }
                }, '-', {
                    xtype: 'button',
                    text: '禁用配置',
                    listeners: {
                        click: {
                            fn: me.disableMachineArtificialConfig,
                            scope: me
                        }
                    }
                }]
            });
        }
        return me.toolbar;
    },
    // // 修改配置
    // modifyMachineArtificialConfig: function (button, e, eOpts) {
    //     var grid = this.getUserGrid(),
    //         selModel = grid.getSelectionModel(),
    //         selRecords = selModel.getSelection(),
    //         window = this.getMachineArtificialConfigWindow();
    //     if (selRecords == null || selRecords.length <= 0) {
    //         Ext.ux.Toast.msg("温馨提示", "请先选择要修改配置信息");
    //         return;
    //     }
    //     window.bindData(selRecords[0], true);
    //     window.getForm().getForm().findField('gameName').readOnly = true;
    //     window.getForm().getForm().findField('enable').readOnly = true;
    //     window.show();
    // },
    // 禁用配置
    disableMachineArtificialConfig: function (button, e, eOpts) {
        var grid = this.getUserGrid(),
            selModel = grid.getSelectionModel(),
            selRecords = selModel.getSelection();
        if (selRecords == null || selRecords.length <= 0) {
            Ext.ux.Toast.msg("温馨提示", "请先选择要禁用的配置");
            return;
        }
        var record = selRecords[0];
        if (!record.get('enable')) {
            Ext.ux.Toast.msg("温馨提示", "该配置已经禁用！");
            return;
        }
        Ext.MessageBox.confirm('温馨提示', '确定禁用该配置吗？', function (btn) {
            if (btn == 'yes') {
                Ext.Ajax.request({
                    url: './repository/disabledMachineArtificialConfig.action',
                    params: {'machineArtificialConfig.id': record.get('id')},
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
    // 启用配置
    enableMachineArtificialConfig: function (button, e, eOpts) {
        var grid = this.getUserGrid(),
            selModel = grid.getSelectionModel(),
            selRecords = selModel.getSelection();
        if (selRecords == null || selRecords.length <= 0) {
            Ext.ux.Toast.msg("温馨提示", "请先选择要启用的配置");
            return;
        }
        var record = selRecords[0];
        if (record.get('enable')) {
            Ext.ux.Toast.msg("温馨提示", "该配置已经启用！");
            return;
        }
        Ext.MessageBox.confirm('温馨提示', '确定启用该配置吗？', function (btn) {
            if (btn == 'yes') {
                Ext.Ajax.request({
                    url: './repository/enableMachineArtificialConfig.action',
                    params: {'machineArtificialConfig.id': record.get('id')},
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
    //删除系统配置
    deleteMachineArtificialConfig: function () {
        var me = this;
        var grid = me.getUserGrid();
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
                    url: './repository/deleteMachineArtificialConfig.action',
                    params: {'configId': record.get('id')},
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
    // 增加机器转人工配置
    addGetMachineArtificialConfigWindow: null,
    getMachineArtificialConfigWindow: function () {
        if (this.addGetMachineArtificialConfigWindow == null) {
            this.addGetMachineArtificialConfigWindow = new MyApp.view.addGetMachineArtificialConfigWindow();
        }
        return this.addGetMachineArtificialConfigWindow;
    },

    addGetMachineArtificialConfig: function (button, e, eOpts) {
        var window = this.getMachineArtificialConfigWindow();
        window.bindData(Ext.create('MyApp.model.MachineArtificialConfigModel'), false);
        window.getForm().getForm().findField('gameName').readOnly = false;
        window.show();
    },
    queryForm: null,
    getQueryForm: function () {
        var me = this;
        if (me.queryForm == null) {
            me.queryForm = Ext.widget('form', {
                layout: 'column',
                defaults: {
                    margin: '10 10 10 10',
                    columnWidth: .2,
                    labelWidth: 80,
                    xtype: 'textfield'
                },
                items: [Ext.create('Ext.form.ComboBox', {
                    fieldLabel: '游戏名称',
                    name: 'gameName',
                    id: 'goldCountComboBox',
                    columnWidth: 0.4,
                    store: Ext.create(
                        'MyApp.store.MallGameNameIdStore', {
                            autoLoad: true
                        }),
                    displayField: 'name',
                    valueField: 'id',
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
        return this.queryForm;
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
    store: null,
    getStore: function () {
        var me = this;
        if (me.store == null) {
            me.store = Ext.create('MyApp.store.MachineArtificialConfigStore', {
                autoLoad: true,
                listeners: {
                    beforeload: function (store, operation, eOpts) {
                        var queryForm = me.getQueryForm();
                        var idValue = Ext.getCmp('goldCountComboBox').getRawValue();
                        // alert(Ext.getCmp('goldCountComboBox').getRawValue())
                        if (queryForm != null) {
                            var values = queryForm.getValues();
                            // alert(idValue);
                            Ext.apply(operation, {
                                params: {
                                    'machineArtificialConfig.gameName': idValue,
                                }
                            });
                        }
                    }
                }

            });
        }
        return me.store;
    },
    userGrid: null,
    getUserGrid: function () {
        var me = this;
        if (Ext.isEmpty(me.userGrid)) {
            me.userGrid = Ext.widget('gridpanel', {
                header: false,
                columnLines: true,
                store: me.getStore(),
                columns: [{
                    dataIndex: 'gameName',
                    flex: 1,
                    align: 'center',
                    text: '游戏名称'
                }, {
                    dataIndex: 'createTime',
                    flex: 1,
                    align: 'center',
                    xtype: 'datecolumn',
                    format: 'Y-m-d H:i:s',
                    text: '创建时间'
                }, {
                    dataIndex: 'updateTime',
                    flex: 1,
                    align: 'center',
                    xtype: 'datecolumn',
                    format: 'Y-m-d H:i:s',
                    text: '更新时间'
                }, {
                    dataIndex: 'enable',
                    sortable: false,
                    flex: 0.5,
                    text: '是否启用',
                    renderer: function (value, metaData, record, rowIndex, colIndex, store, view) {
                        return DataDictionary.rendererSubmitToDisplay(value, 'isEnabled');
                    }
                }],
                dockedItems: [me.getToolbar(), me.getPagingToolbar()],
                selModel: Ext.create('Ext.selection.CheckboxModel', {
                    allowDeselect: true,
                    mode: 'MULTI'
                })
            });
        }
        return me.userGrid;
    },
    initComponent: function () {
        var me = this;
        Ext.applyIf(me, {
            items: [me.getQueryForm(), me.getUserGrid()]
        });
        me.callParent(arguments);
    }
});
