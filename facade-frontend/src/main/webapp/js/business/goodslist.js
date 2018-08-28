// С�ܲ���
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
 * ��ȡ��Ϸ���̳�������Ʒ
 *         Update History
 *         Date         Name            Reason For Update
 *         ----------------------------------------------
 *         2017/5/12    wrf              ZW_C_JB_00008�̳�����ͨ��
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
     * ��Ϸ���̳�������Ʒ
     */
    var me = GameGoldMall.HotRecommend = {
        container: "dvMoneyMall",
        /**
         * ������Ϣ
         */
        config: null,
        /**
         * ������Ʒ����
         */
        goodsList: null,

        analysisUrlResponse:null,
        /**
         * ��ȡ������Ʒ����
         */
        load: function () {
            // �ж�ҳ��Ԫ���Ƿ����
            if ($("#" + me.container).length <= 0) {
                return;
            }
            var request = {};
            request.currentUrl = (function(){
                var currentUrl = window.location.href;
                // ȥ������Ĳ�������ֹ��̨���������������ַ��������쳣
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
         * ����������Ʒ�б�
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
                '<div class="hot_buy_w1"><a class="sellerEnterLink" href="' + lowestPriceUrl + '" target="_blank">�۸��ѯ</a></div>'+
                '<div class="hot_buy_w2">��Ʒ����</div>' +
                '<div class="hot_buy_w3">��������</div>' +
                '<div class="hot_buy_w4">������</div>' +
                '<div class="hot_buy_w5">��������</div>' +
                '</div>' +
                '<strong>' +
                '<a class="sellerEnterLink" target="_blank" href="' + sellerEnterUrl + '">���ż����������������פ</a>' +
                '</strong>' +
                '</div>' +
                '<div class="hot_recobox">' +
                '<ul id="hot_recommend_goods" class="hot_buy_list"></ul>' +
                '</div>';
            var parent = $("#" + me.container);
            parent.attr("class", "hot_recommend");
            parent.html(html);

            if (me.goodsList.length != 0){
                // �����������Ʒ�б�
                me.buildGoodsList();

                // ���¼�
                me.bindEvents();
            }

            /* if(me.goodsList.length <= 2){
             var li =
             '<li>' +
             '<div class="hot_buy_detail_w7">' +
             '���ż����������������ס����λ�Դ�'+
             '</div>' +
             '<div class="hot_buy_detail_w8">' +
             '<a class="a_jion_now" href="http://yxbmall.5173.com/applyseller.html" target="_blank"></a>' +
             '</div>' +
             '</li>';

             $("#hot_recommend_goods").append(li);
             }*/

            // ������פ������˸
            me.changeSellerEnterColor();
        },
        /**
         * ������Ʒ�б�li
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
                var price = toDecimal2(1 / parseFloat(goods.unitPrice)); // 1Ԫ��Ӧ���ٽ�
                // var moneyName = isNull(goods.moneyName) ? "��" : goods.moneyName;
                var count = me.getDefaultGoldCount(me.minBuyAmount,goods);
                var totalPrice = toDecimal2(parseFloat(goods.unitPrice) * count);
                var totalPriceArray = totalPrice.toString().split(".");
                var repositoryCount = "";// �������
                var settingid = "bq_1013_1432541248318";

                if(goods.goodsCat == 1 /*|| goods.goodsCat == 3*/){
                    title = '<span style="color:red">' + title + '</span>';
                    repositoryCount = goods.sellableCount;
                } else if (goods.goodsCat == 2 || goods.goodsCat == 3) {
                     //if (goods.gameName == '���³�����ʿ') {
                         //title = '<a href="http://aid.5173.com/sc/jbsc/1746.html" target="_blank">' + title + '</a>';
                     //}
                    repositoryCount = "����";
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
                    '<p class="hot_price">1Ԫ=<span>' + price + '</span>' + /**moneyName**/unitName + goods.goodsTypeName+'</p>' +  /**ZW_C_JB_00008_20170512  mod**/
                    '<p class="repository">���������' + repositoryCount + '</p>';
                html+=getSellAdd(goods);
                html+= '</div>' +
                    '<div class="hot_buy_detail_w3" style="' +((goods.goodsCat == 2)?"z-index:105;":"z-index:101;") + '">' +
                    '<div style="' +((goods.goodsCat == 2)?"z-index:106;":"z-index:102;") + '" class="' + ((goods.goodsCat == 2)?"select_list_input":"select_list_input3")  + '">' +
                    '<input id="goods_'+ goods.goodsCat + '_' + goods.id +'_goldCount" name="goldCount"  value="' + count + '" maxlength="8" class="input_select_list_number" style="z-index:103;">' +
                    '<span class="' +((goods.goodsCat == 2)?"select_list_unit":"select_list_unit2") + '">' + /**moneyName**/unitName +goods.goodsTypeName+ '</span>'; /**ZW_C_JB_00008_20170512  mod**/
                if (goods.goodsCat == 2) {
                    // ������Ŀ2�Ĳ���ʾ������
                    html += '<div class="popup_select" style="z-index:107;"></div>';
                }
                html+=          '</div>' +
                    '</div>' +
                    '<div class="hot_buy_detail_w4">' +
                    '<span class="hot_price_account_buy">' +
                    '<strong>' + totalPriceArray[0] + '</strong>.' + totalPriceArray[1] + '<em>Ԫ</em>' +
                    '</span>' +
                    '</div>' +
                    '<div class="hot_buy_detail_w6">' +
                    '<ul class="integrity_ul">' + serviceTypeHtml + '</ul>' +
                    '</div>' +
                    '<div class="hot_buy_detail_w5">' +
                    '<a class="a_buy_now" href="javascript:void(0);">��������</a>' +
                    /*'<div style="width:185px; height:7px; "></div>'+*/
                    /*'<a class="a_online_consult" onclick="NTKF.im_openInPageChat(\'bq_1013_1432633336480\')"></a>' +*/
                    '</div>';

                html+=  '</li>';
            }

            $("#hot_recommend_goods").html(html);
            //html="";

            ////var gameName='��������';
            ////var gameArea='˫�߰ˡ�����';
            ////var gameServer='һͳǧ��';
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
            //if(gameName=="����") {
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
            //                var priceTemp = toDecimal2(1 / parseFloat(json.masterPrice)); // 1Ԫ��Ӧ���ٽ�
            //                var count = priceTemp * smallestAmount;
            //                count = Math.ceil(count);
            //
            //                var title = "��������";
            //                jpPrice = json.masterPrice;
            //                var gameInfo = gameArea + "/" + gameServer;
            //                var serviceTypeHtml = me.getServiceTypeIcon(me.goodsList[0]);
            //                var price = toDecimal2(1 / parseFloat(json.masterPrice)); // 1Ԫ��Ӧ���ٽ�
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
            //                    '<p class="hot_price">1Ԫ=<span>' + price + '</span>' + moneyName + '</p>' +
            //                    '<p class="repository">���������' + json.quantity + '</p>';
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
            //                    '<strong>' + totalPriceArray[0] + '</strong>.' + totalPriceArray[1] + '<em>Ԫ</em>' +
            //                    '</span>' +
            //                    '</div>' +
            //                    '<div class="hot_buy_detail_w6">' +
            //                    '<ul class="integrity_ul">' + serviceTypeHtml + '</ul>' +
            //                    '</div>' +
            //                    '<div class="hot_buy_detail_w5">' +
            //                    '<a class="buyJp" href="javascript:void(0);">��������</a>' +
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
            //                        alert("�ü۸����������:" + json.quantity + ",�벻Ҫ����Ŷ!");
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
            // ʵʱ��أ�input������ı仯���Լ�ʱ���ɼ۸�
            $('.hot_recommend .input_select_list_number').bind('input propertychange', me.changeGoldCount);

            // ��Ϸ�ҹ������������
            $(".hot_recommend .input_select_list_number").css("ime-mode", "disabled");
            $(".hot_recommend .input_select_list_number").bind('keypress', function (event) {
                var keycode = document.all ? event.keyCode : event.charCode;
                if (keycode != 8 && keycode != 9 && keycode != 0) {
                    if ((keycode < 48 || keycode > 57)) {
                        return false;
                    }
                }
            });

            // ���select���������¼�
            $(".hot_recommend .select_list_input").bind('click', me.toggleGoldSelect);

            // ���������¼�
            $(".hot_recommend .a_buy_now").bind('click', me.openOrderUrl);

            // ��ҳ��հ״������������Ϸ������������
            $(document.body).bind('click', function () {
                // ���������������Ϸ��select������
                $(".hot_recommend .select_list_popup").hide();
            });
        },
        /**
         * ��Ϸ�ҹ��������ı��¼�
         */
        changeGoldCount: function () {
            // ��ǰ��������
            var goldCount = $(this).val();

            if (isNull(goldCount)) {
                goldCount = 0;
                $(this).val(0);
            } else if (parseInt(goldCount) < 0) {
                goldCount = 0;
                $(this).val(0);
            }

            if (!isNumber(goldCount)) {
                alert("��Ϸ�ҹ���������������ȷ�����֣�");
                return;
            }

            // ����������99999999
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
                $("#goods_" + goods.goodsCat + "_" + goodsId + " .hot_price_account_buy").html("<strong>" + totalPriceArray[0] + "</strong>." + totalPriceArray[1] + "<em>Ԫ</em>");
            }
            else{
                var totalPrice = toDecimal2(parseFloat(jpPrice) * parseInt(goldCount));
                var totalPriceArray = totalPrice.toString().split(".");
                $("#goods_2 .hot_price_account_buy").html("<strong>" + totalPriceArray[0] + "</strong>." + totalPriceArray[1] + "<em>Ԫ</em>");
            }
        },
        /**
         * ��ȡ��С��Ϸ�ҹ�������
         * @param goods
         * @returns {number}
         */
        getDefaultGoldCount: function(minBuyCount,goods) {
            // ��С������
            // var smallestAmount = me.getSmallestAmount(goods.gameName, goods.goodsTypeName);

            // �������ٹ�����Ϸ������
            var count =  Math.ceil(minBuyCount/goods.unitPrice);
            return count;
        },
        /**
         * ������Ϸ�ж���С��Ϸ�ҹ�����
         * @param gameName
         */
        getSmallestAmount: function(gameName, goodsTypeName) {
            var smallestAmount = 0;
            if (gameName == "����") {
                smallestAmount = 35; // �������ٹ���35Ԫ
            } else if (gameName == "��ս��") {
                smallestAmount = 50; // ��ս�����ٹ���50Ԫ
            } else if (gameName == "�������µ�") {
                smallestAmount = 100; // �������ٹ���100Ԫ
            } else {
                smallestAmount = 20; // ������Ϸ���ٹ���20Ԫ
            }
            if(goodsTypeName=="��ս��"){
                smallestAmount = 50; //��ս����С����50Ԫ
            }
            if (goodsTypeName == "Ԫ��Ʊ") {
                smallestAmount = 50; //Ԫ��Ʊ��С����50Ԫ
            }
            return smallestAmount;
        },
        /**
         * ��ȡ��������ͼ��
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
                        serviceTypeHtml += '<li><a href="javascript:void(0);" title="���ֶ��룬��������"><span class="int_bg">���緢��</span></a></li>';
                    } else if (parseInt(icons[ii]) == 2) {
                        serviceTypeHtml += '<li><a href="javascript:void(0);" title="�̳ǳ�Ʒ��������Ʒ;�ʹ����ң���������"><span class="int_bg integrity_ul_hb">����</span></a></li>';
                    } else if (parseInt(icons[ii]) == 3) {
                        serviceTypeHtml += '<li><a href="javascript:void(0);" title="�����գ�������Ʒ���һء���ŵȣ����չ�˾�����������⸶��"><span class="int_bg integrity_ul_bz">������</span></a></li>';
                    } else if (parseInt(icons[ii]) == 4) {
                        serviceTypeHtml += '<li><a href="javascript:void(0);" title="�������Ʒ��5173���������й�������ҵ����1��Ǯ"><span class="int_bg integrity_ul_ai">���ķ���</span></a></li>';
                    } else if (parseInt(icons[ii]) == 5) {
                        serviceTypeHtml += '<li><a href="javascript:void(0);" title="���緢���������з��պͽ��׽������"><span class="service_type_5">AAA�ʺ�</span></a></li>';
                    }
                }
            }

            //if(goods.goodsCat==3){
            //    serviceTypeHtml+="<li class='sellersetting'>�ɹ��ʣ�"+goods.successPercent*100+"%</li>";
            //    serviceTypeHtml+="<li class='sellersetting'>�̼�������<i class='ico_dm' style='width:"+(13+(goods.praiseCount-1)*15)+"px;'></i></li>";
            //    serviceTypeHtml+="<li class='sellersetting'>�³ɽ�������"+goods.monthDealCount+"</li>";
            //    serviceTypeHtml+="<li class='sellersetting'>ƽ�������ٶȣ�"+goods.deliverSpeed+"����</li>";
            //}
            if(serviceTypeHtml==""){
                serviceTypeHtml="<li></li>";
            }

            return serviceTypeHtml;
        },
        /**
         * ��ʾ/������Ϸ��������
         */
        toggleGoldSelect: function(event) {
            // ��ֹ�¼�ð��
            //event.stopPropagation();

            if (isNull(me.config)) {
                return;
            }

            var id = $(this).parent().parent().attr("id");
            var goodsId = id.substring(id.indexOf("_")+1);
            var popupMenu = $("#goods_select_" + goodsId);
            if (popupMenu.length > 0) {
                var isHide = popupMenu.is(":hidden");
                // ����������select������
                $(".hot_recommend .select_list_popup").hide();

                if (isHide) {
                    popupMenu.show();
                }
            } else {
                // ����������select������
                $(".hot_recommend .select_list_popup").hide();

                var goldCountArr = me.config.goldCounts.split(",");
                var li = "";
                for (var i = 0; i < goldCountArr.length; i++) {
                    li += "<li goldCount=" + goldCountArr[i] + "><a href='javascript:void(0)'>" + goldCountArr[i] + "</a></li>";
                }
                var html = '<div id="goods_select_' + goodsId + '" class="select_list_popup"><ul>' + li + '</ul></div>';
                $("#goods_" + goodsId + " .popup_select").html(html);

                // ��Ϸ������ѡ�����¼�
                $("#goods_select_" + goodsId + " li").click(function(event) {
                    $("#goods_" + goodsId + " .input_select_list_number").val($(this).attr("goldCount"));
                    // �����¼�
                    $("#goods_" + goodsId + " .input_select_list_number").trigger("input");

                    // ���������select������
                    $("#goods_select_" + goodsId).hide();

                    // ��ֹ�¼�ð�ݣ���������toggleGoldSelect()�¼�
                    //event.stopPropagation();
                    return false;
                });
            }

            //��ֹ�¼�ð��
            return false;
        },
        /**
         * ���µ���ַ
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
                    alert("��������Ҫ�������Ϸ��������");
                    return;
                }
                if (!isNumber(goldCount)) {
                    alert("��������ȷ����Ϸ�ҹ���������");
                    return;
                }

                // ��ȡ��С��Ϸ�ҹ�������
                var smallestGoldCount = me.getDefaultGoldCount(me.minBuyAmount,goods);
                if (goldCount < smallestGoldCount) {
                    alert(goods.gameName + "��С��Ϸ�ҹ���������������" + smallestGoldCount + goods.moneyName);
                    return;
                }

                //if (goods.goodsCat != 2) {
                if (goods.goodsCat == 1) {
                    // ������Ŀ2��Ҫ�жϹ��������Ƿ���ڿ������
                    if (goldCount > goods.sellableCount) {
                        alert("�ü۸����������:" + goods.sellableCount+",�벻Ҫ����Ŷ!");
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
         * �ı�������פ������ɫ
         */
        changeSellerEnterColor: function() {
            // ������פ��������
            var i = 0;
            setInterval(function () {
                $(".sellerEnterLink").css("color", i == 0 ? "#06c" : "#f60");
                i == 2 ? i = 0 : i++;
            }, 300);
        },
        /**
         * ��ȡ��Ʒ��Ϣ
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
        if (goods.goodsCat == 3&&goods.gameName == '�������µ�'&&goods.sellerLoginAccount == 'fafafa000')
            msg="<div class='sellAd'>�ڴ˵��̹��򵥱ʳ���500Ԫ�Ķ�����������100��</div>";
        return msg;
    }
})(jQuery);
