package com.wzitech.gamegold.common.utils;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * WEB服务工具类
 * 
 * @author ztjie
 * 
 */
public class DateUtil {
	
	/**
	 * 
	 * <p>得到传入时间所在的月的最后一天</p> 
	 * @author ztjie
	 * @date 2013-12-5 下午10:41:16
	 * @param date
	 * @return
	 * @see
	 */
	public static Date monthLastTime(Date date, int beforeMonth){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, cal.get(Calendar.MONTH)+beforeMonth);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
		cal.set(Calendar.MILLISECOND, 999);
		return cal.getTime();
	}
	
	/**
	 * 
	 * <p>得到传入时间所在月的第一天</p> 
	 * @author ztjie
	 * @date 2013-12-5 下午9:40:15
	 * @param date
	 * @return
	 * @see
	 */
	public static Date monthFirstTime(Date date, int beforeMonth){
		Calendar cal=Calendar.getInstance();//获取当前日期 
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天 
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+beforeMonth, cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
	
	/**
	 * 
	 * <p>得到传入时间后几天的开始时间</p> 
	 * @author ztjie
	 * @date 2013-12-5 下午9:03:54
	 * @return
	 * @see
	 */
	public static Date beforeDateStartTime(Date date, int before){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)+before, 0, 0, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
	
	/**
	 * 
	 * <p>得到传入时间后几天的结束时间</p> 
	 * @author ztjie
	 * @date 2013-12-5 下午9:04:18
	 * @return
	 * @see
	 */
	public static Date beforeDateLastTime(Date date, int before){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)+before, 23, 59, 59);
		cal.set(Calendar.MILLISECOND, 999);
		return cal.getTime();
	}

	/**
	 * 调整时间为得到传入时间所在的天最后一秒
	 * @param date
	 * @return
	 */
	public static Date oneDateLastTime(Date date){
		if(date==null){
			return date;
		}
		return DateUtil.beforeDateLastTime(date, 0);
	}
	
	/**
	 * 
	 * <p>得到几年前的当前时间</p> 
	 * @author Think
	 * @date 2013-12-7 上午10:16:13
	 * @param date
	 * @param before
	 * @return
	 * @see
	 */
	public static Date beforeYearTime(Date date, int before){
		Calendar cal = Calendar.getInstance();//得到一个Calendar的实例  
		cal.setTime(date);   //设置时间为当前时间  
		cal.add(Calendar.YEAR, before); //年份减1  
		return cal.getTime();
	}

    public static XMLGregorianCalendar convertToXMLGregorianCalendar(Date date) {

        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        XMLGregorianCalendar gc = null;
        try {
            gc = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return gc;
    }
}
