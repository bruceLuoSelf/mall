//主游戏配置
Ext.define('MyApp.view.deliveryConfigManager', {
        extend: 'Ext.panel.Panel',
        id: 'deliveryConfigManager',
        closable: true,
        layout: "border",
        title: '收货信息配置',
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
                                fn: me.deleteConfig,
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
                    items: [{
                        xtype: 'deliveryConfigSelector',
                        itemId: 'MyApp_view_deliveryConfig_ID',
                        columnWidth: .7,
                        allowBlank: true,
                        fieldLabel: '游戏属性'
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
            return me.queryForm;
        },
        addConfig: function (button, e, eOpts) {
            var window = this.getMainGameConfigWindow();
            window.bindData(Ext.create('MyApp.model.DeliveryConfigModel'), false);
            window.show();
        },
        // 修改系统配置地信息
        modifyShGameConfig: function (button, e, eOpts) {
            var grid = this.getGameConfigGrid(),
                selModel = grid.getSelectionModel(),
                selRecords = selModel.getSelection(),
                window = this.getMainGameConfigWindow();
            if (selRecords == null || selRecords.length <= 0) {
                Ext.ux.Toast.msg("温馨提示", "请先选择要修改配置信息");
                return;
            }
            window.bindData(selRecords[0], true);
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
                        url: './deliveryType/deleteDeliveryConfig.action',
                        params: {'deliveryConfig.id': record.get('id')},
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
        store: null,
        getStore: function () {
            var me = this;
            if (me.store == null) {
                me.store = Ext.create('MyApp.store.DeliveryConfigStore', {
                    autoLoad: true,
                    listeners: {
                        beforeload: function (store, operation, eOpts) {
                            var queryForm = me.getQueryForm();
                            if (queryForm != null) {
                                var values = queryForm.getValues();
                                Ext.apply(operation, {
                                    params: {
                                        'deliveryConfig.gameName': values.gameName,
                                        'deliveryConfig.deliveryTypeId': values.deliveryTypeId,
                                        'deliveryConfig.goodsTypeId': values.goodsTypeId,
                                        'deliveryConfig.tradeTypeId': values.tradeTypeId,
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
                        dataIndex: 'goodsTypeName',
                        text: '商品类型',
                        flex: 1.2,
                        align: 'center'
                    }, {
                        dataIndex: 'deliveryTypeName',
                        text: '收货模式',
                        flex: 1.2,
                        align: 'center'
                    }, {
                        dataIndex: 'tradeTypeName',
                        text: '交易方式',
                        flex: 1.2,
                        align: 'center'
                    }, {
                        xtype: 'datecolumn',
                        dataIndex: 'createTime',
                        format: 'Y-m-d H:i:s',
                        sortable: false,
                        flex: 1.5,
                        align: 'center',
                        text: '创建时间 '
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
    title: '新增/修改收货配置信息',
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
                    {
                        xtype: 'deliveryConfigSelector',
                        itemId: 'MyApp_view_deliveryConfig_ID',
                        columnWidth: 1,
                        allowBlank: false,
                        fieldLabel: '游戏属性'
                    }, {
                        xtype: 'htmleditor',
                        anchor: '100%',
                        name: 'orderHelpInfo',
                        title: '请输入内容',
                        height: 250,
                        columnWidth: 1,
                        allowBlank: true,
                        enableFont: false,
                        fieldLabel: '出货提示'
                    }, {
                        xtype: 'htmleditor',
                        anchor: '100%',
                        name: 'orderTip',
                        title: '请输入内容',
                        height: 250,
                        columnWidth: 1,
                        allowBlank: true,
                        enableFont: false,
                        fieldLabel: '商家提醒'
                    }, {
                        name: 'helpeUrl',
                        columnWidth: 1,
                        align: 'center',
                        fieldLabel: '交易帮助链接'
                    }
                ],
                buttons: [{
                    text: '保存',
                    handler: function () {
                        var formView = me.getForm(),
                            form = formView.getForm(), params,
                            record = form.getRecord(),
                            url = './deliveryType/addDeliveryCofig.action',
                            message = '新增';
                        if (!form.isValid()) {
                            return;
                        }
                        form.updateRecord(record);

                        var queryForm = me.getForm();
                        var values = queryForm.getValues();

                        var params = {};
                        if (me.isUpdate) {
                            url = './deliveryType/modifyDeliveryCofig.action';
                            message = "修改";
                            Ext.Object.merge(params, {
                                'deliveryConfig.id': record.get('id'),
                                'deliveryConfig.orderHelpInfo': values.orderHelpInfo,
                                'deliveryConfig.orderTip': values.orderTip,
                                'deliveryConfig.helpeUrl': values.helpeUrl
                            });
                        } else {
                            Ext.Object.merge(params, {
                                'deliveryConfig.gameName': values.gameName,
                                'deliveryConfig.deliveryTypeId': values.deliveryTypeId,
                                'deliveryConfig.goodsTypeId': values.goodsTypeId,
                                'deliveryConfig.tradeTypeId': values.tradeTypeId,
                                'deliveryConfig.orderHelpInfo': values.orderHelpInfo,
                                'deliveryConfig.orderTip': values.orderTip,
                                'deliveryConfig.helpeUrl': values.helpeUrl
                            });
                        }

                        form.submit({
                            url: url,
                            method: 'POST',
                            params: params,
                            success: function (from, action, json) {
                                var deliveryConfigManager = Ext.getCmp('deliveryConfigManager'),
                                    store = deliveryConfigManager.getStore();
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
            form = me.getForm().getForm(), formView = me.getForm(),
            gameProp = formView.getComponent('MyApp_view_deliveryConfig_ID');
        form.reset();
        form.loadRecord(record);
        me.isUpdate = isUpdate;
        if (!isUpdate) {
            gameProp.setVisible(true);
            form.reset();
        } else {
            gameProp.setVisible(false);
        }
    }, constructor: function (config) {
        var me = this, cfg = Ext.apply({}, config);
        me.items = [me.getForm()]
        me.callParent([cfg]);
    }
});
