<%@ page language="java" import="java.util.*" isELIgnored="false" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-eqiv="X-UA-Compatible" content="IE=Edge,chrome=1">
    <!--如果IE用户安装了Chrome Frame插件，则使用这个插件渲染页面，否则用IE最新的、最好的引擎来渲染页面-->
    <meta content="yes" name="apple-mobile-web-app-capable">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <title>会员激活</title>
    <link type="text/css" href="css/common/common.css" rel="stylesheet">
    <link type="text/css" href="css/activation.css" rel="stylesheet">
    <!--<script type="text/javascript" src="js/common/jquery-1.7.2.min.js"></script>-->
    <script type="text/javascript" src="js/common/jQuery-1.11.3.js"></script>
    <script type="text/javascript" src="js/common/jWeChat-Adaptive.js"></script>
    <script type="text/javascript" src="js/common/m.tool.juxinbox.com.js"></script>
    <script type="text/javascript" src="js/common/jWeChat-1.0.0.js"></script>
    <script type="text/javascript" src="js/activation.js"></script>

</head>
<body>
<div class="zoomer">
    <div class="alertTips" style="background-color:rgba(10,10,10,0.6);width:100%;height:100%;position:fixed;top:0;left:0;z-index:1100;display:none;">
        <div class="alertInside" style="background-color:#fff;width: 479px;min-height:204px;position:absolute;left: 43%;margin-left: -202px;top: 25%;margin-top:-102px;border-radius:11px;">
            <p style="font-size: 23px;margin: 24px 28px;line-height: 29px;width: 420px;color: #666666;">亲，您该微信账号<span class="f_23 red">已</span>使用号码为<br/><span class="f_23 red oldPhone">15195877336</span>的手机号进行<span class="f_23 red">注册过</span>，请<span class="f_23 red">更换微信号</span>进行<span class="f_23 green">激活</span>。</p>
            <p style="font-size: 18px;line-height: 25px;width: 420px;margin: 0px 28px;color: #666666;">ips：如您需继续使用当前微信号进行激活，请点击下方“更换注册用户”按钮。我们将向您<span class="f_18 red oldPhone">15195877336</span>的手机号发送验证短信，当您通过验证后可继续激活。</p>
            <p style="font-size: 18px;line-height: 25px;width: 420px;margin: 10px 28px;color: #666666;">激活成功后，你注册号码为<span class="f_18 red oldPhone" >15195877336</span>的账户可通过其他微信号激活或通过APP“车答应”登录使用。</p>
            <!--<div class="changeUser">更换注册用户</div>-->
            <div class="changeWx"> 算了，换个微信号吧！</div>
        </div>
    </div>



    <div class="alertTip1" style="background-color:rgba(10,10,10,0.6);width:100%;height:100%;position:fixed;top:0;left:0;z-index:1100;display:none;">
        <div class="alertInside" style="background-color:#fff;width: 519px;min-height: 254px;position:absolute;left: 43%;margin-left: -224px;top: 25%;margin-top:-102px;border-radius:11px;">
            <input type="text" id="code1" class="code" maxlength="6" placeholder="请输入您收到的验证码"/>
            <div class="btn_3" style="display: block">重新发送验证码</div>
            <div class="btn_4" style="display: none">60秒</div>
            <div class="sureChange">确定更换</div>
            <p style="font-size: 22px;color: red;margin: 74px 43px;position: absolute;width: 500px;height: 30px;">更换后该微信号不再能查看到原先账户信息</p>
        </div>
    </div>






    <div class="content">
        <div class="top">
            <h1>激活说明</h1>
            <div><p class="tip">如您是通过 <span class="red f_26">第三方平台注册</span>成为车答应会员，请在下方输入您 <span class="red f_26">注册时使用的手机号</span>进行 <span class="red f_26">激活</span>。</p></div>
        </div>
        <div class="form">
            <p>手机号码</p>
            <input type="text" id="phone" class="phone" maxlength="11"/>
            <p>验证码</p>
            <input type="text" id="activaCode" class="code" maxlength="6"/>
            <div class="btn_3" style="display: block">发送验证码</div>
            <div class="btn_4" style="display: none">60秒</div>
        </div>
        <div class="jiHuo">激活</div>



    </div>
</div>
<div class="fooot" style="width: 100%;height: 104px;background: rgba(27, 36, 34, 0.89);position: fixed;bottom: 0px;">
    <div class="perCenter">
    <div class="touXiang" style="width: 76px;height: 76px;border-radius: 100px;overflow: hidden;position: absolute;top: 14px;left: 42px;">
        <img src="img/erweima.png" width="76" height="76" alt=""/>
    </div>
    <div class="name"style="font-size: 30px;color: white;position: absolute;top: 22px;left: 150px;"></div>
    <div class="phone" style="font-size: 30px;color: white;position: absolute;top: 62px;left: 144px;"></div>
    </div>
    <div class="pic0" style="width: 74px;height: 74px;border-radius: 18px;overflow: hidden;position: absolute;right: 214px;top: 14px;border: 1px solid #fff;">
        <img src="img/home.png" width="74" height="74" alt=""/>
    </div>
    <div class="pic" style="width: 74px;height: 74px;border-radius: 18px;overflow: hidden;position: absolute;right: 27px;top: 14px;border: 1px solid #fff;">
        <img src="img/icon_coupon_80.png" width="74" height="74" alt=""/>
    </div>
    <div class="pic1" style="width: 74px;height: 74px;border-radius: 18px;overflow: hidden;position: absolute;right: 119px;top: 14px;border: 1px solid #fff;">
        <img src="img/u18.png" width="74" height="74" alt=""/>
    </div>
    <div class="yuanQuan" style="width: 38px;height: 38px;border-radius: 100px;position: absolute;background: #ef3831;top: 2px;right: 10px;"></div>
</div>

</body>
</html>