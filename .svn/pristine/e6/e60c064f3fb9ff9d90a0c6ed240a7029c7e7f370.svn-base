/**
 * 已发送短信管理
 */
Ext.define('MyApp.view.sentMessageManager', {
    extend: 'Ext.panel.Panel',
    id: 'sentMessageManager',
    closable: true,
    title: '已发送短信',
    ////////////////////////////////////////////////////
    autoScroll: false,
    listeners:{
        'resize':function(){
            this.grid.setHeight(window.document.body.offsetHeight-200);
        }
    },
    ////////////////////////////////////////////////////
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
                items: [
                    DataDictionary.getDataDictionaryCombo('SgameName',
                        {
                            fieldLabel: '游戏名称',
                            name: 'gameName',
                            labelWidth: 100,
                            editable: false
                        },
                        {value: null,display:'全部'}
                    ),
                    {
                        fieldLabel: '订单号',
                        columnWidth: .3,
                        labelWidth: 80,
                        name: 'orderId'
                    },{
                        fieldLabel: '创建日期',
                        columnWidth: .5,
                        labelWidth: 80,
                        xtype: 'rangedatefield',
                        //起始日期组件的name属性。
                        fromName: 'createStartTime',
                        //终止日期组件的name属性。
                        toName: 'createEndTime'
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
        return this.queryForm;
    },
    store: null,
    getStore: function(){
        var me = this;
        if(me.store==null){
            me.store = Ext.create('MyApp.store.SentMessageStore',{
                autoLoad: true,
                listeners: {
                    beforeload : function(store, operation, eOpts) {
                        var queryForm = me.getQueryForm();
                        if (queryForm != null) {
                            var values = queryForm.getValues();
                            Ext.apply(operation, {
                                params: {
                                    'createStartTime': values.createStartTime,
                                    'createEndTime': values.createEndTime,
                                    'gameName': values.gameName,
                                    'orderId': values.orderId
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
    grid: null,
    getGrid: function(){
        var me = this;
        if(Ext.isEmpty(me.grid)){
            me.grid = Ext.widget('gridpanel',{
                header: false,
                columnLines: true,
                store: me.getStore(),
                columns: [{
                    xtype: 'rownumberer'
                },{
                    dataIndex: 'order',
                    text: '游戏名称',
                    flex: 1.3,
                    align: 'center',
                    renderer: function(value){
                        return value.gameName;
                    }
                },{
                    dataIndex: 'order',
                    text: '订单号',
                    sortable: false,
                    flex: 1,
                    align: 'center',
                    renderer: function(value) {
                        return value.orderId;
                    }
                },{
                    dataIndex: 'order',
                    text: '买家',
                    sortable: false,
                    flex: 1,
                    align: 'center',
                    renderer: function(value){
                        return value.userAccount;
                    }
                },{
                    dataIndex: 'order',
                    sortable: false,
                    flex: 1,
                    text: '手机',
                    renderer: function(value){
                        return value.mobileNumber;
                    }
                },{
                    xtype: 'datecolumn',
                    format:'Y-m-d H:i:s',
                    dataIndex: 'createTime',
                    align: 'center',
                    text: '创建时间',
                    flex: 1
                },{
                    dataIndex: 'content',
                    align: 'center',
                    sortable: false,
                    text: '短信内容',
                    flex: 2,
                    renderer: function (value, metaData) {
                        metaData.tdAttr = 'data-qtip="' + value + '"';
                        return value;
                    }
                }],
                dockedItems: [me.getPagingToolbar()]
            });
        }
        return me.grid;
    },
    initComponent: function() {
        var me = this;
        Ext.applyIf(me, {
            items: [me.getQueryForm(),me.getGrid()]
        });
        me.callParent(arguments);
    }
});