
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
    <title>代办保养申请</title>
    <link type="text/css" href="css/common/common.css" rel="stylesheet">
    <link type="text/css" href="css/maintain.css" rel="stylesheet">
    <!--<script type="text/javascript" src="js/common/jquery-1.7.2.min.js"></script>-->
    <!--<script type="text/javascript" src="js/common/m.tool.juxinbox.com.js"></script>-->
    <script type="text/javascript" src="js/common/jQuery-1.11.3.js"></script>
    <script type="text/javascript" src="js/common/jWeChat-Adaptive.js"></script>
    <script type="text/javascript" src="js/common/m.tool.juxinbox.com.js"></script>
    <script type="text/javascript" src="js/common/jWeChat-1.0.0.js"></script>
    <script type="text/javascript" src="js/maintain.js"></script>
    <script type="text/javascript" src="js/common/common.js"></script>
    <script type="text/javascript">



//        var toggle = function(func1 , func2){
//            this.index = 0;
//            this.change = function(){
//                if(this.index == 0){
//                    console.log("this is function 1");
//                    func1();
//                    this.index++;
//                }else{
//                    console.log("this is function 2");
//                    func2();
//                    this.index--;
//                }
//            }
//        };
//
//        $(function(){
//
//            var t = new toggle(function(){
//                $(".c1").css("color","red");
//                $(".c1").css("border", "1px solid red");
//            },function(){
//                $(".c1").css("color","");
//                $(".c1").css("border", "1px solid ");
//            });
//
//            $(".c1").click(function(){
//                t.change();
//            });
//        });

    </script>
</head>
<body>
<div class="zoomer">
    <div class="content">
        <h1>代办保养申请</h1>
        <div class="tip">我们提供上门代办保养，请提供您的信息</div>
        <h2>代办类型*</h2>
        <div class="choseType">
            <label for="checkBox1" class="checkBox1"  >
                <div class="choseThis c1" >保养</div>
            </label>
            <label for="checkBox2" class="checkBox1" >
                <div class="choseThis c2" >年检</div>
            </label>
            <label for="checkBox3" class="checkBox1" >
                <div class="choseThis c3" >保险</div>
            </label>
            <label for="checkBox4" class="checkBox1" >
                <div class="choseThis c4">维修</div>
            </label>
            <input type="checkbox" hidden="hidden" id="checkBox1"  value="保养" name="choseTypes" onchange="changeColor()">
            <input type="checkbox" hidden="hidden" id="checkBox2"  value="年检" name="choseTypes" onchange="changeColor()">
            <input type="checkbox" hidden="hidden" id="checkBox3"  value="保险" name="choseTypes" onchange="changeColor()">
            <input type="checkbox" hidden="hidden" id="checkBox4"  value="维修" name="choseTypes" onchange="changeColor()">
        </div>

        <div class="phone11">
            <h2>联系电话*</h2>
            <input class="tle"  maxlength="11" type="text"/>
        </div>

        <div class="carType">
            <h2>保养车型*</h2>
            <input  class="type" type="text"/>
        </div>
        <div class="apply">预约</div>
        <div class="hr"></div>
        <div class="tip_2">
            <h2>预约须知</h2>
            <p>预约成功后，我们将以最快的速度联系您。</p>
        </div>










    </div>
</div>
<div class="fooot" style="width: 100%;height: 104px;background: rgba(27, 36, 34, 0.89);position: fixed;bottom: 0px;">
    <div class="perCenter">
    <div class="touXiang" style="width: 76px;height: 76px;border-radius: 100px;overflow: hidden;position: absolute;top: 14px;left: 42px;">
        <!--<img src="img/erweima.png" width="76" height="76" alt=""/>-->
    </div>
    <div class="name"style="font-size: 30px;color: white;position: absolute;top: 23px;left: 150px;"></div>
    <div class="phone" style="font-size: 30px;color: white;position: absolute;top: 61px;left: 144px;"></div>
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