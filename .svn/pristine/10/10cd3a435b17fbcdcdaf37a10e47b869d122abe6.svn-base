Ext.define('MyApp.view.AutoDeliverSettingWindow', {
    extend: 'Ext.window.Window',
    title: '新增寄售全自动配置信息',
    width: 600,
    closeAction: 'hide',
    modal: true,
    form: null,
    getForm: function(){
        var me = this;
        if(me.form==null){
            me.form = Ext.widget('form',{
                layout: 'column',
                defaults: {
                    margin: '10 10 5 5',
                    columnWidth: .5,
                    labelWidth: 130,
                    xtype: 'textfield'
                },
                items: [{
                    xtype: 'gamelinkselectorsellersetting',
                    itemId : 'MyApp_view_goods_gamelink_ID',
                    columnWidth: 1,
                    allowBlank: false,
                    fieldLabel: '游戏属性'
                }],
                buttons: [{
                    text:'保存',
                    handler: function() {
                        var formView = me.getForm(),
                            form = formView.getForm(),
                            gameName = form.findField('gameName'),
                            record = form.getRecord(),
                            url = './repository/addAutoDeliverSetting.action';
                        if(!form.isValid()){
                            return;
                        }
                        form.updateRecord(record);
                        var  params = {
                            'autoDeliverSetting.gameName': Ext.String.trim(gameName.getRawValue()),
                            'autoDeliverSetting.region': record.get('region')
                        };
                        if(me.isUpdate){
                            url = './repository/modifyAutoDeliverSetting.action';
                            Ext.Object.merge(params,{
                                'id': record.get('id')
                            });
                        }
                        form.submit({
                            url : url,
                            method: 'POST',
                            params: params,
                            success : function(from, action, json) {
                                var autoDeliverSettingManager = Ext.getCmp('autoDeliverSettingManager'),
                                    store = autoDeliverSettingManager.getStore();
                                Ext.ux.Toast.msg("温馨提示", json.message);
                                me.close();
                                store.load();
                            },
                            exception : function(from, action, json) {
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
    bindData: function(record,isUpdate){
        var me = this,
            formView = me.getForm()
        form = formView.getForm(),
            gameProp = formView.getComponent('MyApp_view_goods_gamelink_ID');
        form.reset();
        form.loadRecord(record);
        me.isUpdate = isUpdate;
        if(!isUpdate){
            gameProp.setDisabled(false);
            form.reset();
        }
        else{
            gameProp.setDisabled(true);
        }
    },
    constructor : function(config) {
        var me = this, cfg = Ext.apply({}, config);
        me.items = [me.getForm()]
        me.callParent([ cfg ]);
    }
});

Ext.define('MyApp.view.autoDeliverSettingManager', {
    extend: 'Ext.panel.Panel',
    id:'autoDeliverSettingManager',
    closable: true,
    title: '寄售全自动配置',
    autoScroll: false,
    listeners:{
        'resize':function(){
            this.logGrid.setHeight(window.document.body.offsetHeight-190);
        }
    },
    toolbar: null,
    getToolbar: function(){
        var me = this;
        if(Ext.isEmpty(me.toolbar)){
            me.toolbar = Ext.widget('toolbar',{
                dock: 'top',
                items: [{
                    text: '新增',
                    listeners: {
                        click: {
                            fn: me.addAutoDeliverSetting,
                            scope: me
                        }
                    }
                },'-',{
                    text: '删除',
                    listeners: {
                        click: {
                            fn: me.deleteAutoDeliverSetting,
                            scope: me
                        }
                    }
                },'-',{
                    xtype: 'button',
                    itemId: 'enableButton',
                    text: '启用',
                    listeners: {
                        click: {
                            fn: me.enableUser,
                            scope: me
                        }
                    }
                },'-',{
                    xtype: 'button',
                    itemId: 'disableButton',
                    text: '禁用',
                    listeners: {
                        click: {
                            fn: me.disableUser,
                            scope: me
                        }
                    }
                }]
            });
        }
        return me.toolbar;
    },
    // 新增配置
    addAutoDeliverSetting: function(button, e, eOpts) {
        var window = this.getAutoDeliverSettingWindow();
        window.bindData(Ext.create('MyApp.model.AutoDeliverSettingModel'), false);
        window.show();
    },
    // 删除配置
    deleteAutoDeliverSetting: function(button, e, eOpts) {
        var grid = this.getLogGrid(),
            selModel = grid.getSelectionModel(),
            selRecords = selModel.getSelection();
        if(selRecords == null||selRecords.length<=0){
            Ext.ux.Toast.msg("温馨提示", "请先选择要删除的配置");
            return;
        }
        var ids = [];
        for (var i = 0, j = selRecords.length; i < j; i++) {
            ids.push(selRecords[i].get("id"));
        }
        Ext.MessageBox.confirm('温馨提示', '确定删除该配置吗？', function(btn){
            if(btn == 'yes'){
                Ext.Ajax.request({
                    url : './repository/deleteAutoDeliverSetting.action',
                    params: {'ids': ids},
                    success : function(response, opts) {
                        Ext.ux.Toast.msg("温馨提示", "删除成功！");
                        grid.getStore().load();
                        grid.getSelectionModel().deselectAll();
                    },
                    exception : function(response, opts) {
                        var json = Ext.decode(response.responseText);
                        Ext.ux.Toast.msg("温馨提示", json.message);
                    }
                });
            }else{
                return;
            }
        });
    },
    // 禁用配置
    disableUser: function(button, e, eOpts) {
        var grid = this.getLogGrid(),
            selModel = grid.getSelectionModel(),
            selRecords = selModel.getSelection();
        if(selRecords == null||selRecords.length<=0){
            Ext.ux.Toast.msg("温馨提示", "请先选择要禁用的配置");
            return;
        }
        var ids = [];
        for (var i = 0, j = selRecords.length; i < j; i++) {
            ids.push(selRecords[i].get("id"));
        }
        Ext.MessageBox.confirm('温馨提示', '确定禁用该配置吗？', function(btn){
            if(btn == 'yes'){
                Ext.Ajax.request({
                    url : './repository/settingEnableDeliverSetting.action',
                    params: {'ids':ids,'autoDeliverSetting.enabled':false},
                    success : function(response, opts) {
                        Ext.ux.Toast.msg("温馨提示", "禁用成功！");
                        grid.getStore().load();
                        grid.getSelectionModel().deselectAll();
                    },
                    exception : function(response, opts) {
                        var json = Ext.decode(response.responseText);
                        Ext.ux.Toast.msg("温馨提示", json.message);
                    }
                });
            }else{
                return;
            }
        });
    },
    // 启用配置
    enableUser: function(button, e, eOpts) {
        var grid = this.getLogGrid(),
            selModel = grid.getSelectionModel(),
            selRecords = selModel.getSelection();
        if(selRecords == null||selRecords.length<=0){
            Ext.ux.Toast.msg("温馨提示", "请先选择要启用的配置");
            return;
        }
        var ids = [];
        for (var i = 0, j = selRecords.length; i < j; i++) {
            ids.push(selRecords[i].get("id"));
        }
        Ext.MessageBox.confirm('温馨提示', '确定启用该配置吗？', function(btn){
            if(btn == 'yes'){
                Ext.Ajax.request({
                    url : './repository/settingEnableDeliverSetting.action',
                    params: {'ids':ids,'autoDeliverSetting.enabled':true},
                    success : function(response, opts) {
                        Ext.ux.Toast.msg("温馨提示", "启用成功！");
                        grid.getStore().load();
                        grid.getSelectionModel().deselectAll();
                    },
                    exception : function(response, opts) {
                        var json = Ext.decode(response.responseText);
                        Ext.ux.Toast.msg("温馨提示", json.message);
                    }
                });
            }else{
                return;
            }
        });
    },

    autoDeliverSettingWindow: null,
    getAutoDeliverSettingWindow: function(){
        if(this.autoDeliverSettingWindow == null){
            this.autoDeliverSettingWindow = new MyApp.view.AutoDeliverSettingWindow();
        }
        return this.autoDeliverSettingWindow;
    },
    queryForm: null,
    getQueryForm: function(){
        var me = this;
        if(me.queryForm==null){
            me.queryForm = Ext.widget('form',{
                layout: 'column',
                defaults: {
                    margin: '10 10 10 10',
                    xtype: 'textfield'
                },
                items: [{
                    xtype: 'gamelinkselectorsellersetting',
                    itemId : 'MyApp_view_goods_gamelink_ID',
                    columnWidth: .7,
                    allowBlank: true,
                    fieldLabel: '游戏属性'
                }],
                buttons: [{
                    text:'重置',
                    handler: function() {
                        me.getQueryForm().getForm().reset();
                    }
                },'->',{
                    text:'查询',
                    handler: function() {
                        me.getPagingToolbar().moveFirst();
                    }
                }]
            });
        }
        return me.queryForm;
    },
    store: null,
    getStore: function(){
        var me = this;
        if(me.store==null){
            me.store = Ext.create('MyApp.store.AutoDeliverSettingStore',{
                autoLoad: true,
                listeners: {
                    beforeload : function(store, operation, eOpts) {
                        var queryForm = me.getQueryForm();
                        var gameName = queryForm.getForm().findField('gameName');
                        if (queryForm != null) {
                            var values = queryForm.getValues();
                            gameName = Ext.String.trim(gameName.getRawValue());
                            var region = Ext.String.trim(values.region);
                            Ext.apply(operation, {
                                params: {
                                    'autoDeliverSetting.gameName': gameName,
                                    'autoDeliverSetting.region': region
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
    getPagingToolbar: function(){
        var me = this;
        if(me.pagingToolbar==null){
            me.pagingToolbar = Ext.widget('pagingtoolbar',{
                store: me.getStore(),
                dock: 'bottom',
                displayInfo: true
            });
        }
        return me.pagingToolbar;
    },
    logGrid: null,
    getLogGrid: function(){
        var me = this;
        if(Ext.isEmpty(me.logGrid)){
            me.logGrid = Ext.widget('gridpanel',{
                header: false,
                columnLines: true,
                store: me.getStore(),
                columns: [{
                    xtype: 'rownumberer'
                },{
                    dataIndex: 'gameName',
                    text: '游戏',
                    flex: 0.7,
                    align: 'center'
                },{
                    dataIndex: 'region',
                    text: '所在区',
                    flex: 0.7,
                    align: 'center'
                },{
                    dataIndex: 'enabled',
                    text: '是否启用',
                    flex: 1,
                    align: 'center',
                    renderer: function(value, metaData, record, rowIndex, colIndex, store, view) {
                        return CommonFunction.rendererEnable(!value);
                    }
                }],
                dockedItems: [me.getToolbar(),me.getPagingToolbar()],
                selModel: Ext.create('Ext.selection.CheckboxModel', {
                    allowDeselect: true,
                    mode: 'MULTI'
                })
            });
        }
        return me.logGrid;

    },
    initComponent: function() {
        var me = this;
        Ext.applyIf(me, {
            items: [me.getQueryForm(),me.getLogGrid()]
        });
        me.callParent(arguments);
    }
});