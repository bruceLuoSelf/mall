/*
评价信息
 */
Ext.define('MyApp.model.ServiceEvaluateModel', {
    extend: 'Ext.data.Model',
    fields: [{
        name: 'id',
        type: 'int'
    },{
        name: 'account'//客服账号
    },{
        name: 'totalCount',//评价笔数
        type: 'int'
    },{
        name: 'goodRate',//好评率
        type: 'float'
    },{
        name: 'veryGoodCount',//非常满意笔数
        type: 'int'
    },{
        name: 'goodCount',//满意笔数
        type: 'int'
    },{
        name: 'normalCount',//一般笔数
        type: 'int'
    },{
        name: 'yawpCount',//不满意笔数
        type: 'int'
    },{
        name: 'veryYawpCount',//非常不满意笔数
        type: 'int'
    },{
        name: 'defaultCount',//默认评价笔数
        type: 'int'
    }]
});