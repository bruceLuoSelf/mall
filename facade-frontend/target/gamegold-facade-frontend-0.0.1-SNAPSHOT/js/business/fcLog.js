var params = {
    gameName: getUrlParam('gameName'),
    region: getUrlParam('gameRegion'),
    server: getUrlParam('gameServer'),
    gameAccount: getUrlParam('gameAccount'),
    roleName: getUrlParam('roleName'),
    incomeType: '',
    logType: '',
    startTime: yesterday(),
    endTime: timestampToTime(new Date(),true)
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
function getDateDiff(startDate,endDate){
    var startTime = new Date(startDate);
    var endTime = new Date(endDate);
    var days = (startTime - endTime)/(1000*60*60*24);
    return  days;
};
$(function () {
    $('#gameInfo').text(getUrlParam('gameName') + '/' + getUrlParam('gameRegion') + '/' + getUrlParam('gameServer'));
    $('#gameAccount').text(getUrlParam('gameAccount'));
    $('#gameRoleName').text(getUrlParam('roleName'));
    var incomeList = [{ name: "收入", val: 1 }, { name: "支出", val: 2 }],
        fcTypeList = [{ name: "分仓所得", val: 1 }, { name: "收货所得", val: 2 }, { name: "分仓支出", val: 3 }, { name: "销售支出", val: 4 }],
        incomeListHtml = '', fcTypeHtml = '';
    incomeList.forEach(function (item) {
        incomeListHtml += '<option value=' + item.val + ' style="width: 90px;margin-right: 0">' + item.name + '</option>'
    });
    $('#income').append(incomeListHtml);
    fcTypeList.forEach(function (item) {
        fcTypeHtml += '<option value=' + item.val + ' style="width: 90px;margin-right: 0">' + item.name + '</option>'
    });
    $('#fcType').append(fcTypeHtml);
    $('#startTime').val(yesterday());
    $('#endTime').val(timestampToTime(new Date(),true));
    me.init();
    $('#btnSearch').click(function () {
        params.incomeType = $('#income option:selected').attr('value'),
            params.logType = $('#fcType option:selected').attr('value') ? $('#fcType option:selected').attr('value') : '',
            params.startTime = $("#startTime").val() ? $("#startTime").val() : ($("#startTime").attr('realvalue') ? $("#startTime").attr('realvalue') : ''),
            params.endTime = $('#endTime').val() ? $('#endTime').val() : ($("#endTime").attr('realvalue') ? $("#endTime").attr('realvalue') : '');
            var startTime=new Date($("#startTime").val());
            var endTime=new Date($("#endTime").val());
            if(getDateDiff(endTime,startTime)>7){
                alert("订单时间查询间隔不能超过7天");
                return;
             }
            me.init();
    })
})
var me = {
    pageSize: 10,
    fcLogList: null,
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
        params.page=page_index;
        $.ajax({
            type: "GET",
            url: baseServiceUrl + "services/splitRepositoryLog/queryRepositoryLogList?t=" + new Date().getTime(),
            data: params,
            contentType: "application/json; charset=UTF-8",
            dataType: "json",
            success: function (resp) {
                var code = resp.responseStatus.code;
                if (code == "00") {
                    me.fcLogList = resp.splitRepositoryLogList ? resp.splitRepositoryLogList : [];
                    if (me.fcLogList != null && me.fcLogList.length > 0) {

                        $(".totalPage").html("共" + resp.totalCount + "笔，共" + resp.totalPage + "页");
                    }
                    else {
                        $(".totalPage").html("共0笔，共1页");
                        $('#fcGetSum').text('0');
                        $('#fcPaySum').text('0');
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
                if (resp.totalPage != 0) {
                    count = resp.totalPage;
                }
                $('#pagination1').jqPaginator('option', {
                    totalPages: count
                });
            }
        });
    },
    appendData: function () {
        var fcLogList = me.fcLogList;
        var html = '', logHtml = '', paySum = 0, getSum = 0;
        if (fcLogList.length) {
            for (var i = 0; i < fcLogList.length; i++) {
                if (fcLogList[i].incomeType == 1) {
                    logHtml = '<p class="red">+' + fcLogList[i].count + '</p>';
                    getSum += fcLogList[i].count;
                } else {
                    logHtml = '<p>-' + fcLogList[i].count + '</p>';
                    paySum += fcLogList[i].count;
                }
                html += '<tr><td class="spc" style="padding-left: 20px"><p>' + timestampToTime(fcLogList[i].createTime, true) + '</p>' +
                    '</td><td>' + logHtml + '</td><td><p>' + fcIncomeText(fcLogList[i].logType) + '</p></td>' +
                    ' <td><p>' + fcLogList[i].fcOrderId + '</p></td></tr>';
            }
            $('.table_fc tbody').html(html);
            $('#fcGetSum').text(getSum == 0 ? '0' : '+' + getSum);
            $('#fcPaySum').text(paySum == 0 ? '0' : ('-' + paySum));
        } else {
            $('.table_fc tbody').html('');
        }
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
function fcIncomeText(status) {
    switch (status) {
        case 1:
            return '分仓所得';
            break;
        case 2:
            return '收货所得';
            break;
        case 3:
            return '分仓支出';
            break;
        case 4:
            return '销售支出';
            break;
    }
}
// 获取URL对应参数值
function getUrlParam(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
    var r = window.location.search.substr(1).match(reg);  //匹配目标参数
    if (r != null) return decodeURI(r[2]); return null; //返回参数值
}
function addZero(param) {
    if (param < 10) {
        return '0' + param
    } else {
        return param
    }
}