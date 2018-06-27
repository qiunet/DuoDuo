package org.qiunet.utils.badword;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 * 涉及玩家输入的敏感词屏蔽
 * @Author qiunet
 * @Date Create in 2018/6/27 16:04
 **/
public class BadWordFilter {

	private Pattern pattern;

	private volatile static BadWordFilter instance;

	private BadWordFilter() {
		if (instance != null) throw new RuntimeException("Instance Duplication!");
		instance = this;
	}

	public static BadWordFilter getInstance() {
		if (instance == null) {
			synchronized (BadWordFilter.class) {
				if (instance == null)
				{
					new BadWordFilter();
				}
			}
		}
		return instance;
	}

	/***
	 * 加载
	 * @param badWords
	 */
	public void loadBadWord(IBadWord badWords) {
		this.pattern = Pattern.compile(badWords.getPatternString());
	}
	/**
	 * 如有敏感词返回相关的词，否则返回null
	 * @param str
	 * @return
	 */
	public String find(String str) {
		if (pattern == null) {
			throw new NullPointerException("need loadBadWord first!");
		}


		if (str == null || str.trim().length() == 0) {
			return null;
		}
		Matcher m = pattern.matcher(str);
		if (m.find()){
			return m.group();
		}
		return null;
	}

	/**
	 * 把敏感词替换成 相同数量的 *
	 * @param str
	 * @return
	 */
	public String doFilter(String str) {
		if (pattern == null) {
			throw new NullPointerException("need loadBadWord first!");
		}

		if (str == null || str.trim().length() == 0) {
			return "";
		}
		Matcher matcher = pattern.matcher(str);
		boolean result = matcher.find();
		if (result) {
			StringBuffer sb = new StringBuffer();
			do {
				String matched = matcher.group();
				matcher.appendReplacement(sb, createStarCharByCount(matched.length()));
				result = matcher.find();
			} while (result);
			matcher.appendTail(sb);
			return sb.toString();
		}
		return str;
	}
	private static String [] arrays = {"", "*", "**", "***", "****", "*****"};
	private String createStarCharByCount(int count) {
		if (count <= arrays.length) return arrays[count];

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < count; i++) {
			sb.append("*");
		}
		return sb.toString();
	}
}
