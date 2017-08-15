/**
 * Created by Administrator on 2017/8/14.
 */
function initAllInfo() {
    $.ajax({
        type: "GET",
        url: getRootPath() + "/count/carCodeStatis.htm",
        dataType: "json",
        success: function (data) {
            console.log(data);
            var all=JSON.parse(data.all);
            var bind=JSON.parse(data.bind);
            $("#totalCarNum").text(all.total);
            $("#todayCarNum").text(bind.total);
            $("#totalAddNum").text(all.today);
            $("#todayAddNum").text(bind.today);
        },
        error: function (res) {
            console.log(res)
        }
    });
}

$(function () {
    initAllInfo();
})