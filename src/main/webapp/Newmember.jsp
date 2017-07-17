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
    <title>确认充值信息</title>
    <link type="text/css" href="css/common/common.css" rel="stylesheet">
    <link type="text/css" href="css/Newmember.css" rel="stylesheet">
    <!--<script type="text/javascript" src="js/common/jquery-1.7.2.min.js"></script>-->
    <!--<script type="text/javascript" src="js/common/m.tool.juxinbox.com.js"></script>-->
    <script type="text/javascript" src="js/common/jQuery-1.11.3.js"></script>
    <script type="text/javascript" src="js/common/jWeChat-Adaptive.js"></script>
    <script type="text/javascript" src="js/common/m.tool.juxinbox.com.js"></script>
    <script type="text/javascript" src="js/common/jWeChat-1.0.0.js"></script>
    <script type="text/javascript" src="js/Newmember.js"></script>
    <script type="text/javascript" src="js/common/common.js"></script>

    <script type="text/javascript" >

    </script>

</head>
<body>
<div class="zoomer">
    <div class="content">
    <div class="top">
        <h1>欢迎加入车答应会员</h1>
        <h2>（首次加油成功即可成为车答应会员）</h2>
    </div>
    <div class="selectWay">
        <p>是否使用已有加油卡充值</p>
        <select name="isWhich" class="isWhich" id="isWhich" onchange="chance()">
            <option value="0">暂无加油卡，我要办车答应加油卡</option>
            <option value="1">已有加油卡，我要绑定自己的油卡</option>
        </select>
        <p class="tip1">您选择了办理车答应加油卡，我们将在确认支付成功后的15个工作日内为您寄出油卡！</p>
        <p class="tip2">您选择了绑定自有加油卡，我们将在确认支付成功后的次月起每月20日为您充值到账！</p>
    </div>
    <div class="form">
            <div class="information">
                <p >*收件人</p>
                <input type="text" id="name" maxlength="5" placeholder="请如实填写，便于我们后期跟踪回访..."/>
                <p >*收件人联系电话</p>
                <input type="text" id="phone" maxlength="11" placeholder="请如实填写，便于我们后期跟踪回访..."/>
                <p >*寄送地址</p>
                <input type="text" id="address" placeholder="请如实填写，便于我们后期跟踪回访..."/>
            </div>
            <div class="information1">
                <p >*加油卡号</p>
                <input type="text" id="oilCard" maxlength="18" placeholder="请如实填写，便于我们后期跟踪回访..."/>
                <p >*加油卡关联手机号</p>
                <input type="text" id="linkPhone" maxlength="11" placeholder="请如实填写，便于我们后期跟踪回访..."/>
                <p >*加油卡关联姓名</p>
                <input type="text" id="linkName" maxlength="5" placeholder="请如实填写，便于我们后期跟踪回访..."/>
            </div>

            <div class="protocol">
                <input type="checkbox" id="checkBox" class="checkBoxBox">
                <label for="checkBox" class="checkBox">同意《车答应易加油服务条款》</label><br>

            </div>

        </div>
    <div class="yes">立即充值</div>

    </div>
</div>
<div class="fooot" style="width: 100%;height: 104px;background: rgba(27, 36, 34, 0.89);position: fixed;bottom: 0px;">
    <div class="touXiang" style="width: 76px;height: 76px;border-radius: 100px;overflow: hidden;margin: 11px 45px;">
        <!--<img src="img/erweima.png" width="76" height="76" alt=""/>-->
    </div>
    <div class="name" style="font-size: 30px;color: white;position: absolute;top: 4px;left: 150px;"></div>
    <div class="phone" style="font-size: 30px;color: white;position: absolute;top: 46px;left: 144px;"></div>
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