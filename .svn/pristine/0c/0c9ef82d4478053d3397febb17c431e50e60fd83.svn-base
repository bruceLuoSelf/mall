/*
 * 短信配置管理页面
 */
Ext.define('MyApp.view.textMessageManager', {
	extend : 'Ext.panel.Panel',
	id : 'textMessageManager',
	closable : true,
	title : '防骗短信配置管理',
	autoScroll : false,
	listeners : {
		'resize' : function() {
			this.textMessageGrid.setHeight(window.document.body.offsetHeight - 172);
		}
	},
	toolbar : null,
	getToolbar : function() {
		var me = this;
		if (Ext.isEmpty(me.toolbar)) {
			me.toolbar = Ext.widget('toolbar', {
						dock : 'top',
						items : [{
									text : '新增',
									listeners : {
										click : {
											fn : me.addTextMessage,
											scope : me
										}
									}
								}, '-', {
									text : '修改',
									listeners : {
										click : {
											fn : me.modifyTextMessage,
											scope : me
										}
									}
								}, '-', {
									text : '删除',
									listeners : {
										click : {
											fn : me.deleteTextMessage,
											scope : me
										}
									}
								}, '-', {
									text : '挂起',
									id:'bt_disable',
									listeners : {
										click : {
											fn : me.turnOffTextMessage,
											scope : me
										}
									}
								},  '-', {
									text : '解挂',
									id:'bt_enable',
									listeners : {
										click : {
											fn : me.turnOnTextMessage,
											scope : me
										}
									}
								}, '->',{
									text : '查找',
									handler: function() {
										me.getPagingToolbar().moveFirst();
									}
								}]
					});
		}
		return me.toolbar;
	},
	// 新增短信规则按钮
	addTextMessage : function(button, e, eOpts) {
		var window = Ext.getCmp('fuckwindow');
		if (window == null){
			var window = Ext.create('MyApp.view.textMessageDetialWindow');
		}
		window.bindData(null, true);
		window.show();
		window.getPanel().hide();
	},
	// 修改短信规则按钮
	modifyTextMessage : function(button, e, eOpts) {
		var grid = this.getTextMessageGrid(), selModel = grid.getSelectionModel(), 
			selRecords = selModel.getSelection();
		var window = Ext.getCmp('fuckwindow');;
		if (window == null){
			window = Ext.create('MyApp.view.textMessageDetialWindow');
		}
		if (selRecords == null || selRecords.length <= 0) {
			Ext.ux.Toast.msg("温馨提示", "请先选择要修改的信息");
			return;
		}
		if (selRecords.length > 1) {
			Ext.ux.Toast.msg("温馨提示", "请选择 <b style='color:red'>1</b> 条您要修改的信息！");
			return;
		}
		window.bindData(selRecords[0], true);
		window.getPanel().show();
		window.show();
	},
	// 删除短信规则按钮
	deleteTextMessage : function(button, e, eOpts) {
		var grid = this.getTextMessageGrid(), selModel = grid.getSelectionModel(), 
			selRecords = selModel.getSelection(), textMessageId;
		if (selRecords == null || selRecords.length != 1) {
			Ext.ux.Toast.msg("温馨提示", "请先选择一条要删除的信息");
			return;
		}
		textMessageId=selRecords[0].get('id');
		Ext.MessageBox.confirm('温馨提示', '确定删除该信息吗？', function(btn) {
					if (btn == 'yes') {
						Ext.Ajax.request({
									url : './messageRule/delete.action',
									params : {
										'id' : textMessageId
									},
									success : function(response, opts) {
										Ext.ux.Toast.msg("温馨提示", "删除成功！");
										grid.getStore().load();
										grid.getSelectionModel().deselectAll();
									},
									exception : function(response, opts) {
										var json = Ext.decode(response.responseText);
										Ext.ux.Toast.msg("温馨提示", json.message);
									}
								});
					} else {
						return;
					}
				});
	},
	//挂起按钮
	turnOffTextMessage:function(){
		var grid = this.getTextMessageGrid(), 
			selModel = grid.getSelectionModel(), 
			selRecords = selModel.getSelection(), 
			textMessageId;
		if (selRecords == null || selRecords.length != 1) {
			Ext.ux.Toast.msg("温馨提示", "请先选择一条要挂起的信息");
			return;
		}
		textMessageId=selRecords[0].get('id');
		Ext.Ajax.request({
			url : './messageRule/disabled.action',
			params : {
				'id' : textMessageId
			},
			success : function(response, opts) {
				Ext.ux.Toast.msg("温馨提示", "挂起成功！");
				grid.getStore().load();
				grid.getSelectionModel().deselectAll();
			},
			exception : function(response, opts) {
				var json = Ext.decode(response.responseText);
				Ext.ux.Toast.msg("温馨提示", json.message);
			}
		});
	},
	//解挂按钮
	turnOnTextMessage:function(){
		var grid = this.getTextMessageGrid(), 
		selModel = grid.getSelectionModel(), 
		selRecords = selModel.getSelection(), 
		textMessageId;
		if (selRecords == null || selRecords.length != 1) {
			Ext.ux.Toast.msg("温馨提示", "请先选择一条要解挂的信息");
			return;
		}
		textMessageId=selRecords[0].get('id');
		Ext.Ajax.request({
			url : './messageRule/enabled.action',
			params : {
				'id' : textMessageId
			},
			success : function(response, opts) {
				Ext.ux.Toast.msg("温馨提示", "解挂成功！");
				grid.getStore().load();
				grid.getSelectionModel().deselectAll();
			},
			exception : function(response, opts) {
				var json = Ext.decode(response.responseText);
				Ext.ux.Toast.msg("温馨提示", json.message);
			}
		});
	},
	queryForm : null,
	getQueryForm : function() {
		var me = this;
		if (me.queryForm == null) {
			me.queryForm = Ext.widget('form', {
				layout : 'form',
				defaults : {
					margin : '10 10 10 10',
					labelWidth : 80
				},
				items : [{
					xtype : 'container',
					layout : 'column',
					defaults : {
						margin : '5 5 5 5',
						xtype : 'textfield'
					},
					items : [Ext.create('Ext.form.ComboBox', {
						fieldLabel : '游戏名称',
						id : 'gameNameCombox',
						store : Ext.create('MyApp.store.MallGameNameIdStore', {
									autoLoad : true,
									listeners : {
										'load' : function(store) {
											var allArea = Ext.create(
															'MyApp.model.MallGameNameIdModel',
															{
																'id' : '',
																'name' : '全部'
															});
											store.insert(0, allArea);
										}
									}
								}),
						listeners : {
							'select' : function() {
								me.store.reload();
							}
						},
						displayField : 'name',
						valueField : 'id'
					})]
				}]
			})
		}

		return this.queryForm;
	},
	textMessageGrid: null,
	getTextMessageGrid: function(){
		var me = this;
		if(Ext.isEmpty(me.textMessageGrid)){
			me.textMessageGrid = Ext.widget('gridpanel',{
				id: 'textMessageGrid',
				header: false,
				region:'center',
				columnLines: true,
			    store: me.getStore(),
			    columns: [{
					xtype : 'rownumberer'
				}, {
					dataIndex : 'gameName',
					sortable : false,
					width : 100,
					align : 'center',
					text : '游戏2'
				}, {
					dataIndex : 'name',
					name : 'ruleNameText',
					sortable : false,
					width : 100,
					align : 'center',
					text : '规则名称'
				}, {
			        dataIndex: 'orderStatus',
			        sortable: false,
			        flex: 1,
			        align: 'center',
			        renderer: function(value){
						return DataDictionary.rendererSubmitToDisplay(value,'orderState');
					},
			        text: '发送节点'
			    }, {
					dataIndex : 'delay',
					sortable : false,
					width : 60,
					align : 'center',
					text : '延时(秒)'
				},{
					dataIndex : 'period',
					sortable : false,
					width : 60,
					align : 'center',
					text : '周期(分)',
					renderer: function(value){
						return value/60;
					}
				},{
					dataIndex : 'content',
					sortable : false,
					width : 260,
					align : 'center',
					text : '短信内容'
				},{
					dataIndex : 'operator',
					sortable : false,
					width : 100,
					align : 'center',
					text : '操作员',
					renderer: function(value){
						if(value!=null && value.nickName!=null){
							return value.nickName;
						}
					}
				},{
					dataIndex : 'lastUpdateTime',
					sortable : false,
					width : 150,
					align : 'center',
					text : '操作日期',
					format:'Y-m-d H:i:s',
					xtype: 'datecolumn'
				},{
					dataIndex : 'enabled',
					sortable : false,
					width : 100,
					align : 'center',
					text : '挂起/解挂',
					renderer: function(value){
						if(value){
							return '解挂';
						}else{
							return '挂起';
						}
					}
				}],
			    dockedItems: [me.getToolbar(),me.getPagingToolbar()],
			    selModel: Ext.create('Ext.selection.CheckboxModel', {
			    	allowDeselect: true,
			        mode: 'SINGLE',
			        listeners: {
			        	selectionchange: {
			                fn: function(records , selected, eOpts){
			                	if(selected[0]==null){
			                		Ext.getCmp('bt_disable').enable();
			                		Ext.getCmp('bt_enable').enable();
			                	}else if(selected[0].get('enabled')){
			                		Ext.getCmp('bt_disable').enable();
			                		Ext.getCmp('bt_enable').disable();
			                	}else{
			                		Ext.getCmp('bt_disable').disable();
			                		Ext.getCmp('bt_enable').enable();
			                	}
			                }
			            }
			        }
			    })
			});
		}
		return me.textMessageGrid;
	},
	store : null,
	getStore : function() {
		var me = this;
		if (me.store == null) {
			me.store = Ext.create('MyApp.store.TextMessageStore', {
						autoLoad : true,
						listeners : {
							beforeload : function(store, operation, eOpts) {
								var idValue = Ext.getCmp('gameNameCombox').getRawValue();
								var queryForm = me.getQueryForm();
								if (queryForm != null) {
									if (idValue=='全部'){
										idValue = '';
									}
									Ext.apply(operation, {
												params : {
													'gameName' : idValue
												}
											});
								}
							}
						}
					});
		}
		
		return me.store;
	},
	pagingToolbar : null,
	getPagingToolbar : function() {
		var me = this;
		if (me.pagingToolbar == null) {
			me.pagingToolbar = Ext.widget('pagingtoolbar', {
						store : me.getStore(),
						dock : 'bottom',
						displayInfo : true
					});
		}
		return me.pagingToolbar;
	},
	initComponent : function() {
		var me = this;
		Ext.applyIf(me, {
					items : [me.getQueryForm(), me.getTextMessageGrid()]
				});
		me.callParent(arguments);
	}
});
//新增、修改总窗口
Ext.define('MyApp.view.textMessageDetialWindow',{
	extend: 'Ext.window.Window',
	width: 800,
	autoScroll: true,
	maxHeight: 1000,
    title: '短信规则详情',
	closeAction: 'hide',
	modal: true,
	id: 'fuckwindow',
	defaults: {
		margin: '5 5 5 5'
	},
	form: null,
	getForm: function(){
		var me = this;
		if(me.form==null){
			me.form = Ext.create('MyApp.view.textMessageDetialForm');
		}
		return me.form;
	},
	panel: null,
	getPanel: function(){
		var me = this;
		if(me.panel == null){
			me.panel = Ext.create('MyApp.view.logsPanel');
		}
		return me.panel;
	},
	bindData: function(record){
		var me = this,
		form = me.getForm().getForm(),
		operate = form.findField('operator'),
		period = form.findField('period'),
		panel = me.getPanel();
		
		if (record==null){
			form.reset();
		}else{
			form.reset();
			form.loadRecord(record);
			period.setValue(record.data.period+'');
			operate.setValue(record.data.operator.nickName);
			panel.loadData(record);
		}
	},
    initComponent: function() {
        var me = this;
        Ext.applyIf(me, {
            items: [me.getForm(),me.getPanel()]
        });
        me.callParent(arguments);
    }
});

/*
 * 短信规则信息管理页面
 */
Ext.define('MyApp.view.textMessageDetialForm',{
	extend: 'Ext.form.Panel',
    layout: 'column',
    //title: '短信规则信息',
    border: false,
	defaults: {
		margin: '2 2 2 2',
		columnWidth: .333,
		labelWidth: 80,
		xtype: 'textfield'
	},
	constructor : function(config) {
		var me = this, cfg = Ext.apply({}, config);
		me.items = [{
			name: 'name',
			fieldLabel: '规则名称'
		},Ext.create('Ext.form.ComboBox', {
			name:'gameName',
			fieldLabel : '游戏名称',
			store : Ext.create('MyApp.store.MallGameNameIdStore', {
						autoLoad : true
					}),
			displayField : 'name',
			valueField : 'name',
			editable: false
		}),DataDictionary.getDataDictionaryCombo('orderState',{
        	fieldLabel: '触发状态',
        	labelWidth: 100,
			name: 'orderStatus',
			editable: false
		}),{
			allowDecimals : false,
			name: 'delay',
			fieldLabel: '延时(秒/s)',
			xtype: 'numberfield',
			minValue: 0,
			allowDecimals: false
		}, DataDictionary.getDataDictionaryCombo('textMessageCycle',{
        	fieldLabel: '发送周期(分/min)',
        	labelWidth: 120,
			name: 'period',
			editable: false
		}),{
			name: 'operator',
			fieldLabel: '操作员',
			readOnly: true,
			hidden: true, 
			hideLabel:true
		},{
			name: 'content',
			columnWidth: .666,
			fieldLabel: '短信内容'
		},{
			name: 'id',
			fieldLabel: 'Id',
			hidden: true, 
			hideLabel:true
		}];
		me.buttons = [{
        	text:'保存',
        	handler: function() {
        		if (me.getValues().name==''){
        			Ext.ux.Toast.msg("温馨提示", "请输入短信规则名称。");
        			return;
        		}
        		if (me.getValues().gameName==''){
        			Ext.ux.Toast.msg("温馨提示", "请选择游戏名称名称。");
        			return;
        		}
        		if (me.getValues().orderStatus==''){
        			Ext.ux.Toast.msg("温馨提示", "请选择触发状态。");
        			return;
        		}
        		if (me.getValues().delay==''){
        			Ext.ux.Toast.msg("温馨提示", "请输入延迟时间。");
        			return;
        		}
        		if (me.getValues().period==''){
        			Ext.ux.Toast.msg("温馨提示", "请选择周期。");
        			return;
        		}
        		if (me.getValues().content==''){
        			Ext.ux.Toast.msg("温馨提示", "请输入短信规则内容。");
        			return;
        		}
        		
        		var id = me.getValues().id;
				if (id==null||id=='') {
					message = '新增';
					url = './messageRule/add.action';
					var record = {
							"name" : me.getValues().name,
							"gameName" : me.getValues().gameName,
							"orderStatus" : me.getValues().orderStatus,
							"delay" : me.getValues().delay,
							"period" : me.getValues().period,
							"content" : me.getValues().content
						};
				}else{
					url = './messageRule/update.action';
					message = '修改';
					record = {
							"id": me.getValues().id,
							"name" : me.getValues().name,
							"gameName" : me.getValues().gameName,
							"orderStatus" : me.getValues().orderStatus,
							"delay" : me.getValues().delay,
							"period" : me.getValues().period,
							"content" : me.getValues().content
						};
				}
				Ext.Ajax.request({
					url : url,
					method : 'POST',
					jsonData : {
						'rule' : record
					},
					success : function(response, opts) {
						var textMessageManager = Ext.getCmp('textMessageManager');
						var selModel = textMessageManager.getTextMessageGrid().getSelectionModel();
						selModel.deselectAll();
						Ext.ux.Toast.msg("温馨提示", message + "成功");
						Ext.getCmp('fuckwindow').close();
						textMessageManager.getStore().load();
					},
					exception : function(response, opts) {
						var json = Ext
								.decode(response.responseText);
						Ext.ux.Toast.msg("温馨提示", json.message);
					}
				});
          	}
        }];
		me.callParent([ cfg ]);
	}
});


Ext.define('MyApp.view.logsPanel',{
	extend: 'Ext.panel.Panel',
	title: '规则变更日志',
	defaults: {
		margin: '2 2 2 2'
	},
	loadData: function(textMessageRecord){
		var me = this,
			logGrid = me.getLogGrid(),
			selectModel = logGrid.getSelectionModel(),
			store = logGrid.getStore();
		logGrid.queryParam = {
			'ruleId': textMessageRecord.get('id')
		};
		store.loadPage(1);
	},
	logGrid : null,
	getLogGrid : function(){
		if(this.logGrid==null){
			this.logGrid = Ext.create('MyApp.order.LogGrid',{
				autoScroll: true,
				height : 213
			});
		}
		return this.logGrid;
	},
	constructor: function(config){
		var me = this,
			cfg = Ext.apply({}, config);
		me.items = [me.getLogGrid()];
		me.callParent([cfg]);
	}
});


//日志查询结果表格页面
Ext.define('MyApp.order.LogGrid',{
	extend: 'Ext.grid.Panel',
	store : null,
	bbar: null,
	queryParam: null,
	constructor: function(config){
		var me = this,
			cfg = Ext.apply({}, config);
		me.columns = [{
	        dataIndex: 'type',
	        align: 'center',
	        flex: 1,
	        text: '操作内容',
	        renderer: function(value){
	        	if (value == 'ADD'){
	        		return '添加';
	        	}else if(value == 'MODIFY'){
	        		return '修改';
	        	}else if(value == 'ENABLED'){
	        		return '解挂';
	        	}else if(value == 'DISABLED'){
	        		return '挂起';
	        	}
	        }
	    },{
	        dataIndex: 'operator',
	        align: 'center',
	        flex: 1,
	        text: '操作员',
	        renderer: function(value){
	        	if(value!=null && value.nickName!=null){
	        		return value.nickName;
	        	}
	        }
	    },{
	        dataIndex: 'createTime',
	        align: 'center',
	        flex: 1,
	        text: '操作时间',
	        xtype: 'datecolumn',
	        format:'Y-m-d H:i:s'
	    }];
		me.store = Ext.create('MyApp.store.TextMessageLogStore',{
			listeners: {
				beforeload : function(store, operation, eOpts) {
					Ext.apply(operation, {
						params : me.queryParam
					});
				}				
			}
		});
		me.callParent([cfg]);
	}
});