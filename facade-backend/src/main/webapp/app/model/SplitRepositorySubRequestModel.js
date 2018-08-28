/*
 * 分仓请求
 */
Ext.define('MyApp.model.SplitRepositorySubRequestModel', {
    extend: 'Ext.data.Model',
    fields: [{
        name: 'id',
        type: 'int'
    },{
    	name:'orderId'//分仓请求订单号
    },{
        name: 'useRepertoryCount'//金币来源
    },{
        name: 'count'//缺口值
    },{
        name: 'realCount'//实际分仓数量
    },{
        name: 'gameRole'//角色
    },{
        name: 'robotOtherReason'//自动化异常原因
    },{
    	name: 'status',//状态
        type: 'int'
    },{
        name: 'createTime',//更新时间
        dateReadFormat: 'Y-m-dTH:i:s',
        type: 'date'
    },{
        name: 'updateTime',//更新时间
        dateReadFormat: 'Y-m-dTH:i:s',
        type: 'date'
    }]
});