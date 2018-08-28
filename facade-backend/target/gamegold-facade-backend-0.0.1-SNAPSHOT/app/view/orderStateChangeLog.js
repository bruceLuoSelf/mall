Ext.define('MyApp.view.orderStateChangeLog', {
	extend: 'Ext.panel.Panel',
	id:'orderStateChangeLog',
	closable: true,
    title: '订单同步日志',
    autoScroll: false,
	listeners:{  
    'resize':function(){
        this.logGrid.setHeight(window.document.body.offsetHeight-191);
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
				items: [{
					fieldLabel: '订单号',
					columnWidth: .25,
					labelWidth: 80,
				    name: 'orderId'
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
				},DataDictionary.getDataDictionaryCombo('syncState',{
					fieldLabel: '同步状态',
					name: 'syncState',
					columnWidth: .25,
					labelWidth: 80,
					editable: false
				},{value: null,display:'全部'})],
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
									'syncState': values.syncState,
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
					dataIndex: 'orderId',
					text: '订单号',
					width: 135,
					align: 'center'
				},{
					dataIndex: 'createTime',
					text: '同步时间',
					width: 150,
					xtype: 'datecolumn',
					format:'Y-m-d H:i:s',
					align: 'center'
                },{
					dataIndex: 'syncState',
					text: '同步状态',
					width: 135,
					align: 'center'
				},{
					dataIndex: 'syncState',
					text: '游戏',
					width: 135,
					align: 'center'
				},{
					dataIndex: 'syncState',
					text: '大区',
					width: 135,
					align: 'center'
				},{
					dataIndex: 'syncState',
					text: '服务器',
					width: 135,
					align: 'center'
				},{
					dataIndex: 'syncState',
					text: '数量',
					width: 135,
					align: 'center'
				},{
					dataIndex: 'syncState',
					text: '金额',
					width: 135,
					align: 'center'
				},{
					dataIndex: 'syncState',
					text: '单价',
					width: 135,
					align: 'center'
				},{
					dataIndex: 'syncState',
					text: '订单状态',
					width: 135,
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