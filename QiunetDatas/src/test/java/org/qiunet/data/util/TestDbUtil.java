package org.qiunet.data.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
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
		Assertions.assertEquals("guild_member", DbUtil.getDefaultTableName("GuildMemberDo"));
		Assertions.assertEquals("item", DbUtil.getDefaultTableName("ItemDo"));
	}

	@Test
	public void testGroupIdLengthZero(){
		int serverGroupId = 0;
		Assertions.assertEquals(0, ServerType.getGroupIdLength(serverGroupId));

		long id = DbUtil.buildId(1, serverGroupId);
		Assertions.assertEquals(10, id);

		int serverGroupId1 = DbUtil.getServerGroupId(id);
		Assertions.assertEquals(serverGroupId1, serverGroupId);
	}

	@Test
	public void buildPlayerId(){
		int serverGroupId = 2;
		int incrId = 11;
		long id = DbUtil.buildId(incrId, serverGroupId);
		Assertions.assertEquals(id, 1121);

		Assertions.assertEquals(1, DbUtil.getTbIndex(id, serverGroupId));

		Assertions.assertEquals(serverGroupId, DbUtil.getServerGroupId(id));
	}

	@Test
	public void testTbIndex(){
		String openId = "1";
		int groupId = 1;

		Assertions.assertEquals(9, DbUtil.getTbIndex(openId, groupId));
	}

	@Test
	public void testGroupId1(){
		int groupId = 2;
		ServerType serverType = ServerType.LOGIC;

		int serverId = serverType.buildServerId(groupId, 4);

		Assertions.assertEquals(2041, serverId);
		Assertions.assertEquals(groupId, ServerType.getGroupId(serverId));

		Assertions.assertEquals(serverType, ServerType.getServerType(serverId));
	}

	@Test
	public void testGroupId2(){
		int groupId = 0;
		ServerType serverType = ServerType.CROSS;

		int serverId = serverType.buildServerId(groupId, 4);

		Assertions.assertEquals(42, serverId);
		Assertions.assertEquals(groupId, ServerType.getGroupId(serverId));

		Assertions.assertEquals(serverType, ServerType.getServerType(serverId));
	}
}
