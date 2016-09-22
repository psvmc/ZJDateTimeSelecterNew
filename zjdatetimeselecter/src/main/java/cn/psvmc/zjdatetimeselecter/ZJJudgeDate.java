package cn.psvmc.zjdatetimeselecter;

import java.text.SimpleDateFormat;

public class ZJJudgeDate {


	/**
	 * 判断是否为合法的日期时间字符串
	 * @param str_input 输入字符串
	 * @param rDateFormat 格式化方式
     * @return boolean
     */
	public static  boolean isDate(String str_input,String rDateFormat){
		if (!isNull(str_input)) {
	         SimpleDateFormat formatter = new SimpleDateFormat(rDateFormat);
	         formatter.setLenient(false);
	         try {
	             formatter.format(formatter.parse(str_input));
	         } catch (Exception e) {
	             return false;
	         }
	         return true;
	     }
		return false;
	}
	public static boolean isNull(String str){
		return str==null;
	}
}