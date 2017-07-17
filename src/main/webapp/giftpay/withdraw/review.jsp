<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false" language="java" %>
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
		<title>有礼付提现审核</title>
		<script type="text/javascript">
			$(function () {
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
                    if (!$(this).hasClass("disabled")) {
                        $(".review_pop").show();
                    }
                })

				//确认按钮
				$("#allow").click(function(){
                    window.location.href = getRootPath() + "/giftpay/withdraw/allowWithdraw.htm";
				})

                //审核取消
                $(".review_pop .close").on("click",function(){
                    $(".review_pop").fadeOut();
                })

				if($("#status").val() == 0){
                    if($("#isBlack").val()=="true"){
						$("#blackUser").html("黑用户");
                        $(".review").addClass("disabled");
					}else{
                        $("#blackUser").hide();
					}
                    $(".review_list").show();
				}else if($("#status").val() == 1){
				    $(".review_list").hide();
				    $("#success").show();
				}else{
                    $(".review_list").hide();
                    $("#false").show();
				}
            })
		</script>
	</head>
	<body>
		<div class="zoomer">
			<!--审核弹框-->
			<div class="pop review_pop">
				<div class="centerDiv">
					<h1>是否通过审核？</h1>
					<div class="btn">
						<a class="close" href="javascript:;">取消</a>
						<a href="javascript:;" id="allow">通过</a>
					</div>
				</div>
			</div>
				<!--审核-->
				<div class="review_list">
					<!--有礼付订单信息-->
					<div class="tip">
						<h1>有礼付提现信息</h1>
						<p>
							<label>手机号：</label>
							<span>${withdrawModel.phone}</span>
						</p>
						<p>
							<label>提现申请时间：</label>
							<span>${withdrawModel.createTime}</span>
						</p>
						<p class="money">
							<label>提现金额：</label>
							<span id="withdrawAmount">￥${withdrawModel.withdrawAmount}</span>
						</p>
						<p>
							<label>零钱余额：</label>
							<span id="changeAmount">￥${balanceModel.balanceNumber}</span>
						</p>
						<p>
							<label>IP：</label>
							<span>${withdrawModel.ipAddress}</span>
						</p>
						<p>
							<label>OpenID：</label>
							<span>${withdrawModel.openId}</span>
						</p>
						<a class="btn" href="javascript:;" id="blackUser"></a>
					</div>
					<!--审核按钮/失效添加class:disabled-->
					<a class="review" href="javascript:;">审核通过</a>
				</div>
				<!--充值成功/默认隐藏审核通过显示-->
				<div class="status" id="success">
					<h1>
						<label>提现状态</label>
						<span>成功</span>
					</h1>
					<p>
						<label>付款时间</label>
						<span>${withdrawModel.auditTime}</span>
					</p>
					<p>
						<label>付款金额</label>
						<span>${withdrawModel.withdrawAmount}</span>
					</p>
				</div>
				<div class="status" id="false">
					<h1>
						<label>提现状态</label>
						<span>不通过</span>
					</h1>
					<p>
						<label>订单编号</label>
						<span>${withdrawModel.withdrawId}</span>
					</p>
				</div>
		</div>
		<input type="hidden" id="status" value="${withdrawModel.status}" />
		<input type="hidden" id="isBlack" value="${isBlack}" />
	</body>
</html>
