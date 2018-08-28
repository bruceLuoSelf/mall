/*
 * 商品信息
 */
Ext.define('MyApp.model.GoodsModel', {
    extend: 'Ext.data.Model',
    fields: [{
        name: 'id'
    },{
        name: 'gameName'//游戏名称
    },{
        name: 'moneyName'//游戏币名称
    },{
        name: 'region'//所在区
    },{
        name: 'server'//所在服
    },{
        name: 'gameRace'//所属阵营
    },{
        name: 'title'//商品名称
    },{
        name: 'unitPrice',//单价(1游戏币兑换多少元)
        type: 'float'
    },{
        name: 'imageUrls'//图片地址
    },{
        name: 'goodsCat',//商品所属栏目
        type: 'int'
    },{
        name: 'sales',//销量
        type: 'int'
    },{
        name: 'deliveryTime',//发货速度(几分钟内可以发货，单位：分钟)
        type: 'int'
    },{
        name: 'lastUpdateTime',//最后更新时间
        dateReadFormat: 'Y-m-dTH:i:s',
        type: 'date'
    },{
        name: 'createTime',//创建时间
        dateReadFormat: 'Y-m-dTH:i:s',
        type: 'date'
    },{
    	name: 'isDeleted',//是否删除
    	type: 'boolean'
    },{
    	name: 'discountList'//折扣列表
    },{
        name: 'goodsTypeId',//商品类型
        type: 'int'
    },{
        name: 'goodsTypeName',// 商品类型名称
        type: 'string'
    }]
});