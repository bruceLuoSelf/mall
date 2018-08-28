Ext.onReady(function() {
    Ext.QuickTips.init();
	var loginForm = Ext.create('Ext.form.Panel', {
		title: '5173游戏币后台管理',
		id: 'login_form',
		url: './index.action',
		standardSubmit: true,
		bodyPadding: 20,
		width: 400,
		layout: 'anchor',
		defaults: {
			anchor: '100%'
		},
		defaultType: 'textfield',
		items: [{
			fieldLabel: '用户名',
			name: 'loginAccount',
			allowBlank: false
		},{
			fieldLabel: '密码',
			name: 'password',
			inputType: 'password',
			allowBlank: false
		},{
			name: 'doLogin',
			xtype: 'hiddenfield',
			value: true
		}],
		buttons: [{
			text: '重置',
			handler: function() {
				this.up('form').getForm().reset();
			}
		}, {
			text: '登录',
			formBind: true,
			disabled: true,
			handler: function() {
				var form = this.up('form').getForm();
				if (form.isValid()) {
					form.submit();
				}
			}
		}],
		renderTo: Ext.getBody()
	});
	if(errorMessage!=''){
		Ext.ux.Toast.msg("温馨提示", errorMessage);
	}
	
	document.onkeydown = function(evt){
		if(window.event){
			evt = window.event;
		}
		if(evt.keyCode==13){
			loginForm.getForm().submit();
		}
	};
});