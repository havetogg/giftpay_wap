<%@ page language="java" import="java.util.*" isELIgnored="false" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en" class="html_gray">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=0">
    <meta charset="UTF-8">
    <title>平安车主信用卡</title>
    <script src="lib/jquery.js"></script>
    <script src="js/index.js"></script>
    <script src="js/layout.js"></script>
    <script src="js/m.tool.juxinbox.com.js"></script>
    <script src="../../js/common/jWeChat-Adaptive.js"></script>
    <script src="../../js/common/jWeChat-1.0.0.js" ></script>
    <link rel="stylesheet" href="css/tool.css">
    <link rel="stylesheet" href="css/app.css">
    <link rel="stylesheet" href="css/animate.min.css">
</head>
<body>

<script>

    <%

        //从请求中获取当前传递openId
       String request_openId  = request.getParameter("openId")==""?"0":request.getParameter("openId");
       String openId = request_openId.equals("null")?"0":request_openId;

       //String openId ="null";  //测试openId为空时，页面是否 弹窗提示
       //String openId = request.getParameter("openId")==""?"0":request.getParameter("openId");

       String phone = request.getParameter("phone")==""?"0":request.getParameter("phone");
    %>

    //将用户的openId作为用户分享当前链接的渠道码
    var trird_name = "<%=openId%>"
    var phone = "<%=phone%>";

    console.log("当前用户openId作为分享渠道码:"+trird_name)
    console.log("当前用户的手机号码是否存在:"+phone)

    $(function (){

        //判断渠道码是否存在并更新点击率
        if(trird_name=="0"){
            TipShow("分享功能已失效,未能成功获取您当前登陆状态,请重新登陆此页面！",1000);
            //移除click [当前用户无法点击提现功能按钮]
            $('#getCashBtn').unbind("click");
        }else{
            wxShare(trird_name);//配置分享链接[根据当前]
            SearchShareTime(trird_name);//查询当前用户openId作为渠道号其他用户浏览次数
            SearchPaBankUser_SuccessCount(trird_name); //查询当前用户openId作为渠道号成功办卡用户数量
        }

        //判断当前用户是否存在手机号码
        if(phone!="0"){
            console.log("存在手机号码")
            $('#userPhone').val("15730356136")
            $("#phone_input").hide();
            $("#getCashBtn").removeClass("my_money_getBtn_");
        }else{
            console.log("不存在手机号码!强制用户更新手机号码")
        }

        //分享配置
        function wxShare(third_name_input){
            $.ajax({
                url: getRootPath()+"/giftpay/wap/shareFriend.htm",
                data: {'url': location.href},
                dataType: "json",
                success: function (config) {
                    wx.config(config);
                    shareData = {
                        title:"油价大战升级！中石化加油88折！",
                        desc: "加油88折！还送110万意外险！还送......",
                        //分享跳转链接
                        //link:getRootPath()+'/giftUserGo/wxLogin.htm?third_name=zfwcy',
                        link:getRootPath()+'/giftUserGo/wxLogin.htm?third_name='+third_name_input,
                        //分享连接图片
                        imgUrl:getRootPath()+'/giftpay/giftUserCount/img/share88.png',

                        // linkpath + '/share.jsp='+wxId+'&id='+currentId;
                        trigger: function (res) {
                            //alert('用户点击发送给朋友');
                        },
                        success: function (res) {
                            //alert('已分享');
                        },
                        cancel: function (res) {
                            //alert('已取消');
                        },
                        fail: function (res) {
                            //alert("this is "+JSON.stringify(res));
                        }
                    };
                    //发送给朋友圈
                    shareFriends = {
                        title:"油价大战升级！中石化加油88折！",
                        desc: "加油88折！还送110万意外险！还送......",
                        //分享跳转链接
                        // link:getRootPath()+'/giftUserGo/wxLogin.htm?third_name=zfwcy',
                        link:getRootPath()+'/giftUserGo/wxLogin.htm?third_name='+third_name_input,
                        //分享连接图片
                        imgUrl:getRootPath()+'/giftpay/giftUserCount/img/share88.png',
                        // linkpath + '/share.jsp='+wxId+'&id='+currentId;
                        trigger: function (res) {
                            //alert('用户点击发送给朋友');
                        },
                        success: function (res) {
                            //alert('已分享');
                        },
                        cancel: function (res) {
                            //alert('已取消');
                        },
                        fail: function (res) {
                            //alert("this is "+JSON.stringify(res));
                        }
                    };
                    wx.ready(function () {
                        wx.onMenuShareAppMessage(shareData);
                        wx.onMenuShareTimeline(shareFriends);
                    });
                    wx.error(function (res) {
                        //alert(JSON.stringify(res));
                    });
                },
                error: function (json) {
                    //alert(JSON.stingify(json));
                }
            });
        }

        //我的分享浏览次数
        function SearchShareTime(openId_input){
            $.ajax({
                url: getRootPath()+"/giftGO/searchChannelList.htm",
                data: {'channel_code': openId_input},
                dataType: "json",
                success: function (data) {
                    console.log("当前用户分享链接被浏览次数:"+data[0].click_num)
                    $("a[name=personAll]").text(data[0].click_num);
                },
                error: function (data) {
                    console.log("查询分享结果失败！")
                }
            });
        }

        //分享成功次数[其他用户成功办卡]
        function SearchPaBankUser_SuccessCount(openid_input2){
            $.ajax({
                url: getRootPath()+"/pinganShare/searchPaBankUser.htm",
                data: {'openid': openid_input2},
                dataType: "json",
                success: function (data) {
                    console.log("当前用户openId作为渠道码分享用户成功办卡数量:"+data[0].get_bankuser)
                    $("a[name=personSuccess]").text(data[0].get_bankuser);

                    //当前用户金额
                    $("a[name=userMoneyGet]").text(data[0].get_bankuser*70)

                },
                error: function (data) {
                    console.log("查询分享结果失败！")
                }
            });
        }

    })

</script>

    <div class="recommend_bg">
        <div id="shareTip" class="shareTip" onclick="shareTipHide()">
            <img src="img/shareBg.png" alt="">
        </div>
        <div id="money_tip" class="my_money_off">
            <div class="my_money_content">
                <div style="padding: 10px 20px;">
                    <div class="my_money_title">我的奖金</div>
                    <ul class="flex subContent">
                        <li class="flex-1">
                            <div class="my_money_subTitle">已推荐</div>
                            <div class="my_money_subNumber"><a name="personAll">0</a><a class="person_">人</a></div>
                        </li>
                        <li class="flex-1">
                            <div class="my_money_subTitle">推荐成功</div>
                            <div class="my_money_subNumber"><a name="personSuccess">0</a><a class="person_">人</a></div>
                        </li>
                    </ul>
                    <ul class="flex imgss">
                        <li class="flex-1 jmt_center">
                            <img src="img/pig.png" alt="">
                        </li>
                        <li class="flex-1">
                            <div class="tips1">已获奖金</div>
                            <div><a name="userMoneyGet" class="tips2">0</a><a class="tips3">元</a></div>
                        </li>
                    </ul>
                </div>

                <div class="my_money_iknow" onclick="iKnows()">我知道了</div>
            </div>
        </div>

        <div id="GetMoney_tip" class="getMy_money_off">
            <div class="my_money_content1">
                <div style="padding: 20px 20px;">
                    <div class="my_money_title">申请提现</div>

                    <div class="getCash_txt">
                        <div class="getCash_text">
                            确认将已获奖金
                        </div>
                        <div class="getCash_text">
                            提现至微信钱包吗
                        </div>
                        <div id="phone_input" style="text-align:center" >
                            <input id="userPhone" class="my_money_input"   maxlength="11" type="tel" placeholder="请输入手机号码">
                        </div>
                    </div>
                    </div>


                <div class="my_money_iknow flex" onclick="iKnows()">
                    <div class="flex-1 getCash_cancel" onclick="getCashCancel()">取消</div>
                    <div id="getCashBtn" class="flex-1 my_money_getBtn_" onclick="getCashSure()">确认提现</div>
                </div>
            </div>
        </div>
        <div class="recommend_">
            <div class="r_title">
                <img src="img/title.png" alt="">
            </div>

            <div class="recommend_txt">
                平安车主信用卡，加油只要88折
            </div>
            <ul class="flex recommend_content">
                <li class="flex-1">
                    <img src="img/img1.png" alt="">
                </li>
                <li class="flex-1">
                    <img src="img/img2.png" alt="">
                </li>
                <li class="flex-1">
                    <img src="img/img3.png" alt="">
                </li>
                <li class="flex-1">
                    <img src="img/img4.png" alt="">
                </li>
                <li class="flex-1">
                    <img src="img/img5.png" alt="">
                </li>
                <li class="flex-1">
                    <img src="img/img6.png" alt="">
                </li>
            </ul>
            <div>
                <img class="recommend_img" src="img/recommendImg.png" alt="">
            </div>
            <div>
                <img class="recommend_btns" src="img/recommendBtn.png" alt="" onclick="shareTip()">
            </div>
            <div>
                <img class="money_btns" src="img/btn_money.png" alt="" onclick="showMoneyTip()">
                <img class="money_btns" src="img/getCash.png" alt="" onclick="getCascadeTip()">
            </div>
            <div>
                <img class="recommend_logo" src="img/logo2.png" alt="">
            </div>
        </div>
    </div>
</body>
</html>