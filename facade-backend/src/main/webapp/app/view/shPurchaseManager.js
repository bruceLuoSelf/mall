//采购商管理页面
Ext.define('MyApp.view.shPurchaseManager', {
	extend: 'Ext.panel.Panel',
	id: 'shPurchaseManager',
	closable: true,
	layout: "border",
	title: '采购商管理',
	toolbar: null,
	getToolbar: function(){
		var me = this;
		if(Ext.isEmpty(me.toolbar)){
			me.toolbar = Ext.widget('toolbar',{
				dock: 'top',
				items: [{
					text: '修改',
					listeners: {
						click: {
							fn: me.updatePurchase,
							scope: me
						}
					}
				},{
				text: '明细',
					listeners:{
						click: {
							fn: me.queryDetail,
							scope: me
						}
					}
			}]
			});
		}
		return me.toolbar;
	},
	// 修改采购商信息
	updatePurchase: function(button, e, eOpts) {
		var me = this,
			grid = me.getRefundOrderGrid().getSelectionModel(),
			selRecords = grid.getSelection(),
			window = me.getUpdateshPurchaseWindow();
		if (selRecords == null || selRecords.length <= 0) {
			Ext.ux.Toast.msg("温馨提示", "请先选择要修改的采购商信息");
			return;
		}
		window.bindData(selRecords[0]);
		window.show();
	},
	queryDetail:function(button,e,eOpts) {
		var grid= this.getRefundOrderGrid(),
			selModel = grid.getSelectionModel(),
			selRecords =selModel.getSelection(),order,
			window = this.getDetailRefundOrderWindow();
		if(selRecords == null||selRecords.length<=0){
			Ext.ux.Toast.msg("温馨提示", "请先选择要处理的订单");
			return;
		}
		record = selRecords[0];

		window.bindData(record);
		window.show();
	},
	detailRefundOrderWindow:null,
	getDetailRefundOrderWindow:function(){
		var me = this;
		if(me.detailRefundOrderWindow==null){
			me.detailRefundOrderWindow=Ext.create('MyApp.view.detailRefundOrderWindow');
		}
		return me.detailRefundOrderWindow;
	},
	updatePurchaseWindow: null,
	getUpdateshPurchaseWindow: function () {
		if (this.updatePurchaseWindow == null) {
			this.updatePurchaseWindow = new MyApp.view.updatePurchaseWindow();
		}
		return this.updatePurchaseWindow;
	},


	queryForm: null,
	getQueryForm: function () {
		var me = this;
		if (me.queryForm == null) {
			me.queryForm = Ext.widget('form', {
				layout: 'column',
				region: 'north',
				defaults: {
					margin: '10 10 10 10'
				},
				items: [{
					xtype: 'textfield',
					columnWidth: .2,
					labelWidth: 80,
					fieldLabel: '商家姓名',
					name: 'name'
				}, {
					xtype: 'textfield',
					columnWidth: .2,
					labelWidth: 80,
					fieldLabel: '商家账号',
					name: 'loginAccount'
				}
				],
				buttons: [{
					text: '查询',
					handler: function () {
						me.getPagingToolbar().moveFirst();
					}
				}, '-', {
					text: '重置',
					handler: function () {
						me.getQueryForm().getForm().reset();
					}
				}, '->']
			});
		}
		return this.queryForm;
	},
	store: null,
	getStore: function(){
		var me = this;
		if(me.store==null){
			me.store = Ext.create('MyApp.store.shPurchaseStore',{
				autoLoad: true,
				listeners: {
					beforeload: function (store, operation, eOpts) {
						var queryForm = me.getQueryForm();
						if (queryForm != null) {
							var values = queryForm.getValues();
							var params = {
								'purchaserDatas.name':Ext.String.trim(values.name),
								'purchaserDatas.loginAccount':Ext.String.trim(values.loginAccount)
							}
							Ext.apply(operation, {
								params: params
							})
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
	purchaseGrid: null,
	getRefundOrderGrid: function(){
		var me = this;
		if(Ext.isEmpty(me.refundOrderGrid)){
			me.refundOrderGrid = Ext.widget('gridpanel',{
				header: false,
				columnLines: true,
				region: 'center',
				store: me.getStore(),
				columns: [{
					xtype: 'rownumberer'
				},{
					dataIndex: 'name',
					text: '商家姓名',
					flex:  1.7,
					align: 'center'
				},{
					dataIndex: 'loginAccount',
					text: '商家账号',
					flex:  1.7,
					align: 'center'
				},{
					dataIndex: 'phoneNumber',
					text: '商家电话',
					flex:  1.7,
					align: 'center'
				},{
					dataIndex: 'cjl',
					text: '成交率',
					flex:  1.7,
					align: 'center'
				},{
					dataIndex: 'tradingVolume',
					text: '成交量',
					flex:  1.7,
					align: 'center'
				},{
					dataIndex: 'pjys',
					text: '平均用时',
					flex:  1.7,
					align: 'center'
				},{
					dataIndex: 'credit',
					text: '商家信誉',
					flex:  1.7,
					align: 'center'
				},{
					dataIndex: 'totalAmount',
					text: '总金额',
					flex:  1.7,
					align: 'center'
				},{
					dataIndex: 'freezeAmount',
					text: '冻结金额',
					flex:  1.7,
					align: 'center'
				},{
					dataIndex: 'availableAmount',
					text: '可用金额',
					flex:  1.7,
					align: 'center'
				},{
					dataIndex: 'totalAmountZBao',
					text: '7bao总金额',
					flex:  1.7,
					align: 'center'
				},{
					dataIndex: 'freezeAmountZBao',
					text: '7bao冻结金额',
					flex:  1.7,
					align: 'center'
				},{
					dataIndex: 'availableAmountZBao',
					text: '7bao可用金额',
					flex:  1.7,
					align: 'center'
				}, {
					dataIndex: 'isSplit',
					sortable: true,
					flex: 1.7,
					text: '收货商分仓开关',
					renderer: function (value, metaData, record, rowIndex, colIndex, store, view) {
						return DataDictionary.rendererSubmitToDisplay(value, 'isSplit');
					}
				},{
					dataIndex: 'repositoryCount',
					text: '库存上限',
					flex:  1.7,
					align: 'center'
				},{
					dataIndex: 'needCount',
					text: '缺口上限',
					flex:  1.7,
					align: 'center'
				}],
				dockedItems: [me.getToolbar(),me.getPagingToolbar()],
				selModel: Ext.create('Ext.selection.CheckboxModel', {
					allowDeselect: true,
					mode: 'MULTI'
				})
			});
		}
		return me.refundOrderGrid;

	},
	initComponent: function() {
		var me = this;
		Ext.applyIf(me, {
			items: [me.getQueryForm(),me.getRefundOrderGrid()]
		});
		me.callParent(arguments);
	}
});

//修改采购商信息
Ext.define('MyApp.view.updatePurchaseWindow', {
	extend: 'Ext.window.Window',
	title: '修改采购商信息',
	width: 400,
	closeAction: 'hide',
	modal: true,
	store: null,
	form: null,
	getForm: function () {
		var me = this;
		if (me.form == null) {
			me.form = Ext.widget('form', {
				layout: 'column',
				defaults: {
					margin: '10 10 5 5',
				},
				items: [{
					xtype: 'textfield',
					allowBlank: false,
					columnWidth: 1,
					labelWidth: 130,
					fieldLabel: '成交率',
					name: 'cjl'
				}, {
					xtype: 'textfield',
					allowBlank: false,
					columnWidth: 1,
					labelWidth: 130,
					fieldLabel: '成交量',
					name: 'tradingVolume'
				}, {
					xtype: 'textfield',
					allowBlank: false,
					columnWidth: 1,
					labelWidth: 130,
					fieldLabel: '平均用时',
					name: 'pjys'
				},{
					xtype: 'textfield',
					allowBlank: false,
					columnWidth: 1,
					labelWidth: 130,
					fieldLabel: '商家信誉',
					name: 'credit'
				},{
					xtype: 'textfield',
					allowBlank: true,
					columnWidth: 1,
					labelWidth: 130,
					fieldLabel: '库存上限',
					name: 'repositoryCount'
				},{
					xtype: 'textfield',
					allowBlank: true,
					columnWidth: 1,
					labelWidth: 130,
					fieldLabel: '缺口上限',
					name: 'needCount'
				},DataDictionary.getDataDictionaryCombo('isSplit', {
					fieldLabel: '分仓开关',
					labelWidth: 80,
					columnWidth: 1,
					labelWidth: 130,
					name: 'isSplit',
					editable: false,
					value: true,
				}),DataDictionary.getDataDictionaryCombo('fundType', {
					fieldLabel: '开始交易时间',
					labelWidth: 80,
					columnWidth: 1,
					labelWidth: 130,
					name: 'startTime',
					editable: false,
					value: 0,
					id:'stTime'
				}),DataDictionary.getDataDictionaryCombo('fundType', {
					fieldLabel: '结束交易时间',
					labelWidth: 80,
					columnWidth: 1,
					labelWidth: 130,
					name: 'endTime',
					editable: false,
					value: 0,
					id:'enTime'
				}),],
				buttons: [{
					text: '保存',
					handler: function () {
						var form = me.getForm().getForm();
						if (!form.isValid()) {
							return;
						}
						var record = form.getRecord(),
							values = form.getValues(),
							store = Ext.getCmp('shPurchaseManager').getStore();
						form.updateRecord(record);
                        var params={
							'purchaserDatas.id': record.get('id'),
                            'purchaserDatas.cjl': record.get('cjl'),
                            'purchaserDatas.tradingVolume': record.get('tradingVolume'),
                            'purchaserDatas.pjys': record.get('pjys'),
                            'purchaserDatas.credit': record.get('credit'),
							'purchaserDatas.startTime': record.get('startTime'),
							'purchaserDatas.endTime': record.get('endTime'),
							'purchaserDatas.repositoryCount': record.get('repositoryCount'),
							'purchaserDatas.needCount': record.get('needCount'),
							'purchaserDatas.isSplit': record.get('isSplit'),
                        };
						var startTime= Ext.getCmp('stTime').getValue();
						var endTime= Ext.getCmp('enTime').getValue();
						if(endTime<startTime){
							Ext.ux.Toast.msg("温馨提示", "开始时间不能大于结束时间");
							return;
						}
						form.submit({
								url: './shpurchase/updateShPurchase.action',
                            params: params,
							success: function (from, action, json) {
								Ext.ux.Toast.msg("温馨提示", "修改成功");
								me.close();
								store.load();
							},
							exception: function (from, action, json) {
								Ext.ux.Toast.msg("温馨提示", json.responseStatus.message);
							}
						});
						me.close();

					}
				}]
			});
		}
		return me.form;
	},
	bindData: function (record) {
		var me = this,
			form = me.getForm().getForm();
		form.loadRecord(record);
		// console.log(record);
	},
	constructor: function (config) {
		var me = this, cfg = Ext.apply({}, config),
			form = me.getForm();
		me.items = [me.getForm()];
		me.callParent([cfg]);
	},
});

//采购商管理明细
Ext.define('MyApp.view.detailRefundOrderWindow',{
	extend: 'Ext.window.Window',
	title: '采购商明细',
	width: 800,
	border: false,
	padding: '10 10 10 10',
	autoScroll: true,
	closeAction: 'hide',
	modal: true,
	form: null,
	getForm:function(){
		var me=this;
		if(me.form==null){
			me.form = Ext.create('MyApp.view.detailRefundOrderForm');
		}
		return me.form;
	},
	bindData: function(record){
		var me = this,
			form = me.getForm().getForm();
			form.reset();
			form.loadRecord(record);
	}
	,initComponent: function() {
		var me = this;
		Ext.applyIf(me, {
			items: [me.getForm()]
		});
		me.callParent(arguments);
	}
});

Ext.define('MyApp.view.detailRefundOrderForm',{
	extend:'Ext.form.Panel',
	layout:'column',
	border: false,
	defaults: {
		margin: '5 5 5 5',
		columnWidth: .333,
		labelWidth: 100,
		readOnly: true,
		xtype: 'textfield'
	},
	constructor:function (config) {
		var me =this, cfg = Ext.apply({}, config);
		me.items=[{
			name: 'totalAmount',
			fieldLabel:'总金额'
		},{
			name: 'availableAmount',
			fieldLabel:'可用金额'
		},{
			name: 'freezeAmount',
			fieldLabel:'冻结金额'
		},{
			name: 'startTime',
			fieldLabel:'创建时间'
		},{
			name: 'endTime',
			fieldLabel:'结束时间'
		},{
			name: 'cjl',
			fieldLabel:'成交率'
		},{
			name: 'tradingVolume',
			fieldLabel:'成交量'
		},{
			name: 'pjys',
			fieldLabel:'平均用时'
		},{
			name: 'credit',
			fieldLabel:'商家信誉'
		},{
			name: 'name',
			fieldLabel:'商家姓名'
		},{
			name: 'loginAccount',
			fieldLabel:'商家账号'
		},{
			name: 'phoneNumber',
			fieldLabel:'商家电话'
		},{
			name: 'repositoryCount',
			fieldLabel:'库存上限'
		},{
			name: 'needCount',
			fieldLabel:'缺口上限'
		}];
		me.callParent([cfg]);
	}
});