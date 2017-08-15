/**
 * Created by Administrator on 2017/6/14.
 */


function initCountInfo() {
    $.ajax({
        type: "GET",
        url: getRootPath() + "/count/countAllUserData.htm",
        dataType: "json",
        success: function (data) {
            initAllData(data);
        },
        error: function (res) {
            console.log(res)
        }
    });
}
function initPayPageInfo() {
    $.ajax({
        type: "GET",
        url: getRootPath() + "/count/paySuccessStatistics.htm",
        dataType: "json",
        success: function (data) {
            console.log("paySuccessStatistics"+data);
            $("#payUser").text();
            $("#payRegisterUser").text();
        },
        error: function (res) {
            console.log(res)
        }
    });
}
function initAllData(data) {
    $("#todayForm .add").text(data.dataB);//今日新增
    $("#totalForm .add").text(data.dataA);//累计新增
    $("#todayForm .register").text(data.dataC);//今日新增登记
    $("#totalForm .register").text(data.dataD);//累计登记
    $("#todayForm .zsh").text(data.dataE);//中石化今日登记
    $("#totalForm .zsh").text(data.dataF);//中石化累计登记
    $(".allusercount").html((data.dataG * 1 + data.dataD * 1));//总用户数
}
function thirdUserInfo() {
    //第三方用户统计
    $.ajax({
        type: "GET",
        url: getRootPath() + "/giftpay/third/thirdCount.htm",
        dataType: "json",
        success: function (data) {
            var allxy = JSON.parse(data.xingye).length;//其他渠道累计登记
            $("#totalForm .other").text(allxy);
            var allfl = JSON.parse(data.fuli).length;//福利中心累计登记
            $("#totalForm .fuli").text(allfl);
            var todayxy = JSON.parse(data.xingyeToday).length;//其他渠道今日登记
            $("#todayForm .other").text(todayxy);
            var todayfl = JSON.parse(data.fuliToday).length;//福利中心今日登记
            $("#todayForm .fuli").text(todayfl);
        }
    });
}

function initDataGridInfo() {
    $.ajax({
        type: "GET",
        url: getRootPath() + "/count/countAllUserBaseInfo.htm",
        dataType: "json",
        success: function (data) {
            console.log(data);
            var str='';
            $.each(data,function(index,item){
                if(index%2==0){
                    str+='<tr class="odd gradeX">';
                }else{
                    str+='<tr class="even gradeC">';
                }
                str+=' <td>'+item.createTime.substring(0,item.createTime.lastIndexOf("."))+'</td>';
                str+='<td>'+item.id+'</td>';
                str+=' <td>'+item.phone+'</td>';
                str+='<td>'+item.city+'</td>';
                str+='<td class="center">'+item.balanceNumber+'</td>';
                str+=' <td class="center"></td></tr>';
            })
            $("#example tbody").html(str);
            $('#example').DataTable();
        }
    });
}
function initPaySuccessStatistics(){
    $.ajax({
        type: "GET",
        url: getRootPath() + "/count/paySuccessStatistics.htm",
        dataType: "json",
        success: function (data) {
            console.log("paySuccessStatistics:"+data);
            var authInfo=data.authInfo;
            var registerInfo=data.registerInfo;
            authInfo=JSON.parse(authInfo);
            registerInfo=JSON.parse(registerInfo);
            $(".payUser").text(authInfo.total);
            $(".payRegisterUser").text(registerInfo.total);
            $(".todaypayUser").text(authInfo.today);
            $(".todaypayRegisterUser").text(registerInfo.today);
        }
    });
}

function initAdvertiseList(){
    $.ajax({
        type: "GET",
        url: getRootPath() + "/giftpay/commonSetting/queryAdvertiseList.htm",
        dataType: "json",
        success: function (data) {
            console.log(data);
            $(".advertise1").text(data[0].clickNum);
           /* $(".advertise2").text(data[1].clickNum);
            $(".advertise3").text(data[2].clickNum);*/
        }
    });
}
function initEchart(){
	$.ajax({
        type: "GET",
        url: getRootPath() + "/count/countEchartData.htm",
        dataType: "json",
        success: function (data) {
            console.log(data);
            var fuliUser=data.fuliUser;
            var newUser=data.newUser;
            var otherUser=data.otherUser;
            var registerUser=data.registerUser;
            var zshUser=data.zshUser;
            
            var aa=[];
            var bb=[];
            var cc=[];
            var dd=[];
            var ee=[];
            var timearr=[];
            $.each(fuliUser,function(index,item){
            	aa.push([index,item.num*1]);
                timearr.push([index,(item.addTime).substring((item.addTime).indexOf("-")+1,(item.addTime).length)]);
            })
            $.each(newUser,function(index,item){
            	bb.push([index,item.num*1]);
            })
            $.each(otherUser,function(index,item){
            	cc.push([index,item.num*1]);
            })
            $.each(registerUser,function(index,item){
            	dd.push([index,item.num*1]);
            })
            $.each(zshUser,function(index,item){
            	ee.push([index,item.num*1]);
            })
            var plot =$.plot($("#flot-demo"),
            [
                // {data: bb, label: "新增用户"},
            {data: dd, label: "登记用户"},
            {data: ee, label: "中石化登记用户"},
            {data: aa, label: "福利中心登记用户"},
            {data: cc, label: "渠道登记用户"}], {
                    xaxis: {
                        ticks:timearr
                    },
                    series: {
                        lines: { show: true},
                        points: { show: true }
                    }
                }
            );
        }
    });
}
$(function () {
    initCountInfo();
    initPayPageInfo();
    thirdUserInfo();
    initDataGridInfo();
    initEchart();
    initAdvertiseList();
    initPaySuccessStatistics();
    $('#frequency1').bind('input propertychange', function() {
        if($('#frequency1').val()>100){
            $('#frequency1').val('');
        }
        else{
            $('#frequency2').val(100-$('#frequency1').val());
        }
    });
    $('#frequency2').bind('input propertychange', function() {
        if($('#frequency2').val()>100){
            $('#frequency2').val('');
        }
        else{
            $('#frequency1').val(100-$('#frequency2').val());
        }
    });
})
function showAd() {
    $('#ad_tip').css('display','flex');
}function closeAd(id) {
    $('#'+id).hide();
}
function onlyNum() {
    if(!(event.keyCode==46)&&!(event.keyCode==8)&&!(event.keyCode==37)&&!(event.keyCode==39))
        if(!((event.keyCode>=48&&event.keyCode<=57)||(event.keyCode>=96&&event.keyCode<=105)))
            event.returnValue=false;
}
var ifShow=false;
function add_adBtn() {
    ifShow=!ifShow;
    if(ifShow){
        $('.uls2').show();
        $('.add_ad2_btn').html('隐藏');
    }else{
        $('.uls2').hide();
        $('.add_ad2_btn').html('增加广告位');
    }

}
function open_dialog1(){
    document.getElementById("img1").click();
}
function open_dialog2(){
    document.getElementById("img2").click();
}
function showFilePath()
{
    alert(document.getElementById("img1").value);
}
function showGoods() {
    $('#goods_tip').css('display','flex');
}