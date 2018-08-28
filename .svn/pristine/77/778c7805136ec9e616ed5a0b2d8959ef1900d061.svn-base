/**
 * 出货订单日志
 */
Ext.define('MyApp.view.deliveryOrderLogManager', {
    extend: 'Ext.panel.Panel',
    id:'deliveryOrderLogManager',
    closable: true,
    layout: "border",
    title: '出货订单日志',
    autoScroll: false,
    // listeners:{
    //     'resize':function(){
    //         this.deliveryOrderLogGrid.setHeight(window.document.body.offsetHeight-290);
    //     }
    // },
    queryForm: null,
    getQueryForm: function(){
        var me = this;
        if(me.queryForm==null){
            me.queryForm = Ext.widget('form',{
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
                    fromValue: new Date(new Date()-30*24*60*60*1000),
                    toValue: new Date()
                },{
                    fieldLabel: '出货订单号',
                    name: 'orderId'
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
            me.store = Ext.create('MyApp.store.DeliveryOrderLogStore',{
                autoLoad: true,
                listeners: {
                    beforeload : function(store, operation, eOpts) {
                        var queryForm = me.getQueryForm();
                        if (queryForm != null) {
                            var values = queryForm.getValues();
                            Ext.apply(operation, {
                                params: {
                                    'orderLog.orderId': values.orderId,
                                    'startTime':values.createStartTime,
                                    'endTime':values.createEndTime
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
    deliveryOrderLogGrid: null,
    getDeliveryOrderLogGrid: function(){
        var me = this;
        if(Ext.isEmpty(me.deliveryOrderLogGrid)){
            me.deliveryOrderLogGrid = Ext.widget('gridpanel',{
                header: false,
                region: 'center',
                columnLines: true,
                store: me.getStore(),
                columns: [{
                    xtype: 'rownumberer'
                },{
                    dataIndex: 'orderId',
                    text: '出货订单号',
                    flex: 1.5,
                    align: 'center'
                },{
                    xtype: 'linebreakcolumn',
                    dataIndex: 'log',
                    text: '日志内容',
                    flex: 1.5,
                    align: 'center'
                },{
                    xtype: 'datecolumn',
                    format:'Y-m-d H:i:s',
                    dataIndex: 'createTime',
                    sortable: false,
                    flex: 1.5,
                    align: 'center',
                    text: '创建时间 '
                }],
                dockedItems: [me.getPagingToolbar()],
                selModel: Ext.create('Ext.selection.CheckboxModel', {
                    allowDeselect: true,
                    mode: 'MULTI'
                })
            });
        }
        return me.deliveryOrderLogGrid;

    },
    initComponent: function() {
        var me = this;
        Ext.applyIf(me, {
            items: [me.getQueryForm(),me.getDeliveryOrderLogGrid()]
        });
        me.callParent(arguments);
    }
});