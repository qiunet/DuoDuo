package org.qiunet.utils.badword;

import java.util.Arrays;
import java.util.List;

/***
 *
 * @Author qiunet
 * @Date Create in 2018/6/27 19:24
 **/
public class DefaultBadWord implements IBadWord {

	private String pattern;

	public DefaultBadWord(List<String> badwords) {
		StringBuilder sb = new StringBuilder();
		for (String badword : badwords) {
			sb.append(badword).append('|');
		}
		if(sb.length() > 0) sb.deleteCharAt(sb.length() - 1);
		this.pattern = sb.toString();
	}

	public DefaultBadWord(String [] badwords) {
		this(Arrays.asList(badwords));
	}


	public DefaultBadWord(String pattern) {
		this.pattern = pattern;
	}


	@Override
	public String getPatternString() {
		return pattern;
	}
}
