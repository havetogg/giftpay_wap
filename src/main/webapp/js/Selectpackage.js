/**
 * Created by Administrator on 2015/11/24.
 */
$(function(){

    $(".nu11").click(function(){
        $(".nu11").addClass("green white");
        $(".nu12").removeClass("green white");
        $(".nu13").removeClass("green white");
    });
    $(".nu12").click(function(){
        $(".nu12").addClass("green white");
        $(".nu11").removeClass("green white");
        $(".nu13").removeClass("green white");
    });
    $(".nu13").click(function(){
        $(".nu13").addClass("green white");
        $(".nu12").removeClass("green white");
        $(".nu11").removeClass("green white");
    });

    $(".nu21").click(function(){
        $(".nu21").addClass("green white");
        $(".nu22").removeClass("green white");
        $(".nu23").removeClass("green white");
    });
    $(".nu22").click(function(){
        $(".nu22").addClass("green white");
        $(".nu21").removeClass("green white");
        $(".nu23").removeClass("green white");
    });
    $(".nu23").click(function(){
        $(".nu23").addClass("green white");
        $(".nu22").removeClass("green white");
        $(".nu21").removeClass("green white");
    });


$(".apply").click(function(){
    var howMuch = $('input:radio[name="howMuch"]:checked').val();
    var time = $('input:radio[name="time"]:checked').val();
    var checkBool = document.getElementById("checkBox").checked;

    if(!checkBool){
        alerw("请先阅读并同意活动协议！");
    }
    //发送ajax请求，向后台发送howMuch，time



})



});
