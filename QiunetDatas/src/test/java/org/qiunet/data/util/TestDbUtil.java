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
	public void testGroupIdLengthZero(){
		int serverGroupId = 0;
		Assert.assertEquals(0, ServerType.getGroupIdLength(serverGroupId));

		long id = DbUtil.buildId(1, serverGroupId);
		Assert.assertEquals(10, id);

		int serverGroupId1 = DbUtil.getServerGroupId(id);
		Assert.assertEquals(serverGroupId1, serverGroupId);
	}

	@Test
	public void buildPlayerId(){
		int serverGroupId = 2;
		int incrId = 11;
		long id = DbUtil.buildId(incrId, serverGroupId);
		Assert.assertEquals(id, 1121);

		Assert.assertEquals(1, DbUtil.getTbIndex(id, serverGroupId));

		Assert.assertEquals(serverGroupId, DbUtil.getServerGroupId(id));
	}

	@Test
	public void testTbIndex(){
		String openId = "1";
		int groupId = 1;

		Assert.assertEquals(9, DbUtil.getTbIndex(openId, groupId));
	}

	@Test
	public void testGroupId1(){
		int groupId = 2;
		ServerType serverType = ServerType.LOGIC;

		int serverId = serverType.buildServerId(groupId, 4);

		Assert.assertEquals(2104, serverId);
		Assert.assertEquals(groupId, ServerType.getGroupId(serverId));

		Assert.assertEquals(serverType, ServerType.getServerType(serverId));
	}

	@Test
	public void testGroupId2(){
		int groupId = 0;
		ServerType serverType = ServerType.CROSS;

		int serverId = serverType.buildServerId(groupId, 4);

		Assert.assertEquals(204, serverId);
		Assert.assertEquals(groupId, ServerType.getGroupId(serverId));

		Assert.assertEquals(serverType, ServerType.getServerType(serverId));
	}
}
