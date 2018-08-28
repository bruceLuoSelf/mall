// var isKai = true
var isKaiGuan = false
var numTotle = ""
$(function() {
    $('#hdShow').leanModal({
        closeButton: ".modal_close"
    });
    me.init(true);
    me.getLog();
    me.bindBasicData();
    me.bindEvent();
    $("#isToTop").click(function() {
        window.scrollTo(0, 0)
    })
    // $("#divMsg").scroll(function() {
    //   console.log('-------------------',$(".chat-line").length);
    //   numTotle = $(".chat-line").length
    //     // viewH = $(this).height(), //可见高度
    //     //     contentH = $(this).get(0).scrollHeight, //内容高度
    //     //     scrollTop = $(this).scrollTop(); //滚动高度
    //     // console.log("di", contentH - viewH - scrollTop);
    //     // if (contentH - viewH - scrollTop < 30) { //
    //     //     isKai = true
    //     // } else {
    //     //     isKai = false
    //     // }
    //
    // });

});

var me = {
    id: "",
    orderId: "",
    deliverOrder: null,
    msg: "",
    url: "",
    subOrderList: {},
    isClose: function() {
        $(".sure_after").hide();
    },
    //撤单接口
    eidGoods: function(id) {
        if (me.deliveryOrder != null && me.deliveryOrder != "") {
            if (me.deliveryOrder.status == shStatus.Waiting || me.deliveryOrder.status == shStatus.Queue || me.deliveryOrder.status == shStatus.Dealing) {
                if(id){
                  isKaiGuan = id
                }else{
                  isKaiGuan = false
                }
                $('#hdShow').bind("click");
                $('#hdShow').leanModal({
                    closeButton: ".modal_close"
                });
                $('#hdShow').click();
            } else {
                alert("当前状态无法撤单！");
                $('#hdShow').unbind("click");
            }
        } else {
            alert("当前状态无法撤单！");
            $('#hdShow').unbind("click");
        }
    },
    //转人工
    faPerson: function(id) {
        var request = {};
        request.id = id;
        $.ajax({
            type: "GET",
            url: baseServiceUrl + "services/chorder/turnToArtifical",
            data: request,
            contentType: "application/json; charset=UTF-8",
            dataType: "json",
            success: function(resp) {
                var code = resp.responseStatus.code;
                if (code == "00") {
                    alert("转人工申请成功")
                    me.getLog()
                } else if (code == "21016") {
                    history.back();
                }
            }
        });

    },
    //无异议
    faNoMessage: function(id) {
        // var request = {};
        // request.SubOrderId = id;
        $.ajax({
            type: "GET",
            url: baseServiceUrl + "services/deliveryOrder/noQuestion",
            data: {
                "SubOrderId": id
            },
            contentType: "application/json; charset=UTF-8",
            dataType: "json",
            success: function(resp) {
                var code = resp.responseStatus.code;
                if (code == "00") {
                    alert("无异议申请成功")
                    me.getLog()
                } else if (code == "21016") {
                    history.back();
                }
            }
        });
    },
    isTrueShorder: function() {
        var request = {}
        var requestNum = {}
        $('.alert_jg').hide();
        for (var i in me.subOrderList) {
            if ($('.num_input').eq(i).val() == "") {
                $('.alert_jg').eq(i).show();
                return false
            }
            request[me.subOrderList[i].id] = Number($('.num_input').eq(i).val())
        }
        requestNum.json = JSON.stringify(request)
        // request.subOrderId = me.subOrderList[0].id
        // request.realCount= $('.num_input').val();
        $.ajax({
            type: "GET",
            url: baseServiceUrl + "services/chorder/confirmShipment",
            data: requestNum,
            contentType: "application/json; charset=UTF-8",
            dataType: "json",
            success: function(resp) {
                var code = resp.responseStatus.code;
                if (code == "00") {
                    me.getLog()
                    $(".sure_after").hide();
                } else if (code == "21016") {
                    history.back();
                }
            }
        });
    },
    init: function(isFirstReques) {
        me.orderId = $.trim(getUrlParam("orderId"));
        var request = {};
        request.orderId = me.orderId;
        $.ajax({
            type: "GET",
            url: baseServiceUrl + "services/chorder/selectOrderById?t=" + new Date().getTime(),
            data: request,
            contentType: "application/json; charset=UTF-8",
            dataType: "json",
            success: function(resp) {
                var code = resp.responseStatus.code;
                if (code == "00") {
                    if (resp.deliveryOrder.appealOrder) {
                        window.location.href = "succ_single.html?orderId=" + resp.deliveryOrder.orderId
                    }
                    me.deliveryOrder = resp.deliveryOrder;

                    me.tradeLogo = resp.deliveryOrder.tradeLogo;
                    $("#qq-link").attr("href", "tencent://message/?uin=" + me.deliveryOrder.serviceQq + "&Site=5173&Menu=yes");
                    if (resp.deliveryConfig) {
                        me.url = resp.deliveryConfig.helpeUrl;
                    }
                    me.initPageData(isFirstReques);
                } else if (code == "21016") {
                    history.back();
                }
            }
        });
    },
    initPageData: function(isFirstReques) {
        if (me.deliveryOrder != null && me.deliveryOrder != "") {
            $.ajax({
                type: "GET",
                url: baseServiceUrl + "services/deliveryOrder/selectDeliverySubOrderData?t=" + new Date().getTime(),
                data: {
                    orderId: me.orderId,
                    sellerInputCount: me.deliveryOrder.count
                },
                contentType: "application/json; charset=UTF-8",
                dataType: "json",
                success: function(resp) {
                    var code = resp.responseStatus.code;
                    if (code == "00") {
                        console.log(resp);
                        var totalCount = ""
                        var totalSellerInputCount = ""
                        var totalRealCount = ""
                        if (resp.subOrderList != null && resp.subOrderList != undefined && resp.subOrderList != '' && resp.subOrderList.length > 0) {
                            me.subOrderList = resp.subOrderList
                            if ($("#btnLoginGame").html() == "已登录游戏") {
                                if (me.deliveryOrder.orderStatusStr == "待分配角色") {
                                    $(".new-table").hide()
                                    $('.order-table').hide()
                                } else {
                                    if (me.tradeLogo == 5) {
                                        $(".new-table").hide()
                                        $(".order-table").show()
                                    } else {
                                        $(".new-table").show()
                                        $('.order-table').hide()
                                    }

                                }


                            }

                            var htmlStr = ""
                            if (me.tradeLogo == 5) {
                              htmlStr += '<table><thead><tr><td width="125px">收货角色信息</td><td width="120px">装备截图</td><td width="125px">拍卖价格（金）</td><td width="95px">状态</td><td width="195px">操作</td></tr></thead><tbody><tr>';
                            }
                            for (var i in resp.subOrderList) {
                                subOrder = resp.subOrderList[i];
                                totalCount = Number(subOrder.count) + Number(totalCount);
                                totalSellerInputCount = Number(subOrder.sellerInputCount) + Number(totalSellerInputCount);
                                totalRealCount = Number(subOrder.realCount) + Number(totalRealCount)
                                if (subOrder == null || subOrder.gameRole == null) {
                                    break;
                                }

                                if (me.tradeLogo == 5) {

                                    htmlStr += "<td>" + subOrder.gameRole + "(" + subOrder.roleLevel + ")级</td>";

                                    if (subOrder.robotImgList.length == 0) {
                                        htmlStr += "<td><a target='_blank'></a></td>";
                                    }else{
                                      var arr=[];
                                      htmlStr +="<td>"
                                      for(var j in subOrder.robotImgList){
                                        arr.push(subOrder.robotImgList[j].imgSrc)
                                      }
                                      htmlStr += "<a href=orderDetailImage.html?url="+arr.join(',')+" target=\"_black\">点击查看<br/></a>";
                                      htmlStr +="</td>"
                                    }


                                    htmlStr += "<td>" + (subOrder.count*10000+subOrder.afterFour) + "</td>";
                                    htmlStr += "<td>" + subOrder.status + "</td>";
                                    // htmlStr += "<td>" + me.deliveryOrder.orderStatusStr + "</td>";
                                    if (subOrder.subOrderStatus == 4 || subOrder.subOrderStatus == 5 || subOrder.subOrderStatus == 6) {
                                        htmlStr += "<td><span style='cursor:not-allowed;' class='btn active'>取消订单</span></td></tr>";
                                    } else if(subOrder.machineArtificialStatus == 1){
                                        htmlStr += "<td><span style='cursor:not-allowed;' class='btn active'>取消订单</span></td></tr>";
                                    } else if (subOrder.subOrderStatus == 3) {
                                        htmlStr += "<td><span class='btn' onclick='me.eidGoods(" + subOrder.id + ")'>取消订单</span></td></tr>";
                                    } else {
                                        htmlStr += "<td><span style='cursor:not-allowed;' class='btn active'>取消订单</span></td></tr>";
                                    }


                                } else {
                                    htmlStr += "<tr class='new_item'>";
                                    htmlStr += "<td width='112'><span>" + subOrder.gameRole + "</span><em>(" + subOrder.roleLevel + ")级</em></td>";
                                    htmlStr += "<td width='112'>" + subOrder.count + "万</td>";
                                    if (subOrder.sellerInputCount == null || "") {
                                        htmlStr += "<td width='112'></td>";
                                    } else {
                                        htmlStr += "<td width='112'>" + subOrder.sellerInputCount + "万</td>";
                                    }
                                    if (subOrder.realCount == null) {
                                        htmlStr += "<td width='85'></td>";
                                    } else if (subOrder.sellerInputCount == null) {
                                        htmlStr += "<td width='85'></td>";
                                    } else {
                                        htmlStr += "<td width='85'>" + subOrder.realCount + "万</td>";
                                    }

                                    htmlStr += "<td width='85'>" + subOrder.status + "</td>";
                                    htmlStr += "<td width='150'>";
                                    if (me.subOrderList.length == 1) {
                                        if (subOrder.subOrderStatus == 6 || subOrder.subOrderStatus == 4 || subOrder.subOrderStatus == 5) {
                                            htmlStr += "<div><a class='elect_order active' style='cursor:not-allowed;'>取消订单</a>";
                                            htmlStr += "<a class='ote_order active' style='cursor:not-allowed;'>我已发货</a></div>";
                                            htmlStr += "<div><a style='cursor:not-allowed;'>无异议</a>";
                                            htmlStr += "<a style='cursor:not-allowed;'>转人工</a></div>";
                                            $('#btnCancel').hide()
                                        } else if (subOrder.subOrderStatus == 3 && subOrder.sellerInputCount == "") {
                                            htmlStr += "<div><a onclick='me.eidGoods()' class='elect_order'>取消订单</a>";
                                            htmlStr += "<a onclick='me.isTrueClick()' class='ote_order'>我已发货</a></div>";

                                            if (subOrder.waitToConfirm) {
                                                htmlStr += "<div><a style='color: #06C;' onclick='me.faNoMessage(" + subOrder.id + ")' >无异议</a>";
                                                htmlStr += "<a style='color: #06C;' onclick='me.faPerson(" + subOrder.id + ")' >转人工</a></div>";
                                            } else {
                                                htmlStr += "<div><a style='cursor:not-allowed;'>无异议</a>";
                                                htmlStr += "<a style='cursor:not-allowed;'>转人工</a></div>";

                                            }
                                            $('#btnCancel').show()

                                        } else if (subOrder.subOrderStatus == 3 && subOrder.sellerInputCount == null && subOrder.realCount == null) {
                                            htmlStr += "<div><a onclick='me.eidGoods()' class='elect_order'>取消订单</a>";
                                            htmlStr += "<a onclick='me.isTrueClick()' class='ote_order'>我已发货</a></div>";

                                            if (subOrder.waitToConfirm) {
                                                htmlStr += "<div><a style='color: #06C;' onclick='me.faNoMessage(" + subOrder.id + ")' >无异议</a>";
                                                htmlStr += "<a style='color: #06C;' onclick='me.faPerson(" + subOrder.id + ")' >转人工</a></div>";
                                            } else {
                                                htmlStr += "<div><a style='cursor:not-allowed;'>无异议</a>";
                                                htmlStr += "<a style='cursor:not-allowed;'>转人工</a></div>";

                                            }
                                            $('#btnCancel').show()
                                        } else if (subOrder.subOrderStatus == 3 && subOrder.sellerInputCount != "" && subOrder.realCount != 0) {
                                            htmlStr += "<div><a  class='elect_order active' style='cursor:not-allowed;'>取消订单</a>";
                                            htmlStr += "<a class='ote_order active' style='cursor:not-allowed;'>我已发货</a></div>";
                                            if (subOrder.waitToConfirm) {
                                                htmlStr += "<div><a style='color: #06C;' onclick='me.faNoMessage(" + subOrder.id + ")' >无异议</a>";
                                                htmlStr += "<a style='color: #06C;' onclick='me.faPerson(" + subOrder.id + ")' >转人工</a></div>";
                                            } else {
                                                htmlStr += "<div><a style='cursor:not-allowed;'>无异议</a>";
                                                htmlStr += "<a style='cursor:not-allowed;'>转人工</a></div>";

                                            }
                                            $('#btnCancel').hide()
                                        } else if (subOrder.subOrderStatus == 3 && subOrder.sellerInputCount != "" && subOrder.realCount == "0") {
                                            htmlStr += "<div><a onclick='me.eidGoods()' class='elect_order'>取消订单</a>";
                                            //后期去掉d

                                            htmlStr += "<a class='ote_order active' style='cursor:not-allowed;'>我已发货</a></div>";
                                            if (subOrder.waitToConfirm) {
                                                htmlStr += "<div><a style='color: #06C;' onclick='me.faNoMessage(" + subOrder.id + ")' >无异议</a>";
                                                htmlStr += "<a style='color: #06C;' onclick='me.faPerson(" + subOrder.id + ")' >转人工</a></div>";
                                            } else {
                                                htmlStr += "<div><a style='cursor:not-allowed;'>无异议</a>";
                                                htmlStr += "<a style='cursor:not-allowed;'>转人工</a></div>";
                                            }
                                            $('#btnCancel').show()
                                        }
                                    } else {

                                        if (subOrder.subOrderStatus == 6 || subOrder.subOrderStatus == 4 || subOrder.subOrderStatus == 5) {
                                            htmlStr += "<div><a style='cursor:not-allowed;'>无异议</a>";
                                            htmlStr += "<a style='cursor:not-allowed;'>转人工</a></div>";
                                        } else if (subOrder.subOrderStatus == 3 && subOrder.sellerInputCount != "" && subOrder.realCount != 0) {
                                            if (subOrder.waitToConfirm) {
                                                htmlStr += "<div><a style='color: #06C;' onclick='me.faNoMessage(" + subOrder.id + ")' >无异议</a>";
                                                htmlStr += "<a style='color: #06C;' onclick='me.faPerson(" + subOrder.id + ")' >转人工</a></div>";
                                            } else {
                                                htmlStr += "<div><a style='cursor:not-allowed;'>无异议</a>";
                                                htmlStr += "<a style='cursor:not-allowed;'>转人工</a></div>";

                                            }
                                        } else if (subOrder.subOrderStatus == 3 && subOrder.sellerInputCount == null && subOrder.realCount == "0") {

                                            if (subOrder.waitToConfirm) {
                                                htmlStr += "<div><a style='color: #06C;' onclick='me.faNoMessage(" + subOrder.id + ")' >无异议</a>";
                                                htmlStr += "<a style='color: #06C;' onclick='me.faPerson(" + subOrder.id + ")' >转人工</a></div>";
                                            } else {
                                                htmlStr += "<div><a style='cursor:not-allowed;'>无异议</a>";
                                                htmlStr += "<a style='cursor:not-allowed;'>转人工</a></div>";
                                            }
                                        } else if (subOrder.subOrderStatus == 3 && subOrder.sellerInputCount != "" && subOrder.realCount == "0") {

                                            if (subOrder.waitToConfirm) {
                                                htmlStr += "<div><a style='color: #06C;' onclick='me.faNoMessage(" + subOrder.id + ")' >无异议</a>";
                                                htmlStr += "<a style='color: #06C;' onclick='me.faPerson(" + subOrder.id + ")' >转人工</a></div>";
                                            } else {
                                                htmlStr += "<div><a style='cursor:not-allowed;'>无异议</a>";
                                                htmlStr += "<a style='cursor:not-allowed;'>转人工</a></div>";
                                            }
                                        }
                                    }
                                    htmlStr += "</td>";
                                    htmlStr += "</tr>";

                                }
                                if (me.tradeLogo != 5) {
                                  if (me.subOrderList.length > 1) {

                                      htmlStr += " <tr class='new_item myskin'>";
                                      htmlStr += "<td width='112'>汇总</td>";
                                      htmlStr += "<td width='112'>" + totalCount + "万</td>";
                                      if (totalSellerInputCount + "" == "null" || totalSellerInputCount == 0) {
                                          htmlStr += "<td width='112'></td>";
                                      } else {
                                          htmlStr += "<td width='112'>" + totalSellerInputCount + "万</td>";
                                      }

                                      if (totalRealCount + "" == "null" || totalRealCount == 0) {
                                          htmlStr += "<td width='85'></td>";
                                      } else if (totalSellerInputCount == 0) {
                                          htmlStr += "<td width='85'></td>";
                                      } else {
                                          htmlStr += "<td width='85'>" + totalRealCount + "万</td>";
                                      }


                                      htmlStr += "<td width='85'>" + me.deliveryOrder.orderStatusStr + "</td>";
                                      if (me.deliveryOrder.status == 4 || me.deliveryOrder.status == 5 || me.deliveryOrder.status == 6) {
                                          htmlStr += "<td width='150'><a style='cursor:not-allowed;' class='slt_order record active'>取消订单</a><a style='cursor:not-allowed;' class='slt_num active'>我已发货</a></td></tr>";
                                          $('#btnCancel').hide()
                                      } else if (me.deliveryOrder.status == 3 && totalSellerInputCount == 0) {
                                          htmlStr += "<td width='150'><a class='slt_order record' onclick='me.eidGoods()'>取消订单</a><a class='slt_num' onclick='me.isTrueClick()'>我已发货</a></td></tr>";
                                          $('#btnCancel').show()
                                      } else {
                                          htmlStr += "<td width='150'><a style='cursor:not-allowed;' class='slt_order record active'>取消订单</a><a style='cursor:not-allowed;' class='slt_num active'>我已发货</a></td></tr>";
                                          $('#btnCancel').hide()
                                      }

                                  }
                                }

                            }
                            if (me.tradeLogo == 5) {
                                htmlStr += "</tbody></table></div>"
                                $('#newrc-paimai').html(htmlStr);
                            } else {
                                $('#newrc-table').html(htmlStr);

                            }






                        }

                    }
                }
            });



            var deliveryOrder = me.deliveryOrder;
            me.id = deliveryOrder.id;
            $("#spGameInfo").html(deliveryOrder.gameName + "/" + deliveryOrder.region + "/" + deliveryOrder.server);

            var chInfo = "<p><span class=\"word\">出货角色：</span>" + deliveryOrder.roleName + "</p>";
            chInfo += "<p><span class=\"word\">订单单价：</span><em class='price'>" + deliveryOrder.price + "</em>元/" + deliveryOrder.moneyName + "</p>";
            chInfo += "<p><span class=\"word\">订单金额：</span><em class='price'>" + deliveryOrder.amount.toFixed(2) + "</em>元</p>";
            chInfo += "<p><span class=\"word\">交易模式：</span>机器收货</p>";
            chInfo += "<p><span class=\"word\">交易方式：</span>" + deliveryOrder.tradeTypeName + "<a href=" + me.url + " target='_blank'>出货教程</a></p>";
            chInfo += "<p><span class=\"word\">交易地点：</span>无</p>";
            if (deliveryOrder.orderStatusStr != "取消订单") {
                chInfo += "<p><span class=\"word\">交易状态：</span>" + deliveryOrder.orderStatusStr + "</p>";;
            }
            chInfo += "<p><span class=\"word\">交易类目：</span>" + deliveryOrder.goodsTypeName + "</p>";
            chInfo += "<p><span class=\"word\">订单编号：</span>" + deliveryOrder.orderId + "</p>";
            //chInfo+="交易状态：未登录";
            // for (var p in ChOrderState) {
            //     if (deliveryOrder.status == ChOrderState[p].code) {
            //         chInfo += "交易状态：" + ChOrderState[p].value;
            //         break;
            //     }
            // }

            if (deliveryOrder.machineArtificialStatus != 1) {
                $("#btnFinish").css('display', 'none');
                $("#btnPartFinish").css('display', 'block');
            } else { //转人工成功，显示我已发货按钮，隐藏部分完成按钮
                $("#btnFinish").css('display', 'block');
                $("#btnPartFinish").css('display', 'none');
                //判断我已发货按钮是否已经触发过，若触发过将其禁用
                if (deliveryOrder.realCount >= 1) {
                    $("#btnFinish").attr('disabled', 'false');
                    $("#btnFinish").css({
                        'color': '#999',
                        'background': 'url(./img/qgbtnico_bg.png) -118px -115px no-repeat'
                    });
                }
            }

            if (deliveryOrder.status == shStatus.Waiting) {
                $("#btnLoginGame").removeClass("unenabled");
                $("#btnLoginGame").html("我已登录");


                //我已登录
                $("#btnLoginGame").unbind().bind('click', function() {
                    clearInterval(me.loopGetData())
                    me.loginGame();
                });
            } else {

                $("#btnLoginGame").html("已登录游戏").addClass("unenabled").unbind("click");

            }
            if (isFirstReques) {
                me.loopGetData();
            }

            if (deliveryOrder.status == shStatus.Complete || deliveryOrder.status == shStatus.PartComplete || deliveryOrder.status == shStatus.Cancle || deliveryOrder.status == shStatus.HumanOp) {
                //跳转
                window.open(baseHtmlUrl + "orderResult1.html?orderId=" + deliveryOrder.orderId + "&t=" + (new Date()).getTime(), "_self");
            }
            $("#spChInfo").html(chInfo);
            if(deliveryOrder.status=="3"){
              $("#btnCancel").hide()
            }
            // $("#pOrderId").html("订单编号：" + deliveryOrder.orderId);
            $("#spanCount").html("订单数量：" + deliveryOrder.count + deliveryOrder.moneyName);
            $("#spanHaveCount").html("已交易：<em>" + deliveryOrder.realCount + "</em>" + deliveryOrder.moneyName);
        }
    },
    //收货列表选项
    isTrueClick: function() {
        $(".sure_after").show();

        console.log(me.subOrderList)
        if (me.subOrderList != null && me.subOrderList != undefined && me.subOrderList != '' && me.subOrderList.length > 0) {
            if (me.subOrderList.length < 4) {
                $('.list_det').css('width', "510px")
                $('.title_list').css('width', "508px")
            } else {
                $('.list_det').css('width', "527px")
                $('.title_list').css('width', "491px")
            }

            if (me.subOrderList.length == 1) {
                $('.slet_title').hide()
            } else {
                $('.slet_title').show()
            }
            var strNumber = ""
            var sellNumber = ""
            var htmlStr = ""
            for (var i in me.subOrderList) {
                popSubOrder = me.subOrderList[i];
                strNumber = Number(popSubOrder.count) + Number(strNumber)
                if (popSubOrder == null || popSubOrder.gameRole == null) {
                    break;
                }
                if (popSubOrder.sellerInputCount == null) {
                    popSubOrder.sellerInputCount = ""
                }
                htmlStr += "<tr class='new_item'>";
                htmlStr += "<td>" + popSubOrder.gameRole + "</td>";
                htmlStr += "<td><span>" + popSubOrder.count + "</span>万</td>";
                htmlStr += "<td><em><input type='text' class='num_input'>万</em><em class='alert_jg'>请输入已发货数量</em></td>";

                htmlStr += "</td>";
                htmlStr += "</tr>";

            }
            $('.slet_title').html('<span>汇总</span><span>' + strNumber + '万</span><span>' + sellNumber + '万</span>')
            $('#delog_button').html(htmlStr);
            $('.alert_jg').hide();
            $('.num_input').bind('input propertychange', function() {
                var num = Number($(this).parent().parent().prev().find("span").html())

                if ($(this).val() > num) {
                    $(this).val($(this).val().substring(0, $(this).val().length - 1))
                }
                if ($(this).val().indexOf(".") > 0) {
                    $(this).val($(this).val().substring(0, $(this).val().length - 1))
                }
                var re = /^\+?[1-9][0-9]*$/;
                if (!re.test($(this).val())) {
                    $(this).val($(this).val().substring(0, $(this).val().length - 1))
                }


            })
            $('.num_input').focus(function() {
                $('.alert_jg').hide()
            })
            // $('.num_input').blur(function() {
            //     sellNumber = ""
            //     for (var i = 0; i < $('.num_input').length; i++) {
            //
            //         console.log($('.num_input').eq(i).val())
            //         if ($('.num_input').eq(i).val() != "") {
            //             sellNumber = Number($('.num_input').eq(i).val()) + Number(sellNumber)
            //         }
            //     }
            //
            //     $('.slet_title').html('<span>汇总</span><span>' + strNumber + '万</span><span>' + sellNumber + '万</span>')
            // })


            $('.num_input').keyup(function() {
                sellNumber = ""
                for (var i = 0; i < $('.num_input').length; i++) {

                    console.log($('.num_input').eq(i).val())
                    if ($('.num_input').eq(i).val() != "") {
                        sellNumber = Number($('.num_input').eq(i).val()) + Number(sellNumber)
                    }
                }

                $('.slet_title').html('<span>汇总</span><span>' + strNumber + '万</span><span>' + sellNumber + '万</span>')
            })

        }

    },
    bindEvent: function() {
        //弹出发货页面
        $('#btnFinish').click(function() {
            $('#poprm1').css('display', 'block');
        })
        //关闭发货弹窗
        $('.rc-cancel').click(function() {
            $('#poprm1').css('display', 'none');
        })
        //确认发货弹窗
        $('.rc-success').click(function() {
            orderId = me.orderId;
            realCount = $("#shOrderNum").val();
            //验证输入数字的合法性
            var flag = checkValidity(realCount, me.deliveryOrder.count);
            if (!flag) {
                alert("请输入正确的收货数量");
                $("#shOrderNum").val("");
                return;
            }

            $.ajax({
                type: "GET",
                url: baseServiceUrl + "services/chorder/confirmShipment",
                data: {
                    "orderId": orderId,
                    "realCount": realCount
                },
                contentType: "application/json; charset=UTF-8",
                dataType: "json",
                success: function() {
                    me.getLog()
                }
            });
        })
        //是否弹出撤单页面
        $("#btnCancel").click(function() {
            if (me.deliveryOrder != null && me.deliveryOrder != "") {
                if (me.deliveryOrder.status == shStatus.Waiting || me.deliveryOrder.status == shStatus.Queue || me.deliveryOrder.status == shStatus.Dealing) {
                    $('#hdShow').bind("click");
                    $('#hdShow').leanModal({
                        closeButton: ".modal_close"
                    });
                    $('#hdShow').click();
                } else {
                    alert("当前状态无法撤单！");
                    $('#hdShow').unbind("click");
                }
            } else {
                alert("当前状态无法撤单！");
                $('#hdShow').unbind("click");
            }
        });

        //撤单
        $("#btnCancle").click(function() {
            me.cancle();
        });

        // 申请部分完成
        $("#btnPartFinish").click(function() {
            if (me.deliveryOrder != null && me.deliveryOrder != "") {
                if (me.deliveryOrder.status == shStatus.Dealing) {
                    me.partComplete();
                } else {
                    alert("当前状态无法部分完成！");
                }
            } else {
                alert("当前状态无法部分完成！");
            }
        });
    },
    //绑定撤单信息
    bindBasicData: function() {
        for (var i in ChCancleReason) {
            $("#selCancleReason").append("<option value='" + ChCancleReason[i].code + "'>" + ChCancleReason[i].value + "</option>");
        }
        $("#otherCancleReason").attr("disabled", "disabled");

        $("#selCancleReason").change(function() {
            if ($(this).val() == 408) {
                $("#otherCancleReason").removeAttr("disabled");
            } else {
                $("#otherCancleReason").attr("disabled", "disabled");
            }
        });
    },
    //撤单
    cancle: function() {
        var cancleReason = $("#selCancleReason").val();
        var otherReason = $.trim($("#otherCancleReason").val());
        if (cancleReason == "请选择撤单原因") {
            alert("请选择撤单原因");
            return;
        }
        if (cancleReason == 408) {
            if (otherReason == "") {
                alert("请填写其他原因");
                return;
            }
        }

        var request = {};
        request.reason = $("#selCancleReason").val();
        request.remark = $("#otherCancleReason").val();
        if(isKaiGuan){
          request.subOrderId = isKaiGuan;
          $.ajax({
              type: "GET",
              url: baseServiceUrl + "services/chorder/cancelSubOrder",
              data: request,
              contentType: "application/json; charset=UTF-8",
              dataType: "json",
              success: function(resp) {
                  var code = resp.responseStatus.code;
                  if (code == "00") {
                      //跳转
                      window.location.reload()
                  }
              }
          });
        }else{
          request.id = me.id;
          $.ajax({
              type: "GET",
              url: baseServiceUrl + "services/chorder/cancelOrder",
              data: request,
              contentType: "application/json; charset=UTF-8",
              dataType: "json",
              success: function(resp) {
                  var code = resp.responseStatus.code;
                  if (code == "00") {
                      //跳转
                      window.open(baseHtmlUrl + "orderResult1.html?orderId=" + me.orderId + "&t=" + (new Date()).getTime(), "_self");
                  }
              }
          });
        }

    },
    partComplete: function() {
        if (confirm("是否确定部分完成?")) {
            var request = {};
            request.id = me.id;

            $.ajax({
                type: "GET",
                url: baseServiceUrl + "services/chorder/applyForCompletePart",
                data: request,
                contentType: "application/json; charset=UTF-8",
                dataType: "json",
                success: function(resp) {
                    var code = resp.responseStatus.code;
                    if (code == "00") {
                        //跳转
                        window.open(baseHtmlUrl + "orderResult1.html?orderId=" + me.orderId + "&t=" + (new Date()).getTime(), "_self");
                    }
                }
            });
        }
    },
    loginGame: function() {
        var request = {};
        request.id = me.id;
        $.ajax({
            type: "GET",
            url: baseServiceUrl + "services/chorder/startTrading?t=" + new Date().getTime(),
            data: request,
            contentType: "application/json; charset=UTF-8",
            dataType: "json",
            success: function(resp) {
                var code = resp.responseStatus.code;
                if (code == "303") {
                    window.location.reload();
                }
                if (code == "00") {
                    $("#btnLoginGame").html("已登录游戏").addClass("unenabled").unbind("click");
                    me.getLog()
                    //me.loopGetData();
                }
            }
        });
    },
    loopGetData: function() {
        setInterval("me.getLog()", 10000);
    },
    getLog: function() {
        if (me.orderId == null || me.orderId == "") {
            return;
        }
        //日志
        var request = {};
        request.id = me.orderId;
        numTotle = $(".chat-line").length
        $.ajax({
            type: "GET",
            url: baseServiceUrl + "services/chorder/getLogByChId?t=" + new Date().getTime(),
            data: request,
            contentType: "application/json; charset=UTF-8",
            dataType: "json",
            success: function(resp) {
                var code = resp.responseStatus.code;
                if (code == "00") {
                    var orderLogList = resp.orderLogList;
                    var htmlStr = "";
                    for (var i = 0; i < orderLogList.length; i++) {
                        var order = orderLogList[i];
                        // if (order.log == cancelOrderLog) {
                        //     alert(order.log);
                        //     window.location.reload();
                        // }
                        // if(order.type==0){
                        //     //$("#divMsg").append("<p>"+orderLogList[i].log+"</p>");
                        //
                        // }
                        // else if(order.type==1){
                        //     $("#spanShRole").html("收货角色："+order.roleName+"【"+order.level+"】 级");
                        //     $("#shRoleName").html("收货角色（级别）<br/>"+order.roleName +"【"+order.level+"】 级");
                        //     $("#canDealCount").html("本角色可交易（万金币）<br/>"+order.count);
                        // }
                        // else if(order.type==2){
                        //     $("#address").html("附魔师等级<br/><img src=\""+order.imgPath+"\"/>");
                        // }
                        // else if(order.type==3){
                        //     $("#fmLevel").html("交易地点<br/><img src=\""+order.imgPath+"\"/>");
                        // }

                        htmlStr += "<div class='chat-line robot-chat clearfix'><div class='avatar avatar1 fl'></div><div class='chat-content fl'><i class='icon-arrow'></i><p><span class='time'>" + new Date(order.createTime).Format("yyyy-MM-dd hh:mm:ss") + "</span>&nbsp;" + orderLogList[i].log + "</p></div></div>";



                    }

                    $("#divMsg").html(me.msg + htmlStr);


                      if (numTotle!=$(".chat-line").length) {
                          $('#divMsg').scrollTop(999999);
                      }

                    //间隔一段时间，初始化获取页面数据
                    me.init(false);
                }
            }
        });
    }
}


//交易类型
var shStatus = {
    "Waiting": 1, // 等待交易
    "Queue": 2, // 排队中
    "Dealing": 3, //交易中
    "Complete": 4, //交易完成
    "PartComplete": 5, //部分完单
    "Cancle": 6, //撤单
    "HumanOp": 7 //需人工介入
}
var cancelOrderLog = "收货商收货角色异常，已进行撤单处理，请您重新下单。";
//判断收货数量的合法性
function checkValidity(count, maxCount) {
    var num = /^[1-9][0-9]*$/;
    var result = num.test(count);
    if (result) {
        var limitCount = count - maxCount;
        if (limitCount <= 0) {
            return true;
        }
    }
    return false;
}
