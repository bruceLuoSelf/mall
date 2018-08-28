package com.wzitech.gamegold.facade.backend.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.wzitech.gamegold.common.expection.TypeMismatchAccessException;
/**
 * 
 * 时间格式转换工具类
 */
public final class DateTimeHelper {
    private DateTimeHelper(){
    }
    public static String formatDate(Date date, String formatPattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatPattern);
        return sdf.format(date);
    }
    
    public static Date getDateFromString(String value) {
    	return getDateFromString(value, Locale.getDefault());
    }
    
    public static Date getDateFromString(String value, Locale locale) {
		Date result = null;
		DateFormat[] dfs = getDateFormats(locale);

		DateFormat df = null;
		for (DateFormat df1 : dfs) {
			try {
				result = df1.parse(value);
				df = df1;
				if (result != null) {
					break;
				}
			} catch (ParseException ignore) {
				// 忽略转换异常，转入下一个format
			}
		}
		
		if (df == null) {
			df = DateFormat.getDateInstance(DateFormat.SHORT, locale);
		}
		try {
			df.setLenient(false);
			result = df.parse(value);
		} catch (ParseException e) {
			throw new TypeMismatchAccessException();
		}
		
		return result;
    }
    
    private static DateFormat[] getDateFormats(Locale locale) {
		DateFormat dt1 = 
				DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.LONG, locale);
		DateFormat dt2 = 
				DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM, locale);
		DateFormat dt3 = 
				DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, locale);

		DateFormat d1 = DateFormat.getDateInstance(DateFormat.SHORT, locale);
		DateFormat d2 = DateFormat.getDateInstance(DateFormat.MEDIUM, locale);
		DateFormat d3 = DateFormat.getDateInstance(DateFormat.LONG, locale);
		DateFormat d4 = new SimpleDateFormat("yyyy-MM-dd");

		DateFormat rfc3399 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

		DateFormat dt4 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		

		// 添加yyyy-MM-dd HH:mm:ss格式的日期转换
		DateFormat[] dfs = {dt1, dt2, dt3, rfc3399, dt4, d1, d2, d3, d4};
		return dfs;
	}
}
