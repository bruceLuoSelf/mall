


Ext.define('MyApp.view.goodsLogManager', {
	extend: 'Ext.panel.Panel',
	id:'goodsLogManager',
	closable: true,
    title: '商品日志',
    autoScroll: false,
	listeners:{  
    'resize':function(){
        this.logGrid.setHeight(window.document.body.offsetHeight-257);
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
				items: [DataDictionary.getDataDictionaryCombo('goodsLogOperations',{
					fieldLabel: '操作类型',
					name: 'goodsLogOperations',
					columnWidth: .25,
					labelWidth: 80,
					editable: false
				},{value: null,display:'全部'}),{
					fieldLabel: '用户名',
					columnWidth: .25,
					labelWidth: 80,
				    name: 'userAccount'
				},{
					fieldLabel: '商品名称',
					columnWidth: .25,
					labelWidth: 80,
				    name: 'title'
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
			me.store = Ext.create('MyApp.store.GoodsLogStore',{
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
									'logType': values.goodsLogOperations,
									'gameName': gameName,
									'userAccount':values.userAccount,
									'region': region,
									'server': server,
									'gameRace': Ext.String.trim(values.gameRace),
									'title':values.title
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
					width: 100,
					align: 'center',
					renderer: function(value){
						return DataDictionary.rendererSubmitToDisplay(value,'goodsLogOperations');
					}
                },{
                    dataIndex: 'intUserType',
                    sortable: false,
                    width: 105,
                    //xtype: 'ellipsiscolumn',
                    align: 'center',
                    text: '用户类型',
                    renderer: function(value){
						if(value==null || value==""){
							return "";
						}
						return DataDictionary.rendererSubmitToDisplay(value,'userType2');
					}
                },{
                    dataIndex: 'userAccount',
                    sortable: false,
                    width: 100,
                    xtype: 'ellipsiscolumn',
                    align: 'center',
                    text: '用户名'
                },{
                	dataIndex: 'title',
                    sortable: false,
                    width: 110,
                    xtype: 'ellipsiscolumn',
                    align: 'center',
                    text: '商品名称'
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