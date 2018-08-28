/*
 * 添加配置管理界面
 */

Ext.define('MyApp.view.addGetRepositoryConfigWindow', {
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
                    fieldLabel : '游戏名称',
                    id : 'gameName',
                    columnWidth : 0.5,
                    store : Ext.create(
                        'MyApp.store.MallGameNameIdStore', {
                            autoLoad : true
                        }),
                    displayField : 'name',
                    valueField : 'id'
                }), Ext.create('Ext.form.ComboBox', {
                    fieldLabel : '商品类型',
                    id : 'goodsTypeName',
                    columnWidth : 0.5,
                    store : Ext.create(
                        'MyApp.store.GameCategoryConfigStore', {
                            autoLoad : true
                        }),
                    displayField : 'name',
                    valueField : 'id'
                }),{
                    name: 'repositoryCount',
                    allowBlank: false,
                    fieldLabel: '库存限制',
                }, DataDictionary.getDataDictionaryCombo('isEnabled', {
                    fieldLabel: '是否启用',
                    editable: false,
                    name: 'isEnabled',
                    labelWidth: 100,
                    value: true
                })],
                buttons: [{
                    text: '保存',
                    handler: function () {
                        var formView = me.getForm(),
                            form = formView.getForm(), params,
                            record = form.getRecord(),
                            url = './repository/addRepositoryConfig.action',
                            message = '新增';
                        if (!form.isValid()) {
                            return;
                        }
                        form.updateRecord(record);
                        var count = record.get("repositoryCount");
                        var  re = /^\d*$/;
                        if (!re.test(count)) {
                            Ext.ux.Toast.msg("温馨提示请填写正确的库存数量");
                            return;
                        }
                        var params = {
                            'repositoryConfine.gameName': form.findField('gameName').getRawValue(),
                            'repositoryConfine.repositoryCount': record.get('repositoryCount'),
                            'repositoryConfine.goodsTypeName': form.findField('goodsTypeName').getRawValue(),
                            'repositoryConfine.isEnabled': record.get('isEnabled')
                        };
                        if (me.isUpdate) {
                            url = './repository/modifyRepositoryConfig.action';
                            message = '修改';
                            Ext.Object.merge(params, {
                                'repositoryConfine.id': record.get('id')
                            });
                        }

                        form.submit({
                            url: url,
                            params: params,
                            success: function (from, action, json) {
                                var RepositoryConfineManager = Ext.getCmp('RepositoryConfineManager'),
                                    store = RepositoryConfineManager.getStore();
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
            gameName = form.findField('gameName'),
            repositoryCount = form.findField('repositoryCount');

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

/*Ext.define('MyApp.view.addGetRepositoryCountWindow', {
        extend: 'Ext.window.Window',
        width: 700,
        title: '修改通用游戏库存限制配置',
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
                items: [{
                    name: 'repositoryCount',
                    allowBlank: false,
                    labelWidth: 150,
                    fieldLabel: '通用游戏库存限制配置'
                }],
                buttons: [{
                    text: '保存',
                    handler: function () {
                        var formView = me.getForm(),
                            form = formView.getForm(), params,
                            url = './repository/updateRepositoryConfig.action',
                            message = '修改';
                        console.log("我要之"+form.getValues().repositoryCount);
                        var  reg = /^\d*$/;
                        if (!reg.test(form.getValues().repositoryCount)) {
                            Ext.ux.Toast.msg("请填写正确的通用游戏限制库存数量！");
                            return;
                        }
                        form.submit({
                            url: url,
                            params: params,
                            success: function (from, action, json) {
                                var RepositoryConfineManager = Ext.getCmp('RepositoryConfineManager'),
                                    store = RepositoryConfineManager.getStore();
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
            repositoryCount = form.findField('repositoryCount');

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
});*/


/*
 * 参考价库存限制配置管理
 */
Ext.define('MyApp.view.RepositoryConfineManager', {
    extend: 'Ext.panel.Panel',
    id: 'RepositoryConfineManager',
    closable: true,
    title: '参考价库存限制配置管理',
    ////////////////////////////////////////////////////
    autoScroll: false,
    listeners: {
        'resize': function () {
            this.userGrid.setHeight(window.document.body.offsetHeight - 190);
        }
    },
    ////////////////////////////////////////////////////
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
                            fn: me.addGetRepositoryConfig,
                            scope: me
                        }
                    }
                }, '-', {
                    text: '删除配置',
                    listeners: {
                        click: {
                            fn: me.deleteRepositoryConfig,
                            scope: me
                        }
                    }
                }, '-', {
                    text: '修改库存配置',
                    listeners: {
                        click: {
                            fn: me.modifyRepositoryConfig,
                            scope: me
                        }
                    }
                },'-', {
                    xtype: 'button',
                    text: '启用配置',
                    listeners: {
                        click: {
                            fn: me.enableRepositoryConfig,
                            scope: me
                        }
                    }
                }, '-', {
                    xtype: 'button',
                    text: '禁用配置',
                    listeners: {
                        click: {
                            fn: me.disableRepositoryConfig,
                            scope: me
                        }
                    }
                }]
            });
        }
        return me.toolbar;
    },
    //修改通用游戏限制配置
    /*modifyRepositoryCount: function (button, e, eOpts) {
        var json;
        var window = this.getRepositoryCountWindow();
        Ext.Ajax.request({
            url: './repository/queryRepositoryCountIdList.action',
            // params: {'repositoryConfine.id': record.get('id')},
            method: 'POST',
            success: function (response, opts) {
                // alert(response+"-=-----");

                json = Ext.decode(response.responseText);
                // json =  json.repositoryCountList[0].name;
                console.log("=="+json.repositoryCountList[0].name);
                // console.log("=====",json);
                // Ext.ux.Toast.msg("温馨提示", json.repositoryCountList[0].name);
                window.getForm().getForm().findField('repositoryCount').setValue(json.repositoryCountList[0].name);
                window.getForm().getForm().findField('repositoryCount').readOnly = false;
                window.show();
            }
        });


    },*/
    // 修改系统配置地信息
    modifyRepositoryConfig: function (button, e, eOpts) {
        var grid = this.getUserGrid(),
            selModel = grid.getSelectionModel(),
            selRecords = selModel.getSelection(),
            window = this.getRepositoryConfigWindow();
        if (selRecords == null || selRecords.length <= 0) {
            Ext.ux.Toast.msg("温馨提示", "请先选择要修改配置信息");
            return;
        }
        window.bindData(selRecords[0], true);
        window.getForm().getForm().findField('gameName').readOnly = true;
        window.getForm().getForm().findField('isEnabled').readOnly = true;
        window.show();

    },
    // 禁用配置
    disableRepositoryConfig: function (button, e, eOpts) {
        var grid = this.getUserGrid(),
            selModel = grid.getSelectionModel(),
            selRecords = selModel.getSelection();
        if (selRecords == null || selRecords.length <= 0) {
            Ext.ux.Toast.msg("温馨提示", "请先选择要禁用的配置");
            return;
        }
        var record = selRecords[0];
        if (!record.get('isEnabled')) {
            Ext.ux.Toast.msg("温馨提示", "该配置已经禁用！");
            return;
        }
        Ext.MessageBox.confirm('温馨提示', '确定禁用该用户吗？', function (btn) {
            if (btn == 'yes') {
                Ext.Ajax.request({
                    url: './repository/disableRepositoryConfig.action',
                    params: {'repositoryConfine.id': record.get('id')},
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
    enableRepositoryConfig: function (button, e, eOpts) {
        var grid = this.getUserGrid(),
            selModel = grid.getSelectionModel(),
            selRecords = selModel.getSelection();
        if (selRecords == null || selRecords.length <= 0) {
            Ext.ux.Toast.msg("温馨提示", "请先选择要启用的配置");
            return;
        }
        var record = selRecords[0];
        if (record.get('isEnabled')) {
            Ext.ux.Toast.msg("温馨提示", "该配置已经启用！");
            return;
        }
        Ext.MessageBox.confirm('温馨提示', '确定启用该配置吗？', function (btn) {
            if (btn == 'yes') {
                Ext.Ajax.request({
                    url: './repository/enableRepositoryConfig.action',
                    params: {'repositoryConfine.id': record.get('id')},
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
    deleteRepositoryConfig: function () {
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
                    url: './repository/deleteRepositoryConfig.action',
                    params: {'repositoryConfine.id': record.get('id')},
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
    //修改通用游戏限制配置库存信息
/*    addGetRepositoryCountWindow: null,
    getRepositoryCountWindow: function () {
        if (this.addGetRepositoryCountWindow == null) {
            this.addGetRepositoryCountWindow = new MyApp.view.addGetRepositoryCountWindow();
        }
        return this.addGetRepositoryCountWindow;
    },*/

    // 增加参考价库存管理配置信息
    addGetRepositoryConfigWindow: null,
    getRepositoryConfigWindow: function () {
        if (this.addGetRepositoryConfigWindow == null) {
            this.addGetRepositoryConfigWindow = new MyApp.view.addGetRepositoryConfigWindow();
        }
        return this.addGetRepositoryConfigWindow;
    },

    addGetRepositoryConfig: function (button, e, eOpts) {
        var window = this.getRepositoryConfigWindow();
        window.bindData(Ext.create('MyApp.model.RepositoryConfigModel'), false);
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
                    columnWidth: 0.5,
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
            me.store = Ext.create('MyApp.store.RepositoryConfineStore', {
                autoLoad: true,
                listeners: {
                    beforeload: function (store, operation, eOpts) {
                        var queryForm = me.getQueryForm();
                        var idValue = Ext.getCmp('goldCountComboBox').getRawValue();
                       // alert(Ext.getCmp('goldCountComboBox').getRawValue())
                        if (queryForm != null) {
                            var values = queryForm.getValues();
                            Ext.apply(operation, {
                                params: {
                                    'repositoryConfine.gameName': idValue,
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
                    dataIndex: 'goodsTypeName',
                    flex: 1,
                    align: 'center',
                    text: '商品类型'
                },{
                    dataIndex: 'repositoryCount',
                    flex: 1,
                    align: 'center',
                    text: '库存限制'
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
                    dataIndex: 'isEnabled',
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
