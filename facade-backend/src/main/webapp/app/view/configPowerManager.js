/*
 * 库存管理页面
 * Update History
 * Date        Name                Reason for change
 * ----------  ----------------    ----------------------
 * 2017/05/12  liuchanghua           ZW_C_JB_00008 商城增加通货
 */
Ext.define('MyApp.view.configPowerManager', {
    extend: 'Ext.panel.Panel',
    id: 'configPowerManager',
    closable: true,
    title: '配单权限管理',
    ////////////////////////////////////////////////////
    autoScroll: false,
	listeners:{  
    'resize':function(){
        this.configPowerGrid.setHeight(window.document.body.offsetHeight-215);
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
					labelWidth: 80,
					xtype: 'textfield'
				},
                items: [{
					xtype: 'gameselectorsellersetting',////ZW_C_JB_00008_20170512 MOD
					itemId : 'MyApp_view_goods_gamelink_ID',
					columnWidth: 1,
					//allowBlank: false,
					fieldLabel: '游戏属性'
				},DataDictionary.getDataDictionaryCombo('check',{
                	fieldLabel: '是否自动配单',
                	labelWidth: 100,
					name: 'configPower',
					editable: false
				},{value:null,display:'全部'}),DataDictionary.getDataDictionaryCombo('tradeType',{
                	fieldLabel: '交易方式',
                	labelWidth: 80,
					editable: false,
					name: 'tradeType'
				},{value:null,display:'全部'}),
					/**ZW_C_JB_00008_20170512 START ADD**/
					Ext.create('Ext.form.ComboBox', {
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
					})
					/**ZW_C_JB_00008_20170512 START ADD**/
				],
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
			me.store = Ext.create('MyApp.store.ConfigPowerStore',{
				autoLoad: true,
				listeners: {
					beforeload : function(store, operation, eOpts) {
						var queryForm = me.getQueryForm(),
							gameName = queryForm.getForm().findField('gameName');
						if (queryForm != null) {
							var values = queryForm.getValues(),params={};
							params = {
								'gameName': Ext.String.trim(gameName.getRawValue()),
								'tradeType':values.tradeType,
								'goodsTypeName':values.goodsTypeName ////ZW_C_JB_00008_20170512 ADD
							};
							if(!Ext.isEmpty(values.configPower)){
								params = Ext.Object.merge(params,{
									'configPower':values.configPower
								});
							}
							Ext.apply(operation,{
								params:params
							});							
						};	
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
	toolbar: null,
	getToolbar: function(){
		var me = this;
		if(Ext.isEmpty(me.toolbar)){
			me.toolbar = Ext.widget('toolbar',{
				dock: 'top',
				items: [{
					text: '修改交易方式',
					handler: function(){
						me.alterTradeType();
					}
				}/**ZW_C_JB_00008_20170512 START ADD**/
					,'-',{
					text: '新增管理配单',
					handler: function(){
						me.addConfigPower();
					}
				}
					/**ZW_C_JB_00008_20170512 END**/
					,'-',{
					text: '赋予自动配单',
					handler: function(){
						me.givePower();
					}
				},'-',{
					text: '取消自动配单',
					handler: function(){
						me.cancelPower();
					}
				},'-',{
					text: '修改最大数额',
					handler: function(){
						me.setLimit();
					}
				}]
			});
		}
		return me.toolbar;
	},
	//修改交易方式
	alterTradeType: function(){
		var grid = this.getConfigPowerGrid(),
			selModel = grid.getSelectionModel(),
			selRecords = selModel.getSelection();
		if(selRecords == null||selRecords.length<=0){
			Ext.ux.Toast.msg("温馨提示", "请先选择要修改的信息");
			return;
		}
		Ext.MessageBox.confirm('温馨提示', '确定修改该游戏交易方式吗？', function(btn){
			if(btn == 'yes'){
				Ext.Ajax.request({
					url : './order/alterTradeType.action',
					params:{'id':selRecords[0].get('id')},
					success : function(response, opts) {
						Ext.ux.Toast.msg("温馨提示", "修改交易方式成功！");
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
	//赋予自动配单权限
	givePower:function(button,e,eOpts){
		var grid = this.getConfigPowerGrid();
			selModel = grid.getSelectionModel(),
			selRecords = selModel.getSelection();
		if(selRecords == null||selRecords.length<=0){
			Ext.ux.Toast.msg("温馨提示","请先选择要修改的信息");
			return;
		}
    	Ext.MessageBox.confirm('温馨提示', '确认该游戏开启自动配单吗？', function(btn){
			if(btn == 'yes'){
				Ext.Ajax.request({
					url : './order/givePower.action',
					params:{'id':selRecords[0].get('id')},
					success : function(response, opts) {
						Ext.ux.Toast.msg("温馨提示", "开启自动配单成功！");
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
	//取消自动配单权限
	cancelPower:function(button,e,eOpts){
		var grid = this.getConfigPowerGrid();
			selModel = grid.getSelectionModel(),
			selRecords = selModel.getSelection();
		if(selRecords == null||selRecords.length<=0){
			Ext.ux.Toast.msg("温馨提示","请先选择要修改的信息");
			return;
		}
    	Ext.MessageBox.confirm('温馨提示', '确定取消该游戏自动配单吗？', function(btn){
			if(btn == 'yes'){
				Ext.Ajax.request({
					url : './order/cancelPower.action',
					params:{'id':selRecords[0].get('id')},
					success : function(response, opts) {
						Ext.ux.Toast.msg("温馨提示", "取消自动配单成功！");
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
	//修改最大配单数额
	setLimit: function(button, e, eOpts) {
		var grid = this.getConfigPowerGrid();
		selModel = grid.getSelectionModel(),
		selRecords = selModel.getSelection();
	if(selRecords == null||selRecords.length<=0){
		Ext.ux.Toast.msg("温馨提示","请先选择要修改的信息");
		return;
	}
		var window = this.getCountLimitWindow();
		window.bindData(selRecords[0], true);
		window.show();
    },
	/**ZW_C_JB_00008_20170512 START ADD**/
	addConfigPower: function(button, e, eOpts) {
		var window = this.getConfigPowerWindow();
		// window.bindData(Ext.create('MyApp.model.ConfigPowerModel'), false);
		window.show();
	},
	configPowerWindow: null,
	getConfigPowerWindow: function(){
		if(this.configPowerWindow == null){
			this.configPowerWindow = new MyApp.view.configPowerWindow();
		}
		return this.configPowerWindow;
	},
	/**ZW_C_JB_00008_20170512 END**/
	countLimitWindow:null,
	getCountLimitWindow:function(){
		if(this.countLimitWindow == null){
			this.countLimitWindow = new MyApp.view.countLimitWindow();
		}
		return this.countLimitWindow;
	},
	configPowerGrid: null,
	getConfigPowerGrid: function(){
		var me = this;
		if(Ext.isEmpty(me.configPowerGrid)){
			me.configPowerGrid = Ext.create('MyApp.view.configPowerGrid',{
				store: me.getStore(),
				dockedItems: [me.getToolbar(),me.getPagingToolbar()]
			});
		}
		return me.configPowerGrid;
	},
    initComponent: function() {
        var me = this;
        Ext.applyIf(me, {
            items: [me.getQueryForm(),me.getConfigPowerGrid()]
        });
        me.callParent(arguments);
    }
});

Ext.define('MyApp.view.configPowerGrid', {
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
		/**ZW_C_JB_00008_20170512 START ADD**/
		dataIndex: 'goodsTypeName',
		sortable: false,
		flex: 1,
		align: 'center',
		text: '商品类型',
		renderer: function(value, metaData, record, rowIndex, colIndex, store, view) {
			if(value==null||value==""){
				return "游戏币";
			}else return value;
		}
		/**ZW_C_JB_00008_20170512 END**/
	},{
    	dataIndex: 'tradeType',
    	sortable: false,
        flex: 1,
        align: 'center',
        text: '交易方式',
        renderer: function(value, metaData, record, rowIndex, colIndex, store, view) {
        	if(value==1){
        		return "当面交易";
        	}else return "邮寄交易";
        }
	},{
        dataIndex: 'configPower',
        sortable: false,
        flex: 1,
        align: 'center',
        text: '自动配单权限',
        renderer: function(value, metaData, record, rowIndex, colIndex, store, view) {
        	return CommonFunction.rendererEnable(!value);
        },
    },{
    	dataIndex:'configMaxCount',
    	sorttable:false,
    	flex:1,
    	align:'center',
    	text:'最高数额'
         
    }],
    constructor : function(config) {
		var me = this, cfg = Ext.apply({}, config);
		me.selModel = Ext.create('Ext.selection.CheckboxModel', {
			allowDeselect: true,
			mode: 'MULTI'
		});
		me.callParent([ cfg ]);
	}
});

/**ZW_C_JB_00008_20170512 START ADD**/
Ext.define('MyApp.view.configPowerWindow', {
	extend: 'Ext.window.Window',
	title: '新增管理配单',
	width: 600,
	closeAction: 'hide',
	modal: true,
	defaults: {
		margin: '2 2 2 2'
	},
	editer : null,
	getEditer : function(){
		if(this.editer==null){
			this.editer = Ext.create('Ext.grid.plugin.CellEditing', {
				//设置鼠标点击多少次，触发编辑
				clicksToEdit: 1
			});
		}
		return this.editer;
	},
	form: null,
	getForm: function(){
		var me = this;
		if(me.form==null){
			me.form = Ext.widget('form',{
				layout: 'column',
				defaults: {
					margin: '10 10 5 5',
					columnWidth: .5,
					labelWidth: 65,
					xtype: 'textfield'
				},
				items: [{
					xtype: 'gameselectorsellersetting',
					itemId : 'MyApp_view_goods_gamelink_ID',
					columnWidth: 1,
					allowBlank: false,
					gameChanged: function(ths, the, eOpts){
						var form = me.getForm().getForm();
						var categoryCombo = form.findField("goodsTypeId");
						categoryCombo.setRawValue(null);
						categoryCombo.getStore().load();
					},
					fieldLabel: '游戏属性'
				},DataDictionary.getDataDictionaryCombo('check',{
					fieldLabel: '是否自动配单',
					allowBlank: false,
					name: 'configPower',
					columnWidth: .5,
					labelWidth: 100,
					editable: false
				}),Ext.create('Ext.form.ComboBox', {
					fieldLabel: '交易类目',
					allowBlank: false,
					name: 'goodsTypeId',
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
				}),{
					fieldLabel: '最大数额',
					allowBlank: false,
					name: 'configMaxCount',
					xtype:'numberfield',
					step:1000
				}]
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
				var formView = me.getForm(),
					form = formView.getForm(),
					gameName = form.findField('gameName'),
					url = './order/addConfigPower.action',
					message = '新增';
				if(!form.isValid()){
					return;
				}
				var values = form.getValues();
				var params = {
					'configPowerEO.gameName': Ext.String.trim(gameName.getRawValue()),
					'configPowerEO.goodsTypeId':values.goodsTypeId,
					'configPowerEO.goodsTypeName':form.findField("goodsTypeId").getRawValue(),
					'configPowerEO.configPower':values.configPower,
					'configPowerEO.configMaxCount':form.findField("configMaxCount").getValue()
				};
				Ext.Ajax.request({
					url : url,
					method: 'POST',
					params: params,
					success : function(from, action, json) {
						var goodsManager = Ext.getCmp('configPowerManager'),
							store = goodsManager.getStore();
						Ext.ux.Toast.msg("温馨提示", message + "成功");
						me.close();
						store.load();
					},
					exception : function(response, opts) {
						var json = Ext.decode(response.responseText);
						Ext.ux.Toast.msg("温馨提示", json.message);
					}
				});
				// if(!Ext.isEmpty(values.configPower)){
				// 	params = Ext.Object.merge(params,{
				//
				// 	});
				// }
				// form.submit({
				// 	url : url,
				// 	method: 'POST',
				//
				// 	success : function(from, action, json) {
				// 		var goodsManager = Ext.getCmp('configPowerManager'),
				// 			store = goodsManager.getStore();
				// 		Ext.ux.Toast.msg("温馨提示", message + "成功");
				// 		me.close();
				// 		store.load();
				// 	},
				// 	exception : function(from, action, json) {
				// 		Ext.ux.Toast.msg("温馨提示", json.message);
				// 	}
				// });

			}
		}];
		me.callParent([ cfg ]);
	}
});
/**ZW_C_JB_00008_20170512 END **/

Ext.define('MyApp.view.countLimitWindow', {
    extend: 'Ext.window.Window',
    title: '修改最大数额',
    width: 600,
	closeAction: 'hide',
	modal: true,
	defaults: {
		margin: '2 2 2 2'
	},
	form: null,
	getForm: function(){
		var me = this;
		if(me.form==null){
			me.form = Ext.widget('form',{
                    layout: 'column',
					defaults: {
						margin: '10 10 5 5',
						columnWidth: .5,
						 labelWidth: 65,
						xtype: 'textfield'
					},
                    items: [{
					    fieldLabel: '最大数额',
					    allowBlank: false,
					    name: 'configMaxCount',
					    xtype:'numberfield',
					    step:1000
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
			
		form.loadRecord(record);
		me.isUpdate = isUpdate;	
	},
	constructor : function(config) {
		var me = this, cfg = Ext.apply({}, config);
		me.items = [me.getForm()];
		me.buttons = [{
			text:'保存',
			handler: function() {
				var formView = me.getForm(),
					form = formView.getForm(),
					record =form.getRecord();
				configMaxCount = form.findField('configMaxCount').getValue();
				Ext.Ajax.request({
					url : './order/modifyMaxCount.action',
					params:{'id':record.get('id'),
						'configMaxCount':configMaxCount},
					success : function(response, opts) {
						Ext.ux.Toast.msg("温馨提示", "修改成功！");
						var configPowerManager = Ext.getCmp('configPowerManager');
						store = configPowerManager.getStore();
						store.load();
					},
					exception : function(response, opts) {
						var json = Ext.decode(response.responseText);
						Ext.ux.Toast.msg("温馨提示", json.message);
					}
					
				});
				
				me.close();
			}
		}];
		me.callParent([ cfg ]);
	}
});