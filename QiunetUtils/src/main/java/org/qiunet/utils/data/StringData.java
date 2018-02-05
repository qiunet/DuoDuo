package org.qiunet.utils.data;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import org.qiunet.utils.date.DateUtil;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 把一个类的所有字段拼串. {} 为对象.  [] 数组
 * 符合json规范
 * @author qiunet
 *
 */
public class StringData {
	private static final Logger logger = LoggerFactory.getLogger(LoggerType.QIUNET_UTILS);
	private StringBuilder sb;

	private StringData(Object obj){
		if(obj == null) throw new NullPointerException("can not be null!");

		try {
			sb = new StringBuilder(getObjectVal(obj).toString());
		} catch (Exception e) {
			logger.error("Class: "+obj.getClass().getName(), e);
		}
	}

	private StringBuilder loadObject(Object obj){
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		Field [] fields = obj.getClass().getDeclaredFields();
		for( Field f : fields){
				if(Modifier.isStatic(f.getModifiers()) || Modifier.isFinal(f.getModifiers())  || Modifier.isTransient(f.getModifiers())) continue;

				if("logger".equals(f.getName())) continue;
				/*包含switch列. 这个也可以去掉.*/
				if(f.getName().contains("SWITCH_TABLE")) continue;

				f.setAccessible(true);
				Object val = null;
				try {
					val = getObjectVal(f.get(obj));
				} catch (Exception e) {
					logger.error("[StringData] Exception: ", e);
				}finally{
					f.setAccessible(false);
				}
				this.append(sb , f.getName() , (val == null ? "null" : val));
		}
		if(sb.length() > 1) sb.deleteCharAt(sb.length() - 1);
		sb.append("}");
		return sb;
	}

	@SuppressWarnings("rawtypes")
	private Object getObjectVal(Object obj){
		if(obj == null) return null;
		try {
			Class<?> c = obj.getClass();
 			if( c.isArray()){
 				return getArrayString(obj);
 			}
 			if(c.isPrimitive() || Number.class.isAssignableFrom(c)){
 				// 基本类型.
 				return obj;
 			}else{
 				// 处理特殊类.
 				if(c == String.class){
 					return obj;
 				}else if(c == Boolean.class || c == boolean.class){
 					return obj.toString();
 				}else if(Enum.class.isAssignableFrom(c)){
 					return obj.toString();
 				}else if(c == Date.class){
 					return getDate((Date)obj);
 				}else if(Map.class.isAssignableFrom(c)){
 					return getMapVal((Map)obj);
 				}else if(Entry.class.isAssignableFrom(c)){
 					return getEntry((Entry) obj);
 				}else if(Collection.class.isAssignableFrom(c)){
 					return getArrayString(((Collection)obj).toArray());
 				}
 				return loadObject(obj);
 			}
		} catch (Exception e) {
			logger.error("["+getClass().getSimpleName()+"] Exception: ", e);
		}
		return "ERR";
	}

	@SuppressWarnings("rawtypes")
	private StringBuilder getEntry(Entry en){
		StringBuilder sb = new StringBuilder();
		sb.append("\"").append(getObjectVal(en.getKey()).toString()).append('-').append(getObjectVal(en.getValue())).append("\"");
		return sb;
	}
	@SuppressWarnings("rawtypes")
	private StringBuilder getMapVal(Map map){
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		for(Object key : map.keySet()){
			this.append(sb, getObjectVal(key), getObjectVal(map.get(key)));
		}
		if(sb.length() > 1) sb.deleteCharAt(sb.length() - 1);
		sb.append("}");
		return sb;
	}

	private String getDate(Date date){
		return DateUtil.dateToString(date);
	}

	private StringBuilder getArrayString(Object obj){
		int length = Array.getLength(obj);
		StringBuilder sb = new StringBuilder();
		sb.append('[');
		for(int i = 0 ; i < length; i++){
			Object val = Array.get(obj, i);
			sb.append(getObjectVal(val)).append(',');
		}
		if(sb.length() > 1) sb.deleteCharAt(sb.length() - 1);
		sb.append(']');
		return sb;
	}


	private void append(StringBuilder sb , Object name , Object val){
		boolean needQuot = !(val instanceof StringBuilder);
		boolean isNum = Number.class.isAssignableFrom(val.getClass());
		sb.append("\"").append(name).append("\":");
		if(needQuot && !isNum) sb.append("\"");
		sb.append(val);
		if(needQuot && !isNum) sb.append("\"");
		sb.append(',');
	}

	public static String parseString(Object obj){
		StringData sd = new StringData(obj);
		return sd.sb.toString();
	}
}
