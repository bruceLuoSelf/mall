$(function () {
    ServicerVote.init();
});
var ServicerVote = {
    init: function () {
        // 变手型
        $('.servicer').hover(function () {
            $(this).attr('title', '点击投票');
            $(this).css('cursor', 'pointer');
        });
        $('.servicer').click(this.vote);
        $('a').click(this.vote);
        //this.loadServicersData();

        $('.winners').vTicker({
            speed: 500,
            pause: 1000,
            animation: 'fade',
            mousePause: true,
            showItems: 7,
            height:100
        });
    },
    /**
     * 获取客服数据
     */
    loadServicersData: function () {
        $.ajax({
            type: "GET",
            url: baseServiceUrl + "services/servicer/queryServicers?t="+new Date().getTime(),
            contentType: "application/json; charset=UTF-8",
            dataType: "json",
            beforeSend: function (request) {
                //request.setRequestHeader("5173_authkey", getAuthkey());
            },
            success: function (resp) {
                var responseStatus = resp.responseStatus;
                var code = responseStatus.code;
                if (code == "00") {
                    var servicers = resp.userInfoEOs;
                    for (var i = 0; i < servicers.length; i++) {
                        $("#vote_" + servicers[i].id).text(servicers[i].vote);
                    }
                }
            },
            error: function (resp) {
                console.log(resp);
            }
        });
    },
    /**
     * 投票
     */
    vote: function () {
        if (ServicerVote.hasVoted()) {
            alert("您已经投过票，请不要重复投票！");
            return;
        }
        var request = {servicerId: $(this).attr('dataId')};
        $.ajax({
            type: "POST",
            url: baseServiceUrl + "services/servicer/incrVote",
            data: $.toJSON(request),
            contentType: "application/json; charset=UTF-8",
            dataType: "json",
            beforeSend: function (request) {
                request.setRequestHeader("5173_authkey", getAuthkey());
            },
            success: function (resp) {
                var responseStatus = resp.responseStatus;
                var code = responseStatus.code;
                if (code == "00") {
                    alert("投票成功！");
                    $.cookie('voted', true);
                    //ServicerVote.refreshVote(request.servicerId, resp.vote);
                } else if (code == "13002") {
                    $.cookie('voted', true);
                }
            },
            error: function (resp) {
                console.log(resp);
            }
        });
    },
    /**
     * 判断是否已经投票
     * @returns {boolean}
     */
    hasVoted: function () {
        var hasVoted = $.cookie("voted");
        return hasVoted ? true : false;
    },
    /**
     * 刷新客服投票
     * @param servicerId
     */
    refreshVote: function (servicerId, vote) {
        $("#vote_" + servicerId).text(vote);
    }
};
