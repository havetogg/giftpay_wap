﻿<%@ page language="java" import="java.util.*" isELIgnored="false" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="initial-scale=0.5,maximum-scale=0.5,minimum-scale=0.5, width=640, target-densitydpi=device-dpi">
    <meta http-eqiv="X-UA-Compatible" content="IE=Edge,chrome=1">
    <meta content="yes" name="apple-mobile-web-app-capable">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <title>支付结果</title>
    <link type="text/css" href="css/common/common.css" rel="stylesheet">
    <link type="text/css" href="css/app.css" rel="stylesheet">
    <link type="text/css" href="css/style.css" rel="stylesheet">
    <link type="text/css" href="css/flexslider.css" rel="stylesheet">
    <script type="text/javascript" src="js/common/jQuery-1.11.3.js"></script>
    <script type="text/javascript" src="js/common/jWeChat-Adaptive.js"></script>
    <script type="text/javascript" src="js/common/m.tool.juxinbox.com.js"></script>
    <!--<script type="text/javascript" src="js/common/jWeChat-1.0.0.js"></script>-->
    <script type="text/javascript" src="js/common/common.js"></script>
    <script type="text/javascript" src="js/jquery.flexslider-min.js"></script>
    <style>
        .success_bg {
            width: 100%;
            height: 100%;
            background-color: rgba(0, 0, 0, 0.5);
            z-index: 999;
            position: fixed;
            display: none;
        }

        .success_tip {
            width: 80%;
            background-color: #fff;
            margin: 30% auto;
            padding: 60px 40px;
            border-radius: 5px;
            box-sizing: border-box;
            position: relative;
        }

        .success_content {
            font-size: 24px;
            color: #888888;
            font-family: 'Microsoft YaHei';
            width: 80%;
            margin: 60px auto;
        }

        .success_tittle {
            text-align: center;
            font-size: 30px;
            color: #000;
            font-family: 'Microsoft YaHei';
        }

        .iknow {
            font-size: 24px;
            background-color: #ff5224;
            color: #fff;
            text-align: center;
            height: 70px;
            line-height: 70px;
            font-family: 'Microsoft YaHei';
            border-radius: 5px;
        }

        .close_ {
            position: absolute;
            right: 30px;
            top: 30px;
            width: 60px;
        }
    </style>
</head>

<script>
    function onBridgeReady() {
        WeixinJSBridge.call('hideOptionMenu');
    }
    if (typeof WeixinJSBridge == "undefined") {
        if (document.addEventListener) {
            document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);
        } else if (document.attachEvent) {
            document.attachEvent('WeixinJSBridgeReady', onBridgeReady);
            document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
        }
    } else {
        onBridgeReady();
    }
    function close_tip() {
        $('.success_bg').hide();
    }
    function iKnow() {
        location.href = 'https://ccshop.cib.com.cn:8010/application/cardapp/cappl/ApplyCard/toSelectCard?id=40cd0bf21a6c4bce99a82befd7e68c7b';
    }

    function getRedPhone() {
       /* $.ajax({
            type: "GET",
            url: getRootPath() + "/giftpay/wallet/getRedPhone.htm",
            dataType: "json",
            success: function (data) {
                $(".success_content").html("请用" + data.mess + "的号码办理兴业银行信用卡，否则支付红包无法激活哦！");
            }, error: function (res) {
                alert(JSON.stringify(res));
            }
        });*/
        $(".success_content").html("请用${phoneNum}的号码办理兴业银行信用卡，否则支付红包无法激活哦！");
    }
</script>
<script>
    $(function () {
        //2
        $(".flexslider").flexslider({
            animation: "slide", //String: Select your animation type, "fade" or "slide"图片变换方式：淡入淡出或者滑动
            slideshowSpeed: 4000, //展示时间间隔ms
            animationSpeed: 1100, //滚动时间ms
            touch: true //是否支持触屏滑动
        });
        $(".jmt_center").on("click", function () {
            $('.success_bg').show();
        });
        getRedPhone();
    })
</script>
<body style="height: 1250px;">
<div class="success_bg">
    <div class="success_tip">
        <img src="img/x.png" alt="" class="close_" onclick="close_tip()">
        <div class="success_tittle">激活提示</div>
        <div class="success_content">请用的号码办理兴业银行信用卡，否则支付红包无法激活哦！</div>
        <div class="iknow" onclick="iKnow()">我知道了</div>
    </div>
</div>
<div class="zoomer" style="background-color: #ffffff;height: 1250px;">
    <div style="margin: 80px auto 200px;">
        <div class="jmt_center">
            <img src="img/red_success.png" alt="">
        </div>
        <div class="paySuccess_content">
        <!--    恭喜您获得 <a class="specialText">兴业银行提供的50元加油支付红包</a>！点击按钮激活您的支付红包-->
            <!--<a class="specialText">（活动仅限首次办理兴业银行信用卡用户）</a>-->
            亲，您有一个50元加油支付红包等待激活哟！点击按钮激活您的支付红包
        </div>
        <div class="jmt_center">
            <img src="img/activate.png" alt="">
        </div>

    </div>
    <div class="index_content12">
    <ul class="flex">
    <li class="flex-1 tuijian_line"></li>
    <li class="tuiguang_">推广</li>
    <li class="flex-1 tuijian_line"></li>
    </ul>
    </div>

    <a class="index_content6" href="http://m.linkgift.cn/index.html">
    <img src="img/banner3.png" alt="" width="100%">
    </a>
    <div class="jmt_center pay_bottom_tel">
    <a  style="padding-left: 12px;padding-right: 12px;"  href="tel:4000808065">推广合作</a>
    </div>
</div>
</body>
</html>