/**
 * Created by Administrator on 2016/11/25.
 */
var rechargeAmount='';
var tel='';
var phoneType=0;
var phoneflag=false;
var prodID='';           //充值套餐种类
function selectAmount(self,price,pprodID) {
    rechargeAmount=price;
    prodID=pprodID;
    $('.cellPhoneNumberPrice').find('li').attr('class','flex-1 jmt_center phone');
    $(self).attr('class','flex-1 jmt_center phone_on');
    ifPay();
}
function rechargeNow() {
	tel=$('#number').val();
    if(!tel){
        TipShow("请输入手机号",1000);
    }
    else if(13!=tel.length){
        TipShow("请输入正确的手机号",1000);
    }
    else if(!phoneflag){
        TipShow("请输入正确的手机号",1000);
    }else if(!rechargeAmount){
        TipShow("请选择充值金额",1000);
    }else{
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

function OnInput (event) {
    var tel=event.target.value;
    // 13位时调用接口
    if(tel.length==13){
    	getphoneStatus(tel);
    }else{
    	// 小于13位时,不能支付
    	$("#phoneaddress").html("&nbsp;&nbsp;");
    	phoneflag=false;
    	phoneType=0;
    	siwtchGoodList(phoneType);
    	ifPay();
    }
}

function ifPay() {
    //选择充值金额
    if(rechargeAmount&&phoneflag){
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

function handle() {
    var inputVal = $('#number').val(),
        trimPhoneNum = inputVal.replace(/\s+/g, ''),
        len = trimPhoneNum.length;
    if (len < 4) {
        var value1 = trimPhoneNum.replace(/\s+/g, '');
        $('#number').val(value1);
    } else if (len >= 4 && len < 8) {
        $('#number').val(trimPhoneNum.replace(/(\d{3})(\d+)/g, '$1 $2'));
    } else if (len >= 8 && len < 11) {
        $('#number').val(trimPhoneNum.replace(/(\d{3})(\d{4})(\d+)/g, '$1 $2 $3'));
    } else if (len == 11) {
        $('#number').val(trimPhoneNum.replace(/(\d{3})(\d{4})(\d{4})/g, '$1 $2 $3'));
    } else if (trimPhoneNum.length > 11) {
        return false;
    }
}

//进行手机号归属地的查询
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
	        	phoneType=0;
	            if(!num){
	            	// 号码不对
	            	TipShow("请输入正确手机号",1000);
	            	phoneflag=false;
	            	phoneType=0;
	            }else{
	            	$("#phoneaddress").html(num);
	            	phoneflag=true;
	            	if(num.indexOf("移动")>0){
	                	phoneType=1;
	            	}else if(num.indexOf("电信")>0){
	            		phoneType=3;
	            	}else{
	            		phoneType=2;
	            	}
	            }
            	siwtchGoodList(phoneType);
	            ifPay();
	         },
	         error:function (){   
	        	 TipShow("请输入正确手机号",1000);
	           phoneflag=false;
	           phoneType=0;
	           siwtchGoodList(phoneType);
	           ifPay();
	         }
	     });
}

//充值提交
function changeSubmit() {
	tel=tel.replace(new RegExp(" ","gm"),"");
    $.ajax({
		type : 'post',
		url : 'saveOrder_flow.htm',
        data:{'prodID':prodID,   //充值套餐种类
        	'tel':tel,
        	'phoneType':phoneType},
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

function siwtchGoodList(type) {
	console.log('type：'+type); 
	 rechargeAmount='';
	 prodID='';   
    $('.cellPhoneNumberPrice').find('li').attr('class','flex-1 jmt_center phone');
	
    if(type==0){
        $("#yidong").show();
        $("#liantong").hide();
        $("#dianxin").hide();
    }
    else if(type==1){
        $("#yidong").show();
        $("#liantong").hide();
        $("#dianxin").hide();
    }
    else if(type==2){
        $("#yidong").hide();
        $("#liantong").show();
        $("#dianxin").hide();
    }
    else if(type==3){
        $("#yidong").hide();
        $("#liantong").hide();
        $("#dianxin").show();
    }
}

function changelist(){
	window.location="changelist.htm"
}
