/**
 * Created by jhlcitadmin on 2017/1/5.
 */
var deliveryOrder = {};
var subOrderList = {}
$(function(){
    me.init();
    me.getLog();
    setInterval("me.init()",10000);
    setInterval("me.getLog()",5000);

    $('#selsetName').change(function(){
        var roleName = $(this).val(),customName=$('#customName');
        (roleName != "") ? customName.css('display','none') : customName.css('display','inline')
        me.selectedRoleName = roleName;
    });

    //提交分配收货角色
    $('#shSub').click(function(){
        var params={};
        var roleName=$('#selsetName').val(),customName=$('#customName').val();
        if(roleName==""){
            if(customName==""){
                alert('请选择收货角色或输入自定义角色名！');
                return;
            }else{
                roleName=customName ;
            }
        }
        //校验数字合法性
        var num = $('#rcPrice').val();
        if(num == "" || !/^[0-9]*$/.test(num)) {
            alert("请输入正确的收货数量");
            $('#rcPrice').val('');
            return;
        }
        var totalNum = $('#rc-fp-span').text();
        if(parseFloat(num)>parseFloat(totalNum)){
            alert("你输入的数目过大,请重新输入");
            $('#rcPrice').attr('value', '');
            return ;
        }
        params.roleName=roleName;
        params.orderId=deliveryOrder.orderId;
        params.count=$('#rcPrice').val();
        $.ajax({
            type:'POST',
            url:baseServiceUrl+"services/manualShOrder/assignGameAccount",
            data:  $.toJSON(params),
            contentType: "application/json; charset=UTF-8",
            dataType: "json",
            success:function (resp) {
                var code=resp.responseStatus.code;
                if(code=="00"){
                    alert("成功分配收货角色");
                    //刷新页面
                    // window.location.reload();
                    me.init(false);
                }
            }
        });
    });

    //取消分配收货角色
    $('#cancalSub').click(function(){
        $('#customName').val('');
        $('#rcPrice').val('')
    });

    $('.hx_send').click(function () { //点击发送消息
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
})

var me={
    subOrderList:null,
    selectedRoleName:null,
    orderId:$.trim(getUrlParam("orderId")),
    ofableCount:null,
    init:function () {
        $.ajax({
            type: "GET",
            url: baseServiceUrl + "services/deliveryOrder/selectDeliverySubOrderData?t="+new Date().getTime(),
            data: {orderId:me.orderId},
            contentType: "application/json; charset=UTF-8",
            dataType: "json",
            success: function (resp) {
                var code = resp.responseStatus.code;
                if (code == "00") {
                    deliveryOrder=resp.deliveryOrder;
                    subOrderList = resp.subOrderList;
                    orderStatus=resp.orderStatus;
                    roleNames=resp.roleNames;
                    me.initPage();
                    if(deliveryOrder.tradeLogo==5){
                      if(subOrderList.length>0){
                          $(".upload-screenshot").show()
                          up.init();
                          setTimeout(function () {
                              if($('#imgPicker').find('input').length){
                                  $('#imgPicker').find('input').addClass('trPath')
                                  //$('#imgPicker').find('input').attr('onchange',fullPath(this))
                              }
                            },100);
                      }else{
                        $(".upload-screenshot").hide()
                      }
                    }else{
                      $(".upload-screenshot").hide()
                    }

                }
            }
        });
    },
    initPage:function() {
        var totalCount=0;
        //初始化页面
        if (deliveryOrder != null) {
            if(deliveryOrder.status==shStatus.Complete||deliveryOrder.status==shStatus.PartComplete||deliveryOrder.status==shStatus.Cancel||deliveryOrder.status==shStatus.HumanOp){
            //跳转
            window.open(baseHtmlUrl + "orderManualResult.html?orderId="+deliveryOrder.orderId+"&t="+(new Date()).getTime(), "_self");
            }
            $('#rc-span-gameName').html(deliveryOrder.gameName+"/"+deliveryOrder.region+"/"+deliveryOrder.server);
            $('#rc-sapn-recnum').text(deliveryOrder.id);
            $('#rc-sapn-ordernum').text(deliveryOrder.orderId);
            $('#rc-sapn-recprice').html(deliveryOrder.price+"<em>元 / "+deliveryOrder.moneyName+"</em>");
            $('#rc-sapn-status').text(orderStatus);
            // if(deliveryOrder.status==shStatus.Queue){
            //     $('#rc-sapn-status').text("待分配角色");
            // }else{
            //     $('#rc-sapn-status').text(deliveryOrder.orderStatusStr);
            // }
            $('#rc-sapn-ordercount').html(deliveryOrder.count+"<em>"+deliveryOrder.moneyName+"</em>");
            $('#rc-sapn-trade').text(deliveryOrder.tradeTypeName);
            $('#rc-sapn-total').text(deliveryOrder.amount);
            $('#rc-sapn-add').text(deliveryOrder.address);
            $('#rc-sapn-realcount').html(deliveryOrder.realCount+"<em>"+deliveryOrder.moneyName+"</em>");
            $('#rc-sapn-name').text(deliveryOrder.roleName);
            $("#qq-link").attr("href","tencent://message/?uin="+deliveryOrder.serviceQq+"&Site=m5173&Menu=yes");
            $("#qq-img").attr("src","http://html.5173.com/dlc2c/images/global_v2/seller/qqinline.gif?p=2:"+deliveryOrder.serviceQq+":1");
            $('#rc-sapn-createtime').text(new Date((deliveryOrder.createTime)).Format("yyyy-MM-dd hh:mm:ss"));
            if(deliveryOrder.tradeEndTime!=null){
                $('#rc-sapn-endtime').text(new Date((deliveryOrder.tradeEndTime)).Format("yyyy-MM-dd hh:mm:ss"));
            }
            // $('#rc-sapn-reason').text(deliveryOrder.orderId);

            //先删除之前的数据（防止重复数据）
            $(".mytr").remove();
            if (subOrderList != null && subOrderList != undefined && subOrderList != ''&& subOrderList.length > 0) {
                for (var i in subOrderList) {
                    subOrder = subOrderList[i];
                    if (subOrder == null || subOrder.gameRole == null) {
                        break;
                    }
                    htmlStr = "<tr class='rc-table-tr mytr'>";
                    htmlStr += "<td width='100'>" + subOrder.gameRole + "</td>";
                    htmlStr += "<td width='100'>" + subOrder.count + "</td>";
                    htmlStr += "<td width='100'>" + subOrder.realCount + "</td>";
                    htmlStr += "<td width='80'>" + subOrder.status + "</td>";
                    htmlStr += "<td width='200'>";
                    if(subOrder.subOrderStatus==shStatus.WaitDelivery||subOrder.subOrderStatus==shStatus.DeliveryFinish){
                        htmlStr += "<button onclick='cancelGoods("+subOrder.id+")' class='rc-cancel' >申请撤单</button>";
                        htmlStr += "<button onclick='successGoods("+subOrder.id+")' class='rc-success'  >确认收货</button>";
                    }
                    htmlStr += "</td>";
                    htmlStr += "</tr>";
                    $('#rc-table').append(htmlStr);
                    totalCount+=subOrder.realCount;
                }
            } else {
                $('#rc-table').append('');
            }
            var ofablCount=deliveryOrder.count-totalCount;
            $('#rc-fp-span').text(ofablCount);
        }
        //获取收货角色
        $(".myoption").remove();
        if(roleNames!=null && roleNames.length>0){
            for(i=0;i<roleNames.length;i++){
                var roleName = roleNames[i];
                var optionStr="<option class='myoption' value ='"+roleName+"'>"+roleName+"</option>";
                $("#selsetName").append(optionStr);
            }
            $("#selsetName").val(me.selectedRoleName);
        }else {
            $("#selsetName").append('');
        }

        $('.rc-fp-em').text(deliveryOrder.moneyName);
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
                        if (2 == orderLog.userType) {
                            tp = "hx_sh";
                        } else if (3 == orderLog.userType) {
                            tp = 'hx_sys';
                        } else if (orderLog.userType == null) {
                            return;
                        }
                        // sysHxmesg.push(orderLog.id);
                         console.log("数据库数据=====",orderLog);
                        var msg = orderLog.log;
                        var time = formatDateTime(new Date(orderLog.createTime));
                        var cont = myMsgCont(msg, tp, time)
                        result = result + cont;
                    }
                    $('.hxMsgC').html(result);
                    toobottom();
                }
            }
        });
    }
};

//申请撤单
function cancelGoods(id){
    $.ajax({
        type:'GET',
        url:baseServiceUrl+"services/manualShOrder/cancelSubOrder",
        data: {subOrderId:id},
        contentType: "application/json; charset=UTF-8",
        dataType:'json',
        success:function (resp) {
            var code=resp.responseStatus.code;
            var msg=resp.responseStatus.message;
            if(code=="00"){
                alert("成功申请撤单");
                //刷新页面
                // window.location.reload();
                me.init(false);
            }
        }
    });
}

//确认收货
function successGoods(id){
    var confirmDelivery=confirm("您确认已收到全部货物了吗？")
    if(confirmDelivery){
        $.ajax({
         type:'GET',
         url:baseServiceUrl+"services/manualShOrder/confirmReceived",
         data: {subOrderId:id},
         contentType: "application/json; charset=UTF-8",
         dataType:'json',
         success:function (resp) {
                var code=resp.responseStatus.code;
             var msg=resp.responseStatus.message;
              if(code=="00"){
                 alert("成功确认收货");
                //刷新页面
                //   window.location.reload();
                  me.init(false);
         }
        }
    });}
}

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
    "Waiting":1, // 等待交易
    "Queue":2, // 排队中
    "Dealing":3,//交易中
    "Complete":4,//交易完成
    "PartComplete":5,//部分完单
    "Cancel":6,//撤单
    "HumanOp":7,//需人工介入
    "ApplyCompletePart":8,//申请部分完单
    "WaitDelivery":9,//待发货
    "DeliveryFinish":10//已发货
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
    var tips = '出货商：'//出货商发送的消息
    if (tp == 'hx_my') {//判断自己发送的消息
        tips = '我：'
    } else if (tp == 'hx_sys') {//判断系统发送的消息
        tips = '系统消息：'
    }
    var cont = '<div class="hxMsgL ' + tp + '">' + time + ' <strong>' + tips + '</strong>' + msg + '</div>'
    return cont;
}

function toobottom() {
    $('.hxMsgC').scrollTop($('.hxMsgC')[0].scrollHeight);
};

var sendPrivateText = function (msg_text) {  //文字消息
    var msg_text1 = msg_text.replace(/<br \/>/g, " ");
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
    });;
};
