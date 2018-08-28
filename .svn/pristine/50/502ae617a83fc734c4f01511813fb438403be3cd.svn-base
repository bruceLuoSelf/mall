var id = "";
var appealOrderStatus = "";
var myOrder = "";
var endReason = ""
var mainOrderId = ""
$(function(){
  selectOrderById()
  // $(".receive_new").click(function(){
  //   $('.sure_after2').show()
  // })
})
function isCloseResult(){
  $('.sure_after').hide()
}
//获取订单详情
function selectOrderById(){
  var orderId = $.trim(getUrlParam("orderId"));
  var request = {};
  request.orderId = orderId;
  $.ajax({
      type: "GET",
      url: baseServiceUrl + "services/chorder/selectOrderById?t=" + new Date().getTime(),
      data: request,
      contentType: "application/json; charset=UTF-8",
      dataType: "json",
      success: function (resp) {
          var code = resp.responseStatus.code;
          if (code == "00") {
            id = resp.deliveryOrder.id
            endReason = resp.endReason;
            mainOrderId =  resp.mainOrderId;
            if(endReason==undefined){
                endReason=""
            }
            appealOrderStatus = resp.appealOrderStatus;
            myOrder = resp.deliveryOrder.orderId
            selectOrderByDetail(resp.deliveryOrder)
          }
          else if (code == "21016") {
              history.back();
          }
      }
  });
}
//获取订单详情填充页面
function selectOrderByDetail(deliveryOrder){
  var html = '<div class="bord_new">'+
    '<div class="new_orderservice">'+
      '<div class="common"><span class="one_order"></span>选择申请售后的订单 &gt;</div>'+
      '<div class="common"><span class="two_order"></span>填写售后原因、金额 &gt;</div>'+
      '<div class="common active"><span class="three_order"></span>售后结果</div>'+
    '</div></div><div class="detail_neworder"><div class="detail_row border-bottom">'
      if(deliveryOrder.status==3){
          html +='<p class="title_list">您已成功申请售后，等待客服处理<a href="succ_single.html?orderId='+deliveryOrder.orderId+'">&lt; 返回订单结果页</a></p>'
      }else if(deliveryOrder.status==6){
          html +='<p class="title_list">您的订单已撤销申请售后<a href="succ_single.html?orderId='+deliveryOrder.orderId+'">&lt; 返回订单结果页</a></p>'
      }else if(deliveryOrder.status==4){
        html +='<p class="title_list">您的售后问题已处理完毕<a href="succ_single.html?orderId='+deliveryOrder.orderId+'">&lt; 返回订单结果页</a></p>'
      }else if(deliveryOrder.status==5){
        html +='<p class="title_list">您的售后问题已处理完毕<a href="succ_single.html?orderId='+deliveryOrder.orderId+'">&lt; 返回订单结果页</a></p>'
      }else{
          html +='<p class="title_list">您已成功申请售后，等待客服处理<a href="succ_single.html?orderId='+deliveryOrder.orderId+'">&lt; 返回订单结果页</a></p>'
      }


      html +='<div class="sigle_cont">为方便售后客服联系，请您保持联系方式畅通。售后客服QQ：'+deliveryOrder.serviceQq+'<div class="rc-tag-qq">'+
      '<a target="_blank" id="qq-link" href="tencent://message/?uin='+deliveryOrder.serviceQq+'&amp;Site=m5173&amp;Menu=yes">'+
      '<img id="qq-img" border="0" src="http://html.5173.com/dlc2c/images/global_v2/seller/qqinline.gif?p=2:554475156:1" alt="点击这里给我发消息" title="点击这里给我发消息"></a>'+
      '</div>'+
      '</div>'+
    '</div>'+
    '<div class="fl mt-10">'
    if(deliveryOrder.status==3){
      html+='<dl>'+
          '<dt>申请时间：</dt><dd>'+new Date((deliveryOrder.createTime)).Format("yyyy-MM-dd hh:mm:ss")+'</dd>'+
          '<dt>售后补单编号：</dt><dd>'+deliveryOrder.orderId+'</dd>'+
          '<dt>关联订单：</dt><dd>'+mainOrderId+'_'+deliveryOrder.appealOrder+'</dd>'+
          '<dt>申请售后金额：</dt><dd>'+deliveryOrder.amount+'元('+deliveryOrder.count+'万金)</dd>'+
          '<dt>处理进度：</dt><dd>'+deliveryOrder.orderStatusStr+'</dd>'+
          '<dt>备注：</dt><dd>'+endReason+'</dd>'+
        '</dl>'
    }else if(deliveryOrder.status==6){
      html+='<dl>'+
          // '<dt>申诉编号：</dt><dd>'+21+'</dd>'+
          '<dt>售后补单编号：</dt><dd>'+deliveryOrder.orderId+'</dd>'+
          '<dt>关联订单：</dt><dd>'+mainOrderId+'_'+deliveryOrder.appealOrder+'</dd>'+
          '<dt>申请售后金额：</dt><dd>'+deliveryOrder.amount+'元('+deliveryOrder.count+'万金)</dd>'+
          '<dt>处理进度：</dt><dd>'+deliveryOrder.orderStatusStr+'</dd>'+
          '<dt>撤单时间：</dt><dd>'+new Date((deliveryOrder.tradeEndTime)).Format("yyyy-MM-dd hh:mm:ss")+'</dd>'+
          '<dt>备注：</dt><dd>'+endReason+'</dd>'+
        '</dl>'
    }else if(deliveryOrder.status==4||deliveryOrder.status==5){
      html+='<dl>'+
          // '<dt>申诉编号：</dt><dd>'+21+'</dd>'+
          '<dt>售后补单编号：</dt><dd>'+deliveryOrder.orderId+'</dd>'+
          '<dt>关联订单：</dt><dd>'+mainOrderId+'_'+deliveryOrder.appealOrder+'</dd>'+
          '<dt>申请售后金额：</dt><dd>'+deliveryOrder.amount+'元('+deliveryOrder.count+'万金)</dd>'+
          '<dt>处理进度：</dt><dd>'+deliveryOrder.orderStatusStr+'</dd>'+
          '<dt>完成时间：</dt><dd>'+new Date((deliveryOrder.tradeEndTime)).Format("yyyy-MM-dd hh:mm:ss")+'</dd>'+
          '<dt>备注：</dt><dd>'+endReason+'</dd>'+
        '</dl>'
    }else{
      html+='<dl>'+
          '<dt>申请时间：</dt><dd>'+new Date((deliveryOrder.createTime)).Format("yyyy-MM-dd hh:mm:ss")+'</dd>'+
          '<dt>售后补单编号：</dt><dd>'+deliveryOrder.orderId+'</dd>'+
          '<dt>关联订单：</dt><dd>'+mainOrderId+'_'+deliveryOrder.appealOrder+'</dd>'+
          '<dt>申请售后金额：</dt><dd>'+deliveryOrder.amount+'元('+deliveryOrder.count+'万金)</dd>'+
          '<dt>处理进度：</dt><dd>'+deliveryOrder.orderStatusStr+'</dd>'+
          '<dt>备注：</dt><dd>'+endReason+'</dd>'+
        '</dl>'
    }


      html+='<div class="clearfix"></div>'+
      '<p class="new_submit">'

      // if(){
      //
      // }
      if(deliveryOrder.status==3){
        html +='<a class="receive_new" href="javascript:void(0)" onclick="isDeletedComplaint()">撤销申请</a>'+
          '<a class="return_new" href="succ_single.html?orderId='+deliveryOrder.orderId+'">返回</a></p>'+
          '<p class="new_submit">注：<em>售后申请撤销后无法再次申请</em></p>'+
        '</div></div><div class="clearfix"></div>';
      }else if(deliveryOrder.status==6){
        html += '<a class="norec_new" href="javascript:void(0)">已撤销</a>'+
          '<a class="return_new" href="succ_single.html?orderId='+deliveryOrder.orderId+'">返回</a></p>'+
          '<p class="new_submit">注：<em>售后申请撤销后无法再次申请</em></p>'+
        '</div></div><div class="clearfix"></div>';
      }else if(deliveryOrder.status==4||deliveryOrder.status==5){
        html += '<a class="return_new" href="succ_single.html?orderId='+deliveryOrder.orderId+'">返回</a></p>'+
          '<p class="new_submit">注：<em>售后申请撤销后无法再次申请</em></p>'+
        '</div></div><div class="clearfix"></div>';
      }else {
        html +='<a class="receive_new" href="javascript:void(0)" onclick="isDeletedComplaint()">撤销申请</a>'+
          '<a class="return_new" href="succ_single.html?orderId='+deliveryOrder.orderId+'">返回</a></p>'+
          '<p class="new_submit">注：<em>售后申请撤销后无法再次申请</em></p>'+
        '</div></div><div class="clearfix"></div>';
      }


  $('.succ_neworder').html(html)

}

//撤销申诉
function isDeletedComplaint(){
  $('.sure_after2').show()
  // selectOrderById()

}
//返回按钮
// function isReturnHtml(){
//   window.location.href="succ_single.html?orderId="+deliveryOrder.orderId
// }
//撤销申诉确认按钮
function isStrue(){
  var request = {};
  request.id = id;
  request.reason = 408;
  request.remark = "售后申请撤销";

  $.ajax({
      type: "GET",
      url: baseServiceUrl + "services/chorder/cancelOrder",
      data: request,
      contentType: "application/json; charset=UTF-8",
      dataType: "json",
      success: function (resp) {
          var code = resp.responseStatus.code;
          if (code == "00") {
              //跳转
              window.location.href="succ_single.html?orderId="+myOrder
              // window.open(baseHtmlUrl + "orderResult1.html?orderId=" + me.orderId + "&t=" + (new Date()).getTime(), "_self");
          }
      }
  });
}
//返回
// function isRetrun(orderId){
//   window.location.href= "succ_single.html?orderId="+orderId
// }
