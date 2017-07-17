<%@ page language="java" import="java.util.*" isELIgnored="false" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en" style="background: #f2f2f2">
<head>
    <meta charset="UTF-8">
    <meta http-eqiv="X-UA-Compatible" content="IE=Edge,chrome=1">
    <!--如果IE用户安装了Chrome Frame插件，则使用这个插件渲染页面，否则用IE最新的、最好的引擎来渲染页面-->
    <meta content="yes" name="apple-mobile-web-app-capable">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <title>我的加油红包</title>
    <link type="text/css" href="css/common/common.css" rel="stylesheet">
    <link type="text/css" href="css/addSearch.css" rel="stylesheet">
    <!--<script type="text/javascript" src="js/common/jquery-1.7.2.min.js"></script>-->
    <!--<script type="text/javascript" src="js/common/m.tool.juxinbox.com.js"></script>-->
    <script type="text/javascript" src="js/common/jQuery-1.11.3.js"></script>
    <script type="text/javascript" src="js/common/jWeChat-Adaptive.js"></script>
    <script type="text/javascript" src="js/common/m.tool.juxinbox.com.js"></script>
    <script type="text/javascript" src="js/common/jWeChat-1.0.0.js"></script>
    <script type="text/javascript" src="js/addSearch.js"></script>
    <script type="text/javascript" src="js/common/common.js"></script>

</head>
<body>
<div class="zoomer">
    <div class="content">
        <div class="part0">
            <p class="oil">提取至油卡</p>
            <p class="oilRecord">提现记录</p>

        </div>

        <div class="parts">
            <div class="kong"></div>
        <!--<div class="part1">-->
            <!--<p>距下一次到账</p>-->
            <!--<p><span class="leftDays red">28</span>天</p>-->
        <!--</div>-->
        <div class="part2">
            <p>待发放加油红包余额</p>
            <p> <span class="green f_w">￥</span><span class="leftMoney green f_w"></span></p>
        </div>
        <div class="part3">
            <p>累计获得加油红包</p>
            <p><span class="green f_w">￥</span><span class="totalMoney green f_w"></span>+<span class="thisMoney red f_w"></span></p>
        </div>
        <div class="part4">
            <p>累计享受优惠</p>
            <p><span class="green f_w">￥</span><span class="totalGet green f_w"></span></p>
        </div>
        <div class="part5">
            <div class="jiLu">购买记录</div>
            <div class="addOil">领取记录</div>
        </div>

        </div>
        <div class="tips f_20" style="font-size: 18px;"><span class="red f_24" style="font-size: 18px;">温馨提示</span>：成功充值后，次月起系统将自动按照您购买的套餐协议进行定期返还。如您多次购买套餐，系统会每月自动叠加计算返还。<br />如有任何疑问，请致电客服：<span class="red f_24">400-8828-170</span> 咨询。</div>




    </div>


</div>
<div class="fooot" style="width: 100%;height: 104px;background: rgba(27, 36, 34, 0.89);position: fixed;bottom: 0px;">
    <div class="perCenter">
    <div class="touXiang" style="width: 76px;height: 76px;border-radius: 100px;overflow: hidden;margin: 11px 45px;">
        <!--<img src="img/erweima.png" width="76" height="76" alt=""/>-->
    </div>
    <div class="name" style="font-size: 30px;color: white;position: absolute;top: 22px;left: 150px;"></div>
    <div class="phone" style="font-size: 30px;color: white;position: absolute;top: 58px;left: 144px;"></div>
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