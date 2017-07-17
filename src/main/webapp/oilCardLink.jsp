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
    <title>绑定油卡</title>
    <link type="text/css" href="css/common/common.css" rel="stylesheet">
    <link type="text/css" href="css/oilCardLink.css" rel="stylesheet">

    <!--<script type="text/javascript" src="js/common/jquery-1.7.2.min.js"></script>-->
    <!--<script type="text/javascript" src="js/common/m.tool.juxinbox.com.js"></script>-->
    <script type="text/javascript" src="js/common/jQuery-1.11.3.js"></script>
    <script type="text/javascript" src="js/common/jWeChat-Adaptive.js"></script>
    <script type="text/javascript" src="js/common/m.tool.juxinbox.com.js"></script>
    <script type="text/javascript" src="js/common/jWeChat-1.0.0.js"></script>
    <script type="text/javascript" src="js/oilCardLink.js"></script>
    <script type="text/javascript" src="js/common/common.js"></script>



</head>
<body>
<div class="zoomer">
    <div class="content">
        <div class="top">
            <h1>请先绑定您的油卡</h1>
            <h2>绑定油卡后，您的加油红包金额将提现至该油卡中</h2>
        </div>

        <div class="form">
            <div class="information1">
                <p ><span class="red f_28">*</span>加油卡号</p>
                <input type="text" id="oilCard" maxlength="19" placeholder="请如实填写，便于我们后期跟踪回访..."/>
                <p ><span class="red f_28">*</span>加油卡关联手机号</p>
                <input type="text" id="linkPhone" maxlength="11" placeholder="请如实填写，便于我们后期跟踪回访..."/>
                <p ><span class="red f_28">*</span>加油卡关联姓名</p>
                <input type="text" id="linkName" maxlength="5" placeholder="请如实填写，便于我们后期跟踪回访..."/>
            </div>

            <div class="protocol">
                <input type="checkbox" id="checkBox" class="checkBoxBox" checked="checked">
                <label for="checkBox" style="color: #666666;" class="checkBox">同意<span class="xieYi">《车答应易加油服务条款》</span></label><br>
                <div class="readAgreement">阅读协议</div>
            </div>

        </div>
        <div class="yes">立即绑定</div>

    </div>
</div>
<div class="fooot" style="width: 100%;height: 104px;background: rgba(27, 36, 34, 0.89);position: fixed;bottom: 0px;">
    <div class="perCenter">
    <div class="touXiang" style="width: 76px;height: 76px;border-radius: 100px;overflow: hidden;margin: 11px 45px;">
        <!--<img src="img/erweima.png" width="76" height="76" alt=""/>-->
    </div>
    <div class="name" style="font-size: 30px;color: white;position: absolute;top: 4px;left: 150px;"></div>
    <div class="phone" style="font-size: 30px;color: white;position: absolute;top: 46px;left: 144px;"></div>
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