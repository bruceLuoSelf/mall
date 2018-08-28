/*
 * 添加用户页面
 */
/*
 * 用户管理页面
 */
Ext.define('MyApp.view.userLoginManager', {
    extend: 'Ext.panel.Panel',
    id: 'userLoginManager',
    closable: true,
    title: '用户信息日志管理',
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
                items: []
            });
        }
        return me.toolbar;
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
                items: [{
                    name: 'userAccount',
                    fieldLabel: '操作人邮箱'
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
            me.store = Ext.create('MyApp.store.UserLoginStore',{
                autoLoad: true,
                listeners: {
                    beforeload : function(store, operation, eOpts) {
                        var queryForm = me.getQueryForm();
                        if (queryForm != null) {
                            var values = queryForm.getValues();
                            Ext.apply(operation, {
                                params: {
                                    'userModifilog.userAccount': values.userAccount
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
                    dataIndex: 'userAccount',
                    flex: 1,
                    align: 'center',
                    text: '操作人邮箱账号'
                },{
                    dataIndex: 'updateTime',
                    flex: 1,
                    align: 'center',
                    xtype: 'datecolumn',
                    format: 'Y-m-d H:i:s',
                    text: '修改时间'
                },{
                    dataIndex: 'log',
                    flex: 1,
                    align: 'center',
                    text: '日志信息'
                },{
                    dataIndex: 'userType',
                    text: '用户类型',
                    flex: 1,
                    align: 'center',
                    renderer: function(value){
                        return DataDictionary.rendererSubmitToDisplay(value,'userType');
                    }
                },{
                    dataIndex: 'modifiUserId',
                    flex: 1,
                    align: 'center',
                    text: '被操作行id'
                }],
                dockedItems: [me.getToolbar(),me.getPagingToolbar()],
                selModel: Ext.create('Ext.selection.CheckboxModel', {
                    allowDeselect: true,
                    mode: 'MULTI'
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
