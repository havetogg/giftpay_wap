/**
 * Created by Administrator on 2017/6/14.
 */


function initTotalInfo() {
    $.ajax({
        type: "GET",
        url: getRootPath() + "/count/countOilRecordTotalInfo.htm",
        dataType: "json",
        success: function (data) {
            initTotal(data.total);
        },
        error: function (res) {
            console.log(res)
        }
    });
}

function initTodayInfo() {
    $.ajax({
        type: "GET",
        url: getRootPath() + "/count/countOilRecordTodayInfo.htm",
        dataType: "json",
        success: function (data) {
            initToday(data.today);
        },
        error: function (res) {
            console.log(res)
        }
    });
}

function initMonthInfo() {
    $.ajax({
        type: "GET",
        url: getRootPath() + "/count/countOilRecordMonthInfo.htm",
        dataType: "json",
        success: function (data) {
            initMonth(data.month);
        },
        error: function (res) {
            console.log(res)
        }
    });
}

function initLineInfo() {
    $.ajax({
        type: "GET",
        url: getRootPath() + "/count/countOilRecordLineInfo.htm",
        dataType: "json",
        success: function (data) {
            initLine(data.line);
        },
        error: function (res) {
            console.log(res)
        }
    });
}

function initRecordInfo() {
    $.ajax({
        type: "GET",
        url: getRootPath() + "/count/countOilRecordRecordInfo.htm",
        dataType: "json",
        success: function (data) {
            initTable(data.oilRecord);
        },
        error: function (res) {
            console.log(res)
        }
    });
}

function initTotal(data) {
    $("#totalNum").text(data.totalNum);
    $("#totalPay").text(data.payAmount);
    $("#totalAmount").text(data.totalAmount);
    $("#totalReturn").text(data.totalAmountReturn);
}
function initToday(data) {
    $("#todayNum").text(data.todayNum);
    $("#todayPay").text(data.todayPay);
    $("#todayReturn").text(data.todayAmount);
}
function initMonth(data) {
    $("#monthNum").text(data.monthNum);
    $("#monthPay").text(data.monthPay);
    $("#monthReturn").text(data.monthReturn);
}
function initLine(data) {
    var timearr = data.dates;
    var timearray=[];
    for(var i=0;i<timearr.length;i++){

        timearray.push([i,timearr[i].substring(5,10)]);
    }
    console.log(timearr);
    var aa=[];
    for(var i=0;i<data.lineNum.length;i++){
        aa.push([i,data.lineNum[i]*1]);
    }
    console.log(aa)
    var plot =$.plot($("#flot-demo"),
        [{data: aa, label: "易加油服务购买"}], {
            xaxis: {
                ticks:timearray
            },
            series: {
                lines: { show: true},
                points: { show: true }
            }
        }
    );
}
function initTable(data){
    var str='';
    $.each(data,function(index,item){
        if(index%2==0){
            str+='<tr class="odd gradeX">';
        }else{
            str+='<tr class="even gradeC">';
        }
        str+=' <td>'+item.openId+'</td>';
        str+='<td>'+item.phone+'('+item.attribution+')'+'</td>';
        str+='<td class="center">'+item.num+'</td>';
        str+='<td class="center">'+item.createTime+'</td>';
        str+='<td class="center">'+item.lastTime+'</td>';
        str+='<td class="center">'+item.pay+'</td>';
        str+='<td class="center">'+item.amount+'</td>';
        str+='<td class="center">'+item.return+'</td></tr>';
    })
    $("#example tbody").html(str);
    $('#example').DataTable();
}


$(function () {
    initTotalInfo();
    initTodayInfo();
    initMonthInfo();
    initLineInfo();
    initRecordInfo();
})

