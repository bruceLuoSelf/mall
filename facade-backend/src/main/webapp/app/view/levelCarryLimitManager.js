/**
 * 等级携带上限配置
 */
Ext.define('MyApp.view.levelCarryLimitManager',{
    extend:'Ext.panel.Panel',
    id:'levelCarryLimitManager',
    layout:"border",
    closable:true,
    title:'等级携带上限配置',
    toolbar:null,
    getToolbar:function () {
        var me = this;
        if (Ext.isEmpty(me.toolbar)) {
            me.toolbar = Ext.widget('toolbar',{
                dock:'top',
                items:[{
                    text:'添加配置',
                    listeners:{
                        click:{
                            fn:me.addLevelCarryLimit,
                            scope:me
                        }
                    }
                },'-',{
                    text:'修改配置',
                    listeners:{
                        click:{
                            fn:me.updateLevelCarryLimit,
                            scope:me
                        }
                    }
                },'-',{
                    text:'删除配置',
                    listeners:{
                        click:{
                            fn:me.deleteLevelCarryLimit,
                            scope:me
                        }
                    }
                },'-',{
                    text:'同步redis',
                    listeners:{
                        click:{
                            fn:me.buildRedisData,
                            scope:me
                        }
                    }
                }]
            })
        }
        return me.toolbar;
    },
    
    addLevelCarryLimitWindow:null,
    getLevelCarryLimitWindow:function () {
        if (this.addLevelCarryLimitWindow == null) {
            this.addLevelCarryLimitWindow = new MyApp.view.addLevelCarryLimitWindow();
        }
        return this.addLevelCarryLimitWindow;
    },

    addLevelCarryLimit:function (button,e,eOpts) {
        var window = this.getLevelCarryLimitWindow();
        window.bindData(Ext.create('MyApp.model.LevelCarryLimitModel'),false);
        window.show();
    },

    updateLevelCarryLimit:function(button,e,eOpts){
        var grid = this.getLevelCarryLimitGrid(),
            selModel = grid.getSelectionModel(),
            selRecords = selModel.getSelection(),
            window = this.getLevelCarryLimitWindow();
        if (selRecords == null || selRecords.length <= 0) {
            Ext.ux.Toast.msg("温馨提示","请先选择要修改的配置")
            return ;
        }
        window.bindData(selRecords[0],true);
        window.show();
    },

    deleteLevelCarryLimit:function () {
        var me = this,
            grid = me.getLevelCarryLimitGrid(),
            selModel = grid.getSelectionModel(),
            selRecords = selModel.getSelection();
        if (selRecords == null || selRecords.length <= 0) {
            Ext.ux.Toast.msg("温馨提示", "请先选择要删除的配置");
            return;
        }
        var record = selRecords[0];
        Ext.MessageBox.confirm('温馨提示','确定删除吗?',function(btn){
            if (btn == 'yes') {
                me.getStore().remove(record);
                Ext.Ajax.request({
                    url:'./delivery/deleteById.action',
                    params:{
                        'id':record.get('id')
                    },
                    method:'POST',
                    success:function (response,opts) {
                        Ext.ux.Toast.msg("温馨提示", "删除成功！");
                        me.getStore().load();
                        grid.getSelectionModel().deselectAll();
                    },
                    exception:function (response,opts) {
                        var json = Ext.decode(response.responseText);
                        Ext.ux.Toast.msg("温馨提示", json.message);
                    }
                })
            }else{
                return ;
            }
        })
    },
    
    buildRedisData:function() {
        Ext.MessageBox.confirm('温馨提示','确定要同步redis数据吗?',function(btn){
            if (btn == 'yes') {
                Ext.Ajax.request({
                    url:'./delivery/buildRedisData.action',
                    method:'POST',
                    success:function (response,opts) {
                        Ext.ux.Toast.msg("温馨提示", "同步成功！");
                    },
                    exception:function (response,opts) {
                        var json = Ext.decode(response.responseText);
                        Ext.ux.Toast.msg("温馨提示", json.message);
                    }
                })
            }else{
                return ;
            }
        })
    },
    
    queryForm:null,
    getQueryForm:function () {
        var me = this;
        if (me.queryForm == null) {
            me.queryForm = Ext.widget('form',{
                region:'north',
                layout:'column',
                defaults:{
                    margin:'10 10 10 10'
                },
                items:[{
                        xtype: 'gameselectorsellersetting',
                        itemId : 'MyApp_view_goods_gamelink_ID',
                        columnWidth: .45,
                        allowBlank: true,
                        fieldLabel: '游戏名称'
                    },Ext.create('Ext.form.ComboBox', {
                        fieldLabel: '商品类型',
                        labelWidth: 80,
                        name: 'goodsTypeId',
                        store: Ext.create('MyApp.store.MallGoodsTypeNameIdStore', {
                            listeners: {
                                load: function (store, records, successful, eOpts) {
                                    //添加
                                    store.insert(0,{id:0, name:'全部'});
                                }
                            }
                        }),
                        displayField: 'name',
                        valueField: 'id',
                        value:'全部',
                        editable: false
                    }),
                    {
                    xtype:'numberfield',
                    columnWidth:.15,
                    fieldLabel:'最小等级',
                    name:'minLevel'
                },{
                    xtype:'numberfield',
                    columnWidth:.15,
                    fieldLabel:'最大等级',
                    name:'maxLevel'
                },{
                    xtype:'numberfield',
                    columnWidth:.2,
                    fieldLabel:'携带上限',
                    name:'carryUpperLimit'
                }],
                buttons:[{
                    text:'查询',
                    handler:function () {
                        me.getPagingToolbar().moveFirst();
                    }
                },'-',{
                    text:'重置',
                    handler:function () {
                        me.getQueryForm().getForm().reset()
                    }
                },'->']
            });
        }
        return this.queryForm;
    },

    store:null,
    getStore:function () {
        var me = this;
        if (me.store == null) {
            me.store = Ext.create('MyApp.store.LevelCarryLimitStore',{
                autoLoad:true,
                listeners:{
                    beforeload:function (store,operation,eOpts) {
                        var queryForm = me.getQueryForm();
                        var form = queryForm.getForm();
                        var gameName = form.findField("gameName").getRawValue();
                        var goodsType = form.findField("goodsTypeId");
                        console.log(goodsType)
                        if (queryForm != null) {
                            var values = queryForm.getValues();
                            var params ={
                                'levelCarryLimitEO.minLevel':values.minLevel,
                                'levelCarryLimitEO.maxLevel':values.maxLevel,
                                'levelCarryLimitEO.carryUpperLimit':values.carryUpperLimit,
                                'levelCarryLimitEO.gameName':Ext.String.trim(gameName),
                                'levelCarryLimitEO.goodsTypeId':goodsType.getValue(),

                            }
                            Ext.apply(operation,{
                                params:params
                            })
                        }
                    }
                }
            })
        }
        return me.store;
    },

    pagingToolbar:null,
    getPagingToolbar:function () {
        var me = this;
        if (me.pagingToolbar == null) {
            me.pagingToolbar = Ext.widget('pagingtoolbar',{
                store:me.getStore(),
                dock:'bottom',
                displayInfo:true
            })
        }
        return me.pagingToolbar;
    },

    levelCarryLimitGrid:null,
    getLevelCarryLimitGrid:function () {
        var me = this;
        if (Ext.isEmpty(me.levelCarryLimitGrid)) {
            me.levelCarryLimitGrid = Ext.widget('gridpanel',{
                region:'center',
                header:false,
                columnLines:true,
                store:me.getStore(),
                columns:[{
                    xtype:'rownumberer'
                },{
                    dataIndex: 'gameName',
                    sortable: false,
                    flex: 1,
                    align: 'center',
                    text: '游戏名称'
                },{
                    dataIndex: 'goodsTypeName',
                    sortable: false,
                    flex: 1,
                    align: 'center',
                    text: '商品类型'
                },{
                    dataIndex: 'minLevel',
                    text: '最小等级',
                    flex: 1.7,
                    align: 'center'
                },{
                    dataIndex: 'maxLevel',
                    text: '最大等级',
                    flex: 1.7,
                    align: 'center'
                },{
                    dataIndex: 'carryUpperLimit',
                    text: '携带上限',
                    flex: 1.7,
                    align: 'center'
                }],
                dockedItems:[me.getToolbar(),me.getPagingToolbar()],
                selModel:Ext.create('Ext.selection.CheckboxModel',{
                    allowDeselect:true,
                    mode:'MULTI'
                })
            });
        }
        return me.levelCarryLimitGrid;
    },

    initComponent:function () {
        var me = this;
        Ext.applyIf(me,{
            items:[me.getQueryForm(),me.getLevelCarryLimitGrid()]
        });
        me.callParent(arguments);
    }

});


Ext.define('MyApp.view.addLevelCarryLimitWindow',{
    extend:'Ext.window.Window',
    title:'新增/修改等级携带上限配置',
    width:600,
    closeAction:'hide',
    modal:true,
    form:null,
    getForm:function () {
        var me = this;
        if (me.form == null) {
            me.form = Ext.widget('form',{
                layout:'column',
                defaults:{
                    margin:'10 10 5 5',
                    columnWidth:.5,
                    labelWidth:130,
                    xtype:'textfield'
                },
                items:[
                    {
                        xtype:'hidden',
                        name:'id'
                    },{
                        xtype:'gameselectorsellersetting',
                        itemId:'MyApp_view_gamelink_ID',
                        columnWidth:1,
                        allowBlank:false,
                        gameChanged:function(ths,the,eOpts) {
                            var form = me.getForm().getForm();
                            var categoryCombo = form.findField("goodsTypeId");
                            categoryCombo.setRawValue(null);
                            categoryCombo.getStore().load();
                        },
                        fieldLabel: '游戏属性'
                    },
                    Ext.create('Ext.form.ComboBox', {
                        itemId: 'MyApp_view_goods_type_ID',
                        fieldLabel: '商品类型',
                        allowBlank: false,
                        name: 'goodsTypeId',
                        labelWidth: 130,
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
                        fieldLabel:'最小等级',
                        allowBlank:false,
                        xtype:'numberfield',
                        name:'minLevel'
                    },{
                        fieldLabel:'最大等级',
                        allowBlank:false,
                        xtype:'numberfield',
                        name:'maxLevel'
                    }
                    ,{
                        fieldLabel:'携带上限',
                        allowBlank:false,
                        xtype:'numberfield',
                        name:'carryUpperLimit'
                    }
                ],
                buttons:[{
                    text:'保存',
                    handler:function () {
                        var form = me.getForm().getForm();
                        var gameName = form.findField('gameName');
                        var goodsType = form.findField("goodsTypeId");
                        console.log(goodsType)
                        var record = form.getRecord();
                        var url = './delivery/addLevelCarryLimit.action',
                            message = '新增';
                        if (!form.isValid()){
                            return ;
                        }
                        var params = {
                            'levelCarryLimitEO.minLevel':form.findField("minLevel").getValue(),
                            'levelCarryLimitEO.maxLevel':form.findField("maxLevel").getValue(),
                            'levelCarryLimitEO.carryUpperLimit':form.findField("carryUpperLimit").getValue(),
                            'levelCarryLimitEO.gameName':gameName.getRawValue(),
                            'levelCarryLimitEO.goodsTypeId':goodsType.getValue(),
                            'levelCarryLimitEO.goodsTypeName':goodsType.getRawValue(),
                        };
                        if (me.isUpdate){
                            url = './delivery/updateLevelCarryLimit.action';
                            message = '修改';
                            Ext.Object.merge(params,{
                                'levelCarryLimitEO.id':form.findField("id").getValue()
                            });
                            console.log(params)
                        }
                        form.submit({
                            url:url,
                            method:'POST',
                            params:params,
                            success:function (form,action,json) {
                                var carryLimitManager = Ext.getCmp('levelCarryLimitManager'),
                                    store = carryLimitManager.getStore();
                                Ext.ux.Toast.msg("温馨提示",message + "成功");
                                me.close();
                                store.load();
                            },
                            exception:function (form,action,json) {
                                Ext.ux.Toast.msg("温馨提示",json.message);
                            }
                        })
                    }
                }]
            })
        }
        return me.form;
    },

    isUpdate:null,
    bindData:function (record,isUpdate) {
        var me = this;
        var formView = me.getForm();
        var form = formView.getForm();
        form.reset();;
        form.loadRecord(record);
        me.isUpdate = isUpdate;
        if (!isUpdate) {
            form.reset();
            form.findField("company").setDisabled(false);
            form.findField("gameName").setDisabled(false);
            form.findField("goodsTypeId").setDisabled(false);
            form.findField("minLevel").setDisabled(false);
            form.findField("maxLevel").setDisabled(false);
        }else{
            form.findField("company").setDisabled(true);
            form.findField("gameName").setDisabled(true);
            form.findField("goodsTypeId").setDisabled(true);
            form.findField("minLevel").setDisabled(true);
            form.findField("maxLevel").setDisabled(true);

        }
    },

    constructor:function (config) {
        var me = this,
            cfg = Ext.apply({},config);
        me.items = [me.getForm()]
        me.callParent([cfg]);
    }
})