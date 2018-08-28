/*
 * 出货子订单
 */
Ext.define('MyApp.model.SplitRepositoryLogModel', {
    extend: 'Ext.data.Model',
    fields: [{
        name: 'fcId',//分仓订单号
        type: 'int'
    },{
        name: 'buyerAccount'//收货方5173账号
    },{
        name: 'gameName'//游戏名称
    },{
        name: 'region'//游戏区
    },{
        name: 'server'//游戏服
    },{
        name: 'gameRace'//游戏阵营
    },{
        name: 'gameAccount'//游戏账号
    },{
        name: 'log'//日志内容
    },{
        name: 'imgPath'//图片路径
    },{
        name: 'roleName'//角色名称
    },{
        name: 'logType'//分仓类型
    },{
        name: 'fcOrderId'//分仓主订单
    },{
        name: 'count'//分仓数量
    },{
        name: 'incomeType',//收入支出类型
    },{
        name: 'createTime',//创建时间
        dateReadFormat: 'Y-m-dTH:i:s',
        type: 'date'
    }]
});