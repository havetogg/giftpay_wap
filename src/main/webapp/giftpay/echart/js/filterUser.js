$(function () {
    checkUserIsLogin();
})

function checkUserIsLogin() {
    $.ajax({
        type: "GET",
        url: getRootPath() + "/count/isLogin.htm",
        dataType: "json",
        success: function (data) {
            if (data.code == '0') {
                alert("请登录后查看！");
                location.href = 'index.html';
                return;
            }
        },
        error: function (res) {
            console.log(res)
        }
    });
}

function initClosePage(){
    if
    (confirm("您确定要关闭本页吗？")){
        window.location.href="about:blank";
        window.close();
    }
    else{
        return false;
    }
}
function initLogon(){

    if
    (confirm("您确定要注销吗？")){
        $.ajax({
            type: "GET",
            url: getRootPath() + "/count/logOn.htm",
            dataType: "json",
            success: function (data) {
                if(data.code=='0'){
                    alert("注销失败");
                    return ;
                }else if(data.code=='1'){
                    alert("注销成功");
                    location.href = 'index.html';
                }
            },
            error: function (res) {
                console.log(res)
            }
        });
    }
    else{
        return false;
    }
}

