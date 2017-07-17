/**
 * Created by Administrator on 2015/12/2.
 */
$(function(){

    $(".yes").click(function() {
        var oilCard = $("#oilCard").val(),
            linkName = $("#linkName").val(),
            linkPhone = $("#linkPhone").val(),
            checkBool = document.getElementById("checkBox").checked;
            zsy = new RegExp(/^9\d{15}$/);
            zsh = new RegExp(/^(100011(\d{2})(000)(\d{8}))$/);

        if(!($("#oilCard").val() == "" || (zsh.test($("#oilCard").val()) || zsy.test($("#oilCard").val())))){
            alerw("请填写正确的加油卡号！");
        }else if(linkPhone.length<11||jxTool.isMobile(linkPhone)==false){
            alerw("请填写正确的手机号码!");
        }else if(linkName.length<2||jxTool.isChinese(linkName)==false){
            alerw("请填写正确的姓名!");
        }else if(!checkBool){
            alerw("请先阅读并同意服务条款！");
        }else {
            loading("start");
            $.ajax({
                url:getRootPath()+"/wechat/gasCard/buildGasCard.htm",
                data:{"gasCardNumber": oilCard,"buildTel": linkPhone,"buildUname": linkName},
                dataType:"json",
                type:"post",
                success:function(data){
                    loading("stop");
                    if(data.code==1){
                        location.href=getRootPath()+"/moneyTocard.jsp";
                    }else{
                        alerw(data.mess)
                    }
                }
            });



        }

    });

});