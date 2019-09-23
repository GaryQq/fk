package com.study.util.tool;

/**
 * 字符串工具类
 * 
 * @author nht
 *
 */
public class StringUtil {

	/**
	 * 判断当前对象是否是空字符串
	 * 
	 * @param obj
	 * @return true || false
	 */
	public static boolean eqBlank(Object obj) {
		if ("".equals(StringUtil.nullBlank(obj))) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 对象转成String类型
	 * 
	 * @param str
	 * @return
	 */
	public static String nullBlank(Object str) {
		if (str == null) {
			return "";
		} else if (str != null && str.toString().trim().contains("null")) {
			return "";
		} else {
			return str.toString();
		}
	}

	/**
	 * 对象转成Integer类型
	 * 
	 * @param obj
	 * @return
	 */
	public static int nullZero(Object obj) {
		int result = 0;
		if (obj == null) {
			return result;
		}
		try {
			result = Integer.parseInt(obj.toString());
		} catch (Exception e) {
			result = 0;
		}
		return result;
	}

	/**
	 * 对象转long类型
	 * 
	 * @param str
	 * @return
	 */
	public static long nullZeroLong(Object str) {
		if (null == str || "".equals(str)) {
			return 0;
		} else {
			return Long.parseLong(str.toString());
		}
	}

	/**
	 * 对象转float类型
	 * 
	 * @param str
	 * @return
	 */
	public static float nullZeroFloat(Object str) {
		if (null == str || "".equals(str)) {
			return 0;
		} else {
			return Float.parseFloat(str.toString());
		}
	}

	/**
	 * 对象转double类型
	 * 
	 * @param str
	 * @return
	 */
	public static double nullDouble(Object str) {
		if (null == str || "".equals(str)) {
			return 0;
		} else {
			return Double.parseDouble(str.toString());
		}
	}

	/**
	 * 处理字符串返回特定条件下的默认值
	 * 
	 * @param originStr
	 * @param res
	 * @return
	 */
	public static String getResString(String originStr, String res) {

		if (originStr == null || "null".equals(originStr) || "".equals(originStr.trim())) {
			return res;
		} else {
			return originStr.trim();
		}
	}

}
