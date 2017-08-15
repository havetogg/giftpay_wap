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
//获取网址里的name参数
function getUrlParam(name) {
    //构造一个含有目标参数的正则表达式对象
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    //匹配目标参数
    var r = window.location.search.substr(1).match(reg);
    //alert(unescape(r[2]))
    //返回参数值.
    if (r != null) return unescape(r[2]);
    return null;
}
//var totalMoneys = getUrlParam("name");   页面获取参数



//分享功能
$(function () {


});
function closeAd(id) {
    $("#"+id).hide();
    document.body.style.position='absolute';
}
//t弹框页面
// var html= "    <div  id=\"ad_id\" class=\"advertisement_bg\">"
//     + "        <div class=\"tip-cont-\">"
//     + "                <div class=\"ad_title\">平安银行信用卡</div>"
//     + "                <div class=\"ad_log\"><img src=\"img/ad_logo.png\" alt=\"\"></div>"
//     + "                <div class=\"ad_little_img\"><img src=\"img/ad_1.png\" alt=\"\"></div>"
//     + "                <a class=\"ad_toBTN\" href=\"http://www.linkgift.cn/giftpay_wap//giftpay/giftUserCount/index.jsp?third_name=zfwcy\">我要办卡</a>"
//     + "            <div class=\"ad_little_img\"><img src=\"img/closebtn.png\" alt=\"\" onclick=\"closeAd('ad_id')\"></div>"
//     + "        </div>"
//     + "    </div>";
var html= "  <div  id=\"ad_id1\" class=\"advertisement_bg\">"
    + "        <div class=\"tip-cont1-\">"
    + "            <img src=\"img/ad_bg0.png\" alt=\"\">"
    + "        <a class=\"ad_toBTN\" href=\"http://www.linkgift.cn/giftpay_wap//giftpay/giftUserCount/index.jsp?third_name=zfwcy\">我要办卡</a>"
    + "            <div class=\"ad_little_img\"><img src=\"img/closebtn.png\" alt=\"\" onclick=\"closeAd('ad_id1')\"></div>"
    + "        </div>"
    + "  </div>"
function ad_tip(state) {
    if(state == 'show'){
        $('.zoomer').prepend(html);
        $('.zoomer').css('position','fixed;')
        document.body.style.position='fixed';
    }else if(state=='hide'){
        $('#ad_id').remove();
        document.body.style.position='absolute';
    }
}
//t弹框页面
var html1= "  <div  id=\"ad_id1\" class=\"advertisement_bg\">"
    + "        <div class=\"tip-cont1-\">"
    + "            <img src=\"img/ad_bg1.png\" alt=\"\">"
    + "        <a class=\"ad_toBTN\" href=\"http://www.linkgift.cn/giftpay_wap//giftpay/giftUserCount/index.jsp?third_name=zfwcy\">我要办卡</a>"
    + "            <div class=\"ad_little_img\"><img src=\"img/closebtn.png\" alt=\"\" onclick=\"closeAd('ad_id1')\"></div>"
    + "        </div>"
    + "  </div>";
function ad_tip1(state) {
    if(state == 'show'){
        document.body.style.position='fixed';
        $('.zoomer').prepend(html1);
    }else if(state=='hide'){
        $('#ad_id1').remove();
        document.body.style.position='absolute';




        
    }
}
//t弹框页面
var html2= "  <div  id=\"ad_id2\" class=\"advertisement_bg\">"
    + "        <div class=\"tip-cont11-\">"
    + "            <img src=\"img/ad_bg2.png\" alt=\"\">"
    + "        <a class=\"ad_toBTN\" href=\"https://prodone.juxinbox.com/sinopecGameCt/weixinMng/ManageC/userIn.htm\">去游戏中心</a>"
    + "            <div class=\"ad_little_img\"><img src=\"img/closebtn.png\" alt=\"\" onclick=\"closeAd('ad_id2')\"></div>"
    + "        </div>"
    + "  </div>";
function ad_tip2(state) {
    if(state == 'show'){
        document.body.style.position='fixed';
        $('.zoomer').prepend(html2);
    }else if(state=='hide'){
        $('#ad_id2').remove();
        document.body.style.position='absolute';
    }
}
//t3弹框页面
var html3= "    <div  id=\"ad_id3\" class=\"advertisement_bg\">"
    + "        <div class=\"tip-cont22-\">"
    + "            <img src=\"img/ad_bg3.png\" alt=\"\">"
    + "            <img src=\"img/codeGo.png\" alt=\"\" class=\"fingerCode\">"
    + "            <div class=\"finger\">"
    + "                <img src=\"img/line_yellw.png\" alt=\"\" class=\"line_yellow\">"
    + "                <div></div>"
    + "            </div>"
    + "            <div class=\"finger_bottom_close\"><img src=\"img/closebtn.png\" alt=\"\" onclick=\"closeAd('ad_id3')\"></div>\""
    + "        </div>"
    + "    </div>"
function ad_tip3(state) {
    if(state == 'show'){
        document.body.style.position='fixed';
        $('.zoomer').prepend(html3);
    }else if(state=='hide'){
        $('#ad_id3').remove();
        document.body.style.position='absolute';
    }
}
//t3 弹框页面
var html4= "<div  id=\"ad_id4\" class=\"advertisement_bg\">"
    + "        <div class=\"tip-cont22-\">"
    + "            <img src=\"img/ad_bg4.png\" alt=\"\">"
    + "            <a class=\"ad_toBTN_new\" href=\"https://xyk.cebbank.com/cebmms/apply/ps/card-list.htm?level=124&pro_code=FHTG200000SJ01NJJM\">去办卡</a>"
    + "            <div class=\"finger_bottom_close_\"><img src=\"img/closebtn.png\" alt=\"\" onclick=\"closeAd('ad_id4')\"></div>\""
    + "        </div>"
    + "    </div>";
function ad_tip4(state) {
    if(state == 'show'){
        document.body.style.position='fixed';
        $('.zoomer').prepend(html4);
    }else if(state=='hide'){
        $('#ad_id4').remove();
        document.body.style.position='absolute';
    }
}