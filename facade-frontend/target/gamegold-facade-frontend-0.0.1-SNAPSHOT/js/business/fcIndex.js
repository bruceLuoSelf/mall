var fcStatus = [
    { name: '分仓中', val: 1 }, { name: '成功', val: 2 }, { name: '失败', val: 3 }, { name: '部分分仓', val: 4 }
], params = {};
function setParams(state) {
    params.createStartTime = $("#startTime").val() ? $("#startTime").val() : ($("#startTime").attr('realvalue') ? $("#startTime").attr('realvalue') : ''),
        params.createEndTime = $("#endTime").val() ? $("#endTime").val() : ($("#endTime").attr('realvalue') ? $("#endTime").attr('realvalue') : ''),
        params.splitRepositoryOrderNo = $('#fcOrder').val(),//分仓订单号
        params.shOrderId = $('#relationOr').val(),//收货订单号
        params.gameAccount = $('#fcAccount').val(),//分仓游戏帐号
        params.gameRole = $('#deliveryAccount').val(),//收货角色
        params.splitRepositoryRole = $('#fcRole').val(),//分仓角色
        params.status = $('#fcSel option:selected').attr('data-status') ? $('#fcSel option:selected').attr('data-status') : ''
    if (state) return params
}
function yesterday() {
    var yesterday = new Date((new Date().getTime()) - 24 * 60 * 60 * 1000);
    var h = yesterday.getFullYear();
    var m = yesterday.getMonth() + 1;
    var d = yesterday.getDate();
    var hh = yesterday.getHours();
    var mm = yesterday.getMinutes();
    var ss = yesterday.getSeconds();
    m = m < 10 ? "0" + m : m;
    d = d < 10 ? "0" + d : d;
    hh = hh < 10 ? "0" + hh : hh;
    mm = mm < 10 ? "0" + mm : mm;
    ss = ss < 10 ? "0" + ss : ss;
    return h + "-" + m + "-" + d + " " + hh + ":" + mm + ":" + ss;
}
function getDateDiff(startDate, endDate) {
    var startTime = new Date(startDate);
    var endTime = new Date(endDate);
    var days = (startTime - endTime) / (1000 * 60 * 60 * 24);
    return days;
};
function goDetail(orderNo) {
    var params = {};
    params = setParams(1)
    params.orderNo = orderNo;
    location.href = href = '/fcOrderDetail.html?' + parseParam(params);
}
function setVal(id,val) { 
    $('#'+id).val(getUrlParam(val))
 }
$(function () {
    var html = '';
    for (var i = 0; i < fcStatus.length; i++) {
        html += '<option style="width: 70px;margin-right: 0" data-status=' + fcStatus[i].val + '>' + fcStatus[i].name + '</option>';
    }
    $('#fcSel').append(html);
    if (getUrlParam('createStartTime')) {
        $('#startTime').val(getUrlParam('createStartTime'))
    } else {
        $('#startTime').val(yesterday());
    }
    if (getUrlParam('createEndTime')) {
        $('#endTime').val(getUrlParam('createEndTime'))
    } else {
        $('#endTime').val(timestampToTime(new Date(), true));
    }
    if(getUrlParam('status')){
        var str=getStatus(Number(getUrlParam('status')));
        $('#fcSel').val(str)
    }
    if(getUrlParam('splitRepositoryOrderNo')){
        setVal('fcOrder','splitRepositoryOrderNo')
    }
    if(getUrlParam('shOrderId')){
        setVal('relationOr','shOrderId')
    }
    if(getUrlParam('gameAccount')){
        setVal('fcAccount','gameAccount')
    }
    if(getUrlParam('gameRole')){
        setVal('deliveryAccount','gameRole')
    }
    if(getUrlParam('splitRepositoryRole')){
        setVal('fcRole','splitRepositoryRole')
    }
    me.init();
    $('#downloadexcel').click(function () {
        var startTime = new Date($("#startTime").val());
        var endTime = new Date($("#endTime").val());
        if (getDateDiff(endTime, startTime) > 7) {
            alert("订单时间查询间隔不能超过7天");
            return;
        }
        setParams();
        me.exportData()
    });
    //点击查询
    $('#btnSearch').click(function () {
        var startTime = new Date($("#startTime").val());
        var endTime = new Date($("#endTime").val());
        if (getDateDiff(endTime, startTime) > 7) {
            alert("订单时间查询间隔不能超过7天");
            return;
        }
        me.init();
    })
})
var me = {
    pageSize: 10,
    fcOrderList: null,
    init: function () {
        $.jqPaginator('#pagination1', {
            totalPages: 10,
            visiblePages: me.pageSize,
            currentPage: 1,
            onPageChange: function (num, type) {
                me.pageselectCallback(num);
            }
        });
    },
    pageselectCallback: function (page_index) {
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
        setParams();
        params.gameName = gameName || '地下城与勇士';
        params.regionName = region;
        params.serverName = server;
        params.pageSize = me.pageSize;
        params.page = page_index;
        $.ajax({
            type: "POST",
            url: baseServiceUrl + "services/splitRepository/queryOrderList?t=" + new Date().getTime(),
            data: JSON.stringify(params),
            contentType: "application/json; charset=UTF-8",
            dataType: "json",
            success: function (resp) {
                var code = resp.responseStatus.code, totalPage;
                if (code == "00") {
                    me.fcOrderList = resp.data ? resp.data : [];
                    totalPage = parseInt(resp.totalCount / me.pageSize) + (resp.totalCount % me.pageSize != 0 ? 1 : 0);
                    if (me.fcOrderList != null && me.fcOrderList.length > 0) {

                        $(".totalPage").html("共" + resp.totalCount + "笔，共" + totalPage + "页");
                    }
                    else {
                        $(".totalPage").html("共0笔，共1页");
                    }
                    me.appendData();
                } else {
                    // if (code == "10007" || code == "10008" || code == "10010" || code == "B1022") {
                    //     window.location.href = "applyseller.html";
                    // }
                    // else if (code == "10012") {
                    //     window.location.href = "rechargeList.html";
                    // }
                    $(".totalPage").html("共0笔，共1页");
                }

                //数据为0时，默认设置插件页码为1
                var count = 1;
                if (totalPage != 0) {
                    count = totalPage;
                }
                $('#pagination1').jqPaginator('option', {
                    totalPages: count
                });
            }
        });
    },
    appendData: function () {
        var fcOrderList = me.fcOrderList;
        var html = '';
        if (fcOrderList.length) {
            for (var i = 0; i < fcOrderList.length; i++) {
                html += '<tr><td width="150"><p>' + timestampToTime(fcOrderList[i].createTime).ymd + '</p><p>' + timestampToTime(fcOrderList[i].createTime).hms + '</p></td>' +
                    '<td class="spc" width="200"><p>' + fcOrderList[i].orderId + '</p><p class="color-999">' + fcOrderList[i].gameName + '/' + fcOrderList[i].region + '/' + fcOrderList[i].server + '</p></td>' +
                    '<td width="100"><p>' + getStatus(fcOrderList[i].status) + '</p></td><td width="120"><p>' + fcOrderList[i].realCount + '</p></td>' +
                    '<td width="150"><p>' + fcOrderList[i].shOrderId + '</p></td><td width="120"><a class="fc-check" onclick="goDetail(' + JSON.stringify(fcOrderList[i].orderId).replace(/"/g, '&quot;') + ')">查看</a></td>' +
                    '</tr>';
            }
            $('.table_fc tbody').html(html);
        } else {
            $('.table_fc tbody').html('');
        }
    },
    //导出数据
    exportData: function () {
        url = baseServiceUrl + "services/splitRepository/export?t=" + new Date().getTime();
        var obj = {}
        params.gameName = $.trim($("#game").text());
        params.regionName = $.trim($("#region").text());
        params.serverName = $.trim($("#server").text());
        if (params.gameName == "请选择游戏" || params.gameName == "全部游戏") {
            params.gameName = "";
        }
        if (params.regionName == "请选择区" || params.regionName == "全部区") {
            params.regionName = "";
        }
        if (params.serverName == "请选择服" || params.serverName == "全部服") {
            params.serverName = "";
        }
        var paramsData = parseParam(params);
        //console.log(params);
        // if ($.trim(gameName).length != 0) {
        //     params += "&gameName=" + gameName;
        // }
        // if ($.trim(region).length != 0) {
        //     params += "&region=" + region;
        // }
        // if ($.trim(server).length != 0) {
        //     params += "&server=" + server;
        // }
        // if ($.trim(gameAccount).length != 0) {
        //     params += "&gameAccount=" + gameAccount;
        // }
        if (paramsData.length > 0) {
            url += '&' + paramsData;
        }
        window.open(url);
    },
}
function getStatus(status) {
    switch (status) {
        case 1:
            return '分仓中';
            break;
        case 2:
            return '成功';
            break;
        case 3:
            return '失败';
            break;
        case 4:
            return '部分分仓';
            break;
    }
}
function timestampToTime(timestamp, full) {
    if (!timestamp)
        return '----'
    var date = timestamp.length == 10 ? new Date(timestamp * 1000) : new Date(timestamp);//时间戳为10位需*1000，时间戳为13位的话不需乘1000
    Y = date.getFullYear() + '-';
    M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
    D = addZero(date.getDate()) + ' ';
    h = addZero(date.getHours()) + ':';
    m = addZero(date.getMinutes()) + ':';
    s = addZero(date.getSeconds());
    //return Y+M+D+h+m+s;
    if (full) {
        return Y + M + D + h + m + s;
    } else {
        return { ymd: Y + M + D, hms: h + m + s }
    }
}
function parseParam(param, key) {
    var paramStr = "";
    if (param instanceof String || param instanceof Number || param instanceof Boolean) {
        paramStr += "&" + key + "=" + encodeURI(param)//encodeURIComponent(param);
    } else {
        $.each(param, function (i) {
            var k = key == null ? i : key + (param instanceof Array ? "[" + i + "]" : "." + i);
            paramStr += '&' + parseParam(this, k);
        });
    }
    return paramStr.substr(1);
}
function addZero(param) {
    if (param < 10) {
        return '0' + param
    } else {
        return param
    }
}