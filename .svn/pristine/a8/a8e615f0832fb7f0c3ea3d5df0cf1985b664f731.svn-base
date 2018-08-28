var gameId="";
var gameName="";
var zoneId="";
var zoneName="";
var sellerInfo,isJishou;
var upFirst=true;//用于设定第一次上传，ie中每次导入错误模板会弹出增加一次错误信息

$(document).ready(function(){
    $('input[rel*=leanModal]').leanModal({closeButton: ".modal_close" });
    GameGoldMall.RepositoryManage.load();
});

(function ($) {
    if (typeof GameGoldMall === 'undefined') {
        GameGoldMall = {};
    }
    var me = GameGoldMall.RepositoryManage = {
        totalCount:null,
        goodsList: null,
        onlineState:-1,//当前上下线状态
        repositoryDialog: null,
        downloadModuleDialog:null,
        firstSearch:true,
        isOnline:null,
        pageCount:25,
        sort:"",
        isAsc:true,
        load:function(){
            getGame();
            me.initData();
            me.bindServiceInfo();
            me.bindEvent();
            me.uploaderFile();
            //me.uploadCurrencyFile();//ZW_C_JB_00008_20170513 ADD
        },
        //初始化数据
        initData:function(){
            $.jqPaginator('#pagination1', {
                totalPages: 10,
                visiblePages:7,
                currentPage: 1,
                onPageChange: function (num, type) {
                    me.pageselectCallback(num)
                }
            });
        },
        //分页数据
        pageselectCallback: function (page_index) {
            var gameName = $.trim($("#game").text());
            var region = $.trim($("#region").text());
            var server = $.trim($("#server").text());
            var race = $.trim($("#race").text());
            var gameLoginAccount = $("#gameLoginAccount").val();
            var gameRole = $("#gameRole").val();
            var goodsTypeName = $.trim($("#currency").text());
            /**********ZW_C_JB_00008_20170517 MODIFY START******************/
            if (gameName == "请选择" || gameName == "全部游戏") {
                gameName = "";
            }
            if (region == "请选择" || region == "全部区") {
                region = "";
            }
            if (server == "请选择" || server == "全部服") {
                server = "";
            }
            if (race == "请选择" || race == "全部阵营") {
                race = "";
            }
            /**********ZW_C_JB_00008_20170517 MODIFY END******************/
            /**********ZW_C_JB_00008_20170517 ADD START******************/
            if (goodsTypeName == "请选择") {
                //默认情况下首页查询条件为空或者为请选择,表示查询游戏币
                goodsTypeName = "全部";
            }
            /**********ZW_C_JB_00008_20170517 MODIFY END******************/
            $(".sin_pdlbox").html("");
            var request = {};
            request.gameName = gameName;
            request.gameRegion = region;
            request.gameServer = server;
            request.gameRace = race;
            request.gameAccount = gameLoginAccount;
            request.gameRole = gameRole;
            request.page = page_index;
            request.pageSize = me.pageCount;
            request.sortByField = me.sort;
            request.isAsc = me.isAsc;
            request.goodsTypeName = goodsTypeName;//ZW_C_JB_00008_20170514 ADD
            /**********ZW_C_JB_00008_20170517 MODIFY END******************/

            $.ajax({
                type: "GET",
                url: baseServiceUrl + "services/seller/repository/page",
                data: request,
                contentType: "application/json; charset=UTF-8",
                dataType: "jsonp",
                jsonp: "callback",
                success: function (resp) {
                    var responseStatus = resp.responseStatus;
                    var code = responseStatus.code;
                    if (code == "00") {
                        me.goodsList = resp.repositoryList;
                        isJishou=resp.isJiShou
                        if(!isJishou){
                            $('#fcSysInvent').hide();
                        }else{
                            $('#fcSysInvent').show(); 
                        }
                        me.appendData();
                        if (me.goodsList != null && me.goodsList.length > 0) {
                            $(".totalPage").html("共" + resp.totalCount + "笔，共" + resp.totalPage + "页");
                        }
                        else {
                            $(".totalPage").html("共0笔，共1页");
                        }
                    }
                    else {
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
        //拼接列表数据
        appendData: function (pageIndex) {
            $(".sin_pdlbox").html("");
            var htmlStr = "";
            if (me.goodsList != null && me.goodsList.length > 0) {
                for (var p in me.goodsList) {
                    var json = me.goodsList[p];
                    var gameRace = json.gameRace;
                    if (gameRace == null) {
                        gameRace = "";
                    }
                    if(Math.abs(json.stockCount-json.sellableGoldCount)>json.thresholdCount&&isJishou){
                        htmlStr += "<div class=\"active pdlistbox\" index=\"" + p + "\">";
                    }else{
                        htmlStr += "<div class=\"pdlistbox\" index=\"" + p + "\">";
                    }
                    if (p % 2 == 0) {
                        htmlStr += "<ul class=\"pdlist_info clearfix tr0\">";
                    }
                    else {
                        htmlStr += "<ul class=\"pdlist_info clearfix tr1\">";
                    }
                    /*************************ZW_C_JB_00008_20170514 MODIFY START***************************************/
                    if (gameRace == null || gameRace == undefined || gameRace == "") {
                        htmlStr += "<li class=\"gameName\">" + json.gameName + "/" + json.gameRegion + "/" + json.gameServer + "</li>";
                    } else {
                        htmlStr += "<li class=\"gameName\">" + json.gameName + "/" + json.gameRegion + "/" + json.gameServer + "/" + gameRace + "</li>";
                    }
                    if (json.goodsTypeName == null || json.goodsTypeName == undefined || json.goodsTypeName == "") {
                        htmlStr += "<li class=\"currency\">游戏币</li>";
                    } else {
                        htmlStr += "<li class=\"currency\">" + json.goodsTypeName + "</li>";
                    }
                    /*************************ZW_C_JB_00008_20170514 MODIFY END**************************************/
                    htmlStr += "<li class=\"gameLoginAccount\">" + json.gameAccount + "</li>";
                    htmlStr += "<li class=\"gamePwd\">******</li>";
                    htmlStr += "<li class=\"gameRole\">" + json.gameRole + "</li>";
                    htmlStr += "<li class=\"unit\">" + json.moneyName + "</li>";
                    htmlStr += "<li class=\"price\">" + json.unitPrice + "</li>";
                    // htmlStr += "<li class=\"goldCount\">" + json.goldCount + "</li>";
                    htmlStr += "<li class=\"sellableCount\">" + json.sellableGoldCount + "</li>";
                    if(isJishou){
                        htmlStr += "<li class=\"goldCount\">" + json.stockCount + "</li>";
                    }
                    htmlStr += "<li class=\"chk\"><input type=\"checkbox\" onclick=\"GameGoldMall.RepositoryManage.checkEdit($(this)," + p + ")\"></li>";
                    htmlStr += "</ul>";
                    htmlStr += "</div>";
                }
            }
            $(".sin_pdlbox").html(htmlStr);
            $(".sin_pdlbox").find("ul").click(function () {
                $(".sin_pdlbox").find("li").removeClass("selected");
                $(this).find("li").addClass("selected");
            });
        },
        //绑定事件
        bindEvent: function () {
            $(".close_btn").click(function () {
                $("#gsBox").hide();
                $("#searchbar_arrow").hide();
            });

            /**********ZW_C_JB_00008_20170513 MODIFY START************/
            //选择区服时选择全部时的效果
            $("#all").click(function () {
                if ($("#all").attr("flag") == "game") {
                    $("#game").html("全部游戏")
                    $("#region").html("全部区");
                    $("#server").html("全部服");
                    $("#race").html("全部阵营");
                    gameName = "全部游戏"
                    zoneName = "全部区";
                    serverName = "全部服";
                    raceName = "全部阵营";
                }
                else if ($("#all").attr("flag") == "region") {
                    $("#region").html("全部区");
                    $("#server").html("全部服");
                    $("#race").html("全部阵营");
                    zoneName = "全部区";
                    serverName = "全部服";
                    raceName = "全部阵营";
                } else if($("#all").attr("flag") == "server"){
                    $("#server").html("全部服");
                    $("#race").html("全部阵营");
                    serverName = "全部服";
                    raceName = "全部阵营";
                }else if($("#all").attr("flag") == "race"){
                    $("#race").html("全部阵营");
                    raceName = "全部阵营";
                }else if($("#all").attr("flag") == "currency"){
                    $("#currency").html("全部");
                    currencyName = "全部";
                }
                /**********ZW_C_JB_00008_20170513 MODIFY END************/
                $("#gsBox").hide();
                $('#searchbar_arrow').hide();
            });

            //查询
            $("#search").click(function () {
                me.sort = "";
                me.isAsc = true;
                me.initData();
            });

            //保存
            $("#btnSave").click(function () {
                me.saveData();
            });

            $("#btnSave").mouseover(function () {
                $("#divInfo").slideDown(300);
            })
            $("#btnSave").mouseout(function () {
                $("#divInfo").slideUp(300);
            })

            //设置用户的上线和离线
            $(".state input").click(function () {
                if ($(this).val() != me.isOnline) {
                    me.setOnlineOrOffline($(this).val(), $(this).attr("info"));
                }
            });

            //新增保存操作
            $("#btnAddSave").click(function () {
                var gameName = $.trim($("#selAddGame option:selected").text());
                var regionName = $.trim($("#selAddRegion option:selected").text());
                var serverName = $.trim($("#selAddServer option:selected").text());
                var raceName = "";
                if (gameName == "魔兽世界(国服)") {
                    raceName = $.trim($("#selAddRace option:selected").text());
                }
                var goldCount = $.trim($("#addGoldCount").val());
                var sellableGoldCount = $.trim($("#addSellableGoldCount").val());
                var unitPrice = $.trim($("#addUnitPrice").val());
                var moneyName = $.trim($("#addMoneyName").val());
                var gameAccount = $.trim($("#addGameAccount").val());
                var gamePwd = $.trim($("#addGamePwd").val());
                var gameRole = $.trim($("#addGameRole").val());
                var secondaryPwd = $.trim($("#addSecondaryPwd").val());
                var warehousePwd = $.trim($("#addWarehousePwd").val());
                var goodsTypeName = $.trim($("#currencyType option:selected").text());//ZW_C_JB_00008_20170516 ADD
                var goodsTypeId =  $.trim($("#currencyType option:selected").attr("id"));//ZW_C_JB_00008_20170516 ADD
                if (!me.validate(gameName, regionName, serverName, raceName, goldCount, sellableGoldCount, unitPrice, moneyName, gameAccount, gamePwd, gameRole, goodsTypeName)) {
                    return;
                }

                var request = {};
                request.gameName = gameName;
                request.gameRegion = regionName;
                request.gameServer = serverName;
                request.gameRace = raceName;
                request.goldCount = goldCount;
                request.sellableGoldCount = sellableGoldCount;
                request.unitPrice = unitPrice;
                request.moneyName = moneyName;
                request.gameAccount = gameAccount;
                request.gamePwd = gamePwd;
                request.gameRole = gameRole;
                request.secondaryPwd = secondaryPwd;
                request.warehousePwd = warehousePwd;
                request.goodsTypeName = goodsTypeName;//ZW_C_JB_00008_20170516 ADD
                request.goodsTypeId = goodsTypeId;//ZW_C_JB_00008_20170522 ADD
                $.ajax({
                    type: "POST",
                    url: baseServiceUrl + "services/seller/repository/add",
                    data: $.toJSON(request),
                    contentType: "application/json; charset=UTF-8",
                    dataType: "jsonp",
                    jsonp: "callback",
                    success: function (resp) {
                        var responseStatus = resp.responseStatus;
                        var code = responseStatus.code;
                        if (code == "00") {
                            alert("新增成功！");
                            me.sort = "";
                            me.isAsc = true;
                            me.initData();
                            $("#OpenWindow").hide();
                            $("#lean_overlay").hide();
                        }
                        else {
                            alert(responseStatus.message);
                        }
                    },
                    error: function (resp) {
                    }
                });
            });
            $('#addGoldCount').bind('input propertychange', me.changeGoldCountInAdd);
            $('#addSellableGoldCount').bind('input propertychange', me.changeSellableGoldCountInAdd);

            //下载模板
            $("#btnDownloadModule").click(function () {
                window.open(baseHtmlUrl + "download/repositoryTemplate/repositoryTemplate.zip");
            });

            /**********************ZW_C_JB_00008_20170517 ADD START****************************/
            // //下载通货模板需要生成xlsx文件增加'商品类目'
            $("#btnDownloadCurrencyModule").click(function () {
                window.open(baseHtmlUrl + "download/repositoryTemplate/repositoryTemplateCategory.zip");
            });
            /**********************ZW_C_JB_00008_20170517 ADD END****************************/

            $("#btnpricegrade").click(function () {
                me.sort = "1";
                me.isAsc = true;
                me.initData();
            });

            $("#btnNumSort").click(function () {
                me.sort = "2";
                me.isAsc = false;
                me.initData();
            });
        },
        //新增前的验证
        validate: function (gameName, regionName, serverName, raceName, goldCount, sellableGoldCount, unitPrice, moneyName, gameAccount, gamePwd, gameRole) {
            if (isNull(gameName) || gameName == "请选择") {
                alert("请选择游戏");
                return false;
            }
            if (isNull(regionName) || regionName == "请选择") {
                alert("请选择游戏区");
                return false;
            }
            if (isNull(serverName) || serverName == "请选择") {
                alert("请选择游戏服");
                return false;
            }
            if (gameName == "魔兽世界(国服)") {
                if (isNull(raceName) || raceName == "请选择") {
                    alert("请选择阵营");
                    return false;
                }
            }
            if (isNull(goldCount)) {
                alert("商品数目不能为空！");
                return false;
            }
            else {
                if (!isNumber(goldCount)) {
                    alert("商品数目请输入正确的整数！");
                    return false;
                }
                if (parseInt(goldCount) > 999999999) {
                    alert("商品数目不能大于999999999");
                    return false;
                }
            }
            if (isNull(sellableGoldCount)) {
                alert("可销售库存不能为空！");
                return false;
            }
            else {
                if (!isNumber(sellableGoldCount)) {
                    alert("可销售库存请输入正确的整数！");
                    return false;
                }
                if (parseInt(sellableGoldCount) > parseInt(goldCount)) {
                    alert("可销售库存不能大于商品数目");
                    return false;
                }
            }

            if (isNull(unitPrice)) {
                alert("单价不能为空！");
                return false;
            }
            else {
                if (isNaN(unitPrice)) {
                    alert("单价请输入正确的数字！");
                    return false;
                }
            }

            // if (isNull(moneyName)) {
            //     alert("商品单位不能为空");
            //     return false;
            // }
            if (isNull(gameAccount)) {
                alert("游戏账号不能为空");
                return false;
            }
            /*if(isNull(gamePwd)){
             alert("游戏密码不能为空");
             return false;
             }*/
            if (isNull(gameRole)) {
                alert("角色名不能为空");
                return false;
            }
            return true;
        },
        //保存修改的数据
        saveData: function () {
            //判断是否存在需要保存的勾选信息
            var flag = false;
            $(".pdlistbox").each(function () {
                $(this).find("li").each(function () {
                    if ($(this).find("input[type='checkbox']").is(':checked') == true) {
                        flag = true;
                    }
                });
            });
            if (!flag) {
                alert("没有任何可保存的数据");
                return;
            }
            //勾选的信息过滤出，进行暂存到json对象中
            var json = new Array();
            if (confirm("是否确定保存?")) {
                var checkFlag = true;
                $(".pdlistbox").each(function () {
                    var index = $(this).attr("index");
                    // var unit = $.trim($(this).find(".unit").find("input").val());
                    var gameLoginAccount = $.trim($(this).find(".gameLoginAccount").find("input").val());
                    var gamePwd = $.trim($(this).find(".gamePwd").find("input").val());
                    var gameRole = $.trim($(this).find(".gameRole").find("input").val());
                    var price = $.trim($(this).find(".price").find("input").val());
                    //var goldCount = $.trim($(this).find(".goldCount").find("input").val());
                    var sellableCount = $.trim($(this).find(".sellableCount").find("input").val());
                    var goodsTypeName = $.trim($(this).find(".currency").find("select option:selected").text());//ZW_C_JB_00008_20170515 ADD '增加通货'
                    if ($(this).find("li").find("input[type='checkbox']").is(':checked') == true) {
                        if (gameLoginAccount == "" || gameLoginAccount == undefined) {
                            alert("游戏账号不能为空");
                            checkFlag = false;
                            return false;
                        }
                        if (gameRole == "" || gameRole == undefined) {
                            alert("角色名不能为空");
                            checkFlag = false;
                            return false;
                        }
                        /*********ZW_C_JB_00008_20170516 ADD '增加通货'******/
                        // if (unit == "" || unit == undefined) {
                        //     var unit = me.goodsList[index].moneyName;
                        //     if(unit==""||unit==null||unit==undefined){
                        //         alert("商品单位不能为空");
                        //         checkFlag = false;
                        //         return false;
                        //     }
                        // }
                        /*********ZW_C_JB_00008_20170516 ADD '增加通货'******/
                        if (price == "" || price == undefined) {
                            alert("单价不能为空");
                            checkFlag = false;
                            return false;
                        }
                        // if (goldCount == "" || goldCount == undefined) {
                        //     alert("商品数目不能为空");
                        //     checkFlag = false;
                        //     return false;
                        // }
                        if (sellableCount == "" || sellableCount == undefined) {
                            alert("可销售库存不能为空");
                            checkFlag = false;
                            return false;
                        }
                        var entity = {};
                        entity.id = me.goodsList[index].id;
                        // entity.moneyName = unit;
                        entity.gameAccount = gameLoginAccount;
                        entity.gamePwd = gamePwd;
                        entity.gameRole = gameRole;
                        entity.unitPrice = price;
                        //entity.goldCount = goldCount;
                        entity.sellableGoldCount = sellableCount;
                        entity.goodsTypeName = goodsTypeName;//ZW_C_JB_00008_20170515 ADD '增加通货'
                        json.push(entity);
                    }

                });
                if (!checkFlag) {
                    return;
                }

                //批量保存勾选的数据
                $.ajax({
                    type: "POST",
                    url: baseServiceUrl + "services/seller/repository/batchUpdate",
                    data: $.toJSON(json),
                    contentType: "application/json; charset=UTF-8",
                    dataType: "jsonp",
                    jsonp: "callback",
                    success: function (resp) {
                        var responseStatus = resp.responseStatus;
                        var code = responseStatus.code;
                        if (code == "00") {
                            me.sort = "";
                            me.isAsc = true;
                        }
                        else {
                            alert(responseStatus.message);
                        }
                        me.initData();
                    }
                });
            }
        },
        //设置用户的上线和离线
        setOnlineOrOffline: function (state, info) {
            if (confirm("是否确定" + info + "?")) {
                var action = "";
                if (state == 1) {
                    //上线
                    action = "online";
                }
                else if (state == 0) {
                    //离线
                    action = "offline";
                }
                $.ajax({
                    type: "GET",
                    url: baseServiceUrl + "services/seller/repository/" + action,
                    contentType: "application/json; charset=UTF-8",
                    dataType: "jsonp",
                    jsonp: "callback",
                    success: function (resp) {
                        var responseStatus = resp.responseStatus;
                        var code = responseStatus.code;
                        if (code == "00") {
                            me.isOnline = state;
                            alert("设置成功！");
                        }
                        else {
                            if (me.isOnline == 1) {
                                $(".state input[value='1']").attr("checked", "checked");
                            }
                            else {
                                $(".state input[value='0']").attr("checked", "checked");
                            }
                            alert(responseStatus.message);
                        }
                    },
                    error: function (resp) {
                    }
                });
            }
            else {
                if (me.isOnline == 1) {
                    $(".state input[value='1']").attr("checked", "checked");
                }
                else {
                    $(".state input[value='0']").attr("checked", "checked");
                }
            }
        },
        //获取客服信息
        bindServiceInfo: function () {
            var queryRequest = {};
            $.ajax({
                type: "POST",
                url: baseServiceUrl + "services/seller/querysellerinfo?t=" + new Date().getTime(),
                data: $.toJSON(queryRequest),
                contentType: "application/json; charset=UTF-8",
                dataType: "json",
                beforeSend: function (request) {
                    request.setRequestHeader("5173_authkey", getAuthkey());
                },
                success: function (resp) {
                    var responseStatus = resp.responseStatus;
                    var code = responseStatus.code;
                    if (code == "00") {
                        // 卖家信息
                        sellerInfo = resp.sellerInfo;
                        if (!isNull(sellerInfo)) {
                            // 组装客服信息
                            if (sellerInfo.checkState == CheckState.PassAudited) {
                                $(".wrapper").hide();
                                $(".content").show();
                            }
                            else if (sellerInfo.checkState == CheckState.UnAudited) {
                                $(".wrapper").show();
                                $(".content").hide();
                            }
                            else if (sellerInfo.checkState == CheckState.UnPassAudited) {
                                // 审核未通过的，跳转到申请页面
                                window.location.href = baseHtmlUrl + "applyseller.html";
                                return;
                            }

                            me.isOnline = sellerInfo.isOnline;
                            me.buildServicerHtml();
                        } else {
                            // 跳转到申请卖家页面
                            window.location.href = baseHtmlUrl + "applyseller.html";
                            return;
                        }
                    }
                },
                error: function (resp) {
                }
            });
        },
        //渲染客服信息
        buildServicerHtml: function () {
            var request = {};
            request.servicerType = UserType.EnterService;
            request.size = 8;
            $.ajax({
                type: "POST",
                url: baseServiceUrl + "services/queryservicer/applyservice?t=" + new Date().getTime(),
                data: $.toJSON(request),
                contentType: "application/json; charset=UTF-8",
                dataType: "json",
                success: function (resp) {
                    var code = resp.responseStatus.code;
                    if (code == "00") {
                        if (resp.userInfoEOs.length > 0) {
                            var servicerInfo = resp.userInfoEOs[0];
                            var avatarUrl = isNull(servicerInfo.avatarUrl) ? "img/head.png" : buildImageUrl(servicerInfo.avatarUrl, "64x64");
                            $(".service_avatar").attr("src", avatarUrl);
                            $(".chose_service_qq").unbind("click");
                            $(".chose_service_qq").click(function () {
                                if (isNull(servicerInfo) || isNull(servicerInfo.qq)) {
                                    return;
                                }
                                window.open("http://wpa.qq.com/msgrd?v=3&uin=" + servicerInfo.qq + "&site=qq&menu=yes");
                            });
                            $(".p_border").html("尊敬的<strong>" + sellerInfo.name + "</strong>我将马上联系您并向您介绍大卖家入驻的细则，请您保持手机畅通。");
                            var nickName = isNull(servicerInfo.nickName) ? "公主" : servicerInfo.nickName;
                            $(".service_nick").html("客服：" + nickName);
                            $(".service_name").html(nickName);
                            var qq = isNull(servicerInfo.qq) ? "" : servicerInfo.qq;
                            $(".service_qq").html("Q Q：" + qq);
                            var phoneNumber = isNull(servicerInfo.phoneNumber) ? "" : servicerInfo.phoneNumber;
                            $(".chose_service_phone").html("手机：" + phoneNumber);
                            var weiXin = isNull(servicerInfo.weiXin) ? "" : servicerInfo.weiXin;
                            $(".chose_service_wechat").html("微信：" + weiXin);

                            if (me.isOnline) {
                                $(".state input[value='1']").attr("checked", "checked");
                            }
                            else {
                                $(".state input[value='0']").attr("checked", "checked");
                            }
                        }
                    }
                },
                error: function (resp) {
                }
            });

        },
        //点选勾选款，使当前行出现可修改的效果
        checkEdit: function (obj, index) {
            var json = me.goodsList[index];
            if (obj.is(':checked') == true) {
                // if(json.goodsTypeName=="游戏币"){
                //     obj.parent("li").parent("ul").find(".unit").html("<input type='text' value='" + json.moneyName + "'>");
                // }else{
                //     //obj.parent("li").parent("ul").find(".unit").html("<a type='text' value='" + json.moneyName + "'readonly='readonly'>")
                // }
                obj.parent("li").parent("ul").find(".gameLoginAccount").html("<input type='text' value='" + json.gameAccount + "'>");
                obj.parent("li").parent("ul").find(".gamePwd").html("<input type='text' value=''>");
                obj.parent("li").parent("ul").find(".gameRole").html("<input type='text' value='" + json.gameRole + "'>");
                obj.parent("li").parent("ul").find(".price").html("<input class='inputPrice' type='text' value='" + json.unitPrice + "'>");
                //obj.parent("li").parent("ul").find(".goldCount").html("<input class='inputGoldCount' type='text' value='" + json.goldCount + "'>");
                obj.parent("li").parent("ul").find(".sellableCount").html("<input class='inputSellableCount' type='text' value='" + json.sellableGoldCount + "'>");
                $('.inputPrice').unbind('input propertychange');
                $('.inputPrice').bind('input propertychange', function () {
                    me.changePriceInInput($(this));
                });
                $('.inputGoldCount').unbind('input propertychange');
                $('.inputGoldCount').bind('input propertychange', function () {
                    me.changeGoldCountInInput($(this));
                });
                $('.inputSellableCount').unbind('input propertychange');
                $('.inputSellableCount').bind('input propertychange', function () {
                    me.changeSellableCountInInput($(this));
                });
            }
            else {
                obj.parent("li").parent("ul").find(".unit").html(json.moneyName);
                obj.parent("li").parent("ul").find(".gameLoginAccount").html(json.gameAccount);
                obj.parent("li").parent("ul").find(".gamePwd").html("******");
                obj.parent("li").parent("ul").find(".gameRole").html(json.gameRole);
                obj.parent("li").parent("ul").find(".price").html(json.unitPrice);
                //obj.parent("li").parent("ul").find(".goldCount").html(json.goldCount);
                obj.parent("li").parent("ul").find(".sellableCount").html(json.sellableGoldCount);
                /****************ZW_C_JB_00008_20170516 ADD START******************************/
                obj.parent("li").parent("ul").find(".currency").html(json.goodsTypeName);
                /****************ZW_C_JB_00008_20170516 ADD END******************************/
            }
        },
        //新增页面游戏币数目和可销售库存联动
        changeGoldCountInAdd: function () {
            var goldCount = $("#addGoldCount").val();
            if (!isNumber(goldCount)) {
                alert("商品数目请输入正确的整数！");
                $("#addGoldCount").val("");
                return;
            }
            if (parseInt(goldCount) > 999999999) {
                alert("商品数目不能大于999999999");
                return;
            }
            $("#addSellableGoldCount").val(goldCount);
        },
        //新增页面的可销售库存限制条件
        changeSellableGoldCountInAdd: function () {
            var sellableGoldCount = $("#addSellableGoldCount").val();
            if (!isNumber(sellableGoldCount)) {
                alert("可销售库存请输入正确的整数！");
                $("#addSellableGoldCount").val("");
                return;
            }
            if (parseInt(sellableGoldCount) > parseInt($("#addGoldCount").val())) {
                alert("可销售库存不能大于商品数目");
                //如果用户选填的数量不正确，将总库存数量填入文本框中
                $("#addSellableGoldCount").val($("#addGoldCount").val());
                return;
            }
        },
        //主页面勾选修改，修改前判断单价的合法性
        changePriceInInput: function (obj) {
            if (isNaN(obj.val())) {
                alert("单价请输入正确的数字！");
                obj.val("0");
                return;
            }
        },
        //主页面勾选修改，游戏币数目和可销售库存联动
        changeGoldCountInInput: function (obj) {
            var goldCount = obj.val();
            if (!isNumber(goldCount)) {
                alert("商品数目请输入正确的整数！");
                obj.val("0");
                return;
            }
            if (parseInt(goldCount) > 999999999) {
                alert("商品数目不能大于999999999");
                return;
            }
            obj.parents("ul").find(".sellableCount").find(".inputSellableCount").val(goldCount);
        },
        //主页面勾选修改，可销售库存限制条件
        changeSellableCountInInput: function (obj) {
            var sellableGoldCount = obj.val();
            var goldCount = obj.parents("ul").find(".goldCount").find(".inputGoldCount").val();
            if (!isNumber(sellableGoldCount)) {
                alert("可销售库存请输入正确的整数！");
                obj.val("0");
                return;
            }
            if (parseInt(sellableGoldCount) > parseInt(goldCount)) {
                alert("可销售库存不能大于商品数目");
                obj.val("0");
                return;
            }
        },
        uploaderFile: function () {
            //上传库存
            $('#excelUpload').fileUploader({
                autoUpload: true,
                selectFileLabel: '上传库存',
                allowedExtension: 'xls|xlsx',

                onFileChange: function (e, form) {
                    upFirst = true;
                    if ($("#selGame").val() == "0") {
                        alert("请选择游戏");
                        return -1;
                    }
                    // 判断文件大小等
                    var size = e.size; // 单位：字节
                    if (size >= 20 * 1024 * 1024) {
                        alert("上传文件不能超过20M");
                        return -1;
                    }
                    $("#gameName").attr("value", $("#selGame option:selected").text());
                },
                // 设置请求头
                setRequestHeader: function (request) {
                    request.setRequestHeader("5173_authkey", getAuthkey());
                },
                // 每次上传后
                afterEachUpload: function (data, status, formContainer) {
                    $("#px-form-1").hide();
                    if (status == "success") {
                        me.sort = "";
                        me.isAsc = true;
                        me.initData();
                        alert("上传成功！");
                    }
                    else {
                        //ie浏览器中无法弹出错误信息，手动加入
                        if (navigator.userAgent.indexOf('MSIE') >= 0 && upFirst) {
                            upFirst = false;
                            alert(data.responseStatus.message);
                        }
                    }
                }
            });
        },
    }
})(jQuery);

//加载游戏
function getGame() {
    var htmlStr = "";
    for (var p in gameData) {
        var pCode = gameData[p].code;
        var pValue = gameData[p].value;
        htmlStr += "<li id=\"" + pCode + "\" lang=\"netgame\"><a class=\"hot\" title=\"" + pValue + "\" onclick=\"GetGameZone('" + pCode + "','" + pValue + "',false)\">" + pValue + "</a></li>"
    }
    $("#gsList").html(htmlStr);
    $(".gs_name dt h1").html("请选择游戏：");

    htmlStr = "<option value='0'>请选择</option>"
    for (var p in gameData) {
        var pCode = gameData[p].code;
        var pValue = gameData[p].value;
        htmlStr += "<option value='" + pCode + "'>" + pValue + "</option>"
    }
    $("#selGame").html(htmlStr);

    /************ ZW_C_JB_00008_20170516 '查询商品类目' START*************/
    //查询页面点击游戏根据gameName查询当前游戏的商品类目
    var gameName = $(this).find("li").text();
    if (gameName.length != 0 && gameName != '') {
        $.ajax({
            type: "GET",
            url: baseServiceUrl + "services/shGameConfig/getAllConfigByGameName?t=" + new Date().getTime(),
            contentType: "application/json; charset=UTF-8",
            dataType: "json",
            data: {
                "gameName": gameName,
                "enableMall": true
            },
            success: function (resp) {
                var responseStatus = resp.responseStatus;
                var code = responseStatus.code;
                GameConfigList = resp.shGameConfigList;
                if (code == "00") {
                    //将查询的商品类目加入节点中
                    if (GameConfigList != null) {
                        for (var i = 0; i < GameConfigList.length; i++) {
                            $("<option value='" + GameConfigList[i].unitName + "' onchange='selectCurrencyType(this)' id='"+GameConfigList[i].goodsTypeId+"'>" + GameConfigList[i].goodsTypeName + "</option>").appendTo($("#currencyType"));
                        }
                    }
                }
            }
        });
    }

    /************ ZW_C_JB_00008_20170516 '查询商品类目' END*************/

    //新增页面中
    //游戏
    $("#selAddGame").html(htmlStr);

    //加载阵营
    //$("#selAddRace").html("<option value='0'>请选择</option><option value='1'>联盟</option><option value='2'>部落</option>");

    $("#all").show();
    $("#all").attr("flag", "game");
    $("#all").html("全部游戏");
}
//根据游戏的id获取大区
function GetGameZone(gameIdTemp, gameNameTemp, flagInit) {
    $("#region").html("请选择");
    $("#server").html("请选择");
    $("#race").val("请选择");
    $("#searchRaceId").attr("value", gameIdTemp);//ZW_C_JB_00008_20170515 ADD
    console.log("服务:" + gameIdTemp);
    if (gameNameTemp == "魔兽世界(国服)") {
        $("#race").removeAttr("disabled");
    }
    else {
        $("#race").attr("disabled", "disabled");
    }
    if (!flagInit) {
        $("#gs_area").html("游戏区");
        $("#gs_server").html("游戏服");
    }
    gameId = gameIdTemp;
    gameName = gameNameTemp;
    $("#searchbar_arrow").css("left", "290px");
    $("#game").html(gameName);
    $(".gs_name dt h1").html("请选择游戏区：");
    $("#all").show();
    $("#all").attr("flag", "region");
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
                    htmlstr += "<li id=\"" + json.id + "\" lang=\"undefined\"><a onclick=\"GetGameServer('" + json.id + "','" + json.name + "')\">" + json.name + "</a></li>";
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
    $("#server").html("请选择");
    $("#searchbar_arrow").css("left", "400px");
    $("#region").html(zoneName);
    $(".gs_name dt h1").html("请选择游戏服：");
    $("#all").show();
    $("#all").attr("flag", "server");
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
                    htmlstr += "<li id=\"" + json.id + "\" lang=\"undefined\"><a onclick=\"getGameRace('" + json.name + "')\">" + json.name + "</a></li>";
                }
            }
            $("#gsList").html(htmlstr);
        }
    });
};

//选择服务器
function selectServer(obj) {
    $("#server").html(obj.html());
    $('#gsBox').hide();
    $('#searchbar_arrow').hide();
}
/****************ZW_C_JB_00008_20170518 ADD START******************/
function getGameRace(obj) {
    var gameServer = $("#server").text();
    if(gameServer==null||gameServer==undefined||gameServer==""||gameServer=="请选择"){
        var reg = /^[0-9a-zA-Z]+$/;
        if(!reg.test(obj)){
            $("#server").html(obj);
        }else{
            $("#server").html(gameServer);
        }
    }else{
        $("#server").html(gameServer);
    }
    var gameName = $("#game").text();
    if (gameName != "魔兽世界(国服)") {
        $("#race").text("请选择");
        $('#gsBox').hide();
        $('#searchbar_arrow').hide();
    }
    var htmlstr = "<li><a onclick=\"selectToRace('部落')\">部落</a></li>";
    htmlstr += "<li><a onclick=\"selectToRace('联盟')\">联盟</a></li>";
    $("#gsList").html(htmlstr);
    $(".gs_name dt h1").html("请选择阵营：");
    $("#all").show();
    $("#all").attr("flag", "race");
    $("#all").html("全部阵营");
}
/****************ZW_C_JB_00008_20170518 ADD START******************/
function selectToRace(obj) {
    $("#race").html(obj)
    $('#gsBox').hide();
    $('#searchbar_arrow').hide();
}
/****************ZW_C_JB_00008_20170515 ADD END***********************/

function select(obj) {
    if (obj.attr("id") == "game") {
        $("#searchbar_arrow").css("left", "180px");
        getGame();
    }
    else if (obj.attr("id") == "region") {
        $("#searchbar_arrow").css("left", "180px");
        if (gameId != null && gameId != "") {
            $("#searchbar_arrow").css("left", "290px");
            GetGameZone(gameId, gameName, true);
        }
    } else if (obj.attr("id") == "race") { //增加阵营
        /****************ZW_C_JB_00008_20170515 ADD START******************************/
        if ($("#race").text() == "全部") {
            $('#gsBox').hide();
            $('#searchbar_arrow').hide();
            return;
        }
        if($("#game").text()!="魔兽世界(国服)"){
            return;
        }
        $("#searchbar_arrow").css("left", "533px");
        getGameRace(gameId);

    } else if (obj.attr("id") == "currency") { //商品类目
        $("#searchbar_arrow").css("left", "781px");
        getGameCurrency();
        /****************ZW_C_JB_00008_20170515 ADD END******************************/
    } else {
        $("#searchbar_arrow").css("left", "180px");
        if ($("#region").text() == "全部区") {
            $('#gsBox').hide();
            $('#searchbar_arrow').hide();
            return;
        }
        if (zoneId != null && zoneId != "") {
            $("#searchbar_arrow").css("left", "400px");
            GetGameServer(zoneId, zoneName);
        }
    }
    $("#gsBox").show();
    $("#searchbar_arrow").show();
}

/****************ZW_C_JB_00008_20170515 ADD START******************************/
function getGameCurrency() {
    $(".gs_name dt h1").html("请选择商品类型：");
    $("#all").show();
    $("#all").attr("flag", "currency");
    $("#all").html("全部");
    var htmlstr = "";
    //根据获取的gameId查询商品类目
    var gameName = $("#game").text();
    var queryUrl = "";
    if(gameName==""||gameName==null||gameName==undefined||gameName=="请选择"||gameName=="全部游戏"){
        queryUrl = "services/gameCategory/queryAllGameCategory";
        $.ajax({
            type: "GET",
            url: baseServiceUrl + queryUrl +"?t=" + new Date().getTime(),
            contentType: "application/json; charset=UTF-8",
            dataType: "json",
            success: function (resp) {
                var responseStatus = resp.responseStatus;
                var code = responseStatus.code;
                GameConfigList = resp.data;
                if (code == "00") {
                    //将查询的商品类目加入节点中
                    if (GameConfigList != null) {
                        for (var i = 0; i < GameConfigList.length; i++) {
                            var googsTypeName = GameConfigList[i].name;
                            htmlstr += "<li lang=\"undefined\"><a onclick=\"searchOver('" + googsTypeName + "')\">" + googsTypeName + "</a></li>";
                        }
                        $("#gsList").html(htmlstr);
                    }
                }
            }
        });
    }else{
        queryUrl= "services/shGameConfig/getAllConfigByGameName";
        $.ajax({
            type: "GET",
            url: baseServiceUrl + queryUrl +"?t=" + new Date().getTime(),
            contentType: "application/json; charset=UTF-8",
            dataType: "json",
            data: {
                "gameName": gameName,
                "enableMall": true
            },
            success: function (resp) {
                var responseStatus = resp.responseStatus;
                var code = responseStatus.code;
                GameConfigList = resp.shGameConfigList;
                if (code == "00") {
                    //将查询的商品类目加入节点中
                    if (GameConfigList != null) {
                        for (var i = 0; i < GameConfigList.length; i++) {
                            var googsTypeName = GameConfigList[i].goodsTypeName;
                            htmlstr += "<li lang=\"undefined\"><a onclick=\"searchOver('" + googsTypeName + "')\">" + googsTypeName + "</a></li>";
                        }
                        $("#gsList").html(htmlstr);
                    }
                }
            }
        });
    }
}

function searchOver(goodsTypeName) {
    $("#currency").html(goodsTypeName);
    $('#gsBox').hide();
    $('#searchbar_arrow').hide();
}
/****************ZW_C_JB_00008_20170515 ADD END******************************/

//新增页面中根据游戏查找游戏大区列表
function selectGame(value) {
    $("#selAddServer").html("<option value='0'>请选择</option>");
    $.ajax({
        type: "GET",
        url: "http://fcd.5173.com/ajax.axd?methodName=gamesV31&cache=600&gameType=GameAreas&tradingType=other&id=" + value + "&jsoncallback=callarea",
        dataType: "jsonp",
        jsonp: "jsoncallback",
        scriptCharset: "GBK",
        jsonpCallback: "callarea",
        cache: true,
        success: function (jsonList) {
            var htmlstr = "<option value='0'>请选择</option>";
            if (jsonList != null && jsonList != undefined) {
                for (var i = 0; i < jsonList.length; i++) {
                    var json = jsonList[i];
                    htmlstr += "<option value='" + json.id + "'>" + json.name + "</option>";
                }
            }
            $("#selAddRegion").html(htmlstr);
        }
    });
    /****************ZW_C_JB_00008_20170517 ADD START******************************/
    //新增页面根据对应的gameId动态获取阵营信息
    var request = {};
    request.gameid = value;
    $.ajax({
        type: "POST",
        url: baseServiceUrl + "services/gameinfo/getGameRaceInfo",
        contentType: "application/json; charset=UTF-8",
        data: $.toJSON(request),
        success: function (resp) {
            var htmlstr = "<option value='0'>请选择</option>";
            var json = eval(resp);
            var gameRaceList = json.gameRaceInfoList;
            if (gameRaceList != null && gameRaceList != undefined&&gameRaceList.length>0) {
                for (var i = 0; i < gameRaceList.length; i++) {
                    var race = gameRaceList[i].name;
                    htmlstr += "<option value='"+i+1+"'>"+race+"</option>";
                }
                $("#selAddRace").html(htmlstr);
            }
        }
    });
    /****************ZW_C_JB_00008_20170517 ADD START******************************/

    /****************ZW_C_JB_00008_20170516 ADD START******************************/
    //根据获取的gameId获取对应的gameName查询商品类目
    var gameName = "";
    for (var data in gameData) {
        if (gameData[data].code == value) {
            gameName = gameData[data].value;
        }
    }
    if (gameName.length != 0 && gameName != '') {
        $.ajax({
            type: "GET",
            url: baseServiceUrl + "services/shGameConfig/getAllConfigByGameName?t=" + new Date().getTime(),
            contentType: "application/json; charset=UTF-8",
            dataType: "json",
            data: {
                "gameName": gameName,
                "enableMall": true
            },
            success: function (resp) {
                var responseStatus = resp.responseStatus;
                var code = responseStatus.code;
                GameConfigList = resp.shGameConfigList;
                if (code == "00") {
                    //将查询的商品类目加入节点中
                    if (GameConfigList != null) {
                        //现将之前的节点中的内容清空
                        $("#currencyType").find("option").remove();
                        $("<option value='0' onchange='selectCurrencyType(this)'>请选择</option>").appendTo($("#currencyType"));
                        for (var i = 0; i < GameConfigList.length; i++) {
                            $("<option value='" + GameConfigList[i].unitName +"' onchange='selectCurrencyType(this)' id='"+GameConfigList[i].goodsTypeId+"'>" + GameConfigList[i].goodsTypeName + "</option>").appendTo($("#currencyType"));
                        }
                    }
                }
            }
        });
    }

    /****************ZW_C_JB_00008_20170516 ADD END******************************/
}

/****************ZW_C_JB_00008_20170516 ADD START******************************/
//新增页面点选中游戏之后商品类目已选,将选择的相应的单位填入到文本中
function selectCurrencyType(obj) {
    var moneyName = obj.value;
    $("#addMoneyName").attr("value",moneyName);
}
/****************ZW_C_JB_00008_20170516 ADD END******************************/

//新增页面中根据游戏大区查找服务器列表
function selectRegion(value) {
    $.ajax({
        type: "GET",
        url: "http://fcd.5173.com/ajax.axd?methodName=gamesV31&cache=600&gameType=GameServers&tradingType=other&id=" + value + "&jsoncallback=callserver",
        dataType: "jsonp",
        jsonp: "jsoncallback",
        scriptCharset: "GBK",
        jsonpCallback: "callserver",
        cache: true,
        success: function (jsonList) {
            var htmlstr = "<option value='0'>请选择</option>";
            if (jsonList != null && jsonList != undefined) {
                for (var i = 0; i < jsonList.length; i++) {
                    var json = jsonList[i];
                    htmlstr += "<option value='" + json.id + "'>" + json.name + "</option>";
                }
            }
            $("#selAddServer").html(htmlstr);
        }
    });
}

//游戏集合
var gameData = [
    {code: '44343b06076d4a7a95a0ef22aac481ae', value: '地下城与勇士'},
    {code: '1a0d128d66b24896bf7dcf7430083cf0', value: '剑灵'},
    {code: '880', value: '魔兽世界(国服)'},
    {code: 'da451dc0df8d40e9a7aa54842687a127', value: 'QQ三国'},
    {code: 'a36ead01453c40b584f8e1e687723f2d', value: '剑侠情缘Ⅲ'},
    {code: '0ba72c47be2a46eeac63bf45d336b0ba', value: '疾风之刃'},
    {code: '5865420422194d68947c3d4b79a83204', value: '龙之谷'},
    {code: '2fdfb7d3fcd84b97b1e702d02c9ee7a7', value: '斗战神'},
    {code: '3cb8fe8afd2743e08ab577cbb525650f', value: '天涯明月刀'},
    {code: '56b957c99db144ddb042541daa4df8fd', value: '上古世纪'},
    {code: 'c96417a69a86411d95be5d5e5c0c12fa', value: '怪物猎人OL'},
    {code: '19217d865b294d88b775744afb7bdfa5', value: '冒险岛2'},
    {code: '20c8bbc1b9794fc98bd96859624d4769', value: '新天龙八部'}
];