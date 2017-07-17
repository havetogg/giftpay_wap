/**
 * Created by Administrator on 2015/12/7.
 */

var choiceWhich;
var toggle = function(func1 , func2){
    this.index = 0;
    this.change = function(){
        if(this.index == 0){
            console.log("this is function 1");
            func1();
            this.index++;
        }else{
            console.log("this is function 2");
            func2();
            this.index--;
        }
    }
};

$(function(){

    var t = new toggle(function(){
        $(".c1").css("color","red");
        $(".c1").css("border", "1px solid red");
    },function(){
        $(".c1").css("color","");
        $(".c1").css("border", "1px solid ");
    });

    $(".c1").click(function(){
        t.change();
    });

});
$(function(){

    var t = new toggle(function(){
        $(".c2").css("color","red");
        $(".c2").css("border", "1px solid red");
    },function(){
        $(".c2").css("color","");
        $(".c2").css("border", "1px solid ");
    });

    $(".c2").click(function(){
        t.change();
    });

});
$(function(){

    var t = new toggle(function(){
        $(".c3").css("color","red");
        $(".c3").css("border", "1px solid red");
    },function(){
        $(".c3").css("color","");
        $(".c3").css("border", "1px solid ");
    });

    $(".c3").click(function(){
        t.change();
    });

});

$(function(){

    var t = new toggle(function(){
        $(".c4").css("color","red");
        $(".c4").css("border", "1px solid red");
    },function(){
        $(".c4").css("color","");
        $(".c4").css("border", "1px solid ");
    });

    $(".c4").click(function(){
        t.change();
    });

});


function changeColor(){
    var value = new Array();
    var redInfo = $("input[name='choseTypes']");
    $(redInfo).each(function(i,n){
        if($(n).prop('checked')){
            value.push($(n).val());

        }

    });
    choiceWhich = value.toString();
}



$(function(){

    $.ajax({
        url: getRootPath() + "/wechat/userC/queryWetInfo.htm",
        dataType: "json",
        type: "post",
        success: function (data) {
            $(".tle").val(data.resultObject.uTel);
        }
    });



    $(".apply").click(function(){

        var tel = $(".tle").val();
        var types=$(".type").val();
        var isNotSelectRedInfo = false;



        $("input[name='choseTypes']").each(function(i,n){
            if($(n).prop("checked")){
                isNotSelectRedInfo = true;
                return false ;
            }
        });

        if(!isNotSelectRedInfo){
            alerw("请先选择所需的服务项目！");
        }else if(tel.length<11||jxTool.isMobile(tel)==false){
            alerw("请填写正确的手机号码!");
        }else if(types.length<2||jxTool.isWord(types)==true){
            alerw("请填写您的汽车型号!")
        }else{
            loading("start");

            $.ajax({
                url: getRootPath() + "/wechat/addMaintenance.htm",
                data: {"userTel": tel,"carType":types,"serviceType":choiceWhich},
                dataType: "json",
                type: "post",
                success: function (data) {
                    loading("stop");
                    alerw("您已成功预约！","确定",function(){location.href=getRootPath()+"/index.jsp"})

                }
            });

        }

    });
});









