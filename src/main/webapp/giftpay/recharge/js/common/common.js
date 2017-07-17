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
function queryGoodsList(goodsType, goodsSubType, status,callback) {
    var data = new Object();
    var res;
    data.goodsType = goodsType;
    data.goodsSubType = goodsSubType;
    data.status = status;
    var page = window.location.pathname;
    page = page.substring(page.lastIndexOf("/") + 1, page.lastIndexOf("."));
    data.page = page;
    $.ajax({
        url: getRootPath() + "/giftpay/commonSetting/queryAllGoodsList.htm",  //服务器
         // url: "/giftpay/commonSetting/queryAllGoodsList.htm",  //本地测试
        data: data,
        dataType: "json",
        type: "post",
        success: callback,
        error: function () {
            loading("stop");
        }
    });
    return res;
}

function queryUserRedPkgList(callback) {
    $.ajax({
        url: getRootPath() + "/giftpay/telRecharge/getRedPkg.htm",
        dataType: "json",
        type: "post",
        success: callback
    });
}