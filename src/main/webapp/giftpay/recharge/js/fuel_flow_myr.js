
//流量套餐选择
function selectAmount(T,rechargeM_get,goodsId_get,price ) {
    //获取当前 流量套餐
    goodsId=goodsId_get;
    rechargeM =rechargeM_get;
    var t =T;

    //获取当前 套餐价格
    dealMoney=price;

    tel=$('#number').val().replace(/\s+/g, '');

    //置灰其他所有li下的样式
    $('.cellPhoneNumberPrice').find('li').attr('class','flex-1 jmt_center phone');

    //添加样式 高亮选中 div[phone_on 高亮,phone 置灰]
    $(t).attr('class','flex-1 jmt_center phone_on');

    payMoney();
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

                console.log("当前运营商："+phoneType)
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

//实时监听input输入框value的改变【判断用户输入手机号码的归属运营商】
function OnInput (event) {



    $('#companyName').html("<div class=\"serviceCompany\"> </div>");

    var tel=event.target.value.replace(/\s+/g, '');
    if(tel&&(/^1[34578]\d{9}$/.test(tel))&&rechargeM){
        $('#PayShow').show();
        $('#PayHide').hide();
    }
    else{
        $('#PayShow').hide();
        $('#PayHide').show();
    }
    if(tel.length==11&&!(/^1[34578]\d{9}$/.test(tel))){
        $('#companyName').html("<div class=\"serviceCompanyNo\">无法识别运营商，请稍后重试</div>");
    }
    else if((tel.length==11&&(/^1[34578]\d{9}$/.test(tel)))){
        getphoneStatus(tel); //淘宝接口 查询手机号码归属地

        $('.historyNumber').hide();
    }

}

//鼠标点击input 触发事件
function handle() {

    //创建多个var对象并且赋值
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

//根据type 判断显示不同的运营商
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

    //当用户输入不同手机号之后，切换运营商流量套餐时候，默认置灰全部li
    $('.cellPhoneNumberPrice').find('li').attr('class','flex-1 jmt_center phone');
    rechargeM="";
}

//点击事件，将之前充值的号码显示到input输入框
function selectHistoryNumber(self) {

    //根据当前对象 ，获取该对象子类下的span标签中的test文本信息
    var number = $(self).text();
    $('#number').val(number);

    //执行方法 将手机号码 按照一定格式显示
    handle();

    //执行接口 查询当前手机号归属地
    getphoneStatus(number);

    //用户重新点击时，重新赋值当前流量套餐为null
    rechargeM="";
    //充值 是否允许充值操作[根据number手机号和rechargeAmount充值套餐]
    payMoney();

    //点击之后 隐藏div
    $('.historyNumber').hide();

}

//显示历史记录
$(function () {
    // 当number获得焦点的时候
    $("#number").focus(function(){
        //在显示历史记录时候 清空number的input框中的values
        $("#number").val("");
        //判断 number是否为空，如果为空，充值样式为'no'
        payMoney();

        console.log("HistoryCount:"+HistoryCount);
        //如果历史充值记录等于0，不显示历史记录充值框
        if(HistoryCount=="1"){

            if($(".historyNumber").is(":hidden")){
                //显示历史记录
                $('.historyNumber').show();  //如果元素为隐藏,则将它显现
                console.log("如果元素为隐藏,则将它显现");
            }else{
                $(".historyNumber").hide();     //如果元素为显现,则将其隐藏
                console.log("如果元素为显现,则将其隐藏");
            }


        }else{
            console.log("不显示数据");
        }

    });



})

//充值button 置灰和显示
function payMoney() {
    var number = $("#number").val();
    //判断手机号是否满足13位数
    if(number.length==13){
        var IsNum =   isNaN(parseInt(number.toString().replace(" ","").replace(" ","")));
        //判断当前是否都为数字
        if(IsNum==false){
            //为tel全局变量赋值
            tel=parseInt(number.toString().replace(" ","").replace(" ",""));
            //当前number的输入框和select的流量套餐都不能为空
            if(rechargeM!=""){
                $("#payMoney").removeClass().addClass("toPay");
            }
        }else{
            console.log("非数字");
        }
    }else{
        $("#payMoney").removeClass().addClass("toPay no");
    }
}

//跳转 充值记录 查询页面
function ToNumRecord(){
    window.location.href="rechargeRecords.jsp?type=2";
}

//兑换 抵用卷button
function changeRedPack(self){
    var redPkg = $(self).children('option:selected').text();
    $(self).parent().find("span").html(redPkg);

    //重新赋值
    var redSub = $(self).children('option:selected').val();

    redId = redSub.substring(0,redSub.indexOf("-")) ; //红包redId
    redValue = redSub.substring(redSub.indexOf("-")+1) ;  // 红包价格redPrice
    // console.log("红包id"+redId+"-红包value"+redValue);
}

// ---------------------------------------------------------------------------------

//初始化加载查询数据
function initPhoneGoods(subType) {
    // console.log("初始化加载页面数据 1");
    //loading("start");
    queryGoodsList("3",subType,"",function(res){
        //loading("stop");
        if(res.code=='0'){
            alerw(res.mess);
        }else{
            console.log("展示商品数据");
            showPhoneGoodsList(res.mess);
        }
    });
}

//展示商品列表
function showPhoneGoodsList(mess){
    // console.log("将数据显示到页面 2")
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

//输出展示到页面
function makePhoneGoodsList(data,str,type){

    // console.log("展示数据到页面上 3");

    var lineNum=data[0].lineNum*1;
    var i=0;
    $.each(data,function(index,item){
        if(item.goodsSubType==type){
            if(i==lineNum){
                i=0;
                str+='</ul><ul class="flex">';
            }
            str+='<li class="flex-1 jmt_center phone" onclick="selectAmount(this,'+item.goodsPrice+','+item.id+',\''+item.realPrice+'\')">';
            str+='<div>'+item.goodsPrice+'M</div>';
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



