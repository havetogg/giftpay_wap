<!DOCTYPE html>
<%@ page language="java" contentType="text/html;charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
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
    <link type="text/css" href="${contentPath}/jsp/weixinMng/mallMng/css/common/common.css" rel="stylesheet">
    <link type="text/css" href="${contentPath}/jsp/weixinMng/mallMng/css/app.css" rel="stylesheet">
    <script type="text/javascript" src="${contentPath}/jsp/weixinMng/mallMng/js/common/jQuery-1.11.3.js"></script>
    <script type="text/javascript" src="${contentPath}/jsp/weixinMng/mallMng/js/common/common.js"></script>
    <script type="text/javascript" src="${contentPath}/jsp/weixinMng/mallMng/js/index.js"></script>
    <script type="text/javascript" src="${contentPath}/jsp/weixinMng/mallMng/js/common/jWeChat-Adaptive.js"></script>
       <script type="text/javascript" src="${contentPath}/jsp/weixinMng/mallMng/js/common/m.tool.juxinbox.com.js"></script>
    <script type="text/javascript" src="${contentPath}/jsp/weixinMng/mallMng/js/fastclick.min.js"></script>
<script type="text/javascript">
    function gotopay(order_no){
    	confirw("继续支付款?", "", "", "", 
    			function(){
    		       // OK
    		        window.location="gotoPay.htm?ORDER_NO="+order_no;
    	        }, 
    	        function(){
    	        	// NG
    	        });
    }
</script>
</head>
<body>
<div class="zoomer">
    <div class="tab_text">
        <img src="${contentPath}/jsp/weixinMng/mallMng/img/arrowLeft.png" alt="" class="arrowLeft">
        <div>充值记录</div>
    </div>
    <div class="rechargeRecords_Content">
         <div class="recordLists_time"></div>
         <div class="recordLists">
             <ul>
                 <c:forEach var="obj" items="${CHANGELIST}" varStatus="status">
                 
                     <c:choose> 
			            <c:when test="${obj.STATE==0}">
                           <li class="flex" onclick="gotopay('${obj.ORDER_NO}')">
                        </c:when> 
			            <c:otherwise>
                            <li class="flex">
                        </c:otherwise> 
				     </c:choose>
	                 <c:choose> 
			            <c:when test="${obj.PRODUCT_TYPE==1 || obj.PRODUCT_TYPE==2}">
<!-- 			            话费充值 -->
                          <c:choose> 
				            <c:when test="${obj.PHONE_TYPE==1}">
	                         <!-- 			            话费充值(移动) -->
	                         <div><img src="${contentPath}/jsp/weixinMng/mallMng/img/yidong.png" alt=""></div>
	                        </c:when> 
				            <c:when test="${obj.PHONE_TYPE==2}">
	                         <!-- 			            话费充值(联通) -->
	                         <div><img src="${contentPath}/jsp/weixinMng/mallMng/img/liantong.png" alt=""></div>
	                        </c:when> 
				            <c:otherwise>
	                         <!-- 			            话费充值(电信) -->
	                         <div><img src="${contentPath}/jsp/weixinMng/mallMng/img/zgdx.png" alt=""></div>
				            </c:otherwise> 
					     </c:choose>
                        </c:when> 

			            <c:otherwise>
<!-- 			            加油卡充值 -->
                          <c:choose> 
				            <c:when test="${obj.GAS_TYPE==1}">
	                         <!-- 			            中国石化 -->
	                           <div><img src="${contentPath}/jsp/weixinMng/mallMng/img/zsh.png" alt=""></div>
	                        </c:when> 
				            <c:otherwise>
	                         <!-- 			            中国石油 -->
                               <div><img src="${contentPath}/jsp/weixinMng/mallMng/img/zsy.png" alt=""></div>
				            </c:otherwise> 
					     </c:choose>

			            </c:otherwise> 
				     </c:choose>
                     <div class="flex-1 records_content">
                         <div class="flex"><a class="flex-1 records_money">￥${obj.ORDER_PRICE}</a><a class="flex-1 records_time">${obj.CREATE_TIME_HIS}</a></div>
                           <c:choose> 
				            <c:when test="${obj.STATE==-1}">
	                           <div class="flex"><a class="flex-1 records_text">${obj.PRODUCT_NAME}-${obj.PHONE}</a><a class="records_status1">已取消 </a></div>
	                        </c:when> 
				            <c:when test="${obj.STATE==0}">
	                           <div class="flex"><a class="flex-1 records_text">${obj.PRODUCT_NAME}-${obj.PHONE}</a><a class="records_status1">未付款 </a></div>
	                        </c:when> 
				            <c:when test="${obj.STATE==1}">
	                           <div class="flex"><a class="flex-1 records_text">${obj.PRODUCT_NAME}-${obj.PHONE}</a><a class="records_status1">已付款 </a></div>
	                        </c:when> 
				            <c:when test="${obj.STATE==2}">
	                           <div class="flex"><a class="flex-1 records_text">${obj.PRODUCT_NAME}-${obj.PHONE}</a><a class="records_status1">充值中</a></div>
	                        </c:when> 
				            <c:when test="${obj.STATE==3}">
	                           <div class="flex"><a class="flex-1 records_text">${obj.PRODUCT_NAME}-${obj.PHONE}</a><a class="records_status1">充值完成</a></div>
	                        </c:when> 
				            <c:when test="${obj.STATE==4}">
	                           <div class="flex"><a class="flex-1 records_text">${obj.PRODUCT_NAME}-${obj.PHONE}</a><a class="records_status1">充值失败</a></div>
	                        </c:when> 
				            <c:when test="${obj.STATE==5}">
	                           <div class="flex"><a class="flex-1 records_text">${obj.PRODUCT_NAME}-${obj.PHONE}</a><a class="records_status1">已退款</a></div>
	                        </c:when> 
					     </c:choose>
                    
                     </div>
                 </li>
                </c:forEach>

    </div>
</div>
</body>
</html>