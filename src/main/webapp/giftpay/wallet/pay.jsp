<%--
  Created by IntelliJ IDEA.
  User: YuanYu
  Date: 15/12/28
  Time: 下午9:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java"
		 isELIgnored="false" %>
<html>
<head>
	<base href="${urlPrefix}/" />
	<title>微信支付</title>
	<script type="text/javascript" src="js/jQuery-1.11.3.js"></script>
	<script type="text/javascript" src="js/jWeChat-1.0.0.js"></script>
	<script src="js/jWeChat-Adaptive.js"></script>
	<script type="text/javascript">
		$(function(){
			$.ajax({
				url:$("base").attr("href")+'authorize/pay/$loadJs.htm',
				dataType : 'json',
				data:{currUrl : location.href.split('#')[0]},
				success : function(data)
				{
					wx.config({
						debug: data.debug,
						appId: data.appId,
						timestamp:data.timestamp,
						nonceStr:data.nonceStr,
						signature:data.signature,
						jsApiList:['chooseWXpay']
					});


// 				wx.error(function(res){
// 					alert(JSON.stringify(res));
// 				});

					wx.ready(function(){
						$.ajax({
							url: $("base").attr("href")+'authorize/pay/launchPay.htm',
							data: {tradeOrderID:$("#tradeOrderID").text()},
							dataType: 'json',
							success: function (data) {
								
								wx.chooseWXPay({
									timestamp: data.timestamp,
									nonceStr: data.nonceStr,
									package: data.package,
									signType: data.signType,
									paySign: data.paySign,
									success: function (res) {
										window.location.href = $("#redirectURL").text();
									},fail:function(err){
										alert("chooseWXPAY失败:"+JSON.stringify(err));
									}
								});
							},fail:function(msg){
								alert("luanchPay失败："+JSON.stringify(msg));
							}
						});
					});
				}
			});



		})
	</script>


</head>


<div style="display: none;" id="tradeOrderID">${orderInfo.ordertradeID}</div>
<div style="display: none;" id="redirectURL">
	${orderInfo.redirectURL}
</div>
<body>

</body>
</html>
