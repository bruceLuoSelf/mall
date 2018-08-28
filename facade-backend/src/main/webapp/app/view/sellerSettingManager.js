Ext.define('MyApp.view.SellerSettingWindow', {
    extend: 'Ext.window.Window',
    title: '新增/修改店铺配置信息',
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
                    xtype: 'gameselectorsellersetting',
                    itemId : 'MyApp_view_goods_gamelink_ID',
                    columnWidth: 1,
                    allowBlank: false,
                    gameChanged: function(ths, the, eOpts){
                        var form = me.getForm().getForm();
                        var categoryCombo = form.findField("goodsTypeId");
                        categoryCombo.setRawValue(null);
                        categoryCombo.getStore().load();
                    },
                    fieldLabel: '游戏属性'
                },Ext.create('Ext.form.ComboBox', {
                    itemId: 'MyApp_view_goods_type_ID',
                    fieldLabel: '商品类型',
                    allowBlank: false,
                    name: 'goodsTypeId',
                    columnWidth: 0.5,
                    store: Ext.create(
                        'MyApp.store.ShGameConfigByGameNameStore', {
                            autoLoad: true,
                            listeners: {
                                beforeload : function(store, operation, eOpts) {
                                    var form = me.getForm(),
                                        gameName = form.getForm().findField('gameName');
                                    if (form != null) {
                                        var values = form.getValues(),params={};
                                        params = {
                                            'shGameConfig.gameName': Ext.String.trim(gameName.getRawValue()),
                                        };
                                        Ext.apply(operation,{
                                            params:params
                                        });
                                    };
                                }
                            }
                        }),
                    displayField: 'goodsTypeName',
                    valueField: 'goodsTypeId',
                    editable: false
                }),{
                    fieldLabel: '卖家5173账号',
                    allowBlank: false,
                    name: 'loginAccount',
                    labelWidth: 95,
                    columnWidth: .333
                },{
                    xtype: 'numberfield',
                    fieldLabel: '佣金',
                    allowBlank: false,
                    labelWidth: 75,
                    columnWidth: .333,
                    decimalPrecision: 3,
                    step: 0.001,
                    minValue: 0,
                    maxValue:1,
                    name: 'commision'
                },{
                    xtype: 'numberfield',
                    fieldLabel: '商家信誉',
                    allowBlank: false,
                    columnWidth: .29,
                    labelWidth: 85,
                    name: 'praiseCount',
                    minValue: 1,
                    maxValue:3
                },{
                    xtype : 'tbtext',
                    text : '星',
                    columnWidth: .043
                },{
                    xtype: 'numberfield',
                    fieldLabel: '平均发货速度',
                    allowBlank: false,
                    labelWidth: 95,
                    columnWidth: .29,
                    minValue: 0,
                    maxValue:100,
                    name: 'deliverSpeed'
                },{
                    xtype : 'tbtext',
                    text : '分',
                    columnWidth: .043
                },{
                    xtype: 'numberfield',
                    fieldLabel: '成功率',
                    allowBlank: false,
                    labelWidth: 75,
                    columnWidth: .333,
                    decimalPrecision: 4,
                    step: 0.0001,
                    minValue: 0,
                    maxValue:1,
                    name: 'successPercent'
                },{
                    xtype: 'numberfield',
                    fieldLabel: '月成交笔数',
                    allowBlank: false,
                    columnWidth: .333,
                    labelWidth: 85,
                    name: 'monthDealCount',
                    minValue: 0
                }],
                buttons: [{
                    text:'保存',
                    handler: function() {

                        var formView = me.getForm(),
                            form = formView.getForm(),
                            gameName = form.findField('gameName'),
                            goodsTypeId = form.findField('goodsTypeId'),
                            record = form.getRecord(),
                            url = './repository/addSellerSetting.action';
                        if(!form.isValid()){
                            return;
                        }
                        form.updateRecord(record);
                        var  params = {
                                'sellerSetting.gameName': Ext.String.trim(gameName.getRawValue()),
                                'sellerSetting.loginAccount': Ext.String.trim(record.get('loginAccount')),
                                'sellerSetting.commision': record.get('commision'),
                                'sellerSetting.praiseCount': record.get('praiseCount'),
                                'sellerSetting.successPercent': record.get('successPercent'),
                                'sellerSetting.deliverSpeed': record.get('deliverSpeed'),
                                'sellerSetting.monthDealCount': record.get('monthDealCount'),
                                'sellerSetting.goodsTypeId':goodsTypeId.getValue(),
                                'sellerSetting.goodsTypeName':goodsTypeId.getRawValue()//ZW_C_JB_00008_20170515 ADD

                        };
                        if(me.isUpdate){
                            url = './repository/modifySellerSetting.action';
                            Ext.Object.merge(params,{
                                'id': record.get('id')
                            });
                        }
                        form.submit({
                            url : url,
                            method: 'POST',
                            params: params,
                            success : function(from, action, json) {
                                var sellerSettingManager = Ext.getCmp('sellerSettingManager'),
                                    store = sellerSettingManager.getStore();
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
            gameProp = formView.getComponent('MyApp_view_goods_gamelink_ID'),
            goodsProp =formView.getComponent('MyApp_view_goods_type_ID'),
        form.reset();
        form.loadRecord(record);
        me.isUpdate = isUpdate;
        if(!isUpdate){
            gameProp.setDisabled(false);
            goodsProp.setDisabled(false);
            form.reset();
        }
        else{
            gameProp.setDisabled(true);
            goodsProp.setDisabled(true);
        }
    },
    constructor : function(config) {
        var me = this, cfg = Ext.apply({}, config);
        me.items = [me.getForm()]
        me.callParent([ cfg ]);
    }
});

Ext.define('MyApp.view.sellerSettingManager', {
    extend: 'Ext.panel.Panel',
    id:'sellerSettingManager',
    closable: true,
    title: '店铺配置',
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
                            fn: me.addSellerSetting,
                            scope: me
                        }
                    }
                },'-',{
                    text: '修改',
                    listeners: {
                        click: {
                            fn: me.modifySellerSetting,
                            scope: me
                        }
                    }
                },'-',{
                    text: '删除',
                    listeners: {
                        click: {
                            fn: me.deleteSellerSetting,
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
                },'-',{
                    xtype: 'button',
                    itemId: 'moveTopButton',
                    text: '置顶',
                    listeners: {
                        click: {
                            fn: me.moveTop,
                            scope: me
                        }
                    }
                },'-',{
                    xtype: 'button',
                    itemId: 'cancelMoveTopButton',
                    text: '取消置顶',
                    listeners: {
                        click: {
                            fn: me.cancelMoveTop,
                            scope: me
                        }
                    }
                }]
            });
        }
        return me.toolbar;
    },
    // 新增配置
    addSellerSetting: function(button, e, eOpts) {
        var window = this.getSellerSettingWindow();
        window.bindData(Ext.create('MyApp.model.SellerSettingModel'), false);
        window.show();
    },
    // 修改配置
    modifySellerSetting: function(button, e, eOpts) {
        var grid = this.getLogGrid(),
            selModel = grid.getSelectionModel(),
            selRecords = selModel.getSelection(),
            window = this.getSellerSettingWindow();
        if(selRecords == null||selRecords.length<=0){
            Ext.ux.Toast.msg("温馨提示", "请先选择要修改的游戏配置信息");
            return;
        }
        window.bindData(selRecords[0], true);
        window.show();
    },
    // 删除配置
    deleteSellerSetting: function(button, e, eOpts) {
        var grid = this.getLogGrid(),
            selModel = grid.getSelectionModel(),
            selRecords = selModel.getSelection();
        if(selRecords == null||selRecords.length<=0){
            Ext.ux.Toast.msg("温馨提示", "请先选择要删除的店铺配置");
            return;
        }
        var ids = [];
        for (var i = 0, j = selRecords.length; i < j; i++) {
            ids.push(selRecords[i].get("id"));
        }
        Ext.MessageBox.confirm('温馨提示', '确定删除该店铺配置吗？', function(btn){
            if(btn == 'yes'){
                Ext.Ajax.request({
                    url : './repository/deleteSellerSetting.action',
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
    // 置顶
    moveTop: function(button, e, eOpts) {
        var grid = this.getLogGrid(),
            selModel = grid.getSelectionModel(),
            selRecords = selModel.getSelection();
        if(selRecords == null||selRecords.length<=0){
            Ext.ux.Toast.msg("温馨提示", "请先选择1条要置顶的店铺");
            return;
        }
        Ext.MessageBox.confirm('温馨提示', '确定要置顶该店铺吗？', function(btn){
            if(btn == 'yes'){
                Ext.Ajax.request({
                    url : './repository/updateShopSort.action',
                    params: {'id':selRecords[0].get("id"), 'sort': 1},
                    success : function(response, opts) {
                        Ext.ux.Toast.msg("温馨提示", "置顶成功！");
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
    // 取消店铺置顶
    cancelMoveTop: function(button, e, eOpts) {
        var grid = this.getLogGrid(),
            selModel = grid.getSelectionModel(),
            selRecords = selModel.getSelection();
        if(selRecords == null||selRecords.length<=0){
            Ext.ux.Toast.msg("温馨提示", "请先选择1条要取消置顶的店铺");
            return;
        }
        Ext.MessageBox.confirm('温馨提示', '确定要取消置顶该店铺吗？', function(btn){
            if(btn == 'yes'){
                Ext.Ajax.request({
                    url : './repository/updateShopSort.action',
                    params: {'id':selRecords[0].get("id"), 'sort': 0},
                    success : function(response, opts) {
                        Ext.ux.Toast.msg("温馨提示", "置顶成功！");
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
    // 禁用店铺配置
    disableUser: function(button, e, eOpts) {
        var grid = this.getLogGrid(),
            selModel = grid.getSelectionModel(),
            selRecords = selModel.getSelection();
        if(selRecords == null||selRecords.length<=0){
            Ext.ux.Toast.msg("温馨提示", "请先选择要禁用的店铺配置");
            return;
        }
        var ids = [];
        for (var i = 0, j = selRecords.length; i < j; i++) {
            ids.push(selRecords[i].get("id"));
        }
        Ext.MessageBox.confirm('温馨提示', '确定禁用该店铺配置吗？', function(btn){
            if(btn == 'yes'){
                Ext.Ajax.request({
                    url : './repository/settingEnable.action',
                    params: {'ids':ids,'sellerSetting.isDeleted':true},
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
    // 启用店铺配置
    enableUser: function(button, e, eOpts) {
        var grid = this.getLogGrid(),
            selModel = grid.getSelectionModel(),
            selRecords = selModel.getSelection();
        if(selRecords == null||selRecords.length<=0){
            Ext.ux.Toast.msg("温馨提示", "请先选择要启用的店铺配置");
            return;
        }
        var ids = [];
        for (var i = 0, j = selRecords.length; i < j; i++) {
            ids.push(selRecords[i].get("id"));
        }
        Ext.MessageBox.confirm('温馨提示', '确定启用该店铺配置吗？', function(btn){
            if(btn == 'yes'){
                Ext.Ajax.request({
                    url : './repository/settingEnable.action',
                    params: {'ids':ids,'sellerSetting.isDeleted':false},
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

    sellerSettingWindow: null,
    getSellerSettingWindow: function(){
        if(this.sellerSettingWindow == null){
            this.sellerSettingWindow = new MyApp.view.SellerSettingWindow();
        }
        return this.sellerSettingWindow;
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
                    fieldLabel: '卖家5173账号',
                    name: 'loginAccount'
                },{
                    xtype: 'gameselectorsellersetting',
                    itemId : 'MyApp_view_goods_gamelink_ID',
                    columnWidth: .7,
                    allowBlank: true,
                    fieldLabel: '游戏属性'
                },Ext.create('Ext.form.ComboBox', {
                    fieldLabel: '商品类型',
                    labelWidth: 100,
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
            me.store = Ext.create('MyApp.store.SellerSettingStore',{
                autoLoad: true,
                listeners: {
                    beforeload : function(store, operation, eOpts) {
                        var queryForm = me.getQueryForm();
                        var gameName = queryForm.getForm().findField('gameName');
                        if (queryForm != null) {
                            var values = queryForm.getValues();
                            gameName = Ext.String.trim(gameName.getRawValue());
                            //var region = Ext.String.trim(values.region);
                            Ext.apply(operation, {
                                params: {
                                    'sellerSetting.loginAccount': values.loginAccount,
                                    'sellerSetting.gameName': gameName,
                                    'sellerSetting.goodsTypeName': values.goodsTypeName //ZW_C_JB_00008_20170515 ADD
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
                    dataIndex: 'goodsTypeName',
                    text: '商品类型',
                    flex: 0.7,
                    align: 'center'
                },{
                    dataIndex: 'loginAccount',
                    text: '卖家5173账号',
                    flex: 0.7,
                    align: 'center'
                },{
                    dataIndex: 'commision',
                    text: '佣金',
                    flex: 0.7,
                    align: 'center'
                },{
                    dataIndex: 'successPercent',
                    text: '成功率',
                    flex: 0.7,
                    align: 'center'
                },{
                    dataIndex: 'deliverSpeed',
                    text: '平均发货速度',
                    flex: 0.7,
                    align: 'center'
                },{
                    dataIndex: 'praiseCount',
                    text: '商家信誉',
                    flex: 0.7,
                    align: 'center'
                },{
                    dataIndex: 'monthDealCount',
                    text: '月成交笔数',
                    flex: 0.7,
                    align: 'center'
                },{
                    dataIndex: 'isDeleted',
                    text: '是否启用',
                    flex: 1,
                    align: 'center',
                    renderer: function(value, metaData, record, rowIndex, colIndex, store, view) {
                        return CommonFunction.rendererEnable(value);
                    }
                },{
                    dataIndex: 'sort',
                    text: '是否置顶',
                    flex: 1,
                    align: 'center',
                    renderer: function(value, metaData, record, rowIndex, colIndex, store, view) {
                        if (Ext.isEmpty(value))
                            return '<input type="checkbox" unChecked onclick="return false" />';

                        if (value > 0) {
                            return '<input type="checkbox" Checked onclick="return false" />';
                        } else {
                            return '<input type="checkbox" unChecked onclick="return false" />';
                        }
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