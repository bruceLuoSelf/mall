/*
 * 日志管理页面
 */
Ext.define('MyApp.view.logManager', {
    extend: 'Ext.panel.Panel',
    id: 'logManager',
    closable: true,
    title: '日志监控',
    ////////////////////////////////////////////////////
    autoScroll: false,
	listeners:{  
    'resize':function(){
        this.logGrid.setHeight(window.document.body.offsetHeight-233);
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
                items: [DataDictionary.getDataDictionaryCombo('module',{
					fieldLabel: '操作模块',
					name: 'module',
					columnWidth: .25,
					labelWidth: 80,
					editable: false
				},{value: null,display:'全部'}),
				DataDictionary.getDataDictionaryCombo('userType',{
					fieldLabel: '用户类型',
					name: 'currentUserType',
					columnWidth: .25,
					labelWidth: 80,
					editable: false,
					listeners: {
				    	'change': function(field, newValue, oldValue, eOpts ){
							var form = me.getQueryForm().getForm(),
								currentUserId = form.findField('currentUserId'),
								currentUserAccount = form.findField('currentUserAccount');
							if(newValue==4){
								currentUserAccount.setDisabled(false);
								currentUserId.setDisabled(true);
							}else{
								currentUserAccount.setDisabled(true);
								currentUserId.setDisabled(false);
							}
						}
					}
				},[{value: 4,display:'5173注册用户'},{value: null,display:'全部'}]),{
                	xtype: 'commonuserselector',
				    fieldLabel: '后台用户',
				    columnWidth: .25,
				    disabled: true,
					labelWidth: 80,
				    name: 'currentUserId'
				},{
				    fieldLabel: '5173用户',
				    columnWidth: .25,
				    disabled: true,
					labelWidth: 80,
				    name: 'currentUserAccount'
				},{
				    fieldLabel: '操作线程ID',
				    columnWidth: .25,
					labelWidth: 80,
				    name: 'currentThreadId'
				},{
				    fieldLabel: '操作内容',
				    columnWidth: .25,
					labelWidth: 80,
				    name: 'operateInfo'
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
			me.store = Ext.create('MyApp.store.LogStore',{
				autoLoad: true,
				listeners: {
					beforeload : function(store, operation, eOpts) {
						var queryForm = me.getQueryForm(),
							gameName = queryForm.getForm().findField('gameName');
						if (queryForm != null) {
							var values = queryForm.getValues();
							Ext.apply(operation, {
								params: {
									'createStartTime': values.createStartTime,
									'createEndTime': values.createEndTime,
									'logInfo.module': values.module,
									'logInfo.currentThreadId': values.currentThreadId,
									'logInfo.currentUserId': values.currentUserId,
									'logInfo.currentUserAccount': values.currentUserAccount,
									'logInfo.currentUserType': values.currentUserType,
									'logInfo.operateInfo': Ext.String.trim(values.operateInfo)
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
                    dataIndex: 'currentThreadId',
                    sortable: false,
                    flex: 1.4,
                    xtype: 'ellipsiscolumn',
                    align: 'center',
                    text: '操作线程ID'
                },{
                    dataIndex: 'currentUserAccount',
                    sortable: false,
                    flex: 1.3,
                    xtype: 'ellipsiscolumn',
                    align: 'center',
                    text: '操作用户帐号'
                },{
					dataIndex: 'currentUserType',
					text: '用户类型',
					flex: 0.7,
					align: 'center',
					renderer: function(value){
						if(value==4){
							return "5173注册用户";
						}
						return DataDictionary.rendererSubmitToDisplay(value,'userType');
					}
				},{
                    dataIndex: 'module',
                    sortable: false,
                    flex: 0.6,
                    align: 'center',
                    text: '操作模块',
                    renderer: function(value){
						return DataDictionary.rendererSubmitToDisplay(value,'module');
					}
                },{
                	xtype: 'linebreakcolumn',
                    dataIndex: 'operateInfo',
                    sortable: false,
                    flex: 3,
                    text: '操作内容'
                },{
					xtype: 'datecolumn',
					format:'Y-m-d H:i:s',
					dataIndex: 'createTime',
					align: 'center',
					text: '创建时间',
					flex: 1.5
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