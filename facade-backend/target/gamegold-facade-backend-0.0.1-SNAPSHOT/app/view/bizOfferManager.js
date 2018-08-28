/*
 * 担保收货管理页面
 */
Ext.define('MyApp.view.bizOfferManager', {
    extend: 'Ext.panel.Panel',
    id: 'bizOfferManager',
    closable: true,
    title: '低价单收货',
    ////////////////////////////////////////////////////
    autoScroll: false,
	listeners:{  
    'resize':function(){
        this.grid.setHeight(window.document.body.offsetHeight-258);
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
					columnWidth: .25,
					xtype: 'textfield'
				},
                items: [{
				    fieldLabel: '开始单价',
					xtype: 'numberfield',
				    name: 'minPrice',
				    decimalPrecision: 4,
				    step: 0.0001,
				    value: 0,
				    listeners: {
				    	'change': function(field, newValue, oldValue, eOpts ){
							var form = me.getQueryForm().getForm(),
								maxPrice = form.findField('maxPrice');
							maxPrice.setMinValue(newValue);
						}
					}
				},{
				    fieldLabel: '结束单价',
					xtype: 'numberfield',
				    name: 'maxPrice',
				    decimalPrecision: 4,
				    step: 0.0001,
				    value: 100,
				    listeners: {
				    	'change': function(field, newValue, oldValue, eOpts ){
				    		var form = me.getQueryForm().getForm(),
				    			minPrice = form.findField('minPrice');
				    		minPrice.setMaxValue(newValue);
						}
					}
				},DataDictionary.getDataDictionaryCombo('serviceType',{
					fieldLabel: '服务类型',
					name: 'serviceType',
					value: 1,
					columnWidth: .25,
					labelWidth: 80,
					editable: false
				}),{
					xtype: 'gamelinkselector',
					itemId : 'MyApp_view_goods_gamelink_ID',
					columnWidth: .5,
					allowBlank: false,
					fieldLabel: '游戏属性'
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
			me.store = Ext.create('MyApp.store.BizOfferStore',{
				autoLoad: true,
				listeners: {
					beforeload : function(store, operation, eOpts) {
						var queryForm = me.getQueryForm(),
							gameName = queryForm.getForm().findField('gameName');
						if (queryForm != null) {
							var values = queryForm.getValues();
							Ext.apply(operation, {
								params: {
									'gameName': gameName.getRawValue(),
									'region': values.region,
									'server': values.server,
									'gameRace': values.gameRace,
									'minPrice': values.minPrice,
									'maxPrice': values.maxPrice,
									'serviceType': values.serviceType
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
	grid: null,
	getGrid: function(){
		var me = this;
		if(Ext.isEmpty(me.grid)){
			me.grid = Ext.widget('gridpanel',{
                header: false,
                columnLines: true,
                store: me.getStore(),
				columns: [{
					xtype: 'rownumberer'
				},{
                    dataIndex: 'bizOfferId',
                    sortable: false,
                    flex: 1,
                    align: 'center',
                    text: '发布单ID'
                },{
                    dataIndex: 'bizOfferName',
                    sortable: false,
                    flex: 1,
                    align: 'center',
                    text: '发布单名称'
                },{
                    dataIndex: 'gameName',
                    sortable: false,
                    flex: 1.5,
                    align: 'center',
                    text: '游戏名称'
                },{
            		dataIndex: 'gameAreaName',
            		sortable: false,
            		text: '所在区',
            		align: 'center',
            		flex: 1
            	},{
            		dataIndex: 'gameServerName',
            		sortable: false,
            		flex: 1,
            		align: 'center',
            		text: '所在服'
            	},{
            		dataIndex: 'gameRaceName',
            		sortable: false,
            		flex: 1,
            		align: 'center',
            		text: '所在阵营'
            	},{
		            dataIndex: 'price',
		            flex: 1,
		            sortable: false, 
		            renderer: function(v) {
			            return Ext.util.Format.currency(v, '￥', 2);
			        },
			        align: 'center',
		            text: '单价'
		        },{
		            dataIndex: 'totalPrice',
		            flex: 1,
		            sortable: false, 
		            renderer: function(v) {
			            return Ext.util.Format.currency(v, '￥', 2);
			        },
			        align: 'center',
		            text: '总价'
		        },{
                    dataIndex: 'serviceType',
                    sortable: false,
                    flex: 0.7,
                    align: 'center',
                    text: '服务类型',
                    renderer: function(value){
						return DataDictionary.rendererSubmitToDisplay(value,'serviceType');
					}
                },{
					dataIndex: 'createDate',
					align: 'center',
					text: '创建时间',
					flex: 1
				},{
                    dataIndex: 'buyUrl',
                    sortable: false,
                    flex: 1,
                    text: '购买地址',
                    renderer: function(value, metaData, record, rowIndex, colIndex, store, view) {
                    	return '<a href="'+value+'" target="_blank">购买地址</a>'
		            }
                }],
				dockedItems: [me.getPagingToolbar()],
				selModel: Ext.create('Ext.selection.CheckboxModel', {
					allowDeselect: true,
					mode: 'SINGLE'
				})
			});
		}
		return me.grid;
	},
    initComponent: function() {
        var me = this;
        Ext.applyIf(me, {
            items: [me.getQueryForm(),me.getGrid()]
        });
        me.callParent(arguments);
    }
});