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
    <title>提现至加油卡</title>
    <link type="text/css" href="css/common/common.css" rel="stylesheet">
    <link type="text/css" href="css/moneyTocard.css" rel="stylesheet">
    <!--<script type="text/javascript" src="js/common/jquery-1.7.2.min.js"></script>-->
    <!--<script type="text/javascript" src="js/common/m.tool.juxinbox.com.js"></script>-->
    <script type="text/javascript" src="js/common/jQuery-1.11.3.js"></script>
    <script type="text/javascript" src="js/common/jWeChat-Adaptive.js"></script>
    <script type="text/javascript" src="js/common/m.tool.juxinbox.com.js"></script>
    <script type="text/javascript" src="js/common/jWeChat-1.0.0.js"></script>
    <script type="text/javascript" src="js/moneyTocard.js"></script>
    <script type="text/javascript" src="js/common/common.js"></script>

</head>
<body>
<div class="zoomer">
    <div class="content">
        <div class="top">
            <h1>请选择提现金额</h1>
            <p class="tip">非会员每月提额不得超过500元，单笔提额不低于100元会员每月提额不限，单笔提额不超过500元</p>
        </div>

        <div class="tipsIm">
            <div class="picTip">
                <img src="img/noserche.png" width="350" height="350"  alt=""/>
            </div>
            <p class="sorry">很抱歉，暂时还没有任何记录</p>
        </div>
        <div class="selectRed" >





            <!--<div class="redMoney">-->
                <!--<input type="checkbox" id="checkBox1" money="500.00" class="checkBoxBox1" value="ddvfv" name="redbage" onchange="send()">-->
                <!--<label for="checkBox1" class="checkBox1">-->
                    <!--<div class="redMoney1">-->
                        <!--<div class="top2">红包金额：￥<span class="redMoney2" id="redMoney1">2</span></div>-->
                        <!--<div class="comeFrom1">来自<span id="comeFrom">XXX</span>活动的加油红包</div>-->
                        <!--<div class="time1" id="time1">2015-10-18 14:58:00</div>-->
                        <!--<div class="state1">未提现</div>-->
                    <!--</div>-->
                <!--</label>-->
            <!--</div>-->






        </div>

            <!--<div class="selectRed" >-->
                <!--<div class="redMoney">-->
                    <!--<input type="checkbox" id="checkBox2" money="2.00" class="checkBoxBox1" value="11" name="redbage" onchange="send()">-->
                    <!--<label for="checkBox2" class="checkBox1" >-->
                        <!--<div class="redMoney1">-->
                            <!--<div class="top2">红包金额：￥<span class="redMoney2">2</span></div>-->
                            <!--<div class="comeFrom1">购买的加油红包</div>-->
                            <!--<div class="time1" id="time2">2015-10-18 14:58:00</div>-->
                            <!--<div class="state1">未提现</div>-->
                        <!--</div>-->
                    <!--</label>-->
                <!--</div>-->
            <!--</div>-->


        <div class="protocol">
            <input type="checkbox" id="checkBox" class="checkBoxBox" checked="checked">
            <label for="checkBox" class="checkBox">同意《车答应易加油服务条款》</span></label><br>
            <div class="readAgreement">阅读协议</div>
        </div>

        <div class="pay">提取金额：<span id="totalMoney" class="money">0.00</span>元</div>
        <div class="apply">立即申请</div>



    </div>
</div>
<div class="fooot" style="width: 100%;height: 104px;background: rgba(27, 36, 34, 0.89);position: fixed;bottom: 0px;">
    <div class="perCenter">
    <div class="touXiang" style="width: 76px;height: 76px;border-radius: 100px;overflow: hidden;position: absolute;top: 14px;left: 42px;">
        <!--<img src="img/erweima.png" width="76" height="76" alt=""/>-->
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