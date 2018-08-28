//出货地址配置
Ext.define('MyApp.model.ConfigModel', {
    extend: 'Ext.data.Model',
    fields: [{
        name: 'id',
        type: 'int'
    }, {
        name: 'configKey'//配置名称
    }, {
        name: 'configValue'//配置的值
    }, {
        name: 'remark',//备注
    }, {
        name: 'enabled',//是否启用
        type: 'boolean'
    }]
});