/*
 * 卖家信息
 */
Ext.define('MyApp.model.SellerSettingModel', {
    extend: 'Ext.data.Model',
    fields: [{
        name: 'id',
        type: 'int'
    },{
        name: 'gameName'//游戏
    },{
        name: 'region'//大区
    },{
        name: 'loginAccount'//用户账号(5173的用户账号)
    },{
        name: 'commision',//佣金
        type: 'float'
    },{
        name: 'praiseCount',//商家信誉
        type: 'int'
    },{
        name: 'successPercent',//成功率
        type: 'float'
    },{
        name: 'deliverSpeed',//平均发货速度
        type: 'int'
    },{
        name: 'monthDealCount',//月成交笔数
        type: 'int'
    },{
        name: 'isDeleted',//是否启用
        type: 'boolean'
    },{
        name: 'sort' // 排序字段
    },{
        name: 'goodsTypeId', // 商品类目
        type: 'int'
    },{
        name: 'goodsTypeName', // 商品类目名称
        type: 'string'
    }]
});