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
    <script type="text/javascript" src="${contentPath}/jsp/weixinMng/mallMng/js/fuel_cell.js"></script>
    <script type="text/javascript" src="${contentPath}/jsp/weixinMng/mallMng/js/fastclick.min.js"></script>
    <script>

    </script>
</head>
<body>
<div class="zoomer">
     <div class="tab_text">
         <img src="${contentPath}/jsp/weixinMng/mallMng/img/arrowLeft.png" alt="" class="arrowLeft">
         <div>话费充值</div>
         <div class="rechargeRecords" onclick="changelist()">充值记录</div>
     </div>
    <div class="recharge_Content">
        <div class="recharge_Content_goods">
            <div class="serviceName">请输入需要充值的手机号</div>
            <div>
                <input id="number" type="tel" maxlength="13" class="phoneNumber" onkeyup="handle()" oninput="OnInput (event)" onpropertychange="OnPropChanged(event)">
            </div>
            <div id="companyName">
                <div class="serviceCompany" id="phoneaddress">&nbsp;&nbsp;</div>
            </div>

            <div class="cellPhoneNumberPrice">
                <ul class="flex">
                    <li class="flex-1 jmt_center phone" onclick="selectAmount(this,'10','CELLTEST')">
<!--                     <li class="flex-1 jmt_center phone" onclick="selectAmount(this,'10','CELL001')"> -->
                        <div>1元</div>
                        <div>测试售价：1元</div>
                        <img src="${contentPath}/jsp/weixinMng/mallMng/img/yellow_arrow.png" alt="">
                    </li>
                    <li class="flex-1 jmt_center phone" onclick="selectAmount(this,'20','CELL002')">
                        <div>20元</div>
                        <div>售价：20元</div>
                        <img src="${contentPath}/jsp/weixinMng/mallMng/img/yellow_arrow.png" alt="">
                    </li>
                    <li class="flex-1 jmt_center phone" onclick="selectAmount(this,'30','CELL003')">
                        <div>30元</div>
                        <div>售价：30元</div>
                        <img src="${contentPath}/jsp/weixinMng/mallMng/img/yellow_arrow.png" alt="">
                    </li>
                </ul>

                <ul class="flex">
                    <li class="flex-1 jmt_center phone" onclick="selectAmount(this,'50','CELL004')">
                        <div>50元</div>
                        <div>售价：50元</div>
                        <img src="${contentPath}/jsp/weixinMng/mallMng/img/yellow_arrow.png" alt="">
                    </li>
                    <li class="flex-1 jmt_center phone" onclick="selectAmount(this,'100','CELL005')">
                        <div>100元</div>
                        <div>售价：100元</div>
                        <img src="${contentPath}/jsp/weixinMng/mallMng/img/yellow_arrow.png" alt="">
                    </li>
                    <li class="flex-1 jmt_center phone" onclick="selectAmount(this,'200','CELL006')">
                        <div>200元</div>
                        <div>售价：200元</div>
                        <img src="${contentPath}/jsp/weixinMng/mallMng/img/yellow_arrow.png" alt="">
                    </li>
                </ul>
            </div>
        </div>

        <div class="jmt_center">
            <img id="PayShow" src="${contentPath}/jsp/weixinMng/mallMng/img/pay_now.png" alt="" width="100%" onclick="rechargeNow()">
            <img id="PayHide" src="${contentPath}/jsp/weixinMng/mallMng/img/pay_off.png" alt="" width="100%">
        </div>
        <div class="jmt_center marginTop30">
            <img src="${contentPath}/jsp/weixinMng/mallMng/img/moreService.png" alt="" width="100%">
        </div>
        <div class="moreService">
            <ul class="flex">
                <li class="flex-1 jmt_center">
                    <img src="${contentPath}/jsp/weixinMng/mallMng/img/fuelCard_recharge.png" alt="">
                    <div class="moreIconText">油卡充值</div>
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