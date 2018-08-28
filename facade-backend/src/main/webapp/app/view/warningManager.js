/*
 * 库存信息
 *  * Date        Name                Reason for change
 * ----------  ----------------    ----------------------
 * 2017/05/15  liuchanghua           ZW_C_JB_00008 商城增加通货
 */
Ext.define('MyApp.view.WarningWindow', {
	extend: 'Ext.window.Window',
	title: '新增/修改友情提示配置',
	width: 600,
	closeAction: 'hide',
	modal: true,
	form: null,
	getForm: function(){
		var me = this;
		if(me.form==null){
			me.form = Ext.widget('form',{
				layout: 'column',
				defaults: {
					margin: '10 10 5 5',
					columnWidth: .5,
					labelWidth: 130,
					xtype: 'textfield'
				},
				items: [{
					xtype: 'gameselectorsellersetting',
					itemId : 'MyApp_view_goods_gamelink_ID',
					columnWidth: 1,
					gameChanged: function(ths, the, eOpts){
						var form = me.getForm().getForm();
						var categoryCombo = form.findField("goodsTypeName");
						categoryCombo.setRawValue(null);
						categoryCombo.getStore().load();
					},
					fieldLabel: '游戏属性'
				},Ext.create('Ext.form.ComboBox', {
					fieldLabel: '商品类型',
					itemId : 'MyApp_view_goods_type_ID',
					name: 'goodsTypeName',
					columnWidth: 0.5,
					store: Ext.create(
						'MyApp.store.ShGameConfigByGameNameStore', {
							autoLoad: true,
							listeners: {
								beforeload : function(store, operation, eOpts) {
									var form = me.getForm(),
										gameName = form.getForm().findField('gameName');
									if (form != null) {
										var values = form.getValues(),params={};
										params = {
											'shGameConfig.gameName': Ext.String.trim(gameName.getRawValue()),
										};
										Ext.apply(operation,{
											params:params
										});
									};
								}
							}
						}),
					displayField: 'goodsTypeName',
					valueField: 'goodsTypeId',
					editable: false
				}),DataDictionary.getDataDictionaryCombo('refererTypeName', {
					fieldLabel: '所属平台',
					itemId : 'MyApp_view_trade_type_ID',
					columnWidth: .5,
					labelWidth: 100,
					name: 'tradeType',
					editable: false
				}),{
					xtype: 'htmleditor',
					anchor: '100%',
					name: 'warning',
					title: '请输入内容',
					height: 250,
					columnWidth: 1,
					allowBlank: true,
					enableFont: false
				}],
				buttons: [{
					text:'保存',
					handler: function() {
						var formView = me.getForm(),
							form = formView.getForm(),
							gameName = form.findField('gameName'),
							record = form.getRecord(),
							url = './goods/addWarning.action';
						if(!form.isValid()){
							return;
						}
						var msg = "新增";
						form.updateRecord(record);
						var values = form.getValues();
						var  params = {
							'warning.gameId':values.gameName,
							'warning.gameName': Ext.String.trim(gameName.getRawValue()),
							'warning.goodsTypeId':values.goodsTypeName,
							'warning.goodsTypeName':form.findField("goodsTypeName").getRawValue(),
							'warning.warning':values.warning,
							'warning.tradeType':values.tradeType
						};
						if(me.isUpdate){
							url = './goods/modifyWarning.action';
							msg = "修改";
							Ext.Object.merge(params,{
								'id': record.get('id')
							});
						}
						form.submit({
							url : url,
							method: 'POST',
							params: params,
							success : function(from, action, json) {
								var warningManager = Ext.getCmp('warningManager'),
									store = warningManager.getStore();
								Ext.ux.Toast.msg("温馨提示", msg+"成功");
								me.close();
								store.load();
							},
							exception : function(from, action, json) {
								Ext.ux.Toast.msg("温馨提示", json.message);
							}
						});
					}
				}]
			});
		}
		return me.form;
	},
	isUpdate: null,
	bindData: function(record,isUpdate){
		var me = this,
			formView = me.getForm()
		form = formView.getForm(),
			gameProp = formView.getComponent('MyApp_view_goods_gamelink_ID'),
		goodsProp = formView.getComponent('MyApp_view_goods_type_ID'),
			tradeProp = formView.getComponent('MyApp_view_trade_type_ID');

		form.reset();
		form.loadRecord(record);
		me.isUpdate = isUpdate;
		if(!isUpdate){
			gameProp.setDisabled(false);
			goodsProp.setDisabled(false);
			// tradeProp.setDisabled(false);
			form.reset();
		}
		else{
			// tradeProp.setDisabled(true);
			gameProp.setDisabled(true);
			goodsProp.setDisabled(true);
		}
	},
	constructor : function(config) {
		var me = this, cfg = Ext.apply({}, config);
		me.items = [me.getForm()]
		me.callParent([ cfg ]);
	}
});

Ext.define('MyApp.view.warningManager', {
	extend: 'Ext.panel.Panel',
	id:'warningManager',
	closable: true,
	title: '友情提示配置',
	autoScroll: false,
	listeners:{
		'resize':function(){
			this.warningGrid.setHeight(window.document.body.offsetHeight-190);
		}
	},
	toolbar: null,
	getToolbar: function(){
		var me = this;
		if(Ext.isEmpty(me.toolbar)){
			me.toolbar = Ext.widget('toolbar',{
				dock: 'top',
				items: [{
					text: '新增',
					listeners: {
						click: {
							fn: me.addWarning,
							scope: me
						}
					}
				},'-',{
					text: '修改',
					listeners: {
						click: {
							fn: me.modifyWarning,
							scope: me
						}
					}
				},'-',{
					text: '删除',
					listeners: {
						click: {
							fn: me.deleteWarning,
							scope: me
						}
					}
				}]
			});
		}
		return me.toolbar;
	},
	// 新增配置
	addWarning: function(button, e, eOpts) {
		var window = this.getWarningWindow();
		window.bindData(Ext.create('MyApp.model.WarningModel'), false);
		window.show();
	},
	// 修改配置
	modifyWarning: function(button, e, eOpts) {
		var grid = this.getWarningGrid(),
			selModel = grid.getSelectionModel(),
			selRecords = selModel.getSelection(),
			window = this.getWarningWindow();
		if(selRecords == null||selRecords.length<=0){
			Ext.ux.Toast.msg("温馨提示", "请先选择要修改的友情提示信息");
			return;
		}
		window.bindData(selRecords[0], true);
		window.show();
	},
	// 删除配置
	deleteWarning: function(button, e, eOpts) {
		var grid = this.getWarningGrid(),
			selModel = grid.getSelectionModel(),
			selRecords = selModel.getSelection();
		if(selRecords == null||selRecords.length<=0){
			Ext.ux.Toast.msg("温馨提示", "请先选择要删除的友情提示配置");
			return;
		}
		var ids = [];
		for (var i = 0, j = selRecords.length; i < j; i++) {
			ids.push(selRecords[i].get("id"));
		}
		Ext.MessageBox.confirm('温馨提示', '确定删除该友情提示配置吗？', function(btn){
			if(btn == 'yes'){
				Ext.Ajax.request({
					url : './goods/deleteWarning.action',
					params: {'ids': ids},
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
			}else{
				return;
			}
		});
	},

	warningWindow: null,
	getWarningWindow: function(){
		if(this.warningWindow == null){
			this.warningWindow = new MyApp.view.WarningWindow();
		}
		return this.warningWindow;
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
					xtype: 'gameselectorsellersetting',
					itemId : 'MyApp_view_goods_gamelink_ID',
					columnWidth: .7,
					allowBlank: true,
					fieldLabel: '游戏属性'
				},Ext.create('Ext.form.ComboBox', {
					fieldLabel: '商品类型',
					labelWidth: 100,
					name: 'goodsTypeName',
					store: Ext.create('MyApp.store.MallGoodsTypeNameIdStore', {
						listeners: {
							load: function (store, records, successful, eOpts) {
								//添加
								store.insert(0,{id:0, name:'全部'});
							}
						}
					}),
					displayField: 'name',
					valueField: 'name',
					value:'全部',
					editable: false
				}),DataDictionary.getDataDictionaryCombo('refererTypeName', {
					fieldLabel: '所属平台',
					columnWidth: .5,
					labelWidth: 100,
					name: 'tradeType',
					editable: false
				}, {value: null, display: '全部'})],
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
			me.store = Ext.create('MyApp.store.WarningStore',{
				autoLoad: true,
				listeners: {
					beforeload : function(store, operation, eOpts) {
						var queryForm = me.getQueryForm();
						var gameName = queryForm.getForm().findField('gameName');
						if (queryForm != null) {
							var values = queryForm.getValues();
							Ext.apply(operation, {
								params: {
									'gameName': gameName = Ext.String.trim(gameName.getRawValue()),
									'goodsTypeName':values.goodsTypeName,
									'tradeType':values.tradeType
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
	warningGrid: null,
	getWarningGrid: function(){
		var me = this;
		if(Ext.isEmpty(me.warningGrid)){
			me.warningGrid = Ext.create('MyApp.view.warningGrid',{
				store: me.getStore(),
				dockedItems: [me.getToolbar(),me.getPagingToolbar()]
			});
		}
		return me.warningGrid;
	},
	initComponent: function() {
		var me = this;
		Ext.applyIf(me, {
			items: [me.getQueryForm(),me.getWarningGrid()]
		});
		me.callParent(arguments);
	}
});
Ext.define('MyApp.view.warningGrid', {
	extend: 'Ext.grid.Panel',
	header: false,
	autoScroll: true,
	columnLines: true,
	columns: [{
		xtype: 'rownumberer'
	},{
		dataIndex: 'gameName',
		sortable: false,
		flex: 1,
		align: 'center',
		text: '游戏名称'
	},{
		dataIndex: 'tradeType',
		text: '所属平台 ',
		flex: 1,
		sortable: false,
		align: 'center',
		renderer: function (value, metaData, record, rowIndex, colIndex, store, view) {
			if (value == 1 || value == 2) {
				return '金币商城内部';
			} else {
				return DataDictionary.rendererSubmitToDisplay(value, 'refererTypeName');
			}
		}
	},{
		dataIndex: 'goodsTypeName',
		sortable: false,
		flex: 1,
		align: 'center',
		text: '商品类型',
		renderer: function(value, metaData, record, rowIndex, colIndex, store, view) {
			/*if(value==null||value==""){
				return "游戏币";
			}else*/ return value;
		}
	},{
		dataIndex: 'warning',
		text: '友情提示 ',
		flex: 1,
		sortable: false,
		align: 'center',
		renderer: function (value, metaData, record, rowIndex, colIndex, store, view) {
			return value;
		}
	}]
});
