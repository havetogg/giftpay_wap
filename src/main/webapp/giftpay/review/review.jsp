<%@ page language="java" import="java.util.*" isELIgnored="false" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html;charset=utf-8">
		<meta name="viewport" content="initial-scale=0.5,maximum-scale=0.5,minimum-scale=0.5, user-scalable=no,width=640, target-densitydpi=device-dpi">
		<meta name="apple-mobile-web-app-capable" content="yes">
		<meta name="apple-mobile-web-app-status-bar-style" content="black">
		<meta name="format-detection" content="telephone=no">
		<link rel="stylesheet" href="css/count.css" />
		<link rel="stylesheet" href="css/common/common.css" />
		<script type="text/javascript" src="js/jQuery-1.11.3.js"></script>
		<script type="text/javascript" src="js/layout.js" ></script>
		<script type="text/javascript" src="js/common/common.js" ></script>
		<script type="text/javascript" src="js/common/m.tool.juxinbox.com.js" ></script>
		<title>有礼付订单审核</title>
	</head>
	<body>
		<div class="zoomer">
			<!--审核弹框-->
			<div class="pop review_pop">
				<div class="centerDiv">
					<h1>是否通过审核？</h1>
					<div class="btn">
						<a class="close" href="javascript:;">取消</a>
						<a class="toRechargeOrder" href="javascript:;">通过</a>
					</div>
				</div>
			</div>
			<!--审核-->
			<div class="review_list">
				<!--有礼付订单信息-->
				<div class="tip">
					<h1>有礼付订单信息</h1>
					<p>
						<label>手机号：</label>
						<span class="orderPhone"></span>
					</p>
					<p>
						<label>商品名称：</label>
						<span class="orderName"></span>
					</p>
					<p>
						<label>预支付订单时间：</label>
						<span class="orderTime"></span>
					</p>
					<p class="money">
						<label>订单金额：</label>
						<span class="orderMoney">￥</span>
					</p>
					<p>
						<label>实际支付金额：</label>
						<span class="orderPayMoney"></span>
					</p>
					<p>
						<label>IP：</label>
						<span class="orderIP"></span>
					</p>
					<p>
						<label class="orderOpenIdLabel">OpenID：</label>
						<span class="orderOpenId"></span>
					</p>
					<a class="btn" href="javascript:;">拉黑</a>
				</div>
				<!--微信商户后台信息-->
				<div class="tip2">
					<h1>微信商户后台订单</h1>
					<p>
						<label>支付时间：</label>
						<span class="wxTime"></span>
					</p>
					<p>
						<label>微信订单号：</label>
						<span class="wxOrder"></span>
					</p>
					<p class="money">
						<label>支付金额：</label>
						<span class="wxMoney"></span>
					</p>
					<p>
						<label>OpenID：</label>
						<span class="wxOpenId"></span>
					</p>
				</div>
				<!--审核按钮/失效添加class:disabled-->
				<a class="review" href="javascript:;">审核通过</a>
			</div>
			<!--充值成功/默认隐藏审核通过显示-->
			<div class="status">
				<h1>
					<label>充值状态</label>
					<span id="success">成功</span>
				</h1>
				<p>
					<label>商品名称</label>
					<span id="goodsName">油卡充值</span>
				</p>
				<p>
					<label>充值时间</label>
					<span id="time"></span>
				</p>
				<p>
					<label id="rechargeNumType">充值号码 </label>
					<span id="number"></span>
				</p>
			</div>
		</div>
		<script>
			//拉黑
			/*$(".tip .btn").on("click",function(){
				if ($(this).hasClass("black")) {
					$(this).text("拉黑").removeClass("black");
					$(".review").removeClass("disabled");
				} else{
					$(this).text("取消拉黑").addClass("black");
					$(".review").addClass("disabled");
				}
			})*/
			
			//审核通过
			$(".review").on("click",function(){
				//$(".review").addClass("disabled");
				if (!$(this).hasClass("disabled")) {
					$(".review_pop").show();
				} 
			})
			
			//审核取消
			$(".review_pop .close").on("click",function(){
				$(".review_pop").fadeOut();
			})
		</script>
		<script>
			$(function(){
				showOrderInfo();
				$(".toRechargeOrder").on("click",function(){
					//获取充值类型
					loading("start");
					$.ajax({
						url: getRootPath() + "/giftpay/review/toRechargeGoods.htm",
						type: "post",
						data: {"orderNo": $("#orderNo").val()},
						dataType: "json",
						success: function (data) {
							loading("stop");
									$(".review_pop").hide();
							console.log(data);
							alert(data.mess);
						},error:function(){
							loading("stop");
						}
					})
				});
			})

			function showOrderInfo(){
				loading("start");
				$.ajax({
					url: getRootPath() + "/giftpay/review/queryOrderInfo.htm",
					type: "post",
					data: {"orderNo": $("#orderNo").val()},
					dataType: "json",
					success: function (data) {
						loading("stop");
						if(data.code=='0'){
							alert(data.mess);
						}else if(data.code=='1'){
							var result=JSON.parse(data.mess);
							var orderInfo=(JSON.parse(result.orderInfo))[0];
							var payInfo=(JSON.parse(result.payInfo))[0];
							if(payInfo.ofOrder!=''){
								$(".review_list").hide();
								$(".status").show();
								if(orderInfo.fromType=='3'){
									//流量充值反馈数据
                                    queryFlowOrder(orderInfo.orderNo)
								}else{
                                    queryAllOrder(orderInfo.orderNo);
								}

							}else{
								$(".review_list").show();
								$(".status").hide();
								$(".orderName").text(orderInfo.goodsName);
								$(".orderTime").text((payInfo.dealTime.substring(0,payInfo.dealTime.indexOf("."))));
								$(".orderMoney").text("￥"+orderInfo.payMoney);
								$(".orderPayMoney").text("￥"+payInfo.dealRealMoney);
								$(".orderIP").text(orderInfo.ip);
								$(".orderOpenId").text(orderInfo.openId);
								var wxMap=JSON.parse(result.wxMap);
								console.log(wxMap)
								if(!wxMap.result){
									$(".wxTime").text(wxMap.errMsg.substring(wxMap.errMsg.indexOf("A[")+2,wxMap.errMsg.indexOf("]]")));
									$(".wxMoney").text("");
								}else{
									$(".wxTime").text(wxMap.timeEnd);
									$(".wxOrder").text(wxMap.transactionId);
									var wxMoney=new Number(wxMap.cashFee);
									$(".wxMoney").text("￥"+(wxMoney/100).toFixed(2));
									$(".wxOpenId").text(wxMap.openId);
								}
								showUserInfo(orderInfo.openId);
								var blackStatus=JSON.parse(result.black);
								if(blackStatus){
									//黑名单
									$(".tip .btn").html("在黑名单");
									$(".review").addClass("disabled");
								}else{
									$(".tip .btn").html("非黑名单");
									$(".review").removeClass("disabled");
								}
							}
						}else if(data.code=='2'){
							var result=JSON.parse(data.mess);
							var orderInfo=(JSON.parse(result.balance))[0];
							console.log(orderInfo);
							if(orderInfo.ofOrder!=''){
								$(".review_list").hide();
								$(".status").show();
								$("#goodsName").text(orderInfo.dealInfo+" "+orderInfo.dealRealMoney+"元");
								$("#time").text((orderInfo.dealTime.substring(0,orderInfo.dealTime.indexOf("."))));
								$("#number").text(orderInfo.orderNo);
								$("#rechargeNumType").html("充值订单号:");
								//显示用户余额数据
							}else{
								//显示审核订单信息
								$(".review_list").show();
								$(".status").hide();
								$(".orderName").text(orderInfo.dealInfo+" "+orderInfo.dealMoney+"元");
								$(".orderTime").text((orderInfo.dealTime.substring(0,orderInfo.dealTime.indexOf("."))));
								$(".orderMoney").text("￥"+orderInfo.dealMoney);
								$(".orderPayMoney").text("￥"+orderInfo.dealRealMoney);
								$(".orderIP").text(orderInfo.ip);
								$(".orderOpenId").text(orderInfo.accountId);
								$(".orderOpenIdLabel").html("用户UserID：");
							}
						}
					},error:function(){
						loading("stop");
					}
				})
			}

			function showUserInfo(accountId){
				loading("start");
				$.ajax({
					url: getRootPath() + "/giftpay/review/queryUserInfoByAccountId.htm",
					type: "post",
					data: {"accountId":accountId},
					dataType: "json",
					success: function (data) {
						loading("stop");
						if(data.code=='0'){
							alert(data.mess);
						}else{
							var result=JSON.parse(data.mess);
							result=result[0];
							$(".orderPhone").text(result.phone);
						}
					},error:function(){
						loading("stop");
					}
				})
			}
			function queryAllOrder(order){
				loading("start");
				$.ajax({
					url: getRootPath() + "/giftpay/review/queryOFOrderInfo.htm",
					type: "post",
					data: {"orderNo":order},
					dataType: "json",
					success: function (data) {
						loading("stop");
						location.href=data;
					},error:function(){
						loading("stop");
					}
				})
			}

			//流量充值操作结果查询
			function queryFlowOrder(order){
				loading("start");
                $.ajax({
                    url: getRootPath() + "/flow/searchOrderStatusByOrderNo.htm",
                    type: "post",
                    data: {"orderNo":order},
                    dataType: "json",
                    success: function (data) {
						loading("stop");
                        if(data.ofOrder!=""){
							$("#success").text("成功")
							$("#goodsName").text(data.goodsName);
                            $("#time").text(data.createTime)
                            $("#number").text(data.remark)
						}
                    },error:function(){
						loading("stop");
					}
                })
			}
		</script>
		<input type="hidden" id="orderNo" value="${orderNo}" />
	</body>
</html>
