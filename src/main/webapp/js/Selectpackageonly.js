/**
 * Created by Administrator on 2015/11/26.
 */
$(function(){

    $(".apply").click(function(){
        var oldMoney=$(".oldMoney").html();
        var lastMoth=$(".lastMoth").html();
        var checkBool = document.getElementById("checkBox").checked;
        if(!checkBool){
            alerw("请先阅读并同意活动条款！");
        }else{
        $.ajax({
            url: getRootPath() + "/wechat/orderInfoC/buyOrder.htm",
            data: {"allMoney": oldMoney, "monthCount": lastMoth},
            dataType: "json",
            type: "post",
            success: function (data) {
                loading("stop");
                if(data.code==1){
                    location.href=data.resultObject.payurl;
                }else{
                    alerw(data.mess)
                }

            }
        });
        }

    })
});
