/*
 * 安心买游戏币数量下拉菜单信息
 */
Ext.define('MyApp.model.ShAddressModel', {
    extend: 'Ext.data.Model',
    fields: [{
        name: 'id'
    },{
    	name: 'gameName'//游戏名称
    },{
        name: 'tradeAddress'//交易地点
    },{
        name: 'enabled',//启用禁用
        type: 'boolean'
    }]
});