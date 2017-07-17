/**
 * Created by Administrator on 2017/5/17.
 */
var pageNum = 0;
var infoData = '';
var isData = true;
var orderNo=0;
var state='';
$(window).scroll(
    function () {
        var totalheight = parseFloat($(window).height()) + parseFloat($(window).scrollTop());

        if(totalheight>=$(document).height()){
            loadMore();
        }
    });
$(function () {
    state=getUrlParam("state")
    if(state=='1'){
        $(".redName").html("激活红包数");
    }else if(state=='2'){
        $(".redName").html("使用红包数");
    }
    initAllOilRecord();
})
function loadMore(){
    pageNum=pageNum+5;
    initAllOilRecord();
}
function initAllOilRecord() {
    if (isData) {
        $.ajax({
            type: "GET",
            url: getRootPath() + "/count/countRedPkgAllInfo.htm",
            data:{"state":state},
            dataType: "json",
            success: function (res) {
                var data=res.data;
                console.log(data)
                var str = "";
                var money = 0;
                if (data.length > 0) {
                    isData = false;
                    infoData = data;
                    $.each(data, function (index, item) {
                        if (index >= pageNum && index < (pageNum * 1 +5)) {
                            orderNo++;
                            str += '<li><h1><label>用户ID：</label>';
                            str += '<span>' + item.userId + '</span>';
                            str += '</h1><p><label>红包名称：</label>';
                            str += '<span class="f40">' + item.redpkgName + '</span>';
                            str += '</p><p><label>红包类型：</label>';
                            str += '<span>' + item.brand + '</span>';
                            str += '</p><p>';
                            str += '<label>红包金额：</label>';
                            str += '<span>' + item.value + '</span>元';
                            str += '</p><b>' + (orderNo) + '</b></li>';
                        }
                    })
                } else {
                    isData = false;
                }
                $(".redPacket_list ul").append(str);
                $(".allcount").text(data.length);
            },
            error: function (res) {
                console.log(res)
            }
        });
    }else{
        var str='';
        $.each(infoData, function (index, item) {
            if (index >= pageNum && index < (pageNum * 1 + 5)) {
                orderNo++;
                str += '<li><h1><label>用户ID：</label>';
                str += '<span>' + item.userId + '</span>';
                str += '</h1><p><label>红包名称：</label>';
                str += '<span class="f40">' + item.redpkgName + '</span>';
                str += '</p><p><label>红包类型：</label>';
                str += '<span>' + item.brand + '</span>';
                str += '</p><p>';
                str += '<label>红包金额：</label>';
                str += '<span>' + item.value + '</span>元';
                str += '</p><b>' + (orderNo) + '</b></li>';
            }
        })
        $(".redPacket_list ul").append(str);
    }
}

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
