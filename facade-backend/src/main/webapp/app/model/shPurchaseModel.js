//采购商
Ext.define('MyApp.model.shPurchaseModel', {
    extend: 'Ext.data.Model',
    idProperty: 'extId',
    idgen: 'uuid',
    fields: [{
        name: 'id', //id
        type: 'int'
    },{
        name: 'totalAmount',  //总金额
        type: 'float'
    },{
        name: 'availableAmount',//可用金额
        type: 'float'
    },{
        name: 'freezeAmount',// 冻结金额
        type: 'float'
    },{
        name: 'totalAmountZBao',  //7bao总金额
        type: 'float'
    },{
        name: 'availableAmountZBao',//7bao可用金额
        type: 'float'
    },{
        name: 'freezeAmountZBao',// 7bao冻结金额
        type: 'float'
    },{
        name: 'startTime', //创建时间
    },{
        name: 'endTime',//结束时间
    },{
        name: 'cjl',//成交率
    },{
        name: 'tradingVolume'//成交量
    },{
        name: 'pjys',//平均用时
    },{
        name: 'credit'//商家信誉
    },{
        name: 'name'//商家姓名
    },{
        name: 'loginAccount'//商家账号
    },{
        name: 'name'
    },{
        name: 'loginAccount'
    },{
        name: 'phoneNumber'
    },{
        name: 'deliveryType' //收货方式
    },{
        name: 'tradeType'   //交易模式
    },{
        name: 'tradeTypeName' //交易模式名称
    },{
        name: 'isSplit' ,//收货商分仓开关
        type: 'boolean'
    },{
        name: 'repositoryCount' //库存上限
    },{
        name: 'needCount' //缺口上限
    }]
});