$(function(){
	$("#file2").change(function() {
		loading("start");
		//�ϴ����ݵ�������
	    var img = this.files[0];//��ȡͼƬ��Ϣ
        var type = img.type;//��ȡͼƬ���ͣ��ж�ʹ��
        var url = getObjectURL(this.files[0]);//ʹ���Զ��庯������ȡͼƬ����url
        var fd = new FormData();//ʵ���������ύ����ʹ��
        fd.append('file', img);//��img׷�ӽ�ȥ
	
	$.ajax({
		url: getRootPath() + "/recoGoods/uploadImg.htm",
		data:fd,
		type:"post",
        dataType: "json",
		processData: false,  // tell jQuery not to process the data  ������Ǳ���ģ�����ᱨ��
        contentType: false,   // tell jQuery not to set contentType
        dataType: 'text',
		success:function(data){
			loading("stop");
			console.log(data);
			data=JSON.parse(data);
			var url =data.name;
			 $("#goodsImg").val(url);
		},error:function(){
			loading("stop");
			data=JSON.parse(data);
		    alert(data.msg);
		 }
	});
	});	
});


//����һ���ɴ�ȡ����file��url
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


function addGoods(){
	loading("start");
	var name = $("#goodsName").val();
	var imgUrl = $("#goodsImg").val();
	var linkUrl = $("#goodsLink").val();
	var originalCost = $("#goodsOldPrice").val();
	var discountCost = $("#goodsPrice").val();
	
	if(name==""){
		alert("请输入商品名称!");
		return false;
	}
	if(imgUrl==""){
		alert("请选择图片!");
		return false;
	}
	if(linkUrl==""){
		alert("请输入链接地址!");
		return false;
	}
	if(originalCost==""){
		alert("请输入商品原价!");
		return false;
	}
	if(discountCost==""){
		alert("请输入商品折扣价!");
		return false;
	}
	
	
	$.ajax({
		url:getRootPath()+"/recoGoods/addGoods.htm",
		data:{"name":name,"imgUrl":imgUrl,"linkUrl":linkUrl,"originalCost":originalCost,"discountCost":discountCost},
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

