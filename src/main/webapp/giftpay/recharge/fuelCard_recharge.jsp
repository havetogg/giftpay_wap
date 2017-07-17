<!DOCTYPE html>
<%@ page language="java" contentType="text/html;charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html>
<head>
	<c:set var="contentPath" value="<%=request.getContextPath()%>"></c:set>
    <meta charset="UTF-8">
    <meta name="viewport" content="initial-scale=0.5,maximum-scale=0.5,minimum-scale=0.5, width=640, target-densitydpi=device-dpi">
    <meta http-eqiv="X-UA-Compatible" content="IE=Edge,chrome=1">
    <meta content="yes" name="apple-mobile-web-app-capable">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <title>有礼付</title>
    <link type="text/css" href="${contentPath}/jsp/weixinMng/mallMng/css/common/common.css" rel="stylesheet">
    <link type="text/css" href="${contentPath}/jsp/weixinMng/mallMng/css/app.css" rel="stylesheet">
    <script type="text/javascript" src="${contentPath}/jsp/weixinMng/mallMng/js/common/jQuery-1.11.3.js"></script>
    <script type="text/javascript" src="${contentPath}/jsp/weixinMng/mallMng/js/common/common.js"></script>
    <script type="text/javascript" src="${contentPath}/jsp/weixinMng/mallMng/js/index.js"></script>
    <script type="text/javascript" src="${contentPath}/jsp/weixinMng/mallMng/js/common/jWeChat-Adaptive.js"></script>
       <script type="text/javascript" src="${contentPath}/jsp/weixinMng/mallMng/js/common/m.tool.juxinbox.com.js"></script>
    <script type="text/javascript" src="${contentPath}/jsp/weixinMng/mallMng/js/fuel.js"></script>
    <script type="text/javascript" src="${contentPath}/jsp/weixinMng/mallMng/js/fastclick.min.js"></script>
</head>
<body>
<div class="zoomer">
    <div class="tab_text">
        <img src="${contentPath}/jsp/weixinMng/mallMng/img/arrowLeft.png" alt="" class="arrowLeft">
        <div>油卡充值</div>
        <div class="rechargeRecords" onclick="changelist()">充值记录</div>
    </div>
    <div class="recharge_Content">
        <div class="recharge_Content_goods">
            <div class="cellPhoneNumberPrice">
                <ul class="flex">
<!--                     <li class="flex-1 jmt_center phone" onclick="selectAmount(this,'50','GSATEST')"> -->
                       
                    <li class="flex-1 jmt_center phone" onclick="selectAmount(this,'50','GSA001')"> 
                        <div>50元</div>
                        <div>售价：50元</div>
                        <img src="${contentPath}/jsp/weixinMng/mallMng/img/yellow_arrow.png" alt="">
                    </li>
                    <li class="flex-1 jmt_center phone" onclick="selectAmount(this,'100','GSA002')">
                        <div>100元</div>
                        <div>售价：100元</div>
                        <img src="${contentPath}/jsp/weixinMng/mallMng/img/yellow_arrow.png" alt="">
                    </li>
                    <li class="flex-1 jmt_center phone" onclick="selectAmount(this,'200','GSA003')">
                        <div>200元</div>
                        <div>售价：200元</div>
                        <img src="${contentPath}/jsp/weixinMng/mallMng/img/yellow_arrow.png" alt="">
                    </li>
                </ul>

                <ul class="flex">
                    <li class="flex-1 jmt_center phone" onclick="selectAmount(this,'500','GSA004')">
                        <div>50元</div>
                        <div>售价：50元</div>
                        <img src="${contentPath}/jsp/weixinMng/mallMng/img/yellow_arrow.png" alt="">
                    </li>
                    <li class="flex-1 jmt_center phone" onclick="selectAmount(this,'1000','GSA005')">
                        <div>1000元</div>
                        <div>售价：1000元</div>
                        <img src="${contentPath}/jsp/weixinMng/mallMng/img/yellow_arrow.png" alt="">
                    </li>
                    <li class="flex-1 jmt_center phone" onclick="selectAmount(this,'1000','GSA005')">
                        <div>2000元</div>
                        <div>售价：2000元</div>
                        <img src="${contentPath}/jsp/weixinMng/mallMng/img/yellow_arrow.png" alt="">
                    </li>
                </ul>
            </div>
<!--             <div>
                <input id="amount" class="fuelRecharge_inputMoney" type="tel" placeholder="自定义充值金额" onkeyup="this.value=this.value.replace(/[^0-9]/g,'')" oninput="amountInput (event)" onpropertychange="OnPropChanged(event)" disabled="disabled">
            </div> -->
        </div>
        <div class="fuel_inputArea">
            <div class="flex">
                <a class="fuel_inputName">加油卡号</a>
                <a class="flex-1">
                    <input id="cardNo" type="tel" maxlength="19" oninput="cardInput (event)" onpropertychange="OnPropChanged(event)">
                </a>
                <a class="fuel_inputArea_leftName" id="cardtypeid">末知</a>
            </div>
            <div class="flex">
                <a class="fuel_inputName">姓&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名</a>
                <a class="flex-1">
                    <input id="name" type="text" maxlength="10" onblur="nameOnInput()" onchange="nameOnInput()">
                </a>
                <a class="fuel_inputArea_leftName"></a>
            </div>
            <div class="flex">
                <a class="fuel_inputName">手机号码</a>
                <a class="flex-1">
                    <input id="tel" type="tel" maxlength="13" oninput="OnInput (event)" onkeyup="handle()" onpropertychange="OnPropChanged(event)">
                </a>
                <a class="fuel_inputArea_leftName" id="phoneaddress">末知</a>
            </div>

        </div>
        <div class="jmt_center">
            <img id="PayShow" src="${contentPath}/jsp/weixinMng/mallMng/img/pay_now.png" alt="" width="100%" onclick="rechargeNow()">
            <img id="PayHide" src="${contentPath}/jsp/weixinMng/mallMng/img/pay_off.png" alt="" width="100%">
        </div>
        <div class="fuel_content_Text">
            <p>1.支持全国中石化/中石油个人加油卡或公司卡主卡。不支持各类副卡、不记名卡、过期卡、中石油司机卡、BP卡。</p>
            <p>2.充值后，如果超过12小时仍未到账，请您尽快与我们的客服取得联系（4008828170）。查询充值结果，请致电中石化客户服务热线（95105888）；中石油客服热线（95504），告知他们是从网上充值，查询到账金额和余款。</p>
            <p>3.充值成功后，在您加油消费时，需要到中石化/中石油加油站圈存机上圈存后即可使用，并可看到已充值金额。有任何疑问请咨询加油站服务员或致电有礼付客服（4008828170）。</p>
        </div>
        <div class="jmt_center marginTop30">
            <img src="${contentPath}/jsp/weixinMng/mallMng/img/moreService.png" alt="" width="100%">
        </div>
        <div class="moreService">
            <ul class="flex">
                <li class="flex-1 jmt_center">
                    <img src="${contentPath}/jsp/weixinMng/mallMng/img/bill_recharge.png" alt="">
                    <div class="moreIconText">话费充值</div>
                </li>
                <li class="flex-1 jmt_center">
                    <img src="${contentPath}/jsp/weixinMng/mallMng/img/flow_recharge.png" alt="">
                    <div class="moreIconText">流量充值</div>
                </li>
                <li class="flex-1 jmt_center">
                    <img src="${contentPath}/jsp/weixinMng/mallMng/img/giftPay_mall.png" alt="">
                    <div class="moreIconText">有礼商城</div>
                </li>
            </ul>
        </div>
    </div>
</div>
</body>
</html>