/**
 * Created by Administrator on 2017/5/11.
 */



// jWeChat-1.0.0.js 用分享的时候需要导入wx的js
// $(function(){
//     wxShare();  //暂时注释，分享配置 在jsp页面function方法中
// })

function wxShare(){
    $.ajax({
        url: getRootPath()+"/giftpay/wap/shareFriend.htm",
        data: {'url': location.href},
        dataType: "json",
        success: function (config) {
            wx.config(config);
            shareData = {
                title:"油价大战升级！中石化加油88折！",
                desc: "加油88折！还送110万意外险！还送......",
                //分享跳转链接
               link:getRootPath()+'/giftUserGo/wxLogin.htm?third_name=zfwcy',
                // link:getRootPath()+'/giftUserGo/wxLogin.htm?third_name='+third_name,
                //分享连接图片
               imgUrl:getRootPath()+'/giftpay/giftUserCount/img/share88.png',

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
                title:"油价大战升级！中石化加油88折！",
                desc: "加油88折！还送110万意外险！还送......",
                //分享跳转链接
                link:getRootPath()+'/giftUserGo/wxLogin.htm?third_name=zfwcy',
                // link:getRootPath()+'/giftUserGo/wxLogin.htm?third_name='+third_name,
                //分享连接图片
                imgUrl:getRootPath()+'/giftpay/giftUserCount/img/share88.png',
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