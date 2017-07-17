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
		<title>有礼付</title>
		<link type="text/css" href="css/common/common.css" rel="stylesheet">
		<link type="text/css" href="css/app.css" rel="stylesheet">
		<script type="text/javascript" src="js/common/jQuery-1.11.3.js"></script>
		<script type="text/javascript" src="js/common/jWeChat-Adaptive.js"></script>
		<script type="text/javascript" src="js/common/m.tool.juxinbox.com.js"></script>
		<script type="text/javascript" src="js/common/common.js"></script>
		<script type="text/javascript" src="js/city.js"></script>
		<script type="text/javascript" src="js/index.js"></script>
	</head>

	<script>
		var fid = ""; //记录当前家庭id
		$(function() {
			initFamilyInfo();
			$(".arrowBtn").on("click", function() {
				location.href = getRootPath() + '/giftpay/liftpayment/addAccount.html?fid=' + fid + "&accountType=" + $(this).attr("name");
			});

			$(".index_editAccount").on("click", function() {
				location.href = getRootPath() + '/giftpay/liftpayment/editAccount.html?fid=' + fid + "&accountType=" + $(this).attr("name");
			})
			$(".index_addBtn").on("click", function() {
				console.log($(this).attr("name"))
			})
			$(".setting_icon").on("click", function() {
				location.href = getRootPath() + '/giftpay/liftpayment/familyManager.html?fid=' + fid;
			})
			$(".flex-1").on("click",function(){
				var name=$(this).attr("class");
				name=name.substring(name.lastIndexOf(" ")+1,name.length);
				name=name.substring(0,name.indexOf("Account"));
				var no=$(this).find(".index_accountNo").text();
				console.log(no)
				if(no!=''){
					if(name=='shui'){
						location.href = getRootPath() + '/giftpay/liftpayment/rechargeAccount.html?accNo=' + $(".shuiId").val();
					}else if(name=='dian'){
						location.href = getRootPath() + '/giftpay/liftpayment/rechargeAccount3.html?accNo=' +  $(".dianId").val();
					}else{
						location.href = getRootPath() + '/giftpay/liftpayment/rechargeAccount2.html?accNo=' +  $(".meiId").val();
					}
				}
			})
		})

		function editAccount() {
			$('#edit').hide();
			$('#cancelEdit').show();
			$.each($(".index_accountNo"),function(index,item){
				var cla=$(item).attr("class");
				cla=cla.substring(cla.indexOf(" ")+1,cla.indexOf("AccountNo"));
				var content=$(item).html();
				if(content==''){
					$('.'+cla+"Span").hide();
					$('.arrowBtn').show();
				}else{
					$('.'+cla+"Span").show();
				}
			})
		}

		function editAccountCancel() {
			$('#cancelEdit').hide();
			$('#edit').show();
			$('.t_hide').hide();
			$('.arrowBtn').show();
			initAccountInfo(fid)
		}
		//选择我家  房东

		function selectTab(lab, id) {
			//			if(index == 1) {
			//				$('.html_top1').find('label:eq(0)').attr('class', 'label1');
			//				$('.html_top1').find('label:eq(1)').attr('class', 'label2');
			//			} else if(index == 2) {
			//				$('.html_top1').find('label:eq(1)').attr('class', 'label1');
			//				$('.html_top1').find('label:eq(0)').attr('class', 'label2');
			//			}
			$(".label1").removeClass("label1").addClass("label2");
			$(lab).removeAttr("class").addClass("label1");
			fid=id;
			selectFamily(id);
			editAccountCancel();
		}

		function initFamilyInfo(id) {
			loading("start");
			$.ajax({
				url: getRootPath() + "/giftpay/liftpayment/queryFamilyInfo.htm",
				type: "post",
				dataType: "json",
				success: function(data) {
					loading("stop");
					if(data.code=='0'){
						location.href = getRootPath() + "/giftpay/liftpayment/addFamily.html";
					}
					var fam = data.mess;
					fam = JSON.parse(fam);
					var str = '';
					$.each(fam, function(index, item) {
						if(index == '0') {
							str += '<label class="label1" onclick="selectTab(this,\'' + item.id + '\')">' + item.familyName + '</label>';
							$(".familyCity").html(item.cityName);
							fid = item.id;
						} else {
							str += '<label class="label2" onclick="selectTab(this,\'' + item.id + '\')">' + item.familyName + '</label>';
						}
					})
					str += '<span onclick="javascript:location.href=\'records.html\'">缴费记录</span>';
					$(".html_top1").html(str);
					initAccountInfo(fid);
				},
				error: function() {
					loading("stop");
				}
			});
		}

		function selectFamily(id) {
			loading("start");
			$.ajax({
				url: getRootPath() + "/giftpay/liftpayment/queryFamilyInfo.htm",
				type: "post",
				data:{"fid":id},
				dataType: "json",
				success: function(data) {
					loading("stop");
					var fam = data.mess;
					fam = JSON.parse(fam);
					$(".familyCity").html(fam[0].cityName);
					initAccountInfo(id);
				},
				error: function() {
					loading("stop");
				}
			});
		}

		function initAccountInfo(id) {
			loading("start");
			$.ajax({
				url: getRootPath() + "/giftpay/liftpayment/queryAccountInfo.htm",
				type: "post",
				data: { "fId": id },
				dataType: "json",
				success: function(data) {
					console.log(data);
					loading("stop");
					$(".shuiAccount").attr("class", "flex-1 index_ulName_ shuiAccount");
					$(".dianAccount").attr("class", "flex-1 index_ulName_ dianAccount");
					$(".meiAccount").attr("class", "flex-1 index_ulName_ meiAccount");
					$(".shuiAccountNo").hide();
					$(".dianAccountNo").hide();
					$(".meiAccountNo").hide();
					$(".shuiAccountNo").html("");
					$(".dianAccountNo").html("");
					$(".meiAccountNo").html("");
					$(".addShui").show();
					$(".addDian").show();
					$(".addMei").show();
					$(".shuiId").val("");
					$(".dianId").val("");
					$(".meiId").val("");
					$(".arrowBtn").show();
					if(data.code == '2') {
						data = data.mess;
						data = JSON.parse(data);
						$.each(data, function(index, item) {
							console.log(item)
							if(item.accountType == '001') {
								$(".shuiAccount").attr("class", "flex-1 index_haveAccount shuiAccount");
								$(".shuiAccountNo").show();
								$(".shuiAccountNo").html(item.payAccount);
								$(".addShui").hide();
								$(".shuiId").val(item.id);
								$(".arrowBtn[name='shui']").hide();
							} else if(item.accountType == '002') {
								$(".dianAccount").attr("class", "flex-1 index_haveAccount dianAccount");
								$(".dianAccountNo").show();
								$(".dianAccountNo").html(item.payAccount);
								$(".addDian").hide();
								$(".dianId").val(item.id);
								$(".arrowBtn[name='dian']").hide();
							} else {
								$(".meiAccount").attr("class", "flex-1 index_haveAccount meiAccount");
								$(".meiAccountNo").show();
								$(".meiAccountNo").html(item.payAccount);
								$(".addMei").hide();
								$(".meiId").val(item.id);
								$(".arrowBtn[name='mei']").hide();
							}
						});
					}
				},
				error: function() {
					loading("stop");
				}
			});
		}

		function deleteSure(id) {
			//    删除操作
			var accountId=$("."+id).val();
			$.ajax({
				url: getRootPath() + "/giftpay/liftpayment/removeFamily.htm",
				type: "post",
				dataType: "json",
				data: { "accountId": accountId },
				success: function(data) {
					loading("stop");
					console.log(data);
					if(data.code == '0') {
						TipShow('删除失败！', 1000);
						return;
					} else {
						TipShow('删除成功！', 1000);
						location.href = getRootPath() + "/giftpay/liftpayment/index.jsp";
					}
				},
				error: function() {
					loading("stop");
				}
			});
			//			$('#del').attr('class', 'bg_delete_off');
		}
	</script>

	<body style="background-color: #f0f0f4">
		<div style="background-color: #f0f0f4;position:fixed;" class="zoomer">
			<div class="index">
				<div class="html_top1">
					<!--	<label class="label1" onclick="selectTab(1)">我家</label>
					<label class="label2" onclick="selectTab(2)">房东</label>
					<span>缴费记录</span>-->
				</div>
				<div class="common_content">
					<div class="index_location">
						<img src="img/location.png" alt="">
						<span class="familyCity">南京市</span>
						<a href="javascript:;">
							<img src="img/setting.png" alt="" class="setting_icon">
						</a>

					</div>
				</div>
				<div class="common_content">
					<div class="index_account">
						<span>缴费账户</span>
						<label id="edit" onclick="editAccount()">编辑</label>
						<label id="cancelEdit" style="display: none;" onclick="editAccountCancel()">取消编辑</label>
					</div>
					<ul class="index_ul">
						<li class="flex" name="shui">
							<div class="index_ulIcon">
								<img src="img/water.png" alt="">
							</div>
							<div class="flex-1 index_haveAccount shuiAccount">
								<div class="index_ulName">水费</div>
								<span class="index_accountNo shuiAccountNo"></span>
							</div>
							<div class="index_ulName_1">
								<span class="t_hide shuiSpan">
                               <a class="index_editAccount" name="shui">修改</a>
                               <input type="hidden" value="" class="shuiId" />
                             	<a class="index_addBtn" onclick="deleteSure('shuiId')" name="shui">删除</a>
                         </span>
								<span class="arrowBtn" name="shui">
                            	<a class="index_addBtn1 t_hide addShui"  name="shui">添加账号</a><img src="img/right_arrow.png" alt="">
                         </span>
							</div>
						</li>

						<li class="flex" name="dian">
							<div class="index_ulIcon">
								<img src="img/bulb.png" alt="">
							</div>
							<div class="flex-1 index_ulName_ dianAccount">
								<div class="index_ulName">电费</div>
								<span class="index_accountNo dianAccountNo"></span>
							</div>
							<div class="index_ulName_">
								<span class="t_hide dianSpan">
                            <a class="index_editAccount" name="dian">修改</a>
                             <input type="hidden" value="" class="dianId" />
                             <a class="index_addBtn" onclick="deleteSure('dianId')"  name="dian">删除</a>
                         </span>
								<span class="arrowBtn" name="dian">
                           <a class="index_addBtn1 addDian"  name="dian">添加账号</a><img src="img/right_arrow.png" alt="">
                         </span>
							</div>
						</li>
						<li class="flex" name="mei">
							<div class="index_ulIcon">
								<img src="img/fire.png" alt="">
							</div>
							<div class="flex-1 index_ulName_ meiAccount">
								<div class="index_ulName">燃气费</div>
								<span class="index_accountNo meiAccountNo"></span>
							</div>
							<div class="index_ulName_">
								<span class="t_hide meiSpan">
                            <a class="index_editAccount"  name="mei">修改</a>
                             <input type="hidden" value="" class="meiId" />
                            <a class="index_addBtn" onclick="deleteSure('meiId')"  name="mei">删除</a>
                         </span>
								<span class="arrowBtn" name="mei">
                             <a class="index_addBtn1 addMei"  name="mei">添加账号</a><img src="img/right_arrow.png" alt="">
                         </span>
							</div>
						</li>

					</ul>
				</div>
			</div>
		</div>
	</body>

</html>