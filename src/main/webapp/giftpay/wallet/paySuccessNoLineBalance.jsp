<%@ page language="java" import="java.util.*" isELIgnored="false" pageEncoding="UTF-8"%>
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
	<script type="text/javascript" src="js/recoGoods.js"></script>
</head>
<script>
	$(function() {
		ad_tip4('show');
		var timestamp = $("#timestamp").val();
		var redAmount = $("#redAmount").val();
		var realAmount = new Number($("#realAmount").val());
		$(".gunOilBox3 a").html(timestamp.substring(0,timestamp.lastIndexOf(":")));
		var redirectURL=$("#redirectURL").val();
		if(redAmount == '' || redAmount == '0' || redAmount == null) {
			$(".gunOilBox2").html("<a>实付" +
					realAmount + "元</a>");
		} else {
			var num = new Number(redAmount);
			$(".gunOilBox2").html(" 需付" + ((num + realAmount).toFixed(2)) + "元，红包抵扣" + num.toFixed(2) + "元，<a>实付" +
					realAmount + "元</a>");
		}
		$(".getCodeRedBtn").on("click", function() {
			var thirdOpenId=$("#thirdOpenId").val();
			var channelId=$("#channelId").val();
			var amount=$("#amount").val();
			var hasRed=$("#hasRed").val();
			var orderId=$("#orderId").val();
			var redId=$("#redId").val();
			window.location.href = getRootPath()+"/giftpay/wallet/wxRedPkgBase.htm?thirdOpenId="+thirdOpenId+"&channelId="+channelId+"&amount="
					+amount+"&hasRed="+hasRed+"&redId="+redId+"&orderId="+orderId;
		})
		$(".gunOilBox_returnBack").on("click",function(){
			if(redirectURL==''){
				$(".gunOilBox_returnBack").hide();
			}else{
				location.href=redirectURL;
			}
		})
		initAdvertise();
	})

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
<body >

<input id="gunNo" type="hidden" value="${gunNo}" />
<input id="timestamp" type="hidden" value="${timestamp}" />
<input id="redAmount" type="hidden" value="${redAmount}" />
<input id="realAmount" type="hidden" value="${realAmount}" />

<input id="thirdOpenId" type="hidden" value="${thirdOpenId}" />
<input id="channelId" type="hidden" value="${channelId}" />
<input id="hasRed" type="hidden" value="${hasRed}" />
<input id="redId" type="hidden" value="${redId}" />
<input id="orderId" type="hidden" value="${orderId}" />
<input id="empno" type="hidden" value="${empno}" />
<input id="redirectURL" type="hidden" value="${redirectURL}">


<div class="zoomer" style="background-color: #ffffff;overflow-x:-webkit-overflow-scrolling:touch;">
	<div style="margin: 40px 40px 10px;text-align: center">
		<img src="img/paySuccess_1.png" alt="">
	</div>
	<div class="gunOilBox">
		<!--<div class="gunOilBox1">1号油枪加油完成，请放行</div>-->
		<div class="gunOilBox2"></div>
		<div class="gunOilBox3">
			<img src="img/dottedO.png" alt="">
			<a class="timeBg"></a>
			<img src="img/dottedO.png" alt="">
		</div>
        <%--<div class="gunOilBox_returnBack" onclick="BalanceGoBack()">返回</div>--%>
	</div>
	<!--<div class="index_content4 flex">
		<div>
			<img src="img/code3.png" alt="" width="160px">
			<img src="img/code3.png" alt="" style="    position: fixed;top: 20%;width: 70%;left: 0px;opacity: 0;">
		</div>
		<div class="flex-1">
			<div class="flex getRedCodeBanner_Div">
				<div class="flex-1 getRedTittle">恭喜您获得 <a>兴业银行提供的50元加油支付红包</a></div>
				<div class="getCodeRedBtn">领取</div>
			</div>
			<div class="getRedCodeBanner_tip">长按二维码随时查看你的红包和更多优惠</div>
		</div>
	</div>-->
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

	<script src="js/Scratch.js"></script>
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
			
		<a class="index_content5" style="margin: 20px 0;" href="https://prodone.juxinbox.com/sinopecGameCt/weixinMng/activity/getOilDropReward.htm">
			<img src="img/ad_oil.png" alt="" width="572px">
		</a>
			<!--<a class="index_content5">-->
			<!--<img src="img/banner5.png" alt="" width="572px">-->
			<!--</a>-->
			<div class="jmt_center pay_bottom_tel">
				<a href="tel:4000808065">推广合作</a>
			</div>
		</div>
</body>
	<script src="https://s22.cnzz.com/z_stat.php?id=1261960521&web_id=1261960521

" language="JavaScript"></script>
</html>