/**
 * 通货配置
 */
Ext.define('MyApp.view.CurrencyConfigManager', {
    extend: 'Ext.panel.Panel',
    id: 'CurrencyConfigManager',
    closable: true,
    title: '通货配置',
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
                            fn: me.addCurrencyConfig,
                            scope: me
                        }
                    }
                }, '-', {
                    text: '删除配置',
                    listeners: {
                        click: {
                            fn: me.deleteCurrencyConfig,
                            scope: me
                        }
                    }
                }, '-', {
                    text: '修改配置',
                    listeners: {
                        click: {
                            fn: me.modifyCuurencyConfig,
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
    modifyCuurencyConfig: function (button, e, eOpts) {
        var grid = this.getUserGrid(),
            selModel = grid.getSelectionModel(),
            selRecords = selModel.getSelection(),
            window = this.getCurrencyConfigWindow();
        if (selRecords == null || selRecords.length <= 0) {
            Ext.ux.Toast.msg("温馨提示", "请先选择要修改配置信息");
            return;
        }
        window.bindData(selRecords[0], true);
        window.show();

    },
    //删除系统配置
    deleteCurrencyConfig: function () {
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
                    url: './CurrencyConfig/deleteCurrencyConfig.action',
                    params: {'currencyConfigEO.id': record.get('id')},
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

    // 增加配置信息
    addCurrencyConfigWindow: null,
    getCurrencyConfigWindow: function () {
        if (this.addCurrencyConfigWindow == null) {
            this.addCurrencyConfigWindow = new MyApp.view.addCurrencyConfigWindow();
        }
        return this.addCurrencyConfigWindow;
    },

    addCurrencyConfig: function (button, e, eOpts) {
        var window = this.getCurrencyConfigWindow();
        window.bindData(Ext.create('MyApp.model.CurrencyConfigModel'), false);
        // window.getForm().getForm().findField('gameName').readOnly = false;
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
                items: [{
                    xtype: 'gameselectorsellersetting',
                    itemId : 'MyApp_view_goods_gamelink_ID',
                    columnWidth: .45,
                    allowBlank: true,
                    fieldLabel: '游戏名称'
                }],
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
            me.store = Ext.create('MyApp.store.CurrencyConfigStore', {
                autoLoad: true,
                listeners: {
                    beforeload: function (store, operation, eOpts) {
                        var queryForm = me.getQueryForm();
                        // var idValue = Ext.getCmp('goldCountComboBox').getRawValue();
                        // alert(Ext.getCmp('goldCountComboBox').getRawValue())
                        if (queryForm != null) {
                            var values = queryForm.getValues();
                            Ext.apply(operation, {
                                params: {
                                    'currencyConfigEO.gameName':queryForm.getForm().findField("gameName").getRawValue()
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
                    xtype: 'rownumberer'
                },{
                    dataIndex: 'gameName',
                    flex: 1,
                    align: 'center',
                    text: '游戏名称'
                }, {
                    dataIndex: 'goodsType',
                    sortable: false,
                    flex: 0.5,
                    text: '商品类型',
                    renderer: function (value, metaData, record, rowIndex, colIndex, store, view) {
                        return DataDictionary.rendererSubmitToDisplay(value, 'goodsType');
                    }
                }, {
                    dataIndex: 'enabled',
                    sortable: false,
                    flex: 0.5,
                    text: '是否必填',
                    renderer: function (value, metaData, record, rowIndex, colIndex, store, view) {
                        return DataDictionary.rendererSubmitToDisplay(value, 'enabled');
                    }
                }, {
                    dataIndex: 'field',
                    sortable: false,
                    flex: 0.5,
                    text: '字段',
                    renderer: function (value, metaData, record, rowIndex, colIndex, store, view) {
                        return DataDictionary.rendererSubmitToDisplay(value, 'field');
                    }
                }, {
                    dataIndex: 'fieldMeaning',
                    flex: 1,
                    align: 'center',
                    text: '字段含义'
                }, {
                    dataIndex: 'fieldType',
                    sortable: false,
                    flex: 0.5,
                    text: '字段类型',
                    renderer: function (value, metaData, record, rowIndex, colIndex, store, view) {
                        return DataDictionary.rendererSubmitToDisplay(value, 'fieldType');
                    }
                }, {
                    dataIndex: 'minValue',
                    flex: 1,
                    align: 'center',
                    text: '最小值'
                }, {
                    dataIndex: 'maxValue',
                    flex: 1,
                    align: 'center',
                    text: '最大值'
                }, {
                    dataIndex: 'sort',
                    flex: 1,
                    align: 'center',
                    text: '排序值'
                }, {
                    dataIndex: 'value',
                    flex: 1,
                    align: 'center',
                    text: '值'
                }, {
                    dataIndex: 'unitName',
                    flex: 1,
                    align: 'center',
                    text: '单位'
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


/*
 * 添加配置管理界面
 */

Ext.define('MyApp.view.addCurrencyConfigWindow', {
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
                    xtype: 'gameselectorsellersetting',
                    itemId : 'MyApp_view_goods_gamelink_ID',
                    columnWidth: .70,
                    allowBlank: true,
                    fieldLabel: '游戏名称'
                }, DataDictionary.getDataDictionaryCombo('enabled', {
                    fieldLabel: '是否必填',
                    editable: false,
                    name: 'enabled',
                    labelWidth: 100,
                    value: true
                }),Ext.create('Ext.form.ComboBox', {
                    fieldLabel: '商品类型',
                    columnWidth:.30,
                    name: 'goodsType',
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
                }),DataDictionary.getDataDictionaryCombo('field',{
                    fieldLabel: '字段',
                    name: 'field',
                    labelWidth: 100,
                    editable: false
                }), {
                    name: 'fieldMeaning',
                    allowBlank: false,
                    fieldLabel: '字段含义',
                },DataDictionary.getDataDictionaryCombo('fieldType',{
                    fieldLabel: '字段类型',
                    name: 'fieldType',
                    labelWidth: 100,
                    editable: false
                }), {
                    xtype: 'numberfield',
                    name: 'minValue',
                    allowBlank: true,
                    fieldLabel: '最小值',
                }, {
                    xtype: 'numberfield',
                    name: 'maxValue',
                    allowBlank: true,
                    fieldLabel: '最大值',
                }, {
                    name: 'unitName',
                    allowBlank: true,
                    fieldLabel: '单位',
                }, {
                    xtype: 'numberfield',
                    name: 'sort',
                    allowBlank: true,
                    fieldLabel: '排序值',
                },{
                    xtype: 'textarea',
                    name: 'value',
                    columnWidth: 1,
                    allowBlank: true,
                    height: 100,
                    fieldLabel: '值'
                }],
                buttons: [{
                    text: '保存',
                    handler: function () {
                        var formView = me.getForm(),
                            form = formView.getForm(), params,
                            record = form.getRecord(),
                            url = './CurrencyConfig/addCurrencyConfig.action',
                            message = '新增';
                        if (!form.isValid()) {
                            return;
                        }
                        form.updateRecord(record);
                        // var count = record.get("repositoryCount");
                        // var  re = /^\d*$/;
                        // if (!re.test(count)) {
                        //     Ext.ux.Toast.msg("温馨提示请填写正确的库存数量");
                        //     return;
                        // }
                        //     console.log(form.findField('gameName').getRawValue())
                        //     console.log(record.get('goodsType'))
                        //     console.log(record.get('field'))
                        //     console.log(record.get('fieldMeaning'))
                        //     console.log(record.get('fieldType'))
                        //     console.log(record.get('minValue'))
                        //     console.log(form.findField('minValue').getRawValue())
                        // console.log(form.findField('maxValue').getRawValue())
                        var params = {
                            // 'repositoryConfine.gameName': form.findField('gameName').getRawValue(),
                            // 'repositoryConfine.repositoryCount': record.get('repositoryCount'),
                            // 'repositoryConfine.isEnabled': record.get('isEnabled')
                            'currencyConfigEO.gameName':form.findField('gameName').getRawValue(),
                            'currencyConfigEO.goodsType':record.get('goodsType'),
                            'currencyConfigEO.field':record.get('field'),
                            'currencyConfigEO.fieldMeaning':record.get('fieldMeaning'),
                            'currencyConfigEO.fieldType':record.get('fieldType'),
                            'currencyConfigEO.minValue':form.findField('minValue').getRawValue(),
                            'currencyConfigEO.maxValue':form.findField('maxValue').getRawValue(),
                            'currencyConfigEO.value':record.get('value'),
                            'currencyConfigEO.unitName':record.get('unitName'),
                            'currencyConfigEO.enabled':record.get('enabled'),
                            // 'currencyConfigEO.minLength':form.findField('minLength').getRawValue(),
                            // 'currencyConfigEO.maxLength':form.findField('maxLength').getRawValue(),
                            'currencyConfigEO.sort':form.findField('sort').getRawValue(),
                        };
                        if (me.isUpdate) {
                            url = './CurrencyConfig/modifyCurrencyConfig.action';
                            message = '修改';
                            Ext.Object.merge(params, {
                                'currencyConfigEO.id': record.get('id')
                            });
                        }
                        console.log(params);
                        form.submit({
                            url: url,
                            params: params,
                            success: function (from, action, json) {
                                var CurrencyConfigManager = Ext.getCmp('CurrencyConfigManager'),
                                    store = CurrencyConfigManager.getStore();
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
            // gameName.setReadOnly(true);
        } else {
            gameName.setReadOnly(false);
            form.reset();
        }

    },

    constructor: function (config) {
        var me = this, cfg = Ext.apply({}, config);
        me.items = [me.getForm()]
        me.callParent([cfg]);
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



