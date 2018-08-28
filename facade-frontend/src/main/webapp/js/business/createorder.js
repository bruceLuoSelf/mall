/**
 * 创建订单页面
 */
var gameName_createOrder;
var gameRegion_createOrder;
var gameServer_createOrder;
var gameRace_createOrder;
var gameId_createOrder;
var regionId_createOrder;
var serverId_createOrder;
var raceId_createOrder;
var goodsId_createOrder;
var goodsCat_createOrder;
var goodsSeller_createOrder;
var goldCount_createOrder;
var goodsDiscount_createOrder = 1;
var maxCount;
var refererType;
var internetBar;
var capacity;
var piccOrNot;
var rate;
var ceiling;
var floor;
var insuranceSettings = null;
var loginAccount;

$(document).ready(function() {
	gameName_createOrder = $.trim(getUrlParam("gameName"));
	gameRegion_createOrder = $.trim(getUrlParam("gameRegion"));
	gameServer_createOrder = $.trim(getUrlParam("gameServer"));
	gameRace_createOrder = isNull(getUrlParam("gameRace"))?null:$.trim(getUrlParam("gameRace"));
	gameId_createOrder = isNull(getUrlParam("gameId"))?null:getUrlParam("gameId");
	regionId_createOrder = isNull(getUrlParam("regionId"))?null:getUrlParam("regionId");
	serverId_createOrder = isNull(getUrlParam("serverId"))?null:getUrlParam("serverId");
	raceId_createOrder = isNull(getUrlParam("raceId"))?null:getUrlParam("raceId");
	goodsCat_createOrder = isNull(getUrlParam("goodsCat"))?null:getUrlParam("goodsCat");//1、2、3
	goodsId_createOrder = isNull(getUrlParam("goodsId"))?null:getUrlParam("goodsId");
	goldCount_createOrder = getUrlParam("goldCount");
	refererType = getUrlParam("refererType");
	internetBar = getUrlParam("internetBar");
	loginAccount=isNull(getUrlParam("loginAccount"))?null:getUrlParam("loginAccount");
	
	// 订单页联合查询
	var orderHtmlQuery = {};
	orderHtmlQuery.gameName = gameName_createOrder;
	orderHtmlQuery.region = gameRegion_createOrder;
	orderHtmlQuery.server = gameServer_createOrder;
	orderHtmlQuery.gameRace = gameRace_createOrder;
	orderHtmlQuery.goodsId = goodsId_createOrder;
	orderHtmlQuery.goodsCat = goodsCat_createOrder;
	orderHtmlQuery.goldCount = goldCount_createOrder;
	orderHtmlQuery.servicerType = UserType.CustomerService;
	orderHtmlQuery.loginAccount = loginAccount;
	orderHtmlQuery.size = 8;
	setInterval(function(){changeColor();},300);

	$.ajax({
		type: "POST",
		url: baseServiceUrl + "services/order/htmlquery?t="+new Date().getTime(),
		data: $.toJSON(orderHtmlQuery),
		contentType: "application/json; charset=UTF-8",
		dataType: "json",
		async: false,
		success: function(resp) {
			console.log(resp.insuranceSettings);
			if(resp.insuranceSettings != null){
				insuranceSettings = resp.insuranceSettings;
				enabled = resp.insuranceSettings.enabled;
				rate = resp.insuranceSettings.rate;
				ceiling = resp.insuranceSettings.ceiling;
				floor = resp.insuranceSettings.floor;
				if (enabled){
					$("#piccTitle").show();
					$("#piccInput").show();
					$("#piccInfo").show();

					// 默认选中购买保险按钮
					if (gameName_createOrder == "斗战神"
						|| gameName_createOrder == "魔兽世界(国服)"
						|| gameName_createOrder == "QQ三国"
						|| gameName_createOrder == "剑灵"
						|| gameName_createOrder == "剑侠情缘Ⅲ"
						|| gameName_createOrder == "疾风之刃"
						|| gameName_createOrder == "龙之谷") {
						$(":radio[name='picc'][value='buyPicc']").prop("checked", true);
					}
				}
			}
			var responseStatus = resp.responseStatus;
			////////////////////////////////////////////////////////////////////////////
			capacity = resp.maxCount;
			////////////////////////////////////////////////////////////////////////////
            var code = responseStatus.code;
            if (code == "00") {
				maxCount = resp.maxCount;
				goodsInfo_createOrder = resp.goodsInfo;

				if(goldCount_createOrder > capacity && goodsCat_createOrder!=2){
					alert("卖家库存不足，当前价格最大购买数量："+capacity+"("+goodsInfo_createOrder.moneyName+")，请重新填写数量或者返回选择其它商品。");
					// 购买金币数
					$("[name='txtReceivingCount']").attr("value", capacity);
				}else{
					$("[name='txtReceivingCount']").attr("value", goldCount_createOrder);
				}

            	var servicerList = resp.userInfoEOs;
            	// 组装客服信息
            	buildServicerHtml(servicerList);
            	//搜索客服
            	$("#search_button").bind("click",function(){
            		searchServicer(servicerList);
            	});
            	// 如果客服数量为1，默认选中
            	if(!isNull(servicerList) && servicerList.length == 1){
            		var serviceInfo = servicerList[0];
            		selectServicerId = serviceInfo.id;
            		selectServicer(serviceInfo.id);
            	}

            	//判断商品是否为空
            	if(isNull(goodsInfo_createOrder) || goodsInfo_createOrder.isDeleted == true){
            		alert("该商品未找到(已下架或网络问题)，请重新下单！");
            		window.location.href='http://www.5173.com/';
            	}
            	// 显示金币名称
            	$(".box_price").html(goodsInfo_createOrder.moneyName);
            	
            	// 根据最近订单，自动补充收货信息
            	var lastOrder = resp.orderInfoEO;
            	// redis中含有该游戏的收获信息
            	if(!isNull(lastOrder)){
            		// 自动填充订单收获信息
            		$("[name='txtPhone']").val(lastOrder.mobileNumber);
					$("[name='txtQq']").val(lastOrder.qq);
					$("[name='txtLevel']").val(lastOrder.gameLevel);
            	}
                if (!isNull(resp.orderInfoEOs)) {
                    createReceivingRoles(resp);
                } else {
                    // 没有历史订单的，触发“使用其他收货角色名”功能
                    $("#radioOtherReceivingRole").bind('click', changeReceivingRole);
                    $("#radioOtherReceivingRole").trigger('click');
                    $("#p_other_receiving_role").hide();
                    $("#ReceivingRole").hide();
                }
                


            }
		}
	});
	
	
	
	// 补充订单游戏信息
	$(".box_word").html(gameName_createOrder+"/&nbsp;"+gameRegion_createOrder+"/&nbsp;"+gameServer_createOrder);
	if(!isNull(gameRace_createOrder)){
		$(".box_word").append("/&nbsp;"+gameRace_createOrder);
	}

	if (goodsCat_createOrder==1 || goodsCat_createOrder==3){
		$(".box_word").append("/&nbsp; 单价:1元="+toDecimal2(1 / parseFloat(goodsInfo_createOrder.unitPrice))+goodsInfo_createOrder.moneyName+" /&nbsp; 库存:"+capacity+goodsInfo_createOrder.moneyName);
	}else{
		$(".box_word").append("/&nbsp; 单价:1元="+toDecimal2(1 / parseFloat(goodsInfo_createOrder.unitPrice))+goodsInfo_createOrder.moneyName+" /&nbsp; 库存:充足");
	}
	
	if(gameName_createOrder == "地下城与勇士"){
		var html = "<div class='tips_sharp_box' style='width:700px;left:0;' >";
			html += "<div class='content_tips sharp_bottom'>";		
			html += "友情提示：<br>";
			html += "1.请正确填写您的收货角色名并选择正确的区服，如果因为上述两个原因错误导致的问题，损失由买家自行承担。<br>";
			html += "2.由于DNF邮寄上限问题，您购买的订单可能分多次邮寄给您，请耐心等待。<br>";
			html += "<b><font color='red'>3.如您的角色名有特殊符号或者其它无法正确输入的情况，请查看<a href='http://aid.5173.com/js/buyer/gmsp/951.html' target='_blank' class='fblue'>《DNF如何复制角色名》</a>获取帮助。</font></b><br>";
            html += "4.角色名前后请不要带空格，提交时会自动去掉，如果因为该原因导致的问题，损失由买家自行承担。<br>";
            html += "5.商城订单无需填写游戏等级，如果因为该原因导致的问题，损失由买家自行承担。<br>";
            html += "6.因游戏官方问题，邮件会出现延时到账情况，请您退出游戏后重新上号查收。<br/>";
            html += "</div>";
			html += "<div class='bottom' style='width:700px;'><span class='left'></span><span class='right' style='width:667px;'></span></div>";
			html += "</div>";			
			$(".append").append(html);
	}else if(gameName_createOrder == "剑灵"){
		var html =  "<div class='fill_info' style='left:0;' >";	
			html += "<div class='fanbox'>";		
			html += "<span id='PurchaseOrderNew1_BuyModeInfo1_spanIcon' class='arrow_need' style='left:35px;'></span>";
			html += "<div class='pushbox_g'>";
			html += "<div class='pushboxmain'>";
			html += "<p id='PurchaseOrderNew1_BuyModeInfo1_pClewFee'>温馨提示：<br/>";
			html += "①任何一个以<b><font color='red'>黑货</font></b>或<b><font color='red'>封号</font></b>或<b><font color='red'>返利</font></b>等其它理由取回交易商品的人都是骗子，谨防被骗。<br/>"
			html += "②购买订单后您只需在交易地点等待工作人员主动交易给您货物，请勿回答任何人的问题。</br> ";
			html += "③游戏内询问您订单信息的全部是骗子，更加不要点击别人发给您的网站或者QQ邮件，均是骗子。</br> ";
			html += "④游戏内不存在任何5173工作人员.不要在游戏内泄漏自己的QQ号码或者手机号码 。</br> ";
			html += "交易成功以后,请不要理会任何来联系您的人,有任何疑问请马上通过联系QQ客服。</br> ";
			html += "</p>";		
			html += "</div>";	
			html += "</div>";
			html += "</div>";
			html += "</div>";
		$(".append").append(html);
	}else if(gameName_createOrder == "QQ三国"){
		var html =  "<div class='tips_sharp_box' style='width:700px;left:0;' >";
			html += "<div class='content_tips sharp_bottom'>友情提示：<br>";		
			html += "1.该游戏交易地点<b><font color='red'>自称工作人员的骗子</font></b>众多，谨防被骗。<br>";
			html += "2.购买订单后您只需在交易地点等待工作人员主动交易给您货物，<b><font color='red'>请勿回答任何人的问题</font></b>。<br>";
			html += "3.任何一个以<b><font color='red'>黑货</font></b>或<b><font color='red'>其它理由</font></b>取回交易商品的人都是骗子，谨防被骗。<br>";
			html += "</div>";
			html += "<div class='bottom'>";
			html += "<span class='left'></span>";		
			html += "<span class='right'></span>";	
			html += "</div>";
			html += "</div>";
		$(".append").append(html);
	}else if(gameName_createOrder == "新寻仙"){
		var html =  "<div class='tips_sharp_box' style='left:0;' >";	
			html += "<div class='content_tips sharp_bottom'>友情提醒：";		
			html += "1.请您到 跨服交易线 交易集市 坐标:【630，630】 等待交易";
			html += "2.该游戏交易地点<strong><font color='red'>自称工作人员的骗子</font></strong>众多，谨防被骗。";
			html += "3.购买订单后您只需在交易地点等待工作人员主动交易给您货物，<strong><font color='red'>请勿回答任何人的问题</font></strong>。";
			html += "4.任何一个以<strong><font color='red'>黑货</font></strong>或<strong><font color='red'>其他理由</font></strong>取回交易商品的人都是骗子，谨防被骗。";
			html += "</div>";
			html += "<div class='bottom'>";		
			html += "<span class='left'></span>";	
			html += "<span class='right'></span>";
			html += "</div>";
			html += "</div>";
		$(".append").append(html);
	}else if(gameName_createOrder == "魔兽世界(国服)"){
		var html =  "<div class='tips_sharp_box' style='width:700px;left:0;' >";
		html += "<div class='content_tips sharp_bottom'>友情提醒：</br>";
		html += "1.游戏里如有卖家出现<strong><font color='red'>代练、代打、让您输入/script开头的命令（利用游戏漏洞诈骗）</font></strong>或者交易完成后<strong><font color='red'>以洗黑金、截图为借口</font></strong>向您取回交易物品的都是骗子，谨防被骗。</br>";
		html += "2.购买订单后您只需在交易地点等待工作人员主动交易给您货物，<strong><font color='red'>请勿回答任何人的问题</font></strong>。<br/>";
        html += "3.尊敬的买家，由于魔兽世界游戏币交易的特殊性，建议您在<strong><font color='red'>下线之前使用完所购买的游戏币</font></strong>，以免造成不必要的损失。</br>";
		html += "4.<strong><font color='red'>凡用户购买商城魔兽世界游戏币，3天内游戏币被找回或者被官方没收等，一经查证属实，均由卖家全额赔款</font></strong>。</br>";
		html += "</div>";
		html += "<div class='bottom'>";
		html += "<span class='left'></span>";
		html += "<span class='right'></span>";
		html += "</div>";
		html += "</div>";
	    $(".append").append(html);
	} else if (gameName_createOrder == "剑侠情缘Ⅲ") {
        var html =  "<div class='tips_sharp_box' style='width:700px;left:0;' >";
        html += "<div class='content_tips sharp_bottom'>友情提醒：</br>";
        html += "1.为加快交易速度，订单支付成功后，请您在<strong><font color='red'>扬州城西面交易区<信使></font></strong>处等待物服员与您交易。</br>";
        html += "2.购买订单后您只需在交易地点等待工作人员主动交易给您货物，<strong><font color='red'>请勿回答任何人的问题</font></strong>。</br>";
        html += "3.任何一个以<strong><font color='red'>黑货</font></strong>或<strong><font color='red'>其它理由</font></strong>取回交易商品的人都是骗子，谨防被骗。</br>";
        html += "4.请正确填写您的收货角色名并选择正确的区服，如果因为上述两个原因错误导致的问题，损失由买家自行承担。</br>";
        html += "</div>";
        html += "<div class='bottom'>";
        html += "<span class='left'></span>";
        html += "<span class='right'></span>";
        html += "</div>";
        html += "</div>";
        $(".append").append(html);
    } else if (gameName_createOrder == "疾风之刃") {
        var html =  "<div class='tips_sharp_box' style='width:700px;left:0;' >";
        html += "<div class='content_tips sharp_bottom'>友情提醒：</br>";
        html += "1. 拍卖交易，，请您将挂好的物品截图发给您的接手客服，游戏收取5%的手续费需要买家您承担。</br>";
		html += "2. 该游戏存在携带上限，可点击<strong><font color='red'><a href='http://aid.5173.com/db/tiro/1675.html' target='_blank'>【疾风之刃携带上限表】</a></font></strong>查看具体携带金额，如携带上限请及时告知客服，防止邮件金额过多无法取出。</br>";
        html += "3. 拍卖详细请见说明<a href='http://aid.5173.com/sc/jbsc/1787.html' target='_blank'>http://aid.5173.com/sc/jbsc/1787.html</a>"
		html += "</div>";
        html += "<div class='bottom'>";
        html += "<span class='left'></span>";
        html += "<span class='right'></span>";
        html += "</div>";
        html += "</div>";
        $(".append").append(html);
    } else if (gameName_createOrder == "斗战神") {
        var html =  "<div class='tips_sharp_box' style='width:700px;left:0;' >";
        html += "<div class='content_tips sharp_bottom'>友情提醒：</br>";
        html += "1. 商品默认以邮寄方式发送给您，邮寄交易和当面交易游戏运营商都收取<strong><font color='red'>3%</font></strong>的手续费<strong><font color='red'>（由买家承担）。</font></strong></br>";
        html += "</div>";
        html += "<div class='bottom'>";
        html += "<span class='left'></span>";
        html += "<span class='right'></span>";
        html += "</div>";
        html += "</div>";
        $(".append").append(html);
    } else if (gameName_createOrder == "龙之谷") {
        var html =  "<div class='tips_sharp_box' style='width:700px;left:0;' >";
        html += "<div class='content_tips sharp_bottom'>友情提醒：</br>";
        html += "1. 游戏币交易，游戏运营商收取<strong><font color='red'>n*0.3%</font></strong>的手续费<strong><font color='red'>（由买家承担）。</font></strong></br>";
        html += "注：n=游戏币数量 单位：金<br/> ";
        html += "<strong>为快速交易，请您支付后及时加接手客服QQ进行交易，感谢您的配合。</strong>";
        html += "</div>";
        html += "<div class='bottom'>";
        html += "<span class='left'></span>";
        html += "<span class='right'></span>";
        html += "</div>";
        html += "</div>";
        $(".append").append(html);
    } else if (gameName_createOrder == "天涯明月刀") {
		var html =  "<div class='tips_sharp_box' style='width:700px;left:0;' >";
		html += "<div class='content_tips sharp_bottom'>友情提醒：</br>";
		html += "1. 为加快交易速度，订单支付成功后，请您确保没有在队伍状态下前往主线<strong><font color='red'>杭州拍卖行附近邮差【杭州(1844,458)】</font></strong>处等待交易。</br>";
		html += "2. <strong><font color='red'>20级</font></strong>以下角色不可进行交易，建议您升到<strong><font color='red'>20级</font></strong>以上进行购买。<br/> ";
		html += "3. 游戏不支持邮寄,请您上游戏交易。<br/>";
		html += "4、商家会采用等价或拍卖方式交易，请配合商家进行交易，如果出现被劫金风险，将由商家承担处理，确保您安全无忧的买到金币。（<strong><font color='red'>拍卖行所产生的手续费由买家自行承担。</font></strong>） <br/>";
		html += "5. <a href='http://aid.5173.com/sc/jbsc/1780.html' target='_blank'>天涯明月刀收购和拍卖估价交易步骤说明。</a>";
		html += "</div>";
		html += "<div class='bottom'>";
		html += "<span class='left'></span>";
		html += "<span class='right'></span>";
		html += "</div>";
		html += "</div>";
		$(".append").append(html);
	}

    // 如果是魔兽世界(国服)，则显示交易方式
    if (gameName_createOrder == "魔兽世界(国服)" || gameId_createOrder == "880") {
        $("#TradeTypeDiv").show();
        $("#Level").show();
    } else if (gameName_createOrder == "疾风之刃") {
        $("#Level").show();
    } else {
        $("#Level").hide();
    }
	
	
	
	// 实时监控，input金币数的变化，以及时生成价格
	$("[name='txtReceivingCount']").bind('input propertychange', function() {
		// 当前购买金币数
		countOrderAmount();
		/*var currentGoldCount = $(this).val();
		
		if(isNull(currentGoldCount)){
			return;
		}
		
		if(!isNumber(currentGoldCount)){
			alert("请输入数字");
			$(this).val(0);
			currentGoldCount = 0;
		}
		
		
		// 数量不超过99999999
		if(parseInt(currentGoldCount)>=99999999){
			currentGoldCount = 99999999;
			$(this).val(99999999);
		}
		
		var disCountList = goodsInfo_createOrder.discountList;
		goodsDiscount_createOrder = 1;
		if(!isNull(disCountList)){
			for(var i=0; i<disCountList.length; i++){
				if(currentGoldCount >= disCountList[i].goldCount){
					goodsDiscount_createOrder = disCountList[i].discount;
				}
			}
		}
		/!*----------------------------------------------------------------------------------------------------------------------------------*!/
		var buyPiccOrNot = $(':radio[name="picc"]:checked').val();
		var piccFee = 0;
		piccFee = parseFloat(goodsInfo_createOrder.unitPrice) * parseInt(currentGoldCount) * parseFloat(goodsDiscount_createOrder) * rate / 100;
		if (piccFee < floor){piccFee = floor;}
		if (piccFee > ceiling){piccFee = ceiling;}
		$("#piccFeeDisplay").html(toDecimal2(piccFee));
		if (buyPiccOrNot == "buyPicc"){
			var totalPrice = toDecimal2(parseFloat(goodsInfo_createOrder.unitPrice) * parseInt(currentGoldCount) * parseFloat(goodsDiscount_createOrder) + piccFee);
		}else{
			var totalPrice = toDecimal2(parseFloat(goodsInfo_createOrder.unitPrice) * parseInt(currentGoldCount) * parseFloat(goodsDiscount_createOrder));
		}
		/!*----------------------------------------------------------------------------------------------------------------------------------*!/
		var totalPriceArray = totalPrice.toString().split(".");
		$("#spamprice").html("<strong>"+totalPriceArray[0]+"</strong>."+totalPriceArray[1]+"元");*/
	});
	
	// 每次加载，自动触发显示价格
	//$("[name='txtReceivingCount']").trigger("input");
	
	// 添加QQ点击绑定事件
	$(".chose_service_qq").click(function() {
		// 客服详细信息
		var servicerInfo = $.evalJSON($("#servicerId_"+selectServicerId).attr("value"));
		if(isNull(servicerInfo) || isNull(servicerInfo.qq)){
			return;
		}
		window.open("http://wpa.qq.com/msgrd?v=3&uin="+servicerInfo.qq+"&site=qq&menu=yes");
	});
});
/**
 * 创建收货角色名列表
 * @param resp
 */
function createReceivingRoles(resp) {
	var orders = resp.orderInfoEOs;
	if (!isNull(orders)) {
        var receivers = [];
        var html = "";
		for (var i = 0, j = orders.length; i < j; i++) {
			var order = orders[i];

            // 避免多次创建同个收货角色
            var created = false;
            for (var ii = 0, jj = receivers.length; ii < jj; ii++) {
                if (receivers[ii] == order.receiver) {
                    created = true;
                    break;
                }
            }
            if (!created) {
                receivers.push(order.receiver);
                var id = "radioReceivingRole_" + i;

                html += "   <li>";
                html += '       <input type="radio" id="' + id + '" name="radioReceivingRole" value="' + order.receiver + '"/>';
                html += '       <label for="' + id + '" title="'+ order.receiver +'">' + order.receiver + '</label>';
                html += '   </li>';

                if (receivers.length % 3 == 0) {
                    html = '<li><ul class="rolesNewline">' + html + '</ul></li>';
                    $(html).insertBefore(".receivingRoles>ul>li:last");
                    html = "";
                }

            }
		}
        if (html != "") {
            html = '<li><ul class="rolesNewline">' + html + '</ul></li>';
            $(html).insertBefore(".receivingRoles>ul>li:last");
        }
	} else {
		var lastOrder = resp.orderInfoEO;
		if (!isNull(lastOrder)) {
			$("[name='txtReceivingRole']").val(lastOrder.receiver);
			$("[name='txtSureReceivingRole']").val(lastOrder.receiver);
		}
	}

	// 清除firefox历史选中“使用其他角色名”选项
	if ($("#radioOtherReceivingRole").prop("checked")) {
		$("#radioOtherReceivingRole").prop("checked", false);
	}

	// 添加事件
	$(":radio[name='radioReceivingRole']").bind('click', changeReceivingRole);
}
/**
 * 改变收货角色名事件
 */
function changeReceivingRole() {
    var value = $(this).val();
    if (value != -1) {
        // 使用历史收货角色名
        $(".otherReceivingRole").hide();
        $("#SureReceivingRole").hide();
    } else {
        // 使用其他收货角色名
        $(".otherReceivingRole").show();
        $("#SureReceivingRole").show();
    }
}
//改变购买方式
function changeBuyWay(){
	var buyWay = $(':radio[name="buyway"]:checked').val();
	if(buyWay=='buyyuan'){
		//重新填充购买数
		$("[name='txtReceivingCount']").attr("value", 100);
		// 实时监控，input金币数的变化，以及时生成价格
		//$("[name='txtReceivingCount']").bind('input propertychange', function() {
			// 当前购买数
		//	countOrderAmount();
			/*var currentGoldCount = $(this).val();
            var insuranceAmount = toDecimal2(currentGoldCount*rate/100);
			$("#piccFeeDisplay").html(insuranceAmount);
			if(insuranceAmount<floor){
				$("#piccFeeDisplay").html(toDecimal2(floor));
			}
			if(insuranceAmount>ceiling){
				$("#piccFeeDisplay").html(toDecimal2(ceiling));
			}
			
			if(isNull(currentGoldCount)){
				return;
			}

			if(!isNumber(currentGoldCount)){
				alert("请输入数字");
				$(this).val(0);
				currentGoldCount = 0;
			}
			
			// 数量不超过99999999
			if(parseInt(currentGoldCount)>=99999999){
				currentGoldCount = 99999999;
				$(this).val(99999999);
			}
			
			var disCountList = goodsInfo_createOrder.discountList;
			goodsDiscount_createOrder = 1;
			if(!isNull(disCountList)){
				for(var i=0; i<disCountList.length; i++){
					if(currentGoldCount >= disCountList[i].goldCount){
						goodsDiscount_createOrder = disCountList[i].discount;
					}
				}
			}
			var price = toDecimal2(1/parseFloat(goodsInfo_createOrder.unitPrice));
			var totalPrice = toDecimal2(parseInt(currentGoldCount)/parseFloat(goodsInfo_createOrder.unitPrice)* parseFloat(goodsDiscount_createOrder));
			var totalPrice = Math.ceil(totalPrice);
			$("#spamprice").html("<strong>"+totalPrice+"</strong>"+goodsInfo_createOrder.moneyName);*/
		//});
		// 每次加载，自动触发显示价格
		$("[name='txtReceivingCount']").trigger("input");
		$("#YXBcount").html("购买数量：");
		$("#buycount").html("购买金额：");
		$(".box_price").html("元");
	}else{
		//重新填充购买金币数
		$("[name='txtReceivingCount']").attr("value", goldCount_createOrder);
		// 实时监控，input金币数的变化，以及时生成价格
		//$("[name='txtReceivingCount']").bind('input propertychange', function() {
			// 当前购买金币数
			//countOrderAmount();
			/*var currentGoldCount = $(this).val();
			
			if(isNull(currentGoldCount)){
				return;
			}
			
			if(!isNumber(currentGoldCount)){
				alert("请输入数字");
				$(this).val(0);
				currentGoldCount = 0;
			}
			
			
			// 数量不超过99999999
			if(parseInt(currentGoldCount)>=99999999){
				currentGoldCount = 99999999;
				$(this).val(99999999);
			}
			
			var disCountList = goodsInfo_createOrder.discountList;
			goodsDiscount_createOrder = 1;
			if(!isNull(disCountList)){
				for(var i=0; i<disCountList.length; i++){
					if(currentGoldCount >= disCountList[i].goldCount){
						goodsDiscount_createOrder = disCountList[i].discount;
					}
				}
			}
			/!*----------------------------------------------------------------------------------------------------------------------------------*!/
			var buyPiccOrNot = $(':radio[name="picc"]:checked').val();
			var piccFee = 0;
			piccFee = parseFloat(goodsInfo_createOrder.unitPrice) * parseInt(currentGoldCount) * parseFloat(goodsDiscount_createOrder) * rate / 100;
			if (piccFee < floor){piccFee = floor;}
			if (piccFee > ceiling){piccFee = ceiling;}
			$("#piccFeeDisplay").html(toDecimal2(piccFee));
			if (buyPiccOrNot == "buyPicc"){
				var totalPrice = toDecimal2(parseFloat(goodsInfo_createOrder.unitPrice) * parseInt(currentGoldCount) * parseFloat(goodsDiscount_createOrder) + piccFee);
			}else{
				var totalPrice = toDecimal2(parseFloat(goodsInfo_createOrder.unitPrice) * parseInt(currentGoldCount) * parseFloat(goodsDiscount_createOrder));
			}
			/!*----------------------------------------------------------------------------------------------------------------------------------*!/
			var totalPriceArray = totalPrice.toString().split(".");
			$("#spamprice").html("<strong>"+totalPriceArray[0]+"</strong>."+totalPriceArray[1]+"元");*/
		//});
		// 每次加载，自动触发显示价格
		$("[name='txtReceivingCount']").trigger("input");
		$("#YXBcount").html("应付款：");
		$("#buycount").html("购买数量：");
		$(".box_price").html(goodsInfo_createOrder.moneyName);
	}

	// 清除错误提示信息
	$("#txtReceivingCount").removeClass();
	$("#txtReceivingCount").addClass("normal_box_140 f18");
	$("#spCountOk").hide();
	$("#xCount").hide();
}
/**
 * 计算订单金额
 * @returns {number}
 */
function countOrderAmount() {
	var countEl = $(":input[name='txtReceivingCount']");
	var count = countEl.val();
	if (isNull(count)) {
		return 0;
	}
	if(!isNumber(count)){
		alert("请输入数字");
		countEl.val(0);
		return 0;
	}
	// 数量不超过99999999
	if(parseInt(count)>=99999999){
		count = 99999999;
		countEl.val(99999999);
	}

	var buyPiccOrNot = $(':radio[name="picc"]:checked').val();
	var buyWay = $(':radio[name="buyway"]:checked').val();
	if (buyWay == 'buycount') {
		// 按数量购买

		// 计算视频收费客服费用
		var serviceCharge = 0;
		if(isNull(selectServicerId)){
			//alert("请先选择为你服务的客服");
		} else {
			var servicerInfo = $.evalJSON($("#servicerId_"+selectServicerId).attr("value"));
			serviceCharge = servicerInfo.serviceCharge;
			//console.log("serviceCharge="+serviceCharge);
		}

		// 计算购买保险费用
		var piccFee = parseFloat(goodsInfo_createOrder.unitPrice) * parseInt(count) * rate / 100;
		if (piccFee < floor){piccFee = floor;}
		if (piccFee > ceiling){piccFee = ceiling;}
		if (buyPiccOrNot == "buyPicc"){
			$("#piccFeeDisplay").html(toDecimal2(piccFee));
			totalPrice = parseFloat(goodsInfo_createOrder.unitPrice) * parseInt(count)  + piccFee;
		}else{
			$("#piccFeeDisplay").html("0.00");
			totalPrice = parseFloat(goodsInfo_createOrder.unitPrice) * parseInt(count);
		}
		totalPrice += serviceCharge;
		totalPrice = toDecimal2(totalPrice);
		var totalPriceArray = totalPrice.toString().split(".");

		var $total = "<strong>"+totalPriceArray[0]+"</strong>."+totalPriceArray[1]+"元";
		if (buyPiccOrNot == "buyPicc") {
			$total += "&nbsp;(包含商业保障险：" + toDecimal2(piccFee) + "元)";
		}

		$("#spamprice").html($total);
	} else {
		var piccFee = toDecimal2(count*rate/100);
		if (buyPiccOrNot == "buyPicc") {
			$("#piccFeeDisplay").html(piccFee);
			if(piccFee<floor){
				piccFee = toDecimal2(floor);
				$("#piccFeeDisplay").html(piccFee);
			}
			if(piccFee>ceiling){
				piccFee = toDecimal2(ceiling);
				$("#piccFeeDisplay").html(piccFee);
			}
		} else {
			$("#piccFeeDisplay").html("0.00");
		}

		// 按元购买
		var totalCount = toDecimal2(parseInt(count)/parseFloat(goodsInfo_createOrder.unitPrice));
		var totalCount = Math.ceil(totalCount);
		var $total = "<strong>"+totalCount+"</strong>"+goodsInfo_createOrder.moneyName;
		if (buyPiccOrNot == "buyPicc") {
			$total += "&nbsp;(包含商业保障险：" + piccFee + "元)";
		}
		$("#spamprice").html($total);
	}
}
// 提交订单
function submitOrder(){
    var buyWay = $(':radio[name="buyway"]:checked').val();
	if(isNull(selectServicerId)){
		alert("请选择为你服务的客服");
		return;
	}

	// 如果是魔兽世界(国服)，则显示交易方式
	if(gameName_createOrder == "魔兽世界(国服)" || gameId_createOrder == "880") {
		if ($(":radio[name='tradeType']:checked").length == 0) {
			alert("请选择交易方式！");
			return;
		}
	}
	if (!checkCount()) {
		return;
	}
	if(isNull($("[name='txtLevel']").val())&&(gameName_createOrder == "魔兽世界(国服)" || gameId_createOrder == "880")){
		alert("请输入角色等级！");
		return;
	}
	if($("[name='txtLevel']").val()<=0&&(gameName_createOrder == "魔兽世界(国服)" || gameId_createOrder == "880")){
		alert("角色等级不能小于1！");
		return;
	}
    if(isNull($("[name='txtLevel']").val())&&(gameName_createOrder == "疾风之刃")){
        alert("请输入角色等级！");
        return;
    }


	var receiver; // 收货角色名
	var radioReceivingRole = $(":radio[name='radioReceivingRole']:checked");
	if (radioReceivingRole.length == 0) {
		// 未选择收货角色名
		alert("收货角色名不能为空！");
		return;
	} else {
		var v = radioReceivingRole.val();
		if (v == -1) {
			// 使用其他收货角色名
			var txtReceivingRole = $("#txtReceivingRole").val();
			if ($.trim(txtReceivingRole) == "") {
				alert("收货角色名不能为空！");
				return;
			} else {
				var txtSureReceivingRole = $(":input[name='txtSureReceivingRole']").val();
				if ($.trim(txtSureReceivingRole) == '') {
					alert("请再次输入收货角色名！");
					return;
				} else {
					if (txtReceivingRole != txtSureReceivingRole) {
						alert("2次输入的收货角色名不一致，请重新输入。");
						return;
					}
				}
				receiver = txtSureReceivingRole;
			}
		} else {
			// 使用历史收货角色名
			receiver = radioReceivingRole.val();
		}
	}

	if ($.trim($("[name='txtPhone']").val()) == '') {
		alert("联系电话不能为空！");
		return;
	} else {
        if (!checkPhone()) {
            return;
        }
    }

	if ($.trim($("[name='txtQq']").val()) == '') {
		alert("联系QQ不能为空！");
		return;
	}

	var addOrderRequest = {};
	addOrderRequest.servicerId = selectServicerId;
	addOrderRequest.mobileNumber = $("[name='txtPhone']").val();
	addOrderRequest.gameLevel = $("[name='txtLevel']").val();
	addOrderRequest.qq = $("[name='txtQq']").val();
	addOrderRequest.receiver = receiver;
	addOrderRequest.goodsId = goodsInfo_createOrder.id;
	addOrderRequest.gameName = gameName_createOrder;
	addOrderRequest.region = gameRegion_createOrder;
	addOrderRequest.server = gameServer_createOrder;
	addOrderRequest.gameRace = gameRace_createOrder;
	addOrderRequest.gameId = gameId_createOrder;
	addOrderRequest.regionId = regionId_createOrder;
	addOrderRequest.serverId = serverId_createOrder;
	addOrderRequest.raceId = raceId_createOrder;
	addOrderRequest.title = goodsInfo_createOrder.title;
	addOrderRequest.goodsCat = goodsCat_createOrder;
	addOrderRequest.sellerLoginAccount = goodsInfo_createOrder.sellerLoginAccount;
	if(insuranceSettings==null || insuranceSettings.enable==false || $(':radio[name="picc"]:checked').val()=='notBuyPicc'){
		addOrderRequest.isBuyInsurance = false;
	}
	if($(':radio[name="picc"]:checked').val()=='buyPicc'){
		addOrderRequest.isBuyInsurance = true;
	}
	

	if(buyWay=="buyyuan"){
		addOrderRequest.goldCount = $(".service_price strong").html();
	}else{
		addOrderRequest.goldCount = $("[name='txtReceivingCount']").val();
	}
	addOrderRequest.unitPrice = goodsInfo_createOrder.unitPrice;
	if(goodsInfo_createOrder.deliveryTime==null || goodsInfo_createOrder.deliveryTime==''){
		addOrderRequest.deliveryTime = 20;
	}else{
		addOrderRequest.deliveryTime = goodsInfo_createOrder.deliveryTime;
	}
	addOrderRequest.discount = goodsDiscount_createOrder;
	addOrderRequest.moneyName = goodsInfo_createOrder.moneyName;
	addOrderRequest.refererType = refererType;
	addOrderRequest.internetBar = internetBar;

	// 如果是魔兽世界(国服)，则提交交易方式
	if(gameName_createOrder == "魔兽世界(国服)" || gameId_createOrder == "880") {
		addOrderRequest.tradeType = $(":radio[name='tradeType']:checked").val();
	}

	$.ajax({
		type: "POST",
		url: baseServiceUrl + "services/order/addorder",
		data: $.toJSON(addOrderRequest),
		contentType: "application/json; charset=UTF-8",
		dataType: "json",
		beforeSend: function (request){
            request.setRequestHeader("5173_authkey", getAuthkey());
        },
		success: function(resp) {
			var responseStatus = resp.responseStatus;
            var code = responseStatus.code;
            
           /* if(code == "80014"){
            	alert("当前库存不足，请重新填写购买数量或者返回重新购买。");
            	window.location.reload();
            }*/
            
            if (code == "00") {
            	var orderInfo = resp.orderInfo;
            	// 请求支付
            	window.location.href = baseServiceUrl + "payment?orderId="+orderInfo.orderId;
            }
		}
	});
}
//搜索客服
function searchServicer(servicerList){
	selectServicerId=null;//取消选择的客服
	var search_servicer = $.trim($("#search").val());
	var search_falg=true;
	if(isNull(search_servicer)){
		$(".chose_service_block").remove();
		buildServicerHtml(servicerList);
		search_falg=false;
	}
	if(!isNull(search_servicer)){
		var rexStr = new RegExp(".*?"+search_servicer+".*?");
		var selectServiceList = new Array();
		var j =0;
		for(var i =0;i<servicerList.length;i++){
			if (rexStr.test(servicerList[i].nickName)||rexStr.test(servicerList[i].qq)){
				search_falg=false;
				selectServiceList[j] = servicerList[i];
				j++;
//				$(".chose_servie_ul li:not(.li1)").css("display","none");
//				var servicer = servicerList[i];
//				var servicerJson = $.toJSON(servicer);
//				var avatar = isNull(servicer.avatarUrl)?"img/head.png":buildImageUrl(servicer.avatarUrl,"64x64");
//				var name = isNull(servicer.nickName)?"公主":servicer.nickName;
//				var sign = isNull(servicer.sign)?"心随你动 服务在心":servicer.sign;
//				
//				var html = "<div id='servicerId_"+servicer.id+"' class='chose_service_block' value='"+servicerJson+ "'" +
//						"onclick='javascript:selectServicer("+servicer.id+")'>";
//				html += "<div class='chose_service_img'><img src='"+avatar+"' /></div>";
//				html += "<div class='chose_service_infor'>";
//				html += "<div class='chose_service_name'>客服：<strong>"+name+"</strong></div>";
//				html += "<div class='chose_serive_word'>"+sign+"</div>";
//				html += "</div></div>";
//				$(".chose_service_list").html(html);
//				// 鼠标移动到客服上，添加鼠标事件，自动选择客服
//				$(".chose_service_block").click(function(){
//						$(this).trigger("click");
//					});
			}
		}
		if(search_falg){
			alert("您搜索的客服不存在，请输入正确的客服昵称！");
		}else {
			$(".chose_service_block").remove();
			buildServicerHtml(selectServiceList);
		}
	}
}

// 组装客服html
function buildServicerHtml(servicerList){
	$(".service_box").css("display","none");
	if(isNull(servicerList)){
		return;
	}
	var servicer_pages = Math.ceil(servicerList.length/8);
	var html_pages='';
	for(var i=1;i<=servicer_pages;i++){
		html_pages+='<li onmouseover="onmover(this)" onmouseout="onmout(this)" style="padding:0px;background-color:#999fff;"class="li'+i+'"><a style="border:0px;"><font color="white">'+i+'</font></a></li>';
	}
	$(".chose_servie_ul").html(html_pages);
	for(var i=0; i<servicerList.length&&i<8; i++){
		var servicer = servicerList[i];
		// 该客服被禁用，不显示
		if(servicer.isDeleted == true){
			continue;
		}
		var servicerJson = $.toJSON(servicer);
		var avatar = isNull(servicer.avatarUrl)?"img/head.png":buildImageUrl(servicer.avatarUrl,"64x64");
		var name = isNull(servicer.nickName)?"公主":servicer.nickName;
		var sign = isNull(servicer.sign)?"心随你动 服务在心":servicer.sign;
		var serviceCharge = isNull(servicer.serviceCharge)?0:servicer.serviceCharge;
		var goodRate=isNull(servicer.goodRate)?100:servicer.goodRate;

		var html = "<div id='servicerId_"+servicer.id+"' class='chose_service_block' value='"+servicerJson+ "'" +
				"onclick='javascript:selectServicer("+servicer.id+")'>";
		html += "<div class='chose_service_img'><img src='"+avatar+"' /></div>";
		html += "<div class='chose_service_infor'>";
		html += "<div class='chose_service_name'>客服：<strong>"+name+"</strong></div>";
		html += "<div class='chose_serive_word'>"+sign+"</div>";
		html += "</div>";
		html += "<div class='service_fee'>";
		html += "<div class='service_fee_right'>好评率：<span class='service_fee_span'>"+goodRate+"%</span> 服务费：<span class='service_fee_span'>￥" + serviceCharge + "</span></div>";
		html += "</div>";
		html += "</div>";
		$(".chose_service_list").append(html);
	}
	
	// 鼠标移动到客服上，添加鼠标事件，自动选择客服
	/*$(".chose_service_block").click(function(){
		$(this).trigger("click");
	});*/
	//填充分页客服
		$(".li1").bind("click",function(){
			var html = '';
			$(".chose_service_list").html(html);
			for(var j=0;j<8;j++){
				var servicer = servicerList[j];
				
				var servicerJson = $.toJSON(servicer);
				var avatar = isNull(servicer.avatarUrl)?"img/head.png":buildImageUrl(servicer.avatarUrl,"64x64");
				var name = isNull(servicer.nickName)?"公主":servicer.nickName;
				var sign = isNull(servicer.sign)?"心随你动 服务在心":servicer.sign;
				var goodRate=isNull(servicer.goodRate)?100:servicer.goodRate;
			
				var html = "<div id='servicerId_"+servicer.id+"' class='chose_service_block' value='"+servicerJson+ "'" +
					"onclick='javascript:selectServicer("+servicer.id+")'>";
				html += "<div class='chose_service_img'><img src='"+avatar+"' /></div>";
				html += "<div class='chose_service_infor'>";
				html += "<div class='chose_service_name'>客服：<strong>"+name+"</strong></div>";
				html += "<div class='chose_serive_word'>"+sign+"</div>";
				html += "</div>";
				html += "<div class='service_fee'>";
				html += "<div class='service_fee_right'>好评率：<span class='service_fee_span'>"+goodRate+"%</span> 服务费：<span class='service_fee_span'>￥" + serviceCharge + "</span></div>";
				html += "</div></div>";
				$(".chose_service_list").append(html);
				if(j+1 == servicerList.length){
					break;
				}
			}
			if(!isNull(selectServicerId)){
				selectServicer(selectServicerId);
			}
		});
		$(".li2").bind("click",function(){
			var html = '';
			$(".chose_service_list").html(html);
			for(var j=8;j<16;j++){
				var servicer = servicerList[j];
				
				var servicerJson = $.toJSON(servicer);
				var avatar = isNull(servicer.avatarUrl)?"img/head.png":buildImageUrl(servicer.avatarUrl,"64x64");
				var name = isNull(servicer.nickName)?"公主":servicer.nickName;
				var sign = isNull(servicer.sign)?"心随你动 服务在心":servicer.sign;
				var goodRate=isNull(servicer.goodRate)?100:servicer.goodRate;
			
				var html = "<div id='servicerId_"+servicer.id+"' class='chose_service_block' value='"+servicerJson+ "'" +
					"onclick='javascript:selectServicer("+servicer.id+")'>";
				html += "<div class='chose_service_img'><img src='"+avatar+"' /></div>";
				html += "<div class='chose_service_infor'>";
				html += "<div class='chose_service_name'>客服：<strong>"+name+"</strong></div>";
				html += "<div class='chose_serive_word'>"+sign+"</div>";
				html += "</div>";
				html += "<div class='service_fee'>";
				html += "<div class='service_fee_right'>好评率：<span class='service_fee_span'>"+goodRate+"%</span> 服务费：<span class='service_fee_span'>￥" + serviceCharge + "</span></div>";
				html += "</div></div>";
				$(".chose_service_list").append(html);	
				if(j+1 == servicerList.length){
					break;
				}
			}
			if(!isNull(selectServicerId)){
				selectServicer(selectServicerId);
			}
		});
		$(".li3").bind("click",function(){
			var html = '';
			$(".chose_service_list").html(html);
			for(var j=16;j<24;j++){
				var servicer = servicerList[j];
				
				var servicerJson = $.toJSON(servicer);
				var avatar = isNull(servicer.avatarUrl)?"img/head.png":buildImageUrl(servicer.avatarUrl,"64x64");
				var name = isNull(servicer.nickName)?"公主":servicer.nickName;
				var sign = isNull(servicer.sign)?"心随你动 服务在心":servicer.sign;
				var goodRate=isNull(servicer.goodRate)?100:servicer.goodRate;
			
				var html = "<div id='servicerId_"+servicer.id+"' class='chose_service_block' value='"+servicerJson+ "'" +
					"onclick='javascript:selectServicer("+servicer.id+")'>";
				html += "<div class='chose_service_img'><img src='"+avatar+"' /></div>";
				html += "<div class='chose_service_infor'>";
				html += "<div class='chose_service_name'>客服：<strong>"+name+"</strong></div>";
				html += "<div class='chose_serive_word'>"+sign+"</div>";
				html += "</div>";
				html += "<div class='service_fee'>";
				html += "<div class='service_fee_right'>好评率：<span class='service_fee_span'>"+goodRate+"%</span> 服务费：<span class='service_fee_span'>￥" + serviceCharge + "</span></div>";
				html += "</div></div>";
				$(".chose_service_list").append(html);	
				if(j+1 == servicerList.length){
					break;
				}
			}
			if(!isNull(selectServicerId)){
				selectServicer(selectServicerId);
			}
		});	
		$(".li4").bind("click",function(){
			var html = '';
			$(".chose_service_list").html(html);
			for(var j=24;j<32;j++){
				var servicer = servicerList[j];
				
				var servicerJson = $.toJSON(servicer);
				var avatar = isNull(servicer.avatarUrl)?"img/head.png":buildImageUrl(servicer.avatarUrl,"64x64");
				var name = isNull(servicer.nickName)?"公主":servicer.nickName;
				var sign = isNull(servicer.sign)?"心随你动 服务在心":servicer.sign;
			
				var html = "<div id='servicerId_"+servicer.id+"' class='chose_service_block' value='"+servicerJson+ "'" +
					"onclick='javascript:selectServicer("+servicer.id+")'>";
				html += "<div class='chose_service_img'><img src='"+avatar+"' /></div>";
				html += "<div class='chose_service_infor'>";
				html += "<div class='chose_service_name'>客服：<strong>"+name+"</strong></div>";
				html += "<div class='chose_serive_word'>"+sign+"</div>";
				html += "</div></div>";
				$(".chose_service_list").append(html);	
				if(j+1 == servicerList.length){
					break;
				}
			}
			if(!isNull(selectServicerId)){
				selectServicer(selectServicerId);
			}
		});	
}

// 选择客服
var selectServicerId = null;
function selectServicer(servicerId){
	selectServicerId = servicerId;
	
	// 删除其他客服的选中css
	$(".chose_service_block").removeClass("chose_service_block_chosed");
	$(".span_closed").remove();
	// 设置当前客服选择css
	$("#servicerId_"+servicerId).addClass("chose_service_block_chosed");
	$("#servicerId_"+servicerId+" .chose_service_infor").after("<span class='span_chosed'></span>");
	$(".service_box").css("display","block");
	
	if($("#servicerId_"+servicerId).length <= 0){
		return;
	}
	
	// 补充客服详细信息
	var servicerInfo = $.evalJSON($("#servicerId_"+servicerId).attr("value"));
	
	var avatarUrl = isNull(servicerInfo.avatarUrl)?"img/head.png":buildImageUrl(servicerInfo.avatarUrl,"64x64");
	$("#service_avatar").attr("src", avatarUrl);
	
	var nickName = isNull(servicerInfo.nickName)?"公主":servicerInfo.nickName;
	$(".service_name").html(nickName);
	
	$(".service_word").html("<p>请稍等，即将变身为您发货；如迫切需要救援，务必联系我。</p>");
	
	var qq = isNull(servicerInfo.qq)?"":servicerInfo.qq;
	$(".service_qq").html("QQ：<strong>"+qq+"</strong>");
	
	var phoneNumber = isNull(servicerInfo.phoneNumber)?"":servicerInfo.phoneNumber;
	$(".chose_service_phone").html("手机<strong>"+phoneNumber+"</strong>");
	
	var weiXin = isNull(servicerInfo.weiXin)?"":servicerInfo.weiXin;
	$(".chose_service_wechat").html("微信<strong>"+weiXin+"</strong>");

	var yy = isNull(servicerInfo.yy)?"":servicerInfo.yy;
	if(yy!="")
	{
		$(".chose_service_yy").html("YY<strong>"+yy+"</strong>");
	}
	else
	{
		$(".chose_service_yy").html("");
	}

	countOrderAmount();
}
//改变字体颜色
var i = 0;
function changeColor(){
	$("#wlqx").css("color",i==0?"#06c":"#f60");
	$("#servicerVoteLink").css("color",i==0?"#06c":"#f60");
	i==2?i=0:i++;
}
//客服翻页onmouseover事件
function onmover(obj){
	obj.style.border='1px solid #333';
}
//客服翻页onmouseout事件
function onmout(obj){
	obj.style.border='1px solid #aaa';
}

function buyPiccOrNot(){
	$("[name='txtReceivingCount']").trigger("input");
}
