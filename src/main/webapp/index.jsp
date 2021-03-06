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
    <title>首页</title>
    <link type="text/css" href="css/common/common.css" rel="stylesheet">
    <link type="text/css" href="css/index.css" rel="stylesheet">
    <!--<script type="text/javascript" src="js/common/jquery-1.7.2.min.js"></script>-->
    <!--<script type="text/javascript" src="js/common/m.tool.juxinbox.com.js"></script>-->
    <script type="text/javascript" src="js/common/jQuery-1.11.3.js"></script>
    <!--<script type="text/javascript" src="js/common/jWeChat-Adaptive.js"></script>-->
    <script type="text/javascript" src="js/common/m.tool.juxinbox.com.js"></script>
    <script type="text/javascript" src="js/common/jWeChat-1.0.0.js"></script>
    <script type="text/javascript" src="js/index.js"></script>
    <script type="text/javascript" src="js/common/common.js"></script>

</head>
<body>
<input id="wxId" type="hidden" value="${openId}">
    <div class="zoomer">
        <div class="quan"></div>
        <div class="content">
            <div class="banner"><img src="img/index_head.jpg"width="640"height="350" alt=""/></div>
            <div class="part1">
                <div class="returnBalance">￥<span class="returnMoney"></span><br>待发放加油红包</div>
                <div class="addOil"><img src="img/addOil.png" style="margin: 43px 0px;" width="56"  height="103" alt=""/></div>
                <div class="problem">？常见问题</div>
            </div>
            <div class="oldUser"><img src="img/oldUser.png" alt=""/></div>
            <div class="action">
                <div class="action1" id="ad_1"><img src="img/ad1.png"width="310"height="175" alt=""/></div>
                <div class="action1" id="ad_2"><img src="img/ad2.png"width="310"height="175" alt=""/></div>
                <div class="action1" id="ad_3"><img src="img/ad3.png"width="310"height="175" alt=""/></div>
                <div class="action1" id="ad_4"><img src="img/ad4.png"width="310"height="175" alt=""/></div>

            </div>



        </div>
    </div>
    <div class="fooot" style="width: 100%;height: 104px;background: rgba(27, 36, 34, 0.89);position: fixed;bottom: 0px;">
        <div class="perCenter">
        <div class="touXiang" style="width: 76px;height: 76px;border-radius: 100px;overflow: hidden;position: absolute;top: 14px;left: 42px;">
            <!--<img src="img/erweima.png" width="76" height="76" alt=""/>-->
        </div>
        <div class="name"style="font-size: 30px;color: white;position: absolute;top: 5px;left: 150px;"></div>
        <div class="phone" style="font-size: 30px;color: white;position: absolute;top: 44px;left: 144px;"></div>
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