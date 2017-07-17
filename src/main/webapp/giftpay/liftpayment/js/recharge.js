var lifeType = '';
$(function() {
	console.log($("#redpkg").val());
	var accNo = getUrlParam("accNo");
	initUnitInfo(accNo);
	initBalance(accNo);
	initRedPkgList();
	$(".recharge").on("click", function() {
		var money = $(".rechargeMoney").val();
		var data = {};
		if(!money) {
			TipShow("请输入充值金额", 1000);
			return;
		}
		var redPkgId=$("#redpkg option:selected").val();
		var redPkgValue="";
		if(redPkgId==undefined||redPkgId==null||redPkgId==''){
			redPkgId='';
		}else{
			redPkgValue=$("#redpkg option:selected").attr("name");
		}
		
		location.href=getRootPath()+"/giftpay/liftpayment/redirectPayHome.htm?payamount="+money+
			"&rechargeType=life&lifeType="+lifeType+"&redPkgId="+redPkgId+"&redPkgValue="+redPkgValue+"&accNo="+accNo;
		/*loading("start");
		$.ajax({
			url: getRootPath() + "/giftpay/liftpayment/redirectPayHome.htm",
			type: "post",
			data: { "accNo": accNo,"payamount":money,"rechargeType":"life","lifeType":lifeType,"redPkgId":redPkgId,"redPkgValue":redPkgValue},
			dataType: "json",
			success: function(data) {
				loading("stop");
			},
			error: function() { loading("stop"); }
		});*/
	})
})

function initUnitInfo(accNo) {
	loading("start");
	$.ajax({
		url: getRootPath() + "/giftpay/liftpayment/queryAccountInfo.htm",
		type: "post",
		data: { "accNo": accNo },
		dataType: "json",
		success: function(data) {
			loading("stop");
			var data = data.mess;
			data = JSON.parse(data);
			$(".unitName").html(data[0].payUnitName);
			$(".unitNo").html(data[0].payAccount);
			lifeType = data[0].accountType;

		},
		error: function() { loading("stop"); }
	});
}

function initBalance(accNo) {
	loading("start");
	$.ajax({
		url: getRootPath() + "/giftpay/liftpayment/queryAccountBalance.htm",
		type: "post",
		data: { "accNo": accNo },
		dataType: "json",
		success: function(res) {
			loading("stop");
			var data = res.mess;
			var code=res.code;
			if(code!='1'){
				$("#balance").removeAttr("class");
				$("#balance").html("无欠费");
				$(".arrearage").hide();
				$(".recharge").attr("style", "display:none;");
				$(".not_arrearage").show();
				initPayList(accNo);
			}else{
				data = JSON.parse(data);
				if(data.balance * 1 > 0) {
					$("#balance").addClass("rmb");
					$("#balance").html(data.balance);
					$(".arrearage").show();
					$(".recharge").attr("style", "display:block;");
					$(".not_arrearage").hide();
				} else {
					$("#balance").removeAttr("class");
					$("#balance").html("无欠费");
					$(".arrearage").hide();
					$(".recharge").attr("style", "display:none;");
					$(".not_arrearage").show();
					initPayList(accNo);
				}
			}

			console.log(data);
		},
		error: function() { loading("stop"); }
	});
}
function initPayList(accNo){
	loading("start")
	$.ajax({
		url: getRootPath() + "/giftpay/liftpayment/queryPayList.htm",
		type: "post",
		dataType: "json",
		success: function (data) {
			loading("stop");
			console.log(data);
			if(data.code=='2'){
				$(".not_arrearage").hide();
			}else if(data.code=='1'){
				var pay=JSON.parse(data.mess);
				var str="";
				var result=true;
				$.each(pay,function(index,item){
					console.log(index)
					if(item.dealRemark==$(".unitNo").text()&&result){
						var time=item.dealTime;
						time=time.substring(0,time.indexOf(" "));
						$(".rechargeTime").text(time);
						$(".rmb").text(item.dealMoney);
						result=false;
						return;
					}
				});
				if(result){
					$(".not_arrearage").hide();
				}
			}else if(data.code=='3'){
				TipShow(data.mess,1000);
			}
		},
		error: function () {
			loading("stop");
		}
	});
}
function initRedPkgList() {
	loading("start");
	$.ajax({
		url: getRootPath() + "/giftpay/liftpayment/queryRedPkgList.htm",
		type: "post",
		dataType: "json",
		success: function(data) {
			loading("stop");
			console.log(data);
			var str = "";
			if(data==null||data==''||data==undefined){
				$(".redLi").hide();
			}else{
				$(".redLi").show();
			}
			if(data.length > 0) {
				str += '<select name="" id="redpkg" class="select11"><option value="">不使用抵用券</option>';
				$.each(data, function(index, item) {
					str += '<option name="' + item.value + '" value="' + item.redpkgId + '">' + item.redpkgName + '</option>';
				});
				str += '</select><img src="img/right_arrow.png" alt="">';
			} else {
				str += '<select name="" id="redpkg" disabled="disabled" class="select11"><option value="">暂无抵用券</option>';
			}
			$(".redpkgs").html(str);
		},
		error: function() { loading("stop"); }
	});
}