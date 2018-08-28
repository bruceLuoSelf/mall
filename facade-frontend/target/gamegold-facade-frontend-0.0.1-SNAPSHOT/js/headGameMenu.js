
var gameData = new Array();
var gameId ="";
var gameName ="";
var zoneId = "";
var zoneName = "";
var serverName = "";
var raceName = "";
var serverId = "";
$(document).ready(function () {
    //加载所有游戏
    getGameData();

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
            GetGameZone(gameId, gameName,true);
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
        if($("#all").attr("flag") == "game"){
            $("#gs_game").html("全部游戏");
            $("#gs_area").html("全部区");
            $("#gs_server").html("全部服");
            gameName = "全部游戏";
            zoneName = "全部区";
            serverName = "全部服";
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
    });
});

function initPageData(){
    $('#gsBox').hide();
    $('#searchbar_arrow').hide();
    $("#SiteMap1_divSelectedConditions").hide();
    $("#gs_game").html("请选择游戏");
    $("#gs_area").html("请选择区");
    $("#gs_server").html("请选择服");

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
        $("#CategorySelector1_divGameNavigationBar").hide();
        $("#gameRace").show();
    }
    else{
        $("#CategorySelector1_divGameNavigationBar").hide();
        $("#gameRace").hide();
    }

    //选中游戏
    var raceTemp=$("#dlraceid a.selected").html();//阵营
    if (gameTemp != "游戏名称") {
        $("#SiteMap1_divSelectedConditions").show();
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
    }
    else{
        regionTemp="";
        $("#cancle_area").hide();
        $("#CategorySelector1_divGameNavigationBar").show();
    }

    //选中游戏服
    if (serverNameTemp != "游戏服" && serverNameTemp != "全部服") {
        $("#cancle_server").attr("tid",serverId);
        $("#cancle_server").attr("title","点击取消 游戏服：" + serverNameTemp);
        $("#cancle_server a span").html(serverNameTemp + "<em></em>");
        $("#cancle_server").show();
    }
    else{
        serverNameTemp="";
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
    }
    else if(serverNameTemp != "游戏服"||serverNameTemp != "全部服"){
        showQf("server");
    }
    else{
        $("#gameRegion").hide();
    }
    //跳转到采购单详情页
    window.open(baseHtmlUrl + "purchaseOrder.html?gameName="+escape(gameTemp)+"&zoneName="+escape(regionTemp)+"&serverName="+escape(serverNameTemp)+"&zoneId="+zoneId+"&serverId="+serverId+"&flag="+true+"&raceName=&t="+new Date().getTime())
}

//加载区服
function showQf(flag){
    $("#gameRegion").show();
    $("#dlgs").html("");
    if(flag=="area"){
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
                        htmlstr += "<dt><a href=\"#\" title=\""+json.name+"\" onclick=\"selectMenu('" + json.id + "','" + json.name + "','" + flag + "')\">"+json.name+"</a></dt>";
                    }
                }
                $("#dlgs").html(htmlstr);
            }
        });
    }
    else if(flag=="server"){
        selectServerMenu(flag);
    }
}

//选择服务菜单
function selectServerMenu(flag){
    $("#gameRegion .choose").html("按服务器选择：");
    $.ajax({
        type: "GET",
        url: "http://fcd.5173.com/ajax.axd?methodName=gamesV31&cache=600&gameType=GameServers&tradingType=other&id=" + zoneId + "&jsoncallback=callserver",
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
        }
    });
}

//加载游戏
function GetGame() {
    var html="";
    for(var p=0;p< gameData.length;p++){
        var pCode=gameData[p].code;
        var pValue=gameData[p].value;
        html+="<li id=\""+pCode+"\" lang=\"netgame\"><a class=\"hot\" title=\""+pValue+"\" onclick=\"GetGameZone('"+pCode+"','"+pValue+"')\">"+pValue+"</a></li>"
    }
    $("#gsList").html(html);
    $(".gs_name dt h1").html("请选择游戏：");
    $("#all").show();
    $("#all").attr("flag","game");
    $("#all").html("全部游戏");
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
                    htmlstr += "<li id=\"" + $.trim(json.id) + "\" lang=\"undefined\"><a onclick=\"GetGameServer('" + $.trim(json.id) + "','" + $.trim(json.name) + "')\">" + $.trim(json.name) + "</a></li>";
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
    $("#searchbar_arrow").css("left", "395px");
    $("#gs_area").html(zoneName);
    $(".gs_name dt h1").html("请选择游戏服：");
    $("#all").show();
    $("#all").attr("flag","server");
    $("#all").html("全部服");
    $.ajax({
        type: "GET",
        url: "http://fcd.5173.com/ajax.axd?methodName=gamesV31&cache=600&gameType=GameServers&tradingType=other&id=" + zoneId + "&jsoncallback=callserver",
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
                    for(var i=0;i<mainGameConfigList.length;i++){
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
                }
            }
        }
    });
}