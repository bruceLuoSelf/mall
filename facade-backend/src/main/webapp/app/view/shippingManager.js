/*
 * 出库订单页面
 */
Ext.define('MyApp.view.ShippingInfoForm',{
	extend: 'Ext.form.Panel',
    layout: 'column',
    border: false,
	defaults: {
		margin: '5 5 5 5',
		columnWidth: .333,
		labelWidth: 100,
		readOnly: true,
		xtype: 'textfield'
	},
	constructor : function(config) {
		var me = this, cfg = Ext.apply({}, config);
		me.items = [{
			name: 'orderId',
			fieldLabel: '订单号'
		},{
			name: 'buyerRole',
			fieldLabel: '买家角色名'
		},{
		    fieldLabel: '买家角色等级',
		    name: 'buyerRoleLevel'
		},{
			name: 'buyer',
			fieldLabel: '买方用户名'
		},{
			name: 'gameProp',
			fieldLabel: '下单游戏/区/服'
		},{
			fieldLabel: '发货游戏/区/服',
			name: 'shippingGameProp',
			header: '发货游戏/区/服',
			dataIndex: 'configCount',
			sortable: false,
			flex: 1,
			editor: {
				name: 'discount',
				minValue: 0,
				listeners: {
					init: function (field, newValue, oldValue, eOpts) {alert(2)
						this.setValue("222");
					},
					focus: function (component, eventObject, eOpts) {alert(1)
						this.setValue("111");
					}
				}
			}
		},{
		    fieldLabel: '买家QQ',
		    name: 'buyerQQ'
		},{
			fieldLabel: '商品类型',
			name: 'goodsTypeName',
		},{
		    fieldLabel: '购买数量',
		    name: 'goldCount'
		},{
		    fieldLabel: '订单价格',
		    name: 'totalPrice'
		},{
		    fieldLabel: '买家电话',
		    name: 'buyerPhoneNumber'
		},{
		    fieldLabel: '订单单价',
		    name: 'unitPrice'
		},DataDictionary.getDataDictionaryCombo('tradeType',{
			fieldLabel: '交易方式',
			name: 'tradeType',
			readOnly: true
		}),{
		    fieldLabel: '交易地点',
		    name: 'tradePlace'
		},{
			fieldLabel: '收货角色数字ID',
			name: 'gameNumberId'
		},{
		    fieldLabel: '订单客服',
		    name: 'realName',
			columnWidth: 1
		},{
			xtype: 'container',
			columnWidth: .15,
			margin: '10 20 10 20',
			items: [{
		        xtype: 'button',
		        text: '复制物品清单',
		        plugins: {
					ptype: 'zeroclipboardplugin',
					targetFun: function(component) {
						var button = component,
							form = button.up('form').getForm(),
							record = form.getRecord();
						var goodsTypeName = (record.get('goodsTypeName')==null||record.get('goodsTypeName')==""||record.get('goodsTypeName')=="游戏币")?"":(record.get('moneyName')+record.get('goodsTypeName'));
						var buyerRoleLevel = "买方角色等级\t:"+record.get('buyerRoleLevel') +'\n';
						var gameNumberId = Ext.isEmpty(record.get('gameNumberId'))?"" : "收货角色数字ID\t:"+record.get('gameNumberId') +'\n';
						return	"订单号\t\t:"+record.get('orderId')+'\n'+
								"商品名称\t:"+record.get('title')+'\n'+
							"下单游戏/区/服\t:"+record.get('gameProp')+'\n'+
								"商品价格\t:"+record.get('totalPrice')+'\n'+
								"买方游戏角色名\t:"+record.get('buyerRole')+'\n'+
							           buyerRoleLevel +gameNumberId+
								"发货数量\t:"+record.get('goldCount')+ goodsTypeName +'\n'+
								"交易地点\t:"+record.get('tradePlace')+'\n'+
							"扩展信息\t:"+record.get('field')+'\n'+
								"您好，服务人员将按照您所填写的角色相关信息进行交易，请您稍等。\n"+
								"友情提示:\n"+
								"①5173服务人员不会以任何理由向您取回已交易商品，不会在游戏中索要您的5173、QQ帐号等私密信息！\n"+
								"②他人发送且需要登陆和支付的链接都是钓鱼网站，一旦支付，资金将有去无回。";
					}
				}
		    }]
		},{
			xtype: 'container',
			columnWidth: .85,
			margin: '10 20 10 20',
			items: [{
		        xtype: 'button',
		        text: '复制物品清单（卖家）',
		        plugins: {
					ptype: 'zeroclipboardplugin',
					targetFun: function(component) {
						var button = component,
							form = button.up('form').getForm(),
							record = form.getRecord();
						var copyshipsellertxet = '';
						Ext.Ajax.request({
							url: './order/queryOrderConfig.action',
							params: {'orderId':record.get('orderId') },
							async: false, 
							success: function(response, opts) {
								var json = Ext.decode(response.responseText);
								c = json.configInfoList;
								Ext.each(c, function(item, index){
									copyshipsellertxet += "订单号\t\t:"+record.get('orderId');
									//if(c.length>1){
										//var aporderIds =String.fromCharCode(65+index);
                                       // var aporderIds = "_"+item.id;
									    copyshipsellertxet +="_"+item.id;
									//}
									var goodsTypeName = (record.get('goodsTypeName')==null||record.get('goodsTypeName')==""||record.get('goodsTypeName')=="游戏币")?"":(record.get('moneyName')+record.get('goodsTypeName'));
									var buyerRoleLevel = "买方角色等级\t:"+record.get('buyerRoleLevel') +'\n';
									var gameNumberId = Ext.isEmpty(record.get('gameNumberId'))?"" : "收货角色数字ID\t:"+record.get('gameNumberId') +'\n';

									var fhString ;
									if(item.gameName=='地下城与勇士'&& record.get('goodsTypeName') =='游戏币'){
										fhString = "发货游戏/区/服\t:"+ item.gameName + '  —>  ' + item.region + '  —>  ' + item.server + (item.gameRace == null ? '' : ' —>' + item.gameRace )  +'\n';
									}else{
										fhString = "";
									}

									copyshipsellertxet +="\n商品名称\t:"+record.get('title')+'\n'+
									"下单游戏/区/服\t:"+record.get('gameProp')+'\n'+
										fhString
										+
									"商品价格\t:"+item.totalPrice+'\n'+
									"买方游戏角色名\t:"+record.get('buyerRole')+'\n'+
										buyerRoleLevel+gameNumberId+
									"发货数量\t:"+item.configGoldCount + goodsTypeName +'\n'+
									"卖家5173账号\t:"+item.loginAccount+'\n'+
									"交易地点\t:"+record.get('tradePlace')+'\n'+
									"扩展信息\t:"+record.get('field')+'\n\n';
								});
								record.set('isCopy',true);
								Ext.Ajax.request({
									url : './order/setCopy.action',
									jsonData: {
										'orderId': record.get('orderId')
									},
									success : function(response, opts) {
									},
									exception : function(response, opts) {
										var json = Ext.decode(response.responseText);
										Ext.ux.Toast.msg("温馨提示", json.message);
									}
								});
							},
							exception : function(response, opts) {
								var json = Ext.decode(response.responseText);
								Ext.ux.Toast.msg("温馨提示", json.message);
							}
						});    
						return copyshipsellertxet;
					}
				}
		    }]
		},{
		    fieldLabel: '备注',
		    columnWidth: 1,
		    name: 'notes'
		}];
		me.callParent([ cfg ]);
	}
});

Ext.define('MyApp.view.SippingInfoWindow',{
	extend: 'Ext.window.Window',
    title: '出库订单明细',
	width: 800,
	border: false,
	padding: '10 10 10 10',
	autoScroll: true,
	closeAction: 'hide',
	modal: true,
	form: null,
	getForm: function(){
		var me = this;
		if(me.form==null){
			me.form = Ext.create('MyApp.view.ShippingInfoForm');
		}
		return me.form;
	},
	grid: null,
	getGrid: function(){
		var me = this;
		if(Ext.isEmpty(me.grid)){
			var store = Ext.create('Ext.data.Store',{
	            model: 'MyApp.model.SellerModel',
	            proxy: {
					type: 'ajax',
					async: false,
					actionMethods: 'POST',
					url: './order/querySellerByOrderId.action',
					reader: {
						type: 'json',
						root: 'sellerList'
					}
				}
	        });
			me.grid = Ext.widget('gridpanel',{
			    header: false,
			    columnLines: true,
			    store: store,
			    columns: [{
			            dataIndex: 'loginAccount',
			            text: '卖家帐号',
			            sortable: false,
			            align: 'center',
			            flex: 1
			        },{
			            dataIndex: 'name',
			            text: '卖家名字',
			            sortable: false,
			            align: 'center',
			            flex: 1
			        },{
			            dataIndex: 'qq',
			            text: '卖家联系QQ',
			            sortable: false,
			            align: 'center',
			            flex: 1
			        },{
			            dataIndex: 'phoneNumber',
			            flex: 1,
			            sortable: false,
			            align: 'center',
			            text: '卖家联系电话'
			        }
			    ]
			});
		}
		return me.grid;
	},
	bindData: function(record){
		var me = this,
			store = me.getGrid().getStore(),
			form = me.getForm().getForm();
		realName = form.findField('realName'),
		form.reset();

		form.loadRecord(record);
		realName.setValue(record.get('servicerInfo').realName);
		store.load({
			params: {
				'orderId': record.get('orderId')
			},
			callback:function(records, options, success){
				fieldShippingGameProp = form.findField('shippingGameProp');
				var shippingGameProp ="";
				store.each(function(record, index){
					shippingGameProp += record.get('shippingGameProp');
					shippingGameProp += '^'
					console.log('bindData:store.each:'+shippingGameProp);
				});
				fieldShippingGameProp.setValue(shippingGameProp);
			}
		});


		// if(store.getCount() == 0){
		// 	store = Ext.create('Ext.data.Store',{
		// 		model: 'MyApp.model.SellerModel',
		// 		proxy: {
		// 			type: 'ajax',
		// 			actionMethods: 'POST',
		// 			async: false,
		// 			url: './order/querySellerByOrderId.action',
		// 			reader: {
		// 				type: 'json',
		// 				root: 'sellerList'
		// 			}
		// 		}
		// 	});
		// 	console.log("store.getCount() == 0")
		// }

	},
    initComponent: function() {
        var me = this;
        Ext.applyIf(me, {
            items: [me.getForm(),me.getGrid()]
        });
        me.callParent(arguments);
    }
});


//库存查询结果表格页面
Ext.define('MyApp.shipping.RepositoryGrid',{
	extend: 'Ext.grid.Panel',
	queryParam: null,
	constructor: function(config){
		var me = this,
			cfg = Ext.apply({}, config);
		me.columns = [{
	        dataIndex: 'loginAccount',
	        align: 'center',
	        flex: 1,
	        renderer: function(value, metaData, record, rowIndex, colIndex, store, view) {	
	        	if(record.get('isShield')){
	        		return "****";
	        	}
	        	else return value;
	        },
	        text: '卖家5173<br/>账号'
	    },{
	        dataIndex: 'gameName',
	        align: 'center',
	        flex: 1,
	        text: '游戏名称'
	    },{
			dataIndex: 'region',
			align: 'center',
			text: '所在区',
			flex: 1
		},{
			dataIndex: 'server',
			align: 'center',
			flex: 1,
			text: '所在服'
		},{
			dataIndex: 'unitPrice',
			align: 'center',
			flex: 0.5,
			text: '单价'
		},{
			dataIndex: 'goldCount',
			align: 'center',
			flex: 1,
			text: '游戏币<br/>数目'
		},{
			dataIndex: 'sellableCount',
			align: 'center',
			flex: 1,
			text: '可销售<br/>库存'
		}];
		me.store = Ext.create('MyApp.store.RepositoryStore',{
			pageSize: 5,
			listeners: {
				beforeload : function(store, operation, eOpts) {
					Ext.apply(operation, {
						params : me.queryParam
					});
				}				
			}
		});
		me.selModel = Ext.create('Ext.selection.CheckboxModel', {
	        mode: 'SINGLE'
	    });
		me.bbar = Ext.create('Ext.PagingToolbar', {
			store: me.store
		});
		me.callParent([cfg]);
	}
});

Ext.define('MyApp.view.ReplaceConfigWindow',{
	extend: 'Ext.window.Window',
	width: 800,
	autoScroll: true,
    title: '重配订单',
	closeAction: 'hide',
	modal: true,
	defaults: {
		margin: '5 5 5 5'
	},
	repositoryGrid : null,
	getRepositoryGrid : function(){
		if(this.repositoryGrid==null){
			this.repositoryGrid = Ext.create('MyApp.shipping.RepositoryGrid',{
				height : 213
			});
		}
		return this.repositoryGrid;
	},
	configRecord: null,
	orderRecord: null,
	currentGrid: null,
	bindData: function(orderRecord, configRecord, currentGrid){
		var me = this,
			repositoryGrid = me.getRepositoryGrid(),
			selectModel = repositoryGrid.getSelectionModel(),
			store = repositoryGrid.getStore();
		me.orderRecord = orderRecord;
		me.configRecord = configRecord;
		me.currentGrid = currentGrid;
		repositoryGrid.queryParam = {
			'repository.gameName': Ext.String.trim(orderRecord.get('gameName')),
			'repository.region': Ext.String.trim(orderRecord.get('region')),
			'repository.server': Ext.String.trim(orderRecord.get('server')),
			'repository.goldCount': configRecord.get('configGoldCount'),
			'repository.unitPrice': orderRecord.get('unitPrice'),
			'repository.gameRace': orderRecord.get('gameRace'),
			'goodsTypeName': orderRecord.get('goodsTypeName'),
			'sellerIsOnline': true,
			'isNeed':true
		};
		// // 栏目3的商品，只获取指定卖家的库存
		// if (configRecord.get("orderInfoEO").goodsCat == 3) {
		// 	repositoryGrid.queryParam['repository.loginAccount'] = configRecord.get("orderInfoEO").sellerLoginAccount;
		// }

		store.loadPage(1);
	},
    initComponent: function() {
        var me = this;
        Ext.applyIf(me, {
            items: [me.getRepositoryGrid()],
            buttons: [{
            	text:'重配',
            	plugins: {
            		ptype: 'buttondisabledplugin',
            		seconds: 5
            	},
            	handler: function() {
            		var grid = me.getRepositoryGrid(),
            			configRecord = me.configRecord,
            			selectModel = grid.getSelectionModel(),
            			selRecords = selectModel.getSelection(),record;
    				if(selRecords == null||selRecords.length<=0){
    					Ext.ux.Toast.msg("温馨提示", "请先选择用于配置的库存信息");
    					return;
    				}
    				repository = selRecords[0];
    				if(Ext.isEmpty(configRecord.get('repositoryInfo'))){
    					configRecord.set('repositoryInfo', null);
    				}
					if(me.orderRecord.get('servicerInfo')==""){
						me.orderRecord.set('servicerInfo',null);
					}
    				if(repository.get('goldCount')<configRecord.get('sellableCount')){
    					Ext.ux.Toast.msg("温馨提示", "请选择库存中可售游戏币数大于要配置的游戏币数");
    					return;
    				}
    				Ext.Ajax.request({
    					url : './order/replaceConfigOrder.action',
    					method: 'POST',
    					jsonData: {
    						'repository': repository.data,
    						'configResult': me.configRecord.data,
    						'orderInfo': me.orderRecord.data
    					},
    					success : function(response, opts) {
    						var store = me.currentGrid.getStore();
    						Ext.ux.Toast.msg("温馨提示", "重配成功");
    						me.close();
    						store.load({
    							params: {
    								'orderId': me.orderRecord.get('orderId')
    							}
    						});
    					},
    					exception : function(response, opts) {
    						var json = Ext.decode(response.responseText);
    						Ext.ux.Toast.msg("温馨提示", json.message);
    					}
    				});
            	}
            }]
        });
        me.callParent(arguments);
    }
});

//订单库存配置信息
Ext.define('MyApp.shipping.OrderConfigPanel',{
	extend: 'Ext.panel.Panel',
	title: '订单库存配置信息',
	padding: '5 20 5 20',
	defaults: {
		padding: '5 5 5 5'
	},
	orderRecord: null,
	bindData: function(record, grid, rowBodyElement){
		var me = this,
			store = me.getStore();
		me.orderRecord = record;
		store.load({
			params: {
				'orderId': record.get('orderId')
			}
		});
	},
	getStore: function(){
		var me = this;
		if(me.store==null){
			me.store = Ext.create('MyApp.store.ConfigResultStore');
		}
		return me.store;
	},
	orderConfigGrid: null,
	getOrderConfigGrid: function(){
		var me = this;
		if(Ext.isEmpty(me.orderConfigGrid)){
			me.orderConfigGrid = Ext.create('MyApp.view.orderConfigGrid',{
				height: 200,
				store: me.getStore()
			});
		}
		return me.orderConfigGrid;
	},
	toolbar: null,
	getToolbar: function(){
		var me = this;
		if(Ext.isEmpty(me.toolbar)){
			me.toolbar = Ext.widget('toolbar',{
				dock: 'top',
				items: [{
				    text: '结果重配',
				    listeners: {
				        click: {
				            fn: me.replaceConfig,
				            scope: me
				        }
				    }
				},{
                    text: '结果取消',
                    listeners: {
                        click: {
                            fn: me.cancelConfig,
                            scope: me
                        }
                    }
                },{
					text: '重设寄售物服',
					listeners: {
						click: {
							fn: me.sendBackFromRobot,
							scope: me
						}
					}
				}]
			});
		}
		return me.toolbar;
	},
	// 重新配置订单结果
	replaceConfig: function(button, e, eOpts) {
		var me = this,
			grid = me.getOrderConfigGrid(),
			selModel = grid.getSelectionModel(),
			selRecords = selModel.getSelection(),
			shippingManager = Ext.getCmp('shippingManager'),
			window = shippingManager.getReplaceConfigWindow();
    	if(selRecords == null||selRecords.length<=0){
    		Ext.ux.Toast.msg("温馨提示", "请先选择要重新配置的结果信息");
    		return;
    	}
    	record = selRecords[0];
    	if(record.get('state')!=7){
    		Ext.ux.Toast.msg("温馨提示", "请选择已取消状态的订单配置结果");
    		return;
    	}
		window.show();
		window.bindData(me.orderRecord, record, grid);
    },
    // 取消配单
    cancelConfig: function(button, e, eOpts) {
        var me = this,
            grid = me.getOrderConfigGrid(),
            selModel = grid.getSelectionModel(),
            selRecords = selModel.getSelection();
        if(selRecords == null||selRecords.length<=0){
            Ext.ux.Toast.msg("温馨提示", "请先选择要取消配置的结果信息");
            return;
        }
        record = selRecords[0];
        if(record.get('state')!=3){
            Ext.ux.Toast.msg("温馨提示", "请选择待发货状态的订单配置结果");
            return;
        }
		var isJsRobot = record.get('isJsRobot'); // 是否寄售机器人单子
		if (isJsRobot) {
			Ext.ux.Toast.msg("温馨提示", "寄售机器人订单不能取消重配，请直接重设寄售物服");
			return;
		}
        Ext.MessageBox.confirm("取消配单","您确定要取消这笔配单记录吗？", function(v) {
            if (v == 'yes') {
                Ext.Ajax.request({
                    url : './order/cancelConfigResult.action',
                    jsonData: {
                        'id': record.get('orderId')+"_"+record.get('id')
                    },
                    success : function(response, opts) {
                        Ext.ux.Toast.msg("温馨提示", "取消成功");
                        me.getStore().load({
                            params: {
                                'orderId': record.get('orderId')
                            }
                        });
                    },
                    exception : function(response, opts) {
                        var json = Ext.decode(response.responseText);
                        Ext.ux.Toast.msg("温馨提示", json.message);
                    }
                });
            }
        });
    },
	/**
	 * 重设寄售物服
	 */
	sendBackFromRobot: function(button, e, eOpts) {
		var me = this,
			grid = me.getOrderConfigGrid(),
			selModel = grid.getSelectionModel(),
			selRecords = selModel.getSelection();
		if(selRecords == null||selRecords.length<=0){
			Ext.ux.Toast.msg("温馨提示", "请先选择要重设寄售物服的结果信息");
			return;
		}
		record = selRecords[0];
		if(record.get('state')!=3){
			Ext.ux.Toast.msg("温馨提示", "请选择待发货状态的订单配置结果");
			return;
		}
		var isConsignment = record.get('isConsignment'); // 是否寄售
		var isJsRobot = record.get('isJsRobot'); // 是否寄售机器人单子
		if (!isConsignment) {
			Ext.ux.Toast.msg("温馨提示", "不是寄售的订单，不能重设寄售物服");
			return;
		}
		if (!isJsRobot) {
			Ext.ux.Toast.msg("温馨提示", "不是寄售机器人订单，不能重设寄售物服");
			return;
		}
		Ext.MessageBox.confirm("重设寄售物服","您确定要重设寄售物服吗？", function(v) {
			if (v == 'yes') {
				Ext.Ajax.request({
					url : './order/sendbackFromRobot.action',
					jsonData: {
						'orderId': record.get('orderId'),
						'subOrderId': record.get('id'),
						'reason': ''
					},
					success : function(response, opts) {
						Ext.ux.Toast.msg("温馨提示", "重设寄售物服成功");
						me.getStore().load({
							params: {
								'orderId': record.get('orderId')
							}
						});
					},
					exception : function(response, opts) {
						var json = Ext.decode(response.responseText);
						Ext.ux.Toast.msg("温馨提示", json.message);
					}
				});
			}
		});
	},
	constructor: function(config){
		var me = this,
			cfg = Ext.apply({}, config);
		me.dockedItems = [me.getToolbar()];
		me.items = [me.getOrderConfigGrid()];
		me.callParent([cfg]);
	}
});

Ext.define('MyApp.view.shippingManager', {
    extend: 'Ext.panel.Panel',
    id: 'shippingManager',
    closable: true,
    title: '出库订单',
    autoScroll: false,
	listeners:{  
    'resize':function(){
        this.orderGrid.setHeight(window.document.body.offsetHeight-265);
    	}
	},
    replaceConfigWindow: null,
	getReplaceConfigWindow: function(){
		var me = this;
		if(me.replaceConfigWindow==null){
			me.replaceConfigWindow = Ext.create('MyApp.view.ReplaceConfigWindow');
		}
		return me.replaceConfigWindow;
	},
    toolbar: null,
	getToolbar: function(){
		var me = this;
		if(Ext.isEmpty(me.toolbar)){
			me.toolbar = Ext.widget('toolbar',{
				dock: 'top',
				items: [{
				    text: '导出',
				    listeners: {
				        click: {
				            fn: me.exportOrder,
				            scope: me
				        }
				    }
				},{
				    text: '明细',
				    listeners: {
				        click: {
				            fn: me.showShippingInfo,
				            scope: me
				        }
				    }
				}]
			});
		}
		return me.toolbar;
	},
	// 导出出库订单
	exportOrder: function(button, e, eOpts){
		var me = this,url
			queryForm = me.getQueryForm(),
			values = queryForm.getValues();
		url = './order/exportShippingOrder.action?' + Ext.urlEncode(values);
		window.open(url);
	},
	shippingInfoWindow: null,
	getShippingInfoWindow: function(){
		var me = this;
		if(me.shippingInfoWindow==null){
			me.shippingInfoWindow = Ext.create('MyApp.view.SippingInfoWindow');
		}
		return me.shippingInfoWindow;
	},
	// 出库订单详情
	showShippingInfo: function(button, e, eOpts){
		var grid = this.getOrderGrid(),
			selModel = grid.getSelectionModel(),
			selRecords = selModel.getSelection(),record,
			window = this.getShippingInfoWindow();
		if(selRecords == null||selRecords.length<=0){
			Ext.ux.Toast.msg("温馨提示", "请先选择出库订单记录");
			return;
		}
		record = selRecords[0];
		window.bindData(record);
		window.show();
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
		return this.pagingToolbar;
	},
	statebar: null,
	getStatebar: function(){
		var me = this;
		if(Ext.isEmpty(me.orderStatebar)){
			me.orderStatebar = Ext.widget('toolbar',{
				dock: 'top',
				items: [{xtype: 'label', text: '订单状态：'},
						{xtype: 'image', src: 'images/common/s_ing.png'},
						{xtype: 'label', text: '待发货'},
						{xtype: 'image', src: 'images/common/close.png'},
						{xtype: 'label', text: '已取消'},
						{xtype: 'image', src: 'images/common/p_eyes.png'},
						{xtype: 'label', text: '已复制'}
				]
			});
		}
		return me.orderStatebar;
	},
	orderGrid: null,
	getOrderGrid: function(){
		var me = this;
		if(Ext.isEmpty(me.orderGrid)){
			me.orderGrid = Ext.widget('gridpanel',{
			    header: false,
			    columnLines: true,
			    store: me.getStore(),
			    columns: [{
			            xtype: 'rownumberer'
			        },{
			    		sortable: false, 
			    		dataIndex: 'state',
			    		width:60,
			    		text: '状态',
			    		renderer: function(value, metaData, record, rowIndex, colIndex, store, view) {	
		    				var content;
		    				switch(value){
		    				case 0:
		    					//待发货
		    					if(record.get('isCopy')!=true){
		    						content='<div class="container"><div class="leftDiv icons_s_ing"></div></div>';
		    					}else content='<div class="container"><div class="leftDiv icons_s_ing"></div><div class="rightDiv icons_p_eyes"></div></div>';
		    				 	break;
		    				default:
		    					//已取消
		    					if(record.get('isCopy')!=true){
		    						content='<div class="container"><div class="leftDiv icons_close"></div><div class="rightDiv">'+value+'</div></div>';
		    					}else content='<div class="container"><div class="leftDiv icons_close"></div><div class="centerDiv">'+value+'</div><div class="rightDiv icons_p_eyes"></div></div>';
		    				}
		    				return content;
		    			}
			    	},{
						dataIndex: 'orderId',
						flex: 1,
						sortable: false,
						align: 'center',
						text: '订单号'
					},{
			            dataIndex: 'buyer',
			            flex: 1,
			            sortable: false,
			            align: 'center',
			            text: '买家'
			        },{
			            dataIndex: 'title',
			            text: '发布单名称',
			            sortable: false,
			            align: 'center',
			            flex: 1
			        },{
			            dataIndex: 'goldCount',
			            flex: 1,
			            sortable: false,
			            align: 'center',
			            text: '商品数量 '
			        },{
					dataIndex: 'goodsTypeName',
					flex: 1,
					sortable: false,
					align: 'center',
					text: '商品类型 ',
					renderer: function(value, metaData, record, rowIndex, colIndex, store, view) {
						if(value==null||value==""){
							return "游戏币";
						}else return value;
					}
				},{
			            dataIndex: 'unitPrice',
			            flex: 1,
			            sortable: false,
				        align: 'center',
			            text: '订单单价'
			        },{
			            dataIndex: 'totalPrice',
			            flex: 1,
			            sortable: false,
			            renderer: function(v) {
				            return Ext.util.Format.currency(v, '￥', 2);
				        },
				        align: 'center',
			            text: '订单总额'
			        },{
			            dataIndex: 'tradeType',
			            text: '选择交易方式 ',
			            flex: 1,
			            sortable: false,
			            align: 'center',
			            renderer: function(value, metaData, record, rowIndex, colIndex, store, view) {
			            	if(value!=0){
			            		return DataDictionary.rendererSubmitToDisplay(value,'tradeType');			            		
			            	}else{
			            		return '';
			            	}
			            }
			        },{
			        	xtype: 'datecolumn',
			        	format:'Y-m-d H:i:s',
			            dataIndex: 'createTime',
			            sortable: false,
			            flex: 1.5,
			            align: 'center',
			            text: '创建时间 '
			        },{
			            dataIndex: 'endTime',
			            xtype: 'datecolumn',
			        	format:'Y-m-d H:i:s',
			        	flex: 1.5,
			        	align: 'center',
			        	sortable: false,
			            text: '结束时间'
			        },{
			            dataIndex: 'notes',
			            flex: 1,
			            sortable: false,
			            align: 'center',
			            text: '备注'
			        }
			    ],
			    listeners: {
			    	itemdblclick: function(view, record, item, index, e, eOpts ){
			    		var window = me.getShippingInfoWindow();
			    		window.bindData(record);
			    		window.show();
			    	}
			    },
			    plugins: [{
                    ptype: 'gamegoldrowexpander',
                    header: true,
                    rowsExpander: false,
                    expandOnDblClick: false,
                    rowBodyElement : 'MyApp.shipping.OrderConfigPanel'
                }],
			    dockedItems: [me.getToolbar(),me.getStatebar(),me.getPagingToolbar()],
			    selModel: Ext.create('Ext.selection.CheckboxModel', {
			    	allowDeselect: true,
			        mode: 'SINGLE'
			    })
			});
		}
		return me.orderGrid;
	},
	store: null,
	getStore: function(){
		var me = this;
		if(me.store==null){
			me.store = Ext.create('MyApp.store.ShippingStore',{
				autoLoad: true,
				listeners: {
					beforeload : function(store, operation, eOpts) {
						var queryForm = me.getQueryForm();
						var	values = queryForm.getValues();
						var gameName = queryForm.getForm().findField('gameName');
						if (queryForm != null) {
							Ext.apply(operation, {
								params : {
									'orderId': Ext.String.trim(values.orderId),
									'createStartTime': values.createStartTime,
									'createEndTime': values.createEndTime,
									'buyerAccount': Ext.String.trim(values.buyerAccount),
									'gameName': gameName.getRawValue(),
									'goodsTypeName': Ext.String.trim(values.goodsTypeName),
									'detailServiceAccount': Ext.String.trim(values.detailServiceAccount),
									'sellerAccount': Ext.String.trim(values.sellerAccount),
									'onlyDisplayCancelled': Ext.getCmp('onlyDisplayCancelled').getValue(),
                                    'orderType': values.orderType
								}
							});	
						}
					}
				}
			});
		}
		return me.store;
	},
	queryForm: null,
	getQueryForm: function(){
		var me = this;
		if(me.queryForm==null){
			me.queryForm = Ext.widget('form',{
				width: '99%',
				border: false,
				layout: 'column',
				defaults: {
					margin: '5 5 5 5',
					columnWidth: .25,
					labelWidth: 100,
					xtype: 'textfield'
				},
                items: [{
					name: 'orderId',
					fieldLabel: '订单号'
				},{
					xtype: 'gameselectorsellersetting',
					itemId : 'MyApp_view_goods_gamelink_ID',
					columnWidth: .7,
					allowBlank: true,
					fieldLabel: '游戏属性'
				},{
			        fieldLabel: '创建日期',
			        columnWidth: .5,
			        xtype: 'rangedatefield',
			        fromName: 'createStartTime',
			        toName: 'createEndTime',
			        fromValue: new Date(),
			        toValue: new Date()
			    },{
					fieldLabel: '接手客服',
					hidden: CurrentUser.getUserTypeCode()!=2&&CurrentUser.getUserTypeCode()!=3,
				    name: 'detailServiceAccount'
				},{
			    	fieldLabel: '卖家5173账号',
				    name: 'sellerAccount'
				},{
					name: 'buyerAccount',
					fieldLabel: '买家5173账号'
				},DataDictionary.getDataDictionaryCombo('orderType',{
                    fieldLabel: '订单类型',
                    labelWidth: 100,
                    name: 'orderType',
                    editable: false
                },{
                    value:null,
                    display:'全部'
                }),{
					xtype: 'checkbox',
			    	boxLabel: '只显示“已取消”的订单',
			    	columnWidth: 0.15,
			    	inputValue: '1',
			    	id:'onlyDisplayCancelled',
			    	checked: false
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
				})],
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
	initComponent: function() {
	    var me = this;
	    Ext.applyIf(me, {
	        items: [me.getQueryForm(),me.getOrderGrid()]
	    });
	    me.callParent(arguments);
	}
});