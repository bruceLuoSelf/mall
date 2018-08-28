$(document).ready(function () {
    GameGoldMall.SellerSignUp.init();
});
(function ($) {

    /**
     * 5173游戏币商城卖家入驻
     */
    var me = {
        /**
         * 客服列表
         */
        servicerList: null,
        /**
         * 选中的客服ID
         */
        selectedServicerId: null,
        /**
         * 初始化脚本
         */
        init: function () {
            // 获取卖家信息
            this.loadSellerInfo();

            // 获取客服信息
            this.loadServicers();

            // 绑定客服QQ点击事件
            this.bindQQClickEvent();

            // 初始化验证信息
            this.initValidation();

            // 立即报名
            $("#btnSignUp").click(this.postSignUpRequest);
        },
        /**
         * 获取卖家信息
         */
        loadSellerInfo: function () {
            var queryRequest = {};
            $.ajax({
                type: "POST",
                url: baseServiceUrl + "services/seller/querysellerinfo?t="+new Date().getTime(),
                data: $.toJSON(queryRequest),
                contentType: "application/json; charset=UTF-8",
                dataType: "json",
                beforeSend: function (request) {
                    request.setRequestHeader("5173_authkey", getAuthkey());
                },
                success: function (resp) {
                    var code = resp.responseStatus.code;
                    if (code == "00") {
                        var sellerInfo = resp.sellerInfo;
                        if (!isNull(sellerInfo)) {
                            if (sellerInfo.checkState != CheckState.UnPassAudited) {
                                // 如果当前卖家信息已存在，未审核或审核通过的，跳转到applystate.html
                                window.location.href = "applystate1.html";
                                return;
                            } else {
                                // 卖家未通过审核，表单填充卖家信息
                                me.initPageSellerInfo(sellerInfo);
                            }
                        }
                        $(".wrapper_service").css("display", "block");
                    }
                },
                error: function (resp) {
                    console.log(resp);
                }
            });
        },
        /**
         * 初始化页面上的卖家信息
         * @param sellerInfo
         */
        initPageSellerInfo: function (sellerInfo) {
            $(":text[name='seller']").val(sellerInfo.name);
            $(":text[name='sellerPhone']").val(sellerInfo.phoneNumber);
            $(":text[name='sellerQQ']").val(sellerInfo.qq);
            $(":radio[name='sellerType']").val(sellerInfo.sellerType);
        },
        /**
         * 获取客服信息
         */
        loadServicers: function () {
            // 选择符合条件的客服
            var request = {};
            request.servicerType = UserType.EnterService;
            request.size = 8;
            $.ajax({
                type: "POST",
                url: baseServiceUrl + "services/queryservicer/applyservice?t="+new Date().getTime(),
                data: $.toJSON(request),
                contentType: "application/json; charset=UTF-8",
                dataType: "json",
                success: function (resp) {
                    var code = resp.responseStatus.code;
                    if (code == "00") {
                        me.servicerList = resp.userInfoEOs;

                        // 组装客服信息
                        me.initPageServicerList();
                    }
                },
                error: function (resp) {
                    console.log(resp);
                }
            });
        },
        /**
         * 组装客服信息
         */
        initPageServicerList: function () {
            if (isNull(me.servicerList)) {
                return;
            }

            for (var i = 0; i < me.servicerList.length; i++) {
                var servicer = me.servicerList[i];

                // 该客服被禁用，不显示
                if (servicer.isDeleted == true) {
                    continue;
                }

                var avatar = isNull(servicer.avatarUrl) ? "img/head.png" : buildImageUrl(servicer.avatarUrl, "64x64");
                var name = isNull(servicer.nickName) ? "公主" : servicer.nickName;
                var sign = isNull(servicer.sign) ? "心随你动 服务在心" : servicer.sign;

                var html = "<div id='servicerId_" + servicer.id + "' class='chose_service_block'>";
                html += "<div class='chose_service_img_applyseller'><img src='" + avatar + "' /></div>";
                html += "<div class='chose_service_infor_applyseller'>";
                html += "<div class='chose_service_name_applyseller'>客服：<strong>" + name + "</strong></div>";
                html += "<div class='chose_serive_word'>" + sign + "</div>";
                html += "</div></div>";

                $(".chose_service_list").append(html);

            }

            // 选择客服事件
            $(".chose_service_block").click(function(){
                var id = $(this).attr("id");
                var servicerId = id.substring(id.indexOf("_")+1);
                me.selectServicer(servicerId);
            });
        },
        /**
         * 选择客服
         * @param servicerId
         */
        selectServicer: function(servicerId) {
            me.selectedServicerId = servicerId;

            // 删除其他客服的选中css
            $(".chose_service_block").removeClass("chose_service_block_chosed");
            $(".span_chosed").remove();

            // 设置当前客服选择css
            $("#servicerId_" + servicerId).addClass("chose_service_block_chosed");
            $("#servicerId_" + servicerId + " .chose_service_infor_applyseller").after("<span class='span_chosed'></span>");


            // 补充客服详细信息
            var servicerInfo = me.getServicer(servicerId);
            if (servicerInfo == null) {
                return;
            }

            $(".service_box").css("display", "block");

            var avatarUrl = isNull(servicerInfo.avatarUrl) ? "img/head.png" : buildImageUrl(servicerInfo.avatarUrl, "64x64");
            $("#service_avatar").attr("src", avatarUrl);

            var nickName = isNull(servicerInfo.nickName) ? "公主" : servicerInfo.nickName;
            $(".service_name").html(nickName);

            $(".service_word").html("<p>掌柜感谢您选择我，入驻细则您可以QQ在线咨询或留下您的联系方式，小女子会马上联系您！</p>");

            var qq = isNull(servicerInfo.qq) ? "" : servicerInfo.qq;
            $(".service_qq").html("QQ：<strong>"+qq+"</strong>");

            var phoneNumber = isNull(servicerInfo.phoneNumber) ? "" : servicerInfo.phoneNumber;
            $(".chose_service_phone").html("手机<strong>" + phoneNumber + "</strong>");

            var weiXin = isNull(servicerInfo.weiXin) ? "" : servicerInfo.weiXin;
            $(".chose_service_wechat").html("微信<strong>" + weiXin + "</strong>");
        },
        /**
         * 绑定客服QQ点击事件
         */
        bindQQClickEvent: function() {
            // 添加QQ点击绑定事件
            $(".chose_service_qq").click(function() {
                // 客服详细信息
                var servicerInfo = me.getServicer(me.selectedServicerId);
                if (isNull(servicerInfo) || isNull(servicerInfo.qq)) {
                    return;
                }
                window.open("http://wpa.qq.com/msgrd?v=3&uin=" + servicerInfo.qq + "&site=qq&menu=yes");
            });
        },
        /**
         * 获取指定客服信息
         * @param servicerId
         * @returns {*}
         */
        getServicer: function(servicerId) {
            if (isNull(me.servicerList))
                return null;


            for (var i = 0, j = me.servicerList.length; i < j; i++) {
                if (servicerId == me.servicerList[i].id) {
                    return me.servicerList[i];
                }
            }
            return null;
        },
        initValidation: function() {
            // 卖家游戏
            $(":text[name='sellerGames']").bind({
                focus: function() {
                    checkSellerGames();
                },
                blur: function() {
                    checkSellerGames();
                },
                keyup: function() {
                    checkSellerGames();
                }
            });
            // 联系人
            $(":text[name='sellerName']").bind({
                focus: function() {
                    checkSellerName();
                },
                blur: function() {
                    checkSellerName();
                },
                keyup: function() {
                    checkSellerName();
                }
            });
            // 联系电话
            $(":text[name='sellerPhone']").bind({
                focus: function() {
                    checkPhone();
                },
                blur: function() {
                    checkPhone();
                },
                keyup: function() {
                    checkPhone();
                },
                keypress: function(event) {
                    onlyInputNumber(event);
                }
            });
            $(":text[name='sellerPhone']").css("ime-mode", "disabled");

            // 卖家QQ
            $(":text[name='sellerQQ']").bind({
                focus: function() {
                    checkQQ();
                },
                blur: function() {
                    checkQQ();
                },
                keyup: function() {
                    checkQQ();
                },
                keypress: function(event) {
                    onlyInputNumber(event);
                }
            });
            $(":text[name='sellerQQ']").css("ime-mode", "disabled");
        },
        postSignUpRequest: function() {
            if (me.selectedServicerId == null) {
                alert("请选择为您服务的客服！");
                return;
            }
            if (!checkSellerGames()) {
                return;
            }
            if (!checkSellerName()) {
                return;
            }
            if (!checkPhone()) {
                return;
            }
            if (!checkQQ()) {
                return;
            }

            var request = {};
            request.games = $(":text[name='sellerGames']").val();
            request.name = $(":text[name='sellerName']").val();
            request.phoneNumber = $(":text[name='sellerPhone']").val();
            request.qq = $(":text[name='sellerQQ']").val();
            request.sellerType = $(':radio[name="sellerType"]:checked').val();
            request.servicerId = me.selectedServicerId;
            request.isOpenSh=$('#isOpenSh').attr("checked")=='checked'?true:false;
            $.ajax({
                type: "POST",
                url: baseServiceUrl + "services/seller/applyseller",
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
                        // 申请成功，等待审核
                        // 跳转到审核状态页面
                        window.location.href = baseHtmlUrl + "applystate1.html";
                    } else {
                        alert(responseStatus.message);
                    }
                }
            });
        }
    };

    /**
     * 检查卖家游戏是否填写
     */
    function checkSellerGames() {
        var isValid = false;
        var icon = $("#tip_icon_sellerGames");
        var tip = $("#tip_sellerGames");

        var value = $(":text[name='sellerGames']").val();
        if (value != '') {
            isValid = true;
            icon.removeClass("tip_wrong").addClass("tip_right");
            tip.removeClass("tips_newbox").addClass("tips_newbox_2");
        } else {
            isValid = false;
            icon.removeClass("tip_right").addClass("tip_wrong");
            tip.removeClass("tips_newbox_2").addClass("tips_newbox");
        }
        icon.css("visibility", "visible");
        tip.show();
        return isValid;
    }

    /**
     * 检查联系人
     * @returns {boolean}
     */
    function checkSellerName() {
        var isValid = false;
        var icon = $("#tip_icon_sellerName");
        var tip = $("#tip_sellerName");

        var value = $(":text[name='sellerName']").val();
        if (value != '') {
            isValid = true;
            icon.removeClass("tip_wrong").addClass("tip_right");
            tip.removeClass("tips_newbox").addClass("tips_newbox_2");
        } else {
            isValid = false;
            icon.removeClass("tip_right").addClass("tip_wrong");
            tip.removeClass("tips_newbox_2").addClass("tips_newbox");
        }
        icon.css("visibility", "visible");
        tip.show();
        return isValid;
    }

    /**
     * 检查联系电话
     * @returns {boolean}
     */
    function checkPhone() {
        var isValid = false;
        var icon = $("#tip_icon_sellerPhone");
        var tip = $("#tip_sellerPhone");

        var msg1 = "手机号码请以“<em class=\"red\">13</em>”“<em class=\"red\">14</em>”“<em class=\"red\">15</em>”“<em class=\"red\">18</em>”开头（暂时只支持国内手机号码）\r\n固话或小灵通请加区号（例：02188888888）。";
        var msg2 = "建议填写手机，固话或小灵通请加区号（例：02188888888）";

        var value = $(":text[name='sellerPhone']").val();
        if (value != '') {
            var pattern = /^1[3,4,5,7,8]\d{9}$/;
            if(pattern.test(value)) {
                // 判断手机号码
                isValid = true;
            } else {
                // 判断电话号码
                pattern = /^0(([1,2]\d)|([3-9]\d{2}))\d{7,8}$/;
                if(pattern.test(value)) {
                    isValid = true;
                } else {
                    isValid = false;
                }
            }

            if (isValid) {
                $("#tip_sellerPhone span").html(msg2);
                tip.addClass("w340").removeClass("two");

                icon.removeClass("tip_wrong").addClass("tip_right");
                tip.removeClass("tips_newbox").addClass("tips_newbox_2");
            } else {
                $("#tip_sellerPhone span").html(msg1);
                tip.addClass("w340").addClass("two");

                icon.removeClass("tip_right").addClass("tip_wrong");
                tip.removeClass("tips_newbox_2").addClass("tips_newbox");
            }

        } else {
            isValid = false;
            $("#tip_sellerPhone span").html(msg2);
            tip.removeClass("w340").removeClass("two");

            icon.removeClass("tip_right").addClass("tip_wrong");
            tip.removeClass("tips_newbox_2").addClass("tips_newbox");
        }
        icon.css("visibility", "visible");
        tip.show();
        return isValid;
    }

    /**
     * 检查QQ是否正确
     * @param event
     */
    function checkQQ() {
        var isValid = false;
        var icon = $("#tip_icon_sellerQQ");
        var tip = $("#tip_sellerQQ");

        var value = $(":text[name='sellerQQ']").val();
        if (value != '') {
            var pattern = /^[1-9]\d{4,10}$/;
            if (pattern.test(value)) {
                isValid = true;
                icon.removeClass("tip_wrong").addClass("tip_right");
                tip.removeClass("tips_newbox").addClass("tips_newbox_2");
            } else {
                isValid = false;
                icon.removeClass("tip_right").addClass("tip_wrong");
                tip.removeClass("tips_newbox_2").addClass("tips_newbox");
            }
        } else {
            isValid = false;
            icon.removeClass("tip_right").addClass("tip_wrong");
            tip.removeClass("tips_newbox_2").addClass("tips_newbox");
        }
        icon.css("visibility", "visible");
        tip.show();
        return isValid;
    }

    //限制只能输入整数
    function onlyInputNumber(event) {
        event = window.event || event;
        var keycode = document.all ? event.keyCode : event.charCode;

        /**
         * 8:退格键
         * 9:TAB键
         * 48-57：数字0~9
         */
        if (keycode != 8 && keycode != 9 && keycode != 0) {
            if ((keycode < 48 || keycode > 57)) {
                setReturnValueFalse(event);
            }
        }
    }
    function setReturnValueFalse(event) {
        if (document.all) {
            window.event.returnValue = false;
        } else {
            event.preventDefault();
        }
    }


    if (typeof window.GameGoldMall === 'undefined') {
        window.GameGoldMall = {};
    }

    GameGoldMall.SellerSignUp = {
        init: function() {
            me.init();
        }
    };
})(jQuery);
