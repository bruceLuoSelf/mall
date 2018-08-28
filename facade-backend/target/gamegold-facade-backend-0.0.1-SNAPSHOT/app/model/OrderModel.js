/*
 * 订单信息
 */
Ext.define('MyApp.model.OrderModel', {
    extend: 'Ext.data.Model',
    idProperty: 'extId',
    idgen: 'uuid',
    fields: [{
    	name: 'extId'
    },{
    	name: 'id',
    	type: 'int'
    },{
	    name: 'orderId'//订单号
	},{
    	name: 'uid',//订单所属用户ID
    	type: 'int'
    },{
    	name: 'userAccount'//订订单所属用户帐号
    },{
        name: 'servicerId',//订单选择客服id
        type: 'int'
    },{
        name: 'servicerInfo'//客服信息
    },{
        name: 'tradeType',//交易方式
        convert: function(v, record){
    		if(Ext.isEmpty(v)){
    			return null;
    		}
    		return v;
    	},
        type: 'int'
    },{
        name: 'mobileNumber'//联系方式
    },{
        name: 'qq'//QQ
    },{
        name: 'receiver'//收货人姓名(游戏角色)
    },{
        name: 'goodsId',//商品ID
        type: 'int'
    },{
        name: 'goodsCat', // 商品所属栏目
        type: 'int'
    },{
        name: 'gameName'//游戏名称
    },{
        name: 'region'//所在区
    },{
        name: 'server'//所在服
    },{
        name: 'gameRace'//所在阵营
    },{
        name: 'gameLevel',//游戏等级
        type: 'int'
    },{
        name: 'deliveryTime',//发货速度(几分钟内可以发货，单位：分钟)
        type: 'int'
    },{
        name: 'title'//发布单名称
    },{
        name: 'goldCount',//购买总数(购买金币数)
        type: 'int'
    },{
        name: 'unitPrice',//商品单价(1金币对应多少人民币)
        type: 'float'
    },{
    	name: 'totalPrice',//总费用
    	type: 'float'
    },{
    	name: 'orderState',//订单状态
    	type: 'int'
    }, {
        name: 'goodsTypeId',//商品类型id
        type: 'int'
    }, {
        name: 'goodsTypeName'//商品类型名称
    },{
    	name: 'manualOperation',//人工操作
    	type: 'boolean'
    },{
    	name: 'isDelay',//是否延迟
    	convert: function(v, record){
    		if(Ext.isEmpty(v)){
    			return null;
    		}
    		return v;
    	},
    	type: 'boolean'
    },{
    	name: 'isHaveStore',//是否有货
    	convert: function(v, record){
    		if(Ext.isEmpty(v)){
    			return null;
    		}
    		return v;
    	},
    	type: 'boolean'
    },{
        name: 'createTime',//下单时间
        dateReadFormat: 'Y-m-dTH:i:s',
        type: 'date'
    },{
        name: 'sellerLoginAccount' //商品所属卖家
    },{
        name: 'payTime',//付款时间
        dateReadFormat: 'Y-m-dTH:i:s',
        type: 'date'
    },{
        name: 'sendTime',//发货时间
        dateReadFormat: 'Y-m-dTH:i:s',
        type: 'date'
    },{
        name: 'endTime',//结束时间
        dateReadFormat: 'Y-m-dTH:i:s',
        type: 'date'
    },{
        name: 'notes'//备注，买家留言
    },{
		name: 'refundReason',
        type: 'int'
	},{
        name: 'insuranceAmount', //保费
        type: 'float'
    },{
        name: 'insuranceExpireTime', //保险有效期
        type: 'date'
    },{
        name: 'insuranceRate', //保险费率,
        type: 'float'
    },{
        name: 'remark' //备注
    },{
        name: 'refererType',
        type: 'int'
    },{
        name: 'internetBar'
    },{
        name: 'redEnvelope', //红包,
        type: 'float'
    },{
        name: 'shopCoupon', //店铺券,
        type: 'float'
    },{
        name: 'insuranceAmount', //保险费用,
        type: 'float'
    },{
        name: 'serviceCharge', //视频服务费,
        type: 'float'
    },{
        name: 'gameLevel', //游戏等级,
        type: 'int'
    },{
        name: 'gameNumberId' //收货角色数字ID,
    },{
        name: 'field' //扩展信息,
    }]
});