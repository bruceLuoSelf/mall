$(function () {
	me.init();
	me.getDeliveryType();
	me.downLoadTemplate();
	me.uploaderFile();
	//查询
	$("#btnSearch").click(function () {
		me.init();
	});

	$("input[type='radio']").click(function(){
		var ans = document.getElementsByName("layout");
		for(var i=0;i<ans.length;i++)
			if(ans[i].checked==true){
				if (ans[i].value==1){//用户选择开启手动收货
					alert("确认修改吗？");
					me.updateHandDelivery(2,"手工收货");//后台修改状态值为2
				}
				if (ans[i].value==2){//用户选择关闭手动收货
					alert("确认修改吗？");
					me.updateHandDelivery(1,"机器收货");//后台修改状态值为1
				}
			}
	});


});


var me = {
	pageSize: 20,
	gameAccountList: null,
	OrderList: null,

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

	//获取收货方式
	getDeliveryType:function () {
		var request = {};
		request.gameName =$.trim($("#game").text());
		$.ajax({
			type: "GET",
			url: baseServiceUrl + "services/shGameConfig/getConfigByGameName?t="+new Date().getTime(),
			data: request,
			contentType: "application/json; charset=UTF-8",
			dataType: "json",
			success: function (resp) {
				//console.log(resp.shGameConfigList[0].tradeType);
				var str=[];
				str=resp.shGameConfigList[0].tradeType.split(",");
				me.initDelivery(str);
			}
		});
	},

	initDelivery:function (str) {
		//console.log(str);
		var htmlStr= "";
		htmlStr+="<span class=\"open_font\">收货模式:</span> <span class=\"open_yo\">";
		for (var i=0;i<str.length;i++) {
			var deli=str[i];
			//console.log(deli);
			htmlStr += "<input value=\""+deli+"\" type=\"checkbox\" name=\"modele\" >"+deli+"</span> <span class=\"open_yo\">";
		}
		$("#deli_type").html(htmlStr);

		//绑定事件
		var chk_value =[];
		$("input[type='checkbox']").click(function() {
			chk_value=[];
			$('input[name="modele"]:checked').each(function () {
				chk_value.push($(this).val());
			});
			me.updateTradeType(chk_value);
		});
	},

	//分页数据
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

		var me = this;
		var request = {};
		request.gameName = gameName;
		request.region = region;
		request.server = server;
		request.pageSize = me.pageSize;
		request.page = page_index;
		$.ajax({
			type: "GET",
			url: baseServiceUrl + "services/purchaseOrder/queryPurchaseOrderList?t="+new Date().getTime(),
			data: request,
			contentType: "application/json; charset=UTF-8",
			dataType: "json",
			success: function (resp) {
				var code = resp.responseStatus.code;
				if (code == "00") {
					me.purchaseOrderList = resp.purchaseOrderList;
					//console.log(resp.purchaseOrderList);
					if (me.purchaseOrderList != null && me.purchaseOrderList.length > 0) {
						$(".totalPage").html("共" + resp.totalCount + "笔，共" + resp.totalPage + "页");
					}
					else {
						$(".totalPage").html("共0笔，共1页");
					}
					// 初始化页面
					me.initPage();
				} else {
					if (code == "10007" || code == "10008" || code == "10010" || code == "B1022") {
						window.location.href = "applyseller.html";
					}
					else if (code == "10012") {
						window.location.href = "rechargeList.html";
					}
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
		return false;
	},



	//初始化渲染页面数据
	initPage: function () {
		var htmlStr = "<tr class=\"titt\" style=\"border-top:#c8e1ff 1px solid\"><td colspan=\"7\"><input type=\"checkbox\" class=\"vam\" id=\"selectAll\" onclick=\"me.selectAllMsg()\"><span class=\"vam\" style=\"margin:0 30px 0 10px\">全选</span><a href=\"javascript:void(0)\" onclick=\"me.onlineMuliti()\" class=\"btn_gray\"><span>批量上架</span></a><a href=\"javascript:void(0)\" class=\"btn_gray\" onclick=\"me.offlineMuliti()\"><span>批量下架</span></a></td></tr>";

		htmlStr += "<tr class=\"tith\"><td width=\"25\" ></td><td width=\"120\" align=\"center\">游戏区服 </td><td width=\"120\" align=\"center\">收货数量</td><td width=\"120\" align=\"center\">单价("+11+")</td><td width=\"120\" align=\"center\">最低采购数量</td><td width=\"50\" align=\"center\">总价</td><td width=\"50\" align=\"center\">状态 </td><td width=\"110\" align=\"center\">操作 </td></tr>";
		for (var i in me.purchaseOrderList) {
			var purchaseOrder = me.purchaseOrderList[i];
			//console.log(me.purchaseOrderList[i]);
			htmlStr += "<tr class=\"txt\"><td width=\"25\" class=\"pl10\"><input type=\"checkbox\" name=\"msgId\" class=\"vam\" id=\"chk_" + purchaseOrder.id + "\" onclick=\"SetDeleteMsgList(this)\" value=\"1fe58b4775734b7a8faeaea24a4174d9\"></td>";
			htmlStr += "<td width=\"200\" class=\"yxqf\">";

			htmlStr+=purchaseOrder.gameName + "/" + purchaseOrder.region + "/" + purchaseOrder.server;
			htmlStr += "<ul id=\"account_" + purchaseOrder.id + "\"></ul>";

			htmlStr += "</td>";
			htmlStr += "<td width=\"80\" align=\"center\" class=\"yxqf-count\">";
			htmlStr += "<input style='width: 50px;' class=\"counts\"  type=\"text\" value=\"" + purchaseOrder.count + "\"/>";
			htmlStr += "<ul id=\"countInput_" + purchaseOrder.id + "\"></ul>";

			htmlStr += "</td>";

			htmlStr += "</td>";
			htmlStr += "<td width=\"80\" align=\"center\" >";
			htmlStr += "<input style='width: 50px;' class=\"counts1\"  type=\"text\" value=\"" + purchaseOrder.price + "\"/>元";
			htmlStr += "<ul id=\"countInput_" + purchaseOrder.id + "\"></ul>";

			htmlStr += "</td>";
			htmlStr += "<td width=\"80\" align=\"center\">";
			htmlStr += "<input style='width: 50px;' class=\"counts2\"  type=\"text\" value=\"" + purchaseOrder.minCount + "\"/>";
			htmlStr += "<ul id=\"countInput_" + purchaseOrder.id + "\"></ul>";

			htmlStr += "<td width=\"80\" align=\"center\" class='totalPrice' >" + toDecimal2(purchaseOrder.price * purchaseOrder.count) + "元</td>";
			if (purchaseOrder.isOnline) {
				htmlStr += "<td width=\"50\" align=\"center\">已上架</td>";
				htmlStr += "<td width=\"110\" align=\"center\"><a href=\"javascript:void(0)\" class=\"btn_white\" onclick=\"me.saveData(" + purchaseOrder.id + ")\"><span>保存</span></a> <a href=\"javascript:void(0)\" onclick=\"me.offline([" + purchaseOrder.id + "])\" class=\"btn_white\"><span>下架</span></a></td>";
			} else {
				htmlStr += "<td width=\"50\" align=\"center\" style='color:red;'>已下架</td>";
				htmlStr += "<td width=\"110\" align=\"center\"><a href=\"javascript:void(0)\" class=\"btn_white\" onclick=\"me.saveData(" + purchaseOrder.id + ")\"><span>保存</span></a> <a href=\"javascript:void(0)\" onclick=\"me.online([" + purchaseOrder.id + "])\" class=\"btn_white\"><span>上架</span></a></td>";
			}

			htmlStr += "</tr>";
		}
		$("#orderList tbody").html(htmlStr);

		//输入合理化验证
		me.validateInput();
	},
	validateInput: function () {
		$(".yxqf").each(function () {
			$(this).click(function () {
				if ($(this).find("span").html() == "-") {
					$(this).find("ul").hide();
					$(this).parents(".txt").find(".yxqf-count").find("ul").hide();
					$(this).find("span").html("+");
				}
				else {
					$(this).find("ul").show();
					$(this).next().find("ul").show();
					$(this).find("span").html('-');

					var id = $(this).find("span").attr("id");
					me.queryGameAccountList(id);
				}
			});
		});

		$(".price input[type='text']").keyup(function () {
			var nums = $(this).val();
			if (isNaN(nums)) {
				alert('单价只能为数字');
				$(this).val($(this).attr("preValue"));
			}
		});
	},
	//根据采购单区服查找对应的账号角色信息
	queryGameAccountList: function (id) {
		var purchaseOrder = me.getPurchase(id);
		if (purchaseOrder != null) {
			var request = {};
			request.gameName = purchaseOrder.gameName;
			request.region = purchaseOrder.region;
			request.server = purchaseOrder.server;
			request.gameRace = "";
			$.ajax({
				type: "GET",
				url: baseServiceUrl + "services/gameAccount/queryGameAccountList?t="+new Date().getTime(),
				data: request,
				contentType: "application/json; charset=UTF-8",
				dataType: "json",
				success: function (resp) {
					var code = resp.responseStatus.code;
					if (code == "00") {
						me.gameAccountList = resp.gameAccountList;
						me.appendAccountListData(id);
					} else {
						if (code == "10007" || code == "10008" || code == "10010" || code == "B1022") {
							window.location.href = "applyseller.html";
						}
						else if (code == "10012") {
							window.location.href = "rechargeList.html";
						}
						if (resp.responseStatus.code != "11") {
							alert(resp.responseStatus.message);
						}
					}
				}
			});
		}
	},
	//加载账号角色信息
	appendAccountListData: function (id) {
		var htmlStr = "";
		var htmlStrInput = "";
		htmlStr += "<li class=\"tit\">帐号</li>";
		htmlStr += "<li class=\"tit\">角色名</li>";
		for (var i in me.gameAccountList) {
			var gameAccount = me.gameAccountList[i];
			htmlStr += "<li>" + gameAccount.gameAccount + "</li>";
			htmlStr += "<li>" + gameAccount.roleName + "</li>";

			htmlStrInput += "<li id=" + gameAccount.id + " class='account_" + gameAccount.isShRole + "_" + id + "'>";
			if (gameAccount.isShRole) {
				htmlStrInput += "<input class=\"counts0\" type=\"text\" value=\"" + gameAccount.count + "\" size=\"18\"/>";
			}
			htmlStrInput += "</li>";
		}
		;
		$("#account_" + id).html(htmlStr);
		$("#countInput_" + id).html(htmlStrInput);

		$(".yxqf-count input[type='text']").keyup(function () {

			var nums = $(this).val();
			var reg = new RegExp('^[0-9][0-9]*$', 'g');
			if (!reg.test(nums)) {
				alert('请输入正确的数量');
				$(this).val(0);
			}

			var totalval = 0;
			$(this).parents(".yxqf-count ul").find('input').each(function () {
				totalval += parseInt($(this).val());
			});
			$(this).parents(".yxqf-count").find(".counts").val(totalval);

			var price = $(this).parents("tr").find(".price").find("input").val();

			$(this).parents("tr").find(".totalPrice").html(toDecimal2(parseFloat(price) * parseFloat(totalval)));
		});
	},
	getPurchase: function (id) {
		for (var i in me.purchaseOrderList) {
			var purchaseOrder = me.purchaseOrderList[i];
			if (purchaseOrder.id == id) {
				return purchaseOrder;
				break;
			}
		}
		return null;
	},
	//上传库存
	uploaderFile: function () {
		$('#excelUpload').fileUploader({
			autoUpload: true,
			selectFileLabel: '上传库存',
			allowedExtension: 'xls|xlsx',

			onFileChange: function (e, form) {
				upFirst = true;
				// 判断文件大小等
				var size = e.size; // 单位：字节
				if (size >= 20 * 1024 * 1024) {
					alert("上传文件不能超过20M");
					return -1;
				}
			},
			// 设置请求头
			setRequestHeader: function (request) {
				request.setRequestHeader("5173_authkey", getAuthkey());
			},
			// 每次上传后
			afterEachUpload: function (data, status, formContainer) {
				$("#px-form-1").hide();
				if (status == "success") {
					me.init();
					alert("上传成功！");
				}
				else {
					//ie浏览器中无法弹出错误信息，手动加入
					if (navigator.userAgent.indexOf('MSIE') >= 0 && upFirst) {
						upFirst = false;
						alert(data.responseStatus.message);
					}
				}
			}
		});
	},
	checkValue: function (price, count) {
		if (isNull(price)) {
			alert("采购单价不能为空");
			return false;
		}
		else {
			if (isNaN(price)) {
				alert("采购单价请输入正确的数字！");
				return false;
			}
			else if (price <= 0) {
				alert("采购单价只能大于0");
				return false;
			}
		}

		if (isNull(count)) {
			alert("采购数量不能为空");
			return false;
		}
		else {
			if (!isNumber(count)) {
				alert("采购数量请输入正确的整数");
				return false;
			}
			// else if (count < 1000) {
			//     alert("采购数量不能小于1000");
			//     return false;
			// }
			//待写，最小采购数量需要小于采购数量
			// else if (count % 1000 != 0) {
			// 	alert("采购数量只能1000的整数倍");
			// 	return false;
			// }
		}
		return true;
	},
	//上架
	online: function (ids) {
		if (confirm('是否确定上架？')) {
			var request = {};
			request.ids = ids;
			// var count = $("#" + ids).parents("tr").find(".yxqf-count").find("input").val();
			// if(count==null||count<=0){
			// 	alert('采购数量不能为空且必须大于0');
			// 	return;
			// }
			// request.count=count;
			request.isOnline = true;
			$.ajax({
				type: "POST",
				url: baseServiceUrl + "services/manualShOrder/setOnline",
				data: $.toJSON(request),
				contentType: "application/json; charset=UTF-8",
				dataType: "json",

				success: function (resp) {
					var code = resp.responseStatus.code;
					if (code == "00") {
						alert("上架成功！");
						me.init();
					} else {
						if (code == "10007" || code == "10008" || code == "10010" || code == "B1022") {
							window.location.href = "applyseller.html";
						}
						else if (code == "10012") {
							window.location.href = "rechargeList.html";
						}
						if (resp.responseStatus.code != "11") {
							//alert(resp.responseStatus.message);
							me.errorHref(resp.responseStatus.code);
						}
					}
				}
			});
		}
	},
	errorHref: function (code) {
		if (code == "10007" || code == "10008" || code == "10010" || code == "B1022") {
			window.location.href = "applystate1.html";
		}
	},
	//下架
	offline: function (ids) {
		if (confirm('是否确定下架？')) {
			var request = {};
			//var count = $("#" + ids).parents("tr").find(".yxqf-count").find("input").val();
			// if(count==null||count<=0){
			// 	alert('采购数量不能为空且必须大于0');
			// 	return;
			// }
			//request.count=count;
			request.ids = ids;
			//console.log(ids);
			request.isOnline = false;
			$.ajax({
				type: "POST",
				url: baseServiceUrl + "services/manualShOrder/setOnline",
				data: $.toJSON(request),
				contentType: "application/json; charset=UTF-8",
				dataType: "json",
				success: function (resp) {
					var code = resp.responseStatus.code;
					if (code == "00") {
						alert("下架成功！");
						me.init();
					} else {
						alert("下架失败！");
					}
				}
			});
		}
	},
	//批量选中与取消选中
	selectAllMsg: function () {
		if ($("#selectAll").is(":checked")) {
			$(".vam").attr("checked", "checked");
		}
		else {
			$(".vam").removeAttr("checked");
		}
	},
	//批量上架
	onlineMuliti: function () {
		var ids = [];
		$(".vam").each(function () {
			var idsStr = $(this).attr("id");
			if (idsStr != null && idsStr.indexOf('chk_') > -1 && $(this).is(":checked")) {
				var id = idsStr.substr(4);
				ids.push(id);
			}
		});
		if (ids == "") {
			alert("请勾选需要上架的采购单！");
			return;
		}
		me.online(ids);
	},
	//批量下架
	offlineMuliti: function () {
		var ids = [];
		$(".vam").each(function () {
			var idsStr = $(this).attr("id");
			if (idsStr != null && idsStr.indexOf('chk_') > -1 && $(this).is(":checked")) {
				var id = idsStr.substr(4);
				ids.push(id);
			}
		});
		if (ids == "") {
			alert("请勾选需要下架的采购单！");
			return;
		}
		me.offline(ids);
	},
	//保存
	saveData: function (id) {
		var purchaseOrder = me.getPurchase(id);
		// if (purchaseOrder.isOnline) {
		//     alert("请先下架再进行保存操作");
		//     return;
		// }

		var gameAccountList = {};
		$(".account_true_" + id).each(function () {
			var accountId = $(this).attr("id");
			gameAccountList[accountId] = $(this).find("input").val();
		});

		//更新价格和采购数量
		var count = $(".counts").val();
		var price = $(".counts1").val();
		var mincount=$(".counts2").val();


		if(count<mincount){
			alert("不能低于最低采购数量!");
		}
		console.log(count+","+price);
		//保存前的检验
		if (!me.checkValue(price, count)) {
			return false;
		}

		if (confirm('是否确定保存？')) {
			var request = {};
			request.id = id;
			request.price = price;
			request.count = count;
			request.minCount=mincount;
			$.ajax({
				type: "POST",
				url: baseServiceUrl + "services/manualShOrder/updatePurchaseOrderPriceAndCountAndNum",
				data: $.toJSON(request),
				contentType: "application/json; charset=UTF-8",
				dataType: "json",
				success: function (resp) {
					var code = resp.responseStatus.code;
					if (code == "00") {
						alert("保存成功");
					} else {
						if (code == "10007" || code == "10008" || code == "10010" || code == "B1022") {
							window.location.href = "applyseller.html";
						}
						else if (code == "10012") {
							window.location.href = "rechargeList.html";
						}
						if (resp.responseStatus.code != "11") {
							//alert(resp.responseStatus.message);
							me.errorHref(resp.responseStatus.code);
						}
					}
				}
			});
		}
	},
	//修改收货状态
	updateHandDelivery:function (num,str) {
		var request = {};
		request.strue = num;
		request.str=str;
		//console.log(num);
		$.ajax({
			type: "GET",
			url: baseServiceUrl + "services/manualShOrder/updateHandDelivery",
			data: request,
			contentType: "application/json; charset=UTF-8",
			dataType: "jsonp",
			jsonp: "callback",
			success: function (resp) {
				var responseStatus = resp.responseStatus;
				var code = responseStatus.code;
				if (code == "00") {

				}
			}
		});
	},

	//修改收货方式
	updateTradeType:function (chk_value) {
		var str=chk_value.join(",");
		var request = {};
		request.str = str;
		$.ajax({
			type: "GET",
			url: baseServiceUrl + "services/manualShOrder/updateTradeType",
			data: request,
			contentType: "application/json; charset=UTF-8",
			dataType: "jsonp",
			jsonp: "callback",
			success: function (resp) {
				var responseStatus = resp.responseStatus;
				var code = responseStatus.code;
				if (code == "00") {
				}
			}
		});
	},

	//下载模板
	downLoadTemplate: function () {
		$("#btnDownloadTemplate").click(function () {
			window.open(baseHtmlUrl + "download/repositoryTemplate/shTemplate.rar");
		});
	}
};