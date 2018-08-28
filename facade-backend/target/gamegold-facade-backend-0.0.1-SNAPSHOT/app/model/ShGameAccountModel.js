/*
 * 收货角色
 */
Ext.define('MyApp.model.ShGameAccountModel', {
    extend: 'Ext.data.Model',
    fields: [{
        name: 'id',
        type: 'int'
    },{
        name: 'buyerAccount'//收货方5173账号
    },{
        name: 'buyerUid'//收货方5173UID
    },{
        name: 'gameName'//游戏名
    },{
        name: 'region'//游戏区
    },{
        name: 'server'//游戏服
    },{
        name: 'gameRace'//游戏阵营
    },{
        name: 'gameAccount'//收货的游戏账号
    },{
        name: 'gamePwd',//游戏密码
    },{
        name: 'roleName',//收货的角色名
    },{
        name: 'secondPwd',//二级密码
    },{
        name: 'level',//等级
        type: 'int'
    },{
        name: 'count',//收货数量
        type: 'int'
    },{
        name: 'price',//收货单价
        type: 'float'
    },{
        name: 'repositoryCount',//库存数量
        type: 'int'
    },{
        name: 'isShRole',//是否收货角色
        type: 'boolean'
    },{
        name: 'isPackFull',//背包是否已满
        type: 'boolean'
    },{
        name: 'status',//角色状态
        type: 'int'
    },{
        name: 'updateTime',//更新时间
        dateReadFormat: 'Y-m-dTH:i:s',
        type: 'date'
    }]
});