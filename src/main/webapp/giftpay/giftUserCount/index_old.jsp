<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2017/6/19
  Time: 16:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>

<script type="text/javascript" src="js/jsAdd/common/jQuery-1.11.3.js"></script>
<script type="text/javascript" >
    <%
        String phone = request.getParameter("phone");
    %>

    var phone = '<%=phone%>';

    $(function (){
        $("#phone").text(phone);
    })

</script>

<body>
        <h3>index 加载中....</h3>

        <h4 id="phone">用户手机号码：无</h4>

</body>
</html>
