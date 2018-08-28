var deliveryOrder = {};
var subOrderList = {};
var leastAmount = "";
$(function(){
  var request = {};
  findSubOrderById($.trim(getUrlParam("id")))
  $('#SellOutNum').bind('input propertychange', function() {
      console.log($('#SellOutNum').val(),deliveryOrder.price)
      if($('#SellOutNum').val()>999999999){
        $('#SellOutNum').val($('#SellOutNum').val().substring(0, $('#SellOutNum').val().length - 1))
      }
      if($('#SellOutNum').val().indexOf(".")>0){
        $('#SellOutNum').val($('#SellOutNum').val().substring(0, $('#SellOutNum').val().length - 1))
      }
      var re = /^\+?[1-9][0-9]*$/;
      if(!re.test($('#SellOutNum').val())){
        $('#SellOutNum').val($('#SellOutNum').val().substring(0, $('#SellOutNum').val().length - 1))
      }
      $('.refundAmount').html(($('#SellOutNum').val()*deliveryOrder.price).toFixed(2)+"元")
  });

})
var tradingArr=['当面交易','附魔交易','邮寄交易','黑市交易','拍卖交易']
function tradingMode(arr,state){
  return arr[state-1];
}
function findSubOrderById(id){
  var request = {};
  request.id =id
  //查询子订单
  $.ajax({
      type: "GET",
      url: baseServiceUrl + "services/deliveryOrder/findSubOrderById",
      data:request ,
      contentType: "application/json; charset=UTF-8",
      dataType: "json",
      success: function (resp) {
          var code = resp.responseStatus.code;
          subOrderList = resp.subOrderList[0];
          deliveryOrder = resp.deliveryOrder;
          leastAmount = resp.leastAmount;
          if (code == "00") {
              initSubOrderList(subOrderList);
              initPageDataSale(deliveryOrder);
          }
          else if (code == "21016") {
              history.back();
          }
      }
  });
}

function initPageDataSale(deliveryOrder){
  console.log(deliveryOrder)
  $(".order_detail").html(
  '<h2>订单摘要</h2><span><em class="left_code">游戏/区/服:</em><em class="right_code">'+deliveryOrder.gameName+'/'+deliveryOrder.region+'/'+deliveryOrder.server+'</em></span>'+
  '<span><em class="left_code">订单单价:</em><em class="right_code color-fab">'+deliveryOrder.price+'元/'+deliveryOrder.moneyName+'</em></span>'+
  '<span><em class="left_code">订单金额:</em><em class="right_code">'+deliveryOrder.amount.toFixed(2)+'元</em></span>'+
  '<span><em class="left_code">交易模式:</em><em class="right_code">'+tradingMode(tradingArr,deliveryOrder.tradeLogo)+'</em></span>'+
  '<span><em class="left_code">交易地点:</em><em class="right_code">无</em></span>'+
  '<span><em class="left_code">交易状态:</em><em class="right_code">'+deliveryOrder.orderStatusStr+'</em></span>'+
  '<span><em class="left_code">交易类目:</em><em class="right_code">'+deliveryOrder.goodsTypeName+'</em></span>'+
  '<span><em class="left_code">主订单:</em><em class="right_code">'+deliveryOrder.orderId+'</em></span>'+
  '<span><em class="left_code">下单时间:</em><em class="right_code">'+new Date((deliveryOrder.createTime)).Format("yyyy-MM-dd hh:mm:ss")+'</em></span>'+
  '<span><em class="left_code">结单时间:</em><em class="right_code">'+new Date((deliveryOrder.tradeEndTime)).Format("yyyy-MM-dd hh:mm:ss")+'</em></span>')
    // if(deliveryOrder.tradeEndTime==undefined){
    //
    // }
}
//右边列表填充
function initSubOrderList(subOrderList){
  $("#subOrderList dl dd").eq(0).html(subOrderList.id)
  $("#subOrderList dl dd").eq(1).html(subOrderList.gameRole)
  $(".bord_tisi").html("最少"+leastAmount+"万金")
}
//弹出框隐藏
function isClose(){
  $('.sure_after').hide()
}
function isSrue(){
  $('.sure_after').hide()
  request = {"cgId":"779","roleName":"1231321","count":"1000","address":"cece","phone":"15555555555","qq":"123456","deliveryType":1,"appealOrder":2101,"appealReason":"1231321"}
  console.log(deliveryOrder)
  request.cgId = deliveryOrder.cgId
  request.roleName = subOrderList.gameRole
  request.count = $('#SellOutNum').val()
  request.address = "邮寄交易"
  request.phone = deliveryOrder.buyerPhone
  request.qq = deliveryOrder.qq
  request.deliveryType = deliveryOrder.deliveryType
  request.appealOrder = subOrderList.id
  request.appealReason = $('#selectResult').find("option:selected").val()
  $.ajax({
      type: "POST",
      url: baseServiceUrl + "services/chorder/createAppealOrder?t=" + new Date().getTime(),
      data: JSON.stringify(request),
      contentType: "application/json; charset=UTF-8",
      dataType: "json",
      success: function (resp) {
          var code = resp.responseStatus.code;
          // window.location.href="single_result.html"
          if (code == "00") {
              window.location.href="single_result.html?orderId="+resp.deliveryOrder.orderId
              // me.initPageData(isFirstReques);
          }
          else if (code == "21016") {
              history.back();
          }else{
            alert(resp.responseStatus.message)
          }
      }
  });
}
function isReturnHtml(){
  window.location.href="orderResult1.html?orderId="+deliveryOrder.orderId
}
function isClickResult(){
    $(".close_newde").hide()
}
function single_sellBack(){
  if($('#selectResult').find("option:selected").val()=="请选择售后原因"){
    $(".close_newde").show()
    return false
  }
  if($("#SellOutNum").val()==""){
    alert("请填写售后游戏币数量")
    return false
  }
  if($("#SellOutNum").val()<leastAmount){
    alert("售后游戏币数量最少为"+leastAmount+"万金")
    return false
  }
  if(Number($(".refundAmount").html().replace("元",""))<0||Number($(".refundAmount").html().replace("元",""))==0){
    alert("退款金额必须大于0")
    return false
  }
  $('.reasom02').eq(0).html($('#selectResult').find("option:selected").val())
  $('.reasom02').eq(1).html($(".refundAmount").html()+"（"+$("#SellOutNum").val()+"万金）")
  $('.sure_after').show()

}
