/**
 * Created by Administrator on 2015/12/3.
 */


$(function(){
    $.ajax({
        url: getRootPath() + "/wechat/userC/queryWetInfo.htm",
        dataType: "json",
        type: "post",
        success: function (data) {
            $(".name").html(data.resultObject.nickname);
            $(".tel").html(data.resultObject.uTel);
        }
    });


    $.ajax({
        url: getRootPath() + "/wechat/gasCard/queryGasCard.htm",
        dataType: "json",
        type: "post",
        success: function (data) {

            if(data.resultObject==null){
                $(".information2").css("display","none");
            }else{
                $(".information2").css("display","block");
                $(".oilCard").html(data.resultObject.gasCardNumber);
                $(".banTel").html(data.resultObject.buildTel);
                $(".oilName").html(data.resultObject.buildUname);
            }
        }
    });
$(".change").click(function(){
    location.href = getRootPath()+"/oilCardchange.jsp";
});

});