Ext.define('MyApp.view.repositoryWindow', {
    extend: 'Ext.window.Window',
    title: '新增/修改库存',
    //title: '修改库存',
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
						margin: '5 5 5 5',
						columnWidth: .5,
						labelWidth: 90,
						readOnly: true,
						allowBlank: true,
						xtype: 'textfield'
					},
                    items: [{
					    fieldLabel: '所属客服ID',
					    disabled: true,
					    name: 'servicerId'
					},{
						fieldLabel: '卖家5173账号',
					    name: 'accountUid',
					    xtype: 'commonsellerselector',
					    listeners: {
					    	'select': function(combo, records, eOpts){
					    		var form = me.getForm().getForm(),
					    			designer = form.findField('accountUid');
					    		if(!Ext.isEmpty(records)&&records.length>0){
					    			var record = records[0];
					    			form.getRecord().set('loginAccount',record.get('loginAccount'));
					    		}else{
					    			form.getRecord().set('loginAccount','');
					    		}
					    	}
					    }
					},{
    					xtype: 'gamelinkselector',
    					itemId : 'MyApp_view_goods_gamelink_ID',
    					columnWidth: 1,
    					//allowBlank: false,
    					fieldLabel: '游戏属性'
    				},{
					    fieldLabel: '游戏账号',
					    allowBlank: false,
					    name: 'gameAccount'
					},{
					    fieldLabel: '子账号',
					    name: 'sonAccount'
					},{
					    fieldLabel: '游戏角色',
					    allowBlank: false,
					    name: 'sellerGameRole'
					},{
					    fieldLabel: '游戏币名',
					    allowBlank: false,
					    name: 'moneyName'
					},{
					    xtype: 'numberfield',
					    fieldLabel: '单价(元)',
					    allowBlank: false,
					    readOnly: false,
					    decimalPrecision: 5,
					    step:0.00001,
					    name: 'unitPrice',
					    minValue: 0
					},{
					    xtype: 'numberfield',
					    fieldLabel: '通货数目',
					    readOnly: false,
					    allowBlank: false,
					    name: 'goldCount',
					    minValue: 0
					},{
					    xtype: 'numberfield',
					    fieldLabel: '可销售库存',
					    readOnly: false,
					    allowBlank: false,
					    name: 'sellableCount',
					    minValue: 0
					},{
						fieldLabel: '商品类目',
						readOnly: true,
						allowBlank: false,
						name: 'goodsTypeName'
					}],
					buttons: [{
						text:'保存',
						handler: function() {
							var form = me.getForm().getForm(),
								record = form.getRecord(),
								url = './repository/addRepository.action',
								message = '新增';
							form.updateRecord(record);
							var data = record.data;
							data.stockCount = null;
							if(form.isValid()){
								if(me.isUpdate){
									url = './repository/modifyRepository.action';
									message = '修改';
								}
								Ext.Ajax.request({
									url : url,
									method: 'POST',
									jsonData: {
										'repository': data
									},
									success : function(response, opts) {
										var repositoryManager = Ext.getCmp('repositoryManager'),
											store = repositoryManager.getStore();
										Ext.ux.Toast.msg("温馨提示", message + "成功");
										me.close();
										store.load();
									},
									exception : function(response, opts) {
										var json = Ext.decode(response.responseText);
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
	isUpdate: null,
	bindData: function(record,isUpdate){
		var me = this,
			form = me.getForm().getForm(),
			servicerId = form.findField('servicerId'),
			sellableCount = form.findField('sellableCount');
		form.reset();
		form.loadRecord(record);
		me.isUpdate = isUpdate;
		if(!isUpdate){
			form.reset();
			servicerId.setValue(CurrentUser.getCurrentUser().id);
		}
		/*if(Ext.String.trim(record.get('gameName'))==="地下城与勇士"){
			sellableCount.setReadOnly(false);
			sellableCount.setVisible(true);	
		}else{
			sellableCount.setReadOnly(true);
			sellableCount.setVisible(false);
		}*/
	},
	constructor : function(config) {
		var me = this, cfg = Ext.apply({}, config);
		me.items = [me.getForm()]
		me.callParent([ cfg ]);
	}
});
Ext.define('MyApp.view.repositoryWindow1', {
    extend: 'Ext.window.Window',
    title: '新增/修改库存',
    //title: '修改库存',
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
						margin: '5 5 5 5',
						columnWidth: .5,
						labelWidth: 90,
						readOnly: true,
						allowBlank: true,
						xtype: 'textfield'
					},
                    items: [{
					    fieldLabel: '所属客服ID',
					    disabled: true,
					    name: 'servicerId'
					},{
						fieldLabel: '卖家5173账号',
					    name: 'accountUid',
					    xtype: 'commonsellerselector',
					    listeners: {
					    	'select': function(combo, records, eOpts){
					    		var form = me.getForm().getForm(),
					    			designer = form.findField('accountUid');
					    		if(!Ext.isEmpty(records)&&records.length>0){
					    			var record = records[0];
					    			form.getRecord().set('loginAccount',record.get('loginAccount'));
					    		}else{
					    			form.getRecord().set('loginAccount','');
					    		}
					    	}
					    }
					},{
    					xtype: 'gamelinkselector',
    					itemId : 'MyApp_view_goods_gamelink_ID',
    					columnWidth: 1,
    					//allowBlank: false,
    					fieldLabel: '游戏属性'
    				},{
					    fieldLabel: '游戏账号',
					    allowBlank: false,
					    name: 'gameAccount'
					},{
					    fieldLabel: '子账号',
					    name: 'sonAccount'
					},{
					    fieldLabel: '游戏角色',
					    allowBlank: false,
					    name: 'sellerGameRole'
					},{
					    fieldLabel: '游戏币名',
					    allowBlank: false,
					    name: 'moneyName'
					},{
					    xtype: 'numberfield',
					    fieldLabel: '单价(元)',
					    allowBlank: false,
					    readOnly: false,
					    decimalPrecision: 5,
					    step:0.00001,
					    name: 'unitPrice',
					    minValue: 0
					},{
					    xtype: 'numberfield',
					    fieldLabel: '通货数目',
					    readOnly: false,
					    allowBlank: false,
					    name: 'goldCount',
					    minValue: 0,
						maxValue:999999999
					},{
					    xtype: 'numberfield',
					    fieldLabel: '可销售库存',
					    readOnly: false,
					    allowBlank: false,
					    name: 'sellableCount',
					    minValue: 0,
						maxValue:999999999
					},{
						fieldLabel: '商品类目',
						readOnly: true,
						allowBlank: false,
						name: 'goodsTypeName'
					}],
					buttons: [{
						text:'保存',
						handler: function() {
							var form = me.getForm().getForm(),
								record = form.getRecord(),
								url = './repository/addRepository.action',
								message = '新增';
							form.updateRecord(record);
							var data = record.data;
							data.stockCount = null;
							if(form.isValid()){
								if(me.isUpdate){
									url = './repository/modifyRepository.action';
									message = '修改';
								}
								Ext.Ajax.request({
									url : url,
									method: 'POST',
									jsonData: {
										'repository': data
									},
									success : function(response, opts) {
										var repositoryManager = Ext.getCmp('repositoryManager'),
											store = repositoryManager.getStore();
										Ext.ux.Toast.msg("温馨提示", message + "成功");
										me.close();
										store.load();
									},
									exception : function(response, opts) {
										var json = Ext.decode(response.responseText);
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
	isUpdate: null,
	bindData: function(record,isUpdate){
		var me = this,
			form = me.getForm().getForm(),
			servicerId = form.findField('servicerId'),
			sellableCount = form.findField('sellableCount');
		form.reset();
		form.loadRecord(record);
		me.isUpdate = isUpdate;
		if(!isUpdate){
			form.reset();
			servicerId.setValue(CurrentUser.getCurrentUser().id);
		}
	},
	constructor : function(config) {
		var me = this, cfg = Ext.apply({}, config);
		me.items = [me.getForm()]
		me.callParent([ cfg ]);
	}
});

Ext.define('MyApp.view.repositoryGrid', {
    extend: 'Ext.grid.Panel',
    header: false,
    autoScroll: true,
    columnLines: true,
    columns: [{
		xtype: 'rownumberer'
	},{
        dataIndex: 'loginAccount',
        sortable: false,
        flex: 1.5,
        align: 'center',
        text: '卖家5173<br/>账号'
    },{
        dataIndex: 'gameAccount',
        sortable: false,
        flex: 1.5,
        align: 'center',
        text: '游戏账号'
    },{
        dataIndex: 'sellerGameRole',
        sortable: false,
        flex: 1,
        align: 'center',
        text: '卖家游戏<br/>角色名'
    },{
        dataIndex: 'gameName',
        sortable: false,
        flex: 1.5,
        align: 'center',
        text: '游戏名称'
    },{
		dataIndex: 'goodsTypeName',
		sortable: false,
		flex: 1,
		align: 'center',
		text: '类目'
	},{
		dataIndex: 'moneyName',
		sortable: false,
		flex: 1,
		align: 'center',
		text: '单位'
	},{
		dataIndex: 'region',
		sortable: false,
		text: '所在区',
		align: 'center',
		flex: 1
	},{
		dataIndex: 'server',
		sortable: false,
		flex: 1,
		align: 'center',
		text: '所在服'
	},{
		dataIndex: 'gameRace',
		sortable: false,
		flex: 1,
		align: 'center',
		text: '所在阵营'
	},{
		dataIndex: 'unitPrice',
		sortable: false,
		flex: 1,
		/*renderer: function(v, metaData, record, rowIndex, colIndex, store, view) {
			if(record.get('gameName')=="魔兽世界(国服)"||record.get('gameName')=="地下城与勇士"){
				return Ext.util.Format.currency(v, '￥', 5);
			}else
				return Ext.util.Format.currency(v, '￥', 4);
        },*/
        align: 'center',
		text: '单价'
	},{
		dataIndex: 'goldCount',
		sortable: false,
		flex: 1,
		align: 'center',
		text: '通货数目'
	},{
		dataIndex: 'sellableCount',
		sortable: false,
		flex: 1,
		align: 'center',
		text: '可销售库存'
	},{
		dataIndex: 'stockCount',
		sortable: false,
		flex: 1,
		align: 'center',
		text: '盘存数量'
	}],
    constructor : function(config) {
		var me = this, cfg = Ext.apply({}, config);
		me.selModel = Ext.create('Ext.selection.CheckboxModel', {
			allowDeselect: true,
			mode: 'MULTI'
		})
		me.callParent([ cfg ]);
	}
});

// Ext.define('MyApp.view.uploadWindow', {
//     extend: 'Ext.window.Window',
//     title: '上传库存',
//     width: 500,
// 	closeAction: 'hide',
// 	modal: true,
// 	form: null,
// 	getForm: function(){
// 		var me = this;
// 		if(me.form==null){
// 			me.form = Ext.widget('form',{
//                     layout: 'column',
// 					defaults: {
// 						margin: '5 5 5 5',
// 						labelWidth: 90
// 					},
//                     items: [{
// 					    xtype: 'filefield',
// 					    name: 'repositoryFile',
// 					    columnWidth: 1,
// 					    allowBlank: false,
// 					    vtype: 'excel',
// 					    fieldLabel: '库存文件'
// 					}]
//                 });
// 		}
// 		return me.form;
// 	},
// 	constructor : function(config) {
// 		var me = this, cfg = Ext.apply({}, config);
// 		me.items = [me.getForm()];
// 		me.buttons = [{
// 			text:'保存',
// 			handler: function() {
// 				var form = me.getForm().getForm();
// 				if(!form.isValid()){
// 					return;
// 				}
// 				form.submit({
// 					url : './repository/uploadRepository.action',
// 					method: 'POST',
// 					success : function(from, action, json) {
// 						var repositoryManager = Ext.getCmp('repositoryManager'),
// 							store = repositoryManager.getStore();
// 						Ext.ux.Toast.msg("温馨提示", "上传成功");
// 						me.close();
// 						store.load();
// 					},
// 					exception : function(from, action, json) {
// 						Ext.ux.Toast.msg("温馨提示", json.message);
// 					}
// 				});
// 			}
// 		}];
// 		me.callParent([ cfg ]);
// 	}
// });

/**
 * 2017/05/12 wubiao ZW_C_JB_00008 商城增加通货
 */
/*************ZW_C_JB_00008_20170512_START***************************/
Ext.define('MyApp.view.currencyWindow', {
	extend: 'Ext.window.Window',
	title: '通货上传',
	width: 500,
	closeAction: 'hide',
	modal: true,
	form: null,
	getForm: function(){
		var me = this;
		if(me.form==null){
			me.form = Ext.widget('form',{
				layout: 'column',
				defaults: {
					margin: '5 5 5 20',
					labelWidth: 100
				},
				items: [{
					xtype: 'filefield',
					name: 'repositoryFile',
					labelWidth: 100,
					allowBlank: false,
					vtype: 'excel',
					fieldLabel: '库存文件'
				},  Ext.create('Ext.form.ComboBox', {
					fieldLabel: '商品类型',
					labelWidth: 100,
					allowBlank: false,
					name: 'goodsTypeName',
					store: Ext.create('MyApp.store.MallGoodsTypeNameIdStore', {
						listeners: {
							load: function (store, records, successful, eOpts) {
								//添加
								// store.insert(0,{id:0, name:'全部'});
							}
						}
					}),
					displayField: 'name',
					valueField: 'name',
					editable: false
				})
				]
			});
		}
		return me.form;
	},
	constructor : function(config) {
		var me = this, cfg = Ext.apply({}, config);
		me.items = [me.getForm()];
		me.buttons = [{
			text:'保存',
			handler: function() {
				var form = me.getForm().getForm();
				var value = form.getValues(); //ZW_C_JB_00008_20170513 ADD '商品类目'
				if(!form.isValid()){
					return;
				}
				form.submit({
					url : './repository/uploadRepository.action',
					method: 'POST',
					params:{goodsTypeName:value.goodsTypeName},//ZW_C_JB_00008_20170513 ADD '商品类目'
					success : function(from, action, json) {
						var repositoryManager = Ext.getCmp('repositoryManager'),
							store = repositoryManager.getStore();
						Ext.ux.Toast.msg("温馨提示", "上传成功");
						me.close();
						store.load();
					},
					exception : function(from, action, json) {
						Ext.ux.Toast.msg("温馨提示", json.message);
					}
				});
			}
		}];
		me.callParent([ cfg ]);
	}
});
/*************ZW_C_JB_00008_20170512_END***************************/

/*
 * 库存管理页面
 */
Ext.define('MyApp.view.repositoryManager', {
    extend: 'Ext.panel.Panel',
    id: 'repositoryManager',
    closable: true,
    title: '库存管理',
	autoScroll: false,
	listeners:{  
    'resize':function(){
        this.repositoryGrid.setHeight(window.document.body.offsetHeight-265);
    	}
	},
    toolbar: null,
	getToolbar: function(){
		var me = this;
		if(Ext.isEmpty(me.toolbar)){
			me.toolbar = Ext.widget('toolbar',{
				dock: 'top',
				items: [{
					text: '修改库存',
					//text: '修改游戏币数目',
					listeners: {
						click: {
							fn: me.modifyRepository,
							scope: me
						}
					}
				},'-',{
					text: '批量删除',
					listeners: {
						click: {
							fn: me.deleteAllRepository,
							scope: me
						}
					}
				},'-',{
				    text: '导出',
				    listeners: {
				        click: {
				            fn: me.exportRepository,
				            scope: me
				        }
				    }
				},'-',{
					text: '上传库存',
					listeners: {
						click: {
							fn: me.currencyRepository,
							scope: me
						}
					}
				}]
			});
		}
		return me.toolbar;
	},
	// 导出库存
	exportRepository: function(button, e, eOpts){
		var me = this,url,
			queryForm = me.getQueryForm(),
			gameName = queryForm.getForm().findField('gameName'),
			region,server;
		if (queryForm != null) {
			var values = queryForm.getValues();

			gameName = Ext.String.trim(gameName.getRawValue());
			if (gameName == '') {
				// 没有选择游戏属性时，获取游戏属性2的值进行筛选
				var gameProps = queryForm.getForm().findField('gameProps').getValue();

				if (gameProps != '') {
					var gamePropsArr = gameProps.split("/"); // 按“空格”分割
					gameName = gamePropsArr[0]; // 游戏名称
					server = gamePropsArr[1] || "";	// 游戏服务器
				}
			} else {
				region = Ext.String.trim(values.region);
				server =  Ext.String.trim(values.server);
			}

			var params = {
				'repository.loginAccount': Ext.String.trim(values.loginAccount),
				'repository.gameName': gameName,
				'repository.region': region,
				'repository.server': server,
				'repository.gameRace': Ext.String.trim(values.gameRace),
				'repository.sellerGameRole': Ext.String.trim(values.sellerGameRole),
				'repository.goodsTypeName': Ext.String.trim(values.goodsTypeName)//ZW_C_JB_00008_20170512 ADD
			}
		}
		url = './repository/exportRepository.action?' + Ext.urlEncode(params);
		window.open(url);
	},
	//上传库存
	// uploadWindow: null,
	// getUploadWindow: function(){
	// 	if(this.uploadWindow == null){
	// 		this.uploadWindow = new MyApp.view.uploadWindow();
	// 	}
	// 	return this.uploadWindow;
	// },
	// // 上传库存信息
	// uploadRepository: function(button, e, eOpts) {
	// 	var window = this.getUploadWindow(),
	// 		form = window.getForm().getForm();
	// 	form.reset();
	// 	window.show();
    // },

	/*************ZW_C_JB_00008_20170512_START********************/
	currencyWindow: null,
	getCurrencyWindow: function(){
		if(this.currencyWindow == null){
			this.currencyWindow = new MyApp.view.currencyWindow();
		}
		return this.currencyWindow;
	},
	currencyRepository: function(button, e, eOpts) {
		var window = this.getCurrencyWindow(),
			form = window.getForm().getForm();
		form.reset();
		window.show();
	},
	/*************ZW_C_JB_00008_20170512_END*********************/

	repositoryWindow: null,
	getRepositoryWindow: function(){
		if(this.repositoryWindow == null){
			this.repositoryWindow = new MyApp.view.repositoryWindow();
		}
		return this.repositoryWindow;
	},
	repositoryWindow1: null,
	getRepositoryWindow1: function(){
		if(this.repositoryWindow1 == null){
			this.repositoryWindow1 = new MyApp.view.repositoryWindow1();
		}
		return this.repositoryWindow1;
	},
	// 新增库存信息
    addRepository: function(button, e, eOpts) {
		var window = this.getRepositoryWindow();
		window.bindData(Ext.create('MyApp.model.RepositoryModel'), false);
		window.show();
    },
    // 修改库存信息
    modifyRepository: function(button, e, eOpts) {
		var grid = this.getRepositoryGrid(),
			selModel = grid.getSelectionModel(),
			selRecords = selModel.getSelection(),
			/*****************ZW_C_JB_00008_20170519 MODIFY*******************/
			window;
    	if(selRecords == null||selRecords.length<=0){
    		Ext.ux.Toast.msg("温馨提示", "请先选择要修改的库存信息");
    		return;
    	}else{
			if(selRecords[0].get('gameName')=="魔兽世界(国服)"){
				window = this.getRepositoryWindow1();
			}else {
				window = this.getRepositoryWindow();
			}
		}
		/*****************ZW_C_JB_00008_20170519 MODIFY*******************/
    	window.bindData(selRecords[0], true);
    	window.show();
    },
    // 批量删除库存信息
    deleteAllRepository: function(button, e, eOpts) {
    	var grid = this.getRepositoryGrid(),
			selModel = grid.getSelectionModel(),
			selRecords = selModel.getSelection(),repositoryIds=[];
    	if(selRecords == null||selRecords.length<=0){
    		Ext.ux.Toast.msg("温馨提示", "请先选择要删除的库存信息");
    		return;
    	}
		Ext.Array.each(selRecords, function(record, index, records){
			repositoryIds.push(record.get('id'));
		});
    	Ext.MessageBox.confirm('温馨提示', '确定删除该库存信息吗？', function(btn){
			if(btn == 'yes'){
				Ext.Ajax.request({
					url : './repository/deleteRepository.action',
					params: {'repositoryIds': repositoryIds},
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
    queryForm: null,
	getQueryForm: function(){
		var me = this;
		if(me.queryForm==null){
			me.queryForm = Ext.widget('form',{
                layout: 'form',
				defaults: {
					margin: '10 10 10 10',
					labelWidth: 80
				},
                items: [{
					xtype: 'container',
					layout: 'column',
					defaults: {
						margin: '5 5 5 5',
						xtype: 'textfield'
					},
					items:[
						{
							fieldLabel: '卖家5173账号',
							columnWidth: .25,
							labelWidth: 100,
							name: 'loginAccount'
						},{
							xtype: 'gamelinkselector',
							itemId : 'MyApp_view_goods_gamelink_ID',
							columnWidth: .5,
							allowBlank: true,
							fieldLabel: '游戏属性'
						},Ext.create('Ext.form.ComboBox', {
							fieldLabel: '商品类目',
							columnWidth: .20,
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
						}), {
							fieldLabel: '游戏角色名',
							columnWidth: .30,
							// 	labelWidth: 0,
							name: 'sellerGameRole'
						}, {
							fieldLabel: '游戏帐号',
							columnWidth: .30,
							name: 'gameAccount'
						},{
							fieldLabel: '游戏属性2',
							columnWidth: .30,
							name: 'gameProps',
							emptyText: '游戏名称/所在服 例如:地下城/四川5'
						}
					]
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
			me.store = Ext.create('MyApp.store.RepositoryStoreStock',{
				autoLoad: true,
				listeners: {
					beforeload : function(store, operation, eOpts) {
						var queryForm = me.getQueryForm(),
							gameName = queryForm.getForm().findField('gameName'),
							region = "",
							server = "";
						if (queryForm != null) {
							var values = queryForm.getValues();
							gameName = Ext.String.trim(gameName.getRawValue());
							if (gameName == '') {
								// 没有选择游戏属性时，获取游戏属性2的值进行筛选
								var gameProps = queryForm.getForm().findField('gameProps').getValue();
								//console.log("gameProps:"+gameProps);
								if (gameProps != '') {
									var gamePropsArr = gameProps.split("/"); // 按“空格”分割 stock
									gameName = gamePropsArr[0]; // 游戏名称
									server = gamePropsArr[1] || "";	// 游戏服务器
								}
							} else {
								region = Ext.String.trim(values.region);
								server =  Ext.String.trim(values.server);
							}
							//console.log("gameName:"+ gameName + "/region:" + region +  "/server:" + server);
							Ext.apply(operation, {
								params: {
									'repository.loginAccount': Ext.String.trim(values.loginAccount),
									'repository.gameName': gameName,
									'repository.region': region,
									'repository.server': server,
									'repository.gameRace': Ext.String.trim(values.gameRace),
									'repository.goodsTypeName':Ext.String.trim(values.goodsTypeName),//ZW_C_JB_00008_20170512 ADD
									'repository.sellerGameRole': Ext.String.trim(values.sellerGameRole),
									'repository.gameAccount': Ext.String.trim(values.gameAccount),
									'isNeed':false
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
	repositoryGrid: null,
	getRepositoryGrid: function(){
		var me = this;
		if(Ext.isEmpty(me.repositoryGrid)){
			me.repositoryGrid = Ext.create('MyApp.view.repositoryGrid',{
				store: me.getStore(),
				dockedItems: [me.getToolbar(),me.getPagingToolbar()]
			});
		}
		return me.repositoryGrid;
	},
    initComponent: function() {
        var me = this;
        Ext.applyIf(me, {
            items: [me.getQueryForm(),me.getRepositoryGrid()]
        });
        me.callParent(arguments);
    }
});