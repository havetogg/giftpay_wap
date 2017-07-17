<%@ page language="java" import="java.util.*" isELIgnored="false" pageEncoding="UTF-8"%>
<%@include file="inc/timestamp.inc"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="initial-scale=0.5,maximum-scale=0.5,minimum-scale=0.5, width=640, target-densitydpi=device-dpi">
    <meta http-eqiv="X-UA-Compatible" content="IE=Edge,chrome=1">
    <meta content="yes" name="apple-mobile-web-app-capable">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <title>中石化微信支付DEMO</title>
    <link type="text/css" href="css/common/common.css" rel="stylesheet">
    <link type="text/css" href="css/app.css" rel="stylesheet">
    <link type="text/css" href="css/style.css" rel="stylesheet">
    <link type="text/css" href="css/flexslider.css" rel="stylesheet">
    <script type="text/javascript" src="js/common/jQuery-1.11.3.js"></script>
    <script type="text/javascript" src="js/common/jWeChat-Adaptive.js"></script>
    <script type="text/javascript" src="js/common/m.tool.juxinbox.com.js"></script>
    <script type="text/javascript" src="js/Scratch.js"></script>
    <script type="text/javascript" src="js/common/common.js"></script>
    <script type="text/javascript" src="js/jquery.flexslider-min.js"></script>
</head>
<script>
	$(function(){
		var openId=$("#openId").val();
		$(".toPay").on("click",function(){
			location.href="demoPay.html?openId="+openId;
		})
		$(".toPayNoLine").on("click",function(){
			location.href="demoPayNoLine.html?openId="+openId;
		})
	})
</script>
<body >
<div class="zoomer" style="background-color: #ffffff;overflow-x: hidden;position: fixed">
	<div style="text-align: center;font-size: 24px;margin: 30px;">中石化微信支付DEMO</div>
	<div style="width:100%;text-align: center;">
		<div class="toPay" style="border: 0;font-size: 50px;width: 40%;margin: 0 auto; text-align: center;margin-top: 40%;background:#41629A;
			color: #fff;">线上支付</div>
			<div class="toPayNoLine" style="border: 0;font-size: 50px;width: 40%;margin: 0 auto; text-align: center;margin-top: 20%;background:#41629A;
			color: #fff;">线下支付</div>
	</div>
	<input type="hidden" id="openId" value="${openId}" />
</div>
</body>
</html>