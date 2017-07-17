package org.jumutang.giftpay.tools;


import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by mumu on 2017/6/4.
 */
public class GetRequestIpAddress {

    private Logger logger  = Logger.getLogger(GetRequestIpAddress.class);

    /**
     * 获取用户真实IP地址，不使用request.getRemoteAddr();的原因是有可能用户使用了代理软件方式避免真实IP地址,
     * 参考文章： http://developer.51cto.com/art/201111/305181.htm
     *
     * 可是，如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值，究竟哪个才是真正的用户端的真实IP呢？
     * 答案是取X-Forwarded-For中第一个非unknown的有效IP字符串。
     *
     * 如：X-Forwarded-For：192.168.1.110, 192.168.1.120, 192.168.1.130,
     * 192.168.1.100
     *
     * 用户真实IP为： 192.168.1.110
     *
     * @param request
     * @return
     */
    public  String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }


    //检验电话号码是否一致。[判断用户不能多次提交同一个手机号码]
    public String checkPhoneNumber(HttpSession session ,String InputNumber){

        //1.首先判断当前session是否存在电话号码
        String number =String.valueOf( session.getAttribute("phoneNumber"));

        //2.如果不存在当前电话号码，存入号码到session中
        if(number.equals("")){

            logger.info("当前号码，没有参与充值，准许进行充值操作！");
            session.setAttribute("phoneNumber",InputNumber);
            return "true";
        }else{

            //3.判断session中 第一次充值号码和当前充值号码是否相同
            if(InputNumber.equals(number)){
                logger.info("当前号码第二次充值，返回充值失败操作！");
                return "false";
            }else{
                return "true";
            }

        }

    }


}
