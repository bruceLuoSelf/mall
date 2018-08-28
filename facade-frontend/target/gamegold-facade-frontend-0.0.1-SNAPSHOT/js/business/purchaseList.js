var gameId = "";
var gameName = "";
var zoneId = "";
var zoneName = "";
var serverName="";
var raceName="";
var serverId="";
var initFlag=true;
var flagName="";
var flag = "";
$(document).ready(function () {
    gameName=$.trim(getUrlParam("gameName"));
    zoneName=$.trim(getUrlParam("zoneName"));
    serverName=$.trim(getUrlParam("serverName"));
    zoneId=$.trim(getUrlParam("zoneId"));
    serverId=$.trim(getUrlParam("serverId"));
    flag =$.trim(getUrlParam("flag"));
    if(gameName != ""){
        $("#gs_game").html(gameName);
    }
    if(zoneName != ""){
        $("#gs_area").html(zoneName);
    }
    if(serverName != ""){
        $("#gs_server").html(serverName);
    }
    if(flag != ""){
        initFlag = false;
    }
    //加载全部游戏
    getGameData();

    //初始化区服快速查询功能
    $('#searchSerQuick').bind('input propertychange', function(){
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
        $("#searchbar_arrow").css("left", "215px");
        GetGame();
    });

    //点击大区的时候，加载对应数据
    $("#gs_area").click(function () {
        $('#gsBox').show();
        $('#searchbar_arrow').show();
        $("#searchbar_arrow").css("left", "305px");

        if (gameId != null && gameId!="") {
            GetGameZone(gameId, gameName);
        }
    });

    //点击服务器的时候，加载对应数据
    $("#gs_server").click(function () {
        $('#gsBox').show();
        $('#searchbar_arrow').show();
        $("#searchbar_arrow").css("left", "395px");

        if($("#gs_area").text()=="全部区"){
            $('#gsBox').hide();
            $('#searchbar_arrow').hide();
            return;
        }
        if(zoneId!=null&&zoneId!="") {
            GetGameServer(zoneId, zoneName);
        }
    });

    //点击搜索功能，加载数据和对应效果
    $("#gsSearchBtn").click(function () {
        $("#gsBox").hide();
        $('#searchbar_arrow').hide();
        raceName="";
        search();
    });

    //选择区服时选择全部时的效果
    $("#all").click(function(){
        if($("#all").attr("flag")=="game"){
            $("#gs_game").html("全部游戏");
            $("#gs_area").html("全部区");
            $("#gs_server").html("全部服");
            gameName = "全部游戏"
            zoneName = "全部区";
            serverName = "全部服";
            $("#CategorySelector1_divGameNavigationBar").hide();
        }else if($("#all").attr("flag") == "region"){
            $("#gs_area").html("全部区");
            $("#gs_server").html("全部服");
            zoneName = "全部区";
            serverName = "全部服";
        }else{
            $("#gs_server").html("全部服");
            serverName="全部服";
        }
        $("#gsBox").hide();
        $('#searchbar_arrow').hide();
        $("#navGame").html($("#gs_game").text());
    });

    //点击搜索功能，加载数据和对应效果
    $("#gsBox").hide();
    $('#searchbar_arrow').hide();

});

function initPageData(){
    $('#gsBox').hide();
    $('#searchbar_arrow').hide();
    $("#SiteMap1_divSelectedConditions").hide();
    $("#gs_game").html(isNull(gameName)?"游戏名称":gameName);

    $('#gsBox').hide();
    $('#searchbar_arrow').hide();

    //加载游戏区服
    for(var p in gameData){
        if(gameData[p].value==gameName){
            GetGameZone(gameData[p].code, gameName);
            break;
        }
    }
}

//搜索功能
function search(){
    var gameTemp=$("#gs_game").html();//游戏名
    var regionTemp=$("#gs_area").html();//大区
    var serverNameTemp=$("#gs_server").html();//服务器

    $(".push_up").html("显示全部");
    $(".push_up").attr("class","push");
    $(".filterbox .sing_filbox dl").css("height","48px");
    if(gameTemp=="魔兽世界(国服)"){
        $("#gameRace").show();
    }else{
        $("#CategorySelector1_divGameNavigationBar").hide();
        $("#gameRace").hide();
    }

    //选中游戏
    var raceTemp=$("#dlraceid a.selected").html();//阵营
    if (gameTemp != "全部游戏") {
        $("#SiteMap1_divSelectedConditions").hide();
        $("#cancle_game").attr("tid",gameId);
        $("#cancle_game").attr("title","点击取消 游戏：" + gameTemp);
        $("#cancle_game a span").html(gameTemp);
        $("#navGame").html(gameTemp);
    }
    //选中游戏区
    if (regionTemp != "游戏区"&&regionTemp != "全部区") {
        $("#cancle_area").attr("tid",zoneId);
        $("#cancle_area").attr("title","点击取消 游戏区：" + regionTemp);
        $("#cancle_area a span").html(regionTemp + "<em></em>");
        $("#cancle_area").show();
    }else{
        $("#cancle_area").hide();
        $("#CategorySelector1_divGameNavigationBar").show();
    }

    //选中游戏服
    if (serverNameTemp != "游戏服"&&serverNameTemp != "全部服") {
        $("#cancle_server").attr("tid",serverId);
        $("#cancle_server").attr("title","点击取消 游戏服：" + serverNameTemp);
        $("#cancle_server a span").html(serverNameTemp + "<em></em>");
        $("#cancle_server").show();
    }else{
        $("#cancle_server").hide();
        $("#CategorySelector1_divGameNavigationBar").show();
    }

    //选中阵营
    if(!isNull(raceTemp)&&gameName=="魔兽世界(国服)"){
        $("#cancle_race").attr("title","点击取消 阵营：" + raceTemp);
        $("#cancle_race a span").html(raceTemp + "<em></em>");
    }
    else{
        $("#cancle_race").hide();
    }

    //选择区服对应显示数据
    if(regionTemp=="游戏区"||regionTemp=="全部区"){
        showQf("area");
    }else if(serverNameTemp == "游戏服"||serverNameTemp == "全部服"){
        showQf("server");
    }else{
        $("#gameRegion").hide();
    }

    //选择阵营触发事件，重新加载数据
    if(gameTemp=="魔兽世界(国服)"){
        $("#dlraceid a").unbind("click");
        $("#dlraceid a").click(function(){
            raceName=$(this).html();
            $("#sifting_box dd:eq(3)").remove();
            $("#sifting_box dd:eq(2)").after("<dd id='cancle_race' title=\"阵营：" + raceName + "\" onclick=\"cancleSelect($(this))\"><a onclick=\"#\"><span>" + raceName + "<em></em></span></a></dd>");
            $("#gameRace").hide();
            $("#dlraceid a").removeClass("selected");
            $(this).addClass("selected");

            GameGoldMall.HotRecommend.load();
        });
    }

    GameGoldMall.HotRecommend.load();

    //设置选择区服显示隐藏
    if( $("#gs_area").html()=="全部区"||$("#gs_area").html()=="游戏区"||$("#gs_server").html()=="全部服"||$("#gs_server").html()=="游戏服"){
        $("#CategorySelector1_divGameNavigationBar").show();
        $("#gameRegion").show();
        if(gameName=="魔兽世界(国服)"){
            if($("#cancle_server").css("display")=="none"&&$("#cancle_race").css("display")=="none"){
                $("#gameRegion").show();
                $("#gameRace").show();
            }
            else if($("#cancle_server").css("display")=="none"&&$("#cancle_race").css("display")!="none"){
                $("#gameRegion").show();
                $("#gameRace").hide();
            }
            else if($("#cancle_server").css("display")!="none"&&$("#cancle_race").css("display")=="none"){
                $("#gameRegion").hide();
                $("#gameRace").show();
            }
            else{
                $("#gameRegion").hide();
                $("#gameRace").hide();
                $("#CategorySelector1_divGameNavigationBar").hide();
            }
        }
        else{
            $("#gameRace").hide();
        }
    }else{
        if(gameName=="魔兽世界(国服)") {
            $("#gameRegion").hide();
            $("#gameRace").show();
            $("#CategorySelector1_divGameNavigationBar").show();
            if($("#cancle_server").css("display")=="none"&&$("#cancle_race").css("display")=="none"){
                $("#gameRegion").show();
                $("#gameRace").show();
            }else if($("#cancle_server").css("display")=="none"&&$("#cancle_race").css("display")!="none"){
                $("#gameRegion").show();
                $("#gameRace").hide();
            }else if($("#cancle_server").css("display")!="none"&&$("#cancle_race").css("display")=="none"){
                $("#gameRegion").hide();
                $("#gameRace").show();
            }else{
                $("#gameRegion").hide();
                $("#gameRace").hide();
                $("#CategorySelector1_divGameNavigationBar").hide();
            }

        }else{
            $("#CategorySelector1_divGameNavigationBar").hide();
        }
    }
    if(gameTemp == "全部游戏"){
        $("#CategorySelector1_divGameNavigationBar").hide();
    }
}

//取消选择
function cancleSelect(obj){
    $('#searchSerQuick').val("");
    $("#CategorySelector1_divGameNavigationBar").show();
    if(obj.attr("id")=="cancle_game"){
        $("#CategorySelector1_divGameNavigationBar").hide();
        $("#SiteMap1_divSelectedConditions").hide();
    }
    else if(obj.attr("id")=="cancle_area"){
        $("#cancle_server").hide();
        flagName="area";
        //showQf("area");
        $("#gs_area").html("游戏区");
        $("#gs_server").html("游戏服");
    }
    else if(obj.attr("id")=="cancle_server"){
        flagName="server";
        showQf("server");
        $("#gs_server").html("游戏服");
    }
    else if(obj.attr("id")=="cancle_race"){
        $("#gameRace").show();
    }
    obj.hide();

    search();
}

//加载区服
function showQf(flag){
    $("#gameRegion").show();
    $("#dlgs").html("");
    if(flag=="area"){
        $("#gameRegion .choose").html("游戏区选择：");
        $.ajax({
            type: "GET",
            url: "http://fcd.5173.com/ajax.axd?methodName=gamesV31&cache=600&gameType=GameAreas&tradingType=other&id=" + gameId + "&jsoncallback=callarea&t="+new Date().getTime(),
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
                        htmlstr += "<dt><a href=\"#\" title=\""+json.name+"\" onclick=\"selectMenu('" + json.id + "','" + json.name + "','" + flag + "')\">"+json.name+"</a></dt>";
                    }
                }
                $("#dlgs").html(htmlstr);
                filteroption("dlgs");
            }
        });
    }else if(flag=="server"){
        selectServerMenu(flag);
    }
}

//选择服务菜单
function selectServerMenu(flag){
    $("#gameRegion .choose").html("按服务器选择：");
    $.ajax({
        type: "GET",
        url: "http://fcd.5173.com/ajax.axd?methodName=gamesV31&cache=600&gameType=GameServers&tradingType=other&id=" + zoneId + "&jsoncallback=callserver&t="+new Date().getTime(),
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
                    htmlstr += "<dt><a href=\"#\" title=\""+json.name+"\" onclick=\"selectMenu('" + json.id + "','" + json.name + "','" + flag + "')\">"+json.name+"</a></dt>";
                }
            }
            $("#dlgs").html(htmlstr);
            filteroption("dlgs");
        }
    });
}

function selectMenu(id,name,flag){
    $('#searchSerQuick').val("");
    if(flag=="area") {
        $("#cancle_area").attr("tid", id);
        $("#cancle_area").attr("title", "点击取消 游戏区：" + name);
        $("#cancle_area a span").html(name + "<em></em>");
        $("#gs_area").html(name);
        $("#cancle_area").show();
        zoneId=id;
        // selectServerMenu("server");
    }
    else if(flag=="server"){
        $("#cancle_server").attr("tid", id);
        $("#cancle_server").attr("title", "点击取消 游戏服：" + name);
        $("#cancle_server a span").html(name + "<em></em>");
        $("#gs_server").html(name);
        $("#cancle_server").show();
        $("#gameRegion").hide();
    }
    search();
}

function showQfDiv(obj){
    if(obj.attr("class")=="push"){
        obj.html("精简显示");
        obj.attr("class","push_up");
        $(".filterbox .sing_filbox dl").css("height","auto");
    }
    else{
        obj.html("显示全部");
        obj.attr("class","push");
        $(".filterbox .sing_filbox dl").css("height","48px");
    }
}
var gameData = new Array();
//加载游戏
function GetGame() {
    var html="";
    for(var p in gameData){
        var pCode=gameData[p].code;
        var pValue=gameData[p].value;
        html+="<li id=\""+pCode+"\" lang=\"netgame\"><a class=\"hot\" title=\""+pValue+"\" onclick=\"GetGameZone('"+pCode+"','"+pValue+"')\">"+pValue+"</a></li>"
    }
    $("#gsList").html(html);
    $(".gs_name dt h1").html("请选择游戏：");
    $("#all").show();
    $("#all").attr("flag","game");
    $("#all").text("全部游戏");
}

//根据游戏的id获取大区
function GetGameZone(gameIdTemp, gameNameTemp) {
    gameId = gameIdTemp;
    gameName = gameNameTemp;
    $("#searchbar_arrow").css("left", "305px");
    $("#gs_game").html(gameName);
    $(".gs_name dt h1").html("请选择游戏区：");
    $("#all").show();
    $("#all").attr("flag","region");
    $("#all").html("全部区");
    $.ajax({
        type: "GET",
        url: "http://fcd.5173.com/ajax.axd?methodName=gamesV31&cache=600&gameType=GameAreas&tradingType=other&id=" + gameId + "&jsoncallback=callarea&t="+new Date().getTime(),
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
                    if(initFlag){
                        if($.trim(json.name)== zoneName){
                            GetGameServer(json.id,zoneName);
                            break;
                        }
                    }
                }
            }
            if(zoneName==""&&initFlag){
                search();
                initFlag=false;
            }
            $("#gsList").html(htmlstr);
        }
    });
};

//根据选择的大区ID获取服务器
function GetGameServer(zoneIdTemp, zoneNameTemp) {
    zoneId = zoneIdTemp;
    zoneName = zoneNameTemp;
    $("#searchbar_arrow").css("left", "395px");
    $("#gs_area").html(zoneName);
    $(".gs_name dt h1").html("请选择游戏服：");
    $("#all").show();
    $("#all").attr("flag","server");
    $("#all").html("全部服");
    $.ajax({
        type: "GET",
        url: "http://fcd.5173.com/ajax.axd?methodName=gamesV31&cache=600&gameType=GameServers&tradingType=other&id=" + zoneId + "&jsoncallback=callserver&t="+new Date().getTime(),
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
                    htmlstr += "<li id=\"" +$.trim( json.id) + "\" lang=\"undefined\"><a onclick=\"selectServer($(this))\">" + $.trim(json.name) + "</a></li>";

                    //第一次加载
                    if(initFlag){
                        if($.trim(json.name)== serverName){
                            $("#gs_server").html(serverName);
                            serverId=json.id;
                            search();
                            initFlag=false;
                            break;
                        }
                    }
                }
            }
            if(initFlag){
                search();
                initFlag=false;
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

function showLoading()
{
    var screenWidth = $(window).width();//当前窗口宽度
    var screenHeight = $(window).height();//当前窗口高度

    $("#divLoading").css({"display":"","position": "fixed","background": "#000","z-index": "10000","-moz-opacity": "0.5","opacity":".50","filter": "alpha(opacity=50)","width":screenWidth,"height":screenHeight});
    $("#loadingInfo").show();
    $("#divLoading").show();
}

(function ($) {
    if (typeof GameGoldMall === 'undefined') {
        GameGoldMall = {};
    }
    var me = GameGoldMall.HotRecommend = {
        totalCount:null,
        goodsList: null,
        pageSize:25,
        load: function () {
            var paraRaceName="";
            if($("#cancle_race").css("display")!="none"){
                paraRaceName=$("#cancle_race").text();
            }
            me.initData("price","desc");
            me.bindEvents();
        },
        initData:function(field,sort){
            $.jqPaginator('#pagination1', {
                totalPages: 10,
                visiblePages: me.pageCount,
                currentPage: 1,
                onPageChange: function (num, type) {
                    me.pageselectCallback(num,field,sort)
                }
            });
        },
        //分页数据
        pageselectCallback :function(page_index,field,sort) {
            showLoading();
            var game= $.trim($("#gs_game").html());
            var region=$.trim($("#gs_area").html());
            var server=$.trim($("#gs_server").html());
            if(game=="请选择游戏" || game=="全部游戏" || game=="游戏名称"){
                game="";
            }
            if(region=="请选择区" || region=="全部区" || region=="游戏区"){
                region="";
            }
            if(server=="请选择服" || server=="全部服" || server=="游戏服"){
                server="";
            }
            $(".pdlistbox").html("");
            var request = {};
            request.gameName = game;
            request.region = region;
            request.server = server;
            request.page=page_index;
            request.pageSize=me.pageSize;
            request.fieldName=field;
            request.sort=sort;
            $.ajax({
                type: "GET",
                url: baseServiceUrl + "services/purchaseOrder/page?t="+new Date().getTime(),
                data: request,
                contentType: "application/json; charset=UTF-8",
                dataType: "jsonp",
                jsonp: "callback",
                success: function (resp) {
                    var responseStatus = resp.responseStatus;
                    var code = responseStatus.code;
                    if (code == "00") {
                        me.goodsList=resp.purchaseOrderList;
                        me.appendData();
                        if(me.goodsList!=null&&me.goodsList.length>0) {
                            $(".totalPage").html("共" + resp.totalCount + "笔，共" + resp.totalPage + "页");
                        }
                        else{
                            $(".totalPage").html("共0笔，共1页");
                        }
                    }
                    else{
                        $(".totalPage").html("共0笔，共1页");
                    }

                    //数据为0时，默认设置插件页码为1
                    var count=1;
                    if(resp.totalPage!=0){
                        count=resp.totalPage;
                    }
                    $('#pagination1').jqPaginator('option', {
                        totalPages: count
                    });
                }
            });
            return false;
        },
        appendData:function(gameName,regionName,serverName,filedName,sort,pageCount,paraRaceName){
            var htmlStr="";
            if(me.goodsList!=null&&me.goodsList.length>0) {
                for(var p in me.goodsList) {
                    var json = me.goodsList[p];
                    var priceCount = toDecimal2(1 / parseFloat(json.price));
                    var shopName=json.shopName==null?"":json.shopName;
                    var img = "";
                    var tradeType="";
                    var jqTradeType="";
                    var sellerType=0;
                    if(json.deliveryType==1) {
                        if(json.tradeLogo.indexOf('3')>-1){
                            img += "<a class=\"ico_youji\" style=\"padding-right: 0px;\" title=\"邮寄交易方式\"></a>";
                            jqTradeType+="邮寄交易 ";
                        }
                        if(json.tradeLogo.indexOf('5')>-1){
                            img += "<a class=\"ico_paimai\" style=\"padding-right: 0px;\" title=\"拍卖行交易，出货商承担手续费为"+(me.poundage*100)+"%\"></a>"
                            jqTradeType+="拍卖交易 ";
                        }
                        img += "<a class=\"ico_ji\" style=\"padding-right: 0px;\" title=\"机器收货模式\"></a>"
                        // img += "<a class=\"ico_youji\" style=\"padding-right: 0px;\" title=\"邮寄交易方式\"></a>"

                        sellerType=1;
                        //jqTradeType="邮寄";
                    }
                    if(json.deliveryType==2){
                        tradeType = json.tradeTypeName
                    }
                    htmlStr+="<div class=\"sin_pdlbox\">";
                    htmlStr+="<ul class=\"pdlist_info\">";
                    htmlStr+="<li class=\"tt\">"+img+json.gameName+"/"+json.region+"/"+json.server;
                    htmlStr+="</li>";
                    htmlStr+="<li><strong>商家</strong>：<span  class=\"shopName\">"+shopName+"</span></li>";
                    htmlStr+="<li><strong>商家信誉</strong>：<i class='ico_dm' style='width:"+(13+(json.credit-1)*15)+"px;'></i></li>";
                    htmlStr+="<li><strong>交易方式</strong>："+jqTradeType+tradeType+"</li>";

                    htmlStr+="</ul>";
                    htmlStr+="<ul class=\"pdlist_cjl\">";
                    htmlStr+="<li class=\"pr\"><strong>"+parseFloat(json.cjl)*100+"%</strong>";
                    htmlStr+="</li>";
                    htmlStr+="</ul>";
                    htmlStr+="<ul class=\"pdlist_pjys\">";
                    htmlStr+="<li class=\"pr\"><strong><span class='spanPjys'>"+json.pjys+"</span>分钟</strong>";
                    htmlStr+="</li>";
                    htmlStr+="</ul>";
                    htmlStr+="<ul class=\"pdlist_num\">";
                    htmlStr+="<li class=\"pr\"><strong><span class='sz'>"+json.count+"<span class='unitName'></span></span><br/>（>=<span class='sz'>"+json.minCount+"<span class='unitName'></span></span>"+"）</strong>";
                    htmlStr+="</li>";
                    htmlStr+="</ul>";
                    htmlStr+="<ul class=\"pdlist_priceSh\">";
                    htmlStr+="<li class=\"pr\"><strong><span class='sz'>"+json.price+"</span>元/"+"<span class='unitName'></span><br/>1元="+priceCount+"<span class='unitName'></span></strong>";
                    htmlStr+="</li>";
                    htmlStr+="</ul>";
                    htmlStr+="<ul class=\"pdlist_buy\">";
                    htmlStr+="<li><a id=\"buy_"+json.id+"\" href=\"#\" sellerType=\""+sellerType+"\" class=\"buy_now\"><img src=\"img/btnSeller.jpg\" /></a></li>";
                    htmlStr+="</ul>";
                    htmlStr+="</div>";
                }
                $(".pdlistbox").html(htmlStr);
                $.ajax({
                    type: "GET",
                    url: baseServiceUrl + "services/shGameConfig/getConfigByGameName",
                    data: json,
                    dataType:'json',
                    contentType: "application/json; charset=UTF-8",
                    success:function (resp) {
                        if(resp.shGameConfig !=null){
                            unitName=resp.shGameConfig.unitName;
                            me.poundage = resp.shGameConfig.poundage;
                            $(".unitName").html(unitName);
                            $(".ico_paimai").attr("title","拍卖行交易，出货商承担手续费为"+(me.poundage*100)+"%")
                        }
                    }
                });

            }
            $("#divLoading").hide();
            $("#loadingInfo").hide();

            // 立即出货事件
            $(".buy_now").bind('click', me.openOrderUrl);

        },
        bindEvents:function() {
            $('#btnJyl').unbind("click");
            $('#btnJyl').click(function(){
                me.initData("cjl","desc");
            })

            $('#btnPjys').unbind("click");
            $('#btnPjys').click(function(){
                me.initData("pjys","asc");
            })

            $('#btnCount').unbind("click");
            $('#btnCount').click(function(){
                me.initData("count","desc");
            })

            $('#btnPrice').unbind("click");
            $('#btnPrice').click(function(){
                me.initData("price","desc");
            })
        },
        openOrderUrl:function() {
            var t=$(this).attr("sellerType");
            var id = $(this).attr("id");
            var goodsId = id.substring(id.indexOf("_") + 1);
            if(t==1){
                window.open(baseHtmlUrl + "deliveryOrder.html?id=" + goodsId+"&t="+(new Date()).getTime(), "_blank");
            }else{
                window.open(baseHtmlUrl + "deliveryOrder1.html?id=" + goodsId+"&t="+(new Date()).getTime(), "_blank");
            }
        }
    }
})(jQuery);

//游戏集合
function getGameData() {
    $.ajax({
        type: "GET",
        url: baseServiceUrl + "services/maingameconfig/getgamelist",
        contentType: "application/json; charset=UTF-8",
        dataType: "jsonp",
        success: function (resq) {
            var code = resq.responseStatus.code;
            var mainGameConfigList = resq.mainGameConfigList;
            if (code = "00") {
                if(mainGameConfigList != null && mainGameConfigList.length >0){
                    var code,value;
                    //将第一个游戏名设置为默认值
                    if(gameName == null){
                        gameName = mainGameConfigList[0].gameName;
                    }
                    for(var i in mainGameConfigList){
                        code = mainGameConfigList[i].gameId;
                        value = mainGameConfigList[i].gameName;
                        gameData.push({"code":code, "value":value});
                    }
                    GetGame();

                    //初始化页面显示隐藏效果
                    if(!isNull(raceName)&&gameName=="魔兽世界(国服)"){
                        $("#cancle_race").attr("title","点击取消 阵营：" + raceName);
                        $("#cancle_race a span").html(raceName + "<em></em>");
                        $("#dlraceid a").each(function(){
                            if($(this).text()==raceName){
                                $(this).addClass("selected");
                            }
                        })
                        $("#cancle_race").show();
                        $("#gameRace").hide();
                    }
                    else{
                        $("#cancle_race").hide();
                        $("#gameRace").show();
                    }
                    initPageData();
                    search();
                }
            }
        }
    });
}
