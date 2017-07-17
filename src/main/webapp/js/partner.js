/**
 * Created by Administrator on 2015/12/8.
 */
$(function(){
    $.ajax({
        url: getRootPath() + "/wechat/userC/queryWetInfo.htm",
        dataType: "json",
        type: "post",
        success: function (data) {
            $(".phones").val(data.resultObject.uTel);
        }
    });




    $(".apply").click(function(){
        var tel = $(".phones").val();
        var person = $(".person").val();
        var textContents=$(".textContents").val();

        if(textContents.length<3||jxTool.isWord(textContents)==true){
            alerw("请输入您的合作内容！");
        }else if(tel.length<11||jxTool.isMobile(tel)==false){
            alerw("请填写正确的手机号码!");
        }else if(person.length<2||jxTool.isChinese(person)==false){
            alerw("请填写您的姓名！")
        }else{
            loading("start");
            $.ajax({
                url: getRootPath() + "/wechat/addBusiness.htm ",
                data: {"userTel": tel,"context":textContents,"userName":person},
                dataType: "json",
                type: "post",
                success: function (data) {
                    loading("stop");
                    alerw("提交成功，请等待客服人员与您联系！","确定",function(){location.href=getRootPath()+"/index.jsp"})

                }
            })

        }
    });


});