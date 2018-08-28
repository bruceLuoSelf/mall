/**资金统计
 * Created by Administrator on 2016/12/15.
 */
Ext.define('MyApp.view.shFundStatisticsManager', {
    extend: 'Ext.panel.Panel',
    id:'shFundStatisticsManager',
    closable: true,
    title: '资金统计',
    autoScroll: false,
    layout: "border",
    shFundStatisticsWindow: null,
    getShFundStatisticsWindow: function(){
        if(this.shFundStatisticsWindow == null){
            this.shFundStatisticsWindow = new MyApp.view.ShFundStatisticsWindow();
        }
        return this.shFundStatisticsWindow;
    },
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
                    fieldLabel: ' 期初日期',
                    xtype: 'rangedatefield',
                    fromName: 'createStartTime',
                    toName: 'createEndTime',
                    fromValue: new Date(new Date()-30*24*60*60*1000),
                    toValue: new Date()
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
            me.store = Ext.create('MyApp.store.ShFundStatisticsStore',{
                autoLoad: true,
                listeners: {
                    beforeload : function(store, operation, eOpts) {
                        var queryForm = me.getQueryForm();
                        if (queryForm != null) {
                            var values = queryForm.getValues();
                            Ext.apply(operation, {
                                params: {
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
    shFundStatisticsGrid: null,
    getShFundStatisticsGrid: function(){
        var me = this;
        if(Ext.isEmpty(me.shFundStatisticsGrid)){
            me.shFundStatisticsGrid = Ext.widget('gridpanel',{
                header: false,
                region: 'center',
                columnLines: true,
                store: me.getStore(),
                columns: [{
                    xtype: 'rownumberer'
                },{
                    dataIndex: 'qcBalance',
                    text: '期初余额',
                    flex:  1,
                    align: 'center'
                },{
                    dataIndex: 'zfAmount',
                    text: '支付金额',
                    flex: 1.2,
                    align: 'center'
                },{
                    dataIndex: 'tkAmount',
                    text: '退款金额',
                    flex: 1.2,
                    align: 'center'
                },{
                    dataIndex: 'fkAmount',
                    text: '付款金额',
                    flex: 1.2,
                    align: 'center'
                },{
                    dataIndex: 'jbTozbao',
                    text: '金币转7bao金额',
                    flex: 1.2,
                    align: 'center'
                },{
                    dataIndex: 'qmBalance',
                    text: '期末余额',
                    flex: 1,
                    align: 'center'
                },{
                    xtype: 'datecolumn',
                    format:'Y-m-d H:i:s',
                    dataIndex: 'startTime',
                    sortable: false,
                    flex: 1.5,
                    align: 'center',
                    text: '期初时间'
                },{
                    xtype: 'datecolumn',
                    format:'Y-m-d H:i:s',
                    dataIndex: 'endTime',
                    sortable: false,
                    flex: 1.5,
                    align: 'center',
                    text: '期末时间'
                }],
                dockedItems: [me.getPagingToolbar()],
                selModel: Ext.create('Ext.selection.CheckboxModel', {
                    allowDeselect: true,
                    mode: 'MULTI'
                })
            });
        }
        return me.shFundStatisticsGrid;

    },
    initComponent: function() {
        var me = this;
        Ext.applyIf(me, {
            items: [me.getQueryForm(),me.getShFundStatisticsGrid()]
        });
        me.callParent(arguments);
    }
});