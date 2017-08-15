/**
 * Created by Administrator on 2017/8/3.
 */
$(function(){
    initUserPriceInfo();
    initAdvertiseList();
})

function initAdvertiseList(){
    $.ajax({
        type: "GET",
        url: getRootPath() + "/count/oilUserStatistics.htm",
        dataType: "json",
        success: function (data) {
            $("#allUserNum").text(data.userSum);
            $("#allOilNum").text(data.oilSum);
            $("#allPayNum").text(data.paySum);
        }
    });
}

function initUserPriceInfo(){
    $.ajax({
        type: "GET",
        url: getRootPath() + "/count/countGameUserPrice.htm",
        dataType: "json",
        success: function (data) {
           console.log(data);
            var str="";
            $.each(data.userList,function(index,item){
                if(index%2==0){
                    str+='<tr class="odd gradeX">';
                }else{
                    str+='<tr class="even gradeC">';
                }
                str+=' <td>'+item.create_time+'</td>';
                str+='<td>'+item.open_id+'</td>';
                str+='<td class="center">'+item.mobile+'</td>';
                str+='<td class="center">'+item.used_pool+'</td>';
                str+='<td class="center">'+item.prize_pool+'</td></tr>';
            })
            $("#example tbody").html(str);
            $('#example').DataTable();
            var today=data.today;
            var total=data.total;
            $("#totalUser").text(total.total);
            $("#totalPlay").text(total.total_diamond);
            $("#totalRegister").text(total.total_mobile);
            $("#totalPrize").text(total.total_prize);

            $("#todayUser").text(today.today_total);
            $("#todayPlay").text(today.today_diamond);
            $("#todayRegister").text(today.today_mobile);
            $("#todayPrize").text(today.today_prize);
        },
        error: function (res) {
            console.log(res)
        }
    });
}