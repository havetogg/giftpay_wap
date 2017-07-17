/**
 * 油卡充值方法js
 */

var historyNum = true;
var goodsID = 0;//商品编号
var cardNo = '';//油卡号码
var cardNoflag = false;//油卡号码校验
var cardUserName = '未知';//油卡用户名称
var redPkgValue = '';//红包的名称
var redpkgId = '';//红包id
var company = '';
var chargeType = 0;
var fuelCardType = 0;
var rechargeAmount ;

$(function () {
    if($("#cardNo").val().trim()==""){
        /*queryRedPkgInfo();//查询红包*/
        initPhoneGoods("", "0");
    }else{
        $('.historyNumber2').hide();
        cardNo = $("#cardNo").val().trim();//卡号
        $(".phone_on").attr("class","flex-1 jmt_center phone");//移除选择的框
        // 值了判断是不是正确的金额
        if (cardNo) {
            // 有值
            if (!/^\d+$/.test(cardNo)) {
                cardNoflag = false;
                TipShow("请输入正确的卡号!", 1000);
            } else {
                if (0 == cardNo.indexOf("100011")) {
                    // 中石化
                    if (19 == cardNo.length) {
                        queryFuelCardInfo(cardNo, '1');
                        cardNoflag = true;
                        company = "中国石化   ";
                        $("#companyName").html(company+"   " + cardUserName);
                        initPhoneGoods("", "1");
                    } else {
                        cardNoflag = false;
                        $("#companyName").html("未知");
                    }
                    fuelCardType = 1;
                } else if (0 == cardNo.indexOf("9")) {
                    // 中石石油卡
                    if (16 == cardNo.length) {
                        queryFuelCardInfo(cardNo, '2');
                        cardNoflag = true;
                        company = "中国石油   ";
                        $("#companyName").html(company+"   " + cardUserName);
                        initPhoneGoods("", "2");
                    } else {
                        cardNoflag = false;
                        $("#companyName").html("未知");
                    }

                    fuelCardType = 2;
                } else {
                    cardNoflag = false;
                    $("#companyName").html("未知");
                    fuelCardType = -1;

                }
            }
        } else {
            //没有值
            if (historyNum) {
                $('.historyNumber2').show();
            }
            cardNoflag = false;
            $("#companyName").html("未知");
            fuelCardType = -1;
        }
        ifPay();
    }

    queryFuelCardRecord();//查询用户成功历史充值记录
    $("#cardNo").focus(function () {
        //点击展开充值记录面板
        if (historyNum) {
            $('.historyNumber2').show();
        }
    });
    $("#oilBanner").on("click", function(){
        toNineOil()
    });

})

function toNineOil() {
    $.ajax({
        url: getRootPath() + "/giftpay/wap/queryUserInfo.htm",
        dataType: "json",
        type: "post",
        success: function (data) {
            console.log(data);
            if (data.success) {
                location.href = getRootPath() + "/rechargeoil/index.html?userId=" + data.userId + "&openId=" + data.openId;
            } else {
                alert("获取用户信息超时，请重新登录");
                location.href = getRootPath() + "/giftpay/wap/wxLogin.htm";
            }
        }, error: function (res) {
        }
    });
}
//初始化商品数据
function initPhoneGoods(subType, type) {
    loading("start");
    queryGoodsList("1", subType, "", function (res) {
        loading("stop");
        if (res.code == '0') {
            alerw(res.mess);
        } else {
            oilData = res.mess;
            showPhoneGoodsList(res.mess, type);
        }
    });
}
//初始化商品数据
function showPhoneGoodsList(mess, subtype) {
    var data = JSON.parse(mess);
    if (data.length > 0) {
        var str = '<ul class="flex">';
        str = makePhoneGoodsList(data, str, subtype);
        $(".cellPhoneNumberPrice").html(str);
    } else {
        alerw("加载失败");
    }
}
//初始化商品数据
function makePhoneGoodsList(data, str, type) {
    var lineNum = data[0].lineNum * 1;
    var i = 0;
    $.each(data, function (index, item) {
        if (item.goodsSubType == type) {
            if (i == lineNum) {
                i = 0;
                str += '</ul><ul class="flex">';
            }
            str += "<li class=\"flex-1 jmt_center phone\" onclick=\"selectAmount(this," + item.id + ",'" + item.realPrice + "')\">";
            str += '<div>' + item.goodsPrice + '元</div>';
            str += '<div>售价：' + item.realPrice + '元</div>';
            str += '<img class="ico1" src="img/yellow_arrow.png" alt="">';
            str += '<img class="ico2" src="img/gif.png" alt="">';
            str += '<img class="ico3" src="img/gif1.png" alt="">';
            str += ' </li>';
            i++;
        }
    })
    str += '</ul></div>';
    return str;
}

//在卡号记录中选择卡号
function selectHistoryNumber(self) {
    $('#cardNo').val($(self).find('span').html());
    cardUserName = $(self).find('label').html()
    if ($('#cardNo').val().length == 19) {
        company = '中国石化';
        chargeType = "1";
        initPhoneGoods("", chargeType);
    } else {
        company = '中国石油';
        chargeType = "2";
        initPhoneGoods("", chargeType);
    }
    $("#companyName").html(company + "  " + cardUserName);
    cardNoflag = true;
    $('.historyNumber2').hide();
    $(".phone").remove('phone_on');
    showCardType($('#cardNo').val());
    ifPay();
}


//选择商品
function selectAmount(self, goodsid,pri) {
    goodsID = goodsid;//商品编号]
    rechargeAmount = pri;
    $(".phone_on").attr("class","flex-1 jmt_center phone");//移除选择的框
    $(self).attr("class","flex-1 jmt_center phone_on");//添加选择的框
    ifPay();
}

//确认支付
function rechargeNow() {
    if (goodsID==0) {
        TipShow("请选择充值金额", 1000);
    }
    else if (!cardNoflag) {
        TipShow("请输入充值油卡", 1000);
    }
    else {
        // 确认提交
        confirw("确认提交充值", "", "", "",
            function () {
                // OK
                changeSubmit();
            }, function () {
                // NG
            });
    }
}

//输入油卡
function cardInput(event) {
    $('.historyNumber2').hide();
    cardNo = event.target.value;//卡号
    $(".phone_on").attr("class","flex-1 jmt_center phone");//移除选择的框
    // 值了判断是不是正确的金额
    if (cardNo) {
        // 有值
        if (!/^\d+$/.test(cardNo)) {
            cardNoflag = false;
            TipShow("请输入正确的卡号!", 1000);
        } else {
            if (0 == cardNo.indexOf("100011")) {
                // 中石化
                if (19 == cardNo.length) {
                    queryFuelCardInfo(cardNo, '1');
                    cardNoflag = true;
                    company = "中国石化   ";
                    $("#companyName").html(company+"   " + cardUserName);
                    initPhoneGoods("", "1");
                } else {
                    cardNoflag = false;
                    $("#companyName").html("未知");
                }
                fuelCardType = 1;
            } else if (0 == cardNo.indexOf("9")) {
                // 中石石油卡
                if (16 == cardNo.length) {
                    queryFuelCardInfo(cardNo, '2');
                    cardNoflag = true;
                    company = "中国石油   ";
                    $("#companyName").html(company+"   " + cardUserName);
                    initPhoneGoods("", "2");
                } else {
                    cardNoflag = false;
                    $("#companyName").html("未知");
                }

                fuelCardType = 2;
            } else {
                cardNoflag = false;
                $("#companyName").html("未知");
                fuelCardType = -1;

            }
        }
    } else {
        //没有值
        if (historyNum) {
            $('.historyNumber2').show();
        }
        cardNoflag = false;
        $("#companyName").html("未知");
        fuelCardType = -1;
    }
    ifPay();
}
function showCardType(cardNo) {
    if (cardNo) {
        // 有值
        if (!/^\d+$/.test(cardNo)) {
            cardNoflag = false;
            TipShow("请输入正确的卡号!", 1000);
        } else {
            if (0 == cardNo.indexOf("100011")) {
                // 中石化
                if (19 == cardNo.length) {
                    queryFuelCardInfo(cardNo, '1');
                    cardNoflag = true;
                    company = "中国石化   ";
                    $("#companyName").html("中国石化   " + cardUserName);
                    initPhoneGoods("", "1");
                } else {
                    cardNoflag = false;
                    $("#companyName").html("未知");
                }
                fuelCardType = 1;
            } else if (0 == cardNo.indexOf("9")) {
                // 中石石油卡
                if (16 == cardNo.length) {
                    queryFuelCardInfo(cardNo, '2');
                    cardNoflag = true;
                    company = "中国石油   ";
                    $("#companyName").html("中国石油   " + cardUserName);
                    initPhoneGoods("", "2");
                } else {
                    cardNoflag = false;
                    $("#companyName").html("未知");
                }

                fuelCardType = 2;
            } else {
                cardNoflag = false;
                $("#companyName").html("未知");
                fuelCardType = -1;

            }
        }
    }
}
//显示充值按钮
function ifPay() {
    var oilCardNo = $(".phone_on").length;
    if (cardNoflag &&oilCardNo>0) {
        $('#submitButt').attr('class', 'toPay');
        $('#submitButt').bind("click", function () {
            rechargeNow();
        });
        return true
    }
    else {
        $('#submitButt').attr('class', 'toPay no');
        $('#submitButt').unbind();
    }
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////
//发送提交请求
function changeSubmit() {
    var pgame_userid = $('#cardNo').val();
    var predPkgValue = redPkgValue;
    var predpkgId = redpkgId;

    var oil = new Object();
    oil.redPkgId = predpkgId;
    oil.redPkgValue = predPkgValue;
    oil.game_userid = pgame_userid;
    oil.chargeType = fuelCardType;
    oil.goodsID=goodsID;
    oil.rechargeAmount=rechargeAmount;

    $.ajax({
        type: 'post',
        url: getRootPath() + '/giftpay/FuelCardrecharge/toOrderPayFuelCard.htm',
        data: oil,
        dataType: 'json',
        success: function (data) {
            console.log(data);
            if (data.success == '1') {
                location.href = getRootPath() + "/giftpay/liftpayment/orderPay.html?payamount=" + data.payamount + "&rechargeType=" + data.rechargeType + "&lifeType=" + data.lifeType + "&redPkgId=" + data.redPkgId + "&redPkgValue=" + data.redPkgValue + "&payInfo=" + data.payInfo;
            } else {
                alert(data.errMsg);
            }
        },
        error: function () {
        }
    });
}

//删除全部充值记录
function delAll() {
    $(".historyNumber2 ul").html("");
    $.ajax({
        type: 'post',
        url: getRootPath() + '/giftpay/FuelCardrecharge/delAllRecord.htm',
        dataType: 'json',
        success: function (data) {
        },
        error: function () {
        }
    });
}

//删除指定记录
function delOne(self) {
    var num = $(self).parent().find("div").find("span").html();
    $.ajax({
        type: 'post',
        url: getRootPath() + '/giftpay/FuelCardrecharge/delOneRecord.htm',
        dataType: 'json',
        data: {game_userid: num},
        success: function (data) {
        },
        error: function () {
        }
    });
    $(self).parent().remove();
}

//查询红包
function queryRedPkgInfo() {
    $.ajax({
        type: 'post',
        url: getRootPath() + '/giftpay/FuelCardrecharge/queryRedPkgInfo.htm',
        dataType: 'json',
        success: function (data) {
            if (data != "" && data.data != null) {
                $("#redPkg").html("");

                $.each(data.data, function (i, item) {
                    $("#redPkg").removeAttr("disabled");

                    $("#redPkg").append('<option name="' + item.value + '"value="' + item.redpkgId + '">' + item.redpkgName + '</option>');
                });
            } else {
                $("#redpkgdiv").hide();
            }
        },
        error: function () {

        }
    });
}

//查询充值号码记录
function queryFuelCardRecord() {
    $.ajax({
        url: getRootPath() + "/giftpay/FuelCardrecharge/queryFuelCardRecord.htm",
        dataType: "json",
        type: "post",
        success: function (data) {
            if (data.code == "0") {
                historyNum = false;
                $('.historyNumber2').hide();
            } else {
                $(".historyNumber2 ul").html("");
                $.each(data, function (i, item) {
                    $(".historyNumber2 ul").append("<li ><div onclick='selectHistoryNumber(this)'>" + "<span>" + item.number + "</span>" + "<label>" + item.number_desc + "</label>"
                        + "</div><a class='delete' onclick='delOne(this)'></a>");
                });
            }
        }
    });
}
//获得卡号的用户名称
function queryFuelCardInfo(cardNum, type) {
    $.ajax({
        type: 'post',
        url: getRootPath() + '/giftpay/FuelCardrecharge/queryfuelCardInfo.htm',
        dataType: 'json',
        data: {"game_userid": cardNum, "chargeType": type},
        success: function (data) {
            if (data.err_msg.length == 0) {
                cardUserName = data.username;
                $("#companyName").html(company + "  " + cardUserName);
            } else {
                cardUserName = '未知';
                $("#companyName").html(company + "  " + cardUserName);
            }
        },
        error: function () {
            cardUserName = "未知";
        }
    });
}

//选择红包
function gradeChange(self) {
    var redPkgText = $(self).children('option:selected').text();
    redPkgValue = $(self).children('option:selected').attr("name");
    redpkgId = $(self).children('option:selected').val();
    $(self).parent().find("span").html(redPkgText);

}