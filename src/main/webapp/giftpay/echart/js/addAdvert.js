$(function(){
	$("#file0").change(function() {
		loading("start");
		//上传数据到服务器
	    var img = this.files[0];//获取图片信息
        var type = img.type;//获取图片类型，判断使用
        var url = getObjectURL(this.files[0]);//使用自定义函数，获取图片本地url
        var fd = new FormData();//实例化表单，提交数据使用
        fd.append('file', img);//将img追加进去
	
	$.ajax({
		url: getRootPath() + "/addAdvertise/uploadImg.htm",
		data:fd,
		type:"post",
        dataType: "json",
		processData: false,  // tell jQuery not to process the data  ，这个是必须的，否则会报错
        contentType: false,   // tell jQuery not to set contentType
        dataType: 'text',
		success:function(data){
			loading("stop");
			console.log(data);
			data=JSON.parse(data);
			var url =data.name;
			 $("#img1").val(url);
		},error:function(){
			loading("stop");
			data=JSON.parse(data);
		    alert(data.msg);
		 }
	});
	});
	
	
	$("#file1").change(function() {
		loading("start");
		//上传数据到服务器
	    var img = this.files[0];//获取图片信息
        var type = img.type;//获取图片类型，判断使用
        var url = getObjectURL(this.files[0]);//使用自定义函数，获取图片本地url
        var fd = new FormData();//实例化表单，提交数据使用
        fd.append('file', img);//将img追加进去
	
	$.ajax({
		url: getRootPath() + "/addAdvertise/uploadImg.htm",
		data:fd,
		type:"post",
        dataType: "json",
		processData: false,  // tell jQuery not to process the data  ，这个是必须的，否则会报错
        contentType: false,   // tell jQuery not to set contentType
        dataType: 'text',
		success:function(data){
			loading("stop");
			console.log(data);
			data=JSON.parse(data);
			var url =data.name;
			 $("#img2").val(url);
		},error:function(){
			loading("stop");
			data=JSON.parse(data);
		    alert(data.msg);
		 }
	});
	});
	
	
	
});


//建立一個可存取到该file的url
function getObjectURL(file) {
	var url = null;
	if (window.createObjectURL != undefined) { // basic
	     url = window.createObjectURL(file);
		} else if (window.URL != undefined) { // mozilla(firefox)
		 url = window.URL.createObjectURL(file);
		} else if (window.webkitURL != undefined) { // webkit or chrome
		 url = window.webkitURL.createObjectURL(file);
		}
		return url;
		}


function addAdvert(){
	loading("start");
	var name1 = $("#name1").val();
	var img1 = $("#img1").val();
	var link1 = $("#link1").val();
	var frequency1 = $("#frequency1").val();
	
	var arr1=new Array();
	arr1.push(name1);
	arr1.push(img1);
	arr1.push(link1);
	arr1.push(frequency1);
	
	
	var name2 = $("#name2").val();
	var img2 = $("#img2").val();
	var link2 = $("#link2").val();
	var frequency2 = $("#frequency2").val();
	
	var arr2 = new Array();
	
	if(name1==""){
		alert("请输入广告1名称!");
		return false;
	}
	if(link1==""){
		alert("请输入广告1链接!");
		return false;
	}
	if(img1==""){
		alert("请输入广告1图片!");
		return false;
	}
	if(frequency1==""){
		alert("请输入广告1概率!");
		return false;
	}
	if(!$(".uls2").is(":hidden")) {
		if(name2==""){
			alert("请输入广告2名称!");
			return false;
		}
		if(img2==""){
			alert("请输入广告2图片!");
			return false;
		}
		if(link2==""){
			alert("请输入广告2链接!");
			return false;
		}
		if(frequency2==""){
			alert("请输入广告2概率!");
			return false;
		}
		
		arr2.push(name2);
		arr2.push(img2);
		arr2.push(link2);
		arr2.push(frequency2);	
    }
	
	var arrData1 = JSON.stringify(arr1);
	var arrData2 = JSON.stringify(arr2);
	
	$.ajax({
		url:getRootPath()+"/addAdvertise/add.htm",
		data:{"arrOne":arrData1,"arrTwo":arrData2},
		dataType:"json",
		type:"post",
		success:function(res){
			loading("stop");
			console.log(res);
			alert(res.msg);
			closeAd();
			location.reload();
		}
	});
	
}