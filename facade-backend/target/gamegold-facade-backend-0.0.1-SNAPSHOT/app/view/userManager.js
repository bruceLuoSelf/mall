/*
 * 添加用户页面
 */

Ext.define('MyApp.view.userWindow', {
    extend: 'Ext.window.Window',
    width: 700,
    title: '新增/修改用户',
	closeAction: 'hide',
	modal: true,
	form: null,
	getForm: function(){
		var me = this;
		if(me.form==null){
			me.form = Ext.widget('form',{
                    layout: 'column',
					defaults: {
						margin: '5 5 5 5',
						columnWidth: .333,
						labelWidth: 85,
						xtype: 'textfield'
					},
                    items: [{
						name: 'loginAccount',
						vtype: 'email',
						allowBlank: false,
						fieldLabel: '登录名(邮箱)'
					},{
						name: 'password',
						allowBlank: false,
						fieldLabel: '密码',
						inputType: 'password'
					},DataDictionary.getDataDictionaryCombo('sex',{
						fieldLabel: '性别',
						name: 'sex',
						labelWidth: 85,
						editable: false
					}),{
						name: 'realName',
						allowBlank: false,
						fieldLabel: '真实姓名'
					},{
						name: 'nickName',
						allowBlank: false,
						fieldLabel: '昵称'
					},{
						name: 'qq',
						fieldLabel: 'QQ'
					},DataDictionary.getDataDictionaryCombo('userType',{
						fieldLabel: '用户类型',
						name: 'userType',
						allowBlank: false,
						labelWidth: 85,
						editable: false
					}),{
						name: 'weiXin',
						fieldLabel: '微信'
					},{
						name: 'phoneNumber',
						regex: /(^(([0\+]\d{2,3}-)?(0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$)|(^0{0,1}1[3|4|5|6|7|8|9][0-9]{9}$)/,
    					regexText: '请输入正确的手机号码(11位)或电话(区号-电话号)！',
						fieldLabel: '电话号码'
					},{
						name: 'yy',
						fieldLabel: 'YY'
					},{
						name: 'hxAppAccount',
						fieldLabel: '环信账号'
					},{
						name: 'hxAppPwd',
						fieldLabel: '环信密码'
					},{
						name: 'serviceCharge',
						fieldLabel: '服务费',
						xtype: 'numberfield',
						allowBlank: false,
						minValue: 0,
						value:0
					},{
						name: 'star',
						fieldLabel: '客服星级',
						xtype: 'numberfield',
						minValue: 0,
						maxValue:7,
						value:0
					},{
						name: 'fundsAccountId',
						allowBlank: false,
						xtype: 'numberfield',
						minValue: 0,
						fieldLabel: '资金账号Id'
					},{
						name: 'fundsAccount',
						allowBlank: false,
						fieldLabel: '资金账号'
					},{
						name: 'yxAccount',
						fieldLabel: '云信账号'
					},{
						name: 'yxPwd',
						fieldLabel: '云信密码'
					},{
						name: 'qqPwd',
						fieldLabel: 'QQ密码'
					},/*{
						xtype: 'checkboxgroup',
						fieldLabel: '通讯工具管理',
						labelWidth: 100,
						allowBlank: false,
						columnWidth : 0.5,
						name: 'checkBoxCategoryIcon1',
						vertical: false,
						items: [
							{ boxLabel: 'QQ', name: 'communicationTools', inputValue: '0' },
							{ boxLabel: '环信', name: 'communicationTools', inputValue: '1' },
							{ boxLabel: '云信', name: 'communicationTools', inputValue: '2' }
						]
					},*/{
					    xtype: 'filefield',
					    name: 'avatarUrl',
					    columnWidth: 1,
					    vtype: 'image',
					    fieldLabel: '头像图片'
					},{
						xtype: 'textarea',
						name: 'sign',
						columnWidth: 1,
						height: 100,
						fieldLabel: '个性签名'
					}],
                    buttons: [{
						text:'保存',
						formBind: true,
						disabled: true,
						handler: function() {
							var tab = Ext.getCmp('userManager'),
								userGrid = tab.getUserGrid(),
								userGridStore = userGrid.getStore(),
								form = me.getForm(),
								record = form.getRecord(),
								url = './user/addUser.action',params,
								message = '新增';
							if(!form.isValid()){
								return;
							}
							form.updateRecord(record);
							console.log(form);
							/*var communicationTools = "",
							checkBoxCategoryIcon1 = form.getForm().findField('checkBoxCategoryIcon1').getChecked();
							console.log(checkBoxCategoryIcon1);

							for (var i = 0, j = checkBoxCategoryIcon1.length; i <j ; i++) {
								communicationTools += checkBoxCategoryIcon1[i].getSubmitValue();
								if (i != (j -1)) {
									communicationTools += ",";
								}
							}*/
							var params = {
									'user.loginAccount': record.get('loginAccount'),
									'user.password': record.get('password'),
									'user.fundsAccount': record.get('fundsAccount'),
									'user.fundsAccountId': record.get('fundsAccountId'),
									'user.sex': record.get('sex'),
									'user.realName': record.get('realName'),
									'user.nickName': record.get('nickName'),
									'user.qq': record.get('qq'),
									'user.userType': record.get('userType'),
									'user.weiXin': record.get('weiXin'),
									'user.phoneNumber': record.get('phoneNumber'),
									'user.sign': record.get('sign'),
									'user.yy': record.get('yy'),
									'user.serviceCharge': record.get('serviceCharge'),
									'user.star': record.get('star'),
									'user.hxAppAccount': record.get('hxAppAccount'),
									'user.hxAppPwd': record.get('hxAppPwd'),
									'user.yxAccount': record.get('yxAccount'),
									'user.yxPwd': record.get('yxPwd'),
									'user.qqPwd': record.get('qqPwd')/*,
									'user.communicationTools': communicationTools*/
							};
							if(me.isUpdate){
								url = './user/modifyUser.action';
								message = '修改';
								Ext.Object.merge(params,{
									'id': record.get('id')
								});
							}
							
							form.submit({
								url : url,
								params: params,
								success : function(from, action, json) {
									var userManager = Ext.getCmp('userManager'),
										store = userManager.getStore();
									var selgrid = userManager.getUserGrid(), selModel = selgrid.getSelectionModel();
									selModel.deselectAll();
									Ext.ux.Toast.msg("温馨提示", message + "成功");
									me.close();
									store.load();
								},
								exception : function(from, action, json) {
									Ext.ux.Toast.msg("温馨提示", json.message);
								}
							});
						}
					}]
                });
		}
		return this.form;
	},
	isUpdate: null,
	bindData: function(record,isUpdate){
		var me = this,
			form = me.getForm().getForm(),
			loginAccount = form.findField('loginAccount'),
			password = form.findField('password'),
			star=form.findField('star');
		form.reset();
		form.loadRecord(record);
		me.isUpdate = isUpdate;
		if(isUpdate){
			loginAccount.setReadOnly(true);
			password.setReadOnly(true);

			/*checkBoxCategoryIcon1 = form.findField('checkBoxCategoryIcon1');

			if(record.data.communicationTools!=""){
				console.log(record.data.communicationTools);
				var communicationTools_string = record.data.communicationTools.split(",");
				checkBoxCategoryIcon1.setValue({communicationTools:communicationTools_string});
			}
			console.log(record.data.communicationTools);*/
		}else{
			loginAccount.setReadOnly(false);
			password.setReadOnly(false);
			form.reset();
		}

		//担保客服，才显示星级编辑
		if(record.get('userType')=="1"){
			star.setVisible(true);
		}
		else{
			star.setVisible(false);
		}
	},
    initComponent: function() {
        var me = this;
        Ext.applyIf(me, {
            items: [me.getForm()]
        });
        me.callParent(arguments);
    }
});

Ext.define('MyApp.view.gameConfigure', {
	extend: 'Ext.window.Window',
	width: 700,
	title: '游戏配置',
	closeAction: 'hide',
	modal: true,
	form: null,
	getForm: function(){
		var me = this;
		if(me.form==null){
			me.form = Ext.widget('form',{
				layout: 'column',
				defaults: {
					margin: '5 5 5 5',
					columnWidth: .333,
					labelWidth: 85,
					xtype: 'textfield'
				},
				items: [{
					name: 'loginAccount',
					vtype: 'email',
					allowBlank: false,
					fieldLabel: '登录名(邮箱)',
					columnWidth: 1,
				},{
					xtype: 'checkboxgroup',
					fieldLabel: '游戏',
					name: 'games',
					columns: 4,
					columnWidth: 1,
					vertical: true,
					items: [
						{ boxLabel: '地下城与勇士', name: 'dxc'},
						{ boxLabel: '剑灵', name: 'jl'},
						{ boxLabel: '魔兽世界(国服)', name: 'mssj'},
						{ boxLabel: 'QQ三国', name: 'qqsg'},
						{ boxLabel: '剑侠情缘Ⅲ', name: 'jxqy' },
						{ boxLabel: '疾风之刃', name: 'jfzr'},
						{ boxLabel: '龙之谷', name: 'lzg'},
						{ boxLabel: '斗战神', name: 'dzs'},
						{ boxLabel: '天涯明月刀', name: 'tymyd' },
						{ boxLabel: '上古世纪', name: 'sgsj' },
						{ boxLabel: '怪物猎人OL', name: 'gwlr' },
						{ boxLabel: '冒险岛2', name: 'mxd2' },
						{ boxLabel: '新天龙八部', name: 'xtlbb' }
					]
				}],
				buttons: [{
					text:'保存',
					formBind: true,
					disabled: true,
					handler: function() {
						var tab = Ext.getCmp('userManager'),
							userGrid = tab.getUserGrid(),
							form = me.getForm(),
							record = form.getRecord();

						//获取通过checkboxgroup定义的checkbox值，获取checked的值
						//var gamesValue = Ext.getCmp('games').getChecked();
						var CheckboxGroup = form.getForm().findField('games');


						var games="";
						//Ext.Array.each(gamesValue, function(item){
						//	games += item.boxLabel+",";
						//});
						for (var i = 0; i < CheckboxGroup.items.length; i++){
							if (CheckboxGroup.items.get(i).checked){
								games += CheckboxGroup.items.get(i).boxLabel+",";
							}
						}

						Ext.Ajax.request({
							url : './user/saveUser.action',
							params: {'userId': record.get('id'),"games":games},
							success : function(response, opts) {
								Ext.ux.Toast.msg("温馨提示", "保存成功！");
								me.close();
							},
							exception : function(response, opts) {
								var json = Ext.decode(response.responseText);
								Ext.ux.Toast.msg("温馨提示", json.message);
							}
						});
					}
				}]
			});
		}
		return this.form;
	},
	bindData: function(record,isUpdate){
		var me = this,
			form = me.getForm().getForm();
		form.reset();
		form.loadRecord(record);
		//url = './user/modifyPwd.action',params;
		var params = {
			'usersGame.userId': record.get('id')
		};

		form.submit({
			url : './user/queryUsersGame.action',
			params: params,
			success : function(from, action, json) {
				var usersGameList=json.usersGameList;
				for(i=0;i<usersGameList.length;i++){
					var usersGame=usersGameList[i];
					for(var p in gameData){
						var pCode=gameData[p].code;
						var pValue=gameData[p].value;

						if(pValue==usersGame.gameName){
							form.findField(pCode).setValue(true);
						}
					}
				}
			},
			exception : function(from, action, json) {
				Ext.ux.Toast.msg("温馨提示", json.message);
			}
		});
	},
	initComponent: function() {
		var me = this;
		Ext.applyIf(me, {
			items: [me.getForm()]
		});
		me.callParent(arguments);
	}
});

Ext.define('MyApp.view.pwdWindow', {
	extend: 'Ext.window.Window',
	width: 400,
	title: '修改密码',
	closeAction: 'hide',
	modal: true,
	form: null,
	getForm: function(){
		var me = this;
		if(me.form==null){
			me.form = Ext.widget('form',{
				layout: 'column',
				defaults: {
					margin: '5 5 5 5',
					columnWidth: 1,
					labelWidth: 85,
					xtype: 'textfield'
				},
				items: [{
					name: 'loginAccount',
					vtype: 'email',
					allowBlank: false,
					fieldLabel: '登录名(邮箱)'
				},{
					name: 'passwordNew',
					allowBlank: false,
					fieldLabel: '新密码',
					inputType: 'password'
				},{
					name: 'passwordConfirm',
					allowBlank: false,
					fieldLabel: '确认密码',
					inputType: 'password'
				}],
				buttons: [{
					text:'保存',
					formBind: true,
					disabled: true,
					handler: function() {
						var tab = Ext.getCmp('userManager'),
							userGrid = tab.getUserGrid(),
							userGridStore = userGrid.getStore(),
							form = me.getForm(),
							record = form.getRecord(),
							url = './user/modifyPwd.action',params;
						if(!form.isValid()){
							return;
						}
						form.updateRecord(record);

						var pwdNew=form.getForm().findField('passwordNew').value;
						var pwdConfirm=form.getForm().findField('passwordConfirm').value
						if(pwdNew==""){
							Ext.ux.Toast.msg("温馨提示", "新密码不能为空");
							return;
						}
						else if(pwdNew!=pwdConfirm){
							Ext.ux.Toast.msg("温馨提示", "两次密码输入不一致");
							return;
						}

						var params = {
							'user.loginAccount': record.get('loginAccount'),
							'user.password': pwdNew,
							'id': record.get('id')
						};
						form.submit({
							url : url,
							params: params,
							success : function(from, action, json) {
								var userManager = Ext.getCmp('userManager'),
									store = userManager.getStore();
								Ext.ux.Toast.msg("温馨提示", "修改成功");
								me.close();
								store.load();
							},
							exception : function(from, action, json) {
								Ext.ux.Toast.msg("温馨提示", json.message);
							}
						});
					}
				}]
			});
		}
		return this.form;
	},
	isUpdate: null,
	bindData: function(record,isUpdate){
		var me = this,
			form = me.getForm().getForm(),
			loginAccount = form.findField('loginAccount');
		form.reset();
		form.loadRecord(record);
		loginAccount.setReadOnly(true);
	},
	initComponent: function() {
		var me = this;
		Ext.applyIf(me, {
			items: [me.getForm()]
		});
		me.callParent(arguments);
	}
});

Ext.define('MyApp.view.userAvatarImage', {
	extend : 'Ext.Img',
	src: null,
	height: 64,
	width: 64,
	bindData : function(record, value, metadata, store, view) {
		var me = this;
		if(Ext.isEmpty(record.get('avatarUrl'))){
			return false;
		}else{
			me.setSrc(SystemUtil.getImageUrl(record.get('avatarUrl'), 'userAvatarSize'));
			return true;			
		}
	},
	constructor : function(config) {
		var me = this, cfg = Ext.apply({}, config);
		me.callParent([ cfg ]);
	}
});

/*
 * 用户管理页面
 */
Ext.define('MyApp.view.userManager', {
    extend: 'Ext.panel.Panel',
    id: 'userManager',
    closable: true,
    title: '用户信息管理',
    ////////////////////////////////////////////////////
    autoScroll: false,
	listeners:{  
    'resize':function(){
        this.userGrid.setHeight(window.document.body.offsetHeight-190);
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
					xtype: 'button',
					itemId: 'addButton',
					text: '新增',
					listeners: {
						click: {
							fn: me.addUser,
							scope: me
						}
					}
				},'-',{
					xtype: 'button',
					itemId: 'editButton',
					text: '编辑',
					listeners: {
						click: {
							fn: me.modifyUser,
							scope: me
						}
					}
				},'-',{
					xtype: 'button',
					itemId: 'deleteButton',
					text: '删除',
					listeners: {
						click: {
							fn: me.deleteUser,
							scope: me
						}
					}
				},'-',{
					xtype: 'button',
					itemId: 'enableButton',
					text: '启用',
					listeners: {
						click: {
							fn: me.enableUser,
							scope: me
						}
					}
				},'-',{
					xtype: 'button',
					itemId: 'disableButton',
					text: '禁用',
					listeners: {
						click: {
							fn: me.disableUser,
							scope: me
						}
					}
				},'-',{
					xtype: 'button',
					itemId: 'gameConfigure',
					text: '游戏配置',
					listeners: {
						click: {
							fn: me.gameConfigure,
							scope: me
						}
					}
				}
					/*,'-',{
					text: '初始化环信账号',
					handler: function(){
						me.productionAccount();
					}
				}/*,'-',{
					xtype: 'button',
					itemId: 'pwdButton',
					text: '修改密码',
					listeners: {
						click: {
							fn: me.modifyPwd,
							scope: me
						}
					}
				}*/]
			});
		}
		return me.toolbar;
	},
	userWindow: null,
	getUserWindow: function(){
		if(this.userWindow == null){
        	this.userWindow = new MyApp.view.userWindow();
        }
		return this.userWindow;
	},
	gameConfigureWindow: null,
	getGameConfigureWindow: function(){
		if(this.gameConfigureWindow == null){
			this.gameConfigureWindow = new MyApp.view.gameConfigure();
		}
		return this.gameConfigureWindow;
	},
	pwdWindow: null,
		getPwdWindow: function(){
		if(this.pwdWindow == null){
			this.pwdWindow = new MyApp.view.pwdWindow();
		}
		return this.pwdWindow;
	},
	// 新增用户
    addUser: function(button, e, eOpts) {
		var window = this.getUserWindow();
		window.bindData(Ext.create('MyApp.model.UserModel'),false);
		window.show();
    },
    // 编辑用户
    modifyUser: function(button, e, eOpts) {
		var grid = this.getUserGrid(),
			selModel = grid.getSelectionModel(),
			selRecords = selModel.getSelection(),
			window = this.getUserWindow();
    	if(selRecords == null||selRecords.length<=0){
    		Ext.ux.Toast.msg("温馨提示", "请先选择要编辑的用户");
    		return;
    	}
    	window.bindData(selRecords[0],true);
		window.show();
    },
	//游戏配置
	gameConfigure: function(button, e, eOpts) {
		var grid = this.getUserGrid(),
			selModel = grid.getSelectionModel(),
			selRecords = selModel.getSelection(),
			window = this.getGameConfigureWindow();
		if(selRecords == null||selRecords.length<=0){
			Ext.ux.Toast.msg("温馨提示", "请先选择要配置的用户");
			return;
		}
		if(selRecords[0].get("userType")!="1"){
			Ext.ux.Toast.msg("温馨提示", "只能对担保客服进行配置");
			return;
		}
		window.bindData(selRecords[0],true);
		window.show();
	},

	//初始化环信账号
	productionAccount:function () {
		Ext.Ajax.request({
			url : './user/productionAccount.action',
			success : function(response, opts) {
				Ext.ux.Toast.msg("温馨提示", "生成成功！");
				grid.getStore().load();
				grid.getSelectionModel().deselectAll();
			},
			exception : function(response, opts) {
				var json = Ext.decode(response.responseText);
				Ext.ux.Toast.msg("温馨提示", json.message);
			}
		});

	},
	//删除用户
	deleteUser:function(button,e,eOpts){
		var grid = this.getUserGrid(),
			selModel = grid.getSelectionModel(),
			selRecords = selModel.getSelection();
		if(selRecords == null||selRecords.length<=0){
			Ext.ux.Toast.msg("温馨提示", "请先选择要删除的用户");
			return;
		}
		var record = selRecords[0];
		Ext.MessageBox.confirm('温馨提示', '确定删除该用户吗？', function(btn) {
			if(btn == 'yes'){
				Ext.Ajax.request({
					url : './user/deleteUser.action',
					params: {'id': record.get('id')},
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
    // 禁用用户
    disableUser: function(button, e, eOpts) {
    	var grid = this.getUserGrid(),
			selModel = grid.getSelectionModel(),
			selRecords = selModel.getSelection();
    	if(selRecords == null||selRecords.length<=0){
    		Ext.ux.Toast.msg("温馨提示", "请先选择要禁用的用户");
    		return;
    	}
		var record = selRecords[0];
		if(record.get('isDeleted')){
			Ext.ux.Toast.msg("温馨提示", "该用户已经禁用！");
			return;
		}
    	Ext.MessageBox.confirm('温馨提示', '确定禁用该用户吗？', function(btn){
			if(btn == 'yes'){
				Ext.Ajax.request({
					url : './user/disableUser.action',
					params: {'id': record.get('id')},
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
	// 启用用户
    enableUser: function(button, e, eOpts) {
    	var grid = this.getUserGrid(),
			selModel = grid.getSelectionModel(),
			selRecords = selModel.getSelection();
    	if(selRecords == null||selRecords.length<=0){
    		Ext.ux.Toast.msg("温馨提示", "请先选择要启用的用户");
    		return;
    	}
		var record = selRecords[0];
		if(!record.get('isDeleted')){
			Ext.ux.Toast.msg("温馨提示", "该用户已经启用！");
			return;
		}
    	Ext.MessageBox.confirm('温馨提示', '确定启用该用户吗？', function(btn){
			if(btn == 'yes'){
				Ext.Ajax.request({
					url : './user/enableUser.action',
					params: {'id': record.get('id')},
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
	//修改密码
	/*modifyPwd:function(button, e, eOpts) {
		var grid = this.getUserGrid(),
			selModel = grid.getSelectionModel(),
			selRecords = selModel.getSelection(),
			window = this.getPwdWindow();
		if(selRecords == null||selRecords.length<=0){
			Ext.ux.Toast.msg("温馨提示", "请先选择要编辑的用户");
			return;
		}
		window.bindData(selRecords[0],true);
		window.show();
	},*/
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
                items: [DataDictionary.getDataDictionaryCombo('userType',{
                	fieldLabel: '用户类型',
                	labelWidth: 80,
					name: 'userType',
					editable: false
				},{value: null,display: '全部'}),{
					name: 'loginAccount',
					fieldLabel: '登录名'
				},{
					name: 'nickName',
					fieldLabel: '昵称'
				},{
					name: 'realName',
					fieldLabel: '姓名'
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
	store: null,
	getStore: function(){
		var me = this;
		if(me.store==null){
			me.store = Ext.create('MyApp.store.UserStore',{
				autoLoad: true,
				listeners: {
					beforeload : function(store, operation, eOpts) {
						var queryForm = me.getQueryForm();
						if (queryForm != null) {
							var values = queryForm.getValues();
							Ext.apply(operation, {
								params: {
									'user.userType': values.userType,
									'user.loginAccount': Ext.String.trim(values.loginAccount),
									'user.nickName': Ext.String.trim(values.nickName),
									'user.realName': Ext.String.trim(values.realName)
								}
							});	
						}
					}
				}
			});
		}
		return me.store;
	},
	userGrid: null,
	getUserGrid: function(){
		var me = this;
		if(Ext.isEmpty(me.userGrid)){
			me.userGrid = Ext.widget('gridpanel',{
                header: false,
                columnLines: true,
				store: me.getStore(),
				columns: [{
					xtype: 'rownumberer'
				},{
					dataIndex: 'loginAccount',
					text: '登录名(邮箱)',
					xtype: 'tipcolumn', 
					tipConfig: {
						trackMouse: true,
					    hideDelay: 500 
					},
					align: 'center',
					tipBodyElement:'MyApp.view.userAvatarImage',
					flex: 1.5
				},{
					dataIndex: 'nickName',
					flex: 1,
					align: 'center',
					text: '昵称'
				},{
					dataIndex: 'realName',
					flex: 1,
					align: 'center',
					text: '姓名'
				},{
					dataIndex: 'fundsAccount',
					flex: 2,
					align: 'center',
					text: '客服资金账号'
				},{
					dataIndex: 'sex',
					text: '性别',
					flex: 0.5,
					align: 'center',
					renderer: function(value){
						return DataDictionary.rendererSubmitToDisplay(value,'sex');
					}
				},{
					dataIndex: 'phoneNumber',
					flex: 1.5,
					align: 'center',
					text: '电话号码'
				},{
					dataIndex: 'userType',
					text: '用户类型',
					flex: 1,
					align: 'center',
					renderer: function(value){
						return DataDictionary.rendererSubmitToDisplay(value,'userType');
					}
				},{
					dataIndex: 'qq',
					flex: 1,
					align: 'center',
					text: 'QQ'
				},{
					dataIndex: 'qqPwd',
					flex: 1,
					align: 'center',
					text: 'QQ密码'
				},{
					dataIndex: 'weiXin',
					flex: 1,
					align: 'center',
					text: '微信'
				},{
					dataIndex: 'sign',
					text: '个性签名',
					align: 'center',
					flex: 2
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
					mode: 'SINGLE',
					listeners: {
						select: function(rowModel, record, index, eOpts){ 
							var toolbar = me.getToolbar(),
								editButton = toolbar.getComponent('editButton');
							if(record.get('userType')==0){
								editButton.disable();
							}else{
								editButton.enable();
							}
				        }
				    }
				})
			});
		}
		return me.userGrid;
	},
    initComponent: function() {
        var me = this;
        Ext.applyIf(me, {
            items: [me.getQueryForm(),me.getUserGrid()]
        });
        me.callParent(arguments);
    }
});


//游戏集合
var gameData=[
	{code: 'dxc',value:'地下城与勇士'},
	{code: 'jl',value:'剑灵'},
	{code: 'mssj',value:'魔兽世界(国服)'},
	{code: 'qqsg',value:'QQ三国'},
	{code: 'jxqy',value:'剑侠情缘Ⅲ'},
	{code: 'jfzr',value:'疾风之刃'},
	{code: 'lzg',value:'龙之谷'},
	{code: 'dzs',value:'斗战神'},
	{code: 'tymyd',value:'天涯明月刀'},
	{code: 'sgsj',value:'上古世纪'},
	{code: 'gwlr',value:'怪物猎人OL'},
	{code: 'mxd2',value:'冒险岛2'},
	{code: 'xtlbb',value:'新天龙八部'}
];