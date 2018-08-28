$(function () {
    var orderNo = getUrlParam('orderNo');
    getOrderDetail(orderNo)
    $('#goFcList').attr('href','/fcIndex.html?'+location.href.split('?')[1])
})
function getOrderDetail(orderNo) {
    $.ajax({
        type: "GET",
        url: baseServiceUrl + "services/splitRepository/queryOrderDetails?t=" + new Date().getTime(),
        data: { orderNo: orderNo },
        contentType: "application/json; charset=UTF-8",
        dataType: "json",
        success: function (resp) {
            var html = '', orderDetail = resp.order, listDetail = resp.subOrderList, listHtml = '', relationHtml = '';
            if (resp.useRepertoryCount) {
                $('#useChest').parent().show();
                $('#useChest').text(resp.useRepertoryCount + '万金');
            } else {
                $('#useChest').parent().hide();
            }
            if (orderDetail.robotReason == 1) {
                $('#fcTopAccount').show()
            } else {
                $('#fcTopAccount').hide()
            }
            for (var i = 0; i < listDetail.length; i++) {
                listHtml += '<div>【' + listDetail[i].gameRole + '】' + listDetail[i].realCount + '万金</div>'
            }
            html += '<div class="s-content fl"><div class="s-item clearfix"><div class="w-front fl">游戏区服：</div>' +
                '<div class="w-behind fl">' + orderDetail.gameName + '/' + orderDetail.region + '/' + orderDetail.server + '</div></div><div class="s-item"><div class="w-front fl">分仓订单编号：</div>' +
                '<div class="w-behind fl">' + orderDetail.orderId + '</div></div><div class="s-item"><div class="w-front fl">分仓游戏帐号：</div>' +
                '<div class="w-behind fl">' + orderDetail.gameAccount + '</div></div><div class="s-item clearfix"><div class="w-front fl">分仓明细：</div>' +
                '<div class="w-behind fl">' + listHtml + '</div></div></div>' +
                '<div class="s-content fl"><div class="s-item clearfix"><div class="w-front fl">创建时间：</div><div class="w-behind fl">' + timestampToTime(orderDetail.createTime, true) + '</div>' +
                '</div><div class="s-item"><div class="w-front fl">分仓状态：</div><div class="w-behind fl">' + getStatus(orderDetail.status) + '</div></div>' +
                '<div class="s-item"><div class="w-front fl">分仓数量：</div><div class="w-behind fl">' + orderDetail.realCount + '万金</div></div>' +
                '<div class="s-item"><div class="w-front fl">分仓设置：</div><div class="w-behind fl">' + orderDetail.splitReason + '</div></div></div>';
            $('#fcOrderContent').append(html);
            relationHtml += '<div class="s-content fl"><div class="s-item clearfix"><div class="w-front fl">订单编号：</div>' +
                '<div class="w-behind fl"><a style="color: #0066CC;" href=/shOrderList.html?orderId='+orderDetail.shOrderId+' target="_blank">' + orderDetail.shOrderId + '</a></div></div></div><div class="s-content fl"><div class="w-front fl">收货角色：</div><div class="w-behind fl">' + orderDetail.gameRole + '</div>' +
                '</div>';
            // '<div class="s-item clearfix"><div class="w-front fl">实际数量：</div><div class="w-behind fl">' + deliveryOrder.realCount + '万金</div></div>' +
            // '</div>' +
            // '<div class="s-content fl"><div class="s-item clearfix"><div class="w-front fl">订单时间：</div><div class="w-behind fl">2018-11-11 11:11:11</div>' +
            // '</div></div>';
            $('#relationOrderDetail').append(relationHtml);
        }
    });
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
function addZero(param) {
    if (param < 10) {
        return '0' + param
    } else {
        return param
    }
}