
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
    <title>选择套餐</title>
    <link type="text/css" href="css/common/common.css" rel="stylesheet">
    <link type="text/css" href="css/Selectpackage.css" rel="stylesheet">
    <!--<script type="text/javascript" src="js/common/jquery-1.7.2.min.js"></script>-->
    <!--<script type="text/javascript" src="js/common/m.tool.juxinbox.com.js"></script>-->
    <script type="text/javascript" src="js/common/jQuery-1.11.3.js"></script>
    <script type="text/javascript" src="js/common/jWeChat-Adaptive.js"></script>
    <script type="text/javascript" src="js/common/m.tool.juxinbox.com.js"></script>
    <script type="text/javascript" src="js/common/jWeChat-1.0.0.js"></script>
    <script type="text/javascript" src="js/Selectpackage.js"></script>
    <script type="text/javascript" src="js/common/common.js"></script>

</head>
<body>
<div class="zoomer">
    <div class="content">
        <div class="top">
            <h1>欢迎加入车答应会员</h1>
            <p class="tip">（首次加油成功即可成为车答应会员）</p>
        </div>
        <div class="select1">
            <p>套餐额度：</p>
            <div class="quota">
            <label  for="howMuch1">
                    <div class="nu11 green white" >
                        3000
                    </div>
            </label>
            <label  for="howMuch2">
                <div class="nu12">
                    9000
                </div>
            </label>
            <label  for="howMuch3">
                <div class="nu13 ">
                    12000
                </div>
            </label>
                <input type="radio"  style="display: none" name="howMuch" id="howMuch1" value="3000" checked="checked"  />
                <input type="radio"  style="display: none" name="howMuch" id="howMuch2" value="9000"  />
                <input type="radio"  style="display: none" name="howMuch" id="howMuch3" value="12000"  />
            </div>

            </div>

        <div class="select2">
            <p>套餐额度：</p>
            <div class="time">
                <label  for="time1">
                    <div class="nu21 green white">
                       6个月
                    </div>
                </label>
                <label  for="time2">
                    <div class="nu22">
                        12个月
                    </div>
                </label>
                <label  for="time3">
                    <div class="nu23">
                        18个月
                    </div>
                </label>
                <input type="radio"  style="display: none" name="time" id="time1" value="6" checked="checked" />
                <input type="radio"  style="display: none" name="time" id="time2" value="12"  />
                <input type="radio"  style="display: none" name="time" id="time3" value="18"  />
            </div>

        </div>

        <div class="protocol">
            <input type="checkbox" id="checkBox" class="checkBoxBox">
            <label for="checkBox" class="checkBox">同意<span class="jiaYou">《车答应易加油服务条款》</span></label><br>
        </div>

        <div class="pay">实际支付：<span class="money">2700</span>元</div>
        <div class="apply">立即申请</div>



    </div>
</div>
<div class="fooot" style="width: 100%;height: 104px;background: rgba(27, 36, 34, 0.89);position: fixed;bottom: 0px;">
    <div class="touXiang" style="width: 76px;height: 76px;border-radius: 100px;overflow: hidden;position: absolute;top: 14px;left: 42px;">
        <!--<img src="img/erweima.png" width="76" height="76" alt=""/>-->
    </div>
    <div class="name"style="font-size: 30px;color: white;position: absolute;top: 22px;left: 150px;"></div>
    <div class="phone" style="font-size: 30px;color: white;position: absolute;top: 62px;left: 144px;"></div>
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