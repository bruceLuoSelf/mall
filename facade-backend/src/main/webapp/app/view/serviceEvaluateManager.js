Ext.define('MyApp.view.serviceEvaluateManager', {
    extend: 'Ext.panel.Panel',
    id:'serviceEvaluateManager',
    closable: true,
    title: '评价统计',
    autoScroll: false,
    listeners:{
        'resize':function(){
            this.logGrid.setHeight(window.document.body.offsetHeight-190);
        }
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
                    fieldLabel: '查询日期',
                    columnWidth: .5,
                    xtype: 'rangedatefield',
                    //起始日期组件的name属性。
                    fromName: 'createStartTime',
                    //终止日期组件的name属性。
                    toName: 'createEndTime',
                    fromValue:new Date(new Date()-6*24*60*60*1000),
                    toValue: new Date()
                },{
                    fieldLabel: '客服账号',
                    name: 'account'
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
            me.store = Ext.create('MyApp.store.ServiceEvaluateStore',{
                autoLoad: true,
                listeners: {
                    beforeload : function(store, operation, eOpts) {
                        var queryForm = me.getQueryForm();
                        if (queryForm != null) {
                            var values = queryForm.getValues();
                            //var region = Ext.String.trim(values.region);
                            Ext.apply(operation, {
                                params: {
                                    'serviceEvaluateStatistics.account': values.account,
                                    'serviceEvaluateStatistics.startTime': values.createStartTime,
                                    'serviceEvaluateStatistics.endTime': values.createEndTime
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
                    dataIndex: 'account',
                    text: '客服账号',
                    flex: 1.2,
                    align: 'center'
                },{
                    dataIndex: 'totalCount',
                    text: '评价笔数',
                    flex: 0.7,
                    align: 'center'
                },{
                    dataIndex: 'goodRate',
                    text: '好评率',
                    flex: 0.7,
                    align: 'center'
                },{
                    dataIndex: 'veryGoodCount',
                    text: '非常满意笔数',
                    flex: 0.8,
                    align: 'center'
                },{
                    dataIndex: 'goodCount',
                    text: '满意笔数',
                    flex: 0.6,
                    align: 'center'
                },{
                    dataIndex: 'normalCount',
                    text: '一般笔数',
                    flex: 0.6,
                    align: 'center'
                },{
                    dataIndex: 'yawpCount',
                    text: '不满意笔数',
                    flex: 0.8,
                    align: 'center'
                },{
                    dataIndex: 'veryYawpCount',
                    text: '非常不满意笔数',
                    flex: 1,
                    align: 'center'
                },{
                    dataIndex: 'defaultCount',
                    text: '默认评价笔数',
                    flex: 0.8,
                    align: 'center'
                }],
                dockedItems: [me.getPagingToolbar()],
                selModel: Ext.create('Ext.selection.CheckboxModel', {
                    allowDeselect: false,
                    mode: 'single'
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