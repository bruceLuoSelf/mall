/**
 * Created by Administrator on 2017/1/4.
 */
/**
 /**
 * Created by Administrator on 2017/1/4.
 */
/**
 * 收货游戏配置
 */
Ext.define('MyApp.model.ShGameConfigModel', {
    extend: 'Ext.data.Model',
    fields: [{
        name: 'id',//
        type:'int'
    },{
        name: 'gameName'//游戏名称
    },{
        name: 'unitName'//单位
    },{
        name: 'isEnabled',// 是否启用
        type: 'boolean'
    },{
        name: 'createTime',//创建时间
        dateReadFormat: 'Y-m-dTH:i:s',
        type: 'date'
    },{
        name: 'updateTime',//最后更新时间
        dateReadFormat: 'Y-m-dTH:i:s',
        type: 'date'
    },{
        name: 'tradeType'//当前游戏支持的交易方式
    },{
        name: 'tradeTypeId'//当前游戏支持的交易方式ID
    },{
        name:'goodsTypeId',//游戏类目ID
        type:'int'
    },{
        name:'goodsTypeName'//游戏类目
    },{
        name:'minBuyAmount',//最低购买金额 ADD 20170606
        type: 'float'
    },{
        name:'enableRobot',//是否支持机器收货
        type: 'boolean'
    },{
        name:'enableMall',//商城是否启用 add by lcs
        type: 'boolean'
    },{
        name:'nineBlockConfigure' //九宫格json形式字符串
    },{
        name:'deliveryMessage' //发货信息提示 
    },{
        name:'nineBlockEnable'
    },{
        name:'minCount'
    },{
        name:'poundage'
    },{
        name:'repositoryCount' //库存上限
    },{
        name:'needCount'  //缺口上限
    },{
        name:'mailFee'  //邮寄手续费
    },{
        name:'thresholdCount'  //分仓阈值
    },{
        name:'isSplit' , //收货商分仓开关
        type: 'boolean'
    }]
});