var phoneType=0;
$(function () {
	 historyTel();
	 $("#yidong").show();
     $("#liantong").hide();
     $("#dianxin").hide();
 
})

 function historyTel(){
	   $.ajax({
    	   url:getRootPath() + "/giftpay/telRecharge/historyNumber.htm",
    	   dataType: "json",
           type: "post",
   	       success:function(data){
   	    	$(".history").html("");
   	    	 if("0"!=data.length){
      			 $.each(data,function(i,item){
      				 $(".history").append("<li>" + "<span onclick='selectHistoryNumber(this)'>" +item.number+"</span>" +"<label>"+item.number_desc+"</label>"
      						 +"<a class='delete' onclick='deleteHistory(this)' ></a>");
      			 });
   	    	 }else{
   	    		 $(".history").hide();
   	    	 }
   	    	 
   		}
       });
   }
function deleteHistory(obj){
	$.ajax({
 	   url:getRootPath() + "/giftpay/telRecharge/del.htm",
 	   data:{"number":$(obj).parent().find("span").html()},
 	   dataType: "json",
       type: "post",
	   success:function(data){
		  if("1"==data.code){
			  historyTel();
		  }
		}
    });
}
function delAll(){
	$.ajax({
  	   url:getRootPath() + "/giftpay/telRecharge/delAll.htm",
  	   dataType: "json",
        type: "post",
 	   success:function(data){
 		  if("1"==data.code){
 			  historyTel();
 			 $('.historyNumber').hide();
 		  }
 		}
     });
}

function getphoneStatus(tel){
    //淘宝接口
    telnospace=tel.replace(new RegExp(" ","gm"),"");

    $.ajax({
        type: "get",
        url: 'http://tcc.taobao.com/cc/json/mobile_tel_segment.htm?tel='+telnospace,
        dataType: "jsonp",
        jsonp: "callback",
        //调用成功
        success: function(data){
            console.log(data);
            var    province = data.province; //城市地址  例如：江苏
            var    operators = data.catName; //运营商  例如：中国移动
            var    num = data.carrier; //  例如： 江苏移动
            phoneflag=false;
            phoneType=0;
            if(!num){
                // 号码不对
               num = "请输入正确手机号";
                phoneflag=false;
                phoneType=0;
            }else{
                phoneflag=true;
                //判断运营商
                if(num.indexOf("移动")>0){
                    phoneType=1; //移动
                }else if(num.indexOf("电信")>0){
                    phoneType=3; //电信
                }else{
                    phoneType=2; //联通
                }

                console.log("当前运营商："+phoneType);
                siwtchGoodList(phoneType);
                //页面显示当前运营商
                $('#companyName').html("<div class=\"serviceCompany\">"+num+"</div>");

            }
        },
        //调用接口失败
        error:function (){
            TipShow("请输入正确手机号",1000);
            phoneflag=false;
            phoneType=0;
        }

    });

}
function initPhoneGoods(subType) {
    loading("start");
    queryGoodsList("2",subType,"",function(res){
        loading("stop");
        if(res.code=='0'){
            alerw(res.mess);
        }else{
            showPhoneGoodsList(res.mess);
        }
    });
}
function showPhoneGoodsList(mess){
    var data=JSON.parse(mess);
    if(data.length>0){
        var str='<div class="cellPhoneNumberPrice" id="yidong"><ul class="flex">';
        var lt='<div class="cellPhoneNumberPrice" id="liantong"><ul class="flex">';
        var dx='<div class="cellPhoneNumberPrice" id="dianxin"><ul class="flex">';
        str=makePhoneGoodsList(data,str,"1");
        lt=makePhoneGoodsList(data,lt,"2");
        dx=makePhoneGoodsList(data,dx,"3");
        $(".recharge_Content_goods").append(str+lt+dx);
    }else{
        alerw("加载失败");
    }
}
function makePhoneGoodsList(data,str,type){
    var lineNum=data[0].lineNum*1;
    var i=0;
    $.each(data,function(index,item){
        if(item.goodsSubType==type){
            if(i==lineNum){
                i=0;
                str+='</ul><ul class="flex">';
            }
            str+='<li class="flex-1 jmt_center phone" onclick="selectAmount(this,'+item.id+',\''+item.realPrice+'\')">';
            str+='<div>'+item.goodsPrice+'元</div>';
            str+='<div>售价：'+item.realPrice+'元</div>';
            str+='<img class="ico1" src="img/yellow_arrow.png" alt="">';
            str+='<img class="ico2" src="img/gif.png" alt="">';
            str+='<img class="ico3" src="img/gif1.png" alt="">';
            str+='</li>';
            i++;
        }
    })
    str+='</ul></div>';
    return str;
}
function initRedpkgList(){
    queryUserRedPkgList(function(data){
        $("#red").hide();
        return ;
        if (data == "0") {
            $("#red").hide();
            TipShow("请重新登录", 1000);
        } else if (data == "" || data.data == "" || data.data == null) {
            $("#red").hide();
        } else {
            $("#ticket").html("");
            $("#ticket").parent().find("span").html("");
            $("#ticket").append("<option value='0' data='0'>不使用抵用券</option>");
            $.each(data.data, function (i, item) {
                $("#ticket").append(" <option value='" + item.redpkgId + "' data='" + item.value + "'>" + item.redpkgName + "</option>");
            });
        }
    })
}

function siwtchGoodList(type) {
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

