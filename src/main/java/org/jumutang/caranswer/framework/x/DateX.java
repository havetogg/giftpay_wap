package org.jumutang.caranswer.framework.x;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 日期增强类
 * @author YuanYu
 *
 */
public final class DateX {


	public static final String _DATE = "yyyy-MM-dd";

	public static final String _DATE_TIME_24 = "yyyy-MM-dd HH:mm:ss";

	public static final String _DATE_TIME_12 = "yyyy-MM-dd hh:mm:ss";
	
	private static final Map<Integer, Integer> $weekPools = new HashMap<Integer, Integer>();
	
	private static final Map<Integer, Integer> $monthPools = new HashMap<Integer, Integer>();

	private DateX() {
	}
	
	static{
		$weekPools.put(1, 7);
		$weekPools.put(2, 1);
		$weekPools.put(3, 2);
		$weekPools.put(4, 3);
		$weekPools.put(5, 4);
		$weekPools.put(6, 5);
		$weekPools.put(7, 6);
		$monthPools.put(0, 1);
		$monthPools.put(1, 2);
		$monthPools.put(2, 3);
		$monthPools.put(3, 4);
		$monthPools.put(4, 5);
		$monthPools.put(5, 6);
		$monthPools.put(6, 7);
		$monthPools.put(7, 8);
		$monthPools.put(8, 9);
		$monthPools.put(9, 10);
		$monthPools.put(10, 11);
		$monthPools.put(11, 12);
	}

	/**
	 * 格式化日期
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static final String formatToString(Date date, String pattern) {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(date);
	}

	/**
	 * 格式化日期
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static final Date formatToDate(String date, String pattern) {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		try {
			return format.parse(date);
		} catch (ParseException e) {
		}
		return null;
	}

	/**
	 * 改变天数
	 * 		传入正数增加
	 *		传入负数减少 
	 * @param date
	 * @param day
	 * @return
	 */
	public static final Date changeDay(Date date, int day) { 
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, day);
		return calendar.getTime();
	}
	
	/**
	 * 改变月份
	 * 		传入正数增加
	 * 		传入负数减少
	 * @param date
	 * @param month
	 * @return
	 */
	public static final Date changeMonth(Date date , int month){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, month);
		return calendar.getTime();
	}
	
	/**
	 * 改变年份
	 * 		传入正数增加
	 * 		传入负数减少
	 * @param date
	 * @param year
	 * @return
	 */
	public static final Date changeYear(Date date,int year){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.YEAR, year);
		return calendar.getTime();
	}
	/**
	 * 获取年份
	 * @param date
	 * @return
	 */
	public static final int getYear(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.YEAR);
	}
	/**
	 * 获取月份
	 * @param date
	 * @return
	 */
	public static final int getMonth(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return $monthPools.get(calendar.get(Calendar.MONTH));
	}
	/**
	 * 获取具体几号
	 * @param date
	 * @return
	 */
	public static final int getDayForMonth(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_MONTH);
	}
	/**
	 * 设置为具体几号
	 * @param date
	 * @param day
	 * @return
	 */
	public static final Date setDayForMonth(Date date,int day){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, day);
		return calendar.getTime();
	}
	/**
	 * 获取今天是周几
	 * @param date
	 * @return
	 */
	public static final int getDayForWeek(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return $weekPools.get(calendar.get(Calendar.DAY_OF_WEEK));
	}
	
}
