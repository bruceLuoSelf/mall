/*
 * 商品日志
 */
Ext.define('MyApp.model.GoodsLogModel', {
    extend: 'Ext.data.Model',
    fields: [{
        name: 'id'
    },{
    	name:'logType'//操作类型
    },{
        name: 'userType'//用户类型
    },{
        name: 'intUserType',//用户类型
        type: 'int'
    },{
        name: 'userId'//操作用户ID
    },{
        name: 'userAccount'//操作用户帐号
    },{
    	name: 'remark'//备注
    },{
        name: 'createTime',//创建时间
        dateReadFormat: 'Y-m-dTH:i:s',
        type: 'date'
    },{
    	name: 'ip'//操作的用户IP
    },{
    	name: 'goodsId'//商品ID
    },{
    	name: 'title'//商品名称
    },{
    	name: 'gameName'//游戏名
    },{
    	name: 'region'//大区
    },{
    	name: 'server'//服务器
    },{
    	name: 'gameRace'//游戏阵营
    },{
    	name: 'sellerAccount'//卖家5173账号
    },{
    	name: 'gameAccount'//游戏账号
    },{
    	name: 'sellerGameRole'//卖家游戏角色名
    }]
});