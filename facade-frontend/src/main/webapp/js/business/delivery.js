$(function () {
    me.init();
    me.getIsNewFund();
    me.getDeliveryType();
    me.downLoadTemplate();
    me.uploaderFile();
    $('.Margin').hide()
    $('.Available').hide()
    //me.getDelivery();
    //查询
    //
    $("#receipt").click(function () {
        me.getDelivery();
    })
    $("#btnSearch").click(function () {
        me.init();
    });
    me.getPurchaserData();
    // $('.poprm').click(function () {
    // 	$(this).hide()
    // })
    // $('#receipt').click(function () {
    // 	$.ajax({
    // 		type: "GET",
    // 		url: baseServiceUrl + "services/purchaseData/getCurrentPurchaserData?t="+new Date().getTime(),
    // 		contentType: "application/json; charset=UTF-8",
    // 		dataType: "json",
    // 		success: function (resp) {
    // 			console.log("ddd",resp)
    //
    // 			if(resp.purchaserData.totalAmount>500) {
    // 				//请求弹出
    //
    // 				$('.poprm').show()
    // 			}else{
    //
    // 			}
    // 			// if(resp.purchaserData.availableAmount<200||resp.purchaserData.availableAmount==200) {
    // 			// 	//请求弹出
    // 			// 	$('.poprms').show()
    // 			// }else{
    //            //
    // 			// }
    // 		}
    // 	});
    // })
    /*$("input[type='radio']").bind('click',function(){
     me.updateDeliveryType($(this).val());
     });*/

});


var me = {
    pageSize: 20,
    gameAccountList: null,
    OrderList: null,
    moneyName: null,
    searchRequest: {},
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


    getIsNewFund: function () {
        $.ajax({
            type: "GET",
            url: baseServiceUrl + "services/readAggrement/getIsNewFund?t=" + new Date().getTime(),
            contentType: "application/json; charset=UTF-8",
            dataType: "json",
            success: function (resp) {
                console.log(resp.isNewFund);
                if (resp.isNewFund == true && resp.isAgree == true) {
                    me.newAccount = true;
                    $(".option").find('a').eq(3).hide();
                } else {
                    $(".option").find('a').eq(3).show();
                }
                if (resp.isNewFund == true && resp.isAgree == false) {
                    $("#agreement").show();
                    $(".agree").click(function () {
                        //调用同意协议接口
                        me.oldAccountAgree();
                        location.reload([true]);
                    });
                    $(".agree_no").click(function () {
                        //不同意跳转入驻页面页面
                        window.location.href = "rechargeList.html";
                    });
                }

            }
        });
    },

    getDelivery: function () {
        $.ajax({
            type: "GET",
            url: baseServiceUrl + "services/purchaseData/getCurrentPurchaserData?" + new Date().getTime(),
            contentType: "application/json; charset=UTF-8",
            dataType: "json",
            success: function (resp) {
                var code = resp.responseStatus.code;
                if (code == 31018) {
                    location.href = zbaoUrl +"/html/pay_withdrawals.html"
                } else {
                    location.href = "gameAccount.html"
                }

            }
        });
    },
    //用户同意接口
    oldAccountAgree: function () {
        $.ajax({
            type: "GET",
            async: false,
            url: baseServiceUrl + "services/agrre/agreeInitAccount",
            contentType: "application/json; charset=UTF-8",
            dataType: "json",
            global: false,
            success: function (resp) {
                var code = resp.responseStatus.code;
                if (code == "00") {
                    $(".poprm").hide();
                } else {
                    if (code == "10007" || code == "10008" || code == "10010" || code == "B1022") {
                        window.location.href = "applyseller.html";
                        alert(resp.responseStatus.message);
                    }
                    else if (code == "10012") {
                        if (me.newAccount) {
                            window.location.href = zbaoUrl + "/html/pay_withdrawals.html";
                        }
                        else {
                            window.location.href = "rechargeList.html";
                        }
                        alert(resp.responseStatus.message);
                    } else if (code == "5011") {
                        alert(resp.responseStatus.message);
                        window.open('http://user.5173.com/userinfo/realnameauthenticate.aspx');
                        return false;
                    }else {
                        alert(resp.responseStatus.message);
                    }
                }
            }
        });
    },


    //获取收货方式
    getDeliveryType: function () {
        var request = {};
        request.gameName = $.trim($("#game").text());
        //console.log(request.gameName);
        $.ajax({
            type: "GET",
            url: baseServiceUrl + "services/shGameConfig/getConfigByGameName?t=" + new Date().getTime(),
            data: request,
            contentType: "application/json; charset=UTF-8",
            dataType: "json",
            success: function (resp) {
                console.log(resp);
                if (resp.shGameConfig != null) {
                    if (resp.shGameConfig.unitName != null) {
                        moneyName = "(" + resp.shGameConfig.unitName + ")";
                        $("#moneyName-text").html(moneyName);
                    }
                    var tradeTypeArr = resp.shGameConfig.tradeType.split(",");
                    var tradeTypeIdArr = resp.shGameConfig.tradeTypeId.split(",");

                    me.getPurchaserData();
                    //me.initDelivery(tradeTypeArr, tradeTypeIdArr);
                }
            }
        });
    },


    getPurchaserData: function () {
        $.ajax({
            type: "GET",
            url: baseServiceUrl + "services/purchaseData/getCurrentPurchaserData?t=" + new Date().getTime(),
            contentType: "application/json; charset=UTF-8",
            dataType: "json",
            success: function (resp) {
                console.log(resp);
                var code = resp.responseStatus.code;
                if (code == 31018) {
                    // alert("123456");
                    location.href = zbaoUrl + "/html/pay_withdrawals.html"
                    // return;
                } else if (resp.purchaserData != null) {
                    console.log(resp);
                    var deliveryType = resp.purchaserData.deliveryType;
                    var tradeTypeStr = resp.purchaserData.tradeType;
                    $("#deliveryType-" + deliveryType).attr("checked", "checked").unbind();
                    if (deliveryType != 2) {
                        $("#deliveryType-3").attr("checked", true).unbind();
                    }

                    var tradeTypeArr = tradeTypeStr.split(",");
                    for (var i = 0; i < tradeTypeArr.length; i++) {
                        $("#tradeType-" + tradeTypeArr[i]).attr("checked", true);
                    }
                }
            }
        });
    },
    initDelivery: function (tradeTypeArr, tradeTypeIdArr) {
        //console.log(str);
        var htmlStr = "";
        htmlStr += "<span class=\"open_font\">收货模式：</span> <span class=\"open_yo\">";
        for (var i = 0; i < tradeTypeArr.length; i++) {
            var tradeType = tradeTypeArr[i];
            //console.log(deli);
            var tradeTypeId = tradeTypeIdArr[i];
            htmlStr += "<input value=\"" + tradeType + "," + tradeTypeId + "\" type=\"checkbox\" name=\"modele\" class='tradeTypeClass' id='tradeType-" + tradeTypeId + "'>" + tradeType + "</span> <span class=\"open_yo\">";
        }
        $("#deli_type").html(htmlStr);

        //绑定事件
        var chk_tradeType = [];
        var chk_tradeTypeId = [];
        $(".tradeTypeClass").click(function () {
            if ($('.tradeTypeClass:checked').size() == 0) {
                if ($("#deliveryType-2:checked").size() > 0) {
                    alert("请至少选择一种收货模式");
                    return false;
                }
            }

            chk_tradeType = [];
            chk_tradeTypeId = [];
            $('.tradeTypeClass:checked').each(function () {
                chk_tradeType.push($(this).val().split(",")[0]);
                chk_tradeTypeId.push($(this).val().split(",")[1])
            });
            me.updateTradeType(chk_tradeType, chk_tradeTypeId);
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
        me.searchRequest.gameName = gameName;
        me.searchRequest.region = region;
        me.searchRequest.server = server;
        me.searchRequest.pageSize = me.pageSize;
        me.searchRequest.deliveryType = 2;
        me.searchRequest.page = page_index;
        $.ajax({
            type: "GET",
            url: baseServiceUrl + "services/purchaseOrder/queryPurchaseOrderList?t=" + new Date().getTime(),
            data: me.searchRequest,
            contentType: "application/json; charset=UTF-8",
            dataType: "json",
            success: function (resp) {
                var code = resp.responseStatus.code;
                if (code == "00") {
                    me.purchaseOrderList = resp.purchaseOrderList;
                    //console.log(resp.purchaseOrderList);
                    //moneyName=resp.purchaseOrderList[0].moneyName;
                    //console.log(resp.purchaseOrderList[0]);
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
                        if (me.newAccount) {
                            window.location.href = zbaoUrl + "/html/pay_withdrawals.html";
                        }
                        else {
                            window.location.href = "rechargeList.html";
                        }
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
        $(".mytr").remove();
        for (var i in me.purchaseOrderList) {
            var purchaseOrder = me.purchaseOrderList[i];
            //console.log(me.purchaseOrderList[i]);
            var htmlStr = "<tr class='txt mytr'><td width=\"25\" class=\"pl10\"><input type=\"checkbox\" name=\"msgId\" class=\"vam\" id=\"chk_" + purchaseOrder.id + "\" value=\"1fe58b4775734b7a8faeaea24a4174d9\"></td>";
            htmlStr += "<td width=\"200\" class=\"yxqf\">";

            htmlStr += purchaseOrder.gameName + "/" + purchaseOrder.region + "/" + purchaseOrder.server;
            htmlStr += "<ul id=\"account_" + purchaseOrder.id + "\"></ul>";

            htmlStr += "</td>";
            htmlStr += "<td width=\"80\" align=\"center\" class=\"yxqf-count\">";
            htmlStr += "<input style='width: 50px;' class=\"counts\" id='counts-" + purchaseOrder.id + "' type=\"text\" value=\"" + purchaseOrder.count + "\"/>";
            htmlStr += "<ul id=\"countInput_" + purchaseOrder.id + "\"></ul>";

            htmlStr += "</td>";

            htmlStr += "</td>";
            htmlStr += "<td width=\"80\" align=\"center\" >";
            htmlStr += "<input style='width: 50px;' class=\"counts1\" id='counts1-" + purchaseOrder.id + "'  type=\"text\" value=\"" + purchaseOrder.price + "\"/>元/<span class='moneyNameClass'>" + purchaseOrder.moneyName + "</span></td>";
            htmlStr += "<ul id=\"countInput_" + purchaseOrder.id + "\"></ul>";

            htmlStr += "</td>";
            htmlStr += "<td width=\"80\" align=\"center\">";
            htmlStr += "<input style='width: 50px;' class=\"counts2\" id='counts2-" + purchaseOrder.id + "'  type=\"text\" value=\"" + purchaseOrder.minCount + "\"/>";
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

            $("#orderList tbody").append(htmlStr);
        }


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
                url: baseServiceUrl + "services/gameAccount/queryGameAccountList?t=" + new Date().getTime(),
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
                            if (me.newAccount) {
                                window.location.href = zbaoUrl + "/html/pay_withdrawals.html";
                            }
                            else {
                                window.location.href = "rechargeList.html";
                            }
                        }
                        else if (code == 31017) {
                            //请求弹出
                            $('.poprm').show()
                            return false
                        } else {
                            location.href = "delivery.html?oc=0"
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
                /*var SelectFalse = false;
                 var CheckBox = $('input[name = modele]');
                 for(var i = 0; i < CheckBox.length; i++) {
                 if(CheckBox[i].checked){
                 SelectFalse = true;
                 }
                 }
                 if(!SelectFalse) {
                 alert("对不起：至少要选一项收货方式");
                 return -1;
                 }*/
                upFirst = true;
                // 判断文件大小等
                var size = e.size; // 单位：字节
                if (size >= 20 * 1024 * 1024) {
                    alert("上传文件不能超过20M");
                    return -1;
                }
                ;
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
                    //ie浏览器中无法弹出错误信息，机器加入
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
            else if (price < 0) {
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
                        alert(resp.responseStatus.message);
                        me.init();
                    } else {
                        if (code == "10007" || code == "10008" || code == "10010" || code == "B1022") {
                            window.location.href = "applyseller.html";
                        }
                        else if (code == "10012" || code == "31019") {
                            if (me.newAccount) {
                                window.location.href = zbaoUrl + "/html/pay_withdrawals.html";
                            }
                            else {
                                window.location.href = "rechargeList.html";
                            }
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
    //全部上架或者下架
    onlineAll: function (isOnline) {
        if (confirm('是否确定全部' + (isOnline == true ? '上架?' : '下架?'))) {
            me.searchRequest.isOnline = isOnline;
            $.ajax({
                type: "POST",
                url: baseServiceUrl + "services/purchaseOrder/onlineAll",
                data: $.toJSON(me.searchRequest),
                contentType: "application/json; charset=UTF-8",
                dataType: "json",

                success: function (resp) {
                    var code = resp.responseStatus.code;
                    if (code == "00") {
                        alert((isOnline == true ? '上架' : '下架') + "成功！");
                        me.init();
                    } else {
                        if (code == "10007" || code == "10008" || code == "10010" || code == "B1022") {
                            window.location.href = "applyseller.html";
                        }
                        else if (code == "10012" || code == "31019") {
                            if (me.newAccount) {
                                window.location.href = zbaoUrl + "/html/pay_withdrawals.html";
                            }
                            else {
                                window.location.href = "rechargeList.html";
                            }
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
    //保存
    saveData: function (id) {
        var purchaseOrder = me.getPurchase(id);

        var gameAccountList = {};
        $(".account_true_" + id).each(function () {
            var accountId = $(this).attr("id");
            gameAccountList[accountId] = $(this).find("input").val();
        });

        //更新价格和采购数量
        var count = $("#counts-" + id).val();
        var price = $("#counts1-" + id).val();
        var mincount = $("#counts2-" + id).val();


        //console.log(count+","+price);

        //保存前的检验
        if (!me.checkValue(price, count)) {
            return false;
        }

        if (confirm('是否确定保存？')) {
            var request = {};
            request.id = id;
            request.price = price;
            request.count = count;
            request.minCount = mincount;
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
                        me.init();
                    } else {
                        if (code == "10007" || code == "10008" || code == "10010" || code == "B1022") {
                            window.location.href = "applyseller.html";
                        }
                        else if (code == "10012") {
                            if (me.newAccount) {
                                window.location.href = zbaoUrl + "/html/pay_withdrawals.html";
                            }
                            else {
                                window.location.href = "rechargeList.html";
                            }
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
    //修改收货方式
    updateDeliveryType: function (deliveryType) {
        if (deliveryType == 2) {
            var count = $('.tradeTypeClass:checked').size();
            if (count == 0) {
                $("#deliveryType-3").attr("checked", "checked").unbind();
                alert("请先选择至少一种收货模式，再开启手工收货");
                return false;
            }
        }

        var request = {};
        request.deliveryType = deliveryType;
        //console.log(deliveryType);
        $.ajax({
            type: "GET",
            url: baseServiceUrl + "services/purchaseData/updateDeliveryType",
            data: request,
            contentType: "application/json; charset=UTF-8",
            dataType: "jsonp",
            jsonp: "callback",
            success: function (resp) {
                $("#deliveryType-" + deliveryType).unbind();
                $("input[type='radio'][id!=deliveryType-" + deliveryType + "]").unbind().bind('click', function () {
                    me.updateDeliveryType($(this).val());
                });
                var responseStatus = resp.responseStatus;
                var code = responseStatus.code;
                if (code == "00") {
                    alert("操作成功");
                }
            }
        });
    },

    //修改交易方式
    updateTradeType: function (chk_tradeTypeArr, chk_tradeTypeIdArr) {
        var tradeTypes = chk_tradeTypeArr.join(",");
        var tradeTypeIds = chk_tradeTypeIdArr.join(",");
        var request = {};
        request.tradeTypeName = tradeTypes;
        request.tradeType = tradeTypeIds;
        $.ajax({
            type: "GET",
            url: baseServiceUrl + "services/purchaseData/updateTradeType",
            data: request,
            contentType: "application/json; charset=UTF-8",
            dataType: "jsonp",
            jsonp: "callback",
            success: function (resp) {
                var responseStatus = resp.responseStatus;
                var code = responseStatus.code;
            }
        });
    },
    //批量删除采购单
    deleteAll: function () {
        var ids = [];
        $(".vam").each(function () {
            var idsStr = $(this).attr("id");
            if (idsStr != null && idsStr.indexOf('chk_') > -1 && $(this).is(":checked")) {
                var id = idsStr.substr(4);
                ids.push(id);
            }
        });
        if (ids == "") {
            alert("请选择收货单再进行删除操作！");
            return;
        }
        if (confirm("确定删除该收货单吗？")) {
            var request = {};
            request.ids = ids;
            $.ajax({
                type: "POST",
                url: baseServiceUrl + "services/purchaseOrder/deleteAll",
                data: $.toJSON(request),
                dataType: "json",
                contentType: "application/json; charset=UTF-8",
                success: function (resp) {
                    var code = resp.responseStatus.code;
                    var message = resp.responseStatus.message;
                    if (code == "00") {
                        alert(message);
                        me.init();
                    } else {
                        alert("删除失败")
                    }
                }
            })
        }
    },

    //下载模板
    downLoadTemplate: function () {
        $("#btnDownloadTemplate").click(function () {
            window.open(baseHtmlUrl + "download/repositoryTemplate/shTemplate.rar");
        });
    }
};