/**
 *  采购商游戏配置Model
 */
Ext.define('MyApp.model.shPurchaseGameModel', {
    extend: 'Ext.data.Model',
    fields: [{
        name: 'id', //id
        type: 'int'
    },{
        name: 'purchaseId',  //收货商ID
    },{
        name: 'purchaseAccount',//收货商名称
    },{
        name: 'gameName',// 游戏名称
    },{
        name: 'goodsTypeId', //交易类目ID
        type: 'int'
    },{
        name: 'goodsTypeName',//交易类目名称
    },{
        name: 'deliveryTypeId',//收货模式ID
    },{
        name: 'deliveryTypeName'//收货模式名称
    },{
        name: 'tradeTypeId',//交易方式ID
    },{
        name: 'tradeTypeName'//交易方式名称
    }]
});