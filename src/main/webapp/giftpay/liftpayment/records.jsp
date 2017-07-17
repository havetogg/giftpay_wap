<!DOCTYPE html>
<%@ page language="java" contentType="text/html;charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib  prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<html>
<head>
	<c:set var="contentPath" value="<%=request.getContextPath()%>"></c:set>
    <meta charset="UTF-8">
    <meta name="viewport" content="initial-scale=0.5,maximum-scale=0.5,minimum-scale=0.5, width=640, target-densitydpi=device-dpi">
    <meta http-eqiv="X-UA-Compatible" content="IE=Edge,chrome=1">
    <meta content="yes" name="apple-mobile-web-app-capable">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <title>有礼付</title>
    <link type="text/css" href="${contentPath}/jsp/weixinMng/liveMng/css/common/common.css" rel="stylesheet">
    <link type="text/css" href="${contentPath}/jsp/weixinMng/liveMng/css/app.css" rel="stylesheet">
    <script type="text/javascript" src="${contentPath}/jsp/weixinMng/liveMng/js/common/jQuery-1.11.3.js"></script>
    <script type="text/javascript" src="${contentPath}/jsp/weixinMng/liveMng/js/common/jWeChat-Adaptive.js"></script>
    <script type="text/javascript" src="${contentPath}/jsp/weixinMng/liveMng/js/common/m.tool.juxinbox.com.js"></script>
    <script type="text/javascript" src="${contentPath}/jsp/weixinMng/liveMng/js/common/common.js"></script>
</head>

<script>
function return_tip_bg_btnShow() {
    $('.bg_block1').show();
}
function return_tip_bg_btn() {
    $('.bg_block1').hide();
}
</script>
<script type="text/javascript">
    function gotopay(order_no){
    	confirw("继续支付款?", "", "", "", 
    			function(){
    		       // OK
    				 window.location="gotoPayLive.htm?ORDER_NO="+order_no;
    	        }, 
    	        function(){
    	        	// NG
    	        });
    }
    
  //退到首页
    function gobak(){
       window.location="index.htm";	
    }
</script>

<body style="background-color: #f0f0f4">
<div class="bg_block1">
    <div class="return_tip_bg">
            <div class="records_Name">退款说明</div>
            <div class="return_tip_bg_content">
                <div>退款将存入有礼付钱包</div>
                <div>请在钱包中提现</div>
            </div>
            <div class="return_tip_bg_btn" onclick="return_tip_bg_btn()">确定</div>
    </div>
</div>
<div style="background-color: #f0f0f4;position:fixed;" class="zoomer">
    <div class="html_top">
        <img src="${contentPath}/jsp/weixinMng/liveMng/img/left_arrow.png" alt="" onclick="gobak()">
        生活缴费
        <label class="return_tip" onclick="return_tip_bg_btnShow()">退款说明</label>
    </div>
        <div class="records_">
          <ul>
          
             <c:forEach var="obj" items="${CHANGELIST}" varStatus="status">
          
	              <li class="flex">
						<c:choose>
						    <c:when test="${'001'==obj.BDUSERCARDMODE.ADDTYPE}">
						        <div><img src="${contentPath}/jsp/weixinMng/liveMng/img/icon2.png" alt=""></div>
						    </c:when>
						    <c:when test="${'002'==obj.BDUSERCARDMODE.ADDTYPE}">
						        <div><img src="${contentPath}/jsp/weixinMng/liveMng/img/icon3.png" alt=""></div>
						    </c:when>
						    <c:otherwise>
						        <div><img src="${contentPath}/jsp/weixinMng/liveMng/img/icon4.png" alt=""></div>
						    </c:otherwise>
						</c:choose>
	                  <div class="flex-1 flex_li_2">
	                      <div><label class="records_Name">实付款￥${obj.BDUSERCARDMODE.NEEDPAYMONEY}</label><label class="records_Time">${fn:substring(obj.CREATE_TIME,0,16)}</label></div>
						<c:choose>
						    <c:when test="${'001'==obj.BDUSERCARDMODE.ADDTYPE}">
						        <div><label class="records_Detail">水费充值${obj.BDUSERCARDMODE.NEEDPAYMONEY}元-${obj.BDUSERCARDMODE.USERCARDID}</label>
						    </c:when>
						    <c:when test="${'002'==obj.BDUSERCARDMODE.ADDTYPE}">
						        <div><label class="records_Detail">电费充值${obj.BDUSERCARDMODE.NEEDPAYMONEY}元-${obj.BDUSERCARDMODE.USERCARDID}</label>
						    </c:when>
						    <c:otherwise>
						       <div><label class="records_Detail">燃气充值${obj.BDUSERCARDMODE.NEEDPAYMONEY}元-${obj.BDUSERCARDMODE.USERCARDID}</label>
						    </c:otherwise>
						</c:choose>
						 <c:choose> 
				            <c:when test="${obj.STATE==-1}">
	                           <label class="records_status1">已取消</label></div>
	                        </c:when> 
				            <c:when test="${obj.STATE==0}">
	                           <label class="records_status1" onclick="gotopay('${obj.ORDER_NO}')">未付款</label></div>
	                        </c:when> 
				            <c:when test="${obj.STATE==1}">
	                           <label class="records_status1">已付款</label></div>
	                        </c:when> 
				            <c:when test="${obj.STATE==2}">
	                           <label class="records_status2">充值中</label></div>
	                        </c:when> 
				            <c:when test="${obj.STATE==3}">
	                           <label class="records_status1">缴费完成</label></div>
	                        </c:when> 
				            <c:when test="${obj.STATE==4}">
	                           <label class="records_status1">缴费失败</label></div>
	                        </c:when> 
				            <c:when test="${obj.STATE==5}">
	                           <label class="records_status">已退款</label></div>
	                        </c:when> 
	                  	 </c:choose>
	                  </div>
	              </li>
              </c:forEach>
              
<%--               <li class="flex">
                  <div><img src="${contentPath}/jsp/weixinMng/liveMng/img/icon3.png" alt=""></div>
                  <div class="flex-1 flex_li_2">
                      <div><label class="records_Name">实付款￥100</label><label class="records_Time">2017-11-23  01:06</label></div>
                      <div><label class="records_Detail">电费充值100元-1234567891</label><label class="records_status2">充值中</label></div>
                  </div>
              </li>
              <li class="flex">
                  <div><img src="${contentPath}/jsp/weixinMng/liveMng/img/icon4.png" alt=""></div>
                  <div class="flex-1 flex_li_2">
                      <div><label class="records_Name">实付款￥100</label><label class="records_Time">2017-11-23  01:06</label></div>
                      <div><label class="records_Detail">燃气充值100元-1234567891</label><label class="records_status">已退款</label></div>
                  </div>
              </li> --%>
          </ul>
        </div>
    </div>
</div>
</body>
</html>