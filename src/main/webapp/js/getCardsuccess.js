/**
 * Created by Administrator on 2015/11/30.
 */
function getUrlParam(name) {
    //����һ������Ŀ�������������ʽ����
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    //ƥ��Ŀ�����
    var r = window.location.search.substr(1).match(reg);
    //alert(unescape(r[2]))
    //���ز���ֵ.
    if (r != null) return unescape(r[2]);
    return null;
}
$(function(){
    var totalMoneys = getUrlParam("Money");
   $(".totleMoney").html(totalMoneys);

});