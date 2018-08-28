var huanxinkefu, data, ServiceType;
var conn = new WebIM.connection({
    https: WebIM.config.https,
    url: WebIM.config.xmppURL,
    isAutoLogin: true,
    isMultiLoginSessions: WebIM.config.isMultiLoginSessions
});

var options = {
    apiUrl: WebIM.config.apiURL,
    user: "", //账号
    pwd: "", //密码
    appKey: hxAppKey
};

//获取当前订单id
var orderId = $.trim(getUrlParam("orderId"));
var chatroomId;
conn.listen({
    onOpened: function (message) {          //连接成功回调
        console.log(message);
        console.log("连接成功");
        // 如果isAutoLogin设置为false，那么必须手动设置上线，否则无法收消息
        // 手动上线指的是调用conn.setPresence(); 如果conn初始化时已将isAutoLogin设置为true
        // 则无需调用conn.setPresence();
    },
    onClosed: function (message) {
    },         //连接关闭回调
    onTextMessage: function (message) {
        console.log('收到的文本消息', message)
        receivedMsg(message)
    },    //收到文本消息
    onEmojiMessage: function (message) {
    },   //收到表情消息
    onPictureMessage: function (message) {
        console.log('收到的图片消息', message)
        receivedMsgImg(message)
    }, //收到图片消息

    onCmdMessage: function (message) {
    },     //收到命令消息
    onAudioMessage: function (message) {
    },   //收到音频消息
    onLocationMessage: function (message) {
    },//收到位置消息
    onFileMessage: function (message) {  //收到文件消息
        console.log('收到的文件消息', message)
    },
    onVideoMessage: function (message) {
    },   //收到视频消息
    onPresence: function (message) {
    },   //收到联系人订阅请求、处理群组、聊天室被踢解散等消息 
    onRoster: function (message) {
    },         //处理好友申请
    onInviteMessage: function (message) {
    },  //处理群组邀请
    onOnline: function () {
    },                  //本机网络连接成功
    onOffline: function () {
    },                 //本机网络掉线
    onError: function (message) {
        console.log('失败回调', message)
    },          //失败回调
    onBlacklistUpdate: function (list) {       //黑名单变动
                                               // 查询黑名单，将好友拉黑，将好友从黑名单移除都会回调这个函数，list则是黑名单现有的所有好友信息
        console.log(list);
    }
});

var sendPrivateText = function (msg_text, data) {  //文字消息
    var id = conn.getUniqueId();                 // 生成本地消息id
    var msg = new WebIM.message('txt', id);      // 创建文本消息
    var msg_text1 = msg_text.replace(/<br \/>/g, " ");
    console.log("msg_text1--" + msg_text1);
    hxSendSuccess(msg_text);
    var option = {
        msg: msg_text1,          // 消息内容
        to: chatroomId,               // 接收消息对象(聊天室id)
        roomType: false,
        chatType: 'chatRoom',
        ext: {'userType': 'buyer'},
        success: function () {
            console.log('send room text success');
        },
        fail: function () {
            console.log('failed');
        }
    };
    //保存聊天消息
    $.ajax({
        type: "GET",
        url: baseServiceUrl + "services/orderLog/saveChattingRecords",
        data: {orderId: orderId, chattingRecords: msg_text1},
        contentType: "application/json; charset=UTF-8",
        dataType: "jsonp",
        success: function (resp) {
            var code = resp.responseStatus.code;
            if (code == "00") {
                console.log("保存成功");
            }
        }
    });
    msg.set(option);
    msg.setGroup('groupchat');
    conn.send(msg.body);

};

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

var hxSendSuccess = function (msgCont) { //发送文本消息
    var tp = 'hx_my'; //自己发送的消息
    var time = formatDateTime(new Date());
    var cont = myMsgCont(msgCont, tp, time);
    $('.hxMsgC').append(cont)
    $('#msg_text').val('');
    toobottom()
}

var receivedMsg = function (message) { //收到文本消息
    if (chatroomId != message.to) {
        return;
    }
    var msgCont = message.data;
    var tp = 'hx_sh';//出货商发送的消息
    if (message.ext && message.ext.userType == 'buyerSystem') {
        tp = 'hx_sys';
    } else if (message.ext && message.ext.userType == 'buyer') {
        tp = 'hx_my';
    } else if (message.ext && message.ext.userType == 'sellerSystem') {
        return;
    }
    // console.log("环信的信息===========",message);

    if (message.ext.id != undefined && tp == 'hx_sys') {
        if (sysHxmesg.contains(Number(message.ext.id))) {
            // console.log("消息userType",message.ext.userType);
            return;
        }
    }

    var time = formatDateTime(new Date());
    var cont = myMsgCont(msgCont, tp, time);
    // alert(99);
    $('.hxMsgC').append(cont);
    toobottom()
}

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


/*var zhuceHuanxin = function(request){
 $.ajax({ //返回环信客服
 type: "get",
 url: CONSTANTS.APIUser+"huanxin/updateHuanXinToken",
 data: request,
 contentType: "application/json; charset=UTF-8",
 dataType: "json",
 jsonp: "callback"
 }).done(function(resp){
 console.log("返回环信Token",resp)
 }).fail(function(){
 });

 $.ajax({ // 测试用于注册环信账号
 type: "POST",
 url: CONSTANTS.APIUser+"account/forceRegisterHuanXin",
 data: JSON.stringify(request),
 contentType: "application/json; charset=UTF-8",
 dataType: "json",
 }).done(function(resp){
 console.log("强制注册环信",resp)
 if(resp.responseStatus.code == "00"){
 huanxinzhanghao(request)
 }
 }).fail(function(){
 }).always(function(){
 });
 }*/
/*var huanxinzhanghao = function (request) {
 $.ajax({
 type: "POST",
 url: CONSTANTS.APIUser+"account/getHXInfo",
 data: JSON.stringify(request),
 contentType: "application/json; charset=UTF-8",
 dataType: "json",
 }).done(function(resp){
 if(resp.responseStatus.code == "00"){
 var options = {
 apiUrl: WebIM.config.apiURL,
 user:resp.hxAccount,
 pwd: resp.hxPassword,
 appKey: resp.hxAppKey
 };
 conn.open(options);
 }
 }).fail(function(){
 })
 }*/

var sysHxmesg = [];
$(function () {
    // conn.open(options);//初始化用户登录
    var request = {}
    var url = location.search; //获取url中"?"符后的字串
    //url="";
    var theRequest = new Object();
    if (url.indexOf("?") != -1) {
        var str = url.substr(1);
        strs = str.split("&");
        for (var i = 0; i < strs.length; i++) {
            theRequest[strs[i].split("=")[0]] = (strs[i].split("=")[1]);
        }
    }
    //OrderId = theRequest["orderId"];
    //ServiceType = theRequest["serviceType"];
    //查询所有聊天记录

    $.ajax({
        type: "GET",
        url: baseServiceUrl + "services/orderLog/selectChattingRecords",
        data: {orderId: orderId},
        contentType: "application/json; charset=UTF-8",
        dataType: "jsonp",
        success: function (resp) {
            var code = resp.responseStatus.code;
            if (code == "00") {
                var list = resp.orderLogs;
                if (list == null && list.length <= 0) {
                    return;
                }
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
                    sysHxmesg.push(orderLog.id);
                    // console.log("数据库数据=====",orderLog);
                    var msg = orderLog.log;
                    var time = formatDateTime(new Date(orderLog.createTime));
                    var cont = myMsgCont(msg, tp, time)
                    $('.hxMsgC').append(cont);
                    toobottom();

                }
            }
        }
    });
    //查询当前用户环信账号和密码
    $.ajax({
        type: "GET",
        url: baseServiceUrl + "services/chorder/selectBuyerOrderById?t=" + new Date().getTime(),
        data: {orderId: orderId},
        contentType: "application/json; charset=UTF-8",
        dataType: "jsonp",
        success: function (resp) {
            var code = resp.responseStatus.code;
            if (code == "00") {
                var deliveryOrder = resp.deliveryOrder;
                options.user = deliveryOrder.buyerHxAccount;
                options.pwd = deliveryOrder.buyerHxPwd;
                chatroomId = deliveryOrder.chatroomId;
                conn.open(options);//初始化用户登录
            }
        }
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
        sendPrivateText(msg_text, data);
    })
    document.onkeydown = function (e) {
        var ev = document.all ? window.event : e;
        if (ev.keyCode == 13) {
            var msg_text = $('#msg_text').val();
            if (msg_text != "")
                sendPrivateText(msg_text, data);
        }
    }

    toobottom();
    //zhuceHuanxin(request);  //强制注册环信账号
    /*huanxinzhanghao(request);
     $.ajax({ //返回环信客服
     type: "get",
     url: CONSTANTS.APIUser+"huanxin/getKeFuAccount",
     data: request,
     contentType: "application/json; charset=UTF-8",
     dataType: "json",
     jsonp: "callback"
     }).done(function(resp){
     console.log("返回环信客服",resp)
     if(resp.responseStatus.code == "00"){
     huanxinkefu=resp.keFuAccount;
     }
     }).fail(function(){
     })*/
})

function toobottom() {
    $('.hxMsgC').scrollTop($('.hxMsgC')[0].scrollHeight);
};
Array.prototype.contains = function (obj) {
    var i = this.length;
    while (i--) {
        if (this[i] === obj) {
            return true;
        }
    }
    return false;
}
