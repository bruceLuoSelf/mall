/**
 * Created by 340096 on 2017/8/10.
 */
/*
 * 添加用户页面
 */

Ext.define('MyApp.view.blackListWindow', {
	extend: 'Ext.window.Window',
	width: 700,
	title: '新增用户',
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
					allowBlank: false,
					fieldLabel: '5173用户'
				},
				// 	{
				// 	name: 'addPerson',
				// 	allowBlank: false,
				// 	fieldLabel: '添加人'
				// }
					,],
				buttons: [{
					text:'保存',
					formBind: true,
					disabled: true,
					handler: function() {
						var tab = Ext.getCmp('BlackListManager'),
							userGrid = tab.getUserGrid(),
							userGridStore = userGrid.getStore(),
							form = me.getForm(),
							record = form.getRecord(),
							url = './blackList/addUser1.action',params,
							message = '新增';
						if(!form.isValid()){
							return;
						}
						form.updateRecord(record);
						var params = {
							'blackListeo.loginAccount': record.get('loginAccount'),
							'blackListeo.addPerson': record.get('addPerson'),
						};
						form.submit({
							url : url,
							params: params,
							success : function(from, action, json) {
								var userManager = Ext.getCmp('BlackListManager'),
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
			password = form.findField('password'),
			star=form.findField('star');
		form.reset();
		form.loadRecord(record);
		me.isUpdate = isUpdate;
		if(isUpdate){
			loginAccount.setReadOnly(true);
			// password.setReadOnly(true);
		}else{
			loginAccount.setReadOnly(false);
			// password.setReadOnly(false);
			form.reset();
		}
	}
	,
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
 * 黑名单管理页面
 */
Ext.define('MyApp.view.BlackListManager', {
	extend: 'Ext.panel.Panel',
	id: 'BlackListManager',
	closable: true,
	title: '黑名单信息管理',
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
				}
				]
			});
		}
		return me.toolbar;
	},
	blackListWindow: null,
	getBlackListWindow: function(){
		if(this.blackListWindow == null){
			this.blackListWindow = new MyApp.view.blackListWindow();
		}
		return this.blackListWindow;
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
		var window = this.getBlackListWindow();
		window.bindData(Ext.create('MyApp.model.BlackListModel'),false);
		window.show();
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
					url : './blackList/deleteUser1.action',
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
		if(record.get('enable')){
			Ext.ux.Toast.msg("温馨提示", "该用户已经禁用！");
			return;
		}
		Ext.MessageBox.confirm('温馨提示', '确定禁用该用户吗？', function(btn){
			if(btn == 'yes'){
				Ext.Ajax.request({
					url : './blackList/disableUser1.action',
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
		if(!record.get('enable')){
			Ext.ux.Toast.msg("温馨提示", "该用户已经启用！");
			return;
		}
		Ext.MessageBox.confirm('温馨提示', '确定启用该用户吗？', function(btn){
			if(btn == 'yes'){
				Ext.Ajax.request({
					url : './blackList/enableUser1.action',
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
				items: [
					{
						name: 'loginAccount',
						fieldLabel: '5173用户'
					},{
						fieldLabel: '添加时间',
						columnWidth: .5,

						xtype: 'rangedatefield',
						fromName: 'createStartTime',
						toName: 'createEndTime',
						fromValue: new Date(),
						toValue: new Date()
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
			me.store = Ext.create('MyApp.store.BlackListStore',{
				autoLoad: true,
				listeners: {
					beforeload : function(store, operation, eOpts) {
						var queryForm = me.getQueryForm();
						if (queryForm != null) {
							var values = queryForm.getValues();
							Ext.apply(operation, {
								params: {
									'blackListeo.loginAccount': Ext.String.trim(values.loginAccount),
									'startTime':values.createStartTime,
									'endTime':values.createEndTime
								}
							});
						}
					},
					// load:function (value) {
					//     console.log(value)
					// }

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
				},

					{
						dataIndex: 'loginAccount',
						flex: 1,
						align: 'center',
						text: '5173用户'
					}, {
						dataIndex: 'addPerson',
						flex: 1.5,
						align: 'center',
						text: '添加人'
					},
					{
						xtype: 'datecolumn',
						format: 'Y-m-d H:i:s',
						dataIndex: 'createTime',
						flex: 1,
						align: 'center',
						text: '添加时间'
					},
					{
						dataIndex: 'enable',
						text: '状态',
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
