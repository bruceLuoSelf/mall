// 小能参数
/*NTKF_PARAM = {
     siteid: "bq_1000",
     sellerid: "bq_1013",
     settingid: "bq_1013_1432541248318",
     itemid: "",
     uid: "",
     uname: "",
     orderid: "",
     orderprice: "",
     userlevel: ""
 }*/


/**
 * 获取游戏币商城热卖商品
 *         Update History
 *         Date         Name            Reason For Update
 *         ----------------------------------------------
 *         2017/5/12    wrf              ZW_C_JB_00008商城增加通货
 */
$(document).ready(function () {

    //$.getScript('http://download.ntalker.com/b2b/ntkfstat.js');
    GameGoldMall.HotRecommend.load();

});

var jpPrice=0;
(function ($) {
    if (typeof GameGoldMall === 'undefined') {
        GameGoldMall = {};
    }
    /**
     * 游戏币商城热卖商品
     */
    var me = GameGoldMall.HotRecommend = {
        container: "dvMoneyMall",
        /**
         * 配置信息
         */
        config: null,
        /**
         * 热卖商品数据
         */
        goodsList: null,

        analysisUrlResponse:null,
        /**
         * 获取热卖商品数据
         */
        load: function () {
            // 判断页面元素是否存在
            if ($("#" + me.container).length <= 0) {
                return;
            }
            var request = {};
            request.currentUrl = (function(){
                var currentUrl = window.location.href;
                // 去除多余的参数，防止后台解析不正常编码字符串出现异常
                if (currentUrl.indexOf("?keyword=") != -1) {
                    currentUrl = currentUrl.substring(0, currentUrl.indexOf("?keyword="));
                }
                return currentUrl;
            })();

            if(!isNull(getUrlParam("gameName"))){
                request.gameName = decodeURI(escape(getUrlParam("gameName")));
                request.region = decodeURI(escape(getUrlParam("region")));
                request.server = decodeURI(escape(getUrlParam("server")));
                request.goodsTypeName = decodeURI(escape(getUrlParam("goodsTypeName")));
            }

            $.ajax({
                type: "GET",
                url: baseServiceUrl + "services/goods/QueryAllCategoryGoods?t="+new Date().getTime(),
                data: request,
                contentType: "application/json; charset=UTF-8",
                dataType: "jsonp",
                jsonp: "callback",
                success: function (resp) {
                    var responseStatus = resp.responseStatus;
                    var code = responseStatus.code;
                    if (code == "00") {
                        me.goodsList = resp.goodsList;
                        me.minBuyAmount=resp.minBuyAmount;
                        me.config = resp.hotRecommendConfig;
                        me.analysisUrlResponse=resp.analysisUrlResponse;
                        me.unitName=resp.unitName;   /**ZW_C_JB_00008_20170512 start mod**/
                        me.generate();
                    }
                }
            });
        },
        /**
         * 生成热卖商品列表
         */
        generate: function () {
            if (me.goodsList.length == 0) {
                return;
            }

            var count = 0;
            for (var i = 0; i < me.goodsList.length; i++) {
                if (!me.goodsList[i].isDeleted) {
                    count++;
                    break;
                }
            }
            if (count == 0) {
                return;
            }

            var lowestPriceUrl = baseHtmlUrl + "lowestPriceTable.html";
            var sellerEnterUrl = baseHtmlUrl + "applyseller.html";

            var html =  '<div class="hot_recommen_tt">' +
                            '<em></em>' +
                            '<div class="hot_buy_title">' +
                        		'<div class="hot_buy_w1"><a class="sellerEnterLink" href="' + lowestPriceUrl + '" target="_blank">价格查询</a></div>'+
                        		'<div class="hot_buy_w2">商品单价</div>' +
                        		'<div class="hot_buy_w3">购买数量</div>' +
                        		'<div class="hot_buy_w4">付款金额</div>' +
                        		'<div class="hot_buy_w5">服务类型</div>' +
                            '</div>' +
                            '<strong>' +
                                '<a class="sellerEnterLink" target="_blank" href="' + sellerEnterUrl + '">零门槛、高销量、免费入驻</a>' +
                            '</strong>' +
                        '</div>' +
                        '<div class="hot_recobox">' +
                            '<ul id="hot_recommend_goods" class="hot_buy_list"></ul>' +
                        '</div>';
            var parent = $("#" + me.container);
            parent.attr("class", "hot_recommend");
            parent.html(html);

            if (me.goodsList.length != 0){
                // 创建具体的商品列表
                me.buildGoodsList();

                // 绑定事件
                me.bindEvents();
            }

           /* if(me.goodsList.length <= 2){
                var li =
                    '<li>' +
                        '<div class="hot_buy_detail_w7">' +
                            '零门槛、高销量、免费入住、虚位以待'+
                        '</div>' +
                        '<div class="hot_buy_detail_w8">' +
                            '<a class="a_jion_now" href="http://yxbmall.5173.com/applyseller.html" target="_blank"></a>' +
                        '</div>' +
                    '</li>';

                $("#hot_recommend_goods").append(li);
            }*/

            // 卖家入驻文字闪烁
            me.changeSellerEnterColor();
        },
        /**
         * 生成商品列表li
         * @returns {string}
         */
        buildGoodsList: function() {
            var html = "";
            var image="";
            var unitName=me.unitName;

            for (var i = 0, j = me.goodsList.length; i < j; i++) {
                var goods = me.goodsList[i];
                if (isNull(goods) || goods.isDeleted) {
                    continue;
                }
                image = buildImageUrl(goods.imageUrls, "55x55");
                var title = isNull(goods.title) ? "" : goods.title;
                var gameInfo = goods.region + "/" + goods.server + "/" + (isNull(goods.gameRace) ? "" : goods.gameRace);
                var price = toDecimal2(1 / parseFloat(goods.unitPrice)); // 1元对应多少金
                // var moneyName = isNull(goods.moneyName) ? "金" : goods.moneyName;
                var count = me.getDefaultGoldCount(me.minBuyAmount,goods);
                var totalPrice = toDecimal2(parseFloat(goods.unitPrice) * count);
                var totalPriceArray = totalPrice.toString().split(".");
                var repositoryCount = "";// 库存数量
                var settingid = "bq_1013_1432541248318";

                if(goods.goodsCat == 1 /*|| goods.goodsCat == 3*/){
                	title = '<span style="color:red">' + title + '</span>';
                	repositoryCount = goods.sellableCount;
                } else if (goods.goodsCat == 2 || goods.goodsCat == 3) {
                	repositoryCount = "充足";
                }

                var serviceTypeHtml = me.getServiceTypeIcon(goods);
                html += '<li id="goods_' + goods.goodsCat + '_' + goods.id + '" goodsTypeName="'+goods.goodsTypeName+'" style="z-index:9;">'+
                '<div class="hot_buy_detail_w0">' +
                                '<div class="hot_img">' +
                                    '<img width="55" height="55" src="' + image + '">' +
                                '</div>' +
                            '</div>' +
                            '<div class="hot_buy_detail_w1"><div class="title">' +
                                '<p class="hot_time"><strong>' + title + '</strong></p>' +
                                '<p class="hot_service">' + gameInfo + '</p>' +
                            '</div></div>' +
                            '<div class="hot_buy_detail_w2" style="z-index:99;">' +
                                '<p class="hot_price">1元=<span>' + price + '</span>' + /**moneyName**/unitName + goods.goodsTypeName+'</p>' +  /**ZW_C_JB_00008_20170512  mod**/
                                '<p class="repository">库存数量：' + repositoryCount + '</p>';
                html+=getSellAdd(goods);
                html+= '</div>' +
                            '<div class="hot_buy_detail_w3" style="' +((goods.goodsCat == 2)?"z-index:105;":"z-index:101;") + '">' +
                                '<div style="' +((goods.goodsCat == 2)?"z-index:106;":"z-index:102;") + '" class="' + ((goods.goodsCat == 2)?"select_list_input":"select_list_input3")  + '">' +
                                    '<input id="goods_'+ goods.goodsCat + '_' + goods.id +'_goldCount" name="goldCount"  value="' + count + '" maxlength="8" class="input_select_list_number" style="z-index:103;">' +
                                    '<span class="' +((goods.goodsCat == 2)?"select_list_unit":"select_list_unit2") + '">' + /**moneyName**/unitName +goods.goodsTypeName+ '</span>'; /**ZW_C_JB_00008_20170512  mod**/
                                    if (goods.goodsCat == 2) {
                                        // 不是栏目2的不显示下拉框
                                        html += '<div class="popup_select" style="z-index:107;"></div>';
                                    }
                html+=          '</div>' +
                            '</div>' +
                            '<div class="hot_buy_detail_w4">' +
                                '<span class="hot_price_account_buy">' +
                                    '<strong>' + totalPriceArray[0] + '</strong>.' + totalPriceArray[1] + '<em>元</em>' +
                                '</span>' +
                            '</div>' +
                            '<div class="hot_buy_detail_w6">' +
                                '<ul class="integrity_ul">' + serviceTypeHtml + '</ul>' +
                            '</div>' +
                            '<div class="hot_buy_detail_w5">' +
                                '<a class="a_buy_now" href="javascript:void(0);">立即购买</a>' +
                                /*'<div style="width:185px; height:7px; "></div>'+*/
                                /*'<a class="a_online_consult" onclick="NTKF.im_openInPageChat(\'bq_1013_1432633336480\')"></a>' +*/
                            '</div>';

                html+=  '</li>';
            }

            $("#hot_recommend_goods").html(html);
            //html="";

            ////var gameName='御龙在天';
            ////var gameArea='双线八、九区';
            ////var gameServer='一统千军';
            ////var accountId="zsn110";
            //var gameName="";
            //var gameArea="";
            //var gameServer="";
            //var accountId="151y151y";
            //var analysis=me.analysisUrlResponse;
            //if(analysis!=null){
            //    gameName=analysis.gm;
            //    gameArea=analysis.ga;
            //    gameServer=analysis.gs;
            //}
            //if(gameName=="剑灵") {
            //    if (me.goodsList.length == 3) {
            //        $("#hot_recommend_goods > li").eq(2).remove();
            //    }
            //    jQuery.ajax({
            //        type: "get",
            //        url: "http://mall.5173.com/Service/product.aspx",
            //        dataType: "jsonp",
            //        jsonp: "callback",
            //        data: "gameName=" + escape(gameName) + "&gameArea=" + escape(gameArea) + "&gameServer=" + escape(gameServer) + "&accountId=" + accountId + "&callback=?",
            //        success: function (json) {
            //            if (json.flag == "success") {
            //                //alert(json.id+"--"+json.masterPrice+"--"+json.quantity+"----"+json.userName);
            //                //gjj011101
            //                var smallestAmount = me.getSmallestAmount(gameName);
            //                var priceTemp = toDecimal2(1 / parseFloat(json.masterPrice)); // 1元对应多少金
            //                var count = priceTemp * smallestAmount;
            //                count = Math.ceil(count);
            //
            //                var title = "金牌卖家";
            //                jpPrice = json.masterPrice;
            //                var gameInfo = gameArea + "/" + gameServer;
            //                var serviceTypeHtml = me.getServiceTypeIcon(me.goodsList[0]);
            //                var price = toDecimal2(1 / parseFloat(json.masterPrice)); // 1元对应多少金
            //                var totalPrice = toDecimal2(parseFloat(json.masterPrice) * count);
            //                var totalPriceArray = totalPrice.toString().split(".");
            //                var moneyName = json.unit;
            //                html += '<li id="goods_2" style="z-index:9;"><div class="hot_buy_detail_w0">' +
            //                    '<div class="hot_img">' +
            //                    '<img width="55" height="55" src="' + image + '">' +
            //                    '</div>' +
            //                    '</div>' +
            //                    '<div class="hot_buy_detail_w1"><div class="title">' +
            //                    '<p class="hot_time"><strong>' + title + '</strong></p>' +
            //                    '<p class="hot_service">' + gameInfo + '</p>' +
            //                    '</div></div>' +
            //                    '<div class="hot_buy_detail_w2" style="z-index:99;">' +
            //                    '<p class="hot_price">1元=<span>' + price + '</span>' + moneyName + '</p>' +
            //                    '<p class="repository">库存数量：' + json.quantity + '</p>';
            //                html += '</div>' +
            //                    '<div class="hot_buy_detail_w3" style="z-index:105">' +
            //                    '<div style="z-index:102" class="select_list_input3">' +
            //                    '<input id="goods_3_JP_goldCount" name="goldCount"  value="' + count + '" maxlength="8" class="input_select_list_numberJp" style="z-index:103;">' +
            //                    '<span class="select_list_unit2">' + moneyName + '</span>';
            //                html += '<div class="popup_select" style="z-index:107;"></div>';
            //                html += '</div>' +
            //                    '</div>' +
            //                    '<div class="hot_buy_detail_w4">' +
            //                    '<span class="hot_price_account_buy">' +
            //                    '<strong>' + totalPriceArray[0] + '</strong>.' + totalPriceArray[1] + '<em>元</em>' +
            //                    '</span>' +
            //                    '</div>' +
            //                    '<div class="hot_buy_detail_w6">' +
            //                    '<ul class="integrity_ul">' + serviceTypeHtml + '</ul>' +
            //                    '</div>' +
            //                    '<div class="hot_buy_detail_w5">' +
            //                    '<a class="buyJp" href="javascript:void(0);">立即购买</a>' +
            //                    '</div></li>';
            //                if ($("#hot_recommend_goods > li").length > 0) {
            //                    $("#hot_recommend_goods > li").eq($("#hot_recommend_goods > li").length - 1).after(html);
            //                }
            //                else{
            //                    $("#hot_recommend_goods").html(html);
            //                }
            //                $(".buyJp").click(function () {
            //                    var goldCount = parseInt($("#goods_JP_goldCount").val());
            //                    if (goldCount > parseInt(json.quantity)) {
            //                        alert("该价格最大库存量是:" + json.quantity + ",请不要超买哦!");
            //                        return;
            //                    }
            //
            //                    totalPrice = toDecimal2(parseFloat(json.masterPrice) * goldCount);
            //                    window.open("http://mall.5173.com/EasyBuy/EasyOrder.aspx?productId=" + json.id + "&IsBuyMoney=True&OrderPrice=" + totalPrice + "&t=" + (new Date()).getTime(), "_blank");
            //                });
            //
            //                $('#goods_2 .input_select_list_numberJp').bind('input propertychange', me.changeGoldCount);
            //                $("#goods_2 .input_select_list_numberJp").css("ime-mode", "disabled");
            //                $("#goods_2 .input_select_list_numberJp").bind('keypress', function (event) {
            //                    var keycode = document.all ? event.keyCode : event.charCode;
            //                    if (keycode != 8 && keycode != 9 && keycode != 0) {
            //                        if ((keycode < 48 || keycode > 57)) {
            //                            return false;
            //                        }
            //                    }
            //                });
            //            }
            //        }
            //    });
            //}
            //alert($("#hot_recommend_goods > li").eq(2).html());
            //return html;
        },
        bindEvents: function() {
            // 实时监控，input金币数的变化，以及时生成价格
            $('.hot_recommend .input_select_list_number').bind('input propertychange', me.changeGoldCount);

            // 游戏币购买数量输入框
            $(".hot_recommend .input_select_list_number").css("ime-mode", "disabled");
            $(".hot_recommend .input_select_list_number").bind('keypress', function (event) {
                var keycode = document.all ? event.keyCode : event.charCode;
                if (keycode != 8 && keycode != 9 && keycode != 0) {
                    if ((keycode < 48 || keycode > 57)) {
                        return false;
                    }
                }
            });

            // 金币select下拉框点击事件
            $(".hot_recommend .select_list_input").bind('click', me.toggleGoldSelect);

            // 立即购买事件
            $(".hot_recommend .a_buy_now").bind('click', me.openOrderUrl);

            // 在页面空白处点击，触发游戏币下拉框隐藏
            $(document.body).bind('click', function () {
                // 点击后隐藏所有游戏币select下拉框
                $(".hot_recommend .select_list_popup").hide();
            });
        },
        /**
         * 游戏币购买数量改变事件
         */
        changeGoldCount: function () {
            // 当前购买金币数
            var goldCount = $(this).val();

            if (isNull(goldCount)) {
                goldCount = 0;
                $(this).val(0);
            } else if (parseInt(goldCount) < 0) {
                goldCount = 0;
                $(this).val(0);
            }

            if (!isNumber(goldCount)) {
                alert("游戏币购买数量请输入正确的数字！");
                return;
            }

            // 数量不超过99999999
            if (parseInt(goldCount) >= 99999999) {
                goldCount = 99999999;
                $(this).val(goldCount);
            }

            var id = $(this).attr("id");
            var goodsCat=id.split("_")[1];
            var goodsId = id.split("_")[2];
            if(goodsId!="JP") {
                var goods = me.getGoodsInfo(goodsId,goodsCat);
                var totalPrice = toDecimal2(parseFloat(goods.unitPrice) * parseInt(goldCount));
                var totalPriceArray = totalPrice.toString().split(".");
                $("#goods_" + goods.goodsCat + "_" + goodsId + " .hot_price_account_buy").html("<strong>" + totalPriceArray[0] + "</strong>." + totalPriceArray[1] + "<em>元</em>");
            }
            else{
                var totalPrice = toDecimal2(parseFloat(jpPrice) * parseInt(goldCount));
                var totalPriceArray = totalPrice.toString().split(".");
                $("#goods_2 .hot_price_account_buy").html("<strong>" + totalPriceArray[0] + "</strong>." + totalPriceArray[1] + "<em>元</em>");
            }
        },
        /**
         * 获取最小游戏币购买数量
         * @param goods
         * @returns {number}
         */
        getDefaultGoldCount: function(minBuyCount,goods) {
            // 最小购买金额
            // var smallestAmount = me.getSmallestAmount(goods.gameName, goods.goodsTypeName);

            // 计算最少购买游戏币数量
            var count =  Math.ceil(minBuyCount/goods.unitPrice);
            return count;
        },
        /**
         * 根据游戏判断最小游戏币购买金额
         * @param gameName
         */
        getSmallestAmount: function(gameName, goodsTypeName) {
            var smallestAmount = 0;
            if (gameName == "剑灵") {
                smallestAmount = 35; // 剑灵最少购买35元
            } else if (gameName == "斗战神") {
                smallestAmount = 50; // 斗战神最少购买50元
            } else if (gameName == "天涯明月刀") {
                smallestAmount = 100; // 剑灵最少购买100元
            } else {
                smallestAmount = 20; // 其它游戏最少购买20元
            }
            if(goodsTypeName=="挑战书"){
                smallestAmount = 50; //挑战书最小购买50元
            }
            if (goodsTypeName == "元宝票") {
                smallestAmount = 50; //元宝票最小购买50元
            }
            return smallestAmount;
        },
        /**
         * 获取服务类型图标
         * @param goods
         * @returns {string}
         */
        getServiceTypeIcon: function(goods) {
            var categoryIcon = "";
            if (!isNull(me.config)) {
                if (goods.goodsCat == 1) {
                    if (!isNull(me.config.categoryIcon1)) {
                        categoryIcon = me.config.categoryIcon1;
                    }
                } else if (goods.goodsCat == 2) {
                    if (!isNull(me.config.categoryIcon2)) {
                        categoryIcon = me.config.categoryIcon2;
                    }
                } else if (goods.goodsCat == 3) {
                    //if (!isNull(me.config.categoryIcon3)) {
                    //    categoryIcon = me.config.categoryIcon3;
                    //}
                    categoryIcon="1,2,3,4";
                }
            }

            var serviceTypeHtml = "";
            if (categoryIcon != "") {
                var icons = categoryIcon.split(",");
                for (var ii = 0, jj = icons.length; ii < jj; ii++) {
                    if (parseInt(icons[ii]) == 1) {
                        serviceTypeHtml += '<li><a href="javascript:void(0);" title="争分夺秒，急你所急"><span class="int_bg">闪电发货</span></a></li>';
                    } else if (parseInt(icons[ii]) == 2) {
                        serviceTypeHtml += '<li><a href="javascript:void(0);" title="商城出品，必属精品;皇冠卖家，热力倾销"><span class="int_bg integrity_ul_hb">灵活购买</span></a></li>';
                    } else if (parseInt(icons[ii]) == 3) {
                        serviceTypeHtml += '<li><a href="javascript:void(0);" title="购买保险，如有商品被找回、封号等，保险公司将对您进行赔付。"><span class="int_bg integrity_ul_bz">安心买</span></a></li>';
                    } else if (parseInt(icons[ii]) == 4) {
                        serviceTypeHtml += '<li><a href="javascript:void(0);" title="购买此商品，5173将代您向中国慈善事业捐赠1分钱"><span class="int_bg integrity_ul_ai">爱心奉献</span></a></li>';
                    } else if (parseInt(icons[ii]) == 5) {
                        serviceTypeHtml += '<li><a href="javascript:void(0);" title="闪电发货无拍卖行风险和交易金额限制"><span class="service_type_5">AAA帐号</span></a></li>';
                    }
                }
            }

            //if(goods.goodsCat==3){
            //    serviceTypeHtml+="<li class='sellersetting'>成功率："+goods.successPercent*100+"%</li>";
            //    serviceTypeHtml+="<li class='sellersetting'>商家信誉：<i class='ico_dm' style='width:"+(13+(goods.praiseCount-1)*15)+"px;'></i></li>";
            //    serviceTypeHtml+="<li class='sellersetting'>月成交笔数："+goods.monthDealCount+"</li>";
            //    serviceTypeHtml+="<li class='sellersetting'>平均发货速度："+goods.deliverSpeed+"分钟</li>";
            //}
            if(serviceTypeHtml==""){
                serviceTypeHtml="<li></li>";
            }

            return serviceTypeHtml;
        },
        /**
         * 显示/隐藏游戏币下拉框
         */
        toggleGoldSelect: function(event) {
            // 阻止事件冒泡
            //event.stopPropagation();

            if (isNull(me.config)) {
                return;
            }

            var id = $(this).parent().parent().attr("id");
            var goodsId = id.substring(id.indexOf("_")+1);
            var popupMenu = $("#goods_select_" + goodsId);
            if (popupMenu.length > 0) {
                var isHide = popupMenu.is(":hidden");
                // 先隐藏所有select下拉框
                $(".hot_recommend .select_list_popup").hide();

                if (isHide) {
                    popupMenu.show();
                }
            } else {
                // 先隐藏所有select下拉框
                $(".hot_recommend .select_list_popup").hide();

                var goldCountArr = me.config.goldCounts.split(",");
                var li = "";
                for (var i = 0; i < goldCountArr.length; i++) {
                    li += "<li goldCount=" + goldCountArr[i] + "><a href='javascript:void(0)'>" + goldCountArr[i] + "</a></li>";
                }
                var html = '<div id="goods_select_' + goodsId + '" class="select_list_popup"><ul>' + li + '</ul></div>';
                $("#goods_" + goodsId + " .popup_select").html(html);

                // 游戏币下拉选项点击事件
                $("#goods_select_" + goodsId + " li").click(function(event) {
                    $("#goods_" + goodsId + " .input_select_list_number").val($(this).attr("goldCount"));
                    // 触发事件
                    $("#goods_" + goodsId + " .input_select_list_number").trigger("input");

                    // 点击后隐藏select下拉框
                    $("#goods_select_" + goodsId).hide();

                    // 阻止事件冒泡，触发父级toggleGoldSelect()事件
                    //event.stopPropagation();
                    return false;
                });
            }

            //阻止事件冒泡
            return false;
        },
        /**
         * 打开下单地址
         */
        openOrderUrl: function() {
            var id = $(this).parent().parent().attr("id");
            var goodsTypeName=$(this).parent().parent().attr("goodsTypeName");   /** ZW_C_JB_00008_2017/5/15 add **/
            var goodsCat =id.split("_")[1];
            var goodsId = id.substring(id.lastIndexOf("_") + 1);
            var goods = me.getGoodsInfo(goodsId,goodsCat);
            if (goods != null) {
                var goldCount = $("#goods_" + goods.goodsCat + "_" + goodsId + " .input_select_list_number").val();
                if (isNull(goldCount) || goldCount == 0) {
                    alert("请输入需要购买的游戏币数量！");
                    return;
                }
                if (!isNumber(goldCount)) {
                    alert("请输入正确的游戏币购买数量！");
                    return;
                }

                // 获取最小游戏币购买数量
                var smallestGoldCount = me.getDefaultGoldCount(me.minBuyAmount,goods);
                if (goldCount < smallestGoldCount) {
                    alert(goods.gameName + "最小游戏币购买数量不能少于" + smallestGoldCount + goods.moneyName);
                    return;
                }

                //if (goods.goodsCat != 2) {
                if (goods.goodsCat == 1) {
                    // 不是栏目2需要判断购买数量是否大于库存数量
                    if (goldCount > goods.sellableCount) {
                        alert("该价格最大库存量是:" + goods.sellableCount+",请不要超买哦!");
                        return;
                    }
                }

                var gameName = escape(goods.gameName);
                var gameRegion = escape(goods.region);
                var gameServer = escape(goods.server);
                var gameRace = isNull(goods.gameRace) ? "" : escape(goods.gameRace);
                var gameId = isNull(goods.gameId) ? "" : goods.gameId;
                var regionId = isNull(goods.regionId) ? "" : goods.regionId;
                var serverId = isNull(goods.serverId) ? "" : goods.serverId;
                var goodsId = isNull(goods.id) ? "" : goods.id;
                var raceId = isNull(goods.raceId) ? "" : goods.raceId;
                var goodsTypeName = escape(goods.goodsTypeName);
                //var goodsCat = goods.goodsCat;

                if(goodsCat==1||goodsCat==2){
                    window.open(baseHtmlUrl + "createorder.html?gameId=" + gameId + "&regionId=" + regionId + "&serverId=" + serverId + "&raceId=" + raceId + "&gameName=" + gameName + "&gameRegion=" +gameRegion + "&gameServer=" + gameServer + "&gameRace=" + gameRace + "&goodsId=" + goodsId +"&goodsCat=" + goodsCat + "&goldCount=" + goldCount + "&goodsTypeName="+goodsTypeName+"&t="+(new Date()).getTime(), "_blank"); /** ZW_C_JB_00008_2017/5/15 add **/
                }
                else if(goodsCat==3){
                    window.open(baseHtmlUrl + "sellergoodslist.html?gameName=" + gameName + "&zoneName=" +gameRegion + "&serverName=" + gameServer + "&raceName=" + gameRace +"&goodsTypeName="+goodsTypeName+"&t="+(new Date()).getTime(), "_blank");   /** ZW_C_JB_00008_2017/5/15 add **/
                }
            }
        },
        /**
         * 改变卖家入驻文字颜色
         */
        changeSellerEnterColor: function() {
            // 卖家入驻文字闪动
            var i = 0;
            setInterval(function () {
                $(".sellerEnterLink").css("color", i == 0 ? "#06c" : "#f60");
                i == 2 ? i = 0 : i++;
            }, 300);
        },
        /**
         * 获取商品信息
         */
        getGoodsInfo: function (goodsId,goodsCat) {
            for (var i = 0, j = me.goodsList.length; i < j; i++) {
                if (me.goodsList[i].id == parseInt(goodsId)&&me.goodsList[i].goodsCat==goodsCat) {
                    goods = me.goodsList[i];
                    return goods;
                }
            }
            return null;
        }
    };

    function getSellAdd(goods){
        var msg="";
        if (goods.goodsCat == 3&&goods.gameName == '天涯明月刀'&&goods.sellerLoginAccount == 'fafafa000')
            msg="<div class='sellAd'>在此店铺购买单笔超过500元的订单额外赠送100金。</div>";
        return msg;
    }
})(jQuery);

