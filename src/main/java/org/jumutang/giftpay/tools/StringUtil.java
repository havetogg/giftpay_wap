package org.jumutang.giftpay.tools;

import java.util.Date;
import java.util.Random;

public class StringUtil {
	private  static int number = 0;
	/**
	 * 生成订单号
	 * @return
	 */
	public static String getOrderId(){
		Date date = new Date();
		String time = date.getTime()+"";
		String[]  chars = new String[]{"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
		StringBuffer sb = new StringBuffer();
		String str = "";
		number +=1;
		 str = number+"";
			if(str.length()==1){
				str ="00"+str;
			}
			if(str.length()==2){
				str = "0"+str;
			}
			if(number==999){
				number = 0;
			}
		for(int i= 0;i<3;i++){
			int index = new Random().nextInt(25);
			sb.append(chars[index]);
		}
		return sb.toString()+time+str;
	}
	
	public static void main(String[] args) {
		System.out.println(StringUtil.getOrderId());
	}
}
