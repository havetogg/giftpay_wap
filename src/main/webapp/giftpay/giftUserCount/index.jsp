<%@ page language="java" import="java.util.*" isELIgnored="false" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en" class="html_gray">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=0.5, maximum-scale=0.5, user-scalable=0">
    <meta charset="UTF-8">
    <title>平安银行</title>
    <script src="js/layout.js"></script>
    <script src="lib/jquery.js"></script>
    <script src="js/index.js"></script>
    <script src="js/m.tool.juxinbox.com.js"></script>
    <script src="../../js/common/jWeChat-Adaptive.js"></script>
    <script src="../../js/common/jWeChat-1.0.0.js" ></script>
    <%--<script src="js/wx_share.js"></script>--%>
    <link rel="stylesheet" href="css/tool.css">
    <link rel="stylesheet" href="css/app.css">
</head>
<body>

<script type="text/javascript">

    <%
        //从请求中获取当前手机号码phone,根据有无传入手机号码判断当前用户是否存在已经绑定手机号码.
        String phone = request.getParameter("phone")==""?"0":request.getParameter("phone");

        //String third_name = request.getParameter("third_name")==""?"0":request.getParameter("third_name");

        //从请求中获取渠道码,判断渠道码不为""或者null
        String request_thirdName  = request.getParameter("third_name")==""?"0":request.getParameter("third_name");
        String third_name = request_thirdName.equals("null")?"0":request_thirdName;

        //判断当前是否pc端访问
        String mode  = request.getParameter("mode")=="pc"?"0":request.getParameter("mode");

    %>


    var phone = "<%=phone%>"

    var trird_name = "<%=third_name%>"

    var modePC = "<%=mode%>";

    $(function () {

            console.log("当前手机号码:["+ phone+"]");
            console.log("当前渠道码:["+trird_name+"]");

            if (phone !="0") {
                console.log("存在手机号，不需要绑定用户手机号！")
                $("#phoneNumber").css('display', 'none');//隐藏输入框
                $("#btn_y").css('display', 'block'); //显示办卡按钮
                $("#btn_n").css('display', 'none');//置灰办卡按钮

                //防止非空验证报错［电话号码，验证码］
                $('#tel').val("15730356136");
                $('#code').val("[不需要验证码！]");

                <%--$(".index_content").css("height", "1200px");--%>
            } else {
                console.log("不存在手机号码，需要进行校验操作。")
                $("#btn_y").css('display', 'none');
                $("#btn_n").css('display', 'block');
            }

            //判断渠道码是否存在并更新点击率
            if(trird_name=="0"){
                console.log("未获取到渠道码，不更新点击率 !")
                //当前获取openId为null，渠道码变更zfwcy
                wxShare("zfwcy");
            }else{
                //更新点击率
                addChannelCount(trird_name);
                //配置分享链接[根据当前]
                wxShare(trird_name);
            }

            //添加渠道码点击率
            function addChannelCount(trird_name_input){
                $.ajax({
                    type : "POST",  //提交方式
                    url : getRootPath()+"/giftGO/addChannelData.htm",//路径
                    data : {"third_name" : trird_name_input},//数据，这里使用的是Json格式进行传输
                    dataType:'json',
                    success : function(data) {//返回数据根据结果进行相应的处理
                        if(data.result=="success"){

                            console.log("成功！~~更新点击率!")
                            if(data.code =="0010"){
                                console.log("更新当前渠道码:[${third_name}]点击率成功!");
                            }else if (data.code =="0020"){
                                console.log("渠道数据创建 成功!")
                            }

                        } else{
                            console.log("错误！"+data.mess)
                        }
                    }
                });
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

    })


</script>

<div class="index_bg">
    <div class="index_title">
        <img src="img/title.png" alt="">
    </div>
    <div class="index_content">

        <div class="box">
        <img src="img/txt.png" alt="">
        </div>
        <ul id="phoneNumber" class="inputs">
            <li>
                <input id="tel" type="tel" placeholder="请输入手机号码" maxlength="11">
            </li>
            <li>
                <input id="code" type="tel" placeholder="请输入验证码" maxlength="6">
                <label class="code_btn" onclick="sendCode(this)">获取验证码</label>
                <label style="display: none;" id="timer" class="code_btn">获取验证码</label>
            </li>
        </ul>
        <div class="bottom_btn">
            <img id="btn_y" style="display:none " src="img/btn.png" alt="" onclick="signUp()">
            <img id="btn_n" style="display:block" src="img/btn_gray.png" alt="">
        </div>

        <div class="bottom_btn">tips:加油88折优惠活动仅限平安车主信用卡,进入办卡页面请选择 <a class="text_btn"><img src="img/more.png" alt=""></a>按钮,找到平安车主信用卡,点击进行在线办理
        </div>
        <div class="bottom_logo">
            <img src="img/logo.png" alt="">
        </div>
    </div>
</div>
</body>
</html>