/*
 * 库存信息
 *  * Date        Name                Reason for change
 * ----------  ----------------    ----------------------
 * 2017/05/12  liuchanghua           ZW_C_JB_00008 商城增加通货
 */
Ext.define('MyApp.model.ConfigPowerModel', {
    extend: 'Ext.data.Model',
    fields: [{
        name: 'id',
        type: 'int'
    },{
        name: 'gameName'//游戏名称
    },{
        name: 'configPower',//配单权限
        type: 'boolean'
    },{
    	name:'configMaxCount',
    	type:'int'
    },/**ZW_C_JB_00008_20170512 START**/
        {
        name:'goodsTypeName',
        type:'String'
    }
        /**ZW_C_JB_00008_20170512 END**/
        ,{
        name: 'tradeType',//交易方式
        convert: function(v, record){
    		if(Ext.isEmpty(v)){
    			return null;
    		}
    		return v;
    	},
        type: 'int'
    }]
});