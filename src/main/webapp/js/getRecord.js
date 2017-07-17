/**
 * Created by Administrator on 2015/12/2.
 */
$(function(){
    function listDom(){
        var dom = $(
            '<div class="record1">'+
            '<div class="payMoney">领取金额：￥<span class="payMoney1 f_28b">2700</span></div>'+
            '<div class="dataTime">2014-11-11 14:58:00</div>'+
            '<div class="comeFrom">来自<span class="comeFrom1 red f_25">9折加油活动</span>的加油红包</div>'+
            '<div class="leftState">未提现</div>'+
            '</div>'
        );
        return dom;
    }

    $.ajax({
        url: getRootPath() + "/wechat/redInfoC/loadRedGetInfo.htm",
        dataType: "json",
        type: "post",
        success: function (data) {
            console.log(data);
            if (data.resultList[0] == null) {
                $(".tipsIm").css("display", "block");
            } else {
                $.each(data.resultList, function (index, obj) {
                    var dom = listDom();
                    $(".payMoney1", dom).html(obj.redMoney);
                    $(".dataTime", dom).html(obj.startTime);
                    //$(".comeFrom1", dom).html(obj.redName);
                    if(obj.redInfoState=="已提现"){
                        $(".leftState", dom).css("color","#999");
                    }
                    $(".leftState", dom).html(obj.redInfoState);
                    $(".notPass").append(dom);
                });
            }
        }
    });


});