    <%@ page contentType="text/html; charset=utf-8" language="java" isELIgnored="false" errorPage="" %>
        <%@include file="inc/timestamp.inc" %>
        <!DOCTYPE html>
        <html lang="en">
        <head>
        <meta charset="UTF-8">
        <base href="${urlPrefix}/" />
        <title></title>
        <%@include file="inc/head.inc" %>
        <meta name="viewport" content="initial-scale=0.5,maximum-scale=0.5,minimum-scale=0.5, width=640, target-densitydpi=device-dpi">
        <meta http-eqiv="X-UA-Compatible" content="IE=Edge,chrome=1">
        <meta content="yes" name="apple-mobile-web-app-capable">
        <meta content="black" name="apple-mobile-web-app-status-bar-style">
        <meta content="telephone=no" name="format-detection">
        <title>中石化微信支付</title>
        <link type="text/css" href="../css/cssAdd/app.css" rel="stylesheet">
        <link type="text/css" href="../css/cssAdd/common/common.css" rel="stylesheet">
        <script type="text/javascript" src="../js/jsAdd/common/jQuery-1.11.3.js"></script>
        <script type="text/javascript" src="../js/jsAdd/common/jWeChat-Adaptive.js"></script>
        <script type="text/javascript" src="../js/jsAdd/common/m.tool.juxinbox.com.js"></script>
        <script type="text/javascript" src="../js/jsAdd/common/jWeChat-1.0.0.js"></script>
        <script type="text/javascript" src="../js/jsAdd/index.js"></script>
        <script src="../js/userPay.js?<%=v%>"></script>
        <script>
        var rechargeValue=0;
        var exp = new RegExp("^\\d+(\\.\\d+)?$");
        var jinzhi=100;
        var payId="";
        function close_tip() {
        $(".bg_block").hide();
        jinzhi=100;
        }
        function Tip_bank(type) {
        alert(type);
        $(".bg_block").hide();
        }
        //支付类型选择
        function selectType() {
        $(".pay_type ").show();
        $(".red_type ").hide();
        $(".integral_type ").hide();
        $("#payTypeSelected ").html($("#payType").val());
        }
        //没有支付类型选择
        function selectNone() {
        $(".pay_type ").hide();
        }
        function selectPayType(self,id) {
        payId=id;
        $("#payType").val($(self).html());
        $(".pay_type ").hide();
        }

        //红包选择
        function selectRed(){
        if(!$("#rechargeValue").val()){
        TipShow('请输入充值金额',1000);
        }
        else {
        $(".red_type ").show();
        $(".pay_type ").hide();
        $(".integral_type ").hide();
        $("#redTypeSelected ").html($("#redType").val());
        }
        }
        function selectRedNone() {
        $(".red_type ").hide();
        }
        var hasBenefit=0;
        var redId="";
        function selectRedType(self,cut,id) {
        if(cut){
        hasBenefit=1;
        redId=id;
        }
        else{
        hasBenefit=0;
        redId=id="";
        }
        $("#redType").val($(self).html());
        $(".red_type ").hide();
        $("#redTypeSelected ").html($(self).html());
        $("#realAmmount").html(($("#rechargeValue").val()-cut/100).toFixed(2));
        var html= "<div onclick=\"selectRedNone(this)\"><label id=\"redTypeSelected\">"+$(self).html()+"</label><img src=\"img/arrow2.png\" alt=\"\" class=\"arrow_tip\"></div>";
        $("#redParent").html(html);
        }
        //积分选择
        function selectIntegral(){
        $(".integral_type ").show();
        $(".red_type ").hide();
        $(".pay_type ").hide();
        $("#integralTypeSelected ").html($("#integralType").val());
        }
        function selectIntegralNone() {
        $(".integral_type ").hide();
        }
        function selectIntegralType(self) {
        $("#integralType").val($(self).html());
        $(".integral_type").hide();
        $("#integralTypeSelected ").html($(self).html());
        }
        function selectTipShow() {
        $('.bg_block').show();
        jinzhi=0;
        document.addEventListener("touchmove",function(e){
        if(jinzhi==0){ e.preventDefault(); e.stopPropagation(); } },false);
        }
        //立即支付
        function payNow() {
        if(!$('#rechargeValue').val()){
        TipShow('充值金额不能为空',1000);
        }
        else if(!exp.test($('#rechargeValue').val())){
        TipShow('请输入正确的金额',1000);
        }
        else{
        totale();
        }
        }
        $('.bg_block').click(function (e) {
        e.stopPropagation();
        return false;
        });
        function init() {
        <%--初始化支付方式--%>
        showPayType();
        initRed();
        var bankListHTML="";
        $.ajax({
        url:"http://tdev.juxinbox.com/queryActivities.do",
        dataType:"json",
        type:"post",
        async:false,
        success:function(data){
        if(data.resultList.length>0){
        for(var i=0;i<data.resultList.length;i++){
        bankListHTML+="    <li class=\"flex\">"
        + "                    <div class=\"flex-1 lineHeight50\">"
        + "                        <img src=\""+data.resultList[i].icon+"\" alt=\"\" width=\"50px\">"
        + "                    </div>"
        + "                    <div class=\"flex-4 jmt_left bankContent\">"
        + "                        <div>"+data.resultList[i].name+"</div>"
        + "                        <nav>"+data.resultList[i].desc+"</nav>"
        + "                    </div>"
        + "                </li>";
        }
        $("#bankListHTML").html(bankListHTML);
        }
        else{
        $("#bankListHTML").html("   <div class=\"jmt_center\">暂无数据</div>");
        }
        },
        error:function () {
        alert("获取失败");
        }
        });
        }
        function getRechargeValue() {
        if(!exp.test($('#rechargeValue').val())){
        $("#rechargeValue").val("");
        TipShow('请输入正确的金额',1000);
        }
        else{
        rechargeValue=$("#rechargeValue").val();
        $("#realAmmount").html($("#rechargeValue").val());
        switchRed(rechargeValue);
        $("#redType").val("请选择红包");
        }
        }
        function initRed() {
        var redListHTML= "<nav id=\"redParent\"></nav>";
        $.ajax({
        url:"http://tdev.juxinbox.com/queryExchanges.do?openId=123",
        dataType:"json",
        type:"post",
        async:false,
        success:function(data){
        console.log(data);
        if(data.resultList.length>0){
        for(var i=0;i<data.resultList.length;i++){
        redListHTML+="<div onclick=\"selectRedType(this,"+data.resultList[i].benefit+","+data.resultList[i].id+")\">"+data.resultList[i].desc+"</div>";
        }
        $("#redListHTML").html(redListHTML);
        }
        else{
        $("#redListHTML").html("   <div class=\"jmt_center\">暂无数据</div>");
        }
        },
        error:function () {
        alert("获取失败");
        }
        });
        }
        function switchRed(ammount) {
        var redListHTML= "<nav id=\"redParent\"></nav>";
        $.ajax({
        url:"http://tdev.juxinbox.com/queryExchanges.do?openId=123",
        dataType:"json",
        type:"post",
        async:false,
        success:function(data){
        if(data.resultList.length>0){
        for(var i=0;i<data.resultList.length;i++){
        if($('#rechargeValue').val()>=data.resultList[i].full/100){
        redListHTML+="<div onclick=\"selectRedType(this,"+data.resultList[i].benefit+")\">"+data.resultList[i].desc+"</div>";
        }
        }
        $("#redListHTML").html(redListHTML);
        }
        else{
        $("#redListHTML").html("   <div class=\"jmt_center\">暂无数据</div>");
        }
        },
        error:function () {
        alert("获取失败");
        }
        });
        }
        function totale() {
        var payType=$("#payType").val();//支付类型
        var redTypeSelected=$("#redType").val();//红包类型
        var payMoney=$("#realAmmount").html();//支付金额
        // alert($("#openId").val());
        loading("start");
        $.ajax({
        url: getRootPath() + "/preOrder.do",
        data: {
        "money": payMoney,
        "openid":$("#openId").val(),
        "goodsName":payType
        },
        dataType: "json",
        type: "post",
        success: function (data) {
        loading("stop");
        if(data.success){
        wx.config({
        debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
        appId: data.data.appId, // 必填，公众号的唯一标识
        timestamp: data.data.timestamp, // 必填，生成签名的时间戳
        nonceStr: data.data.nonceStr, // 必填，生成签名的随机串
        signature: data.data.paySign,// 必填，签名，见附录1
        jsApiList: ['chooseWXPay'] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
        });
        wx.ready(function(){
        // config信息验证后会执行ready方法，所有接口调用都必须在config接口获得结果之后，config是一个客户端的异步操作，所以如果需要在页面加载时就调用相关接口，则须把相关接口放在ready函数中调用来确保正确执行。对于用户触发时才调用的接口，则可以直接调用，不需要放在ready函数中。
        wx.chooseWXPay({
        timestamp: data.data.timestamp, // 支付签名时间戳，注意微信jssdk中的所有使用timestamp字段均为小写。但最新版的支付后台生成签名使用的timeStamp字段名需大写其中的S字符
        nonceStr: data.data.nonceStr, // 支付签名随机串，不长于 32 位
        package: data.data.package, // 统一支付接口返回的prepay_id参数值，提交格式如：prepay_id=***）
        signType: data.data.signType, // 签名方式，默认为'SHA1'，使用新版支付需传入'MD5'
        paySign: data.data.paySign, // 支付签名
        success: function (res) {
        //订单查询
        $.ajax({
        url: getRootPath() + "/queryOrder.do",
        data: {
        "openid":$("#openId").val(),
        "orderId":data.data.orderId,
        "hasBenefit":hasBenefit,
        "benefitid":redId
        },
        dataType: "json",
        type: "post",
        success: function (data) {
        window.location.href="/paySuccess.jsp?openId="+$("#openId").val()+"&activityId="+data;
        },error:function (res) {
        loading("stop");
        console.log(res);
        }
        });
        },error:function(res){
        alert('f');
        }
        });
        });
        }else{
        alert(data.errMsg);
        }
        },
        error:function (res) {
        loading("stop");
        console.log(res);
        }
        });
        }
        function showPayType() {
        var data = {
        userNum : getQueryString("iUID")
        }

        $.ajax({
        url : $("base").attr("href")+'/bms/wechat/paytype/queryPayType.htm',
        method:'post',
        data : data ,
        datatype:'json',
        success : function(data) {
        var jsondata = JSON.parse(data);
        $("#options").html("");
        var html ="  <div onclick=\"selectNone()\">"+jsondata[0].payName+"</div>";
        payId=jsondata[0].payID;
        for(var i=0;i<jsondata.length;i++){
        if(jsondata.length>0){
        html+="<div onclick=\"selectPayType(this,"+jsondata[i].payID+")\">"+jsondata[i].payName+"</div>"
        }
        }
        $(".pay_type").append(html);
        }
        })
        }
        </script>
        </head>
        <body onload="init()">
        <!--弹框-->
        <div class="bg_block">
        <div class="index_bg">
        <div class="index_TipTop">银行卡支付优惠
        <img src="img/close.png" alt="" onclick="close_tip()">
        </div>
        <div class="bankLists">
        <ul id="bankListHTML">

        </ul>
        </div>
        </div>
        </div>
        <div style="background-color: #f2f2f2" class="zoomer">
        <div class="index_content">
        <div>
        <div class="index_tittle">欢迎使用中石化微信支付</div>
        <div class="index_input">
        <ul>
        <li><span class="index_inputName">支付类型</span>
        <input id="payType" type="text" readonly value="加油加气" onclick="selectType()">
        <img src="img/arrow1.png" alt="">
        <div class="pay_type">
        <div onclick="selectNone()"><span id="payTypeSelected">加油加气</span><img src="img/arrow2.png" alt="" style="top: 25px;width: 30px;">
        </div>
        <div onclick="selectPayType(this)">加油加气</div>
        <div onclick="selectPayType(this)">非油品购买</div>
        </div>
        </li>
        <li><span class="index_inputName">支付金额</span>
        <input id="rechargeValue" type="text" style="font-family:'黑体'" placeholder="请输入任意金额" onblur="getRechargeValue()">
        </li>
        <div>
        <span class="real_ammount">实付金额：¥ <a id="realAmmount">0</a></span>
        </div>
        </ul>
        </div>
        </div>
        </div>
        <div class="arrows">
        <img src="img/lingxing.png" alt="" width="100%">
        </div>
        <div class="index_content1">
        <ul>
        <li><span>支付红包</span><input id="redType" type="text" readonly value="请选择红包" onclick="selectRed()">
        <img src="img/arrow1.png" alt="" class="arrow1">
        <div class="red_type" id="redListHTML">
        <!--<div onclick="selectRedNone(this,100,10)"><label id="redTypeSelected">满100元可使用10元支付红包</label><img src="img/arrow2.png" alt="" class="arrow_tip">-->
        <!--</div>-->
        <!--<div onclick="selectRedType(this,100,10)">满100元可使用10元支付红包</div>-->
        <!--<div onclick="selectRedType(this,200,20)">满200元可使用20元支付红包</div>-->
        <!--<div onclick="selectRedType(this,300,30)">满300元可使用30元支付红包</div>-->
        <!--<div onclick="selectRedType(this,400,40)">满400元可使用40元支付红包</div>-->
        <!--<div onclick="selectRedType(this,500,100)">满500元可使用100元支付红包</div>-->
        </div>
        </li>
        <!--<li><span>积分抵现</span><input id="integralType" type="text" readonly value="招行100积分可抵用1元" onclick="selectIntegral()">-->
        <!--<img src="img/arrow1.png" alt="" class="arrow1">-->
        <!--<div class="integral_type">-->
        <!--<div onclick="selectIntegralNone()"><label id="integralTypeSelected">招行100积分可抵用1元</label><img src="img/arrow2.png" alt="" class="arrow_tip">-->
        <!--</div>-->
        <!--<div onclick="selectIntegralType(this)">招行400积分可抵用1元</div>-->
        <!--<div onclick="selectIntegralType(this)">招行6100积分可抵用1元</div>-->
        <!--<div onclick="selectIntegralType(this)">招行700积分可抵用1元</div>-->
        <!--<div onclick="selectIntegralType(this)">招行200积分可抵用1元</div>-->
        <!--<div onclick="selectIntegralType(this)">招行800积分可抵用1元</div>-->
        <!--</div>-->
        <!--</li>-->
        <li><span>银行活动</span>
        <label class="bankTip_select" onclick="selectTipShow()">
        <a>银行卡</a> 银行卡支付优惠
        <div class="bankIcons">
        <img src="img/bank_zs.png" alt="" width="30px">
        <img src="img/bank_pf.png" alt="" width="30px">
        ...
        </div>

        </label>
        <img src="img/arrow1.png" alt="" class="arrow1">
        </li>
        </ul>
        <div class="jmt_center" style="margin: 50px 30px 40px;">
        <img src="img/payNow.png" alt="" onclick="payNow()">
        </div>
        <div class="jmt_center" style="height: 1px;">
        <img src="img/line.png" alt="" width="100%" height="1px">
        </div>
        </div>
        <div class="index_footer_tittle">
        <div class="footer_tittle">
        支付须知
        </div>
        <div class="index_footer_content">
        支付类型、支付金额为必填选项，其他选项均为非必填选项请选择正确的支付类型，否则会影响到后续服务
        </div>
        </div>
        </div>
        <input type="hidden" id="openId" value="${openId}">
        </body>
        </html>