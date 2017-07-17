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
    <link type="text/css" href="${contentPath}/jsp/weixinMng/liveMng/css/common/common.css" rel="stylesheet">
    <link type="text/css" href="${contentPath}/jsp/weixinMng/liveMng/css/app.css" rel="stylesheet">
    <script type="text/javascript" src="${contentPath}/jsp/weixinMng/liveMng/js/common/jQuery-1.11.3.js"></script>
    <script type="text/javascript" src="${contentPath}/jsp/weixinMng/liveMng/js/common/jWeChat-Adaptive.js"></script>
    <script type="text/javascript" src="${contentPath}/jsp/weixinMng/liveMng/js/common/m.tool.juxinbox.com.js"></script>
    <script type="text/javascript" src="${contentPath}/jsp/weixinMng/liveMng/js/common/common.js"></script>
</head>

<script>
	function handle() {
	var inputVal = $('#usercardid').val(),
	trimPhoneNum = inputVal.replace(/\s+/g, ''),
	len = trimPhoneNum.length;
	if (len < 4) {
	var value1 = trimPhoneNum.replace(/\s+/g, '');
	$('#usercardid').val(value1);
	} else if (len >= 4 && len < 7) {
	$('#usercardid').val(trimPhoneNum.replace(/(\d{3})(\d+)/g, '$1 $2'));
	} else if (len >= 7 && len < 11) {
	$('#usercardid').val(trimPhoneNum.replace(/(\d{3})(\d{3})(\d+)/g, '$1 $2 $3'));
	} else if (len == 11) {
	$('#usercardid').val(trimPhoneNum.replace(/(\d{3})(\d{3})(\d{4})/g, '$1 $2 $3'));
	} else if (trimPhoneNum.length > 11) {
	return false;
	}
	}
</script>

<body style="background-color: #f0f0f4">
<div style="background-color: #f0f0f4;position:fixed;" class="zoomer">
    <div class="html_top">
        <img src="${contentPath}/jsp/weixinMng/liveMng/img/left_arrow.png" alt="" onclick="gobak()">
        修改账户
    </div>
    <div class="switchAccount">
    <input type="hidden" id="payModeId" value=""/>
    <input type="hidden" id="payModeName" value=""/>
    <input type="hidden" id="productId" value=""/>
    <input type="hidden" id="productName" value=""/>
	<div style="position:relative;">
            <span class="switchAccount_tittle">充值对象</span>
            <select  onChange="sech(this);" name="" id="danweiid" class="switchAccount_object">
                 <option value="-1" selected>选择对象</option>
	             <c:forEach var="obj" items="${PAYUNITLIST}" varStatus="status">
	                   <option PROVINCEID="${obj.PAYPROJECT03MODE.PROVINCEID}" 
								PROVINCENAME="${obj.PAYPROJECT03MODE.PROVINCENAME}" 
								CITYID="${obj.PAYPROJECT03MODE.CITYID}" 
								CITYNAME="${obj.PAYPROJECT03MODE.CITYNAME}" 
								PAYPROJECTID="${obj.PAYPROJECT03MODE.PAYPROJECTID}" 
								PAYPROJECTNAME="${obj.PAYPROJECT03MODE.PAYPROJECTNAME}" 
								PAYUNITID="${obj.PAYUNITID}" 
								PAYUNITNAME="${obj.PAYUNITNAME}"
								value="1"  >
								${obj.PAYUNITNAME }
					   </option>
	             </c:forEach>
            </select>
	<img src="${contentPath}/jsp/weixinMng/liveMng/img/down_arrow.png" alt="" class="down_arrow_forAccount">
        </div>
        <div class="switchAccount_tip3">请输入您的账户号码</div>
		<div><input id="usercardid" type="tel" onkeyup="handle()" onpropertychange="OnPropChanged(event)"></div>
        <%--<nav class="switchAccount_tip2">未查询到账户号</nav>--%>
    </div>
    <div class="switchAccount_save" onclick="getBalance()">保存</div>
</div>
</body>
<script type="text/javascript">

function sech(dom) {//省市改变时触发，select的onchange事件

	var value=$(dom).val();
	if(-1==value){
		return;
	}
	var PROVINCEID=$(dom).find("option:selected").attr("PROVINCEID");
	var PROVINCENAME=$(dom).find("option:selected").attr("PROVINCENAME");
	var CITYID=$(dom).find("option:selected").attr("CITYID");
	var CITYNAME=$(dom).find("option:selected").attr("CITYNAME");
	var PAYPROJECTID=$(dom).find("option:selected").attr("PAYPROJECTID");
	var PAYPROJECTNAME=$(dom).find("option:selected").attr("PAYPROJECTNAME");
	var PAYUNITID=$(dom).find("option:selected").attr("PAYUNITID");
    var PAYUNITNAME=$(dom).find("option:selected").attr("PAYUNITNAME");
	
	// 取得缴费方式
	getPayMode(PROVINCEID,PROVINCENAME,CITYID,CITYNAME,PAYPROJECTID,PAYPROJECTNAME,PAYUNITID,PAYUNITNAME);

}

//取得缴费方式
function getPayMode(PROVINCEID,PROVINCENAME,CITYID,CITYNAME,PAYPROJECTID,PAYPROJECTNAME,PAYUNITID,PAYUNITNAME) {

	$.ajax({
		type : 'post',
		url : 'getPayMode.htm',
		data:{'PROVINCEID':PROVINCEID,
			'PROVINCENAME':PROVINCENAME,
			'CITYID':CITYID,
			'CITYNAME':CITYNAME,
			'PAYPROJECTID':PAYPROJECTID,
			'PAYPROJECTNAME':PAYPROJECTNAME,
			'PAYUNITID':PAYUNITID,
			'PAYUNITNAME':PAYUNITNAME
		},
		dataType : 'json',
        beforeSend: function () {
            loading("start");
        },
        complete: function (XMLHttpRequest) {
            loading("stop");
        },
		success : function(data, textStatus) {
          	if(-1==data.timeout){
          		// session已经过期了
          		//window.location="list.htm"; 
          		alert("ajax-timeout");
          		return;
          	}
			if(1==data.code){
				$("#payModeId").val(data.resultObject.PAYMODEID);
				//alert($("#payModeId").val());
			}else{
				alert(data.msg);
			}
		},
        //调用出错执行的函数
        error: function(XMLHttpRequest, textStatus, errorThrown){
              //请求出错处理
            alert("系统异常!"+textStatus);
        } 
	});
}

// 公共事业缴费方式查询 和  公共事业商品
function getPayMode(PROVINCEID,PROVINCENAME,CITYID,CITYNAME,PAYPROJECTID,PAYPROJECTNAME,PAYUNITID,PAYUNITNAME) {

	$.ajax({
		type : 'post',
		url : 'getPayMode.htm',
		data:{'PROVINCEID':PROVINCEID,
			'PROVINCENAME':PROVINCENAME,
			'CITYID':CITYID,
			'CITYNAME':CITYNAME,
			'PAYPROJECTID':PAYPROJECTID,
			'PAYPROJECTNAME':PAYPROJECTNAME,
			'PAYUNITID':PAYUNITID,
			'PAYUNITNAME':PAYUNITNAME
		},
		dataType : 'json',
        beforeSend: function () {
            loading("start");
        },
        complete: function (XMLHttpRequest) {
            loading("stop");
        },
		success : function(data, textStatus) {
          	if(-1==data.timeout){
          		// session已经过期了
          		//window.location="list.htm"; 
          		alert("ajax-timeout");
          		return;
          	}
			if(1==data.code){
				$("#payModeId").val(data.resultObject.PAYMODEID);
				$("#productId").val(data.resultObject.PRODUCTID);
				$("#payModeName").val(data.resultObject.PAYMODENAME);
				$("#productName").val(data.resultObject.PRODUCTNAME);
				
				//alert($("#payModeId").val());
				//alert($("#productId").val());
				
			}else{
				alert(data.msg);
			}
		},
        //调用出错执行的函数
        error: function(XMLHttpRequest, textStatus, errorThrown){
              //请求出错处理
            alert("系统异常!"+textStatus);
        } 
	});
}

//公共事业账单查询接口
function getBalance() {
	var value=$("#danweiid").val();
	if(-1==value){
		alerw("请选择充值对象!");
		return;
	}
	var dom =$("#danweiid");
	var PROVINCEID=$(dom).find("option:selected").attr("PROVINCEID");
	var PROVINCENAME=$(dom).find("option:selected").attr("PROVINCENAME");
	var CITYID=$(dom).find("option:selected").attr("CITYID");
	var CITYNAME=$(dom).find("option:selected").attr("CITYNAME");
	var PAYPROJECTID=$(dom).find("option:selected").attr("PAYPROJECTID");
	var PAYPROJECTNAME=$(dom).find("option:selected").attr("PAYPROJECTNAME");
	var PAYUNITID=$(dom).find("option:selected").attr("PAYUNITID");
    var PAYUNITNAME=$(dom).find("option:selected").attr("PAYUNITNAME");
    var PAYMODEID=$("#payModeId").val();
    var PAYMODENAME=$("#payModeName").val();
    var PRODUCTID=$("#productId").val();
    var PRODUCTNAME=$("#productName").val();
    
    var USERCARDID=$("#usercardid").val().replace(/\s/ig,'');
	if(jxTool.isNull(USERCARDID)){
		alerw("请输入卡号!");
		return;
	}
	
	$.ajax({
		type : 'post',
		url : 'getBalance.htm',
		data:{'PROVINCEID':PROVINCEID,
			'PROVINCENAME':PROVINCENAME,
			'CITYID':CITYID,
			'CITYNAME':CITYNAME,
			'PAYPROJECTID':PAYPROJECTID,
			'PAYPROJECTNAME':PAYPROJECTNAME,
			'PAYUNITID':PAYUNITID,
			'PAYUNITNAME':PAYUNITNAME,
			'PAYMODEID':PAYMODEID,
			'PRODUCTID':PRODUCTID,
			'USERCARDID':USERCARDID,
			'ADDTYPE':'${param.ADDTYPE}'
		},
		dataType : 'json',
        beforeSend: function () {
            loading("start");
        },
        complete: function (XMLHttpRequest) {
            loading("stop");
        },
		success : function(data, textStatus) {
          	if(-1==data.timeout){
          		// session已经过期了
          		//window.location="list.htm"; 
          		alert("ajax-timeout");
          		return;
          	}
			if(1==data.code){
				$(".switchAccount_tip2").html(data.resultObject.USEADDRESS);
				saveUser();
			}else if(2==data.code){
				$(".switchAccount_tip2").html("");
				saveUser();
			}else{
				alert(data.msg);
			}
		},
        //调用出错执行的函数
        error: function(XMLHttpRequest, textStatus, errorThrown){
              //请求出错处理
            alert("系统异常!"+textStatus);
        } 
	});
}

//保存绑定的信息
function saveUser() {
	var value=$("#danweiid").val();
	if(-1==value){
		alerw("请选择充值对象!");
		return;
	}
	var dom =$("#danweiid");
	var PROVINCEID=$(dom).find("option:selected").attr("PROVINCEID");
	var PROVINCENAME=$(dom).find("option:selected").attr("PROVINCENAME");
	var CITYID=$(dom).find("option:selected").attr("CITYID");
	var CITYNAME=$(dom).find("option:selected").attr("CITYNAME");
	var PAYPROJECTID=$(dom).find("option:selected").attr("PAYPROJECTID");
	var PAYPROJECTNAME=$(dom).find("option:selected").attr("PAYPROJECTNAME");
	var PAYUNITID=$(dom).find("option:selected").attr("PAYUNITID");
    var PAYUNITNAME=$(dom).find("option:selected").attr("PAYUNITNAME");
    var PAYMODEID=$("#payModeId").val();
    var PAYMODENAME=$("#payModeName").val();
    var PRODUCTID=$("#productId").val();
    var PRODUCTNAME=$("#productName").val();
    var USERNAME= $(".switchAccount_tip2").html();
    
    var USERCARDID=$("#usercardid").val();
	if(jxTool.isNull(USERCARDID)){
		alerw("请输入卡号!");
		return;
	}
	
	$.ajax({
		type : 'post',
		url : 'saveUser.htm',
		data:{'PROVINCEID':PROVINCEID,
			'PROVINCENAME':PROVINCENAME,
			'CITYID':CITYID,
			'CITYNAME':CITYNAME,
			'PAYPROJECTID':PAYPROJECTID,
			'PAYPROJECTNAME':PAYPROJECTNAME,
			'PAYUNITID':PAYUNITID,
			'PAYUNITNAME':PAYUNITNAME,
			'PAYMODEID':PAYMODEID,
			'PAYMODENAME':PAYMODENAME,
			'PRODUCTID':PRODUCTID,
			'PRODUCTNAME':PRODUCTNAME,
			'USERCARDID':USERCARDID,
			'USERNAME':USERNAME,
			'ADDTYPE':'${param.ADDTYPE}'
		},
		dataType : 'json',
        beforeSend: function () {
            loading("start");
        },
        complete: function (XMLHttpRequest) {
            loading("stop");
        },
		success : function(data, textStatus) {
          	if(-1==data.timeout){
          		// session已经过期了
          		//window.location="list.htm"; 
          		alert("ajax-timeout");
          		return;
          	}
			if(1==data.code){
				alerw("绑定成功!", "", function(){
					window.location="index.htm";
				});
			}else{
				alert(data.msg);
			}
		},
        //调用出错执行的函数
        error: function(XMLHttpRequest, textStatus, errorThrown){
              //请求出错处理
            alert("系统异常!"+textStatus);
        } 
	});
}

//退到首页
function gobak(){
   window.location="index.htm";	
}

</script>
</html>