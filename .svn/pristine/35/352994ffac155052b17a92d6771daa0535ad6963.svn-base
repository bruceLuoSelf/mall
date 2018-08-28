package com.wzitech.gamegold.facade.backend.conversion;

import java.util.Date;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.util.StrutsTypeConverter;

import com.wzitech.gamegold.common.expection.TypeMismatchAccessException;
import com.wzitech.gamegold.facade.backend.util.DateTimeHelper;

/**
 * struts2日期转换器，扩展struts2默认的转换器
 * 
 * <p/>支持locale所有的日期格式和一些非标准特殊的格式，
 * 例如：yyyy-MM-dd, yyyy-MM-dd'T'HH:mm:ss, yyyy-MM-dd HH:mm:ss
 * @author ztjie
 * @since
 * @version
 */
public class DateConverter extends StrutsTypeConverter {
	
	protected Log logger = LogFactory.getLog(getClass());
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Object convertFromString(Map context, String[] values, Class toClass) {
		if (Date.class.isAssignableFrom(toClass)) {
			// 空的值，直接返回null
			if (values == null || values.length <= 0) {
				return null;
			}

			if (values.length == 1) {
				return this.doConvertToDate(context, values[0], toClass);
			}
		}
		
		return super.performFallbackConversion(context, values, toClass);
	}

	/**
	 * Date类型转换成string，调用struts2默认的转换器
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public String convertToString(Map context, Object o) {
		return (String) super.convertValue(o, String.class);
	}

	@SuppressWarnings({ "rawtypes" })
	private Object doConvertToDate(Map<String, Object> context, String value, Class toType) {
		// NULL或者空字符串直接返回null
		if (StringUtils.isBlank(value)) {
			return null;
		}
		
		Locale locale = getLocale(context);
		
		Date result = null;
		try {
			result = DateTimeHelper.getDateFromString(value, locale);
		} catch (TypeMismatchAccessException e) {
			if (this.logger.isWarnEnabled()) {
				this.logger.warn("Could not parse date", e);
			}
			return null;
		}
		
		return result;
	}

}
