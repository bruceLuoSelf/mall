/**
 * Created by jhlcitadmin on 2017/1/5.
 */

$(function () {
    me.init();
})

var user;
var me = {
    subOrderList: null,
    orderId: $.trim(getUrlParam("orderId")),
    ofableCount: null,
    init: function () {
        $.ajax({
            type: "GET",
            url: baseServiceUrl + "services/deliveryOrder/selectDeliverySubOrderData?t=" + new Date().getTime(),
            data: { orderId: me.orderId },
            contentType: "application/json; charset=UTF-8",
            dataType: "json",
            success: function (resp) {
                var code = resp.responseStatus.code;
                if (code == "00") {
                    deliveryOrder = resp.deliveryOrder;
                    subOrderList = resp.subOrderList;
                    orderStatus = resp.orderStatus;
                    roleNames = resp.roleNames;
                    me.initPage();
                    me.getLog();
                }
            }
        });
    },
    initPage: function () {
        var totalCount = 0;
        //初始化页面
        if (deliveryOrder != null) {
            $('#rc-span-gameName').html(deliveryOrder.gameName + "/" + deliveryOrder.region + "/" + deliveryOrder.server);
            $('#rc-sapn-recnum').text(deliveryOrder.id);
            $('#rc-sapn-ordernum').text(deliveryOrder.orderId);
            $('#rc-sapn-recprice').html(deliveryOrder.price + "<em>元 / " + deliveryOrder.moneyName + "</em>");
            //$('#rc-sapn-status').text(deliveryOrder.orderStatusStr);
            if (deliveryOrder.status == shStatus.Queue) {
                $('#rc-sapn-status').text("待分配角色");
            } else {
                $('#rc-sapn-status').text(deliveryOrder.orderStatusStr);
            }
            $('#rc-sapn-ordercount').html(deliveryOrder.count + "<em>" + deliveryOrder.moneyName + "</em>");
            if (deliveryOrder.tradeLogo == 5) {
                $('#rc-sapn-trade').text(deliveryOrder.address+"（机器收货）");
            } else {
                $('#rc-sapn-trade').text('');
            }
            $('#rc-sapn-total').text(deliveryOrder.amount);
            if (deliveryOrder.tradeLogo == 5) {
                $('#rc-sapn-add').text('无');
            } else {
                $('#rc-sapn-add').text(deliveryOrder.address);
            }
            //$('#rc-sapn-add').text(deliveryOrder.address);
            $('#rc-sapn-realcount').html(deliveryOrder.realCount*10000 + "<em>金</em>");
            $('#rc-sapn-name').text(deliveryOrder.roleName+"（"+deliveryOrder.sellerRoleLevel+"级）");
            $("#qq-link").attr("href", "tencent://message/?uin=" + deliveryOrder.serviceQq + "&Site=5173&Menu=yes");
            $("#qq-img").attr("src", "http://html.5173.com/dlc2c/images/global_v2/seller/qqinline.gif?p=2:" + deliveryOrder.serviceQq + ":1");
            $('#rc-sapn-createtime').text(new Date((deliveryOrder.createTime)).Format("yyyy-MM-dd hh:mm:ss"));
            if (deliveryOrder.tradeEndTime != null) {
                $('#rc-sapn-endtime').text(new Date((deliveryOrder.tradeEndTime)).Format("yyyy-MM-dd hh:mm:ss"));
            }
            // $('#rc-sapn-reason').text(deliveryOrder.orderId);
            if (subOrderList != null && subOrderList.length > 0) {
                for (var i in subOrderList) {
                    subOrder = subOrderList[i];
                    htmlStr = "<tr class='rc-table-tr'>";
                    htmlStr += "<td width='100'>" + subOrder.gameRole + "</td>";
                    htmlStr += "<td width='100'>" + subOrder.roleLevel + "</td>";
                    if(subOrder.robotImgList.length==0){
                        htmlStr += "<td width='100'><a></a></td>";
                    }else{
                      var arr=[];
                      htmlStr +="<td width='100'>"
                      for(var j in subOrder.robotImgList){
                        arr.push(subOrder.robotImgList[j].imgSrc)
                      }
                      htmlStr += "<a href=orderDetailImage.html?url="+arr.join(',')+" target=\"_black\">点击查看<br/></a>";
                      htmlStr +="</td>"
                    }
                    htmlStr += "<td width='80'>" + (subOrder.count*10000+subOrder.afterFour) + "</td>";
                    if(subOrder.realCount==null){
                        htmlStr += "<td width='200'>"+0;
                    }else{
                      htmlStr += "<td width='200'>"+(subOrder.realCount*10000);
                    }
                    htmlStr += "</td>";
                    htmlStr += "</tr>";
                    $('#rc-table').append(htmlStr);
                    totalCount += subOrder.realCount;
                }
            } else {
                $(".rc-table").append("");
            }
            var ofablCount = deliveryOrder.count - totalCount;
            $('#rc-fp-span').text(ofablCount);
        }
        //获取收货角色
        if (roleNames != null && roleNames.length > 0) {
            for (var i in roleNames) {
                var optionStr = "<option value ='" + roleNames[i] + "'>" + roleNames[i] + "</option>";
                $("#selsetName").append(optionStr);
            }
        } else {
            $("#selsetName").append('');
        }
        $('.rc-fp-em').text(deliveryOrder.moneyName);
    },
    getLog: function () {
        if (me.orderId == null || me.orderId == "") {
            return;
        }
        var request = {};
        request.id = me.orderId;
        $.ajax({
            type: "GET",
            url: baseServiceUrl + "services/chorder/getLogByChId?t=" + new Date().getTime(),
            data: request,
            contentType: "application/json; charset=UTF-8",
            dataType: "json",
            success: function (resp) {
                var code = resp.responseStatus.code;
                var userInfo = resp.userInfo;
                // console.log("用户",userInfo);
                if (code == "00") {
                    var orderLogList = resp.orderLogList;
                    var htmlStr = "";
                    for (var i = 0; i < orderLogList.length; i++) {
                        var order = orderLogList[i];
                        if (order.userType == '1') {
                            htmlStr += '<div class="hxMsgL hx_my">' + '<p>' + new Date(order.createTime).Format("yyyy-MM-dd hh:mm:ss") + "我:" + orderLogList[i].log + '</p>' + '</div>';
                        } else if (order.userType == '2') {
                            htmlStr += '<div class="hxMsgL hx_sh">' + '<p>' + new Date(order.createTime).Format("yyyy-MM-dd hh:mm:ss") + "出货商:" + orderLogList[i].log + '</p>' + '</div>';
                        }else{
                            htmlStr += '<div class="hxMsgL hx_sys">' + '<p>' + new Date(order.createTime).Format("yyyy-MM-dd hh:mm:ss") + "系统消息：" + orderLogList[i].log + '</p>' + '</div>';
                        }
                    }
                    $("#divLog").html(htmlStr);
                }
            }
        });

    }
};


// function validate(num){
//     var reg = new RegExp("^[0-9]*$");
//     if(!/^[0-9]*$/.test(num)){
//         alert("收货数量请输入数字!");
//         $('#rcPrice').val("")
//         return false
//     }
//     return true
// }

//交易类型
var shStatus = {
    "Waiting": 1, // 等待交易
    "Queue": 2, // 排队中
    "Dealing": 3,//交易中
    "Complete": 4,//交易完成
    "PartComplete": 5,//部分完单
    "Cancle": 6,//撤单
    "HumanOp": 7,//需人工介入
    "ApplyCompletePart": 8,//申请部分完单
    "WaitDelivery": 9,//待发货
    "DeliveryFinish": 10//已发货
}
