<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="initial-scale=0.5,maximum-scale=0.5,minimum-scale=0.5, width=640, target-densitydpi=device-dpi">
    <meta http-eqiv="X-UA-Compatible" content="IE=Edge,chrome=1">
    <meta content="yes" name="apple-mobile-web-app-capable">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <title>有礼付</title>

    <link type="text/css" href="css/common/common.css" rel="stylesheet">
    <link type="text/css" href="css/app.css" rel="stylesheet">
    <script type="text/javascript" src="js/common/jQuery-1.11.3.js"></script>
    <script type="text/javascript" src="js/common/common.js"></script>
    <script type="text/javascript" src="js/index.js"></script>
    <script type="text/javascript" src="js/common/jWeChat-Adaptive.js"></script>
    <script type="text/javascript" src="js/fastclick.min.js"></script>

    <%-- function errors ! please see it  --%>
    <script type="text/javascript" src="js/fuel_flow_myr.js" ></script>
    <script>

        <%--//因为项目没有正式启动，从页面 传递session ，后台获取数据--%>
         <%--<% session.setAttribute("openId","o4FD4v_eJboaoT6zIojPC7Xt4L54");%>--%>
        <%--<% session.setAttribute("userId","o4FD4v_eJboaoT6zIojPC7Xt4L54");%>--%>

        //全局变量 判断是否存在历史充值记录
        var HistoryCount  = 0;

        //商品goodsId
        var goodsId='';

        //流量
        var rechargeM = "";

        //充值流量手机号
        var tel='';
        //红包 value 和 redId
        var redValue = 0;
        var redId = "";
        // 价格 预付款
        var dealMoney ="";

        //流量订购-请求参数 【微聚api】
        var data2 ={
            mobile:"", //手机号码
            packCode:"",  //流量套餐编码
            number_desc:"",        //运营商
            rechargeM:"",     //商品goodsId
            dealMoney:"",
            realMoney:"",
            redPkgId:"",
            redPkgValue:"",
            rechargeType:"liuliang"
        }


        //删除当前历史记录中手机号 5条
        function delNumber(t){
            //获取当前删除的手机号码
           var number = $(t).parents("li").find("span").text();

            //删除当前对象 [parents向上遍历直到找到指定对象]
            $(t).parents("li").remove();

            $.ajax({
//              url: 'http://tdev.juxinbox.com/giftpay_wap/flow/delNumber.htm',
//              url: 'http://www.amumu.xin/giftpay_wap/flow/delNumber.htm',
                url:getRootPath()+'/flow/delNumber.htm',
//                url:'/flow/delNumber.htm',
                type: 'POST',
                data: {number:number},
                success: function () {
                    console.log("删除成功！");

                    //删除成功之后，查询数据库当前数据是否为0，如果为0则隐藏div，大于0显示
                    $.ajax({
//                    url: 'http://tdev.juxinbox.com/giftpay_wap/flow/getFlowNumber5.htm',
//                    url: 'http://www.amumu.xin/giftpay_wap/flow/getFlowNumber5.htm',
                    url:getRootPath()+'/flow/getFlowNumber5.htm',
//                        url:'/flow/getFlowNumber5.htm',
                        type: 'POST',
                        dataType: 'json',
                        success: function (json) {

                            if(json.length>0){
                                console.log("不隐藏div，数据库依然存在大于0条的数据");

                            }else{
                                console.log("隐藏DIV,数据库不存在数据");
                                $('.historyNumber').hide();
                                HistoryCount = 0;
                                console.log("更新全局变量HouistoryCnt:"+0)
                            }


                        }
                    })
                }
            })

        }

        //删除全部历史充值记录
        function delAllFive(){
            $.ajax({
//                url: 'http://tdev.juxinbox.com/giftpay_wap/flow/delFiveNumber.htm',
//                url: 'http://www.amumu.xin/giftpay_wap/flow/delFiveNumber.htm',
                url:getRootPath()+'/flow/delFiveNumber.htm',
//                url:'/flow/delFiveNumber.htm',
                type: 'POST',
                success: function () {
//                    $("#historyNumber").find("li").remove();

                    //删除全部历史充值数据 直接隐藏历史记录界面
                    $('.historyNumber').hide();
                    HistoryCount = 0;


                }
            })
        }

        //查询历史充值手机流量记录 5 条
        $(function () {

            $.ajax({
//                url: 'http://tdev.juxinbox.com/giftpay_wap/flow/getFlowNumber5.htm',
//                    url: 'http://www.amumu.xin/giftpay_wap/flow/getFlowNumber5.htm',
                url:getRootPath()+'/flow/getFlowNumber5.htm',
//                url:'/flow/getFlowNumber5.htm',
                type: 'POST',
                dataType: 'json',
                success: function (json) {

                    console.log("是否查询数据:"+json.length);

                    if(json.length>0){

                        HistoryCount=1;

                        for(var i in json){
                            var tr = $(" <li  >" +
                                "<span onclick='selectHistoryNumber(this)'>"+json[i].number+"</span>"+
                                "<label>"+json[i].number_desc+"</label>"+
                                "<a class='delete' id="+json[i].number+" onclick='delNumber(this)' href='javascript:;'></a>"+
                                "</li>");

                            $("#historyNumber").append(tr);
                        }

                    }


                }
            })

        })

        //红包
        $(function () {

            $.ajax({
//                url: 'http://tdev.juxinbox.com/giftpay_wap/flow/redGet.htm',
//                url: 'http://www.amumu.xin/giftpay_wap/flow/redGet.htm',
                url:getRootPath()+'/flow/redGet.htm',
//                url:'/flow/redGet.htm',
                type: 'POST',
                dataType: 'json',
                success: function (json) {

                    var length=-1;
                    try{
                         length = json["data"].length;
                    }catch(e) {
                          console.log(e);
                    }

                    if(length>0){
                        $("#sel").parent().find("span").html("请选择");

                        for(var i in json){
                            for(var o in json[i]){
                                var redNum = json[i][o].value; //价钱
                                var redId = json[i][o].redpkgId; //红包id
                                var redPackName = json[i][o].redpkgName;//红包名称

                                var tr = $(" <option    value='"+redId+"-"+redNum+"'>"+ redPackName+"</option>");
                                $("#sel").append(tr);
                            }
                        }

                    }else{
//                        $("#sel").parent().find("span").html("暂无抵用卷");
//                        console.log("暂无抵用卷");
//                        $('#sel').attr("disabled","disabled");

                        //如果不存在红包，不显示红包框架
                        $("#redPackDiv").hide();
                    }


                }
            })

        })

        //获取当前套餐代码 例如:y100010
        function getPackCode(things,numberDesc){
//            var taocan = String(things).substr(0,String(things).indexOf("M"));
            var taocan = String(things);
            var new_taocan = taocan.length>3?(taocan):(taocan.length==3?("0"+taocan):("00"+taocan));
//            if(String(numberDesc).indexOf("电信")>0){
//                new_taocan="10"+new_taocan;
//            }else if(String(numberDesc).indexOf("移动")>0){
//                new_taocan="10"+new_taocan;
//            }else {
//                new_taocan="10"+new_taocan;
//            }

            //替换if规则，现在充值opackCode运营商都一样
            new_taocan="10"+new_taocan;

            console.log("最终当前packCode:"+new_taocan)

            return new_taocan;
        }

        //立即支付 function
        function rechargeNow() {

            //为mobile参数赋值 手机号
            tel=$('#number').val().replace(/\s+/g, '');
            data2.mobile = tel;
            //更新参数 流量套餐
            data2.rechargeM = rechargeM;
            //商品goodsId
            data2.goodsId = goodsId;
            //运营商
            data2.number_desc =  $(".serviceCompany").text();
            //价格
            data2.dealMoney = dealMoney;
//            data2.dealMoney = "0.01";  //微信测试 0.01元
            //红包ID
            data2.redPkgId=redId;
            //红包金额
            data2.redPkgValue=redValue;
            //流量套餐代码
            data2.packCode = getPackCode(rechargeM,data2.number_desc);
            //实付款
            var price = data2.dealMoney-parseInt(data2.redPkgValue)<0?0:data2.dealMoney-parseInt(data2.redPkgValue);
            data2.realMoney = price;


            //进行判断 ， 通过判断执行ajax请求
            if(!tel){
                TipShow("请输入手机号",1000);
            }
            else if(!(/^1[34578]\d{9}$/.test(tel))){
                TipShow("请输入正确的手机号",1000);
            }
            else if(!rechargeM){
                console.log(rechargeM);
                TipShow("请选择充值金额",1000);
            }
            else{

                //打印查看data2数据
                console.log(data2);

                //传入数据到Session中
                $.ajax({
//                    url: getRootPath()+'/flow/addValueSession.htm',  //旧版本 正式
                    url: getRootPath()+'/flow/prePayFlow.htm',
                    type: 'POST',
                    data: data2,
                    success: function (json) {
                        json=JSON.parse(json);
                        //2017-06-05 重新修改跳转支付界面
                        if(json.success=='1'){
                            location.href=getRootPath()+"/giftpay/liftpayment/orderPay.html?payamount=" + json.payamount + "&rechargeType="+json.rechargeType+"&lifeType="+json.lifeType+"&redPkgId=" + json.redPkgId + "&redPkgValue=" + json.redPkgValue+"&payInfo="+json.payInfo;
                        }else{
                            alert(json.errMsg);
                        }
                    }
                })

            }
        }


        function eventHandle(e)
        {
            var e=e||window.event;
            var obj=e.target||e.srcElement;
            if(!obj.id){
                $('.historyNumber').hide();
            }
        }

        //初始化默认加载移动 充值流量页面[默认查询流量充值3大运营商流量套餐]
        initPhoneGoods("");


    </script>
</head>
<body   onclick="eventHandle(event)">
<div class="zoomer">
    <div class="tab_text">
        <%--<img src="img/arrowLeft.png" alt="" class="arrowLeft">--%>
        <div>流量充值</div>
        <div class="rechargeRecords" onclick="ToNumRecord()" >充值记录</div>
    </div>
    <div class="recharge_Content">
        <div class="recharge_Content_goods">
            <div class="serviceName">请输入需要充值的手机号</div>
            <div class="phone_num">
                <input id="number" type="tel" maxlength="13" class="phoneNumber" onkeyup="handle()" oninput="OnInput (event)" onpropertychange="OnPropChanged(event)">
                <!--历史充值手机号码-->
                <div     class="historyNumber">
	                <ul id="historyNumber" class="">

		            </ul>
		            <p>最多存在5条记录，请删除无用记录 <a class="" onclick="delAllFive()">删除全部</a></p>
	            </div>
            </div>
            <div id="companyName">
                <!--服务提供商-->
            </div>

            <!--流量套餐-->
            <div id="flowPackCode" class="cellPhoneNumberPrice">

                <%--<!--移动-->--%>
                <%--<div id="yidong">--%>
                    <%--<ul class="flex">--%>
                        <%--<li class="flex-1 jmt_center phone" onclick="selectAmount(this,'30M',4.85)">--%>
                            <%--<div>30M</div>--%>
                            <%--<div>售价：4.85元</div>--%>
                            <%--<img class="ico1" src="img/yellow_arrow.png" alt="">--%>
                            <%--<img class="ico2" src="img/gif.png" alt="">--%>
                            <%--<img class="ico3" src="img/gif1.png" alt="">--%>
                        <%--</li>--%>
                        <%--<li class="flex-1 jmt_center phone" onclick="selectAmount(this,'70M',9.65)">--%>
                            <%--<div>70M</div>--%>
                            <%--<div>售价：9.65元</div>--%>
                            <%--<img class="ico1" src="img/yellow_arrow.png" alt="">--%>
                            <%--<img class="ico2" src="img/gif.png" alt="">--%>
                            <%--<img class="ico3" src="img/gif1.png" alt="">--%>
                        <%--</li>--%>
                        <%--<li class="flex-1 jmt_center phone" onclick="selectAmount(this,'150M',19.2)">--%>
                            <%--<div>150M</div>--%>
                            <%--<div>售价：19.2元</div>--%>
                            <%--<img class="ico1" src="img/yellow_arrow.png" alt="">--%>
                            <%--<img class="ico2" src="img/gif.png" alt="">--%>
                            <%--<img class="ico3" src="img/gif1.png" alt="">--%>
                        <%--</li>--%>
                    <%--</ul>--%>

                    <%--<ul class="flex">--%>
                        <%--<li class="flex-1 jmt_center phone" onclick="selectAmount(this,'500M',28.8)">--%>
                            <%--<div>500M</div>--%>
                            <%--<div>售价：28.8元</div>--%>
                            <%--<img class="ico1" src="img/yellow_arrow.png" alt="">--%>
                            <%--<img class="ico2" src="img/gif.png" alt="">--%>
                            <%--<img class="ico3" src="img/gif1.png" alt="">--%>
                        <%--</li>--%>
                        <%--<li class="flex-1 jmt_center phone" onclick="selectAmount(this,'1024M',47.6)">--%>
                            <%--<div>1G</div>--%>
                            <%--<div>售价：47.6元</div>--%>
                            <%--<img class="ico1" src="img/yellow_arrow.png" alt="">--%>
                            <%--<img class="ico2" src="img/gif.png" alt="">--%>
                            <%--<img class="ico3" src="img/gif1.png" alt="">--%>
                        <%--</li>--%>
                        <%--<li style="visibility: hidden" class="flex-1 jmt_center phone" onclick="selectAmount(this,'1024M',40)">--%>
                            <%--<div>1G</div>--%>
                            <%--<div>售价：40元</div>--%>
                            <%--<img class="ico1" src="img/yellow_arrow.png" alt="">--%>
                            <%--<img class="ico2" src="img/gif.png" alt="">--%>
                            <%--<img class="ico3" src="img/gif1.png" alt="">--%>
                        <%--</li>--%>
                    <%--</ul>--%>
                <%--</div>--%>
               <%--<!--联通-->--%>
                <%--<div id="liantong">--%>
                    <%--<ul class="flex">--%>
                        <%--<li class="flex-1 jmt_center phone" onclick="selectAmount(this,'20M',2.98)">--%>
                            <%--<div>20M</div>--%>
                            <%--<div>售价：2.98元</div>--%>
                            <%--<img class="ico1" src="img/yellow_arrow.png" alt="">--%>
                            <%--<img class="ico2" src="img/gif.png" alt="">--%>
                            <%--<img class="ico3" src="img/gif1.png" alt="">--%>
                        <%--</li>--%>
                        <%--<li class="flex-1 jmt_center phone" onclick="selectAmount(this,'50M',5.9)">--%>
                            <%--<div>50M</div>--%>
                            <%--<div>售价：5.9元</div>--%>
                            <%--<img class="ico1" src="img/yellow_arrow.png" alt="">--%>
                            <%--<img class="ico2" src="img/gif.png" alt="">--%>
                            <%--<img class="ico3" src="img/gif1.png" alt="">--%>
                        <%--</li>--%>
                        <%--<li class="flex-1 jmt_center phone" onclick="selectAmount(this,'100M',9.85)">--%>
                            <%--<div>100M</div>--%>
                            <%--<div>售价：9.85元</div>--%>
                            <%--<img class="ico1" src="img/yellow_arrow.png" alt="">--%>
                            <%--<img class="ico2" src="img/gif.png" alt="">--%>
                            <%--<img class="ico3" src="img/gif1.png" alt="">--%>
                        <%--</li>--%>
                    <%--</ul>--%>

                    <%--<ul class="flex">--%>
                        <%--<li class="flex-1 jmt_center phone" onclick="selectAmount(this,'200M',14.75)">--%>
                            <%--<div>200M</div>--%>
                            <%--<div>售价：14.75元</div>--%>
                            <%--<img class="ico1" src="img/yellow_arrow.png" alt="">--%>
                            <%--<img class="ico2" src="img/gif.png" alt="">--%>
                            <%--<img class="ico3" src="img/gif1.png" alt="">--%>
                        <%--</li>--%>
                        <%--<li class="flex-1 jmt_center phone" onclick="selectAmount(this,'500M',29.5)">--%>
                            <%--<div>500M</div>--%>
                            <%--<div>售价：29.5元</div>--%>
                            <%--<img class="ico1" src="img/yellow_arrow.png" alt="">--%>
                            <%--<img class="ico2" src="img/gif.png" alt="">--%>
                            <%--<img class="ico3" src="img/gif1.png" alt="">--%>
                        <%--</li>--%>
                        <%--<li style="visibility: hidden" class="flex-1 jmt_center phone" onclick="selectAmount(this,'1024M',40)">--%>
                            <%--<div>1G</div>--%>
                            <%--<div>售价：40元</div>--%>
                            <%--<img class="ico1" src="img/yellow_arrow.png" alt="">--%>
                            <%--<img class="ico2" src="img/gif.png" alt="">--%>
                            <%--<img class="ico3" src="img/gif1.png" alt="">--%>
                        <%--</li>--%>
                    <%--</ul>--%>
                <%--</div>--%>
                <%--<!--电信-->--%>
                <%--<div id="dianxin">--%>
                    <%--<ul class="flex">--%>
                        <%--<li class="flex-1 jmt_center phone" onclick="selectAmount(this,'30M',4.7)">--%>
                            <%--<div>30M</div>--%>
                            <%--<div>售价：4.7元</div>--%>
                            <%--<img class="ico1" src="img/yellow_arrow.png" alt="">--%>
                            <%--<img class="ico2" src="img/gif.png" alt="">--%>
                            <%--<img class="ico3" src="img/gif1.png" alt="">--%>
                        <%--</li>--%>
                        <%--<li class="flex-1 jmt_center phone" onclick="selectAmount(this,'50M',6.5)">--%>
                            <%--<div>50M</div>--%>
                            <%--<div>售价：6.5元</div>--%>
                            <%--<img class="ico1" src="img/yellow_arrow.png" alt="">--%>
                            <%--<img class="ico2" src="img/gif.png" alt="">--%>
                            <%--<img class="ico3" src="img/gif1.png" alt="">--%>
                        <%--</li>--%>
                        <%--<li class="flex-1 jmt_center phone" onclick="selectAmount(this,'100M',8.9)">--%>
                            <%--<div>100M</div>--%>
                            <%--<div>售价：8.9元</div>--%>
                            <%--<img class="ico1" src="img/yellow_arrow.png" alt="">--%>
                            <%--<img class="ico2" src="img/gif.png" alt="">--%>
                            <%--<img class="ico3" src="img/gif1.png" alt="">--%>
                        <%--</li>--%>
                    <%--</ul>--%>

                    <%--<ul class="flex">--%>
                        <%--<li class="flex-1 jmt_center phone" onclick="selectAmount(this,'200M',13.5)">--%>
                            <%--<div>200M</div>--%>
                            <%--<div>售价：13.5元</div>--%>
                            <%--<img class="ico1" src="img/yellow_arrow.png" alt="">--%>
                            <%--<img class="ico2" src="img/gif.png" alt="">--%>
                            <%--<img class="ico3" src="img/gif1.png" alt="">--%>
                        <%--</li>--%>
                        <%--<li class="flex-1 jmt_center phone" onclick="selectAmount(this,'500M',27.1)">--%>
                            <%--<div>500M</div>--%>
                            <%--<div>售价：27.1元</div>--%>
                            <%--<img class="ico1" src="img/yellow_arrow.png" alt="">--%>
                            <%--<img class="ico2" src="img/gif.png" alt="">--%>
                            <%--<img class="ico3" src="img/gif1.png" alt="">--%>
                        <%--</li>--%>
                        <%--<li style="visibility: hidden" class="flex-1 jmt_center phone" onclick="selectAmount(this,'1024M',44.1)">--%>
                            <%--<div>1G</div>--%>
                            <%--<div>售价：44.1元</div>--%>
                            <%--<img class="ico1" src="img/yellow_arrow.png" alt="">--%>
                            <%--<img class="ico2" src="img/gif.png" alt="">--%>
                            <%--<img class="ico3" src="img/gif1.png" alt="">--%>
                        <%--</li>--%>
                    <%--</ul>--%>
                <%--</div>--%>

            </div>

        </div>
        <div id="redPackDiv"  class="ticketSelect flex hide">
            <label id="redPack" class="">抵用券</label>
            <span> </span>
            <!--无抵用券时 disabled/选择抵用券时,替换上面span文字-->
            <select id="sel" onclick="changeRedPack(this)" class="flex-1"  >
                <option hidden value=" ">请选择</option>
                <option  value="0-0">暂不选择</option>
                <!--<option value="50元兑换券">50元抵用券</option>-->
                <!--<option value="100元兑换券">100元抵用券</option>-->
            </select>
        </div>


        <!--立即支付-->
        <div class="jmt_center">
            <a id="payMoney" class="toPay no" onclick="rechargeNow()"  >充值</a>
        </div>
    </div>
</div>
</body>
</html>