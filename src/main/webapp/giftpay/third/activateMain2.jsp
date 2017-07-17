<%@ page language="java" import="java.util.*" isELIgnored="false" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="initial-scale=0.5,maximum-scale=0.5,minimum-scale=0.5, width=640, target-densitydpi=device-dpi">
    <meta http-eqiv="X-UA-Compatible" content="IE=Edge,chrome=1">
    <meta content="yes" name="apple-mobile-web-app-capable">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <title>办卡</title>
    <link type="text/css" href="css/common/common.css" rel="stylesheet">
    <!--<link type="text/css" href="css/app2.css" rel="stylesheet">-->
    <link type="text/css" href="css/flexslider.css" rel="stylesheet">
    <script type="text/javascript" src="js/common/jQuery-1.11.3.js"></script>
    <script type="text/javascript" src="js/common/jWeChat-Adaptive.js"></script>
    <script type="text/javascript" src="js/common/m.tool.juxinbox.com.js"></script>
    <script type="text/javascript" src="js/common/common.js"></script>
    <script type="text/javascript" src="js/index.js"></script>
    <script type="text/javascript" src="js/jquery.flexslider-min.js"></script>
</head>
<style>
    *{
        margin:0;
        padding:0;
    }
    li{
        list-style-type: none;
    }
    /**
      * TipShow Stylesheets
      */
    .loading-backdrop {
        width: 100%;
        height: 100%;
        background: rgba(0,0,0,0.5);
        position: fixed;
        top: 0;
        right: 0;
        bottom: 0;
        left: 0;
        z-index: 1000;
        opacity: 0;
        display: none;
        transition: opacity 0.3s linear;
        -webkit-transition: opacity 0.3s linear;
    }
    .loading-wrapper {
        float: left;
        position: relative;
        left: 50%;
        top: 36%;
    }
    .loading-content {
        width: 275px;
        border-radius: 4px;
        background: rgba(0,0,0,0.7);
        color: #fff;
        font-size: 26px;
        padding: 10px;
        text-align: center;
        line-height: 45px;
        position: relative;
        left: -50%;
    }
    .loading-backdrop.visible {
        opacity: 1;
        display: block;
        right: 0;
        bottom: 0;
        left: 0;
        z-index: 1000;
        transition: opacity 0.3s linear;
    }
    .index_bg{
        background: url(img/red_bg123123.png);
        height: 960px;
        width: 620px;
        background-repeat: no-repeat;
        position: absolute;
        font-size: 14px;
        left:10px;
        top: 40px;
        margin: 1px auto;
    }
    .redNew_bg{
        background: url(img/redNew1.png);
        height: 431px;
        width: 592px;
        background-repeat: no-repeat;
        position: absolute;
        font-size: 14px;
        right: 15px;
        top: 70px;
        text-align: center;
    }
    .yuan{
        font-size: 60px;
        color: #fff;
        font-family: "Microsoft YaHei";
        text-align: center;
    }
    .activate_yuan{
        font-size: 40px;
        color: #fff;
        font-family: "Microsoft YaHei";
        text-align: center;
    }
    .yuanText{
        color: #fff;
        font-size: 36px;
        margin-top: 25px;
        text-align: center;
    }
    .index_time{
        font-size: 20px;
        color: #fff;
        margin-top: 15px;
        text-align: center;
    }
    .redContent{
        margin-top: 85px;
    }
    .index_contentText{
        margin: 80% auto;
    }
    .activate_content_text{
        font-size: 20px;
        color: #ff4444;
        width: 90%;
        margin: 15px auto 0;
    }
    .index_contentText input{
        width: 90%;
        height: 70px;
        font-size: 24px;
        color: #c1c1c1;
        font-family: "Microsoft YaHei";
        position: relative;
        border: 1px solid #d6d6d6;
        text-indent: 1em;
        border-radius: 5px;
    }
    .tips_text{
        background-color: #ff5224;
        height: 30px;
        color: #fff;
        border-radius: 5px;
        padding: 3px 5px;
        font-size: 18px;
        line-height: 30px;
        font-family: "Microsoft YaHEi";
        width: 10%;
        float: left;
        text-align: center;
    }
    .tips_contents{
        color: #888888;
        font-size: 20px;
        padding: 0px 10px;
        width: 88%;
        float: right;
        box-sizing: border-box;
    }
    .tips_parent{
        width: 90%;
        margin: 0 auto;
    }
    ::-webkit-input-placeholder{
        font-family: 'Microsoft YaHei';
        color: #cccccc;
    }
    .codeText{
        display: inline-block;
        text-align: center;
        width: 35%;
        color: #fff;
        background-color: #ffaa24;
        height: 70px;
        line-height: 70px;
        font-size: 24px;
        border-radius: 5px;
        float: right;
        font-family: 'Microsoft YaHei';
        position: relative;
        box-sizing: border-box;
    }
    #timer{
        display: none;
    }
    .block_bg{
        position: fixed;
        width: 100%;
        height: 100%;
        background-color: rgba(0,0,0,0.3);
        z-index: 999;
    }
    .activate_bg{
        background-color: rgba(0,0,0,0.5);
        width: 100%;
        height: 100%;
        z-index: 999;
        position: fixed;
        display: none;
    }
    .activate_div{
        width: 65%;
        background-color: #fff;
        margin: 50% auto;
        padding: 50px 35px;
        position: relative;
        border-radius: 10px;
    }
    .activate_title{
        font-size: 30px;
        color: #000;
        text-align: center;
        font-family: 'Microsoft YaHei';
    }
    .activate_content{
        line-height: 40px;
        text-align: center;
        font-size: 24px;
        color: #888;
        font-family: 'Microsoft YaHei';
        margin: 50px 0px;
    }
    .close{
        position: absolute;
        right: 30px;
        top: 35px;
        width: 60px;
    }
    .tip_sure{
        display: inline-block;
        width: 45%;
        text-align: center;
        background-color: #ff5224;
        line-height: 70px;
        font-size: 24px;
        color: #fff;
        border-radius: 5px;
        font-family: 'Microsoft YaHei';
        float: right;
        box-sizing: border-box;
    }
    .tip_cancel{
        display: inline-block;
        width: 45%;
        text-align: center;
        background-color: #fff;
        line-height: 70px;
        font-size: 24px;
        color: #000;
        border: 1px solid #888;
        border-radius: 5px;
        font-family: 'Microsoft YaHei';
        box-sizing: border-box;
    }
    .activity{
        width: 100%;
        background-color: #ff5224;
        text-align: center;
        font-size: 24px;
        color: #fff;
        line-height: 70px;
        height: 70px;
        border-radius: 5px;
        font-family: 'Microsoft YaHei';
    }
</style>
<script>
    function onBridgeReady(){
        WeixinJSBridge.call('hideOptionMenu');
    }
    if (typeof WeixinJSBridge == "undefined"){
        if( document.addEventListener ){
            document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);
        }else if (document.attachEvent){
            document.attachEvent('WeixinJSBridgeReady', onBridgeReady);
            document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
        }
    }else{
        onBridgeReady();
    }
</script>
<script>
	var thirdName='${thirdName}';
    $(function(){
    	if(thirdName=='pingan'){
    		$(".index_bg").css("background","url(img/red_pan.png)");
    		$(".tips_contents").html("您拿到平安银行信用卡后,只需在任意消费场景下首刷20元即可激活50元加油支付红包.如有问题,请详询客服电话:4000808065");
    		$(".activate_content_text").html(" 亲,本优惠活动仅限通过本页面\"申请办卡激活红包\"按钮首次申请办理平安银行信用卡的用户哦.老用户活动很快会有的,谢谢参与!");
    	}else if(thirdName=='other'){
    		$(".index_bg").css("background","url(img/red_xy.png)");
    		$(".tips_parent .flex").hide();
    		$(".activate_content_text").hide();
    	}else if(thirdName=='fuli'){
    		$(".index_bg").css("background","url(img/red_xy.png)");
    		$(".tips_contents").html("您拿到兴业银行信用卡后,只需在任意消费场景下首刷20元即可激活50元加油支付红包.如有问题,请详询客服电话:4000808065");
    		$(".activate_content_text").html(" 亲,本优惠活动仅限通过本页面\"申请办卡激活红包\"按钮首次申请办理兴业银行信用卡的用户哦.老用户活动很快会有的,谢谢参与!");
    	}
        if (/Android/gi.test(navigator.userAgent)) {
            window.addEventListener('resize', function () {
                if (document.activeElement.tagName == 'INPUT' || document.activeElement.tagName == 'TEXTAREA') {
                    window.setTimeout(function () {
                        document.activeElement.scrollIntoViewIfNeeded();
                    }, 0);
                }
            })
        }
    })
    function clsoe_tip() {
        $("#fail_block").hide();
    }
    function show_tip() {
        $("#fail_block").show();
    }
    var countdown = 60
    function setValTime(obj){
        if(!$('#tel').val()){
            TipShow('请输入手机号',1000);
        }
        else  if(!(/^1[34578]\d{9}$/.test($('#tel').val()))){
            TipShow('请输入正确的手机号',1000);
        }
        else{
            if(countdown==60){
                loading("start");
                $.ajax({
                    url:getRootPath()+"/giftpay/third/sendMsg.htm",
                    type:"post",
                    data:{"phone":$('#tel').val()},
                    dataType:"json",
                    success:function(data){
                        loading("stop");
                    },error:function(){
                        loading("stop");
                    }
                });
            }
            setValMsgTime(obj);
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
    function activate() {
        var tel=$.trim($('#tel').val());
        var code=$.trim($('#code').val());
        if(!tel){
            TipShow('请输入手机号',1000);
        }
        if(!code){
            TipShow('请输入手机验证码',1000);
        }
        else if(!jxTool.isMobile(tel)){
            TipShow('请输入正确的手机号',1000);
        }
        else{
            // alert("激活成功！");
            comitInfo(tel,code);
        }
        //show_tip();
    }
    function comitInfo(tel,code){
        loading("start");
        $.ajax({
            type: "GET",
            url:getRootPath()+"/giftpay/third/addThirdUser.htm",
//            data: {'mobile':tel,"valNum":code},
            data: {'mobile':tel,"valNum":code,"thirdName":'${thirdName}'},
            dataType: "json",
            success: function(data){
                loading("stop");
                if(data.code=='20000'){
                    TipShow(data.mess,1000);
                }else{
                    TipShow('添加成功',1000);
                    if(thirdName=='pingan'){
                    	   location.href='https://c.pingan.com/apply/mobile/apply/index.html?scc=920000981&ccp=1a2a3a4a5a8a9a10a11a12a13&showt=0';
                    }else if(thirdName=='zsh'){
                    		location.href='https://ccshop.cib.com.cn:8010/application/cardapp/cappl/ApplyCard/toSelectCard?id=40cd0bf21a6c4bce99a82befd7e68c7b';   	
                    }else{
                    	   location.href='https://ccshop.cib.com.cn:8010/application/cardapp/cappl/ApplyCard/toSelectCard?id=40cd0bf21a6c4bce99a82befd7e68c7b';
                    }
                 
                }
            },error:function(res){
                loading("stop");
                alerw(JSON.stringify(res));
            }
        });
    }
    function OnInput (event) {
        var tel=event.target.value;
        if(tel&&jxTool.isMobile(tel)&&$('#code').val()){
            $('#yes').show();
            $('#no').hide();
        }
        else{
            $('#yes').hide();
            $('#no').show();
        }
    }
    function OnInput1 (event) {
        var tel=$('#tel').val();
        if(tel&&jxTool.isMobile(tel)&&$('#code').val()){
            $('#yes').show();
            $('#no').hide();
        }
        else{
            $('#yes').hide();
            $('#no').show();
        }
    }
    function closeTip() {
        $('.activate_bg').hide();
    }
</script>
<body style="background-color: #ffaa24">
    <div class="activate_bg">
        <div class="activate_div">
            <img src="../img/close.png" alt="" class="close" onclick="closeTip()">
            <div class="activate_title">激活红包</div>
            <div class="activate_content">关注微信公众号“有礼付”，点击菜单栏“红包中心”，即可查看您的红包激活情况哦~</div>
            <div class="activity">
               激活红包
            </div>
        </div>
    </div>
<div class="zoomer" style="background-color: #ffaa24;overflow-y: scroll;position: fixed;">
    <div class="content">
        <div class="index_bg">
            <div class="redNew_bg">
                <div class="redContent">
                </div>
            </div>

            <div class="index_contentText" style="">
                <div>
                    <div style="text-align: center">
                        <input id="tel" type="tel" placeholder="请输入您用于绑定办理信用卡的手机号码" maxlength="11" oninput="OnInput (event)" onpropertychange="OnPropChanged (event)">
                    </div>
                    <div style="width: 90%;margin: 15px auto;">
                        <input id="code" style="width: 60%;" id="code" type="tel" placeholder="请输入您收到的短信验证码" maxlength="11" oninput="OnInput1 (event)" onpropertychange="OnPropChanged (event)">
                        <span onclick="setValTime(this)" class="codeText">获取验证码</span>
                        <span id="timer" class="codeText"></span>
                    </div>

                </div>
                <div class="activate_content_text">
                亲,本优惠活动仅限通过本页面"申请办卡激活红包"按钮首次申请办理兴业银行信用卡的用户哦.老用户活动很快会有的,谢谢参与!
                </div>
                <div class="tips_parent">
            <ul class="flex">
            <li class="tips_text">
            Tips
            </li>
            <li class="flex-1 tips_contents">
            您拿到兴业银行信用卡后,只需在任意消费场景下首刷20元即可激活50元加油支付红包.如有问题,请详询客服电话:4000808065
            </li>
            </ul>
                    <div style="margin-top: 15px">
                        <img style="margin-top: 20px;"  id="no" src="img/submit123.png" alt="" width="100%">
                        <img style="margin-top: 20px;display: none;" id="yes" src="img/activate123.png" alt="" width="100%" onclick="activate()">
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>