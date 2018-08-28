var orderId_orderState;
var isAdd;
var productNum="";
var serviceAttitude="";
var deliverSpeed="";
$(function () {
    orderId_orderState = getUrlParam("orderId");
    isAdd=getUrlParam("isAdd");

    $('#evalArea').bind('keyup', function() {
        showCount();
    });

    init();
    $(".btnlink_b").click(function(){
        submit();
    });
});

function init(){
    var request = {};
    request.orderId = orderId_orderState;
    $.ajax({
        type: "POST",
        url: baseServiceUrl + "services/evaluate/queryByOrderId?t="+new Date().getTime(),
        data: $.toJSON(request),
        contentType: "application/json; charset=UTF-8",
        dataType: "json",
        success: function(resp) {
            var responseStatus = resp.responseStatus;
            var code = responseStatus.code;
            if (code == "00") {
                var startProductNum=0;
                var startServiceAttitude=0;
                var startDeliverSpeed=0;
                if(resp.totalCount>0){
                    startProductNum=resp.score1;
                    startServiceAttitude=resp.score2;
                    startDeliverSpeed=resp.score3;
                    $("#evalArea").val(resp.remark);
                }
                productNum=startProductNum;
                serviceAttitude=startServiceAttitude;
                deliverSpeed=startDeliverSpeed;
                setRaty(startProductNum,startServiceAttitude,startDeliverSpeed);
                showCount();
            }
            else{
                alert(responseStatus.message);
            }
        },
        error: function(resp) {
            console.log(resp);
        }
    });
}

function showCount(){
    var count=$('#evalArea').val().length;
    $("#normalText").html(count);
    if(count>300){
        $("#normalMsg").hide();
        $("#extraMsg").show();
        $("#extraText").html(count-300);
    }
    else{
        $("#normalMsg").show();
        $("#extraMsg").hide();
    }
}

function setRaty(startProductNum,startServiceAttitude,startDeliverSpeed){
    $('#productNum').raty({
        hintList:  ['严重不符，商品少货，未收到货，非常不满意', '部分不符，商品数量有出入还不承认，不满意', '基本符合，数量有误后及时补货，还算满意', '数量相符，扣除交易税后数量基本一致，还挺满意', '完全相符，实际到货数量一致，非常满意'],
        number: 5,
        start:startProductNum,
        onClick: function (score) {
            productNum=score;
        }
    });

    $('#serviceAttitude').raty({
        hintList:  ['客服态度很差，出了问题不解决，不理人', '客服明显拖时间，回答还有点不耐烦', '客服回复有点慢，但态度还行', '客服态度好，回复及时，有问题都给解决了', '艾玛，太赞了，又专业又快，还服务超好'],
        number: 5,
        start:startServiceAttitude,
        onClick: function (score) {
            serviceAttitude=score;
        }
    });

    $('#deliverSpeed').raty({
        hintList:  ['无力吐槽，再三提醒下才来交易，耽误我时间', '交易太慢了，催了几次才发货', '速度一般般，在指定时间内发货了', '发货及时，到货快', '给力，发货超快，很满意'],
        number: 5,
        start:startDeliverSpeed,
        onClick: function (score) {
            deliverSpeed=score;
        }
    });
}

function submit(){
    var remark=$("#evalArea").val();
    if(productNum==""||serviceAttitude==""||deliverSpeed==""||remark==""){
        alert("请填写完整");
        return;
    }
    var request = {};
    request.orderId = orderId_orderState;
    request.score1=productNum;
    request.score2=serviceAttitude;
    request.score3=deliverSpeed;
    request.remark= remark;
    var count=$('#evalArea').val().length;
    if(count>300){
        return;
    }

    var url="";
    if(isAdd==1){
        url=baseServiceUrl + "services/evaluate/addevaluate";
    }
    else{
        url=baseServiceUrl + "services/evaluate/modifyevaluate";
    }
    $.ajax({
        type: "POST",
        url: url,
        data: $.toJSON(request),
        contentType: "application/json; charset=UTF-8",
        dataType: "json",
        success: function(resp) {
            var responseStatus = resp.responseStatus;
            var code = responseStatus.code;
            if (code == "00") {
                window.location.href = baseServiceUrl + "commentsuccess.html";
            }
        },
        error: function(resp) {
            console.log(resp);
        }
    });
}