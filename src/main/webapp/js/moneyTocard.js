/**
 * Created by Administrator on 2015/11/24.
 */
//function whichWay(){
//    var usePage = $('input:radio[name="usePage"]:checked').val();
//    if(usePage=="bySelf"){
//        $(".selectRed").css("display","block")
//    }else{
//        $(".selectRed").css("display","none")
//    }
//}
var whichPage;
function send(){
    var value = new Array();
    var redInfo = $("input[name='redbage']");
    $(redInfo).each(function(i,n){
        if($(n).prop('checked')){
            value.push($(n).val());
        }
    });
    whichPage = value.toString();
}





$(function(){




    function listDom(forWhich,money){
        var dom = $(
            '<div class="redMoney">'+
            '<input type="checkbox" id="'+forWhich+'" class="checkBoxBox1" money="'+money+'" value="'+forWhich+'" name="redbage" onchange="send()">'+
            '<label for="'+forWhich+'" class="checkBox1">'+
            '<div class="redMoney1">'+
            '<div class="top2">红包金额：￥<span class="redMoney2" id="redMoney1">2</span></div>'+
            '<div class="comeFrom1">来自9折加油</span>活动的加油红包</div>'+
            '<div class="time1" id="time1">2015-10-18 14:58:00</div>'+
            '<div class="state1">未提现</div>'+
            '</div>'+
            '</label>'+
            '</div>'
        );
        return dom;
    }
    var willMoney;
    $.ajax({
        url: getRootPath() + "/wechat/redInfoC/canExtractRed.htm",
        dataType: "json",
        type: "post",
        success: function (data) {
            console.log(data);
            if(data.resultList[0]==null){
                $(".tipsIm").css("display","block");
                $(".protocol").css("display","none");
                $(".apply").css("display","none");
                $(".pay").css("display","none");
            }else{
            $.each(data.resultList, function (index, obj) {
                var dom = listDom(obj.redID,obj.redMoney);

                $(".redMoney2", dom).html(obj.redMoney);
                //$(".comeFrom1", dom).html(obj["activityDetail"]);
                $(".time1", dom).html(obj.startTime);
                $(".selectRed").append(dom);
            });
            var redInfo = $("input[name='redbage']");
            $(redInfo).each(function(i,n){
                $(n).click(function(){

                    var currTotalMoney = parseFloat($("#totalMoney").text());
                    var thisMoney = parseFloat($(this).attr("money"));
                    //var willMoney;

                    if($(this).prop('checked')){
                        willMoney = currTotalMoney + thisMoney;
                    }else{
                        willMoney = currTotalMoney - thisMoney;
                    }
                    $("#totalMoney").text(willMoney.toFixed(2));
                });
            });

            }
        }
    });

    //$(".nu11").click(function(){
    //    $(".nu11").addClass("green white");
    //    $(".nu12").removeClass("green white");
    //    $(".nu13").removeClass("green white");
    //});
    //$(".nu12").click(function(){
    //    $(".nu12").addClass("green white");
    //    $(".nu11").removeClass("green white");
    //    $(".nu13").removeClass("green white");
    //});
    //$(".nu13").click(function(){
    //    $(".nu13").addClass("green white");
    //    $(".nu12").removeClass("green white");
    //    $(".nu11").removeClass("green white");
    //});
    //
    //$(".nu21").click(function(){
    //    $(".nu21").addClass("green white");
    //    $(".nu22").removeClass("green white");
    //    $(".nu23").removeClass("green white");
    //});
    //$(".nu22").click(function(){
    //    $(".nu22").addClass("green white");
    //    $(".nu21").removeClass("green white");
    //    $(".nu23").removeClass("green white");
    //});
    //$(".nu23").click(function(){
    //    $(".nu23").addClass("green white");
    //    $(".nu22").removeClass("green white");
    //    $(".nu21").removeClass("green white");
    //});


    $(".apply").click(function(){
        //var howMuch = $('input:radio[name="howMuch"]:checked').val();
        //var time = $('input:radio[name="time"]:checked').val();

        var checkBool = document.getElementById("checkBox").checked;
        //var oilRedId = document.getElementById(forWhich).checked;

        var isNotSelectRedInfo = false;

        $("input[name='redbage']").each(function(i,n){
            if($(n).prop("checked")){
                isNotSelectRedInfo = true;
                return false ;
            }
        });


        if(!isNotSelectRedInfo){
            alerw("请先选择所需提现的红包！");
        }else if(!checkBool){
            alerw("请先阅读并同意服务条款！");
        }else{
            loading("start");
            $.ajax({
                url: getRootPath() + "/wechat/redInfoC/extractRed.htm",
                data:{"redids": whichPage},
                dataType: "json",
                type: "post",
                success: function (data) {
                    loading("stop");
                    if(data.code == 1){
                        location.href = getRootPath()+"/getCardsuccess.jsp?Money=" + willMoney;
                    }else{
                        alerw(data.mess);
                    }


                }
            });
        }

    })



});
/**
 * Created by Administrator on 2015/11/30.
 */
