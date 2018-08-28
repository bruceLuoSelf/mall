/*
 * 订单管理页面
 */
Ext.define('MyApp.view.OrderSimpleInfoForm',{
    extend: 'Ext.form.Panel',
    layout: 'column',
    title: '订单信息',
    defaults: {
        margin: '2 2 2 2',
        columnWidth: .333,
        labelWidth: 80,
        readOnly: true,
        xtype: 'textfield'
    },
    constructor : function(config) {
        var me = this, cfg = Ext.apply({}, config);
        me.items = [{
            name: 'orderId',
            fieldLabel: '订单号'
        },{
            name: 'userAccount',
            fieldLabel: '买家'
        },{
            name: 'receiver',
            fieldLabel: '游戏角色'
        },{
            name: 'mobileNumber',
            fieldLabel: '联系方式'
        },{
            name: 'qq',
            fieldLabel: 'QQ'
        },{
            fieldLabel: '数量',
            name: 'goldCount'
        },{
            fieldLabel: '单价',
            name: 'unitPrice'
        },{
            fieldLabel: '价格',
            xtype: 'numberfield',
            decimalPrecision: 2,
            name: 'totalPrice'
        },{
            fieldLabel: '接手客服',
            name: 'realName'
        },DataDictionary.getDataDictionaryCombo('tradeType',{
            fieldLabel: '交易方式',
            name: 'tradeType',
            readOnly: true,
            columnWidth: .333,
            labelWidth: 80
        }),{
            xtype: 'textareafield',
            columnWidth: 1,
            fieldLabel: '备注',
            name: 'notes',
            height: 50
        }];
        me.callParent([ cfg ]);
    }
});



Ext.define('MyApp.view.DetailOrderWindow',{
    extend: 'Ext.window.Window',
    width: 800,
    autoScroll: true,
    maxHeight: 1000,
    title: '处理订单',
    closeAction: 'hide',
    modal: true,
    defaults: {
        margin: '5 5 5 5'
    },
    form: null,
    getForm: function(){
        var me = this;
        if(me.form==null){
            me.form = Ext.create('MyApp.view.OrderSimpleInfoForm');
        }
        return me.form;
    },
    panel: null,
    getPanel: function(){
        var me = this;
        if(me.panel == null){
            me.panel = Ext.create('MyApp.order.OrderDetailPanel');
        }
        return me.panel;
    },
    bindData: function(record){
        var me = this,
            form = me.getForm().getForm(),
            realName = form.findField('realName'),
            panel = me.getPanel();
        form.reset();
        form.loadRecord(record);
        realName.setValue(record.get('servicerInfo').realName);
        panel.loadData(record);
    },
    initComponent: function() {
        var me = this;
        Ext.applyIf(me, {
            items: [me.getForm(),me.getPanel()]
        });
        me.callParent(arguments);
    }
});

Ext.define('MyApp.view.changeStateWindow', {
    extend: 'Ext.window.Window',
    width: 700,
    title: '修改订单状态',
    closeAction: 'hide',
    modal: true,
    form: null,
    getForm: function(){
        var me = this;
        if(me.form==null){
            me.form = Ext.widget('form',{
                layout: 'column',
                defaults: {
                    margin: '5 5 5 5',
                    columnWidth: .5,
                    labelWidth: 100,
                    readOnly: true,
                    xtype: 'textfield'
                },
                items: [{
                    name: 'orderId',
                    fieldLabel: '订单号'
                },{
                    name: 'userAccount',
                    fieldLabel: '所属用户'
                },{
                    name: 'payTime',
                    xtype: 'datefield',
                    format:'Y-m-d H:i:s',
                    fieldLabel: '付款时间'
                },{
                    name: 'deliveryTime',
                    fieldLabel: '发货速度(分钟)'
                },DataDictionary.getDataDictionaryCombo('orderState',{
                    fieldLabel: '订单状态',
                    labelWidth: 100,
                    allowBlank: false,
                    name: 'orderState',
                    listeners: {
                        'expand': function(combo, e){
                            var store = combo.getStore(),orderStateValue = combo.getValue();
                            me.filterData(store, orderStateValue);
                        }
                    },
                    editable: false
                }),DataDictionary.getDataDictionaryCombo('check',{
                    fieldLabel: '是否延迟',
                    name: 'isDelay',
                    labelWidth: 100,
                    editable: false
                }),DataDictionary.getDataDictionaryCombo('refundReason',{
                    fieldLabel: '退款原因',
                    name: 'refundReason',
                    labelWidth: 100,
                    columnWidth: 1,
                    editable: false
                })],
                buttons: [me.getSaveButton()]
            });
        }
        return this.form;
    },
    isUpdate: null,
    saveButton: null,
    getSaveButton: function(){
        var me = this;
        if(me.saveButton==null){
            me.saveButton = Ext.create('Ext.Button',{
                text:'保存',
                formBind: true,
                disabled: true,
                handler: function() {
                    var form = me.getForm().getForm(),
                        order = form.getRecord();
                    if(order.get('gameName')=="魔兽世界(国服)"&&form.findField('orderState').getValue()==5){
                        Ext.ux.Toast.msg("温馨提示","魔兽世界不能人工结单");
                        return;
                    }
                    if(form.findField('refundReason').getValue()==0&&form.findField('orderState').getValue()==6){
                        Ext.ux.Toast.msg("温馨提示","请选择退款原因");
                        return;
                    }
                    form.updateRecord(order);
                    if(form.isValid()){
                        Ext.Ajax.request({
                            url : './order/changeOrderState.action',
                            method: 'POST',
                            jsonData: {
                                'orderInfo': order.data
                            },
                            success : function(response, opts) {
                                var orderManager = Ext.getCmp('orderManager'),
                                    store = orderManager.getStore();
                                Ext.ux.Toast.msg("温馨提示", "订单状态设置成功");
                                me.close();
                                store.load();
                            },
                            exception : function(response, opts) {
                                var json = Ext.decode(response.responseText);
                                Ext.ux.Toast.msg("温馨提示", json.message);
                            }
                        });
                    }
                }
            });
        }
        return me.saveButton;
    },
    filterData: function(store, orderStateValue){
        store.removeAll();
        if(orderStateValue==1){
            store.loadData(DataDictionary.getDataByTermsCode('orderState',[orderStateValue,7]));
            return;
        }
        if(orderStateValue==2||orderStateValue==3){
            store.loadData(DataDictionary.getDataByTermsCode('orderState',[orderStateValue,6]));
            return;
        }
        if(orderStateValue==4){
            store.loadData(DataDictionary.getDataByTermsCode('orderState',[orderStateValue,5]));
            return;
        }
        store.loadData(DataDictionary.getDataByTermsCode('orderState',[orderStateValue]));
        /*store.filterBy(function(item) {
         if(item.get("value")==orderStateValue){
         return true;
         }
         //待付款 -- 已取消(可后台修改)
         if(orderStateValue==1){
         return item.get("value")==7;
         }
         //已付款 -- 已退款(可后台修改)
         if(orderStateValue==2){
         return item.get("value")==6;
         }
         //待发货 -- 已退款(可后台修改)
         if(orderStateValue==3){
         return item.get("value")==6;
         }
         //已发货 -- 结单(可后台修改)
         if(orderStateValue==4){
         return item.get("value")==5;
         }
         });*/
    },
    bindData: function(record){
        var me = this,
            form = me.getForm().getForm(),
            saveButton = me.getSaveButton(),
            orderState = form.findField('orderState'),
            orderStateValue = record.get('orderState'),
            store = orderState.getStore();
        form.reset();
        if(orderStateValue<5){
            saveButton.setVisible(true);
            orderState.setDisabled(false);
        }else{
            saveButton.setVisible(false);
            orderState.setDisabled(true);
        }
        //待付款 -- 已取消(可后台修改)

        //已付款 -- 待发货(配单修改)
        //已付款 -- 已退款(可后台修改)

        //待发货 -- 已发货(RC/其他系统)
        //待发货 -- 已退款(可后台修改)

        //已发货 -- 结单(可后台修改、自动修改、前台修改)

        //已退款、已取消及结单后，状态不可修改
        me.filterData(store, orderStateValue);
        form.loadRecord(record);
    },
    initComponent: function() {
        var me = this;
        Ext.applyIf(me, {
            items: [me.getForm()]
        });
        me.callParent(arguments);
    }
});

Ext.define('MyApp.view.OrderInfoForm1',{
    extend: 'Ext.form.Panel',
    layout: 'column',
    defaults: {
        margin: '5 2 5 2',
        columnWidth: .333,
        labelWidth: 80,
        readOnly: true,
        xtype: 'textfield'
    },
    constructor : function(config) {
        var me = this, cfg = Ext.apply({}, config);
        me.items = [{
            name: 'orderId',
            fieldLabel: '订单号'
        },{
            name: 'userAccount',
            fieldLabel: '买家'
        },{
            name: 'servicerInfo',
            fieldLabel: '客服信息'
        },DataDictionary.getDataDictionaryCombo('tradeType',{
            fieldLabel: '交易方式',
            name: 'tradeType',
            readOnly: true,
            columnWidth: .333,
            labelWidth: 80
        }),{
            name: 'mobileNumber',
            fieldLabel: '联系方式'
        },{
            name: 'qq',
            fieldLabel: 'QQ'
        },{
            name: 'receiver',
            fieldLabel: '游戏角色'
        },{
            name: 'title',
            fieldLabel: '发布单名称'
        },{
            name: 'gameProp',
            columnWidth: .999,
            fieldLabel: '游戏属性'
        },{
            name: 'deliveryTime',
            fieldLabel: '发货速度'
        },{
            fieldLabel: '数量',
            name: 'goldCount'
        },{
            fieldLabel: '单价',
            name: 'unitPrice'
        },{
            fieldLabel: '价格',
            xtype: 'numberfield',
            decimalPrecision: 2,
            name: 'totalPrice'
        },DataDictionary.getDataDictionaryCombo('orderState',{
            fieldLabel: '订单状态',
            readOnly: true,
            name: 'orderState',
            columnWidth: .333,
            labelWidth: 80
        }),DataDictionary.getDataDictionaryCombo('check',{
            fieldLabel: '是否延迟',
            readOnly: true,
            name: 'isDelay',
            columnWidth: .333,
            labelWidth: 80
        }),DataDictionary.getDataDictionaryCombo('check',{
            fieldLabel: '是否有货',
            name: 'isHaveStore',
            readOnly: true,
            columnWidth: .333,
            labelWidth: 80
        }),{
            name: 'createTime',
            xtype: 'datefield',
            format:'Y-m-d H:i:s',
            fieldLabel: '创建时间'
        },{
            name: 'payTime',
            xtype: 'datefield',
            format:'Y-m-d H:i:s',
            fieldLabel: '付款时间'
        },{
            name: 'sendTime',
            xtype: 'datefield',
            format:'Y-m-d H:i:s',
            fieldLabel: '发货时间'
        },{
            name: 'endTime',
            xtype: 'datefield',
            format:'Y-m-d H:i:s',
            fieldLabel: '结束时间'
        },{
            fieldLabel: '备注',
            columnWidth: .999,
            name: 'notes'
        }];
        me.callParent([ cfg ]);
    }
});

Ext.define('MyApp.view.orderInfoWindow1', {
    extend: 'Ext.window.Window',
    width: 800,
    title: '订单信息',
    closeAction: 'hide',
    modal: true,
    form: null,
    getForm: function(){
        var me = this;
        if(me.form==null){
            me.form = Ext.create('MyApp.view.OrderInfoForm1');
        }
        return this.form;
    },
    isUpdate: null,
    bindData: function(record){
        var me = this,
            form = me.getForm().getForm(),
            servicerInfo = form.findField('servicerInfo'),
            gameProp = form.findField('gameProp');
        form.reset();
        form.loadRecord(record);
        servicerInfo.setValue(record.get('servicerInfo').realName);
        var prop = record.get('gameName')+'||'+record.get('region')+'||'+record.get('server');
        prop += Ext.isEmpty(record.get('gameRace'))?"":'||'+record.get('gameRace');
        gameProp.setValue(prop);
    },
    initComponent: function() {
        var me = this;
        Ext.applyIf(me, {
            items: [me.getForm()]
        });
        me.callParent(arguments);
    }
});

Ext.define('MyApp.view.orderConfigGrid', {
    extend: 'Ext.grid.Panel',
    header: false,
    columnLines: true,
    constructor : function(config) {
        var me = this, cfg = Ext.apply({}, config);
        var v1;

        me.features = [{
            ftype: 'groupingsummary',
            hideGroupedHeader:true
        }];
        me.columns = [{
            header: '订单号',
            dataIndex: 'orderId',
            width:60
        },{
            dataIndex: 'id',
            sortable: false,
            flex: 1,
            align: 'center',
            text: 'ID',
            renderer: function(value, metaData, record, rowIndex, colIndex, store, view){
                var isConsignment = record.get('isConsignment');
                if(Ext.isEmpty(isConsignment)) {
                    return value;
                } else if (isConsignment == true) {
                    // 寄售交易
                    return '<img src="images/common/consignment.png"/>' + value ;
                } else if (isConsignment == false) {
                    // 担保交易
                    return '<img src="images/common/assure.png"/>' + value;
                }
            }
        },{
            dataIndex: 'optionUser',
            sortable: false,
            flex: 1,
            align: 'center',
            renderer: function(value){
                return value.loginAccount + "<br/>" + value.nickName ;
            },
            text: '交易员'
        },{
            dataIndex: 'state',
            sortable: false,
            flex: 1,
            align: 'center',
            renderer: function(value){
                return DataDictionary.rendererSubmitToDisplay(value,'orderState');
            },
            text: '状态'
        },{
            dataIndex: 'loginAccount',
            sortable: false,
            flex: 1,
            align: 'center',
            text: '卖家',
            summaryRenderer: function(value, summaryData, dataIndex){ return '订单小计';}
        },{
            dataIndex: 'configGoldCount',
            sortable: false,
            flex: 0.7,
            align: 'center',
            text: '数目',
            summaryType: 'sum'
        },{
            dataIndex: 'repositoryUnitPrice',
            flex: 0.7,
            sortable: false,
            align: 'center',
            renderer: function(v, metaData, record, rowIndex, colIndex, store, view) {
                return Ext.util.Format.currency(v, '￥', 5);
            },
            summaryType: 'average',
            text: '库存单价'
        },{
            dataIndex: 'totalPrice',
            flex: 1,
            sortable: false,
            align: 'center',
            renderer: function(value, metaData, record, rowIndex, colIndex, store, view) {
                return Ext.util.Format.currency(value, '￥', 2);
            },
            summaryType: 'sum',
            text: '配置入库总额'
        },{
            dataIndex: 'income',
            flex: 1,
            sortable: false,
            align: 'center',
            renderer: function(value, metaData, record, rowIndex, colIndex, store, view) {
                if (value == 0) { // 兼容性代码，稳定后删除
                    var gameName = record.get('orderInfoEO').gameName;
                    var region = record.get('orderInfoEO').region;
                    var createTime = record.get('orderInfoEO').createTime;

                    if(gameName==="剑灵"){
                        v1=0.95;
                        if (region==="电信传说区") {
                            if (createTime>='2015-03-17T00:00:00') {
                                v1=0.95;
                            } else if(createTime<'2015-03-17T00:00:00') {
                                v1=1;
                            }
                        }
                    } else if (record.get('orderInfoEO').gameName==="疾风之刃") {
                        v1=1;
                        if (createTime >='2015-03-18T00:00:00') {
                            v1=0.94;
                        }
                    } else{
                        v1 = 1-SystemUtil.getSubCommissionBase();
                    }
                    var income = record.get('totalPrice')*v1;
                    return Ext.util.Format.currency(income, '￥', 2);
                }
                return Ext.util.Format.currency(value, '￥');
            },
            summaryRenderer: function(value, summaryData, dataIndex){
                if (value == 0) {// 兼容性代码，稳定后删除
                    var income = summaryData.record.data.totalPrice*v1;
                    return  Ext.util.Format.currency(income, '￥', 2);
                }
                return  Ext.util.Format.currency(value, '￥');
            },
            summaryType: 'sum',
            text: '卖家收益'
        },{
            dataIndex: 'commission',
            flex: 0.7,
            sortable: false,
            align: 'center',
            renderer: function(value, metaData, record, rowIndex, colIndex, store, view) {
                if (value == 0) {// 兼容性代码，稳定后删除
                    // 剑灵游戏佣金改成5%,电信传说区,电信傲雪区
                    return Ext.util.Format.currency(record.get('totalPrice')*(1-v1), '￥', 2);
                }
                return  Ext.util.Format.currency(value, '￥');
            },
            summaryRenderer: function(value, summaryData, dataIndex){
                if (value == 0) {// 兼容性代码，稳定后删除
                    var income = summaryData.record.data.totalPrice*(1-v1);
                    return  Ext.util.Format.currency(income, '￥', 2);
                }
                return  Ext.util.Format.currency(value, '￥');
            },
            summaryType: 'sum',
            text: '卖家佣金'
        },{
            dataIndex: 'orderUnitPrice',
            flex: 0.7,
            sortable: false,
            align: 'center',
            renderer: function(value, metaData, record, rowIndex, colIndex, store, view) {
                return Ext.util.Format.currency(value, '￥', 5);
            },
            summaryType: 'average',
            text: '订单单价'
        },{
            dataIndex: 'id',
            flex: 1,
            sortable: false,
            align: 'center',
            renderer: function(value, metaData, record, rowIndex, colIndex, store, view) {
                return Ext.util.Format.currency(record.get('orderUnitPrice')*record.get('configGoldCount'), '￥', 2);
            },
            summaryType: function(records){
                var length = records.length,
                    total = 0,
                    record;
                for (var i=0; i < length; ++i) {
                    record = records[i];
                    total = total + record.get('orderUnitPrice')*record.get('configGoldCount');
                }
                return total;
            },
            summaryType: 'sum',
            text: '配置出库总额'
        },{
            dataIndex: 'balance',
            flex: 1,
            sortable: false,
            align: 'center',
            renderer: function(value, metaData, record, rowIndex, colIndex, store, view) {
                var v = record.get('balance');
                if (v == 0) {
                    v = record.get('orderUnitPrice')*record.get('configGoldCount')-record.get('totalPrice');
                    v = v.toFixed(2);
                }
                return Ext.util.Format.currency(v, '￥');
            },
            summaryType: function(records){
                var length = records.length,
                    total = 0.00,
                    record;
                for (var i=0; i < length; ++i) {
                    record = records[i];
                    var balance = record.get('balance');
                    if (balance == 0) {
                        balance = record.get('orderUnitPrice')*record.get('configGoldCount')-record.get('totalPrice');
                    }
                    balance = Number(balance).toFixed(2);
                    total = parseFloat(total) + parseFloat(balance);
                }
                return total;
            },
            text: '差额收入'
        }];
        /*me.selModel = Ext.create('Ext.selection.CheckboxModel', {
         allowDeselect: true,
         mode: 'SINGLE'
         });*/
        me.callParent([ cfg ]);
    }
});

//订单库存配置信息
Ext.define('MyApp.order.OrderConfigPanel',{
    extend: 'Ext.panel.Panel',
    title: '订单库存配置信息',
    padding: '5 20 5 20',
    defaults: {
        padding: '5 5 5 5'
    },
    bindData: function(record, grid, rowBodyElement){
        var me = this,
            store = me.getStore();
        store.load({
            params: {
                'orderId': record.get('orderId')
            }
        });
    },
    store: null,
    getStore: function(){
        var me = this;
        if(me.store==null){
            me.store = Ext.create('MyApp.store.ConfigResultStore');
        }
        return me.store;
    },
    orderConfigGrid: null,
    getOrderConfigGrid: function(){
        var me = this;
        if(Ext.isEmpty(me.orderConfigGrid)){
            me.orderConfigGrid = Ext.create('MyApp.view.orderConfigGrid',{
                height: 200,
                store: me.getStore()
            });
        }
        return me.orderConfigGrid;
    },
    constructor: function(config){
        var me = this,
            cfg = Ext.apply({}, config);
        me.items = [me.getOrderConfigGrid()];
        me.callParent([cfg]);
    }
});

Ext.define('MyApp.view.insuranceFeeLogManager', {
    extend: 'Ext.panel.Panel',
    id: 'insuranceFeeLogManager',
    closable: true,
    title: '保费统计查询',
    toolbar: null,
    getToolbar: function(){
        var me = this;
        if(Ext.isEmpty(me.toolbar)){
            me.toolbar = Ext.widget('toolbar',{
                dock: 'top',
                items: [{
                    text: '订单详情',
                    listeners: {
                        click: {
                            fn: me.orderInfo,
                            scope: me
                        }
                    }
                },{
                    text: '导出',
                    listeners: {
                        click: {
                            fn: me.exportOrder,
                            scope: me
                        }
                    }
                }]
            });
        }
        return me.toolbar;
    },
    payInfobar: null,
    getPayInfobar: function(){
        var me = this;
        if(Ext.isEmpty(me.payInfobar)){
            me.payInfobar = Ext.widget('toolbar',{
                dock: 'top',
                items: [{xtype: 'label', text: '支付状态：'},
                    {xtype: 'image', height:10, src: 'images/common/p_no.png'},
                    {xtype: 'label', text: '未支付'},
                    {xtype: 'image', height:10, src: 'images/common/p_yes.png'},
                    {xtype: 'label', text: '已支付'},
                    {xtype: 'image', height:10, src: 'images/common/p_back.png'},
                    {xtype: 'label', text: '已退款'},
                    '->',
                    {xtype: 'label', text: '订单状态：'},
                    {xtype: 'image', height:10, src: 'images/common/t_ing.png'},
                    {xtype: 'label', text: '交易中'},
                    {xtype: 'image', height:10, src: 'images/common/s_ing.png'},
                    {xtype: 'label', text: '待发货'},
                    {xtype: 'image', height:10, src: 'images/common/s_yes.png'},
                    {xtype: 'label', text: '已发货'},
                    {xtype: 'image', height:10, src: 'images/common/t_yes.png'},
                    {xtype: 'label', text: '交易成功'},
                    {xtype: 'image', height:10, src: 'images/common/close.png'},
                    {xtype: 'label', text: '已取消'},
                    {xtype:	'image', height:10, src: 'images/common/p_delay.png'},
                    {xtype:	'label', text:	'延时打款'}
                ]
            });
        }
        return me.payInfobar;
    },
    detailOrderWindow: null,
    getDetailOrderWindow: function(){
        var me = this;
        if(me.detailOrderWindow==null){
            me.detailOrderWindow = Ext.create('MyApp.view.DetailOrderWindow');
        }
        return me.detailOrderWindow;
    },
    changeStateWindow: null,
    getChangeStateWindow: function(){
        var me = this;
        if(me.changeStateWindow==null){
            me.changeStateWindow = Ext.create('MyApp.view.changeStateWindow');
        }
        return me.changeStateWindow;
    },
    orderInfoWindow: null,
    getOrderInfoWindow: function(){
        var me = this;
        if(me.orderInfoWindow==null){
            me.orderInfoWindow = Ext.create('MyApp.view.orderInfoWindow1');
        }
        return me.orderInfoWindow;
    },
    // 修改订单状态
    changeState: function(button, e, eOpts){
        var grid = this.getOrderGrid(),
            selModel = grid.getSelectionModel(),
            selRecords = selModel.getSelection(),order,
            window = this.getChangeStateWindow();
        if(selRecords == null||selRecords.length<=0){
            Ext.ux.Toast.msg("温馨提示", "请先选择要修改的订单");
            return;
        }
        order = selRecords[0];
        /*if(order.get('orderState')>2){
         Ext.ux.Toast.msg("温馨提示", "请选择待付款与已付款状态的订单进行修改");
         return;
         }*/
        window.show();
        window.bindData(order);
    },
    // 处理订单
    detailOrder: function(button, e, eOpts) {
        var grid = this.getOrderGrid(),
            selModel = grid.getSelectionModel(),
            selRecords = selModel.getSelection(),order,
            window = this.getDetailOrderWindow();
        if(selRecords == null||selRecords.length<=0){
            Ext.ux.Toast.msg("温馨提示", "请先选择要处理的订单");
            return;
        }
        record = selRecords[0];
        if(record.get('orderState')!=2){
            Ext.ux.Toast.msg("温馨提示", "请选择已付款状态的订单进行处理");
            return;
        }
        if(record.get('manualOperation')!=true){
            Ext.ux.Toast.msg("温馨提示", "该订单为自动配置的订单，不能人工操作");
            return;
        }
        window.show();
        window.bindData(record);
    },
    // 订单详情
    orderInfo: function(button, e, eOpts) {
        var grid = this.getOrderGrid(),
            selModel = grid.getSelectionModel(),
            selRecords = selModel.getSelection(),order,
            window = this.getOrderInfoWindow();
        if(selRecords == null||selRecords.length<=0){
            Ext.ux.Toast.msg("温馨提示", "请先选择订单");
            return;
        }
        order = selRecords[0];
        window.show();
        window.bindData(order);
    },
    /**
     * 导出订单
     */
    exportOrder: function(button, e, eOpts) {
        var me = this,url,
        queryForm = me.getQueryForm(),
            values = queryForm.getValues();
        url = './order/exportInsuranceOrder.action?' + Ext.urlEncode(values);
        window.open(url);
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
        return this.pagingToolbar;
    },
    orderGrid: null,
    getOrderGrid: function(){
        var me = this;
        if(Ext.isEmpty(me.orderGrid)){
            me.orderGrid = Ext.widget('gridpanel',{
                header: false,
                region:'center',
                columnLines: true,
                store: me.getStore(),
                columns: [{
                    xtype: 'rownumberer'
                },{
                    sortable: false,
                    dataIndex: 'orderState',
                    width:60,
                    text: '状态',
                    renderer: function(value, metaData, record, rowIndex, colIndex, store, view) {
                        var content;
                        switch(value){
                            case 1:
                                //待付款===未支付/交易中
                                content='<div class="container"><div class="leftDiv icons_p_no"></div><div class="rightDiv icons_t_ing"></div>';
                                break;
                            case 2:
                                //已付款===已支付/交易中
                                content='<div class="container"><div class="leftDiv icons_p_yes"></div><div class="rightDiv icons_t_ing"></div>';
                                break;
                            case 3:
                                //待发货===已支付/待发货
                                content='<div class="container"><div class="leftDiv icons_p_yes"></div><div class="rightDiv icons_s_ing"></div>';
                                break;
                            case 4:
                                //已发货===已支付/已发货
                                content='<div class="container"><div class="leftDiv icons_p_yes"></div><div class="rightDiv icons_s_yes"></div>';
                                break;
                            case 5:
                                //结单===已支付/交易成功
                                content='<div class="container"><div class="leftDiv icons_p_yes"></div><div class="rightDiv icons_t_yes"></div>';
                                break;
                            case 6:
                                //已退款===已退款/已取消
                                content='<div class="container"><div class="leftDiv icons_p_back"></div><div class="rightDiv icons_close"></div>';
                                break;
                            case 7:
                                //已取消===未支付/已取消
                                content='<div class="container"><div class="leftDiv icons_p_no"></div><div class="rightDiv icons_close"></div>';
                                break;
                            default:
                                content='';
                        }
                        var hutime = new Date()-record.get('payTime')
                        if(record.get('gameName')=="魔兽世界(国服)"&&record.get('orderState')== 4 && hutime<48*3600*1000){
                            content+='<div class="centerDiv icons_p_delay"></div></div>';

                        }else{
                            content+='</div>';
                        }
                        return content;
                    }
                },{
                    dataIndex: 'orderId',
                    xtype: 'ellipsiscolumn',
                    flex: 1.5,
                    sortable: false,
                    align: 'center',
                    text: '订单号'
                },{
                    dataIndex: 'userAccount',
                    flex: 1,
                    sortable: false,
                    align: 'center',
                    text: '买家'
                },{
                    dataIndex: 'qq',
                    flex: 1,
                    sortable: false,
                    align: 'center',
                    text: 'QQ'
                },{
                    dataIndex: 'title',
                    text: '发布单<br/>名称',
                    sortable: false,
                    align: 'center',
                    flex: 1
                },{
                    dataIndex: 'goldCount',
                    flex: 0.7,
                    sortable: false,
                    align: 'center',
                    text: '数量'
                },{
                    dataIndex: 'totalPrice',
                    flex: 0.7,
                    sortable: false,
                    renderer: function(v) {
                        return Ext.util.Format.currency(v, '￥', 2);
                    },
                    align: 'center',
                    text: '价格'
                },{
                    dataIndex: 'insuranceAmount',
                    flex: 0.7,
                    sortable: false,
                    renderer: function(v) {
                        if (Ext.isEmpty(v))
                            return "";
                        return Ext.util.Format.currency(v, '￥');
                    },
                    align: 'center',
                    text: '保费'
                },{
                    dataIndex: 'insuranceRate',
                    flex: 0.7,
                    align: 'center',
                    sortable: false,
                    text: '保险费率'
                },{
                    dataIndex: 'insuranceExpireTime',
                    xtype: 'datecolumn',
                    format:'Y-m-d H:i:s',
                    flex: 1.5,
                    align: 'center',
                    sortable: false,
                    text: '保险有效期'
                },{
                    xtype: 'datecolumn',
                    format:'Y-m-d H:i:s',
                    dataIndex: 'createTime',
                    sortable: false,
                    flex: 1.5,
                    align: 'center',
                    text: '创建时间 '
                },{
                    dataIndex: 'sendTime',
                    xtype: 'datecolumn',
                    format:'Y-m-d H:i:s',
                    flex: 1.5,
                    align: 'center',
                    sortable: false,
                    text: '发货时间'
                },{
                    dataIndex: 'endTime',
                    xtype: 'datecolumn',
                    format:'Y-m-d H:i:s',
                    flex: 1.5,
                    align: 'center',
                    sortable: false,
                    text: '完成时间'
                }
                ],
                listeners: {
                    itemdblclick: function(view, record, item, index, e, eOpts ){
                        var window = me.getDetailOrderWindow();
                        if(record.get('orderState')!=2){
                            Ext.ux.Toast.msg("温馨提示", "请选择已付款状态的订单进行处理");
                            return;
                        }
                        if(record.get('manualOperation')!=true){
                            Ext.ux.Toast.msg("温馨提示", "该订单为自动配置的订单，不能人工操作");
                            return;
                        }
                        window.bindData(record);
                        window.show();
                    }
                },
                plugins: [{
                    ptype: 'gamegoldrowexpander',
                    header: true,
                    rowsExpander: false,
                    expandOnDblClick: false,
                    rowBodyElement : 'MyApp.order.OrderConfigPanel'
                }],
                dockedItems: [me.getToolbar(),me.getPagingToolbar()],
                selModel: Ext.create('Ext.selection.CheckboxModel', {
                    allowDeselect: true,
                    mode: 'SINGLE'
                })
            });
        }
        return me.orderGrid;
    },
    store: null,
    getStore: function(){
        var me = this;
        if(me.store==null){
            me.store = Ext.create('MyApp.store.BuyInsuranceOrderStore',{
                autoLoad: true,
                listeners: {
                    beforeload : function(store, operation, eOpts) {
                        var queryForm = me.getQueryForm(),
                            values = queryForm.getValues(),params={};
                        if (queryForm != null) {
                            params = {
                                'createStartTime': values.createStartTime,
                                'createEndTime': values.createEndTime,
                                'buyerAccount': Ext.String.trim(values.buyerAccount),
                                'orderState': values.orderState,
                                'gameName': Ext.String.trim(values.gameName),
                                'orderId': Ext.String.trim(values.orderId),
                                'goodsTypeName':'全部',
                                'buyerQq': Ext.String.trim(values.buyerQq)
                            };

                            Ext.apply(operation, {
                                params : params
                            });
                        }
                    }
                }
            });
        }
        return me.store;
    },
    queryForm: null,
    getQueryForm: function(){
        var me = this;
        if(me.queryForm==null){
            me.queryForm = Ext.widget('form',{
                width: '99%',
                border: false,
                layout: 'column',
                defaults: {
                    margin: '5 5 5 5',
                    columnWidth: .25,
                    labelWidth: 100,
                    xtype: 'textfield'
                },
                items: [{
                    fieldLabel: '下单日期',
                    columnWidth: .5,
                    xtype: 'rangedatefield',
                    //起始日期组件的name属性。
                    fromName: 'createStartTime',
                    //终止日期组件的name属性。
                    toName: 'createEndTime',
                    //初始化查询开始时间
                    fromValue: new Date(),
                    //初始化查询结束时间
                    toValue: new Date()
                },{
                    name: 'buyerAccount',
                    fieldLabel: '买家5173账号'
                },DataDictionary.getDataDictionaryCombo('orderState2',{
                    fieldLabel: '订单状态',
                    labelWidth: 100,
                    name: 'orderState',
                    value: 5,
                    editable: false
                },{value: null,display:'全部'}),DataDictionary.getDataDictionaryCombo('SgameName',{
                    fieldLabel: '游戏名称',
                    name: 'gameName',
                    labelWidth: 100,
                    editable: false
                },{value: null,display:'全部'}),{
                    name: 'orderId',
                    fieldLabel: '订单号'
                },{
                    name: 'buyerQq',
                    fieldLabel: '买家QQ'
                },{
                    xtype: 'container',
                    layout: 'column',
                    columnWidth:1,
                    items:[{
                        xtype: 'checkbox',
                        id: 'isStatisticPremiums',
                        name: 'isStatisticPremiums',
                        boxLabel: '是否统计保费',
                        columnWidth:.1
                    },{
                        xtype: 'tbtext',
                        id: 'totalPreminms', //保费总金额
                        name: 'totalPreminms',
                        text: '',
                        columnWidth:.2,
                        style: {
                            padding: "5px",
                            fontWeight: 'bold',
                            color: 'red'
                        }
                    }]
                }],
                buttons: [{
                    text:'重置',
                    handler: function() {
                        me.getQueryForm().getForm().reset();
                    }
                },'->',{
                    text:'查询',
                    handler: function() {
                        var form = me.getQueryForm().getForm();
                        var isStatisticPremiums = form.findField('isStatisticPremiums').getValue();
                        // 是否统计保费金额
                        if (isStatisticPremiums) {
                            var values = me.getQueryForm().getValues();
                            var params = {
                                'createStartTime': values.createStartTime,
                                'createEndTime': values.createEndTime,
                                'buyerAccount': Ext.String.trim(values.buyerAccount),
                                'orderState': values.orderState,
                                'gameName': Ext.String.trim(values.gameName),
                                'orderId': Ext.String.trim(values.orderId),
                                'goodsTypeName':'全部',
                                'buyerQq': Ext.String.trim(values.buyerQq)
                            };
                            Ext.Ajax.request({
                                url : './order/statisticPremiums.action',
                                method: 'POST',
                                params: params,
                                success : function(response, opts) {
                                    var json = Ext.decode(response.responseText);
                                    var totalPreminums = Ext.util.Format.currency(json.totalPreminums, "￥");
                                    Ext.getCmp('totalPreminms').setText("保费总金额：" + totalPreminums);
                                },
                                exception : function(response, opts) {
                                    var json = Ext.decode(response.responseText);
                                    Ext.ux.Toast.msg("温馨提示", json.message);
                                }
                            });
                        }
                        me.getPagingToolbar().moveFirst();
                    }
                }]
            });
        }
        return this.queryForm;
    },

    splitPanel:null,
    getSplitPanel:function(){
        var me = this;
        if(Ext.isEmpty(me.splitPanel)){
            me.splitPanel = Ext.create('Ext.panel.Panel', {
                region: 'north',
                split:true,
                collapsible: true,
                collapseDirection : 'bottom',
                //border:false,
                header:false,
                collapseMode:'mini',
                items:[me.getQueryForm(), me.getPayInfobar()]
            });
        }
        return me.splitPanel;
    },

    initComponent: function() {
        var me = this;
        Ext.applyIf(me, {
            intervalID: null,
            layout: 'border',
            header:false,
            listeners:{
                //渲染结束定时进行订单的刷新。
                boxready : function(){
                    /*me.intervalID = setInterval(
                        function(){
                            me.getStore().load();
                        },SystemUtil.getOrderAutoLoadTime()
                    );*/
                },
                destroy: function(){
                    clearInterval(me.intervalID);
                },
                'resize': function () {
                    me.orderGrid.setHeight(window.document.body.offsetHeight - 288);
                }
            },
            items: [me.getSplitPanel(),me.getOrderGrid()]
            //items: [me.getSplitPanel()]
        });
        me.callParent(arguments);
    }
});