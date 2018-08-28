/**
 * 商城订单页
 */
var queryRequest = {
    ordersState: null,// 商品状态
    pageSize: 8,// 当前页大小
    start: 0,
    goodsList: null,// 商品列表
    orderCreateTime_s: null,
    orderCreateTime_e: null,
    orderId: null,
    orderGameOperator: null,
    orderGameName: null,
    orderRegion: null,
    orderServer: null,
    orderGoodsType: null,

    cleanData: function () {
        queryRequest.goodsList = null;
    }
};

var GameAreaList = null;

$(document).ready(function () {
    $("#buy_yxb").attr('class', 'nonce');

    var buyerOrder = getQueryOrdersRequest();
    queryOrder(buyerOrder);
    /**
     * 点击查询事件
     */
    $("#doBtnSearch").click(function () {
        var orderCreateTime_s = $("#d4311").val();
        var orderCreateTime_e = $("#d4312").val();
        var searchOrderId = $("#searchOrderId").val();
        var orderGameNametext = $("#sygs_name option:selected").text();
        var orderGameNameval = $("#sygs_name option:selected").val()
        var orderGameArea = $("#sygs_area").val();
        var orderGameServer = $("#sygs_server").val();

        queryRequest.orderCreateTime_s = orderCreateTime_s;
        queryRequest.orderCreateTime_e = orderCreateTime_e;
        queryRequest.orderId = searchOrderId;
        if (orderGameNameval != '') {
            queryRequest.orderGameName = orderGameNametext;
        } else {
            queryRequest.orderGameName = '';
        }
        queryRequest.orderRegion = orderGameArea;
        queryRequest.orderServer = orderGameServer;

        var buyerOrder = getQueryOrdersRequest();
        queryOrder(buyerOrder);
        return false;
    });
    //游戏运营商的下拉框
    var selectOperator = $("#sygs_operator");
    //游戏名称下拉框
    var selectGamename = $("#sygs_name");
    //游戏区下拉框
    var selectArea = $("#sygs_area");
    //游戏服下拉框
    var selectServer = $("#sygs_server");
    //商品类型下拉框
    var selectGoodsType = $("#sygs_goodstype");

    $.ajax({
        type: "POST",
        url: baseServiceUrl + "services/gameinfo/getallcompanies",
        contentType: "application/json; charset=UTF-8",
        dataType: "json",
        beforeSend: function (request) {
            request.setRequestHeader("5173_authkey", getAuthkey());
        },
        success: function (resp) {
            var responseStatus = resp.responseStatus;
            var code = responseStatus.code;
            var gameOperatorsList = resp.companielist;
            if (code == "00") {
                if (isNull(gameOperatorsList)) {
                    return;
                }
                //将悬浮框中数据填充
                selectOperator.html("");
                $("<option value=''>请选择游戏厂商</option>").appendTo(selectOperator);
                for (var i = 0; i < gameOperatorsList.length; i++) {
                    if (gameOperatorsList[i] == -1) {
                        continue;
                    }
                    $("<option value='" + gameOperatorsList[i] + "'>" + gameOperatorsList[i] + "</option>").appendTo(selectOperator);
                }
            }
        },
        error: function (resp) {
            console.log(resp);
        }
    });


    selectOperator.change(function () {
        //1.需要获得当前下拉框的值
        var selectOperatorValue = $(this).val();
        selectGamename.html("");
        selectArea.html("");
        $("<option value=''>请选择游戏</option>").appendTo(selectGamename);
        $("<option value=''>请选择区</option>").appendTo(selectArea);
        selectServer.html("");
        $("<option value=''>请选择服</option>").appendTo(selectServer);
        //新增商品类型查询条件
        selectGoodsType.html("");
        $("<option value=''>请选择商品类型</option>").appendTo(selectGoodsType);
        if (selectOperatorValue != "") {
            if (!selectOperator.data(selectOperatorValue)) {
                var queryDiscountRequest = {};
                queryDiscountRequest.companie = selectOperatorValue;
                $.ajax({
                    type: "POST",
                    url: baseServiceUrl + "services/gameinfo/getgamebycompany",
                    contentType: "application/json; charset=UTF-8",
                    dataType: "json",
                    data: $.toJSON(queryDiscountRequest),
                    success: function (resp) {
                        var responseStatus = resp.responseStatus;
                        var code = responseStatus.code;
                        var gameNamesList = resp.gameList;
                        if (code == "00") {
                            if (isNull(gameNamesList)) {
                                return;
                            }
                            //将悬浮框中数据填充
                            for (var i = 0; i < gameNamesList.length; i++) {
                                $("<option value='" + gameNamesList[i].id + "'>" + gameNamesList[i].name + "</option>").appendTo(selectGamename);
                            }

                            selectOperator.data(selectOperatorValue, gameNamesList);
                        }
                    },
                    error: function (resp) {
                        console.log(resp);
                    }
                });
            } else {
                var gameNamesList = selectOperator.data(selectOperatorValue);
                if (gameNamesList != 0) {
                    for (var i = 0; i < gameNamesList.length; i++) {
                        $("<option value='" + gameNamesList[i].id + "'>" + gameNamesList[i].name + "</option>").appendTo(selectGamename);
                    }
                }
            }
        }
        return false;
    });
    //游戏名下拉框值改变
    selectGamename.change(function () {
        //1.需要获得当前下拉框的值
        var selectGamenameValue = $(this).val();
        var gameName = $(this).find('option:selected').text();//ZW_C_JB_00008  根据游戏名获取商品类目 ADD
        selectArea.html("");
        selectServer.html("");
        //新增商品类型查询条件
        selectGoodsType.html("");
        $("<option value=''>请选择服</option>").appendTo(selectServer);
        $("<option value=''>请选择商品类型</option>").appendTo(selectGoodsType);
        $("<option value=''>请选择区</option>").appendTo(selectArea);
        if (selectGamenameValue != "") {
            var queryDiscountRequest = {};
            queryDiscountRequest.gameid = selectGamenameValue;
            if (!selectGamename.data(selectGamenameValue)) {
                $.ajax({
                    type: "POST",
                    url: baseServiceUrl + "services/gameinfo/getdetailgame",
                    contentType: "application/json; charset=UTF-8",
                    dataType: "json",
                    data: $.toJSON(queryDiscountRequest),
                    success: function (resp) {
                        var responseStatus = resp.responseStatus;
                        var code = responseStatus.code;
                        GameAreaList = resp.gameDetailInfo.gameAreaList;
                        if (code == "00") {
                            //将悬浮框中数据填充
                            if (GameAreaList != null) {
                                for (var i = 0; i < GameAreaList.length; i++) {
                                    $("<option value='" + GameAreaList[i].name + "'>" + GameAreaList[i].name + "</option>").appendTo(selectArea);
                                }
                            }
                            selectGamename.data(selectGamenameValue, GameAreaList);
                        }
                    },
                    error: function (resp) {
                        console.log(resp);
                    }
                });
            } else {
                var gameAreaList = selectGamename.data(selectGamenameValue);
                if (gameAreaList != 0) {
                    for (var i = 0; i < gameAreaList.length; i++) {
                        $("<option value='" + gameAreaList[i].name + "'>" + gameAreaList[i].name + "</option>").appendTo(selectArea);
                    }
                }
            }
        }
        //根据游戏获取商品类型
        getCategoryByGameName(gameName);
    });

    /**
     *通过游戏名称，和是否启用商城来获取商品类目
     * ZW_C_JB_00008_20170513 增加通货类型 update by hyl
     * @param gameName
     */
    function getCategoryByGameName(gameName) {
        var gameCategoryKey = gameName + ":category";
        selectGoodsType.html("");
        $("<option value=''>请选择商品类型</option>").appendTo(selectGoodsType);

        if (!selectGamename.data(gameCategoryKey)) {
            $.ajax({
                type: "GET",
                url: baseServiceUrl + "services/shGameConfig/getAllConfigByGameName?t=" + new Date().getTime(),
                contentType: "application/json; charset=UTF-8",
                dataType: "json",
                data: {
                    "gameName": gameName
                },
                success: function (resp) {
                    var responseStatus = resp.responseStatus;
                    var code = responseStatus.code;
                    GameConfigList = resp.shGameConfigList;
                    if (code == "00") {
                        //将悬浮框中数据填充
                        if (GameConfigList != null) {
                            for (var i = 0; i < GameConfigList.length; i++) {
                                $("<option value='" + GameConfigList[i].goodsTypeName + "'>" + GameConfigList[i].goodsTypeName + "</option>").appendTo(selectGoodsType);
                            }
                        }
                        selectGamename.data(gameCategoryKey, GameConfigList);
                    }
                },
                error: function (resp) {
                    console.log(resp);
                }
            });
        } else {
            var GameConfigList = selectGamename.data(gameCategoryKey);
            if (GameConfigList != null) {
                for (var i = 0; i < GameConfigList.length; i++) {
                    $("<option value='" + GameConfigList[i].goodsTypeName + "'>" + GameConfigList[i].goodsTypeName + "</option>").appendTo(selectGoodsType);
                }
            }
        }
    }

    //游戏区发生变化
    selectArea.change(function () {
        //1.需要获得当前下拉框的值
        var selectAreaValue = $(this).val();
        selectServer.html("");
        selectServer.html("");
        $("<option value=''>请选择服</option>").appendTo(selectServer);
        if (selectAreaValue != "") {
            if (!selectArea.data(selectAreaValue)) {
                for (var i = 0; i < GameAreaList.length; i++) {
                    if (GameAreaList[i].name == selectAreaValue) {
                        var serverList = GameAreaList[i].gameServerList;
                        if (serverList != null) {
                            for (var j = 0; j < serverList.length; j++) {
                                $("<option value='" + serverList[j].name + "'>" + serverList[j].name + "</option>").appendTo(selectServer);
                            }
                        }
                        selectArea.data(selectAreaValue, serverList);
                    }
                }
            } else {
                var serverList = selectArea.data(selectAreaValue);
                if (serverList != 0) {
                    for (var i = 0; i < serverList.length; i++) {
                        $("<option value='" + serverList[i].name + "'>" + serverList[i].name + "</option>").appendTo(selectServer);
                    }
                }
            }

        }
    })
});

function queryOrder(queryRequest) {

    //增加商品类目查询条件,当传入为条件为空时,查询全部商品类型的数据
    var orderGoodsType = $("#sygs_goodstype").val();
    if (orderGoodsType == "" || orderGoodsType == null) {
        orderGoodsType = "全部";
    }
    queryRequest.goodsTypeName = orderGoodsType;
    queryRequest.orderBy = "CREATE_TIME";
    queryRequest.isAsc = false;

    $.ajax({
        type: "POST",
        url: baseServiceUrl + "services/order/queryorder?t=" + new Date().getTime(),
        data: $.toJSON(queryRequest),
        contentType: "application/json; charset=UTF-8",
        dataType: "json",
        beforeSend: function (request) {
            request.setRequestHeader("5173_authkey", getAuthkey());
        },
        success: function (resp) {
            var responseStatus = resp.responseStatus;
            var code = responseStatus.code;
            if (code == "00") {
                // 组装订单信息 buildPageHtml(totalCount,totalPage,currentPage)
                var orders = resp.orders;
                if (isNull(orders)) {
                    return;
                }
                buildOrderHtml(orders.data);
                buildPageHtml(orders.totalCount, orders.totalPageCount, orders.currentPageNo, orders.pageSize);
            }
        },
        error: function (resp) {
            console.log(resp);
        }
    });
}

function buildOrderHtml(orderList) {
    // 清除原有数据
    $(".L-content .listmain").remove();
    if (isNull(orderList)) {
        return;
    }
    for (var i = 0; i < orderList.length; i++) {
        var orderInfo = orderList[i];
        var servicerInfo = orderInfo.servicerInfo;
        var orderId = orderInfo.orderId;
        var createDate = new Date(orderInfo.createTime);
        var createTime = createDate.getFullYear() + "年" + (createDate.getMonth() + 1) + "月" + createDate.getDate() + "日" + createDate.getHours() + ":" + createDate.getMinutes();
        var gameInfo = orderInfo.gameName + "/" + orderInfo.region + "/" + orderInfo.server;
        var totalPrice = orderInfo.totalPrice;
        var servicerName = isNull(servicerInfo) || isNull(servicerInfo.nickName) ? "公主" : servicerInfo.nickName;
        var qq = isNull(servicerInfo) || isNull(servicerInfo.qq) ? "" : servicerInfo.qq;
        var orderState = OrderState.getText(orderInfo.orderState);
        var price = toDecimal2(1 / parseFloat(orderInfo.unitPrice)); // 1元对应多少金
        var myGameRace = isNull(orderInfo.gameRace) ? "" : orderInfo.gameRace;
        var goodsTypeName = orderInfo.goodsTypeName;//增加商品类目信息
        var html = "<div class='listmain'  id='listmain_5' status='151'>";
        html += "<p class='banner'>";
        html += "<span class='tb1'>[" + goodsTypeName + "]</span><span class='tb2'>订单编号：" + orderId + "</span><span class='tb3'>创建时间：" + createTime + "</span>";
        html += "</p>";
        html += "<ul class='u1'>";
        console.log(orderInfo.moneyName)
        if (null == orderInfo.moneyName || typeof (orderInfo.moneyName) == "undefined" || "" == orderInfo.moneyName) {
            console.log(orderInfo.moneyName)
            html += "<li class='ico_shang'><a href='" + baseHtmlUrl + "orderstate.html?orderId=" + orderInfo.orderId + "' target='_blank'>" + orderInfo.deliveryTime +
                "分钟发货 1元=" + toDecimal2(price) + orderInfo.moneyName + orderInfo.goodsTypeName + " " + orderInfo.goldCount + orderInfo.moneyName + orderInfo.goodsTypeName + "</a></li>";
        } else {
            html += "<li class='ico_shang'><a href='" + baseHtmlUrl + "orderstate.html?orderId=" + orderInfo.orderId + "' target='_blank'>" + orderInfo.deliveryTime +
                "分钟发货 1元=" + toDecimal2(price) + orderInfo.moneyName + " " + orderInfo.goldCount + orderInfo.moneyName + "</a></li>";
        }
        html += "<li>游戏:" + gameInfo + "/" + myGameRace + "</li>";
        html += "</ul>";
        html += "<ul class='u2'>";
        html += "<li class='orange'>￥" + toDecimal2(totalPrice) + "</li>";
        html += "</ul>";
        html += "<ul class='u3'>";
        html += "<li>" + orderInfo.goldCount + "</li>";
        html += "</ul>";
        html += "<ul class='u5'>";
        html += "<li><span class='p_color3'>" + servicerName + "</span></li>";
        html += "<li><a href='javascript:void(0)' onclick=\"serviceHx(this, '" + baseServiceUrl + "services/orderdata/getOrderIm?orderid=" + orderId + "&usertype=1')\"  class='xn_kfzx_btn'></a></li>";
        //html += "<li><a href='javascript:void(0)' onclick=getServiceHxAccount('"+orderId+"',1) class='xn_kfzx_btn'></a></li>";
        html += "</ul>";
        html += "<ul class='u5'>";
        html += "<li class='orange'>" + orderState + "</li>";
        if (orderInfo.orderState == OrderState.WaitPayment) {
            // 待付款 baseServiceUrl + "payment?orderId="+orderInfo.orderId;
            html += "<li><a href='" + baseServiceUrl + "payment?orderId=" + orderInfo.orderId + "' class='btn_L paypal_btn' target='_blank'>" +
                "<span>立即付款</span></a><div class='fed-toolstip paypal—layer' style='display:none'>";
            html += "<div class='fed-tipsarr fed-arrtop'><span class='arr-3'>◆</span><span class='arr-1'>◆</span><span class='arr-2'>◆</span></div>";
            html += "<div class='fed-tipcon' style='display:none'>";
            html += "<p>Paypal支付需经审核，未完成支付前请点击继续提交信息。</p>";
            html += "</div></div></li>";
        } else if (orderInfo.orderState == OrderState.Paid) {
            // 已付款
//			html += "<li><a href='http://dkjy.5173.com/bizoffer/cccard/orderviewfacadev2.aspx?orderid=DK20140222813078402' class='btn_L paypal_btn' target='_blank'>" +
//			"<span>已付款</span></a><div class='fed-toolstip paypal—layer' style='display:none'>";
//			html += "<div class='fed-tipsarr fed-arrtop'><span class='arr-3'>◆</span><span class='arr-1'>◆</span><span class='arr-2'>◆</span></div>";
//			html += "<div class='fed-tipcon' style='display:none'>";
//			html += "<p>Paypal支付需经审核，未完成支付前请点击继续提交信息。</p>";
//			html += "</div></div></li>";
        } else if (orderInfo.orderState == OrderState.WaitDelivery) {
            // 待发货
//			html += "<li><a href='http://dkjy.5173.com/bizoffer/cccard/orderviewfacadev2.aspx?orderid=DK20140222813078402' class='btn_L paypal_btn' target='_blank'>" +
//			"<span>待发货</span></a><div class='fed-toolstip paypal—layer' style='display:none'>";
//			html += "<div class='fed-tipsarr fed-arrtop'><span class='arr-3'>◆</span><span class='arr-1'>◆</span><span class='arr-2'>◆</span></div>";
//			html += "<div class='fed-tipcon' style='display:none'>";
//			html += "<p>Paypal支付需经审核，未完成支付前请点击继续提交信息。</p>";
//			html += "</div></div></li>";
        } else if (orderInfo.orderState == OrderState.Delivery) {
            // 已发货
            html += "<li><a href=\"javascript:receiveGold('" + orderInfo.orderId + "','" + orderInfo.gameName + "')\" class='btn_L paypal_btn'>" +
                "<span>确认收货</span></a><div class='fed-toolstip paypal—layer' style='display:none'>";
            html += "<div class='fed-tipsarr fed-arrtop'><span class='arr-3'>◆</span><span class='arr-1'>◆</span><span class='arr-2'>◆</span></div>";
            html += "<div class='fed-tipcon' style='display:none'>";
            html += "<p>Paypal支付需经审核，未完成支付前请点击继续提交信息。</p>";
            html += "</div></div></li>";
        } else if (orderInfo.orderState == OrderState.Statement) {
            // 结单
//			html += "<li><a href='http://dkjy.5173.com/bizoffer/cccard/orderviewfacadev2.aspx?orderid=DK20140222813078402' class='btn_L paypal_btn' target='_blank'>" +
//			"<span>结单</span></a><div class='fed-toolstip paypal—layer' style='display:none'>";
//			html += "<div class='fed-tipsarr fed-arrtop'><span class='arr-3'>◆</span><span class='arr-1'>◆</span><span class='arr-2'>◆</span></div>";
//			html += "<div class='fed-tipcon' style='display:none'>";
//			html += "<p>Paypal支付需经审核，未完成支付前请点击继续提交信息。</p>";
//			html += "</div></div></li>";
            html += "<li><a href='http://baoxian.5173.com/MyInfo/Step1.aspx?op=1&id=" + orderInfo.orderId + "' target='_blank'>售后保障</a></li>";
        } else if (orderInfo.orderState == OrderState.Refund) {
            // 已退款
//			html += "<li><a href='http://dkjy.5173.com/bizoffer/cccard/orderviewfacadev2.aspx?orderid=DK20140222813078402' class='btn_L paypal_btn' target='_blank'>" +
//			"<span>已退款</span></a><div class='fed-toolstip paypal—layer' style='display:none'>";
//			html += "<div class='fed-tipsarr fed-arrtop'><span class='arr-3'>◆</span><span class='arr-1'>◆</span><span class='arr-2'>◆</span></div>";
//			html += "<div class='fed-tipcon' style='display:none'>";
//			html += "<p>Paypal支付需经审核，未完成支付前请点击继续提交信息。</p>";
//			html += "</div></div></li>";
        } else if (orderInfo.orderState == OrderState.Cancelled) {
            // 已取消
//			html += "<li><a href='http://dkjy.5173.com/bizoffer/cccard/orderviewfacadev2.aspx?orderid=DK20140222813078402' class='btn_L paypal_btn' target='_blank'>" +
//			"<span>已取消</span></a><div class='fed-toolstip paypal—layer' style='display:none'>";
//			html += "<div class='fed-tipsarr fed-arrtop'><span class='arr-3'>◆</span><span class='arr-1'>◆</span><span class='arr-2'>◆</span></div>";
//			html += "<div class='fed-tipcon' style='display:none'>";
//			html += "<p>Paypal支付需经审核，未完成支付前请点击继续提交信息。</p>";
//			html += "</div></div></li>";
        }

        html += "<li><a style='' href='" + baseHtmlUrl + "orderstate.html?orderId=" + orderInfo.orderId + "' target='_blank'>";
        html += "详情</a>&nbsp;</li>";
        //alert(orderInfo.isEvaluate+"---"+orderInfo.isReEvaluate);
        if (orderInfo.orderState == OrderState.Delivery || orderInfo.orderState == OrderState.Statement || orderInfo.orderState == OrderState.Refund) {
            if (orderInfo.isEvaluate != true) {
                html += "<li><a style='' href='" + baseHtmlUrl + "comment.html?orderId=" + orderInfo.orderId + "&isAdd=1' target='_blank'>";
                html += "评价</a>&nbsp;</li>";
            }
            else if (orderInfo.isReEvaluate != true) {
                html += "<li><a style='' href='" + baseHtmlUrl + "comment.html?orderId=" + orderInfo.orderId + "&isAdd=2' target='_blank'>";
                html += "追加评价</a>&nbsp;</li>";
            }
        }
        html += "</ul>";
        html += "</div>";

        $("[class='pagebox beta']").before(html);
    }

    // 客服qq，添加点击事件
    $(".a_qqtalk").click(function () {
        var qqNumber = $(this).attr("qq");
        if (isNull(qqNumber)) {
            return;
        }
        window.open("http://wpa.qq.com/msgrd?v=3&uin=" + qqNumber + "&site=qq&menu=yes");
    });
}

function buildPageHtml(totalCount, totalPage, currentPage, pageSize) {
    var html = "<ul>";
    html += "<li class='yeshu'>共 " + totalCount + " 单，共 " + totalPage + " 页</li>";
    html += "<li class='up_off'><a onclick='lastPage(" + totalCount + "," + totalPage + "," +
        currentPage + "," + pageSize + ")'>上一页</a></li>";
    html += "<li class='nb_on'><a id='pageBottom_current'>" + currentPage + "</a></li>";
    html += "<li class='down_off'><a onclick='nextPage(" + totalCount + "," + totalPage + "," +
        currentPage + "," + pageSize + ")'>下一页</a></li>";
    html += "<li class='yeshu'>到第<input type='text' value='" + currentPage + "' id='pagerBottom_input' class='w30' >页</li>";
    html += "<li class='find'><a ><span onclick='goToPage(" + totalCount + "," + totalPage + "," +
        currentPage + "," + pageSize + ")'>查看</span></a></li>";
    html += "</ul>";

    $("#pagerBottom").html(html);
}

// 下一页
function nextPage(totalCount, totalPage, currentPage, pageSize) {
    if (currentPage >= totalPage) {
        return;
    }
    $("#pageBottom_current").html(currentPage + 1);
    var queryRequest = getQueryOrdersRequest();//修改传递参数
    queryRequest.pageSize = pageSize;
    queryRequest.start = currentPage * pageSize;

    queryOrder(queryRequest);
}

//得到查询条件
function getQueryOrdersRequest() {
    var buyerOrder = {};
    buyerOrder.pageSize = queryRequest.pageSize;
    buyerOrder.start = queryRequest.start;
    buyerOrder.orderBy = "CREATE_TIME";
    buyerOrder.orderState = queryRequest.ordersState;
    buyerOrder.startOrderCreate = queryRequest.orderCreateTime_s;
    buyerOrder.endOrderCreate = queryRequest.orderCreateTime_e;
    buyerOrder.orderId = queryRequest.orderId;
    buyerOrder.gameName = queryRequest.orderGameName;
    buyerOrder.region = queryRequest.orderRegion;
    buyerOrder.server = queryRequest.orderServer;
    buyerOrder.goodsTypeName = queryRequest.orderGoodsType;
    return buyerOrder;
}

// 上一页
function lastPage(totalCount, totalPage, currentPage, pageSize) {
    if (currentPage <= 1) {
        return;
    }
    $("#pageBottom_current").html(currentPage - 1);
    var queryRequest = getQueryOrdersRequest();
    queryRequest.pageSize = pageSize;
    queryRequest.start = (currentPage - 2) * pageSize;

    queryOrder(queryRequest);
}

//切换查询条件（根据商品状态查询）
function getStatus(res) {
    var selectGoodsState = null;
    $(".option a").removeClass("on").addClass("off");
    // 全部
    if (res == "all") {
        $("#all").removeClass("off").addClass("on");
        selectGoodsState = null;
    }
//	// 等待付款
//	if (res == "check") {
//		$("#check").removeClass("off").addClass("on");
//		selectGoodsState = OrderState.WaitDelivery;
//	}
    // 等待发货
    if (res == "wait") {
        $("#wait").removeClass("off").addClass("on");
        selectGoodsState = OrderState.WaitDelivery;
    }
    // 等待确认收货
    if (res == "con") {
        $("#con").removeClass("off").addClass("on");
        selectGoodsState = OrderState.Delivery;
    }
    //交易成功
    if (res == "suc") {
        $("#suc").removeClass("off").addClass("on");
        selectGoodsState = OrderState.Statement;
    }
    // 交易取消
    if (res == "cal") {
        $("#cal").removeClass("off").addClass("on");
        selectGoodsState = OrderState.Refund;
    }

    queryRequest.ordersState = selectGoodsState;
    var queryGoodsRequest = getQueryOrdersRequest();
    queryOrder(queryGoodsRequest);
}

// 到第几页
function goToPage(totalCount, totalPage, currentPage, pageSize) {
    currentPage = $("#pagerBottom_input").val();
    $("#pageBottom_current").html(currentPage);
    var queryRequest = getQueryOrdersRequest();
    queryRequest.pageSize = pageSize;
    queryRequest.start = (currentPage - 1) * pageSize;

    queryOrder(queryRequest);
}

//我已收货
function receiveGold(orderId, gameName) {
    var request = {};
    var rexStr = new RegExp("魔兽世界" + ".*?");
    if (rexStr.test(gameName)) {
        alert('该订单已经设置转账延时，24小时后自动转账给卖家！');
        return;
    }
    request.orderId = orderId;
    request.orderState = OrderState.Statement;
    if (!rexStr.test(gameName)) {
        updateOrder(request);
    }
}

// 修改订单
function updateOrder(request) {
    $.ajax({
        type: "POST",
        url: baseServiceUrl + "services/order/modifyorder",
        data: $.toJSON(request),
        contentType: "application/json; charset=UTF-8",
        dataType: "json",
        beforeSend: function (request) {
            request.setRequestHeader("5173_authkey", getAuthkey());
        },
        success: function (resp) {
            var responseStatus = resp.responseStatus;
            var code = responseStatus.code;
            if (code == "00") {
                // 刷新页面
                window.location.reload();
            }
        }
    });
}
