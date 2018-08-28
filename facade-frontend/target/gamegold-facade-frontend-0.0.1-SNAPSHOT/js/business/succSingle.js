var QQNumber = ""
var endReason = ""
var mainOrderId = ""
$(function(){
  var orderId=$.trim(getUrlParam("orderId"));
  if(orderId==""){
      alert("缺少必要的参数！");
      return;
  }
  var request = {};
  request.orderId=orderId;
  $.ajax({
      type: "GET",
      url: baseServiceUrl + "services/chorder/selectOrderById?t="+new Date().getTime(),
      data: request,
      contentType: "application/json; charset=UTF-8",
      dataType: "json",
      success: function (resp) {
          var code = resp.responseStatus.code;
          if (code == "00") {
              QQNumber = resp.deliveryOrder.serviceQq;
              endReason = resp.endReason;
              mainOrderId =  resp.mainOrderId;
              isdeliveryOrder(resp.deliveryOrder)
          }
      }
  });
})
function isdeliveryOrder(deliveryOrder){
  if(deliveryOrder.status=="3"){
    if(endReason==undefined){
      endReason=""
    }
    if(deliveryOrder.appealReason==undefined){
      deliveryOrder.appealReason=""
    }
    var html = '<div class="title_neworder">'+
    '<span class="midd_newrec"></span>'+
  			 '<em>当前订单为用户申诉补单, 订单确认后由客服操作完成 </em>'+
  			 '<div class="rc-tag-qq">'+
         '<a target="_blank" id="qq-link" href="tencent://message/?uin='+QQNumber+'&amp;Site=m5173&amp;Menu=yes">'+
         '<img id="qq-img" border="0" src="http://html.5173.com/dlc2c/images/global_v2/seller/qqinline.gif?p=2:'+QQNumber+':1" alt="点击这里给我发消息" title="点击这里给我发消息">'+
         '</a></div>'+
  		 '</div>'+
  		 '<div class="detail_neworder">'+
  			 '<p class="rectop">申请补单详情</p>'+
  			 '<dl>'+
  				 '<dt>游戏/区/服：</dt><dd>'+deliveryOrder.gameName+'/'+deliveryOrder.region+'/'+deliveryOrder.server+'</dd>'+
  				 '<dt>订单编号：</dt><dd>【申诉】'+deliveryOrder.orderId+'</dd>'+
  				 '<dt>创建时间：</dt><dd>'+new Date((deliveryOrder.createTime)).Format("yyyy-MM-dd hh:mm:ss")+'</dd>'+
  				 '<dt>单价：</dt><dd>'+deliveryOrder.price+'元/'+deliveryOrder.moneyName+'</dd>'+
  				 '<dt>订单数量：</dt><dd>'+deliveryOrder.count+'万</dd>'+
  				 '<dt>订单金额：</dt><dd>'+deliveryOrder.amount+'元</dd>'+
  				 '<dt>关联订单：</dt><dd>'+mainOrderId+'_'+deliveryOrder.appealOrder+'</dd>'+
  				 '<dt>订单来源：</dt><dd>申诉补单</dd>'+
  				 '<dt>申诉原因：</dt><dd>'+deliveryOrder.appealReason+'</dd>'+
  				 '<dt>状态：</dt><dd>'+deliveryOrder.orderStatusStr+'</dd>'+
  				 '<dt>备注：</dt><dd>'+endReason+'</dd>'+
  			 '</dl>'+
  		 '</div>'
  }else if(deliveryOrder.status=="4"||deliveryOrder.status=="5"){
    if(endReason==undefined){
      endReason=""
    }
    if(deliveryOrder.appealReason==undefined){
      deliveryOrder.appealReason=""
    }
    var html = '<div class="title_neworder">'+
    '<span class="succ_newrec"></span>'+
  			 '<em>当前订单为用户申诉补单，经买卖双方确认，交易完成 </em>'+
  			 '<div class="rc-tag-qq">'+
         '<a target="_blank" id="qq-link" href="tencent://message/?uin='+QQNumber+'&amp;Site=m5173&amp;Menu=yes">'+
         '<img id="qq-img" border="0" src="http://html.5173.com/dlc2c/images/global_v2/seller/qqinline.gif?p=2:'+QQNumber+':1" alt="点击这里给我发消息" title="点击这里给我发消息">'+
         '</a></div>'+
  		 '</div>'+
  		 '<div class="detail_neworder">'+
  			 '<p class="rectop">申请补单详情</p>'+
  			 '<dl>'+
         '<dt>游戏/区/服：</dt><dd>'+deliveryOrder.gameName+'/'+deliveryOrder.region+'/'+deliveryOrder.server+'</dd>'+
         '<dt>订单编号：</dt><dd>【申诉】'+deliveryOrder.orderId+'</dd>'+
         '<dt>创建时间：</dt><dd>'+new Date((deliveryOrder.createTime)).Format("yyyy-MM-dd hh:mm:ss")+'</dd>'+
         '<dt>单价：</dt><dd>'+deliveryOrder.price+'元/'+deliveryOrder.moneyName+'</dd>'+
         '<dt>订单数量：</dt><dd>'+deliveryOrder.count+'万</dd>'+
         '<dt>订单金额：</dt><dd>'+deliveryOrder.amount+'元</dd>'+
         '<dt>交易数量：</dt><dd>'+deliveryOrder.realCount+'</dd>'+
         '<dt>交易金额：</dt><dd>'+deliveryOrder.realAmount+'</dd>'+
          '<dt>关联订单：</dt><dd>'+mainOrderId+'_'+deliveryOrder.appealOrder+'</dd>'+
         '<dt>订单来源：</dt><dd>申诉补单</dd>'+
         '<dt>申诉原因：</dt><dd>'+deliveryOrder.appealReason+'</dd>'+
         '<dt>状态：</dt><dd>'+deliveryOrder.orderStatusStr+'</dd>'+
         '<dt>备注：</dt><dd>'+endReason+'</dd>'+
  			 '</dl>'+
  		 '</div>'
  }else if(deliveryOrder.status=="6"){
    if(endReason==undefined){
      endReason=""
    }
    if(deliveryOrder.appealReason==undefined){
      deliveryOrder.appealReason=""
    }
    var html = '<div class="title_neworder">'+
    '<span class="failed_newrec"></span>'+
  			 '<em>当前订单为用户申诉补单，经买卖双方确认，交易取消 </em>'+
  			 '<div class="rc-tag-qq">'+
         '<a target="_blank" id="qq-link" href="tencent://message/?uin='+QQNumber+'&amp;Site=m5173&amp;Menu=yes">'+
         '<img id="qq-img" border="0" src="http://html.5173.com/dlc2c/images/global_v2/seller/qqinline.gif?p=2:'+QQNumber+':1" alt="点击这里给我发消息" title="点击这里给我发消息">'+
         '</a></div>'+
  		 '</div>'+
  		 '<div class="detail_neworder">'+
  			 '<p class="rectop">申请补单详情</p>'+
  			 '<dl>'+
         '<dt>游戏/区/服：</dt><dd>'+deliveryOrder.gameName+'/'+deliveryOrder.region+'/'+deliveryOrder.server+'</dd>'+
         '<dt>订单编号：</dt><dd>【申诉】'+deliveryOrder.orderId+'</dd>'+
         '<dt>创建时间：</dt><dd>'+new Date((deliveryOrder.createTime)).Format("yyyy-MM-dd hh:mm:ss")+'</dd>'+
         '<dt>单价：</dt><dd>'+deliveryOrder.price+'元/'+deliveryOrder.moneyName+'</dd>'+
         '<dt>订单数量：</dt><dd>'+deliveryOrder.count+'万</dd>'+
         '<dt>订单金额：</dt><dd>'+deliveryOrder.amount+'元</dd>'+
         '<dt>交易数量：</dt><dd>'+deliveryOrder.realCount+'</dd>'+
         '<dt>交易金额：</dt><dd>'+deliveryOrder.realAmount+'</dd>'+
          '<dt>关联订单：</dt><dd>'+mainOrderId+'_'+deliveryOrder.appealOrder+'</dd>'+
         '<dt>订单来源：</dt><dd>申诉补单</dd>'+
         '<dt>申诉原因：</dt><dd>'+deliveryOrder.appealReason+'</dd>'+
         '<dt>状态：</dt><dd>'+deliveryOrder.orderStatusStr+'</dd>'+
         '<dt>备注：</dt><dd>'+endReason+'</dd>'+
  			 '</dl>'+
  		 '</div>'
  }else{
    if(endReason==undefined){
      endReason=""
    }
    if(deliveryOrder.appealReason==undefined){
      deliveryOrder.appealReason=""
    }
    var html = '<div class="title_neworder">'+
    '<span class="midd_newrec"></span>'+
  			 '<em>当前订单为用户申诉补单, 订单确认后由客服操作完成 </em>'+
  			 '<div class="rc-tag-qq">'+
         '<a target="_blank" id="qq-link" href="tencent://message/?uin='+QQNumber+'&amp;Site=m5173&amp;Menu=yes">'+
         '<img id="qq-img" border="0" src="http://html.5173.com/dlc2c/images/global_v2/seller/qqinline.gif?p=2:'+QQNumber+':1" alt="点击这里给我发消息" title="点击这里给我发消息">'+
         '</a></div>'+
  		 '</div>'+
  		 '<div class="detail_neworder">'+
  			 '<p class="rectop">申请补单详情</p>'+
  			 '<dl>'+
  				 '<dt>游戏/区/服：</dt><dd>'+deliveryOrder.gameName+'/'+deliveryOrder.region+'/'+deliveryOrder.server+'</dd>'+
  				 '<dt>订单编号：</dt><dd>【申诉】'+deliveryOrder.orderId+'</dd>'+
  				 '<dt>创建时间：</dt><dd>'+new Date((deliveryOrder.createTime)).Format("yyyy-MM-dd hh:mm:ss")+'</dd>'+
  				 '<dt>单价：</dt><dd>'+deliveryOrder.price+'元/'+deliveryOrder.moneyName+'</dd>'+
  				 '<dt>订单数量：</dt><dd>'+deliveryOrder.count+'万</dd>'+
  				 '<dt>订单金额：</dt><dd>'+deliveryOrder.amount+'元</dd>'+
  				  '<dt>关联订单：</dt><dd>'+mainOrderId+'_'+deliveryOrder.appealOrder+'</dd>'+
  				 '<dt>订单来源：</dt><dd>申诉补单</dd>'+
  				 '<dt>申诉原因：</dt><dd>'+deliveryOrder.appealReason+'</dd>'+
  				 '<dt>状态：</dt><dd>'+deliveryOrder.orderStatusStr+'</dd>'+
  				 '<dt>备注：</dt><dd>'+endReason+'</dd>'+
  			 '</dl>'+
  		 '</div>'
  }

  $('.succ_neworder').html(html)
}
