package org.jumutang.giftpay.web.recoGoods;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.jumutang.giftpay.base.web.controller.BaseController;
import org.jumutang.giftpay.model.RecoGoodsModel;
import org.jumutang.giftpay.tools.UploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.sf.json.JSONObject;

/**
 * 添加支付完成页商品列表
 * @author oywj
 *
 * @Date 2017年7月6日 下午4:16:04
 */
@Controller
@RequestMapping(value = "/recoGoods" , method ={RequestMethod.GET , RequestMethod.POST})
public class RecoGoodsController extends BaseController{
	
	Logger logger = Logger.getLogger(RecoGoodsController.class);
	 
	@ResponseBody
	@RequestMapping(value = "/addGoods" ,method = {RequestMethod.GET,RequestMethod.POST})
	public String addGoods(HttpServletRequest request,HttpServletResponse response){
		String name = request.getParameter("name");
		String imgUrl = request.getParameter("imgUrl");
		String linkUrl = request.getParameter("linkUrl");
		String originalCost = request.getParameter("originalCost");
		String discountCost = request.getParameter("discountCost");
		
		double dou = Double.valueOf(discountCost)/Double.valueOf(originalCost);
		BigDecimal   b   =   new   BigDecimal(dou); 
		dou   =   b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();  
		String discount  = (dou+"").substring(2,  (dou+"").length()) ;
		
		logger.info("添加一条新数据，参数为：name="+name+",imgUrl="+imgUrl+",linkUrl="+linkUrl+",originalCost="+originalCost
				   +",discountCost="+discountCost+",discount="+discount);
		
		RecoGoodsModel goodsModel =new RecoGoodsModel();
		goodsModel.setName(name);
		goodsModel.setImgUrl(imgUrl);
		goodsModel.setLinkUrl(linkUrl);
		goodsModel.setOriginalCost(originalCost);
		goodsModel.setDiscountCost(discountCost);
		goodsModel.setDiscount(discount);
		int flag = this.recoGoodsService.addGoods(goodsModel);
		JSONObject object =new JSONObject();
		if(flag==0){
			object.put("code", "1");
			object.put("msg", "添加商品失败!");
			logger.info("添加商品失败");
		}else{
			object.put("code", "0");
			object.put("msg", "添加商品成功!");
			logger.info("添加商品成功");
		}
		return object.toString();
	}
	
	
	/**
     * 上传图片
     * @param req
     * @param res
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/uploadImg",method = {RequestMethod.GET,RequestMethod.POST})
    public void uploadImg(HttpServletRequest req, HttpServletResponse res) throws Exception {
          //String uploadPath="C:/Users/Administrator/Desktop/proj1/11.rar";//本地
          String uploadPath="C:\\apache-tomcat-7.0.63\\servers\\img\\";//正式
         // String uploadPath="D:\\apache-tomcat-7.0.69\\apache-tomcat-7.0.69\\webapps\\img\\";//测试
        UploadUtil.uploadFile(req,res,uploadPath);    
    }
    

     public static void main(String[] args) {
    	double dou = Double.valueOf("30")/Double.valueOf("48");
 		BigDecimal   b   =   new   BigDecimal(dou); 
 		dou   =   b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();  
 		String discount  = (dou+"").substring(2,  (dou+"").length()) ;
 		System.out.println(discount);
	}
     
     @ResponseBody
     @RequestMapping(value = "/queryList",method = {RequestMethod.GET,RequestMethod.POST})
     public String queryGoodsList(HttpServletRequest request,HttpServletResponse response){
    	 List<RecoGoodsModel> list = this.recoGoodsService.queryGoodsList();
    	 JSONObject object = new JSONObject();
    	 if(list.size()==0){
    		 object.put("code", "1");
    		 object.put("msg", "没有推荐商品");
    		 logger.info("没有推荐商品");
    		 return object.toString();
    	 }else{
    		 object.put("code", "0");
    		 object.put("data", list);
    	 }
		return object.toString();
    	 
     }
     
     /**
      * 更新点击量
      * @param request
      * @return
      */
     @ResponseBody
     @RequestMapping(value = "/addClick" ,method = {RequestMethod.POST,RequestMethod.GET})
     public String addClick(HttpServletRequest request){
    	 int id = Integer.valueOf(request.getParameter("id"));
    	 RecoGoodsModel  goodsModel = new RecoGoodsModel();
    	 goodsModel.setId(id);
    	 int flag = this.recoGoodsService.updateClickNum(goodsModel);
    	 JSONObject object = new JSONObject();
    	 if(flag==1){
    		 object.put("code", "0");
    		 object.put("msg", "操作成功!");
    	 }else{
    		 object.put("code", "0");
    		 object.put("msg", "业务失败!");
    	 }
		return object.toString();
    	 
     }
}
