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
    <title>一键支付</title>
    <link type="text/css" href="css/common/common.css" rel="stylesheet">
    <link type="text/css" href="css/oneKeyPay.css" rel="stylesheet">
    <script type="text/javascript" src="js/common/jquery-1.7.2.min.js"></script>
    <script type="text/javascript" src="js/common/m.tool.juxinbox.com.js"></script>
    <script type="text/javascript" src="js/common/common.js"></script>
    <script type="text/javascript" src="js/oneKeyPay.js"></script>
</head>
<body>
<div class="zoomer">
    <div class="content">
       <div class="top">
           <h1>付款给车答应</h1>
           <p class="money">￥<span class="haoMuch">2,700.00</span></p>
       </div>
       <div class="bankCard">
           <p class="cardNum">卡　号</p>
           <input  type="text" placeholder="请输入信用卡或储蓄卡卡号" style="border:black "/>
       </div>
        <div class="pay">一键支付付款</div>
        <div class="chaBank">查看支持银行列表</div>
        <div class="tip">本服务由易宝支付(yeepay)提供<br/>易宝支付客服电话：<span class="green f_23">4001-500-800</span></div>












    </div>

</div>
<div class="fooot" style="width: 100%;height: 104px;background: rgba(27, 36, 34, 0.89);position: fixed;bottom: 0px;">
    <div class="touXiang" style="width: 76px;height: 76px;border-radius: 100px;overflow: hidden;position: absolute;top: 14px;left: 42px;">
        <img src="img/erweima.png" width="76" height="76" alt=""/>
    </div>
    <div class="name"style="font-size: 30px;color: white;position: absolute;top: 22px;left: 150px;">小美队长</div>
    <div class="phone" style="font-size: 30px;color: white;position: absolute;top: 62px;left: 144px;">1526569999</div>
    <div class="pic" style="width: 74px;height: 74px;border-radius: 18px;overflow: hidden;position: absolute;right: 46px;top: 14px;border: 1px solid #fff;">
        <img src="img/icon_coupon_80.png" width="74" height="74" alt=""/>
    </div>
    <div class="pic1" style="width: 74px;height: 74px;border-radius: 18px;overflow: hidden;position: absolute;right: 152px;top: 14px;border: 1px solid #fff;">
        <img src="img/u18.png" width="74" height="74" alt=""/>
    </div>
    <div class="yuanQuan" style="width: 38px;height: 38px;border-radius: 100px;position: absolute;background: #ef3831;top: 2px;right: 34px;"></div>

</div>

</body>
</html>