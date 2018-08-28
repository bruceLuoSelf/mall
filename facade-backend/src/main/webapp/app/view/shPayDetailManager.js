Ext.define('MyApp.view.shPayDetailManager',{
    extend:'Ext.panel.Panel',
    id:'shPayDetailManager',
    closable: true,
    title: '付款明细',
    ////////////////////////////////////////////////////
    autoScroll: false,
    layout:'border',
    queryForm : null,
    getQueryForm: function(){
        var me=this;
        if(me.queryForm==null){
            me.queryForm = Ext.widget('form',{
                layout:'column',
                region:'north',
                defaults: {
                    margin: '10 10 10 10',
                    xtype: 'textfield'
                },
                items: [{
                    fieldLabel: '创建日期',
                    xtype: 'rangedatefield',
                    fromName: 'createStartTime',
                    toName: 'createEndTime',
                    fromValue: new Date(new Date()-30*24*60*60*1000),
                    toValue: new Date()
                },{
                    fieldLabel: '付款明细订单',
                    name: 'orderId'
                },{
                    fieldLabel: '充值订单号',
                    name: 'payOrderId'
                },{
                    fieldLabel: '出货订单号',
                    name: 'chOrderId'
                }],
                buttons: [{
                    text:'重置',
                    handler: function() {
                        me.getQueryForm().getForm().reset();
                    }
                },'->',{
                    text:'查询',
                    handler: function (){
                        me.getPagingToolbar().moveFirst();
                    }
                }]
            });
        }
        return me.queryForm;
    },
    store:null,
    getStore: function(){
      var me=this;
        if(me.store==null){
            me.store = Ext.create('MyApp.store.PayDetailStore',{
                autoLoad: true,
                listeners: {
                    beforeload: function(store,operation,eOpts) {
                        var queryForm = me.getQueryForm();
                        if(queryForm !=null){
                            var values = queryForm.getValues();
                            Ext.apply(operation, {
                                params:{
                                    'payDetail.orderId':Ext.String.trim(values.orderId),
                                    'startTime':values.createStartTime,
                                    'endTime':values.createEndTime,
                                    'payDetail.payOrderId':Ext.String.trim(values.payOrderId),
                                    'payDetail.chOrderId':Ext.String.trim(values.chOrderId),
                                    'account':values.amount,
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
    payDetailGrid: null,
    getPayDetailGrid: function (){
        var me=this;
        if(Ext.isEmpty(me.payDetailGrid)){
            me.payDetailGrid =Ext.widget('gridpanel',{
                header: false,
                columnLines: true,
                region:'center',
                store: me.getStore(),
                columns: [{
                    xtype: 'rownumberer'
                },{
                    dataIndex: 'orderId',
                    text: '付款明细订单',
                    flex:  1,
                    align: 'center'
                },{
                    dataIndex: 'payOrderId',
                    text: '充值订单号',
                    flex:  1,
                    align: 'center'
                },{
                    dataIndex: 'chOrderId',
                    text: '出货订单号',
                    flex:  1,
                    align: 'center'
                },{
                    dataIndex: 'amount',
                    text: '金额',
                    flex: 1,
                    align: 'center'
                },{
                    xtype: 'datecolumn',
                    format:'Y-m-d H:i:s',
                    dataIndex: 'createTime',
                    sortable: false,
                    flex: 1.5,
                    align: 'center',
                    text: '时间'
                }],
                dockedItems: [me.getPagingToolbar()],
                selModel: Ext.create('Ext.selection.CheckboxModel', {
                    allowDeselect: true,
                    mode: 'MULTI',
                    
                })
            });
        }
        return me.payDetailGrid;

    },
    initComponent: function() {
        var me = this;
        Ext.applyIf(me, {
            items: [me.getQueryForm(),me.getPayDetailGrid()]
        });
        me.callParent(arguments);
    }
});