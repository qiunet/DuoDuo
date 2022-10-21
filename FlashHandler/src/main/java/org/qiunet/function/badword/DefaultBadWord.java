package org.qiunet.function.badword;

import java.util.Arrays;
import java.util.List;

/***
 *
 * @Author qiunet
 * @Date Create in 2018/6/27 19:24
 **/
public class DefaultBadWord implements IBadWord {
	private final List<String> badWords;

	public DefaultBadWord(List<String> badwords) {
		this.badWords = badwords;
	}

	public DefaultBadWord(String [] badwords) {
		this(Arrays.asList(badwords));
	}

	@Override
	public List<String> getBadWordList() {
		return badWords;
	}
}
