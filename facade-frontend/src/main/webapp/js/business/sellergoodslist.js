var gameId = "";
var gameName = "";
var zoneId = "";
var zoneName = "";
var serverName = "";
var raceName = "";
var serverId = "";
var initFlag = true;
var flagName = "";
var goodsTypeName = "";
var goodsTypeId = "";
var minBuyAmounts = {};
$(document).ready(function () {
    //console.log(baseHtmlUrl + "sellergoodslist.html?gameName=" + escape("剑灵") + "&zoneName=" +escape("电信一区") + "&serverName=" + escape("飞扇堂") + "&raceName=&t="+(new Date()).getTime())
    // console.log(unescape(getUrlParam("gameName")) + "====" + unescape(getUrlParam("zoneName")) + "====" + unescape(getUrlParam("serverName")))
    gameName = $.trim(getUrlParam("gameName"));
    zoneName = $.trim(getUrlParam("zoneName"));
    serverName = $.trim(getUrlParam("serverName"));
    raceName = $.trim(getUrlParam("raceName"));
    goodsTypeName = $.trim(getUrlParam("goodsTypeName"));
    // gameName=$.trim("剑灵");
    // zoneName=$.trim("电信一区");
    // serverName=$.trim("飞扇堂");

    initPageData();

    //初始化区服快速查询功能
    $('#searchSerQuick').bind('input propertychange', function () {
        resetOption($.trim($("#searchSerQuick").val()), "dlgs");
    });

    //点击关闭
    $(".close_btn").click(function () {
        $('#gsBox').hide();
        $('#searchbar_arrow').hide();
    });

    //点击选择游戏时，加载游戏列表
    $("#gs_game").click(function () {
        $('#gsBox').show();
        $('#searchbar_arrow').show();
        $("#searchbar_arrow").css("left", "190px");
        GetGame();
    });

    //点击大区的时候，加载对应数据
    $("#gs_area").click(function () {
        $('#gsBox').show();
        $('#searchbar_arrow').show();
        $("#searchbar_arrow").css("left", "270px");

        if (gameId != null && gameId != "") {
            GetGameZone(gameId, gameName);
        }
    });

    //点击服务器的时候，加载对应数据
    $("#gs_server").click(function () {
        $('#gsBox').show();
        $('#searchbar_arrow').show();
        $("#searchbar_arrow").css("left", "350px");

        if ($("#gs_area").text() == "全部区") {
            $('#gsBox').hide();
            $('#searchbar_arrow').hide();
            return;
        }
        if (zoneId != null && zoneId != "") {
            GetGameServer(zoneId, zoneName);
        }
    });

    //点击商品类型的时候，加载对应数据
    $("#gs_type").click(function () {
        $('#gsBox').show();
        $('#all').hide();
        $('#searchbar_arrow').show();
        $("#searchbar_arrow").css("left", "350px");

        if (goodsTypeName != null && goodsTypeName != "") {
            Getcategory(gameId, gameName, false);
        }
    });

    //点击搜索功能，加载数据和对应效果
    $("#gsSearchBtn").click(function () {
        $("#gsBox").hide();
        $('#searchbar_arrow').hide();
        raceName = "";
        search();
    });

    //选择区服时选择全部时的效果
    $("#all").click(function () {
        if ($("#all").attr("flag") == "region") {
            $("#gs_area").html("全部区");
            $("#gs_server").html("全部服");
            zoneName = "全部区";
            serverName = "全部服";
        } else if ($("#all").attr("flag") == "server") {
            $("#gs_server").html("全部服");
            serverName = "全部服";
        } else {
            $("#gs_type").html("全部类型");
            goodsTypeName = "";
        }
        $("#gsBox").hide();
        $('#searchbar_arrow').hide();
    });
});

function initPageData() {
    $('#gsBox').hide();
    $('#searchbar_arrow').hide();
    $("#SiteMap1_divSelectedConditions").hide();
    $("#gs_game").html(isNull(gameName) ? "游戏名称" : gameName);

    $('#gsBox').hide();
    $('#searchbar_arrow').hide();

    //加载游戏区服
    for (var p in gameData) {
        if (gameData[p].value == gameName) {
            Getcategory(gameData[p].code, gameName);
            break;
        }
    }
}

//搜索功能
function search() {
    var gameTemp = $("#gs_game").html();//游戏名
    var regionTemp = $("#gs_area").html();//大区
    var serverNameTemp = $("#gs_server").html();//服务器
    var goodsTypeNameTemp = $("#gs_type").html();

    $(".push_up").html("显示全部");
    $(".push_up").attr("class", "push");
    $(".filterbox .sing_filbox dl").css("height", "48px");
    $("#gameCategory").show()
    selectCategoryMenu("category");
    //$("#CategorySelector1_divGameNavigationBar").hide();

    //选中游戏
    if (gameTemp != "游戏名称") {
        $("#SiteMap1_divSelectedConditions").show();
        $("#cancle_game").attr("tid", gameId);
        $("#cancle_game").attr("title", "点击取消 游戏：" + gameTemp);
        $("#cancle_game a span").html(gameTemp);
        $("#navGame").html(gameTemp);
    }
    //选中游戏区
    if (regionTemp != "游戏区" && regionTemp != "全部区") {
        $("#cancle_area").attr("tid", zoneId);
        $("#cancle_area").attr("title", "点击取消 游戏区：" + regionTemp);
        $("#cancle_area a span").html(regionTemp + "<em></em>");
        $("#cancle_area").show();
    } else {
        $("#cancle_area").hide();
    }

    //选中游戏服
    if (serverNameTemp != "游戏服" && serverNameTemp != "全部服") {
        $("#cancle_server").attr("tid", serverId);
        $("#cancle_server").attr("title", "点击取消 游戏服：" + serverNameTemp);
        $("#cancle_server a span").html(serverNameTemp + "<em></em>");
        $("#cancle_server").show();
    } else {
        $("#cancle_server").hide();
    }

    //选择区服对应显示数据
    if (regionTemp == "游戏区" || regionTemp == "全部区") {
        showQf("area");
        $("#CategorySelector1_divGameNavigationBar").show();
    } else if (serverNameTemp == "游戏服" || serverNameTemp == "全部服") {
        showQf("server");
        $("#CategorySelector1_divGameNavigationBar").show();
    } else {
        $("#gameRegion").hide();
    }

    //选中商品类型
    if (goodsTypeNameTemp != "全部类型" && goodsTypeNameTemp != "全部") {
        $("#cancle_category").attr("tid", goodsTypeId);
        $("#cancle_category").attr("title", "点击取消 商品类型：" + goodsTypeNameTemp);
        //$("#cancle_category a span").html(goodsTypeNameTemp + "<em></em>");
        $("#cancle_category a span").html(goodsTypeNameTemp);
        $("#cancle_category").show();
        //$("#gameCategory").hide();
    }
    else {
        $("#cancle_category").hide();
        selectCategoryMenu("category");
        $("#CategorySelector1_divGameNavigationBar").show();
    }

    //选中阵营
    var raceTemp = $("#dlraceid a.selected").html();//阵营
    //选择阵营触发事件，重新加载数据
    if (gameTemp == "魔兽世界(国服)") {
        if (!isNull(raceTemp)) {
            $("#cancle_race").attr("title", "点击取消 阵营：" + raceTemp);
            $("#cancle_race a span").html(raceTemp + "<em></em>");
            $("#cancle_race").show();
            $("#gameRace").hide();
        } else {
            $("#cancle_race").hide();
            $("#gameRace").show();
            $("#CategorySelector1_divGameNavigationBar").show();
        }

        $("#dlraceid a").unbind("click").bind('click', function () {
            raceName = $(this).html();
            $("#sifting_box dd:eq(3)").remove();
            $("#sifting_box dd:eq(2)").after("<dd id='cancle_race' title=\"阵营：" + raceName + "\" onclick=\"cancleSelect($(this))\"><a onclick=\"#\"><span>" + raceName + "<em></em></span></a></dd>");
            $("#gameRace").hide();
            $("#dlraceid a").removeClass("selected");
            $(this).addClass("selected");

            GameGoldMall.HotRecommend.load();
        });
    } else {
        $("#cancle_race").hide();
        $("#gameRace").hide();
    }

    GameGoldMall.HotRecommend.load();

    //设置选择区服显示隐藏
    // if( $("#gs_area").html()=="全部区"||$("#gs_area").html()=="游戏区"||$("#gs_server").html()=="全部服"||$("#gs_server").html()=="游戏服"){
    //     $("#CategorySelector1_divGameNavigationBar").show();
    //     $("#gameRegion").show();
    //     if(gameName=="魔兽世界(国服)"){
    //         if($("#cancle_server").css("display")=="none"&&$("#cancle_race").css("display")=="none"){
    //             $("#gameRegion").show();
    //             $("#gameRace").show();
    //         }
    //         else if($("#cancle_server").css("display")=="none"&&$("#cancle_race").css("display")!="none"){
    //             $("#gameRegion").show();
    //             $("#gameRace").hide();
    //         }
    //         else if($("#cancle_server").css("display")!="none"&&$("#cancle_race").css("display")=="none"){
    //             $("#gameRegion").hide();
    //             $("#gameRace").show();
    //         }
    //         else{
    //             $("#gameRegion").hide();
    //             $("#gameRace").hide();
    //             $("#CategorySelector1_divGameNavigationBar").hide();
    //         }
    //     }
    //     else{
    //         $("#gameRace").hide();
    //     }
    // }
    // else
    // {
    //     if(gameName=="魔兽世界(国服)") {
    //         $("#gameRegion").hide();
    //         $("#gameRace").show();
    //         $("#CategorySelector1_divGameNavigationBar").show();
    //         if($("#cancle_server").css("display")=="none"&&$("#cancle_race").css("display")=="none"){
    //             $("#gameRegion").show();
    //             $("#gameRace").show();
    //         }
    //         else if($("#cancle_server").css("display")=="none"&&$("#cancle_race").css("display")!="none"){
    //             $("#gameRegion").show();
    //             $("#gameRace").hide();
    //         }
    //         else if($("#cancle_server").css("display")!="none"&&$("#cancle_race").css("display")=="none"){
    //             $("#gameRegion").hide();
    //             $("#gameRace").show();
    //         }
    //         else{
    //             $("#gameRegion").hide();
    //             $("#gameRace").hide();
    //             $("#CategorySelector1_divGameNavigationBar").hide();
    //         }
    //     }
    //     else
    //     {
    //         $("#CategorySelector1_divGameNavigationBar").hide();
    //     }
    // }
}

//取消选择
function cancleSelect(obj) {
    if (obj.attr('tid') == goodsTypeId) return
    $('#searchSerQuick').val("");
    $("#CategorySelector1_divGameNavigationBar").show();
    if (obj.attr("id") == "cancle_game") {
        $("#CategorySelector1_divGameNavigationBar").hide();
        $("#SiteMap1_divSelectedConditions").hide();
    }
    else if (obj.attr("id") == "cancle_area") {
        $("#cancle_server").hide();
        flagName = "area";
        //showQf("area");
        $("#gs_area").html("游戏区");
        $("#gs_server").html("游戏服");
    }
    else if (obj.attr("id") == "cancle_server") {
        flagName = "server";
        //showQf("server");
        $("#gs_server").html("游戏服");
    }
    else if (obj.attr("id") == "cancle_race") {
        $("#dlraceid a").removeClass("selected");
    } else if (obj.attr("id") == "cancle_category") {
        flagName = "category";
        $("#gs_type").html("全部类型");
    }
    obj.hide();

    search();
}

//加载区服
function showQf(flag) {
    $("#gameRegion").show();
    $("#dlgs").html("");
    if (flag == "area") {
        $("#gameRegion .choose").html("游戏区选择：");
        $.ajax({
            type: "GET",
            url: "http://fcd.5173.com/ajax.axd?methodName=gamesV31&cache=600&gameType=GameAreas&tradingType=other&id=" + gameId + "&jsoncallback=callarea",
            dataType: "jsonp",
            jsonp: "jsoncallback",
            scriptCharset: "GBK",
            jsonpCallback: "callarea",
            cache: true,
            success: function (jsonList) {
                var htmlstr = "";
                if (jsonList != null && jsonList != undefined) {
                    for (var i = 0; i < jsonList.length; i++) {
                        var json = jsonList[i];
                        htmlstr += "<dt><a href=\"#\" title=\"" + json.name + "\" onclick=\"selectMenu('" + json.id + "','" + json.name + "','" + flag + "')\">" + json.name + "</a></dt>";
                    }
                }
                $("#dlgs").html(htmlstr);
                filteroption("dlgs");
            }
        });
    }
    else if (flag == "server") {
        selectServerMenu(flag);
    }
}

//选择服务菜单
function selectServerMenu(flag) {
    $("#gameRegion .choose").html("按服务器选择：");
    $.ajax({
        type: "GET",
        url: "http://fcd.5173.com/ajax.axd?methodName=gamesV31&cache=600&gameType=GameServers&tradingType=other&id=" + zoneId + "&jsoncallback=callserver&t=" + new Date().getTime(),
        dataType: "jsonp",
        jsonp: "jsoncallback",
        scriptCharset: "GBK",
        jsonpCallback: "callserver",
        cache: true,
        success: function (jsonList) {
            var htmlstr = "";
            if (jsonList != null && jsonList != undefined) {
                for (var i = 0; i < jsonList.length; i++) {
                    var json = jsonList[i];
                    htmlstr += "<dt><a href=\"#\" title=\"" + json.name + "\" onclick=\"selectMenu('" + json.id + "','" + json.name + "','" + flag + "')\">" + json.name + "</a></dt>";
                }
            }
            $("#dlgs").html(htmlstr);
            filteroption("dlgs");
        }
    });
}

//选择商品类型菜单
function selectCategoryMenu(flag) {
    $("#gameCategory").show();
    $("#dlCategory").html("");
    $("#gameCategory .choose").html("按商品类型选择：");
    var request = {};
    request.gameName = gameName;
    request.enableMall = true;
    $.ajax({
        type: "GET",
        url: baseServiceUrl + "services/shGameConfig/getAllConfigByGameName?t=" + new Date().getTime(),
        data: request,
        contentType: "application/json; charset=UTF-8",
        dataType: "jsonp",
        jsonp: "callback",
        success: function (response) {
            var htmlstr = "";
            if (response != null && response != undefined) {
                shGameConfigList = response.shGameConfigList;
                if (shGameConfigList != null && shGameConfigList != undefined) {
                    for (var i = 0; i < shGameConfigList.length; i++) {
                        var json = shGameConfigList[i];
                        htmlstr += "<dt><a href=\"#\" title=\"" + json.goodsTypeName + "\" onclick=\"selectMenu('" + json.goodsTypeId + "','" + json.goodsTypeName + "','" + flag + "')\">" + json.goodsTypeName + "</a></dt>";
                        minBuyAmounts[json.goodsTypeName] = json;
                    }
                }
                $("#dlCategory").html(htmlstr);
                filteroption("dlCategory");
            }
        }
    });

}

function selectMenu(id, name, flag) {
    $('#searchSerQuick').val("");
    if (flag == "area") {
        $("#cancle_area").attr("tid", id);
        $("#cancle_area").attr("title", "点击取消 游戏区：" + name);
        $("#cancle_area a span").html(name + "<em></em>");
        $("#gs_area").html(name);
        $("#cancle_area").show();
        zoneId = id;
        //selectServerMenu("server");
    } else if (flag == "server") {
        $("#cancle_server").attr("tid", id);
        $("#cancle_server").attr("title", "点击取消 游戏服：" + name);
        $("#cancle_server a span").html(name + "<em></em>");
        $("#gs_server").html(name);
        $("#cancle_server").show();
        $("#gameRegion").hide();
    } else if (flag == "category") {
        $("#cancle_category").attr("tid", id);
        $("#cancle_category").attr("title", "点击取消 商品类型：" + name);
        $("#cancle_category a span").html(name + "<em></em>");
        $("#gs_type").html(name);
        $("#cancle_category").show();
        $("#gameCategory").hide();
    }
    search();
}

function showQfDiv(obj) {
    if (obj.attr("class") == "push") {
        obj.html("精简显示");
        obj.attr("class", "push_up");
        $(".filterbox .sing_filbox dl").css("height", "auto");
    }
    else {
        obj.html("显示全部");
        obj.attr("class", "push");
        $(".filterbox .sing_filbox dl").css("height", "48px");
    }
}

//加载游戏
function GetGame() {
    var html = "";
    for (var p in gameData) {
        var pCode = gameData[p].code;
        var pValue = gameData[p].value;
        html += "<li id=\"" + pCode + "\" lang=\"netgame\"><a class=\"hot\" title=\"" + pValue + "\" onclick=\"GetGameZone('" + pCode + "','" + pValue + "')\">" + pValue + "</a></li>"
    }
    $("#gsList").html(html);
    $(".gs_name dt h1").html("请选择游戏：");
    $("#all").hide();
}

//根据游戏的id获取大区
function GetGameZone(gameIdTemp, gameNameTemp) {
    gameId = gameIdTemp;
    gameName = gameNameTemp;
    $("#searchbar_arrow").css("left", "270px");
    $("#gs_game").html(gameName);
    $(".gs_name dt h1").html("请选择游戏区：");
    $("#all").show();
    $("#all").attr("flag", "region");
    $("#all").html("全部区");
    $.ajax({
        type: "GET",
        url: "http://fcd.5173.com/ajax.axd?methodName=gamesV31&cache=600&gameType=GameAreas&tradingType=other&id=" + gameId + "&jsoncallback=callarea&t=" + new Date().getTime(),
        dataType: "jsonp",
        jsonp: "jsoncallback",
        scriptCharset: "GBK",
        jsonpCallback: "callarea",
        cache: true,
        success: function (jsonList) {
            var htmlstr = "";
            if (jsonList != null && jsonList != undefined) {
                for (var i = 0; i < jsonList.length; i++) {
                    var json = jsonList[i];
                    htmlstr += "<li id=\"" + $.trim(json.id) + "\" lang=\"undefined\"><a onclick=\"GetGameServer('" + $.trim(json.id) + "','" + $.trim(json.name) + "')\">" + $.trim(json.name) + "</a></li>";

                    //第一次加载
                    if (initFlag) {
                        if ($.trim(json.name) == zoneName) {
                            GetGameServer(json.id, zoneName);
                            break;
                        }
                    }
                }
            }
            $("#gsList").html(htmlstr);
        }
    });
};

//根据选择的大区ID获取服务器
function GetGameServer(zoneIdTemp, zoneNameTemp) {
    zoneId = zoneIdTemp;
    zoneName = zoneNameTemp;
    $("#searchbar_arrow").css("left", "350px");
    $("#gs_area").html(zoneName);
    $(".gs_name dt h1").html("请选择游戏服：");
    $("#all").show();
    $("#all").attr("flag", "server");
    $("#all").html("全部服");
    $.ajax({
        type: "GET",
        url: "http://fcd.5173.com/ajax.axd?methodName=gamesV31&cache=600&gameType=GameServers&tradingType=other&id=" + zoneId + "&jsoncallback=callserver&t=" + new Date().getTime(),
        dataType: "jsonp",
        jsonp: "jsoncallback",
        scriptCharset: "GBK",
        jsonpCallback: "callserver",
        cache: true,
        success: function (jsonList) {
            var htmlstr = "";
            if (jsonList != null && jsonList != undefined) {
                for (var i = 0; i < jsonList.length; i++) {
                    var json = jsonList[i];
                    htmlstr += "<li id=\"" + $.trim(json.id) + "\" lang=\"undefined\"><a onclick=\"selectServer($(this))\">" + $.trim(json.name) + "</a></li>";

                    //第一次加载
                    if (initFlag) {
                        if ($.trim(json.name) == serverName) {
                            $("#gs_server").html(serverName);
                            serverId = json.id;
                            search();
                            initFlag = false;
                            break;
                        }
                    }
                }
            }
            $("#gsList").html(htmlstr);
        }
    });
};
//新增通货类型 by lcs 2017.5.12
//根据游戏名称获取启用的商品类型
function Getcategory(gameId, gameName, isShow) {
    $("#searchbar_arrow").css("left", "450px");
    $("#gs_game").html(gameName);
    $(".gs_name dt h1").html("请选择商品类型：");
    if (!isShow) {
        $("#all").hide();
    } else {
        $("#all").show();
        $("#all").attr("flag", "category");
        $("#all").html("全部类型");
    }
    var request = {};
    request.gameName = gameName;
    request.enableMall = true;
    $.ajax({
        type: "GET",
        url: baseServiceUrl + "services/shGameConfig/getAllConfigByGameName?t=" + new Date().getTime(),
        data: request,
        dataType: "jsonp",
        contentType: "application/json; charset=UTF-8",
        jsonp: "callback",
        success: function (request) {
            var htmlstr = "";
            if (request != null && request != undefined) {
                shGameConfigList = request.shGameConfigList;
                for (var i = 0; i < shGameConfigList.length; i++) {
                    var json = shGameConfigList[i];
                    htmlstr += "<li id=\"" + $.trim(json.id) + "\" lang=\"undefined\"><a onclick=\"selectCategory($(this))\">" + $.trim(json.goodsTypeName) + "</a></li>";

                    minBuyAmounts[json.goodsTypeName] = json;
                    //第一次加载
                    if (initFlag) {
                        $("#gs_type").html(goodsTypeName);
                        if ($.trim(json.goodsTypeName) == goodsTypeName) {
                            goodsTypeId = json.id;
                            GetGameZone(gameId, gameName);
                            break;
                        }
                    }
                }
            }
            $("#gsList").html(htmlstr);
        }
    });
};


function selectServer(obj) {
    $("#gs_server").html(obj.html());
    $('#gsBox').hide();
    $('#searchbar_arrow').hide();
}

function selectCategory(obj) {
    $("#gs_type").html(obj.html());
    $('#gsBox').hide();
    $('#searchbar_arrow').hide();
}

function showLoading() {
    var screenWidth = $(window).width();//当前窗口宽度
    var screenHeight = $(window).height();//当前窗口高度

    $("#divLoading").css({
        "display": "",
        "position": "fixed",
        "background": "#000",
        "z-index": "10000",
        "-moz-opacity": "0.5",
        "opacity": ".50",
        "filter": "alpha(opacity=50)",
        "width": screenWidth,
        "height": screenHeight
    });
    $("#loadingInfo").show();
    $("#divLoading").show();
}

(function ($) {
    if (typeof GameGoldMall === 'undefined') {
        GameGoldMall = {};
    }
    var me = GameGoldMall.HotRecommend = {
        totalCount: null,
        goodsList: null,
        load: function () {
            showLoading();
            var paraRaceName = "";
            if ($("#cancle_race").css("display") != "none") {
                paraRaceName = $("#cancle_race").text();
            }
            $("#btnpricegrade").unbind("click");
            $("#btnpricegrade").click(function () {
                me.initData($("#gs_game").html(), $("#gs_area").html(), $("#gs_server").html(), "UNIT_PRICE", "ASC", paraRaceName, $("#gs_type").html());
            });
            $("#btnNumSort").unbind("click");
            $("#btnNumSort").click(function () {
                me.initData($("#gs_game").html(), $("#gs_area").html(), $("#gs_server").html(), "SELLABLE_COUNT", "DESC", paraRaceName, $("#gs_type").html());
            });
            me.initData($("#gs_game").html(), $("#gs_area").html(), $("#gs_server").html(), "UNIT_PRICE", "ASC", paraRaceName, $("#gs_type").html());
        },
        initData: function (gameName, regionName, serverName, filedName, sort, paraRaceName, goodsTypeName) {
            if (regionName == "游戏区" || regionName == "全部区") {
                regionName = "";
            }
            if (serverName == "游戏服" || serverName == "全部服") {
                serverName = "";
            }
            var request = {};
            request.gameName = gameName;
            request.regionName = regionName;
            request.serverName = serverName;
            request.filedName = filedName;
            request.raceName = paraRaceName;
            request.goodsTypeName = goodsTypeName;
            request.sort = sort;
            request.startIndex = 0;
            request.pageCount = 20;
            $.ajax({
                type: "GET",
                url: baseServiceUrl + "services/repository/countSellerGoodslist?t=" + new Date().getTime(),
                data: request,
                contentType: "application/json; charset=UTF-8",
                dataType: "jsonp",
                jsonp: "callback",
                success: function (resp) {
                    var responseStatus = resp.responseStatus;
                    var code = responseStatus.code;
                    if (code == "00") {
                        me.totalCount = resp.totalCount;
                        me.appendData(gameName, regionName, serverName, filedName, sort, request.pageCount, paraRaceName, goodsTypeName);
                    }
                }
            });
        },
        appendData: function (gameName, regionName, serverName, filedName, sort, pageCount, paraRaceName, goodsTypeName) {
            $("#Pagination").pagination(me.totalCount, {
                num_edge_entries: 2, //边缘页数
                num_display_entries: 3, //主体页数
                callback: pageselectCallback,
                items_per_page: 1, //每页显示条数
                prev_text: "上一页",
                next_text: "下一页"
            });

            function pageselectCallback(page_index, jq) {
                showLoading();
                var htmlStr = "<div class='tj'></div>";
                $(".pdlistbox").html("");

                var request = {};
                request.gameName = gameName;
                request.regionName = regionName;
                request.serverName = serverName;
                request.filedName = filedName;
                request.raceName = paraRaceName;
                request.sort = sort;
                request.startIndex = page_index;
                request.pageCount = pageCount;
                request.goodsTypeName = goodsTypeName;
                $.ajax({
                    type: "GET",
                    url: baseServiceUrl + "services/repository/querySellerGoodslist?t=" + new Date().getTime(),
                    data: request,
                    contentType: "application/json; charset=UTF-8",
                    dataType: "jsonp",
                    jsonp: "callback",
                    success: function (resp) {
                        var responseStatus = resp.responseStatus;
                        var code = responseStatus.code;
                        if (code == "00") {
                            me.goodsList = resp.sellerRepositoryList;
                            var hotCount = 0;
                            if (me.goodsList != null && me.goodsList.length > 0) {
                                for (var p in me.goodsList) {
                                    var json = me.goodsList[p];
                                    var shopName = json.shopName == null ? "" : json.shopName;
                                    var price = toDecimal2(1 / parseFloat(json.unitPrice));
                                    var smallestAmount = me.getSmallestAmount(json.gameName);
                                    var count = me.getDefaultGoldCount(json);
                                    var totalPrice = toDecimal2(parseFloat(json.unitPrice) * count);
                                    var totalPriceArray = totalPrice.toString().split(".");
                                    var sort = json.sort;
                                    var category = json.goodsTypeName;
                                    var race = json.gameRace == null ? "" : json.gameRace;
                                    //var gameIcon="";
                                    var isShieldedType = json.isShieldedType;
                                    var imgUrl = "db_icon.png";
                                    if (isShieldedType == true) {
                                        imgUrl = "js_icon.png";
                                    }

                                    if (sort != 0 && regionName != "" && serverName != "") {
                                        //累计置顶卖家数
                                        hotCount += 1;
                                    }
                                    htmlStr += "<div class=\"sin_pdlbox\">";
                                    //for(var t in gameIconData){
                                    //    if(gameIconData[t].code==json.gameName){
                                    //        gameIcon=gameIconData[t].value;
                                    //        break;
                                    //    }
                                    //}
                                    // console.log(json.goodsTypeName);
                                    //htmlStr+="<ul><li><img src=\"img/gameIcon/"+gameIcon+"\" /></li></ul>";
                                    htmlStr += "<ul class=\"pdlist_info\">";
                                    if (gameName == "魔兽世界(国服)") {
                                        htmlStr += "<li class=\"tt\"><img src=\"img/" + imgUrl + "\" />" + json.gameName + "/" + json.region + "/" + json.server + "/" + race + "/" + json.goodsTypeName + "【商城出品，质量保证】";
                                    } else {
                                        htmlStr += "<li class=\"tt\"><img src=\"img/" + imgUrl + "\" />" + json.gameName + "/" + json.region + "/" + json.server + "/" + json.goodsTypeName + "【商城出品，质量保证】";
                                    }
                                    htmlStr += "</li>";
                                    htmlStr += "<li><strong>商家</strong>：<span  class=\"shopName\">" + shopName + "</span></li>";
                                    htmlStr += "<li class=\"credit\"><strong>成功率</strong>：" + parseFloat(json.successPercent) * 100 + "%  <strong class='st'>商家信誉</strong>：<i class='ico_dm' style='width:" + (13 + (json.praiseCount - 1) * 15) + "px;'></i></li>";
                                    htmlStr += "<li><strong>月成交笔数</strong>：" + json.monthDealCount + "<strong class='st'>平均发货速度</strong>：" + json.deliverSpeed + "分钟</li>";
                                    htmlStr += "</ul>";
                                    htmlStr += "<ul class=\"pdlist_price\">";
                                    console.log(json.moneyName)
                                    if (json.moneyName == "" || json.moneyName == null || typeof (json.moneyName) == "undefined") {
                                        htmlStr += "<li class=\"pr\" id=\"price_" + json.id + "\" price=\"" + json.unitPrice + "\"><strong>1元=" + price + json.goodsTypeName + "</strong>";
                                    } else {
                                        htmlStr += "<li class=\"pr\" id=\"price_" + json.id + "\" price=\"" + json.unitPrice + "\"><strong>1元=" + price + json.moneyName + "</strong>";
                                    }
                                    if (json.gameName == "天涯明月刀" && json.seller == "fafafa000") {
                                        htmlStr += "<div class='sellAd'>在此店铺购买单笔超过500元的订单额外赠送100金。</div>";
                                    }
                                    htmlStr += "</li>";
                                    htmlStr += "</ul>";
                                    htmlStr += "<ul class=\"pdlist_num\">";
                                    htmlStr += "<li><div class=\"select_list_input3\">";
                                    if (json.moneyName == "" || json.moneyName == null || typeof (json.moneyName) == "undefined") {
                                        htmlStr += "<input id=\"seller_product_" + json.id + "\" name=\"goldCount\" value=\"" + count + "\" maxlength=\"8\" class=\"input_select_list_number\"><span class=\"select_list_unit2\">" + json.moneyName + json.goodsTypeName + "</span>";
                                    } else {
                                        htmlStr += "<input id=\"seller_product_" + json.id + "\" name=\"goldCount\" value=\"" + count + "\" maxlength=\"8\" class=\"input_select_list_number\"><span class=\"select_list_unit2\">" + json.moneyName + "</span>";
                                    }
                                    htmlStr += "</div></li>";
                                    htmlStr += "</ul>";
                                    htmlStr += "<ul class=\"pdlist_unitprice\">";
                                    htmlStr += "<li><span id=\"totalPrice_" + json.id + "\"><strong>" + totalPriceArray[0] + "</strong>." + totalPriceArray[1] + "</span>元</li>";
                                    htmlStr += "</ul>";
                                    htmlStr += "<ul class=\"pdlist_ensure\">";
                                    htmlStr += "<li>" + json.sellableCount + "</li>";
                                    htmlStr += "</ul>";
                                    htmlStr += "<ul class=\"pdlist_buy\">";
                                    htmlStr += "<li><a id=\"buy_" + json.id + "\" href=\"#\" class=\"buy_now\"><img src=\"img/bug.jpg\" /></a></li>";
                                    htmlStr += "</ul>";
                                    htmlStr += "</div>";
                                }
                                $("#Pagination").show();
                            }
                            else {
                                console.log($("#gs_type").html());
                                htmlStr += "<div id=\"divNoResult\" class=\"no_result\">";
                                htmlStr += "<div class=\"leftbox_bg\"></div>"
                                htmlStr += "<div class=\"rightbox\">";
                                htmlStr += "<h4><strong>很抱歉！</strong><span>在</span> 游戏：<span>" + $("#gs_game").html() + "</span>&gt;<span>" + $("#gs_area").html() + "</span>&gt;<span>" + $("#gs_server").html() + "</span>&gt;<span>" + $("#gs_type").html() + "</span>";
                                if (gameName == "魔兽世界(国服)") {
                                    htmlStr += "&gt;<span>" + paraRaceName + "</span>";
                                }
                                htmlStr += "</h4>";
                                htmlStr += "<p class=\"pb\">分类下没找到相关商品。</p>";
                                htmlStr += "</div></div>";
                                $("#Pagination").hide();
                            }

                            $(".pdlistbox").html(htmlStr);

                            //推荐列表的边框设置
                            var heightSet = 0;
                            var i = 0;
                            if (hotCount == 0) {
                                //当没有置顶的卖家时，隐藏边框
                                $(".tj").hide();
                            }
                            else {
                                //循环列表
                                $(".pdlistbox .sin_pdlbox").each(function () {
                                    i++;
                                    if (i <= hotCount) {
                                        heightSet += $(this).height() + 21;//动态计算每行置顶的列表的高度
                                    }
                                });
                                $(".tj").css("height", heightSet - (hotCount - 1) + "px");//设置计算完的边框高度
                                $(".tj").show();
                            }

                            me.bindEvents();

                            $("#divLoading").hide();
                            $("#loadingInfo").hide();
                        }
                    }
                });

                return false;
            }

        },
        bindEvents: function () {
            // 实时监控，input金币数的变化，以及时生成价格
            $('.input_select_list_number').bind('input propertychange', me.changeGoldCount);

            // 立即购买事件
            $(".buy_now").bind('click', me.openOrderUrl);
        },
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
                alert("购买数量请输入正确的数字！");
                return;
            }

            // 数量不超过99999999
            if (parseInt(goldCount) >= 99999999) {
                goldCount = 99999999;
                $(this).val(goldCount);
            }

            var id = $(this).attr("id");
            var goodsId = id.split("_")[2];
            var unitPrice = $("#price_" + goodsId).attr("price");
            var totalPrice = toDecimal2(parseFloat(unitPrice) * parseInt(goldCount));
            var totalPriceArray = totalPrice.toString().split(".");
            $("#totalPrice_" + goodsId).html("<strong>" + totalPriceArray[0] + "</strong>." + totalPriceArray[1]);
        },
        openOrderUrl: function () {
            var id = $(this).attr("id");
            var goodsId = id.substring(id.indexOf("_") + 1);
            var goods = me.getGoodsInfo(goodsId);
            var goodsTypeName = goods.goodsTypeName;
            if (goods != null) {
                var goldCount = $("#seller_product_" + goodsId).val();
                if (isNull(goldCount) || goldCount == 0) {
                    alert("请输入需要购买的" + goodsTypeName + "数量！");
                    return;
                }
                if (!isNumber(goldCount)) {
                    alert("请输入正确的" + goodsTypeName + "购买数量！");
                    return;
                }

                // 获取最小商品购买数量
                var smallestGoldCount = me.getDefaultGoldCount(goods);
                if (goldCount < smallestGoldCount) {
                    alert(goods.gameName + "最小" + goodsTypeName + "购买金额不能少于" + smallestGoldCount + goods.moneyName);
                    return;
                }
                if (goldCount > goods.sellableCount) {
                    alert("卖家库存不足，当前价格最大购买数量：" + goods.sellableCount + "(" + goods.moneyName + ")，请重新填写数量或者返回选择其它商品。");
                    return;
                }

                var game = escape(goods.gameName);
                var gameRegion = escape(goods.region);
                var gameServer = escape(goods.server);
                var gameRace = isNull(goods.gameRace) ? "" : escape(goods.gameRace);
                var goodsTypeName = isNull(goods.goodsTypeName) ? "" : escape(goods.goodsTypeName);
                var gameId = isNull(goods.gameId) ? "" : goods.gameId;
                var regionId = isNull(goods.regionId) ? "" : goods.regionId;
                var serverId = isNull(goods.serverId) ? "" : goods.serverId;
                var goodsId = isNull(goodsId) ? "" : goodsId;
                var raceId = isNull(goods.raceId) ? "" : goods.raceId;
                var seller = escape(goods.seller);

                window.open(baseHtmlUrl + "createorder.html?gameId=" + gameId + "&regionId=" + regionId + "&serverId=" + serverId + "&raceId=" + raceId + "&gameName=" + game + "&gameRegion=" + gameRegion + "&gameServer=" + gameServer + "&gameRace=" + gameRace + "&goodsTypeName=" + goodsTypeName + "&goodsId=" + goodsId + "&goodsCat=3&goldCount=" + goldCount + "&seller=" + seller + "&t=" + (new Date()).getTime(), "_blank");
            }
        },
        /**
         * 获取商品信息
         */
        getGoodsInfo: function (goodsId) {
            for (var i = 0, j = me.goodsList.length; i < j; i++) {
                if (me.goodsList[i].id == parseInt(goodsId)) {
                    goods = me.goodsList[i];
                    return goods;
                }
            }
            return null;
        },
        /**
         * 获取最小商品购买数量
         * @param goods
         * @returns {number}
         */
        getDefaultGoldCount: function (goods) {
            // 最小购买金额
            var smallestAmount = minBuyAmounts[goods.goodsTypeName].minBuyAmount;
            // var smallestAmount = jsons.minBuyAmount;
            // console.log(smallestAmount + "----------");
            // 计算最少购买商品数量
            var price = 1 / parseFloat(goods.unitPrice); // 1元对应多少金
            var count = price * smallestAmount;
            count = Math.ceil(count);
            return count;
        },
        getSmallestAmount: function (gameName, goodsTypeName) {
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
            if (goodsTypeName == "挑战书") {
                smallestAmount = 50; //挑战书最小购买50元
            }
            if (goodsTypeName == "元宝票") {
                smallestAmount = 50; //元宝票最小购买50元
            }
            return smallestAmount;
        }
    }
})(jQuery);

var gameData = [
    { code: '44343b06076d4a7a95a0ef22aac481ae', value: '地下城与勇士' },
    { code: '1a0d128d66b24896bf7dcf7430083cf0', value: '剑灵' },
    { code: '880', value: '魔兽世界(国服)' },
    { code: 'da451dc0df8d40e9a7aa54842687a127', value: 'QQ三国' },
    { code: 'a36ead01453c40b584f8e1e687723f2d', value: '剑侠情缘Ⅲ' },
    { code: '0ba72c47be2a46eeac63bf45d336b0ba', value: '疾风之刃' },
    { code: '5865420422194d68947c3d4b79a83204', value: '龙之谷' },
    { code: '2fdfb7d3fcd84b97b1e702d02c9ee7a7', value: '斗战神' },
    { code: '3cb8fe8afd2743e08ab577cbb525650f', value: '天涯明月刀' },
    { code: 'ddd44a893e504e10818418a955fe08b6', value: '神魔大陆' },
    { code: '3971da6971b3483987caa24bdfb30425', value: '新寻仙' },
    { code: '56b957c99db144ddb042541daa4df8fd', value: '上古世纪' },
    { code: 'c96417a69a86411d95be5d5e5c0c12fa', value: '怪物猎人OL' },
    { code: '19217d865b294d88b775744afb7bdfa5', value: '冒险岛2' },
    { code: '20c8bbc1b9794fc98bd96859624d4769', value: '新天龙八部' }
];


var gameIconData = [
    { code: '地下城与勇士', value: 'dxc.jpg' },
    { code: '剑灵', value: 'jl.jpg' },
    { code: '魔兽世界(国服)', value: 'mssj.jpg' },
    { code: 'QQ三国', value: 'qqsg.jpg' },
    { code: '剑侠情缘Ⅲ', value: 'jxqy.jpg' },
    { code: '疾风之刃', value: 'jfzr.jpg' },
    { code: '龙之谷', value: 'lzg.jpg' },
    { code: '斗战神', value: 'dzs.jpg' },
    { code: '天涯明月刀', value: 'tymyd.jpg' },
    { code: '神魔大陆', value: 'smdl.jpg' },
    { code: '新寻仙', value: 'xxx.jpg' }
];
