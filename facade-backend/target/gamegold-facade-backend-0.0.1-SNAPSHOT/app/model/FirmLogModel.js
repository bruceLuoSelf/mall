/*
 * Update History
 * Date        Name                Reason for change
 * ----------  ----------------    ----------------------
 * 2017/07/17  wurongfan           ZW_J_JB_00072金币商城对外接口优化
 *
 */
Ext.define('MyApp.model.FirmLogModel', {
    extend: 'Ext.data.Model',
    idProperty: 'extId',
    idgen: 'uuid',
    fields: [{
        name: 'id',
        type: 'int'
    },{
        name: 'userId'
    },{
        name: 'userAccount' 
    },{
        name: 'userType', 
    },{
        name:'log',
    },{
        name:'modifyFirmName',
    },{
        name: 'updateTime',//创建时间
        dateReadFormat: 'Y-m-dTH:i:s',
        type: 'date'
    }]
});