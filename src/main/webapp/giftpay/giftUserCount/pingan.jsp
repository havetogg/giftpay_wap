<%@ page language="java" import="java.util.*" isELIgnored="false" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="initial-scale=0.5,maximum-scale=0.5,minimum-scale=0.5, width=640, target-densitydpi=device-dpi">
    <meta http-eqiv="X-UA-Compatible" content="IE=Edge,chrome=1">
    <meta content="yes" name="apple-mobile-web-app-capable">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <title>有礼付</title>
    <link type="text/css" href="css/common/common.css" rel="stylesheet">
    <link type="text/css" href="css/app.css" rel="stylesheet">
    <link type="text/css" href="css/flexslider.css" rel="stylesheet">
    <script type="text/javascript" src="js/common/jQuery-1.11.3.js"></script>
    <script type="text/javascript" src="js/common/jWeChat-Adaptive.js"></script>
    <script type="text/javascript" src="js/common/jWeChat-1.0.0.js" ></script>
    <script type="text/javascript" src="js/common/m.tool.juxinbox.com.js"></script>
    <script type="text/javascript" src="js/common/common.js"></script>
    <script type="text/javascript" src="js/index.js" ></script>
    <script type="text/javascript" src="js/jquery.flexslider-min.js" ></script>
</head>
<style>
    input::placeholder{
        color: #888;
    }
</style>

<script>
    var TipShow = function (msg, duration) {
        var timeoutId = -1;
        var $backdropObj = $(".loading-backdrop");
        if (!$backdropObj[0]) {
            htmlStr = "<div class='loading-backdrop'>" +
                "<div class='loading-wrapper'>" +
                "<div class='loading-content'>" +
                msg +
                "</div></div></div>";
            $("body").append(htmlStr);
            $(".loading-backdrop").addClass('visible');
            if (typeof duration == "number" && duration > 0) {
                if (timeoutId > 0) {
                    clearTimeout(timeoutId);
                    delete timeoutId;
                }
                timeoutId = setTimeout(function () {
                    TipHide()
                }, duration);
            }
        } else {
            $(".loading-content")[0].innerText = msg;
            $(".loading-backdrop").addClass('visible');
            if (typeof duration == "number" && duration > 0) {
                if (timeoutId > 0) {
                    clearTimeout(timeoutId);
                    delete timeoutId;
                }

                timeoutId = setTimeout(function () {
                    TipHide()
                }, duration);
            }
        }
    };
    var TipHide = function () {
        $(".loading-backdrop").removeClass('visible');
    };
    function checkMobile(sMobile){
        if(!(/^1[3|4|5|7|8][0-9]\d{4,8}$/.test(sMobile))){
            return false;
        }
        else{
            return true;
        }
    }
    $(function () {
        $(".flexslider").flexslider({
            animation: "slide", //String: Select your animation type, "fade" or "slide"图片变换方式：淡入淡出或者滑动
            slideshowSpeed: 4000, //展示时间间隔ms
            animationSpeed: 1100, //滚动时间ms
            touch: true //是否支持触屏滑动
        });
    })
    function submitInfo() {
        var name=$('#name').val();
        var tel=$('#tel').val();
        var code=$('#code').val();
    }
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
</script>

<script>

    <%
         //从请求中获取当前手机号码phone,根据有无传入手机号码判断当前用户是否存在已经绑定手机号码.
        String phone = request.getParameter("phone")==""?"0":request.getParameter("phone");
    %>

    var phone = "<%=phone%>"
    $(function () {
        $('#name').bind('input propertychange', function() {
            if($('#name').val()&&$('#code').val()&&$('#tel').val()){
                $('#submit_data').removeClass("submitBtn1").addClass("submitBtn2");
            }else{
                $('#submit_data').removeClass("submitBtn2").addClass("submitBtn1");
            }
        })
        $('#tel').bind('input propertychange', function() {
            if($('#name').val()&&$('#code').val()&&$('#tel').val()){
                $('#submit_data').removeClass("submitBtn1").addClass("submitBtn2");
            }else{
                $('#submit_data').removeClass("submitBtn2").addClass("submitBtn1");
            }
        })
        $('#code').bind('input propertychange', function() {
            if($('#name').val()&&$('#code').val()&&$('#tel').val()){
                $('#submit_data').removeClass("submitBtn1").addClass("submitBtn2");
            }else{
                $('#submit_data').removeClass("submitBtn2").addClass("submitBtn1");
            }
        })
    })
    $(function (){

        //自动获取当前用户授权下的手机号码
//        if(phone!="0"){
//            $("#tel").val(phone);
//            $("#code").val("验证码已输入!")
//            $("#li_phone").css("display","none")
//            $("#li_card").css("display","none")
//        }else{
//            $("#li_phone").css("display","block")
//            $("#li_card").css("display","block")
//        }


        $("#submit_data").click(function (){

            var phone = $("#tel").val();
            var PhoneCard = $("#code").val();
            var name = $("#name").val();

            if(!phone){
                alert("请输入你的手机号码!",1000);
            }else if(!name){
                alert("请输入你的姓名!",1000);
            }else{
                $.ajax({
                    url:getRootPath()+"/pinganBankGo/doPaBankLoan.htm",
                    type:"post",
                    data:{"phone":phone,"code":PhoneCard,"username":name,"third_code":"zfwcy"},
                    dataType:"json",
                    success:function(json){
                        if(json.status=="success"){
                            alert("提交成功，我们的客服会尽快联系您!",1000);
                            //之前是刷新当前页面
                            window.location.reload();
                        }else{
                            alert(json.mess,1000);
                            console.log("当前用户不存在订单数据!")
                        }

                        console.log("提交成功!")

                    }
                });
            }
        });

        addChannelCount("zfwcy");
        //更新点击率
        function addChannelCount(trird_name_input){
            $.ajax({
                type : "POST",  //提交方式
                url : getRootPath()+"/giftGO/addChannelData.htm",//路径
                data : {"third_name" : trird_name_input},//数据，这里使用的是Json格式进行传输
                dataType:'json',
                success : function(data) {//返回数据根据结果进行相应的处理
                    if(data.result=="success"){

                        console.log("成功！~~更新点击率!")
                        if(data.code =="0010"){
                            console.log("更新当前渠道码:[${third_name}]点击率成功!");
                        }else if (data.code =="0020"){
                            console.log("渠道数据创建 成功!")
                        }

                    } else{
                        console.log("错误！"+data.mess)
                    }
                }
            });
        }

        wxShare("zfwcy");
        //分享配置
        function wxShare(third_name_input){
            $.ajax({
                url: getRootPath()+"/giftpay/wap/shareFriend.htm",
                data: {'url': location.href},
                dataType: "json",
                success: function (config) {
                    wx.config(config);
                    shareData = {
                        title:"平安银行新一贷",
                        desc: "最高50万，最快当天放款。创业梦想即可启动！",
                        //分享跳转链接
                        //link:getRootPath()+'/giftUserGo/wxLogin.htm?third_name=zfwcy',
                        link:getRootPath()+'/pinganBankGo/wxLogin.htm?third_name='+third_name_input,
                        //分享连接图片
                        imgUrl:getRootPath()+'/giftpay/giftUserCount/img/pingandaikuan.png',

                        // linkpath + '/share.jsp='+wxId+'&id='+currentId;
                        trigger: function (res) {
                            //alert('用户点击发送给朋友');
                        },
                        success: function (res) {
                            //alert('已分享');
                        },
                        cancel: function (res) {
                            //alert('已取消');
                        },
                        fail: function (res) {
                            //alert("this is "+JSON.stringify(res));
                        }
                    };
                    //发送给朋友圈
                    shareFriends = {
                        title:"平安银行新一贷",
                        desc: "最高50万，最快当天放款。创业梦想即可启动！",
                        //分享跳转链接
                        // link:getRootPath()+'/giftUserGo/wxLogin.htm?third_name=zfwcy',
                        link:getRootPath()+'/pinganBankGo/wxLogin.htm?third_name='+third_name_input,
                        //分享连接图片
                        imgUrl:getRootPath()+'/giftpay/giftUserCount/img/pingandaikuan.png',
                        // linkpath + '/share.jsp='+wxId+'&id='+currentId;
                        trigger: function (res) {
                            //alert('用户点击发送给朋友');
                        },
                        success: function (res) {
                            //alert('已分享');
                        },
                        cancel: function (res) {
                            //alert('已取消');
                        },
                        fail: function (res) {
                            //alert("this is "+JSON.stringify(res));
                        }
                    };
                    wx.ready(function () {
                        wx.onMenuShareAppMessage(shareData);
                        wx.onMenuShareTimeline(shareFriends);
                    });
                    wx.error(function (res) {
                        //alert(JSON.stringify(res));
                    });
                },
                error: function (json) {
                    //alert(JSON.stingify(json));
                }
            });
        }

    })


</script>

<body>
<div class="bg_block1">
    <div class="return_tip_bg">
        <div class="records_Name">退款说明</div>
        <div class="return_tip_bg_content">
            <div>退款将存入有礼付钱包</div>
            <div>请在钱包中提现</div>
        </div>
        <div class="return_tip_bg_btn" onclick="return_tip_bg_btn()">确定</div>
    </div>
</div>
<div style="background-color: #f0f0f4;position:fixed;" class="zoomer">
    <div class="flexslider">
        <ul class="slides">
            <li><img  src="img/guide-bg2.jpg" alt="1" width="100%"/></li>
            <li><img  src="img/guide-bg3.jpg" alt="1" width="100%"/></li>
            <li><img  src="img/guide-bg4.jpg" alt="1" width="100%"/></li>
        </ul>
    </div>
    <div class="gulide-box">
        <ul class="pec-gulide">
            <li class="pec-gulide-item">
                <div class="pec-item-1">
                    <img src="img/name1.png" alt="">
                    <input id="name" type="text" class="pec-input" placeholder="请填写姓名">
                </div>
            </li>
            <li id="li_phone"  class="pec-gulide-item">
                <div class="pec-item-1">
                    <img src="img/tel1.png" alt="">
                    <input id="tel" type="tel" maxlength="11" class="pec-input" placeholder="请填写手机号">
                </div>
            </li>
            <li id="li_card"  class="pec-gulide-item">
                <div class="pec-item-1">
                    <img src="img/safe1.png" alt="">
                    <input id="code" type="tel" maxlength="6" class="pec-input1" placeholder="请填写验证码">
                    <label class="getCode_text" onclick="sendCode(this)">获取验证码</label>
                    <label class="getCode_text" id="timer"></label>
                </div>
            </li>
        </ul>
        <div id="submit_data" class="submitBtn1">立即提交</div>
    </div>
</div>
</div>
</body>
</html>