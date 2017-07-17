/**
 * Created by Administrator on 2016/11/25.
 */
var rechargeAmount='';   //选择时的金额
var tel='';
var cardNo='';
var amount='';           //手输入的金额
var prodID='';           //充值套餐种类
var phoneflag=false;
var cardNoflag=false;
var nameflag=false;

var changeType=-1;     // 1:固定充值 2:任意充值
var fuelCardType=-1;     // 加油卡类型   1:中石化 2:中石油
var cardNum=0;           // 充值金额（任意冲金额，其余面额固定为1）
function selectAmount(self,price,pprodID) {
    rechargeAmount=price;
    prodID=pprodID;
    $('.cellPhoneNumberPrice').find('li').attr('class','flex-1 jmt_center phone');
    $(self).attr('class','flex-1 jmt_center phone_on');
    if(rechargeAmount!='other'){
    	// 选择金额
    	$('#amount').val("");
    	$('#amount').attr("disabled", true);
        ifPay();
    }else{
    	// 选择其他金额
    	$('#amount').val("");
    	$('#amount').attr("disabled",false);
    	ifPay();
    }

}
function rechargeNow() {
    tel=$('#tel').val();
    amount=$('#amount').val();
    name=$('#name').val();
    if(!tel){
        TipShow("请输入手机号",1000);
    }
    else if(13!=tel.length){
        TipShow("请输入正确的手机号",1000);
    }
    else if(!phoneflag){
        TipShow("请输入正确的手机号",1000);
    }else if(!nameflag){
        TipShow("请输入持卡人姓名",1000);
    }
    else if(rechargeAmount=='other' && !amount){
    	 TipShow("请输入充值金额",1000);
    }
    else if(rechargeAmount=='other' && !jxTool.isInt(amount)){
   	     TipShow("请输入正确的充值金额",1000);
   }
    else if(!rechargeAmount){
        TipShow("请选择充值金额",1000);
    }
    else{
    	if(rechargeAmount=='other'){
    		 TipShow("other充值成功！"+amount,1000);
    		 changeType=2;          // 固定充
    		 cardNum=amount;
    	}else{
            TipShow("充值成功！"+rechargeAmount,1000);
            changeType=1;          // 任意充
            cardNum=rechargeAmount;
    	}
    	// 确认提交
    	confirw("确认提交充值", "", "", "", 
    			function(){
    		    // OK 
    		    // 充值提交
    		    changeSubmit();
    	        }, function(){
    	         // NG
    	        	
    	        });
    }
}
$("#tel").on("input propertychange",function(){
    alert("1243546");
})

// 输入手机号
function OnInput (event) {
    var tel=event.target.value;
    // 13位时调用接口
    if(tel.length==13){
    	getphoneStatus(tel);
    }else{
    	// 小于13位时,不能支付
    	$("#phoneaddress").html("未知");
    	phoneflag=false;
    	ifPay();
    }

}

//输入姓名
function nameOnInput() {
    var name=event.target.value;
    $("#name").val(name.trim());  //清除空格
    // 13位时调用接口
    if(""!=name.trim()){
    	// 不是空
    	nameflag=true;
    }else{
    	// 空
    	nameflag=false;
    }
    ifPay();
}
// 输入油卡
function cardInput(event) {
    cardNo=event.target.value;
    // 值了判断是不是正确的金额
	if(cardNo){
		// 有值
		if(!/^\d+$/.test(cardNo)){
			 cardNoflag=false;
			 TipShow("请输入正确的卡号!",1000);
		}else{
			 if(0==cardNo.indexOf("100011")){
				 // 中石化
				 if(19==cardNo.length){
					 cardNoflag=true;
					 $("#cardtypeid").html("中国石化");
				 }else{
					 cardNoflag=false;
					 $("#cardtypeid").html("未知");
				 }
				 fuelCardType=1;
			 }else if(0==cardNo.indexOf("9")){
				// 中石石油卡
				 if(16==cardNo.length){
					 cardNoflag=true;
					 $("#cardtypeid").html("中国石油");
				 }else{
					 cardNoflag=false;
					 $("#cardtypeid").html("未知");
				 }

				 fuelCardType=2;
			 }else{
				 cardNoflag=false;
				 $("#cardtypeid").html("未知");
				 fuelCardType=-1;
				 
			 }
		}
	}else{
		//没有值
		 cardNoflag=false;
		 $("#cardtypeid").html("未知");
		 fuelCardType=-1;
	}
    ifPay();
}
function ifPay() {
    amount=$('#amount').val();
    //选择充值金额
    if(rechargeAmount!='other'){
//                alert("选择金额");
        if(phoneflag&&cardNoflag&&nameflag&&rechargeAmount){
            $('#PayShow').show();
            $('#PayHide').hide();
            return true
        }
        else{
            $('#PayShow').hide();
            $('#PayHide').show();
            return false
        }
    }
    //输入充值金额
    else{
//      alert("输入金额");
        if(phoneflag&&cardNoflag&&nameflag&&amount){
            $('#PayShow').show();
            $('#PayHide').hide();
            return true
        }
        else{
            $('#PayShow').hide();
            $('#PayHide').show();
            return false
        }
    }
}

// 其他时,输入金额
function amountInput() {
    amount=$('#amount').val();
    // 值了判断是不是正确的金额
	if(amount&&!jxTool.isInt(amount)){
		 TipShow("请输入正确的充值金额!",1000);
	}else{
	    ifPay();
	}

}
function handle() {
    var inputVal = $('#tel').val(),
        trimPhoneNum = inputVal.replace(/\s+/g, ''),
        len = trimPhoneNum.length;
    if (len < 4) {
        var value1 = trimPhoneNum.replace(/\s+/g, '');
        $('#tel').val(value1);
    } else if (len >= 4 && len < 8) {
        $('#tel').val(trimPhoneNum.replace(/(\d{3})(\d+)/g, '$1 $2'));
    } else if (len >= 8 && len < 11) {
        $('#tel').val(trimPhoneNum.replace(/(\d{3})(\d{4})(\d+)/g, '$1 $2 $3'));
    } else if (len == 11) {
        $('#tel').val(trimPhoneNum.replace(/(\d{3})(\d{4})(\d{4})/g, '$1 $2 $3'));
    } else if (trimPhoneNum.length > 11) {
        return false;
    }
}

// 进行手机号归属地的查询
function getphoneStatus(tel){
	    //淘宝接口    
		telnospace=tel.replace(new RegExp(" ","gm"),"");
	    $.ajax({
	         type: "get",
	         url: 'http://tcc.taobao.com/cc/json/mobile_tel_segment.htm?tel='+telnospace,
             dataType: "jsonp",
             jsonp: "callback",
	         success: function(data){
	            console.log(data);
	            var province = data.province;
	            var    operators = data.catName;
	            var    num = data.carrier;
	            phoneflag=false;
	            if(!num){
	            	// 号码不对
	            	TipShow("请输入正确手机号",1000);
	            	phoneflag=false;
	            }else{
	            	$("#phoneaddress").html(num);
	            	phoneflag=true;
	            }
	            ifPay();
	         },
	         error:function (){   
	        	 TipShow("请输入正确手机号",1000);
	           phoneflag=false;
	           ifPay();
	         }
	     });
}

// 充值提交
function changeSubmit() {
	tel=tel.replace(new RegExp(" ","gm"),"");
    $.ajax({
		type : 'post',
		url : 'saveOrder_fuelCard.htm',
        data:{'fuelCardType':fuelCardType,  // 加油卡类型   1:中石化 2:中石油
        	'changeType':changeType,  // 1:固定充值 2:任意充值
        	'cardNo':cardNo,   //卡号
        	'prodID':prodID,   //充值套餐种类
        	'cardNum':cardNum,  // 金额
        	'tel':tel,
        	'name':name},
		dataType : 'json',
		timeout : 10000000, 
        beforeSend: function () {
            loading("start");
        },
        complete: function (XMLHttpRequest) {
            loading("stop");
        },
		success : function(data, textStatus) {
          	if(-1==data.timeout){
          		// session已经过期了
          		alert("ajax-timeout");
          		return;
          	}
			if(data.code==1){
				 window.location="gotoPay.htm?ORDER_NO="+data.resultObject.order_no;
			}else if(data.code==2){
				//xiqueshu<77
				//$(".alertTips2").css("display","block");
			}else{
				//
				alerw(data.msg);
			}
		},
        //调用出错执行的函数
        error: function(XMLHttpRequest, textStatus, errorThrown){
              //请求出错处理
            alert("系统异常!"+textStatus);
        } 
	});
}

function changelist(){
	window.location="changelist.htm"
}