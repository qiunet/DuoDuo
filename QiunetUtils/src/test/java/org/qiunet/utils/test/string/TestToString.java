package org.qiunet.utils.test.string;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.string.IgnoreToString;
import org.qiunet.utils.string.ToString;

import java.util.List;

/***
 *
 * @author qiunet
 * 2021/11/3 07:39
 */
public class TestToString {
	@Test
	public void test(){
		String s = ToString.toString(ImmutableMap.of("qiunet", "qiuyang"));
		Assertions.assertEquals(s, "{qiunet = qiuyang}");

		s = ToString.toString(LoggerType.DUODUO);
		Assertions.assertEquals(s, "DUODUO");

		s = ToString.toString(new User("qiunet", ImmutableList.of(1, 2, 3)));
		Assertions.assertEquals("User[account = qiunet, scores = {1, 2, 3}]", s);

		s = ToString.toString(new int[]{1, 2, 3});
		Assertions.assertEquals(s, "int[]{1, 2, 3}");
	}

	public static class User {
		private final String account;
		private final List<Integer> scores;
		@IgnoreToString
		private final String level;

		private final ITest test;
		public User(String account, List<Integer> scores) {
			this.test = new ITest(2);
			this.account = account;
			this.level = "3423424";
			this.scores = scores;
		}
	}

	@IgnoreToString
	public static class ITest {
		private final int val;

		public ITest(int val) {
			this.val = val;
		}

		public int getVal() {
			return val;
		}
	}
}
