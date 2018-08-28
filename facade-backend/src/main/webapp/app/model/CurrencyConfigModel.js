/**
 * Created by Administrator on 2017/1/4.
 */
/*
/**
 * 收货游戏配置
 */
Ext.define('MyApp.model.CurrencyConfigModel', {
    extend: 'Ext.data.Model',
    fields: [{
        name: 'id',//
        type:'int'
    },{
        name: 'gameName'//游戏名称
    },{
        name: 'goodsType'//商品类型
    },{
        name: 'field'//字段
    },{
        name: 'fieldMeaning'//字段含义
    },{
        name: 'fieldType'//字段类型
    },{
        name: 'minValue'//最小值
    },{
        name: 'maxValue'//最大值
    },{
        name: 'enabled',//是否必填
        type: 'boolean'
    },{
        name: 'value'//值
    },{
        name: 'unitName'//单位
    },{
        name: 'sort'//排序值
    }]
});