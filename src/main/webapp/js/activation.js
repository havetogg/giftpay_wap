/**
 * Created by Administrator on 2015/11/24.
 */



function secCount(){
    var second = parseInt($(".btn_4").html().substr(0,$(".btn_4").html().length-1));
    if(second>0){
        setTimeout(function(){
            $(".btn_4").html("").html(second-1+"秒");
            secCount();
        },1000);
    }else{
        $(".btn_3").css("display","block");
        $(".btn_4").html("").html(60+"秒").css("display","none");
    }
}
function daoJishi(){
    secCount();
    $(".btn_4").css("display", "block");
    $(".btn_3").css("display", "none");
}


$(function(){
    $.ajax({
        url: getRootPath() + "/wechat/userC/validationWechatIsRegisterM.htm",
        dataType: "json",
        type: "post",
        success: function (data) {
            if(data.code==1){
                $(".alertTips").css("display","block");
                $(".oldPhone").html(data.resultObject.userTel)
            }else{
            }
        }
    });






    $(".btn_3").click(function() {
        var phoneNum=$(".phone").val();
        if(phoneNum.length<11||jxTool.isMobile(phoneNum)==false){
            alerw("请填写正确的手机号码!");
        }else {
            secCount();
            $(".btn_4").css("display", "block");
            $(".btn_3").css("display", "none");
            $.ajax({
                url: getRootPath() + "/wechat/userC/getActivitySecurityCodeM.htm",
                data: {"tel": phoneNum},
                dataType: "json",
                type: "post",
                success: function (data) {

                }
            });
        }
    } );

    $(".jiHuo").click(function() {
        var codeNum = $("#activaCode").val();

        var phoneNum = $("#phone").val();
        //alert(phoneNum);
        //alert(codeNum);

        if(phoneNum.length<11||jxTool.isMobile(phoneNum)==false){
            alerw("请填写正确的手机号码!");
        }else{
            loading("start");
            $.ajax({
                url: getRootPath() + "/wechat/userC/activateUserInfo.htm",
                data: {"tel": phoneNum, "securityCode": codeNum},
                dataType: "json",
                type: "post",
                success: function (data) {
                    loading("stop");
                    if (data.code == 1) {
                        alerw("恭喜您，激活成功！关闭页面后，点击菜单“首页“，开始享受车答应优惠服务吧！", "好嘞");
                    } else {
                        alerw(data.mess);
                    }
                }
            });
        }
    });




    //
    //$(".changeUser").click(function() {
    //    $(".alertTip1").css("display", "block");
    //    $(".alertTip").css("display", "none");
    //    daoJishi();
    //    $.ajax({
    //        url:getRootPath()+"/wechat/userC/getRegisterSecurityCodeM.htm",
    //        data:{"phone": phoneNum},
    //        dataType:"json",
    //        type:"post",
    //        success:function(data){
    //
    //        }
    //    });
    //
    //} );

    //
    //$(".sureChange").click(function() {
    //    $.ajax({
    //        url:getRootPath()+"?",
    //        data:{"code":codeNum},
    //        dataType:"json",
    //        type:"post",
    //        success:function(data){
    //
    //        }
    //    });
    //} );



    $(".changeWx").click(function() {
        WeixinJSBridge.call('closeWindow');
    } );





});

