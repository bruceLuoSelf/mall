/*
 * 添加配置管理界面
 */

Ext.define('MyApp.view.addGetRepositoryWindow', {
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
                items: [{
                    xtype: 'gamelinkselector',
                    itemId: 'MyApp_view_goods_gamelink_ID',
                    columnWidth: .7,
                    allowBlank: true,
                    fieldLabel: '游戏属性'
                },  Ext.create('Ext.form.ComboBox', {
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
                }, DataDictionary.getDataDictionaryCombo('check', {
                    fieldLabel: '是否启用',
                    editable: false,
                    name: 'enabled',
                    value: true
                })],
                buttons: [{
                    text: '保存',
                    handler: function () {

                        var formView = me.getForm(),
                            form = formView.getForm(),
                            record = form.getRecord(),
                            url = './repository/addGameRepository.action',

                            gameId = form.findField('gameName').getRawValue(),
                            regionId = form.findField('region').getRawValue(),
                            serverId = form.findField('server').getRawValue(),
                            raceId = form.findField('gameRace').getRawValue()
                        message = '新增';

                        if (!form.isValid()) {
                            return;
                        }
                        form.updateRecord(record);
                        var count = record.get("repositoryCount");
                        var re = /^\d*$/;
                        if (!re.test(count)) {
                            Ext.ux.Toast.msg("温馨提示请填写正确的库存数量");
                            return;
                        }
                        var params = {
                            'repositoryConfineInfo.repositoryCount': record.get('repositoryCount'),
                            'repositoryConfineInfo.enabled': record.get('enabled'),
                            'repositoryConfineInfo.gameName': gameId,
                            'repositoryConfineInfo.regionName': regionId,
                            'repositoryConfineInfo.serverName': serverId,
                            'repositoryConfineInfo.raceName': raceId,
                            'repositoryConfineInfo.goodsTypeName': form.findField('goodsTypeName').getRawValue(),
                        };
                        if (me.isUpdate) {
                            url = './repository/updateGameRepository.action';
                            message = '修改';
                            Ext.Object.merge(params, {
                                'repositoryConfineInfo.id': record.get('id')
                            });
                        }
                        form.submit({
                            url: url,
                            params: params,
                            success: function (from, action, json) {
                                var RepositoryConfineManager = Ext.getCmp('GameRepositoryManager'),
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

Ext.define('MyApp.view.addGetRepositoryCountWindow', {
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
                items: [{
                    name: 'repositoryCount',
                    allowBlank: false,
                    fieldLabel: '库存限制',
                }, DataDictionary.getDataDictionaryCombo('check', {
                    fieldLabel: '是否启用',
                    editable: false,
                    name: 'enabled',
                    value: true
                })],
                buttons: [{
                    text: '保存',
                    handler: function () {

                        var formView = me.getForm(),
                            form = formView.getForm(),
                            record = form.getRecord(),
                            url = './repository/addGameRepository.action',


                            message = '新增';

                        if (!form.isValid()) {
                            return;
                        }
                        form.updateRecord(record);
                        var count = record.get("repositoryCount");
                        var re = /^\d*$/;
                        if (!re.test(count)) {
                            Ext.ux.Toast.msg("温馨提示请填写正确的库存数量");
                            return;
                        }
                        var params = {
                            'repositoryConfineInfo.repositoryCount': record.get('repositoryCount'),
                            'repositoryConfineInfo.enabled': record.get('enabled'),

                        };
                        if (me.isUpdate) {
                            url = './repository/updateGameRepository.action';
                            message = '修改';
                            Ext.Object.merge(params, {
                                'repositoryConfineInfo.id': record.get('id')
                            });
                        }
                        form.submit({
                            url: url,
                            params: params,
                            success: function (from, action, json) {
                                var RepositoryConfineManager = Ext.getCmp('GameRepositoryManager'),
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
        // gameName = form.findField('gameName'),
            repositoryCount = form.findField('repositoryCount');

        form.reset();
        form.loadRecord(record);
        me.isUpdate = isUpdate;
        if (isUpdate) {
            // gameName.setReadOnly(true);
        } else {
            //gameName.setReadOnly(false);
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
 * 参考价库存限制配置管理
 */
Ext.define('MyApp.view.GameRepositoryManager', {
    extend: 'Ext.panel.Panel',
    id: 'GameRepositoryManager',
    closable: true,
    layout: "border",
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
                            fn: me.modifyGameConfig,
                            scope: me
                        }
                    }
                }, '-', {
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
                }, '-', {
                    xtype: 'button',
                    text: '全局更新',
                    listeners: {
                        click: {
                            fn: me.updateAllForLongTime,
                            scope: me
                        }
                    }
                }]
            });
        }
        return me.toolbar;
    },
    //修改通用游戏限制配置
    modifyRepositoryCount: function (button, e, eOpts) {
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
                console.log("==" + json.repositoryCountList[0].name);
                // console.log("=====",json);
                // Ext.ux.Toast.msg("温馨提示", json.repositoryCountList[0].name);
                window.getForm().getForm().findField('repositoryCount').setValue(json.repositoryCountList[0].name);
                window.getForm().getForm().findField('repositoryCount').readOnly = false;
                window.show();
            }
        });


    },
    // 修改游戏配置地信息
    modifyGameConfig: function (button, e, eOpts) {
        var grid = this.getUserGrid(),
            selModel = grid.getSelectionModel(),
            selRecords = selModel.getSelection();
        var window = this.getRepositoryCountWindow();
        if (selRecords == null || selRecords.length <= 0) {
            Ext.ux.Toast.msg("温馨提示", "请先选择要修改的游戏配置信息");
            return;
        }
        window.bindData(selRecords[0], true);
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
        if (!record.get('enabled')) {
            Ext.ux.Toast.msg("温馨提示", "该配置已经禁用！");
            return;
        }
        Ext.MessageBox.confirm('温馨提示', '确定禁用该用户吗？', function (btn) {
            if (btn == 'yes') {
                Ext.Ajax.request({
                    url: './repository/disableRepository.action',
                    params: {'repositoryConfineInfo.id': record.get('id')},
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
    //全局更新
    updateAllForLongTime: function (button, e, eOpts) {
        var grid = this.getUserGrid();
        Ext.MessageBox.confirm('温馨提示', '确定要全局更新吗？这可能要花费很长时间', function (btn) {
            if (btn == 'yes') {
                Ext.Ajax.request({
                    url: './repository/updateAll.action',
                    success: function (response, opts) {
                        Ext.ux.Toast.msg('温馨提示', '更新成功！');
                        grid.getStore().load();
                    },
                    exception: function (response, opts) {
                        var json = Ext.decode(response.responseText);
                        Ext.ux.msg('温馨提示', json.message);
                    }
                })
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
                    url: './repository/enableRepository.action',
                    params: {'repositoryConfineInfo.id': record.get('id')},
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
                    url: './repository/deleteGameRepository.action',
                    params: {'repositoryConfineInfo.id': record.get('id')},
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
    addGetRepositoryCountWindow: null,
    getRepositoryCountWindow: function () {
        if (this.addGetRepositoryCountWindow == null) {
            this.addGetRepositoryCountWindow = new MyApp.view.addGetRepositoryCountWindow();
        }
        return this.addGetRepositoryCountWindow;
    },

    // 增加参考价库存管理配置信息
    addGetRepositoryWindow: null,
    getRepositoryConfigWindow: function () {
        if (this.addGetRepositoryWindow == null) {
            this.addGetRepositoryWindow = new MyApp.view.addGetRepositoryWindow();
        }
        return this.addGetRepositoryWindow;
    },


    addGetRepositoryConfig: function (button, e, eOpts) {
        var window = this.getRepositoryConfigWindow();
        window.bindData(Ext.create('MyApp.model.GameRepositoryModel'), false);
        window.getForm().getForm().findField('gameName').readOnly = false;
        window.show();
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
                    columnWidth: .2,
                    labelWidth: 80,
                    xtype: 'textfield'
                },
                items: [{
                    xtype: 'gamelinkselector',
                    itemId: 'MyApp_view_goods_gamelink_ID',
                    columnWidth: .7,
                    allowBlank: true,
                    fieldLabel: '游戏属性'
                },Ext.create('Ext.form.ComboBox', {
                    fieldLabel: '商品类目',
                    columnWidth: .20,
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
            me.store = Ext.create('MyApp.store.GameRepositoryStore', {
                autoLoad: true,
                listeners: {
                    beforeload: function (store, operation, eOpts) {
                        var queryForm = me.getQueryForm();

                        var gameId = queryForm.getForm().findField('gameName').getRawValue();
                        var regionId = queryForm.getForm().findField('region').getRawValue();
                        var seId = queryForm.getForm().findField('server').getRawValue();
                        var raceId = queryForm.getForm().findField('gameRace').getRawValue();


                        if (queryForm != null) {
                            var values = queryForm.getValues();
                            Ext.apply(operation, {
                                params: {
                                    'repositoryConfineInfo.gameName': gameId,
                                    'repositoryConfineInfo.regionName': regionId,
                                    'repositoryConfineInfo.serverName': seId,
                                    'repositoryConfineInfo.raceName': raceId,
                                    'repositoryConfineInfo.goodsTypeName':Ext.String.trim(values.goodsTypeName)
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
                region: 'center',
                store: me.getStore(),
                columns: [{
                    dataIndex: 'gameName',
                    flex: 1,
                    align: 'center',
                    text: '游戏名称'
                },{
                    dataIndex: 'goodsTypeName',
                    flex: 1,
                    align: 'center',
                    text: '商品类型'
                }, {
                    dataIndex: 'regionName',
                    flex: 1,
                    align: 'center',
                    text: '游戏区'
                }, {
                    dataIndex: 'serverName',
                    flex: 1,
                    align: 'center',
                    text: '游戏服'
                }, {
                    dataIndex: 'raceName',
                    flex: 1,
                    align: 'center',
                    text: '游戏阵营'
                }, {
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
                    dataIndex: 'enabled',
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
