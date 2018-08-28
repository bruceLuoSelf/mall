$(function () {
    me.init();
});

var me = {
    orderId: null,
    deliveryOrder: null,
    init: function () {
        me.orderId = $.trim(getUrlParam("orderId"));
        if (me.orderId == "") {
            alert("缺少必要的参数！");
            return;
        }
        var request = {};
        request.orderId = me.orderId;
        $.ajax({
            type: "GET",
            url: baseServiceUrl + "services/chorder/selectOrderById?t=" + new Date().getTime(),
            data: request,
            contentType: "application/json; charset=UTF-8",
            dataType: "json",
            success: function (resp) {
                var code = resp.responseStatus.code;
                if (code == "00") {
                    me.deliveryOrder = resp.deliveryOrder;
                    me.initPageData();
                    me.getLog();
                }
            }
        });
    },
    initPageData: function () {
        var htmlStr = "";
        if (me.deliveryOrder != null && me.deliveryOrder != "") {
            var order = me.deliveryOrder, trHtml = '', trType = '';
            for (var p in ChOrderState) {
                if (order.status == ChOrderState[p].code) {
                    //htmlStr+="<span class=\"b6\">"+ChOrderState[p].value+"</span>";
                    if (order.status == shStatus.HumanOp) {
                        $('#orderSt').text('此订单'+ChOrderState[p].value)
                        //htmlStr += "<dt><span></span></dt><dd class=\"txt_b\">" + ChOrderState[p].value + "</dd>";
                    }
                    else if (order.status == shStatus.Cancle) {
                        $('#orderSt').text('此订单已'+ChOrderState[p].value)
                        $('#trIcon').removeClass().addClass('icon-revoke')
                        //htmlStr += "<dt><span></span></dt><dd class=\"txt_b\">此订单已" + ChOrderState[p].value + "</dd>";

                        // for (var p in FullChCancleReason) {
                        //     if (order.reason == FullChCancleReason[p].code) {
                        //         htmlStr += "<dt class=\"txt_b\">撤销原因：</dt><dd class=\"txt_b\">" + FullChCancleReason[p].value + "</dd>";
                        //     }
                        // }
                        // if ($.trim(order.otherReason).length > 0) {
                        //     htmlStr += "<dt class=\"txt_b\">其他原因：</dt><dd class=\"txt_b\">" + order.otherReason + "</dd>";
                        // }

                    }
                    else if (order.status == shStatus.Complete || order.status == shStatus.PartComplete) {
                        //htmlStr += "<dt><span></span></dt><dd class=\"txt_b\">此订单已" + ChOrderState[p].value + "</dd>";
                        $('#orderSt').text('此订单已'+ChOrderState[p].value)
                        $('#trIcon').removeClass().addClass('icon-suc')
                    }
                    else {
                        window.open(baseHtmlUrl + "myShOrder.html?orderId=" + me.orderId + "&t=" + (new Date()).getTime(), "_self");
                    }
                    break;
                }
            }
            //htmlStr+="<dt><span></span></dt><dd class=\"txt_b\">此订单已取消</dd>";
            if (order.status == 4 || order.status == 5) {
                if(order.tradeLogo!=5){
                  trHtml = '<div class="check-link"><span>预收货数量：</span><span>' + order.count + order.moneyName + '</span></div>' +
                      '<div class="check-link"><span>实际查收数量：</span><span>' + order.realCount + order.moneyName + '</span></div>'
                }else{
                  trHtml = '<div class="check-link"><span>出货角色等级：</span><span>' + order.sellerRoleLevel + '</span></div>' +
                      '<div class="check-link"><span>预收货数量：</span><span>' + order.count + order.moneyName + '</span></div>' +
                      '<div class="check-link"><span>实际查收数量：</span><span>' + order.realCount + order.moneyName + '</span></div>'
                }
            } else {
                trHtml += '<div class="check-link"><span>订单数量 : </span><span>' + order.count + order.moneyName + '</span></div>'
            }
            if (order.deliveryType == 1) {
                trType = '<div class="check-link"><span>交易方式 : </span><span>' + order.tradeTypeName + '</span></div>'
            } else {
                trType = '<div class="check-link"><span>交易地点 ：</span><span>' + order.address + '</span></div>'
            }
            htmlStr += '<div class="check-link">' +
                '<span>订单编号 : </span><span>' + order.orderId + '</span></div><div class="check-link"><span>订单时间 : </span>' +
                '<span>' + new Date(order.createTime).Format("yyyy-MM-dd hh:mm:ss") + '</span></div><div class="check-link"><span>游戏区服 : </span><span>' + order.gameName + "/" + order.region + "/" + order.server + '</span>' +
                '</div><div class="check-link"><span>出货方 : </span><span>' + order.sellerAccount + '</span></div><div class="check-link"><span>出货角色 : </span>' +
                '<span>'+order.roleName+'</span>' + trHtml + '<div class="check-link">' +
                '<span>订单单价 : </span><span><em>' + order.price + '</em>元/' + order.moneyName + '</span></div><div class="check-link"><span>成交金额 : </span><span>' + order.realAmount + '</span>' +
                '</div>' + trType
            /* htmlStr+="<dt>订单编号：</dt><dd>"+order.orderId+"</dd>";
            htmlStr+="<dt>订单时间：</dt><dd>"+new Date(order.createTime).Format("yyyy-MM-dd hh:mm:ss")+"</dd>";
            htmlStr+="<dt>游戏区服：</dt><dd>"+order.gameName+"/"+order.region+"/"+order.server+"</dd>";
            htmlStr+="<dt>出货方：</dt><dd>"+order.sellerAccount+"</dd>";
            htmlStr+="<dt>出货角色：</dt><dd>"+order.roleName+"</dd>";
            if(order.status==4||order.status==5){
              htmlStr+="<dt>预收货数量：</dt><dd>"+order.count+order.moneyName+"</dd>";
              htmlStr+="<dt>实际查收数量：</dt><dd>"+order.realCount+order.moneyName+"</dd>";
            }else{
              htmlStr+="<dt>订单数量：</dt><dd>"+order.realCount+order.moneyName+"</dd>";
            }
            htmlStr+="<dt>单价：</dt><dd>"+order.price+"元/"+order.moneyName+"</dd>";
            htmlStr+="<dt>成交金额：</dt><dd>"+order.realAmount+"元</dd>";
            if(order.deliveryType==1){
               htmlStr+="<dt>交易方式：</dt><dd>邮寄交易</dd>";
           }else{
               htmlStr+="<dt>交易地点：</dt><dd>"+order.address+"</dd>";
           } */
            $("#divOrder").append(htmlStr);
            // htmlStr+="<dt>申请售后：</dt><dd>SH1709060000103   【甲甲甲】 <a href='after_sale.html?id="+order.orderId+"'><span class='after_sale'>申请售后</span></a></dd>";
            if (order.status == 4 || order.status == 5 || order.status == 6) {

                $.ajax({
                    type: "GET",
                    url: baseServiceUrl + "services/deliveryOrder/selectDeliverySubOrderData?t=" + new Date().getTime(),
                    data: { orderId: order.orderId, sellerInputCount: order.count },
                    contentType: "application/json; charset=UTF-8",
                    dataType: "json",
                    success: function (resp) {

                        var code = resp.responseStatus.code;
                        if (code == "00") {
                            console.log(resp);
                            $("#divOrder dl").html(htmlStr);

                            console.log(resp.blackUser)

                            if (resp.subOrderList != null && resp.subOrderList != undefined && resp.subOrderList != '' && resp.subOrderList.length > 0) {
                                me.subOrderList = resp.subOrderList
                                if (!resp.blackUser) {

                                    if(resp.deliveryOrder.tradeLogo==5){
                                      return
                                    }
                                    for (var i in resp.subOrderList) {
                                        console.log(resp.subOrderList[i].appealOrder)
                                        if (resp.subOrderList[i].appealOrderStatus == 4) {
                                            if (i == 0) {
                                                htmlStr='<div class="check-link"><span>申请售后：</span><span>'+resp.subOrderList[i].id+' 号机器人 【'+resp.subOrderList[i].gameRole+'】 <a style=\'cursor:not-allowed;\'><span class=\'after_saleno\'>超时禁止申诉</span></a></span></div>';
                                                //htmlStr += "<dt>申请售后：</dt><dd>" + resp.subOrderList[i].id + "号机器人【" + resp.subOrderList[i].gameRole + "】 <a style='cursor:not-allowed;'><span class='after_saleno'>超时禁止申诉</span></a></dd>";
                                            } else {
                                                htmlStr='<div class="check-link"><span></span><span>'+resp.subOrderList[i].id+' 号机器人 【'+resp.subOrderList[i].gameRole+'】 <a style=\'cursor:not-allowed;\'><span class=\'after_saleno\'>超时禁止申诉</span></a></span></div>'
                                                //htmlStr += "<dt></dt><dd>" + resp.subOrderList[i].id + "号机器人【" + resp.subOrderList[i].gameRole + "】 <a style='cursor:not-allowed;'><span class='after_saleno'>超时禁止申诉</span></a></dd>";
                                            }

                                        } else if (resp.subOrderList[i].appealOrderStatus == 5) {
                                            if (i == 0) {
                                                htmlStr='<div class="check-link"><span>申请售后：</span><span>'+resp.subOrderList[i].id+' 号机器人 【'+resp.subOrderList[i].gameRole+'】 <a href="after_sale.html?id=' + resp.subOrderList[i].id + '"><span class=\'after_sale\'>申请售后</span></a></span></div>';
                                                //htmlStr += "<dt>申请售后：</dt><dd>" + resp.subOrderList[i].id + "号机器人【" + resp.subOrderList[i].gameRole + "】 <a href='after_sale.html?id=" + resp.subOrderList[i].id + "'><span class='after_sale'>申请售后</span></a></dd>";
                                            } else {
                                                htmlStr='<div class="check-link"><span></span><span>'+resp.subOrderList[i].id+' 号机器人 【'+resp.subOrderList[i].gameRole+'】 <a href="after_sale.html?id=' + resp.subOrderList[i].id + '"><span class=\'after_sale\'>申请售后</span></a></span></div>';
                                                //htmlStr += "<dt></dt><dd>" + resp.subOrderList[i].id + "号机器人【" + resp.subOrderList[i].gameRole + "】 <a href='after_sale.html?id=" + resp.subOrderList[i].id + "'><span class='after_sale'>申请售后</span></a></dd>";
                                            }

                                        } else if (resp.subOrderList[i].appealOrderStatus == 1) {
                                            if (i == 0) {
                                                htmlStr='<div class="check-link"><span>申请售后：</span><span>'+resp.subOrderList[i].id+' 号机器人 【'+resp.subOrderList[i].gameRole+'】 <a href="single_result.html?orderId=' + resp.subOrderList[i].appealOrder + '"><span class=\'after_sale\'>售后处理完成</span></a></span></div>';
                                                //htmlStr += "<dt>申请售后：</dt><dd>" + resp.subOrderList[i].id + "号机器人【" + resp.subOrderList[i].gameRole + "】 <a href='single_result.html?orderId=" + resp.subOrderList[i].appealOrder + "'><span class='after_sale'>售后处理完成</span></a></dd>";
                                            } else {
                                                htmlStr='<div class="check-link"><span></span><span>'+resp.subOrderList[i].id+' 号机器人 【'+resp.subOrderList[i].gameRole+'】 <a href="single_result.html?orderId=' + resp.subOrderList[i].appealOrder + '"><span class=\'after_sale\'>售后处理完成</span></a></span></div>';
                                                //htmlStr += "<dt></dt><dd>" + resp.subOrderList[i].id + "号机器人【" + resp.subOrderList[i].gameRole + "】 <a href='single_result.html?orderId=" + resp.subOrderList[i].appealOrder + "'><span class='after_sale'>售后处理完成</span></a></dd>";
                                            }

                                        } else if (resp.subOrderList[i].appealOrderStatus == 3) {
                                            if (i == 0) {
                                                htmlStr='<div class="check-link"><span>申请售后：</span><span>'+resp.subOrderList[i].id+' 号机器人 【'+resp.subOrderList[i].gameRole+'】 <a href="single_result.html?orderId=' + resp.subOrderList[i].appealOrder + '"><span class=\'after_sale\'>售后处理中</span></a></span></div>';
                                                //htmlStr += "<dt>申请售后：</dt><dd>" + resp.subOrderList[i].id + "号机器人【" + resp.subOrderList[i].gameRole + "】 <a href='single_result.html?orderId=" + resp.subOrderList[i].appealOrder + "'><span class='after_sale'>售后处理中</span></a></dd>";
                                            } else {
                                                htmlStr='<div class="check-link"><span></span><span>'+resp.subOrderList[i].id+' 号机器人 【'+resp.subOrderList[i].gameRole+'】 <a href="single_result.html?orderId=' + resp.subOrderList[i].appealOrder + '"><span class=\'after_sale\'>售后处理中</span></a></span></div>';
                                                //htmlStr += "<dt>申请售后：</dt><dd>" + resp.subOrderList[i].id + "号机器人【" + resp.subOrderList[i].gameRole + "】 <a href='single_result.html?orderId=" + resp.subOrderList[i].appealOrder + "'><span class='after_sale'>售后处理中</span></a></dd>";
                                            }

                                        } else if (resp.subOrderList[i].appealOrderStatus == 2) {
                                            if (i == 0) {
                                                htmlStr='<div class="check-link"><span>申请售后：</span><span>'+resp.subOrderList[i].id+' 号机器人 【'+resp.subOrderList[i].gameRole+'】 <a style=\'cursor:not-allowed;\'><span class=\'after_saleno\'>售后已撤销</span></a></span></div>';
                                                //htmlStr += "<dt>申请售后：</dt><dd>" + resp.subOrderList[i].id + "号机器人【" + resp.subOrderList[i].gameRole + "】 <a style='cursor:not-allowed;'><span class='after_saleno'>售后已撤销</span></a></dd>";
                                            } else {
                                                htmlStr='<div class="check-link"><span></span><span>'+resp.subOrderList[i].id+' 号机器人 【'+resp.subOrderList[i].gameRole+'】 <a style=\'cursor:not-allowed;\'><span class=\'after_saleno\'>售后已撤销</span></a></span></div>'
                                                //htmlStr += "<dt>申请售后：</dt><dd>" + resp.subOrderList[i].id + "号机器人【" + resp.subOrderList[i].gameRole + "】 <a style='cursor:not-allowed;'><span class='after_saleno'>售后已撤销</span></a></dd>";
                                            }

                                        }


                                    }
                                }

                                $("#divOrder").append(htmlStr);
                                //成功/失败图标显示
                                if (order.status == 6) {
                                    $(".chddqx_top dl dt span").css("background-position", "-302px -81px");
                                } else {
                                    $(".chddqx_top dl dt span").css("background-position", "-342px -45px");
                                }
                            }

                        }
                    }
                });
            } else {
                $("#divOrder dl").html(htmlStr);
                //成功/失败图标显示
                if (order.status == 6) {
                    $(".chddqx_top dl dt span").css("background-position", "-302px -81px");
                } else {
                    $(".chddqx_top dl dt span").css("background-position", "-342px -45px");
                }
            }


        }




    },
    getLog: function () {
        if (me.orderId == null || me.orderId == "") {
            return;
        }
        //日志
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
                if (code == "00") {
                    var orderLogList = resp.orderLogList;
                    var htmlStr = "";
                    for (var i = 0; i < orderLogList.length; i++) {
                        var order = orderLogList[i];
                        console.log("数据库查询的数据", order);
                        if (order.type == '1') {
                            htmlStr += '<div class="chat-line clearfix robot-chat"><div class="avatar avatar1 fl"></div><div class="chat-content fl">' +
                                '<i class="icon-arrow"></i><p><span class="time">' + new Date(order.createTime).Format("yyyy-MM-dd hh:mm:ss") + '</span>&nbsp' + orderLogList[i].log + '</p>' +
                                '</div></div>'
                            //htmlStr += '<div class="hxMsgL hx_sys">' + '<p>' + new Date(order.createTime).Format("yyyy-MM-dd hh:mm:ss") + "系统消息:" + orderLogList[i].log + '</p>' + '</div>';
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
    "Waiting": 1, // 等待交易
    "Queue": 2, // 排队中
    "Dealing": 3,//交易中
    "Complete": 4,//交易完成
    "PartComplete": 5,//部分完单
    "Cancle": 6,//撤单
    "HumanOp": 7//需人工介入
}
