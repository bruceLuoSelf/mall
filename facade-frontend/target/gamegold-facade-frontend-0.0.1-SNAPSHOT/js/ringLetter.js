//新环信客服取参数
var par = {}
function getServiceHxAccount(orderId,type){
    $.ajax({
        type: "GET",
        url: baseServiceUrl + "services/orderdata/getOrderIm",
        data: {
            orderId:orderId,
            userType:type
        },
        contentType: "application/json; charset=UTF-8",
        dataType: "jsonp",
        jsonpCallback: "jsonp",
        success: function (resp) {

        }
    });

    // $.ajax({
    //     type: 'get',
    //     url: baseServiceUrl + "services/orderdata/getOrderIm",
    //     contentType : "application/json; charset=utf-8" ,
    //     dataType: "jsonp",
    //     jsonpCallback:"jsonp",
    //     data: {
    //         orderId:orderId,
    //         userType:type
    //     },
    // }).done(function (res) {
    //
    // });
};

function jsonp(data){
    getUserAccount();
    par = data 
}

function getUserAccount(){
     $.ajax({
        type: 'get',
        url: 'http://chatregister.5173.com/Easemob/GetUserAccount',
        contentType : "application/json; charset=utf-8" ,
        dataType: "jsonp",
        jsonpCallback:"getUserAcc"
    }).done(function (res) {
        
    });

};
function getUserAcc(data){
    console.log('getUserAcc',data)
    par.HXAccount=data.Result.UserAccount
    par.HXPwd=data.Result.Password
    par.HXAppkey=data.Result.AppKey
    showbox(par)
}