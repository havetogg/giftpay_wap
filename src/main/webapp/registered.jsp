<%@ page language="java" import="java.util.*" isELIgnored="false" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en" style="background:#f2f2f2">
<head>
    <meta charset="UTF-8">
    <meta http-eqiv="X-UA-Compatible" content="IE=Edge,chrome=1">
    <!--如果IE用户安装了Chrome Frame插件，则使用这个插件渲染页面，否则用IE最新的、最好的引擎来渲染页面-->
    <meta content="yes" name="apple-mobile-web-app-capable">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <title>注册页面</title>
    <link type="text/css" href="css/common/common.css" rel="stylesheet">
    <link type="text/css" href="css/registered.css" rel="stylesheet">
    <!--<script type="text/javascript" src="js/common/jquery-1.7.2.min.js"></script>-->
    <!--<script type="text/javascript" src="js/common/m.tool.juxinbox.com.js"></script>-->
    <script type="text/javascript" src="js/common/jQuery-1.11.3.js"></script>
    <script type="text/javascript" src="js/common/jWeChat-Adaptive.js"></script>
    <script type="text/javascript" src="js/common/m.tool.juxinbox.com.js"></script>
    <script type="text/javascript" src="js/common/jWeChat-1.0.0.js"></script>
    <script type="text/javascript" src="js/registered.js"></script>
    <script type="text/javascript" src="js/common/common.js"></script>

</head>
<body>
<div class="zoomer">
    <div class="alertTips" style="background-color:rgba(10,10,10,0.6);width:100%;height:130%;position:fixed;top:0;left:0;z-index:1100;display:none;">
        <div class="alertInside" style="background-color:#fff;width:560px;min-height:204px;position:absolute;left: 43%;margin-left: -246px;top: 25%;margin-top:-50px;border-radius:11px;">
            <p style="font-size: 30px; line-height: 38px;color: #000000; width: 520px; margin: 40px auto 0;">恭喜您，注册成功！即将跳转到您要访问的页面~</p>
        </div>
    </div>

    <div class="content">
        <h1>欢迎访问车答应</h1>
        <h2>您好，免费注册后即可继续使用！</h2>
        <div class="form">
            <p><span class="red f_26">*</span>手机号码</p>
            <input type="text" id="phone" class="phone" maxlength="11"/>
            <p><span class="red f_26">*</span>验证码</p>
            <input type="text" id="code" class="code" style="width: 544px" maxlength="6"/>
            <p>推荐码</p>
            <input type="text" id="recommend" class="code" style="width: 544px" maxlength="11"/>
            <div class="btn_3" style="display: block">发送验证码</div>
            <div class="btn_4" style="display: none">60秒</div>
        </div>
        <div class="tip">若无推荐人，推荐码可不填写</div>
        <div class="jiHuo">注册</div>


    </div>


</div>

</body>
</html>