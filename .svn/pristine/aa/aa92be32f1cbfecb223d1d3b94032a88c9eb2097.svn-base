var moneyName, fcConfig = {};
$(function () {
    me.init();
    me.getStock();
    me.getIsNewFund();
    me.getGameConfig();
    me.getPurchaserData();
    me.downLoadTemplate();
    me.uploaderFile();
    $('.Margin').hide()
    $('.Available').hide()
    $("#machine").click(function () {
        me.getDelivery();
    })
    // $('.poprm').click(function () {
    //     $(this).hide()
    // })
    // // $('#machine').click(function () {
    // //     $.ajax({
    // //         type: "GET",
    // //         url: baseServiceUrl + "services/purchaseData/getCurrentPurchaserData?t="+new Date().getTime(),
    // //         contentType: "application/json; charset=UTF-8",
    // //         dataType: "json",
    // //         success: function (resp) {
    // //             console.log("ddd",resp)
    // //             if(resp.purchaserData.totalAmount>500||resp.purchaserData.totalAmount==500) {
    // //                 //请求弹出
    // //                 $('.poprm').show()
    // //             }else{
    // //
    // //             }
    //             // if(resp.purchaserData.availableAmount<200||resp.purchaserData.availableAmount==200) {
    //             //     //请求弹出
    //             //     $('.poprms').show()
    //             // }else{
    //             //
    //             // }
    //
    //         }
    //     });
    //
    // })
    // $.ajax({
    //     type: "GET",
    //     url: baseServiceUrl + "services/readAggrement/getIsNewFund?t="+new Date().getTime(),
    //     contentType: "application/json; charset=UTF-8",
    //     dataType: "json",
    //     success: function (resp) {
    //         console.log(resp.isNewFund)
    //         if(resp.isNewFund) {
    //             $("#purchase").show()
    //             //请求弹出
    //             $('.poprm').show()
    //         }else{
    //
    //         }
    //     }
    // });
    $('.modal_close').click(function () {
        $('.poprm').css('display', "none")
    })
    //查询
    $("#btnSearch").click(function () {
        me.init();
    });

    /*    $("input[type='radio']").click(function(){
     me.updateDeliveryType($(this).val());
     });*/
    //分仓保存
    $('.s-fcbtn').click(function () {
        var stockVal = $('#prc').val() || 0, grpVal = $('#pnc').val() || 0, isOpen = $('#fc').attr('checked') ? true : false, data = {};
        data.isSplit = isOpen;
        if (isOpen) {
            if (!stockVal && !grpVal) {
                $('#prcerr').show().find('span').text('请填写库存上限值');
                $('#pncerr').show().find('span').text('请填写库存缺口值');
                return
            }
            if (!stockVal) {
                $('#prcerr').show().find('span').text('请填写库存上限值');
                return
            } else if (stockVal < fcConfig.stockVal) {
                $('#prcerr').show().find('span').text('库存上限值不能低于' + fcConfig.stockVal + '万金');
                return
            } else {
                $('#prcerr').hide()
            }
            if (!grpVal) {
                $('#pncerr').show().find('span').text('请填写库存缺口值');
                return
            } else if (grpVal < fcConfig.grpVal) {
                $('#pncerr').show().find('span').text('库存缺口不能低于' + fcConfig.grpVal + '万金');
                return
            } else {
                $('#pncerr').hide()
            }
            data.repositoryCount = stockVal;
            data.needCount = grpVal;
        }
        $.ajax({
            type: "GET",
            url: baseServiceUrl + "services/shGameConfig/updateRepositoryLimit?t=" + new Date().getTime(),
            contentType: "application/json; charset=UTF-8",
            data: data,
            dataType: "json",
            success: function (resp) {
                alert('保存成功')
                me.getStock();
            }
        });

    })
    $('#prc,#pnc').blur(function () {
        var stockVal = $('#prc').val(), grpVal = $('#pnc').val();
        if ($('#fc').attr('checked') && (stockVal || grpVal)) {
            if (stockVal && (stockVal < fcConfig.stockVal)) {
                $('#prcerr').show().find('span').text('库存上限值不能低于' + fcConfig.stockVal + '万金')
            } else {
                setTimeout(function () {
                    $('#prcerr').hide()
                }, 100)
            }
            if (grpVal && (grpVal < fcConfig.grpVal)) {
                $('#pncerr').show().find('span').text('库存缺口不能低于' + fcConfig.grpVal + '万金')
            } else {
                setTimeout(function () {
                    $('#pncerr').hide()
                }, 100)
            }
        } else {
            setTimeout(function () {
                $('#prcerr').hide()
                $('#pncerr').hide()
            }, 100)
        }
    })
});
function fcKeyup(params) {
    return params = params.replace(/\D/g, '')
}
function fcOnafterpaste(params) {
    return params = params.replace(/\D/g, '')
}
var me = {
    newAccount: false,
    pageSize: 20,
    gameAccountList: null,
    purchaseOrderList: null,
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
    getStock: function () {
        $.ajax({
            type: "GET",
            url: baseServiceUrl + "services/shGameConfig/queryRepositoryLimit?t=" + new Date().getTime(),
            contentType: "application/json; charset=UTF-8",
            data: {
                gameName: '地下城与勇士',
                goodsTypeName: '游戏币'
            },
            dataType: "json",
            success: function (resp) {
                if (!resp.splitRepositorySwitch) {
                    $('.u-fcbox').hide();
                } else {
                    $('.u-fcbox').show();
                }
                if (resp.isSplit) {
                    $('#fc').attr('checked', true)
                } else {
                    $('#fc').attr('checked', false)
                }
                //fcConfig.stockVal = resp.purchaserRepositoryCount ? resp.purchaserRepositoryCount : resp.gameRepositoryCount;
                //fcConfig.grpVal = resp.purchaserNeedCount ? resp.purchaserNeedCount : resp.gameNeedCount
                if (resp.purchaserRepositoryCount && resp.purchaserNeedCount) {
                    $('#prc').val(resp.purchaserRepositoryCount);
                    $('#pnc').val(resp.purchaserNeedCount)
                }
                fcConfig.stockVal = resp.gameRepositoryCount;
                fcConfig.grpVal = resp.gameNeedCount;
                $('#prc').attr('placeholder', '最少' + fcConfig.stockVal);
                $('#pnc').attr('placeholder', '最少' + fcConfig.grpVal);
                if (!resp.isSplit) {
                    $('#prc').val('');
                    $('#pnc').val('');
                }
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
                    } else {
                        alert(resp.responseStatus.message);
                    }
                }
            }
        });
    },

    //获取收货方式
    getGameConfig: function () {
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
                    moneyName = resp.shGameConfig.unitName;
                    $(".moneyNameClass").html(moneyName);
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
                var code = resp.responseStatus.code;
                if (code == 31018) {
                    location.href = zbaoUrl + "/html/pay_withdrawals.html"
                }
                if (resp.purchaserData != null) {
                    console.log(resp);
                    var deliveryType = resp.purchaserData.deliveryType;
                    $("#deliveryType-" + deliveryType).attr("checked", "checked").unbind();
                    if (deliveryType != 1) {
                        $("#deliveryType-3").attr("checked", true).unbind();
                    }
                }
            }
        });
    },

    //修改收货状态
    updateDeliveryType: function (deliveryType) {
        var request = {};
        request.deliveryType = deliveryType;
        $.ajax({
            type: "GET",
            url: baseServiceUrl + "services/purchaseData/updateDeliveryType?t=" + new Date().getTime(),
            data: request,
            contentType: "application/json; charset=UTF-8",
            dataType: "jsonp",
            jsonp: "callback",
            success: function (resp) {
                $("#deliveryType-" + deliveryType).unbind();
                $("input[type='radio'][id!=deliveryType-" + deliveryType + "]").unbind().bind('click', function () {
                    me.updateDeliveryType($(this).val());
                    // window.location.reload();
                });
                var responseStatus = resp.responseStatus;
                var code = responseStatus.code;
                alert(responseStatus.message);
            }
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
        me.searchRequest.gameName = gameName;
        me.searchRequest.region = region;
        me.searchRequest.server = server;
        me.searchRequest.gameRace = "";
        me.searchRequest.pageSize = me.pageSize;
        me.searchRequest.deliveryType = 1;
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

            var htmlStr = "<tr class='txt mytr'><td width=\"25\" class=\"pl10\"><input type=\"checkbox\" name=\"msgId\" class=\"vam\" id=\"chk_" + purchaseOrder.id + "\" value=\"1fe58b4775734b7a8faeaea24a4174d9\"></td>";
            htmlStr += "<td width=\"200\" class=\"yxqf\">";
            htmlStr += "<span id=" + purchaseOrder.id + ">+</span>" + purchaseOrder.gameName + "/" + purchaseOrder.region + "/" + purchaseOrder.server;
            htmlStr += "<ul id=\"account_" + purchaseOrder.id + "\"></ul>";

            htmlStr += "</td>";
            htmlStr += "<td width=\"120\" align=\"center\" class=\"yxqf-count\">";
            htmlStr += "<input class=\"counts\"  type=\"text\" value=\"" + purchaseOrder.count + "\" disabled=\"disabled\"/>";
            htmlStr += "<ul id=\"countInput_" + purchaseOrder.id + "\"></ul>";

            htmlStr += "</td>";
            htmlStr += "<td width=\"120\" align=\"center\" class=\"price\"><input type=\"text\" value=\"" + purchaseOrder.price + "\" preValue=\"" + purchaseOrder.price + "\" size=\"8\"/>元/<span class='moneyNameClass'>" + purchaseOrder.moneyName + "</span></td>";
            htmlStr += "<td width=\"80\" align=\"center\" class='totalPrice' >" + toDecimal2(purchaseOrder.price * purchaseOrder.count) + "元</td>";
            if (purchaseOrder.isOnline != null && purchaseOrder.isOnline == true) {
                htmlStr += "<td width=\"50\" align=\"center\">已上架</td>";
                htmlStr += "<td width=\"110\" align=\"center\"><a href=\"javascript:void(0)\" class=\"btn_white\" onclick=\"me.saveData(" + purchaseOrder.id + ")\"><span>保存</span></a> <a href=\"javascript:void(0)\" onclick=\"me.offline([" + purchaseOrder.id + "])\" class=\"btn_white\"><span>下架</span></a></td>";
            } else {
                htmlStr += "<td width=\"50\" align=\"center\" style='color:red;'>已下架</td>";
                htmlStr += "<td width=\"110\" align=\"center\"><a href=\"javascript:void(0)\" class=\"btn_white\" onclick=\"me.saveData(" + purchaseOrder.id + ")\"><span>保存</span></a> <a href=\"javascript:void(0)\" onclick=\"me.online([" + purchaseOrder.id + "])\" class=\"btn_white\"><span>上架</span></a></td>";
            }

            htmlStr += "</tr>";
            $("#orderList tbody").append(htmlStr);
        }
        $(".moneyNameClass").html(moneyName);

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
    getDelivery: function () {
        $.ajax({
            type: "GET",
            url: baseServiceUrl + "services/purchaseData/getCurrentPurchaserData?" + new Date().getTime(),
            contentType: "application/json; charset=UTF-8",
            dataType: "json",
            success: function (resp) {
                var code = resp.responseStatus.code;
                if (code == 31018) {
                    location.href = zbaoUrl + "/html/pay_withdrawals.html"
                } else {
                    location.href = "gameAccount.html"
                }
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

            htmlStrInput += "<li id=" + gameAccount.id + " class='account_" + "true" + "_" + id + "'>";
            // if (gameAccount.isShRole) {
            htmlStrInput += "<input class=\"counts0\" type=\"text\" value=\"" + gameAccount.count + "\" size=\"18\"/>";
            // }
            htmlStrInput += "</li>";
        }
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
            else if (count < 1000) {
                alert("采购数量不能小于1000");
                return false;
            }
            //待写，最小采购数量需要小于采购数量
            //先隐藏，为了好测试
            // else if (count % 1000 != 0) {
            //     alert("采购数量只能1000的整数倍");
            //     return false;
            // }
        }
        return true;
    },
    //批量上架
    online: function (ids) {
        if (confirm('是否确定上架？')) {
            var request = {};
            request.ids = ids;
            var count = $("#" + ids).parents("tr").find(".yxqf-count").find("input").val();
            if (count == null || count <= 0) {
                alert('采购数量不能为空且必须大于0');
                return;
            }
            request.count = count;
            request.isOnline = true;
            $.ajax({
                type: "POST",
                url: baseServiceUrl + "services/purchaseOrder/setPurchaseOrderOnline",
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
    //批量下架
    offline: function (ids) {
        if (confirm('是否确定下架？')) {
            var request = {};
            var count = $("#" + ids).parents("tr").find(".yxqf-count").find("input").val();
            if (count == null || count <= 0) {
                alert('采购数量不能为空且必须大于0');
                return;
            }
            request.count = count;
            request.ids = ids;
            request.isOnline = false;
            $.ajax({
                type: "POST",
                url: baseServiceUrl + "services/purchaseOrder/setPurchaseOrderOnline",
                data: $.toJSON(request),
                contentType: "application/json; charset=UTF-8",
                dataType: "json",
                success: function (resp) {
                    var code = resp.responseStatus.code;
                    if (code == "00") {
                        alert("下架成功！");
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

        var count = $("#" + id).parents("tr").find(".yxqf-count").find("input").val();
        var price = $("#" + id).parents("tr").find(".price").find("input").val();

        // var dis = $("#" + id).parents("tr").find(".yxqf-count").find("input").attr("disabled");
        //
        // if(dis!="disabled"){
        //     alert("不能修改采购数量");
        //     return;
        // }

        //保存前的检验
        if (!me.checkValue(price, count)) {
            return false;
        }

        if (confirm('是否确定保存？')) {
            var request = {};
            request.id = id;
            request.price = price;
            request.count = count;
            request.gameAccountMap = gameAccountList;
            $.ajax({
                type: "POST",
                url: baseServiceUrl + "services/purchaseOrder/updatePurchaseOrderPriceAndCount",
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
