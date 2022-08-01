package org.qiunet.data.util;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

/***
 *
 * @author qiunet
 * 2022/4/12 15:48
 */
public class TestRedisMapUtil {

	public static class User {
		public static final String CONSTANT = "111";
		private int id;

		private String name;

		private long score;

		private List<String> pic;

		public User() {}

		public User(int id, String name, long score) {
			this.id = id;
			this.name = name;
			this.score = score;
			this.pic = Lists.newArrayList("11111");
		}

		public List<String> getPic() {
			return pic;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public long getScore() {
			return score;
		}

		public void setScore(long score) {
			this.score = score;
		}
	}

	@Test
	public void test(){
		String name = "qiunet";
		long score = 1111;
		int id = 11;

		User user = new User(id, name,  score);
		Map<String, String> map = RedisMapUtil.toMap(user);
		Assertions.assertEquals(score+"", map.get("score"));
		Assertions.assertEquals(id+"", map.get("id"));
		Assertions.assertEquals(name, map.get("name"));
		Assertions.assertEquals(4, map.size());

		User user1 = RedisMapUtil.toObj(map, User.class);
		Assertions.assertEquals(score, user1.score);
		Assertions.assertEquals(name, user1.name);
		Assertions.assertEquals(id, user1.id);
	}

}
