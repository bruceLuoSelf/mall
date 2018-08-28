/**
 *  * Update History
 * Date        Name                Reason for change
 * ----------  ----------------    ----------------------
 * 2017/08/16 wangmin				ZW_C_JB_00021 商城资金改造
 *
 */


Ext.define('MyApp.view.sevenBaoBangdingLogManager', {
	extend: 'Ext.panel.Panel',
	id:'sevenBaoBangdingLogManager',
	closable: true,
    title: '7bao绑定日志管理',
    autoScroll: false,
	queryForm: null,
	listeners:{
		'resize':function(){
			this.logGrid.setHeight(window.document.body.offsetHeight-257);
		}
	},
	getQueryForm: function(){
		var me = this;
		if(me.queryForm==null){
			me.queryForm = Ext.widget('form',{
				region: 'north',
				layout: 'column',
				defaults: {
					margin: '10 10 10 10',
					xtype: 'textfield'
				},
				items: [DataDictionary.getDataDictionaryCombo('userType',{
					fieldLabel: '用户类型',
					name: 'userType',
					columnWidth: .25,
					labelWidth: 80,
					editable: false
				}),{
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
			        fromName: 'startTime',
			        //终止日期组件的name属性。
			        toName: 'endTime',
			        //初始化查询开始时间
			        fromValue: new Date(),
			        //初始化查询结束时间
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
			me.store = Ext.create('MyApp.store.sevenBaoLogStore',{
				autoLoad: true,
				listeners: {
					beforeload : function(store, operation, eOpts) {
						var queryForm = me.getQueryForm();//,
						if (queryForm != null) {
							var values = queryForm.getValues();
							Ext.apply(operation, {
								params: {
									'startTime': values.startTime,
									'endTime': values.endTime,
									'userAccount': values.userAccount,
									'userType':values.userType
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
                            return DataDictionary.rendererSubmitToDisplay(value,'userType');
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
					flex: 3.0
                },{
					dataIndex: 'updateTime',
					text: '操作日期',
					width: 200,
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