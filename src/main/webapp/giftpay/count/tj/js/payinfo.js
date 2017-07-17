/**
 * Created by Administrator on 2017/5/17.
 */
var pageNum = 0;
var infoData = '';
var isData = true;
var orderNo=0;
$(window).scroll(
    function () {
        var totalheight = parseFloat($(window).height()) + parseFloat($(window).scrollTop());

        if(totalheight>=$(document).height()){
            loadMore();
        }
    });
$(function () {
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
            url: getRootPath() + "/count/countPayAllInfo.htm",
            dataType: "json",
            success: function (data) {
                console.log(data)
                var str = "";
                var money = 0;
                if (data.length > 0) {
                    isData = false;
                    infoData = data;
                    $.each(data, function (index, item) {
                        if (index >= pageNum && index < (pageNum * 1 +5)) {
                            orderNo++;
                            str += '<li><h1><label>电话：</label>';
                            str += '<span>' + item.phone + '</span>';
                            str += '</h1><p><label>订单类型：</label>';
                            str += '<span class="f40">' + item.businessInfo + '</span>';
                            str += '</p><p><label>订单名称：</label>';
                            str += '<span>' + item.dealInfo + '</span>';
                            str += '</p><p>';
                            str += '<label>订单金额：</label>';
                            str += '<span>' + item.dealMoney + '</span>';
                            str += '</p><p>';
                            str += '<label>订单实付：</label>';
                            str += '<span>' + item.dealRealMoney + '</span>';
                            str += '</p><p>';
                            str += '<label>下单日期：</label>';
                            str += '<span>' + item.dealTime.substring(0, item.dealTime.indexOf('.')) + '</span>';
                            str += '</p><b>' + (orderNo) + '</b></li>';
                        }
                        if(item.dealType=='2'){
                            money+=item.dealRealMoney;
                        }
                    })
                } else {
                    isData = false;
                }
                $(".redPacket_list ul").append(str);
                $(".allcount").text(data.length);
                $(".allmoney").text(money.toFixed(2));
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
                str += '<li><h1><label>电话：</label>';
                str += '<span>' + item.phone + '</span>';
                str += '</h1><p><label>订单类型：</label>';
                str += '<span class="f40">' + item.businessInfo + '</span>';
                str += '</p><p><label>订单名称：</label>';
                str += '<span>' + item.dealInfo + '</span>';
                str += '</p><p>';
                str += '<label>订单金额：</label>';
                str += '<span>' + item.dealMoney + '</span>';
                str += '</p><p>';
                str += '<label>订单实付：</label>';
                str += '<span>' + item.dealRealMoney + '</span>';
                str += '</p><p>';
                str += '<label>下单日期：</label>';
                str += '<span>' + item.dealTime.substring(0, item.dealTime.indexOf('.')) + '</span>';
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
