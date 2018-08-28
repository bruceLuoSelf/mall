/*
 * 修改密码页面
 */
Ext.define('MyApp.view.modifyPassword', {
    extend: 'Ext.form.Panel',
    id: 'modifyPassword',
    closable: true,
    title: '修改密码',
    initComponent: function() {
        var me = this;
        Ext.applyIf(me, {
        	defaults: {
				margin: '10 10 10 10',
				labelWidth: 100,
				width: 300,
				xtype: 'textfield'
			},
        	items: [{
                xtype: 'displayfield',
                fieldLabel: '真实姓名',
                value: CurrentUser.getRealName(),
                name: 'realName'
            },{
                xtype: 'displayfield',
                fieldLabel: '用户帐号',
                value: CurrentUser.getLoginAccount(),
                name: 'emailAddress'
            },{
                name: 'oldPassword',
                inputType: "password",
                allowBlank:false, //不允许为空
                blankText:"原密码不能为空",  //错误提示信息，默认为This field is required!
                fieldLabel: '原密码'
            },{
                name: 'newPassword',
                inputType: "password",
                allowBlank:false, //不允许为空
                blankText:"新密码不能为空",  //错误提示信息，默认为This field is required!
                fieldLabel: '新密码'
            },{
                name: 'repeatPassword',
                inputType: "password",
                allowBlank:false, //不允许为空
                blankText:"不能为空",  //错误提示信息，默认为This field is required!
                fieldLabel: '再次输入新密码'
            }],
	        tbar: [{
				text:'修改密码',
				formBind: true, //only enabled once the form is valid
				disabled: true,
				handler: function() {
					var formPanel = this.up('form')
						form = formPanel.getForm(),
						values = form.getValues();
					if(formPanel.validate(values)){
						//Ajax进行密码修改
						Ext.Ajax.request({
							url: './main/changePassword.action',
							params: {
								'oldPassword': values.oldPassword,
								'newPassword': values.newPassword
							},
							success: function(response, opts) {
								var result = Ext.decode(response.responseText);
								//设置当前登录用户信息
								loginUser = result.currentUser;
								Ext.ux.Toast.msg("温馨提示", "密码修改成功！");
							},
							exception: function(response, opts){
								var result = Ext.decode(response.responseText);
								Ext.ux.Toast.msg("温馨提示", result.message);
							}
						});	
					}
				}
			}]
        });
        me.callParent(arguments);
    },
    validate: function(values) {
    	var oldPassword = values.oldPassword.trim(),
    		newPassword = values.newPassword.trim(),
    		repeatPassword = values.repeatPassword.trim();
    	if(oldPassword == null || oldPassword == '' || newPassword == null 
    			|| newPassword == '' || repeatPassword == null
    			|| repeatPassword == ''){
    		Ext.ux.Toast.msg("温馨提示", "密码不能为空");
    		return false;
    	}
    	if(newPassword != repeatPassword){
    		Ext.ux.Toast.msg("温馨提示", "两次密码不一致");
    		return false;
    	}
    	return values;
    }
});