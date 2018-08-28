$(function () {
    $("#startTime").val(new Date().Format("yyyy-MM-dd"));
    $("#endTime").val(new Date().Format("yyyy-MM-dd"));

    me.init();

    $("#btnReturn").click(function(){
        window.open(baseHtmlUrl + "repositoryGameAccount.html?t="+(new Date()).getTime(), "_self");
    });

    //查询
    $("#btnSearch").click(function(){
        var startTime=new Date($("#startTime").val());
        var endTime=new Date($("#endTime").val());
        if(getDateDiff(endTime,startTime)>30){
            alert("订单时间查询间隔不能超过30天");
            return;
        }
        me.init();
    });
})

function getDateDiff(startDate,endDate){
    var startTime = new Date(startDate);
    var endTime = new Date(endDate);
    var days = (startTime - endTime)/(1000*60*60*24);
    return  days;
};

var me= {
    pageSize: 10,
    splitRepositoryLogList: null,
    init:function(){
        $.jqPaginator('#pagination1', {
            totalPages: 10,
            visiblePages: me.pageSize,
            currentPage: 1,
            onPageChange: function (num, type) {
                me.pageselectCallback(num);
            }
        });
    },
    pageselectCallback :function(page_index) {
        var request = {};
        request.gameName=$.trim(getUrlParam("gameName"));
        request.region=$.trim(getUrlParam("region"));
        request.server=$.trim(getUrlParam("server"));
        request.gameRace=$.trim(getUrlParam("gameRace"));
        request.gameAccount= $.trim(getUrlParam("gameAccount"));
        request.startTime=$("#startTime").val() ;
        request.endTime=$("#endTime").val();
        request.pageSize=me.pageSize;
        request.page=page_index;
        $.ajax({
            type: "GET",
            url: baseServiceUrl + "services/splitRepositoryLog/queryRepositoryLogList?t="+new Date().getTime(),
            data: request,
            contentType: "application/json; charset=UTF-8",
            dataType: "json",
            success: function (resp) {
                var code = resp.responseStatus.code;
                if (code == "00") {
                    me.splitRepositoryLogList=resp.splitRepositoryLogList;
                    if(me.splitRepositoryLogList!=null&&me.splitRepositoryLogList.length>0) {
                        $(".totalPage").html("共" + resp.totalCount + "笔，共" + resp.totalPage + "页");
                    }
                    else{
                        $(".totalPage").html("共0笔，共1页");
                    }
                    me.appendData();
                } else {
                    $(".totalPage").html("共0笔，共1页");
                    if(resp.responseStatus.code != "11"){
                        me.alert(resp.responseStatus.message);
                    }
                }

                //数据为0时，默认设置插件页码为1
                var count=1;
                if(resp.totalPage!=0){
                    count=resp.totalPage;
                }
                $('#pagination1').jqPaginator('option', {
                    totalPages: count
                });
            }
        });
    },
    appendData:function(){
        var htmlStr="";
        if(me.splitRepositoryLogList!=null){
            for(var i in me.splitRepositoryLogList){
                var splitRepositoryLog=me.splitRepositoryLogList[i];
                htmlStr+="<tr>";
                htmlStr+="<td width=\"90\">"+splitRepositoryLog.gameName+"</td>";
                htmlStr+="<td width=\"100\">"+splitRepositoryLog.region+"/"+splitRepositoryLog.server+"</td>";
                htmlStr+="<td width=\"90\">"+splitRepositoryLog.gameAccount+"</td>";
                htmlStr+="<td width=\"260\">"+splitRepositoryLog.log+"</td>";
                htmlStr+="<td width=\"130\">"+new Date((splitRepositoryLog.createTime)).Format("yyyy-MM-dd hh:mm:ss")+"</td>";
            }
        }
        $("#tbLogList tbody").html(htmlStr);
    }
}