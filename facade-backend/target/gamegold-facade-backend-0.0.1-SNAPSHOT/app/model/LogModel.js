/*
 * 日志信息
 */
Ext.define('MyApp.model.LogModel', {
    extend: 'Ext.data.Model',
    fields: [{
        name: 'id'
    },{
    	name: 'currentThreadId'//操作线程ID
    },{
        name: 'currentUserId'//操作用户ID
    },{
        name: 'currentUID'//5173注册用户ID
    },{
        name: 'currentUserAccount'//操作用户帐号
    },{
        name: 'currentUserType',//用户类型
        type: 'int'
    },{
        name: 'module'//操作模块
    },{
        name: 'operateInfo'//操作内容
    },{
        name: 'createTime',//创建时间
        dateReadFormat: 'Y-m-dTH:i:s',
        type: 'date'
    }]
});