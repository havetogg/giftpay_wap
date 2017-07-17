package org.jumutang.giftpay.web.advertise;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.jumutang.giftpay.base.web.controller.BaseController;
import org.jumutang.giftpay.model.AdvertiseModel;
import org.jumutang.giftpay.service.IAdvertiseService;
import org.jumutang.giftpay.tools.UploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping(value = "/addAdvertise" , method ={RequestMethod.GET , RequestMethod.POST})
public class AdvertiseController extends BaseController{

	 Logger logger = Logger.getLogger(AdvertiseController.class);
	
	 
	 @ResponseBody
	 @RequestMapping(value = "/add" , method ={RequestMethod.GET , RequestMethod.POST})
	 public String  addAdvertise(HttpServletRequest request,HttpServletResponse response){
	    JSONObject object = new JSONObject();
		String arrOne =  request.getParameter("arrOne");
		String arrTwo =  request.getParameter("arrTwo");
		JSONArray oneArray = JSONArray.fromObject(arrOne);
		JSONArray twoArray = JSONArray.fromObject(arrTwo);
		logger.info("第一条数据的内容为："+oneArray.toString());
		
		AdvertiseModel oneModel = new AdvertiseModel();
		oneModel.setAdvertiseId("1");
		List<AdvertiseModel> list = this.advertiseService.queryAdvertiseList(oneModel);
		if(list.size()>0){
			this.advertiseService.updateAdvertId(3, 1);
		}
		oneModel.setAdvertiseName(oneArray.getString(0));
		oneModel.setAdvertiseImgUrl(oneArray.getString(1));
		oneModel.setAdvertiseHref(oneArray.getString(2));
		oneModel.setRate(oneArray.getString(3));
		int flag = this.advertiseService.addAdvertise(oneModel);
		if(flag==0){
			object.put("code", "1");
			object.put("msg", "业务失败");
			return object.toString();
		}
		
		if(twoArray.size()>0){
			logger.info("第二条数据的内容为："+twoArray.toString());
			AdvertiseModel twoModel = new AdvertiseModel();
			twoModel.setAdvertiseId("2");
			List<AdvertiseModel> list2 = this.advertiseService.queryAdvertiseList(twoModel);
			if(list2.size()>0){
				this.advertiseService.updateAdvertId(3, 2);
			}
			twoModel.setAdvertiseName(twoArray.getString(0));
			twoModel.setAdvertiseImgUrl(twoArray.getString(1));
			twoModel.setAdvertiseHref(twoArray.getString(2));
			twoModel.setRate(twoArray.getString(3));
			int flag2 = this.advertiseService.addAdvertise(twoModel);
			if(flag2==0){
				object.put("code", "2");
				object.put("msg", "操作失败");
				return object.toString();
			}
			
		}
		
		object.put("code", "0");
		object.put("msg", "提交成功!");
		return object.toString();
	 }
	 	 
	 /**
	     * 上传图片
	     * @param req
	     * @param res
	     * @throws Exception
	     */
	    @ResponseBody
	    @RequestMapping(value = "/uploadImg")
	    public void uploadImg(HttpServletRequest req, HttpServletResponse res) throws Exception {
	       //   String uploadPath="C:/Users/Administrator/Desktop/proj1/11.rar";//本地
	          String uploadPath="C:\\apache-tomcat-7.0.63\\servers\\img\\";//正式
	       // String uploadPath="D:\\apache-tomcat-7.0.69\\apache-tomcat-7.0.69\\webapps\\img\\";//测试
	        UploadUtil.uploadFile(req,res,uploadPath);    
	    }
	    
}
