/*
 * 防骗短信规则信息
 */
Ext.define('MyApp.model.TextMessageModel', {
    extend: 'Ext.data.Model',
    fields: [{
    	name: 'id'
    },{
	    name: 'name'//规则名称
	},{
        name: 'gameName'//游戏名称
    },{
    	name: 'orderStatus'//触发的订单状态
    },{
    	name: 'delay',//延时(秒)
    	type: 'int'
    },{
    	name: 'period',//周期
    	type: 'int'
    },{
        name: 'content'//短信内容
    },{
        name: 'operator'//操作员
    },{
        name: 'lastUpdateTime',//操作时间
        dateReadFormat: 'Y-m-dTH:i:s',
        type: 'date'
    },{
        name: 'enabled',//是否启用
        type: 'boolean'
    }]
});
