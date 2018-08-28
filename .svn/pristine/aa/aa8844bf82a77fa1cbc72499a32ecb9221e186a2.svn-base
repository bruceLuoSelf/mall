Ext.define('MyApp.view.ApperalDeliveryOrderFinishForm', {
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
    constructor: function (config) {
        var me = this, cfg = Ext.apply({}, config);
        me.items = [{
            name: 'gameName',
            labelWidth: 100,
            fieldLabel: '游戏'
        }, {
            name: 'region',
            labelWidth: 100,
            fieldLabel: '游戏区'
        }, {
            name: 'server',
            labelWidth: 100,
            fieldLabel: '游戏服'
        }, {
            name: 'orderId',
            labelWidth: 100,
            fieldLabel: '订单号'
        }, {
            name: 'relationOrderId',
            labelWidth: 100,
            fieldLabel: '原始订单号',
        }, {
            name: 'sellerAccount',
            labelWidth: 100,
            fieldLabel: '出货方账号'
        }, {
            name: 'buyerAccount',
            labelWidth: 100,
            fieldLabel: '收货方账号'
        }, {
            name: 'roleName',
            labelWidth: 100,
            fieldLabel: '出货角色'
        }, {
            name: 'price',
            labelWidth: 100,
            fieldLabel: '单价'
        }, {
            name: 'count',
            labelWidth: 100,
            fieldLabel: '计划出货数量'
        }, {
            name: 'amount',
            labelWidth: 100,
            fieldLabel: '计划出货金额'
        }, {
            name: 'realCount',
            labelWidth: 100,
            fieldLabel: '实际出货数量'
        }, {
            name: 'realAmount',
            labelWidth: 100,
            fieldLabel: '实际出货金额'
        }, {
            name: 'address',
            labelWidth: 100,
            fieldLabel: '交易地点'
        }, {
            name: 'phone',
            labelWidth: 100,
            fieldLabel: '联系电话'
        }, {
            name: 'qq',
            labelWidth: 100,
            fieldLabel: 'qq'
        }, DataDictionary.getDataDictionaryCombo('shOrderType', {
            fieldLabel: '订单状态',
            readOnly: true,
            labelWidth: 100,
            name: 'status'
        }), DataDictionary.getDataDictionaryCombo('transferStatus', {
            fieldLabel: '转账状态',
            readOnly: true,
            labelWidth: 100,
            name: 'transferStatus'
        }), {
            name: 'createTime',
            xtype: 'datefield',
            format: 'Y-m-d H:i:s',
            labelWidth: 100,
            fieldLabel: '创建时间'
        }, {
            name: 'tradeStartTime',
            xtype: 'datefield',
            format: 'Y-m-d H:i:s',
            labelWidth: 100,
            fieldLabel: '开始交易时间'
        }, {
            name: 'tradeEndTime',
            xtype: 'datefield',
            format: 'Y-m-d H:i:s',
            labelWidth: 100,
            fieldLabel: '交易完成时间'
        }, {
            xtype: 'textarea',
            name: 'appealReason',
            columnWidth: 1,
            height: 100,
            fieldLabel: '申诉原因'
        }, {
            xtype: 'textarea',
            id: 'otherReason',
            name: 'otherReason',
            columnWidth: 1,
            height: 100,
            fieldLabel: '申诉回复'
        }];
        me.callParent([cfg]);
    }
});

//人工完单
Ext.define('MyApp.view.ApperalDeliveryOrderFinishWindow', {
    extend: 'Ext.window.Window',
    width: 1000,
    autoScroll: true,
    maxHeight: 800,
    title: '处理订单',
    closeAction: 'hide',
    modal: true,
    defaults: {
        margin: '5 5 5 5'
    },
    form: null,
    getForm: function () {
        var me = this;
        if (me.form == null) {
            me.form = Ext.create('MyApp.view.ApperalDeliveryOrderFinishForm');
        }
        return me.form;
    },
    panel: null,
    getPanel: function () {
        var me = this;
        if (me.panel == null) {
            me.panel = Ext.create('MyApp.deliveryOrder.ApperalDeliverySubOrderPanel');
        }
        return me.panel;
    },
    bindData: function (record) {
        var me = this,
            form = me.getForm().getForm(),
            panel = me.getPanel();
        form.reset();
        form.loadRecord(record);
        panel.loadData(record);

        var otherReason = form.findField('otherReason');
        otherReason.setReadOnly(false);
        Ext.getCmp('otherReason').setFieldLabel("<font style=\"color:red;margin-left:10px;font-weight:bold;\">申诉回复</font>")
    },
    initComponent: function () {
        var me = this;
        Ext.applyIf(me, {
            items: [me.getForm(), me.getPanel()]
        });
        me.callParent(arguments);
    }
});

//订单处理页面
Ext.define('MyApp.deliveryOrder.ApperalDeliverySubOrderPanel', {
    extend: 'Ext.panel.Panel',
    title: '订单配置',
    defaults: {
        margin: '2 2 2 2'
    },
    orderRecord: null,
    loadData: function (orderRecord) {
        var me = this,
            repositoryGrid = me.getRepositoryGrid(),
            selectModel = repositoryGrid.getSelectionModel(),
            store = repositoryGrid.getStore();
        me.orderRecord = orderRecord;
        repositoryGrid.deliverySubOrderList.clear(); // 清除保存的配单数据
        repositoryGrid.queryParam = {
            'deliverySubOrder.chId': orderRecord.get('id')
        };

        store.loadPage(1);
    },
    repositoryGrid: null,
    getRepositoryGrid: function () {
        if (this.repositoryGrid == null) {
            this.repositoryGrid = Ext.create('MyApp.order.ApperalDeliverySubOrderGrid', {
                autoScroll: true,
                height: 213
            });
        }
        return this.repositoryGrid;
    },
    constructor: function (config) {
        var me = this,
            cfg = Ext.apply({}, config);
        me.items = [me.getRepositoryGrid()];
        me.buttons = [{
            text: '保存',
            plugins: {
                ptype: 'buttondisabledplugin',
                seconds: 3
            },
            handler: function () {
                var grid = me.getRepositoryGrid();
                configInfoList = [];
                var list = grid.deliverySubOrderList;
                var mainOrderId = me.orderRecord.data["orderId"];
                if (list == null || list.length <= 0) {
                    Ext.ux.Toast.msg("温馨提示", "请先配置实际申诉数量");
                    return;
                }

                var realCount = 0;
                var record = list.getAt(0);
                if (list.getCount() > 0) {
                    realCount = record.configCount;
                }
                var state = record.status;
                if (state == null || state != 3) {
                    Ext.ux.Toast.msg("温馨提示", "只有交易中的订单才可以进行申诉！");
                    return;
                }
                // for (var i = 0; i < list.getCount(); i++) {
                //     var record = list.getAt(i);
                //     // if(record.configCount%100!=0){
                //     //     Ext.ux.Toast.msg("温馨提示", "收货数量必须是1000的倍数");
                //     //     return;
                //     // }
                //     configInfoList.push({
                //         'id': record.id,
                //         'realCount': record.configCount
                //     });
                //     console.log(record.configCount);
                // }
                // ;
                Ext.Ajax.request({
                    url: './appealDelivery/updateAppealDeliveryOrder.action',
                    method: 'POST',
                    jsonData: {
                        'realCount': realCount,
                        'orderId': mainOrderId,
                        'otherReason': Ext.getCmp('otherReason').getValue()
                    },
                    success: function (response, opts) {
                        var deliveryOrderManager = Ext.getCmp('ApperalDeliveryOrderManager'),
                            window = me.up('window'),
                            store = deliveryOrderManager.getStore();
                        Ext.ux.Toast.msg("温馨提示", "订单配置成功");
                        window.close();
                        store.load();
                    },
                    exception: function (response, opts) {
                        var json = Ext.decode(response.responseText);
                        Ext.ux.Toast.msg("温馨提示", json.message);
                    }
                });

            }
        }];
        me.callParent([cfg]);
    }
});

//库存查询结果表格页面
Ext.define('MyApp.order.ApperalDeliverySubOrderGrid', {
    extend: 'Ext.grid.Panel',
    store: null,
    bbar: null,
    editer: null,
    deliverySubOrderList: new Ext.util.MixedCollection(),//存放配置的库存记录
    getEditer: function () {
        if (this.editer == null) {
            this.editer = Ext.create('Ext.grid.plugin.CellEditing', {
                //设置鼠标点击多少次，触发编辑
                clicksToEdit: 1
            });
        }
        return this.editer;
    },
    changeEvent: function (field, newValue, oldValue, eOpts) {
        if (!Ext.isEmpty(field.getErrors())) {
            Ext.ux.Toast.msg("温馨提示", field.getErrors()[0]);
            return false;
        } else {
            return true;
        }
    },
    queryParam: null,
    constructor: function (config) {
        var me = this,
            cfg = Ext.apply({}, config);
        me.columns = [{
            dataIndex: 'id',
            align: 'center',
            flex: 0.5,
            renderer: function (v, metaData, record, rowIndex, colIndex, store, view) {
                return '<input type="checkbox" name="rowCheckbox" id="repository_' + v + '" value="' + v + '"/>';
            }
        }, {
            dataIndex: 'gameAccount',
            align: 'center',
            flex: 1,
            text: '游戏账号'
        }, {
            dataIndex: 'gameRole',
            align: 'center',
            flex: 1,
            text: '角色'
        }, {
            dataIndex: 'count',
            align: 'center',
            flex: 1,
            text: '计划收货数量'
        }, {
            dataIndex: 'realCount',
            align: 'center',
            flex: 1,
            text: '实际收货数量'
        }, {
            header: '配置实际收货',
            dataIndex: 'configCount',
            sortable: false,
            flex: 1,
            editor: {
                xtype: 'numberfield',
                name: 'discount',
                minValue: 0,
                listeners: {
                    change: function (field, newValue, oldValue, eOpts) {
                        var dataModel = me.getSelectionModel().getLastSelected();
                        var id = dataModel.get("id");
                        newValue = newValue < 0 ? 0 : newValue;

                        if (dataModel.get("count") < newValue) {
                            newValue = dataModel.get("count");
                            this.setValue(newValue);
                            Ext.ux.Toast.msg("温馨提示", "该笔计划收货数量只有" + dataModel.get("count"));
                        }

                        // 将记录放入deliverySubOrderList
                        dataModel.set("configCount", newValue);
                        me.deliverySubOrderList.add(id, dataModel.data);
                        // 选中这行的checkbox
                        Ext.getDom("repository_" + id).checked = true;

                        me.changeEvent(field, newValue, oldValue, eOpts);
                    }
                }
            }
        }];
        me.store = Ext.create('MyApp.store.DeliverySubOrderStore', {
            pageSize: 25,
            listeners: {
                beforeload: function (store, operation, eOpts) {
                    Ext.apply(operation, {
                        params: me.queryParam
                    });
                },
                load: function (store, records, successful, eOpts) {
                    // 选中之前已经选择的记录，并将配单数量设置上
                    var total = store.getCount();
                    for (var i = 0; i < total; i++) {
                        var rec = store.getAt(i);
                        var configCount = 0;
                        if (me.deliverySubOrderList.containsKey(rec.get("id"))) {
                            configCount = me.deliverySubOrderList.get(rec.get("id")).configCount;
                        }
                        // 将记录放入deliverySubOrderList
                        rec.set("configCount", configCount);
                        me.deliverySubOrderList.add(rec.get("id"), rec.data);
                        // 选中这行的checkbox
                        Ext.getDom("repository_" + rec.get("id")).checked = true;
                    }
                }
            }
        });
        me.bbar = Ext.create('Ext.PagingToolbar', {
            store: me.store
        });
        me.plugins = [me.getEditer()];
        me.callParent([cfg]);
    }
});

//订单明细内容
Ext.define('MyApp.view.ApperalDeliveryOrderDetailForm', {
    extend: 'Ext.form.Panel',
    layout: 'column',
    border: false,
    defaults: {
        margin: '5 5 5 5',
        columnWidth: .333,
        labelWidth: 100,
        readOnly: true,
        xtype: 'textfield'
    },
    constructor: function (config) {
        var me = this, cfg = Ext.apply({}, config);
        me.items = [{
            name: 'gameName',
            fieldLabel: '游戏'
        }, {
            name: 'region',
            fieldLabel: '游戏区'
        }, {
            name: 'server',
            fieldLabel: '游戏服'
        }, {
            name: 'orderId',
            fieldLabel: '订单号'
        }, {
            name: 'relationOrderId',
            fieldLabel: '原始订单号'
        }, {
            name: 'sellerAccount',
            fieldLabel: '出货方账号'
        }, {
            name: 'buyerAccount',
            fieldLabel: '收货方账号'
        }, {
            name: 'roleName',
            fieldLabel: '出货角色'
        }, {
            name: 'price',
            fieldLabel: '单价'
        }, {
            name: 'count',
            fieldLabel: '计划出货数量'
        }, {
            name: 'amount',
            fieldLabel: '计划出货金额'
        }, {
            name: 'realCount',
            fieldLabel: '实际出货数量'
        }, {
            name: 'realAmount',
            fieldLabel: '实际出货金额'
        }, {
            name: 'address',
            fieldLabel: '交易地点'
        }, {
            name: 'phone',
            fieldLabel: '联系电话'
        }, {
            name: 'qq',
            fieldLabel: 'qq'
        }, DataDictionary.getDataDictionaryCombo('transferStatus', {
            fieldLabel: '转账状态',
            readOnly: true,
            name: 'transferStatus'
        }), DataDictionary.getDataDictionaryCombo('shOrderType', {
            fieldLabel: '订单状态',
            readOnly: true,
            name: 'status'
        }), {
            name: 'tradeTypeName',
            fieldLabel: '交易方式'
        }, {
            name: 'createTime',
            xtype: 'datefield',
            format: 'Y-m-d H:i:s',
            fieldLabel: '创建时间'
        }, {
            name: 'tradeStartTime',
            xtype: 'datefield',
            format: 'Y-m-d H:i:s',
            fieldLabel: '开始交易时间'
        }, DataDictionary.getDataDictionaryCombo('theDeliveryType', {
            fieldLabel: '收货方式',
            readOnly: true,
            name: 'deliveryType'
        }), {
            name: 'tradeEndTime',
            xtype: 'datefield',
            format: 'Y-m-d H:i:s',
            fieldLabel: '交易完成时间'
        }, {
            name: 'machineArtificialReason',
            fieldLabel: '机器转人工原因'
        }, {
            name: 'machineArtificialTime',
            xtype: 'datefield',
            format: 'Y-m-d H:i:s',
            fieldLabel: '机器转人工时间'
        }, {
            name: 'takeOverSubjectId',
            fieldLabel: '接手物服'
        }, DataDictionary.getDataDictionaryCombo('reasonType', {
            fieldLabel: '撤单原因',
            columnWidth: 1,
            readOnly: true,
            name: 'reason'
        }, {
            value: 0, display: ''
        }), {
            name: 'otherReason',
            columnWidth: 1,
            fieldLabel: '其他原因'
        }];
        me.callParent([cfg]);
    }
});

//订单明细
Ext.define('MyApp.view.ApperalDeliveryOrderDetailWindow', {
    extend: 'Ext.window.Window',
    title: '申诉订单明细',
    width: 1500,
    border: false,
    padding: '10 10 10 10',
    autoScroll: true,
    closeAction: 'hide',
    modal: true,
    form: null,
    getForm: function () {
        var me = this;
        if (me.form == null) {
            me.form = Ext.create('MyApp.view.ApperalDeliveryOrderDetailForm');
        }
        return me.form;
    },
    grid: null,
    getGrid: function () {
        var me = this;
        if (Ext.isEmpty(me.grid)) {
            var store = Ext.create('Ext.data.Store', {
                model: 'MyApp.model.DeliverySubOrderModel',
                proxy: {
                    type: 'ajax',
                    actionMethods: 'POST',
                    url: './delivery/queryDeliverySubOrder.action',
                    reader: {
                        type: 'json',
                        root: 'deliverySubOrderList'
                    }
                }
            });
            me.grid = Ext.widget('gridpanel', {
                header: false,
                columnLines: true,
                store: store,
                columns: [{
                    dataIndex: 'id',
                    text: '子订单id',
                    sortable: false,
                    align: 'center',
                    flex: 1
                },{
                    dataIndex: 'gameAccount',
                    text: '游戏帐号',
                    sortable: false,
                    align: 'center',
                    flex: 1
                }, {
                    dataIndex: 'gameRole',
                    text: '收货角色',
                    sortable: false,
                    align: 'center',
                    flex: 1
                }, {
                    dataIndex: 'count',
                    text: '计划收货数量',
                    sortable: false,
                    align: 'center',
                    flex: 1
                }, {
                    dataIndex: 'realCount',
                    text: '实际收货数量',
                    sortable: false,
                    align: 'center',
                    flex: 1
                }, {
                    dataIndex: 'status',
                    text: '订单状态 ',
                    flex: 1,
                    sortable: false,
                    align: 'center',
                    renderer: function (value, metaData, record, rowIndex, colIndex, store, view) {
                        if (value != 0) {
                            return DataDictionary.rendererSubmitToDisplay(value, 'shOrderType');
                        } else {
                            return '';
                        }
                    }
                },
                    {
                        dataIndex: 'isTimeout',
                        text: '是否超时 ',
                        flex: 1,
                        sortable: false,
                        align: 'center',
                        renderer: function (value, metaData, record, rowIndex, colIndex, store, view) {
                            if (value != null) {
                                return DataDictionary.rendererSubmitToDisplay(value, 'timeOutSub');
                            } else {
                                return '';
                            }
                        }
                    }, {
                        xtype: 'datecolumn',
                        format: 'Y-m-d H:i:s',
                        dataIndex: 'createTime',
                        sortable: false,
                        flex: 1.5,
                        align: 'center',
                        text: '创建时间 '
                    },
                    {
                        xtype: 'datecolumn',
                        format: 'Y-m-d H:i:s',
                        dataIndex: 'updateTime',
                        sortable: false,
                        flex: 1.5,
                        align: 'center',
                        text: '更新时间'
                    }, {
                        dataIndex: 'reason',
                        text: '撤单原因 ',
                        flex: 1,
                        sortable: false,
                        align: 'center',
                        renderer: function (value, metaData, record, rowIndex, colIndex, store, view) {
                            var reasonValue = DataDictionary.rendererSubmitToDisplay(value, 'reasonType');
                            metaData.tdAttr = " title=" + reasonValue;
                            if (value != 0) {
                                return reasonValue;
                            } else {
                                return '';
                            }
                        }
                    }, {
                        dataIndex: 'otherReason',
                        text: '其他原因',
                        sortable: false,
                        align: 'center',
                        flex: 1,
                        renderer: function (value, metaData, record, rowIndex, colIndex, store, view) {
                            metaData.tdAttr = " title=" + value;
                            return value;
                        }
                    }]
            });
        }
        return me.grid;
    },
    bindData: function (record) {
        var me = this,
            store = me.getGrid().getStore(),
            form = me.getForm().getForm();
        form.reset();
        form.loadRecord(record);
        store.load({
            params: {
                'deliverySubOrder.chId': record.get('id')
            }
        });
    },
    initComponent: function () {
        var me = this;
        Ext.applyIf(me, {
            items: [me.getForm(), me.getGrid()]
        });
        me.callParent(arguments);
    }
});

Ext.define('MyApp.view.ApperalOrderLogWindow', {
    extend: 'Ext.window.Window',
    title: '原始收货订单日志',
    width: 800,
    border: false,
    padding: '10 10 10 10',
    autoScroll: true,
    closeAction: 'hide',
    modal: true,
    layout: 'fit',
    grid: null,
    getGrid: function () {
        var me = this;
        if (Ext.isEmpty(me.grid)) {
            var store = Ext.create('Ext.data.Store', {
                model: 'MyApp.model.ShOrderLogModel',
                proxy: {
                    type: 'ajax',
                    actionMethods: 'POST',
                    url: './delivery/queryOrderLogByOrderId.action',
                    reader: {
                        type: 'json',
                        root: 'orderLogList'
                    }
                }
            });
            me.grid = Ext.widget('gridpanel', {
                header: false,
                columnLines: true,
                store: store,
                autoScroll: true,
                maxHeight: 500,
                columns: [{
                    dataIndex: 'log',
                    text: '日志',
                    sortable: false,
                    align: 'left',
                    flex: 3
                }, {
                    xtype: 'datecolumn',
                    dataIndex: 'createTime',
                    format: 'Y-m-d H:i:s',
                    text: '时间',
                    sortable: false,
                    align: 'left',
                    flex: 1
                }]
            });
        }
        return me.grid;
    },
    bindData: function (record) {
        var me = this,
            store = me.getGrid().getStore();
        console.log(record)
        store.load({
            params: {
                'orderId': record.get('relationOrderId'),
                'deliveryType': record.get('deliveryType'),
                'sellerAccount': record.get('sellerAccount')
            }
        });
    },
    initComponent: function () {
        var me = this;
        Ext.applyIf(me, {
            items: [me.getGrid()]
        });
        me.callParent(arguments);
    }
});

Ext.define('MyApp.view.ApperalDeliveryOrderManager', {
    extend: 'Ext.panel.Panel',
    id: 'ApperalDeliveryOrderManager',
    closable: true,
    title: '申诉订单管理',
    layout: "border",
    autoScroll: false,
    toolbar: null,
    getToolbar: function () {
        var me = this;
        if (Ext.isEmpty(me.toolbar)) {
            me.toolbar = Ext.widget('toolbar', {
                dock: 'top',
                items: [{
                    text: '明细',
                    listeners: {
                        click: {
                            fn: me.showDeliveryOrder,
                            scope: me
                        }
                    }
                }, {
                    text: '原始收货订单日志',
                    listeners: {
                        click: {
                            fn: me.showOrderLog,
                            scope: me
                        }
                    }
                }, {
                    text: '人工完单',
                    listeners: {
                        click: {
                            fn: me.manualOp,
                            scope: me
                        }
                    }
                }]
            });
        }
        return me.toolbar;
    },
    showDeliveryOrder: function (button, e, eOpts) {
        var grid = this.getDeliveryOrderGrid(),
            selModel = grid.getSelectionModel(),
            selRecords = selModel.getSelection(), record,
            window = this.getDeliveryOrderWindow();
        if (selRecords == null || selRecords.length <= 0) {
            Ext.ux.Toast.msg("温馨提示", "请先选择申诉订单记录");
            return;
        }
        record = selRecords[0];
        window.bindData(record);
        window.show();
    },
    manualOp: function () {
        var grid = this.getDeliveryOrderGrid(),
            selModel = grid.getSelectionModel(),
            selRecords = selModel.getSelection(), order,
            window = this.getDetailDeliveryOrderWindow();
        if (selRecords == null || selRecords.length <= 0) {
            Ext.ux.Toast.msg("温馨提示", "请先选择要处理的订单");
            return;
        }
        record = selRecords[0];
        var mainOrderStatus = record.data["status"];
        /*if(mainOrderStatus!=7){
         Ext.ux.Toast.msg("温馨提示", "不是人工介入的订单，不能进行人工结单");
         return;
         }*/

        window.show();
        window.bindData(record);
    },
    showOrderLog: function (button, e, eOpts) {
        var grid = this.getDeliveryOrderGrid(),
            selModel = grid.getSelectionModel(),
            selRecords = selModel.getSelection(), record,
            window = this.getApperalOrderLogWindow();
        if (selRecords == null || selRecords.length <= 0) {
            Ext.ux.Toast.msg("温馨提示", "请先选择收货订单记录");
            return;
        }
        record = selRecords[0];
        window.bindData(record);
        window.show();
    },
    detailDeliveryOrderWindow: null,
    getDetailDeliveryOrderWindow: function () {
        var me = this;
        if (me.detailDeliveryOrderWindow == null) {
            me.detailDeliveryOrderWindow = Ext.create('MyApp.view.ApperalDeliveryOrderFinishWindow');
        }
        return me.detailDeliveryOrderWindow;
    },
    deliveryOrderWindow: null,
    getDeliveryOrderWindow: function () {
        if (this.deliveryOrderWindow == null) {
            this.deliveryOrderWindow = new MyApp.view.ApperalDeliveryOrderDetailWindow();
        }
        return this.deliveryOrderWindow;
    },
    apperalOrderLogWindow: null,
    getApperalOrderLogWindow: function () {
        if (this.apperalOrderLogWindow == null) {
            this.apperalOrderLogWindow = new MyApp.view.ApperalOrderLogWindow();
        }
        return this.apperalOrderLogWindow;
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
                },
                items: [{
                    fieldLabel: '创建日期',
                    xtype: 'rangedatefield',
                    fromName: 'createStartTime',
                    toName: 'createEndTime',
                    fromValue: new Date(),
                    toValue: new Date()
                }, {
                    fieldLabel: '订单号',
                    name: 'orderId'
                }, {
                    fieldLabel: '原始订单号',
                    name: 'appealOrder'
                }, {
                    fieldLabel: '收购方账号',
                    name: 'buyerAccount'
                }, {
                    fieldLabel: '出货方账号',
                    name: 'sellerAccount'
                }, {
                    xtype: 'gamelinkselector',
                    itemId: 'MyApp_view_goods_gamelink_ID',
                    //allowBlank: false,
                    fieldLabel: '游戏属性'
                }, DataDictionary.getDataDictionaryCombo('shOrderType', {
                    fieldLabel: '订单状态',
                    labelWidth: 100,
                    name: 'shOrderType',
                    editable: false,
                    value: 0
                }, {
                    value: 0,
                    display: '全部'
                }), DataDictionary.getDataDictionaryCombo('transferStatus', {
                    fieldLabel: '转账状态',
                    labelWidth: 100,
                    name: 'transferStatus',
                    editable: false,
                    value: -1
                }, {
                    value: -1,
                    display: '全部'
                }), DataDictionary.getDataDictionaryCombo('theDeliveryType', {
                    fieldLabel: '收货方式',
                    labelWidth: 100,
                    name: 'deliveryType',
                    editable: false,
                    value: 0
                }), {
                    xtype: 'tradetype',
                    itemId: 'MyApp_view_goods_tradetype_ID',
                    fieldLabel: '交易方式'
                }, DataDictionary.getDataDictionaryCombo('timeoutstatus', {
                    fieldLabel: '是否超时',
                    labelWidth: 100,
                    name: 'isTimeout',
                    editable: false,
                    value: "-1"
                }), DataDictionary.getDataDictionaryCombo('machineArtificialType', {
                    fieldLabel: '机器异常转人工',
                    labelWidth: 100,
                    name: 'machineArtificialStatus',
                    editable: false,
                    value: -1
                }, {
                    value: -1,
                    display: '全部'
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
        return me.queryForm;
    },
    store: null,
    getStore: function () {
        var me = this;
        if (me.store == null) {
            me.store = Ext.create('MyApp.store.ApperalDeliveryOrderStore', {
                autoLoad: true,
                listeners: {
                    beforeload: function (store, operation, eOpts) {
                        var queryForm = me.getQueryForm();
                        var gameName = queryForm.getForm().findField('gameName'),
                            region = "",
                            server = "";
                        if (queryForm != null) {
                            var values = queryForm.getValues();
                            gameName = Ext.String.trim(gameName.getRawValue());
                            if (gameName != '') {
                                region = Ext.String.trim(values.region);
                                server = Ext.String.trim(values.server);
                            }
                            var params = {
                                'deliveryOrder.orderId': Ext.String.trim(values.orderId),
                                'startTime': values.createStartTime,
                                'endTime': values.createEndTime,
                                'deliveryOrder.buyerAccount': Ext.String.trim(values.buyerAccount),
                                'deliveryOrder.sellerAccount': Ext.String.trim(values.sellerAccount),
                                'deliveryOrder.status': values.shOrderType,
                                'deliveryOrder.gameName': gameName,//名
                                'deliveryOrder.region': region,//区
                                'deliveryOrder.server': server,//服
                                'deliveryOrder.transferStatus': values.transferStatus,//转账状态
                                'deliveryOrder.deliveryType': values.deliveryType,
                                'deliveryOrder.tradeTypeName': values.tradeTypeName,
                                'deliveryOrder.machineArtificialStatus': values.machineArtificialStatus,
                                'isTimeOut': values.isTimeout
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
    deliveryOrderGrid: null,
    getDeliveryOrderGrid: function () {
        var me = this;
        if (Ext.isEmpty(me.deliveryOrderGrid)) {
            me.deliveryOrderGrid = Ext.widget('gridpanel', {
                header: false,
                region: 'center',
                columnLines: true,
                store: me.getStore(),
                columns: [{
                    xtype: 'rownumberer'
                }, {
                    dataIndex: 'gameName',
                    text: '游戏',
                    flex: 0.7,
                    align: 'center'
                }, {
                    dataIndex: 'region',
                    text: '游戏区',
                    flex: 0.7,
                    align: 'center'
                }, {
                    dataIndex: 'server',
                    text: '游戏服',
                    flex: 0.7,
                    align: 'center'
                }, {
                    dataIndex: 'orderId',
                    text: '订单号',
                    flex: 1.2,
                    align: 'center'
                }, {
                    dataIndex: 'buyerAccount',
                    text: '收购方',
                    flex: 0.7,
                    align: 'center'
                }, {
                    dataIndex: 'sellerAccount',
                    text: '出货方',
                    flex: 0.7,
                    align: 'center'
                }, {
                    dataIndex: 'price',
                    text: '单价',
                    flex: 0.7,
                    align: 'center'
                }, {
                    dataIndex: 'count',
                    text: '计划收购数量',
                    flex: 0.7,
                    align: 'center'
                }, {
                    dataIndex: 'amount',
                    text: '计划收购金额',
                    flex: 0.7,
                    align: 'center'
                }, {
                    dataIndex: 'realCount',
                    text: '实际收购数量',
                    flex: 0.7,
                    align: 'center'
                }, {
                    dataIndex: 'realAmount',
                    text: '实际收购金额',
                    flex: 0.7,
                    align: 'center'
                }, {
                    dataIndex: 'takeOverSubjectId',
                    text: '接收物服',
                    flex: 0.7,
                    align: 'center'
                }, {
                    dataIndex: 'status',
                    text: '订单状态 ',
                    flex: 1,
                    sortable: false,
                    align: 'center',
                    renderer: function (value, metaData, record, rowIndex, colIndex, store, view) {
                        console.log(record.get("deliveryType"))
                        if (record.get("deliveryType") == 2 && value == 2) {
                            return '待分配角色';
                        } else if (value != 0) {
                            return DataDictionary.rendererSubmitToDisplay(value, 'shOrderType');
                        } else {
                            return '';
                        }
                    }
                }, {
                    dataIndex: 'deliveryType',
                    text: '收货方式 ',
                    flex: 1,
                    sortable: false,
                    align: 'center',
                    renderer: function (value, metaData, record, rowIndex, colIndex, store, view) {
                        if (value != -1) {
                            return DataDictionary.rendererSubmitToDisplay(value, 'theDeliveryType');
                        } else {
                            return '';
                        }
                    }
                }, {
                    dataIndex: 'isTimeout',
                    text: '是否超时 ',
                    flex: 1,
                    sortable: false,
                    align: 'center',
                    renderer: function (value, metaData, record, rowIndex, colIndex, store, view) {
                        if (value != null) {
                            return DataDictionary.rendererSubmitToDisplay(value, 'timeOutSub');
                        } else {
                            return '';
                        }
                    }
                }, {
                    dataIndex: 'transferStatus',
                    text: '转账状态 ',
                    flex: 1,
                    sortable: false,
                    align: 'center',
                    renderer: function (value, metaData, record, rowIndex, colIndex, store, view) {
                        if (value != -1) {
                            return DataDictionary.rendererSubmitToDisplay(value, 'transferStatus');
                        } else {
                            return '';
                        }
                    }
                }, {
                    xtype: 'datecolumn',
                    format: 'Y-m-d H:i:s',
                    dataIndex: 'createTime',
                    sortable: false,
                    flex: 1.5,
                    align: 'center',
                    text: '创建时间 '
                }],
                listeners: {
                    itemdblclick: function (view, record, item, index, e, eOpts) {
                        me.showDeliveryOrder();
                    }
                },
                dockedItems: [me.getToolbar(), me.getPagingToolbar()],
                selModel: Ext.create('Ext.selection.CheckboxModel', {
                    allowDeselect: true,
                    mode: 'MULTI'
                })
            });
        }
        return me.deliveryOrderGrid;

    },
    initComponent: function () {
        var me = this;
        Ext.applyIf(me, {
            items: [me.getQueryForm(), me.getDeliveryOrderGrid()]
        });
        me.callParent(arguments);
    }
});

Ext.define('Tradetype.selector.TradeTypeContainer', {
    extend: 'Gamegold.selector.LinkedContainer',
    alias: 'widget.tradetype',
    layout: 'column',
    tradetype: null,
    getTradeType: getTradeType,
    initComponent: function () {
        var me = this;
        me.items = [me.getTradeType()];
        me.callParent();
    }
});

function getTradeType() {
    var me = this;
    if (Ext.isEmpty(me.tradetype)) {
        me.tradetype = Ext.widget('linkedselector', {
            itemId: 'TradeType_selector_tradetype_ID',
            store: Ext.create('MyApp.store.regularShTradeStore'),
            queryParam: 'enabled',
            displayField: 'name',
            valueField: 'name',
            name: 'tradeTypeName',
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
    return me.tradetype;
}