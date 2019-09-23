package com.study.util.tool;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 *
 * @author nht
 *
 */
public class DateUtil {

	/**
	 * 获取过去第 past 天的日期
	 *
	 * @param past
	 * @return 返回年月日
	 */
	public static String getPastDate(int past) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
		Date today = calendar.getTime();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String result = format.format(today);
		return result;
	}

	/**
	 * 获取未来 第 past 天的日期
	 *
	 * @param past
	 * @return
	 */
	public static String getFetureDate(int past) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + past);
		Date today = calendar.getTime();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String result = format.format(today);
		return result;
	}

	/**
	 * 获取现在时间
	 *
	 * @return 返回时间类型 yyyy-MM-dd HH:mm:ss
	 */
	public static Date getNewDate() {
		Date currentTime_2 = null;
		try {
			Date currentTime = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String dateString = formatter.format(currentTime);
			currentTime_2 = formatter.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return currentTime_2;
	}

	/**
	 * 获取现在时间
	 *
	 * @return返回短时间格式 yyyy-MM-dd
	 */
	public static Date getNewDateShort() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(currentTime);
		ParsePosition pos = new ParsePosition(8);
		Date currentTime_2 = formatter.parse(dateString, pos);
		return currentTime_2;
	}

	/**
	 * 获取现在时间
	 *
	 * @return返回字符串格式 yyyy-MM-dd HH:mm:ss
	 */
	public static String getStringDate() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	/**
	 * 获取现在时间
	 *
	 * @return 返回短时间字符串格式 yyyy-MM-dd
	 */
	public static String getStringDateShort() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	/**
	 * 获取当前年份
	 *
	 * @return
	 */
	public static String getSysYear() {
		Calendar date = Calendar.getInstance();
		String year = String.valueOf(date.get(Calendar.YEAR));
		return year;
	}

	/**
	 * 判断两个日期大小(年月日)
	 *
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int compareDateShort(String date1, String date2) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date dt1 = df.parse(date1);
			Date dt2 = df.parse(date2);
			if (dt1.getTime() > dt2.getTime()) {
				return 1;
			} else if (dt1.getTime() < dt2.getTime()) {
				return -1;
			} else {
				return 0;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return -1;
	}

	/**
	 * 判断两个日期大小(年月日时分秒)
	 *
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int compareDate(String date1, String date2) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date dt1 = df.parse(date1);
			Date dt2 = df.parse(date2);
			if (dt1.getTime() > dt2.getTime()) {
				return 1;
			} else if (dt1.getTime() < dt2.getTime()) {
				return -1;
			} else {
				return 0;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return -1;
	}

	/**
	 * 将当前日期转为毫秒数
	 *
	 * @param date
	 * @return
	 */
	public static long getDateTime(String date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date dt = format.parse(date);
			return dt.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0l;

	}

	/**
	 * 讲年月日时分秒日期转换为年月日
	 *
	 * @param date
	 * @return
	 */
	public static String getDateShort(String date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			System.out.println(date);
			Date dt = format.parse(date);
			return sdf.format(dt);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将秒数转换为日时分秒，
	 *
	 * @param second
	 * @return
	 */
	public static String secondToTime(long second) {
		long days = second / 86400; // 转换天数
		second = second % 86400; // 剩余秒数
		long hours = second / 3600; // 转换小时
		second = second % 3600; // 剩余秒数
		long minutes = second / 60; // 转换分钟
		second = second % 60; // 剩余秒数
		if (days > 0) {
			return days + "天 " + hours + ":" + minutes + ":" + second;
		} else {
			return hours + ":" + minutes + ":" + second;
		}
	}

	/**
	 * 判断两个时间相隔多少秒
	 *
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int calLastedTime(String date1, String date2) {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			long date_time1 = formatter.parse(date1).getTime();
			long date_time2 = formatter.parse(date2).getTime();
			return (int) ((date_time2 - date_time1) / 1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

}
