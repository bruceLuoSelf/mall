/*
 * 等级携带上限
 */
Ext.define('MyApp.store.LevelCarryLimitStore',{
    extend:'Ext.data.Store',
    requires:[
        'MyApp.model.LevelCarryLimitModel'
    ],
    constructor:function (cfg) {
        var me = this;
        cfg = cfg ||{};
        me.callParent([Ext.apply({
            model:'MyApp.model.LevelCarryLimitModel',
            proxy:{
                type:'ajax',
                actionMethods:'POST',
                url:'./delivery/queryLevelCarryLimit.action',
                reader:{
                    type:'json',
                    root:'levelCarryLimitEOList',
                    totalProperty:'totalCount'
                }
            }
        },cfg)]);
    }
});