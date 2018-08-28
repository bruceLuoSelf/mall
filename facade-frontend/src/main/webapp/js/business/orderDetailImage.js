$(function () {
    var img = "";
    img = $.trim(getUrlParam("url"));
    console.log(img.split(","));
    var attr = [];
    var htmlStr = ""
    attr = img.split(",");
    for(var j in attr){
      htmlStr+="<img src="+attr[j]+" />"
    }
    $("#imgOrder").html(htmlStr)
});
