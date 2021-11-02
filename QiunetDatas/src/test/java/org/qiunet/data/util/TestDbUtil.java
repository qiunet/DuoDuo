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

	@Test
	public void buildServerId(){
		int serverId = 100001;
		int incrId = 12;
		long id = DbUtil.buildId(incrId, serverId);
		Assert.assertEquals(id, 121000016);

		Assert.assertEquals(2, DbUtil.getTbIndex(id, serverId));

		Assert.assertEquals(serverId, DbUtil.getServerId(id));
	}

	@Test
	public void testTbIndex(){
		String openId = "1";
		int serverId = 100001;

		Assert.assertEquals(9, DbUtil.getTbIndex(openId, serverId));
	}

	@Test
	public void testGroupId(){
		int groupId = 11;
		int serverId = ServerType.LOGIC.buildServerId(groupId, 1);
		Assert.assertEquals(100011001, serverId);
		Assert.assertEquals(groupId, ServerType.getGroupId(serverId));

		Assert.assertEquals(ServerType.LOGIC, ServerType.getServerType(serverId));
	}
}
