/*
 * 添加子用户页面
 */
Ext.define('MyApp.view.adminSubUserWindow', {
    extend: 'Ext.window.Window',
    width: 700,
    title: '修改子账号',
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
						columnWidth: .5,
						labelWidth: 85,
						xtype: 'textfield'
					},
                    items: [{
						name: 'loginAccount',
						vtype: 'email',
						allowBlank: false,
						fieldLabel: '登录名(邮箱)'
					},{
						name: 'realName',
						allowBlank: false,
						fieldLabel: '姓名'
					},{
						name: 'nickName',
						allowBlank: false,
						fieldLabel: '昵称'
					},{
						name: 'qq',
						fieldLabel: 'QQ'
					},{
						name: 'weiXin',
						fieldLabel: '微信'
					},{
						name: 'phoneNumber',
						regex: /(^(([0\+]\d{2,3}-)?(0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$)|(^0{0,1}1[3|4|5|6|7|8|9][0-9]{9}$)/,
    					regexText: '请输入正确的手机号码(11位)或电话(区号-电话号)！',
						fieldLabel: '电话号码'
					},{
						fieldLabel: '直接上级',
						hidden: CurrentUser.getUserTypeCode()!=3,
						xtype: 'commonuserselector',
						labelWidth: 85,
					    name: 'mainAccountId'
					}],
                    buttons: [{
						text:'保存',
						formBind: true,
						disabled: true,
						handler: function() {
							var form = me.getForm(),
								record = form.getRecord();
							if(!form.isValid()){
								return;
							}
							form.updateRecord(record);
							Ext.Ajax.request({
								url : './user/adminModifySubUser.action',
								jsonData: {'user': record.data},
								success : function(response, opts) {
									var userManager = Ext.getCmp('adminSubUserManager'),
										store = userManager.getStore();
									Ext.ux.Toast.msg("温馨提示", "修改成功");
									me.close();
									store.load();
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
	isUpdate: null,
	bindData: function(record){
		var me = this,
			form = me.getForm().getForm(),
			mainAccountId = form.findField('mainAccountId');
		form.reset();
		form.loadRecord(record);
		mainAccountId.setValue(null);
	},
    initComponent: function() {
        var me = this;
        Ext.applyIf(me, {
            items: [me.getForm()]
        });
        me.callParent(arguments);
    }
});

/*
 * 子用户管理页面
 */
Ext.define('MyApp.view.adminSubUserManager', {
    extend: 'Ext.panel.Panel',
    id: 'adminSubUserManager',
    closable: true,
    title: '子账号管理',
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
					itemId: 'editButton',
					text: '编辑',
					listeners: {
						click: {
							fn: me.modifyUser,
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
        	this.userWindow = new MyApp.view.adminSubUserWindow();
        }
		return this.userWindow;
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
									'user.realName': Ext.String.trim(values.realName),
									'mainAccountIdNotNull': true
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
				},{
					dataIndex: 'mainAccountId',
					text: '直接上级',
					flex: 1,
					align: 'center'
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
					mode: 'SINGLE'
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