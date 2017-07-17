/**
 * Created by Administrator on 2017/5/11.
 */


$(function(){
    wxShare();
})

function wxShare(){
    $.ajax({
        url: getRootPath() + "/giftpay/wap/shareFriend.htm",
        data: {'url': location.href},
        dataType: "json",
        success: function (config) {
            wx.config(config);
            shareData = {
                title:"9.5折加油、缴费充值优惠，一切尽在有礼付",
                desc: "还能获得加油抵扣券哦~",
                link:getRootPath()+'/giftpay/wap/wxLogin.htm',
                imgUrl:getRootPath()+'/giftpay/wap/img/share.png',
                // linkpath + '/share.jsp='+wxId+'&id='+currentId;
                trigger: function (res) {
                    //alert('用户点击发送给朋友');
                },
                success: function (res) {
                    //alert('已分享');
                },
                cancel: function (res) {
                    //alert('已取消');
                },
                fail: function (res) {
                    //alert("this is "+JSON.stringify(res));
                }
            };
            //发送给朋友圈
            shareFriends = {
                title:"9.5折加油、缴费充值优惠，一切尽在有礼付",
                link:getRootPath()+'/giftpay/wap/wxLogin.htm',
                imgUrl:getRootPath()+'/giftpay/wap/img/share.png',
                // linkpath + '/share.jsp='+wxId+'&id='+currentId;
                trigger: function (res) {
                    //alert('用户点击发送给朋友');
                },
                success: function (res) {
                    //alert('已分享');
                },
                cancel: function (res) {
                    //alert('已取消');
                },
                fail: function (res) {
                    //alert("this is "+JSON.stringify(res));
                }
            };
            wx.ready(function () {
                wx.onMenuShareAppMessage(shareData);
                wx.onMenuShareTimeline(shareFriends);
            });
            wx.error(function (res) {
                //alert(JSON.stringify(res));
            });
        },
        error: function (json) {
            //alert(JSON.stingify(json));
        }
    });
}