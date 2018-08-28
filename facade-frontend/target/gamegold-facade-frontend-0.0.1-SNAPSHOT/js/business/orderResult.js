$(function () {
    me.init();
});

var me={
    orderId:null,
    deliveryOrder:null,
    init:function(){
        me.orderId=$.trim(getUrlParam("orderId"));
        if(me.orderId==""){
            alert("缺少必要的参数！");
            return;
        }
        var request = {};
        request.orderId=me.orderId;
        $.ajax({
            type: "GET",
            url: baseServiceUrl + "services/chorder/selectOrderById?t="+new Date().getTime(),
            data: request,
            contentType: "application/json; charset=UTF-8",
            dataType: "json",
            success: function (resp) {
                var code = resp.responseStatus.code;
                if (code == "00") {
                    me.deliveryOrder=resp.deliveryOrder;
                    me.initPageData();
                    me.getLog();
                }
            }
        });
    },
    initPageData:function(){
        var htmlStr="";
        if(me.deliveryOrder!=null&&me.deliveryOrder!=""){
            var order=me.deliveryOrder;
            for(var p in ChOrderState){
                if(order.status==ChOrderState[p].code){
                    //htmlStr+="<span class=\"b6\">"+ChOrderState[p].value+"</span>";
                    if(order.status==shStatus.HumanOp){
                        htmlStr+="<dt><span></span></dt><dd class=\"txt_b\">此订单"+ChOrderState[p].value+"</dd>";
                    }
                    else if(order.status==shStatus.Cancle){
                        htmlStr+="<dt><span></span></dt><dd class=\"txt_b\">此订单已"+ChOrderState[p].value+"</dd>";

                        for(var p in FullChCancleReason) {
                            if (order.reason == FullChCancleReason[p].code) {
                                htmlStr+="<dt class=\"txt_b\">撤销原因：</dt><dd class=\"txt_b\">"+FullChCancleReason[p].value+"</dd>";
                            }
                        }
                        if($.trim(order.otherReason).length>0){
                            htmlStr+="<dt class=\"txt_b\">其他原因：</dt><dd class=\"txt_b\">"+order.otherReason+"</dd>";
                        }

                    }
                    else if(order.status==shStatus.Complete||order.status==shStatus.PartComplete){
                        htmlStr+="<dt><span></span></dt><dd class=\"txt_b\">此订单已"+ChOrderState[p].value+"</dd>";
                    }
                    else{
                        window.open(baseHtmlUrl + "myShOrder.html?orderId="+me.orderId+"&t="+(new Date()).getTime(), "_self");
                    }
                    break;
                }
            }
            //htmlStr+="<dt><span></span></dt><dd class=\"txt_b\">此订单已取消</dd>";

            htmlStr+="<dt>订单编号：</dt><dd>"+order.orderId+"</dd>";
            htmlStr+="<dt>订单时间：</dt><dd>"+new Date(order.createTime).Format("yyyy-MM-dd hh:mm:ss")+"</dd>";
            htmlStr+="<dt>游戏区服：</dt><dd>"+order.gameName+"/"+order.region+"/"+order.server+"</dd>";
            htmlStr+="<dt>出货方：</dt><dd>"+order.sellerAccount+"</dd>";
            htmlStr+="<dt>出货角色：</dt><dd>"+order.roleName+"</dd>";
            htmlStr+="<dt>订单数量：</dt><dd>"+order.realCount+order.moneyName+"</dd>";
            htmlStr+="<dt>单价：</dt><dd>"+order.price+"元/"+order.moneyName+"</dd>";
            htmlStr+="<dt>成交金额：</dt><dd>"+order.realAmount+"元</dd>";
            htmlStr+="<dt>交易地点：</dt><dd>"+order.address+"</dd>";
        }

        $("#divOrder dl").html(htmlStr);

        //成功/失败图标显示
        if(order.status==6){
            $(".chddqx_top dl dt span").css("background-position","-302px -81px");
        }else{
            $(".chddqx_top dl dt span").css("background-position","-342px -45px");
        }
    },
    getLog:function(){
        if(me.orderId==null||me.orderId==""){
            return;
        }
        //日志
        var request = {};
        request.id=me.orderId;
        $.ajax({
            type: "GET",
            url: baseServiceUrl + "services/chorder/getLogByChId?t="+new Date().getTime(),
            data: request,
            contentType: "application/json; charset=UTF-8",
            dataType: "json",
            success: function (resp) {
                var code = resp.responseStatus.code;
                var userInfo = resp.userInfo;
                if (code == "00") {
                    var orderLogList=resp.orderLogList;
                    var htmlStr="";
                    for(var i=0;i<orderLogList.length;i++){
                        var order=orderLogList[i];
                        console.log("数据库查询的数据",order);
                        if (order.userType == '3'&&order.userAccount==userInfo.loginAccount) {
                            htmlStr += '<div class="hxMsgL hx_sys">' + '<p>' + new Date(order.createTime).Format("yyyy-MM-dd hh:mm:ss") + "系统消息:" + orderLogList[i].log + '</p>' + '</div>';
                        } else if (order.userType == '2'&&order.userAccount==userInfo.loginAccount) {
                            htmlStr += '<div class="hxMsgL hx_my">' + '<p>' + new Date(order.createTime).Format("yyyy-MM-dd hh:mm:ss") + "我:" + orderLogList[i].log + '</p>' + '</div>';
                        }else if (order.userType == '1'&&order.userAccount==userInfo.loginAccount){
                            htmlStr += '<div class="hxMsgL hx_sh">' + '<p>' + new Date(order.createTime).Format("yyyy-MM-dd hh:mm:ss") + "收货商:" + orderLogList[i].log + '</p>' + '</div>';
                        }
                    }
                    $("#divLog").html(htmlStr);
                }
            }
        });
    }
}

//交易类型
var shStatus = {
    "Waiting":1, // 等待交易
    "Queue":2, // 排队中
    "Dealing":3,//交易中
    "Complete":4,//交易完成
    "PartComplete":5,//部分完单
    "Cancle":6,//撤单
    "HumanOp":7//需人工介入
}