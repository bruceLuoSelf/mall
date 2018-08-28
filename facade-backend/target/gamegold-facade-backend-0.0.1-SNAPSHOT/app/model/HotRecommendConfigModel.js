/*
 * 安心买游戏币数量下拉菜单信息
 */
Ext.define('MyApp.model.HotRecommendConfigModel', {
    extend: 'Ext.data.Model',
    fields: [{
        name: 'id'
    },{
        name: 'gameId'//游戏Id
    },{
    	name: 'gameName'//游戏名称
    },{
        name: 'goldCounts'//金币数量
    },{
        name: 'categoryIcon1'//
    },{
    	name: 'categoryIcon2'//
    },{
        name: 'categoryIcon3'//
    },{
        name: 'showCategory23'//
    },{
        name: 'isConsignmentMode'//
    },{
        name: 'consignmentStartTime'//
    },{
        name: 'consignmentEndTime'//
    },{
        name: 'createTime'//创建时间
    },{
        name: 'lastUpdateTime'//最后更新时间
    },{
        name: 'goodsTypeId',//商品类型
        type: 'int'
    },{
        name: 'goodsTypeName'//商品类型名称
    }]
});