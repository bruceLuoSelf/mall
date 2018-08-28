$(function () {
    me.bindPayStatus();
    me.init();

    $('#btnAddRecharge').leanModal({closeButton: ".modal_close"});

    //点击查询
    $("#btnSearch").click(function () {
        me.init();
    });

    //退款申请
    $("#btnSave").click(function () {
        if ($("#pid").val() == "") {
            alert("当前单号为空，程序出错！");
            return;
        }
        if($("#reason").val().length>100){
            alert("字符长度不能超过100");
            return;
        }
        if ($("#reason").val() == "") {
            alert("请填写退款原因！");
            return;
        }
        if (confirm("是否确定申请退款?")) {
            me.refund();
        }
    });

    //清空
    $('#btnAddRecharge').click(function () {
        $("#txtAmount").val("");
    });

    //新增支付单
    $("#btnRecharge").click(function () {
        var amount = $.trim($("#txtAmount").val());
        if (amount == "") {
            alert("金额不能为空！");
            return;
        }
        if (isNaN(amount)) {
            alert("金额必须为数字！");
            return;
        }
        if (amount <= 0) {
            alert("支付不能小于等于0元！");
            return;
        }
        if (amount > 99999999) {
            alert("单次支付金额过大，请分次操作！");
            return;
        }
        if (confirm("是否确定支付?")) {
            me.recharge();
        }
    });

    //限制输入
    $("#txtAmount").keyup(function () {
        var regex = /^\d+\.?\d{0,2}$/;
        if (!regex.test(this.value)) {
            $("#txtAmount").val($("#txtAmount").val().substring(0, $("#txtAmount").val().length - 1));
        }
    });
});

var me = {
    pageSize: 10,
    payOrderList: null,
    purchaserData: null,
    init: function () {
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
    //分页数据
    pageselectCallback: function (page_index) {
        var status = $("#selStatus").val();
        var request = {};
        request.startTime = $("#startTime").val();
        request.endTime = $("#endTime").val();
        if (status != -1) {
            request.status = $("#selStatus").val();
        }
        request.pageSize = me.pageSize;
        request.page = page_index;
        $.ajax({
            type: "GET",
            url: baseServiceUrl + "services/payOrder/queryPayOrderList?t="+new Date().getTime(),
            data: request,
            contentType: "application/json; charset=UTF-8",
            dataType: "json",
            success: function (resp) {
                var code = resp.responseStatus.code;
                if (code == "00") {
                    me.payOrderList = resp.payOrderList;
                    me.purchaserData = resp.purchaserData;
                    if (me.payOrderList != null && me.payOrderList.length > 0) {
                        $(".totalPage").html("共" + resp.totalCount + "笔，共" + resp.totalPage + "页");
                    }
                    else {
                        $(".totalPage").html("共0笔，共1页");
                    }
                } else {
                    if (code == "10007" || code == "10008" || code == "10010" || code == "B1022") {
                        window.location.href = "applyseller.html";
                    }
                    $(".totalPage").html("共0笔，共1页");
                }

                // 初始化页面
                me.initPage();
                me.initPurchaseData();

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
    //初始化页面
    initPage: function () {
        var htmlStr = "";
        if (me.payOrderList != null && me.payOrderList.length > 0) {
            for (var i in me.payOrderList) {
                var payOrder = me.payOrderList[i];
                htmlStr += "<tr>";
                htmlStr += "<td width=\"150\" class=\"tc\">" + payOrder.orderId + "</td>";
                htmlStr += "<td width=\"100\">" + payOrder.usedAmount.toFixed(2) + "元</td>";
                htmlStr += "<td width=\"100\">" + payOrder.balance.toFixed(2) + "元</td>";
                htmlStr += "<td width=\"100\">" + payOrder.amount.toFixed(2) + "元</td>";

                htmlStr += "<td width=\"100\" class=\"tc\">" + ChPayStatus.getText(payOrder.status) + "</td>";
                if (ChPayStatus.WaitPay == payOrder.status) {
                    htmlStr += "<td width=\"150\" class=\"tc\"><input type=\"button\" class=\"btn_orange_s\" value=\"支付\" style='cursor:pointer;' onclick=\"me.pay('" + payOrder.orderId + "')\"/></td>";
                }
                else if (ChPayStatus.Paid == payOrder.status) {
                    if (payOrder.balance > 0) {
                        htmlStr += "<td width=\"150\" class=\"tc\"><input type=\"button\" class=\"btn_orange_refuse\" pId=\"" + payOrder.orderId + "\" value=\"申请退款\" style='cursor:pointer;' href=\"#OpenWindow\" rel=\"leanModal\"/></td>";
                    }
                    else {
                        htmlStr += "<td width=\"150\" class=\"tc\"></td>";
                    }
                }
                else if (ChPayStatus.ApplyRefund == payOrder.status) {
                    htmlStr += "<td width=\"150\" class=\"tc\"></td>";
                }
                else if (ChPayStatus.Refund == payOrder.status) {
                    htmlStr += "<td width=\"150\" class=\"tc\"></td>";
                }

                htmlStr += "</tr>";
            }
        }
        $("#tbList tbody").html(htmlStr);

        $('.btn_orange_refuse').leanModal({closeButton: ".modal_close"});
        $('.btn_orange_refuse').click(function () {
            $("#reason").val("");
            $("#pid").val($(this).attr("pid"));
        })
    },
    initPurchaseData: function () {
        var purchaseData = me.purchaserData;
        if (purchaseData != null) {
            $(".totalAmount").html(purchaseData.totalAmount.toFixed(2) + "元");
            $(".freezeAmount").html(purchaseData.freezeAmount.toFixed(2) + "元");
            $(".availableAmount").html(purchaseData.availableAmount.toFixed(2) + "元");
        }
    },
    bindPayStatus: function () {
        var htmlStr = "<option value=\"-1\">请选择</option>";
        for (var p in ChPayStatusSearch) {
            htmlStr += "<option value=\"" + p + "\">" + ChPayStatusSearch[p].value + "</option>";
        }
        $("#selStatus").html(htmlStr);
    },
    pay: function (orderId) {
        window.open(baseServiceUrl + "shPayment?orderId=" + orderId);
    },
    refund: function () {
        var request = {};
        request.reason = $("#reason").val();
        request.payOrderId = $("#pid").val();
        $.ajax({
            type: "GET",
            url: baseServiceUrl + "services/refundOrder/addRefundOrder",
            data: request,
            contentType: "application/json; charset=UTF-8",
            dataType: "json",
            success: function (resp) {
                var code = resp.responseStatus.code;
                if (code == "00") {
                    $("#OpenWindow").hide();
                    $("#lean_overlay").hide();
                    alert("退款申请成功，请耐心等待管理员审核！");
                    me.init();
                } else {
                    if (code == "10007" || code == "10008" || code == "10010" || code == "B1022") {
                        window.location.href = "applyseller.html";
                    }
                }
            }
        });
    },
    recharge: function () {
        var amount = $.trim($("#txtAmount").val());
        var request = {};
        request.amount = amount;
        $.ajax({
            type: "GET",
            url: baseServiceUrl + "services/payOrder/addPayOrder",
            data: request,
            contentType: "application/json; charset=UTF-8",
            dataType: "json",
            success: function (resp) {
                var code = resp.responseStatus.code;
                if (code == "00") {
                    $("#OpenWindowRecharge").hide();
                    $("#lean_overlay").hide();
                    window.location.href = baseServiceUrl + "shPayment?orderId=" + resp.orderId;
                    //me.init();
                } else {
                    if (code == "10007" || code == "10008" || code == "10010" || code == "B1022") {
                        window.location.href = "applyseller.html";
                    }
                }
            }
        });
    }
}

var ChPayStatusSearch = [
    {code: 0, value: '待支付'},
    {code: 1, value: '已支付'},
    {code: 2, value: '申请退款中'},
    {code: 3, value: '已退款'}
]
