Ext.define('MyApp.model.ShGameGoodsTypeModel',{
    extend:'Ext.data.Model',
    fields:[{
        name: 'id',
        type: 'long',
        useNull:true
    },{
        name:'gameGoodsTypeName'//游戏：商品类型
    }]
})