/*
 * 收货地址配置管理页面
 */
Ext.define('MyApp.view.shAddressManager', {
	extend : 'Ext.panel.Panel',
	layout: "border",
	id : 'shAddressManager',
	closable : true,
	title : '收货地址配置管理',
	autoScroll : false,
	listeners : {
		'resize' : function() {
			this.shAddressGrid.setHeight(window.document.body.offsetHeight - 172);
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
							fn : me.addshAddress,
							scope : me
						}
					}
				}, '-', {
					text : '修改',
					listeners : {
						click : {
							fn : me.modifyshAddress,
							scope : me
						}
					}
				}, '-', {
					text : '删除',
					listeners : {
						click : {
							fn : me.deleteshAddress,
							scope : me
						}
					}
				}, '-', {
					text : '禁用',
					id:'bt_disable',
					listeners : {
						click : {
							fn : me.disabledshAddress,
							scope : me
						}
					}
				},  '-', {
					text : '启用',
					id:'bt_enable',
					listeners : {
						click : {
							fn : me.enabledshAddress,
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
	// 新增按钮
	addshAddress : function(button, e, eOpts) {
		var window = Ext.getCmp('fuckwindow');
		if (window == null){
			var window = Ext.create('MyApp.view.shAddressWindow');
		}
		window.bindData(null, true);
		window.show();
	},
	// 修改按钮
	modifyshAddress : function(button, e, eOpts) {
		var grid = this.getshAddressGrid(),
			selModel = grid.getSelectionModel(),
			selRecords = selModel.getSelection();
		var window = Ext.getCmp('fuckwindow');;
		if (window == null){
			window = Ext.create('MyApp.view.shAddressWindow');
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
		window.show();
	},
	// 删除按钮
	deleteshAddress : function(button, e, eOpts) {
		var grid = this.getshAddressGrid(),
			selModel = grid.getSelectionModel(),
			selRecords = selModel.getSelection(),
			shAddressId;
		if (selRecords == null || selRecords.length != 1) {
			Ext.ux.Toast.msg("温馨提示", "请先选择一条要删除的信息");
			return;
		}
		shAddressId=selRecords[0].get('id');
		Ext.MessageBox.confirm('温馨提示', '确定删除该信息吗？', function(btn) {
			if (btn == 'yes') {
				Ext.Ajax.request({
					url : './delivery/deleteConfig.action',
					params : {
						'id' : shAddressId
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
	//禁用按钮
	disabledshAddress:function(){
		var grid = this.getshAddressGrid(),
			selModel = grid.getSelectionModel(),
			selRecords = selModel.getSelection(),
			shAddressId;
		if (selRecords == null || selRecords.length != 1) {
			Ext.ux.Toast.msg("温馨提示", "请先选择一条要禁用的信息");
			return;
		}
		shAddressId=selRecords[0].get('id');
		Ext.Ajax.request({
			url : './delivery/disabled.action',
			params : {
				'id' : shAddressId
			},
			success : function(response, opts) {
				Ext.ux.Toast.msg("温馨提示", "禁用成功！");
				grid.getStore().load();
				grid.getSelectionModel().deselectAll();
			},
			exception : function(response, opts) {
				var json = Ext.decode(response.responseText);
				Ext.ux.Toast.msg("温馨提示", json.message);
			}
		});
	},
	//启用按钮
	enabledshAddress:function(){
		var grid = this.getshAddressGrid(),
			selModel = grid.getSelectionModel(),
			selRecords = selModel.getSelection(),
			shAddressId;
		if (selRecords == null || selRecords.length != 1) {
			Ext.ux.Toast.msg("温馨提示", "请先选择一条要启用的信息");
			return;
		}
		shAddressId=selRecords[0].get('id');
		Ext.Ajax.request({
			url : './delivery/enabled.action',
			params : {
				'id' : shAddressId
			},
			success : function(response, opts) {
				Ext.ux.Toast.msg("温馨提示", "启用成功！");
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
						editable: false,
						id : 'gameNameCombox1',
						value:'全部',
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
	shAddressGrid: null,
	getshAddressGrid: function(){
		var me = this;
		if(Ext.isEmpty(me.shAddressGrid)){
			me.shAddressGrid = Ext.widget('gridpanel',{
				id: 'shAddressGrid',
				header: false,
				region:'center',
				columnLines: true,
				store: me.getStore(),
				columns: [{
					xtype : 'rownumberer'
				}, {
					dataIndex : 'gameName',
					sortable : false,
					width : 400,
					align : 'center',
					text : '游戏名称'
				} ,{
					dataIndex : 'tradeAddress',
					sortable : false,
					width : 700,
					align : 'center',
					text : '交易地址'
				},{
					dataIndex : 'enabled',
					sortable : false,
					width : 530,
					align : 'center',
					text : '禁用/启用',
					renderer: function(value){
						if(value){
							return '启用';
						}else{
							return '禁用';
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
		return me.shAddressGrid;
	},
	store : null,
	getStore : function() {
		var me = this;
		if (me.store == null) {
			me.store = Ext.create('MyApp.store.ShAddressStore', {
				autoLoad : true,
				listeners : {
					beforeload : function(store, operation, eOpts) {
						var idValue = Ext.getCmp('gameNameCombox1').getRawValue();
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
			items : [me.getQueryForm(), me.getshAddressGrid()]
		});
		me.callParent(arguments);
	}
});
//新增、修改总窗口
Ext.define('MyApp.view.shAddressWindow',{
	extend: 'Ext.window.Window',
	width: 800,
	autoScroll: true,
	maxHeight: 1000,
	title: '收货配置详情',
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
			me.form = Ext.create('MyApp.view.shAddressForm');
		}
		return me.form;
	},

	bindData: function(record){
		var me = this,
			form = me.getForm().getForm()
		if (record==null){
			form.reset();
		}else{
			form.reset();
			form.loadRecord(record);
		}
	},
	initComponent: function() {
		var me = this;
		Ext.applyIf(me, {
			items: [me.getForm()]
		});
		me.callParent(arguments);
	}
});

/*
 * 收货地址配置管理页面
 */
Ext.define('MyApp.view.shAddressForm',{
	extend: 'Ext.form.Panel',
	layout: 'column',
	//title: '信息',
	border: false,
	defaults: {
		margin: '2 2 2 2',
		columnWidth: .333,
		labelWidth: 80,
		xtype: 'textfield'
	},
	constructor : function(config) {
		var me = this,
			cfg = Ext.apply({}, config);
		me.items = [Ext.create('Ext.form.ComboBox', {
			name:'gameName',
			fieldLabel : '游戏名称',
			store : Ext.create('MyApp.store.MallGameNameIdStore', {
				autoLoad : true
			}),
			displayField : 'name',
			valueField : 'name',
			editable: false
		}),{
			name: 'tradeAddress',
			columnWidth: .666,
			fieldLabel: '交易地址'
		},{
			name: 'id',
			fieldLabel: 'Id',
			hidden: true,
			hideLabel:true
		}];
		me.buttons = [{
			text:'保存',
			handler: function() {
				if (me.getValues().gameName==''){
					Ext.ux.Toast.msg("温馨提示", "请选择游戏名称名称。");
					return;
				}
				if (me.getValues().tradeAddress==''){
					Ext.ux.Toast.msg("温馨提示", "请输入交易地址。");
					return;
				}
				var id = me.getValues().id;
				if (id==null||id=='') {
					message = '新增';
					url = './delivery/addConfig.action';
					var record = {
						"gameName" : me.getValues().gameName,
						"tradeAddress" : me.getValues().tradeAddress
					};
				}else{
					url = './delivery/updateConfig.action';
					message = '修改';
					record = {
						"id": me.getValues().id,
						"gameName" : me.getValues().gameName,
						"tradeAddress" : me.getValues().tradeAddress
					};
				}
				Ext.Ajax.request({
					url : url,
					method : 'POST',
					jsonData : {
						'config' : record
					},
					success : function(response, opts) {
						var shAddressManager = Ext.getCmp('shAddressManager');
						var selModel = shAddressManager.getshAddressGrid().getSelectionModel();
						selModel.deselectAll();
						Ext.ux.Toast.msg("温馨提示", message + "成功");
						Ext.getCmp('fuckwindow').close();
						shAddressManager.getStore().load();
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

