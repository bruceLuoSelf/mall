/*
 * 订单配置信息
 */
Ext.define('MyApp.model.MainGameConfigModel', {
    extend: 'Ext.data.Model',
    fields: [{
        name: 'id',
    }, {
        name: 'gameName'// 游戏名称
    }, {
        name: 'gameId'// 游戏ID
    }, {
        name: 'ableDelivery'//是否允许收货
    }, {
        name: 'ableSell'//是否允许出售
    }, {
        name: 'createTime', //创建时间
        dateReadFormat: 'Y-m-dTH:i:s',
        type: 'date'
    }, {
        name: 'updateTime', //最后更新时间
        dateReadFormat: 'Y-m-dTH:i:s',
        type: 'date'
    }]
});