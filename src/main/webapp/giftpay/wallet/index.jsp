<%--
  Created by IntelliJ IDEA.
  User: YuanYu
  Date: 15/12/28
  Time: 下午10:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>微信支付首页</title>
    <script type="text/javascript" src="../js/jQuery-1.11.3.js"></script>
    <script type="text/javascript" src="../js/jWeChat-Adaptive.js"></script>
    <script type="text/javascript" src="../js/jWeChat-1.0.0.js"></script>
    <script type="text/javascript">
        $(function() {



            $("#launchPay").click(function(){
                $.ajax({
                    url:'../test/launchPay.htm',
                    dataType:'json',
                    success:function(data){
                        window.location = data.payUrl;
                    }
                });
            });


        });
    </script>
</head>
<body>
<input id="launchPay" style="width: 500px;height: 500px;" type="button" value="首页,点击触发支付功能!">
</body>
</html>
