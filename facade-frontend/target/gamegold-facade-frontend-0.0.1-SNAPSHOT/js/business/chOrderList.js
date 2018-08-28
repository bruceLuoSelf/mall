$(function () {
    $("#startTime").val(new Date().Format("yyyy-MM-dd"));
    $("#endTime").val(new Date().Format("yyyy-MM-dd"));
    me.init();

    //查询
    $("#btnSearch").click(function () {
        var startTime = new Date($("#startTime").val());
        var endTime = new Date($("#endTime").val());
        if (getDateDiff(endTime, startTime) > 30) {
            alert("订单时间查询间隔不能超过30天");
            return;
        }
        me.init();
    });

    //切换状态值查找
    $("#selectStatus").find("a").click(function () {
        var startTime = new Date($("#startTime").val());
        var endTime = new Date($("#endTime").val());
        if (getDateDiff(endTime, startTime) > 30) {
            alert("订单时间查询间隔不能超过30天");
            return;
        }

        $("#selectStatus").find("a").attr("class", "off");
        $(this).attr("class", "on");
        me.status = $(this).attr("status");
        me.init();
    });
});

function getDateDiff(startDate, endDate) {
    var startTime = new Date(startDate);
    var endTime = new Date(endDate);
    var days = (startTime - endTime) / (1000 * 60 * 60 * 24);
    return days;
};

var me = {
    pageSize: 10,
    deliveryOrderList: null,
    status: "",
    init: function () {
        me.static();
        //分页
        $.jqPaginator('#pagination1', {
            totalPages: 10,
            visiblePages: me.pageSize,
            currentPage: 1,
            onPageChange: function (num, type) {
                me.pageselectCallback(num);
            }
        });
    },
    getRequestHead: function () {
        var gameName = $.trim($("#game").text());
        var region = $.trim($("#region").text());
        var server = $.trim($("#server").text());
        if (gameName == "请选择游戏" || gameName == "全部游戏" || gameName == "请选择") {
            gameName = "";
        }
        if (region == "请选择区" || region == "全部区" || region == "请选择") {
            region = "";
        }
        if (server == "请选择服" || server == "全部服" || server == "请选择") {
            server = "";
        }

        var me = this;
        var request = {};
        request.gameName = gameName;
        request.region = region;
        request.server = server;
        request.gameRace = "";
        request.orderId = $.trim($("#txtOrderId").val());
        request.startTime = $("#startTime").val();
        request.endTime = $("#endTime").val();
        request.status = me.status;
        request.fieldName = "create_time";
        request.isAsc = false;
        request.isSell = true;
        return request;
    },
    //统计数量和金额
    static: function () {
        var request = me.getRequestHead();

        $("#spanAmount").html(0);
        $("#spanCount").html(0);
        $.ajax({
            type: "GET",
            url: baseServiceUrl + "services/chorder/statisAmountAndCount?t="+new Date().getTime(),
            data: request,
            contentType: "application/json; charset=UTF-8",
            dataType: "json",
            success: function (resp) {
                var code = resp.responseStatus.code;
                if (code == "00") {
                    if (resp.deliveryOrder != null && resp.deliveryOrder != "") {
                        $("#spanAmount").html(resp.deliveryOrder.realAmount.toFixed(2));
                        $("#spanCount").html(resp.deliveryOrder.realCount);
                    }
                }
            }
        });
    },
    //分页数据
    pageselectCallback: function (page_index) {
        var request = me.getRequestHead();
        request.pageSize = me.pageSize;
        request.page = page_index;
        $.ajax({
            type: "GET",
            url: baseServiceUrl + "services/chorder/selectDeliverOrderInPage?t="+new Date().getTime(),
            data: request,
            contentType: "application/json; charset=UTF-8",
            dataType: "json",
            success: function (resp) {
                var code = resp.responseStatus.code;
                if (code == "00") {
                    me.deliveryOrderList = resp.deliveryOrderList;
                    if (me.deliveryOrderList != null && me.deliveryOrderList.length > 0) {
                        $(".totalPage").html("共" + resp.totalCount + "笔，共" + resp.totalPage + "页");
                    }
                    else {
                        $(".totalPage").html("共0笔，共1页");
                    }
                    // 初始化页面
                    me.initPage();
                } else {
                    $(".totalPage").html("共0笔，共1页");
                }

                //数据为0时，默认设置插件页码为1
                var count = 1;
                if (resp.totalPage != 0) {
                    count = resp.totalPage;
                }
                $('#pagination1').jqPaginator('option', {
                    totalPages: count
                });
            }
        });
        return false;
    },
    initPage: function () {
        console.log(me.deliveryOrderList)
        var htmlStr = "<div class=\"ch_title\"><span class=\"th1\">订单信息</span><span class=\"th2\">单价</span><span class=\"th3\">数量</span><span class=\"th4\">金额</span><span class=\"th5\">交易状态</span></div>";
        for (var i in me.deliveryOrderList) {
            var deliveryOrder = me.deliveryOrderList[i];
            if(deliveryOrder.deliveryType == 1){
              if(deliveryOrder.tradeLogo==5){
                  deliveryOrder.tradeTypeName="拍卖交易";
              }else{
                  deliveryOrder.tradeTypeName="邮寄交易";
              }
            }
            htmlStr += "<dl><dt>";
            if(deliveryOrder.appealOrder){
              htmlStr += "<span class=\"t1\">订单号：【申诉】" + deliveryOrder.orderId + "</span>";
            }else{
              htmlStr += "<span class=\"t1\">订单号：" + deliveryOrder.orderId + "</span>";
            }
            htmlStr += "<span class=\"t2\">成交时间：" + new Date((deliveryOrder.createTime)).Format("yyyy-MM-dd hh:mm:ss") + "</span>";
            htmlStr += "<span class=\"t3\">交易方式：" + deliveryOrder.tradeTypeName + "</span>";
            //htmlStr+="<span class=\"t2\">成交时间：2016-12-06</span>";
            //htmlStr+="<span class=\"t3\"><a href=\"\" target=\"_blank\">查看订单详情</a></span>";

            if(deliveryOrder.appealOrder){
              htmlStr += "<span class=\"t4\" ><a style='color:blue' type=\"button\" class='showLog' href='succ_single.html?orderId=" + deliveryOrder.orderId + "' target='_blank'>查看订单详情</a></span>";
            }else if (deliveryOrder.deliveryType == 2) {
                htmlStr += "<span class=\"t4\" ><a style='color:blue' type=\"button\" class='showLog' href='deliverySubOrderList.html?orderId=" + deliveryOrder.orderId + "' target='_blank'>查看订单详情</a></span>";
            } else {
                htmlStr += "<span class=\"t4\" ><a style='color:blue' type=\"button\" class='showLog' href='myShOrder.html?orderId=" + deliveryOrder.orderId + "' target='_blank'>查看订单详情</a></span>";
            }

            htmlStr += "</dt>";
            htmlStr += "<dd>";
            htmlStr += "<span class=\"b1\">" + deliveryOrder.count + deliveryOrder.moneyName + '=' + deliveryOrder.amount.toFixed(2) + "元</span>";
            htmlStr += "<span class=\"b2\">" + deliveryOrder.gameName + "/" + deliveryOrder.region + "/" + deliveryOrder.server + "</span>";
            htmlStr += "<span class=\"b3\">" + deliveryOrder.price + "</span>";
            htmlStr += "<span class=\"b4\">" + deliveryOrder.realCount + "/" + deliveryOrder.count + deliveryOrder.moneyName + "</span>";
            htmlStr += "<span class=\"b5\">" + deliveryOrder.realAmount + "/" + deliveryOrder.amount.toFixed(2) + "元</span>";

            for(var p in ChOrderState){
                if(deliveryOrder.status==ChOrderState[p].code){
                    htmlStr+="<span class=\"b6\">"+ChOrderState[p].value+"</span>";
                    break;
                }
            }
            //htmlStr+="<span class=\"b6\">已完成</span>";
            htmlStr += "</dd></dl>";
        }

        $("#ch_order").html(htmlStr);

        // $('.showLog').leanModal({closeButton: ".modal_close" });
        //
        // $('.showLog').click(function(){
        //     me.showDetail($(this).attr("id"));
        // });
    },
    showDetail: function (id) {
        var htmlStr = "";
        for (var i in me.deliveryOrderList) {
            var order = me.deliveryOrderList[i];
            if (order.id == id) {
                htmlStr += "<tr><td><span>采购单编号：</span>" + order.cgId + "</td><td><span>订单编号：</span>" + order.orderId + "</td></tr>";

                var statusStr = "";
                for (var p in ChOrderState) {
                    if (order.status == ChOrderState[p].code) {
                        statusStr = ChOrderState[p].value;
                        break;
                    }
                }
                htmlStr += "<tr><td><span>采购单价：</span>" + order.price + "元/万金 </td><td><span>订单状态：</span>" + statusStr + "</td></tr>";
                htmlStr += "<tr><td><span>订单数量：</span>" + order.count + "万金</td><td><span>订单总价：</span>" + order.realAmount + "元</td></tr>";
                htmlStr += "<tr><td><span>实际数量：</span>" + order.realCount + "万金</td><td><span>创建时间：</span>" + new Date(order.createTime).Format("yyyy-MM-dd hh:mm:ss") + "</td></tr>";

                htmlStr += "<tr><td><span>开始交易时间：</span>";
                if (order.tradeStartTime != null && order.tradeStartTime != "") {
                    htmlStr += new Date(order.tradeStartTime).Format("yyyy-MM-dd hh:mm:ss");
                }
                htmlStr += "</td><td><span>交易完成时间：</span>";
                if (order.tradeEndTime != null && order.tradeEndTime != "") {
                    htmlStr += new Date(order.tradeEndTime).Format("yyyy-MM-dd hh:mm:ss");
                }
                htmlStr += "</td></tr>";

                var reasonStr = "";
                for (var p in ChCancleReason) {
                    if (order.reason == ChCancleReason[p].code) {
                        reasonStr = ChCancleReason[p].value;
                        if (order.reason == 408) {
                            reasonStr = order.otherReason;
                        }
                        break;
                    }
                }

                htmlStr += "<tr><td colspan='2' style='word-break:break-all;'><span>结单理由：</span>" + reasonStr + "</td></tr>";

                me.showSubOrder(id, order.gameName, order.region, order.server);
                break;
            }
        }
        $("#tbOrder").html(htmlStr);
    },
    showSubOrder: function (id, gameName, region, server) {
        var request = {};
        request.chId = id;
        $.ajax({
            type: "GET",
            url: baseServiceUrl + "services/chorder/getDeliverySubOrderList?t="+new Date().getTime(),
            data: request,
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
                            for (var p in ChOrderState) {
                                if (subOrder.status == ChOrderState[p].code) {
                                    statusStr = ChOrderState[p].value;
                                    break;
                                }
                            }

                            htmlStr += "<tr>";
                            htmlStr += "<td width='180px'><span>游戏帐号：</span>" + subOrder.gameAccount + "</td>";
                            htmlStr += "<td width='140px'><span>游戏角色：</span>" + subOrder.gameRole + "</td>";
                            htmlStr += "<td width='165px'><span>计划采购数量：</span>" + subOrder.count + "万金</td>";
                            htmlStr += "<td width='165px'><span>实际采购数量：</span>" + subOrder.realCount + "万金</td>";
                            htmlStr += "<td width='70px'>" + statusStr + "</td>";
                            htmlStr += "<td width='70px' class=\"t_center\"><a href=\"" + baseHtmlUrl + "repositoryGameAccount.html?gameName=" + escape(gameName) + "&region=" + escape(region) + "&server=" + escape(server) + "&gameRace=&gameAccount=" + escape(subOrder.gameAccount) + "&roleName=" + escape(subOrder.gameRole) + "\" target=\"_blank\">查看库存</a></td>";
                            htmlStr += "</tr>";
                        }
                    }
                    $("#tbSubOrder").html(htmlStr);
                    $("#OpenWindowLog").css("left", "40%");
                }
            }
        });
    }
}
