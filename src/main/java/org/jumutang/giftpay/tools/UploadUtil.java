package org.jumutang.giftpay.tools;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import net.sf.json.JSONObject;

/**
 * 将图片上传到服务器或者保存到图片
 * 
 * 上传文件
 * 
 * @author oywj
 *
 * @Date 2017年5月12日 下午3:56:36
 */
public class UploadUtil {

    public static void uploadFile(HttpServletRequest request,
                                  HttpServletResponse response, String uploadPath) throws IOException {
        Map<String,Object> m = new HashMap<String,Object>();

        if(uploadPath == null){
            m.put("success", false);
            m.put("msg", "服务器繁忙,请稍后再试");
            response.getWriter().println(JSONObject.fromObject(m).toString());
            return;
        }

        String filePath = "";
        String fileName = "";

        try{
            MultipartHttpServletRequest mutilrequest = (MultipartHttpServletRequest) request;
            MultipartFile f = mutilrequest.getFile("file");// 获得上传的文件
            fileName = f.getOriginalFilename();
            String suffixName = "." + fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());
            String t =new SimpleDateFormat("yyyyMMdd_HHmmssSSS").format(new Date());
            String suffix=fileName.substring(fileName.lastIndexOf(".") + 1,
                    fileName.length());
            filePath = uploadPath + t + suffixName;

            File dir = new File(uploadPath);
            if (!dir.exists()){
                dir.mkdir();
            }
            File file = new File(filePath);
            f.transferTo(file);// 文件落地

            m.put("success", true);
            m.put("msg", "文件上传成功");
             //上传图片到服务器
            m.put("name","http://www.linkgift.cn/img/"+t + suffixName); //正式访问路径
            //m.put("name","http://tdev.juxinbox.com/img/"+t + suffixName);//测试访问路径
            //m.put("name","file：///C:/Users/Administrator/Desktop/proj1/"+t + suffixName); //本地访问路径
            
            response.getWriter().println(JSONObject.fromObject(m).toString());
        } catch (Exception e) {
            e.printStackTrace();
            m.put("success", false);
            m.put("msg", "文件上传错误");
            m.put("name", fileName);
            response.getWriter().println(JSONObject.fromObject(m).toString());
        }
    }


    /**
     * 获取文件md5
     * @param f
     */
    public String getFileMd5(File f) {
        String value = "";
        FileInputStream in = null;
        try {
            in = new FileInputStream(f);
            MappedByteBuffer byteBuffer = in.getChannel().map(
                    FileChannel.MapMode.READ_ONLY, 0, f.length());
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(byteBuffer);
            BigInteger bi = new BigInteger(1, md5.digest());
            value = bi.toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return value;
    }

    /**
     * 文件下载（通用）
     * @param path
     * @param response
     * @return
     */
    public static HttpServletResponse download(String path, HttpServletResponse response) {
        try {
            // path是指欲下载的文件的路径。
            File file = new File(path);
            // 取得文件名。
            String filename = file.getName();
            // 取得文件的后缀名。
            String ext = filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();

            // 以流的形式下载文件。
            InputStream fis = new BufferedInputStream(new FileInputStream(path));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
            response.addHeader("Content-Length", "" + file.length());
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("打印错误信息：" + ex.getMessage());
        }
        return response;
    }

    /**
     * 下载本地文件
     * @param response
     * @throws FileNotFoundException
     */
    public static void downloadLocal(HttpServletResponse response)  {
        // 下载本地文件
        String fileName = "Operator.doc".toString(); // 文件的默认保存名
        // 读到流中
        InputStream inStream;
		try {
			inStream = new FileInputStream("c:/Operator.doc");
			// 文件的存放路径
	        // 设置输出的格式
	        response.reset();
	        response.setContentType("bin");
	        response.addHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
	        // 循环取出流中的数据
	        byte[] b = new byte[100];
	        int len;
	        try {
	            while ((len = inStream.read(b)) > 0)
	                response.getOutputStream().write(b, 0, len);
	            inStream.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			 System.out.println("打印错误信息：" + e1.getMessage());
		}
    }

    /**
     * 下载网络文件
     * @param filePath  文件所在路径
     * @param response
     * @param savePath  保存路径
     */
    public  static void downloadNet(String filePath ,HttpServletResponse response,String savePath) {
        // 下载网络文件
        int bytesum = 0;
        int byteread = 0;

        URL url;
		try {
			url = new URL(filePath);
			  URLConnection conn = url.openConnection();
	            InputStream inStream = conn.getInputStream();
	            FileOutputStream fs = new FileOutputStream(savePath);

	            byte[] buffer = new byte[1204];
	            int length;
	            while ((byteread = inStream.read(buffer)) != -1) {
	                bytesum += byteread;
	                System.out.println(bytesum);
	                fs.write(buffer, 0, byteread);
	            }
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
			 System.out.println("打印错误信息：" + e1.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			 System.out.println("打印错误信息：" + e.getMessage());
		}

      
    }
    
    /**
     * 文件下载（支持文件在线预览）
     * @param filePath
     * @param response
     * @param isOnLine
     * @throws Exception
     */
    public static void downLoad(String filePath, HttpServletResponse response, boolean isOnLine)  {
        File f = new File(filePath);
        if (!f.exists()) {
            try {
				response.sendError(404, "File not found!");
				 BufferedInputStream br = new BufferedInputStream(new FileInputStream(f));
			        byte[] buf = new byte[1024];
			        int len = 0;

			        response.reset(); // 非常重要
			        if (isOnLine) { // 在线打开方式
			            URL u = new URL("file:///" + filePath);
			            response.setContentType(u.openConnection().getContentType());
			            response.setHeader("Content-Disposition", "inline; filename=" + f.getName());
			            // 文件名应该编码成UTF-8
			        } else { // 纯下载方式
			            response.setContentType("application/x-msdownload");
			            response.setHeader("Content-Disposition", "attachment; filename=" + f.getName());
			        }
			        OutputStream out = response.getOutputStream();
			        while ((len = br.read(buf)) > 0)
			            out.write(buf, 0, len);
			        br.close();
			        out.close();
			} catch (IOException e) {
				e.printStackTrace();
				 System.out.println("打印错误信息：" + e.getMessage());
			}
          
        }
       
    }
    
    public static void main(String[] args) {
   
    	  String str = "A08566"+"4c625b7861a92c7971cd2029c2fd3c4a"+"140101"+50+"test001234567"+"20160817140214"+6.0;
		    String md5_str= MD5X.getUpperCaseMD5For32(str);	
		    System.out.println(md5_str);
	}
    
}
