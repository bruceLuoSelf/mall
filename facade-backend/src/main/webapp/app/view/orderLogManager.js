


Ext.define('MyApp.view.orderLogManager', {
	extend: 'Ext.panel.Panel',
	id:'orderLogManager',
	closable: true,
    title: '订单日志',
    autoScroll: false,
	listeners:{  
    'resize':function(){
        this.logGrid.setHeight(window.document.body.offsetHeight-233);
    	}
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
				items: [DataDictionary.getDataDictionaryCombo('orderLogOperations',{
					fieldLabel: '操作类型',
					name: 'orderLogOperations',
					columnWidth: .25,
					labelWidth: 80,
					editable: false
				},{value: null,display:'全部'}),{
					fieldLabel: '订单号',
					columnWidth: .25,
					labelWidth: 80,
				    name: 'orderId'
				},{
					fieldLabel: '用户名',
					columnWidth: .25,
					labelWidth: 80,
				    name: 'userAccount'
				},{
					fieldLabel: '操作日期',
			        columnWidth: .5,
					labelWidth: 80,
			        xtype: 'rangedatefield',
			        //起始日期组件的name属性。
			        fromName: 'createStartTime',
			        //终止日期组件的name属性。
			        toName: 'createEndTime',
			        //初始化查询开始时间
			        fromValue: new Date(),
			        //初始化查询结束时间
				    toValue: new Date()
				}],
				buttons: ['->',{
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
			me.store = Ext.create('MyApp.store.OrderLogStore',{
				autoLoad: true,
				listeners: {
					beforeload : function(store, operation, eOpts) {
						var queryForm = me.getQueryForm();//,
							//gameName = queryForm.getForm().findField('gameName');
						if (queryForm != null) {
							var values = queryForm.getValues();
							Ext.apply(operation, {
								params: {
									'startTime': values.createStartTime,
									'endTime': values.createEndTime,
									//参数欠缺
									'logType': values.orderLogOperations,
									'userAccount': values.userAccount,
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
                	dataIndex: 'logType',
					text: '操作类型',
					flex: 0.7,
					align: 'center',
					renderer: function(value){
						return DataDictionary.rendererSubmitToDisplay(value,'orderLogOperations');
					}
                },{
                    dataIndex: 'intUserType',
                    sortable: false,
                    width: 105,
                    xtype: 'ellipsiscolumn',
                    align: 'center',
                    text: '用户类型',
                    renderer: function(value){
                        if (value != '') {
                            return DataDictionary.rendererSubmitToDisplay(value,'userType2');
                        }
					}
                },{
                    dataIndex: 'userAccount',
                    sortable: false,
                    flex: 0.8,
                    xtype: 'ellipsiscolumn',
                    align: 'center',
                    text: '用户名'
                },{
					dataIndex: 'orderId',
					text: '订单号',
					width: 135,
					align: 'center'
				},{
					xtype: 'linebreakcolumn',
                	dataIndex: 'remark',
					text: '操作内容',
					flex: 3.3
                },{
					dataIndex: 'createTime',
					text: '时间',
					width: 150,
					xtype: 'datecolumn',
					format:'Y-m-d H:i:s',
					align: 'center'
                },{
                	dataIndex: 'ip',
					text: 'IP地址',
					flex: 1.0,
					align: 'center'
				}],
				dockedItems: [me.getPagingToolbar()],
				selModel: Ext.create('Ext.selection.CheckboxModel', {
					allowDeselect: true,
					mode: 'SINGLE'
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