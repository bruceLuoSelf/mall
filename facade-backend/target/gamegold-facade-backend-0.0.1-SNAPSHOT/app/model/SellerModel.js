/*
 * 卖家信息
 */
Ext.define('MyApp.model.SellerModel', {
    extend: 'Ext.data.Model',
    fields: [{
        name: 'id',
        type: 'int'
    },{
        name: 'manualStatus',//卖家库存是否是人工库存
        type: 'boolean'
    },{
        name: 'isShielded',//卖家是否屏蔽
        type: 'boolean'
    },{
        name: 'messagePower',//卖家是否可以收到短信
        type: 'boolean'
    },{
        name: 'uid'//用户id(5173的用户id)
    },{
        name: 'loginAccount'//用户账号(5173的用户账号)
    },{
        name: 'servicerId',//所属客服id
        type: 'int'
    },{
        name: 'name'//联系人
    },{
        name: 'phoneNumber'//联系电话
    },{
    	name: 'qq'//QQ
    },{
    	name: 'sellerType',//卖家类型
        type: 'int'
    },{
        name: 'notes'//审核备注信息
    },/*,{
        name: 'password'//游戏密码
    },{
        name: 'gameName'//游戏名
    },{
        name: 'region'//游戏区
    },{
        name: 'server'//游戏服
    },{
        name: 'gameRace'//所属阵营
    },*/{
        name: 'checkState',//审核状态
        type: 'int'
    },{
        name: 'lastUpdateTime',//最后更新时间
        dateReadFormat: 'Y-m-dTH:i:s',
        type: 'date'
    },{
        name: 'createTime',//创建时间
        dateReadFormat: 'Y-m-dTH:i:s',
        type: 'date'
    },{
    	name: 'isDeleted',//是否删除
    	type: 'boolean'
    },{
        name: 'games'
    },{
        name: 'isShieldedType'
    },{
        name: 'isHelper'
    },{
        name: 'shopName'
    },{
        name: 'isOnline',
        type: 'boolean'
    },{
        name: 'openShState'
    },{
        name: 'isPriceRob',
        type: 'boolean'
    },{
        name: 'sevenBaoAccount'//7bao账号
    }, {
        name: 'isAgree',//是否同意资金托管
        type: 'boolean'
    },{
        name: 'isNewFund',//是否同意资金托管
        type: 'boolean'
    },{
        name: 'shippingGameProp'//发货区服
    },{
        name: 'isCross',//是否开启跨区合并IS_CROSS
        type: 'boolean'
    }
    ]
});