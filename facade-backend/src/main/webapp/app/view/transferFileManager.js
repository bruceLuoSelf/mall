//工作室账号管理信息
Ext.define('MyApp.view.transferFileManager', {
    extend: 'Ext.panel.Panel',
    id: 'transferFileManager',
    layout: "border",
    closable: true,
    title: '合区合服配置',
    toolbar: null,
    getToolbar: function () {
        var me = this;
        if (Ext.isEmpty(me.toolbar)) {
            me.toolbar = Ext.widget('toolbar', {
                dock: 'top',
                items: [{
                    text: '修改配置',
                    listeners: {
                        click: {
                            fn: me.updateFood,
                            scope: me
                        }
                    }
                }]
            });
        }
        return me.toolbar;
    },
// 修改信息
    updateFood: function (button, e, eOpts) {
        var grid = this.getFirmGrid(),
            selModel = grid.getSelectionModel(),
            selRecords = selModel.getSelection(),
            window = this.getUpdateFirm();
        if (selRecords == null || selRecords.length <= 0) {
            Ext.ux.Toast.msg("温馨提示", "请先选择要修改的信息");
            return;
        }
        window.bindData(selRecords[0]);
        window.show();
    },
    addFileWindow: null,
    getAddfoodsWindow: function () {
        if (this.addFileWindow == null) {
            this.addFileWindow = new MyApp.view.updatetrans();
        }
        return this.addFileWindow;
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
            me.store = Ext.create('MyApp.store.TransferFileStore', {
                autoLoad: true,
                listeners: {
                    beforeload: function (store, operation, eOpts) {
                        var queryForm = me.getQueryForm();
                        var values = queryForm.getValues();
                        var gameName = queryForm.getForm().findField('gameName');
                        if (queryForm != null) {
                            var values = queryForm.getValues();
                            var params = {
                                'gameName': values.gameName
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
    updatetrans: null,
    getUpdateFirm: function () {
        if (this.updateWindow == null) {
            this.updateWindow = new MyApp.view.updatetrans();
        }
        return this.updateWindow;
    },
    //主页面显示页面
    FirmGrid: null,
    getFirmGrid: function () {
        var me = this;
        if (Ext.isEmpty(me.FirmGrid)) {
            me.FirmGrid = Ext.widget('gridpanel', {
                region: 'center',
                header: false,
                columnLines: true,
                store: me.getStore(),
                columns: [{
                    xtype: 'rownumberer'
                }, {
                    dataIndex: 'gameName',
                    text: '游戏名称',
                    flex: 1.7,
                    align: 'center'
                }, {
                    dataIndex: 'jsonString',
                    text: '关键字符串',
                    flex: 1.7,
                    align: 'center',
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
                    mode: 'SINGLE'
                })
            });
        }
        return me.FirmGrid;
    },
    initComponent: function () {
        var me = this;
        Ext.applyIf(me, {
            items: [me.getQueryForm(), me.getFirmGrid()]
        });
        me.callParent(arguments);
    }
});

Ext.define('MyApp.view.updatetrans', {
    extend: 'Ext.window.Window',
    title: '修改信息',
    width: 700,
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
                        fieldLabel: '游戏名称',
                        allowBlank: false,
                        readOnly: true,
                        name: 'gameName'
                    }, {
                        fieldLabel: '关键字符串',
                        xtype:'textarea',
                        name: 'jsonString',
                        title: '请输入内容',
                        height: 450,
                        columnWidth: 1,
                        allowBlank: true,
                        enableFont: false
                    }
                ],
                buttons: [{
                    text: '保存',
                    handler: function () {
                        var formView = me.getForm(),
                            form = formView.getForm(), params = {},
                            record = form.getRecord(),
                            url = './goods/updateTransferFile.action',
                            message = '修改';
                        if (!form.isValid()) {
                            return;
                        }
                        var jsonString1 = record.get("jsonString");
                        form.updateRecord(record);
                        // if (typeof record.get('jsonString') == 'string') {
                        // 	try {
                        // 		JSON.parse(record.get('jsonString'));
                        // 	} catch(e) {
                        // 		record.set("jsonString",jsonString1);
                        // 		Ext.ux.Toast.msg("温馨提示", "格式有误,请重新输入");
                        // 		return;
                        // 	}
                        // }
                        params.jsonString = record.get('jsonString');
                        params.id = record.get('id');
                        form.submit({
                            url: url,
                            method: 'POST',
                            params: params,
                            success: function (from, action, json) {
                                var tradeManager = Ext.getCmp('transferFileManager'),
                                    store = tradeManager.getStore();
                                Ext.ux.Toast.msg("温馨提示", message + "成功");
                                me.close();
                                store.load();
                            },
                            exception: function (from, action, json) {
                                var tradeManager = Ext.getCmp('transferFileManager'),
                                    store = tradeManager.getStore();
                                Ext.ux.Toast.msg("温馨提示", json.message);
                                store.load();
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
    },
    constructor: function (config) {
        var me = this, cfg = Ext.apply({}, config);
        me.items = [me.getForm()]
        me.callParent([cfg]);
    }
});





