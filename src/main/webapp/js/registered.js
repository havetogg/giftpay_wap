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
$(function(){

    //$(".btn_3").click(function() {
    //    secCount();
    //    $(".btn_4").css("display", "block");
    //    $(".btn_3").css("display", "none");
    //} );

    $(".btn_3").click(function() {
        var phoneNum=$("#phone").val();
        if(phoneNum.length<11||jxTool.isMobile(phoneNum)==false){
            alerw("请填写正确的手机号码!");
        }else {
            secCount();
            $(".btn_4").css("display", "block");
            $(".btn_3").css("display", "none");
            $.ajax({
                url: getRootPath() + "/wechat/userC/getRegisterSecurityCodeM.htm",
                data: {"tel": phoneNum},
                dataType: "json",
                type: "post",
                success: function (data) {

                }
            });
        }
    } );


    $(".jiHuo").click(function() {
        var phoneNum=$("#phone").val();
        var codeNum=$("#code").val();
        var recommendCode=$("#recommend").val();
        if(phoneNum.length<11||jxTool.isMobile(phoneNum)==false){
            alerw("请填写正确的手机号码!");
        }else {
            loading("start");
            $.ajax({
                url: getRootPath() + "/wechat/userC/registerM.htm",
                data: {"tel": phoneNum, "securityCode": codeNum,"recommendCode": recommendCode},
                dataType: "json",
                type: "post",
                success: function (data) {
                    loading("stop");
                    if(data.code==1){
                        $(".alertTips").css("display","block");
                        setTimeout(function(){
                            location.href=getRootPath()+"/index.jsp";
                        },3000);

                    }else{
                        alerw(data.mess)
                    }

                }
            });
        }
    } )
});
