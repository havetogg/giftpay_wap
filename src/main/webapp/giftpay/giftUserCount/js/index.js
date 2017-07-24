/**
 * Created by Administrator on 2017/6/6.
 */
var countdown=60;
function sendCode(self) {
    console.log("发送验证码操作。。。。")
    var phone = $("#tel").val();
    if(!phone){
        TipShow("请输入手机号码号码",1000);
        return ;
    }
    else if(!checkMobile(phone)){
        TipShow('请输入正确的手机号',1000);
        return ;
    }
    if(countdown==60){
        $.ajax({
            url:getRootPath()+"/giftpay/wallet/sendMsg.htm",
            type:"post",
            data:{"phone":phone},
            dataType:"json",
            success:function(data){
                console.log("发送验证码返回信息:"+data)
            }
        });
    }
    setValMsgTime(self);
}
function signUp() {
    var tel =  $('#tel').val();
    var code =  $('#code').val();
    if(!tel){
        TipShow('请输入手机号',1000);
    }
    else if(!checkMobile(tel)){
        TipShow('请输入正确的手机号',1000);
    }
    else if(!code){
        TipShow('请输入验证码',1000);
    }
    else{

        //立即办卡按钮只能点击一次
        $("#btn_y").unbind('click');

        var byWay = "doBankCard";
        if(modePC!="null"){
            byWay = "doBankPc";
        }

        //请求注册办卡操作
        $.ajax({
            url:getRootPath()+"/giftUserGo/"+byWay+".htm",
            type:"post",
            data:{"phone":tel,"code":code},
            dataType:"json",
            success:function(data){
                if(data.status=="success"){
                    console.log("[success]当前响应数据:"+data.status)
                    //新版跳转链接
                    window.location.href="https://c.pingan.com/apply/mobile/apply/index.html?scc=230000582&ccp=3a2a1a4a8a13a9&showt=1&xl=01a02a03a04a05";
                }else{
                    if(data.code=="9999"){
                        console.log("[false]渠道码获取失败code:"+data.code)
                        //TipShow("系统访问人数过多，正在为您重新链接...！",9999);
                        // window.location.href=getRootPath()+'/giftUserGo/wxLogin.htm?third_name=zfwcy';
                        TipShow("当前分享链接已失效，请重新获取最新分享地址！",9999);
                    }else if(data.code=="3000"){
                        console.log("[false]验证码输入错误code:"+data.code)
                        TipShow("验证码输入错误！",3000);
                    }else if(data.code=="5000"){
                        console.log("[false]手机号码获取异常code:"+data.code)
                        TipShow("系统访问人数过多，请重新进入当前页面！",3000);
                    }else if(data.code="1199"){
                        console.log("当前用户数据获取异常，重新授权！");
                        TipShow("当前办卡用户过多！正在为您重新进入此页面！",3000);
                        window.location.href=getRootPath()+"/giftUserGo/wxLogin.htm?third_name=zfwcy";
                    }
                }
            }
        });

    }
}
function setValMsgTime(obj){
    if (countdown == 0) {
        $(obj).show();
        $('#timer').hide();
        $(obj).html("获取验证码");
        countdown = 60;
        return;
    } else {
        obj.value="重新发送(" + countdown + ")";
        $(obj).hide();
        $('#timer').show();
        $('#timer').html("重新发送("+countdown+")");
        countdown--;
    }
    setTimeout(function() {
            setValMsgTime(obj) }
        ,1000)
}
$(function(){
    $('#tel').bind('input propertychange', function() {
        if(checkMobile($(this).val())&&$('#code').val()){
            $('#btn_y').show();
            $('#btn_n').hide();
        }
        else{
            $('#btn_n').show();
            $('#btn_y').hide();
        }
    });
    $('#code').bind('input propertychange', function() {
        if($('#code').val()&&$('#tel').val()){
            $('#btn_y').show();
            $('#btn_n').hide();
        }
        else{
            $('#btn_n').show();
            $('#btn_y').hide();
        }
    });
    $('#userPhone').bind('input propertychange', function() {
        if(checkMobile($('#userPhone').val())&& $(this).val().length==11){
            $('#getCashBtn').attr('class','flex-1')
        }
        else{
            $('#getCashBtn').attr('class','flex-1 my_money_getBtn_')
        }
    });
});
function iKnows() {
    $('#money_tip').attr('class','my_money_off');
}
function showMoneyTip() {
    $('#money_tip').attr('class','my_money_on');
}
function getCascadeTip() {
    $('#my_money_input').val('');
    $('#GetMoney_tip').attr('class','my_money_on');
}
function getCashCancel() {
    $('#GetMoney_tip').attr('class','my_money_off');
}
function getCashSure() {

    if(checkMobile($('#userPhone').val())&& $('#userPhone').val().length==11){
        //发送请求 确认提现订单
        $('#GetMoney_tip').attr('class','my_money_off');
        console.log("当前用户点击提现操作.......");

        //只能点击一次
        $("#getCashBtn").unbind('click');

        var phone_get = $('#userPhone').val();

        if(trird_name=="0"){
            console.log("当前trird_name为空,不允许提现操作!")
            alert("提现状态异常!请重新授权登陆此页面!");
        }else {
            //发送订单请求
            $.ajax({
                url: getRootPath() + "/pinganShare/doMoney.htm",
                data: {'openid': trird_name, "phone": phone_get},
                dataType: "json",
                success: function (data) {

                    if (data.status == "success") {
                        console.log("提现请求已经提交..。。。。")

                        $("#phone_input").show();
                        $('#userPhone').val("")
                        $("#getCashBtn").addClass("my_money_getBtn_");
                        // TipShow("当前提现订单已提交！",1000);

                        //获取用户点击确认事件
                        //var status =  window.confirm("当前提现请求已经提交!");
                        //if(status==true || status==false){
                        //}

                        window.location.reload();

                    } else {
                        alert(data.mess);
                        console.log("当前用户不存在订单数据!")
                    }


                }
            });
        }

    }
    else{
        alert('请输入正确的手机号码');
    }



}
function shareTip() {
    $('#shareTip').show();
}
function shareTipHide() {
    $('#shareTip').hide();
}