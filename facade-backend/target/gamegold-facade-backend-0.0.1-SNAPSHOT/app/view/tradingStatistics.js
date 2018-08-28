/*
 * 交易统计页面
 */
Ext.define('MyApp.view.tradingStatistics', {
    extend: 'Ext.panel.Panel',
    id: 'tradingStatistics',
    closable: true,
    title: '交易统计',
	countPanel: null,
	getCountPanel: function(){
		var me = this;
		if(me.countPanel==null){
			me.countPanel = Ext.widget('form',{
				border: false,
				defaults: {
					xtype: 'textfield',
				    margin: '15 50',
				    width: 300,
				    readOnly: true,
				    labelWidth: 120
				},
				items: [{
				    fieldLabel: '总成交',
				    name: 'totalFunds'
				},{
				    fieldLabel: '今日总额',
				    name: 'todayIncome'
				},{
				    fieldLabel: '昨日总额',
				    name: 'yesterdayIncome'
				},{
				    fieldLabel: '本月总额',
				    name: 'thisMonthIncome'
				},{
				    fieldLabel: '上月总额',
				    name: 'lastMonthIncome'
				},{
                    fieldLabel: '今日佣金',
                    name: 'commission'
                },{
                    fieldLabel: '今日差价',
                    name: 'priceDiff'
                },{
				    fieldLabel: '今日独立购买人数',
				    name: 'todayBuyCount'
				},{
				    fieldLabel: '昨日独立购买人数',
				    name: 'lastBuyCount'
				}]
			});
		}
		return me.countPanel;
	},
	loadData: function(){
		var me = this;
		Ext.Ajax.request({
			url: './funds/tradingStatistics.action',
			success: function(response, opts) {
				var json = Ext.decode(response.responseText),
					t = json.tradingStatistics,
					countForm = me.getCountPanel().getForm();
				countForm.findField('totalFunds').setValue(Ext.isEmpty(t.totalFunds)?"":"￥"+t.totalFunds);
				countForm.findField('todayIncome').setValue(Ext.isEmpty(t.todayIncome)?"":"￥"+t.todayIncome);
				countForm.findField('yesterdayIncome').setValue(Ext.isEmpty(t.yesterdayIncome)?"":"￥"+t.yesterdayIncome);
				countForm.findField('thisMonthIncome').setValue(Ext.isEmpty(t.thisMonthIncome)?"":"￥"+t.thisMonthIncome);
				countForm.findField('lastMonthIncome').setValue(Ext.isEmpty(t.lastMonthIncome)?"":"￥"+t.lastMonthIncome);
                countForm.findField('commission').setValue(Ext.isEmpty(t.commission)?"":"￥"+t.commission);
                countForm.findField('priceDiff').setValue(Ext.isEmpty(t.priceDiff)?"":"￥"+t.priceDiff);
				countForm.findField('todayBuyCount').setValue(Ext.isEmpty(t.todayBuyCount)?"":t.todayBuyCount);
				countForm.findField('lastBuyCount').setValue(Ext.isEmpty(t.lastBuyCount)?"":t.lastBuyCount);
			},
			exception : function(response, opts) {
				var json = Ext.decode(response.responseText);
				Ext.ux.Toast.msg("温馨提示", json.message);
			}
		});
	},
	initComponent: function() {
	    var me = this;
	    Ext.applyIf(me, {
	    	tbar: [{
				text:'刷新',
				handler: function() {
					me.loadData();
				}
			}],
	        items: [me.getCountPanel()]
	    });
	    me.listeners = {
        	boxready: function(){
        		me.loadData();
        	}	
        };
	    me.callParent(arguments);
	}
});