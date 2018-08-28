Ext.define('MyApp.view.shPurchaseGameManager', {
        extend: 'Ext.panel.Panel',
        id: 'shPurchaseGameManager',
        closable: true,
        layout: "border",
        title: '采购商游戏配置管理',
        autoScroll: false,
        queryForm: null,
        getQueryForm: function () {
            var me = this;
            if (me.queryForm == null) {
                me.queryForm = Ext.widget('form', {
                    layout: 'column',
                    region: 'north',
                    defaults: {
                        margin: '10 10 10 10',

                    },
                    items: [{
                        fieldLabel: '收货商名称',
                        xtype: 'textfield',
                        name: 'purchaseAccount'
                    }, DataDictionary.getDataDictionaryCombo('theDeliveryTypeForShPurchaseGameOnly', {
                            fieldLabel: '收货方式',
                            labelWidth: 100,
                            name: 'deliveryTypeId',
                            editable: false
                    }),{
                        xtype: 'goodsType',
                        itemId: 'GoodsType_selector_GoodsType_ID',
                        fieldLabel: '商品类目'
                    }
                    //     , {
                    //     xtype: 'tradeType',
                    //     itemId: 'TradeType_selector_TradeType_ID',
                    //     fieldLabel: '交易方式'
                    // }
                    ],
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
                me.store = Ext.create('MyApp.store.shPurchaseGameStore', {
                    autoLoad: true,
                    listeners: {
                        beforeload: function (store, operation, eOpts) {

                            var queryForm = me.getQueryForm();
                            if (queryForm != null) {
                                var values = queryForm.getValues();
                                Ext.apply(operation, {
                                    params: {
                                        'purchaseGame.purchaseAccount': Ext.String.trim(values.purchaseAccount),
                                        // 'purchaseGame.tradeTypeName': Ext.String.trim(values.tradeTypeName),
                                        'purchaseGame.goodsTypeName': Ext.String.trim(values.goodsTypeName),
                                        'purchaseGame.deliveryTypeId': Ext.String.trim(values.deliveryTypeId),
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
        purchaseGameGrid: null,
        getpayOpurchaseGameGrid: function () {
            var me = this;
            if (Ext.isEmpty(me.purchaseGameGrid)) {
                me.purchaseGameGrid = Ext.widget('gridpanel', {
                    header: false,
                    columnLines: true,
                    region: 'center',
                    store: me.getStore(),
                    columns: [{
                        xtype: 'rownumberer'
                    },{
                        dataIndex: 'purchaseAccount',
                        text: '收货商名称',
                        flex: 1.2,
                        align: 'center'
                    }, {
                        dataIndex: 'gameName',
                        text: '游戏名称',
                        flex: 1.2,
                        align: 'center'
                    }, {
                        dataIndex: 'goodsTypeName',
                        text: '交易类目名称',
                        flex: 1,
                        align: 'center'
                    }, {
                        dataIndex: 'deliveryTypeName',
                        text: '收货模式名称',
                        flex: 1,
                        align: 'center'
                    }, {
                        dataIndex: 'tradeTypeName',
                        text: '交易方式名称',
                        flex: 1,
                        align: 'center'
                    }],
                    dockedItems: [me.getPagingToolbar()],
                    selModel: Ext.create('Ext.selection.CheckboxModel', {
                        allowDeselect: true,
                        mode: 'SINGLE'
                    })
                });
            }
            return me.purchaseGameGrid;
        },
        initComponent: function () {
            var me = this;
            Ext.applyIf(me, {
                items: [me.getQueryForm(), me.getpayOpurchaseGameGrid()]
            });
            me.callParent(arguments);
        }
    }
);
Ext.define('Goodstype.selector.GoodsTypeContainer', {
    extend: 'Gamegold.selector.LinkedContainer',
    alias: 'widget.goodsType',
    layout: 'column',
    goodstype: null,
    getGoodsType: getGoodsType,
    initComponent: function () {
        var me = this;
        me.items = [me.getGoodsType()];
        me.callParent();
    }
});
Ext.define('Tradetype.selector.TradeTypeContainer', {
    extend: 'Gamegold.selector.LinkedContainer',
    alias: 'widget.tradeType',
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
            itemId: 'TradeType_selector_TradeType_ID',
            store: Ext.create('MyApp.store.ShTradeForSelectStore'),
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

function getGoodsType() {
    var me = this;
    if (Ext.isEmpty(me.goodstype)) {
        me.goodstype = Ext.widget('linkedselector', {
            itemId: 'GoodsType_selector_GoodsType_ID',
            store: Ext.create('MyApp.store.GameCategoryConfigForSelectStore'),
            queryParam: 'isEnabled',
            displayField: 'name',
            valueField: 'name',
            name: 'goodsTypeName',
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
    return me.goodstype;
}

