Ext.define('MyApp.view.repositoryLogManager', {
	extend: 'Ext.panel.Panel',
	id:'repositoryLogManager',
	closable: true,
    title: '库存日志',
    autoScroll: false,
	listeners:{
    'resize':function(){
        this.logGrid.setHeight(window.document.body.offsetHeight-260);
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
				items: [/*DataDictionary.getDataDictionaryCombo('repositoryLogOperations',{
					fieldLabel: '操作类型',
					name: 'repositoryLogOperations',
					columnWidth: .25,
					labelWidth: 80,
					editable: false
				},{value: null,display:'全部'}),*/{
					fieldLabel: '用户名',
					columnWidth: .25,
					labelWidth: 80,
				    name: 'userAccount'
				},{
					name: 'sellerAccount',
					columnWidth: .25,
					labelWidth: 100,
                    fieldLabel: '卖家5173账号'
                },{
                	name: 'gameAccount',
                	columnWidth: .25,
					labelWidth: 80,
                    fieldLabel: '游戏账号'
                },{
                	name: 'sellerGameRole',
                	columnWidth: .25,
					labelWidth: 110,
                    fieldLabel: '卖家游戏角色名'
                },{
					fieldLabel: '操作日期',
			        columnWidth: .5,
					labelWidth: 80,
			        xtype: 'rangedatefield',
			        fromName: 'createStartTime',
			        toName: 'createEndTime',
			        fromValue: new Date(),
				    toValue: new Date()
				},{
					xtype: 'gamelinkselector',
					itemId : 'MyApp_view_goods_gamelink_ID',
					columnWidth: .5,
					allowBlank: true,
					fieldLabel: '游戏属性'
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
			me.store = Ext.create('MyApp.store.RepositoryLogStore',{
				autoLoad: true,
				listeners: {
					beforeload : function(store, operation, eOpts) {
						var queryForm = me.getQueryForm();
						var	gameName = queryForm.getForm().findField('gameName'),
							region = "",
							server = "";
						if (queryForm != null) {
							var values = queryForm.getValues();
							gameName = Ext.String.trim(gameName.getRawValue());
							region = Ext.String.trim(values.region);
							server =  Ext.String.trim(values.server);
							Ext.apply(operation, {
								params: {
									'startTime': values.createStartTime,
									'endTime': values.createEndTime,
									'logType': values.repositoryLogOperations,
									'userAccount': values.userAccount,
									'gameName': gameName,
									'region': region,
									'server': server,
									'gameRace': Ext.String.trim(values.gameRace),
									'sellerAccount':values.sellerAccount,//卖家5173账号
								    'gameAccount':values.gameAccount,//游戏账号
								    'sellerGameRole':values.sellerGameRole//卖家游戏角色名
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
				},/*{
                	dataIndex: 'logType',
					text: '操作类型',
					width: 100,
					align: 'center',
					renderer: function(value){
						return DataDictionary.rendererSubmitToDisplay(value,'repositoryLogOperations');
					}
                },{
                    dataIndex: 'intUserType',
                    sortable: false,
                    width: 105,
                    align: 'center',
                    text: '用户类型',
                    renderer: function(value){
						if(value==null || value==""){
							return "";
						}
						return DataDictionary.rendererSubmitToDisplay(value,'userType2');
					}
                },*/{
                    dataIndex: 'userAccount',
                    sortable: false,
                    width: 100,
                    xtype: 'ellipsiscolumn',
                    align: 'center',
                    text: '操作员'
                },{
                	dataIndex: 'sellerAccount',
                    sortable: false,
                    width: 100,
                    xtype: 'ellipsiscolumn',
                    align: 'center',
                    text: '卖家5173账号'
                },{
                	dataIndex: 'gameAccount',
                    sortable: false,
                    width: 100,
                    xtype: 'ellipsiscolumn',
                    align: 'center',
                    text: '游戏账号'
                },{
                	dataIndex: 'sellerGameRole',
                    sortable: false,
                    width: 120,
                    xtype: 'ellipsiscolumn',
                    align: 'center',
                    text: '卖家游戏角色名'
                },{
                	dataIndex: 'gameName',
                    sortable: false,
                    width: 110,
                    xtype: 'ellipsiscolumn',
                    align: 'center',
                    text: '游戏名'
                },{
                	dataIndex: 'region',
                    sortable: false,
                    width: 120,
                    xtype: 'ellipsiscolumn',
                    align: 'center',
                    text: '大区'
                },{
                	dataIndex: 'server',
                    sortable: false,
                    width: 100,
                    xtype: 'ellipsiscolumn',
                    align: 'center',
                    text: '服务器'
                },{
                	dataIndex: 'gameRace',
                    sortable: false,
                    width: 70,
                    xtype: 'ellipsiscolumn',
                    align: 'center',
                    text: '阵营'
                },{
					xtype: 'linebreakcolumn',
                	dataIndex: 'remark',
					text: '操作内容',
					width: 250
                },{
					dataIndex: 'createTime',
					text: '时间',
					width: 150,
					xtype: 'datecolumn',
					format:'Y-m-d H:i:s',
					align: 'center',
                },{
                	dataIndex: 'ip',
					text: 'IP地址',
					width: 120,
					align: 'center',
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