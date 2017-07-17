var arr = [];

function initZshBaseInfo() {
	$.ajax({
		type: "POST",
		url: getRootPath() + "/count/queryZshBaseInfo.htm",
		dataType: "json",
		success: function(data) {
			if(data.code == '0') {
				var mess = JSON.parse(data.mess);
				$.each(mess.lists, function(index, item) {
					if(item.payTypeName == '加油加气' && item.payType == '1') {
						$(".orderNum").text(item.dayOrderCount);
						$(".orderMoney").text(item.dayAmount);
					}
					if(item.payTypeName == '非油品购买' && item.payType == '1') {
						$(".buyNum").text(item.dayOrderCount);
						$(".buyMoney").text(item.dayAmount);
					}
					if(item.payTypeName == '线上充值' && item.payType == '2') {
						$(".olOrderNum").text(item.dayOrderCount);
						$(".olOrderMoney").text(item.dayAmount);
					}
					if(item.payTypeName == '非油品购买' && item.payType == '2') {
						$(".olBuyNum").text(item.dayOrderCount);
						$(".olBuyMoney").text(item.dayAmount);
					}
				});
			}
		}
	});
}

function initZshEchart() {
	$.ajax({
		type: "POST",
		url: getRootPath() + "/count/queryZshBaseInfoEchart.htm",
		dataType: "json",
		success: function(data) {
			var timeArr=[];//时间

			var pay_jiayouList=[];
			var pay_feiyouList=[];
			var pay_xianshangListOL=[];
			var pay_feiyouListOL=[];

			var order_jiayouList=[];
			var order_feiyouList=[];
			var order_xianshangListOL=[];
			var order_feiyouListOL=[];

			if(data.code=='0'){
				data=JSON.parse(data.mess);
				arr=data.lists;
				console.log(arr);
				$.each(arr,function(index,items){
					var time=items.orderTime;
					time=time.substring(time.indexOf("-")+1,time.length);
					timeArr.push([index,time]);
					var is15=true;
					if(arr.length>15){
						is15=false;
					}
					$.each(items.dayReportList,function(index2,item){
						if(item.payTypeName == '加油加气' && item.payType == '1') {
							pay_jiayouList.push([index,item.dayAmount/10000]);
							order_jiayouList.push([index,item.dayOrderCount/10000]);
						}
						if(item.payTypeName == '非油品购买' && item.payType == '1') {
							pay_feiyouList.push([index,item.dayAmount/10000]);
							order_feiyouList.push([index,item.dayOrderCount/10000]);
						}
						if(item.payTypeName == '线上充值' && item.payType == '2') {
							pay_xianshangListOL.push([index,item.dayAmount/10000]);
							order_xianshangListOL.push([index,item.dayOrderCount/10000]);
						}
						if(item.payTypeName == '非油品购买' && item.payType == '2') {
							pay_feiyouListOL.push([index,item.dayAmount/10000]);
							order_feiyouListOL.push([index,item.dayOrderCount/10000]);
						}
					})
				})

				var plot =$.plot($("#flot-demo"),
					[
						{data: pay_jiayouList, label: "加油加气"},
						{data: pay_feiyouList, label: "非油品购买(线下)"},
						{data: pay_xianshangListOL, label: "线上充值"},
						{data: pay_feiyouListOL, label: "非油品购买(线上)"}], {
						xaxis: {
							ticks:timeArr
						},
						series: {
							lines: { show: true},
							points: { show: true }
						}
					}
				);
				plot=$.plot($("#flot-demo2"),
					[
						{data: order_jiayouList, label: "加油加气"},
						{data: order_feiyouList, label: "非油品购买(线下)"},
						{data: order_xianshangListOL, label: "线上充值"},
						{data: order_feiyouListOL, label: "非油品购买(线上)"}], {
						xaxis: {
							ticks:timeArr
						},
						series: {
							lines: { show: true},
							points: { show: true }
						}
					}
				);
				initZshTables(arr);
			}
		}
	})
}

function initZshTables(arr) {
	var str='';
	$.each(arr,function(i,it){
		$.each(it.dayReportList,function(index,item){
			if(index%2==0){
				str+='<tr class="odd gradeX">';
			}else{
				str+='<tr class="even gradeC">';
			}
			str+=' <td>'+item.orderTime+'</td>';
			str+='<td>'+item.dayOrderCount+'</td>';
			str+=' <td>'+item.dayAmount+'</td>';
			str+='<td class="center">'+(item.payType=='1'?'线下':'线上')+'</td>';
			str+=' <td class="center">'+item.payTypeName+'</td></tr>';
		})
	})
	$("#example tbody").html(str);
	$('#example').DataTable();
}
$(function() {
	initZshBaseInfo();
	initZshEchart();
})