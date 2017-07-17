/**
 * Created by Administrator on 2015/11/24.
 */
$(function(){
    var oilUrl;
    $.ajax({
        url: getRootPath() + "/wechat/redInfoC/redIndex.htm",
        dataType: "json",
        type: "post",
        success: function (data) {
            $(".leftMoney").html(data.resultObject.allReturnMoney);
            $(".totalMoney").html(data.resultObject.allGetRedMoney);
            $(".thisMoney").html(data.resultObject.getLastRedMoney);
            $(".totalGet").html(data.resultObject.allPreferential);
            if(data.resultObject.isBind==0){
                oilUrl="/oilCardLink.jsp";  //未绑定油卡先绑定
            }else{
                oilUrl="/moneyTocard.jsp";  //已绑定油卡直接跳转提至油卡页面
            }
        }
    });

    $(".oil").click(function(){
    location.href=getRootPath()+oilUrl;
    });
    $(".oilRecord").click(function(){
        location.href=getRootPath()+"/takeMoney.jsp";
    });
    $(".jiLu").click(function(){
        location.href=getRootPath()+"/addRecord.jsp";
    });
    $(".addOil").click(function(){
        location.href=getRootPath()+"/getRecord.jsp";
    });



});