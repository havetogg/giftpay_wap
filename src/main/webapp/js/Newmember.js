
$(function(){
    $(".yes").click(function(){

        var  a=$(".isWhich").val(),
            name = $("#name").val(),
            phone = $("#phone").val(),
            address = $("#address").val(),
            oilCard = $("#oilCard").val(),
            linkName = $("#linkName").val(),
            linkPhone = $("#linkPhone").val(),
            checkBool = document.getElementById("checkBox").checked;


        if(a==0){
            if(name.length<2||jxTool.isChinese(name)==false){
                alerw("请填写正确的姓名!");
            }else if(phone.length<11||jxTool.isMobile(phone)==false){
                alerw("请填写正确的手机号码!");
            }else if(address.length<5){
                alerw("请填写正确的地址!");
            }else if(!checkBool){
                alerw("请先阅读并同意活动协议！");
            }else{
                loading("start");
                //判断是否冻结
                //ajax请求，暂无加油卡时的请求




            }
        }else {
            if(oilCard.length<16||jxTool.isNumber(oilCard)==false){
                alerw("请填写正确的加油卡号！");
            }else if(linkPhone.length<11||jxTool.isMobile(linkPhone)==false){
                alerw("请填写正确的手机号码!");
            }else if(linkName.length<2||jxTool.isChinese(linkName)==false){
                alerw("请填写正确的姓名!");
            }else if(!checkBool){
                alerw("请先阅读并同意活动协议！");
            }else{
                loading("start");
                //判断是否冻结
                //ajax请求，已有加油卡时的请求




            }


        }

    });

});

function chance(){
    var a=$(".isWhich").val();

    if(a=="1"){
        $(".information").css("display","none");
        $(".information1").css("display","block");
        $(".tip1").css("display","none");
        $(".tip2").css("display","block");

    }
    if(a=="0"){
        $(".information").css("display","block");
        $(".information1").css("display","none");
        $(".tip1").css("display","block");
        $(".tip2").css("display","none");

    }

}