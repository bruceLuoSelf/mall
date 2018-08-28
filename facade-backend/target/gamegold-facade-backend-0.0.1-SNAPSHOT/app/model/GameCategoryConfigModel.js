//游戏类目配置
Ext.define('MyApp.model.GameCategoryConfigModel', {
    extend: 'Ext.data.Model',
    fields: [{
        name: 'id',
        type: 'int'
    }, {
        name: 'name'//交易方式名称
    }, {
        name: 'isEnabled',//是否启用
        type: 'boolean'
    }, {
        name: 'createTime',//创建时间
        dateReadFormat: 'Y-m-dTH:i:s',
        type: 'date'
    }, {
        name: 'updateTime',//最后更新时间
        dateReadFormat: 'Y-m-dTH:i:s',
        type: 'date'
    }]
});