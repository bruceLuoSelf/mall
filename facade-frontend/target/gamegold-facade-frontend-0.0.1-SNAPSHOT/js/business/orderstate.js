/**
 * 订单状态详情页
 */
var orderId_orderState;
var orderInfo_orderState;
var orderState;
var orderDelay;
//var clipboardClient;

$(document).ready(function() {
	orderId_orderState = getUrlParam("orderId");
	getOrderState();
});

var initClip=false;
// 获取订单信息
setInterval("getOrderState()" , 1000*10);

window.nowTime = new Date().getTime();

function getOrderState(){
	// 根据订单号查询订单详情
	var request = {};
	request.orderId = orderId_orderState;
	$.ajax({
		type: "POST",
		url: baseServiceUrl + "services/order/queryorderbyid?t="+new Date().getTime(),
		data: $.toJSON(request),
		contentType: "application/json; charset=UTF-8",
		dataType: "json",
		success: function(resp) {
			var responseStatus = resp.responseStatus;
            var code = responseStatus.code;
            if (code == "00") {
                if(resp.currentDate && resp.currentDate > 0){
                    // 初始化服务端时间
                    window.nowTime = resp.currentDate;
                }
            	orderInfo_orderState = resp.orderInfoEO;
				//订单状态相同时，不刷新页面
				if(orderState==orderInfo_orderState.orderState&&orderDelay==orderInfo_orderState.isDelay)
				{
					return;
				}

				orderState=orderInfo_orderState.orderState;
				orderDelay=orderInfo_orderState.isDelay;
            	// 显示不同的订单状态
            	buildOrderState(orderInfo_orderState);
            	// 显示订单信息
            	buildOrderInfo(orderInfo_orderState);
            	// 显示客服信息
            	buildServicer(orderInfo_orderState.servicerInfo);
                // 初始化复制功能
                if(!initClip){
                    clipboard();
                    initClip = true;
                }
            }
		},
		error: function(resp) {
			console.log(resp);
		}
	});
}

// 显示不同的订单状态
function buildOrderState(orderInfo){
	// 默认收货流程
	$("#orderInstr dd").removeClass();
	$("#on_reciverOrder").attr("class","on");
	$(".service_word").html("");

	var servicerInfo=orderInfo.servicerInfo;
	var nickName = isNull(servicerInfo.nickName)?"公主":servicerInfo.nickName;
    var displayWord = ""; // 客服显示服务用语
	if(orderInfo.orderState == OrderState.WaitPayment){
		// 待付款状态
		$("#orderInstr dd").removeClass();
		$("#on_payOrder").attr("class","on");
        displayWord = "<p>亲，您现在还未付款哦！如有问题请联系"+nickName+"哦！！</p><br/>";
	}else if(orderInfo.orderState == OrderState.Paid){
		// 没有库存
//		if(orderInfo.isHaveStore == false){
//			$(".service_word").html("<p>非常抱歉，由于无货，本次交易取消</p>" +
//			"<p class='p_close'>无货赔付10.00元，以及交易金额"+toDecimal2(orderInfo.totalPrice)+"元，共计"+toDecimal2(parseFloat(orderInfo.totalPrice)+10)+"元，" +
//					"将退回到您的5173账户<a href='javascript:window.opener=null;window.open(\"\",\"_self\");window.close();'>[关闭页面]</a></p>");
//			return;
//		}
		// 已付款状态
		if(orderInfo.isDelay == true){
            displayWord = "<p>发货超时，"+nickName+"会继续为你发货</p><br/>";
		}else {
            if(window.currentcountdown) {
                window.clearInterval(parseInt(window.currentcountdown));
            }
            var countdownTime = window.setInterval("countdownTime("+orderInfo.payTime+","+orderInfo.deliveryTime+")" , 1000);
            window.currentcountdown = countdownTime.toString();
            if($.trim($(".service_word").html()).length == 0){
                displayWord = "<p>正在为您发货，<strong id='countdownTime'></strong>后请查收，如有问题请与"+nickName+"联系。</p>";
            }
		}
	}else if (orderInfo.orderState == OrderState.WaitDelivery) {
		// 待发货状态
		// 查询交易信息
		displayWord = queryTradePlace(orderInfo, displayWord,nickName);
	}else if (orderInfo.orderState == OrderState.Delivery) {
		// 已发货状态
		displayWord = queryTradePlace(orderInfo, displayWord,nickName);
	}else if (orderInfo.orderState == OrderState.Statement){
		// 结单状态
		$(".service_finish").html("交易完成");
		displayWord = "<p>谢谢您的惠顾，下次购买请找"+nickName+"，希望能再次为您服务！</p><br/>";
//		var html2 = "";
//		if(orderInfo.isDelay == true && orderInfo.isHaveStore == true){
//			html2 += "<p class='p_close'>超时赔付2.00元，已转入到您的5173账户，点击 <a href='http://user.5173.com/default.aspx'>查看余额</a></p>";
//			html += html2;
//		}else if(orderInfo.isDelay != true && orderInfo.isHaveStore != true){
//			html2 += "<p class='p_close'>无货赔付10.00元，已转入到您的5173账户，点击 <a href='http://user.5173.com/default.aspx'>查看余额</a></p>"
//			html += html2;
//		}else if(orderInfo.isDelay == true && orderInfo.isHaveStore != true){
//			html2 += "<p class='p_close'>超时赔付2.00元，无货赔付10.00元，已转入到您的5173账户，点击 <a href='http://user.5173.com/default.aspx'>查看余额</a></p>";
//			html += html2;
//		}
       // displayWord += "<p class='p_close'><a href='javascript:CloseWebPage();'>[关闭页面]</a></p>";
//		$(".service_order_info").css("display", "none");
		$("#orderInstr").css("display", "none");
	}else if (orderInfo.orderState == OrderState.Cancelled || orderInfo.orderState == OrderState.Refund){
		var refundReason="";
		for(var p in jsonRefundData){
			if(jsonRefundData[p].code==orderInfo.refundReason){
				refundReason=jsonRefundData[p].value;
				break;
			}
		}

		$(".service_finish").html("交易取消");
		displayWord = "<p>非常抱歉，本次交易取消，原因  : ";
		if(refundReason!=null&&refundReason!=''){
			displayWord +=refundReason;
		}
		else{
			displayWord +="其他原因";
		}
		displayWord+="<br/><br/>";
        //$(".service_finish").html("交易取消");
        //displayWord = "<p>非常抱歉，本次交易取消，原因  : ";
        //var html2 = "";
        //if(orderInfo.cancelReson!=null&&orderInfo.cancelReson!=''){
        //    if(orderInfo.cancelReson=='卖家游戏角色无货或少货；') {
        //        displayWord += "第三方系统原因";
        //    } else {
        //        displayWord +=orderInfo.cancelReson;
        //    }
        //} else {
        //    displayWord += "第三方系统原因";
        //}


        /* else{
            if(orderInfo.isDelay == true && orderInfo.isHaveStore != false){
                // 发货超时
                displayWord += "发货超时";
            //			html2 += "支付"+orderInfo.totalPrice+"元，超时赔付2.00元，共计"+(orderInfo.totalPrice+2)+"元已退回到您的5173账户，点击 <a href='http://user.5173.com/default.aspx'>查看余额</a>";
            }else if(orderInfo.isDelay != true && orderInfo.isHaveStore != false){
                // 是否无货
                displayWord += "您的个人原因";
            }else if(orderInfo.isDelay == true && orderInfo.isHaveStore == false){
                // 是否无货
                displayWord += "发货超时、无货";
            //			html2 += "支付"+orderInfo.totalPrice+"元，超时赔付2.00元、无货赔付10.00元，共计"+(orderInfo.totalPrice+12)+"元已退回到您的5173账户，点击 <a href='http://user.5173.com/default.aspx'>查看余额</a>";
            }else if(orderInfo.isDelay != true && orderInfo.isHaveStore == false){
                displayWord += "无货";
            //			html2 += "支付"+orderInfo.totalPrice+"元，无货赔付10.00元，共计"+(orderInfo.totalPrice+10)+"元已退回到您的5173账户，点击 <a href='http://user.5173.com/default.aspx'>查看余额</a>";
            }
        }*/
        displayWord += "</p>";
//		displayWord += "<p  class='p_close'>"+html2+"</p>";
        //displayWord += "<p class='p_close'><a href='javascript:CloseWebPage();'>[关闭页面]</a></p>";
	}

    displayWord += "<p class='p_close' style='color:orangered'>温馨提示：任何一个向您取回已交易物品，或索要5173账号的都是骗子。</p>";
    displayWord += "<p class='p_close'>如果关闭了此页面，您可以在“我的5173→我是买家→游戏币商城订单”中再次打开。</p>";
    $(".service_word").html(displayWord);
}

// 查询交易地点
function queryTradePlace(orderInfo, displayWord,nickName){
	var request = {};
	request.gameName = orderInfo.gameName;
	request.goodsTypeName = orderInfo.goodsTypeName;
	$.ajax({
		type: "POST",
		url: baseServiceUrl + "services/querytradeplace/queryplace?t="+new Date().getTime(),
		data: $.toJSON(request),
		contentType: "application/json; charset=UTF-8",
		dataType: "json",
        async:false,
		success: function(resp) {
			var responseStatus = resp.responseStatus;
            var code = responseStatus.code;
            if (code == "00") {
            	var tradeInfo = resp.place;
            	if(isNull(tradeInfo)){
            		tradeInfo = {};
            		tradeInfo.placeName = "";
            		tradeInfo.mailTime = "";
            	}
            	if(orderInfo.orderState == OrderState.WaitDelivery){
                    if(window.currentcountdown) {
                        window.clearInterval(parseInt(window.currentcountdown));
                    }
                    var countdownTime = window.setInterval("countdownTime("+orderInfo.payTime+","+orderInfo.deliveryTime+")" , 1000);
                    window.currentcountdown = countdownTime.toString();
                    if($.trim($(".service_word").html()).length == 0){
                        displayWord = "<p>正在为您发货，<strong id='countdownTime'></strong>后请查收，如有问题请与"+nickName+"联系。</p>";
                    }
                    if(orderInfo.tradeType == TradeType.NoDivid){
                		// 当面交易
                        displayWord = "<p>请您尽快前往 :<strong><br>"+tradeInfo.placeName+"<br></strong> 如有问题请联系"+nickName+"哦！！</p>";
                        if(orderInfo.isDelay == true){
                            displayWord += "<p class='p_close'>非常抱歉,发货超时<br/></p>";
                        }else {
                            displayWord += "<p class='p_close'>承诺发货时间倒计时：<strong id='countdownTime'></strong><a href='javascript:window.location.reload();'>[刷新页面]</a></p>";
                        }
                        if(!isNull(tradeInfo.placeImage)){
                            displayWord += "<p class='p_img'><img src='"+buildImageUrl(tradeInfo.placeImage,"450x246")+"'></img></p>";
                        }
                	}else {
                		// 邮寄交易
                		if(orderInfo.isDelay == true){
                            displayWord = "<p>非常抱歉,发货超时</p><br/>";
                		}else {
                            displayWord = "<p>正在为您邮寄发货，<strong id='countdownTime'></strong>后请查收，如有问题请与"+nickName+"联系</p>";
                			//$(".service_word").append("<p class='p_close'>我承诺发货时间倒计时：<strong id='countdownTime'></strong><a href='javascript:window.location.reload();'>[刷新页面]</a></p>");
        				}
    				}

                    if(orderInfo.gameName == "地下城与勇士"){
                      //  $(".service_word").append("<p class='p_close'>尊敬的用户您好，因游戏内活动导致部分区服爆满，我们的工作人员正在努力为您发货，请耐心等待，如有其他问题请随时与我联系！</p>");
                    }

            	}else if (orderInfo.orderState == OrderState.Delivery) {
            		if(orderInfo.tradeType == TradeType.NoDivid){
            			// 当面交易
                        displayWord = "<p>已将 <strong>"+orderInfo.goldCount+"</strong> "+orderInfo.moneyName+"发到您 <strong>"+orderInfo.receiver
                            +"</strong> 角色 ，请您确认收货，如果未到账，请与"+nickName+"联系。</p>";
                	}else {
                        displayWord = "<p>已将 <strong>"+orderInfo.goldCount+"</strong> "+orderInfo.moneyName+"发到您 <strong>"+orderInfo.receiver
                            +"</strong> 角色 ，请您<strong>"+getMailTimeOnDelivery(orderInfo.sendTime,tradeInfo.mailTime)+"</strong>后查收邮件，如果未到账，请与"+nickName+"联系。</p>";
    				}
            		if(orderInfo.isDelay == true){
//            			$(".service_word").append("<p class='p_close'>非常抱歉由于我发货超时，交易结束后您将获得超时赔付2.0元<a href='javascript:window.opener=null;window.open(\"\",\"_self\");window.close();'>[关闭页面]</a></p>");
            		}
                    displayWord += "<p class='p_close'>如30分钟内您没有确认收货，系统将自动完成交易打款给"+nickName+"</p>";
				}
				displayWord +="<br/>";
			}
		}
	});

    return displayWord;
}

// 显示订单信息
function buildOrderInfo(orderInfo){
    if(!orderInfo_orderState){
        return;
    }
	var html = "<h3><span class='f_l'>您的订单信息</span></h3>";
	html += "<div class='service_order_info_ul_layout'>";
	html += "<ul>";
	html += "<li>订单编号："+orderInfo.orderId+" <a id='href_clip' href='javascript:clipboard();'>[复制订单信息]</a></li>";
	html += "<li>游戏区服："+orderInfo.gameName+"/"+orderInfo.region+"/"+orderInfo.server;
	if(!isNull(orderInfo.gameRace)){
		html += "/"+orderInfo.gameRace+" "+orderInfo.goldCount+"金</li>";
	}
	html += "<li>收货角色名："+orderInfo.receiver+"</li>";
	html += "<li>价格："+toDecimal2(orderInfo.totalPrice)+"元 </li>";
	
	if(orderInfo.orderState == OrderState.Delivery){
		// 已发货状态
		html += "<li><a class='a_service_confirm' href='javascript:receiveGold(\""+orderInfo.orderId+"\",\""+orderInfo.gameName+"\")'><span>我已收货</span></a></li>";
	}
	
	html += "</ul></div></div>";
	
	$(".service_order_info").html(html);

//    // 复制订单功能
//    clipboardClient.addEventListener( "mouseDown", function(client) {
//        clipboardClient.setText(clipTxt);
//    } );
//    clipboardClient.glue( 'href_clip' );
}

// 我已收货
function receiveGold(orderId,gameName){
	var request = {};
	request.orderId = orderId;
	request.orderState = OrderState.Statement;
	var rexStr = new RegExp("魔兽世界"+".*?");
	if(rexStr.test(gameName)){
		alert('该订单已经设置转账延时，24小时后自动转账给卖家！');
		return;
	}
	$.ajax({
		type: "POST",
		url: baseServiceUrl + "services/order/modifyorder",
		data: $.toJSON(request),
		contentType: "application/json; charset=UTF-8",
		dataType: "json",
		beforeSend: function (request){
            request.setRequestHeader("5173_authkey", getAuthkey());
        },
		success: function(resp) {
			var responseStatus = resp.responseStatus;
            var code = responseStatus.code;
            if (code == "00") {
            	getOrderState();
            }
		}
	});
}

// 订单延迟
function delayOrder(orderId){
	var request = {};
	request.orderId = orderId;
	
	$.ajax({
		type: "POST",
		url: baseServiceUrl + "services/order/delay",
		data: $.toJSON(request),
		contentType: "application/json; charset=UTF-8",
		dataType: "json",
		beforeSend: function (request){
            request.setRequestHeader("5173_authkey", getAuthkey());
        },
		success: function(resp) {
			var responseStatus = resp.responseStatus;
            var code = responseStatus.code;
            if (code == "00") {
            	getOrderState();
            }
		}
	});
}

// 客服信息
function buildServicer(servicerInfo){
	var avatarUrl = isNull(servicerInfo.avatarUrl)?"img/head.png":buildImageUrl(servicerInfo.avatarUrl,"64x64");
	$("#service_avatar").attr("src", avatarUrl);
	
	var nickName = isNull(servicerInfo.nickName)?"公主":servicerInfo.nickName;
	$(".service_name").html(servicerInfo.nickName);
	
	// 添加QQ点击绑定事件
	$('.chose_service_qq').unbind("click");
	$(".chose_service_qq").click(function() {
		if(isNull(servicerInfo) || isNull(servicerInfo.qq)){
			return;
		}
		window.open("http://wpa.qq.com/msgrd?v=3&uin="+servicerInfo.qq+"&site=qq&menu=yes", "_blank");
	});
	
	var qq = isNull(servicerInfo.qq)?"":servicerInfo.qq;
	//$(".chose_service_qq").html("QQ<strong>"+qq+"</strong>");
    $(".service_qq").html("QQ：<strong>"+qq+"</strong>");
	
	var phoneNumber = isNull(servicerInfo.phoneNumber)?"":servicerInfo.phoneNumber;
	$(".chose_service_phone").html("手机<strong>"+phoneNumber+"</strong>");
	
	var weiXin = isNull(servicerInfo.weiXin)?"":servicerInfo.weiXin;
	$(".chose_service_wechat").html("微信<strong>"+weiXin+"</strong>");
}

// 复制方法
function clipboard() {
    if(!orderInfo_orderState){
        return;
    }
    var clipTxt =   "订单号\t\t:"+orderInfo_orderState.orderId+'\r\n'+
                    "游戏区服\t:"+orderInfo_orderState.gameName+"/"+orderInfo_orderState.region+"/"+orderInfo_orderState.server+'\r\n'+
                    "收货角色名\t:"+orderInfo_orderState.receiver+'\r\n'+
                    "商品价格\t:"+toDecimal2(orderInfo_orderState.totalPrice)+'元\r\n';

    if(window.clipboardData){      //for ie
        var copyBtn = document.getElementById('href_clip');
        copyBtn.onclick = function(){
            window.clipboardData.setData('text',clipTxt);
            alert("复制订单信息成功");
        }
    }else{
        var clip = new ZeroClipboard.Client(); // 新建一个对象
		clip.setHandCursor( true );
        clip.setText(clipTxt); // 设置要复制的文本。

		clip.addEventListener( "mouseUp", function(client) {
			alert("复制订单信息成功");
		});
		// 注册一个 button，参数为 id。点击这个 button 就会复制。
		//这个 button 不一定要求是一个 input 按钮，也可以是其他 DOM 元素。
		clip.glue('href_clip'); // 和上一句位置不可调换
}
    return false;
}

var jsonRefundData=[
	{code: 101,value:'由于买家选错游戏区服，当前交易被迫终止'},
	{code: 102,value:'由于买家选错游戏阵营，当前交易被迫终止'},
	{code: 103,value:'由于买家的游戏角色名或数字ID错误，当前交易被迫终止'},
	{code: 104,value:'由于买家的游戏角色名等级不够或未到达游戏运营商的最低交易等级，无法到指定地点 交易，当前交易被迫终止'},
	{code: 105,value:'由于买家通过QQ或者电话要求客服终止当前交易'},
	{code: 106,value:'由于买家未及时交易，且客服通过QQ或电话均无法与买家取得联系，当前交易被迫终止'},
	{code: 201,value:'卖家游戏角色无货或少货'},
	{code: 202,value:'卖家游戏角色数据异常，客服无法登录游戏'},
	{code: 203,value:'卖家游戏内当日发送邮件次数已达上限'},
	{code: 204,value:'卖家游戏角色等级今日已达发货上限，无法发货，交易取消'},
	{code: 301,value:'游戏运营商服务器维护（包括：服务器繁忙等）'},
	{code: 302,value:'游戏运营商原因，导致卖家游戏帐号无法登录游戏，交易取消'},
	{code: 401,value:'其他原因'}
];