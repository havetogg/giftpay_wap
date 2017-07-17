function getUrlParam(name) {
    //构造一个含有目标参数的正则表达式对象
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    //匹配目标参数
    var r = window.location.search.substr(1).match(reg);
    //alert(unescape(r[2]))
    //返回参数值.
    if (r != null) return unescape(r[2]);
    return null;
}

$(function(){
    $.ajax({
        url: getRootPath() + "/wechat/orderInfoC/queryOrderRecords.htm",
        dataType: "json",
        type: "post",
        data:{ orderID:getUrlParam("orderID")},
        success: function (data) {
            if (data.resultList[0].backMonth == 0) {
                $(".leftState").html("发放完毕");
                $(".leftState").css("color","#666")
            } else {
                $(".leftDay").html(data.resultList[0].backMonth);
            }

            $(".payMoney1").html(data.resultList[0].price);
            $(".dataTime").html(data.resultList[0].orderTime);
            $(".edu1").html(data.resultList[0].allMoney);
            $(".leftTime1").html(data.resultList[0].monthCount);
            //$(".leftDay").html(data.resultList[0].backMonth);
            $(".proNum1").html(data.resultList[0].orderId);
            $(".moneyNum1").html(data.resultList[0].businessorderid);
        }
    });






    function listDom(){
        var dom = $(
            '<div class="mes1">'+
            '<div class="yuan"></div>'+
            //'<p class="Month1">【<span class="f_25 green month">12</span>月】￥<span class="redMoney f_25 green">500</span>加油红包已发放 </p>'+
            '<p class="Month1"><span class="returnTime1">12</span></p>'+
            '<p class="isNotpay">￥<span class="redMoney f_25 green"></span>&nbsp;加油红包 <span class="fr" style="padding-right: 25px;color: #009900;">已发放</span> </p>'+
            //'<p class="returnTime1">2015-12-16</p>'+
            '</div>'
        );
        return dom;
    }


    var colors = new Array('#444','#666','#888','#aaa','#ccc','#ddd');

    $.ajax({
        url: getRootPath() + "/wechat/orderInfoC/queryOrderDetail.htm",
        dataType: "json",
        type: "post",
        data:{ orderID:getUrlParam("orderID")},
        success: function (data) {
            $.each(data.resultList, function (index, obj) {
                if(obj.isTrue == "未发放"){
                    var dom = listDom();
                    //$(".isNotpay", dom).html("￥&nbsp;"+obj.redMoney+"加油红包即将发放");
                    $(".isNotpay", dom).html("￥"+obj.redMoney+"&nbsp;加油红包");
                    $(".isNotpay", dom).append("<span class='fr'>已发放</span>");

                    $(".fr", dom).html("即将发放");

                    $(".fr", dom).css("color","red");

                    //$(".isNotpay", dom).css("color","red");
                    //$(".returnTime1",dom).css("color","red");
                    $(".Month1", dom).css("color","red");
                    $(".yuan", dom).css("display","none");

                    $(".month", dom).html(obj.month);
                    $(".returnTime1", dom).html($.trim(obj.startTime));
                    $(".isReturned").append(dom);
                }else{
                    var dom = listDom();





                    $(".isNotpay", dom).html("￥"+obj.redMoney+"&nbsp;加油红包");
                    $(".isNotpay", dom).append("<span class='fr' style='padding-right: 25px;color: green; '>已发放</span>");




                    //$(".redMoney", dom).html(obj.redMoney);
                    $(".returnTime1").addClass("green");
                    $(".month", dom).html(obj.month);
                    $(".returnTime1", dom).html($.trim(obj.startTime));
                    //colors[index];
                    $(".yuan",dom).css("background-color",colors[index]);
                    $(".isReturned").append(dom);

                }



            });
        }
    });











    //$.ajax({
    //    url: getRootPath() + "/wechat/orderInfoC/queryOrderDetail.htm",
    //    dataType: "json",
    //    type: "post",
    //    data:{ orderID:getUrlParam("orderID")},
    //    success: function (data) {
    //        $.each(data.resultList, function (index, obj) {
    //            if(index == 0){
    //                $(".redMoney2").html(obj.redMoney);
    //                $(".month2").html(obj.month);
    //                $(".returnTime2").html(obj.startTime);
    //            }else {
    //                var dom = listDom();
    //                $(".redMoney", dom).html(obj.redMoney);
    //                $(".month", dom).html(obj.month);
    //                $(".returnTime1", dom).html(obj.startTime);
    //                $(".isReturned").append(dom);
    //            }
    //        });
    //    }
    //});


});