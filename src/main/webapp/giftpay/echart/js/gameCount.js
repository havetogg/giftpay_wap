/**
 * Created by Administrator on 2017/8/3.
 */
$(function(){
    initUserPriceInfo();
})

function initUserPriceInfo(){
    $.ajax({
        type: "GET",
        url: getRootPath() + "/count/countGameUserPrice.htm",
        dataType: "json",
        success: function (data) {
           console.log(data);
            var str="";
            $.each(data,function(index,item){
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
        },
        error: function (res) {
            console.log(res)
        }
    });
}