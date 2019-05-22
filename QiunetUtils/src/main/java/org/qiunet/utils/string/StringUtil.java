package org.qiunet.utils.string;

import org.qiunet.utils.math.MathUtil;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * 字符处理相关的工具类
 *
 * @author qiunet
 *
 */
public class StringUtil {
	private StringUtil(){}
	/**汉字的正则表达式*/
	public static final Pattern CHINESE_REGEX = Pattern.compile("([\u4E00-\u9FA5]*)");

	/***
	 * 判断是否是空字符串
	 * @return
	 */
	public static boolean isEmpty(String str){
		return str == null || str.length() == 0;
	}
	/**
	 * 分割字符串
	 *
	 * 跟string.split() 略有不同. 不会按照正则表达式分割. 严格按照分隔符号分割字符串.
	 * 例:  ",".split(",").length = 0
	 *   	gameutil.split("," , ",").length = 2
	 *
	 * @param srcStr
	 * @param splitStr
	 * @return
	 */
	public static String [] split(String srcStr , String splitStr){
		if(srcStr == null || srcStr.length() == 0) return new String[0];

		List<String > retList = new ArrayList<String>();
		int before = 0 , cursor = 0 ,validCursor = srcStr.length() ;
		boolean splitValid = false;
		for(int i = 0 ; i < srcStr.length(); i ++){
			if(!splitValid && srcStr.charAt(i) == splitStr.charAt(0)) {
				splitValid = true;
				cursor = i;
			}
			if(splitValid){
				if(srcStr.charAt(i) != splitStr.charAt(i - cursor)){
					splitValid = false;
					continue;
				}
				if(i - cursor == splitStr.length()  -1){
					validCursor = cursor;
					retList.add(srcStr.substring(before, validCursor));
					before = i + 1;
					splitValid= false;
				}
			}
		}
		retList.add(srcStr.substring(before, srcStr.length()));
		return retList.toArray(new String[retList.size()]);
	}
	/***
	 * 字符串数组 转 基础数据类型数组
	 * @param <T>
	 * @param k
	 * @param t
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Number> T[] conversion(String [] k,Class<T> t)
	{
		T [] tt = (T[]) Array.newInstance(t, k.length);
		try {
		for(int i = 0 ; i < k.length; i ++){
				Method m = t.getMethod("valueOf", String.class);
				tt[i] = (T)m.invoke(t , k[i]);
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tt;
	}
	/**
	 * 分隔字符串并转类型
	 * @param src
	 * @param split
	 * @param t
	 * @return
	 */
	public static <T extends Number> T[] conversion(String src , String split, Class<T> t){
		return conversion(split(src, split), t);
	}
	/**
	 * 数组拼串
	 * @param arrays
	 * @param separator
	 * @return
	 */
	public static <T> String arraysToString(T [] arrays ,String separator){
		return arraysToString(arrays, "", "", separator);
	}
	/**
	 * 数组拼串
	 * @param arrays
	 * @param separator
	 * @return
	 */
	public static <T> String arraysToString(T [] arrays , String start ,String end ,String separator){
		return  arraysToString(arrays, start, end, 0, arrays.length - 1, separator);
	}
	/**
	 * 数组拼串
	 * @param arrays
	 * @param separator
	 * @return
	 */
	public static <T> String arraysToString(T [] arrays , String start ,String end , int startIndex, int endIndex, String separator){
		StringJoiner joiner = new StringJoiner(separator, start, end);

		for(int i = startIndex; i <= endIndex; i++){
			joiner.add(objectToString(arrays[i]));
		}
		return joiner.toString();
	}

	private static String objectToString(Object obj) {
		if (obj == null) return null;

		if (obj.getClass().isArray()) {
			return Arrays.toString((Object[]) obj);
		}
		return obj.toString();
	}
	/***
	 * 返回是否符合正则表达式
	 * @param matchStr
	 * @param regex
	 * @return
	 */
	public static boolean regex(String matchStr,String regex){
		if(StringUtil.isEmpty(matchStr) ){
			return false;
		}
		Pattern p = Pattern.compile(regex);
		return regex(matchStr, p);
	}

	/***
	 * 是不是字符串
	 * @param numStr
	 * @return
	 */
	public static boolean isNum(String numStr) {
		if (numStr == null) return false;
		return numStr.matches("-?[0-9]+");
	}
	/**
	 * 是否完美匹配
	 * @param matchStr
	 * @param p
	 * @return
	 */
	public static boolean regex(String matchStr,Pattern p){
		Matcher m = p.matcher(matchStr);
		return m.matches();
	}
	/***
	 * 返回符合正则表达式的个数
	 * @param matchStr
	 * @param regex
	 * @return
	 */
	public static int regexCount(String matchStr,String regex){
		if(StringUtil.isEmpty(matchStr) ){
			return 0;
		}
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(matchStr);
		if(m.find()){
			return m.groupCount();
		}
		return 0;
	}

	/**
	 * 是否是汉字
	 * @param matchStr
	 * @return
	 */
	public static boolean regexChinese(String matchStr){
		return regex(matchStr, CHINESE_REGEX);
	}
	 /**
	 * 格式化输出字符串
	 * @param s
	 * @param p
	 * @return
	 */
	public static String format(String s, Object... p) {
		return MessageFormat.format(s, p);
	}

	public static String getIntHexVal(int val){
		return String.format("%08x", val).toUpperCase();
	}

	public static String getByteHexVal(byte val){
		return String.format("%02x", val).toUpperCase();
	}

	public static String getShortHexVal(short val){
		return String.format("%04x", val).toUpperCase();
	}

	private static final String chars =  "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

	/**
	 * 生成一定长度的随机字符串
	 * @param count
	 * @return
	 */
	public static String randomString (int count) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0 ; i < count; i++) {
			sb.append(chars.charAt(MathUtil.random(chars.length())));
		}
		return sb.toString();
	}
	/**
	 * 屏蔽两端大部分空白字符
	 * @return
	 */
	private static final Character [] spaceChars = {'ㅤ', '　', '\u007F'};
	public static String powerfulTrim(String str){
		if (str == null || str.isEmpty()) return str;

		int start = 0;
		int length = str.length();

		while (start < length && ! isAllowChar(str.charAt(start))) {
			start ++;
		}

		while (length > start && ! isAllowChar(str.charAt(length - 1))) {
			length --;
		}

		if (start > 0 || length < str.length()) {
			return str.substring(start, length);
		}
		return str;
	}

	/***
	 * 判断字符是否是允许字符
	 * @param ch
	 * @return
	 */
	private static boolean isAllowChar(char ch) {
		if (ch > (char) 32 && ch < (char)128) {
			return true;
		}
		String chStr = String.valueOf(ch);
		if (regexChinese(chStr)) {
			return true;
		}
		return false;
	}

	private static int start = Integer.valueOf("4e00", 16);
	private static int end = Integer.valueOf("9fa5", 16);
	/***
	 * 计算中英文混合情况下的真实字数
	 * 汉字 占长度 2
	 * 字母 占长度 1
	 * @param input
	 * @return
	 */
	public static int getMixedStringLength(String input) {
		int length = 0;
		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			length += (c >= start && c <= end ? 2 : 1);
		}
		return length;
	}
}
