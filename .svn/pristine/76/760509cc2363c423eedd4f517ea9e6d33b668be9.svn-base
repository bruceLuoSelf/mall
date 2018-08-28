Ext.define('MyApp.model.GoodsDiscountModel', {
    extend: 'Ext.data.Model',
    fields: [{
		name: 'goldCount',
		type: 'int'
	},{
		name: 'discount',
		type: 'float'
	}]
});

Ext.define('MyApp.view.goodsWindow', {
    extend: 'Ext.window.Window',
    title: '修改商品',
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
	addButton : null,
	getAddButton : function(){
		var me = this;
		if(this.addButton==null){
			this.addButton = Ext.create('Ext.Button', {
				text: '添加',
				handler: function() {
					var rec = Ext.create('MyApp.model.GoodsDiscountModel',{
						color: ''
					}), edit = me.getEditer(),store = me.getGrid().getStore(),flag;
					store.each(function(record, index){
						var goldCount = record.get('goldCount'),
							discount = record.get('discount');
						if(Ext.isEmpty(goldCount)||Ext.isEmpty(discount)){
							Ext.ux.Toast.msg("温馨提示", "请编辑完第"+(index+1)+"行的内容");
							flag=true;
						}
					});
					if(flag){
						return;
					}
					edit.cancelEdit();
					store.insert(store.getCount(), rec);
					edit.startEditByPosition({
						row: 0,
					column: 0
					});
				}
			});
		}
		return this.addButton;
	},
	actionColumn : null,
	getActionColumn : function(){
		var me = this;
		if(this.actionColumn==null){
			this.actionColumn = Ext.create('Ext.grid.column.Action', {
				text: '操作',
				flex: 2,
				items: [{
					icon: 'images/common/delete.png',
					tooltip: '删除',
					handler: function(grid, rowIndex, colIndex) {
						Ext.MessageBox.confirm('温馨提示', '确定删除该商品折扣信息吗？', function(btn){
							if(btn == 'yes'){
								var rec = grid.getStore().getAt(rowIndex);
								grid.getStore().removeAt(rowIndex);
				    		}else{
				    			return;
				    		}
						});
					}
				}]
			});
		}
		return this.actionColumn;
	},
	grid: null,
	getGrid: function(){
		var me = this;
		if(me.grid==null){
			var store = Ext.create('Ext.data.ArrayStore', {
				model: 'MyApp.model.GoodsDiscountModel'
			});
			me.grid = Ext.create('Ext.grid.Panel', {
			    title: '商品折扣',
			    columnLines: true,
			    store: store,
			    columns: [{
					xtype: 'rownumberer'
				},{ 
			    	header: '购买商品数',
			    	dataIndex: 'goldCount',
			    	sortable: false,
			    	flex: 1,
			    	editor: {
			    		xtype: 'numberfield',
			    		minValue: 0,
			    		name: 'goldCount'
			    	}
			    },{ 
			    	header: '享受折扣',  
			    	dataIndex: 'discount',
			    	sortable: false,
			    	flex: 1,
			    	editor: {
			    		xtype: 'numberfield',
			    		name: 'discount',
			    		minValue: 0
			    	}
			    },me.getActionColumn()],
			    plugins: [me.getEditer()],
				tbar: [me.getAddButton()],
			    height: 200
			});
		}
		return me.grid;
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
					    fieldLabel: '商品名称',
					    allowBlank: false,
					    name: 'title'
					},DataDictionary.getDataDictionaryCombo('goodsCat',{
    					fieldLabel: '所属栏目',
    					name: 'goodsCat',
    					columnWidth: .5,
    					labelWidth: 65,
    					editable: false
    				}),{
    					xtype: 'gamelinkselector',
    					itemId : 'MyApp_view_goods_gamelink_ID',
    					columnWidth: 1,
    					allowBlank: false,
    					fieldLabel: '游戏属性'
    				},{
						fieldLabel: '商品类型',
						columnWidth: .45,
						labelWidth: 65,
						allowBlank: false,
						name: 'goodsTypeName',
						editable: false,
						readOnly: true
					},{
					    fieldLabel: '单位',
					    columnWidth: .45,
					    // allowBlank: false,
					    name: 'moneyName',
						editable: false,
						readOnly: true
					},{
					    xtype: 'numberfield',
						fieldLabel: '单价',
					    allowBlank: false,
					    labelWidth: 65,
					    columnWidth: .45,
					    decimalPrecision: 5,
					    step: 0.00001,
					    name: 'unitPrice',
					    validator: function(value){
					    	return value>0?true:"单价必须大于0";
					    }
					},{
					    xtype: 'numberfield',
					    fieldLabel: '发货速度<br/>&nbsp;&nbsp;(分钟)',
					    columnWidth: .333,
					    labelWidth: 85,
					    name: 'deliveryTime',
					    minValue: 0
					}/*,{
					    xtype: 'filefield',
					    name: 'imageUrls',
					    columnWidth: 1,
					    allowBlank: false,
					    vtype: 'image',
					    fieldLabel: '图片地址'
					}*/]
                });
		}
		return me.form;
	},
	isUpdate: null,
	bindData: function(record,isUpdate){
		var me = this,
			formView = me.getForm()
			form = formView.getForm(),
			goodsCat = form.findField('goodsCat'),
			gameProp = formView.getComponent('MyApp_view_goods_gamelink_ID'),
			store = me.getGrid().getStore();
		form.reset();
		form.loadRecord(record);
		me.isUpdate = isUpdate;
		if(!isUpdate){
			goodsCat.setDisabled(false);
			gameProp.setDisabled(false);
			store.loadData([]);
			form.reset();
		}else{
			goodsCat.setDisabled(true);
			gameProp.setDisabled(true);
			Ext.Ajax.request({
				url : './goods/queryGoodsDiscount.action',
				params: {'id': record.get('id')},
				success : function(response, opts) {
					var json = Ext.decode(response.responseText);
					store.loadData(json.goodsDiscountList);
				},
				exception : function(response, opts) {
					var json = Ext.decode(response.responseText);
					Ext.ux.Toast.msg("温馨提示", json.message);
				}
			});
		}
	},
	getValue: function(){
		var me = this,store = me.getGrid().getStore(),discountList = [],flag = false;
		store.each(function(record, i){
			var index = i+1;
			if(Ext.isEmpty(record.get('goldCount'))){
				Ext.ux.Toast.msg("温馨提示", "请配置商品折扣列表中第"+index+"行的金币数信息！");
				flag = true;
			}
			if(Ext.isEmpty(record.get('discount'))){
				Ext.ux.Toast.msg("温馨提示", "请配置商品折扣列表中第"+index+"行的折扣信息！");
				flag = true;
			}
			var j=i+1,length = store.getCount();
			while(j<length){
				var recordStr = Ext.Object.toQueryString(record.data),
					inerRecordStr = Ext.Object.toQueryString(store.getAt(j).data);
				j++;
				if(recordStr==inerRecordStr){
					Ext.ux.Toast.msg("温馨提示", "商品折扣列表中第"+index+"行与第"+j+"行配置一样");
					flag = true;
				}
			}
			discountList.push(record.data);
		});
		if(flag){
			return;
		}
		/*if(discountList.length<=0){
			Ext.Msg.alert("温馨提示", "请配置商品折扣！");
			return;
		}*/
		return discountList;
	},
	constructor : function(config) {
		var me = this, cfg = Ext.apply({}, config);
		me.items = [me.getForm(),me.getGrid()];
		me.buttons = [{
			text:'保存',
			handler: function() {
				var formView = me.getForm(),
					form = formView.getForm(),
					gameName = form.findField('gameName'),
					goodsTypeId = form.findField('goodsTypeId'),
					goodsTypeName = form.findField('goodsTypeName'),
					record = form.getRecord(),
					url = './goods/addGoods.action',
					message = '新增';
				if(!form.isValid()){
					return;
				}
				form.updateRecord(record);
				var discountList = me.getValue(),
				params = {
					'goods.title': record.get('title'),
					'goods.goodsCat': record.get('goodsCat'),
					'goods.gameName': gameName.getRawValue(),
					'goods.goodsTypeName': goodsTypeName.getRawValue(),
					'goods.moneyName': record.get('moneyName'),
					'goods.region': record.get('region'),
					'goods.server': record.get('server'),
					'goods.gameRace': record.get('gameRace'),
					'goods.unitPrice': record.get('unitPrice'),
					'goods.deliveryTime': record.get('deliveryTime'),
					'discountList': Ext.encode(discountList),
					'state':Ext.getCmp('p').getValue()["check"]
				};
				if(me.isUpdate){
					url = './goods/modifyGoods.action';
					message = '修改';
					Ext.Object.merge(params,{
						'goods.id': record.get('id')
					});
				}
				form.submit({
					url : url,
					method: 'POST',
					params: params,
					success : function(from, action, json) {
						var goodsManager = Ext.getCmp('goodsManager'),
							store = goodsManager.getStore();
						Ext.ux.Toast.msg("温馨提示", message + "成功");
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
Ext.define('MyApp.view.goodsWindow1', {
    extend: 'Ext.window.Window',
    title: '新增商品',
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
	addButton : null,
	getAddButton : function(){
		var me = this;
		if(this.addButton==null){
			this.addButton = Ext.create('Ext.Button', {
				text: '添加',
				handler: function() {
					var rec = Ext.create('MyApp.model.GoodsDiscountModel',{
						color: ''
					}), edit = me.getEditer(),store = me.getGrid().getStore(),flag;
					store.each(function(record, index){
						var goldCount = record.get('goldCount'),
							discount = record.get('discount');
						if(Ext.isEmpty(goldCount)||Ext.isEmpty(discount)){
							Ext.ux.Toast.msg("温馨提示", "请编辑完第"+(index+1)+"行的内容");
							flag=true;
						}
					});
					if(flag){
						return;
					}
					edit.cancelEdit();
					store.insert(store.getCount(), rec);
					edit.startEditByPosition({
						row: 0,
					column: 0
					});
				}
			});
		}
		return this.addButton;
	},
	actionColumn : null,
	getActionColumn : function(){
		var me = this;
		if(this.actionColumn==null){
			this.actionColumn = Ext.create('Ext.grid.column.Action', {
				text: '操作',
				flex: 2,
				items: [{
					icon: 'images/common/delete.png',
					tooltip: '删除',
					handler: function(grid, rowIndex, colIndex) {
						Ext.MessageBox.confirm('温馨提示', '确定删除该商品折扣信息吗？', function(btn){
							if(btn == 'yes'){
								var rec = grid.getStore().getAt(rowIndex);
								grid.getStore().removeAt(rowIndex);
				    		}else{
				    			return;
				    		}
						});
					}
				}]
			});
		}
		return this.actionColumn;
	},
	grid: null,
	getGrid: function(){
		var me = this;
		if(me.grid==null){
			var store = Ext.create('Ext.data.ArrayStore', {
				model: 'MyApp.model.GoodsDiscountModel'
			});
			me.grid = Ext.create('Ext.grid.Panel', {
			    title: '商品折扣',
			    columnLines: true,
			    store: store,
			    columns: [{
					xtype: 'rownumberer'
				},{ 
			    	header: '购买商品数',
			    	dataIndex: 'goldCount',
			    	sortable: false,
			    	flex: 1,
			    	editor: {
			    		xtype: 'numberfield',
			    		minValue: 0,
			    		name: 'goldCount'
			    	}
			    },{ 
			    	header: '享受折扣',  
			    	dataIndex: 'discount',
			    	sortable: false,
			    	flex: 1,
			    	editor: {
			    		xtype: 'numberfield',
			    		name: 'discount',
			    		minValue: 0
			    	}
			    },me.getActionColumn()],
			    plugins: [me.getEditer()],
				tbar: [me.getAddButton()],
			    height: 200
			});
		}
		return me.grid;
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
					    fieldLabel: '商品名称',
					    allowBlank: false,
					    name: 'title'
					},DataDictionary.getDataDictionaryCombo('goodsCat',{
    					fieldLabel: '所属栏目',
    					name: 'goodsCat',
    					columnWidth: .5,
    					labelWidth: 65,
    					editable: false
    				}),{
    					xtype: 'gamelinkselector',
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
    				},Ext.create('Ext.form.ComboBox', {
						fieldLabel: '商品类型',
						allowBlank: false,
						name: 'goodsTypeId',
						columnWidth: 0.5,
						labelWidth: 65,
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
						editable: false,
						listeners: {
							'change' : function(ths, the, eOpts) {
								var form = me.getForm().getForm();
								var categoryCombo = form.findField("goodsTypeId");
								var store =categoryCombo.getStore();
								var record  = store.findRecord('goodsTypeName', categoryCombo.getRawValue());
								if(record!=null){
									var unitName = record.get("unitName");
									var moneyName = form.findField("moneyName");
									moneyName.setValue(unitName);
								}

							}
						}
					}),{
					    fieldLabel: '单位',
					    columnWidth: .45,
					    // allowBlank: false,
					    name: 'moneyName',
						readOnly:true
					},{
					    xtype: 'numberfield',
					    fieldLabel: '单价',
					    allowBlank: false,
					    labelWidth: 65,
					    columnWidth: .45,
					    decimalPrecision: 5,
					    step: 0.00001,
					    name: 'unitPrice',
					    validator: function(value){
					    	return value>0?true:"单价必须大于0";
					    }
					},{
					    xtype: 'numberfield',
					    fieldLabel: '发货速度<br/>&nbsp;&nbsp;(分钟)',
					    columnWidth: .333,
					    labelWidth: 85,
					    name: 'deliveryTime',
					    minValue: 0
					}/*,{
					    xtype: 'filefield',
					    name: 'imageUrls',
					    columnWidth: 1,
					    allowBlank: false,
					    vtype: 'image',
					    fieldLabel: '图片地址'
					}*/]
                });
		}
		return me.form;
	},
	isUpdate: null,
	bindData: function(record,isUpdate){
		var me = this,
			formView = me.getForm();
		var form = formView.getForm(),
			goodsCat = form.findField('goodsCat'),
			gameProp = formView.getComponent('MyApp_view_goods_gamelink_ID'),
			store = me.getGrid().getStore();
		form.reset();
		form.loadRecord(record);
		me.isUpdate = isUpdate;
		if(!isUpdate){
			goodsCat.setDisabled(false);
			gameProp.setDisabled(false);
			store.loadData([]);
			form.reset();
		}else{
			goodsCat.setDisabled(true);
			gameProp.setDisabled(true);
			Ext.Ajax.request({
				url : './goods/queryGoodsDiscount.action',
				params: {'id': record.get('id')},
				success : function(response, opts) {
					var json = Ext.decode(response.responseText);
					store.loadData(json.goodsDiscountList);
				},
				exception : function(response, opts) {
					var json = Ext.decode(response.responseText);
					Ext.ux.Toast.msg("温馨提示", json.message);
				}
			});
		}
	},
	getValue: function(){
		var me = this,store = me.getGrid().getStore(),discountList = [],flag = false;
		store.each(function(record, i){
			var index = i+1;
			if(Ext.isEmpty(record.get('goldCount'))){
				Ext.ux.Toast.msg("温馨提示", "请配置商品折扣列表中第"+index+"行的金币数信息！");
				flag = true;
			}
			if(Ext.isEmpty(record.get('discount'))){
				Ext.ux.Toast.msg("温馨提示", "请配置商品折扣列表中第"+index+"行的折扣信息！");
				flag = true;
			}
			var j=i+1,length = store.getCount();
			while(j<length){
				var recordStr = Ext.Object.toQueryString(record.data),
					inerRecordStr = Ext.Object.toQueryString(store.getAt(j).data);
				j++;
				if(recordStr==inerRecordStr){
					Ext.ux.Toast.msg("温馨提示", "商品折扣列表中第"+index+"行与第"+j+"行配置一样");
					flag = true;
				}
			}
			discountList.push(record.data);
		});
		if(flag){
			return;
		}
		/*if(discountList.length<=0){
			Ext.Msg.alert("温馨提示", "请配置商品折扣！");
			return;
		}*/
		return discountList;
	},
	constructor : function(config) {
		var me = this, cfg = Ext.apply({}, config);
		me.items = [me.getForm(),me.getGrid()];
		me.buttons = [{
			text:'保存',
			handler: function() {
				var formView = me.getForm(),
					form = formView.getForm(),
					gameName = form.findField('gameName'),
					record = form.getRecord(),
					url = './goods/addGoods.action',
					message = '新增';
				if(!form.isValid()){
					return;
				}
				form.updateRecord(record);
				var discountList = me.getValue(),
				params = {
					'goods.title': record.get('title'),
					'goods.goodsCat': record.get('goodsCat'),
					'goods.gameName': gameName.getRawValue(),
					'goods.moneyName': record.get('moneyName'),
					'goods.goodsTypeId': formView.getForm().findField('goodsTypeId').getValue(),
					'goods.goodsTypeName': formView.getForm().findField('goodsTypeId').getRawValue(),
					'goods.region': record.get('region'),
					'goods.server': record.get('server'),
					'goods.gameRace': record.get('gameRace'),
					'goods.unitPrice': record.get('unitPrice'),
					'goods.deliveryTime': record.get('deliveryTime'),
					'discountList': Ext.encode(discountList),
					'state':Ext.getCmp('p').getValue()["check"]
				};
				if(me.isUpdate){
					url = './goods/modifyGoods.action';
					message = '修改';
					Ext.Object.merge(params,{
						'goods.id': record.get('id')
					});
				}
				form.submit({
					url : url,
					method: 'POST',
					params: params,
					success : function(from, action, json) {
						var goodsManager = Ext.getCmp('goodsManager'),
							store = goodsManager.getStore();
						Ext.ux.Toast.msg("温馨提示", message + "成功");
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
Ext.define('MyApp.view.inputWindow', {
    extend: 'Ext.window.Window',
    title: '上传价格',
    width: 500,
	closeAction: 'hide',
	closable:true,
	modal: true,
	form: null,
	getForm: function(){
		var me = this;
		if(me.form==null){
			me.form = Ext.widget('form',{
                    layout: 'column',
					defaults: {
						margin: '5 5 5 5',
						labelWidth: 90
					},
                    items: [{
					    xtype: 'filefield',
					    name: 'priceFile',
					    columnWidth: 1,
					    allowBlank: false,
					    vtype: 'excel',
					    fieldLabel: '文件'
					}]
                });
		}
		return me.form;
	},
	constructor : function(config) {
		var me = this, cfg = Ext.apply({}, config);
		me.items = [me.getForm()];
		me.buttons = [{
			text:'上传',
			handler: function() {
				var form = me.getForm().getForm();
				if(!form.isValid()){
					return;
				}
				form.submit({
					url : './goods/uploadPrice.action',
					method: 'POST',
					params:{
						'state':Ext.getCmp('p').getValue()["check"]
					},
					success : function(from, action, json) {
						Ext.ux.Toast.msg("温馨提示", "上传成功");
						me.close();
						Ext.getCmp('goodsManager').getStore().load();
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

Ext.define('MyApp.view.batchModifyPriceWindow', {
    extend: 'Ext.window.Window',
    title: '批量修改价格',
    width: 300,
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
						labelWidth: 65
					},
                    items: [{
					    xtype: 'numberfield',
					    fieldLabel: '相差价格',
					    allowBlank: false,
					    decimalPrecision: 5,
					    step: 0.00001,
					    name: 'differPrice'
					}]
                });
		}
		return me.form;
	},
	bindData: function(record,isUpdate){
		var me = this,
			formView = me.getForm()
			form = formView.getForm();
		form.reset();
	},
	constructor : function(config) {
		var me = this, cfg = Ext.apply({}, config);
		me.items = [me.getForm()];
		me.buttons = [{
			text:'保存',
			handler: function() {
				var goodsManager = Ext.getCmp('goodsManager'),
					queryForm = goodsManager.getQueryForm(),
					gameName = queryForm.getForm().findField('gameName');
					form = me.getForm().getForm(),
					differPrice = form.findField('differPrice');
				if(!form.isValid()){
					return;
				}
				if (queryForm != null) {
					var values = queryForm.getValues();
					Ext.Ajax.request({
						url : './goods/batchModifyPrice.action',
						params: {
							'goods.title': Ext.String.trim(values.title),
							'goods.gameName': Ext.String.trim(gameName.getRawValue()),
							'goods.region': Ext.String.trim(values.region),
							'goods.server': Ext.String.trim(values.server),
							'differPrice': differPrice.getValue(),
							'goods.goodsTypeName': Ext.String.trim(values.goodsTypeName),
							'state':Ext.getCmp('p').getValue()["check"]
						},
						success : function(response, opts) {
							Ext.ux.Toast.msg("温馨提示", "批量修改价格成功！");
							goodsManager.getStore().load();
							me.close();
						},
						exception : function(response, opts) {
							var json = Ext.decode(response.responseText);
							Ext.ux.Toast.msg("温馨提示", json.message);
						}
					});
				}
			}
		}];
		me.callParent([ cfg ]);
	}
});

/*
 * 商品管理页面
 */
Ext.define('MyApp.view.goodsManager', {
    extend: 'Ext.panel.Panel',
    id: 'goodsManager',
    closable: true,
    title: '价格管理',
    ////////////////////////////////////////////////////
    autoScroll: false,
	listeners:{  
    'resize':function(){
        this.goodsGrid.setHeight(window.document.body.offsetHeight-260);
    	}
	},
	////////////////////////////////////////////////////
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
							fn: me.addGoods,
							scope: me
						}
					}
				},'-',{
					text: '修改',
					listeners: {
						click: {
							fn: me.modifyGoods,
							scope: me
						}
					}
				},'-',{
					text:'删除',
					listeners:{
						click:{
							fn:me.delGoods,
							scope:me
						}
					}
				},'-',{
					text: '启用',
					listeners: {
						click: {
							fn: me.enableGoods,
							scope: me
						}
					}
				},'-',{
					text: '禁用',
					listeners: {
						click: {
							fn: me.disableGoods,
							scope: me
						}
					}
				},'-',{
					text: '批量修改价格',
					listeners: {
						click: {
							fn: me.batchModifyPrice,
							scope: me
						}
					}
				},'-',{
					text: '上传价格',
					listeners: {
						click: {
							fn: me.input,
							scope: me
						}
					}
				},'-',{
					xtype:'radiogroup',
					columns:1,
					id:'p',
					items:[{
						boxLabel:'开启',
						name:'check',
						inputValue:'1'
					},{
						boxLabel:'关闭',
						name:'check',
						inputValue:'0',
						checked:true
					}]
				}]
			});
		}
		return me.toolbar;
	},
	goodsWindow: null,
	getGoodsWindow: function(){
		if(this.goodsWindow == null){
			this.goodsWindow = new MyApp.view.goodsWindow();
		}
		return this.goodsWindow;
	},
	goodsWindow1: null,
	getGoodsWindow1: function(){
		if(this.goodsWindow1 == null){
			this.goodsWindow1 = new MyApp.view.goodsWindow1();
		}
		return this.goodsWindow1;
	},
	batchModifyPriceWindow: null,
	getBatchModifyPriceWindow: function(){
		if(this.batchModifyPriceWindow == null){
			this.batchModifyPriceWindow = new MyApp.view.batchModifyPriceWindow();
		}
		return this.batchModifyPriceWindow;
	},
	inputWindow: null,
	getInputWindow: function(){
		if(this.inputWindow == null){
			this.inputWindow = new MyApp.view.inputWindow();
		}
		return this.inputWindow;
	},
	// 批量修改价格
	batchModifyPrice: function(button, e, eOpts) {
		var window = this.getBatchModifyPriceWindow();
		window.show();
    },
	// 新增商品信息
    addGoods: function(button, e, eOpts) {
		var window = this.getGoodsWindow1();
		window.bindData(Ext.create('MyApp.model.GoodsModel'), false);
		window.show();
	},
    //表格导入
    input: function(button, e, eOpts) {
		var window = this.getInputWindow();
		window.show();
    },
    // 修改商品信息
    modifyGoods: function(button, e, eOpts) {
		var grid = this.getGoodsGrid(),
			selModel = grid.getSelectionModel(),
			selRecords = selModel.getSelection(),
			window;
			if(selRecords == null||selRecords.length<=0){
                Ext.ux.Toast.msg("温馨提示", "请先选择要修改的商品信息");
                return;
            } else if (selRecords.length>1) {
                Ext.ux.Toast.msg("温馨提示", "请选择<b style='color:red'>1</b>条您要修改的商品信息！");
                return;
            }
			if(selRecords[0].get('gameName')=="魔兽世界(国服)"){
				window = this.getGoodsWindow1();
			}else {
				window = this.getGoodsWindow();
			}
    	
    	window.bindData(selRecords[0], true);
		window.show();
	},
    //删除商品信息
    delGoods:function(button, e, eOpts){
    	var grid = this.getGoodsGrid(),
    		selModel = grid.getSelectionModel(),
    		selRecords = selModel.getSelection();
    	if(selRecords == null||selRecords.length<=0){
    		Ext.ux.Toast.msg("温馨提示", "请先选择要删除的商品");
    		return;
    	}
    	//var record = selRecords[0];
        var ids = [];
        for (var i = 0, j = selRecords.length; i < j; i++) {
            ids.push(selRecords[i].get("id"));
        }
    	Ext.MessageBox.confirm('温馨提示', '确定删除该商品信息吗？', function(btn){
			if(btn == 'yes'){
				Ext.Ajax.request({
					url : './goods/delGoods.action',
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
    // 启用商品信息
    enableGoods: function(button, e, eOpts) {
    	var grid = this.getGoodsGrid(),
			selModel = grid.getSelectionModel(),
			selRecords = selModel.getSelection();
    	if(selRecords == null||selRecords.length<=0){
    		Ext.ux.Toast.msg("温馨提示", "请先选择要启用的商品信息");
    		return;
    	}
		/*var record = selRecords[0];
		if(!record.get('isDeleted')){
			Ext.ux.Toast.msg("温馨提示", "该商品已经启用！");
			return;
		}*/
        var ids = [];
        for (var i = 0, j = selRecords.length; i < j; i++) {
            ids.push(selRecords[i].get("id"));
        }
    	Ext.MessageBox.confirm('温馨提示', '确定启用该商品信息吗？', function(btn){
			if(btn == 'yes'){
				Ext.Ajax.request({
					url : './goods/enableGoods.action',
					params: {'ids': ids},
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
    		}else{
    			return;
    		}
		});
    },
    // 禁用商品信息
    disableGoods: function(button, e, eOpts) {
    	var grid = this.getGoodsGrid(),
			selModel = grid.getSelectionModel(),
			selRecords = selModel.getSelection();
    	if(selRecords == null||selRecords.length<=0){
    		Ext.ux.Toast.msg("温馨提示", "请先选择要禁用的商品信息");
    		return;
    	}
		/*var record = selRecords[0];
		if(record.get('isDeleted')){
			Ext.ux.Toast.msg("温馨提示", "该商品已经禁用！");
			return;
		}*/
        var ids = [];
        for (var i = 0, j = selRecords.length; i < j; i++) {
            ids.push(selRecords[i].get("id"));
        }
    	Ext.MessageBox.confirm('温馨提示', '确定禁用该商品信息吗？', function(btn){
			if(btn == 'yes'){
				Ext.Ajax.request({
					url : './goods/disableGoods.action',
					params: {'ids': ids},
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
                layout: 'column',
				defaults: {
					margin: '10 10 10 10',
					columnWidth: .2,
					labelWidth: 80,
					xtype: 'textfield'
				},
                items: [{
				    fieldLabel: '商品名称',
				    name: 'title'
				},DataDictionary.getDataDictionaryCombo('check',{
					fieldLabel: '是否禁用',
					name: 'isDeleted',
					labelWidth: 80,
					editable: false
				},{value: null,display:'全部'}),{
					xtype: 'gamelinkselector',
					itemId : 'MyApp_view_goods_gamelink_ID',
					columnWidth: .5,
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
				}),/*{
                        fieldLabel: '游戏名称',
                        name: 'gameName'
                    },{
                        fieldLabel: '所在区',
                        name: 'region'
                    },{
                        fieldLabel: '所在服',
                        name: 'server'
                    }*/
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
			me.store = Ext.create('MyApp.store.GoodsStore',{
				autoLoad: true,
				listeners: {
					beforeload : function(store, operation, eOpts) {
						var queryForm = me.getQueryForm(),
							gameName = queryForm.getForm().findField('gameName');
						if (queryForm != null) {
							var values = queryForm.getValues(), params = {};
							params = {
								'goods.title': Ext.String.trim(values.title),
								'goods.gameName': Ext.String.trim(gameName.getRawValue()),
								'goods.region': Ext.String.trim(values.region),
								'goods.server': Ext.String.trim(values.server),
								'goods.goodsTypeName': queryForm.getForm().findField('goodsTypeName').getRawValue()
							};
							if(!Ext.isEmpty(values.isDeleted)){
								params = Ext.Object.merge(params, {
									'goods.isDeleted': values.isDeleted
								});
							}
							Ext.apply(operation, {
								params: params
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
	goodsGrid: null,
	getGoodsGrid: function(){
		var me = this;
		if(Ext.isEmpty(me.goodsGrid)){
			me.goodsGrid = Ext.widget('gridpanel',{
                header: false,
                columnLines: true,
                store: me.getStore(),
				columns: [{
					xtype: 'rownumberer'
				},{
					dataIndex: 'title',
					text: '商品名称',
					/*xtype: 'tipcolumn', 
					tipConfig: {
						trackMouse: true,
					    hideDelay: 500 
					},*/
					//tipBodyElement:'MyApp.view.imageGoodsImage',
					align: 'center',
					flex: 1
				},{
                    dataIndex: 'gameName',
                    sortable: false,
                    flex: 1,
                    align: 'center',
                    text: '游戏名称'
                },{
                    dataIndex: 'moneyName',
                    sortable: false,
                    flex: 1,
                    align: 'center',
                    text: '单位'
                },{
					dataIndex: 'goodsTypeName',
					sortable: false,
					flex: 1,
					align: 'center',
					text: '商品类型'
				},{
                	xtype: 'templatecolumn',
                	tpl: new Ext.XTemplate(
            		    '{region}/{server}',
        		        '<tpl if="gameRace!=null">',
        		            '/{gameRace}',
        		        '</tpl>'
            		),
                	//tpl: '{region}/{server}/{gameRace}',
					dataIndex: 'region',
					sortable: false,
					align: 'center',
					text: '游戏<br/>区/服/阵营',
					flex: 2
				},{
					//xtype: 'numbercolumn',
					dataIndex: 'unitPrice',
					flex: 1,
					sortable: false,
					/*renderer: function(v, metaData, record, rowIndex, colIndex, store, view) {
						if(record.get('gameName')=="魔兽世界(国服)"){
							return Ext.util.Format.currency(v, '￥', 5);
						}else
							return Ext.util.Format.currency(v, '￥', 4);
			        },*/
			        align: 'center',
					text: '单价(元)'
				},{
					dataIndex: 'goodsCat',
					text: '所属栏目',
					flex: 1,
					align: 'center',
					renderer: function(value){
						return DataDictionary.rendererSubmitToDisplay(value,'goodsCat');
					}
				},{
					xtype: 'numbercolumn', 
					format:'0',
                    dataIndex: 'sales',
                    flex: 1,
                    align: 'center',
                    text: '销量'
                },{
					xtype: 'numbercolumn', 
					format:'0',
                    dataIndex: 'deliveryTime',
                    flex: 1,
                    align: 'center',
                    text: '发货速度<br/>(分钟)'
                },{
					xtype: 'datecolumn',
					format:'Y-m-d H:i:s',
					dataIndex: 'createTime',
					align: 'center',
					text: '创建时间',
					flex: 1.5
				},{
					xtype: 'datecolumn',
					format:'Y-m-d H:i:s',
					dataIndex: 'lastUpdateTime',
					align: 'center',
					text: '最后更新时间',
					flex: 1.5
				},{
					dataIndex: 'isDeleted',
					text: '是否启用',
					flex: 1,
					align: 'center',
					renderer: function(value, metaData, record, rowIndex, colIndex, store, view) {
		            	return CommonFunction.rendererEnable(value);
		            }
				}],
				dockedItems: [me.getToolbar(),me.getPagingToolbar()],
				selModel: Ext.create('Ext.selection.CheckboxModel', {
					allowDeselect: true,
					mode: 'MULTI'
				})
			});
		}
		return me.goodsGrid;
	},
    initComponent: function() {
        var me = this;
        Ext.applyIf(me, {
            items: [me.getQueryForm(),me.getGoodsGrid()]
        });
        me.callParent(arguments);
    }
});