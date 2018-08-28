Ext.define('MyApp.view.addAndEditeWindow', {
	extend : 'Ext.window.Window',
	title : '新增/修改保险功能设置',
	width : 700,
	closeAction : 'hide',
	modal : true,
	form : null,
	getForm : function() {
		var me = this;
		if (me.form == null) {
			me.form = Ext.widget('form', {
				layout : 'column',
				defaults : {
					margin : '5 5 5 5',
					labelWidth : 120,
					readOnly : false,
					allowBlank : true,
					xtype : 'textfield'
				},
				items : [{
					xtype: 'hiddenfield',
					name: 'id'
				},Ext.create('Ext.form.ComboBox', {
							fieldLabel: '游戏名称',
							id: 'gameName',
							columnWidth: 0.5,
							editable : false,
							store: Ext.create(
									'MyApp.store.MallGameNameIdStore', {
										autoLoad : true
								}),
							displayField: 'name',
							valueField: 'id'
				}),  {
					fieldLabel: '保障收费比例(%)',
					xtype: 'numberfield', 
					allowBlank: false,
					columnWidth: 0.5,
					emptyText: '例如：5',
					name: 'rate'
				},{
					fieldLabel : '服务费下限(元)',
					allowBlank : false,
					xtype: 'numberfield', 
					columnWidth : 0.5,
					emptyText: '例如：1',
					name : 'floor'
				},{
					fieldLabel : '服务费上限(元)',
					allowBlank : false,
					columnWidth : 0.5,
					xtype: 'numberfield', 
					emptyText: '例如：20',
					name : 'ceiling'
				},{
					fieldLabel : '保障有效期（天）',
					allowBlank : false,
					columnWidth : 0.5,
					xtype: 'numberfield', 
					allowDecimals: false,
					emptyText: '例如：15',
					name : 'expireDay'
				},{
                    xtype : 'checkbox',
                    boxLabel : '开启商品保障险',
                    columnWidth : 0.5,
                    name: 'enabled',
                    id:'enabled',
                    renderer: function(value){
                        if(value){
                            Ext.getCmp('enabled').setValue(true);
                            return;
                        }
                        Ext.getCmp('enabled').setValue(false);
                        return;
                    }

                }],
				buttons : [{
					text : '保存',
					handler : function() {
						var form = me.getForm().getForm(), 
							url = './insurance/add.action', 
							message = '新增';
						if (form.isValid()) {
							var record = {
									"gameName" : form.findField('gameName').getRawValue(),
									"rate" : form.findField('rate').getValue(),
									"enabled" : form.findField('enabled').getValue(),
									"floor" : form.findField('floor').getValue(),
									"ceiling" : form.findField('ceiling').getValue(),
									"expireDay" : form.findField('expireDay').getValue()
								};
							console.log(record);
							if (me.isUpdate) {
								url = './insurance/update.action';
								message = '修改';
								record = {
										"id":form.findField('id').getValue(),
                                        "gameName" : form.findField('gameName').getRawValue(),
                                        "rate" : form.findField('rate').getValue(),
    									"enabled" : form.findField('enabled').getValue(),
    									"floor" : form.findField('floor').getValue(),
    									"ceiling" : form.findField('ceiling').getValue(),
    									"expireDay" : form.findField('expireDay').getValue()
									};
							}
							Ext.Ajax.request({
								url : url,
								method : 'POST',
								jsonData : {
									'setting' : record
								},
								success : function(response, opts) {
									var insuranceSettingsManager = Ext
											.getCmp('insuranceSettingsManager'), store = insuranceSettingsManager
											.getStore();
									var selgrid = insuranceSettingsManager.getAddAndEditeGrid(), selModel = selgrid.getSelectionModel();
									selModel.deselectAll();
									Ext.ux.Toast.msg("温馨提示", message + "成功");
									me.close();
									store.load();
								},
								exception : function(response, opts) {
									var json = Ext
											.decode(response.responseText);
									Ext.ux.Toast.msg("温馨提示", json.message);
								}
							});
						}
					}
				}]
			});
		}
		return me.form;
	},
	isUpdate : null,
	bindData : function(record, isUpdate) {
		
		var me = this, form = me.getForm().getForm();
		form.reset();
		
		me.isUpdate = isUpdate;
		if (isUpdate) {
			form.loadRecord(record);
		}
	},
	constructor : function(config) {
		var me = this, cfg = Ext.apply({}, config);
		me.items = [me.getForm()]
		me.callParent([cfg]);
	}
});

Ext.define('MyApp.view.addAndEditeGrid', {
	extend : 'Ext.grid.Panel',
	id: 'addAndEditeGrid',
	header : false,
	autoScroll : true,
	columnLines : true,
	constructor : function(config) {
		var me = this, cfg = Ext.apply({}, config);
		me.columns = [{
			xtype : 'rownumberer'
		}, {
			dataIndex : 'gameName',
			sortable : false,
			width : 160,
			align : 'center',
			text : '游戏'
		}, {
			dataIndex : 'enabled',
			sortable : false,
			width : 250,
			align : 'center',
			text : '商品保障险',
			renderer: function(value){
		        if (value == false) {
		            return '已关闭';
		        }
		        return '已开启';
		    }
		}, {
			dataIndex : 'rate',
			sortable : false,
			width : 150,
			align : 'center',
			text : '保障收费比例(%)'
		}, {
			dataIndex : 'floor',
			sortable : false,
			width : 150,
			align : 'center',
			text : '服务费下限(元)'
		},{
			dataIndex : 'ceiling',
			sortable : false,
			width : 150,
			align : 'center',
			text : '服务费上限(元)'
		},{
			dataIndex : 'expireDay',
			sortable : false,
			width : 150,
			align : 'center',
			text : '保障有效期(天)'
		}];
		me.selModel = Ext.create('Ext.selection.CheckboxModel', {
					allowDeselect : true
				})
		me.callParent([cfg]);
	}
});

/*
 * 保险配置管理页面
 */
Ext.define('MyApp.view.insuranceSettingsManager', {
	extend : 'Ext.panel.Panel',
	id : 'insuranceSettingsManager',
	closable : true,
	title : '保险设置管理',
	autoScroll : false,
	listeners : {
		'resize' : function() {
			this.addAndEditeGrid.setHeight(window.document.body.offsetHeight - 172);
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
											fn : me.addSettings,
											scope : me
										}
									}
								}, '-', {
									text : '修改',
									listeners : {
										click : {
											fn : me.modifySettings,
											scope : me
										}
									}
								}, '-', {
									text : '删除',
									listeners : {
										click : {
											fn : me.deleteSettings,
											scope : me
										}
									}
								}]
					});
		}
		return me.toolbar;
	},
	addAndEditeWindow : null,
	getAddAndEditeWindow : function() {
		if (this.addAndEditeWindow == null) {
			this.addAndEditeWindow = new MyApp.view.addAndEditeWindow();
		}
		return this.addAndEditeWindow;
	},
	// 新增游戏下拉数值信息
	addSettings: function(button, e, eOpts) {
		var window = this.getAddAndEditeWindow();
		window.bindData(Ext.create('MyApp.model.InsuranceSettingsModel'), false);
		window.getForm().getForm().findField('gameName').readOnly=false;
		window.show();
	},
	// 修改游戏下拉数值信息
	modifySettings : function(button, e, eOpts) {
		var grid = this.getAddAndEditeGrid(), selModel = grid.getSelectionModel(), selRecords = selModel
				.getSelection(), window;
		window = this.getAddAndEditeWindow();
		if (selRecords == null || selRecords.length <= 0) {
			Ext.ux.Toast.msg("温馨提示", "请先选择要修改的信息");
			return;
		}
		if (selRecords.length > 1) {
			Ext.ux.Toast.msg("温馨提示", "请选择 <b style='color:red'>1</b> 条您要修改的商品信息！");
			return;
		}
		console.log(selRecords[0]);
		window.bindData(selRecords[0], true);
		window.getForm().getForm().findField('gameName').readOnly=true;
		window.show();
	},
	deleteSettings : function(button, e, eOpts) {
		var grid = this.getAddAndEditeGrid(), selModel = grid.getSelectionModel(), selRecords = selModel
				.getSelection();
		if (selRecords == null || selRecords.length <= 0) {
			Ext.ux.Toast.msg("温馨提示", "请先选择要删除的信息");
			return;
		}
		id = selRecords[0].get('id');
		Ext.MessageBox.confirm('温馨提示', '确定删除该信息吗？', function(btn) {
					if (btn == 'yes') {
						Ext.Ajax.request({
									url : './insurance/delete.action',
									params : {
										'id' : id
									},
									success : function(response, opts) {
										Ext.ux.Toast.msg("温馨提示", "删除成功！");
										grid.getStore().load();
										grid.getSelectionModel().deselectAll();
									},
									exception : function(response, opts) {
										var json = Ext
												.decode(response.responseText);
										Ext.ux.Toast.msg("温馨提示", json.message);
									}
								});
					} else {
						return;
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
						id : 'gameNameComboBox',
						editable : false,
						store : Ext.create('MyApp.store.MallGameNameIdStore', {
									autoLoad : true,
									listeners : {
										'load' : function(store) {
											var allArea = Ext.create(
															'MyApp.model.MallGameNameIdModel',
															{
																'id' : null,
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
	store : null,
	getStore : function() {
		var me = this;
		if (me.store == null) {
			me.store = Ext.create('MyApp.store.InsuranceSettingsStore', {
						autoLoad : true,
						listeners : {
							beforeload : function(store, operation, eOpts) {
								var queryForm = me.getQueryForm().getForm();
								if (queryForm != null) {
                                    var gameName = queryForm.findField("gameNameComboBox").getRawValue();
                                    if (gameName == '全部') gameName='';
									Ext.apply(operation, {
												params : {
													'gameName' : gameName
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
	addAndEditeGrid : null,
	getAddAndEditeGrid : function() {
		var me = this;
		if (Ext.isEmpty(me.addAndEditeGrid)) {
			me.addAndEditeGrid = Ext.create('MyApp.view.addAndEditeGrid', {
						store : me.getStore(),
						dockedItems : [me.getToolbar(), me.getPagingToolbar()]
					});
		}
		return me.addAndEditeGrid;
	},
	initComponent : function() {
		var me = this;
		Ext.applyIf(me, {
					items : [me.getQueryForm(), me.getAddAndEditeGrid()]
				});
		me.callParent(arguments);
	}
});