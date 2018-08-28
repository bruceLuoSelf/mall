/*
 * 出货订单
 */
Ext.define('MyApp.model.DeliveryOrderModel', {
    extend: 'Ext.data.Model',
    idProperty: 'extId',
    idgen: 'uuid',
    fields: [{
        name: 'id',
        type: 'int'
    },{
        name: 'cgId',//关联的采购单号
        type: 'int'
    },{
        name: 'orderId'//出货单编号
    },{
        name: 'gameProp',//游戏属性(游戏/区/服)
        convert: function(v, record){
            if(Ext.isEmpty(record.get('gameRace'))){
                return v;
            }
            return v+" —>"+record.get('gameRace');
        }
    },{
        name: 'sellerAccount'//出货方5173账号
    },{
        name: 'buyerAccount'//收货方5173账号
    },{
        name: 'gameName'//游戏名
    },{
        name: 'region'//游戏区
    },{
        name: 'server'//游戏服
    },{
        name: 'gameRace'//游戏阵营
    },{
        name: 'roleName'//出货角色名
    },{
        name: 'price',//出货单价
        type: 'float'
    },{
        name: 'count',//计划出货数量
        type: 'int'
    },{
        name: 'amount',//出货金额
        type: 'float'
    },{
        name: 'realCount',//实际出货数量
        type: 'int'
    },{
        name: 'realAmount',//实际出货金额
        type: 'float'
    },{
        name: 'phone'//出货方手机号
    },{
        name: 'qq'//出货方QQ
    },{
        name: 'words'//密语
    },{
        name: 'address'//交易地点
    },{
        name: 'status',//交易状态
        type: 'int'
    },{
        name: 'transferStatus',//转账状态
        type: 'int'
    },{
        name: 'reason',//撤单原因
        type: 'int'
    },{
        name: 'otherReason'//其他原因
    },{
        name: 'createTime',//创建时间
        dateReadFormat: 'Y-m-dTH:i:s',
        type: 'date'
    },{
        name: 'tradeStartTime',//开始交易时间
        dateReadFormat: 'Y-m-dTH:i:s',
        type: 'date'
    },{
        name: 'tradeEndTime',//交易完成时间
        dateReadFormat: 'Y-m-dTH:i:s',
        type: 'date'
    },{
        name: 'deliveryType',//收货方式
    },{
        name: 'tradeType',//交易方式
    },{
        name: 'deliveryTypeName',//收货方式名称
    },{
        name: 'tradeTypeName',//交易方式名称
    },{
        name: 'isTimeout',//交易方式名称
    },{
        name: 'takeOverSubjectId',//接收物服
    },{
        name:'machineArtificialReason'//机器转人工原因
    },{
        name:'machineArtificialTime',//机器转人工时间
        dateReadFormat: 'Y-m-dTH:i:s',
        type:'date'
    },{
        name: 'machineArtificialStatus',//机器转人工状态
        type: 'int'
    },{
        name: 'appealReason'//申诉单申诉原因
    },{
        name: 'appealOrder'//原始子订单号
    },{
        name: 'relationOrderId'//原始主订单号
    }]
});