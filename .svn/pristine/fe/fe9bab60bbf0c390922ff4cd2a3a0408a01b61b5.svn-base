var orderId_orderState;
var orderInfo_orderState;
var initClip = false;
var orderState;
var orderDelay;

$(document).ready(function () {
    orderId_orderState = getUrlParam("orderId");
    // orderId_orderState = 'SG1711280001082';
    getOrderState();
});
function getOrderState() {
    // var request = {};
    // request.orderId = orderId_orderState;
    $.ajax({
        type: "GET",
        url: baseServiceUrl + "services/chorder/selectBuyerOrderById?t=" + new Date().getTime(),
        data: {orderId: orderId_orderState},
        contentType: "application/json; charset=UTF-8",
        dataType: "jsonp",
        success: function (resp) {
            var code = resp.responseStatus.code;
            if (code == "00") {
                var deliveryOrder = resp.deliveryOrder;
                // options.user = deliveryOrder.buyerHxAccount;
                // options.pwd = deliveryOrder.buyerHxPwd;
                // chatroomId = deliveryOrder.chatroomId;
                $("#regionServer").html(deliveryOrder.gameName + "/" + deliveryOrder.region + "/" + deliveryOrder.server);
                $("#chId").html(deliveryOrder.id);
                $("#orderId").html(deliveryOrder.orderId);
                $("#price").html(deliveryOrder.price);
                for (var p in ChOrderState) {
                    if (deliveryOrder.status == ChOrderState[p].code) {
                        $("#orderState").html(ChOrderState[p].value);
                        break;
                    }
                }
                $("#totalCount").html(deliveryOrder.count);
                $("#totalPrice").html(deliveryOrder.amount);
                $("#realCount").html(deliveryOrder.realCount);

                if (deliveryOrder.createTime != null) {
                    $("#creatTime").html(new Date(deliveryOrder.createTime).Format("yyyy-MM-dd hh:mm:ss"));
                } else {
                    $("#createTimeView").hide();
                }
                if (deliveryOrder.startTime != null) {
                    $("#startTime").html(new Date(deliveryOrder.startTime).Format("yyyy-MM-dd hh:mm:ss"));
                } else {
                    $("#startTimeView").hide();
                }
                if (deliveryOrder.endTime != null) {
                    $("#endTime").html(new Date(deliveryOrder.endTime).Format("yyyy-MM-dd hh:mm:ss"));
                } else {
                    $("#endTimeView").hide();
                }
                // conn.open(options);
                getDeliverySubOrderList(deliveryOrder.id);
            }
        }
    });

    function getDeliverySubOrderList(getChId) {
        $.ajax({
            type: "GET",
            url: baseServiceUrl + "services/chorder/getDeliverySubOrderList?t=" + new Date().getTime(),
            data: {chId: getChId},
            contentType: "application/json; charset=UTF-8",
            dataType: "json",
            success: function (resp) {
                var code = resp.responseStatus.code;
                if (code == "00") {
                    var htmlStr = "";
                    var subOrderList = resp.deliverySubOrderList;
                    console.log(subOrderList)
                    if (subOrderList != null && subOrderList.length > 0) {
                        for (var i in subOrderList) {
                            var subOrder = subOrderList[i];
                            var statusStr = "";
                            console.info(subOrder);
                            for (var p in ChOrderState) {
                                if (subOrder.status == ChOrderState[p].code) {
                                    statusStr = ChOrderState[p].value;
                                    break;
                                }
                            }
                            htmlStr += "<tr>\n" +
                                "<td>"+subOrder.gameAccount+"</td>\n" +
                                "<td>"+subOrder.gameRole+"</td>\n" +
                                "<td>"+subOrder.count+"万金</td>\n" +
                                "<td>"+subOrder.realCount+"万金</td>\n" +
                                "<td>"+statusStr+"</td>\n" +
                                "<td><a href=\""+baseHtmlUrl +"repositoryGameAccount.html?gameName="+escape(subOrder.gameName)+"&region="+escape(subOrder.region)+"&server="+escape(subOrder.server)+"&gameRace=&gameAccount="+escape(subOrder.gameAccount)+"&roleName="+escape(subOrder.gameRole)+"\" target=\"_blank\">查看库存</a></td>\n" +
                                "</tr> "
                        }
                    }else {
                        $(".search_list").hide();
                    }
                    $("#subOrderView").html(htmlStr);
                    $("#OpenWindowLog").css("left", "40%");
                }
            }

        })
    }
    

}
$