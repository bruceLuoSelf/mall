var required = '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>';
Ext.define('MyApp.view.anXinBuyWindow', {
	extend : 'Ext.window.Window',
	title : '新增/修改安心买下拉菜单数值',
	width : 600,
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
					labelWidth : 100,
					readOnly : false,
					allowBlank : true,
					xtype : 'textfield'
				},
				items : [{
					xtype : 'hiddenfield',
					name: 'id'
				},{
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
				},Ext.create('Ext.form.ComboBox', {
					fieldLabel: '商品类型',
					allowBlank: false,
					name:'goodsTypeId',
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
					editable: false,
				}),{
					fieldLabel : '选项值',
					allowBlank : true,
					columnWidth : 1,
					emptyText: '例如：1000,2000,5000 多个以逗号分隔。',
					name : 'goldCounts'
				},{
					xtype : 'checkbox',
					boxLabel : '显示第一栏最低价',
					columnWidth : 0.5,
					name: 'showCategory23'
				},
                {
                    columnWidth : 1,
                    xtype:'fieldset',
                    title: '第一栏最低价寄售模式',
                    defaultType: 'textfield',
                    collapsible: true,
                    layout: 'anchor',
                    defaults: {
                        anchor: '100%'
                    },
                    items :[{
                        xtype: 'radiogroup',
                        fieldLabel: '状态',
                        anchor: '50%',
                        allowBlank:false,
                        cls: 'x-check-group-alt',
                        items: [
                            {boxLabel: '开启', name: 'isConsignmentMode', inputValue: true},
                            {boxLabel: '关闭', name: 'isConsignmentMode', inputValue: false, checked: true}
                        ]
                    },{
                        fieldLabel: '开启时间',
                        anchor: '40%',
                        name: 'consignmentStartTime',
                        value:"1:30",
                        regex: /^(0?\d{1}|1\d{1}|2[0-3]):([0-5]\d{1})$/,
                        regexText: "输入值非法，例如输入：1:30",
                        allowBlank:false
                    },{
                        fieldLabel: '结束时间',
                        anchor: '40%',
                        name: 'consignmentEndTime',
                        value:"8:30",
                        regex: /^(0?\d{1}|1\d{1}|2[0-3]):([0-5]\d{1})$/,
                        regexText: "输入值非法，例如输入：8:30",
                        allowBlank:false
                    }]
                },
                {
					xtype : 'fieldset',
					columnWidth : 1,
					title : '服务类型配置',
					defaultType : 'checkbox', // each item will be a checkbox
					defaults : {
						hideEmptyLabel : false,
						allowBlank : true,
						align : 'center',
						height : 30
					},
					items : [{
					        xtype: 'checkboxgroup',
					        fieldLabel: '栏目一',
					        columns: 5,
					        name: 'checkBoxCategoryIcon1',
					        vertical: false,
					        items: [
					            { boxLabel: '<img src="./images/common/service_type_1.png" width="16px" height="16px" title="争分夺秒，急你所急">', name: 'categoryIcon1', inputValue: '1' },
					            { boxLabel: '<img src="./images/common/service_type_2.png" width="16px" height="16px" title="商城出品，必属精品;皇冠卖家，热力倾销">', name: 'categoryIcon1', inputValue: '2' },
					            { boxLabel: '<img src="./images/common/service_type_3.png" width="16px" height="16px" title="售后因商品问题一律包赔">', name: 'categoryIcon1', inputValue: '3' },
					            { boxLabel: '<img src="./images/common/service_type_4.png" width="16px" height="16px" title="购买此商品，5173将代您向中国慈善事业捐赠1分钱">', name: 'categoryIcon1', inputValue: '4' },
								{ boxLabel: '<img src="./images/common/service_type_5.png" width="16px" height="16px" title="闪电发货无拍卖行风险和交易金额限制">', name: 'categoryIcon1', inputValue: '5' }
							]
					    },{
					        xtype: 'checkboxgroup',
					        fieldLabel: '栏目二',
					        columns: 5,
					        name: 'checkBoxCategoryIcon2',
					        vertical: false,
					        items: [
					            { boxLabel: '<img src="./images/common/service_type_1.png" width="16px" height="16px" title="争分夺秒，急你所急">', name: 'categoryIcon2', inputValue: '1' },
					            { boxLabel: '<img src="./images/common/service_type_2.png" width="16px" height="16px" title="商城出品，必属精品;皇冠卖家，热力倾销">', name: 'categoryIcon2', inputValue: '2' },
					            { boxLabel: '<img src="./images/common/service_type_3.png" width="16px" height="16px" title="售后因商品问题一律包赔">', name: 'categoryIcon2', inputValue: '3' },
					            { boxLabel: '<img src="./images/common/service_type_4.png" width="16px" height="16px" title="购买此商品，5173将代您向中国慈善事业捐赠1分钱">', name: 'categoryIcon2', inputValue: '4' },
								{ boxLabel: '<img src="./images/common/service_type_5.png" width="16px" height="16px" title="闪电发货无拍卖行风险和交易金额限制">', name: 'categoryIcon1', inputValue: '5' }
					        ]
					    }/*,{
					        xtype: 'checkboxgroup',
					        fieldLabel: '栏目三',
					        columns: 4,
					        name: 'checkBoxCategoryIcon3',
					        vertical: false,
					        items: [
					            { boxLabel: '<img src="./images/common/service_type_1.png" width="16px" height="16px" title="争分夺秒，急你所急">', name: 'categoryIcon3', inputValue: '1' },
					            { boxLabel: '<img src="./images/common/service_type_2.png" width="16px" height="16px" title="商城出品，必属精品;皇冠卖家，热力倾销">', name: 'categoryIcon3', inputValue: '2' },
					            { boxLabel: '<img src="./images/common/service_type_3.png" width="16px" height="16px" title="售后因商品问题一律包赔">', name: 'categoryIcon3', inputValue: '3' },
					            { boxLabel: '<img src="./images/common/service_type_4.png" width="16px" height="16px" title="购买此商品，5173将代您向中国慈善事业捐赠1分钱">', name: 'categoryIcon3', inputValue: '4' },
					        ]
					    }*/]
				}],
				buttons : [{
					text : '保存',
					handler : function() {
						var form = me.getForm().getForm(), 
							//record = form.getRecord(),
							url = './hotrecommend/addConfig.action', 
							message = '新增';
							//form.updateRecord(record);
						if (form.isValid()) {
							var categoryIcon1_record = "",
								categoryIcon2_record = "",
								categoryIcon3_record = "";
							checkBoxCategoryIcon1 = form.findField('checkBoxCategoryIcon1').getChecked();
							checkBoxCategoryIcon2 = form.findField('checkBoxCategoryIcon2').getChecked();
							//checkBoxCategoryIcon3 = form.findField('checkBoxCategoryIcon3').getChecked();
							showCategory23 = form.findField('showCategory23').getValue();
							//console.log(showCategory23);
							
							for (var i = 0, j = checkBoxCategoryIcon1.length; i <j ; i++) {
								categoryIcon1_record += checkBoxCategoryIcon1[i].getSubmitValue();
								if (i != (j -1)) {
									categoryIcon1_record += ",";
								}
							}
							for (var i = 0, j = checkBoxCategoryIcon2.length; i <j ; i++) {
								categoryIcon2_record += checkBoxCategoryIcon2[i].getSubmitValue();
								if (i != (j -1)) {
									categoryIcon2_record += ",";
								}
							}
							/*for (var i = 0, j = checkBoxCategoryIcon3.length; i <j ; i++) {
								categoryIcon3_record += checkBoxCategoryIcon3[i].getSubmitValue();
								if (i != (j -1)) {
									categoryIcon3_record += ",";
								}
							}*/

                            var isConsignmentMode = form.findField('isConsignmentMode').getValue();
                            var consignmentStartTime = form.findField('consignmentStartTime').getValue();
                            var consignmentEndTime = form.findField('consignmentEndTime').getValue();
                            //console.log("寄售参数："+isConsignmentMode+"/"+consignmentStartTime+"/"+consignmentEndTime);
							var record = {
									// "gameId" : form.findField('gameName').getValue(),
									"gameName" : form.findField('gameName').getRawValue(),
									"goodsTypeId" : form.findField('goodsTypeId').getValue(),
									"goodsTypeName" : form.findField('goodsTypeId').getRawValue(),
									"goldCounts" : form.findField('goldCounts').getValue(),
									"categoryIcon1" : categoryIcon1_record,
									"categoryIcon2" : categoryIcon2_record,
									//"categoryIcon3" : categoryIcon3_record,
									"showCategory23":showCategory23,
                                    "isConsignmentMode": isConsignmentMode,
                                    "consignmentStartTime": consignmentStartTime,
                                    "consignmentEndTime": consignmentEndTime
								};
							if (me.isUpdate) {
								url = './hotrecommend/updateConfig.action';
								message = '修改';
								record = {
										"id":form.findField('id').getValue(),
                                        "gameName" : form.findField('gameName').getRawValue(),
										"goodsTypeName" : form.findField('goodsTypeId').getRawValue(),
										"goldCounts" : form.findField('goldCounts').getValue(),
										"categoryIcon1" : categoryIcon1_record,
										"categoryIcon2" : categoryIcon2_record,
										"categoryIcon3" : categoryIcon3_record,
										"showCategory23":showCategory23,
                                        "isConsignmentMode": isConsignmentMode,
                                        "consignmentStartTime": consignmentStartTime,
                                        "consignmentEndTime": consignmentEndTime
									};
							}
							Ext.Ajax.request({
								url : url,
								method : 'POST',
								jsonData : {
									'config' : record
								},
								success : function(response, opts) {
									var anXinBuyManager = Ext
											.getCmp('anXinBuyManager'), store = anXinBuyManager
											.getStore();
									/*anXinBuyManager.
									var grid = this.getAnXinBuyGrid(), selModel = grid.getSelectionModel(), selRecords = selModel
									.getSelection(), window;*/
									var selgrid = anXinBuyManager.getAnXinBuyGrid(), selModel = selgrid.getSelectionModel();
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
			checkBoxCategoryIcon1 = form.findField('checkBoxCategoryIcon1');
			checkBoxCategoryIcon2 = form.findField('checkBoxCategoryIcon2');
			//checkBoxCategoryIcon3 = form.findField('checkBoxCategoryIcon3');
			showCategory23 = form.findField('showCategory23');
			
			if(record.data.categoryIcon1!=""){
				var categoryIcon1_string = record.data.categoryIcon1.split(",");
				checkBoxCategoryIcon1.setValue({categoryIcon1:categoryIcon1_string});
			}
			
			if(record.data.categoryIcon2!=""){
				var categoryIcon2_string = record.data.categoryIcon2.split(",");
				checkBoxCategoryIcon2.setValue({categoryIcon2:categoryIcon2_string});
			}
			
			/*if(record.data.categoryIcon3!=""){
				var categoryIcon3_string = record.data.categoryIcon3.split(",");
				checkBoxCategoryIcon3.setValue({categoryIcon3:categoryIcon3_string});
			}*/
			
			//console.log(record.data.showCategory23);
			if(record.data.showCategory23){
				showCategory23.setValue(true);
			}else{
				showCategory23.setValue(false);
			}


            var isConsignmentMode = form.findField('isConsignmentMode');
            var consignmentStartTime = form.findField('consignmentStartTime');
            var consignmentEndTime = form.findField('consignmentEndTime');
            if (record.data.isConsignmentMode) {
                isConsignmentMode.setValue(true);
            }
            if (record.data.consignmentStartTime != "") {
                consignmentStartTime.setValue(record.data.consignmentStartTime);
            }
            if (record.data.consignmentEndTime != "") {
                consignmentEndTime.setValue(record.data.consignmentEndTime);
            }
		}
	},
	constructor : function(config) {
		var me = this, cfg = Ext.apply({}, config);
		me.items = [me.getForm()]
		me.callParent([cfg]);
	}
});

Ext.define('MyApp.view.anXinBuyGrid', {
	extend : 'Ext.grid.Panel',
	id: 'anXinBuyGrid',
	header : false,
	autoScroll : true,
	columnLines : true,
	renderCategoryIcon:function(value) {
		console.log(value);
		if (value == null || value == "")
			return "";
		var arr = value.split(",");
		var html = "";
		for (var i = 0, j = arr.length; i < j; i++) {
			if (parseInt(arr[i]) == 1) {
				html += '<img src="./images/common/service_type_1.png" width="16" height="16" title="争分夺秒，急你所急"/>';
			} else if (parseInt(arr[i]) == 2) {
				html += '<img src="./images/common/service_type_2.png" width="16" height="16" title="商城出品，必属精品;皇冠卖家，热力倾销"/>';
			} else if (parseInt(arr[i]) == 3) {
				html += '<img src="./images/common/service_type_3.png" width="16" height="16" title="售后因商品问题一律包赔"/>';
			} else if (parseInt(arr[i]) == 4) {
				html += '<img src="./images/common/service_type_4.png" width="16" height="16" title="购买此商品，5173将代您向中国慈善事业捐赠1分钱"/>';
			} else if (parseInt(arr[i]) == 5) {
				html += '<img src="./images/common/service_type_5.png" width="16" height="16" title="闪电发货无拍卖行风险和交易金额限制"/>';
			}
		}
		return html;
	},
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
			dataIndex : 'goodsTypeName',
			sortable : false,
			width : 160,
			align : 'center',
			text : '商品类型'
		}, {
			dataIndex : 'goldCounts',
			name : 'goldCountsTxt',
			sortable : false,
			width : 250,
			align : 'center',
			text : '下拉列表显示数值选项'
		}, {
			dataIndex : 'categoryIcon1',
			sortable : false,
			width : 150,
			align : 'center',
			text : '栏目一图标选项',
			renderer : this.renderCategoryIcon
		}, {
			dataIndex : 'categoryIcon2',
			sortable : false,
			width : 150,
			align : 'center',
			text : '栏目二图标选项',
			renderer : this.renderCategoryIcon
		},/*{
			dataIndex : 'categoryIcon3',
			sortable : false,
			width : 150,
			align : 'center',
			text : '栏目三图标选项',
			renderer : me.renderCategoryIcon
		},*/{
			dataIndex : 'showCategory23',
			sortable : false,
			width : 150,
			align : 'center',
			text : '第一栏最低价是否显示',
			renderer: function(value){
				if (value) return "显示";
				else return "不显示";
			}
		},{
            dataIndex: 'isConsignmentMode',
            sortable : false,
            width : 150,
            align : 'center',
            text : '寄售模式',
            renderer: function(value){
                if (value) return "开启";
                else return "关闭";
            }
        }];
		me.selModel = Ext.create('Ext.selection.CheckboxModel', {
					allowDeselect : true,
					mode : 'MULTI'
				})
		me.callParent([cfg]);
	}
});

/*
 * 安心买配置管理页面
 */
Ext.define('MyApp.view.anXinBuyManager', {
	extend : 'Ext.panel.Panel',
	id : 'anXinBuyManager',
	closable : true,
	title : '安心买配置管理',
	autoScroll : false,
	listeners : {
		'resize' : function() {
			this.anXinBuyGrid
					.setHeight(window.document.body.offsetHeight - 172);
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
											fn : me.addAnXinBuy,
											scope : me
										}
									}
								}, '-', {
									text : '修改',
									listeners : {
										click : {
											fn : me.modifyAnXinBuy,
											scope : me
										}
									}
								}, '-', {
									text : '删除',
									listeners : {
										click : {
											fn : me.deleteAnXinBuy,
											scope : me
										}
									}
								}]
					});
		}
		return me.toolbar;
	},
	anXinBuyWindow : null,
	getAnXinBuyWindow : function() {
		if (this.anXinBuyWindow == null) {
			this.anXinBuyWindow = new MyApp.view.anXinBuyWindow();
		}
		return this.anXinBuyWindow;
	},
	// 新增游戏下拉数值信息
	addAnXinBuy : function(button, e, eOpts) {
		var window = this.getAnXinBuyWindow();
		window.bindData(Ext.create('MyApp.model.HotRecommendConfigModel'), false);
		window.getForm().getForm().findField('gameName').readOnly=false;
		window.getForm().getForm().findField('goodsTypeId').readOnly=false;
		window.show();
	},
	// 修改游戏下拉数值信息
	modifyAnXinBuy : function(button, e, eOpts) {
		var grid = this.getAnXinBuyGrid(), selModel = grid.getSelectionModel(), selRecords = selModel
				.getSelection(), window;
		window = this.getAnXinBuyWindow();
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
		window.getForm().getForm().findField('goodsTypeId').readOnly=true;

		window.show();
	},
	deleteAnXinBuy : function(button, e, eOpts) {
		var grid = this.getAnXinBuyGrid(), selModel = grid.getSelectionModel(), selRecords = selModel
				.getSelection(), gameGoldCountIds = [];
		if (selRecords == null || selRecords.length <= 0) {
			Ext.ux.Toast.msg("温馨提示", "请先选择要删除的信息");
			return;
		}
		Ext.Array.each(selRecords, function(record, index, records) {
			gameGoldCountIds.push(record.get('id'));
				});
		Ext.MessageBox.confirm('温馨提示', '确定删除该信息吗？', function(btn) {
					if (btn == 'yes') {
						Ext.Ajax.request({
									url : './hotrecommend/deleteConfig.action',
									params : {
										'ids' : gameGoldCountIds
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
					items : [{
						xtype: 'gameselectorsellersetting',
						itemId : 'MyApp_view_goods_gamelink_ID',
						columnWidth: .7,
						allowBlank: true,
						fieldLabel: '游戏名称',
						gameChanged:function(){
							me.getStore().reload();
						}
					},/*Ext.create('Ext.form.ComboBox', {
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
					})*/],
				}
				]
			})  
		}

		return this.queryForm;
	},
	store : null,
	getStore : function() {
		var me = this;
		if (me.store == null) {
			me.store = Ext.create('MyApp.store.HotRecommendConfigStore', {
						autoLoad : true,
						listeners : {
							beforeload : function(store, operation, eOpts) {
								var queryForm = me.getQueryForm();
								var gameName = queryForm.getForm().findField("gameName");
								if (queryForm != null) {
									Ext.apply(operation, {
												params : {
													'config.gameName' : gameName.getRawValue()
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
	anXinBuyGrid : null,
	getAnXinBuyGrid : function() {
		var me = this;
		if (Ext.isEmpty(me.anXinBuyGrid)) {
			me.anXinBuyGrid = Ext.create('MyApp.view.anXinBuyGrid', {
						store : me.getStore(),
						dockedItems : [me.getToolbar(), me.getPagingToolbar()]
					});
		}
		return me.anXinBuyGrid;
	},
	initComponent : function() {
		var me = this;
		Ext.applyIf(me, {
					items : [me.getQueryForm(), me.getAnXinBuyGrid()]
				});
		me.callParent(arguments);
	}
});