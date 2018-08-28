Ext.define('MyApp.view.shGameTradeManager',{
    extend:'Ext.panel.Panel',
    id:'shGameTradeManager',
    closable:true,
    layout:"border",
    title:'游戏交易配置',
    autoScroll:false,
    toolbar:null,
    getToolbar:function(){
        var me = this;
        if (Ext.isEmpty(me.toolbar)) {
            me.toobar = Ext.widget('toolbar',{
                dock:'top',
                items:[{
                    text:'添加配置',
                    listeners:{
                        click:{
                            fn:me.addShGameTrade,
                            scope:me
                        }
                    }
                },
                //     '-',{
                //     text:'修改配置',
                //     listeners: {
                //         click: {
                //             fn: me.updateShGameTrade,
                //             scope: me
                //         }
                //     }
                // },
                    '-',{
                    text:'删除配置',
                    listeners: {
                        click: {
                            fn: me.deleteShGameTrade,
                            scope: me
                        }
                    }
                }]
            });
        }
        return me.toobar;
    },
    // 增加配置窗口
    addShGameTradeWindow: null,
    getAddShGameTradeWindow: function () {
        if (this.addShGameTradeWindow == null) {
            this.addShGameTradeWindow = new MyApp.view.addShGameTradeWindow();
        }
        return this.addShGameTradeWindow;
    },

    addShGameTrade:function(button,e,eOpts) {
        var window = this.getAddShGameTradeWindow();
        window.bindData(Ext.create('MyApp.model.ShGameTradeModel'),false);
        window.show();
    },
    // 修改
    updateShGameTrade: function (button, e, eOpts) {
        var grid = this.getShGameTradeGrid(),
            selModel = grid.getSelectionModel(),
            selRecords = selModel.getSelection(),
            window = this.getAddShGameTradeWindow();
        if (selRecords == null || selRecords.length <= 0) {
            Ext.ux.Toast.msg("温馨提示", "请先选择要修改配置信息");
            return;
        }
        window.bindData(selRecords[0], true);
        window.show();
    },

    deleteShGameTrade:function(){
        var me = this,
            grid = me.getShGameTradeGrid(),
            selModel = grid.getSelectionModel(),
            selRecords = selModel.getSelection();
        if (selRecords == null || selRecords.length <= 0) {
            Ext.ux.Toast.msg("温馨提示", "请先选择要删除的配置")
            return ;
        }
        var record = selRecords[0];
        Ext.MessageBox.confirm('温馨提示','确定删除吗?',function (btn) {
            if(btn == 'yes') {
                me.getStore().remove(record);
                Ext.Ajax.request({
                    url: './delivery/deleteShGameTrade.action',
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
            }else{
                return;
            }
        })
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
                    xtype: 'textfield'
                },items:[Ext.create('Ext.form.ComboBox', {
                    fieldLabel: '游戏商品类型',
                    labelWidth: 100,
                    name: 'gameGoodsTypeName',
                    store: Ext.create('MyApp.store.ShGameGoodsTypeStore', {
                    }),
                    displayField: 'gameGoodsTypeName',
                    valueField: 'id',
                    editable: false
                }), DataDictionary.getDataDictionaryCombo('shMode', {
                    fieldLabel: '收货模式',
                    editable: false,
                    name: 'shMode',
                    labelWidth: 100,
                },{value:0,display:'全部'})],
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
    store: null,
    getStore: function(){
        var me = this;
        if(me.store==null){
            me.store = Ext.create('MyApp.store.ShGameTradeStore',{
                autoLoad: true,
                listeners: {
                    beforeload : function(store, operation, eOpts) {
                        var queryForm = me.getQueryForm();
                        var values = queryForm.getValues();
                        var gameTableId = queryForm.getForm().findField('gameGoodsTypeName').getValue();
                        if (queryForm != null) {
                            Ext.apply(operation, {
                                params: {
                                    'shGameTrade.gameTableId': gameTableId,
                                    'shGameTrade.shMode': values.shMode
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
    shGameTradeGrid: null,
    getShGameTradeGrid: function () {
        var me = this;
        if (Ext.isEmpty(me.shGameTradeGrid)) {
            me.shGameTradeGrid = Ext.widget('gridpanel',{
                header: false,
                region: 'center',
                columnLines: true,
                store: me.getStore(),
                columns: [{
                    xtype: 'rownumberer'
                },{
                    dataIndex: 'gameGoodsTypeName',
                    text: '游戏配置',
                    flex: 1,
                    align: 'center'
                }, {
                    dataIndex: 'tradeTypeName',
                    text: '交易配置',
                    flex: 0.5,
                    align: 'center'
                }, {
                    dataIndex: 'shMode',
                    sortable: false,
                    flex: 0.5,
                    text: '收货模式',
                    renderer: function (value, metaData, record, rowIndex, colIndex, store, view) {
                        return DataDictionary.rendererSubmitToDisplay(value, 'shMode');
                    }
                }],
                dockedItems: [me.getToolbar(), me.getPagingToolbar()],
                selModel: Ext.create('Ext.selection.CheckboxModel', {
                    allowDeselect: true,
                    mode: 'SINGLE'
                })
            })
        }
        return me.shGameTradeGrid;
    },
    initComponent:function(){
        DataDictionary.loadDictDataByTradeType();
        var me = this;
        Ext.applyIf(me,{
            items:[me.getQueryForm(),me.getShGameTradeGrid()]
        });
        me.callParent(arguments);
    }
});

Ext.define('MyApp.view.addShGameTradeWindow',{
    extend: 'Ext.window.Window',
    title: '新增游戏交易配置信息',
    width: 1000,
    border: false,
    padding: '10 10 10 10',
    autoScroll: true,
    closeAction: 'hide',
    modal: true,
    layout: 'anchor',
    form: null,
    deliveryData: null,
    getForm:function(){
        var me = this;
        if (me.form == null) {
            me.form = Ext.widget('form',{
                layout:'column',
                defaults:{
                    margin:'10 10 5 5',
                    columnWidth: .5,
                    labelWidth: 130,
                    xtype: 'textfield'
                },
                items: [
                    Ext.create('Ext.form.ComboBox', {
                    fieldLabel: '游戏商品类型',
                    itemId: 'MyApp_game_goods_type_ID',
                    allowBlank: false,
                    name: 'gameGoodsTypeName',
                    columnWidth: 0.5,
                    store: Ext.create(
                        'MyApp.store.ShGameGoodsTypeStore', {
                            autoLoad: true
                        }),
                    displayField: 'gameGoodsTypeName',
                    valueField: 'id',
                    editable: false
                }), DataDictionary.getDataDictionaryCombo('shMode', {
                        fieldLabel: '收货模式',
                        editable: false,
                        name: 'shMode',
                        labelWidth: 100,
                        value: 1
                    }),DataDictionary.getDataDictionaryCheckboxGroup('tradeTypes',{
                        fieldLabel: '交易类型',
                        name: 'tradeTypes',
                        readOnly: true,
                        editable: false,
                        labelWidth: 100,
                        columnWidth: 1
                    })],
                buttons:[{
                    text:'保存',
                    handler:function(){
                        var form = me.getForm().getForm(),
                            record = form.getRecord(),
                            params = {},
                            url = './delivery/addShGameTrade.action';
                        form.updateRecord(record);
                        var myCheckboxGroup = form.findField('tradeTypes');
                        var cbitems = myCheckboxGroup.items;
                        var ids = [];
                        for (var i = 0; i < cbitems.length; i++) {
                            if (cbitems.get(i).checked) {
                                ids.push(cbitems.get(i).name);
                            }
                        }
                        console.log(ids)
                        if(ids.length==0){
                            Ext.ux.Toast.msg("温馨提示", "请选择交易类型");
                            return;
                        }
                        params = {
                            'shGameTrade.gameTableId':form.findField('gameGoodsTypeName').getValue(),
                            'shGameTrade.shMode':record.get('shMode'),
                            'tradeIds':ids
                        }
                        form.submit({
                            url:url,
                            method:'POST',
                            params:params,
                            success:function(from,action,json){
                                var shGameTradeManager = Ext.getCmp('shGameTradeManager'),
                                    store = shGameTradeManager.getStore();
                                if (json.errorMessage != null) {
                                    Ext.ux.Toast.msg("温馨提示",json.errorMessage);
                                }else{
                                    Ext.ux.Toast.msg("温馨提示，新增成功");
                                    me.close();
                                    store.load();
                                }
                            },
                            exception:function(from,action,json) {
                                Ext.ux.Toast.msg("温馨提示", json.message);
                            }
                        });
                    }
                }]
                    
            });
        }
        return me.form;
    },
    isUpdate:null,
    bindData: function (record,isUpdate) {
        var me = this,
            formView = me.getForm(),
            form = formView.getForm();
        console.log(record)
        form.reset();
        form.loadRecord(record);
        var tradeTableId = record.data.tradeTableId;
        var gameGoodsTypeProp = formView.getComponent('MyApp_game_goods_type_ID');
        me.isUpdate = isUpdate;
        if (!isUpdate) {
            gameGoodsTypeProp.setDisabled(false);
            form.reset();
        }else{
            form.findField('gameGoodsTypeName')
            gameGoodsTypeProp.setDisabled(true);
            
        }
    },
    constructor:function(config) {
        var me = this, cfg = Ext.apply({}, config);
        me.items = [me.getForm()]
        me.callParent([cfg]);
    }
});
