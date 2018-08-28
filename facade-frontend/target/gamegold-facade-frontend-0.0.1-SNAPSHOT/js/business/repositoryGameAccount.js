var canSale, shGameAccountId;
$(function () {
    //获取初始参数作为设定条件
    if (getUrlParam("region") != null && getUrlParam("region") != "") {
        $("#region").text(getUrlParam("region"));
    }
    if (getUrlParam("server") != null && getUrlParam("server") != "") {
        $("#server").text(getUrlParam("server"));
    }
    if (getUrlParam("gameAccount") != null && getUrlParam("gameAccount") != "") {
        $("#txtGameAccount").val(getUrlParam("gameAccount"));
    }
    if (getUrlParam("roleName") != null && getUrlParam("roleName") != "") {
        $("#txtRoleName").val(getUrlParam("roleName"));
    }

    me.init();
    //查询
    $("#btnSearch").click(function () {
        me.init();
    });

    //导出
    $("#downloadexcel").click(function () {
        me.exportData();
    });

    //限制输入
    $("#gamePwd-input").focus(function () {
        // var regex = /[^\u4e00-\u9fa5]+/;
        // if (!regex.test(this.value)) {
        //     $("#gamePwd-input").val($("#gamePwd-input").val().substring(0, $("#gamePwd-input").val().length - 1));
        // }
        $("#gamePwd-input").val("");
    });
    //限制输入
    $("#secondPwd-input").focus(function () {
        // var regex = /[^\u4e00-\u9fa5]+/;
        // if (!regex.test(this.value)) {
        //     $("#secondPwd-input").val($("#secondPwd-input").val().substring(0, $("#secondPwd-input").val().length - 1));
        // }
        $("#secondPwd-input").val("");
    });
    $('.u-bomb-close,.mask').on('click', function () {
        $('.u-bomb,.mask').hide();
    });
    $('#fcPrice').live('keyup', function () {
        $('#totalPrice').text(($(this).val() * canSale).toFixed(2) + '元')
    });
    $('#syncBtn').click(function () {
        if($('#fcPrice').val()==0){
            alert('单价必须大于0');
            return
        }
        var $val = $('#fcPrice').val() || $('#fcDefaultPri').text().split('元')[0];
        if ($('#synchroContent').find('input').length && !$val) {
            $('.input-tip').show();
        } else {
            $.ajax({
                type: "GET",
                url: baseServiceUrl + "services/sync/syncAddRepository?t=" + new Date().getTime(),
                data: {
                    price: $val,
                    shGameAccountId: shGameAccountId
                },
                contentType: "application/json; charset=UTF-8",
                dataType: "json",
                success: function (resp) {
                    var code = resp.responseStatus.code;
                    if (code == "00") {
                        alert('同步成功');
                        me.pageselectCallback();
                        $('.input-tip,.u-bomb,.mask').hide();
                    }
                    //console.log(resp);
                }
            });
        }
    })
})
function num(value, length) {
    value = value.replace(/[^\d.]/g, "");  //清除“数字”和“.”以外的字符
    value = value.replace(/\.{2,}/g, "."); //只保留第一个. 清除多余的
    value = value.replace(".", "$#$").replace(/\./g, "").replace("$#$", ".");
    var str = '^(\\-)*(\\d+)\\.([0-9]{1,' + length + '}).*$';
    var reg = new RegExp(str);
    value = value.replace(reg, '$1$2.$3');
    if (value.indexOf(".") < 0 && value != "") {//以上已经过滤，此处控制的是如果没有小数点，首位不能为类似于 01、02的金额
        value = parseFloat(value);
    }
    return value
}
function fcSynchro(data) {
    canSale = data.repositoryCount;
    shGameAccountId = data.id;
    var priceHtml = '', priceText = '';
    $.ajax({
        type: "GET",
        url: baseServiceUrl + "services/sync/syncSelectRepositoryPrice?t=" + new Date().getTime(),
        data: {
            gameName: data.gameName,
            region: data.region,
            server: data.server,
            gameAccount: data.gameAccount,
            gameRole: data.roleName
        },
        contentType: "application/json; charset=UTF-8",
        dataType: "json",
        success: function (resp) {
            var code = resp.responseStatus.code;
            if (code == "00") {
                if (resp.price) {
                    priceHtml = '<span class="s-behind fl" id="fcDefaultPri">' + resp.price + '元/万金</span>';
                    priceText = (resp.price * canSale).toFixed(2) + '元';
                } else {
                    priceHtml = '<span class="s-behind fl"><input id="fcPrice" class="u-bomb-input-sm" type="text" placeholder="" onkeyup="this.value=num(this.value,5)" maxlength="7"><span>元/万金</span><div class="input-tip" style="display: none"><span class="span-icon-tip">请填写单价</span></div></span>'
                }
                $('#synchroContent').html('');
                var html = '';
                html += '<div class="u-item clearfix"><span class="s-front fl">游戏/区/服：</span>' +
                    '<span class="s-behind fl">' + data.gameName + '/' + data.region + '/' + data.server + '</span></div><div class="u-item clearfix"><span class="s-front fl">游戏帐号：</span>' +
                    '<span class="s-behind fl">' + data.gameAccount + '</span></div><div class="u-item clearfix"><span class="s-front fl">角色名：</span><span class="s-behind fl">' + data.roleName + '</span></div>' +
                    '<div class="u-item clearfix"><span class="s-front fl">单价：</span>' + priceHtml +
                    '</div><div class="u-item clearfix"><span class="s-front fl">可销售库存：</span><span class="s-behind fl">' + data.repositoryCount + '万金</span>' +
                    '</div><div class="u-item clearfix"><span class="s-front fl">总价：</span><span class="s-behind color fl" id="totalPrice">' + priceText + '</span></div>';
                $('#synchroContent').append(html);
                // if (!$('#synchroContent').find('input').length) {
                //     $('#syncBtn').css('background', '#999')
                // }
                $('.u-bomb,.mask').show();
            }
        }
    });
};
var me = {
    pageSize: 10,
    gameAccountList: null,
    init: function () {
        $.jqPaginator('#pagination1', {
            totalPages: 10,
            visiblePages: me.pageSize,
            currentPage: 1,
            onPageChange: function (num, type) {
                me.pageselectCallback(num);
            }
        });
    },

    //导出数据
    exportData: function () {
        url = baseServiceUrl + "services/gameAccount/exportOrder?t=" + new Date().getTime();
        var gameName = $.trim($("#game").text());
        var region = $.trim($("#region").text());
        var server = $.trim($("#server").text());
        var gameAccount = $.trim($("#txtGameAccount").val());
        var roleName = $.trim($("#txtRoleName").val());
        if (gameName == "请选择游戏" || gameName == "全部游戏") {
            gameName = "";
        }
        if (region == "请选择区" || region == "全部区") {
            region = "";
        }
        if (server == "请选择服" || server == "全部服") {
            server = "";
        }
        var params = '';
        if ($.trim(gameName).length != 0) {
            params += "&gameName=" + gameName;
        }
        if ($.trim(region).length != 0) {
            params += "&region=" + region;
        }
        if ($.trim(server).length != 0) {
            params += "&server=" + server;
        }
        if ($.trim(gameAccount).length != 0) {
            params += "&gameAccount=" + gameAccount;
        }
        if ($.trim(roleName).length != 0) {
            params += "&roleName=" + roleName;
        }
        if (params.length > 0) {
            url += '&' + params.substring(1);
        }
        window.open(url);
    },

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

        var request = {};
        request.gameName = gameName;
        request.region = region;
        request.server = server;
        request.gameRace = "";
        request.gameAccount = $.trim($("#txtGameAccount").val());
        request.roleName = $.trim($("#txtRoleName").val());
        request.pageSize = me.pageSize;
        request.page = page_index;
        if ($('#fcToge option:selected').attr('data-status') == 2 || $('#fcToge option:selected').attr('data-status') == 3) {
            request.Sale =$('#fcToge option:selected').attr('data-status') == 2 ? true : false;
        }
        $.ajax({
            type: "GET",
            url: baseServiceUrl + "services/gameAccount/queryRepositoryGameAccountList?t=" + new Date().getTime(),
            data: request,
            contentType: "application/json; charset=UTF-8",
            dataType: "json",
            success: function (resp) {
                var code = resp.responseStatus.code;
                if (code == "00") {
                    me.gameAccountList = resp.gameAccountList;
                    if (me.gameAccountList != null && me.gameAccountList.length > 0) {
                        $(".totalPage").html("共" + resp.totalCount + "笔，共" + resp.totalPage + "页");
                    }
                    else {
                        $(".totalPage").html("共0笔，共1页");
                    }
                    me.appendData();
                } else {
                    if (code == "10007" || code == "10008" || code == "10010" || code == "B1022") {
                        window.location.href = "applyseller.html";
                    }
                    else if (code == "10012") {
                        window.location.href = "rechargeList.html";
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
    },
    appendData: function () {
        var htmlStr = "";
        if (me.gameAccountList != null) {
            for (var i in me.gameAccountList) {
                var gameAccount = me.gameAccountList[i], toggleHtml = '';
                // var repositoryCount = gameAccount.repositoryCount;
                // if (repositoryCount == undefined || repositoryCount == null || repositoryCount == "") {
                //     repositoryCount = 0;
                // }
                if (gameAccount.isSale) {
                    toggleHtml = '<a class="fc-syn disable" href="javascript:void(0)">同步</a></td>';
                } else {
                    toggleHtml = '<a class="fc-syn" href="javascript:void(0)" onclick="fcSynchro(' + JSON.stringify(gameAccount).replace(/"/g, "&quot;") + ')">同步</a></td>';
                }
                htmlStr += "<tr>";
                htmlStr += "<td width=\"200\">" + gameAccount.gameName + gameAccount.region + "/" + gameAccount.server + "</td>";
                htmlStr += '<td width=\"100\"><a class="fc-log" href="/fcLog.html?gameName=' + gameAccount.gameName + '&gameRegion=' + gameAccount.region + '&gameServer=' + gameAccount.server + '&gameAccount=' + gameAccount.gameAccount + '&roleName=' + gameAccount.roleName + '">' + gameAccount.gameAccount + "</a></td>";
                htmlStr += "<td width=\"100\"><p>" + gameAccount.roleName + "</p><p>(" + gameAccount.level + "级)</p></td>";
                htmlStr += "<td width=\"100\">" + gameAccount.tradeQuota + "</td>";
                htmlStr += "<td width=\"100\">" + gameAccount.repositoryQuota + "</td>";
                htmlStr += "<td width=\"100\">" + gameAccount.repositoryCount + "</td>";
                htmlStr += "<td width=\"100\">" + gameAccount.todaySaleCount + "</td>";
                if (gameAccount.isShRole) {
                    htmlStr += "<td width=\"100\">-</td>";
                } else {
                    htmlStr += "<td width=\"100\">" + gameAccount.repositoryGaps + "</td>";
                }
                // htmlStr += "<td width=\"100\">" + gameAccount.level + "</td>";
                //htmlStr += "<td width=\"100\" class=\"tc\">" + (gameAccount.isShRole ? "是" : "否") + "</td>";
                htmlStr += '<td width="150" class="tc"><a class="fc-log" href="/fcLog.html?gameName=' + gameAccount.gameName + '&gameRegion=' + gameAccount.region + '&gameServer=' + gameAccount.server + '&gameAccount=' + gameAccount.gameAccount + '&roleName=' + gameAccount.roleName + '">日志</a>';
                htmlStr += toggleHtml
                // htmlStr += "<td width=\"200\" class=\"tc\"><input type=\"button\" class=\"update_btn\"gid=\"" + gameAccount.id + "\" gameAccount=\"" + gameAccount.gameAccount + "\" roleName=\"" + gameAccount.roleName + "\" gamePwd=\"******\" secondPwd=\"******\"" +
                //   }                                                       "value=\"修改密码\" style='cursor:pointer;' href=\"#OpenWindowRecharge\" rel=\"leanModal\"/>&nbsp&nbsp&nbsp<input type=\"button\" gid=\"" + gameAccount.id + "\" class=\"delete_btn\" value=\"删除\" /></td>";
            }
        }

        $("#tbRepositoryList tbody").html(htmlStr);
        $('.update_btn').leanModal({ closeButton: ".modal_close" });
        //修改密码
        $('.update_btn').click(function () {
            //显示账号，密码
            $("#txtGameAccount1").val($(this).attr("gameAccount"));
            $("#txtRoleName1").val($(this).attr("roleName"));
            var gamePwd1 = $(this).attr("gamePwd");
            $("#gamePwd-input").val(gamePwd1);
            var secondPwd1 = $(this).attr("secondPwd");
            $("#secondPwd-input").val(secondPwd1);
            $("#gid").val($(this).attr("gid"));

            //保存
            $("#btnUpdatePWD").unbind('click').click(function () {
                var gamePwd = $.trim($("#gamePwd-input").val());
                var secondPwd = $.trim($("#secondPwd-input").val());
                var id = $("#gid").val();
                if (gamePwd == "") {
                    gamePwd = gamePwd1;
                }
                if (secondPwd == "") {
                    secondPwd = secondPwd1;
                }
                var request = {};
                request.id = id;
                request.gamePwd = gamePwd;
                request.secondPwd = secondPwd;
                $.ajax({
                    type: "POST",
                    url: baseServiceUrl + "services/gameAccount/updategamePwd",
                    data: $.toJSON(request),
                    contentType: "application/json; charset=UTF-8",
                    dataType: "json",
                    success: function (resp) {
                        var code = resp.responseStatus.code;
                        if (code == "00") {
                            alert("保存成功");
                            $("#OpenWindowRecharge").hide();
                            $("#lean_overlay").hide();
                            me.init();
                        } else {
                            alert("保存失败");
                        }
                    }
                });
            });

        });
        //删除
        $('.delete_btn').click(function () {

            if (confirm("是否确定删除?")) {
                var request = {};
                request.id = $(this).attr("gid");
                console.log(request);
                $.ajax({
                    type: "GET",
                    url: baseServiceUrl + "services/gameAccount/deleteGameAccount",
                    data: request,
                    contentType: "application/json; charset=UTF-8",
                    dataType: "json",
                    success: function (resp) {
                        var code = resp.responseStatus.code;
                        if (code == "00") {
                            alert("删除成功");
                            me.init();
                        } else {
                            alert("系统异常，删除失败");
                        }
                    }
                });
            }
        });
    }
}