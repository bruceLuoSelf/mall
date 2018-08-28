/*
 * 出货子订单
 */
Ext.define('MyApp.model.DeliverySubOrderModel', {
    extend: 'Ext.data.Model',
    fields: [{
        name: 'chId',//关联的主订单
        type: 'int'
    },{
        name: 'gameAccount'//关联的采购单号
    },{
        name: 'gameRole'//收货的角色名
    },{
        name: 'count',//收货数量
        type: 'int'
    },{
        name: 'realCount',//实际收货数量
        type: 'int'
    },{
        name: 'status',//状态
        type: 'int'
    },{
        name: 'createTime',//创建时间
        dateReadFormat: 'Y-m-dTH:i:s',
        type: 'date'
    },{
        name: 'updateTime',//更新时间
        dateReadFormat: 'Y-m-dTH:i:s',
        type: 'date'
    },{
        name: 'reason',//撤单原因
        type: 'int'
    },{
        name: 'otherReason'//其他原因
    },{
        name:'takeOverSubject'//接手物服
    },{
        name:'takeOverSubjectId'//子订单 单独用  接手物服
    },{
        name:'machineArtificialReason'//机器转人工原因
    },{
        name:'machineArtificialTime',//机器转人工时间
        dateReadFormat: 'Y-m-dTH:i:s',
        type:'date'
    },{
        name: 'isTimeout',//是否超时
        type: 'boolean'
    },{
        name:"remarks" //备注
    }]
});