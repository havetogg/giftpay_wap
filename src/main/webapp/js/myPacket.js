/**
 * Created by Administrator on 2015/12/2.
 */
$(function(){
    function listDom(){
        var dom = $(
            '<div class="addOil1">'+
            '<h1 class="howMoney">￥<span class="payMoney1 f_28b">100</h1>'+
            '<p>红包来源：9折加油</p>'+
            '<p>使用期限：<span class="minTime">不限期</span></p>'+
            '<div class="stage">已提现</div>'+
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
                    //$(".minTime", dom).html(obj.startTime);
                    if(obj.redInfoState=="已提现"){
                        $(".stage", dom).css("color","#999");
                    }
                    $(".stage", dom).html(obj.redInfoState);
                    $(".addOil").append(dom);
                });
            }
        }
    });



});