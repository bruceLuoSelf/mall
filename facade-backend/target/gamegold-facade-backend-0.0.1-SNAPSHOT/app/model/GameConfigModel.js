/*
 * 游戏配置信息
 */
Ext.define('MyApp.model.GameConfigModel', {
    extend: 'Ext.data.Model',
    fields: [{
        name: 'id'
    },{
        name: 'gameName'//游戏名称
    },{
        name: 'placeName'//地点名称
    },{
        name: 'mailTime',//邮寄时间
        type: 'int'
    },{
        name: 'autoPlayTime',//自动打款时间
        type: 'int'
    },{
        name: 'gameImage'//游戏商品图片
    },{
        name: 'placeImage'//地点截图
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
        name: 'commision',//佣金
        type: 'float'
    },{
        /************ZW_C_JB_00008_20170524 ADD START**************/
        name: 'goodsTypeName',//新增'商品类型'字段
        convert: function(v, record){
            //如果查询的'商品类目'值为空表示游戏币
            if(Ext.isEmpty(v)){
                return "游戏币";
            }
            return v;
        }
        /***********ZW_C_JB_00008_20170524 ADD END************/
    }]
});