$(function () {
    me.beforeInit();
    me.bindCancelOrder();
    me.init(true);
    me.getLog();

    $('.hx_send').click(function () {
        var msg_text = $('#msg_text').val();
        if (msg_text == "") {
            alert("请输入聊天内容！");
            return;
        } else if (msg_text.length > 50) {
            alert("聊天信息字数不能超过50个");
            return;
        }
        sendPrivateText(msg_text);
    })
    document.onkeydown = function (e) {
        var ev = document.all ? window.event : e;
        if (ev.keyCode == 13) {
            var msg_text = $('#msg_text').val();
            if (msg_text != "")
                sendPrivateText(msg_text);
        }
    }
});

var me = {
    orderId: $.trim(getUrlParam("orderId")),
    id: null,
    deliverOrderStatus: null,
    msg: '',
    beforeInit: function () {
        //绑定撤单信息
        for (var i in ChCancleReason) {
            $("#selCancleReason").append("<option value='" + ChCancleReason[i].code + "'>" + ChCancleReason[i].value + "</option>");
        }
        $("#otherCancleReason").attr("disabled", "disabled");

        $("#selCancleReason").change(function () {
            $("#otherCancleReason").val("");
            if ($(this).val() == 408) {
                $("#otherCancleReason").removeAttr("disabled");
            }
            else {
                $("#otherCancleReason").attr("disabled", "disabled");
            }
        });
    },
    init: function (isFirstReques) {
        $.ajax({
            type: "GET",
            url: baseServiceUrl + "services/deliveryOrder/selectDeliverySubOrderData?t=" + new Date().getTime(),
            data: {orderId: me.orderId},
            contentType: "application/json; charset=UTF-8",
            dataType: "json",
            success: function (resp) {
                var code = resp.responseStatus.code;
                if (code == "00") {
                    deliveryOrder = resp.deliveryOrder;

                    me.deliverOrderStatus = resp.deliveryOrder.status;
                    subOrderList = resp.subOrderList;
                    me.id = deliveryOrder.id;
                    me.initPage(isFirstReques);

                }
            }
        });
    },

    initPage: function (isFirstReques) {
        var params = {};
        //初始化页面
        if (deliveryOrder != null) {
            $('#game_info').text(deliveryOrder.gameName + "/" + deliveryOrder.region + "/" + deliveryOrder.server);
            $('#roleName-text').text(deliveryOrder.roleName);
            $('#price-text').text(deliveryOrder.price + "元/" + deliveryOrder.moneyName);
            $('#amount-text').text(deliveryOrder.amount);
            $('#tradeTypeName-text').text(deliveryOrder.tradeTypeName);

            $('#address-text').text(deliveryOrder.address);
            if (deliveryOrder.status == shStatus.Queue) {
                $('#orderStatusStr-text').text("等待收货商分配角色");
            } else {
                $('#orderStatusStr-text').text(deliveryOrder.orderStatusStr);
            }
            $('#goodsTypeName-text').text(deliveryOrder.goodsTypeName);
            $('#orderId-text').text(me.orderId);
            $("#count-text").text(deliveryOrder.count + deliveryOrder.moneyName);
            $("#realCount-text").text(deliveryOrder.realCount + deliveryOrder.moneyName);
            $("#qq-link").attr("href", "tencent://message/?uin=" + deliveryOrder.serviceQq + "&Site=5173&Menu=yes");
            $("#qq-img").attr("src", "http://html.5173.com/dlc2c/images/global_v2/seller/qqinline.gif?p=2:" + deliveryOrder.serviceQq + ":1");

            $('.mytr').remove();
            if (subOrderList != null && subOrderList != undefined && subOrderList != '' && subOrderList.length > 0) {
                for (var i in subOrderList) {
                    subOrder = subOrderList[i];

                    if(subOrder==null||subOrder.gameRole==null){
                        break;
                    }

                    var htmlStr = "<tr class='rc-table-tr mytr'>";
                    htmlStr += "<td width='100'>" + subOrder.gameRole + "</td>";
                    htmlStr += "<td width='100'>" + subOrder.count + "</td>";
                    htmlStr += "<td width='100'>" + subOrder.realCount + "</td>";
                    htmlStr += "<td width='80'>" + subOrder.status + "</td>";
                    htmlStr += "<td width='200'>";
                    if (subOrder.subOrderStatus == shStatus.WaitDelivery) {
                        htmlStr += "<button onclick='eidGoods(" + subOrder.id + ")' class='rc-cancel' >取消订单</button>";
                        htmlStr += "<button onclick='fahuo(" + subOrder.id + "," + subOrder.count + "," + subOrder.realCount + ")' class='rc-success'  >我已发货</button>";
                    }
                    htmlStr += "</td>";
                    htmlStr += "</tr>";
                    $('#rc-table').append(htmlStr);
                }
            }
        }
        if (deliveryOrder.status == shStatus.Waiting) {
            $("#btnLoginGame").removeClass("lo_ylogin").addClass("lo_sh");
            $("#btnLoginGame").val("我已登录");
            $('#btnLoginGame').unbind().bind('click', function () {
                me.loginGame();
            });
        } else {
            $("#btnLoginGame").val("已登录游戏");
            $("#btnLoginGame").removeClass("lo_sh").addClass("lo_ylogin").unbind("click");
            if (isFirstReques) {
                me.loopGetData();
            }
        }

        if (deliveryOrder.status == shStatus.Waiting || deliveryOrder.status == shStatus.Queue) {
            $("#btnCancel").show();
        } else {
            $("#btnCancel").hide();
        }

        if (deliveryOrder.status == shStatus.Complete || deliveryOrder.status == shStatus.PartComplete || deliveryOrder.status == shStatus.Cancle || deliveryOrder.status == shStatus.HumanOp) {
            //跳转
            window.open(baseHtmlUrl + "orderResult.html?orderId=" + deliveryOrder.orderId + "&t=" + (new Date()).getTime(), "_self");
        }
    },
    //登录游戏
    loginGame: function () {
        var request = {};
        request.id = me.id;
        $.ajax({
            type: "GET",
            url: baseServiceUrl + "services/manualShOrder/startTradingForManual",
            data: request,
            contentType: "application/json; charset=UTF-8",
            dataType: "json",
            success: function (resp) {
                var code = resp.responseStatus.code;
                if (code == "00") {
                    $("#btnLoginGame").val("已登录游戏");
                    $("#btnLoginGame").removeClass("lo_sh").addClass("lo_ylogin").unbind("click");
                    me.loopGetData();
                }
            }
        });
    },

    loopGetData: function () {
        setInterval("me.init(false)", 10000);
        setInterval("me.getLog()",5000);
    },

    getLog: function () {
        if (me.orderId == null || me.orderId == "") {
            return;
        }
        $.ajax({
            type: "GET",
            url: baseServiceUrl + "services/orderLog/selectChattingRecords",
            data: {orderId: me.orderId},
            contentType: "application/json; charset=UTF-8",
            dataType: "jsonp",
            success: function (resp) {
                var code = resp.responseStatus.code;
                if (code == "00") {
                    var list = resp.orderLogs;
                    if (list == null && list.length <= 0) {
                        return;
                    }

                    var result = "";
                    for (var i in list) {
                        var orderLog = list[i];
                        //根据类型加载聊天记录
                        var tp = "hx_my";
                        if (1 == orderLog.userType) {
                            tp = "hx_sh";
                        } else if (3 == orderLog.userType) {
                            tp = 'hx_sys';
                        } else if (orderLog.userType == null) {
                            return;
                        }
                        console.log('数据库获取', orderLog);

                        var msg = orderLog.log;
                        var time = formatDateTime(new Date(orderLog.createTime));
                        var cont = myMsgCont(msg, tp, time);
                        result = result + cont;
                    }

                    $('.hxMsgC').html(result);
                    toobottom();
                }
            }
        });
    },
    //主订单撤单
    bindCancelOrder: function () {
        //是否弹出撤单页面
        $("#btnCancel").click(function () {
            if (me.deliverOrderStatus != null && me.deliverOrderStatus != "") {
                if (me.deliverOrderStatus == shStatus.Waiting || me.deliverOrderStatus == shStatus.Queue) {
                    $('#btnCancel').unbind("click");
                    $('#btnCancel').leanModal({closeButton: ".modal_close"});
                    $('#btnCancel').click();
                } else {
                    alert("当前状态无法撤单！");
                    $('#btnCancel').unbind("click");
                }
            } else {
                alert("当前状态无法撤单！");
                $('#btnCancel').unbind("click");
            }
        });


        //撤单
        $("#btnSaveCancelOrder").click(function () {
            var cancleReason = $("#selCancleReason").val();
            var otherReason = $.trim($("#otherCancleReason").val());
            if (cancleReason == "请选择撤单原因") {
                alert("请选择撤单原因");
                return;
            }
            if (cancleReason == 408) {
                if (otherReason == "") {
                    alert("请填写其他原因");
                    return;
                }
            }
            var request = {};
            request.id = me.id;
            request.reason = $("#selCancleReason").val();
            request.remark = $("#otherCancleReason").val();
            $.ajax({
                type: 'GET',
                url: baseServiceUrl + 'services/chorder/cancelOrder',
                data: request,
                contentType: "application/json; charset=UTF-8",
                dataType: 'json',
                success: function (resp) {
                    var code = resp.responseStatus.code;
                    if (code == '00') {
                        window.open(baseHtmlUrl + "orderResult.html?orderId=" + deliveryOrder.orderId + "&t=" + (new Date()).getTime(), "_self");
                    }
                    // else {
                    //     alert(resp.responseStatus.message);
                    // }
                }
            });
        });
    },

}

//取消子订单
function eidGoods(id) {
    if (confirm("确认取消订单") == true) {
        $.ajax({
            type: 'GET',
            url: baseServiceUrl + 'services/manualShOrder/cancelSubOrderBySeller',
            data: {"subOrderId": id},
            contentType: "application/json; charset=UTF-8",
            dataType: 'json',
            success: function (resp) {
                var code = resp.responseStatus.code;
                if (code == '00') {
                    window.open(baseHtmlUrl + "orderResult.html?orderId=" + deliveryOrder.orderId + "&t=" + (new Date()).getTime(), "_self");
                }
            }
        });
    } else {
        return;
    }
}

//确定发货
function fahuo(id, count, realCount) {
    $('#poprm').css('display', 'block');
    var remainderCount = count - realCount;
    $('#realCount-input').val(remainderCount);
    $('#confirmDelivery-btn').unbind().bind('click', function () {
        var realCount = $('#realCount-input').val();
        if (!checkNum(realCount, remainderCount)) {
            return;
        }
        var params = {};
        params.subOrderId = id;
        params.realCount = realCount;
        $.ajax({
            type: 'GET',
            url: baseServiceUrl + 'services/manualShOrder/confirmDelivery',
            data: params,
            contentType: "application/json; charset=UTF-8",
            dataType: 'json',
            success: function (resp) {
                var code = resp.responseStatus.code;
                if (code == '00') {
                    alert("成功发货");
                    //刷新页面
                    $('#poprm').css('display', 'none');
                    me.init(false);
                }
            }
        });
    });
    $("#cancelDelivery-btn,#poprmContClose").unbind().bind('click', function () {
        $('#poprm').css('display', 'none');
        $('#poprmContNun').val('');
    })
}

function validate(a) {
    var reg = new RegExp("^[0-9]*$");
    if (!/^[0-9]*$/.test(a)) {
        alert("请输入数字!");
        return false
    }
    return true
}

//校验输入的数量
function checkNum(realCount, remainderCount) {
    if (realCount == null || realCount == 0) {
        alert("输入数量不能为空或者为0");
        $('#realCount-input').val('');
        return false;
    } else if (isNaN(realCount)) {
        alert("请输入正确的数字格式");
        $('#realCount-input').val('');
        return false;
    } else if (realCount > remainderCount) {
        alert("输入的数量过大，请重新输入");
        $('#realCount-input').val('');
        return false;
    }
    return true;
}
//交易类型
var shStatus = {
    "Waiting": 1, // 等待交易
    "Queue": 2, // 待分配角色
    "Dealing": 3,//交易中
    "Complete": 4,//交易完成
    "PartComplete": 5,//部分完单
    "Cancle": 6,//撤单
    "HumanOp": 7,//需人工介入
    "ApplyCompletePart": 8,//申请部分完单
    "WaitDelivery": 9,//待发货
    "DeliveryFinish": 10//已发货
}

function formatDateTime(date) {
    var y = date.getFullYear();
    var m = date.getMonth() + 1;
    m = m < 10 ? ('0' + m) : m;
    var d = date.getDate();
    d = d < 10 ? ('0' + d) : d;
    var h = date.getHours();
    var minute = date.getMinutes();
    var s = date.getSeconds();
    s = s < 10 ? ('0' + s) : s;
    minute = minute < 10 ? ('0' + minute) : minute;
    return y + '-' + m + '-' + d + ' ' + h + ':' + minute + ':' + s;
};

var myMsgCont = function (msg, tp, time) { //显示自己发送的消息
    var tips = '收货商：'
    if (tp == 'hx_my') {
        tips = '我：'
    } else if (tp == 'hx_sys') {
        tips = '系统消息：'
    }
    var cont = '<div class="hxMsgL ' + tp + '">' + time + ' <strong>' + tips + '</strong>' + msg + '</div>'
    return cont;
}

function toobottom() {
    $('.hxMsgC').scrollTop($('.hxMsgC')[0].scrollHeight);
}

var sendPrivateText = function (msg_text) {  //文字消息
    var msg_text1 = msg_text.replace(/<br \/>/g, " ");
    console.log("msg_text1--" + msg_text1);
    $('#msg_text').val('');

    //保存聊天消息
    $.ajax({
        type: "GET",
        url: baseServiceUrl + "services/orderLog/saveChattingRecords",
        data: {orderId: me.orderId, chattingRecords: msg_text1},
        contentType: "application/json; charset=UTF-8",
        dataType: "jsonp",
        success: function (resp) {
            var code = resp.responseStatus.code;
            if (code == "00") {
                me.getLog();
                console.log("保存成功");
            }
        }
    });
};