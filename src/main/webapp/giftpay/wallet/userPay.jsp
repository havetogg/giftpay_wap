
<%@ page contentType="text/html; charset=utf-8" language="java"
	isELIgnored="false" errorPage=""%>
<%@include file="inc/timestamp.inc"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<base href="${urlPrefix}/" />
<title></title>
<%@include file="inc/head.inc"%>
<meta name="viewport"
	content="initial-scale=0.5,maximum-scale=0.5,minimum-scale=0.5, width=640, target-densitydpi=device-dpi">
<meta http-eqiv="X-UA-Compatible" content="IE=Edge,chrome=1">
<meta content="yes" name="apple-mobile-web-app-capable">
<meta content="black" name="apple-mobile-web-app-status-bar-style">
<meta content="telephone=no" name="format-detection">
<title>中石化微信支付</title>
<link type="text/css" href="css/cssAdd/app.css" rel="stylesheet">
<link type="text/css" href="css/cssAdd/common/common.css"
	rel="stylesheet">
<script type="text/javascript" src="js/jsAdd/common/jQuery-1.11.3.js"></script>
<script type="text/javascript" src="js/jsAdd/common/jWeChat-Adaptive.js"></script>
<script type="text/javascript"
	src="js/jsAdd/common/m.tool.juxinbox.com.js"></script>
<script type="text/javascript" src="js/jsAdd/common/jWeChat-1.0.0.js"></script>
<script type="text/javascript" src="js/jsAdd/index.js"></script>
<%--<script src="js/userPay.js?<%=v%>"></script>--%>
<script>
	var rechargeValue = 0;
	var exp = new RegExp("^\\d+(\\.\\d+)?$");
	var jinzhi = 100;
	var payId = "";
	var redListLength=0;
	function close_tip() {
		$(".bg_block").hide();
		jinzhi = 100;
	}
	function Tip_bank(type) {
		$(".bg_block").hide();
	}
	//支付类型选择
	function selectType() {
		$(".pay_type ").show();
		$(".red_type ").hide();
		$(".integral_type ").hide();
<%--$("#payTypeSelected ").html($("#payType").val());--%>
	}
	//没有支付类型选择
	function selectNone() {
		$(".pay_type ").hide();
	}

	//红包选择
	function selectRed() {
		 if (!$("#rechargeValue").val()) {
			TipShow('请输入充值金额', 1000);
		} else if(redListLength>0) {
			$(".red_type ").show();
			$(".pay_type ").hide();
			$(".integral_type ").hide();
			$("#redTypeSelected ").html($("#redType").val());
		}
	}
	var hasBenefit = 0;
	var redId = "";
	function selectRedNone() {
		$(".red_type ").hide();
	}
	function selectRedNone_() {
		$(".red_type ").hide();
		hasBenefit = 0;
		redId = "";
		$("#redType").val("不使用红包");
		$("#realAmount").html($("#rechargeValue").val());
	}
	function selectRedType(self, cut, id) {
		if (cut) {
			hasBenefit = 1;
			redId = id;
		} else {
			hasBenefit = 0;
			redId = id = "";
		}
		$("#redType").val($(self).html());
		$(".red_type ").hide();
		$("#redTypeSelected ").html($(self).html());
		$("#realAmount").html(
				($("#rechargeValue").val() - cut / 100).toFixed(2));
		var html = "<div onclick=\"selectRedNone(this)\"><label id=\"redTypeSelected\">"
				+ $(self).html()
				+ "</label><img src=\"img/arrow2.png\" alt=\"\" class=\"arrow_tip\"></div>";
		$("#redParent").html(html);
	}
	function noSelectRedType(self){
		$("#redType").val("不使用红包");
		$(".red_type ").hide();
		$("#redTypeSelected ").html("不使用红包");
		var html = "<div onclick=\"selectRedNone(this)\"><label id=\"redTypeSelected\">不使用红包</label><img src=\"img/arrow2.png\" alt=\"\" class=\"arrow_tip\"></div>";
	$("#redParent").html(html);
	}
	//积分选择
	function selectIntegral() {
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
		jinzhi = 0;
		document.addEventListener("touchmove", function(e) {
			if (jinzhi == 0) {
				e.preventDefault();
				e.stopPropagation();
			}
		}, false);
	}
	//立即支付
	function payNow() {
		if (!$('#rechargeValue').val()) {
			TipShow('充值金额不能为空', 1000);
		} else if (!exp.test($('#rechargeValue').val())) {
			TipShow('请输入正确的金额', 1000);
		} else {
			loading("start");
			getWxSign(function() {
				totale();
			})
		}
	}
	$('.bg_block').click(function(e) {
		e.stopPropagation();
		return false;
	});
	function init() {
<%--初始化支付方式--%>
	showPayType();
		initRed();
		var bankListHTML = "";
		$
				.ajax({
					url : getRootPath().replace('wxpage','') + "queryActivities.htm",
					dataType : "json",
					type : "post",
					async : false,
					success : function(data) {
						if (data.resultList.length > 0) {
							for (var i = 0; i < data.resultList.length; i++) {
								bankListHTML += "    <li class=\"flex\">"
										+ "                    <div class=\"flex-1 lineHeight50\">"
										+ "                        <img src=\""+data.resultList[i].icon+"\" alt=\"\" width=\"50px\">"
										+ "                    </div>"
										+ "                    <div class=\"flex-4 jmt_left bankContent\">"
										+ "                        <div>"
										+ data.resultList[i].name + "</div>"
										+ "                        <nav>"
										+ data.resultList[i].desc + "</nav>"
										+ "                    </div>"
										+ "                </li>";
							}
							$("#bankListHTML").html(bankListHTML);
						} else {
							$("#bankListHTML").html(
									"   <div class=\"jmt_center\">暂无数据</div>");
						}
					},
					error : function() {
<%--alert("获取失败");--%>
	}
				});
	}
	function getRechargeValue() {
		if (!exp.test($('#rechargeValue').val())) {
			$("#rechargeValue").val("");
			TipShow('请输入正确的金额', 1000);
		} else {
			rechargeValue = $("#rechargeValue").val();
			$("#realAmount").html($("#rechargeValue").val());
			switchRed(rechargeValue);
			if(redListLength>0){
			$("#redType").val("请选择红包");
			}
			else{
			$("#redType").val("暂无红包");
			}
		}
	}
	function initRed() {
		var redListHTML = "<nav id=\"redParent\"></nav>";
		$.ajax({
			url : getRootPath().replace('wxpage','') + "queryExchanges.htm?openId="+getQueryString("userOpenID"),
			dataType : "json",
			type : "post",
			async : false,
			success : function(data) {
				console.log(data);
				if (data.resultList.length > 0) {
					redListHTML+='<div class="jmt_center" onclick="noSelectRedType(this)">不使用红包</div>'
					for (var i = 0; i < data.resultList.length; i++) {
						redListHTML += "<div onclick=\"selectRedType(this,'"
								+ data.resultList[i].benefit + "','"
								+ data.resultList[i].id + "')\">"
								+ data.resultList[i].desc + "</div>";
					}
					$("#redListHTML").html(redListHTML);
				} else {
					$("#redListHTML").html(
							"   <div class=\"jmt_center\">暂无红包</div>");
				}
			},
			error : function() {
<%--alert("获取失败");--%>
	}
		});
	}
	function switchRed(ammount) {
		var redListHTML = "<nav id=\"redParent\"></nav>";
		$.ajax({
					url : getRootPath().replace('wxpage','') + "queryExchanges.htm?openId="+getQueryString("userOpenID"),
					dataType : "json",
					type : "post",
					async : false,
					success : function(data) {
						if (data.resultList.length > 0) {
							for (var i = 0; i < data.resultList.length; i++) {
								if (ammount*100> data.resultList[i].benefit) {
									redListLength++
									redListHTML += "<div onclick=\"selectRedType(this,'"
											+ data.resultList[i].benefit+ "','"
											+ data.resultList[i].id + "')\">"
											+ data.resultList[i].desc
											+ "</div>";
								}
								else{
									$("#redListHTML").html(
									"   <div class=\"jmt_center\">暂无数据</div>");
								}
							}
							redListHTML+= "<div onclick=\"selectRedNone_()\">不用红包</div>";
							$("#redListHTML").html(redListHTML);
						} else {
							$("#redListHTML").html(
									"   <div class=\"jmt_center\">暂无数据</div>");
						}
					},
					error : function() {
						alert("获取失败");
					}
				});
	}
	function totale() {
<%--var payType=$("#payType").val();//支付类型--%>
	
<%--var redTypeSelected=$("#redType").val();//红包类型--%>
	
<%--var payMoney=$("#realAmount").html();//支付金额--%>
	var paydata = {
			payTypeID : payId,
			money : $("#realAmount").html(),
			goodsDesc : '线下支付',
			userOpenID : getQueryString("userOpenID"),
			sbxUserID : getQueryString("sbxUserID"),
			iUID : getQueryString("iUID")
		};

		// 微信支付动作
		wxPay(
				paydata,
				function(orderId) {
					$.ajax({
						url : getRootPath().replace('wxpage','')+ "queryOrderForGiftpay.htm",
						data : {
							"openId" : getQueryString("userOpenID"),
							"orderId" : orderId,
							"hasBenefit" : hasBenefit,
							"benefitid" : redId
						},
						dataType : "json",
						type : "post",
						success : function(data) {
							//alert("activityId="+ data)
							loading("stop");
							//1.有使用抵扣券 更新红包状态为已使用 跳转至交易成功页面			paySuccess3.html
							//2.提示用户去绑定手机号 跳转至获得红包页面					paySuccess.html
							//3.验证通过，提示用户获得支付红包，下次支付可用	paySuccess2.html
							//4.绑定手机号不正确							paySuccess3.html
							//5.手机号为空，提示用户去绑定手机号页面			paySuccess.html
							//失败										payFail.html
							var pageName = 'paySuccess3.html';
							if(data == 1){
								pageName = 'paySuccess3.html';
								window.location.href = getRootPath()+ "/"+pageName+"?openId="+ getQueryString("userOpenID")+ "&activityId="+ data;
							}else if(data == 2){
								pageName = 'paySuccess.html';
								window.location.href = getRootPath()+ "/"+pageName+"?openId="+ getQueryString("userOpenID")+ "&activityId="+ data;
							}else if(data == 4){
								pageName = 'paySuccess4.jsp';
								window.location.href = getRootPath()+ "/"+pageName+"?openId="+ getQueryString("userOpenID")+ "&activityId="+ data + "&realAmount="+ $("#realAmount").html();
							}else if(data == 6){
								pageName = 'paySuccess6.html';
								window.location.href = getRootPath()+ "/"+pageName+"?openId="+ getQueryString("userOpenID")+ "&activityId="+ data + "&realAmount="+ $("#realAmount").html();
							}
							window.location.href = getRootPath()+ "/"+pageName+"?openId="+ getQueryString("userOpenID")+ "&activityId="+ data;
						},
						error : function(res) {
							loading("stop");
							alert("fail");
						}
				});
					}
				, function() {
					loading("stop");
					console.log(res);
					alert("fail");
					window.location.href = getRootPath().replace('wxpage','')+ "payFail.html"
				});
	}
	function showPayType() {
		var data = {
			userNum : getQueryString("iUID")
		}
		$
				.ajax({
					url : getRootPath().replace('wxpage','')
							+ 'bms/wechat/paytype/queryPayType.htm',
					method : 'post',
					data : data,
					datatype : 'json',
					success : function(data) {
						var jsondata = JSON.parse(data);
						var html = "  <div id=\"defaultName\" onclick=\"selectNone()\">"
								+ jsondata[0].payName
								+ "<img src=\"img/arrow2.png\" alt=\"\" style=\"top: 25px;width: 30px;\"></div>";
						payId = jsondata[0].payID;
						for (var i = 0; i < jsondata.length; i++) {
							if (jsondata.length > 0) {
								html += "<div onclick=\"selectPayType(this,'"
										+ jsondata[i].payID + "')\">"
										+ jsondata[i].payName + "</div>";
							}
						}
						$(".pay_type").append(html);
					}
				})
	}
	function selectPayType(self, id) {
		payId = id;
		$("#payType").val($(self).html());
		$(".pay_type ").hide();
		$("#defaultName").html($(self).html());
	}
</script>
</head>
<body onload="init()">
	<!--弹框-->
	<div class="bg_block">
		<div class="index_bg">
			<div class="index_TipTop">
				银行卡支付优惠 <img src="img/close.png" alt="" onclick="close_tip()">
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
						<li><span class="index_inputName">支付类型</span> <input
							id="payType" type="text" readonly value="加油加气"
							onclick="selectType()"> <img src="img/arrow1.png" alt="">
							<div class="pay_type"></div></li>
						<li><span class="index_inputName">支付金额</span> <input
							id="rechargeValue" type="text" style="font-family: '黑体'"
							placeholder="请输入任意金额" onblur="getRechargeValue()"></li>
						<div>
							<span class="real_ammount">实付金额：¥ <a id="realAmount">0</a></span>
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
				<li><span>支付红包</span>
				<input id="redType" type="text" readonly value="请选择红包" onclick="selectRed()">
				 <img src="img/arrow1.png" alt="" class="arrow1">
				<div style="font-size: 18px;color: #888;margin-top: 10px;margin-left: 115px;">TIPS：使用红包，支付金额需大于红包金额</div>
					<div class="red_type" id="redListHTML">
						<!--<div onclick="selectRedNone(this,100,10)"><label id="redTypeSelected">满100元可使用10元支付红包</label><img src="img/arrow2.png" alt="" class="arrow_tip">-->
						<!--</div>-->
						<!--<div onclick="selectRedType(this,100,10)">满100元可使用10元支付红包</div>-->
						<!--<div onclick="selectRedType(this,200,20)">满200元可使用20元支付红包</div>-->
						<!--<div onclick="selectRedType(this,300,30)">满300元可使用30元支付红包</div>-->
						<!--<div onclick="selectRedType(this,400,40)">满400元可使用40元支付红包</div>-->
						<!--<div onclick="selectRedType(this,500,100)">满500元可使用100元支付红包</div>-->
					</div></li>
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
				<li><span>银行活动</span> <label class="bankTip_select"
					onclick="selectTipShow()"> <a>银行卡</a> 银行卡支付优惠
						<div class="bankIcons">
							<img src="img/bank_zs.png" alt="" width="30px" style="visibility:hidden"> <img
								src="img/bank_xy.png" alt="" width="30px"> ...
						</div>

				</label> <img src="img/arrow1.png" alt="" class="arrow1"></li>
			</ul>
			<div class="jmt_center" style="margin: 50px 30px 40px;">
				<img src="img/payNow.png" alt="" onclick="payNow()">
			</div>
			<div class="jmt_center" style="height: 1px;">
				<img src="img/line.png" alt="" width="100%" height="1px">
			</div>
		</div>
		<div class="index_footer_tittle">
			<div class="footer_tittle">支付须知</div>
			<div class="index_footer_content">
				支付类型、支付金额为必填选项，其他选项均为非必填选项请选择正确的支付类型，否则会影响到后续服务</div>
		</div>
	</div>
	<input type="hidden" id="openId" value="${openId}">
</body>
</html>