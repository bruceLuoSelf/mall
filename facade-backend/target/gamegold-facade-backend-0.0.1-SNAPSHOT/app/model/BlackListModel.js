/*
 * 黑名单信息
 */
Ext.define('MyApp.model.BlackListModel', {
    extend: 'Ext.data.Model',
    fields: [{
        name: 'id'//用户id
    },{
        name: 'loginAccount'//5173用户
    },{
        name: 'createTime'//添加时间
    },{
        name: 'addPerson',//添加人
    },{
        name: 'enable',//状态
        type: 'boolean'
    }]
});