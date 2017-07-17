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

$(function(){
    $(".readAgreement,.jiaYou").click(function(){
        location.href=getRootPath()+"/agreement.jsp";
    });

   // 底部用户信息部分数据请求
    $.ajax({
        url: getRootPath() + "/wechat/userC/queryWetInfo.htm",
        dataType: "json",
        type: "post",
        success: function (data) {
            var oImg = $("<img />");
            oImg.attr({
                "src":data.resultObject.headimgurl,
                "width":76,
                "height":76
            });
            $(".touXiang").prepend(oImg);
            $(".name").html(data.resultObject.nickname);
            $(".phone").html(data.resultObject.uTel);
        }
    });
    $(".button").click(function(){
        location.href=getRootPath()+"/index.jsp";
    });

    $(".pic").click(function(){
        location.href=getRootPath()+"/cardCenter.jsp";
    });
    $(".pic1").click(function(){
        location.href=getRootPath()+"/myPacket.jsp";
    });
    $(".touXiang").click(function(){
        location.href=getRootPath()+"/personCenter.jsp";
    });
    $(".perCenter").click(function(){
        location.href=getRootPath()+"/personCenter.jsp";
    });
    $(".pic0").click(function(){
        location.href=getRootPath()+"/index.jsp";
    });


});




$(function () {

    // var phoneWidth = parseInt(window.screen.width),
    //     phoneScale = phoneWidth / 640,
    //     ua = navigator.userAgent;
    // if (/Android/.test(ua)) {
    //     if (/Android (\d+\.\d+)/.test(ua)) {
    //         var version = parseFloat(RegExp.$1);
    //         $("head").append('<meta name="viewport" content="initial-scale=0.5,maximum-scale=0.5,minimum-scale=0.5, width=640, target-densitydpi=device-dpi">');
    //     } else {
    //         $("head").append('<meta name="viewport" content="width=640, target-densitydpi=device-dpi">');
    //     }
    // } else {
    //     $("head").append('<meta name="viewport" content="initial-scale=0.5,maximum-scale=0.5,minimum-scale=0.5, user-scalable=no">');
    // }

    // var w = $(window).width();
    // var h = $(window).height();
    //
    // var scale = w / 640;
    //
    // var tw = Math.ceil(w / scale);
    // var th = Math.ceil(h / scale);

    // $(".zoomer").css({
    //     "-webkit-transform-origin": "0 0",
    //     "-moz-transform-origin": "0 0",
    //     "-ms-transform-origin": "0 0",
    //     "-o-transform-origin": "0 0",
    //     "transform-origin": "0 0"
    // });
    // $(".zoomer").css({
    //     "-webkit-transform": "scale(" + scale + "," + scale + ")",
    //     "-moz-transform": "scale(" + scale + "," + scale + ")",
    //     "-ms-transform": "scale(" + scale + "," + scale + ")",
    //     "-o-transform": "scale(" + scale + "," + scale + ")",
    //     "transform": "scale(" + scale + "," + scale + ")"
    // });
    //
    // $("html").css({"width": w});
    // $("body").css({"width": w});
    // $(".zoomer").css({"width": tw});
    // $(".zoomer").css({"height": th});
    //
    // if($("div.full-screen").length > 0){
    //     $("html").css({"height": h});
    //     $("body").css({"height": h});
    //     $(".zoomer").css({"height": th});
    //     $(".wrap").css({"height": th});
    // }
    /*
    *   在页面上制定了全屏时，再应用高度值
    * */

});
