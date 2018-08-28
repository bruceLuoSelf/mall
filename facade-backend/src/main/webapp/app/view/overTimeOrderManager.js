Ext.define('MyApp.view.orderInfoWin', {
    extend: 'Ext.window.Window',
    width: 800,
    title: '订单信息',
	closeAction: 'hide',
	modal: true,
	form: null,
	getForm: function(){
		var me = this;
		if(me.form==null){
			me.form = Ext.create('MyApp.view.OrderInfoForm');
		}
		return this.form;
	},
	isUpdate: null,
	bindData: function(record){
		var me = this,
			form = me.getForm().getForm(),
			servicerInfo = form.findField('servicerInfo'),
			gameProp = form.findField('gameProp');
		form.reset();
		form.loadRecord(record);
		servicerInfo.setValue(record.get('servicerInfo').realName);
		var prop = record.get('gameName')+'||'+record.get('region')+'||'+record.get('server');
		prop += Ext.isEmpty(record.get('gameRace'))?"":'||'+record.get('gameRace');
		gameProp.setValue(prop);
	},
    initComponent: function() {
        var me = this;
        Ext.applyIf(me, {
            items: [me.getForm()]
        });
        me.callParent(arguments);
    }
});


Ext.define('MyApp.view.overTimeOrderManager', {
    extend: 'Ext.panel.Panel',
    id: 'overTimeOrderManager',
    closable: true,
    title: '超时订单赔付',
    toolbar: null,
	getToolbar: function(){
		var me = this;
		if(Ext.isEmpty(me.toolbar)){
			me.toolbar = Ext.widget('toolbar',{
				dock: 'top',
				items: [{
				    text: '订单详情',
				    listeners: {
				        click: {
				            fn: me.orderInfo,
				            scope: me
				        }
				    }
				}]
			});
		}
		return me.toolbar;
	},
	orderInfoWindow: null,
	getOrderInfoWindow: function(){
		var me = this;
		if(me.orderInfoWindow==null){
			me.orderInfoWindow = Ext.create('MyApp.view.orderInfoWin');
		}
		return me.orderInfoWindow;
	},
	// 订单详情
	orderInfo: function(button, e, eOpts) {
		var grid = this.getOrderGrid(),
			selModel = grid.getSelectionModel(),
			selRecords = selModel.getSelection(),order,
			window = this.getOrderInfoWindow();
    	if(selRecords == null||selRecords.length<=0){
    		Ext.ux.Toast.msg("温馨提示", "请先选择订单");
    		return;
    	}
    	order = selRecords[0];
		window.show();
		window.bindData(order);
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
	orderGrid: null,
	getOrderGrid: function(){
		var me = this;
		if(Ext.isEmpty(me.orderGrid)){
			me.orderGrid = Ext.widget('gridpanel',{
				header: false,
				region:'center',
				columnLines: true,
			    store: me.getStore(),
			    columns: [{
			            xtype: 'rownumberer',
			            width: 40
			        },{
			            dataIndex: 'orderId',
			            xtype: 'ellipsiscolumn',
			            width: 130,
			            sortable: false,
			            align: 'center',
			            text: '订单号'
			        },{
			            dataIndex: 'userAccount',
			            width: 115,
			            sortable: false,
			            align: 'center',
			            text: '买家'
			        },{
						dataIndex: 'qq',
						width: 95,
			            sortable: false,
			            align: 'center',
			            text: 'QQ'
					},{
			            dataIndex: 'title',
			            text: '发布单<br/>名称',
			            sortable: false,
			            align: 'center',
			            width: 105
			        },{
			            dataIndex: 'goldCount',
			            width: 65,
			            sortable: false,
			            align: 'center',
			            text: '数量'
			        },{
			            dataIndex: 'totalPrice',
			            width: 90,
			            sortable: false,
			            renderer: function(v) {
				            return Ext.util.Format.currency(v, '￥', 2);
				        },
				        align: 'center',
			            text: '价格'
			        },{
			            dataIndex: 'deliveryTime',
			            sortable: false,
			            width: 45,
			            align: 'center',
			            text: '发货<br/>速度 '
			        },{
			            sortable: false,
			            width: 45,
			            align: 'center',
			            renderer: function(v, m, record) {
				            return parseInt((record.get('sendTime') - record.get('payTime'))/60000);
				        },
			            text: '发货<br/>用时 '
			        },{
			        	xtype: 'datecolumn' ,
			        	format:'Y-m-d H:i:s',
			            dataIndex: 'payTime',
			            sortable: false,
			            width: 145,
			            align: 'center',
			            text: '付款时间 '
			        },{
			            dataIndex: 'sendTime',
			            xtype: 'datecolumn'  ,
			        	format: 'Y-m-d H:i:s',
			        	width: 145,
			        	align: 'center',
			        	sortable: false,
			            text: '发货时间'
			        }/*,{
			            //dataIndex: 'endTime',
			            xtype: 'datecolumn',
			        	format:'Y-m-d H:i:s',
			        	width: 145,
			        	align: 'center',
			        	sortable: false,
			            text: '发货用时'
			        }*/
			    ],
			    plugins: [{
                    ptype: 'gamegoldrowexpander',
                    header: true,
                    rowsExpander: false,
                    expandOnDblClick: false,
                    rowBodyElement : 'MyApp.order.OrderConfigPanel'
                }],
			    dockedItems: [me.getToolbar(), me.getPagingToolbar()],
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
			me.store = Ext.create('MyApp.store.OrderStore',{
				autoLoad: true,
				listeners: {
					beforeload : function(store, operation, eOpts) {
						var queryForm = me.getQueryForm(),
							values = queryForm.getValues(),params={};
						if (queryForm != null) {
							params = {
									'createStartTime': values.createStartTime,
									'createEndTime': values.createEndTime,
									'sellerAccount': Ext.String.trim(values.sellerAccount),
									'buyerAccount': Ext.String.trim(values.buyerAccount),
									'orderState': 5,
									'gameName': Ext.String.trim(values.gameName),
									'orderId': Ext.String.trim(values.orderId),
									'detailServiceAccount': Ext.String.trim(values.detailServiceAccount),
								    'goodsTypeName': '全部',
									'isDelay':true
								};
							Ext.apply(operation, {
								params : params
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
			        fieldLabel: '下单日期',
			        columnWidth: .5,
			        xtype: 'rangedatefield',
			        fromName: 'createStartTime',
			        toName: 'createEndTime',
				    fromValue: new Date(),
				    toValue: new Date()
			    },{
			    	fieldLabel: '卖家5173账号',
				    name: 'sellerAccount'
				},{
					name: 'buyerAccount',
					fieldLabel: '买家5173账号'
				},DataDictionary.getDataDictionaryCombo('SgameName',{
					fieldLabel: '游戏名称',
				    name: 'gameName',
                	labelWidth: 100,
					editable: false
				},{value: null,display:'全部'}),{
					name: 'orderId',
					fieldLabel: '订单号'
				},{
					fieldLabel: '接手客服',
					hidden: CurrentUser.getUserTypeCode()!=2&&CurrentUser.getUserTypeCode()!=3,
				    name: 'detailServiceAccount'
				}],
                buttons: ['->',{
					text:'查询',
					handler: function() {
						me.getStore().load();
					}
				},{
					text:'导出',
					handler: function() {
						me.exportOverTimeOrder();
					}
				}]
            });
		}
		return this.queryForm;
	},
	exportOverTimeOrder: function(button, e, eOpts){
		var me = this,url
			queryForm = me.getQueryForm(),
			values = queryForm.getValues();
		url = './order/exportDelayOrder.action?' + Ext.urlEncode(values)+'&orderState=5';
		window.open(url);
	},
	splitPanel:null,
	getSplitPanel:function(){
		var me = this;
		if(Ext.isEmpty(me.splitPanel)){
			me.splitPanel = Ext.create('Ext.panel.Panel', {
				region: 'north',
        		split: true,
        		collapsible: true,
        		collapseDirection : 'bottom',
				header:false,
				collapseMode:'mini',
				items:[me.getQueryForm()]
			});
		}
		return me.splitPanel;
	},
	
	initComponent: function() {
	    var me = this;
	    Ext.applyIf(me, {
	    	intervalID: null,
	    	layout: 'border',
	    	header:false,
	    	listeners:{
				//渲染结束定时进行订单的刷新。
				boxready : function(){
					me.intervalID = setInterval(
						function(){
							me.getStore().load();
						},SystemUtil.getOrderAutoLoadTime()
					);
				},
				destroy: function(){
					clearInterval(me.intervalID);
				}
			},
	        items: [me.getSplitPanel(),me.getOrderGrid()]
	    });
	    me.callParent(arguments);
	}
});