/**
 * Created by Administrator on 2017/6/14.
 */


function initCountInfo() {
    $.ajax({
        type: "GET",
        url: getRootPath() + "/giftGO/searchDataAll.htm",
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
                str+=' <td>'+item.channel_code+'</td>';
                str+='<td>'+item.click_num+'</td>';
                str+=' <td>'+(item.exist)+'</td>';
            })
            $("#example2 tbody").html(str);
            $('#example2').DataTable();
        },
        error: function (res) {
            console.log(res)
        }
    });
}
function initThirdTables() {
    $.ajax({
        type: "GET",
        url: getRootPath() + "/giftGO/searchThirdUserList.htm",
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
                str+='<td>'+item.tId+'</td>';
                str+=' <td>'+item.phone+'</td>';
                str+='<td class="center">'+item.name+'</td>';
                str+=' <td class="center">'+item.thirdName+'</td></tr>';
            })
            $("#example tbody").html(str);
            $('#example').DataTable();

        },
        error: function (res) {
            console.log(res)
        }
    });
}

$(function () {
    initCountInfo();
    initThirdTables();
    initAllCount();
})
function initAllCount(){
    $.ajax({
        type: "GET",
        url: getRootPath() + "/giftGO/searchChannelCount.htm",
        dataType: "json",
        success: function (data) {
           $("#allCount").text(data);
        },
        error: function (res) {
            console.log(res)
        }
    });
}

