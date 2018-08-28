/*
 * 添加子用户页面
 */
Ext.define('MyApp.view.subUserWindow', {
    extend: 'Ext.window.Window',
    width: 700,
    title: '新增/修改子用户',
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
					},DataDictionary.getDataDictionaryCombo('subUserType',{
						fieldLabel: '子用户类型',
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
					}/*,{
						name: 'fundsAccountId',
						allowBlank: false,
						xtype: 'numberfield',
						minValue: 0,
						columnWidth: .5,
						fieldLabel: '资金账号Id'
					},{
						name: 'fundsAccount',
						allowBlank: false,
						columnWidth: .5,
						fieldLabel: '资金账号'
					},{
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
					}*/],
                    buttons: [{
						text:'保存',
						formBind: true,
						disabled: true,
						handler: function() {
							var tab = Ext.getCmp('subUserManager'),
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
							var params = {
									'user.loginAccount': record.get('loginAccount'),
									'user.password': record.get('password'),
									/*'user.fundsAccount': record.get('fundsAccount'),
									'user.fundsAccountId': record.get('fundsAccountId'),*/
									'user.sex': record.get('sex'),
									'user.realName': record.get('realName'),
									'user.nickName': record.get('nickName'),
									'user.qq': record.get('qq'),
									'user.userType': record.get('userType'),
									'user.weiXin': record.get('weiXin'),
									'user.phoneNumber': record.get('phoneNumber')/*,
									'user.sign': record.get('sign')*/
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
									var userManager = Ext.getCmp('subUserManager'),
										store = userManager.getStore();
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
			password = form.findField('password');
		form.reset();
		form.loadRecord(record);
		me.isUpdate = isUpdate;
		if(isUpdate){
			loginAccount.setReadOnly(true);
			password.setReadOnly(true);
		}else{
			loginAccount.setReadOnly(false);
			password.setReadOnly(false);
			form.reset();
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

/*Ext.define('MyApp.view.userAvatarImage', {
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
});*/

/*
 * 子用户管理页面
 */
Ext.define('MyApp.view.subUserManager', {
    extend: 'Ext.panel.Panel',
    id: 'subUserManager',
    closable: true,
    title: '子用户信息管理',
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
				}]
			});
		}
		return me.toolbar;
	},
	userWindow: null,
	getUserWindow: function(){
		if(this.userWindow == null){
        	this.userWindow = new MyApp.view.subUserWindow();
        }
		return this.userWindow;
	},
	// 新增子用户
    addUser: function(button, e, eOpts) {
		var window = this.getUserWindow();
		window.bindData(Ext.create('MyApp.model.UserModel'),false);
		window.show();
    },
    // 编辑子用户
    modifyUser: function(button, e, eOpts) {
		var grid = this.getUserGrid(),
			selModel = grid.getSelectionModel(),
			selRecords = selModel.getSelection(),
			window = this.getUserWindow();
    	if(selRecords == null||selRecords.length<=0){
    		Ext.ux.Toast.msg("温馨提示", "请先选择要编辑的子用户");
    		return;
    	}
    	window.bindData(selRecords[0],true);
		window.show();
    },
    // 禁用子用户
    disableUser: function(button, e, eOpts) {
    	var grid = this.getUserGrid(),
			selModel = grid.getSelectionModel(),
			selRecords = selModel.getSelection();
    	if(selRecords == null||selRecords.length<=0){
    		Ext.ux.Toast.msg("温馨提示", "请先选择要禁用的子用户");
    		return;
    	}
		var record = selRecords[0];
		if(record.get('isDeleted')){
			Ext.ux.Toast.msg("温馨提示", "该子用户已经禁用！");
			return;
		}
    	Ext.MessageBox.confirm('温馨提示', '确定禁用该子用户吗？', function(btn){
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
	// 启用子用户
    enableUser: function(button, e, eOpts) {
    	var grid = this.getUserGrid(),
			selModel = grid.getSelectionModel(),
			selRecords = selModel.getSelection();
    	if(selRecords == null||selRecords.length<=0){
    		Ext.ux.Toast.msg("温馨提示", "请先选择要启用的子用户");
    		return;
    	}
		var record = selRecords[0];
		if(!record.get('isDeleted')){
			Ext.ux.Toast.msg("温馨提示", "该子用户已经启用！");
			return;
		}
    	Ext.MessageBox.confirm('温馨提示', '确定启用该子用户吗？', function(btn){
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
                items: [DataDictionary.getDataDictionaryCombo('subUserType',{
                	fieldLabel: '子用户类型',
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
					align: 'center',
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
				}/*,{
					dataIndex: 'fundsAccount',
					flex: 2,
					align: 'center',
					text: '客服资金账号'
				}*/,{
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
					text: '子用户类型',
					flex: 1,
					align: 'center',
					renderer: function(value){
						return DataDictionary.rendererSubmitToDisplay(value,'subUserType');
					}
				},{
					dataIndex: 'qq',
					flex: 1,
					align: 'center',
					text: 'QQ'
				},{
					dataIndex: 'weiXin',
					flex: 1,
					align: 'center',
					text: '微信'
				},/*{
					dataIndex: 'sign',
					text: '个性签名',
					align: 'center',
					flex: 2
				},*/{
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
					mode: 'SINGLE'/*,
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
				    }*/
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