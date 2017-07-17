/**
 * Created by Administrator on 2015/12/2.
 */

$(function(){


    function listDom(address){
        var dom = $(
            '<a href='+address+'>'+
            '<div class="record1">'+
            '<div class="payMoney">支付金额：￥<span class="payMoneys f_28b"></span></div>'+
            '<div class="dataTime">2014-11-11 14:58:00</div>'+
            '<div class="edu">套餐额度：￥<span class="maxMoney f_25">3000</span></div>'+
            '<div class="leftTime">套餐期限：<span class="leftTimes f_25">12</span>个月</div>'+
            '<div class="leftState">余期：<span class="leftDay">5</span>个月</div>'+
            //'<div class="leftState1">发放完毕</div>'+
            '<input type="hidden" class="bookNum"/>'+
            '</div>'+
            '</a>'
        );
        return dom;
    }

    $.ajax({
        url: getRootPath() + "/wechat/orderInfoC/queryOrderRecords.htm",
        dataType: "json",
        type: "post",
        success: function (data) {
            console.log(data);
            if (data.resultList[0] == null) {
                $(".tipsIm").css("display", "block");
            } else {
                $.each(data.resultList, function (index, obj) {

                    var address = getRootPath() + "/detailRecord.jsp?orderID=" + obj.orderId;
                    var dom = listDom(address);
                    $(".bookNum", dom).attr("value", obj.orderId);
                    $(".payMoneys", dom).html(obj.price);
                    $(".dataTime", dom).html(obj.orderTime);
                    $(".maxMoney", dom).html(obj.allMoney);
                    if (obj.backMonth == 0) {
                        $(".leftState", dom).html("发放完毕");
                        $(".leftState", dom).css("color","#666")
                    } else {
                        $(".leftDay", dom).html(obj.backMonth);
                    }
                    //$(".leftDay", dom).html(data.resultObject.backMonth);
                    $(".leftTimes", dom).html(obj.monthCount);
                    $(".notPass").append(dom);
                });
            }
        }
    });


});