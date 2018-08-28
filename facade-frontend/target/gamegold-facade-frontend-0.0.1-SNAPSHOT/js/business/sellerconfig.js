/**
 * 卖家订单信息页面
 */
var cacheSellersData = {
	ordersState : null,// 商品状态
	goodsList : null,// 商品列表
	pageSize : 8,// 当前页大小
	start:0,
	orderCreateTime_s:null,
	orderCreateTime_e:null,
	searchOrderId:null,
	orderGameOperator:null,
	orderGameName:null,
	orderRegion:null,
	orderServer:null,
	goodsTypeName:null,
	
	cleanData : function() {
		cacheSellersData.goodsList = null;
	}
};

//var gameOperatorsList = null;
//var gameInfoList = null;
//游戏区缓存
var GameAreaList = null;
//var GameServerList = null;

$(document).ready(function() {
    //选中左侧菜单
    $("#orderyxb").addClass("nonce");

	var sellerOrder = getQueryOrdersRequest();
	querySellerOrder(sellerOrder);
	/**
	 * 点击查询事件
	 */
	$("#doBtnSearch").click(function(){
		
		var orderCreateTime_s = $("#d4311").val();
		var orderCreateTime_e = $("#d4312").val();
		var searchOrderId = $("#searchOrderId").val();
		var orderGameNametext = $("#sygs_name option:selected").text();
		var orderGameNameval =  $("#sygs_name option:selected").val()
		var orderGameArea = $("#sygs_area").val();
		var orderGameServer = $("#sygs_server").val();
		var goodsTypeName = $("#sygs_goodstype").val();


		
		cacheSellersData.orderCreateTime_s = orderCreateTime_s;
		cacheSellersData.orderCreateTime_e = orderCreateTime_e;
		cacheSellersData.searchOrderId = searchOrderId;
		if(orderGameNameval!=''){
			cacheSellersData.orderGameName = orderGameNametext;
		}else{
			cacheSellersData.orderGameName = '';
		}
		cacheSellersData.orderRegion = orderGameArea;
		cacheSellersData.orderServer = orderGameServer;
		cacheSellersData.goodsTypeName = goodsTypeName;
		var sellerOrder = getQueryOrdersRequest();
		querySellerOrder(sellerOrder);		
		return false;
	});
	/**
	 * 点击导出卖家订单
	 */
	$("#exprotsellerorder").click(function(){
		var exporturl = baseServiceUrl+"exportorder?" ;
		if(cacheSellersData.searchOrderId != null){
		exporturl += "searchOrderId="+cacheSellersData.searchOrderId+"&";
		}
		if(cacheSellersData.orderGameName != null){
		exporturl += "orderGameName="+cacheSellersData.orderGameName+"&";
		}
		if(cacheSellersData.orderRegion != null){
		exporturl += "orderRegion="+cacheSellersData.orderRegion+"&";
		}
		if(cacheSellersData.orderServer != null){
		exporturl += "orderServer="+cacheSellersData.orderServer+"&";
		}
		if(cacheSellersData.orderCreateTime_s != null){
		exporturl += "orderCreateTime_s="+cacheSellersData.orderCreateTime_s+"&";
		}
		if(cacheSellersData.orderCreateTime_e != null){
		exporturl += "orderCreateTime_e="+cacheSellersData.orderCreateTime_e+"&";
		}
		if(cacheSellersData.ordersState != null){
		exporturl += "ordersState="+cacheSellersData.ordersState+"&";
		}
		if(cacheSellersData.goodsTypeName != null && cacheSellersData.goodsTypeName != ""){
			exporturl += "goodsTypeName="+cacheSellersData.goodsTypeName+"&";
		}else {
			exporturl += "goodsTypeName=全部&";
		}
		exporturl = exporturl.substr(0,exporturl.length-1);
		window.open(exporturl);	
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
			url: baseServiceUrl + "services/gameinfo/getallcompanies?t="+new Date().getTime(),
			contentType: "application/json; charset=UTF-8",
            dataType: "json",
			beforeSend: function (request){
	            request.setRequestHeader("5173_authkey", getAuthkey());
	        },
	        success : function(resp){
	        	var responseStatus = resp.responseStatus;
	            var code = responseStatus.code; 
	            var gameOperatorsList = resp.companielist;
	            if(code == "00"){
	            if(isNull(gameOperatorsList)){
	            	return;
	            }
	            	//将悬浮框中数据填充
	                    selectOperator.html("");
						$("<option value=''>请选择游戏厂商</option>").appendTo(selectOperator);
						for (var i = 0; i < gameOperatorsList.length; i++) {
							if (gameOperatorsList[i] == -1) {
								continue;
							}
							$("<option value='" + gameOperatorsList[i] + "'>" + gameOperatorsList[i]+ "</option>").appendTo(selectOperator);
						}
	            }
	        },
	        error: function(resp) {
				//console.log(resp);
			}
		});  

	
	selectOperator.change(function(){
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
		if(!selectOperator.data(selectOperatorValue)){
			var queryDiscountRequest ={};
			queryDiscountRequest.companie = selectOperatorValue;
		$.ajax({
			type: "POST",
			url: baseServiceUrl + "services/gameinfo/getgamebycompany?t="+new Date().getTime(),
			contentType: "application/json; charset=UTF-8",
            dataType: "json",
			data: $.toJSON(queryDiscountRequest),
	        success : function(resp){
	        	var responseStatus = resp.responseStatus;
	            var code = responseStatus.code; 
	        	var gameNamesList = resp.gameList;
	        	if(code == "00"){
	        		if(isNull(gameNamesList)){
		            	return;
		            }
	            	//将悬浮框中数据填充
						for (var i = 0; i < gameNamesList.length; i++) {
							$("<option value='" + gameNamesList[i].id + "'>" + gameNamesList[i].name + "</option>").appendTo(selectGamename);
						}
	        		
	        		selectOperator.data(selectOperatorValue, gameNamesList);
	            }
	        },
	        error: function(resp) {
				//console.log(resp);
			}
		});  
		}else{
			 var gameNamesList  = selectOperator.data(selectOperatorValue);
			if (gameNamesList != 0 ) {
				for (var i = 0; i < gameNamesList.length; i++) {
					$("<option value='" + gameNamesList[i].id + "'>" + gameNamesList[i].name + "</option>").appendTo(selectGamename);
				}
    		}
		  }
	 }
		return false;
	});
	//游戏名下拉框值改变
	 selectGamename.change(function(){
		//1.需要获得当前下拉框的值
		var selectGamenameValue = $(this).val();
		 var gameName = $(this).find('option:selected').text();//ZW_C_JB_00008  根据游戏名获取商品类目 ADD
		selectArea.html("");
		selectServer.html("");
		 //新增商品类型查询条件
		 selectGoodsType.html("");
		$("<option value=''>请选择服</option>").appendTo(selectServer);
		 $("<option value=''>请选择区</option>").appendTo(selectArea);
		 $("<option value=''>请选择商品类型</option>").appendTo(selectGoodsType);
		 if (selectGamenameValue != "") {
			     var queryDiscountRequest ={};
				queryDiscountRequest.gameid = selectGamenameValue;
				if(!selectGamename.data(selectGamenameValue)){
				$.ajax({
					type: "POST",
					url: baseServiceUrl + "services/gameinfo/getdetailgame?t="+new Date().getTime(),
					contentType: "application/json; charset=UTF-8",
                    dataType: "json",
					data:$.toJSON(queryDiscountRequest),
			        success : function(resp){
			            	var responseStatus = resp.responseStatus;
				            var code = responseStatus.code; 
				            GameAreaList = resp.gameDetailInfo.gameAreaList;
				        	if(code == "00"){
			            	//将悬浮框中数据填充
			        		if (GameAreaList != 0 ) {
								for (var i = 0; i < GameAreaList.length; i++) {
									$("<option value='" + GameAreaList[i].name + "'>" + GameAreaList[i].name + "</option>").appendTo(selectArea);
								}
			        		} 
			        		selectGamename.data(selectGamenameValue, GameAreaList);
				        	}
			        },
			        error: function(resp) {
						//console.log(resp);
					}
				});  
				}else{
					 var gameAreaList  = selectGamename.data(selectGamenameValue);
					if (gameAreaList != 0 ) {
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
	 * ZW_C_JB_00008_20170515 增加通货类型 update by hyl
	 * @param gameName
	 */
	function getCategoryByGameName(gameName){
		var gameCategoryKey = gameName + ":category";
		selectGoodsType.html("");
		$("<option value=''>请选择商品类型</option>").appendTo(selectGoodsType);

		if(!selectGamename.data(gameCategoryKey)){
			$.ajax({
				type: "GET",
				url: baseServiceUrl + "services/shGameConfig/getAllConfigByGameName",
				contentType: "application/json; charset=UTF-8",
				dataType: "json",
				data:{
					"gameName":gameName
				},
				success : function(resp){
					var responseStatus = resp.responseStatus;
					var code = responseStatus.code;
					GameConfigList = resp.shGameConfigList;
					if(code == "00"){
						//将悬浮框中数据填充
						if (GameConfigList != 0 ) {
							for (var i = 0; i < GameConfigList.length; i++) {
								$("<option value='" + GameConfigList[i].goodsTypeName + "'>" + GameConfigList[i].goodsTypeName + "</option>").appendTo(selectGoodsType);
							}
						}
						selectGamename.data(gameCategoryKey, GameConfigList);
					}
				},
				error: function(resp) {
					console.log(resp);
				}
			});
		}else{
			var GameConfigList  = selectGamename.data(gameCategoryKey);
			if (GameConfigList != 0 ) {
				for (var i = 0; i < GameConfigList.length; i++) {
					$("<option value='" + GameConfigList[i].goodsTypeName + "'>" + GameConfigList[i].goodsTypeName + "</option>").appendTo(selectGoodsType);
				}
			}
		}
	}

	 //游戏区发生变化
	 selectArea.change(function(){
		   //1.需要获得当前下拉框的值
			var selectAreaValue = $(this).val();
			selectServer.html("");
			 $("<option value=''>请选择服</option>").appendTo(selectServer);
			 if (selectAreaValue != "") {
				 if(!selectArea.data(selectAreaValue)){
					 for(var i = 0;i<GameAreaList.length;i++){
						 if(GameAreaList[i].name == selectAreaValue){
							    var serverList = GameAreaList[i].gameServerList;
							    if (serverList != 0 ) {
                                    for (var j= 0; j < serverList.length; j++) {
                                        $("<option value='" + serverList[j].name + "'>" + serverList[j].name + "</option>").appendTo(selectServer);
                                    }
							    }
							    selectArea.data(selectAreaValue, serverList);
						 }
					 }
				 }else{
					 var serverList = selectArea.data(selectAreaValue);
					    if (serverList != 0 ) {
						for (var i = 0; i < serverList.length; i++) {
							$("<option value='" + serverList[i].name + "'>" + serverList[i].name + "</option>").appendTo(selectServer);
						 }
					    }
				 }
				 
			 }
	 })
	 
			
});
function querySellerOrder(sellerOrder){

	//增加商品类目查询条件,当传入为条件为空时,查询全部商品类型的数据
	if(sellerOrder.goodsTypeName == "" || sellerOrder.goodsTypeName == null){
		sellerOrder.goodsTypeName = "全部";
	}
	$.ajax({
		type: "POST",
		url: baseServiceUrl + "services/order/sellerquery?t="+new Date().getTime(),
		data: $.toJSON(sellerOrder),
		contentType: "application/json; charset=UTF-8",
        dataType: "json",
		beforeSend: function (request){
            request.setRequestHeader("5173_authkey", getAuthkey());
        },
        success : function(resp){
        	var responseStatus = resp.responseStatus;
            var code = responseStatus.code; 
            if(code == "00"){
            	var configResult = resp.configResult;
            	if(isNull(configResult)){
            		return;
            	}
        	buildOrderHtml(configResult.data);
            buildPageHtml(configResult.totalCount,configResult.totalPageCount,configResult.currentPageNo,configResult.pageSize);
            }
        },
        error: function(resp) {
			//console.log(resp);
		}
	});
}



function getQueryOrdersRequest() {
		var sellerOrder = {};
		sellerOrder.pageSize = cacheSellersData.pageSize;
		sellerOrder.start = cacheSellersData.start;
		sellerOrder.orderBy = "CREATE_TIME";
		sellerOrder.orderState = cacheSellersData.ordersState;
		sellerOrder.startOrderCreate = cacheSellersData.orderCreateTime_s;
		sellerOrder.endOrderCreate = cacheSellersData.orderCreateTime_e;
		sellerOrder.searchOrderId =cacheSellersData.searchOrderId;
		sellerOrder.gameName =cacheSellersData.orderGameName;
		sellerOrder.region = cacheSellersData.orderRegion;
		sellerOrder.server	= cacheSellersData.orderServer;
		sellerOrder.goodsTypeName	= cacheSellersData.goodsTypeName;
		return sellerOrder;
	}


function buildOrderHtml(sellerOrderList){
	// 清除原有数据
	$(".L-content .listmain").remove();
	
	if(isNull(sellerOrderList)){
		return;
	}
	
	for(var i=0;i<sellerOrderList.length;i++){
		var configResult = sellerOrderList[i];
		var orderInfoEO = configResult.orderInfoEO;
		var servicerInfo = orderInfoEO.servicerInfo;

		//当区或服或游戏信息为空时取订单主单信息
		var gameInfo = configResult.gameName+"/"+configResult.region+"/"+configResult.server;
		if(configResult.gameName == null || configResult.region == null || configResult.server == null){
			gameInfo = orderInfoEO.gameName+"/"+orderInfoEO.region+"/"+orderInfoEO.server;
		}

		var orderId = configResult.orderId;

		var configGoldCount = configResult.configGoldCount;
		var repositoryUnitPrice = configResult.repositoryUnitPrice;
		var totalPrice = toDecimal2(configResult.totalPrice);
		var state = OrderState.getText(orderInfoEO.orderState);
		var lastDate = new Date(configResult.createTime);
		var lastUpdateTime = lastDate.getFullYear() +"年" + (lastDate.getMonth()+1) +"月"+ lastDate.getDate()+"日" + lastDate.getHours() + ":" + lastDate.getMinutes();
		var myGameRace = isNull(orderInfoEO.gameRace)?"":orderInfoEO.gameRace;
		var goodsTypeName = orderInfoEO.goodsTypeName;//增加商品类目信息
		var html = "<div class='listmain'  id='listmain_5' status='151'>";
		html += "<p class='banner'>";
		html += "<span class='tb1'>["+ goodsTypeName +"]</span><span class='tb2'>订单编号："+orderId+"</span><span class='tb3'>创建时间："+lastUpdateTime+"</span>";
		html += "</p>";
		html += "<ul class='u1'style='width:298px'>";
		if(configGoldCount<orderInfoEO.goldCount){
			//拼单（多单）
			html += "<li><strong>游戏:"+gameInfo+"/"+myGameRace+"</strong></li>"; 
		}else{
			html += "<li>游戏:"+gameInfo+"/"+myGameRace+"</li>";
		}
		html += "</ul>";
		html += "<ul class='u2'>";
		html += "<li class='orange'>￥"+totalPrice+"</li>";
		html += "</ul>";
		html += "<ul class='u3'>";
		html += "<li>"+configGoldCount+"</li>";
		html += "</ul>";
		html += "<ul class='u5'>";
		var nickName=""
		var qq="";
		if(servicerInfo!=null&&servicerInfo.nickName!=null){
			nickName=servicerInfo.nickName;
		}
		if(servicerInfo!=null&&servicerInfo.qq!=null){
			qq=servicerInfo.qq;
		}
		html += "<li><span class='p_color3'>"+nickName+"</span></li>";
		html += "<li><a href='javascript:void(0)' class='a_qqtalk' qq='"+qq+"' ></a></li>";
		html += "</ul>";
		html += "<ul class='u5'>";
		html += "<li class='orange'>"+state+"</li>";
		
		html += "</ul>";
		html += "</div>";
		
		$("[class='pagebox beta']").before(html);
	}
	// 客服qq，添加点击事件
	$(".a_qqtalk").click(function(){
		var qqNumber = $(this).attr("qq");
		if(isNull(qqNumber)){
			return;
		}
		window.open("http://wpa.qq.com/msgrd?v=3&uin="+qqNumber+"&site=qq&menu=yes");
	});
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
	
	cacheSellersData.ordersState = selectGoodsState;
	var queryGoodsRequest = getQueryOrdersRequest();
	querySellerOrder(queryGoodsRequest);
}

function buildPageHtml(totalCount,totalPage,currentPage,pageSize){
	var html = "<ul>";
	html += "<li class='yeshu'>共 "+totalCount+" 单，共 "+totalPage+" 页</li>";
	html += "<li class='up_off'><a onclick='lastPage("+totalCount+","+totalPage+","+
			currentPage+","+pageSize+")'>上一页</a></li>";
	html += "<li class='nb_on'><a id='pageBottom_current'>"+currentPage+"</a></li>";
	html += "<li class='down_off'><a onclick='nextPage("+totalCount+","+totalPage+","+
			currentPage+","+pageSize+")'>下一页</a></li>";
	html += "<li class='yeshu'>到第<input type='text' value='"+currentPage+"' id='pagerBottom_input' class='w30' >页</li>";
	html += "<li class='find'><a ><span onclick='goToPage("+totalCount+","+totalPage+","+
			currentPage+","+pageSize+")'>查看</span></a></li>";
	html += "</ul>";
	
	$("#pagerBottom").html(html);
}

// 下一页
function nextPage(totalCount,totalPage,currentPage,pageSize){
	if(currentPage>=totalPage){
		return;
	}
	$("#pageBottom_current").html(currentPage+1);
	cacheSellersData.start = currentPage*pageSize;
	var sellerOrder = getQueryOrdersRequest();
	querySellerOrder(sellerOrder);
}

// 上一页
function lastPage(totalCount,totalPage,currentPage,pageSize){
	if(currentPage<=1){
		return;
	}
	$("#pageBottom_current").html(currentPage-1);
	cacheSellersData.start = (currentPage-2)*pageSize
	var sellerOrder = getQueryOrdersRequest();
	querySellerOrder(sellerOrder);
}

// 到第几页
function goToPage(totalCount,totalPage,currentPage,pageSize){
	currentPage = $("#pagerBottom_input").val();
	$("#pageBottom_current").html(currentPage);
	cacheSellersData.start = (currentPage-1)*pageSize;
	var sellerOrder = getQueryOrdersRequest();
	querySellerOrder(sellerOrder);
}




