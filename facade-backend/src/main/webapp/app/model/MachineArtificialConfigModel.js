/**
 * Created by jhlcitadmin on 2017/3/17.
 */

/**
 * 机器转人工配置管理
 */
Ext.define('MyApp.model.MachineArtificialConfigModel', {
    extend: 'Ext.data.Model',
    fields: [{
        name: 'id',
        type: 'long'
    }, {
        name: 'gameName'//游戏名称
    }, {
        name: 'createTime',//创建时间
        dateReadFormat: 'Y-m-dTH:i:s',
        type: 'date'
    }, {
        name: 'updateTime',//最后更新时间
        dateReadFormat: 'Y-m-dTH:i:s',
        type: 'date'
    }, {
        name: 'enable',// 是否启用
        type: 'boolean'
    }]
});