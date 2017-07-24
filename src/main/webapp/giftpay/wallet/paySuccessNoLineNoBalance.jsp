﻿<%@ page language="java" import="java.util.*" isELIgnored="false" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">

	<head>
		<meta charset="UTF-8">
		<meta name="viewport" content="initial-scale=0.5,maximum-scale=0.5,minimum-scale=0.5, width=640, target-densitydpi=device-dpi">
		<meta http-eqiv="X-UA-Compatible" content="IE=Edge,chrome=1">
		<meta content="yes" name="apple-mobile-web-app-capable">
		<meta content="black" name="apple-mobile-web-app-status-bar-style">
		<meta content="telephone=no" name="format-detection">
		<title>支付结果</title>
		<link type="text/css" href="demopay/css/common/common.css" rel="stylesheet">
		<link type="text/css" href="css/app.css" rel="stylesheet">
		<link type="text/css" href="demopay/css/style.css" rel="stylesheet">
		<link type="text/css" href="demopay/css/flexslider.css" rel="stylesheet">
		<script type="text/javascript" src="demopay/js/common/jQuery-1.11.3.js"></script>
		<script type="text/javascript" src="demopay/js/common/jWeChat-Adaptive.js"></script>
		<script type="text/javascript" src="demopay/js/common/m.tool.juxinbox.com.js"></script>
		<script type="text/javascript" src="demopay/js/Scratch.js"></script>
		<script type="text/javascript" src="demopay/js/common/common.js"></script>
		<script type="text/javascript" src="demopay/js/jquery.flexslider-min.js"></script>
	</head>
	<script>
		$(function() {
			ad_tip('show');
			var gunNo = getUrlParam("gunNo"); //枪号\
			$(".gunNo").attr("src", "demopay/img/gun" + gunNo + ".png");
			$(".gunOilBox1").html(gunNo + "号油枪加油完成，请放行");
			var timestamp = getUrlParam("timestamp");
			$(".gunOilBox3 a").html(getLocalTime(timestamp));
			$(".getCodeRedBtn").on("click",function(){
				window.location.href="http://www.linkgift.cn/giftpay_wap/giftpay/third/addThirdUserIndex.htm?thirdName=zsh";
			})
			initAdvertise();
		})
		function getLocalTime(time){
			var year=time.substring(0,4);
			var month=time.substring(4,6)
			var day=time.substring(6,8);
			var hour=time.substring(8,10);
			var min=time.substring(10,12);
			var sec=time.substring(12,time.length);
			return year+"-"+month+"-"+day+" "+hour+":"+min;
		}
		function initAdvertise(){
			$.ajax({
				url: getRootPath() + "/giftpay/commonSetting/queryRandomAdvertise.htm",
				type: "post",
				dataType: "json",
				success: function(data) {
					console.log(data);
					$(".index_content6").attr("itemHref",data[0].advertiseHref);
					$(".index_content6").attr("itemId",data[0].advertiseId);
					$(".index_content6").html('<img src="'+data[0].advertiseImgUrl+'" alt="" width="100%">');
					$(".index_content6").on("click",function(){
						$.ajax({
							url: getRootPath() + "/giftpay/commonSetting/updateAdverClickNum.htm",
							type: "post",
							data: {"id": $(".index_content6").attr("itemId")},
							dataType: "json",
							success: function (res) {
								console.log(res);
								if(res.code=='1'){
									location.href=$(".index_content6").attr("itemHref");
								}
							}
						});
					});
				}
			});
		}
	</script>

	<body>
		<div class="zoomer" style="background-color: #ffffff;overflow-x: hidden;position: fixed">
			<div style="margin: 40px 40px 10;">
				<div class="jmt_center gun_background">
            		<a class="gun_number">1</a>
        		</div>
				<div class="paySuccess_content3">
					<span>支付成功</span>
				</div>
			</div>
			<div class="gunOilBox">
        <div class="gunOilBox1">1号油枪加油完成，请放行</div>
        <div class="gunOilBox3">
            <img src="img/dottedO.png" alt="">
            <a>2014-04-01  13:50</a>
            <img src="img/dottedO.png" alt="">
        </div>
    </div>
			<div class="index_content4 flex">
        <div>
            <img src="demopay/img/code3.png" alt="" width="160px">
            <img src="demopay/img/code3.png" alt="" style="    position: fixed;top: 12%;width: 70%;left: 0px;opacity: 0">
        </div>
        <div class="flex-1">
            <div class="flex getRedCodeBanner_Div">
                <div class="flex-1 getRedTittle">恭喜您获得 <a>兴业银行提供的50元加油支付红包</a></div>
                <div class="getCodeRedBtn">领取</div>
            </div>
            <div class="getRedCodeBanner_tip">长按二维码随时查看你的红包和更多优惠</div>
        </div>

    </div>
			<div class="htmleaf-container">
				<div class="max-width">
					<div class="scratch_container">
						<div class="scratch_viewport">
							<a id="getRed">

							</a>
							<canvas id="js-scratch-canvas"></canvas>
						</div>
					</div>
				</div>
			</div>

			<script src="demopay/js/Scratch.js"></script>
			<script type="text/javascript">
				var scratch = new Scratch({
					canvasId: 'js-scratch-canvas',
					imageBackground: './demopay/img/ggbg_prize.png',
					imageBackgroundNone: './demopay/img/ggbg_none.png',
					pictureOver: './demopay/img/ggbg.png',
					cursor: {
						//                png: './images/piece.png',
						//                cur: './images/piece.cur',
						x: '20',
						y: '17'
					},
					radius: 30,
					nPoints: 100,
					percent: 30,
					callback: function() {},
					pointSize: { x: 3, y: 3 }
				});
			</script>
			<div class="index_content12">
				<ul class="flex">
					<li class="flex-1 tuijian_line"></li>
					<li class="tuiguang_">推广</li>
					<li class="flex-1 tuijian_line"></li>
				</ul>
			</div>

			<a class="index_content6">
			</a>
			<!--<a class="index_content5">-->
			<!--<img src="img/banner5.png" alt="" width="572px">-->
			<!--</a>-->
			<div class="jmt_center pay_bottom_tel">
				<a href="tel:4000808065">推广合作</a>
			</div>
		</div>
	</body>

</html>