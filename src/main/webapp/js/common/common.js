/**
 *
 *  build by rwson @ 2015-01-10
 *
 *  完成微信端的一些自适应屏幕功能(css3中的缩放特性)
 *
 */
//js获取项目根路径，如： http://localhost:8083/uimcardprj
function getRootPath() {
    // 获取当前网址，如： http://localhost:8083/uimcardprj/share/meun.jsp
    var curWwwPath = window.document.location.href;
    // 获取主机地址之后的目录，如： uimcardprj/share/meun.jsp
    var pathName = window.document.location.pathname;
    var pos = curWwwPath.indexOf(pathName);
    // 获取主机地址，如： http://localhost:8083
    var localhostPaht = curWwwPath.substring(0, pos);
    // 获取带"/"的项目名，如：/uimcardprj
    var projectName = pathName
        .substring(0, pathName.substr(1).indexOf('/') + 1);
    return (localhostPaht + projectName);
}


$(function () {
    var shareData = {};
    var shareFriends = {};
    $.ajax({
        url: getRootPath() + "/wechat/weChatJSConfigC/getWeCharJSConfigM.htm",
        data: {currUrl: location.href},
        dataType: "json",
        success: function (config) {
            wx.config($.parseJSON(config.resultObject));
            shareData = {
                title: "有礼付9折加油啦",
                desc: "我正在参加有礼付的九折加油活动，都抢疯啦！",
                link: getRootPath(),
                imgUrl: getRootPath() + '/img/share.png',
                // linkpath + '/share.jsp='+wxId+'&id='+currentId;
                trigger: function (res) {
                    //alert('用户点击发送给朋友');
                },
                success: function (res) {
                    //alert('已分享');
                    closeShareTip();
                },
                cancel: function (res) {
                    //alert('已取消');
                },
                fail: function (res) {
                    //alert("this is "+JSON.stringify(res));
                }
            };
            shareFriends = {
                title: "我正在参加有礼付的九折加油活动，都抢疯啦！ ",
                link: getRootPath(),
                imgUrl: getRootPath() + '/img/share.png',
                // linkpath + '/share.jsp='+wxId+'&id='+currentId;
                trigger: function (res) {
                    //alert('用户点击发送给朋友');
                },
                success: function (res) {
                    //alert('已分享');
                    closeShareTip();
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


    $(".get").click(function () {
        location.href = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx55f4f8fba8d287aa&redirect_uri=http%3a%2f%2fb.qianduan.com%2fMammon%2fIndex%3finviteCode%3d99XM33TV&response_type=code&scope=snsapi_userinfo&state=-1#wechat_redirect"
    });

    $(".get1").click(function () {
        location.href = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx55f4f8fba8d287aa&redirect_uri=http%3a%2f%2fb.qianduan.com%2fMammon%2fIndex%3finviteCode%3d99XM33TV&response_type=code&scope=snsapi_userinfo&state=-1#wechat_redirect"
    })
});
