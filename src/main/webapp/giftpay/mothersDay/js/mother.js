//初始化
var objUrl = '';

$(document).ready(function(){
	/*上传图片及预览*/
	$("#file0").change(function(){
		$(this).addClass("hasImg");
		objUrl = getObjectURL(this.files[0]) ;
		console.log("objUrl = "+objUrl) ;
		if (objUrl) {
			$(".img_file").attr("src", objUrl) ;
		}
	});
	
	/*预览头条*/
	$(".preview").on("click",function(){
		var name = $("input.name").val();
		if (name == "") {
			alert("请先填写母亲姓名");
		} else{
			if ($("#file0").hasClass("hasImg")) {
				var index = Math.floor(Math.random() * 3)		//随机选取一个demo显示
				$(".index_bg").hide();
				$(".demo").eq(index).fadeIn();
				$(".name_text").html(name);						//更改demo里面母亲姓名
			} else{
				alert("请先上传照片");
			}
		}
		
	});
	
	/*重新生成*/
	$(".reform").on("click",function(){
		//clearImg();
		$(".demo").hide();
		$(".index_bg").fadeIn();
		$("#file0").removeClass("hasImg");
		objUrl = '';
		$("#file0").val(""); 
	});
	
	/*生成头条*/
	$(".create").on("click",function(){
		//createImg();
		$(".packet").fadeIn().css("display","block");;
		$(".backIndex").fadeIn().css("display","block");;
		$(".ad").fadeIn().css("display","block");;
		$(".create").hide();
		$(".reform").hide();
	})
	
	/*关闭二维码*/
	$(".close").on("click",function(){
		$(".barCode").fadeOut();
	});
	
	/*打开二维码*/
	$(".gift").on("click",function(){
		$(".barCode").fadeIn();
	});
	$(".packet").on("click",function(){
		$(".barCode").fadeIn();
	});
})

//建立一個可存取到該file的url
function getObjectURL(file) {
	var url = null ; 
	if (window.createObjectURL!=undefined) { // basic
		url = window.createObjectURL(file) ;
	} else if (window.URL!=undefined) { // mozilla(firefox)
		url = window.URL.createObjectURL(file) ;
	} else if (window.webkitURL!=undefined) { // webkit or chrome
		url = window.webkitURL.createObjectURL(file) ;
	}
	return url ;
}

//重置上传
function clearImg(){
	$(".demo").hide();
	$(".index_bg").fadeIn();
	$("#file0").removeClass("hasImg");
	objUrl = '';
	$("#file0").val("");  
}

//生成头条
function createImg(){
	$(".packet").fadeIn();
	$(".backIndex").fadeIn();
	$(".ad").fadeIn();
	$(".create").hide();
	$(".reform").hide();
}
