
Ext.define('MyApp.view.userSecretKeyWindow',{
    extend:'Ext.window.Window',
    title:'工作室用户授权码窗口',
    id:'user_secret_key_window',
    width:400,
    border:false,
    padding:'10 10 10 10',
    autoScroll:true,
    closeAction:'hide',
    modal:true,
    form:null,
    getForm:function(){
        var me = this;
        if (me.form == null) {
            me.form = Ext.create('MyApp.view.userSecretKeyForm')
        }
        return me.form;
    },
    bindData:function(record) {
        var id = record.get('id');
        Ext.Ajax.request({
            url:'./goods/selectFirmUserSecret.action',
            params:{'id':record.get('id')},
            success:function(response,opts) {
                var json = Ext.decode(response.responseText);
                Ext.getCmp('user_secret_key_form').setValue(json.firmsAccount.userSecretKey);
            },
            exception:function(response,opts){
                var json = Ext.decode(response.responseText);
                Ext.ux.Toast.msg("温馨提示", json.responseStatus.message);
            }
        });
    },
    initComponent:function() {
        var me = this;
        Ext.applyIf(me,{
            items:[me.getForm()]
        });
        me.callParent(arguments);
    }
});

Ext.define('MyApp.view.userSecretKeyForm',{
    extend:'Ext.form.Panel',
    layout:'column',
    border:true,
    defaults:{
        margin:'5 5 5 5',
        columnWidth:.333,
        labelWidth:120,
        readOnly:true,
        xtype:'textfield'
    },
    constructor:function(config) {
        var me = this,
            cfg = Ext.apply({},config);
        me.items = [{
            id:'user_secret_key_form',
            fieldLabel:'工作室用户授权码',
            name:'userSecretKey',
            columnWidth:1
        },{
            xtype:'container',
            columnWidth:.5,
            margin:'10 50 10 110',
            items:[{
                xtype:'button',
                text:'一键复制',
                plugins:{
                    ptype:'zeroclipboardplugin',
                    targetFun: function(component) {
                        console.log(1111)
                        var userSecretKey = Ext.getCmp("user_secret_key_form").getValue();
                        console.log(userSecretKey)
                        return userSecretKey;
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
                            Ext.getCmp('user_secret_key_window').hide();
                        }
                    }
                }]
            }]
        }];
        me.callParent([cfg]);
    }
});

//工作室账号管理信息
Ext.define('MyApp.view.FirmsAccountManager', {
    extend: 'Ext.panel.Panel',
    id: 'FirmsAccountManager',
    layout: "border",
    closable: true,
    title: '工作室用户管理',
    toolbar: null,
    getToolbar: function () {
        var me = this;
        if (Ext.isEmpty(me.toolbar)) {
            me.toolbar = Ext.widget('toolbar', {
                dock: 'top',
                items: [{
                    text: '添加账号',
                    listeners: {
                        click: {
                            fn: me.addFirm,
                            scope: me
                        }
                    }
                }, '-', {
                    text: '修改卖家账号',
                    listeners: {
                        click: {
                            fn: me.updateShTrade,
                            scope: me
                        }
                    }
                }, '-',{
                    text: '删除账号',
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
                    xtype: 'button',
                    text: '更新授权码',
                    listeners: {
                        click: {
                            fn: me.refreshSecretKey,
                            scope: me
                        }
                    }
                }, '-', {
                    xtype: 'button',
                    text: '获取授权码',
                    listeners: {
                        click: {
                            fn: me.getSecretKey,
                            scope: me
                        }
                    }
                }]
            });
        }
        return me.toolbar;
    },

    addFirmWindow: null,
    getAddFirmAccountWindow: function () {
        if (this.addFirmWindow == null) {
            this.addFirmWindow = new MyApp.view.updateFirm();
        }
        return this.addFirmWindow;
    },

    addFirm: function (button, e, eOpts) {
        var window = this.getAddFirmAccountWindow();
        window.bindData(Ext.create('MyApp.model.FirmInfoModel'), false);
        window.show();
    },
    // 修改系统配置地信息
    updateShTrade: function (button, e, eOpts) {
        var grid = this.getFirmGrid(),
            selModel = grid.getSelectionModel(),
            selRecords = selModel.getSelection(),
            window = this.getUpdateFirm();
        if (selRecords == null || selRecords.length <= 0) {
            Ext.ux.Toast.msg("温馨提示", "请先选择要修改账号信息");
            return;
        }
        window.bindData(selRecords[0], true);
        window.show();
    },
    // 启用
    enableFirm: function (button, e, eOpts) {
        var grid = this.getFirmGrid(),
            selModel = grid.getSelectionModel(),
            selRecords = selModel.getSelection();
        if (selRecords == null || selRecords.length <= 0) {
            Ext.ux.Toast.msg("温馨提示", "请先选择要启用的账号");
            return;
        }
        var record = selRecords[0];
        if (record.get('enabled')) {
            Ext.ux.Toast.msg("温馨提示", "该账号已经启用！");
            return;
        }
        Ext.MessageBox.confirm('温馨提示', '确定启用该账号吗？', function (btn) {
            if (btn == 'yes') {
                Ext.Ajax.request({
                    url: './goods/ableFirm.action',
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
        var grid = this.getFirmGrid(),
            selModel = grid.getSelectionModel(),
            selRecords = selModel.getSelection();
        if (selRecords == null || selRecords.length <= 0) {
            Ext.ux.Toast.msg("温馨提示", "请先选择要禁用的账号");
            return;
        }
        var record = selRecords[0];
        if (!record.get('enabled')) {
            Ext.ux.Toast.msg("温馨提示", "该账号已经禁用！");
            return;
        }
        Ext.MessageBox.confirm('温馨提示', '确定禁用该账号吗？', function (btn) {
            if (btn == 'yes') {
                Ext.Ajax.request({
                    url: './goods/disableFirm.action',
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
    //刷新授权码
    refreshSecretKey:function() {
        var me = this,
            grid = me.getFirmGrid(),
            selModel = grid.getSelectionModel(),
            selRecords = selModel.getSelection();
        if (selRecords == null || selRecords.length <= 0) {
            Ext.ux.Toast.msg("温馨提示", "请先选择要更新授权码的用户");
            return;
        }
        var record = selRecords[0];
        Ext.MessageBox.confirm('温馨提示', '您确定要更新授权码吗？确定更新后该条操作会被记录到日志', function (btn) {
            if (btn == 'yes') {
                Ext.Ajax.request({
                    url:'./goods/refreshSecretKey.action',
                    params:{'id':record.get('id')},
                    success: function (response, opts) {
                        Ext.ux.Toast.msg("温馨提示", "更新成功！");
                        me.getStore().load();
                        grid.getSelectionModel().deselectAll();
                    },
                    exception:function (response,opts) {
                        var json = Ext.decode(response.responseText);
                        Ext.ux.Toast.msg("温馨提示",json.responseStatus.message);
                    }
                })
            }else{
                return ;
            }
        });
    },
    //获取授权码
    getSecretKey:function() {
        var me = this,
            grid = me.getFirmGrid(),
            selModel = grid.getSelectionModel(),
            selRecords = selModel.getSelection();
        if (selRecords == null || selRecords.length <= 0) {
            Ext.ux.Toast.msg("温馨提示", "请先选择要获取授权码的用户");
            return;
        }
        var record = selRecords[0];
        Ext.MessageBox.buttonText.yes="确定";
        Ext.MessageBox.buttonText.no="取消";
        Ext.MessageBox.show({
            title:'温馨提示',
            msg: "您确定要获取授权码吗？确定获取后该条操作会被记录到日志",
            buttons: Ext.MessageBox.YESNO,
            fn: function(btn){
                if(btn=='yes'){
                    var window = me.getUserSecretKeyWindow();
                    window.bindData(record);
                    window.show();
                }else{
                    return;
                }
            }
        });
    },

    userSecretKeyWindow:null,
    getUserSecretKeyWindow:function() {
        if (this.userSecretKeyWindow == null) {
            this.userSecretKeyWindow = Ext.create('MyApp.view.userSecretKeyWindow');
        }
        return this.userSecretKeyWindow;
    },

    //删除
    deleteFirm: function () {
        var me = this,
            grid = me.getFirmGrid(),
            selModel = grid.getSelectionModel(),
            selRecords = selModel.getSelection();
        if (selRecords == null || selRecords.length <= 0) {
            Ext.ux.Toast.msg("温馨提示", "请先选择要删除的账号");
            return;
        }
        var record = selRecords[0];
        Ext.MessageBox.confirm('温馨提示', '确定删除吗？', function (btn) {
            if (btn == 'yes') {
                me.getStore().remove(record);
                Ext.Ajax.request({
                    url: './goods/deleteFirmAccount.action',
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
                items: [Ext.create('Ext.form.ComboBox', {
                    fieldLabel: '工作室名称',
                    labelWidth: 100,
                    name: 'firmsName',
                    store: Ext.create('MyApp.store.FirmAccountStore', {
                        listeners: {
                            load: function (store, records, successful, eOpts) {
                                //添加
                                store.insert(0, {id: 0, name: '全部'});
                            }
                        }
                    }),
                    displayField: 'firmsName',
                    valueField: 'firmsName',
                    value: '全部',
                    editable: true
                }),{
                    xtype: 'textfield',
                    fieldLabel: '卖家账号',
                    name: 'loginAccount'
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
            me.store = Ext.create('MyApp.store.FirmAccountStore', {
                autoLoad: true,
                listeners: {
                    beforeload: function (store, operation, eOpts) {
                        var queryForm = me.getQueryForm();
                        var values = queryForm.getValues();
                        var firmsName = queryForm.getForm().findField('firmsName');
                        if (queryForm != null) {
                            var values = queryForm.getValues();
                            var params = {
                                'firmsAccount.firmsName': Ext.String.trim(firmsName.getRawValue()),
                                'firmsAccount.loginAccount':values.loginAccount
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
    updateFirmAccount: null,
    getUpdateFirm: function () {
        if (this.updateWindow == null) {
            this.updateWindow = new MyApp.view.updateFirmAccount();
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
                    dataIndex: 'firmsName',
                    text: '工作室名称',
                    flex: 1.7,
                    align: 'center'
                },{
                    dataIndex: 'loginAccount',
                    text: '卖家账号',
                    flex: 1.7,
                    align: 'center'
                }, {
                    dataIndex: 'enabled',
                    sortable: false,
                    flex: 1,
                    align: 'center',
                    text: '是否启用',
                    renderer: function (value, metaData, record, rowIndex, colIndex, store, view) {
                        return DataDictionary.rendererSubmitToDisplay(value, 'check');
                    }
                }, {
                    xtype: 'datecolumn',
                    format: 'Y-m-d H:i:s',
                    dataIndex: 'createTime',
                    sortable: false,
                    flex: 1.5,
                    align: 'center',
                    text: '创建时间 '
                }, {
                    xtype: 'datecolumn',
                    format: 'Y-m-d H:i:s',
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

//增加工作室账号管理信息
Ext.define('MyApp.view.updateFirm', {
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

                items: [
                    Ext.create('Ext.form.ComboBox', {
                        fieldLabel: '请选择工作室',
                        labelWidth: 100,
                        name: 'firmsName',
                        store: Ext.create('MyApp.store.FirmInfoStore', {
                        }),
                        displayField: 'firmsName',
                        valueField: 'firmsName',
                        allowBlank: false,
                        //value:'全部',
                        editable: false
                    }),
                  /*  Ext.create('Ext.form.ComboBox', {
                        fieldLabel: '是否启用',
                        labelWidth: 100,
                        name: 'enabled',
                        store: Ext.create("Ext.data.Store", {
                            model: Ext.regModel("PostInfo", {
                                fields: [{name: "displayName"}, {name: "showValue"}]
                            }),
                            data: [
                                {displayName: "启用", showValue: true},
                                {displayName: "禁用", showValue: false},
                            ]
                        }),
                        displayField: 'displayName',
                        valueField: 'showValue',
                        allowBlank: false,
                        triggerAction: "all",        //单击触发按钮显示全部数据
                        queryMode: "local",
                        /!* forceSelection: true,        //要求输入值必须在列表中存在
                         typeAhead: true,*!/    //允许自动选择匹配的剩余部分文本
                        value: true,
                        editable: false
                    }),*/

                    {
                        fieldLabel: '卖家账号',
                        allowBlank: false,
                        name: 'loginAccount'
                    }
                ],
                buttons: [{
                    text: '保存',
                    handler: function () {
                        var formView = me.getForm(),
                            form = formView.getForm(), params,
                            record = form.getRecord(),
                            url = './goods/addFirmAccount.action',
                            message = '新增';
                        if (!form.isValid()) {
                            return;
                        }
                        form.updateRecord(record);
                        params = {
                            'enabled': record.get('enabled'),
                            'loginAccount': record.get('loginAccount'),

                        };
                        if (me.isUpdate) {
                            url = './goods/updateFirmAccount.action';
                            message = '修改';
                            Ext.Object.merge(params, {
                                'id': record.get('id')
                            });
                        }
                        form.submit({
                            url: url,
                            method: 'POST',
                           params: params,
                            success: function (from, action, json) {
                                var tradeManager = Ext.getCmp('FirmsAccountManager'),
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
Ext.define('MyApp.view.updateFirmAccount', {
    extend: 'Ext.window.Window',
    title: '修改信息',
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
                        fieldLabel: '卖家账号',
                        allowBlank: false,
                        name: 'loginAccount'
                    }
                ],
                buttons: [{
                    text: '保存',
                    handler: function () {
                        var formView = me.getForm(),
                            form = formView.getForm(), params,
                            record = form.getRecord()
                        if (me.isUpdate) {
                            url = './goods/updateFirmAccount.action';
                            message = '修改';
                            params={
                                'id': record.get('id')
                            };
                           /* Ext.Object.merge(params, {
                                'id': record.get('id')
                            });*/
                        }
                        form.submit({
                            url: url,
                            method: 'POST',
                            params: params,
                            success: function (from, action, json) {
                                var tradeManager = Ext.getCmp('FirmsAccountManager'),
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





