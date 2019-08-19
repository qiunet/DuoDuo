package org.qiunet.data.util;

import org.junit.Assert;
import org.junit.Test;
import org.qiunet.data.redis.util.DbUtil;

/***
 *
 *
 * qiunet
 * 2019-08-19 12:31
 ***/
public class TestDbUtil {
	@Test
	public void testTableName(){
		Assert.assertEquals("guild_member", DbUtil.getDefaultTableName("GuildMemberDo"));
		Assert.assertEquals("item", DbUtil.getDefaultTableName("ItemDo"));
	}
}
