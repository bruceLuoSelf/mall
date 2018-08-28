Ext.define('MyApp.view.orderEvaluateManager', {
    extend: 'Ext.panel.Panel',
    id:'orderEvaluateManager',
    closable: true,
    title: '订单评价记录',
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
                    columnWidth: 0.35,
                    xtype: 'rangedatefield',
                    //起始日期组件的name属性。
                    fromName: 'createStartTime',
                    //终止日期组件的name属性。
                    toName: 'createEndTime',
                    fromValue:new Date(),
                    toValue: new Date()
                },{
                    fieldLabel: '订单号',
                    columnWidth: 0.25,
                    name: 'orderId'
                },DataDictionary.getDataDictionaryCombo('evaluateState',{
                    fieldLabel: '满意度',
                    name: 'score',
                    columnWidth: 0.25,
                    value: 0,
                    editable: false
                }),{
                    xtype: 'checkbox',
                    boxLabel: '是否显示默认评论',
                    columnWidth: 0.15,
                    inputValue: '1',
                    id:'isDefault',
                    checked: false
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
            me.store = Ext.create('MyApp.store.OrderEvaluateStore',{
                autoLoad: true,
                listeners: {
                    beforeload : function(store, operation, eOpts) {
                        var queryForm = me.getQueryForm();
                        if (queryForm != null) {
                            var values = queryForm.getValues();
                            //var region = Ext.String.trim(values.region);
                            var isDefault = Ext.getCmp('isDefault').getValue();
                            Ext.apply(operation, {
                                params: {
                                    'serviceEvaluate.orderId': values.orderId,
                                    'serviceEvaluate.startTime': values.createStartTime,
                                    'serviceEvaluate.endTime': values.createEndTime,
                                    'serviceEvaluate.score':values.score,
                                    'serviceEvaluate.isDefault':isDefault
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
                    dataIndex: 'orderId',
                    xtype: 'ellipsiscolumn',
                    flex:0.8,
                    sortable: false,
                    align: 'center',
                    text: '订单号'
                },{
                    dataIndex: 'loginAccount',
                    xtype: 'ellipsiscolumn',
                    flex:0.8,
                    sortable: false,
                    align: 'center',
                    text: '客服'
                },{
                    dataIndex: 'score',
                    xtype: 'ellipsiscolumn',
                    flex: 0.5,
                    sortable: false,
                    align: 'center',
                    text: '满意度',
                    renderer: function(value){
                        return DataDictionary.rendererSubmitToDisplay(value,'evaluateState');
                    }
                },{
                    dataIndex: 'remark',
                    xtype: 'ellipsiscolumn',
                    flex: 1.3,
                    sortable: false,
                    align: 'center',
                    text: '评价'
                },{
                    dataIndex: 'isDefault',
                    text: '是否默认评论',
                    flex: 0.5,
                    align: 'center',
                    renderer: function(value, metaData, record, rowIndex, colIndex, store, view) {
                        return CommonFunction.rendererEnable(!value);
                    }
                },{
                    xtype: 'datecolumn',
                    format:'Y-m-d H:i:s',
                    dataIndex: 'createTime',
                    sortable: false,
                    flex: 1.5,
                    align: 'center',
                    text: '评论时间 '
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