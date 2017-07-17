<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="initial-scale=0.5,maximum-scale=0.5,minimum-scale=0.5, width=640, target-densitydpi=device-dpi">
    <meta http-eqiv="X-UA-Compatible" content="IE=Edge,chrome=1">
    <meta content="yes" name="apple-mobile-web-app-capable">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <title>充值记录</title>
    <link type="text/css" href="css/common/common.css" rel="stylesheet">
    <link type="text/css" href="css/app.css" rel="stylesheet">
    <script type="text/javascript" src="js/common/jQuery-1.11.3.js"></script>
    <script type="text/javascript" src="js/common/common.js"></script>
    <script type="text/javascript" src="js/index.js"></script>
    <script type="text/javascript" src="js/common/jWeChat-Adaptive.js"></script>
    <script type="text/javascript" src="js/fastclick.min.js"></script>
</head>
<body>

<script>



    var myDate = new Date();
    var type = "<%=request.getParameter("type")%>";

    $(function (){


//        //获取Url地址传递的数据
//        function  getUrlParam(name){
//            var   reg   =   new   RegExp("(^|&)"+   name   +"=([^&]*)(&|$)");
//            var   r   =   window.location.search.substr(1).match(reg);
//            if   (r!=null)   return   unescape(r[2]);   return   null;
//        }
//        console.log(getUrlParam("type"));



        //当前年份 格式 2017
        var year = new Date().getFullYear();
        //当前月份  格式  1
        var month= new Date().getMonth()>10?new Date().getMonth():"0"+(new Date().getMonth()+1);

        //时间处理[去掉时间数字前的'0']
        function TimeDateForm (data){
            //充值记录时间
            var order_time = String(data)
            var time = order_time.substr(order_time.indexOf("-")+1,11)
            //月+日  例如:4-21
            var time_front = parseInt(time.substr(0,time.indexOf("-")));
            var time_last =parseInt(time.substr(time.indexOf("-")+1,time.indexOf(" ")));

            //时间 例如:9:20
            var time_fin = parseInt(time.substr(time.indexOf(" "),3));
            var time_fin2 =  time.substr(time.indexOf(" ")+3);

            //拼接组成 newTime
            var FinTime = time_front+"-"+time_last+"  "+time_fin+time_fin2;

            return FinTime;
        }

        $.ajax({
            url: getRootPath()+"/flow/getFlowOrderRecord_2.htm",  //服务器使用
//             url: "/flow/getFlowOrderRecord_2.htm",
            type: 'POST',
            async:false,
            data:{"type":type},
            dataType: 'json',
            success:function(data){
             if(data=="none"){
            	 $("#jmt_record").show();
            	 $(".rechargeRecords_Content").hide();
             }else{
            	 $("#jmt_record").hide();
            	 //获取data的长度大小
                 for(var i in data ){

                     //获得系统当前 时间
                     var nowMonth =   myDate.getMonth()+1;
                     var nowYear = myDate.getFullYear();

                     //月份[数据库取得]
                     var getMonth = String(i);
                     var year = parseInt(getMonth.substr(0,getMonth.indexOf("-")));
                     var month =parseInt(getMonth.substr(getMonth.indexOf("-")+1));

                     //判断是否当前年份
                     if(year==nowYear){
                         //判断是否是当前月份
                         if(month==nowMonth){
                             month="本月";
                         }else{
                             month=month+"月";
                         }
                     }else{
                         month=year+"年 "+month+"月";
                     }

                     var tr_1 = "<div class='recordLists_time'>"+month+"</div><div class='recordLists'><ul>";

                     //for循环 打印tr标签
                     var tr_2 ="";
                     for(var o in data[i]){
                         //console.log("log2:---"+data[i][o].dealTime);

                         //根据运营商 显示图标
                         var img = "";
                         if(String(data[i][o].goodsName).indexOf("移动")>0){
                             img="yidong.png";
                         }else if (String(data[i][o].goodsName).indexOf("联通")>0){
                             img="liantong.png";
                         }else{
                             img="zgdx.png";
                         }

                         //充值状态
                         var status = data[i][o].dealState;
                         if(status==1){
                        	 status="已成功";
                         }else if(status==0){
                        	 status="充值中";
                         }else{
                        	 status="已退款";
                         }
                         //充值状态样式
                         var css_status = data[i][o].dealState==1?"records_status2":"records_status1";

                         //时间 处理
                         var FinTime = String(data[i][o].createTime).substr(String(data[i][o].createTime).indexOf("-")+1,11);

                          tr_2+="  <li class='flex'>"+
                         "   <div><img src='img/"+img+"'  ></div>"+
                         "   <div class='flex-1 records_content'>"+
                         "   <div class='flex'><a class='flex-1 records_money'>￥"+data[i][o].goodsMoney+"</a><a class='flex-1 records_time'>"+FinTime+"</a></div>"+
                         "   <div class='flex'><a class='flex-1 records_text'>"+data[i][o].goodsName+"-"+data[i][o].remark+"</a><a class='"+css_status+"'> "+status+"</a></div>"+
                         "   </div></li>";

                     }

                     var tr_3 = " </ul> </div>";

                     //拼接生成新tr
                     var tr = $(tr_1+tr_2+tr_3);

                     //打印输出
                     $(".rechargeRecords_Content").append(tr);
                 }
             }
            }
        });

    })
    
    function getCatName(num){
	//判断运营商
    if(num.indexOf("移动")>0){
        return "yidong"; //移动
    }else if(num.indexOf("电信")>0){
    	return "dianxin"; //电信
    }else{
    	return "liantong"; //联通
    }
}


</script>

<div class="zoomer">
     <div class="jmt_center" id="jmt_record"  style="margin: 20%;">
        <img src="img/noRecords.png" alt="">
        <div class="noRecords_text">暂无记录</div>
    </div>
    <div class="rechargeRecords_Content">


    </div>
</div>
</body>
</html>