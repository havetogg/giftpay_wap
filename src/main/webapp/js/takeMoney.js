/**
 * Created by Administrator on 2015/12/2.
 */
$(function(){
    function listDom(){
        var dom = $(
            '<div class="record2">'+
            '<div class="payMoney">提现金额：￥<span class=" payMoney1 f_28b">2700</span></div>'+
            '<div class="dataTime">2014-11-11 14:58:00</div>'+
            '<div class="edu">使用红包数量：<span class="redNum f_25">3</span></div>'+
            '<div class="stage">未提现</div>'+
            '</div>'
        );
        return dom;
    }

    $.ajax({
        url: getRootPath() + "/wechat/redPickInfoC/redOutRecords.htm",
        dataType: "json",
        type: "post",
        success: function (data) {
            console.log(data);
            if(data.resultList[0]==null){
                $(".tipsIm").css("display","block");
            }else{
            $.each(data.resultList, function (index, obj) {
                var dom = listDom();
                var statu;
                if(obj.status==1){
                    statu="提取成功"
                }else if(obj.status==0){
                    statu="提取失败"
                    $(".stage", dom).css("color","red");
                }else if(obj.status==2){
                    statu="正在提取中！"
                }
                $(".payMoney1", dom).html(obj.redMoney);
                $(".dataTime", dom).html(obj.createTime);
                $(".redNum", dom).html(obj.redNumber);
                $(".stage", dom).html(statu);
                $(".pass").append(dom);
            });
        }
        }
    });


});