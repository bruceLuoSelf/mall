/**
 *  * Update History
 * Date        Name                Reason for change
 * ----------  ----------------    ----------------------
 * 2017/07/17  wurongfan           ZW_J_JB_00072金币商城对外接口优化
 *
 */


Ext.define('MyApp.view.FirmLogManager', {
	extend: 'Ext.panel.Panel',
	id:'FirmLogManager',
	closable: true,
    title: '工作室操作日志',
    autoScroll: false,
	queryForm: null,
	listeners:{
		'resize':function(){
			this.logGrid.setHeight(window.document.body.offsetHeight-233);
		}
	},
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
			me.store = Ext.create('MyApp.store.FirmLogStore',{
				autoLoad: true,
				listeners: {
					beforeload : function(store, operation, eOpts) {
						var queryForm = me.getQueryForm();//,
						if (queryForm != null) {
							var values = queryForm.getValues();
							Ext.apply(operation, {
								params: {
									'createStartTime': values.createStartTime,
									'createEndTime': values.createEndTime
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
                    dataIndex: 'userType',
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
					xtype: 'linebreakcolumn',
                	dataIndex: 'log',
					text: '日志记录',
					flex: 3.3
                },{
					dataIndex: 'updateTime',
					text: '时间',
					width: 150,
					xtype: 'datecolumn',
					format:'Y-m-d H:i:s',
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