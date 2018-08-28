/**
 * Created by jhlcitadmin on 2017/3/17.
 */

/**
 * 游戏详细库存限制配置管理
 */
Ext.define('MyApp.model.GameRepositoryModel', {
    extend: 'Ext.data.Model',
    fields: [{
        name: 'id',//
        type:'long'
    },{
        name: 'gameName'//游戏名称
    },{
        name: 'goodsTypeName'//游戏名称
    },{
        name: 'repositoryCount'//库存限制
    },{
        name: 'createTime',//创建时间
        dateReadFormat: 'Y-m-dTH:i:s',
        type: 'date'
    },{
        name: 'updateTime',//最后更新时间
        dateReadFormat: 'Y-m-dTH:i:s',
        type: 'date'
    },{
        name: 'enabled',// 是否启用
        type: 'boolean'
    },{
        name: 'regionName'//区信息
    },{
        name: 'serverName'//服信息
    },{
        name: 'raceName'//正营信息
    }]
});