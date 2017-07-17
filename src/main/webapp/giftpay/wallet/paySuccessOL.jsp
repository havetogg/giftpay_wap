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
</head>
<style>
		.paySuccess_content11 a {
			font-weight: bold;
			color: rgb(100, 100, 100);
		}
	</style>
<script>
    $(function () {
        $(".flexslider").flexslider({
            animation: "slide", //String: Select your animation type, "fade" or "slide"图片变换方式：淡入淡出或者滑动
            slideshowSpeed: 4000, //展示时间间隔ms
            animationSpeed: 1100, //滚动时间ms
            touch: true //是否支持触屏滑动
        });
        var redAmount=getUrlParam("redAmount");
        var realAmount=new Number(getUrlParam("realAmount"));
        var num = new Number(redAmount);
        num=num/100;
        
        $(".paySuccess_content11").html(" 需付" + ((num + realAmount).toFixed(2)) + "元，红包抵扣" + num.toFixed(2) + "元，<a>实付" 
			+ realAmount + "元</a>");
        $(".banner_bg").on("click",function(){
        	location.href="http://www.linkgift.cn/giftcenter/queryCouponTickerList";
        });
        $(".recommend").on("click",function(){
//        	location.href="http://m.linkgift.cn/detail.html?item_uid=2140281_159624";
            location.href="http://m.linkgift.cn/index.html";
        })
    })
</script>
<body >

<div class="zoomer" style="background-color: #ffffff;overflow-x: hidden;position: fixed" style="bak">
    <div style="margin: 25px;">
        <div class="jmt_center">
            <img src="img/paySuccess.png" alt="">
        </div>
        <div class="paySuccess_content11">
        </div>
        <div class="jmt_center">
            <img src="img/code2.png" alt="" width="250px" height="250px">
            <img src="img/code2.png" alt="" style="    position: absolute;top: 0;left: 0;z-index: 999;opacity: 0;">
        </div>
        <div class="paySuccess_subContent">长按二维码随时查看</div>
        <div class="paySuccess_subContent">你的红包和更多优惠</div>
    </div>
    <!--<div class="noneBlock">-->

    <!--</div>-->
    <%--<div class="index_content3">--%>
        <%--<ul class="banner_bg flex">--%>
            <%--<!--<li class="banner_bg_yuan">--%>
                <%--<lable class="iconY">￥</lable>--%>
                <%--<lable>200</lable>--%>
            <%--</li>--%>
            <%--<li class="flex-1 banner_bg_text">--%>
                <%--<div>恭喜您获得200元中石化易捷进口商城抵用券!</div>--%>
            <%--</li>-->--%>
        <%--</ul>--%>
    <%--</div>--%>
    <div class="index_content12">
        <ul class="flex">
            <li class="flex-1 tuijian_line"></li>
            <li class="tuiguang_">推广</li>
            <li class="flex-1 tuijian_line"></li>
        </ul>
    </div>

    <div class="index_content3 recommend">
        <img src="img/banner3.png" alt="" width="100%">
    </div>
    <div class="jmt_center pay_bottom_tel">
       <a  style="padding-left: 12px;padding-right: 12px;"  href="tel:4000808065">推广合作</a>
    </div>
</div>
</body>
</html>