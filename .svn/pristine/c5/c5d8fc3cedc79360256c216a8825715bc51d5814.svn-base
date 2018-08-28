/*************新增一键复制功能_ADD_20170520_START***************************/
Ext.define('MyApp.view.SecretForm',{
    extend: 'Ext.form.Panel',
    layout: 'column',
    border: true,
    defaults: {
        margin: '5 5 5 5',
        columnWidth: .333,
        labelWidth: 100,
        readOnly: true,
        xtype: 'textfield'
    },
    constructor : function(config) {
        var me = this, cfg = Ext.apply({}, config);
        me.items = [{
            id:'body_form_firms_secret',
            fieldLabel: '工作室密匙',
            name: 'firmsSecret',
            columnWidth: 1
        },{
            xtype: 'container',
            columnWidth: .5,
            margin: '10 50 10 110',
            items: [{
                xtype: 'button',
                text: '一键复制',
                plugins: {
                    ptype: 'zeroclipboardplugin',
                    targetFun: function(component) {
                        var firmsSecret = Ext.getCmp("body_form_firms_secret").getValue();
                        return firmsSecret;
                    }
                }
            },{
                xtype: 'container',
                columnWidth: .5,
                margin: '-23 20 0 120',
                items: [{
                    xtype: 'button',
                    text: '确定',
                    listeners:{
                        'click':function(){
                            Ext.getCmp('body_form_hide').hide();
                        }
                    }
                }]
            }]
        }];
        me.callParent([ cfg ]);
    }
});

Ext.define('MyApp.view.SecretWindow',{
    extend: 'Ext.window.Window',
    title: '获取工作室密匙窗口',
    width: 400,
    id:'body_form_hide',
    border: false,
    padding: '10 10 10 10',
    autoScroll: true,
    closeAction: 'hide',
    modal: true,
    form: null,
    getForm: function(){
        var me = this;
        if(me.form==null){
            me.form = Ext.create('MyApp.view.SecretForm');
        }
        return me.form;
    },
    bindData: function(record){
        var id = record.get("id");
        Ext.Ajax.request({
            url: './goods/showSecret.action',
            params: {
                'id': record.get('id'),
                'firmsName': record.get('firmsName')
            },
            method: 'POST',
            success: function (response, opts) {
                var json = Ext.decode(response.responseText);
                //根据ID将获取的值重新设置到form中
                Ext.getCmp("body_form_firms_secret").setValue(json.firmInfo.firmsSecret);
            },
            exception: function (response, opts) {
                var json = Ext.decode(response.responseText);
                Ext.ux.Toast.msg("温馨提示", json.responseStatus.message);
            }
        });
    },
    initComponent: function() {
        var me = this;
        Ext.applyIf(me, {
            items: [me.getForm()]
        });
        me.callParent(arguments);
    }
});
/*************新增一键复制功能_ADD_20170520_END***************************/


//出货模式配置
Ext.define('MyApp.view.FirmInfoManager', {
    extend: 'Ext.panel.Panel',
    id: 'FirmInfoManager',
    layout: "border",
    closable: true,
    title: '工作室管理',
    toolbar: null,
    getToolbar: function () {
        var me = this;
        if (Ext.isEmpty(me.toolbar)) {
            me.toolbar = Ext.widget('toolbar', {
                dock: 'top',
                items: [{
                    text: '添加工作室',
                    listeners: {
                        click: {
                            fn: me.addFirm,
                            scope: me
                        }
                    }
                }, '-', {
                    text: '修改工作室',
                    listeners: {
                        click: {
                            fn: me.updateShTrade,
                            scope: me
                        }
                    }
                }, '-', {
                    text: '删除工作室',
                    listeners: {
                        click: {
                            fn: me.deleteFirm,
                            scope: me
                        }
                    }
                }, '-', {
                    xtype: 'button',
                    itemId: 'enableButton',
                    text: '启用',
                    listeners: {
                        click: {
                            fn: me.enableFirm,
                            scope: me
                        }
                    }
                }, '-', {
                    xtype: 'button',
                    itemId: 'disableButton',
                    text: '禁用',
                    listeners: {
                        click: {
                            fn: me.disableFirm,
                            scope: me
                        }
                    }
                }, '-', {
                    text: '更新密匙',
                    listeners: {
                        click: {
                            fn: me.refreshFirm,
                            scope: me
                        }
                    }
                },'-', {
                    text: '获取密匙',
                    listeners: {
                        click: {
                            fn: me.showFirm,
                            scope: me
                        }
                    }
                }
                ]
            });
        }
        return me.toolbar;
    },

    addFirmWindow: null,
    getAddFirmWindow: function () {
        if (this.addFirmWindow == null) {
            this.addFirmWindow = new MyApp.view.updateTrade();
        }
        return this.addFirmWindow;
    },
    //新增
    addFirm: function (button, e, eOpts) {
        var window = this.getAddFirmWindow();
        window.bindData(Ext.create('MyApp.model.FirmInfoModel'), false);
        window.show();
    },
    // 修改工作室信息
    updateShTrade: function (button, e, eOpts) {
        var grid = this.getShTradeGrid(),
            selModel = grid.getSelectionModel(),
            selRecords = selModel.getSelection(),
            window = this.getUpdateTrade();
        if (selRecords == null || selRecords.length <= 0) {
            Ext.ux.Toast.msg("温馨提示", "请先选择要修改工作室信息");
            return;
        }
        window.bindData(selRecords[0], true);
        window.show();
    },
    // 启用
    enableFirm: function (button, e, eOpts) {
        var grid = this.getShTradeGrid(),
            selModel = grid.getSelectionModel(),
            selRecords = selModel.getSelection();
        if (selRecords == null || selRecords.length <= 0) {
            Ext.ux.Toast.msg("温馨提示", "请先选择要启用的工作室");
            return;
        }
        var record = selRecords[0];
        if (record.get('enabled')) {
            Ext.ux.Toast.msg("温馨提示", "该工作室已经启用！");
            return;
        }
        Ext.MessageBox.confirm('温馨提示', '确定启用该工作室吗？', function (btn) {
            if (btn == 'yes') {
                Ext.Ajax.request({
                    url: './goods/able.action',
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
    disableFirm: function (button, e, eOpts) {
        var grid = this.getShTradeGrid(),
            selModel = grid.getSelectionModel(),
            selRecords = selModel.getSelection();
        if (selRecords == null || selRecords.length <= 0) {
            Ext.ux.Toast.msg("温馨提示", "请先选择要禁用的工作室");
            return;
        }
        var record = selRecords[0];
        if (!record.get('enabled')) {
            Ext.ux.Toast.msg("温馨提示", "该工作室已经禁用！");
            return;
        }
        Ext.MessageBox.confirm('温馨提示', '确定禁用该工作室吗？', function (btn) {
            if (btn == 'yes') {
                Ext.Ajax.request({
                    url: './goods/disable.action',
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
    //删除
    deleteFirm: function () {
        var me = this,
            grid = me.getShTradeGrid(),
            selModel = grid.getSelectionModel(),
            selRecords = selModel.getSelection();
        if (selRecords == null || selRecords.length <= 0) {
            Ext.ux.Toast.msg("温馨提示", "请先选择要删除的工作室");
            return;
        }
        var record = selRecords[0];
        Ext.MessageBox.confirm('温馨提示', '确定删除该工作室吗？', function (btn) {
            if (btn == 'yes') {
                me.getStore().remove(record);
                Ext.Ajax.request({
                    url: './goods/deleteFirmInfo.action',
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
    //更新密匙
    refreshFirm:function(){
        var me = this,
            grid = me.getShTradeGrid(),
            selModel = grid.getSelectionModel(),
            selRecords = selModel.getSelection();
        if (selRecords == null || selRecords.length <= 0) {
            Ext.ux.Toast.msg("温馨提示", "请先选择要更新密匙的工作室");
            return;
        }
        var record = selRecords[0];
        Ext.MessageBox.confirm('温馨提示', '您确定要更新密匙吗？确定更新后该条操作会被记录到日志', function (btn) {
            if (btn == 'yes') {
                Ext.Ajax.request({
                    url: './goods/refreshSecret.action',
                    params: {
                        'firmInfo.id': record.get('id'),
                        'firmInfo.firmsName': record.get('firmsName'),
                        'firmInfo.enabled': record.get('enabled'),
                        'firmInfo.createTime': record.get('createTime')
                    },
                    method: 'POST',
                    success: function (response, opts) {
                        Ext.ux.Toast.msg("温馨提示", "更新成功！");
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

    secretWindow: null,
    getSecretWindow: function(){
        if(this.secretWindow == null){
            this.secretWindow = Ext.create('MyApp.view.SecretWindow');
        }
        return this.secretWindow;
    },

    //获取密匙
    showFirm:function(){
        var me = this;
            grid = me.getShTradeGrid(),
            selModel = grid.getSelectionModel(),
            selRecords = selModel.getSelection();
            record = selRecords[0];
        if (selRecords == null || selRecords.length <= 0) {
            Ext.ux.Toast.msg("温馨提示", "请先选择要获取密匙的工作室");
            return;
        }
        var record = selRecords[0];
        /*********ADD_20170719_工作室优化_START*********************/
        Ext.MessageBox.buttonText.yes="确定";
        Ext.MessageBox.buttonText.no="取消";
        Ext.MessageBox.show({
            title:'温馨提示',
            msg: "您确定要获取密匙吗？确定获取后该条操作会被记录到日志",
            buttons: Ext.MessageBox.YESNO,
            fn: function(btn){
                if(btn=='yes'){
                    var window = me.getSecretWindow();
                    window.bindData(record);
                    window.show();
                }else{
                    return;
                }
            }
        });
        /*********ADD_20170719_工作室优化_END*********************/
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
                items: [Ext.create('Ext.form.ComboBox', {
                    fieldLabel: '工作室名称',
                    labelWidth: 100,
                    name: 'firmsName',
                    store: Ext.create('MyApp.store.FirmInfoStore', {
                        listeners: {
                            load: function (store, records, successful, eOpts) {
                                //添加
                                //store.insert(0,{id:0, name:'全部'});
                            }
                        }
                    }),
                    displayField: 'firmsName',
                    valueField: 'firmsName',
                    value:'全部',
                    editable: true
                })],
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
            me.store = Ext.create('MyApp.store.FirmInfoStore', {
                autoLoad: true,
                listeners: {
                    beforeload: function (store, operation, eOpts) {
                        var queryForm = me.getQueryForm();
                        var values = queryForm.getValues();
                        var firmsName=queryForm.getForm().findField('firmsName');
                        if (queryForm != null) {
                            var values = queryForm.getValues();
                            var params = {
                                'firmsName':  Ext.String.trim(firmsName.getRawValue()),
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
                    dataIndex: 'firmsName',
                    text: '工作室名称',
                    flex: 1.7,
                    align: 'center'
                },/**{
                    dataIndex: 'firmsSecret',
                    text: '工作室秘钥',
                    flex: 1.7,
                    align: 'center'
                },**/{
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
                    mode: 'SINGLE'
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
Ext.define('MyApp.view.updateTrade', {
    extend: 'Ext.window.Window',
    title: '新增/修改信息',
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
                    fieldLabel: '工作室名称',
                    allowBlank: false,
                    name: 'firmsName'
                },

                   /* Ext.create('Ext.form.ComboBox', {
                        fieldLabel: '是否启用',
                        labelWidth: 100,
                        name: 'enabled',
                        store:
                            Ext.create("Ext.data.Store", {
                                model:  Ext.regModel("PostInfo", {
                                    fields: [{ name: "displayName" }, { name: "showValue" }]
                                }),
                                data: [
                                    { displayName: "启用", showValue: true },
                                    {displayName:"禁用",showValue:false },
                                ]
                            }),
                        displayField: 'displayName',
                        valueField: 'showValue',
                        allowBlank: false,
                        value:true,
                        triggerAction: "all",        //单击触发按钮显示全部数据
                        queryMode: "local",
                        editable: false
                    }),*/
        ],
                buttons: [{
                    text: '保存',
                    handler: function () {
                        var formView = me.getForm(),
                            form = formView.getForm(), params={},
                            record = form.getRecord(),
                            url = './goods/addFirm.action',
                            message = '新增';
                        if (!form.isValid()) {
                            return;
                        }
                        form.updateRecord(record);
                        params.enabled=record.get('enabled');
                        params.firmsName=record.get('firmsName');
                        if (me.isUpdate) {
                            url = './goods/updateFirmName.action';
                            message = '修改';
                            params.id=record.get('id');
                        }
                        form.submit({
                            url: url,
                            method: 'POST',
                            params: params,
                            success: function (from, action, json) {
                                var tradeManager = Ext.getCmp('FirmInfoManager'),
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
        }
    },
    constructor: function (config) {
        var me = this, cfg = Ext.apply({}, config);
        me.items = [me.getForm()]
        me.callParent([cfg]);
    }
});





