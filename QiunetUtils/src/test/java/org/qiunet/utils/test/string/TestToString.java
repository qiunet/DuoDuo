package org.qiunet.utils.test.string;

import org.junit.Assert;
import org.junit.Test;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.string.ToString;

import java.util.List;
import java.util.Map;

/***
 *
 * @author qiunet
 * 2021/11/3 07:39
 */
public class TestToString {
	@Test
	public void test(){
		String s = ToString.toString(Map.of("qiunet", "qiuyang"));
		Assert.assertEquals(s, "{qiunet = qiuyang}");

		s = ToString.toString(LoggerType.DUODUO);
		Assert.assertEquals(s, "DUODUO");

		s = ToString.toString(new User("qiunet", List.of(1, 2, 3)));
		Assert.assertEquals(s, "User[account = qiunet, scores = {1, 2, 3}]");

		s = ToString.toString(new int[]{1, 2, 3});
		Assert.assertEquals(s, "int[]{1, 2, 3}");
	}

	public static class User {
		private final String account;
		private final List<Integer> scores;

		public User(String account, List<Integer> scores) {
			this.account = account;
			this.scores = scores;
		}
	}
}
