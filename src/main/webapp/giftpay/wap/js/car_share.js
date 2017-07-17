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
                title:"测一测你的车现在值多少钱",
                desc: "快来进行测试吧！",
                link:getRootPath()+'/giftpay/wap/carValuation.jsp',
                imgUrl:getRootPath()+'/giftpay/wap/img/shareCar.jpg',
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
                title:"测一测你的车现在值多少钱",
                link:getRootPath()+'/giftpay/wap/carValuation.jsp',
                imgUrl:getRootPath()+'/giftpay/wap/img/shareCar.jpg',
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