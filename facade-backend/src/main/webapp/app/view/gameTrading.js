/*
 * 交易统计页面
 */
Ext.define('MyApp.view.gameTrading', {
    extend: 'Ext.panel.Panel',
    id: 'gameTrading',
    closable: true,
    title: '区服交易信息',
    ////////////////////////////////////////////////////
    autoScroll: false,
	listeners:{  
    'resize':function(){
        this.subGrid.setHeight(window.document.body.offsetHeight-255);
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
					columnWidth: .25,
					labelWidth: 80,
					xtype: 'textfield'
				},
                items: [{
				    fieldLabel: '交易日期',
				    columnWidth: .5,
				    xtype: 'rangedatefield',
				    //起始日期组件的name属性。
				    fromName: 'statementStartTime',
				    //终止日期组件的name属性。
				    toName: 'statementEndTime',
				    //起始日期组件的初始值。
				    fromValue: new Date(),
				    //终止日期组件的初始值。
				    toValue: new Date()
		    	},{
					xtype: 'gamelinkselector',
					columnWidth: .5,
					itemId : 'MyApp_view_goods_gamelink_ID',
					allowBlank: true,
					fieldLabel: '游戏属性'
				}],
                buttons: [{
					text:'查询',
					handler: function() {
						me.getPagingToolbar().moveFirst();
					}
				},{
					text:'导出',
					listeners:{
						click:{
							fn:me.exportInfo,
							scope:me
						}
					}
				}]
            });
		}
		return this.queryForm;
	},
	//导出信息
	exportInfo:function(button,e,eOpts){
		var me = this,url,
		queryForm = me.getQueryForm(),
		values = queryForm.getValues();
		gameName = queryForm.getForm().findField('gameName');
		var params= {
			'disCount':SystemUtil.getSubCommissionBase(),
			'gameName': Ext.String.trim(gameName.getRawValue()),
			'region': Ext.String.trim(values.region),
			'server': Ext.String.trim(values.server),
			'statementStartTime': values.statementStartTime,
			'statementEndTime': values.statementEndTime
		}
		url = './funds/exportGameTradingFlow.action?' + Ext.urlEncode(params);
		window.open(url);
	},
	store: null,
	getStore: function(){
		var me = this;
		if(me.store==null){
			me.store = Ext.create('MyApp.store.gameTradingStore',{
				autoLoad: true,
				listeners: {
					beforeload : function(store, operation, eOpts) {
						var queryForm = me.getQueryForm(),
							values = queryForm.getValues();
							gameName = queryForm.getForm().findField('gameName');
							disCount = SystemUtil.getSubCommissionBase();
						if (queryForm != null) {
							Ext.apply(operation, {
								params : {
									'disCount':disCount,
									'gameName': Ext.String.trim(gameName.getRawValue()),
									'region': Ext.String.trim(values.region),
									'server': Ext.String.trim(values.server),
									'statementStartTime': values.statementStartTime,
									'statementEndTime': values.statementEndTime
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
		return this.pagingToolbar;
	},
	subGrid: null,
	getSubGrid: function(){
		var me = this;
		if(Ext.isEmpty(me.subGrid)){
			me.subGrid = Ext.widget('gridpanel',{
			    header: false,
			    columnLines: true,
			    store: me.getStore(),
			    columns: [{
			            dataIndex: 'gameName',
			            flex: 1.5,
			            sortable: false,
			            align: 'center',
			            renderer: function(value, metaData, record, rowIndex, colIndex, store, view) {
			            	var gameProp = record.get('gameName')+"/"
			            					+record.get('region')+"/"
			            					+record.get('server');
			            	if(Ext.isEmpty(record.get('gameRace'))){
			            		return gameProp;
			            	}else{
			            		return gameProp +'/'+ record.get('gameRace');
			            	}
			            },
			            text: '游戏区服'
			        },{
			            dataIndex: 'tradingNum',
			            flex: 1,
			            sortable: false,
				        align: 'center',
			            text: '成交笔数'
			        },{
			            dataIndex: 'goldCount',
			            flex: 1,
			            sortable: false, 
			            align: 'center',
			            text: '金币总数'
			        },{
			            dataIndex: 'total',
			            flex: 1,
			            sortable: false,
			            renderer: function(v) {
			                return Ext.util.Format.currency(v, '￥', 4);
			            },
			            align: 'center',
			            text: '订单总金额 '
			        },{
			            dataIndex: 'commission',
			            flex: 1,
				        sortable: false,
				        renderer: function(v) {
				            return Ext.util.Format.currency(v, '￥', 4);
				        },
				        align: 'center',
			            text: '佣金收入'
			        },{
			            dataIndex: 'difference',
			        	flex: 1.5,
			        	align: 'center',
			        	sortable: false,
			        	renderer: function(v) {
			                return Ext.util.Format.currency(v, '￥', 4);
			            },
			            text: '差额收入'
			        }]
			});
		}
		return me.subGrid;
	},
	initComponent: function() {
	    var me = this;
	    Ext.applyIf(me, {
	        items: [me.getQueryForm(),me.getSubGrid(),me.getPagingToolbar()]
	    });
	    me.callParent(arguments);
	}
});