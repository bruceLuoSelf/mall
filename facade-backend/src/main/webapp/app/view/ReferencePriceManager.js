/*
 * 添加配置管理界面
 */
/*
 * 参考价后台查询界面
 */
Ext.define('MyApp.view.ReferencePriceManager', {
    extend: 'Ext.panel.Panel',
    id: 'ReferencePriceManager',
    closable: true,
    layout: "border",
    title: '参考价后台查询',
    ////////////////////////////////////////////////////
    autoScroll: false,
    listeners: {
        'resize': function () {
            this.userGrid.setHeight(window.document.body.offsetHeight - 250);
        }
    },
    ////////////////////////////////////////////////////
    toolbar: null,
    getToolbar: function () {
        var me = this;
        if (Ext.isEmpty(me.toolbar)) {
            me.toolbar = Ext.widget('toolbar', {
                dock: 'top',
                items: []
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
                    margin: '10 10 10 10',
                    columnWidth: .1,
                    labelWidth: 80,
                    xtype: 'textfield'
                },
                items: [{
                    xtype: 'gamelinkselector',
                    itemId : 'MyApp_view_goods_gamelink_ID',
                    columnWidth: .5,
                    allowBlank: true,
                    fieldLabel: '游戏属性'
                },Ext.create('Ext.form.ComboBox', {
                    fieldLabel: '商品类目',
                    columnWidth: .20,
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
    store: null,
    getStore: function () {
        var me = this;
        if (me.store == null) {
            me.store = Ext.create('MyApp.store.ReferencePriceStore', {
                autoLoad: true,
                listeners: {
                    beforeload: function (store, operation, eOpts) {
                        var queryForm = me.getQueryForm();

                        var  gameId = queryForm.getForm().findField('gameName').getRawValue();
                        var  regionId = queryForm.getForm().findField('region').getRawValue();
                        var  seId = queryForm.getForm().findField('server').getRawValue();
                        var  raceId=queryForm.getForm().findField('gameRace').getRawValue();


                        if (queryForm != null) {
                            var values = queryForm.getValues();
                            Ext.apply(operation, {
                                params: {
                                    'referencePrice.gameName': gameId,
                                    'referencePrice.regionName': regionId,
                                    'referencePrice.serverName': seId,
                                    'referencePrice.raceName': raceId,
                                    'referencePrice.goodsTypeName':Ext.String.trim(values.goodsTypeName)
                                }
                            });
                        }
                    }
                }

            });
        }
        return me.store;
    },
    userGrid: null,
    getUserGrid: function () {
        var me = this;
        if (Ext.isEmpty(me.userGrid)) {
            me.userGrid = Ext.widget('gridpanel', {
                header: false,
                columnLines: true,
                region: 'center',
                store: me.getStore(),
                columns: [{
                    dataIndex: 'gameName',
                    flex: 1,
                    align: 'center',
                    text: '游戏名称'
                }, {
                    dataIndex: 'goodsTypeName',
                    flex: 1,
                    align: 'center',
                    text: '商品类型'
                },{
                    dataIndex: 'regionName',
                    flex: 1,
                    align: 'center',
                    text: '游戏区'
                },{
                    dataIndex: 'serverName',
                    flex: 1,
                    align: 'center',
                    text: '游戏服'
                },{
                    dataIndex: 'raceName',
                    flex: 1,
                    align: 'center',
                    text: '游戏阵营'
                }, {
                    dataIndex: 'createTime',
                    flex: 1,
                    align: 'center',
                    xtype: 'datecolumn',
                    format: 'Y-m-d H:i:s',
                    text: '创建时间'
                }, {
                    dataIndex: 'updateTime',
                    flex: 1,
                    align: 'center',
                    xtype: 'datecolumn',
                    format: 'Y-m-d H:i:s',
                    text: '更新时间'
                },{
                    dataIndex: 'unitPrice',
                    flex: 0.7,
                    sortable: false,
                    renderer: function(v) {
                        return Ext.util.Format.currency(v, '￥', 5);
                    },
                    align: 'center',
                    text: '价格'
                },{
                    dataIndex: 'totalAccount',
                    flex: 0.7,
                    sortable: false,
                    align: 'center',
                    text: '数量'
                },{
                    dataIndex: 'moneyName',
                    flex: 1,
                    align: 'center',
                    text: '单位'
                }],
                dockedItems: [me.getToolbar(), me.getPagingToolbar()],
                selModel: Ext.create('Ext.selection.CheckboxModel', {
                    allowDeselect: true,
                    mode: 'MULTI'
                })
            });
        }
        return me.userGrid;
    },
    initComponent: function () {
        var me = this;
        Ext.applyIf(me, {
            items: [me.getQueryForm(), me.getUserGrid()]
        });
        me.callParent(arguments);
    }
});
