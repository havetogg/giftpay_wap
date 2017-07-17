$(function(){
	$.ajax({
		url:getRootPath()+"/recoGoods/queryList.htm",
		dataType:"json",
		type:"post",
		success:function(res){
			console.log(res.data);
			if(res.code=="1"){
			//无推荐商品
				$(".goodsLists").hide();
			}else{
				$(".goodsLists").show();
				$(".goodsLists").html("");
				var str="";
				$.each(res.data,function(i,item){
					str+="<li><div onclick=\"jump('"+item.linkUrl+"','"+item.id+"')\">";
					str+="<img src='"+item.imgUrl+"' alt=''>";
					str+="</div><div class='goodsLists_info'>";
					str+="<div class='goodsLists_name'>"+item.name+"</div>";
					str+="<div class='goodsLists_price'><a class='price'>￥"+item.discountCost+"</a><label>" +
							"原价"+item.originalCost+"</label><a class='sale_bg'>"+item.discount+"折</a></div>";
				});
				$(".goodsLists").append(str);
			}
		}
	});
});


function jump(url,id){
	loading("start");
	$.ajax({
		url:getRootPath()+"/recoGoods/addClick.htm",
		data:{"id":id},
		dataType:"json",
		type:"post",
		success:function(res){
			loading("stop");
			console.log(res);
			if(res.code=="0"){
				window.location.href = url;
			}else{
				alert(res.msg);
			}
		}
		
	});
}