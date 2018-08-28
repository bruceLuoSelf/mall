var trId, trLogo, gameList = [
    { name: '地下城与勇士', path: 'http://html.5173.com/images/dnf.jpg' },
    { name: '剑灵', path: 'http://images001.5173cdn.com/images/app_pcgame_logo/628.jpg' },
    { name: '魔兽世界(国服)', path: 'http://html.5173.com/images/ms2.jpg' },
    { name: 'QQ三国', path: 'http://images001.5173cdn.com/images/app_pcgame_logo/691.jpg' },
    { name: '剑侠情缘Ⅲ', path: 'http://images001.5173cdn.com/images/app_pcgame_logo/625.jpg' },
    { name: '疾风之刃', path: 'http://images001.5173cdn.com/images/app_pcgame_logo/629.jpg' },
    { name: "龙之谷", path: 'http://images001.5173cdn.com/images/app_pcgame_logo/608.jpg' },
    { name: '斗战神', path: 'http://images001.5173cdn.com/images/app_pcgame_logo/651.jpg' },
    { name: '天涯明月刀', path: "http://images001.5173cdn.com/images/app_pcgame_logo/558.jpg" },
    { name: '上古世纪', path: 'http://images001.5173cdn.com/images/app_pcgame_logo/576.jpg' },
    { name: '怪物猎人OL', path: 'http://html.5173.com/yunyin/201703/tb1/zy/180x120/gwlr.jpg' },
    { name: '冒险岛2', path: 'http://images001.5173cdn.com/images/app_pcgame_logo/603.jpg' },
    { name: '新天龙八部', path: 'http://sk.5173cdn.com/201310/01/58/SgKowFJsoUkAAAAAAABGXylNU7A56.jpg' }
];
$(function () {
    if ($.trim(getUrlParam("id")) == "") {
        alert("url出现错误！");
        window.open(document.referrer, "_self");
        return;
    }
    me.init();
    $("#isToTop").click(function () {
        window.scrollTo(0, 0)
    })
    $("#chsl").keyup(function () {
        var nums = $(this).val();
        if (!isNumber(nums)) {
            alert('请输入正确的数量');
            $(this).val(0);
        }
    });
    $('.open-word').click(function () {
        $('.sell-tip-arrow-box').addClass('close')
    });
    $('.close-word').click(function () {
        $('.sell-tip-arrow-box').removeClass('close')
    });
    $('#trmode').click(function () {
        $(this).addClass('isZy')
        $('.model-select-arrow').css("transform", "rotate(180deg)");
        var $ul = $(this).find('ul')
        if ($ul.hasClass('isShow')) {
            $ul.hide().removeClass('isShow');
            $(this).removeClass('isZy')
            $('.model-select-arrow').css("transform", "rotate(360deg)");
            return
        }
        $ul.show().addClass('isShow');
    })
    $('#trmode li').live('click', function () {
        $('#jydz').val($(this).text());
        //console.log($(this).text());
        if ($(this).attr('data-tradelogo') == 3) {
            $('#trmodeTip').show();
            $('#trmodeTip').find('span').text('邮寄交易出货商承担5%税费，出货完成，请小退游戏');
            $('#trGrade').hide();
            $('#trmodeTip').hide();
        } else if ($(this).attr('data-tradelogo') == 5) {
            $('#trmodeTip').show();
            $('#trGrade').show();
            $('#trmodeTip').find('span').text('该交易类型出货商承担手续费为' + me.gameConfig.poundage * 100 + '%')
        } else {
            $('#trGrade').hide();
            $('#trmodeTip').hide();
            $('#trmodeTip').find('span').text('');
        }
        trId = $(this).attr('data-tradetype');
        trLogo = $(this).attr('data-tradelogo');
    });
    $('#trLevel').live('keyup', function () {

        if (!/^\+?[1-9][0-9]*$/.test($(this).val())) {
            $('#trGrade #telcode-error').show()
            $('#trGrade #telcode-error').text('请输入整数')
        } else {
            if ($(this).val() < 35 || $(this).val() > 90) {
                $('#trGrade #telcode-error').show()
                $('#trGrade #telcode-error').text('等级范围限定35-90级')
            } else {
                $('#trGrade #telcode-error').hide()
            }
        }
    });
    $('#trLevel').blur(function () {
        if (!/^\+?[1-9][0-9]*$/.test($(this).val())) {
            $('#trGrade #telcode-error').show()
            $('#trGrade #telcode-error').text('请输入整数')
        } else {
            if ($(this).val() < 35 || $(this).val() > 90) {
                $('#trGrade #telcode-error').show()
                $('#trGrade #telcode-error').text('等级范围限定35-90级')
            } else {
                $('#trGrade #telcode-error').hide()
            }
        }
    })
});

function showLoading() {
    var screenWidth = $(window).width();//当前窗口宽度
    var screenHeight = $(window).height();//当前窗口高度

    $("#divLoading").css({ "display": "", "position": "fixed", "background": "#000", "z-index": "10000", "-moz-opacity": "0.5", "opacity": ".50", "filter": "alpha(opacity=50)", "width": screenWidth, "height": screenHeight });
    $("#loadingInfo").show();
    $("#divLoading").show();
}

var me = {
    deliveryOrder: null,
    deliveryConfigList: null,
    gameConfig: null,
    init: function () {
        var me = this;
        me.initPage();

        //立刻出货
        $("#formts").validate({
            submitHandler: function () {
                //验证通过后 的js代码写在这里

                me.createOrder();
            }
        });

        $('#chsl').bind('input propertychange', me.changeCount);
    },
    initPage: function () {
        //初始化获取商家信息和采购信息
        var request = {};
        request.id = $.trim(getUrlParam("id"));
        $.ajax({
            type: "GET",
            url: baseServiceUrl + "services/chorder/selectPurchaseOrderAndCgDataById?t=" + new Date().getTime(),
            data: request,
            contentType: "application/json; charset=UTF-8",
            dataType: "json",
            success: function (resp) {
                var code = resp.responseStatus.code;
                if (code == "00") {
                    me.deliveryOrder = resp.purchaseOrder;
                    me.deliveryConfigList = resp.purchaseConfig.deliveryConfigList;
                    me.gameConfig = resp.purchaseConfig.gameConfig;
                    for (var i = 0; i < gameList.length; i++) {
                        if (gameList[i].name == me.deliveryOrder.gameName) {
                            $('#gameLogo').attr('src', gameList[i].path)
                        }
                    }
                    me.append();
                } else if (code == "21009") {
                    // alert("该商品已下架！");
                    window.open(baseHtmlUrl + "purchaseOrder.html?gameName=%u5730%u4E0B%u57CE%u4E0E%u52C7%u58EB&zoneName=&serverName=&raceName=", "_self");
                }
            }
        });
    },
    append: function () {
        if (me.deliveryOrder != null) {
            if (me.deliveryOrder.isOnline == undefined || !me.deliveryOrder.isOnline) {
                alert("该商品已下架！");
                window.open(document.referrer, "_self");
                return;
            }
            var deliverOrder = me.deliveryOrder;
            if (me.deliveryOrder.tradeLogo.indexOf('3') > -1) {
                $('#gameInfo').before('<a class="icon icon-you fl" title="邮寄交易方式"></a>')
            } if (me.deliveryOrder.tradeLogo.indexOf('5') > -1) {
                $('#gameInfo').before('<a class="icon icon-pai fl" title="拍卖行交易,出货商承担手续费为' + me.gameConfig.poundage * 100 + '%"></a>')
            } if (me.deliveryOrder.deliveryType == 1) {
                $('#gameInfo').before('<a class="icon icon-ji fl" title="机器收货模式"></a>')//<a class="ico_ji" title="机器收货模式"></a>
            }
            $("#spanNav").html(deliverOrder.gameName + "&gt; " + deliverOrder.region + " &gt; " + deliverOrder.server + "&gt; 商铺详情");

            //收购信息
            $("#gameInfo").html(deliverOrder.gameName + "/" + deliverOrder.region + "/" + deliverOrder.server)
            $("#count").html(deliverOrder.count);
            $("#price").html(deliverOrder.price);
            //设置灰色提示值
            $("#chsl").attr("placeholder", "输入最大值" + deliverOrder.count);
            if (deliverOrder.startTime != null && deliverOrder.endTime != null) {
                $("#dealTime").html(deliverOrder.startTime + ":00 — " + deliverOrder.endTime + ":00");
            }
            var date = new Date(deliverOrder.updateTime);
            //$("#updateTime").html(me.formatDate(date));
            $("#updateTime").html(date.Format("yyyy-MM-dd hh:mm:ss"));

            //商家信息
            var shopName = deliverOrder.shopName == null ? "" : deliverOrder.shopName;
            var cjl = deliverOrder.cjl == null ? 0 : deliverOrder.cjl;
            var pjys = deliverOrder.pjys == null ? 0 : deliverOrder.pjys;
            var tradingVolume = deliverOrder.tradingVolume == null ? 0 : deliverOrder.tradingVolume;
            var tradeAddress = deliverOrder.tradeAddress == null ? "" : deliverOrder.tradeAddress;
            $("#shopName").html(shopName);
            $("#cjl").html(parseFloat(cjl) * 100 + "%");
            $("#pjys").html(pjys + "分钟");
            $("#tradingVolume").html(tradingVolume + "笔");
            if (deliverOrder.minCount == deliverOrder.count) {
                $("#lblCountInfo").html("（收货商收货数量要求等于<em>" + deliverOrder.count + "）</em>");
            } else if (deliverOrder.minCount < deliverOrder.count) {
                $("#lblCountInfo").html("（收货商收货数量要求大于等于<em>" + deliverOrder.minCount + "</em>,小于等于<em>" + deliverOrder.count + "）</em>");
            } else {
                $("#lblCountInfo").html("（收货商收货数量要求大于等于<em>" + deliverOrder.minCount + "）</em>");
            }
            me.getShGameConfig(deliverOrder);

            var request = {};
            request.gameName = $.trim(deliverOrder.gameName);

            // var htmlStr="";
            // htmlStr+="<option>邮寄交易</option>";
            // $("#jydz").html(htmlStr);
            // $(".txt_machine").html("【机器自动收货】邮寄时请仔细核对角色名，并保留发货视频")

            // var htmlMachine = "出货提示<br/>"
            // for(i=0;i<4;i++){
            //   htmlMachine+='<div class="xdtit-p"><div class="tit-list">'+(i+1)+'.</div><div class="tit-tex">务必由你邀请收货商交易并录制交易视频 ,如因接受邀请造成的损失由出货商自行承担。</div></div>'
            // }
            // $(".xdtitle2").html(htmlMachine)
            // $.ajax({
            //     type: "GET",
            //     url: baseServiceUrl + "services/addressConfig/getConfigByGameName?t="+new Date().getTime(),
            //     data: request,
            //     contentType: "application/json; charset=UTF-8",
            //     dataType: "json",
            //     success: function (resp) {
            //         var code = resp.responseStatus.code;
            //         if (code == "00") {
            //             var addressList=resp.addressList;
            //
            //             var htmlStr="";
            //             if(addressList!=null){
            //                 for(i=0;i<addressList.length;i++){
            //                     var addressInfo=addressList[i];
            //                     htmlStr+="<option>"+addressInfo.tradeAddress+"</option>";
            //                 }
            //             }
            //             $("#jydz").html(htmlStr);
            //         }
            //     }
            // });

        }

        if (me.deliveryConfigList != null) {

            var deliveryConfigList = me.deliveryConfigList[0];


            var tradeTypeIdArr = me.deliveryOrder.tradeTypeName.split(",");
            var tradeTypeId = me.deliveryOrder.tradeTypeId.split(",");
            //var tradeTypeId = (deliveryConfigList.tradeTypeId + "").split(",");

            var tradeLogo = me.deliveryOrder.tradeLogo.split(",");

            for (var i = 0; i < tradeTypeIdArr.length; i++) {
                $("#trmode ul").append('<li data-tradeType=' + tradeTypeId[i] + ' data-tradeLogo=' + tradeLogo[i] + '>' + tradeTypeIdArr[i] + "</li>");
            }


            $(".txt_machine").html(deliveryConfigList.orderTip)

            //var htmlMachine = "出货提示<br/>"
            // for(i=0;i<4;i++){
            //   htmlMachine+='<div class="xdtit-p"><div class="tit-list">'+(i+1)+'.</div><div class="tit-tex"><p style="color: #f00;">务必由你邀请收货商交易并录制交易视频 ,如因接受邀请造成的损失由出货商自行承担。</p></div></div>'
            // }
            var htmlMachine = deliveryConfigList.orderHelpInfo
            $(".sell-tip-content").html(htmlMachine)
            $('#orderTip').html(deliveryConfigList.orderTip)
        }
    },
    createOrder: function () {

        //出货前的判断
        if ($.trim(getUrlParam("id")) == "") {
            alert("url出现错误！");
            return;
        }
        var roleName = $("#jsmc").val();
        if (roleName.length > 40) {
            alert("角色名称不能大于40个字符！");
            return;
        }

        re = /^1\d{10}$/;
        var tel = $("#telcode").val();
        if (!re.test(tel)) {
            //$("#telcode-error").attr("style", "display: inline-block;");
            $("#telErr").show();
            return;
        }

        var count = $.trim($("#chsl").val());
        // if(count%1000!=0){
        //     alert("出货数量只能是1000的倍数！");
        //     return;
        // }
        if (me.deliveryOrder != null) {
            if (count < me.deliveryOrder.minCount || count > me.deliveryOrder.count) {
                alert("出货数量不符合收货数量范围要求！");
                return;
            }
        }
        if ($.trim($('#jydz').val()) == '拍卖交易' && ($('#trLevel').val() < 35 || $('#trLevel').val() > 90)) {
            alert("等级范围限定35-90级");
            return;
        }
        showLoading();

        //生成出货订单
        var request = {};
        request.cgId = $.trim(getUrlParam("id"));
        request.roleName = $.trim(roleName);//交易角色
        request.count = $.trim(count);//出货数量
        //request.address = $.trim($("#jydz option:selected").val());//交易地址
        request.tradeTypeName = $.trim($('#jydz').val())
        request.phone = $.trim($("#telcode").val());//手机号
        request.qq = $.trim($("#qq").val());//qq
        request.deliveryType = 1;
        request.tradeType = trId;
        request.tradeLogo = trLogo;
        request.sellerRoleLevel = $.trim($('#trLevel').val())

        $.ajax({
            type: "POST",
            url: baseServiceUrl + "services/chorder/createDeliveryOrder",
            data: $.toJSON(request),
            contentType: "application/json; charset=UTF-8",
            dataType: "json",
            success: function (resp) {
                var code = resp.responseStatus.code;
                if (code == "00") {
                    window.open(baseHtmlUrl + "myShOrder.html?orderId=" + resp.orderId + "&t=" + (new Date()).getTime(), "_self");
                } else {
                    $("#divLoading").hide();
                    $("#loadingInfo").hide();
                }
            },
            error: function () {
                $("#divLoading").hide();
                $("#loadingInfo").hide();
                alert("创建失败，请联系管理员");
            }
        });
    },
    changeCount: function () {
        //联动出货数量以及单价，生成交易金额
        var count = $.trim($("#chsl").val());
        var price = $.trim($("#price").html());
        var totalAccount = count * price;
        if (isNaN(totalAccount)) {
            $("#jyje").val(0);
        }
        else {
            $("#jyje").val(toDecimal2(totalAccount));
        }
    },
    getShGameConfig: function (deliverOrder) {
        // var request = {};
        // request.gameName=$.trim(deliverOrder.gameName);
        // console.log(deliverOrder.gameName);
        var unitName = me.gameConfig.unitName;
        $("#unit_name").html(unitName + "*");
        $(".unitName").html(unitName);
        // $.ajax({
        //     type: "GET",
        //     url: baseServiceUrl + "services/shGameConfig/getConfigByGameName?t="+new Date().getTime(),
        //     data: request,
        //     contentType: "application/json; charset=UTF-8",
        //     dataType: "json",
        //     success: function (resp) {
        //         if (resp.shGameConfig != null) {
        //             var unitName = resp.shGameConfig.unitName;
        //             $("#unit_name").html(unitName+"*");
        //             $(".unitName").html(unitName);
        //         }
        //     }
        //
        // });
    }
}
