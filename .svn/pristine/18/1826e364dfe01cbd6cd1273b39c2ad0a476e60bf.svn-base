/*
 * 订单付款查询管理页面
 */
Ext.define('MyApp.view.syncOrderPaymentStatusManager', {
    extend: 'Ext.panel.Panel',
    id: 'syncOrderPaymentStatusManager',
    closable: true,
    title: '掉单处理',
    queryForm: null,
	getQueryForm: function () {
		var me = this;
		if (me.queryForm == null) {
			me.queryForm = Ext.widget('form', {
				border: false,
				layout: 'column',
				defaults: {
					margin: "20 10 20 10",
					labelWidth: 80,
					xtype: 'textfield'
				},
				items: [{
					columnWidth: 0.25,
					name: 'orderId',
					fieldLabel: '订单号',
					allowBlank: false
				},{
					xtype: 'button',
					text: '立即同步',
					handler: function () {
						me.query();
					}
				}]
			});
		}
		return me.queryForm;
	},
	query: function() {
		var form = this.getQueryForm().form;
		if (form.isValid()) {
			var orderId = form.findField('orderId').value;
			Ext.Ajax.request({
				url: './order/syncOrderPaymentStatus.action',
				params: {'orderId': orderId},
				success: function (response, opts) {
					var json = Ext.decode(response.responseText);
					Ext.ux.Toast.msg("温馨提示", json.message);
				},
				exception: function (response, opts) {
					var json = Ext.decode(response.responseText);
					Ext.ux.Toast.msg("温馨提示", json.message);
				}
			});
		}
	},
    initComponent: function() {
        var me = this;
        Ext.applyIf(me, {
            items: [me.getQueryForm()]
        });
        me.callParent(arguments);
    }
});