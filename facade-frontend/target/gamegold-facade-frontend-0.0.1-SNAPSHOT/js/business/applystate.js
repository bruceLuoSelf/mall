/**
 * 卖家信息页面
 */
var sellerInfo;
var selectPasspod;
$(document).ready(function() {
	jQuery(function($){
		$('#excelUpload').fileUploader({
			autoUpload: true,
			selectFileLabel: '请选择文件...',
			allowedExtension: 'xls|xlsx',
			
			onFileChange: function(e, form) {
				// 判断文件大小等
				var size = e.size; // 单位：字节
				if(size >= 20*1024*1024){
					alert("上传文件不能超过20M");
					return -1;
				}
                //$("#serviceId").attr("value", sellerInfo.servicerInfo.id);
			},
			// 设置请求头
			setRequestHeader: function(request) {
				//request.setRequestHeader("gamegold-serviceId", sellerInfo.servicerInfo.id);
				request.setRequestHeader("5173_authkey", getAuthkey());
			},
			// 每次上传后
			afterEachUpload: function(data, status, formContainer) {
				
			}
		});
	});
	
	jQuery(function($){
		$('#passpodUpload').fileUploader({
			autoUpload: true,
			selectFileLabel: '请选择文件...',
			allowedExtension: 'jpg|JPG|png|PNG|jpeg|JPEG|png|PNG',
			
			onFileChange: function(e, form) {
				// 判断文件大小等
				var size = e.size; // 单位：字节
				if(size >= 20*1024*1024){
					alert("上传文件不能超过20M");
					return -1;
				}
				$("#gameName").attr("value", selectPasspod.gameName);
				$("#gameAccount").attr("value", selectPasspod.gameAccount);
			},
			// 设置请求头
			setRequestHeader: function(request) {
				request.setRequestHeader("gamegold-gameName", selectPasspod.gameName);
				request.setRequestHeader("gamegold-gameAccount", selectPasspod.gameAccount);
				request.setRequestHeader("5173_authkey", getAuthkey());
			},
			// 每次上传后
			afterEachUpload: function(data, status, formContainer) {
				// 上传密保卡完成，即时修改页面显示
				if(status == "success"){
					var passpodUrl = buildImageUrl(data.passpodUrl,"80x80");
					var tdHtml = "<a href='"+(imageServiceUrl+data.passpodUrl)+"' target='_blank'><img src='"+passpodUrl+"' width='80px'/></a>";
					$("#" + selectPasspod.gameName + "_" + selectPasspod.gameAccount).find("td").eq(2).html(tdHtml);
				}
			}
		});
	});
	querySellerInfo();
	
});

function applyservice(){
	 // 选择符合条件的客服
    var queryServiceRequest = {};
    queryServiceRequest.size = 8;
    queryServiceRequest.servicerType=7;
    $.ajax({
        type : "POST",
        url : baseServiceUrl + "services/queryservicer/applyservice?t="+new Date().getTime(),
        data : $.toJSON(queryServiceRequest),
        contentType : "application/json; charset=UTF-8",
        dataType : "json",
        success : function(resp) {
            var responseStatus = resp.responseStatus;
            var code = responseStatus.code;
            if (code == "00") {
                var servicerList = resp.userInfoEOs;
                // 组装客服信息
                buildServicerListHtml(servicerList);
            }
        },
        error : function(resp) {
            console.log(resp);
        }
    });
}

// 提交文件
$(".a_service_confirm").click(function(){
	
});

//每十秒请求一次，刷新审核状态
var int = setInterval("querySellerInfo()",1000*10);
//查询当前卖家信息
function querySellerInfo(){
	var queryRequest = {};
	$.ajax({
		type: "POST",
		url: baseServiceUrl + "services/seller/querysellerinfo?t="+new Date().getTime(),
		data: $.toJSON(queryRequest),
		contentType: "application/json; charset=UTF-8",
		dataType: "json",
		beforeSend: function (request){
	        request.setRequestHeader("5173_authkey", getAuthkey());
	    },
		success: function(resp) {
			var responseStatus = resp.responseStatus;
	        var code = responseStatus.code;
	        if (code == "00") {
				if(resp.copyRight){
					$('#authorize').show();
					$('#authorize .c_code_input input').val(resp.copyRight)
				}
	        	// 卖家信息
	        	sellerInfo = resp.sellerInfo;
	        	if(!isNull(sellerInfo)){
					//var isOpenSh=sellerInfo.openShState;
	        		if(sellerInfo.checkState == CheckState.PassAudited){
	        			//$(".service_finish").html("入驻成功<a href='applystate.html' target='_blank'>新版库存管理</a>");
						//var titleStr="入驻成功<a href='applystate.html' target='_blank'>新版库存管理</a><a href='#' target='_blank'>我要收货</a>";
						var titleStr="商城权限入驻成功<a href='applystate.html' target='_blank'>新版库存管理</a>";
						$(".service_finish").html(titleStr);

	        			$(".p_border").html("我是您的专属员工，将会全程负责您的库存的销售。有问题请与我联系！");
	            		$(".service_order_info").css("display","block"); //显示excel库存及密保卡上传
	            		clearInterval(int);
	        		}else if(sellerInfo.checkState == CheckState.UnAudited){
	        			$(".service_finish").html("报名成功");
	            		$(".p_border").html("尊敬的<strong>"+sellerInfo.name+"</strong>我将马上联系您并向您介绍大卖家入驻的细则，请您保持手机畅通。");
	            		$("#worktime").css("display","inline");
	        		}else if(sellerInfo.checkState == CheckState.UnPassAudited){
	        			// 审核未通过的，跳转到申请页面
	        			window.location.href = baseHtmlUrl + "applyseller.html";
	        			return;
	        		}
	        		// 组装客服信息
	            	buildServicerHtml(sellerInfo.servicerInfo);
	            	$("#nowServiceId").val(sellerInfo.servicerInfo.id);

					if(sellerInfo.openShState==null||sellerInfo.openShState==undefined||sellerInfo.checkState == CheckState.UnAudited){
						$("#divSh").hide();
					}else if(sellerInfo.openShState==CheckState.PassAudited){
						$("#divSh").show();
						$(".spanShMsg").html("<span>您的收货权限已经申请成功！</span><span class=\"spanShA\">您可以在<a href=\"gameAccount.html\">收货商中心</a>对收货进行进行管理</span>");
					}else if(sellerInfo.openShState==CheckState.UnAudited){
						$("#divSh").show();
						$(".spanShMsg").html("<span>您的收货权限还在审核中！</span>");
					}else if(sellerInfo.openShState==CheckState.UnPassAudited){
						$("#divSh").show();
						$(".spanShMsg").html("<span>您的收货权限已关闭！</span>");
					}
	        	}else{
	        		// 跳转到申请卖家页面
	        		window.location.href = baseHtmlUrl + "applyseller.html";
	        		return;
	        	}
	        	
	        	// 组装密保卡信息
	        	var repositoryList = resp.repositoryInfos;
	        	buildPasspodHtml(repositoryList);
	        }
		},
		error: function(resp) {
			console.log(resp);
		}
	});
}

//选择客服页面显示
var p ;
function display(){
	if(p==null){
		$(".chose_service_list").css("display","block");
		p = $(".chose_service_list");
		applyservice();
	}
	else {
		$(".chose_service_list").css("display","none");
		$(".service_box_1").css("display","none");
		p=null;
		buildServicerHtml(sellerInfo.servicerInfo);
	}
}

// 页面显示客服详细信息
function buildServicerHtml(servicerInfo){
	var avatarUrl = isNull(servicerInfo.avatarUrl)?"img/head.png":buildImageUrl(servicerInfo.avatarUrl,"64x64");
	$("#service_avatar").attr("src", avatarUrl);
	
	var nickName = isNull(servicerInfo.nickName)?"公主":servicerInfo.nickName;
	$(".service_name").html(nickName);
	
//	if(!isNull(servicerInfo.sign)){
//		$(".service_word").html("<p>"+servicerInfo.sign+"</p>");
//	}
	// 添加QQ点击绑定事件
	$(".chose_service_qq").unbind("click");
	$(".chose_service_qq").click(function() {
		if(isNull(servicerInfo) || isNull(servicerInfo.qq)){
			return;
		}
		window.open("http://wpa.qq.com/msgrd?v=3&uin="+servicerInfo.qq+"&site=qq&menu=yes");
	});
	
    var qq = isNull(servicerInfo.qq) ? "" : servicerInfo.qq;
    $(".service_qq").html("QQ：<strong>"+qq+"</strong>");

	var phoneNumber = isNull(servicerInfo.phoneNumber)?"":servicerInfo.phoneNumber;
	$(".chose_service_phone").html("手机<strong>"+phoneNumber+"</strong>");
	
	var weiXin = isNull(servicerInfo.weiXin)?"":servicerInfo.weiXin;
	$(".chose_service_wechat").html("微信<strong>"+weiXin+"</strong>");
}

// 组装密保卡上传页面
function buildPasspodHtml(repositoryList){
	if(isNull(repositoryList)){
		return;
	}
	// 清空原始值
	$(".table_service_secret_card tbody").html("");
	for ( var i = 0; i < repositoryList.length; i++) {
		var repositoryInfo = repositoryList[i];
		var gameName = repositoryInfo.gameName; //游戏名称
		var gameAccount = repositoryInfo.gameAccount; //账号名称
		var passpodUrl = repositoryInfo.passpodUrl; //密保卡url
		var href = "";
		if(isNull(passpodUrl)){
			passpodUrl = "";
			href = "javascript:void(0)";
		}else {
			href = imageServiceUrl+passpodUrl;
			passpodUrl = buildImageUrl(passpodUrl,"80x80");
		}
		
		var html = "<tr id='"+gameName+"_"+gameAccount+"'>";
		var jsonPasspodInfo = $.toJSON(repositoryInfo);
		html += "<td>"+gameName+"</td><td>"+gameAccount+"</td><td><a href='"+href+"' target='_blank'><img src='"+passpodUrl+"' width='80px' /></a></td>" +
				"<td><a href='javascript:void(0)' class='a_gray' value='"+ jsonPasspodInfo +"'>上传密保卡</a></td>";
		html += "</tr>";
		
		$(".table_service_secret_card tbody").append(html);
	}
	
	// 密保卡上传点击事件
    $(".a_gray").mouseover(function(event){
        // 设置当前选中的密码卡
        var jsonPasspodInfo = $(event.target).attr("value");
        selectPasspod = $.evalJSON(jsonPasspodInfo);
        var top = $(event.target).offset().top;
        var left = $(event.target).offset().left;
        $("#px-form-2-input").css("top", top);
        $("#px-form-2-input").css("left", left);
    });
}

//组装客服html
function buildServicerListHtml(servicerList) {
	$(".chose_service_list").html("");
    if (isNull(servicerList)) {
        return;
    }
    var nowServiceId = $("#nowServiceId").val();
    for (var i = 0; i < servicerList.length; i++) {
        var servicer = servicerList[i];
        // 该客服被禁用，不显示
        if (servicer.isDeleted == true) {
            continue;
        }
        if(nowServiceId != null){
          if (servicer.id == nowServiceId) {
        	continue;
          }
        }
        var servicerJson = $.toJSON(servicer);
        var avatar = isNull(servicer.avatarUrl) ? "img/head.png" : buildImageUrl(servicer.avatarUrl, "64x64");
        var name = isNull(servicer.nickName) ? "公主" : servicer.nickName;
        var sign = isNull(servicer.sign) ? "心随你动 服务在心" : servicer.sign;

        var html = "<div id='servicerId_" + servicer.id + "' class='chose_service_block' value='" + servicerJson + "'" + "onclick='javascript:selectServicer(" + servicer.id + ")'>";
        html += "<div class='chose_service_img'><img src='" + avatar + "' /></div>";
        html += "<div class='chose_service_infor'>";
        html += "<div class='chose_service_name'>客服：<strong>" + name + "</strong></div>";
        html += "<div class='chose_serive_word'>" + sign + "</div>";
        html += "</div></div>";

        $(".chose_service_list").append(html);
    }

//    // 鼠标移动到客服上，添加鼠标事件，自动选择客服
//    $(".chose_service_block").click(function() {
//        $(this).trigger("click");
//    });
    
    // 双击客服上，添加鼠标事件，
    $(".chose_service_block").dblclick(function() {
    	if(confirm("确定要更换客服吗?")){
			 var request = {};
		   	 request.servicerId = selectServicerId;
		   	 $.ajax({
		   	        type : "POST",
		   	        url : baseServiceUrl + "services/seller/alterservice?t="+new Date().getTime(),
		   	        data : $.toJSON(request),
		   	        contentType : "application/json; charset=UTF-8",
		   	        dataType : "json",
		   	        success : function(resp) {
		   	            var responseStatus = resp.responseStatus;
		   	            var code = responseStatus.code;
		   	            if (code == "00") {
		   	            	window.location.reload();
		   	            }
		   	        },
		   	        error : function(resp) {
		   	            console.log(resp);
		   	        }
		   	    });
		   	$(".chose_service_list").css("display","none");
			$(".service_box_1").css("display","none");
			p=null;
    	}
    	 
    });
}

// 选择客服
var selectServicerId = null;
function selectServicer(servicerId) {
    selectServicerId = servicerId;

    // 删除其他客服的选中css
    $(".chose_service_block").removeClass("chose_service_block_chosed");
    $(".span_closed").remove();
    // 设置当前客服选择css
    $("#servicerId_" + servicerId).addClass("chose_service_block_chosed");
    $("#servicerId_" + servicerId + " .chose_service_infor").after("<span class='span_chosed'></span>");
    $(".service_box_1").css("display", "block");

    // 补充客服详细信息
    var servicerInfo = $.evalJSON($("#servicerId_" + servicerId).attr("value"));

    var avatarUrl = isNull(servicerInfo.avatarUrl) ? "img/head.png" : buildImageUrl(servicerInfo.avatarUrl, "64x64");
    $("#service_avatar_sel").attr("src", avatarUrl);

    var nickName = isNull(servicerInfo.nickName) ? "公主" : servicerInfo.nickName;
    $(".service_name").html(nickName);

    var qq = isNull(servicerInfo.qq) ? "" : servicerInfo.qq;
    //$(".chose_service_qq").html("QQ：<strong>" + qq + "</strong>");
	$(".chose_service_qq").unbind("click");
	$(".chose_service_qq").click(function() {
		if(isNull(servicerInfo) || isNull(servicerInfo.qq)){
			return;
		}
		window.open("http://wpa.qq.com/msgrd?v=3&uin="+servicerInfo.qq+"&site=qq&menu=yes");
	});
	$(".service_qq").html("QQ<strong>" + qq + "</strong>");
    
    var phoneNumber = isNull(servicerInfo.phoneNumber) ? "" : servicerInfo.phoneNumber;
    $(".chose_service_phone").html("手机<strong>" + phoneNumber + "</strong>");

    var weiXin = isNull(servicerInfo.weiXin) ? "" : servicerInfo.weiXin;
    $(".chose_service_wechat").html("微信<strong>" + weiXin + "</strong>");
}