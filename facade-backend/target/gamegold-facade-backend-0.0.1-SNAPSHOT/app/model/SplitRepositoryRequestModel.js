/*
 * 分仓请求
 */
Ext.define('MyApp.model.SplitRepositoryRequestModel', {
    extend: 'Ext.data.Model',
    fields: [{
        name: 'id',
        type: 'int'
    },{
    	name:'orderId'//分仓请求订单号
    },{
        name: 'buyerAccount'//收货方5173账号
    },{
        name: 'gameName'//游戏名
    },{
        name: 'region'//游戏区
    },{
        name: 'server'//游戏服
    },{
    	name: 'gameRace'//游戏阵营
    },{
    	name: 'gameAccount'//游戏账号
    },{
    	name: 'fmsRoleName'//附魔师角色名
    },{
    	name: 'gameRole'//游戏角色名
    },{
        name: 'realCount'//实际分仓数量  realCount
    },{
    	name: 'status',//状态
        type: 'int'
    },{
        name: 'createTime',//创建时间
        dateReadFormat: 'Y-m-dTH:i:s',
        type: 'date'
    },{
        name: 'updateTime',//更新时间
        dateReadFormat: 'Y-m-dTH:i:s',
        type: 'date'
    }]
});