$(function () {
    if($.trim(getUrlParam("id"))==""){
        // alert("url出现错误！");
        window.open(document.referrer, "_self");
        return;
    }

    me.init();

    $("#chsl").keyup(function() {
        var nums = $(this).val();
        if(!isNumber(nums)){
            alert('请输入正确的数量');
            $(this).val(0);
        }
    });
    $("#jyfs").change(function(){  
        if($(this).find('option:selected').attr('name')==3){
            $(this).next().text('邮寄交易出货商承担5%税费，出货完成，请小退游戏')
        }else{
            $(this).next().text('') 
        }
    })  
});

function showLoading()
{
    var screenWidth = $(window).width();//当前窗口宽度
    var screenHeight = $(window).height();//当前窗口高度

    $("#divLoading").css({"display":"","position": "fixed","background": "#000","z-index": "10000","-moz-opacity": "0.5","opacity":".50","filter": "alpha(opacity=50)","width":screenWidth,"height":screenHeight});
    $("#loadingInfo").show();
    $("#divLoading").show();
}

var me={
    deliveryOrder:null,
    init:function(){
        var me = this;
        me.initPage();

        //立刻出货
        $("#formts").validate({
            submitHandler: function() {
                //验证通过后 的js代码写在这里
                me.createOrder();
            }
        });

        $('#chsl').bind('input propertychange', me.changeCount);
    },
    initPage:function(){
        //初始化获取商家信息和采购信息
        var request = {};
        request.id=$.trim(getUrlParam("id"));
        $.ajax({
            type: "GET",
            url: baseServiceUrl + "services/chorder/selectPurchaseOrderAndCgDataById?t="+new Date().getTime(),
            data: request,
            contentType: "application/json; charset=UTF-8",
            dataType: "json",
            success: function (resp) {
                var code = resp.responseStatus.code;
                if (code == "00") {
                    me.deliveryOrder=resp.purchaseOrder;
                    me.append();

                }
            }
        });
    },
    append:function(){
        if(me.deliveryOrder!=null){
            if(me.deliveryOrder.isOnline==undefined||!me.deliveryOrder.isOnline){
                alert("该商品已下架！");
                window.open(document.referrer, "_self");
                return;
            }
            var deliverOrder=me.deliveryOrder;

            $("#spanNav").html("<a href=\"#\">"+deliverOrder.gameName+"</a> &gt; "+deliverOrder.region+" &gt; "+deliverOrder.server+"&gt; 商铺详情");

            //收购信息
            $("#gameInfo").html(deliverOrder.gameName+"/"+deliverOrder.region+"/"+deliverOrder.server)
            $("#count").html(deliverOrder.count);
            $("#price").html(deliverOrder.price);
            if (deliverOrder.startTime != null && deliverOrder.endTime != null) {
                $("#dealTime").html(deliverOrder.startTime + ":00 — " + deliverOrder.endTime + ":00");
            }
            var date = new Date(deliverOrder.updateTime);
            //$("#updateTime").html(me.formatDate(date));
            $("#updateTime").html(date.Format("yyyy-MM-dd hh:mm:ss"));

            //商家信息
            var shopName=deliverOrder.shopName==null?"":deliverOrder.shopName;
            var cjl=deliverOrder.cjl==null?0:deliverOrder.cjl;
            var pjys=deliverOrder.pjys==null?0:deliverOrder.pjys;
            var tradingVolume=deliverOrder.tradingVolume==null?0:deliverOrder.tradingVolume;
            var tradeAddress=deliverOrder.tradeAddress==null?"":deliverOrder.tradeAddress;
            var moneyName = deliverOrder.moneyName==null?"":deliverOrder.moneyName;
            $("#shopName").html(shopName);
            $("#cjl").html(parseFloat(cjl)*100+"%");
            $("#pjys").html(pjys+"分钟");
            $("#tradingVolume").html(tradingVolume+"笔");
            $("#unit_name").html(moneyName);
            //设置灰色提示值
            $("#chsl").attr("placeholder", "输入最大值" + deliverOrder.count);
            if(deliverOrder.minCount==deliverOrder.count){
                $("#lblCountInfo").html("（收货商收货数量要求等于"+deliverOrder.count+"）");
            }else if(deliverOrder.minCount<deliverOrder.count){
                $("#lblCountInfo").html("（收货商收货数量要求大于等于"+deliverOrder.minCount+",小于等于"+deliverOrder.count+"）");
            }
            //me.getUnitName(deliverOrder);
            me.getTradeType(deliverOrder);
            me.getShGameConfig(deliverOrder);
            var request = {};
            request.gameName=$.trim(deliverOrder.gameName);
            $.ajax({
                type: "GET",
                url: baseServiceUrl + "services/addressConfig/getConfigByGameName?t="+new Date().getTime(),
                data: request,
                contentType: "application/json; charset=UTF-8",
                dataType: "json",
                success: function (resp) {
                    var code = resp.responseStatus.code;
                    console.log(resp)
                    if (code == "00") {
                        var addressList=resp.addressList;
                        var htmlStr="";
                        if(addressList!=null){
                            for(i=0;i<addressList.length;i++){
                                var addressInfo=addressList[i];
                                htmlStr+="<option>"+addressInfo.tradeAddress+"</option>";
                            }
                        }
                        $("#jydz").html(htmlStr);


                    }
                }
            });
        }
    },
    createOrder:function(){
        //出货前的判断
        if($.trim(getUrlParam("id"))==""){
            alert("url出现错误！");
            return;
        }
        var roleName=$("#jsmc").val();
        if(roleName.length>40){
            alert("角色名称不能大于40个字符！");
            return;
        }
        var count=$.trim($("#chsl").val());
        if(me.deliveryOrder!=null){
            if(count<me.deliveryOrder.minCount||count>me.deliveryOrder.count){
                alert("出货数量不符合收货数量范围要求！");
                return;
            }
        }

        //re = /^1\d{10}$/;
        re=/^(13[0-9]|14[5-9]|15[012356789]|16[0-9]|17[0-8]|18[0-9]|19[0-9])[0-9]{8}$/
        var tel = $("#telcode").val();
        if(!re.test(tel)){
            $("#telcode-error").attr("style","display: inline-block;");
            $("#telcode-error").html("您的手机号码有误，请输入正确的手机号码");
            return;
        }

        showLoading();

        //生成出货订单
        var request = {};
        request.cgId=$.trim(getUrlParam("id"));
        request.roleName= $.trim(roleName);//交易角色
        request.count= $.trim(count);//出货数量
        request.tradeType=$("#jyfs option:selected").val();
        request.tradeTypeName=$("#jyfs option:selected").text();
        request.address= $.trim($("#jydz").val());//交易地址
        request.phone= $.trim($("#telcode").val());//手机号
        request.qq= $.trim($("#qq").val());//qq
        request.tradeLogo = $("#jyfs option:selected").attr("name");
        request.deliveryType=2;


        $.ajax({
            type: "POST",
            url: baseServiceUrl + "services/chorder/createDeliveryOrder",
            data: $.toJSON(request),
            contentType: "application/json; charset=UTF-8",
            dataType: "json",
            success: function (resp) {
                var code = resp.responseStatus.code;
                if (code == "00") {
                    window.open(baseHtmlUrl + "deliverySubOrderList.html?orderId="+resp.orderId+"&t="+(new Date()).getTime(), "_self");
                }else{
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
    changeCount:function(){
        //联动出货数量以及单价，生成交易金额
        var count=$.trim($("#chsl").val());
        var price= $.trim($("#price").html());
        var totalAccount=count*price;
        if (isNaN(totalAccount)){
            $("#jyje").val(0);
        }
        else{
            $("#jyje").val(toDecimal2(totalAccount));
        }
    },
    getTradeType:function(purchaserData){
        // var request = {};
        // request.account=$.trim(deliverOrder.buyerAccount);
        // console.log(deliverOrder.buyerAccount);
        // $.ajax({
        //     type: "GET",
        //     url: baseServiceUrl + "services/manualShOrder/selectOrderByAccount?t="+new Date().getTime(),
        //     data: request,
        //     contentType: "application/json; charset=UTF-8",
        //     dataType: "json",
        //     success: function (resp) {
        //         if (resp.purchaserData != null) {
                    //var purchaserData=deliverOrder.purchaseOrder;
                    // var tradeType = purchaserData.tradeType;
                    // var tradeTypeName =  purchaserData.tradeTypeName;
                    if(purchaserData!=null&&purchaserData !=null){
                        var tradeTypeName=purchaserData.tradeTypeName;
                        var tradeTypeId=purchaserData.tradeTypeId;
                        var tradeLogoId=purchaserData.tradeLogo;
                        //tradeType = purchaserData.tradeType.split(",");
                        var tradeTypeNameArr =  tradeTypeName.split(",");
                        var tradeTypeIdArr=tradeTypeId.split(",");
                        var trLogoName=tradeLogoId.split(",");
                        console.log("1231");
                        console.log(tradeTypeName);
                        for(var i=0;i<tradeTypeNameArr.length;i++) {
                            $("#jyfs").append( "<option name=\""+trLogoName[i]+"\" value=\""+tradeTypeIdArr[i]+"\">"+tradeTypeNameArr[i]+"</option>" );
                        }
                        if(tradeTypeNameArr.length&&trLogoName[0]==3){
                            $('#jyfs').next().text('邮寄交易出货商承担5%税费，出货完成，请小退游戏')
                        }
                    }
        //         }
        //     }
        // });
    },
    getShGameConfig:function (deliverOrder) {
        var request = {};
        request.gameName=$.trim(deliverOrder.gameName);
        console.log(deliverOrder.gameName);
        $.ajax({
            type: "GET",
            url: baseServiceUrl + "services/shGameConfig/getConfigByGameName?t="+new Date().getTime(),
            data: request,
            contentType: "application/json; charset=UTF-8",
            dataType: "json",
            success: function (resp) {
                if (resp.shGameConfig != null) {
                    var unitName = resp.shGameConfig.unitName;;
                    $("#unit_name").html(unitName+"*");
                    $(".unitName").html(unitName);
                    }
                }

        });
    }
}
