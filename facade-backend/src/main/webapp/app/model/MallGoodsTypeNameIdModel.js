/*
 * 商城的所有游戏名称和ID
 */
Ext.define('MyApp.model.MallGoodsTypeNameIdModel', {
    extend: 'Ext.data.Model',
    fields: [{
        name: 'id',
        type: 'long',
        useNull: true
    },{
        name: 'name'//游戏类目名称
    }]
});
