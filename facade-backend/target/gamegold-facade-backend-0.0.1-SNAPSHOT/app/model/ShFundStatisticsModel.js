
/**资金统计
 * Created by Administrator on 2016/12/15.
 */
Ext.define('MyApp.model.ShFundStatisticsModel', {
    extend: 'Ext.data.Model',
    fields: [{
        name: 'id',
        type: 'int'
    },{
        name: 'qcBalance',//期初余额
        type: 'float'
    },{
        name: 'zfAmount',//支付金额
        type: 'float'
    },{
        name: 'tkAmount',//退款金额
        type: 'float'
    },{
        name: 'fkAmount',//付款金额
        type: 'float'
    },{
        name: 'jbTozbao',//金币转7bao金额
        type: 'float'
    },{
        name: 'qmBalance',//期末余额
        type: 'float'
    },{
        name: 'startTime',//期初时间
        dateReadFormat: 'Y-m-dTH:i:s',
        type: 'date'
    },{
        name: 'endTime',//期末时间
        dateReadFormat: 'Y-m-dTH:i:s',
        type: 'date'
    }]
});