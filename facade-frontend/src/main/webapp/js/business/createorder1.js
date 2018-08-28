$(function () {
    Order.init();
});

var Order = {
    /**
     * 客服分页大小
     */
    pageSize: 8,
    /**
     * 所有客服
     */
    servicerList: null,
    /**
     * 选中的客服ID
     */
    selectedServicerId: null,
    /**
     * 商品信息
     */
    goodsInfo: null,
    /**
     * 最大库存
     */
    maxCount: 0,
    /**
     * 保险设置
     */
    insuranceSettings: null,
    /**
     * 历史订单
     */
    historyOrders: null,
    /**
     * 红包
     */
    redEnvelope: null,
    /**
     * 店铺券
     */
    shopCoupon: null,
    /**
     * 使用红包窗口
     */
    redEnvelopeDialog: null,
    /**
     * 使用店铺券窗口
     */
    shopCouponDialog: null,
    /**
     * 订单金额
     */
    orderAmount: 0.00,
    /**
     * 实付订单金额
     */
    actualOrderAmount: 0.00,
    /**
     * 游戏币购买数量
     */
    gamegoldCount: 0,
    /**
     * 最小购买金额
     */
    minBuyAmount: null,
    /**
     * 购买金额，参数传进来，如果空，默认minBuyAmount
     */
    buyCount: null,
    /**
     * 购买金额，参数传进来，如果空，默认minBuyAmount
     */
    buyAmount: null,
    /**
     * 进度条
     */
    progress: null,
    progress2: null,
    init: function () {
        var me = this;
        var params = me.getParams();
        var strGameName = "";
        var request = {};
        request.gameName = params.gameName;
        request.region = params.gameRegion;
        request.server = params.gameServer;
        request.gameRace = params.gameRace;
        request.goodsId = params.goodsId;
        request.goodsCat = params.goodsCat;
        request.goldCount = params.goldCount;
        request.goodsTypeName = params.goodsTypeName;//新增商品类目
        request.servicerType = UserType.CustomerService;
        request.loginAccount = params.seller;
        request.size = 8;
        $.ajax({
            type: "POST",
            url: baseServiceUrl + "services/order/htmlquery?t=" + new Date().getTime(),
            data: $.toJSON(request),
            contentType: "application/json; charset=UTF-8",
            dataType: "json",
            async: false,
            success: function (resp) {
                var code = resp.responseStatus.code;
                if (code == "00") {
                    me.servicerList = resp.userInfoEOs;
                    me.goodsInfo = resp.goodsInfo;
                    me.maxCount = resp.maxCount;
                    me.historyOrders = resp.orderInfoEOs;
                    me.insuranceSettings = resp.insuranceSettings;
                    me.minBuyAmount = isNull(resp.minBuyAmount) ? 1 : resp.minBuyAmount;

                    if (isNull(getUrlParam("buyAmount"))) {
                        if(isNull(getUrlParam("goldCount"))){
                            me.buyAmount = me.goldCount;
                        }else{
                            me.buyAmount = parseFloat(getUrlParam("goldCount"));
                        }
                    } else {
                        var buyAmount = parseFloat(getUrlParam("buyAmount"));
                        if (buyAmount < me.minBuyAmount) {
                            me.buyAmount = me.minBuyAmount
                        } else {
                            me.buyAmount = buyAmount;
                        }
                    }

                    var minBuyCount = me.getMiniBuyCount();

                    if (isNull(getUrlParam("goldCount"))) {
                        me.buyCount = minBuyCount;
                    } else {
                        var goldCount = parseInt(getUrlParam("goldCount"));
                        if (goldCount < minBuyCount) {
                            me.buyCount = minBuyCount;
                        } else {
                            me.buyCount = goldCount;
                        }
                    }

                    // 初始化页面
                    me.initPage();
                } else {
                    if (resp.responseStatus.code != "11") {
                        me.alert(resp.responseStatus.message);
                    }
                }
            }
        });
    },
    /**
     * 初始化页面
     */
    initPage: function () {
        var me = this;
        var params = me.getParams();
        if (isNull(params.gameName) || isNull(params.gameRegion) || isNull(params.gameServer)) {
            me.alert("参数错误，请重新下单！");
            return;
        }

        //判断商品是否为空
        if (isNull(me.goodsInfo) || me.goodsInfo.isDeleted == true) {
            alert("该商品未找到(已下架或网络问题)，请重新下单！");
            window.location.href = 'http://www.5173.com/';
        }

        //新增判断用户选择的购买方式 add by wubiao on 2017/3/25
        me.judgementBuyway(params);
        if (me.buyCount > me.maxCount && params.goodsCat != 2) {
            me.alert("卖家库存不足，当前价格最大购买数量：" + me.maxCount + "(" + me.goodsInfo.moneyName + ")，请重新填写数量或者返回选择其它商品。");
            // 购买金币数
            $(":input[name='buycount']").attr("value", me.maxCount);
        } else {
            $(":input[name='buycount']").attr("value", me.buyCount);
        }
        // 改变页面客服提示文字效果
        setInterval(function () {
            me.changeServiceTip();
        }, 300);

        // 获取客服第一页的数据
        me.buildServicerList(me.servicerList, 1);

        // 客服搜索事件
        me.bindServicerSearchEvents();
        // 添加QQ点击绑定事件
        $(".chose_service_qq").click(function () {
            // 客服详细信息
            var servicerInfo = me.getServicer(me.selectedServicerId);
            if (isNull(servicerInfo) || isNull(servicerInfo.qq)) {
                return;
            }
            window.open("http://wpa.qq.com/msgrd?v=3&uin=" + servicerInfo.qq + "&site=qq&menu=yes");
        });
        // 如果客服数量为1，默认选中
        if (!isNull(me.servicerList) && me.servicerList.length == 1) {
            var serviceInfo = me.servicerList[0];
            me.selectedServicerId = serviceInfo.id;
            me.selectServicer(serviceInfo.id);
        }

        // 修改友情提醒，改为后台动态配置
        var queryWarningRequest = {};
        queryWarningRequest.gameName = params.gameName;
        queryWarningRequest.goodsTypeName = params.goodsTypeName;

        $.ajax({
            type: "GET",
            url: baseServiceUrl + "services/gameCategory/queryWarning?t=" + new Date().getTime(),
            contentType: "application/json; charset=UTF-8",
            dataType: "json",
            data: queryWarningRequest,
            success: function (resp) {
                var responseStatus = resp.responseStatus;
                var code = responseStatus.code;
                result = resp.result;
                if (code == "00") {
                    if (!isNull(result) && !isNull(result.warning)) {
                        $("#remind").html(result.warning);
                    }
                }
                $("#remind a").attr('target', '_blank');
            },
            error: function (resp) {
                console.log(resp);
            }
        });
        // if (params.gameName == "地下城与勇士") {
        //     $("#dxcTipDiv").show();
        // } else if (params.gameName == "魔兽世界(国服)") {
        //     $("#mssjTipDiv").show();
        // } else if (params.gameName == "剑灵") {
        //     $("#jianlingTipDiv").show();
        // } else if (params.gameName == "QQ三国") {
        //     $("#qqsgTipDiv").show();
        // } else if (params.gameName == "剑侠情缘Ⅲ") {
        //     $("#jxqy3TipDiv").show();
        // } else if (params.gameName == "疾风之刃") {
        //     $("#jfzrTipDiv").show();
        // } else if (params.gameName == "斗战神") {
        //     $("#dzsTipDiv").show();
        // } else if (params.gameName == "天涯明月刀") {
        //     $("#tymydTipDiv").show();
        // } else if (params.gameName == "龙之谷") {
        //     $("#lzgTipDiv").show();
        // } else if (params.gameName == "上古世纪") {
        //     $("#sgsjTipDiv").show();
        // } else if (params.gameName == "怪物猎人OL") {
        //     $("#gulrolTipDiv").show();
        // } else if (params.gameName == "冒险岛2") {
        //     $("#mxd2TipDiv").show();
        // } else {
        //     $("#buyTipDiv").hide();
        // }

        // 显示游戏区服信息
        var serverInfo = params.gameName + "->" + params.gameRegion + "->" + params.gameServer;
        if (params.gameRace != null) {
            serverInfo += "->" + params.gameRace;
        }
        serverInfo += "->" + params.goodsTypeName;
        $("#goodsServerInfo").html(serverInfo);
        strGameName = params.gameName
        if(params.gameName=="地下城与勇士"){
            $('.dnfYouJi').show();
            $('.dnfYouJiName').show();
        }else{
            $('.dnfYouJiFirst').hide();
            $('.dnfYouJi').hide();
        };
        // 显示单价
        if (me.goodsInfo.moneyName == "" || me.goodsInfo.moneyName == null || typeof (me.goodsInfo.moneyName) == "undefined") {
            var price1 = "1元=" + toDecimal2(1 / parseFloat(me.goodsInfo.unitPrice)) + me.goodsInfo.moneyName + me.goodsInfo.goodsTypeName; // 1元兑换多少游戏币
            var price2 = parseFloat(me.goodsInfo.unitPrice).toFixed(5) + "元/" + me.goodsInfo.moneyName + me.goodsInfo.goodsTypeName; // 1游戏币兑换多少元
        } else {
            var price1 = "1元=" + toDecimal2(1 / parseFloat(me.goodsInfo.unitPrice)) + me.goodsInfo.moneyName; // 1元兑换多少游戏币
            var price2 = parseFloat(me.goodsInfo.unitPrice).toFixed(5) + "元/" + me.goodsInfo.moneyName; // 1游戏币兑换多少元
        }
        $("#goodsUnitPrice").html(price1 + "&nbsp;" + price2);

        // 显示剩余库存
        var repositoryBalance = 0;
        if (params.goodsCat == 1 || params.goodsCat == 3) {
            repositoryBalance = me.maxCount + me.goodsInfo.moneyName;
        } else {
            repositoryBalance = "充足";
        }
        $("#goodsRepository").html(repositoryBalance);
        // 是否显示游戏交易方式、游戏等级
        $(":input[name='roleLevel']").css("ime-mode", "disabled");
        $(":input[name='roleLevel']").bind("keypress", me.restrictKeyInput);
        $(":input[name='roleLevel']").tooltip({
            content: "请输入角色等级",
            position: 'right'
        });
        if (params.gameName == "魔兽世界(国服)") {
            $("#tradeTypeLine").show();
            $("#gameRoleLevelLine").show();
        } else if (params.gameName == "疾风之刃") {
            $("#gameRoleLevelLine").show();
        } else if (params.gameName == '地下城与勇士' && params.goodsTypeName == '挑战书') {
            $("#gameRoleLevelLine").show();
        } else if (params.gameName == '新天龙八部' && params.goodsTypeName == '元宝票') {
            $("#account_numberId").show();
        }
        // 购买方式改变事件
        $(":radio[name='buyway']").bind("change", function () {
            me.changeBuyway();
        });
        // 购买数量或购买金额
        $(":input[name='buycount']").css("ime-mode", "disabled");
        $(":input[name='buycount']").bind("keypress", me.restrictKeyInput);
        $(":input[name='buycount']").bind("input propertychange", function () {
            me.countOrderAmount();
        });
        $(":radio[name='buyway']").trigger("change"); // 触发购买方式改变事件初始化购买金额信息
        // if (!isNull(params.goldCount)) {
        //     // 传进来的购买数量不为空
        //     var goldCount = params.goldCount;
        //     var minGoldCount = me.getMiniBuyCount(); // 最小购买数量
        //     if (goldCount >= minGoldCount) {
        //         $(":input[name='buycount']").val(goldCount)
        //     } else {
        //         $(":input[name='buycount']").val(minGoldCount)
        //     }
        // }
        // 设置收货角色名
        if (me.hasHistoryGameRole()) {
            // 有历史订单的，不显示填写和确认收货角色名
            $("#inputReceivingRoleLine1").hide();
            $("#inputReceivingRoleLine2").hide();

            // 显示收货角色名选择块
            me.createReceivingRoles();
        } else {
            // 没有历史订单的，显示填写和确认收货角色名
            $("#inputReceivingRoleLine1").show();
            $("#inputReceivingRoleLine2").show();
        }

        $(":input[name='receivingRole1']").tooltip({
            content: "请填写收货的游戏角色名",
            position: 'right'
        });
        $(":input[name='receivingRole2']").tooltip({
            content: "请确认收货的游戏角色名",
            position: 'right'
        });

        // 电话
        $(":input[name='phone']").css("ime-mode", "disabled");
        $(":input[name='phone']").bind("keypress", me.restrictKeyInput);
        $(":input[name='phone']").tooltip({
            content: "建议输入手机，固话或小灵通请加区号（例：02188888888）",
            position: 'right'
        });

        // QQ
        $(":input[name='qq']").css("ime-mode", "disabled");
        $(":input[name='qq']").bind("keypress", me.restrictKeyInput);
        $(":input[name='qq']").tooltip({
            content: "请填写您用来联系客服的QQ号码，交易时仅认准此号码（请将QQ号码设为主显帐号）",
            position: 'right'
        });
        if (me.hasHistoryGameRole()) {
            var o = me.historyOrders[0];
            // 自动填充订单收获信息
            $(":input[name='phone']").val(o.mobileNumber);
            $(":input[name='qq']").val(o.qq);
            // $(":input[name='roleLevel']").val(o.gameLevel);
        }

        // 使用红包
        $("#useRedEnvelope").bind('click', function () {
            me.useRedEnvelopeEvent();
        });

        // 购买店铺商品时，显示“使用店铺券”选项
        if (me.goodsInfo.goodsCat == 3) {
            $("#useShopCouponContainer").show();
            // 使用店铺券
            $("#useShopCoupon").bind('click', function () {
                me.useShopCouponEvent();
            });
        }

        // 是否显示商品保障险
        if (me.insuranceSettings != null && me.insuranceSettings.enabled) {
            $("#spbzx_title").show();
            $("#spbzx_content").show();
            $("#spbzx_info").show();

            // 默认选中购买保险按钮
            /*if (params.gameName == "斗战神"
             || params.gameName == "魔兽世界(国服)"
             || params.gameName == "QQ三国"
             || params.gameName == "剑灵"
             || params.gameName == "剑侠情缘Ⅲ"
             || params.gameName == "疾风之刃"
             || params.gameName == "龙之谷") {
             $(":radio[name='picc'][value='buyPicc']").prop("checked", true);
             }*/
        }
        // 商品保障险购买/不购买
        $(":radio[name='picc']").bind('change', function () {
            me.countOrderAmount();
        });

        // 计算订单金额
        me.countOrderAmount();

        $(window).scroll(function () {
            if (me.progress != null) {
                // 进度条随滚动条垂直居中显示
                var position = {top: $(document).scrollTop() + ($(window).height() - 250) * 0.5};
                me.progress.window("move", position);
            }
            /*if (me.progress2 != null) {
             // 进度条随滚动条垂直居中显示
             var position = {top:$(document).scrollTop() + ($(window).height()-250) * 0.5};
             me.progress2.window("move", position);
             }*/
        });

        // 表单提交
        $("#submitOrder").bind('click', function () {
            if (me.validate()) {
                /*if (me.selectedServicerId == null) {
                 $("#progress2_time").text(6);
                 var time = 5;
                 var intervalId = setInterval(function(){
                 if (time == -1) {
                 // 关闭没有选择客服提示
                 clearInterval(intervalId);
                 me.progress2.dialog('close');
                 // 提交表单
                 me.showSubmitProgress();
                 me.submit();
                 } else {
                 $("#progress2_time").text(time--);
                 }
                 },1000);

                 me.progress2 = $("#progress2").dialog({
                 width: 350,
                 height: 150,
                 border: false,
                 noheader: true,
                 closable: false,
                 modal: true,
                 buttons: [{
                 text: '立即付款',
                 iconCls: 'icon-ok',
                 handler: function () {
                 // 关闭没有选择客服提示
                 clearInterval(intervalId);
                 me.progress2.dialog('close');
                 // 提交表单
                 me.showSubmitProgress();
                 me.submit();
                 }
                 },{
                 text: '挑选客服',
                 iconCls: 'icon-cancel',
                 handler: function () {
                 // 关闭没有选择客服提示
                 clearInterval(intervalId);
                 me.progress2.dialog('close');
                 }
                 }]
                 }).window('center');
                 $("#progress2").show();
                 } else {
                 // 提交表单
                 me.showSubmitProgress();
                 me.submit();
                 }*/

                // 提交表单
                me.showSubmitProgress();
                me.submit();
            }
        });
    },
    showSubmitProgress: function () {
        var me = this;
        // 显示进度条提示
        me.progress = $("#progress").dialog({
            width: 250,
            height: 55,
            border: false,
            noheader: true,
            closable: false,
            modal: true
        }).window('center');
        $("#progress").show();
    },
    /**
     * 获取URL参数
     */
    getParams: function () {
        var params = {
            gameName: $.trim(getUrlParam("gameName")),
            gameRegion: $.trim(getUrlParam("gameRegion")),
            gameServer: $.trim(getUrlParam("gameServer")),
            gameRace: isNull(getUrlParam("gameRace")) ? null : $.trim(getUrlParam("gameRace")),
            gameId: isNull(getUrlParam("gameId")) ? null : getUrlParam("gameId"),
            regionId: isNull(getUrlParam("regionId")) ? null : getUrlParam("regionId"),
            serverId: isNull(getUrlParam("serverId")) ? null : getUrlParam("serverId"),
            raceId: isNull(getUrlParam("raceId")) ? null : getUrlParam("raceId"),
            goldCount: getUrlParam("goldCount"),
            goodsCat: isNull(getUrlParam("goodsCat")) ? null : getUrlParam("goodsCat"),
            goodsId: isNull(getUrlParam("goodsId")) ? null : getUrlParam("goodsId"),
            refererType: getUrlParam("refererType"),
            internetBar: getUrlParam("internetBar"),
            seller: isNull(getUrlParam("seller")) ? null : getUrlParam("seller"),
            //新增参数用于判断用户购买方式 create by wubiao on 2017/3/23************start
            buyway: getUrlParam("buyway"),
            //************************************************************************end
            goodsTypeName: $.trim(getUrlParam("goodsTypeName")) //新增商品类目
        };
        return params;
    },

    /**
     * 判断用户选择的购买方式
     */
    judgementBuyway: function (params) {
        //参数判断
        if (params.buyway == 'buyyuan') {
            //状态为buyyuan显示按金额购买
            this.buyyuan(params);
        } else {
            //默认情况下显示按数量购买
            this.buycount(params);
        }
    },

    /**
     * 按数量购买
     * @param goldCount
     */
    buycount: function (params) {
        var me = this;
        //将前台传递的参数写入当前文本框中
        //$("input[name='buycount']").val(me.buyAmount);
        //将后台获取的购买单位切换
        if (me.goodsInfo != null && me.goodsInfo.moneyName != null && me.goodsInfo.moneyName != undefined) {
            $("#buyWayUnitSpan").text(me.goodsInfo.moneyName);
        } else {
            $("#buyWayUnitSpan").text("");
        }
    },

    /**
     * 按金额购买
     * @param buyAmount
     */
    buyyuan: function (params) {

        var me = this;
        //改变选中样式
        $("input[name='buyway']")[1].checked = 'checked';
        //将前台传递的金额写入文本框中
        $("input[name='buycount']").val(me.buyAmount);
        $("#buyWayLabel").text("购买金额：");
        //购买的单位切换
        if (me.goodsInfo != null && me.goodsInfo.moneyName != null && me.goodsInfo.moneyName != undefined) {
            $("#buyWayUnitSpan").text(me.goodsInfo.moneyName);
        } else {
            $("#buyWayUnitSpan").text(" 元");
        }
        //金币数量和单位换算
        var count,unitPrice;
        if (params.goldCount != null && params.goldCount != undefined) {
            //金币数量存在直接写入文本框
            count = params.goldCount;
            if (me.goodsInfo != null && me.goodsInfo.moneyName != null && me.goodsInfo.moneyName != undefined) {
                $("#gamegold_count").text(count + me.goodsInfo.moneyName);
            } else {
                $("#gamegold_count").text(count);
            }
        } else if (me.goodsInfo != null && me.goodsInfo.unitPrice != null && me.goodsInfo.unitPrice != undefined) {
            //如果参数中的金币数量为空后台计算金币单价
            unitPrice = 1 / parseFloat(me.goodsInfo.unitPrice);
            //计算购买的金币总数
            count = me.buyAmount * unitPrice;
            if (me.goodsInfo != null && me.goodsInfo.moneyName != null && me.goodsInfo.moneyName != undefined) {
                $("#gamegold_count").text(count + me.goodsInfo.moneyName);
            } else {
                $("#gamegold_count").text(count);
            }
        } else {
            //如果金币单价和金币数量都不存在
            count = "该商品单价不存在";
            $("#gamegold_count_line td").eq(1).text(count);
        }

        //样式切换
        $("#gamegold_count_line").show();
    },

    /**
     * 创建客服分页数据
     * @param servicerList 客服列表
     * @param currentPage 要显示第几页的客服数据
     */
    buildServicerList: function (servicerList, currentPage) {
        var me = this;
        if (isNull(servicerList)) return;

        // 先清空所有的客服列表
        $(".chose_service_list").html('');
        $(".service_box").css("display", "none");
        me.selectedServicerId = null;

        // 计算总分页
        var totalPages = Math.ceil(servicerList.length / me.pageSize);
        var pages = '';
        for (var i = 1; i <= totalPages; i++) {
            pages += '<li><a ' + ((currentPage == i) ? 'class="current_page"' : '') + '>' + i + '</a></li>';
        }
        $(".chose_servie_ul").html(pages);

        // 客服分页点击事件
        me.bindPageClickEvents(servicerList);

        var start = (currentPage - 1) * me.pageSize;
        var end = Math.min(servicerList.length, currentPage * me.pageSize);
        for (var i = start; i < end; i++) {
            var servicer = servicerList[i];
            // 该客服被禁用，不显示
            if (servicer.isDeleted == true) {
                continue;
            }

            var avatar = isNull(servicer.avatarUrl) ? "img/head.png" : buildImageUrl(servicer.avatarUrl, "64x64");
            var name = isNull(servicer.nickName) ? "公主" : servicer.nickName;
            var sign = isNull(servicer.sign) ? "心随你动 服务在心" : servicer.sign;
            var serviceCharge = isNull(servicer.serviceCharge) ? 0 : servicer.serviceCharge;
            var goodRate = isNull(servicer.goodRate) ? 100 : servicer.goodRate;
            var starNum = isNull(servicer.star) ? 0 : servicer.star;

            var html = "<div id='servicer_" + servicer.id + "' class='dbkf chose_service_block'>";
            html += "<div class='chose_service_img'><img src='" + avatar + "' /></div>";
            html += "<div class='chose_service_infor'>";
            html += "<div class='chose_service_name'>客服：<strong>" + name + "</strong></div>";
            html += "<div id='chose_service_star_" + i + "' class='chose_service_star'></div>";
            html += "<div class='chose_serive_word'>" + sign + "</div>";
            html += "</div>";
            html += "<div class='service_fee'>";
            html += "<div class='service_fee_right'>好评率：<span class='service_fee_span'>" + goodRate + "%</span> 服务费：<span class='service_fee_span'>￥" + serviceCharge + "</span></div>";
            html += "</div>";
            html += "</div>";
            $(".chose_service_list").append(html);

            if (starNum != 0) {
                //给每位客服设置对应的星级,星级为0的不显示星级
                $('#chose_service_star_' + i).raty({
                    hintList: ['', '', '', '', '', '', ''],
                    number: starNum,
                    start: starNum,
                    readOnly: true
                });
            }
        }

        // 绑定客服点击事件
        me.bindServicerClickEvents();
    },
    /**
     * 绑定搜索客服事件
     */
    bindServicerSearchEvents: function () {
        var me = this;
        $("#search_button").click(function () {
            var text = $.trim($("#search").val());
            if (isNull(text)) {
                me.buildServicerList(me.servicerList, 1);
                return;
            }
            var flag = false;
            var regStr = new RegExp(".*?" + text + ".*?");
            var selectServiceList = new Array();
            for (var i = 0; i < me.servicerList.length; i++) {
                if (regStr.test(me.servicerList[i].nickName) || regStr.test(me.servicerList[i].qq)) {
                    flag = true;
                    selectServiceList.push(me.servicerList[i]);
                }
            }
            if (!flag) {
                me.alert("您搜索的客服不存在，请输入正确的客服昵称！");
            } else {
                $(".chose_service_block").remove();
                me.buildServicerList(selectServiceList, 1);
            }
        });
    },
    /**
     * 绑定客服分页点击事件
     */
    bindPageClickEvents: function (servicerList) {
        var me = this;
        // 分页点击事件
        $(".chose_servie_ul li a").bind('click', function () {
            var page = $(this).html(); // 获取第几页数值
            me.buildServicerList(servicerList, page);
        });
    },
    /**
     * 绑定客服点击事件
     */
    bindServicerClickEvents: function () {
        var me = this;
        $("div.dbkf").bind('click', function () {
            var servicerId = $(this).attr("id").split("_")[1];
            me.selectServicer(servicerId);
        });
    },
    /**
     * 选中客服
     * @param servicerId
     */
    selectServicer: function (servicerId) {
        var me = this;
        me.selectedServicerId = servicerId;

        // 删除其他客服的选中css
        $(".chose_service_block").removeClass("chose_service_block_chosed");
        $(".span_chosed").remove();
        // 设置当前客服选择css
        $("#servicer_" + servicerId).addClass("chose_service_block_chosed");
        $("#servicer_" + servicerId + " .chose_service_infor").after("<span class='span_chosed'></span>");
        $(".service_box").css("display", "block");

        if ($("#servicer_" + servicerId).length <= 0) {
            return;
        }

        // 补充客服详细信息
        var servicerInfo = me.getServicer(servicerId);

        var avatarUrl = isNull(servicerInfo.avatarUrl) ? "img/head.png" : buildImageUrl(servicerInfo.avatarUrl, "64x64");
        $("#service_avatar").attr("src", avatarUrl);

        var nickName = isNull(servicerInfo.nickName) ? "公主" : servicerInfo.nickName;
        $(".service_name").html(nickName);

        $(".service_word").html("<p>尊敬的买家，很高兴为您服务,购买过程中有任何问题，请及时联系" + nickName + "。</p>");

        var qq = isNull(servicerInfo.qq) ? "" : servicerInfo.qq;
        $(".service_qq").html("QQ：<strong>" + qq + "</strong>");

        // var phoneNumber = isNull(servicerInfo.phoneNumber) ? "" : servicerInfo.phoneNumber;
        // $(".chose_service_phone").html("手机<strong>" + phoneNumber + "</strong>");
        //
        // var weiXin = isNull(servicerInfo.weiXin) ? "" : servicerInfo.weiXin;
        // $(".chose_service_wechat").html("微信<strong>" + weiXin + "</strong>");

        var yy = isNull(servicerInfo.yy) ? "" : servicerInfo.yy;
        if (yy != "") {
            $(".chose_service_yy").html("YY<strong>" + yy + "</strong>");
        }
        else {
            $(".chose_service_yy").html("");
        }

        me.countOrderAmount();
    },
    /**
     * 根据ID获取客服信息
     * @param servicerId
     */
    getServicer: function (servicerId) {
        var me = this;
        if (me.servicerList != null && me.servicerList.length > 0) {
            for (var i = 0; i < me.servicerList.length; i++) {
                var servicer = me.servicerList[i];
                if (servicer.id == servicerId) {
                    return servicer;
                }
            }
        }
        return null;
    },
    /**
     * 改变购买方式
     */
    changeBuyway: function () {
        var me = this;
        var buyway = $(":radio[name='buyway']:checked").val();
        //me.judgementBuyway(buyway);
        if (buyway == "buyyuan") {
            $("#buyWayLabel").html("购买金额：");
            // 设置购买的游戏币金额
            $(":input[name='buycount']").val(parseInt(me.orderAmount));
            $("#buyWayUnitSpan").html("元");
            $(":input[name='buycount']").tooltip({
                content: "最低购买金额" + this.getMiniBuyCount() + "元",
                position: 'right'
            });
            // 显示游戏币数量
            $("#gamegold_count_line").show();
        } else {
            $("#buyWayLabel").html("购买数量：");
            // 设置购买的游戏币数量
            $(":input[name='buycount']").val(me.buyAmount);
            if (me.goodsInfo.moneyName == null || me.goodsInfo.moneyName == "" || typeof (me.goodsInfo.moneyName) == "undefined") {
                $("#buyWayUnitSpan").html(me.goodsInfo.goodsTypeName);
            } else {
                $("#buyWayUnitSpan").html(me.goodsInfo.moneyName);

            }
            $(":input[name='buycount']").tooltip({
                content: "最低购买数量" + this.getMiniBuyCount() + me.goodsInfo.moneyName,
                position: 'right'
            });
            // 隐藏游戏币数量
            $("#gamegold_count_line").hide();
        }
        me.countOrderAmount();
    },
    /**
     * 创建收货角色名列表
     */
    createReceivingRoles: function () {
        var me = this;
        if (me.hasHistoryGameRole()) {
            var orders = me.historyOrders;
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
                    var option = $('<option value="' + order.receiver + '" title="' + order.receiver + '">' + order.receiver + "</option>");
                    $("#receivingRolesCombo").append(option);
                }
            }
        }

        $('<option value="useOther" class="other">使用其他收货角色名</option>').appendTo("#receivingRolesCombo");

        // 添加事件
        $("#receivingRolesCombo").bind('change', me.changeReceivingRole);

        // 显示我的收货角色名下拉菜单
        $("#selectReceivingRolesLine").show();
        if(strGameName=="地下城与勇士"){
            $('.dnfYouJiFirst').show()
            $('.dnfYouJi').hide()
        }
    },
    /**
     * 改变收货角色名事件
     */
    changeReceivingRole: function () {
        var value = $(this).val();
        if (value == "useOther") {// 使用其他收货角色名
            // 显示填写和确认收货角色名
            $("#inputReceivingRoleLine1").show();
            $("#inputReceivingRoleLine2").show();
            if(strGameName=="地下城与勇士"){
                $('.dnfYouJiFirst').show()
                $('.dnfYouJi').hide()
            }

        } else {
            // 隐藏填写和确认收货角色名
            $("#inputReceivingRoleLine1").hide();
            $("#inputReceivingRoleLine2").hide();
            if(strGameName=="地下城与勇士"){
                $('.dnfYouJiFirst').show()
                $('.dnfYouJi').hide()
            }
        }
    },
    /**
     * 验证
     * @returns {boolean}
     */
    validate: function () {
        var me = this;
        var params = me.getParams();
        /*if (me.selectedServicerId == null) {
         me.alert("请选择为您服务的客服！");
         return false;
         }*/

        // 验证购买数量或购买金额是否正确
        if (!me.validateBuycount())
            return false;

        // 购买方式
        var buyway = $(":radio[name='buyway']:checked").val();
        // 购买数量或购买金额
        var buycount = $(":input[name='buycount']").val();
        if (buycount < me.getMiniBuyCount()) {
            if (buyway == "buyyuan") {
                me.alert("最低购买金额" + this.getMiniBuyCount() + "元");
                return false;
            } else {
                me.alert("最低购买数量" + this.getMiniBuyCount() + this.goodsInfo.moneyName);
                return false;
            }
        } else if (me.goodsInfo.goodsCat != 2) {
            if (buyway == "buyyuan") {
                // 按元购买
                var totalCount = toDecimal2(buycount / parseFloat(me.goodsInfo.unitPrice));
                totalCount = Math.ceil(totalCount);
                if (totalCount > me.maxCount) {
                    me.alert("库存不足，当前价格剩余库存:" + me.maxCount);
                    return false;
                }
            } else {
                // 按数量购买
                if (buycount > me.maxCount) {
                    me.alert("库存不足，当前价格剩余库存:" + me.maxCount);
                    return false;
                }
            }
        }

        // 收货角色名
        var needInputReceivingRole = true; // 是否需要填写收货角色名
        if (me.hasHistoryGameRole()) {
            var v = $("#receivingRolesCombo").val();
            if (v == -1) {
                me.alert("请选择您的收货角色名！");
                needInputReceivingRole = true;
                return false;
            } else if (v == 'useOther') { // 使用其他收货角色名
                needInputReceivingRole = true;
            } else {
                needInputReceivingRole = false;
            }
        }
        if (needInputReceivingRole) {
            var gameRole1 = $.trim($(":input[name='receivingRole1']").val());
            if (gameRole1 == '') {
                me.alert("请填写收货的游戏角色名！");
                return false;
            }
            var gameRole2 = $.trim($(":input[name='receivingRole2']").val());
            if (gameRole2 == '') {
                me.alert("请确认收货的游戏角色名！");
                return false;
            }
            if (gameRole1 != gameRole2) {
                me.alert("2次填写的收货角色名不一致，请重新填写！");
                return false;
            }
        }

        //游戏等级
        var roleLevel = $(":input[name='roleLevel']").val();
        if (params.gameName == "魔兽世界(国服)" || params.gameName == "疾风之刃" || (params.gameName == '地下城与勇士' && params.goodsTypeName == '挑战书')) {
            var regex = /^[0-9]{1,5}$/;
            if (isNull(roleLevel) || !regex.test(roleLevel)) {
                me.alert("请输入正确角色等级！");
                return false;
            }
            if (roleLevel <= 0) {
                me.alert("角色等级不能小于1！");
                return false;
            }
        }

        //数字id
        if (params.gameName == '新天龙八部' && params.goodsTypeName == '元宝票') {
            var accountNumberID = $.trim($(":input[name='accountNumberID']").val());
            if (accountNumberID == '' || isNull(accountNumberID)) {
                me.alert("请输入正确的收货角色数字ID");
                return false;
            }
        }

        // 电话
        var phone = $.trim($(":input[name='phone']").val());
        if (phone == '') {
            me.alert("建议输入手机，固话或小灵通请加区号（例：02188888888）");
            return false;
        } else {
            var passed = false;
            var pattern = /^1\d{10}$/;
            if (pattern.test(phone)) {
                // 判断手机号码
                passed = true;
            } else {
                // 判断电话号码
                pattern = /^0(([1,2]\d)|([3-9]\d{2}))\d{7,8}$/;
                if (pattern.test(phone)) {
                    passed = true;
                } else {
                    passed = false;
                }
            }
            if (!passed) {
                // me.alert('手机号码请以<em class="red">13</em>、<em class="red">14</em>、<em class="red">15</em>、<em class="red">17</em>、<em class="red">18</em>开头(暂时只支持国内手机号码)\r\n固话或小灵通请加区号(例：02188888888)。');
                me.alert("电话号码格式错误（仅支持国内手机号码或固话，固话前请加区号，如02188888888）");
                return false;
            }
        }

        // QQ
        var qq = $(":input[name='qq']").val();
        if (qq == '') {
            me.alert("请填写您用来联系客服的QQ号码，交易时仅认准此号码（请将QQ号码设为主显帐号）。");
            return false;
        }

        // 使用红包
        if (me.redEnvelope != null) {
            // 验证是否有效
            var request = {"code": me.redEnvelope.code, "pwd": me.redEnvelope.pwd, "type": 1, "amount": me.orderAmount};
            var response = me.validateDiscountCoupon(request);
            var responseCode = response.responseStatus.code;
            if (responseCode != "00" || !response.valid) {
                me.alert(response.responseStatus.message);
                return false;
            }
        }
        // 使用店铺券
        if (me.shopCoupon != null) {
            // 验证是否有效
            var request = {"code": me.shopCoupon.code, "pwd": me.shopCoupon.pwd, "type": 2, "amount": me.orderAmount};
            var response = me.validateDiscountCoupon(request);
            var responseCode = response.responseStatus.code;
            if (responseCode != "00" || !response.valid) {
                me.alert(response.responseStatus.message);
                return false;
            }
        }

        //是否选中客服
        // if(me.selectedServicerId == null){
        //     me.alert('请选择为您服务的客服');
        //     return false;
        // }

        return true;
    },
    /**
     * 验证购买数量或购买金额是否正确
     * @returns {boolean}
     */
    validateBuycount: function () {
        var me = this;
        // 购买方式
        var buyway = $(":radio[name='buyway']:checked").val();
        // 购买数量或购买金额
        var count = $(":input[name='buycount']").val();
        if (count == '') {
            if (buyway == 'buycount') {
                me.alert("请填写购买数量！");
                return false;
            } else {
                me.alert("请填写购买金额！");
                return false;
            }
        } else {
            var regtext = /^[1-9]+\d*$/;
            if (!regtext.test(count)) {
                if (buyway == 'buycount') {
                    me.alert("购买数量请填写正整数！");
                    return false;
                } else {
                    me.alert("购买金额请填写正整数！");
                    return false;
                }
            }
        }
        return true;
    },
    /**
     * 是否有历史收货角色名
     * @returns {boolean}
     */
    hasHistoryGameRole: function () {
        if (!isNull(this.historyOrders) && this.historyOrders.length > 0)
            return true;
        return false;
    },
    /**
     * 提交订单
     */
    submit: function () {
        var me = this;
        var params = me.getParams();
        var request = {};
        request.gameName = params.gameName;
        request.region = params.gameRegion;
        request.server = params.gameServer;
        request.gameRace = params.gameRace;
        request.gameId = params.gameId;
        request.regionId = params.regionId;
        request.serverId = params.serverId;
        request.raceId = params.raceId;
        request.title = me.goodsInfo.title;
        request.goodsCat = params.goodsCat;
        request.goodsId = me.goodsInfo.id;
        request.unitPrice = me.goodsInfo.unitPrice;
        request.goldCount = me.gamegoldCount;
        request.servicerId = me.selectedServicerId;
        request.mobileNumber = $(":input[name='phone']").val();
        request.qq = $(":input[name='qq']").val();
        request.receiver = me.getReceiver();
        //地下城与勇士 挑战书的 要游戏等级
        request.gameLevel = $(":input[name='roleLevel']").val();
        //新天龙八部 元宝票的要收货角色数字ID
        request.gameNumberId = $.trim($(":input[name='accountNumberID']").val());
        request.sellerLoginAccount = me.goodsInfo.sellerLoginAccount;
        request.moneyName = me.goodsInfo.moneyName;
        request.refererType = params.refererType;
        request.internetBar = params.internetBar;
        request.goodsTypeName = params.goodsTypeName; //新增通货商品

        // 如果是魔兽世界(国服)，则提交交易方式
        if (params.gameName == "魔兽世界(国服)" || params.gameId == "880") {
            request.tradeType = $(":radio[name='tradeType']:checked").val();
        }

        if (me.insuranceSettings == null || me.insuranceSettings.enable == false || $(':radio[name="picc"]:checked').val() == 'notBuyPicc') {
            request.isBuyInsurance = false;
        } else if ($(':radio[name="picc"]:checked').val() == 'buyPicc') {
            request.isBuyInsurance = true;
        }

        if (isNull(me.goodsInfo.deliveryTime)) {
            request.deliveryTime = 20;
        } else {
            request.deliveryTime = me.goodsInfo.deliveryTime;
        }

        // 使用红包
        if (me.redEnvelope != null) {
            request.redEnvelopeCode = me.redEnvelope.code;
            request.redEnvelopePwd = me.redEnvelope.pwd;
        }
        // 使用店铺券
        if (params.goodsCat == 3 && me.shopCoupon != null) {
            request.shopCouponCode = me.shopCoupon.code;
            request.shopCouponPwd = me.shopCoupon.pwd;
        }

        $.ajax({
            type: "POST",
            url: baseServiceUrl + "services/order/addorder",
            data: $.toJSON(request),
            contentType: "application/json; charset=UTF-8",
            dataType: "json",
            beforeSend: function (request) {
                request.setRequestHeader("5173_authkey", getAuthkey());
            },
            success: function (resp) {
                var responseStatus = resp.responseStatus;
                var code = responseStatus.code;
                if (code == "00") {
                    var orderInfo = resp.orderInfo;
                    // 请求支付
                    window.location.href = baseServiceUrl + "payment?orderId=" + orderInfo.orderId;
                } else {
                    if (me.progress != null) {
                        me.progress.window('close');
                    }
                    /*if (me.progress2 != null) {
                     me.progress2.window('close');
                     }*/
                    me.alert(responseStatus.message);
                }
            }
        });
    },
    /**
     * 获取收货角色名
     */
    getReceiver: function () {
        var me = this;
        var receiver = null;
        if (me.hasHistoryGameRole()) {
            var v = $("#receivingRolesCombo").val();
            if (v != -1 && v != "useOther") {
                receiver = v;
            } else if (v == "useOther") {
                receiver = $.trim($(":input[name='receivingRole1']").val());
            }
        } else {
            receiver = $.trim($(":input[name='receivingRole1']").val());
        }
        return receiver;
    },
    /**
     * 计算订单金额
     */
    countOrderAmount: function () {
        var me = this;
        var buycount = $(":input[name='buycount']").val();
        if (buycount == '') {
            return 0;
        } else {
            buycount = parseFloat(buycount);
        }
        if (!me.validateBuycount()) return 0;

        // 数量不超过99999999
        if (parseInt(buycount) >= 99999999) {
            buycount = 99999999;
            $(":input[name='buycount']").val(99999999);
        }

        me.orderAmount = 0.00; // 初始化订单金额为0.00元
        me.actualOrderAmount = 0.00; // 初始化订单实际付款金额为0.00元
        var buyPiccOrNot = $(':radio[name="picc"]:checked').val();
        var buyway = $(":radio[name='buyway']:checked").val();

        // 保险配置开启情况
        if (me.insuranceSettings != null && me.insuranceSettings.enabled) {
            if (buyPiccOrNot == "buyPicc") {
                var rate = me.insuranceSettings.rate;
                var floor = me.insuranceSettings.floor;
                var ceiling = me.insuranceSettings.ceiling;
                // 计算购买保险费用
                var piccFee = 0.00;
                if (buyway == 'buycount') {
                    piccFee = parseFloat(me.goodsInfo.unitPrice) * buycount * rate / 100;
                } else if (buyway == 'buyyuan') {
                    piccFee = buycount * rate / 100;
                }

                if (piccFee < floor) {
                    piccFee = floor;
                }
                if (piccFee > ceiling) {
                    piccFee = ceiling;
                }

                $("#spbzx_amount").html(toDecimal2(piccFee));
                if (buyway == 'buycount') {
                    me.actualOrderAmount = parseFloat(me.goodsInfo.unitPrice) * buycount + piccFee;
                    me.orderAmount = parseFloat(me.goodsInfo.unitPrice) * buycount;
                } else if (buyway == 'buyyuan') {
                    me.actualOrderAmount = buycount + piccFee;
                    me.orderAmount = buycount;
                }
            } else {
                $("#spbzx_amount").html("0.00");
                if (buyway == 'buycount') {
                    me.actualOrderAmount = parseFloat(me.goodsInfo.unitPrice) * buycount;
                    me.orderAmount = parseFloat(me.goodsInfo.unitPrice) * buycount;
                } else if (buyway == 'buyyuan') {
                    me.actualOrderAmount = buycount;
                    me.orderAmount = buycount;
                }
            }
        } else {
            if (buyway == 'buycount') {
                me.actualOrderAmount = parseFloat(me.goodsInfo.unitPrice) * buycount;
                me.orderAmount = parseFloat(me.goodsInfo.unitPrice) * buycount;
            } else if (buyway == 'buyyuan') {
                me.actualOrderAmount = buycount;
                me.orderAmount = buycount;
            }
        }

        // 计算视频收费客服费用
        var serviceCharge = 0;
        if (!isNull(me.selectedServicerId)) {
            var servicerInfo = me.getServicer(me.selectedServicerId);
            serviceCharge = servicerInfo.serviceCharge;
            //console.log("serviceCharge="+serviceCharge);
        }
        // 加客服服务费
        me.actualOrderAmount += serviceCharge;
        me.orderAmount += serviceCharge;
        me.orderAmount = toDecimal2(me.orderAmount);

        // 使用红包
        if (me.redEnvelope != null) {
            me.actualOrderAmount -= me.redEnvelope.price;
        }
        // 使用店铺券
        if (me.shopCoupon != null) {
            me.actualOrderAmount -= me.shopCoupon.price;
        }

        me.actualOrderAmount = toDecimal2(me.actualOrderAmount); // 订单实际付款金额
        var totalPriceArray = me.actualOrderAmount.toString().split(".");
        var $total = "<strong>" + totalPriceArray[0] + "</strong>." + totalPriceArray[1] + "元";
        if (buyPiccOrNot == "buyPicc") {
            $total += "&nbsp;<span style='color:black;'>(包含安心买：" + toDecimal2(piccFee) + "元)</span>";
        }
        // 显示商品保障性最高可赔付金额
        // $("#spbzx_pf_amount").html(me.orderAmount + "元");
        // 显示订单实际付款金额
        $("#orderAmount").html($total);

        if (buyway == 'buyyuan') {
            // 按元购买刷新游戏币购买数量
            var totalCount = Math.ceil(buycount / parseFloat(me.goodsInfo.unitPrice));
            me.gamegoldCount = totalCount; // 游戏币购买数量
            if (me.goodsInfo.moneyName == null || me.goodsInfo.moneyName == "" || typeof (me.goodsInfo.moneyName) == "undefined") {
                var $total = "<strong>" + totalCount + "</strong>" + me.goodsInfo.goodsTypeName;
            } else {
                var $total = "<strong>" + totalCount + "</strong>" + me.goodsInfo.moneyName;
            }
            $("#gamegold_count").html($total);
        } else if (buyway == 'buycount') {
            me.gamegoldCount = buycount; // 游戏币购买数量
        }
    },
    /**
     * 获取最小应该购买的游戏币数量或金额
     */
    getMiniBuyCount: function () {
        var me = this;
        var params = me.getParams();
        var buyway = $(":radio[name='buyway']:checked").val();
        var smallestAmount = me.minBuyAmount;
        if (buyway == 'buycount') { // 按数量购买
            // 计算最少需要购买的游戏币数量
            var price = 1 / parseFloat(me.goodsInfo.unitPrice); // 1元对应多少金
            var count = price * smallestAmount;
            count = Math.ceil(count);
            return count;
        } else if (buyway == 'buyyuan') { // 按元购买
            return smallestAmount;
        }
    },
    /**
     * 使用红包事件
     */
    useRedEnvelopeEvent: function () {
        var me = this;
        var obj = $(":checkbox[name='useRedEnvelope']");
        var checked = obj.prop("checked");
        if (checked) {
            if (me.redEnvelopeDialog == null) {
                // 初始化窗口
                me.redEnvelopeDialog = $("#redEnvelopeDialog").dialog({
                    title: '使用红包',
                    iconCls: "icon-edit",
                    resizable: true,
                    width: 350,
                    height: 170,
                    modal: true,
                    onResize: function () {
                        $(this).dialog('center');
                    },
                    onClose: function () {
                        if (me.redEnvelope == null) {
                            obj.prop("checked", false);
                        }
                    }
                }).window('center');
                $(":input[name='redEnvelopeCode']").css("ime-mode", "disabled");
                $(":input[name='redEnvelopePwd']").css("ime-mode", "disabled");
                // 确认使用红包按钮
                $("#btn_use_red_envelope").bind('click', function () {
                    var code = $.trim($(":input[name='redEnvelopeCode']").val());
                    if (code == "") {
                        me.alert("请填写红包号码！");
                        return;
                    }
                    var pwd = $.trim($(":input[name='redEnvelopePwd']").val());
                    if (pwd == "") {
                        me.alert("请填写红包密码！");
                        return;
                    }
                    // 验证是否有效
                    var request = {"code": code, "pwd": pwd, "type": 1, "amount": me.orderAmount};
                    var response = me.validateDiscountCoupon(request);
                    var responseCode = response.responseStatus.code;
                    if (responseCode == "00") {
                        if (response.valid) {
                            // 记录红包
                            me.redEnvelope = {"code": code, "pwd": pwd, "price": response.price};
                            // 关闭窗口
                            me.redEnvelopeDialog.window('close');

                            // 显示添加的优惠券栏
                            $("#discountCouponLine").show();
                            // 显示红包号码
                            $("#discountCouponLine .hongbao").show();
                            var s = "红包号码：<b class='red'>" + code + "</b>&nbsp;&nbsp;&nbsp;价值：<b class='red'>" + response.price + "</b>元";
                            $("#discountCouponLine .hongbao").html(s);

                            me.countOrderAmount();
                        }
                    } else {
                        me.alert(response.responseStatus.message);
                    }
                });
            } else {
                me.redEnvelopeDialog.window('open').window('center');
            }
            $("#redEnvelopeDialog").show();
        } else {
            me.redEnvelope = null;
            // 不显示红包号码
            $("#discountCouponLine .hongbao").hide();

            // 既没有使用红包也没有使用店铺券，则不显示优惠券号码栏
            if (me.shopCoupon == null) {
                $("#discountCouponLine").hide();
            }

            me.countOrderAmount();
        }
    },
    /**
     * 使用店铺券事件
     */
    useShopCouponEvent: function () {
        var me = this;
        var obj = $(":checkbox[name='useShopCoupon']");
        var checked = obj.prop("checked");
        if (checked) {
            if (me.shopCouponDialog == null) {
                // 初始化窗口
                me.shopCouponDialog = $("#shopCouponDialog").dialog({
                    title: '使用店铺券',
                    iconCls: "icon-edit",
                    resizable: true,
                    width: 350,
                    height: 170,
                    modal: true,
                    onResize: function () {
                        $(this).dialog('center');
                    },
                    onClose: function () {
                        if (me.shopCoupon == null) {
                            obj.prop("checked", false);
                        }
                    }
                }).window('center');
                $(":input[name='shopCouponCode']").css("ime-mode", "disabled");
                $(":input[name='shopCouponPwd']").css("ime-mode", "disabled");
                // 确认使用店铺券按钮
                $("#btn_use_shop_coupon").bind('click', function () {
                    var code = $.trim($(":input[name='shopCouponCode']").val());
                    if (code == "") {
                        me.alert("请填写店铺券号码！");
                        return;
                    }
                    var pwd = $.trim($(":input[name='shopCouponPwd']").val());
                    if (pwd == "") {
                        me.alert("请填写店铺券密码！");
                        return;
                    }
                    // 验证是否有效
                    var request = {"code": code, "pwd": pwd, "type": 2, "amount": me.orderAmount};
                    var response = me.validateDiscountCoupon(request);
                    var responseCode = response.responseStatus.code;
                    if (responseCode == "00") {
                        if (response.valid) {
                            // 记录店铺券
                            me.shopCoupon = {"code": code, "pwd": pwd, "price": response.price};
                            // 关闭窗口
                            me.shopCouponDialog.window('close');

                            // 显示添加的优惠券栏
                            $("#discountCouponLine").show();
                            // 显示店铺券号码
                            $("#discountCouponLine .dianpuquan").show();
                            var s = "店铺券号码：<b class='red'>" + code + "</b>&nbsp;&nbsp;&nbsp;价值：<b class='red'>" + response.price + "</b>元";
                            $("#discountCouponLine .dianpuquan").html(s);

                            me.countOrderAmount();
                        }
                    } else {
                        me.alert(response.responseStatus.message);
                    }
                });
            } else {
                me.shopCouponDialog.window('open').window('center');
            }
            $("#shopCouponDialog").show();
        } else {
            me.shopCoupon = null;
            // 不显示店铺券号码
            $("#discountCouponLine .dianpuquan").hide();

            // 既没有使用红包也没有使用店铺券，则不显示优惠券号码栏
            if (me.redEnvelope == null) {
                $("#discountCouponLine").hide();
            }

            me.countOrderAmount();
        }
    },
    /**
     * 验证优惠券
     * @param request
     */
    validateDiscountCoupon: function (request) {
        var response = null;
        $.ajax({
            type: "GET",
            async: false,
            url: baseServiceUrl + "services/order/validateDiscountCoupon",
            data: request,
            contentType: "application/json; charset=UTF-8",
            dataType: "json",
            success: function (resp) {
                response = resp;
            }
        });
        return response;
    },
    /**
     * 弹出框
     * @param msg
     */
    alert: function (msg) {
        $.messager.alert('提示', msg, 'error');
    },
    /**
     * 改变页面客服提示文字效果
     */
    changeServiceTip: function () {
        if (typeof iii == "undefined") iii = 0;
        $("#serviceTip").prop("class", iii == 0 ? "c1" : "c2");
        iii == 2 ? iii = 0 : iii++;
    },
    /**
     * 只能输入数字
     */
    restrictKeyInput: function (event) {
        var keycode = document.all ? event.keyCode : event.charCode;
        if (keycode != 8 && keycode != 9 && keycode != 0) {
            if ((keycode < 48 || keycode > 57)) {
                if (document.all) {
                    window.event.returnValue = false;
                } else {
                    event.preventDefault();
                }
            }
        }
    }
};
